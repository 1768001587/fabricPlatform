package com.hust.keyRD.system.controller;

import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import com.hust.keyRD.commons.entities.CommonResult;
import com.hust.keyRD.commons.entities.User;
import com.hust.keyRD.commons.myAnnotation.LoginToken;
import com.hust.keyRD.system.service.UserService;
import com.hust.keyRD.commons.utils.JwtUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
public class UserController{
    @Resource
    private UserService userService;
    //登录
    @PostMapping(value = "/user/login")
    @LoginToken
    public CommonResult login(@RequestBody User user){
        JSONObject jsonObject = new JSONObject();
        boolean result = userService.login(user);
        if(result) {
            User uresult = userService.findUserByUsername(user.getUsername());
            String token = JwtUtil.createJWT(Integer.MAX_VALUE, uresult);
            jsonObject.put("user", uresult);
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
                jsonObject.put("token", token);
                jsonObject.put("user", user);
                return new CommonResult<>(200,"注册成功",jsonObject);
            }
            else return new CommonResult<>(400,"注册失败,请联系系统管理员",null);
    }
}
