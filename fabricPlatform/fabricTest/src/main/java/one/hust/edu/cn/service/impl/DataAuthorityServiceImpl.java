package one.hust.edu.cn.service.impl;

import one.hust.edu.cn.dao.DataAuthorityDao;
import one.hust.edu.cn.entities.DataAuthority;
import one.hust.edu.cn.service.DataAuthorityService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class DataAuthorityServiceImpl implements DataAuthorityService {
    @Resource
    DataAuthorityDao dataAuthorityDao;
    @Override
    public void addDataAuthority(DataAuthority dataAuthority) {
        dataAuthorityDao.addDataAuthority(dataAuthority);
    }
}
