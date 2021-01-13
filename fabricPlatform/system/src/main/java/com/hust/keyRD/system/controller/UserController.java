package com.hust.keyRD.system.controller;

import cn.hutool.json.JSONObject;
import com.auth0.jwt.JWT;
import com.hust.keyRD.commons.myAnnotation.CheckToken;
import com.hust.keyRD.commons.vo.UserChannelVO;
import com.hust.keyRD.system.service.ChannelService;
import lombok.extern.slf4j.Slf4j;
import com.hust.keyRD.commons.entities.CommonResult;
import com.hust.keyRD.commons.entities.User;
import com.hust.keyRD.commons.myAnnotation.LoginToken;
import com.hust.keyRD.system.service.UserService;
import com.hust.keyRD.commons.utils.JwtUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
public class UserController{
    @Resource
    private UserService userService;
    @Resource
    private ChannelService channelService;
    //登录
    @PostMapping(value = "/user/login")
    @LoginToken
    public CommonResult login(@RequestBody User user){
        JSONObject jsonObject = new JSONObject();
        boolean result = userService.login(user);
        if(result) {
            User uresult = userService.findUserByUsername(user.getUsername());
            String token = JwtUtil.createJWT(Integer.MAX_VALUE, uresult);
            String channelName = channelService.findChannelById(uresult.getChannelId()).getChannelName();
            jsonObject.put("user", uresult);
            jsonObject.put("channelName", channelName);
            jsonObject.put("token", token);
            return new CommonResult<>(200,"登录成功",jsonObject);
        }
        else {
            return new CommonResult<>(400,"登录失败,用户不存在或用户名或密码错误",null);
        }
    }
    //注册
    @PostMapping(value = "/user/register")
    @LoginToken
    public CommonResult register(@RequestBody User user){
            JSONObject jsonObject = new JSONObject();
            if(userService.findUserByUsername(user.getUsername())!=null) return new CommonResult<>(400,"注册失败,用户名已存在",null);
            boolean result = userService.register(user);
            if(result) {
                String token = JwtUtil.createJWT(Integer.MAX_VALUE, user);
                String channelName = channelService.findChannelById(user.getChannelId()).getChannelName();
                jsonObject.put("token", token);
                jsonObject.put("channelName", channelName);
                jsonObject.put("user", user);
                return new CommonResult<>(200,"注册成功",jsonObject);
            }
            else return new CommonResult<>(400,"注册失败,请联系系统管理员",null);
    }

    //获取除了该用户以外的所有用户及其所在的channel名称
    @CheckToken
    @GetMapping(value = "/user/getUserExceptMe")
    public CommonResult getDataListByOriginUserId(HttpServletRequest httpServletRequest){
        // 从 http 请求头中取出 token
        String token = httpServletRequest.getHeader("token");
        Integer userId = JWT.decode(token).getClaim("id").asInt();
        List<User> users = userService.getAllUser();
        List<UserChannelVO> result  = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
            if(!users.get(i).getId().equals(userId)){
                UserChannelVO userChannelVO = new UserChannelVO();
                userChannelVO.setUser(users.get(i));
                userChannelVO.setChannelName(channelService.findChannelById(users.get(i).getChannelId()).getChannelName());
                result.add(userChannelVO);
            }
        }
        return new CommonResult<>(200,"获取除了该用户以外的所有用户及其所在的channel名称成功",result);
    }
}
