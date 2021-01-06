package one.hust.edu.cn.controller;

import com.auth0.jwt.JWT;
import lombok.extern.slf4j.Slf4j;
import one.hust.edu.cn.entities.*;
import one.hust.edu.cn.myAnnotation.CheckToken;
import one.hust.edu.cn.service.ChannelAuthorityService;
import one.hust.edu.cn.service.ChannelService;
import one.hust.edu.cn.service.UserService;
import one.hust.edu.cn.vo.AllChannelUserVO;
import one.hust.edu.cn.vo.AllDataUserAuthorityVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Slf4j
@RestController
public class ChannelAuthorityController {
    @Resource
    ChannelAuthorityService channelAuthorityService;
    @Resource
    ChannelService channelService;
    @Resource
    UserService userService;


    @CheckToken
    @GetMapping(value = "/channel/getAddAuthorityChannels")
    public CommonResult getAddAuthorityChannels(HttpServletRequest httpServletRequest){
        // 从 http 请求头中取出 token
        String token = httpServletRequest.getHeader("token");
        Integer userId = JWT.decode(token).getClaim("id").asInt();
        List<String> list = channelAuthorityService.getAddAuthorityChannels(userId);
        List<Channel> result = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Channel channel = channelService.findChannelByName(list.get(i));
            result.add(channel);
        }
        return new CommonResult<>(200,"获取所有该用户可上传文件channel成功",result);
    }
    //获取所有权限，返回AllDataUserAuthorityVO类
    @GetMapping(value = "/channel/getAllAuthorityChannels")
    public CommonResult getAllAuthorityChannels() {
        List<User> users = userService.getAllUser();
        List<Channel> channels = channelService.getAllChannel();
        List<AllChannelUserVO> result = new ArrayList<>();
        Set<List<Integer>> set = new HashSet<>();//保存用户Id与对应的文件id
        for (int i = 0; i < users.size(); i++) {
            for (int j = 0; j < channels.size(); j++) {
                List<Integer> list = new ArrayList<>();
                list.add(users.get(i).getId());
                list.add(channels.get(j).getId());
                set.add(list);
            }
        }
        Iterator<List<Integer>> it = set.iterator();
        while (it.hasNext()) {
            List<Integer> tmp = it.next();
            List<ChannelAuthority> channelAuthorities = channelAuthorityService.findChannelAuthorityByUserIdAndChannelId(tmp.get(0),tmp.get(1));
            Set<Integer> authoritySet = new HashSet<>();
            for (int i = 0; i < channelAuthorities.size(); i++) {
                authoritySet.add(channelAuthorities.get(i).getAuthorityKey());
            }
            User user = userService.findUserById(tmp.get(0));
            Channel channel = channelService.findChannelById(tmp.get(1));
            AllChannelUserVO allChannelUserVO = new AllChannelUserVO();
            allChannelUserVO.setUserId(tmp.get(0));
            allChannelUserVO.setUserName(user.getUsername());
            allChannelUserVO.setChannelId(channel.getId());
            allChannelUserVO.setChannelName(channel.getChannelName());
            allChannelUserVO.setChannelAuthoritySet(authoritySet);
            result.add(allChannelUserVO);
        }
        return new CommonResult<>(200,"查找成功",result);
    }
}
