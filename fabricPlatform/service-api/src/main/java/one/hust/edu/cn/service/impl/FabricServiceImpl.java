package one.hust.edu.cn.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import feign.Response;

import one.hust.edu.cn.feign.FabricFeignService;
import one.hust.edu.cn.service.FabricService;
import one.hust.edu.cn.utils.HashUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service("fabricService")
public class FabricServiceImpl implements FabricService {

    @Autowired
    private FabricFeignService fabricFeignService;

    @Override
    public String feignTest(int a, int b) {
//        System.out.println(restTemplate.getForObject(URL + "add/?a="+ a + "&b=" + b,String.class));
        return fabricFeignService.add(a, b);
    }

    @Override
    public String invokeChaincode(String peers, String channelName, String ccName,
                                  String fcn, List<String> args) {
        Response response = fabricFeignService.invokeChaincodeTest(peers, channelName, ccName, fcn, args);
//        System.out.println(response);
        return response.body().toString();
    }

    @Override
    public String grantUserPermission2Add(String dstChannelName, String role, String username) {
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
        return invokeChaincode(peers, channelName, ccName, fcn, args);
    }

    @Override
    public String grantUserPermissionOnFile(String dstChannelName, String fileId, String permission, String role, List<String> users) {
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
        return invokeChaincode(peers, channelName, ccName, fcn, args);
    }

    @Override
    public String revokeUserPermissionOnFile(String dstChannelName, String fileId, String permission, String role, List<String> users) {
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
        return invokeChaincode(peers, channelName, ccName, fcn, args);
    }

    @Override
    public String crossAccess(String peers, String channelName, String ccName, String fcn, List<String> args) {
        Response response = fabricFeignService.crossAccess(peers, channelName, ccName, fcn, args);
//        System.out.println(response);
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
            return applyForOptFile(username,dstChannelName, fileId, "add");
        } catch (IOException e) {
            System.out.println("获取创建文件权限失败");
            return "";
        }

    }

    @Override
    public String applyForReadFile(String username, String dstChannelName, String fileId) {
        try {
            return applyForOptFile(username,dstChannelName, fileId, "read");
        } catch (IOException e) {
            System.out.println("获取读取文件权限失败");
            return "";
        }
    }

    @Override
    public String applyForModifyFile(String username, String dstChannelName, String fileId) {
        try {
            return applyForOptFile(username,dstChannelName, fileId, "modify");
        } catch (IOException e) {
            System.out.println("获取创建文件权限失败");
            return "";
        }
    }

    private String updateForOptFile(String fileString, String username, String dstChannelName, String fileId, String txId, String opt){
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
        return dataSyncRecord(peers, channelName, ccName, fcn, args, txId);
    }

    @Override
    public String updateForCreateFile(String fileString, String username, String dstChannelName, String fileId, String txId) {
        return updateForOptFile(fileString, username, dstChannelName, fileId, txId, "add");
    }

    @Override
    public String updateForModifyFile(String fileString, String username, String dstChannelName, String fileId, String txId) {
        return updateForOptFile(fileString, username, dstChannelName, fileId, txId, "modify");
    }


    @Override
    public String createFile(String fileString, String username, String dstChannelName, String fileId) {
        // 1. 权限申请 一次上链
        String txId = applyForCreateFile(username, dstChannelName, fileId);
        if (txId == null || txId.isEmpty()) {
            System.out.println("申请文件创建权限失败");
            return "error";
        }
        System.out.println("1.创建文件成功 txId: " + txId);

        // 2. 文件上传到本地数据库
        System.out.println("2.修改文件。。。。");

        // 3. 更新链上hash 二次上链
        String rawRes = updateForCreateFile(fileString, username, dstChannelName, fileId, txId);
        System.out.println("3. 更新链上hash ： " + rawRes);

        // 4. 授予用户文件的查改权限
        rawRes = grantUserPermissionOnFile("channel1", fileId, "read", "role1", Collections.singletonList(username));
        System.out.println("4.授予用户文件读取权限：" + rawRes);
        rawRes = grantUserPermissionOnFile("channel1", fileId, "modify", "role1", Collections.singletonList(username));
        System.out.println("4.授予用户文件修改权限：" + rawRes);

        return "createFile success";

    }

    @Override
    public String dataSyncRecord(String peers, String channelName, String ccName, String fcn, List<String> args, String txId) {
        Response response = fabricFeignService.dataSyncRecord(peers, channelName, ccName, fcn, args, txId);
        return response.body().toString();
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
