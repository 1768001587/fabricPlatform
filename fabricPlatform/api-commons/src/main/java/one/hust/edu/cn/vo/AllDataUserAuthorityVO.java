package one.hust.edu.cn.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllDataUserAuthorityVO {
    private Integer userId;
    private String userName;
    private Integer dataId;
    private String dataName;
    private Set<Integer> dataAuthoritySet;
}
