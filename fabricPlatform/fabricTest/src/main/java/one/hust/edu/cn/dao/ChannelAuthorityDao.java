package one.hust.edu.cn.dao;

import one.hust.edu.cn.entities.Channel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChannelAuthorityDao {
    List<String> getAddAuthorityChannels(Integer userId);
}
