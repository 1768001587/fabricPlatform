package one.hust.edu.cn.controller;

import com.auth0.jwt.JWT;
import lombok.extern.slf4j.Slf4j;
import one.hust.edu.cn.entities.Channel;
import one.hust.edu.cn.entities.CommonResult;
import one.hust.edu.cn.myAnnotation.CheckToken;
import one.hust.edu.cn.service.ChannelAuthorityService;
import one.hust.edu.cn.service.ChannelService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class ChannelAuthorityController {
    @Resource
    ChannelAuthorityService channelAuthorityService;
    @Resource
    ChannelService channelService;


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
}
