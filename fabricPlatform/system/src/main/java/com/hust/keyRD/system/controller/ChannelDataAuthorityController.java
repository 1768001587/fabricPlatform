package com.hust.keyRD.system.controller;

import com.hust.keyRD.commons.entities.ChannelDataAuthority;
import com.hust.keyRD.commons.entities.CommonResult;
import com.hust.keyRD.commons.entities.User;
import com.hust.keyRD.commons.exception.BadRequestException;
import com.hust.keyRD.commons.utils.JwtUtil;
import com.hust.keyRD.commons.vo.ChannelDataAuthorityVO;
import com.hust.keyRD.system.service.ChannelDataAuthorityService;
import com.hust.keyRD.system.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @program: system
 * @description: ChannelDataAuthorityController
 * @author: zwh
 * @create: 2021-03-22 10:37
 **/
@RestController
@Api(tags = "域间权限Controller")
@RequestMapping("innerChannelAuthority")
public class ChannelDataAuthorityController {
    @Resource
    private UserService userService;
    @Resource
    private ChannelDataAuthorityService channelDataAuthorityService;
    
    @ApiOperation("获取pull权限列表")
    @RequestMapping("getPullAuthorityList")
    public CommonResult<List<ChannelDataAuthorityVO>> getPullAuthorityList(){
        List<ChannelDataAuthorityVO> pullAuthorityList = channelDataAuthorityService.getPullAuthorityList();

        return new CommonResult(200, "success", pullAuthorityList);
    }

    @ApiOperation("获取push权限列表")
    @RequestMapping("getPushAuthorityList")
    public CommonResult<List<ChannelDataAuthorityVO>> getPushAuthorityList(){
        List<ChannelDataAuthorityVO> pushAuthorityList = channelDataAuthorityService.getPushAuthorityList();
        return new CommonResult(200, "success", pushAuthorityList);
    }
    
    
    
    @ApiOperation("添加域间pull权限")
    @PostMapping("addPullAuthority")
    public CommonResult addInterChannelPullAuthority(@RequestBody ChannelDataAuthority channelDataAuthority, HttpServletRequest httpServletRequest){
        // 判断是否是管理员
        Integer userId = JwtUtil.getUserId(httpServletRequest);
        User user = userService.findUserById(userId);
        if(user.getIsAdmin() != 1){
            throw new BadRequestException("非管理员");
        }
        channelDataAuthorityService.addPullAuthority(channelDataAuthority);
        // 区块链添加pull权限
        //TODO fabric操作

        return new CommonResult(200, "success");
    }


    @ApiOperation("添加域间push权限")
    @PostMapping("addPushAuthority")
    public CommonResult addInterChannelPushAuthority(@RequestBody ChannelDataAuthority channelDataAuthority, HttpServletRequest httpServletRequest){
        // 判断是否是管理员
        Integer userId = JwtUtil.getUserId(httpServletRequest);
        User user = userService.findUserById(userId);
        if(user.getIsAdmin() != 1){
            throw new BadRequestException("非管理员");
        }
        channelDataAuthorityService.addPushAuthority(channelDataAuthority);
        // 区块链添加pull权限
        //TODO fabric操作

        return new CommonResult(200, "success");
    }
    
    @ApiOperation("删除域间pull权限")
    @DeleteMapping("deletePullAuthority")
    public CommonResult deleteInterChannelPullAuthority(Integer channelDataAuthorityId, HttpServletRequest httpServletRequest){
        // 判断是否是管理员
        Integer userId = JwtUtil.getUserId(httpServletRequest);
        User user = userService.findUserById(userId);
        if(user.getIsAdmin() != 1){
            throw new BadRequestException("非管理员");
        }
        ChannelDataAuthority channelDataAuthority = channelDataAuthorityService.findById(channelDataAuthorityId);
        if(!channelDataAuthority.getType().equals(2)){
            throw new BadRequestException("非pull类型");
        }
        channelDataAuthorityService.deleteById(channelDataAuthorityId);
        // 区块链删除权限
        //TODO fabric操作

        return new CommonResult(200,"success");
    }

    @ApiOperation("删除域间push权限")
    @DeleteMapping("deletePushAuthority")
    public CommonResult deleteInterChannelPushAuthority(Integer channelDataAuthorityId, HttpServletRequest httpServletRequest){
        // 判断是否是管理员
        Integer userId = JwtUtil.getUserId(httpServletRequest);
        User user = userService.findUserById(userId);
        if(user.getIsAdmin() != 1){
            throw new BadRequestException("非管理员");
        }
        ChannelDataAuthority channelDataAuthority = channelDataAuthorityService.findById(channelDataAuthorityId);
        if(!channelDataAuthority.getType().equals(1)){
            throw new BadRequestException("非push类型");
        }
        channelDataAuthorityService.deleteById(channelDataAuthorityId);
        // 区块链删除权限
        //TODO fabric操作

        return new CommonResult(200,"success");
    }
    
    
}
