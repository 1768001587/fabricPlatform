package com.hust.keyRD.commons.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllDataUserAuthorityVO {
    private Integer userId;
    private String userName;
    private String channelName;
    private Integer dataId;
    private String dataName;
    private Set<Integer> dataAuthoritySet;

    /**
     * 逗号隔开的字符串转Set
     * @param authSetStr
     * @return
     */
    public static Set<Integer> stringToSet(String authSetStr){
//        String[] strings = authSetStr.split(",");
        return Arrays.stream(authSetStr.split(",")).map(Integer::valueOf).collect(Collectors.toSet());
    }
}
