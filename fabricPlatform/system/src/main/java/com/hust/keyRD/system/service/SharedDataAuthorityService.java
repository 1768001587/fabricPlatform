package com.hust.keyRD.system.service;

import com.hust.keyRD.commons.entities.SharedDataAuthority;

import java.util.List;

public interface SharedDataAuthorityService {
    //添加分享权限消息
    void addSharedDataAuthority(SharedDataAuthority sharedDataAuthority);
    //删除（已确认）分享权限消息
    void deleteSharedDataAuthority(SharedDataAuthority sharedDataAuthority);
    //获取所有请求分享信息
    List<SharedDataAuthority> receiveAllSharedDataMsg();
    //检查是否有请求分享的信息
    Integer checkSharedData(SharedDataAuthority sharedDataAuthority);
}
