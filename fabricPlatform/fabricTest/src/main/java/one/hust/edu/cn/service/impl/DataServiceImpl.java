package one.hust.edu.cn.service.impl;

import one.hust.edu.cn.dao.DataDao;
import one.hust.edu.cn.entities.MyFile;
import one.hust.edu.cn.service.DataService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DataServiceImpl implements DataService
{
    @Resource
    private DataDao dataDao;

    @Override
    public void uploadFile(MyFile myFile) {
        dataDao.uploadFile(myFile);
    }

    @Override
    public List<MyFile> getDataList() {
        return dataDao.getDataList();
    }

    @Override
    public Integer deleteDataById(Integer id) {
        return dataDao.deleteDataById(id);
    }

    @Override
    public MyFile findDataById(Integer dataId) {
        return dataDao.findDataById(dataId);
    }

    @Override
    public void updateFile(MyFile myFile) {
        dataDao.updateFile(myFile);
    }
}
