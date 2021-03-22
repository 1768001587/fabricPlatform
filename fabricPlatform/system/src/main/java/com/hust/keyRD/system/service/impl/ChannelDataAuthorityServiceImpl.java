package com.hust.keyRD.system.service.impl;

import com.hust.keyRD.commons.Dto.PushDataInfoDto;
import com.hust.keyRD.commons.entities.DataSample;
import com.hust.keyRD.system.dao.ChannelDataAuthorityDao;
import com.hust.keyRD.system.service.ChannelDataAuthorityService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service("channelDataAuthorityService")
public class ChannelDataAuthorityServiceImpl implements ChannelDataAuthorityService {
    @Resource
    private ChannelDataAuthorityDao channelDataAuthorityDao;

    @Override
    public List<DataSample> getInterChannelPullData(Integer userId, Integer channelId) {
        return channelDataAuthorityDao.getInterChannelPullData(userId, channelId);
    }

    @Override
    public List<PushDataInfoDto> getInnerChannelPushData(Integer userId, Integer channelId) {
        return channelDataAuthorityDao.getInnerChannelPushData(userId, channelId);
    }

    @Override
    public Integer checkPullAuthority(Integer userId, Integer dataId, Integer channelId) {
        return channelDataAuthorityDao.checkPullAuthority(userId,dataId,channelId);
    }

    @Override
    public Integer checkPushAuthority(Integer userId, Integer dataId, Integer channelId) {
        return channelDataAuthorityDao.checkPushAuthority(userId,dataId,channelId);
    }
}