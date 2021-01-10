package com.hust.keyRD.system.service.impl;

import com.hust.keyRD.system.dao.DataDao;
import com.hust.keyRD.commons.entities.DataSample;
import com.hust.keyRD.system.service.DataService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DataServiceImpl implements DataService
{
    @Resource
    private DataDao dataDao;

    @Override
    public void uploadFile(DataSample dataSample) {
        dataDao.uploadFile(dataSample);
    }

    @Override
    public List<DataSample> getDataList() {
        return dataDao.getDataList();
    }

    @Override
    public Integer deleteDataById(Integer id) {
        return dataDao.deleteDataById(id);
    }

    @Override
    public DataSample findDataById(Integer dataId) {
        return dataDao.findDataById(dataId);
    }

    @Override
    public void updateFile(DataSample dataSample) {
        dataDao.updateFile(dataSample);
    }

    @Override
    public List<DataSample> getDataListByOriginUserId(Integer originUserId) {
        return dataDao.getDataListByOriginUserId(originUserId);
    }
}
