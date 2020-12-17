package one.hust.edu.cn.controller;

import lombok.extern.slf4j.Slf4j;
import one.hust.edu.cn.entities.CommonResult;
import one.hust.edu.cn.entities.User;
import one.hust.edu.cn.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RestController
public class TestController {
    @Resource
    private UserService service;
    @GetMapping(value = "/test/{id}")
    public String test(@PathVariable("id") int id){
        return "this is fabricTest id="+id;
    }

    @GetMapping(value = "/testAPI/{id}")
    public CommonResult testAPI(@PathVariable("id") int id){
        CommonResult commonResult = new CommonResult(200,"通用API测试成功,id为："+id,null);
        return commonResult;
    }

    @GetMapping(value = "/findUserById/{id}")
    public CommonResult findUserById(@PathVariable("id") Integer id){
        User result = service.findUserById(id);
        if(result==null) return new CommonResult(400,"无此用户",null);
        return new CommonResult(200,"用户id："+id,result);
    }

    @GetMapping(value = "/getAllUser")
    public CommonResult getAllUser(){
        List<User> result = service.getAllUser();
        if(result==null) return new CommonResult(400,"无用户",null);
        return new CommonResult(200,"查找成功",result);
    }
}
