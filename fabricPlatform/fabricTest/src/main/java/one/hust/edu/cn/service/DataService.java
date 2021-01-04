package one.hust.edu.cn.service;

import one.hust.edu.cn.entities.MyFile;

import java.util.List;

public interface DataService {
    void uploadFile(MyFile myFile);
    //获取文件列表
    List<MyFile> getDataList();
    //根据id删除文件
    Integer deleteDataById(Integer id);
    //根据id获取文件内容
    MyFile findDataById(Integer dataId);
    //更新文件
    void updateFile(MyFile myFile);
}
