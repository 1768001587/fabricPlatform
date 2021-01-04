package one.hust.edu.cn.controller;

import lombok.extern.slf4j.Slf4j;
import one.hust.edu.cn.entities.CommonResult;
import one.hust.edu.cn.entities.DataAuthority;
import one.hust.edu.cn.entities.MyFile;
import one.hust.edu.cn.entities.User;
import one.hust.edu.cn.service.DataAuthorityService;
import one.hust.edu.cn.service.DataService;
import one.hust.edu.cn.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
public class DataAuthorityController {
    @Resource
    DataAuthorityService dataAuthorityService;
    @Resource
    UserService userService;
    @Resource
    DataService dataService;


    //给用户添加权限
    @PostMapping(value = "/channel/addDataAuthority")
    public CommonResult addDataAuthority(@RequestBody DataAuthority dataAuthority) {
        Integer userId = dataAuthority.getUserId();
        Integer dataSampleId = dataAuthority.getDataSampleId();
        Integer authorityKey = dataAuthority.getAuthorityKey();
        User user = userService.findUserById(userId);
        MyFile myFile = dataService.findDataById(dataSampleId);
        if(user==null) return new CommonResult<>(400,"添加权限失败，不存在userId为："+userId+"的用户",null);
        if(myFile==null) return new CommonResult<>(400,"添加权限失败，不存在dataSampleId为："+dataSampleId+"的文件",null);
        if(authorityKey!=1&&authorityKey!=2&&authorityKey!=3) return new CommonResult<>(400,"authorityKey请选择：" +
                "1：查看文件  2：修改文件  3：删除文件",null);
        dataAuthorityService.addDataAuthority(dataAuthority);
        return new CommonResult<>(200,"添加权限成功",dataAuthority);
    }
}
