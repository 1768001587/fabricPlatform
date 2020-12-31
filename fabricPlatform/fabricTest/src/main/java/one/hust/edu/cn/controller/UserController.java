package one.hust.edu.cn.controller;

import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import one.hust.edu.cn.entities.CommonResult;
import one.hust.edu.cn.entities.User;
import one.hust.edu.cn.myAnnotation.LoginToken;
import one.hust.edu.cn.service.UserService;
import one.hust.edu.cn.utils.JwtUtil;
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
    @PostMapping(value = "login")
    @LoginToken
    public CommonResult login(@RequestBody User user){
        JSONObject jsonObject = new JSONObject();
        boolean result = userService.login(user);
        if(result) {
            User uresult = userService.findUserByUsername(user.getUsername());
            String token = JwtUtil.createJWT(6000000, uresult);
            jsonObject.put("user", uresult);
            jsonObject.put("token", token);
            return new CommonResult<>(200,"登录成功",jsonObject);
        }
        else {
            return new CommonResult<>(400,"登录失败,用户不存在或用户名或密码错误",null);
        }
    }
    //注册
    @PostMapping(value = "register")
    @LoginToken
    public CommonResult register(@RequestBody User user){
            JSONObject jsonObject = new JSONObject();
            if(userService.findUserByUsername(user.getUsername())!=null) return new CommonResult<>(400,"注册失败,用户名已存在",null);
            boolean result = userService.register(user);
            if(result) {
                String token = JwtUtil.createJWT(600000000, user);
                jsonObject.put("token", token);
                jsonObject.put("user", user);
                return new CommonResult<>(200,"注册成功",jsonObject);
            }
            else return new CommonResult<>(400,"注册失败,请联系系统管理员",null);
    }
}
