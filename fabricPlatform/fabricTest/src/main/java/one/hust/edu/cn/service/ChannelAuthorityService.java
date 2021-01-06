package one.hust.edu.cn.service;


import one.hust.edu.cn.entities.ChannelAuthority;

import java.util.List;

public interface ChannelAuthorityService {
    List<String> getAddAuthorityChannels(Integer userId);
    //根据用户名和channelId查找channelAuthority
    List<ChannelAuthority> findChannelAuthorityByUserIdAndChannelId(Integer userId, Integer channelId);
}
