package com.hust.keyRD.system.dao;

import com.hust.keyRD.commons.entities.DataSample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @program: system
 * @description: ChannelDataAuthority Dao
 * @author: zwh
 * @create: 2021-03-20 14:25
 **/
@Mapper
public interface ChannelDataAuthorityDao {

    /**
     * 获取当前用户可pull的其他域的文件list
     * @param userId 当前用户id
     * @param channelId  当前用户所在channelId
     * @return
     */
    List<DataSample> getInterChannelPullData(@Param("userId")Integer userId, @Param("channelId") Integer channelId);
}
