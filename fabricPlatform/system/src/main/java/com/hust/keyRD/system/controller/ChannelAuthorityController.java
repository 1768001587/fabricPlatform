package com.hust.keyRD.system.controller;

import com.auth0.jwt.JWT;
import com.hust.keyRD.system.api.service.FabricService;
import lombok.extern.slf4j.Slf4j;
import com.hust.keyRD.commons.entities.*;
import com.hust.keyRD.commons.myAnnotation.CheckToken;
import com.hust.keyRD.system.service.ChannelAuthorityService;
import com.hust.keyRD.system.service.ChannelService;
import com.hust.keyRD.system.service.UserService;
import com.hust.keyRD.commons.vo.AllChannelUserVO;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Slf4j
@RestController
public class ChannelAuthorityController {
    @Resource
    ChannelAuthorityService channelAuthorityService;
    @Resource
    ChannelService channelService;
    @Resource
    UserService userService;
    @Resource
    private FabricService fabricService;


    @CheckToken
    @GetMapping(value = "/channel/getAddAuthorityChannels")
    public CommonResult getAddAuthorityChannels(HttpServletRequest httpServletRequest){
        // 从 http 请求头中取出 token
        String token = httpServletRequest.getHeader("token");
        Integer userId = JWT.decode(token).getClaim("id").asInt();
        List<String> list = channelAuthorityService.getAddAuthorityChannels(userId);
        List<Channel> result = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Channel channel = channelService.findChannelByName(list.get(i));
            result.add(channel);
        }
        return new CommonResult<>(200,"获取所有该用户可上传文件channel成功",result);
    }
    //获取所有权限，返回AllDataUserAuthorityVO类
    @GetMapping(value = "/channel/getAllAuthorityChannels")
    public CommonResult getAllAuthorityChannels() {
        List<User> users = userService.getAllUser();
        List<Channel> channels = channelService.getAllChannel();
        List<AllChannelUserVO> result = new ArrayList<>();
        Set<List<Integer>> set = new LinkedHashSet<>();//保存用户Id与对应的文件id
        for (int i = 0; i < users.size(); i++) {
            for (int j = 0; j < channels.size(); j++) {
                List<Integer> list = new ArrayList<>();
                list.add(users.get(i).getId());
                list.add(channels.get(j).getId());
                set.add(list);
            }
        }
        Iterator<List<Integer>> it = set.iterator();
        while (it.hasNext()) {
            List<Integer> tmp = it.next();
            List<ChannelAuthority> channelAuthorities = channelAuthorityService.findChannelAuthorityByUserIdAndChannelId(tmp.get(0),tmp.get(1));
            Set<Integer> authoritySet = new HashSet<>();
            for (int i = 0; i < channelAuthorities.size(); i++) {
                authoritySet.add(channelAuthorities.get(i).getAuthorityKey());
            }
            User user = userService.findUserById(tmp.get(0));
            Channel channel = channelService.findChannelById(tmp.get(1));
            AllChannelUserVO allChannelUserVO = new AllChannelUserVO();
            allChannelUserVO.setUserId(tmp.get(0));
            allChannelUserVO.setUserName(user.getUsername());
            allChannelUserVO.setChannelId(channel.getId());
            allChannelUserVO.setChannelName(channel.getChannelName());
            allChannelUserVO.setChannelAuthoritySet(authoritySet);
            result.add(allChannelUserVO);
        }
        return new CommonResult<>(200,"查找成功",result);
    }
    //给用户添加管道权限
    @Transactional
    @PostMapping(value = "/channel/addChannelAuthority")
    public CommonResult addDataAuthority(@RequestBody ChannelAuthority channelAuthority) {
        Integer channelId = channelAuthority.getChannelId();
        Integer userId = channelAuthority.getUserId();
        Channel channel = channelService.findChannelById(channelId);
        User user = userService.findUserById(userId);
        Integer authorityKey = channelAuthority.getAuthorityKey();
        List<ChannelAuthority> channelAuthoritys = channelAuthorityService.findChannelAuthority(channelAuthority);
        if(channelAuthoritys.size()>=1) return new CommonResult<>(400,"添加权限失败，该权限已存在",null);
        if(channel==null) return new CommonResult<>(400,"添加权限失败，不存在channelId为："+channelId+"的channel",null);
        if(user==null) return new CommonResult<>(400,"添加权限失败，不存在userId为："+userId+"的用户",null);
        if(authorityKey!=1) return new CommonResult<>(400,"authorityKey请选择：" +
                "1：在该channel上上传文件权限",null);
        log.info("************fabric添加管道权限操作记录区块链开始*****************");
       // fabricService.grantUserPermission2Add(channel.getChannelName(),"AAA",user.getUsername());
        channelAuthorityService.addChannelAuthority(channelAuthority);
        log.info("************fabric添加管道权限操作记录区块链结束*****************");
        return new CommonResult<>(200, "channelAuthority添加权限成功", channelAuthority);
    }
    //撤销某个用户可以上传文件至某通道的权限
    @Transactional
    @PostMapping(value = "/channel/deleteChannelAuthority")
    public CommonResult deleteChannelAuthority(@RequestBody ChannelAuthority channelAuthority) {
        Integer channelId = channelAuthority.getChannelId();
        Integer userId = channelAuthority.getUserId();
        Channel channel = channelService.findChannelById(channelId);
        User user = userService.findUserById(userId);
        Integer authorityKey = channelAuthority.getAuthorityKey();
        if(channel==null) return new CommonResult<>(400,"撤销权限失败，不存在channelId为："+channelId+"的channel",null);
        if(user==null) return new CommonResult<>(400,"撤销权限失败，不存在userId为："+userId+"的用户",null);
        if(authorityKey!=1) return new CommonResult<>(400,"authorityKey请选择：" +
                "1：在该channel上上传文件权限",null);

        Integer count = channelAuthorityService.deleteChannelAuthority(channelAuthority);

        if(count>=1) return new CommonResult<>(200, "channelAuthority撤销权限成功", channelAuthority);
        else return new CommonResult<>(200, "不存在该权限", channelAuthority);
    }
}
