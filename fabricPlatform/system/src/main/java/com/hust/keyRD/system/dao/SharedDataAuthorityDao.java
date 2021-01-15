package com.hust.keyRD.system.dao;

import com.hust.keyRD.commons.entities.SharedDataAuthority;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SharedDataAuthorityDao {
    //添加分享权限消息
    void addSharedDataAuthority(SharedDataAuthority sharedDataAuthority);
    //删除（已确认）分享权限消息
    void deleteSharedDataAuthority(SharedDataAuthority sharedDataAuthority);
    //获取所有请求分享信息
    List<SharedDataAuthority> receiveAllSharedDataMsg();
    //检查是否有请求分享的信息
    Integer checkSharedData(SharedDataAuthority sharedDataAuthority);
}
