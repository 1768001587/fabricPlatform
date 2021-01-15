package com.hust.keyRD.commons.vo;

import com.hust.keyRD.commons.entities.DataSample;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SharedDataVO {
    private Integer shareUserId;//分享者的id
    private Integer sharedUserId;//被分享者的id
    private DataSample dataSample;//分享的文件
}
