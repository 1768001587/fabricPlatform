package one.hust.edu.cn.service;

import one.hust.edu.cn.entities.DataAuthority;

/**
 * @program: fabricPlatform
 * @description: 通过fabric进行授权service
 * @author: zwh
 * @create: 2021-01-06 09:16
 **/
public interface GrantPermissionService {

    /**
     * 将某文件的某一权限授予用户
     * @param dataAuthority dataAuthority实体
     * @return success/fail
     */
    Boolean grantUserPermissionOnFile(DataAuthority dataAuthority);

    /**
     * 撤销某用户对某文件的某一权限
     * @param dataAuthority dataAuthority实体
     * @return success/fail
     */
    Boolean revokeUserPermissionOnFile(DataAuthority dataAuthority);

}
