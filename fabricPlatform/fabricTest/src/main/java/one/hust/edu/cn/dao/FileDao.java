package one.hust.edu.cn.dao;

import one.hust.edu.cn.entities.MyFile;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileDao {
    void uploadFile(MyFile myFile);
}
