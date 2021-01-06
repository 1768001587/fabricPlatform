package one.hust.edu.cn.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataSample implements Serializable {
    private Integer id;
    private Integer channelId;
    private String data;
    private String dataName;
    private String dataType;
    private Double dataSize;
    private Integer originUserId;
    private Timestamp createdTime;
    private Timestamp modifiedTime;
}
