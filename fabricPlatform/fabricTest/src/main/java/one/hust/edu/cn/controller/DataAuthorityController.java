package one.hust.edu.cn.controller;

import lombok.extern.slf4j.Slf4j;
import one.hust.edu.cn.entities.CommonResult;
import one.hust.edu.cn.entities.DataAuthority;
import one.hust.edu.cn.entities.DataSample;
import one.hust.edu.cn.entities.User;
import one.hust.edu.cn.service.*;
import one.hust.edu.cn.vo.AllDataUserAuthorityVO;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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

    // TODO： 身份验证
    //给用户，文件添加权限
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
                "1：查看文件  2：修改文件  3：删除文件",null);
        if(!grantPermissionService.grantUserPermissionOnFile(dataAuthority)){
            return new CommonResult<>(400,"fabric: 添加权限失败");
        }
        dataAuthorityService.addDataAuthority(dataAuthority);
        return new CommonResult<>(200,"添加权限成功",dataAuthority);
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
                "1：查看文件  2：修改文件  3：删除文件",null);
        if(!grantPermissionService.revokeUserPermissionOnFile(dataAuthority)){
            return new CommonResult<>(400,"fabric: 撤销权限失败");
        }
        Integer count = dataAuthorityService.deleteDataAuthority(dataAuthority);
        if(count==1) return new CommonResult<>(200,"撤销权限成功",dataAuthority);
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
            allDataUserAuthorityVO.setDataAuthoritySet(authoritySet);
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
}
