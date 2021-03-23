package com.hust.keyRD.commons.vo;

import com.hust.keyRD.commons.entities.Channel;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @program: system
 * @description: 用户对channel内文件的权限
 * @author: zwh
 * @create: 2021-03-20 18:02
 **/
@Data
public class UserInnerDataVO {
    private Integer id;
    private Integer channelId;
    private String channelName;
    private String mongoId;
    private String dataName;
    private String dataType;
    private Double dataSize;
    private Integer originUserId;
    private Timestamp createdTime;
    private Timestamp modifiedTime;
    private Set<Integer> dataAuthoritySet;
    private Set<Channel> pushChannelSet; 

    /**
     * 逗号隔开的字符串转Set
     * @param authSetStr
     * @return
     */
    public static Set<Integer> stringToSet(String authSetStr){
        if(authSetStr == null || authSetStr.isEmpty()){
            return new HashSet<>();
        }
        return Arrays.stream(authSetStr.split(",")).map(Integer::valueOf).collect(Collectors.toSet());
    }
}
