package one.hust.edu.cn.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import one.hust.edu.cn.entities.DataSample;

import java.io.Serializable;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataUserAuthorityVO implements Serializable {
    private DataSample dataSample;
    private Set<Integer> authoritySet;
}
