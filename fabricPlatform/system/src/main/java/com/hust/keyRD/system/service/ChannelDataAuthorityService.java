package com.hust.keyRD.system.service;


import com.hust.keyRD.commons.entities.ChannelDataAuthority;
import com.hust.keyRD.commons.entities.DataSample;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author zwh
 * @email happyzhao1010@gmail.com
 * @date 2021-03-20 13:21:45
 */
public interface ChannelDataAuthorityService {

    /**
     * 获取当前用户可pull的其他域的文件list
     * @param userId 当前用户id
     * @param channelId 当前用户所在channelId
     * @return
     */
    List<DataSample> getInterChannelPullData(Integer userId, Integer channelId);
}

