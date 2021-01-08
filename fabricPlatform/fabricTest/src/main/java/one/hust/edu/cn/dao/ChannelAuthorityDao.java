package one.hust.edu.cn.dao;

import one.hust.edu.cn.entities.Channel;
import one.hust.edu.cn.entities.ChannelAuthority;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChannelAuthorityDao {
    //根据用户id获得已授权的通道
    List<String> getAddAuthorityChannels(Integer userId);
    //根据用户名和channelId查找channelAuthority
    List<ChannelAuthority> findChannelAuthorityByUserIdAndChannelId(@Param("userId") Integer userId,@Param("channelId") Integer channelId);
    //添加通道权限
    void addChannelAuthority(ChannelAuthority channelAuthority);
    //撤销通道权限
    Integer deleteChannelAuthority(ChannelAuthority channelAuthority);
    //查找通道权限
    List<ChannelAuthority> findChannelAuthority(ChannelAuthority channelAuthority);
}
