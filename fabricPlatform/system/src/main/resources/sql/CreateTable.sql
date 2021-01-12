/*
 Navicat Premium Data Transfer

 Source Server         : 区块链53docker
 Source Server Type    : MySQL
 Source Server Version : 50711
 Source Host           : 211.69.198.53:3307
 Source Schema         : Key-R-D-based-on-fabric

 Target Server Type    : MySQL
 Target Server Version : 50711
 File Encoding         : 65001

 Date: 12/01/2021 14:43:17
*/

SET NAMES utf8mb4;
SET
FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for channel
-- ----------------------------
DROP TABLE IF EXISTS `channel`;
CREATE TABLE `channel`
(
    `id`           bigint(11) NOT NULL COMMENT 'channelID',
    `channel_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'channel名称',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for channel_authority
-- ----------------------------
DROP TABLE IF EXISTS `channel_authority`;
CREATE TABLE `channel_authority`
(
    `id`            bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'channel权限id',
    `channel_id`    bigint(11) NOT NULL COMMENT 'channelId',
    `user_id`       bigint(11) NOT NULL COMMENT '用户id',
    `authority_key` bigint(11) NOT NULL COMMENT '权限字段 1代表添加权限',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for data_authority
-- ----------------------------
DROP TABLE IF EXISTS `data_authority`;
CREATE TABLE `data_authority`
(
    `id`             bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户权限id',
    `user_id`        bigint(20) NOT NULL COMMENT '用户id',
    `data_sample_id` bigint(20) NOT NULL COMMENT '模拟数据id',
    `authority_key`  bigint(100) NOT NULL DEFAULT -1 COMMENT '用户权限 1代表查看文件 2代表修改文件 3代表删除文件',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX            `user_id`(`user_id`) USING BTREE,
    INDEX            `data_sample_id`(`data_sample_id`) USING BTREE,
    CONSTRAINT `data_authority_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `data_authority_ibfk_2` FOREIGN KEY (`data_sample_id`) REFERENCES `data_sample` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 203 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for data_sample
-- ----------------------------
DROP TABLE IF EXISTS `data_sample`;
CREATE TABLE `data_sample`
(
    `id`             bigint(20) NOT NULL AUTO_INCREMENT COMMENT '数据id',
    `channel_id`     bigint(11) NULL DEFAULT NULL COMMENT '该文件所属的channelID',
    `data`           text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '模拟数据',
    `data_name`      varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据名称',
    `data_type`      varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据类型',
    `data_size`      double NULL DEFAULT NULL COMMENT '数据大小',
    `origin_user_id` bigint(11) NULL DEFAULT NULL COMMENT '所属人id，对应user_id',
    `created_time`   datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `modified_time`  datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 203 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '模拟数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    `id`             bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `user_name`      varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户账号',
    `password`       varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '密码',
    `fabric_user_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'fabric 用户id',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 121 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_channel_role
-- ----------------------------
DROP TABLE IF EXISTS `user_channel_role`;
CREATE TABLE `user_channel_role`
(
    `id`         bigint(20) NOT NULL COMMENT 'id',
    `user_id`    int(11) NOT NULL COMMENT '用户id',
    `channel_id` int(11) NOT NULL COMMENT 'channelID',
    `role`       varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '该用户在该channel的角色',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET
FOREIGN_KEY_CHECKS = 1;
