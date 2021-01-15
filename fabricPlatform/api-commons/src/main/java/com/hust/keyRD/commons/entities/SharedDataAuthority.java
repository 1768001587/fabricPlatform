package com.hust.keyRD.commons.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SharedDataAuthority {
    private Integer id;
    private Integer shareUserId;
    private Integer sharedUserId;
    private Integer sharedDataId;
    private Integer authorityKey;
}
