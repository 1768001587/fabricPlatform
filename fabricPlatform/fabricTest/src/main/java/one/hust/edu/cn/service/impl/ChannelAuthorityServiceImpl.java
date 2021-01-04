package one.hust.edu.cn.service.impl;

import one.hust.edu.cn.dao.ChannelAuthorityDao;
import one.hust.edu.cn.entities.Channel;
import one.hust.edu.cn.entities.ChannelAuthority;
import one.hust.edu.cn.service.ChannelAuthorityService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ChannelAuthorityServiceImpl implements ChannelAuthorityService {
    @Resource
    private ChannelAuthorityDao channelAuthorityDao;
    @Override
    public List<String> getAddAuthorityChannels(Integer userId) {
        return channelAuthorityDao.getAddAuthorityChannels(userId);
    }
}
