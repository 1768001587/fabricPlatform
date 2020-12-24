package one.hust.edu.cn.dao;

import one.hust.edu.cn.entities.MyFile;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DataDao {
    void uploadFile(MyFile myFile);
    //获取文件列表
    List<MyFile> getDataList();
    //根据文件id删除文件
    Integer deleteDataById(Integer id);
}
