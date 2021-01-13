package com.hust.keyRD.system.api.service;

import com.hust.keyRD.commons.entities.Record;

import java.util.List;

public interface FabricService {

    /**
     * 调用链码  这里是指使用中心链上org1来进行权限管理
     * @param peers peer list
     * @param channelName channel名
     * @param ccName ccName
     * @param fcn 调用函数
     * @param args 链码调用参数
     * @return 结果
     */
    String invokeChaincode(String peers, String channelName, String ccName,
                           String fcn, List<String> args);


    /**
     * 增加policy
     * @param obj channelName 或 fileId
     * @param permission 权限
     * @param role  角色
     * @param users 用户list
     * @return
     */
    Boolean addPolicy(String obj, String permission, String role, List<String> users);

    /**
     * 更新policy 即更新state <obj+permission, Map<String role, String[] users>的value
     * @param obj channelName 或 fileId
     * @param permission 权限
     * @param func addrole, deleterole, adduser, deleteuser
     * @param role 角色
     * @param users 用户list
     * @return success/fail
     */
    Boolean updatePolicy(String obj, String permission, String func, String role, List<String> users);


    /**
     * 授予 某角色 某些用户 某个channel的（文件）add权限
     * @param dstChannelName 目标channel  授予该channel的增加文件的权限
     * @param role 角色
     * @param username 用户名
     * @return success / throw FabricException
     */
    Boolean grantUserPermission2Add(String dstChannelName, String role, String username);

    /**
     * 授予 某角色 某些用户 某个文件的查看 修改 删除 权限
     * @param_Remove dstChannelName 目标channel  授予该channel文件的权限  由于目前写死了，目标channel只能是channel1
     * @param fileId 文件id
     * @param permission 权限 删改查
     * @param role 角色
     * @param users 用户列表
     * @return success / throw FabricException
     */
    Boolean grantUserPermissionOnFile(String fileId, String permission, String role, List<String> users);


    /**
     * 撤销 某角色 某些用户 某个文件的权限
     * @param dstChannelName 目标channel  授予该channel文件的权限
     * @param fileId 文件id
     * @param permission 权限 删改查
     * @param role 角色
     * @param users 用户列表
     * @return success / throw FabricException
     */
    Boolean revokeUserPermissionOnFile(String dstChannelName, String fileId, String permission, String role, List<String> users);

    /**
     * 撤销 某角色 某些用户 某个channel的（文件）add权限
     * @param dstChannelName 目标channel  授予该channel的增加文件的权限
     * @param role 角色
     * @param username 用户名
     * @return success / throw FabricException
     */
    Boolean revokeUserPermission2Add(String dstChannelName, String role, String username);

    /**
     * 跨链请求权限 操作链第一次上链
     * @param peers peer list
     * @param channelName channel名
     * @param ccName ccName
     * @param fcn 调用函数
     * @param args 链码调用参数
     * @return 交易 tx_id
     */
    String crossAccess(String peers, String channelName, String ccName,
                           String fcn, List<String> args);

    /**
     * 申请创建文件权限  第一次上链
     * @param username 申请用户
     * @param dstChannelName 目标channel 即申请在该目标channel上创建文件
     * @param fileId 文件id
     * @return 交易id 或 空字符串
     */
    String applyForCreateFile(String username, String dstChannelName, String fileId);

    /**
     * 申请读取文件权限  第一次上链
     * @param username 申请用户
     * @param dstChannelName 目标channel 即申请在该目标channel上创建文件
     * @param fileId 文件id
     * @return 交易id 或 throw FabricException
     */
    String applyForReadFile(String username, String dstChannelName, String fileId);

    /**
     * 申请修改文件权限  第一次上链
     * @param username 申请用户
     * @param dstChannelName 目标channel 即申请在该目标channel上创建文件
     * @param fileId 文件id
     * @return 交易id 或 throw FabricException
     */
    String applyForModifyFile(String username, String dstChannelName, String fileId);


    /**
     * 创建文件时 更新链上文件hash值  第二次上链
     * @param fileString 文件字符串
     * @param username 操作用户
     * @param dstChannelName 目标channel 即申请在该目标channel上创建文件
     * @param fileId 文件id
     * @param txId 第一次上链交易id
     * @return
     */
    Record updateForCreateFile(String fileString, String username, String dstChannelName, String fileId, String txId);

    /**
     * 查看文件时 更新链上文件hash值  第二次上链
     * @param fileString 文件字符串
     * @param username 操作用户
     * @param dstChannelName 目标channel 即申请在该目标channel上创建文件
     * @param fileId 文件id
     * @param txId 第一次上链交易id
     * @return
     */
    Record updateForReadFile(String fileString, String username, String dstChannelName, String fileId, String txId);

    /**
     * 修改文件时 更新链上文件hash值  第二次上链
     * @param fileString 文件字符串
     * @param username 操作用户
     * @param dstChannelName 目标channel 即申请在该目标channel上创建文件
     * @param fileId 文件id
     * @param txId 第一次上链交易id
     * @return
     */
    Record updateForModifyFile(String fileString, String username, String dstChannelName, String fileId, String txId);
    

    /**
     * 请求对某个文件的 增删改查 权限  操作链第一次上链 返回交易号
     */

    /**
     * 第二次上链，更新链上数据hash值，实现与链下数据的同步
     * @param peers peer list
     * @param channelName channel名
     * @param ccName ccname
     * @param fcn 调用函数
     * @param args 链码调用参数
     * @param txId 第一次上链的交易号
     * @return 结果
     */
    String dataSyncRecord(String peers, String channelName, String ccName,
                       String fcn, List<String> args, String txId);


    /**
     * 溯源 查找fileId文件最新的交易记录
     * @param fileId 文件id
     * @return 上一次record实例  
     */
    Record traceBackward(String fileId);

    /**
     * 溯源 查找fileId文件txId交易的上一次交易记录  继续溯源的话使用Record.lastTxId和原fileId作为参数 将进行下一次溯源
     * @param fileId 文件id
     * @param txId 交易id
     * @return 上一次record实例
     */
    Record traceBackward(String fileId, String txId);
    
    /**
     * 完成对某个文件的增删改查  第二次上链  返回交易号
     */

    /**
     * 查询权限 如channel1上add的权限  由于权限管理由中心链完成，所以在本例中使用中心链上的org1来查询
     * @param obj channel name
     * @param opt 操作名 如 add
     * @return policy实体
     */
    String getPolicy(String obj, String opt);

    /**
     * 根据交易号查询结构体信息
     * @param txId 交易号（申请权限的交易号）
     * @param order 第几次上链 1/2
     * @return 结构体JSON
     */
    String queryAuthority(String txId, String order);

    String argsTest(String peers, String channelName, String ccName,
                    String fcn, List<String> args);
}
