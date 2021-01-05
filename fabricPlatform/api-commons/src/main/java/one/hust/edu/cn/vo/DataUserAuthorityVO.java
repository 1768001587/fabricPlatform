package one.hust.edu.cn.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import one.hust.edu.cn.entities.MyFile;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataUserAuthorityVO implements Serializable {
    private MyFile myFile;
    private Set<Integer> authoritySet;
}
