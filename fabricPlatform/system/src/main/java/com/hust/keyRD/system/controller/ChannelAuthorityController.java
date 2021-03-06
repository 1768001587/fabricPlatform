package com.hust.keyRD.system.controller;

import com.auth0.jwt.JWT;
import com.hust.keyRD.commons.Dto.UserChannelAuthDto;
import com.hust.keyRD.commons.entities.Channel;
import com.hust.keyRD.commons.entities.ChannelAuthority;
import com.hust.keyRD.commons.entities.CommonResult;
import com.hust.keyRD.commons.entities.User;
import com.hust.keyRD.commons.myAnnotation.CheckToken;
import com.hust.keyRD.commons.vo.AllChannelUserVO;
import com.hust.keyRD.commons.vo.mapper.AllChannelUserAuthorityVOMapper;
import com.hust.keyRD.system.api.service.FabricService;
import com.hust.keyRD.system.service.ChannelAuthorityService;
import com.hust.keyRD.system.service.ChannelService;
import com.hust.keyRD.system.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

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
    @GetMapping(value = "/channelAuthority/getAddAuthorityChannels")
    public CommonResult getAddAuthorityChannels(HttpServletRequest httpServletRequest) {
        // 从 http 请求头中取出 token
        String token = httpServletRequest.getHeader("token");
        Integer userId = JWT.decode(token).getClaim("id").asInt();
        List<String> list = channelAuthorityService.getAddAuthorityChannels(userId);
        List<Channel> result = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Channel channel = channelService.findChannelByName(list.get(i));
            result.add(channel);
        }
        return new CommonResult<>(200, "获取所有该用户可上传文件channel成功", result);
    }

    //获取本管理员所管理的通道的所有权限，返回AllChannelUserVO类
    @CheckToken
    @GetMapping(value = "/channelAuthority/getAllAuthorityChannels")
    public CommonResult getAllAuthorityChannels(HttpServletRequest httpServletRequest) {
        // 从 http 请求头中取出 token
        String token = httpServletRequest.getHeader("token");
        Integer userId = JWT.decode(token).getClaim("id").asInt();//管理员的id号
        User admin = userService.findUserById(userId);
        List<UserChannelAuthDto> usersChannelAuthorityList = channelAuthorityService.findUsersChannelAuthority(userId, admin.getChannelId());
        List<AllChannelUserVO> result = usersChannelAuthorityList.stream().map(AllChannelUserAuthorityVOMapper.INSTANCE::toAllDataUserAuthorityVO).collect(Collectors.toList());
        //这里要返回除了管理员的所有用户，而不是只有权限的用户，这样管理员才可以添加权限
        List<User> users = userService.getAllUser();
        Set<Integer> hasAuth = new HashSet<>();
        for (AllChannelUserVO a: result) {
            hasAuth.add(a.getUserId());
        }
        for (int i = 0; i < users.size(); i++) {//以下添加的是既没有权限的，且属于该管理员通道的用户
            if(!hasAuth.contains(users.get(i).getId())&&users.get(i).getChannelId().equals(admin.getChannelId())){
                User u = users.get(i);
                Channel c = channelService.findChannelById(u.getChannelId());
                AllChannelUserVO allChannelUserVO = new AllChannelUserVO();
                allChannelUserVO.setUserId(u.getId());
                allChannelUserVO.setUserName(u.getUsername());
                allChannelUserVO.setChannelId(c.getId());
                allChannelUserVO.setChannelName(c.getChannelName());
                result.add(allChannelUserVO);
            }
        }
        return new CommonResult<>(200, "查找成功", result);
    }

    //给用户添加管道权限
    @Transactional
    @PostMapping(value = "/channelAuthority/addChannelAuthority")
    public CommonResult addDataAuthority(@RequestBody ChannelAuthority channelAuthority) {
        Integer channelId = channelAuthority.getChannelId();
        Integer userId = channelAuthority.getUserId();
        Channel channel = channelService.findChannelById(channelId);
        User user = userService.findUserById(userId);
        Integer authorityKey = channelAuthority.getAuthorityKey();
        List<ChannelAuthority> channelAuthoritys = channelAuthorityService.findChannelAuthority(channelAuthority);
        if (channelAuthoritys.size() >= 1) return new CommonResult<>(400, "添加权限失败，该权限已存在", null);
        if (channel == null) return new CommonResult<>(400, "添加权限失败，不存在channelId为：" + channelId + "的channel", null);
        if (user == null) return new CommonResult<>(400, "添加权限失败，不存在userId为：" + userId + "的用户", null);
        if (authorityKey != 1) return new CommonResult<>(400, "authorityKey请选择：" +
                "1：在该channel上上传文件权限", null);
        log.info("************fabric添加管道权限操作记录区块链开始*****************");
        fabricService.grantUserPermission2Add(channel.getChannelName(), "AAA", user.getUsername());
        channelAuthorityService.addChannelAuthority(channelAuthority);
        log.info("************fabric添加管道权限操作记录区块链结束*****************");
        return new CommonResult<>(200, "channelAuthority添加权限成功", channelAuthority);
    }

    //撤销某个用户可以上传文件至某通道的权限
    @Transactional
    @PostMapping(value = "/channelAuthority/deleteChannelAuthority")
    public CommonResult deleteChannelAuthority(@RequestBody ChannelAuthority channelAuthority) {
        Integer channelId = channelAuthority.getChannelId();
        Integer userId = channelAuthority.getUserId();
        Channel channel = channelService.findChannelById(channelId);
        User user = userService.findUserById(userId);
        Integer authorityKey = channelAuthority.getAuthorityKey();
        if (channel == null) return new CommonResult<>(400, "撤销权限失败，不存在channelId为：" + channelId + "的channel", null);
        if (user == null) return new CommonResult<>(400, "撤销权限失败，不存在userId为：" + userId + "的用户", null);
        if (authorityKey != 1) return new CommonResult<>(400, "authorityKey请选择：" +
                "1：在该channel上上传文件权限", null);
        if (fabricService.revokeUserPermission2Add(channel.getChannelName(), "AAA", user.getUsername())) {
            Integer count = channelAuthorityService.deleteChannelAuthority(channelAuthority);
            if (count >= 1) {
                return new CommonResult<>(200, "channelAuthority撤销权限成功", channelAuthority);
            }
            else {
                throw new RuntimeException("sql error");
            }
        } else {
            return new CommonResult<>(400, "撤销失败", channelAuthority);
        }

    }
}
