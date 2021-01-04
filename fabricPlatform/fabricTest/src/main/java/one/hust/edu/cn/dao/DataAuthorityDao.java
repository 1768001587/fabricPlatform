package one.hust.edu.cn.dao;

import one.hust.edu.cn.entities.DataAuthority;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface DataAuthorityDao {
    void addDataAuthority(DataAuthority dataAuthority);
}
