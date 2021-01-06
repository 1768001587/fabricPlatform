package one.hust.edu.cn.service;

import one.hust.edu.cn.entities.DataSample;

import java.util.List;

public interface DataService {
    void uploadFile(DataSample dataSample);
    //获取文件列表
    List<DataSample> getDataList();
    //根据id删除文件
    Integer deleteDataById(Integer id);
    //根据id获取文件内容
    DataSample findDataById(Integer dataId);
    //更新文件
    void updateFile(DataSample dataSample);
    //获取文件列表
    List<DataSample> getDataListByOriginUserId(Integer originUserId);
}
