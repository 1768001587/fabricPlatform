package one.hust.edu.cn.service.impl;

import one.hust.edu.cn.dao.DataAuthorityDao;
import one.hust.edu.cn.entities.DataAuthority;
import one.hust.edu.cn.service.DataAuthorityService;
import org.omg.CORBA.DATA_CONVERSION;
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
