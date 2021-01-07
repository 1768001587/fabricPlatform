package one.hust.edu.cn.service.impl;

import lombok.extern.slf4j.Slf4j;
import one.hust.edu.cn.entities.DataAuthority;
import one.hust.edu.cn.service.DataService;
import one.hust.edu.cn.service.FabricService;
import one.hust.edu.cn.service.GrantPermissionService;
import one.hust.edu.cn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @program: fabricPlatform
 * @description: GrantPermissionServiceImpl
 * @author: zwh
 * @create: 2021-01-06 09:25
 **/
@Slf4j
@Service
public class GrantPermissionServiceImpl implements GrantPermissionService {

    @Autowired
    private FabricService fabricService;

    @Autowired
    private DataService dataService;

    @Autowired
    private UserService userService;

    @Override
    public Boolean grantUserPermissionOnFile(DataAuthority dataAuthority) {
        String dstChannelName = "channel" + dataService.findDataById(dataAuthority.getDataSampleId()).getChannelId();
        String fileId = String.valueOf(dataAuthority.getDataSampleId());
        String role = "role1";
        String user = userService.findUserById(dataAuthority.getUserId()).getUsername();
        String permission;
        switch (dataAuthority.getAuthorityKey()) {
            case 1:
                permission = "read";
                break;
            case 2:
                permission = "modify";
                break;
            case 3:
                permission = "delete";
                break;
            default:
                log.info("授权用户权限失败：invalid AuthorityKey");
                return false;
        }
        return fabricService.grantUserPermissionOnFile(dstChannelName, fileId, permission, role, Collections.singletonList(user));
    }

    @Override
    public Boolean revokeUserPermissionOnFile(DataAuthority dataAuthority) {
        String dstChannelName = "channel" + dataService.findDataById(dataAuthority.getDataSampleId()).getChannelId();
        String fileId = String.valueOf(dataAuthority.getDataSampleId());
        String role = "role1";
        String user = userService.findUserById(dataAuthority.getUserId()).getUsername();
        String permission;
        switch (dataAuthority.getAuthorityKey()) {
            case 1:
                permission = "read";
                break;
            case 2:
                permission = "modify";
                break;
            case 3:
                permission = "delete";
                break;
            default:
                log.info("撤销用户权限失败：invalid AuthorityKey");
                return false;
        }
        return fabricService.revokeUserPermissionOnFile(dstChannelName, fileId, permission, role, new ArrayList<String>() {{
            add(user);
        }});
    }
}

