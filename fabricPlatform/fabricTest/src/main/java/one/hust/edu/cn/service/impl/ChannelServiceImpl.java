package one.hust.edu.cn.service.impl;

import one.hust.edu.cn.dao.ChannelDao;
import one.hust.edu.cn.entities.Channel;
import one.hust.edu.cn.service.ChannelService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ChannelServiceImpl implements ChannelService {
    @Resource
    ChannelDao channelDao;
    @Override
    public List<Channel> getAllChannel() {
        return channelDao.getAllChannel();
    }

    @Override
    public Channel findChannelById(Integer channelId) {
        return channelDao.findChannelById(channelId);
    }

    @Override
    public Channel findChannelByName(String channelName) {
        return channelDao.findChannelByName(channelName);
    }
}
