package com.hust.keyRD.system.api.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import feign.Response;

import lombok.extern.slf4j.Slf4j;
import com.hust.keyRD.commons.entities.Record;
import com.hust.keyRD.commons.exception.fabric.FabricException;
import com.hust.keyRD.system.api.feign.FabricFeignService;
import com.hust.keyRD.system.api.service.FabricService;
import com.hust.keyRD.commons.utils.HashUtil;
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
    public Boolean grantUserPermission2Add(String dstChannelName, String role, String username) {
        // 由于该例子中授权都由中心链上的org1完成 暂时写死
        String peers = "peer0.org1.example.com";
        String channelName = "centre";
        String ccName = "audit";
        String fcn = "AddPolicy";
        List<String> args = new ArrayList<String>() {{
            add(dstChannelName);
            add("add");
            add(role);
            add(username);
        }};
        String response = invokeChaincode(peers, channelName, ccName, fcn, args);

        return handleGrantPermissionRes(response, dstChannelName, Collections.singletonList(username), "add");

    }

    @Override
    public Boolean grantUserPermissionOnFile(String dstChannelName, String fileId, String permission, String role, List<String> users) {
        // 由于该例子中授权都由中心链上的org1完成 暂时写死
        String peers = "peer0.org1.example.com";
        String channelName = "centre";
        String ccName = "audit";
        String fcn = "AddPolicy";
        List<String> args = new ArrayList<String>() {{
            add(fileId);
            add(permission);
            add(role);
            addAll(users);
        }};
        String response = invokeChaincode(peers, channelName, ccName, fcn, args);
        return handleGrantPermissionRes(response, dstChannelName, users, permission);
    }

    private boolean handleGrantPermissionRes(String response, String dstChannelName, List<String> users, String permission) {

        if (response.contains("Success")) {
            log.info("授权用户{}在{}上{}文件的权限成功,info:{}", users, dstChannelName, permission, response);
            return true;
        } else if (response.contains("exists")) {
            log.warn("授权用户{}在{}上{}文件的权限失败,info:{}", users, dstChannelName, permission, response);
            throw new FabricException("授权失败,权限已存在,info：" + response);
        } else {
            log.warn("授权用户{}在{}上{}文件的权限失败,info:{}", users, dstChannelName, permission, response);
            throw new FabricException("授权失败,info: " + response);
        }
    }

    @Override
    public Boolean revokeUserPermissionOnFile(String dstChannelName, String fileId, String permission, String role, List<String> users) {
        // 由于该例子中授权都由中心链上的org1完成 暂时写死
        String peers = "peer0.org1.example.com";
        String channelName = "centre";
        String ccName = "audit";
        String fcn = "UpdatePolicy";
        List<String> args = new ArrayList<String>() {{
            add(fileId);
            add(permission);
            add("deleteuser");
            add(role);
            addAll(users);
        }};
        String response = invokeChaincode(peers, channelName, ccName, fcn, args);
        if (response.contains("Success")) {
            log.info("撤销用户{}在{}上{}文件的权限成功,info:{}", users, dstChannelName, permission, response);
            return true;
        } else {
            log.warn("撤销用户{}在{}上{}文件的权限失败,info:{}", users, dstChannelName, permission, response);
            throw new FabricException("撤销权限失败,info: " + response);
        }
    }

    @Override
    public String crossAccess(String peers, String channelName, String ccName, String fcn, List<String> args) {
        Response response = fabricFeignService.crossAccess(peers, channelName, ccName, fcn, args);
        return response.body().toString();
    }

    private String applyForOptFile(String username, String dstChannelName, String fileId, String opt) throws IOException {
        // 由于该例子中一次上链都由org5发起 暂时写死
        String peers = "peer0.org5.example.com";
        String channelName = "channel2";
        String ccName = "record";
        String fcn = "srcAuditRecord";
        //args:  hashdata, srcchain, user, dstchain, dataid, typetx
        List<String> args = new ArrayList<String>() {{
            add("null");
            add("channel2");
            add(username);
            add(dstChannelName);
            add(fileId);
            add(opt);
        }};
        String rawRes = crossAccess(peers, channelName, ccName, fcn, args);
        // 获取上链事务id
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(rawRes);
        return root.path("dst_chain_record").path("this_tx_id").asText();
    }

    @Override
    public String applyForCreateFile(String username, String dstChannelName, String fileId) {
        try {
            String txId = applyForOptFile(username, dstChannelName, fileId, "add");
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
    public String applyForReadFile(String username, String dstChannelName, String fileId) {
        try {
            String txId = applyForOptFile(username, dstChannelName, fileId, "add");
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
    public String applyForModifyFile(String username, String dstChannelName, String fileId) {
        try {
            return applyForOptFile(username, dstChannelName, fileId, "modify");
        } catch (IOException e) {
            log.error("用户{}获取修改{}上的文件{}权限失败", username, dstChannelName, fileId);
            throw new FabricException("获取修改文件权限失败");
        }
    }

    // 二次上链
    private Record updateForOptFile(String fileString, String username, String dstChannelName, String fileId, String txId, String opt) {
        String hashSHA1 = HashUtil.hash(fileString, "SHA1");
        String peers = "peer0.org3.example.com";
        String channelName = "channel1";
        String ccName = "record";
        String fcn = "dataSyncRecord";
        List<String> args = new ArrayList<String>() {{
            add(hashSHA1);
            add("channel2");
            add(username);
            add(dstChannelName);
            add(fileId);
            add(opt);
        }};
        // TODO: 如果fabric有错误，将返回什么？
        // 成功返回Record对象json
        String response = dataSyncRecord(peers, channelName, ccName, fcn, args, txId);
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(response, Record.class);
        } catch (IOException e) {
            log.error("二次上链更新hash失败,username:{},dstChannelName:{},fileId:{},txId:{},opt:{}, response:{}", username, dstChannelName, fileId, txId, opt, response);
            throw new FabricException("二次上链更新hash失败");
        }

    }

    @Override
    public Record updateForCreateFile(String fileString, String username, String dstChannelName, String fileId, String txId) {
        return updateForOptFile(fileString, username, dstChannelName, fileId, txId, "add");
    }

    @Override
    public Record updateForModifyFile(String fileString, String username, String dstChannelName, String fileId, String txId) {
        return updateForOptFile(fileString, username, dstChannelName, fileId, txId, "modify");
    }


    //    todo: 处理返回结果 返回正确结果/抛异常
    @Override
    public String dataSyncRecord(String peers, String channelName, String ccName, String fcn, List<String> args, String txId) {
        Response response = fabricFeignService.dataSyncRecord(peers, channelName, ccName, fcn, args, txId);
        return response.body().toString();
    }

    private Record parseRecordFromResponse (String response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response);
        JsonNode txInfo = root.path("tx_info");
        String recordJson = txInfo.toString().replaceAll("\\\\","");
        recordJson = recordJson.substring(1,recordJson.length()-1);
        return mapper.readValue(recordJson, Record.class);
    }
    
    @Override
    public Record traceBackward(String fileId) {
        String response = fabricFeignService.traceBackward(fileId).body().toString();
        ObjectMapper mapper = new ObjectMapper();
        try {
            return parseRecordFromResponse(response);
        } catch (IOException e) {
            log.error("溯源失败,fileId:{}, response:{}", fileId,  response);
            throw new FabricException("溯源失败: fileId:" + fileId);
        }
    }

    @Override
    public Record traceBackward(String fileId, String txId) {
        String response = fabricFeignService.traceBackward(fileId, txId).body().toString();
        ObjectMapper mapper = new ObjectMapper();
        try {
            return parseRecordFromResponse(response);
        } catch (IOException e) {
            log.error("溯源失败,fileId:{},txId:{}, response:{}", fileId, txId, response);
            throw new FabricException("溯源失败: fileId:" + fileId + ", txId: " + txId);
        }
    }

    @Override
    public String getPolicy(String obj, String opt) {
        Response response = fabricFeignService.getPolicy(obj, opt);
        return response.toString();
    }

    @Override
    public String queryAuthority(String txId, String order) {
        Response response = fabricFeignService.queryAuthority(txId, order);
        return response.toString();
    }

    @Override
    public String argsTest(String peers, String channelName, String ccName, String fcn, List<String> args) {
        return fabricFeignService.argsTest(peers, channelName, ccName, fcn, args);
    }
}
