package com.hust.keyRD.system.service.impl;

import com.hust.keyRD.system.dao.DataAuthorityDao;
import com.hust.keyRD.commons.entities.DataAuthority;
import com.hust.keyRD.system.service.DataAuthorityService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DataAuthorityServiceImpl implements DataAuthorityService {
    @Resource
    DataAuthorityDao dataAuthorityDao;
    @Override
    public void addDataAuthority(DataAuthority dataAuthority) {
        dataAuthorityDao.addDataAuthority(dataAuthority);
    }

    @Override
    public DataAuthority findDataAuthorityById(Integer id) {
        return dataAuthorityDao.findDataAuthorityById(id);
    }

    @Override
    public List<DataAuthority> findDataAuthorityByUserId(Integer userId) {
        return dataAuthorityDao.findDataAuthorityByUserId(userId);
    }

    @Override
    public List<DataAuthority> findDataAuthorityByDataId(Integer dataSampleId) {
        return dataAuthorityDao.findDataAuthorityByDataId(dataSampleId);
    }

    @Override
    public List<DataAuthority> findDataAuthorityByUserIdAndDataId(Integer userId,Integer dataSampleId) {
        return dataAuthorityDao.findDataAuthorityByUserIdAndDataId(userId,dataSampleId);
    }

    @Override
    public void editDataAuthority(Integer id, Integer authorityKey) {
        dataAuthorityDao.editDataAuthority(id,authorityKey);
    }

    @Override
    public Integer checkDataAuthority(DataAuthority dataAuthority) {
        return dataAuthorityDao.checkDataAuthority(dataAuthority);
    }

    @Override
    public void addMasterDataAuthority(Integer originUserId, Integer dataSampleId) {
        dataAuthorityDao.addMasterDataAuthority(originUserId,dataSampleId);
    }

    @Override
    public List<DataAuthority> getAllAuthority() {
        return dataAuthorityDao.getAllAuthority();
    }

    @Override
    public Integer deleteDataAuthority(DataAuthority dataAuthority) {
        return dataAuthorityDao.deleteDataAuthority(dataAuthority);
    }
}
