package one.hust.edu.cn.controller;

import lombok.extern.slf4j.Slf4j;
import one.hust.edu.cn.entities.CommonResult;
import one.hust.edu.cn.entities.User;
import one.hust.edu.cn.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping(value = "login")
    public CommonResult login(@RequestBody User user){
        boolean result = userService.login(user);
        if(result) return new CommonResult<>(200,"登录成功",user);
        else return new CommonResult<>(400,"登录失败,用户不存在或用户名或密码错误",null);
    }
    @PostMapping(value = "register")
    public CommonResult register(@RequestBody User user){
            boolean result = userService.register(user);
            if(result) return new CommonResult<>(200,"注册成功",user);
            else return new CommonResult<>(400,"注册失败,请联系系统管理员",null);
    }
}