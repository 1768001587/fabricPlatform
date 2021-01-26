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
 Date: 22/01/2021 12:26:46
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for channel
-- ----------------------------
DROP TABLE IF EXISTS `channel`;
CREATE TABLE `channel`  (
  `id` bigint(11) NOT NULL COMMENT 'channelID',
  `channel_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'channel名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of channel
-- ----------------------------
INSERT INTO `channel` VALUES (1, 'channel1');
INSERT INTO `channel` VALUES (2, 'channel2');

-- ----------------------------
-- Table structure for channel_authority
-- ----------------------------
DROP TABLE IF EXISTS `channel_authority`;
CREATE TABLE `channel_authority`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'channel权限id',
  `channel_id` bigint(11) NOT NULL COMMENT 'channelId',
  `user_id` bigint(11) NOT NULL COMMENT '用户id',
  `authority_key` bigint(11) NOT NULL COMMENT '权限字段 1代表添加权限',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 142 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of channel_authority
-- ----------------------------
INSERT INTO `channel_authority` VALUES (137, 1, 131, 1);
INSERT INTO `channel_authority` VALUES (139, 2, 133, 1);
INSERT INTO `channel_authority` VALUES (140, 1, 132, 2);
INSERT INTO `channel_authority` VALUES (141, 1, 132, 1);

-- ----------------------------
-- Table structure for data_authority
-- ----------------------------
DROP TABLE IF EXISTS `data_authority`;
CREATE TABLE `data_authority`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户权限id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `data_sample_id` bigint(20) NOT NULL COMMENT '模拟数据id',
  `authority_key` bigint(100) NOT NULL DEFAULT -1 COMMENT '用户权限 1代表查看文件 2代表修改文件 3代表删除文件',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `data_sample_id`(`data_sample_id`) USING BTREE,
  CONSTRAINT `data_authority_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `data_authority_ibfk_2` FOREIGN KEY (`data_sample_id`) REFERENCES `data_sample` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of data_authority
-- ----------------------------
INSERT INTO `data_authority` VALUES (19, 132, 9, 1);
INSERT INTO `data_authority` VALUES (20, 132, 9, 2);
INSERT INTO `data_authority` VALUES (21, 131, 9, 1);

-- ----------------------------
-- Table structure for data_sample
-- ----------------------------
DROP TABLE IF EXISTS `data_sample`;
CREATE TABLE `data_sample`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '数据id',
  `channel_id` bigint(11) NULL DEFAULT NULL COMMENT '该文件所属的channelID',
  `data_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件名称',
  `data_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件类型',
  `data_size` double NULL DEFAULT NULL COMMENT '文件大小',
  `mongo_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'mongoDB中的id',
  `origin_user_id` bigint(11) NULL DEFAULT NULL COMMENT '所属人id，对应user_id',
  `created_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modified_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '模拟数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of data_sample
-- ----------------------------
INSERT INTO `data_sample` VALUES (9, 1, 'test4.txt', '.txt文件', 0, '60015d2a7fed220eccd30b38', 132, '2021-01-15 17:15:22', '2021-01-19 11:06:20');

-- ----------------------------
-- Table structure for shared_data_authority
-- ----------------------------
DROP TABLE IF EXISTS `shared_data_authority`;
CREATE TABLE `shared_data_authority`  (
  `id` int(255) NOT NULL AUTO_INCREMENT COMMENT '分享记录id',
  `share_user_id` int(255) NULL DEFAULT NULL COMMENT '分享者id',
  `shared_user_id` int(255) NULL DEFAULT NULL COMMENT '被分享者id',
  `shared_data_id` int(255) NULL DEFAULT NULL COMMENT '分享文件id',
  `authority_key` int(255) NULL DEFAULT NULL COMMENT '权限key 1代表查看',
  `accept_or_not` int(255) NULL DEFAULT NULL COMMENT '是否同意，同意为1，不同意为0，刚开始为不同意',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 35 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of shared_data_authority
-- ----------------------------
INSERT INTO `shared_data_authority` VALUES (34, 132, 131, 9, 1, 1);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `user_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户账号',
  `password` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '密码',
  `channel_id` int(11) NULL DEFAULT NULL COMMENT '该用户所在channel的id',
  `fabric_user_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'fabric 用户id',
  `is_admin` int(255) NULL DEFAULT NULL COMMENT '1代表是管理员，0代表不是管理员',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 137 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (131, 'userE', '123', 1, '0', 0);
INSERT INTO `user` VALUES (132, 'user', '123', 1, '0', 0);
INSERT INTO `user` VALUES (133, 'user2', '123', 2, '0', 0);
INSERT INTO `user` VALUES (134, 'user3', '123', 1, '0', 0);
INSERT INTO `user` VALUES (135, 'admin1', 'admin', 1, '0', 1);
INSERT INTO `user` VALUES (136, 'admin2', 'admin', 2, '0', 1);

-- ----------------------------
-- Table structure for user_channel_role
-- ----------------------------
DROP TABLE IF EXISTS `user_channel_role`;
CREATE TABLE `user_channel_role`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `channel_id` int(11) NOT NULL COMMENT 'channelID',
  `role` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '该用户在该channel的角色',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_channel_role
-- ----------------------------
INSERT INTO `user_channel_role` VALUES (1, 100, 1, '普通用户');
INSERT INTO `user_channel_role` VALUES (2, 1001, 1, 'da');

SET FOREIGN_KEY_CHECKS = 1;