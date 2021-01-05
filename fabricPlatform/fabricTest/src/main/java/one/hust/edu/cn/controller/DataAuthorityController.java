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
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class DataAuthorityController {
    @Resource
    DataAuthorityService dataAuthorityService;
    @Resource
    UserService userService;
    @Resource
    DataService dataService;

    //给用户，文件添加权限
    @PostMapping(value = "/dataAuthority/addDataAuthority")
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
        MyFile myFile = dataService.findDataById(dataSampleId);
        if(myFile==null) return new CommonResult<>(400,"不存在dataSampleId为："+dataSampleId+"的文件",null);
        List<DataAuthority> result = dataAuthorityService.findDataAuthorityByDataId(dataSampleId);
        return new CommonResult<>(200,"查找成功",result);
    }
    //根据id更改权限
    @PostMapping(value = "/dataAuthority/editDataAuthority")
    public CommonResult editDataAuthority(@RequestBody Map<String, String> params) {
        Integer id = Integer.valueOf(params.get("id"));
        Integer authorityKey = Integer.valueOf(params.get("authorityKey"));
        DataAuthority dataAuthority = dataAuthorityService.findDataAuthorityById(id);
        if(dataAuthority==null) return new CommonResult<>(400,"不存在id为："+id+"的文件权限记录",null);
        if(authorityKey!=1&&authorityKey!=2&&authorityKey!=3) return new CommonResult<>(400,"authorityKey请选择：" +
                "1：查看文件  2：修改文件  3：删除文件",null);
        dataAuthorityService.editDataAuthority(id,authorityKey);
        return new CommonResult<>(200,"更改权限成功",null);
    }
}
