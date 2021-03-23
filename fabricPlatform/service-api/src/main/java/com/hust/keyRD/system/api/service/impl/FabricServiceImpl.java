package com.hust.keyRD.system.api.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hust.keyRD.commons.entities.Record;
import com.hust.keyRD.commons.exception.fabric.FabricException;
import com.hust.keyRD.commons.utils.HashUtil;
import com.hust.keyRD.system.api.Constants.FabricConstant;
import com.hust.keyRD.system.api.feign.FabricFeignService;
import com.hust.keyRD.system.api.service.FabricService;
import feign.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class FabricServiceImpl implements FabricService {

    @Autowired
    private FabricFeignService fabricFeignService;


    @Override
    public String invokeChaincode(String peers, String channelName, String ccName,
                                  String fcn, List<String> args) {
        Response response = fabricFeignService.invokeChaincodeTest(peers, channelName, ccName, fcn, args);
        return response.body().toString();
    }

    @Override
    public Boolean addPolicy(String obj, String permission, String role, List<String> users) {
        String peers = "peer0.org1.example.com";
        String channelName = "centre";
        String ccName = "audit";
        String fcn = "AddPolicy";
        List<String> args = new ArrayList<String>() {{
            add(obj);
            add(permission);
            add(role);
            addAll(users);
        }};
        String response = invokeChaincode(peers, channelName, ccName, fcn, args);
        if (response.contains("Success")) {
            log.info("AddPolicy {} {}成功", obj, permission);
            log.info("AddPolicy {}在{}上{}文件的权限成功,info:{}", users, obj, permission, response);
            return true;
        } else if (response.contains("exists")) {
            log.warn("AddPolicy {} {}失败，权限已存在", obj, permission);
            return false;
        } else {
            log.error("AddPolicy失败，info: {}", response);
            throw new FabricException("AddPolicy失败,info: " + response);
        }
    }

    @Override
    public Boolean updatePolicy(String obj, String permission, String func, String role, List<String> users) {
        String peers = "peer0.org1.example.com";
        String channelName = "centre";
        String ccName = "audit";
        String fcn = "UpdatePolicy";
        List<String> args = new ArrayList<String>() {{
            add(obj);
            add(permission);
            add(func);
            add(role);
            addAll(users);
        }};
        String response = invokeChaincode(peers, channelName, ccName, fcn, args);
        if (response.contains("Success")) {
            return true;
        } else {
            throw new FabricException("updatePolicy 失败，info: " + response);
        }

    }


    @Override
    public Boolean grantUserPermission2Add(String dstChannelName, String role, String username) {
        String response = getPolicy(dstChannelName, "add");
        try {
            if (response.contains("user_history")) {
                // 正常获得policy，即policy已存在
                return updatePolicy(dstChannelName, "add", "adduser", role, Collections.singletonList(username));
            } else {
                return addPolicy(dstChannelName, "add", role, Collections.singletonList(username));
            }
        } catch (FabricException e) {
            log.error("授权用户{}在{}上{}文件的权限失败,info:{}", username, dstChannelName, "add", response);
            throw e;
        }


    }

    @Override
    public Boolean grantUserPermissionOnFile(String fileId, String fileChannelName, String permission, String role, List<String> users) {
        String obj = fileId + FabricConstant.Separator + fileChannelName;
        String response = getPolicy(obj, permission);
        try {
            if (response.contains("user_history")) {
                // 正常获得policy，即policy已存在
                return updatePolicy(obj, permission, "adduser", role, users);
            } else {
                // policy不存在 增加policy
                return addPolicy(obj, permission, role, users);

            }
        } catch (FabricException e) {
            log.error("授权用户{}对文件{}的{}权限失败,info:{}", users, fileId, permission, response);
            throw e;
        }
    }


    /**
     * 撤销用户权限
     *
     * @param obj   channelName或fileId
     * @param opt   权限
     * @param role  角色
     * @param users users
     * @return
     */
    private Boolean revokeUserPermission(String obj, String opt, String role, List<String> users) {
        // 由于该例子中授权都由中心链上的org1完成 暂时写死
        String peers = "peer0.org1.example.com";
        String channelName = "centre";
        String ccName = "audit";
        String fcn = "UpdatePolicy";
        List<String> args = new ArrayList<String>() {{
            add(obj);
            add(opt);
            add("deleteuser");
            add(role);
            addAll(users);
        }};
        String response = invokeChaincode(peers, channelName, ccName, fcn, args);
        if (response.contains("Success")) {
            log.info("撤销用户{}在{}上的{}权限成功,info:{}", users, obj, opt, response);
            return true;
        } else {
            log.warn("撤销用户{}在{}上的{}权限失败,info:{}", users, obj, opt, response);
            throw new FabricException("撤销权限失败,info: " + response);
        }
    }

    @Override
    public Boolean revokeUserPermissionOnFile(String fileId, String fileChannelName, String permission, String role, List<String> users) {
        String obj = fileId + FabricConstant.Separator + fileChannelName;
        return revokeUserPermission(obj, permission, role, users);
    }

    @Override
    public Boolean revokeUserPermission2Add(String dstChannelName, String role, String username) {
        return revokeUserPermission(dstChannelName, "add", role, Collections.singletonList(username));
    }

    @Override
    public String crossAccess(String peers, String channelName, String ccName, String fcn, List<String> args) {
        Response response = fabricFeignService.crossAccess(peers, channelName, ccName, fcn, args);
        return response.body().toString();
    }

    private String applyForOptFile(String fileHash, String srcChannelName, String username, String dstChannelName, String fileId, String opt) throws IOException {
        // 由于该例子中一次上链都由org5发起 暂时写死
        String peers;
        if (srcChannelName.equals("channel1")) {
            peers = "peer0.org3.example.com";
        } else {
            peers = "peer0.org5.example.com";
        }
        String channelName = srcChannelName; // 第一次上链由源链节点调用链码 更新源链记录并通过代理更新目标链记录
        String ccName = "record";
        String fcn = "srcAuditRecord";
        //args:  hashdata, srcchain, user, dstchain, dataid, typetx
        final String fileIdFinal = fileId + FabricConstant.Separator + dstChannelName;
        List<String> args = new ArrayList<String>() {{
            add(fileHash);
            add(srcChannelName);
            add(username);
            add(dstChannelName);
            add(fileIdFinal);
            add(opt);
        }};
        String rawRes = crossAccess(peers, channelName, ccName, fcn, args);
        // 获取上链事务id
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(rawRes);
        return root.path("dst_chain_record").path("this_tx_id").asText();
    }

    @Override
    public String applyForCreateFile(String fileHash, String srcChannelName, String username, String dstChannelName, String fileId) {
        try {
            String txId = applyForOptFile(fileHash, srcChannelName, username, dstChannelName, fileId, "add");
            if (txId == null || txId.isEmpty()) {
                throw new FabricException("获取创建文件权限失败");
            } else {
                return txId;
            }
        } catch (IOException e) {
            log.error("用户{}获取创建{}文件权限失败", username, dstChannelName);
            throw new FabricException("获取创建文件权限失败");
        }

    }

    @Override
    public String applyForReadFile(String fileHash, String srcChannelName, String username, String dstChannelName, String fileId) {
        try {
            String txId = applyForOptFile(fileHash, srcChannelName, username, dstChannelName, fileId, "read");
            if (txId == null || txId.isEmpty()) {
                throw new FabricException("获取创建文件权限失败");
            } else {
                return txId;
            }
        } catch (IOException e) {
            log.error("用户{}获取查看{}文件{}权限失败", username, dstChannelName, fileId);
            throw new FabricException("获取查看文件权限失败");
        }
    }

    @Override
    public String applyForModifyFile(String fileHash, String srcChannelName, String username, String dstChannelName, String fileId) {
        try {
            return applyForOptFile(fileHash, srcChannelName, username, dstChannelName, fileId, "modify");
        } catch (IOException e) {
            log.error("用户{}获取修改{}上的文件{}权限失败", username, dstChannelName, fileId);
            throw new FabricException("获取修改文件权限失败");
        }
    }

    // 二次上链
    private Record updateForOptFile(String fileString, String srcChannelName, String username, String dstChannelName, String fileId, String txId, String opt) {
        String hashSHA1 = HashUtil.hash(fileString, "SHA1");
        String peers;
        if (dstChannelName.equals("channel1")) {
            peers = "peer0.org3.example.com";
        } else {
            peers = "peer0.org5.example.com";
        }
        String channelName = dstChannelName; // 第二次上链由目标链节点调用链码 更新目标链记录 
        String ccName = "record";
        String fcn = "dataSyncRecord";
        final String fileIdFinal = fileId + FabricConstant.Separator + dstChannelName;
        List<String> args = new ArrayList<String>() {{
            add(hashSHA1);
            add(srcChannelName);
            add(username);
            add(dstChannelName);
            add(fileIdFinal);
            add(opt);
        }};
        // 成功返回Record对象json  失败就无法正常进行json解析
        String response = dataSyncRecord(peers, channelName, ccName, fcn, args, txId);
        ObjectMapper mapper = new ObjectMapper();
        try {
            String recordJson = response.replaceAll("\\\\", "");
            recordJson = recordJson.substring(1, recordJson.length() - 1);
            return mapper.readValue(recordJson, Record.class);
        } catch (IOException e) {
            log.error("二次上链更新hash失败,username:{},dstChannelName:{},fileId:{},txId:{},opt:{}, response:{}", username, dstChannelName, fileId, txId, opt, response);
            throw new FabricException("二次上链更新hash失败");
        }

    }

    @Override
    public Record updateForCreateFile(String fileString, String srcChannelName, String username, String dstChannelName, String fileId, String txId) {
        return updateForOptFile(fileString, srcChannelName, username, dstChannelName, fileId, txId, "add");
    }

    @Override
    public Record updateForReadFile(String fileString, String srcChannelName, String username, String dstChannelName, String fileId, String txId) {
        return updateForOptFile(fileString, srcChannelName, username, dstChannelName, fileId, txId, "read");
    }

    @Override
    public Record updateForModifyFile(String fileString, String srcChannelName, String username, String dstChannelName, String fileId, String txId) {
        return updateForOptFile(fileString, srcChannelName, username, dstChannelName, fileId, txId, "modify");
    }


    @Override
    public String dataSyncRecord(String peers, String channelName, String ccName, String fcn, List<String> args, String txId) {
        Response response = fabricFeignService.dataSyncRecord(peers, channelName, ccName, fcn, args, txId);
        return response.body().toString();
    }

    private Record parseRecordFromResponse(String response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response);
        JsonNode txInfo = root.path("tx_info");
        String recordJson = txInfo.toString().replaceAll("\\\\", "");
        recordJson = recordJson.substring(1, recordJson.length() - 1);
        return mapper.readValue(recordJson, Record.class);
    }

    @Override
    public Record traceBackward(String fileId, String fileChannelName) {
        String fileIdFinal = fileId + FabricConstant.Separator + fileChannelName;
        String response = fabricFeignService.traceBackward(fileIdFinal, fileChannelName).body().toString();
        try {
            return parseRecordFromResponse(response);
        } catch (IOException e) {
            log.error("溯源失败,fileId:{}, response:{}", fileId, response);
            throw new FabricException("溯源失败: fileId:" + fileId);
        }
    }

    @Override
    public Record traceBackward(String fileId, String fileChannelName, String txId) {
        String fileIdFinal = fileId + FabricConstant.Separator + fileChannelName;
        String response = fabricFeignService.traceBackward(fileIdFinal, fileChannelName, txId).body().toString();
        try {
            return parseRecordFromResponse(response);
        } catch (IOException e) {
            log.error("溯源失败,fileId:{},txId:{}, response:{}", fileId, txId, response);
            throw new FabricException("溯源失败: fileId:" + fileId + ", txId: " + txId);
        }
    }

    @Override
    public List<Record> traceBackwardAll(String fileId, String fileChannelName) {
        List<Record> ans = new ArrayList<>();
        Record record = traceBackward(fileId, fileChannelName);
        ans.add(record);
        ans.addAll(traceBackwardAll(fileId, fileChannelName, record.getThisTxId()));
        return ans;
    }

    @Override
    public List<Record> traceBackwardAll(String fileId, String fileChannelName, String txId) {
        List<Record> ans = new ArrayList<>();
        Record record = traceBackward(fileId, fileChannelName, txId);
        ans.add(record);
        while(!record.getLastTxId().equals("0")){
            record = traceBackward(fileId, fileChannelName, record.getThisTxId());
            ans.add(record);
        }
        return ans;
    }

    @Override
    public String getPolicy(String channelName, String opt) {
        Response response = fabricFeignService.getPolicy(channelName, opt);
        return response.body().toString();
    }

    @Override
    public String getPolicy(String fileId, String channelName, String opt) {
        String obj = fileId + FabricConstant.Separator + channelName;
        Response response = fabricFeignService.getPolicy(obj, opt);
        return response.body().toString();
    }

    @Override
    public String queryAuthority(String txId, String order) {
        Response response = fabricFeignService.queryAuthority(txId, order);
        return response.body().toString();
    }

    @Override
    public String argsTest(String peers, String channelName, String ccName, String fcn, List<String> args) {
        return fabricFeignService.argsTest(peers, channelName, ccName, fcn, args);
    }
    
    
    public String getPeers(String requestor){
        String str1 = "peer0.org", str2 = ".example.com";
        char n = requestor.charAt(3);
        return str1 + n + str2;
    }
    
    public String getUsername(String requestor){
        String[] strings = requestor.split("_");
        String prefix ;
        if(strings[1].equals("admin")){
            prefix = "Admin";
        }else{
            prefix = "User1";
        }
        char n = requestor.charAt(3);
        return prefix + "@org" + n + ".example.com";
    }
}
