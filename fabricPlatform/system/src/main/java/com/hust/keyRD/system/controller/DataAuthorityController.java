package com.hust.keyRD.system.controller;

import com.auth0.jwt.JWT;
import com.hust.keyRD.commons.entities.*;
import com.hust.keyRD.commons.myAnnotation.CheckToken;
import com.hust.keyRD.commons.vo.AllDataUserAuthorityVO;
import com.hust.keyRD.system.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Slf4j
@RestController
public class DataAuthorityController {
    @Resource
    private DataAuthorityService dataAuthorityService;
    @Resource
    private UserService userService;
    @Resource
    private DataService dataService;
    @Resource
    private ChannelService channelService;
    @Resource
    private GrantPermissionService grantPermissionService;
    @Resource
    SharedDataAuthorityService sharedDataAuthorityService;

    // TODO： 身份验证
    //给用户，文件添加权限
    @Transactional
    @PostMapping(value = "/dataAuthority/addDataAuthority")
    public CommonResult addDataAuthority(@RequestBody DataAuthority dataAuthority) {
        Integer userId = dataAuthority.getUserId();
        Integer dataSampleId = dataAuthority.getDataSampleId();
        Integer authorityKey = dataAuthority.getAuthorityKey();
        User user = userService.findUserById(userId);
        DataSample dataSample = dataService.findDataById(dataSampleId);
        if(user==null) return new CommonResult<>(400,"添加权限失败，不存在userId为："+userId+"的用户",null);
        if(dataSample ==null) return new CommonResult<>(400,"添加权限失败，不存在dataSampleId为："+dataSampleId+"的文件",null);
        if(authorityKey!=1&&authorityKey!=2&&authorityKey!=3) return new CommonResult<>(400,"authorityKey请选择：" +
                "用户权限 1代表查看文件 2代表修改文件 3代表删除文件",null);
        log.info("************fabric添加文件权限操作记录写入区块链开始*****************");
        grantPermissionService.grantUserPermissionOnFile(dataAuthority);
        dataAuthorityService.addDataAuthority(dataAuthority);
        log.info("************fabric添加文件权限操作记录写入区块链结束*****************");
        return new CommonResult<>(200, "dataAuthority添加权限成功", dataAuthority);
    }

    //给用户，文件撤销权限
    @Transactional
    @PostMapping(value = "/dataAuthority/deleteDataAuthority")
    public CommonResult deleteDataAuthority(@RequestBody DataAuthority dataAuthority) {
        Integer userId = dataAuthority.getUserId();
        Integer dataSampleId = dataAuthority.getDataSampleId();
        Integer authorityKey = dataAuthority.getAuthorityKey();
        User user = userService.findUserById(userId);
        DataSample dataSample = dataService.findDataById(dataSampleId);
        if(user==null) return new CommonResult<>(400,"添加权限失败，不存在userId为："+userId+"的用户",null);
        if(dataSample ==null) return new CommonResult<>(400,"添加权限失败，不存在dataSampleId为："+dataSampleId+"的文件",null);

        if(authorityKey!=1&&authorityKey!=2&&authorityKey!=3) return new CommonResult<>(400,"authorityKey请选择：" +
                "用户权限 1代表查看文件 2代表修改文件 3代表删除文件",null);
        log.info("************fabric撤销文件权限操作记录区块链开始*****************");
        if(!grantPermissionService.revokeUserPermissionOnFile(dataAuthority)){
            return new CommonResult<>(400,"fabric: 撤销权限失败");
        }
        Integer count = dataAuthorityService.deleteDataAuthority(dataAuthority);
        log.info("************fabric撤销文件权限操作记录区块链结束*****************");
        if(count>=1) return new CommonResult<>(200,"dataAuthority撤销权限成功",dataAuthority);
        else return new CommonResult<>(400,"撤销权限失败，请联系系统管理员",dataAuthority);
    }

    //获取所有权限，返回AllDataUserAuthorityVO类
    @GetMapping(value = "/dataAuthority/getAllAuthority")
    public CommonResult getAllAuthority() {
        List<AllDataUserAuthorityVO> result = new ArrayList<>();
        Set<List<Integer>> set = new LinkedHashSet<>();//保存用户Id与对应的文件id
        List<User> users = userService.getAllUser();
        List<DataSample> dataSamples = dataService.getDataList();
        for (int i = 0; i < users.size(); i++) {
            for (int j = 0; j < dataSamples.size(); j++) {
                List<Integer> s = new ArrayList<>();
                s.add(users.get(i).getId());
                s.add(dataSamples.get(j).getId());
                set.add(s);
            }
        }
        Iterator<List<Integer>> it = set.iterator();
        while (it.hasNext()) {
            List<Integer> tmp = it.next();
            List<DataAuthority> dataAuthorities = dataAuthorityService.findDataAuthorityByUserIdAndDataId(tmp.get(0),tmp.get(1));
            Set<Integer> authoritySet = new HashSet<>();
            for (int i = 0; i < dataAuthorities.size(); i++) {
                authoritySet.add(dataAuthorities.get(i).getAuthorityKey());
            }
            User user = userService.findUserById(tmp.get(0));
            DataSample dataSample = dataService.findDataById(tmp.get(1));
            AllDataUserAuthorityVO allDataUserAuthorityVO = new AllDataUserAuthorityVO();
            allDataUserAuthorityVO.setUserId(tmp.get(0));
            allDataUserAuthorityVO.setUserName(user.getUsername());
            allDataUserAuthorityVO.setChannelName(channelService.findChannelById(dataSample.getChannelId()).getChannelName());
            allDataUserAuthorityVO.setDataId(tmp.get(1));
            allDataUserAuthorityVO.setDataName(dataSample.getDataName());
            allDataUserAuthorityVO.setChannelAuthoritySet(authoritySet);
            result.add(allDataUserAuthorityVO);
        }
        return new CommonResult<>(200,"查找成功",result);
    }

    //查找某一用户的所有权限
    @PostMapping(value = "/dataAuthority/findDataAuthorityByUserId")
    public CommonResult findDataAuthorityByUserId(@RequestBody Map<String, String> params) {
        Integer userId = Integer.valueOf(params.get("userId"));
        User user = userService.findUserById(userId);
        if(user==null) return new CommonResult<>(400,"不存在userId为："+userId+"的用户",null);
        List<DataAuthority> result = dataAuthorityService.findDataAuthorityByUserId(userId);
        return new CommonResult<>(200,"查找成功",result);
    }

    //查找某一文件的所有权限
    @PostMapping(value = "/dataAuthority/findDataAuthorityByDataId")
    public CommonResult findDataAuthorityByDataId(@RequestBody Map<String, String> params) {
        Integer dataSampleId = Integer.valueOf(params.get("dataSampleId"));
        DataSample dataSample = dataService.findDataById(dataSampleId);
        if(dataSample ==null) return new CommonResult<>(400,"不存在dataSampleId为："+dataSampleId+"的文件",null);
        List<DataAuthority> result = dataAuthorityService.findDataAuthorityByDataId(dataSampleId);
        return new CommonResult<>(200,"查找成功",result);
    }

    //某一用户授权给另一用户查看文件的权限
    @CheckToken
    @PostMapping(value = "/dataAuthority/sharedDataAuthorityOnSeeing")
    public CommonResult shareDataAuthorityOnSeeing(@RequestBody Map<String, String> params, HttpServletRequest httpServletRequest) {
        // 从 http 请求头中取出 token
        String token = httpServletRequest.getHeader("token");
        Integer userId = JWT.decode(token).getClaim("id").asInt();//授权者Id
        Integer sharedUserId = Integer.valueOf(params.get("sharedUserId"));//被授权者用户Id
        Integer sharedDataId = Integer.valueOf(params.get("sharedDataId"));//授权文件Id
        /**
         * 以下将消息传入到消息队列中，等待管理员同意
         */
        //用单播模式
//        SharedDataVO sharedDataVO = new SharedDataVO();
//        sharedDataVO.setShareUserId(userId);
//        sharedDataVO.setSharedUserId(sharedUserId);
//        sharedDataVO.setDataSample(dataService.findDataById(sharedDataId));
        //rabbitTemplate.convertAndSend("fabric.shareDataAuthorityOnSeeing","shareDataAuthorityMsg",sharedDataVO);
        SharedDataAuthority sharedDataAuthority = new SharedDataAuthority();
        sharedDataAuthority.setShareUserId(userId);
        sharedDataAuthority.setSharedUserId(sharedUserId);
        sharedDataAuthority.setSharedDataId(sharedDataId);
        sharedDataAuthority.setAuthorityKey(1);
        sharedDataAuthorityService.addSharedDataAuthority(sharedDataAuthority);
        return new CommonResult<>(200,"发送请求成功",null);
    }

    //管理员对 用户授权给另一用户查看文件的权限 消息进行接收
    @CheckToken
    @GetMapping(value = "/dataAuthority/receiveAllSharedDataMsg")
    public CommonResult receiveSharedDataMsg(HttpServletRequest httpServletRequest) {
        try {
//            List<SharedDataVO> result = new LinkedList<>(); //这里不能直接引用
//            SharedDataVO tmp = (SharedDataVO) rabbitTemplate.receiveAndConvert("shareDataAuthorityMsg");
//            while(tmp!=null){
//                result.add(tmp);
//                tmp = (SharedDataVO) rabbitTemplate.receiveAndConvert("shareDataAuthorityMsg");
//            }
            // 从 http 请求头中取出 token
            String token = httpServletRequest.getHeader("token");
            Integer adminId = JWT.decode(token).getClaim("id").asInt();//
            List<SharedDataAuthority> list = sharedDataAuthorityService.receiveAllSharedDataMsg();
            List<SharedDataAuthority> result = new ArrayList<>();
            //只显示他管理的channel分享请求
            for (int i = 0; i < list.size(); i++) {
                SharedDataAuthority sharedDataAuthority = list.get(i);
                Integer channelId = dataService.findDataById(sharedDataAuthority.getSharedDataId()).getChannelId();
                User admin = userService.findUserById(adminId);
                if(channelId.equals(admin.getChannelId())){
                    result.add(sharedDataAuthority);
                }
            }
            return new CommonResult<>(200,"接收所有请求成功",result);
        }catch (Exception e){
            return new CommonResult<>(500,"接收有误，请联系系统管理员",null);
        }
    }

    //管理员是否同意该授权
    @PostMapping(value = "/dataAuthority/confirmSharedDataMsgOrNot")
    public CommonResult confirmSharedDataMsgOrNot(@RequestBody Map<String, String> params){
        Integer sharedUserId = Integer.valueOf(params.get("sharedUserId"));//被授权者用户Id
        Integer sharedDataId = Integer.valueOf(params.get("sharedDataId"));//授权文件Id
        Integer confirmOrNot = Integer.valueOf(params.get("confirmOrNot"));//是否同意授权，同意为1，不同意为0
        if(confirmOrNot==1){
            //写入数据库
            DataAuthority dataAuthority = new DataAuthority();
            dataAuthority.setUserId(sharedUserId);
            dataAuthority.setDataSampleId(sharedDataId);
            dataAuthority.setAuthorityKey(1);//查看id
            dataAuthorityService.addDataAuthority(dataAuthority);
            return new CommonResult<>(200,"增加权限成功",null);
        }else{
            //TODO:点击不同意后如何处理？
            return null;
        }
    }
}
