package one.hust.edu.cn.service;

import one.hust.edu.cn.entities.DataAuthority;
import org.apache.ibatis.annotations.Param;

public interface DataAuthorityService {
    void addDataAuthority(DataAuthority dataAuthority);
}
