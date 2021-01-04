package one.hust.edu.cn.controller;

import lombok.extern.slf4j.Slf4j;
import one.hust.edu.cn.entities.Channel;
import one.hust.edu.cn.entities.ChannelAuthority;
import one.hust.edu.cn.entities.CommonResult;
import one.hust.edu.cn.service.ChannelAuthorityService;
import one.hust.edu.cn.service.ChannelService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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

    @PostMapping(value = "/channel/getAddAuthorityChannels")
    public CommonResult getAddAuthorityChannels(@RequestBody Map<String, String> params){
        Integer userId = Integer.valueOf(params.get("userId"));
        List<String> list = channelAuthorityService.getAddAuthorityChannels(userId);
        List<Channel> result = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Channel channel = channelService.findChannelByName(list.get(i));
            result.add(channel);
        }
        return new CommonResult<>(200,"获取所有该用户可上传文件channel成功",result);
    }
}
