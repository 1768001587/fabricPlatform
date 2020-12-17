package one.hust.edu.cn.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyFile implements Serializable {
    private Integer id;
    private String filename;
}
