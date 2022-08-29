/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.1.101
 Source Server Type    : MySQL
 Source Server Version : 50736
 Source Host           : 192.168.1.101:3306
 Source Schema         : seeds_admin

 Target Server Type    : MySQL
 Target Server Version : 50736
 File Encoding         : 65001

 Date: 29/08/2022 16:13:02
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `dict_type_id` bigint(20) DEFAULT NULL COMMENT '字典类型ID',
  `dict_label` varchar(32) DEFAULT NULL COMMENT '字典标签',
  `dict_value` varchar(32) DEFAULT NULL COMMENT '字典值',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `created_by` bigint(20) DEFAULT NULL COMMENT '创建者',
  `created_at` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `updated_by` bigint(20) DEFAULT NULL COMMENT '修改者',
  `updated_at` bigint(20) DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 COMMENT='数据字典内容';

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
BEGIN;
INSERT INTO `sys_dict_data` (`id`, `dict_type_id`, `dict_label`, `dict_value`, `remark`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (1, 1, '裤子', '1', NULL, 1, 1, NULL, 1, NULL);
INSERT INTO `sys_dict_data` (`id`, `dict_type_id`, `dict_label`, `dict_value`, `remark`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (2, 1, '帽子', '2', NULL, 2, 1, NULL, 1, NULL);
INSERT INTO `sys_dict_data` (`id`, `dict_type_id`, `dict_label`, `dict_value`, `remark`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (3, 1, '衣服', '3', NULL, 3, 1, NULL, 1, NULL);
INSERT INTO `sys_dict_data` (`id`, `dict_type_id`, `dict_label`, `dict_value`, `remark`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (4, 2, 'liren', '1', NULL, 1, 1, NULL, 1, NULL);
INSERT INTO `sys_dict_data` (`id`, `dict_type_id`, `dict_label`, `dict_value`, `remark`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (5, 2, '王者2', '4', NULL, 2, 1, NULL, 17, 1661134782868);
INSERT INTO `sys_dict_data` (`id`, `dict_type_id`, `dict_label`, `dict_value`, `remark`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (6, 2, '王者', '4', NULL, NULL, 17, 1661131746368, 17, 1661134000434);
INSERT INTO `sys_dict_data` (`id`, `dict_type_id`, `dict_label`, `dict_value`, `remark`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (9, 2, NULL, '5', NULL, NULL, 17, 1661134173495, 17, 1661134173495);
INSERT INTO `sys_dict_data` (`id`, `dict_type_id`, `dict_label`, `dict_value`, `remark`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (10, 2, NULL, '5', NULL, NULL, 17, 1661134178484, 17, 1661134178484);
INSERT INTO `sys_dict_data` (`id`, `dict_type_id`, `dict_label`, `dict_value`, `remark`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (11, 2, '王者5', '4', NULL, NULL, 17, 1661134219154, 17, 1661135285266);
INSERT INTO `sys_dict_data` (`id`, `dict_type_id`, `dict_label`, `dict_value`, `remark`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (12, 2, '王者6', '4', NULL, NULL, 17, 1661134561369, 17, 1661135357370);
INSERT INTO `sys_dict_data` (`id`, `dict_type_id`, `dict_label`, `dict_value`, `remark`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (13, 2, '大话3', '6', NULL, NULL, 17, 1661135610480, 17, 1661135610480);
INSERT INTO `sys_dict_data` (`id`, `dict_type_id`, `dict_label`, `dict_value`, `remark`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (14, 2, '梦幻', '7', NULL, NULL, 17, 1661136958204, 17, 1661136958204);
COMMIT;

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `dict_code` varchar(32) DEFAULT NULL COMMENT '字典编码',
  `dict_name` varchar(32) DEFAULT NULL COMMENT '字典名称',
  `parent_code` varchar(32) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `sort` int(11) DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL COMMENT '创建者',
  `created_at` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `updated_by` bigint(20) DEFAULT NULL COMMENT '修改者',
  `updated_at` bigint(20) DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='数据字典类型表';

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
BEGIN;
INSERT INTO `sys_dict_type` (`id`, `dict_code`, `dict_name`, `parent_code`, `remark`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (1, 'clothes_type', '服装类型', NULL, '1', NULL, 1, 34343, 1, 839274409);
INSERT INTO `sys_dict_type` (`id`, `dict_code`, `dict_name`, `parent_code`, `remark`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (2, 'game_type', '游戏类型', NULL, '1', NULL, 1, NULL, 1, NULL);
COMMIT;

-- ----------------------------
-- Table structure for sys_file
-- ----------------------------
DROP TABLE IF EXISTS `sys_file`;
CREATE TABLE `sys_file` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `object_name` varchar(100) DEFAULT NULL COMMENT '对象名',
  `bucket_name` varchar(200) DEFAULT NULL COMMENT '桶名',
  `file_name` varchar(100) DEFAULT NULL COMMENT '文件名',
  `type` varchar(50) DEFAULT NULL COMMENT '类型',
  `file_size` bigint(20) DEFAULT NULL COMMENT '文件大小',
  `created_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `updated_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  `created_at` bigint(20) DEFAULT NULL COMMENT '上传时间',
  `updated_at` bigint(20) DEFAULT NULL COMMENT '更新时间',
  `delete_flag` tinyint(1) DEFAULT '0' COMMENT '删除标记  1：已删除   0：未删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=166 DEFAULT CHARSET=utf8 COMMENT='文件管理表';

-- ----------------------------
-- Records of sys_file
-- ----------------------------
BEGIN;
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (1, '91a57a9b17f24971b4a3558b7958de37.jpg', 'admin', '1659514544433.jpg', 'nft', 80804, 2, 2, 1659515862181, 1659516077923, 1);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (2, '41062c498ca24e1fbc417eca3588828e.jpg', 'admin', '1659514544433.jpg', 'nft', 80804, 2, 2, 1659516213725, 1659516213725, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (3, 'user/bc761c7fd7e2402a887b4c857119c01c.jpg', 'admin', '1660298840164.jpg', 'user', 1517, 2, 2, 1660298859436, 1660298859436, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (4, 'NFT/a5a14c0b36af479481bfc9b26ff73b8c.jpg', 'admin', '23333333.jpg', 'NFT', 42848, 2, 2, 1661243338666, 1661243338666, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (5, 'NFT/f25b40418d9045edba16c2da784a35a8.png', 'admin', '图片 2 Copy 2@1x.png', 'NFT', 29939, 9, 9, 1661243551915, 1661243551915, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (6, 'NFT/69e777a7e07143559243f66f94681e08.png', 'admin', '图片 2 Copy 3@1x.png', 'NFT', 35200, 9, 9, 1661243592062, 1661243592062, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (7, 'NFT/3bb77b83544842f29183d6ee6999c936.png', 'admin', '图片 2 Copy 3@1x.png', 'NFT', 35200, 9, 9, 1661243614065, 1661243614065, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (8, 'NFT/605ab587eea1410f892f8a46d800a67a.mp4', 'admin', '1d0f6fbfe0f291ff6f59fe9c1a5adac7.mp4', 'NFT', 471604, 9, 9, 1661243875443, 1661243875443, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (9, 'NFT/6bb2c92472254d39b0748a8659b77c47.mp4', 'admin', 'afe3e23a8c91a519af6415ab0dcb03da.mp4', 'NFT', 53316, 9, 9, 1661244030801, 1661244030801, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (10, 'NFT/43cda12a0d8245fbbd7265f412a58aeb.mp4', 'admin', '1d0f6fbfe0f291ff6f59fe9c1a5adac7.mp4', 'NFT', 471604, 9, 9, 1661244202077, 1661244202077, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (11, 'NFT/f5381bb8561a4563bb4b70f2fa31b15a.png', 'admin', '图片 2 Copy 3@1x.png', 'NFT', 35200, 9, 9, 1661244497745, 1661244497745, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (12, 'NFT/f93c300be59f48bcb3c1064caf5b6c51.png', 'admin', '图片 2 Copy 2@1x.png', 'NFT', 29939, 9, 9, 1661244587116, 1661244587116, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (13, 'NFT/e542f14060994a50b5d6abe90dc7e4a7.png', 'admin', '图片 2 Copy 4@1x.png', 'NFT', 34204, 9, 9, 1661244592870, 1661244592870, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (14, 'NFT/57181ca0d96f450d80ed11a1475aec33.png', 'admin', '图片 2 Copy 2@1x.png', 'NFT', 29939, 9, 9, 1661244625009, 1661244625009, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (15, 'NFT/5be2b16a0c5842b0864e5ea36bf5940c.mp4', 'admin', '1d0f6fbfe0f291ff6f59fe9c1a5adac7.mp4', 'NFT', 471604, 9, 9, 1661244686361, 1661244686361, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (16, 'NFT/764338dd074f4293bac3a0f0f53e6678.png', 'admin', '图片 2 Copy 2@1x.png', 'NFT', 30474, 9, 9, 1661244823036, 1661244823036, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (17, 'NFT/ef3a8c652c444fe597c2cfe65b62e727.jpg', 'admin', '4.jpg', 'NFT', 10641, 9, 9, 1661318672225, 1661318672225, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (18, 'NFT/592bf59ba50a4743a5ca98c63bbbd40d.png', 'admin', '图片 2@1x (2).png', 'NFT', 1190, 9, 9, 1661318677995, 1661318677995, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (19, 'NFT/cc87133373324fa1acdb850c5696dc2c.png', 'admin', '图片@1x (4).png', 'NFT', 4367, 9, 9, 1661318684175, 1661318684175, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (20, 'NFT/7b0c1c56cc434d10b0060d2713689212.mp4', 'admin', '1d0f6fbfe0f291ff6f59fe9c1a5adac7.mp4', 'NFT', 471604, 9, 9, 1661318694140, 1661318694140, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (21, 'NFT/a9b8cf8f67624fd98b3d76997e137e57.jpg', 'admin', '3.jpg', 'NFT', 13567, 9, 9, 1661319229785, 1661319229785, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (22, 'NFT/5520cdde30bd4bfdb56c62fa502e085a.jpg', 'admin', '5.jpg', 'NFT', 10980, 9, 9, 1661319233119, 1661319233119, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (23, 'NFT/d5e2595ded8a4ce5a51a2ece324cbaf7.mp4', 'admin', '1d0f6fbfe0f291ff6f59fe9c1a5adac7.mp4', 'NFT', 471604, 9, 9, 1661319264104, 1661319264104, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (24, 'NFT/0e2b762589f44c11abbe2b55721f7940.jpg', 'admin', '2.jpg', 'NFT', 16190, 9, 9, 1661319374208, 1661319374208, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (25, 'NFT/7a2d7b8410634add8dae37c94ee00397.jpg', 'admin', '6.jpg', 'NFT', 10169, 9, 9, 1661319408103, 1661319408103, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (26, 'NFT/e681dc4c582745468b6c2ed82cfccf5d.jpg', 'admin', '3.jpg', 'NFT', 13567, 9, 9, 1661319548418, 1661319548418, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (27, 'NFT/eca4a0e7c95b488a8a96601110bd1312.jpg', 'admin', '5.jpg', 'NFT', 10980, 9, 9, 1661319638034, 1661319638034, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (28, 'NFT/aaba27bfb4eb4d65acceffa4fdb31d1d.jpg', 'admin', '4.jpg', 'NFT', 10641, 9, 9, 1661319983095, 1661319983095, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (29, 'NFT/2396827024314196a883fc308e6fd68d.jpg', 'admin', '3.jpg', 'NFT', 13567, 9, 9, 1661320115333, 1661320115333, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (30, 'NFT/fa2f2a0fb7a54e18acc39884ef82404c.jpg', 'admin', '5.jpg', 'NFT', 10980, 9, 9, 1661320134458, 1661320134458, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (31, 'NFT/a6c5179a3dab47d1a340f6387c9fde19.mp4', 'admin', '1d0f6fbfe0f291ff6f59fe9c1a5adac7.mp4', 'NFT', 471604, 9, 9, 1661320200406, 1661320200406, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (32, 'NFT/5e9648cd321f42aabd89b67e302763e7.mp4', 'admin', '1d0f6fbfe0f291ff6f59fe9c1a5adac7.mp4', 'NFT', 471604, 9, 9, 1661320446647, 1661320446647, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (33, 'NFT/59b9af7d3c9f43aa86a464a223fe7aa0.jpg', 'admin', '3.jpg', 'NFT', 13567, 9, 9, 1661320852415, 1661320852415, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (34, 'NFT/811bbccdd9a94fb49d37922ff1c997e6.jpg', 'admin', '3.jpg', 'NFT', 13567, 9, 9, 1661321164843, 1661321164843, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (35, 'NFT/f3a6fdf50bc44577a8bbd10b1413cc2d.jpg', 'admin', '3.jpg', 'NFT', 13567, 9, 9, 1661321211775, 1661321211775, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (36, 'NFT/ed43a1f1f7f9475dbf257ae8a663b895.jpg', 'admin', '5.jpg', 'NFT', 10980, 9, 9, 1661321239039, 1661321239039, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (37, 'NFT/fa7120e2c5264a3baed9d1daa91f6ad8.jpg', 'admin', '4.jpg', 'NFT', 10641, 9, 9, 1661321483462, 1661321483462, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (38, 'NFT/45a853c502894c65ab8a6cafcdc96517.mp4', 'admin', '1d0f6fbfe0f291ff6f59fe9c1a5adac7.mp4', 'NFT', 471604, 9, 9, 1661321493218, 1661321493218, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (39, 'NFT/95665c44843b40b88cafa4a9fed6a797.mp4', 'admin', '1d0f6fbfe0f291ff6f59fe9c1a5adac7.mp4', 'NFT', 471604, 9, 9, 1661321658362, 1661321658362, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (40, 'NFT/d0a454b122d846578203eb3ac4347eff.jpg', 'admin', '3.jpg', 'NFT', 13567, 9, 9, 1661321737453, 1661321737453, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (41, 'NFT/ddd4a00c8bb24b949cc0121ffc6715a7.jpg', 'admin', '3.jpg', 'NFT', 13567, 9, 9, 1661321847007, 1661321847007, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (42, 'NFT/f7201082a9ed4c929a7faa27f709ffae.mp4', 'admin', '1d0f6fbfe0f291ff6f59fe9c1a5adac7.mp4', 'NFT', 471604, 9, 9, 1661321854242, 1661321854242, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (43, 'NFT/6c50663ded0d4967a4896405109172b9.jpg', 'admin', '2.jpg', 'NFT', 16190, 9, 9, 1661321918834, 1661321918834, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (44, 'NFT/2c889f41319f4d9da1beaae28e6935e7.mp4', 'admin', '1d0f6fbfe0f291ff6f59fe9c1a5adac7.mp4', 'NFT', 471604, 9, 9, 1661321931402, 1661321931402, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (45, 'NFT/01082945dac145fd82d1ecd3bca2ceba.jpg', 'admin', '2.jpg', 'NFT', 16190, 9, 9, 1661322003391, 1661322003391, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (46, 'NFT/913d3700a9c245058059471b84db02fc.jpg', 'admin', '3.jpg', 'NFT', 13567, 9, 9, 1661322050703, 1661322050703, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (47, 'NFT/465ecd1e6233476cbd238809170d753d.jpg', 'admin', '3.jpg', 'NFT', 13567, 9, 9, 1661322063705, 1661322063705, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (48, 'NFT/e98d20f9c82541af996c12e4d367097c.mp4', 'admin', '1d0f6fbfe0f291ff6f59fe9c1a5adac7.mp4', 'NFT', 471604, 9, 9, 1661322446064, 1661322446064, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (49, 'NFT/5ff3575a57e546f99f4ea50469164ff4.mp4', 'admin', '1d0f6fbfe0f291ff6f59fe9c1a5adac7.mp4', 'NFT', 471604, 9, 9, 1661322508879, 1661322508879, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (50, 'NFT/839754c2e39548359c292fc697a6f4dd.mp4', 'admin', '1d0f6fbfe0f291ff6f59fe9c1a5adac7.mp4', 'NFT', 471604, 9, 9, 1661322586009, 1661322586009, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (51, 'NFT/874b501326a04421a5a9a2053cfd1ad1.mp4', 'admin', '1d0f6fbfe0f291ff6f59fe9c1a5adac7.mp4', 'NFT', 471604, 9, 9, 1661322640841, 1661322640841, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (52, 'NFT/a77fcb3b1e4a43839b5632ba35692bbb.mp4', 'admin', '1d0f6fbfe0f291ff6f59fe9c1a5adac7.mp4', 'NFT', 471604, 9, 9, 1661322755229, 1661322755229, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (53, 'NFT/dde9a8fa442c48bea126109c8982088b.jpg', 'admin', '2.jpg', 'NFT', 16190, 9, 9, 1661322802749, 1661322802749, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (54, 'NFT/e8eb4d540ffb42129aec58030d1ba8a5.jpg', 'admin', '2.jpg', 'NFT', 16190, 9, 9, 1661322832732, 1661322832732, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (55, 'NFT/6f7435d4cdbd43858e74928a35ec2982.jpg', 'admin', '3.jpg', 'NFT', 13567, 9, 9, 1661322841193, 1661322841193, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (56, 'NFT/a2ad3d1f0bba43b6a70cc50bfb70435f.jpg', 'admin', '4.jpg', 'NFT', 10641, 9, 9, 1661322846145, 1661322846145, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (57, 'NFT/02963cbcccae47c6ac1e69e917a33825.mp4', 'admin', '1d0f6fbfe0f291ff6f59fe9c1a5adac7.mp4', 'NFT', 471604, 9, 9, 1661322853019, 1661322853019, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (58, 'NFT/155b22179e4843538d0bdd97e9398a9a.jpg', 'admin', '4.jpg', 'NFT', 23093, 9, 9, 1661326750326, 1661326750326, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (59, 'NFT/a0de8dfa61714fd690b4b8c404873513.jpg', 'admin', '4.jpg', 'NFT', 23093, 9, 9, 1661326784718, 1661326784718, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (60, 'NFT/608d65979c6c4caf8570f0c49c46277f.jpg', 'admin', '4.jpg', 'NFT', 23093, 9, 9, 1661326835936, 1661326835936, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (61, 'NFT/19fe1747a9794fa2ae47931a70abda04.jpg', 'admin', '5.jpg', 'NFT', 24214, 9, 9, 1661327115050, 1661327115050, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (62, 'NFT/ed937720b0874f559d4152c4b31b42b5.jpg', 'admin', '4.jpg', 'NFT', 23093, 9, 9, 1661327200007, 1661327200007, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (63, 'NFT/82e73c70971645b78f504d5d11470643.jpg', 'admin', '4.jpg', 'NFT', 23093, 9, 9, 1661327256028, 1661327256028, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (64, 'NFT/f8300c0f90794ebeba38ab5bb722b5eb.jpg', 'admin', '5.jpg', 'NFT', 24214, 9, 9, 1661327331647, 1661327331647, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (65, 'NFT/6145a23d8091410aa75053eaeb0af1a9.jpg', 'admin', '4.jpg', 'NFT', 23093, 9, 9, 1661327346334, 1661327346334, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (66, 'NFT/b917b3cd893847df98edf926afa13e97.jpg', 'admin', '5.jpg', 'NFT', 24214, 9, 9, 1661329307432, 1661329307432, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (67, 'NFT/04ee5da5b6154547869d251c806f9e69.', 'admin', 'blob', 'NFT', 39848, 9, 9, 1661329566286, 1661329566286, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (68, 'NFT/b8262d4f021644eba0ea9b2416c2545b.jpg', 'admin', '4.jpg', 'NFT', 23093, 9, 9, 1661329622053, 1661329622053, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (69, 'NFT/4fdb2f9e8b03447e866803d28e0da6e5.jpg', 'admin', '5.jpg', 'NFT', 10980, 9, 9, 1661330501489, 1661330501489, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (70, 'NFT/96b5df79e50b44ed8584fa3955df55f8.mp4', 'admin', '1d0f6fbfe0f291ff6f59fe9c1a5adac7.mp4', 'NFT', 471604, 9, 9, 1661330508446, 1661330508446, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (71, 'NFT/2f12444c36c449fd8144c86135f7a427.mp4', 'admin', '1d0f6fbfe0f291ff6f59fe9c1a5adac7.mp4', 'NFT', 471604, 9, 9, 1661331012665, 1661331012665, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (72, 'NFT/f5f146b653a64aa386c77afcec7b77f0.jpg', 'admin', '3.jpg', 'NFT', 13567, 9, 9, 1661331017297, 1661331017297, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (73, 'NFT/31607636efad4c0baacfc4d92214a3df.jpg', 'admin', '3.jpg', 'NFT', 13567, 9, 9, 1661331419467, 1661331419467, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (74, 'NFT/ba6a747e12a640efa3349ba010c4d6eb.mp4', 'admin', '1d0f6fbfe0f291ff6f59fe9c1a5adac7.mp4', 'NFT', 471604, 9, 9, 1661331425844, 1661331425844, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (75, 'NFT/ebafab5f91af4284abf3f0bc2f16e796.mp4', 'admin', '1d0f6fbfe0f291ff6f59fe9c1a5adac7.mp4', 'NFT', 471604, 9, 9, 1661331580705, 1661331580705, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (76, 'NFT/bac84af8fb51416d9c4accd97c27f9e3.png', 'admin', '图片 2@1x (1)的副本.png', 'NFT', 98980, 9, 9, 1661331587474, 1661331587474, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (77, 'NFT/077fc99a792d4546b433a5931fff29b6.png', 'admin', '图片 2@1x (1)的副本2.png', 'NFT', 98980, 9, 9, 1661332052622, 1661332052622, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (78, 'NFT/b25dae8b9a2643dea27bdb1e95c4c9c0.mp4', 'admin', '1d0f6fbfe0f291ff6f59fe9c1a5adac7.mp4', 'NFT', 471604, 9, 9, 1661332059354, 1661332059354, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (79, 'NFT/ca5d6b976ebb42f596c0a5b98d01e016.png', 'admin', '图片 2@1x (1)的副本.png', 'NFT', 39848, 9, 9, 1661332081421, 1661332081421, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (80, 'NFT/a7ebaa81320c424b8bef3a94d4113564.png', 'admin', '图片 2@1x (1)的副本2.png', 'NFT', 98980, 9, 9, 1661332223289, 1661332223289, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (81, 'NFT/002fb98085234c518a8a3c337b29879a.mp4', 'admin', '1d0f6fbfe0f291ff6f59fe9c1a5adac7.mp4', 'NFT', 471604, 9, 9, 1661332229681, 1661332229681, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (82, 'NFT/1d9737f751234ccaae727e216630c88b.png', 'admin', '图片 2@1x (1)的副本2.png', 'NFT', 39848, 9, 9, 1661332463389, 1661332463389, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (83, 'NFT/e9c2991571cf4c1c84feb5b58684abcc.png', 'admin', '图片 2@1x (1)的副本2.png', 'NFT', 39848, 9, 9, 1661332562330, 1661332562330, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (84, 'NFT/63bd732e4d2d46969b5cc9b42423106f.png', 'admin', 'NFT_e9c2991571cf4c1c84feb5b58684abcc.png', 'NFT', 39848, 9, 9, 1661333029703, 1661333029703, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (85, 'NFT/ba8cfb6479c749299c23a3cdf507b84f.png', 'admin', 'NFT_63bd732e4d2d46969b5cc9b42423106f.png', 'NFT', 98980, 9, 9, 1661334181412, 1661334181412, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (86, 'NFT/abac4f32db8440b68ac9c116a3631c9a.mp4', 'admin', '1d0f6fbfe0f291ff6f59fe9c1a5adac7.mp4', 'NFT', 471604, 9, 9, 1661334188443, 1661334188443, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (87, 'NFT/929311a8bd60443e8ac935dd7d8d2174.jpg', 'admin', '23333333.jpg', 'NFT', 42848, 2, 2, 1661334247847, 1661334247847, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (88, 'NFT/325eb08c6b14409898473b068acbb29d.png', 'admin', 'NFT_63bd732e4d2d46969b5cc9b42423106f.png', 'NFT', 39848, 9, 9, 1661334248597, 1661334248597, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (89, 'NFT/1bbd82976a2945b5b2699465e77e8e7a.mp4', 'admin', '1d0f6fbfe0f291ff6f59fe9c1a5adac7.mp4', 'NFT', 471604, 9, 9, 1661334882887, 1661334882887, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (90, 'NFT/7b8a81561ea64a0fbf5a7a4975cb81ea.png', 'admin', '图片 2@1x (1)的副本2.png', 'NFT', 98980, 9, 9, 1661335079048, 1661335079048, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (91, 'NFT/b4408d7f667d40d883d1ddc7f08f66ea.mp4', 'admin', '1d0f6fbfe0f291ff6f59fe9c1a5adac7.mp4', 'NFT', 471604, 9, 9, 1661335085076, 1661335085076, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (92, 'NFT/83bb789db53546bc85a8f78ff5843f06.png', 'admin', '图片 2@1x (1)的副本2.png', 'NFT', 39848, 9, 9, 1661335182729, 1661335182729, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (93, 'NFT/c74ec0b1e3ad400da88a72718eeaca36.png', 'admin', 'NFT_63bd732e4d2d46969b5cc9b42423106f.png', 'NFT', 39848, 9, 9, 1661335248892, 1661335248892, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (94, 'NFT/1e4512a97f8e4d17b3397dd7ed055627.png', 'admin', 'NFT_63bd732e4d2d46969b5cc9b42423106f.png', 'NFT', 98980, 9, 9, 1661335517797, 1661335517797, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (95, 'NFT/454809a4b5f844ad8821f48108bb94ae.mp4', 'admin', '1d0f6fbfe0f291ff6f59fe9c1a5adac7.mp4', 'NFT', 471604, 9, 9, 1661335522455, 1661335522455, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (96, 'NFT/6b4132ba45a241d3a1596d7183a1924f.jpg', 'admin', '4.jpg', 'NFT', 10641, 9, 9, 1661390992969, 1661390992969, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (97, 'NFT/cdbebb0acfb3414e98c9a2ebd3587cca.mp4', 'admin', '1d0f6fbfe0f291ff6f59fe9c1a5adac7.mp4', 'NFT', 471604, 9, 9, 1661390999812, 1661390999812, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (98, 'NFT/f28ff5acb53542f39c8013527eda2009.jpg', 'admin', '2.jpg', 'NFT', 16190, 9, 9, 1661392010592, 1661392010592, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (99, 'NFT/337cd40ff8d24753b9d47d5e46af53b6.mp4', 'admin', '1d0f6fbfe0f291ff6f59fe9c1a5adac7.mp4', 'NFT', 471604, 9, 9, 1661392017181, 1661392017181, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (100, 'NFT/c1ddd7ab386242ad8acd1c280384a32c.jpg', 'admin', '2.jpg', 'NFT', 16190, 9, 9, 1661392601526, 1661392601526, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (101, 'NFT/9c67efc974514d1fa06aeec7291e90d7.mp4', 'admin', '1d0f6fbfe0f291ff6f59fe9c1a5adac7.mp4', 'NFT', 471604, 9, 9, 1661392606978, 1661392606978, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (102, 'NFT/89b99a45cf454e6ba1bcd9197dbf405e.jpg', 'admin', '23333333.jpg', 'NFT', 42848, 2, 2, 1661395170372, 1661395170372, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (103, 'NFT/10be40f2808f445086a36db873a7da3d.jpg', 'admin', 'it_takes_two.jpg', 'NFT', 36496, 9, 9, 1661395181658, 1661395181658, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (104, 'NFT/53c40daf650e40cdb4cfe9f3930d7531.mp4', 'admin', '1d0f6fbfe0f291ff6f59fe9c1a5adac7.mp4', 'NFT', 471604, 9, 9, 1661395395126, 1661395395126, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (105, 'NFT/39449beb611d4e0b8ffd8d335e887472.', 'admin', 'blob', 'NFT', 39848, 9, 9, 1661405256395, 1661405256395, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (106, 'NFT/6807b5b27cf64a3fabbfed662041faca.', 'admin', 'blob', 'NFT', 39848, 9, 9, 1661405334218, 1661405334218, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (107, 'NFT/6d3b443e61e244679e9266b86371a1db.', 'admin', 'blob', 'NFT', 307202, 9, 9, 1661405401321, 1661405401321, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (108, 'NFT/2ec173d651ca45c1b95426e01673ac40.', 'admin', 'blob', 'NFT', 307202, 9, 9, 1661405784465, 1661405784465, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (109, 'NFT/1aaeb7f5975740d688d9282746c5c9a8.', 'admin', 'blob', 'NFT', 307202, 9, 9, 1661405865720, 1661405865720, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (110, 'NFT/28824433e06d449ca0e6bbb2d565eabb.', 'admin', 'blob', 'NFT', 307202, 9, 9, 1661405912613, 1661405912613, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (111, 'NFT/95af4cdbf526418c8b4f0c6aee48e059.', 'admin', 'blob', 'NFT', 307202, 9, 9, 1661405958645, 1661405958645, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (112, 'NFT/bed53afa01ba41f6860e459a4fa4d63e.', 'admin', 'blob', 'NFT', 307202, 9, 9, 1661405999555, 1661405999555, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (113, 'NFT/2a4f19589aaf40cfbc683517acd618cd.', 'admin', 'blob', 'NFT', 307202, 9, 9, 1661406031121, 1661406031121, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (114, 'NFT/f60de8720a6446eb914d399fea833e2a.', 'admin', 'blob', 'NFT', 307202, 9, 9, 1661406048271, 1661406048271, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (115, 'NFT/1f5a3ae047174f0d817219f6bd222975.', 'admin', 'blob', 'NFT', 307202, 9, 9, 1661406065151, 1661406065151, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (116, 'NFT/b7e5e3e942a94ca8bef00dfd78b69fa8.', 'admin', 'blob', 'NFT', 306442, 9, 9, 1661406097716, 1661406097716, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (117, 'NFT/b268615308a74c9f9b60a2bd4f36b318.png', 'admin', 'file.png', 'NFT', 306442, 9, 9, 1661406204035, 1661406204035, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (118, 'NFT/0f8ef5f8a9da4ae68702f531ada35bdb.jpg', 'admin', 'it_takes_two.jpg', 'NFT', 40946, 9, 9, 1661406856767, 1661406856767, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (119, 'NFT/4b6a362bfbef4f70a04253525128be89.jpg', 'admin', '3.jpg', 'NFT', 31342, 9, 9, 1661406882533, 1661406882533, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (120, 'NFT/1598a71743d743c6b36a9fb8d4c676de.jpg', 'admin', '5.jpg', 'NFT', 24214, 9, 9, 1661406887769, 1661406887769, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (121, 'NFT/cefeeba7d3784038a198c1e40405968d.jpeg', 'admin', '86d6277f9e2f070828382b712270af99a9014c08543f.jpeg', 'NFT', 19421, 9, 9, 1661407074782, 1661407074782, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (122, 'NFT/9de8812f7481419282938f4310c091e2.mp4', 'admin', '1d0f6fbfe0f291ff6f59fe9c1a5adac7.mp4', 'NFT', 471604, 9, 9, 1661407191297, 1661407191297, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (123, 'NFT/effccf89c2344bc2bef13301d570c408.jpg', 'admin', 'it_takes_two.jpg', 'NFT', 40946, 9, 9, 1661407829943, 1661407829943, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (124, 'NFT/5d8c77d7531e416eaced586ce5e34a1a.png', 'admin', '登录-正常显示.png', 'NFT', 534022, 1, 1, 1661412766323, 1661412766323, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (125, 'NFT/d20e0fed66a442db8587a04a8cc5cdfa.png', 'admin', '大话.png', 'NFT', 41023, 1, 1, 1661413420429, 1661413420429, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (126, 'NFT/36d5ed8677cc4bc4a0cad37e31b708fc.mp4', 'admin', '02-01 区块链是什么.mp4', 'NFT', 92825420, 10, 10, 1661413887409, 1661413887409, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (127, 'NFT/6965053ef177422caa4b2b455f169261.mp4', 'admin', '02-03 区块链应用场景.mp4', 'NFT', 47214380, 10, 10, 1661413933662, 1661413933662, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (128, 'NFT/593fba2aa97640128dc4d5b2f0466252.mp4', 'admin', '02-02 数字货币.mp4', 'NFT', 22918012, 10, 10, 1661414089249, 1661414089249, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (129, 'NFT/1fc1a2e5ed58456fb6cb0a3450799a6a.jpg', 'admin', 'it_takes_two.jpg', 'NFT', 36496, 9, 9, 1661414252179, 1661414252179, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (130, 'NFT/1e9b7a2f6ca04dfca32555121acc1104.jpg', 'admin', 'it_takes_two.jpg', 'NFT', 40946, 9, 9, 1661414279498, 1661414279498, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (131, 'NFT/38683f0ad1934f6793a948557c165c06.jpg', 'admin', 'it_takes_two.jpg', 'NFT', 36496, 9, 9, 1661414391126, 1661414391126, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (132, 'NFT/9c71b23b7363469aa5fe6520527bfa80.mp4', 'admin', '02-01 区块链是什么.mp4', 'NFT', 92825420, 10, 10, 1661414818163, 1661414818163, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (133, 'NFT/7f76e22a05244ab1b8dcb240ae33c2da.mp4', 'admin', '02-01 区块链是什么.mp4', 'NFT', 92825420, 10, 10, 1661414931672, 1661414931672, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (134, 'NFT/232df9db72a542d784fcb7d2eaaaa1e4.mp4', 'admin', '02-03 区块链应用场景.mp4', 'NFT', 47214380, 10, 10, 1661414953158, 1661414953158, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (135, 'NFT/e716e53281a741e5a66cb25112c74021.jpg', 'admin', '6.jpg', 'NFT', 10166, 9, 9, 1661416115048, 1661416115048, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (136, 'NFT/ca78465b05dc49769fb7bf42528cd54a.mp4', 'admin', '1d0f6fbfe0f291ff6f59fe9c1a5adac7.mp4', 'NFT', 471604, 9, 9, 1661416139705, 1661416139705, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (137, 'NFT/dd264e0d4d35416291c6fda9e3dc9af9.mp4', 'admin', '屏幕录制2022-08-25 16.02.37.mp4', 'NFT', 98104456, 9, 9, 1661416153838, 1661416153838, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (138, 'NFT/2dd60ca0942a4e66902f7dd8884dcf44.png', 'admin', '图片 2@1x (1)的副本3.png', 'NFT', 39848, 9, 9, 1661416314917, 1661416314917, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (139, 'NFT/87c429a71b2c4669958b2e37bacb8ca6.png', 'admin', '图片 2@1x (1)的副本5.png', 'NFT', 98980, 9, 9, 1661417523361, 1661417523361, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (140, 'NFT/ead175ac758944d18c175921d3b3070e.mp4', 'admin', 'NFT_1bbd82976a2945b5b2699465e77e8e7a.mp4', 'NFT', 471604, 9, 9, 1661417527579, 1661417527579, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (141, 'NFT/1f45e4d5940a4b9fbd59dac82e0cf5c6.png', 'admin', '图片 2@1x (1)的副本3.png', 'NFT', 98980, 9, 9, 1661417567079, 1661417567079, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (142, 'NFT/524b2a2f4494407385badc1882d55474.mp4', 'admin', 'NFT_1bbd82976a2945b5b2699465e77e8e7a.mp4', 'NFT', 471604, 9, 9, 1661417572353, 1661417572353, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (143, 'NFT/c2717207ba5541eea4ff141fc0ca89c0.png', 'admin', '大话.png', 'NFT', 41023, 1, 1, 1661479603497, 1661479603497, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (144, 'NFT/3349b848f0fe4a95b41f16dd470a593f.png', 'admin', '问道.png', 'NFT', 30671, 1, 1, 1661479856708, 1661479856708, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (145, 'NFT/a5f86d4d0d8d467b882adfb92306c4f1.mp4', 'admin', '65061b28f0d575118173be45969ea4d1.mp4', 'NFT', 1755537, 1, 1, 1661480163554, 1661480163554, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (146, 'NFT/a3441f9a537949838e5cbf30e6acddad.png', 'admin', '问道.png', 'NFT', 30671, 1, 1, 1661480283163, 1661480283163, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (147, 'NFT/03fc019659104b45a2632b155cd73bbb.mp4', 'admin', '65061b28f0d575118173be45969ea4d1.mp4', 'NFT', 1755537, 1, 1, 1661480285494, 1661480285494, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (148, 'NFT/2f05371a0b3247eebe5bb842f634471d.png', 'admin', '问道.png', 'NFT', 30671, 1, 1, 1661480427367, 1661480427367, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (149, 'NFT/389af2213c394bada12dabef1337aaaf.mp4', 'admin', '65061b28f0d575118173be45969ea4d1.mp4', 'NFT', 1755537, 1, 1, 1661480430660, 1661480430660, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (150, 'NFT/19e6cd08c7e1489fa12c0a3a7b31ee0a.png', 'admin', '问道.png', 'NFT', 30671, 1, 1, 1661483434480, 1661483434480, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (151, 'NFT/1e7a1a7bebd74c78b61d5c0e9918beb8.mp4', 'admin', '65061b28f0d575118173be45969ea4d1.mp4', 'NFT', 1755537, 1, 1, 1661483446459, 1661483446459, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (152, 'NFT/1cedcf8546964d5688cb8d44381a91fb.png', 'admin', '问道.png', 'NFT', 30671, 1, 1, 1661491530148, 1661491530148, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (153, 'NFT/a64e19a135e44956bb50bc719e437d66.mp4', 'admin', '65061b28f0d575118173be45969ea4d1.mp4', 'NFT', 1755537, 1, 1, 1661491545480, 1661491545480, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (154, 'NFT/2a8075e401ad445780fa657813422bc6.jpg', 'admin', 'it_takes_two.jpg', 'NFT', 36496, 9, 9, 1661492009315, 1661492009315, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (155, 'NFT/6b20a87729c4473fbbd72f437d1d8339.mp4', 'admin', '1d0f6fbfe0f291ff6f59fe9c1a5adac7.mp4', 'NFT', 471604, 9, 9, 1661492024103, 1661492024103, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (156, 'NFT/6a05a7a972d54bf085c0ccd023339b70.jpg', 'admin', 'it_takes_two.jpg', 'NFT', 36496, 9, 9, 1661492190206, 1661492190206, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (157, 'NFT/c5d02b71a32e4991b780573d4faa35e5.mp4', 'admin', '1d0f6fbfe0f291ff6f59fe9c1a5adac7.mp4', 'NFT', 471604, 9, 9, 1661492196847, 1661492196847, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (158, 'NFT/83832ee5a20e40dda6e40f7d3a0dd46a.png', 'admin', '问道.png', 'NFT', 30671, 1, 1, 1661492312100, 1661492312100, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (159, 'NFT/ede3018efd324ba9b92c574c65b4027c.mp4', 'admin', '65061b28f0d575118173be45969ea4d1.mp4', 'NFT', 1755537, 1, 1, 1661492323740, 1661492323740, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (160, 'NFT/ab3c24f88c824cc2a846b8be43600ff9.png', 'admin', '问道.png', 'NFT', 30671, 1, 1, 1661495216718, 1661495216718, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (161, 'NFT/75feeae5fe3140edbdd9de0425d7630a.mp4', 'admin', '65061b28f0d575118173be45969ea4d1.mp4', 'NFT', 1755537, 1, 1, 1661495219392, 1661495219392, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (162, 'NFT/a9942515d1c14e1c905f97b70f8dcf16.jpg', 'admin', '7.jpg', 'NFT', 30330, 9, 9, 1661739808952, 1661739808952, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (163, 'NFT/8791e8c5587a42e5bc63603e75e260b5.jpg', 'admin', '7.jpg', 'NFT', 30330, 9, 9, 1661742989483, 1661742989483, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (164, 'NFT/0cbaa6b28d8c41819a25204af8ff74ea.jpg', 'admin', '7.jpg', 'NFT', 30330, 9, 9, 1661751326077, 1661751326077, 0);
INSERT INTO `sys_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (165, 'NFT/a93f9c3cf7a2454db9b12d137c574735.jpg', 'admin', '8.jpg', 'NFT', 27897, 9, 9, 1661753543796, 1661753543796, 0);
COMMIT;

-- ----------------------------
-- Table structure for sys_game
-- ----------------------------
DROP TABLE IF EXISTS `sys_game`;
CREATE TABLE `sys_game` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `type_id` bigint(20) DEFAULT NULL COMMENT '游戏类别id',
  `name` varchar(32) DEFAULT NULL COMMENT '游戏名称',
  `brief` varchar(255) DEFAULT NULL COMMENT '简介',
  `price` decimal(10,2) DEFAULT NULL COMMENT '价格',
  `unit` varchar(32) DEFAULT NULL COMMENT '单位 USDC',
  `official_url` varchar(255) DEFAULT NULL COMMENT '官方网址',
  `download_url` varchar(255) DEFAULT NULL COMMENT '下载地址',
  `community_url` varchar(255) DEFAULT NULL COMMENT '社区地址',
  `developer` varchar(125) DEFAULT NULL COMMENT '开发者',
  `picture_url` varchar(255) DEFAULT NULL COMMENT '图片链接',
  `video_url` varchar(255) DEFAULT NULL COMMENT '视频链接',
  `picture_file_id` bigint(20) DEFAULT NULL COMMENT '图片文件id',
  `video_file_id` bigint(20) DEFAULT NULL COMMENT '视频文件id',
  `rank` decimal(2,1) DEFAULT NULL COMMENT '评分',
  `collections` bigint(20) DEFAULT '0' COMMENT '收藏量',
  `comments_allowed` tinyint(1) DEFAULT NULL COMMENT '是否允许评论  0：不允许   1：允许',
  `status` tinyint(1) DEFAULT NULL COMMENT '游戏状态  0：下架   1：正常',
  `delete_flag` tinyint(1) DEFAULT '0' COMMENT '删除标记  1：已删除   0：未删除',
  `introduction` longtext COMMENT '介绍',
  `created_by` bigint(20) DEFAULT NULL COMMENT '创建者',
  `created_at` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `updated_by` bigint(20) DEFAULT NULL COMMENT '修改者',
  `updated_at` bigint(20) DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_official` (`official_url`,`delete_flag`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 COMMENT='系统游戏表';

-- ----------------------------
-- Records of sys_game
-- ----------------------------
BEGIN;
INSERT INTO `sys_game` (`id`, `type_id`, `name`, `brief`, `price`, `unit`, `official_url`, `download_url`, `community_url`, `developer`, `picture_url`, `video_url`, `picture_file_id`, `video_file_id`, `rank`, `collections`, `comments_allowed`, `status`, `delete_flag`, `introduction`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (1, 1, 'liren', NULL, NULL, NULL, 'www.liren.com', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2, 1658978201172, 17, 1659666897622);
INSERT INTO `sys_game` (`id`, `type_id`, `name`, `brief`, `price`, `unit`, `official_url`, `download_url`, `community_url`, `developer`, `picture_url`, `video_url`, `picture_file_id`, `video_file_id`, `rank`, `collections`, `comments_allowed`, `status`, `delete_flag`, `introduction`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (2, 2, 'ces', NULL, NULL, NULL, 'www.baidu.com', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2, 1658978218666, 2, 1658979740990);
INSERT INTO `sys_game` (`id`, `type_id`, `name`, `brief`, `price`, `unit`, `official_url`, `download_url`, `community_url`, `developer`, `picture_url`, `video_url`, `picture_file_id`, `video_file_id`, `rank`, `collections`, `comments_allowed`, `status`, `delete_flag`, `introduction`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (3, 3, 'wendao', NULL, NULL, NULL, 'www.wendao.com', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, 17, 1658978218666, 9, 1661394957032);
INSERT INTO `sys_game` (`id`, `type_id`, `name`, `brief`, `price`, `unit`, `official_url`, `download_url`, `community_url`, `developer`, `picture_url`, `video_url`, `picture_file_id`, `video_file_id`, `rank`, `collections`, `comments_allowed`, `status`, `delete_flag`, `introduction`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (4, 4, '123123', 'asdsadsad', 12312.00, 'USDC', '123123', '12312', '31231', '2312312', NULL, NULL, 94, 95, NULL, NULL, 1, 1, NULL, '&lt;p class=\"ql-align-center\"&gt;&lt;br&gt;&lt;/p&gt;', 9, 1661335822058, 9, 1661336057633);
INSERT INTO `sys_game` (`id`, `type_id`, `name`, `brief`, `price`, `unit`, `official_url`, `download_url`, `community_url`, `developer`, `picture_url`, `video_url`, `picture_file_id`, `video_file_id`, `rank`, `collections`, `comments_allowed`, `status`, `delete_flag`, `introduction`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (5, 5, '123', '123123', 123.00, 'USDC', '123', '123', '123', '123', NULL, NULL, 96, 97, NULL, NULL, 1, 1, NULL, '&lt;p&gt;1231231&lt;/p&gt;', 9, 1661391003894, 9, 1661394953906);
INSERT INTO `sys_game` (`id`, `type_id`, `name`, `brief`, `price`, `unit`, `official_url`, `download_url`, `community_url`, `developer`, `picture_url`, `video_url`, `picture_file_id`, `video_file_id`, `rank`, `collections`, `comments_allowed`, `status`, `delete_flag`, `introduction`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (6, 6, '123333', '123123', 123.00, 'USDC', '123324', '123', '123', '123', NULL, NULL, 98, 99, NULL, NULL, 1, 1, NULL, '&lt;p&gt;123123123123&lt;/p&gt;', 9, 1661392042643, 9, 1661394955478);
INSERT INTO `sys_game` (`id`, `type_id`, `name`, `brief`, `price`, `unit`, `official_url`, `download_url`, `community_url`, `developer`, `picture_url`, `video_url`, `picture_file_id`, `video_file_id`, `rank`, `collections`, `comments_allowed`, `status`, `delete_flag`, `introduction`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (7, 7, '43534', '12321', 534534.00, 'USDC', '534534', '5345', '534534', '534534', NULL, NULL, 100, 101, NULL, NULL, 1, 1, NULL, '&lt;p&gt;3213123&lt;/p&gt;', 9, 1661392609238, 9, 1661394951948);
INSERT INTO `sys_game` (`id`, `type_id`, `name`, `brief`, `price`, `unit`, `official_url`, `download_url`, `community_url`, `developer`, `picture_url`, `video_url`, `picture_file_id`, `video_file_id`, `rank`, `collections`, `comments_allowed`, `status`, `delete_flag`, `introduction`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (8, 3, 'It Takes Two', '游戏需要2名玩家一同进行冒险，玩家在游戏中将扮演相互看不顺眼、被魔咒变成了玩偶的科迪和小梅夫妇。他们一起被困在一个奇幻世界里，每个角落都隐藏着意想不到的东西，他们不得不一起克服挑战，同时挽救他们感情', 33.00, 'USDC', 'www.baidu.com', 'www.taobao.com', 'www.bilibili.com', '余航', 'http://192.168.1.100:9999/admin/public/file/download/admin?objectName=NFT/10be40f2808f445086a36db873a7da3d.jpg', 'http://192.168.1.100:9999/admin/public/file/download/admin?objectName=NFT/593fba2aa97640128dc4d5b2f0466252.mp4', 103, 128, NULL, NULL, 1, 1, 0, '&lt;p&gt;&lt;span style=\"color: rgb(0, 0, 0); background-color: rgb(255, 255, 255);\"&gt;游玩《双人成行》，踏上生命中最疯狂的旅程，这是一款别开生面的平台冒险游戏，完全为合作模式而设计。利用好友通行证*邀请一位好友免费游玩，共同体验多种多样的乐趣，享受颠覆性的玩法挑战。扮演相互看不顺眼的科迪和小梅夫妇，这两个人被魔咒变成了玩偶。他们一起被困在一个奇幻世界里，每个角落都隐藏着意想不到的东西，他们不得不一起克服挑战，同时挽救他们破裂的关系。&lt;/span&gt;&lt;/p&gt;&lt;p&gt;&lt;br&gt;&lt;/p&gt;&lt;p&gt;&lt;span style=\"color: rgb(0, 0, 0); background-color: rgb(255, 255, 255);\"&gt;在每个新关卡中，掌握角色独特且与对方息息相关的能力。一定要互相帮助，克服大量出乎意料的障碍，共度搞笑欢乐的时刻。用力踢流氓松鼠毛茸茸的尾巴，绕过一条内裤，在热闹的夜店里开舞会，坐雪橇穿过神奇的雪花玻璃球。一个真挚又搞笑的故事在游戏中徐徐展开，交织成独特的隐喻体验。&lt;/span&gt;&lt;/p&gt;&lt;p&gt;&lt;br&gt;&lt;/p&gt;&lt;p&gt;&lt;span style=\"color: rgb(0, 0, 0); background-color: rgb(255, 255, 255);\"&gt;《双人成行》由业界合作游戏领导者且屡获殊荣的工作室 Hazelight 开发。他们即将带您踏上狂野而奇妙的旅程，只有一件事是确定的：二人同心，其利断金。&lt;/span&gt;&lt;/p&gt;&lt;h2&gt;主要特色&lt;/h2&gt;&lt;ul&gt;&lt;li&gt;&lt;strong&gt;纯粹而完美的合作模式&lt;/strong&gt;——通过远程同乐**邀请好友免费畅玩，体验一场纯粹为两位玩家打造的惊奇冒险。体验分屏显示模式，选择本地合作或在线联机，面对不断变化的挑战，团结一致是前进的唯一途径。&lt;/li&gt;&lt;li&gt;&lt;br&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;搞笑又颠覆想像的游戏&lt;/strong&gt;——从暴走的真空吸尘器到走心的爱情大师，您永远不会知道接下来会遇到什么。每个关卡都有大量别开生面的挑战和需要掌握的全新角色技能，体验故事和游戏交织的隐喻风格，推动互动式故事徐徐展开。&lt;/li&gt;&lt;li&gt;&lt;br&gt;&lt;/li&gt;&lt;li&gt;&lt;strong&gt;关于人际关系的故事&lt;/strong&gt;——在克服挑战的相处中发现真挚情感。帮助科迪和小梅学习如何克服他们之间的分歧。结识各种奇特和可爱的角色。团结一致，一起踏上冒险之旅！&lt;/li&gt;&lt;/ul&gt;&lt;h2&gt;关于 HAZELIGHT&lt;/h2&gt;&lt;p&gt;&lt;span style=\"color: rgb(0, 0, 0); background-color: rgb(255, 255, 255);\"&gt;Hazelight 位于瑞典斯德哥尔摩，是一家屡获殊荣的独立游戏开发工作室。在 2014 年由 Josef Fares（影片导演和获奖游戏《兄弟：双子传说》的创作者）创办，Hazelight 致力于推动游戏的创意界限。在 2018 年，Hazelight 发布了荣获 BAFTA 奖的游戏《A Way Out》——史上第一款第三人称动作-冒险合作游戏——这是 EA Originals 计划的一部分。&lt;/span&gt;&lt;/p&gt;&lt;h2&gt;关于 EA ORIGINALS&lt;/h2&gt;&lt;p&gt;&lt;span style=\"color: rgb(0, 0, 0); background-color: rgb(255, 255, 255);\"&gt;EA Originals 帮助全球充满热情,自成一格,才华横溢的游戏工作室崭露头角。发掘别出心裁的游戏制作者，探索他们天马行空的想象力，展现新颖而难忘的游戏体验。&lt;/span&gt;&lt;/p&gt;', 9, 1661395518833, 9, 1661495213320);
INSERT INTO `sys_game` (`id`, `type_id`, `name`, `brief`, `price`, `unit`, `official_url`, `download_url`, `community_url`, `developer`, `picture_url`, `video_url`, `picture_file_id`, `video_file_id`, `rank`, `collections`, `comments_allowed`, `status`, `delete_flag`, `introduction`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (9, 5, 'Two Point Campus', '以自己的方式打造属于你的大学！', 29.00, 'USDC', 'www.google.com', 'www.taobao.com', 'www.bilibili.com', '贺伟', 'http://192.168.1.100:9999/admin/public/file/download/admin?objectName=NFT/cefeeba7d3784038a198c1e40405968d.jpeg', 'http://192.168.1.100:9999/admin/public/file/download/admin?objectName=NFT/232df9db72a542d784fcb7d2eaaaa1e4.mp4', 121, 134, NULL, NULL, 1, 1, 0, '&lt;h2&gt;&lt;span class=\"ql-font-SimHei\" style=\"color: rgb(255, 255, 255);\"&gt;&lt;img src=\"https://cdn.akamai.steamstatic.com/steam/apps/1649080/extras/GIF1.gif?t=1660913074\"&gt;&lt;/span&gt;&lt;/h2&gt;&lt;h2&gt;&lt;br&gt;&lt;/h2&gt;&lt;h2&gt;&lt;span class=\"ql-font-SimHei\" style=\"color: rgb(255, 255, 255);\"&gt;是时候让学术界原地起飞了！对学习有着无尽渴望？或者你只是想打造一座教育界的丰碑？校园内含大量全新创建工具，助你打造心目中的大学校园。&lt;/span&gt;&lt;/h2&gt;&lt;h2&gt;&lt;br&gt;&lt;/h2&gt;&lt;h2&gt;&lt;span class=\"ql-font-SimHei\" style=\"color: rgb(255, 255, 255);\"&gt;你将首次有机会进行户外建设，打造属于你的愉快校园环境，并同时具备当地顶级教学设施。不管你是喜欢铺设简单地基，还是精心栽培每棵树木，打造校园的方式都全部掌握在你手中。&lt;/span&gt;&lt;/h2&gt;&lt;h2&gt;&lt;br&gt;&lt;/h2&gt;&lt;h2&gt;&lt;span class=\"ql-font-SimHei\" style=\"color: rgb(255, 255, 255);\"&gt;使用全新简易工具来铺砌道路。种植各式美丽的户外植物。放置长凳、喷泉、雕塑、树篱……甚至尖桩篱笆。唯一限制你的就只有你的想象力（和你在游戏里的银行存款）。&lt;/span&gt;&lt;/h2&gt;&lt;h2&gt;&lt;br&gt;&lt;/h2&gt;&lt;h2&gt;&lt;strong class=\"ql-font-SimHei\" style=\"color: rgb(255, 255, 255);\"&gt;与众不同&lt;/strong&gt;&lt;/h2&gt;&lt;h2&gt;&lt;br&gt;&lt;/h2&gt;&lt;h2&gt;&lt;span class=\"ql-font-SimHei\" style=\"color: rgb(255, 255, 255);\"&gt;&lt;img src=\"https://cdn.akamai.steamstatic.com/steam/apps/1649080/extras/GIF2.gif?t=1660913074\"&gt;&lt;/span&gt;&lt;/h2&gt;&lt;h2&gt;&lt;br&gt;&lt;/h2&gt;&lt;h2&gt;&lt;span class=\"ql-font-SimHei\" style=\"color: rgb(255, 255, 255);\"&gt;当然，若是不荒唐怪诞，这也称不上是一款Two Point游戏了。&lt;/span&gt;&lt;/h2&gt;&lt;h2&gt;&lt;br&gt;&lt;/h2&gt;&lt;h2&gt;&lt;span class=\"ql-font-SimHei\" style=\"color: rgb(255, 255, 255);\"&gt;除了一般的学术课程以外，Two Point县的学生还可享受一系列天马行空的超棒课程：从骑士学院（嘿，我们这一生总得学学马上枪术）到令人垂涎三尺的餐饮业（你的学生将可打造各种令人口水直流的美食，比如巨型披萨和庞大馅饼等）。&lt;/span&gt;&lt;/h2&gt;&lt;h2&gt;&lt;br&gt;&lt;/h2&gt;&lt;h2&gt;&lt;strong class=\"ql-font-SimHei\" style=\"color: rgb(255, 255, 255);\"&gt;学年……开始！&lt;/strong&gt;&lt;/h2&gt;&lt;h2&gt;&lt;br&gt;&lt;/h2&gt;&lt;h2&gt;&lt;span class=\"ql-font-SimHei\" style=\"color: rgb(255, 255, 255);\"&gt;充分利用这个机会，与大学的小伙伴们度过更长的美好时光。学年以暑假开始，因此在学生入学前，你将有足够的时间整顿一切。&lt;/span&gt;&lt;/h2&gt;&lt;h2&gt;&lt;br&gt;&lt;/h2&gt;&lt;h2&gt;&lt;span class=\"ql-font-SimHei\" style=\"color: rgb(255, 255, 255);\"&gt;建造图书馆、聘用最优秀的员工（从古怪教授到疯狂研究员）、为你的校园打造最棒的课程，并亲眼目睹学生发挥自身的学术潜能！&lt;/span&gt;&lt;/h2&gt;&lt;h2&gt;&lt;br&gt;&lt;/h2&gt;&lt;h2&gt;&lt;strong class=\"ql-font-SimHei\" style=\"color: rgb(255, 255, 255);\"&gt;塑造未来&lt;/strong&gt;&lt;/h2&gt;&lt;h2&gt;&lt;br&gt;&lt;/h2&gt;&lt;h2&gt;&lt;span class=\"ql-font-SimHei\" style=\"color: rgb(255, 255, 255);\"&gt;&lt;img src=\"https://cdn.akamai.steamstatic.com/steam/apps/1649080/extras/GIF3.gif?t=1660913074\"&gt;&lt;/span&gt;&lt;/h2&gt;&lt;h2&gt;&lt;br&gt;&lt;/h2&gt;&lt;h2&gt;&lt;span class=\"ql-font-SimHei\" style=\"color: rgb(255, 255, 255);\"&gt;但也不能光是埋头苦干。熟悉你的学生，探索他们的个性、愿望与需求。提供俱乐部、社团等活动，确保他们心情舒畅。&lt;/span&gt;&lt;/h2&gt;&lt;h2&gt;&lt;br&gt;&lt;/h2&gt;&lt;h2&gt;&lt;span class=\"ql-font-SimHei\" style=\"color: rgb(255, 255, 255);\"&gt;帮他们结交朋友、树立关系，并确保他们有足够的娱乐活动、人文关怀和生活乐趣，方可使他们发展为杰出人才，为你创立的大学争光&lt;/span&gt;&lt;/h2&gt;', 9, 1661407276849, 9, 1661507235349);
INSERT INTO `sys_game` (`id`, `type_id`, `name`, `brief`, `price`, `unit`, `official_url`, `download_url`, `community_url`, `developer`, `picture_url`, `video_url`, `picture_file_id`, `video_file_id`, `rank`, `collections`, `comments_allowed`, `status`, `delete_flag`, `introduction`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (10, 10, 'qwewqe1', '123', 1231.00, 'USDC', '123', '23', '23', '123', 'http://192.168.1.100:9999/admin/public/file/download/admin?objectName=NFT/87c429a71b2c4669958b2e37bacb8ca6.png', 'http://192.168.1.100:9999/admin/public/file/download/admin?objectName=NFT/ead175ac758944d18c175921d3b3070e.mp4', 139, 140, NULL, 0, 1, 1, NULL, '&lt;p&gt;1123&lt;/p&gt;', 9, 1661417529934, 9, 1661417551747);
INSERT INTO `sys_game` (`id`, `type_id`, `name`, `brief`, `price`, `unit`, `official_url`, `download_url`, `community_url`, `developer`, `picture_url`, `video_url`, `picture_file_id`, `video_file_id`, `rank`, `collections`, `comments_allowed`, `status`, `delete_flag`, `introduction`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (11, 1, 'wewqe', '123', 123.00, 'USDC', 'qwe1', '123', '123', '123', 'http://192.168.1.100:9999/admin/public/file/download/admin?objectName=NFT/1f45e4d5940a4b9fbd59dac82e0cf5c6.png', 'http://192.168.1.100:9999/admin/public/file/download/admin?objectName=NFT/524b2a2f4494407385badc1882d55474.mp4', 141, 142, NULL, 0, 1, 1, NULL, '&lt;p&gt;123123&lt;/p&gt;', 9, 1661417575074, 9, 1661417579417);
INSERT INTO `sys_game` (`id`, `type_id`, `name`, `brief`, `price`, `unit`, `official_url`, `download_url`, `community_url`, `developer`, `picture_url`, `video_url`, `picture_file_id`, `video_file_id`, `rank`, `collections`, `comments_allowed`, `status`, `delete_flag`, `introduction`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (12, 3, 'fasfasf', '123213', 123.00, 'USDC', '123fsf', '123assf', 'fewrfr', 'gerger', 'http://192.168.1.100:9999/admin/public/file/download/admin?objectName=NFT/2a8075e401ad445780fa657813422bc6.jpg', 'http://192.168.1.100:9999/admin/public/file/download/admin?objectName=NFT/6b20a87729c4473fbbd72f437d1d8339.mp4', 154, 155, NULL, 0, 1, 1, NULL, '&lt;p&gt;1232sdaf&lt;/p&gt;', 9, 1661492029360, 9, 1661492035928);
INSERT INTO `sys_game` (`id`, `type_id`, `name`, `brief`, `price`, `unit`, `official_url`, `download_url`, `community_url`, `developer`, `picture_url`, `video_url`, `picture_file_id`, `video_file_id`, `rank`, `collections`, `comments_allowed`, `status`, `delete_flag`, `introduction`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (13, 5, 'wendao', '问道是一款回合制游戏！', 23.00, 'USDC', 'www.wendao.com', 'https://www.baidu.com', 'wendaoCommunity.com', 'yangyang', 'http://192.168.1.100:9999/admin/public/file/download/admin?objectName=NFT/ab3c24f88c824cc2a846b8be43600ff9.png', 'http://192.168.1.100:9999/admin/public/file/download/admin?objectName=NFT/75feeae5fe3140edbdd9de0425d7630a.mp4', 160, 161, NULL, 0, 1, 1, 0, '&lt;p&gt;问道是一款很好玩的游戏！&lt;/p&gt;', 1, 1661495286925, 1, 1661507806697);
COMMIT;

-- ----------------------------
-- Table structure for sys_game_comments
-- ----------------------------
DROP TABLE IF EXISTS `sys_game_comments`;
CREATE TABLE `sys_game_comments` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `game_id` bigint(20) NOT NULL COMMENT '游戏id',
  `game_name` varchar(255) NOT NULL COMMENT '游戏名称',
  `uc_user_id` bigint(20) NOT NULL COMMENT 'uc端用户id',
  `uc_user_name` varchar(64) NOT NULL COMMENT 'uc端用户名字',
  `comments_time` bigint(20) NOT NULL COMMENT '评价时间',
  `comments` varchar(1024) NOT NULL COMMENT '用户评价',
  `star` decimal(2,1) NOT NULL COMMENT '评分',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '评价状态  0：无效   1：有效',
  `delete_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记  1：已删除   0：未删除',
  `created_by` bigint(20) NOT NULL COMMENT '创建者',
  `created_at` bigint(20) NOT NULL COMMENT '创建时间',
  `updated_by` bigint(20) NOT NULL COMMENT '修改者',
  `updated_at` bigint(20) NOT NULL COMMENT '修改时间',
  `likes` int(11) NOT NULL DEFAULT '0' COMMENT '评论点赞数/有用',
  `bury` int(11) NOT NULL DEFAULT '0' COMMENT '无用',
  `replies` int(11) NOT NULL DEFAULT '0' COMMENT '评论回复数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8 COMMENT='系统游戏表';

-- ----------------------------
-- Records of sys_game_comments
-- ----------------------------
BEGIN;
INSERT INTO `sys_game_comments` (`id`, `game_id`, `game_name`, `uc_user_id`, `uc_user_name`, `comments_time`, `comments`, `star`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`, `likes`, `bury`, `replies`) VALUES (11, 8, 'It Takes Two', 20, '0x84f9f225bd824c4fea9d33095674f82611ee3325', 1660013569984, '我寄给你的信 总要送往邮局 不喜欢放在街边的绿色邮筒中 我总疑心那里会慢一点当我沉默的时候 我觉得很充实 当话 就感到了空虚小的时候，不把他当人 大了以后 也做不了人', 3.5, 0, 0, 10, 1660013569985, 9, 1661493632143, 0, 0, 0);
INSERT INTO `sys_game_comments` (`id`, `game_id`, `game_name`, `uc_user_id`, `uc_user_name`, `comments_time`, `comments`, `star`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`, `likes`, `bury`, `replies`) VALUES (12, 8, 'It Takes Two', 21, '0x85bdaffe9e48f643ed1f0f920dcf6fc019204013', 1660013744487, '我寄给你的信 总要送往邮局111', 3.0, 1, 0, 10, 1660013744488, 10, 1661480529954, 0, 0, 0);
INSERT INTO `sys_game_comments` (`id`, `game_id`, `game_name`, `uc_user_id`, `uc_user_name`, `comments_time`, `comments`, `star`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`, `likes`, `bury`, `replies`) VALUES (13, 9, 'Two Point Campus', 20, '0x84f9f225bd824c4fea9d33095674f82611ee3325', 1660013788957, '游戏很棒，情节很好啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊', 4.5, 1, 0, 10, 1660013788957, 10, 1661480530836, 0, 0, 0);
INSERT INTO `sys_game_comments` (`id`, `game_id`, `game_name`, `uc_user_id`, `uc_user_name`, `comments_time`, `comments`, `star`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`, `likes`, `bury`, `replies`) VALUES (14, 9, 'Two Point Campus', 21, '0x85bdaffe9e48f643ed1f0f920dcf6fc019204013', 1660121550803, '游戏很棒，情节很好啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊', 4.5, 1, 0, 10, 1660121550810, 9, 1661416494039, 0, 0, 0);
INSERT INTO `sys_game_comments` (`id`, `game_id`, `game_name`, `uc_user_id`, `uc_user_name`, `comments_time`, `comments`, `star`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`, `likes`, `bury`, `replies`) VALUES (16, 20, 'test', 21, '0x85bdaffe9e48f643ed1f0f920dcf6fc019204013', 1661741371314, 'i like it,very much', 4.5, 1, 0, 21, 1661741373622, 21, 1661741373622, 1, 0, 0);
COMMIT;

-- ----------------------------
-- Table structure for sys_game_comments_like_bury
-- ----------------------------
DROP TABLE IF EXISTS `sys_game_comments_like_bury`;
CREATE TABLE `sys_game_comments_like_bury` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `comments_id` bigint(20) NOT NULL COMMENT '评论id',
  `uc_user_id` bigint(20) NOT NULL COMMENT 'uc端用户id',
  `type` tinyint(1) NOT NULL DEFAULT '0' COMMENT '类型 1 点赞 2 踩',
  `created_by` bigint(20) NOT NULL COMMENT '创建者',
  `created_at` bigint(20) NOT NULL COMMENT '创建时间',
  `updated_by` bigint(20) NOT NULL COMMENT '修改者',
  `updated_at` bigint(20) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8 COMMENT='游戏评论点赞/踩表';

-- ----------------------------
-- Records of sys_game_comments_like_bury
-- ----------------------------
BEGIN;
INSERT INTO `sys_game_comments_like_bury` (`id`, `comments_id`, `uc_user_id`, `type`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (22, 16, 21, 1, 21, 1661760702329, 21, 1661760702329);
COMMIT;

-- ----------------------------
-- Table structure for sys_game_comments_replies
-- ----------------------------
DROP TABLE IF EXISTS `sys_game_comments_replies`;
CREATE TABLE `sys_game_comments_replies` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `comments_id` bigint(20) NOT NULL COMMENT '评论id',
  `uc_user_id` bigint(20) NOT NULL COMMENT 'uc端用户id',
  `uc_user_name` varchar(50) DEFAULT NULL COMMENT 'uc端用户名字',
  `reply_time` bigint(20) NOT NULL COMMENT '回复时间',
  `replies` varchar(1024) NOT NULL COMMENT '回复内容',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='评论回复表';

-- ----------------------------
-- Records of sys_game_comments_replies
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_game_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_game_type`;
CREATE TABLE `sys_game_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `parent_code` varchar(64) DEFAULT NULL COMMENT '父类别code，一级菜单为空',
  `code` varchar(64) DEFAULT NULL COMMENT '类别编码',
  `name` varchar(64) DEFAULT NULL COMMENT '类别名称',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `status` tinyint(1) DEFAULT NULL COMMENT '是否有效  0：否   1：是',
  `delete_flag` tinyint(1) DEFAULT '0' COMMENT '删除标记  1：已删除   0：未删除',
  `created_by` bigint(20) DEFAULT NULL COMMENT '创建者',
  `created_at` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `updated_by` bigint(20) DEFAULT NULL COMMENT '修改者',
  `updated_at` bigint(20) DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_code` (`code`,`delete_flag`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8 COMMENT='游戏类别表';

-- ----------------------------
-- Records of sys_game_type
-- ----------------------------
BEGIN;
INSERT INTO `sys_game_type` (`id`, `parent_code`, `code`, `name`, `sort`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (1, NULL, 'Action Game', 'ACT', 0, 1, 0, 9, 1661481514464, 9, 1661495136966);
INSERT INTO `sys_game_type` (`id`, `parent_code`, `code`, `name`, `sort`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (2, NULL, 'Roles Play Games', 'RPG', 0, 1, 0, 9, 1661481851858, 9, 1661495142597);
INSERT INTO `sys_game_type` (`id`, `parent_code`, `code`, `name`, `sort`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (3, NULL, 'Adventure Game', 'AVG', 0, 1, 0, 9, 1661481874251, 9, 1661495149161);
INSERT INTO `sys_game_type` (`id`, `parent_code`, `code`, `name`, `sort`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (4, NULL, 'Strategy Game', 'SLG', 0, 1, 0, 9, 1661481901421, 9, 1661495154839);
INSERT INTO `sys_game_type` (`id`, `parent_code`, `code`, `name`, `sort`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (5, NULL, 'Simulation Role-playing Games', 'SRPG', 0, 1, 0, 9, 1661481926071, 9, 1661495160684);
INSERT INTO `sys_game_type` (`id`, `parent_code`, `code`, `name`, `sort`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (6, NULL, 'Real-time Strategy Games', 'RTS', 0, 1, 0, 9, 1661481946892, 9, 1661495167367);
INSERT INTO `sys_game_type` (`id`, `parent_code`, `code`, `name`, `sort`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (7, NULL, 'Fighting Technology Game', 'FTG', 0, 1, 0, 9, 1661482069012, 9, 1661495173135);
INSERT INTO `sys_game_type` (`id`, `parent_code`, `code`, `name`, `sort`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (8, NULL, 'Shooting Games', 'STG', 0, 1, 0, 9, 1661482088221, 9, 1661495178809);
INSERT INTO `sys_game_type` (`id`, `parent_code`, `code`, `name`, `sort`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (9, NULL, 'FPS', 'First Personal Shooting Game', 0, 0, NULL, 9, 1661482117204, 9, 1661482334389);
INSERT INTO `sys_game_type` (`id`, `parent_code`, `code`, `name`, `sort`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (10, 'S', 'FPS', 'First Personal Shooting Game', 0, 0, NULL, 9, 1661482341532, 9, 1661482383328);
INSERT INTO `sys_game_type` (`id`, `parent_code`, `code`, `name`, `sort`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (11, 'STG', 'FPS', 'First Personal Shooting Game', 0, 1, NULL, 9, 1661482426780, 9, 1661495202449);
INSERT INTO `sys_game_type` (`id`, `parent_code`, `code`, `name`, `sort`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (12, 'STG', 'TPS', 'Third Person Shooter', 0, 1, NULL, 9, 1661482771708, 9, 1661495204486);
INSERT INTO `sys_game_type` (`id`, `parent_code`, `code`, `name`, `sort`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (13, NULL, '123123', '123123', 0, 0, NULL, 9, 1661482786195, 9, 1661483050664);
INSERT INTO `sys_game_type` (`id`, `parent_code`, `code`, `name`, `sort`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (14, NULL, '123123333', '123123', 0, 0, NULL, 9, 1661482941702, 9, 1661483052405);
INSERT INTO `sys_game_type` (`id`, `parent_code`, `code`, `name`, `sort`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (15, NULL, 'fdgdfgfd', '324534', 0, 0, NULL, 9, 1661482995367, 9, 1661483054134);
INSERT INTO `sys_game_type` (`id`, `parent_code`, `code`, `name`, `sort`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (16, '', 'werwer', 'werwer', 0, 1, NULL, 9, 1661483559612, 9, 1661483572958);
INSERT INTO `sys_game_type` (`id`, `parent_code`, `code`, `name`, `sort`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (17, 'werwer', 'retret', 'ertret', 0, 1, NULL, 9, 1661483565659, 9, 1661483570731);
INSERT INTO `sys_game_type` (`id`, `parent_code`, `code`, `name`, `sort`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (18, 'Shooting Games', 'First Personal Shooting Game	', 'FPS', 0, 1, 0, 9, 1661495190354, 9, 1661495190354);
INSERT INTO `sys_game_type` (`id`, `parent_code`, `code`, `name`, `sort`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (19, 'Shooting Games', 'Third Person Shooter', 'TPS', 0, 1, 0, 9, 1661495199978, 9, 1661495199978);
COMMIT;

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `operator` varchar(64) NOT NULL COMMENT '操作人',
  `operator_name` varchar(64) DEFAULT NULL COMMENT '操作人名字',
  `operation` varchar(64) NOT NULL COMMENT '具体操作',
  `method` varchar(255) NOT NULL COMMENT '执行的方法',
  `params` varchar(2000) NOT NULL COMMENT '携带的参数',
  `ip` varchar(64) NOT NULL COMMENT 'ip地址',
  `created_at` bigint(20) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8 COMMENT='系统操作日志表';

-- ----------------------------
-- Records of sys_log
-- ----------------------------
BEGIN;
INSERT INTO `sys_log` (`id`, `operator`, `operator_name`, `operation`, `method`, `params`, `ip`, `created_at`) VALUES (11, '10', '贺伟', '添加组织', 'com.seeds.admin.controller.SysOrgController.add', '[{\"orgName\":\"testOrg\",\"owner\":\"张三\",\"comments\":\"备注\"}]', '127.0.0.1', 1659323675118);
INSERT INTO `sys_log` (`id`, `operator`, `operator_name`, `operation`, `method`, `params`, `ip`, `created_at`) VALUES (12, '10', '贺伟', '添加组织', 'com.seeds.admin.controller.SysOrgController.add', '[{\"orgName\":\"testOrg\",\"ownerId\":\"19\",\"ownerName\":\"贺伟\",\"comments\":\"备注\"}]', '127.0.0.1', 1659492318658);
INSERT INTO `sys_log` (`id`, `operator`, `operator_name`, `operation`, `method`, `params`, `ip`, `created_at`) VALUES (13, '10', '贺伟', '编辑组织信息', 'com.seeds.admin.controller.SysOrgController.modify', '[{\"id\":29,\"orgName\":\"编辑组织信息\",\"comments\":\"111\"}]', '127.0.0.1', 1659492664682);
INSERT INTO `sys_log` (`id`, `operator`, `operator_name`, `operation`, `method`, `params`, `ip`, `created_at`) VALUES (14, '10', '贺伟', '删除组织', 'com.seeds.admin.controller.SysOrgController.delete', '[{\"ids\":[1554649580157669376]}]', '192.168.1.101', 1659679186004);
INSERT INTO `sys_log` (`id`, `operator`, `operator_name`, `operation`, `method`, `params`, `ip`, `created_at`) VALUES (15, '10', '贺伟', '删除组织', 'com.seeds.admin.controller.SysOrgController.delete', '[{\"ids\":[1554649580157669376]}]', '192.168.1.101', 1659679208337);
INSERT INTO `sys_log` (`id`, `operator`, `operator_name`, `operation`, `method`, `params`, `ip`, `created_at`) VALUES (16, '10', '贺伟', '删除组织', 'com.seeds.admin.controller.SysOrgController.delete', '[{\"ids\":[1554649580157669376]}]', '192.168.1.101', 1659679252105);
INSERT INTO `sys_log` (`id`, `operator`, `operator_name`, `operation`, `method`, `params`, `ip`, `created_at`) VALUES (17, '17', '邓平', '添加组织', 'com.seeds.admin.controller.SysOrgController.add', '[{\"id\":0,\"parentOrgId\":0,\"orgName\":\"TestOrg\",\"ownerId\":\"29\",\"ownerName\":\"邓阳\",\"comments\":\"备注\",\"sort\":0}]', '127.0.0.1', 1659921291251);
INSERT INTO `sys_log` (`id`, `operator`, `operator_name`, `operation`, `method`, `params`, `ip`, `created_at`) VALUES (18, '17', '邓平', '删除组织', 'com.seeds.admin.controller.SysOrgController.delete', '[{\"ids\":[27]}]', '127.0.0.1', 1659921322527);
INSERT INTO `sys_log` (`id`, `operator`, `operator_name`, `operation`, `method`, `params`, `ip`, `created_at`) VALUES (19, '17', '邓平', '编辑组织信息', 'com.seeds.admin.controller.SysOrgController.modify', '[{\"id\":27,\"parentOrgId\":0,\"orgName\":\"test1\",\"ownerId\":\"28\",\"ownerName\":\"\",\"sort\":0}]', '127.0.0.1', 1659921464206);
INSERT INTO `sys_log` (`id`, `operator`, `operator_name`, `operation`, `method`, `params`, `ip`, `created_at`) VALUES (20, '9', '宋沁峰', '添加组织', 'com.seeds.admin.controller.SysOrgController.add', '[{\"orgName\":\"测试\",\"ownerId\":\"9\",\"ownerName\":\"宋沁峰\",\"comments\":\"123\"}]', '192.168.1.100', 1659941040978);
INSERT INTO `sys_log` (`id`, `operator`, `operator_name`, `operation`, `method`, `params`, `ip`, `created_at`) VALUES (21, '9', '宋沁峰', '添加组织', 'com.seeds.admin.controller.SysOrgController.add', '[{\"orgName\":\"测试1\",\"ownerId\":\"10\",\"ownerName\":\"贺伟\",\"comments\":\"123\"}]', '192.168.1.100', 1659941251369);
INSERT INTO `sys_log` (`id`, `operator`, `operator_name`, `operation`, `method`, `params`, `ip`, `created_at`) VALUES (22, '9', '宋沁峰', '添加组织', 'com.seeds.admin.controller.SysOrgController.add', '[{\"orgName\":\"测试3\",\"ownerId\":\"10\",\"ownerName\":\"贺伟\",\"comments\":\"123\"}]', '192.168.1.100', 1659941283191);
INSERT INTO `sys_log` (`id`, `operator`, `operator_name`, `operation`, `method`, `params`, `ip`, `created_at`) VALUES (23, '9', '宋沁峰', '添加组织', 'com.seeds.admin.controller.SysOrgController.add', '[{\"orgName\":\"测试5\",\"ownerId\":\"12\",\"ownerName\":\"ssss\",\"comments\":\"34343\"}]', '192.168.1.100', 1659941325528);
INSERT INTO `sys_log` (`id`, `operator`, `operator_name`, `operation`, `method`, `params`, `ip`, `created_at`) VALUES (24, '9', '宋沁峰', '编辑组织信息', 'com.seeds.admin.controller.SysOrgController.modify', '[{\"id\":8,\"orgName\":\"宣禾科技\",\"ownerId\":\"1\",\"ownerName\":\"admin\",\"comments\":\"备注备注备注\"}]', '192.168.1.100', 1659941333264);
INSERT INTO `sys_log` (`id`, `operator`, `operator_name`, `operation`, `method`, `params`, `ip`, `created_at`) VALUES (25, '9', '宋沁峰', '编辑组织信息', 'com.seeds.admin.controller.SysOrgController.modify', '[{\"id\":22,\"orgName\":\"testOrg1\",\"ownerId\":\"1\",\"ownerName\":\"张三\",\"comments\":\"备注\"}]', '192.168.1.100', 1659941349343);
INSERT INTO `sys_log` (`id`, `operator`, `operator_name`, `operation`, `method`, `params`, `ip`, `created_at`) VALUES (26, '9', '宋沁峰', '编辑组织信息', 'com.seeds.admin.controller.SysOrgController.modify', '[{\"id\":22,\"orgName\":\"testOrg12\",\"ownerId\":\"1\",\"ownerName\":\"张三\",\"comments\":\"备注\"}]', '192.168.1.100', 1659941466533);
INSERT INTO `sys_log` (`id`, `operator`, `operator_name`, `operation`, `method`, `params`, `ip`, `created_at`) VALUES (27, '9', '宋沁峰', '编辑组织信息', 'com.seeds.admin.controller.SysOrgController.modify', '[{\"id\":22,\"orgName\":\"testOrg126\",\"ownerId\":\"1\",\"ownerName\":\"张三\",\"comments\":\"备注\"}]', '192.168.1.100', 1659941472194);
INSERT INTO `sys_log` (`id`, `operator`, `operator_name`, `operation`, `method`, `params`, `ip`, `created_at`) VALUES (28, '9', '宋沁峰', '删除组织', 'com.seeds.admin.controller.SysOrgController.delete', '[{\"ids\":[1551814073165942784,1551814211401814016,1551814205185855488,1551814208377720832]}]', '192.168.1.100', 1659942277278);
INSERT INTO `sys_log` (`id`, `operator`, `operator_name`, `operation`, `method`, `params`, `ip`, `created_at`) VALUES (29, '9', '宋沁峰', '删除组织', 'com.seeds.admin.controller.SysOrgController.delete', '[{\"ids\":[28,29,30,31]}]', '192.168.1.100', 1659942724641);
INSERT INTO `sys_log` (`id`, `operator`, `operator_name`, `operation`, `method`, `params`, `ip`, `created_at`) VALUES (32, '10', '贺伟', '编辑组织信息', 'com.seeds.admin.controller.SysOrgController.modify', '[{\"id\":8,\"orgName\":\"宣禾科技\",\"ownerId\":\"10\",\"ownerName\":\"贺伟\",\"comments\":\"备注备注备注\"}]', '192.168.1.30', 1660096309466);
INSERT INTO `sys_log` (`id`, `operator`, `operator_name`, `operation`, `method`, `params`, `ip`, `created_at`) VALUES (33, '10', '贺伟', '编辑组织信息', 'com.seeds.admin.controller.SysOrgController.modify', '[{\"id\":8,\"orgName\":\"宣禾科技\",\"ownerId\":\"10\",\"ownerName\":\"贺伟\",\"comments\":\"备注备注备注\"}]', '192.168.1.30', 1660096319425);
INSERT INTO `sys_log` (`id`, `operator`, `operator_name`, `operation`, `method`, `params`, `ip`, `created_at`) VALUES (34, '10', '贺伟', '编辑组织信息', 'com.seeds.admin.controller.SysOrgController.modify', '[{\"id\":8,\"orgName\":\"宣禾科技\",\"ownerId\":\"1\",\"ownerName\":\"管理员\",\"comments\":\"备注备注备注\"}]', '192.168.1.30', 1660096330316);
INSERT INTO `sys_log` (`id`, `operator`, `operator_name`, `operation`, `method`, `params`, `ip`, `created_at`) VALUES (35, '1', '管理员', '添加组织', 'com.seeds.admin.controller.SysOrgController.add', '[{\"orgName\":\"测试6\",\"ownerName\":\"\"}]', '192.168.1.12', 1660210580249);
INSERT INTO `sys_log` (`id`, `operator`, `operator_name`, `operation`, `method`, `params`, `ip`, `created_at`) VALUES (36, '1', '管理员', '删除组织', 'com.seeds.admin.controller.SysOrgController.delete', '[{\"ids\":[32]}]', '192.168.1.12', 1660210617403);
INSERT INTO `sys_log` (`id`, `operator`, `operator_name`, `operation`, `method`, `params`, `ip`, `created_at`) VALUES (37, '1', '管理员', '添加组织', 'com.seeds.admin.controller.SysOrgController.add', '[{\"orgName\":\"测试组\",\"ownerName\":\"\"}]', '192.168.1.12', 1660210639684);
INSERT INTO `sys_log` (`id`, `operator`, `operator_name`, `operation`, `method`, `params`, `ip`, `created_at`) VALUES (38, '1', '管理员', '添加组织', 'com.seeds.admin.controller.SysOrgController.add', '[{\"orgName\":\"测试8组\",\"ownerName\":\"\",\"comments\":\"1\"}]', '192.168.1.12', 1660210780572);
INSERT INTO `sys_log` (`id`, `operator`, `operator_name`, `operation`, `method`, `params`, `ip`, `created_at`) VALUES (39, '1', '管理员', '添加组织', 'com.seeds.admin.controller.SysOrgController.add', '[{\"orgName\":\"测试9组\",\"ownerName\":\"\"}]', '192.168.1.12', 1660210981757);
INSERT INTO `sys_log` (`id`, `operator`, `operator_name`, `operation`, `method`, `params`, `ip`, `created_at`) VALUES (40, '1', '管理员', '添加组织', 'com.seeds.admin.controller.SysOrgController.add', '[{\"orgName\":\"测试11组\",\"ownerName\":\"\"}]', '192.168.1.12', 1660269428567);
INSERT INTO `sys_log` (`id`, `operator`, `operator_name`, `operation`, `method`, `params`, `ip`, `created_at`) VALUES (41, '10', '贺伟', '添加组织', 'com.seeds.admin.controller.SysOrgController.add', '[{\"orgName\":\"test\",\"ownerName\":\"\"}]', '127.0.0.1', 1660269804754);
INSERT INTO `sys_log` (`id`, `operator`, `operator_name`, `operation`, `method`, `params`, `ip`, `created_at`) VALUES (42, '10', '贺伟', '添加组织', 'com.seeds.admin.controller.SysOrgController.add', '[{\"orgName\":\"org\",\"ownerName\":\"\"}]', '127.0.0.1', 1660269840355);
INSERT INTO `sys_log` (`id`, `operator`, `operator_name`, `operation`, `method`, `params`, `ip`, `created_at`) VALUES (43, '1', '管理员', '添加组织', 'com.seeds.admin.controller.SysOrgController.add', '[{\"orgName\":\"测试12组\",\"ownerName\":\"\"}]', '192.168.1.12', 1660875859544);
COMMIT;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `parent_code` varchar(64) DEFAULT NULL COMMENT '父菜单编码，一级菜单为空',
  `code` varchar(64) DEFAULT NULL COMMENT '菜单编码',
  `name` varchar(64) DEFAULT NULL COMMENT '菜单名称',
  `url` varchar(64) DEFAULT NULL COMMENT '菜单URL',
  `permissions` varchar(255) DEFAULT NULL COMMENT '授权(多个用逗号分隔，如：sys:user:list,sys:user:save)',
  `type` tinyint(1) DEFAULT NULL COMMENT '类型   0：菜单   1：按钮',
  `icon` varchar(64) DEFAULT NULL COMMENT '菜单图标',
  `show_flag` tinyint(1) DEFAULT '1' COMMENT '是否展示   0：否   1：是',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `created_by` bigint(20) DEFAULT NULL COMMENT '创建者',
  `created_at` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `updated_by` bigint(20) DEFAULT NULL COMMENT '修改者',
  `updated_at` bigint(20) DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_code` (`code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=132 DEFAULT CHARSET=utf8 COMMENT='系统菜单表';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
BEGIN;
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (1, '', 'menus', 'Menus', '/menus', 'menus.show', 0, 'insertRowBelow', 1, 10, 1, 1657951165876, 9, 1659688190465);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (2, 'menus', 'menus_list', '列表', NULL, 'sys:menu:list', 1, NULL, 1, 1, 1, 1657951165876, 1, 1657951165876);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (3, 'menus', 'menus_add', '添加', NULL, 'sys:menu:add', 1, NULL, 1, 2, 1, 1657951165876, 1, 1657951165876);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (4, 'menus', 'menus_delete', '删除', NULL, 'sys:menu:delete', 1, NULL, 1, 3, 1, 1658128944159, 1, 1658128944159);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (5, 'menus', 'menus_detail', '信息', NULL, 'sys:menu:detail', 1, NULL, 1, 4, 1, 1658128944159, 1, 1658128944159);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (6, 'menus', 'menus_modify', '编辑', NULL, 'sys:menu:modify', 1, NULL, 1, 5, 1, 1658128944159, 1, 1658128944159);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (7, '', 'role', 'Role', '/role', 'role.show', 0, 'team', 1, 12, 1, 1657951165876, 9, 1661479097779);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (8, 'role', 'role_add', '添加', NULL, 'sys:role:add', 1, NULL, 1, 1, 1, 1657951165876, 1, 1657951165876);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (9, 'role', 'role_delete', '删除', NULL, 'sys:role:delete', 1, NULL, 1, 2, 1, 1657951165876, 1, 1657951165876);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (10, 'role', 'role_detail', '信息', NULL, 'sys:role:detail', 1, NULL, 1, 3, 1, 1657951165876, 1, 1657951165876);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (11, 'role', 'role_list', '列表', NULL, 'sys:role:list', 1, NULL, 1, 4, 1, 1657951165876, 1, 1657951165876);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (12, 'role', 'role_page', '分页', NULL, 'sys:role:page', 1, NULL, 1, 5, 1, 1657951165876, 1, 1657951165876);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (13, 'role', 'role_modify', '编辑', NULL, 'sys:role:modify', 1, NULL, 1, 6, 1, 1657951165876, 1, 1657951165876);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (14, '', 'user', 'User', '/user', '', 0, 'user', 1, 10, 1, 1657951165876, 9, 1661479124220);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (15, 'user', 'user_add', '添加', NULL, 'sys:user:add', 1, NULL, 1, 1, 1, 1657951165876, 1, 1657951165876);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (16, 'user', 'user_modify', '编辑', NULL, 'sys:user:modify', 1, NULL, 1, 2, 1, 1657951165876, 1, 1657951165876);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (17, 'user', 'user_detail', '信息', NULL, 'sys:user:detail', 1, NULL, 1, 3, 1, 1657951165876, 1, 1657951165876);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (18, 'user', 'user_delete', '删除', NULL, 'sys:user:delete', 1, NULL, 1, 4, 1, 1657951165876, 1, 1657951165876);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (19, 'user', 'user_switch', '启用/停用', NULL, 'sys:user:switch', 1, NULL, 1, 5, 1, 1657951165876, 1, 1657951165876);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (20, 'user', 'user_page', '分页', NULL, 'sys:user:page', 1, NULL, 1, 6, 1, 1657951165876, 1, 1657951165876);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (21, 'user', 'user_updateRoles', '修改角色', NULL, 'sys:user:updateRoles', 1, NULL, 1, 7, 1, 1657951165876, 1, 1657951165876);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (22, 'user', 'user_changePassword', '修改密码', NULL, 'sys:user:changePassword', 1, NULL, 1, 8, 1, 1657951165876, 1, 1657951165876);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (23, 'user', 'user_resetPassword', '重置密码', NULL, 'sys:user:resetPassword', 1, NULL, 1, 9, 1, 1657951165876, 1, 1657951165876);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (25, '', 'org', 'Dept', '/org', '', 0, 'cluster', 1, 11, 1, 1657951165876, 9, 1661479065133);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (26, 'org', 'org_add', '添加', NULL, 'sys:org:add', 1, NULL, 1, 1, 1, 1657951165876, 1, 1657951165876);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (27, 'org', 'org_delete', '删除', NULL, 'sys:org:delete', 1, NULL, 1, 2, 1, 1657951165876, 1, 1657951165876);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (28, 'org', 'org_detail', '信息', NULL, 'sys:org:detail', 1, NULL, 1, 3, 1, 1657951165876, 1, 1657951165876);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (29, 'org', 'org_list', '列表', NULL, 'sys:org:list', 1, NULL, 1, 4, 1, 1657951165876, 1, 1657951165876);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (30, 'org', 'ogrg_modify', '编辑', NULL, 'sys:org:modify', 1, NULL, 1, 5, 1, 1657951165876, 1, 1657951165876);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (31, '', 'merchant', 'Merchant', '/merchant', '', 0, 'bank', 1, 9, 1, 1657951165876, 9, 1661479116516);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (32, 'merchant', 'merchant_add', '添加', NULL, 'sys:merchant:add', 1, NULL, 1, 1, 1, 1657951165876, 1, 1657951165876);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (33, 'merchant', 'merchant_modify', '编辑', NULL, 'sys:merchant:modify', 1, NULL, 1, 2, 1, 1657951165876, 1, 1657951165876);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (34, 'merchant', 'merchant_delete', '删除', NULL, 'sys:merchant:delete', 1, NULL, 1, 3, 1, 1657951165876, 1, 1657951165876);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (35, 'merchant', 'merchant_detail', '信息', NULL, 'sys:merchant:detail', 1, NULL, 1, 4, 1, 1657951165876, 1, 1657951165876);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (36, 'merchant', 'merchant_list', '列表', NULL, 'sys:merchant:list', 1, NULL, 1, 5, 1, 1657951165876, 1, 1657951165876);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (37, 'merchant', 'merchant_page', '分页', NULL, 'sys:merchant:page', 1, NULL, 1, 6, 1, 1657951165876, 1, 1657951165876);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (38, 'merchant', 'merchant_switch', '停用/启用', NULL, 'sys:merchant:switch', 1, NULL, 1, 7, 1, 1657951165876, 1, 1657951165876);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (39, 'merchant', 'merchant_addUser', '添加用户', NULL, 'sys:merchant:addUser', 1, NULL, 1, 8, 1, 1657951165876, 1, 1657951165876);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (40, 'merchant', 'merchant_deleteUser', '删除用户', NULL, 'sys:merchant:deleteUser', 1, NULL, 1, 9, 1, 1657951165876, 1, 1657951165876);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (41, 'merchant', 'merchant_addGame', '添加游戏', NULL, 'sys:merchant:addGame', 1, NULL, 1, 10, 1, 1657951165876, 1, 1657951165876);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (42, 'merchant', 'merchant_deleteGame', '删除游戏', NULL, 'sys:merchant:deleteGame', 1, NULL, 1, 11, 1, 1657951165876, 1, 1657951165876);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (44, 'game', 'game_add', '添加', NULL, 'sys:game:add', 1, NULL, 1, 1, 1, 1657951165876, 1, 1657951165876);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (45, 'game', 'game_modify', '修改', NULL, 'sys:game:modify', 1, NULL, 1, 2, 1, 1657951165876, 1, 1657951165876);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (46, 'game', 'game_delete', '删除', NULL, 'sys:game:delete', 1, NULL, 1, 3, 1, 1657951165876, 1, 1657951165876);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (47, 'game', 'game_detail', '信息', NULL, 'sys:game:detail', 1, NULL, 1, 4, 1, 1657951165876, 1, 1657951165876);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (48, 'game', 'game_select', '选取', NULL, 'sys:game:select', 1, NULL, 1, 5, 1, 1657951165876, 1, 1657951165876);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (49, 'game', 'game_page', '分页', NULL, 'sys:game:page', 1, NULL, 1, 6, 1, 1657951165876, 1, 1657951165876);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (50, 'game', 'game_switch', '停用/启用', NULL, 'sys:game:switch', 1, NULL, 1, 7, 1, 1657951165876, 1, 1657951165876);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (52, 'menus', 'menus_select', '选取', NULL, 'sys:menu:select', 1, NULL, 1, 6, 1, 1657951165876, 1, 1657951165876);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (56, NULL, 'game', 'Game', '/game', '', 0, 'rocket', 1, 5, 1, 1657951165876, 12, 1660012219423);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (57, NULL, 'nftType', 'NFT Category', '/nftType', '', 0, 'table', 1, 3, 1, 1657951165876, 9, 1661477438344);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (58, 'nftType', 'nftType_add', '添加', NULL, 'sys:nftType:add', 1, NULL, 1, 1, 1, 1657951165876, 1, 1657951165876);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (59, 'nftType', 'nftType_modify', '修改', NULL, 'sys:nftType:modify', 1, NULL, 1, 2, 1, 1657951165876, 1, 1657951165876);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (60, 'nftType', 'nftType_delete', '删除', NULL, 'sys:nftType:delete', 1, NULL, 1, 3, 1, 1657951165876, 1, 1657951165876);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (61, 'nftType', 'nftType_detail', '信息', NULL, 'sys:nftType:detail', 1, NULL, 1, 4, 1, 1657951165876, 1, 1657951165876);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (62, 'nftType', 'nftType_list', '列表', NULL, 'sys:nftType:list', 1, NULL, 1, 5, 1, 1657951165876, 1, 1657951165876);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (63, 'nftType', 'nftType_switch', '停用/启用', NULL, 'sys:nftType:switch', 1, NULL, 1, 6, 1, 1657951165876, 1, 1657951165876);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (64, NULL, 'nft', 'NFT', '/nft', '', 0, 'crown', 1, 2, 1, 1657951165876, 9, 1661477432064);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (65, 'nft', 'nft_add', '添加', NULL, 'sys:nft:add', 1, NULL, 1, 1, 1, 1657951165876, 1, 1657951165876);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (66, 'nft', 'nft_modify', '编辑', NULL, 'sys:nft:modify', 1, NULL, 1, 2, 1, 1657951165876, 1, 1657951165876);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (67, 'nft', 'nft_delete', '删除', NULL, 'sys:nft:delete', 1, NULL, 1, 3, 1, 1657951165876, 1, 1657951165876);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (68, 'nft', 'nft_detail', '信息', NULL, 'sys:nft:detail', 1, NULL, 1, 4, 1, 1657951165876, 1, 1657951165876);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (69, 'nft', 'nft_page', '分页', NULL, 'sys:nft:page', 1, NULL, 1, 5, 1, 1657951165876, 1, 1657951165876);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (70, 'nft', 'nft_switch', '停用/启用', NULL, 'sys:nft:switch', 1, NULL, 1, 6, 1, 1657951165876, 1, 1657951165876);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (71, 'role', 'role_assign', '分配', NULL, 'sys:role:assign', 1, NULL, 1, 7, 1, 1657951165876, 1, 1657951165876);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (72, NULL, 'file', '文件', '/file', '', 0, 'copy', 0, 99, 1, 1657951165876, 9, 1661479135554);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (73, 'file', 'file_upload', '上传', NULL, 'sys:file:upload', 1, NULL, 1, 1, 1, 1657951165876, 1, 1657951165876);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (74, 'file', 'file_download', '下载', NULL, 'sys:file:download', 1, NULL, 1, 2, 1, 1657951165876, 1, 1657951165876);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (75, 'file', 'file_link', '链接', NULL, 'sys:file:link', 1, NULL, 1, 3, 1, 1657951165876, 1, 1657951165876);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (76, 'file', 'file_delete', '删除', NULL, 'sys:file:delete', 1, NULL, 1, 4, 1, 1657951165876, 1, 1657951165876);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (78, NULL, 'dictType', '字典类型', '/dictType', '', 0, NULL, 0, 99, 1, 1657951165876, 9, 1661477135218);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (79, 'dictType', 'dictType_add', '添加', '', 'sys:dictType:add', 1, NULL, 1, 1, 1, 1657951165876, 1, 1660297451478);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (80, 'dictType', 'dictType_modify', '编辑', NULL, 'sys:dictType:modify', 1, NULL, 1, 2, 1, 1657951165876, 1, 1657951165876);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (81, 'dictType', 'dictType_delete', '删除', NULL, 'sys:dictType:delete', 1, NULL, 1, 3, 1, 1657951165876, 1, 1657951165876);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (82, 'dictType', 'dictType_detail', '信息', NULL, 'sys:dictType:detail', 1, NULL, 1, 4, 1, 1657951165876, 1, 1657951165876);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (83, NULL, 'game_comments', 'Game Comments', '/game-comments', '', 0, 'fire', 1, 8, 1, 1657951165876, 9, 1661421196605);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (84, 'game_comments', 'game_comments_add', '添加', '', 'sys:game-comments:add', 1, NULL, 1, 1, 1, 1657951165876, 9, 1661393950657);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (85, 'game_comments', 'game_comments_modify', '编辑', '', 'sys:game-comments:modify', 1, NULL, 1, 2, 1, 1657951165876, 9, 1661393957119);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (86, 'game_comments', 'game_comments_delete', '删除', '', 'sys:game-comments:delete', 1, NULL, 1, 3, 1, 1657951165876, 9, 1661393961798);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (87, 'game_comments', 'game_comments_detail', '信息', '', 'sys:game-comments:detail', 1, NULL, 1, 4, 1, 1657951165876, 9, 1661393966833);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (88, 'game_comments', 'game_comments_page', '分页', '', 'sys:game-comments:page', 1, NULL, 1, 5, 1, 1657951165876, 9, 1661393972537);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (89, NULL, 'home', 'Home', '/home', '', 0, 'home', 1, 0, 12, 1660012164897, 12, 1660012164897);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (90, 'game_comments', 'game_comments_switch', '停用/启用', '', 'sys:game-comments:switch', 1, NULL, 1, 6, 1, 1657951165876, 9, 1661393979484);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (92, 'forum', 'forum_add', '添加', NULL, NULL, 1, '', 1, NULL, 1, 1660202070626, 1, 1660202070626);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (101, 'nftType', 'nftType_select', '选取', NULL, 'sys:nftType:select', 1, NULL, 1, 7, 1, 1660202070626, 1, 1660202070626);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (102, NULL, 'sys_log', '操作日志', '/log', '', 0, NULL, 0, 99, 1, 1660202070626, 9, 1661477131259);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (103, 'sys_log', 'sys_log_page', '分页', NULL, 'sys:log:page', 1, NULL, 1, 1, 1, 1660202070626, 1, 1660202070626);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (105, NULL, 'nftPT', 'NFT Attribute Category', '/nft-properties-type', '', 0, 'database', 1, 4, 1, 1660202070626, 9, 1661477441872);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (106, 'nftPT', 'nftPT_page', '分页', NULL, 'sys:nftPT:page', 1, NULL, 1, 1, 1, 1660202070626, 1, 1660202070626);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (107, 'nftPT', 'nftPT_add', '添加', NULL, 'sys:nftPT:add', 1, NULL, 1, 2, 1, 1660202070626, 1, 1660202070626);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (108, 'nftPT', 'nftPT_detail', '信息', NULL, 'sys:nftPT:detail', 1, NULL, 1, 3, 1, 1660202070626, 1, 1660202070626);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (109, 'nftPT', 'nftPT_modify', '编辑', NULL, 'sys:nftPT:modify', 1, NULL, 1, 4, 1, 1660202070626, 1, 1660202070626);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (110, 'nftPT', 'nftPT_delete', '删除', NULL, 'sys:nftPT:delete', 1, NULL, 1, 5, 1, 1660202070626, 1, 1660202070626);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (111, NULL, 'dictData', NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, 9, 1661562643093);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (112, 'dictData', 'dictDate_add', '添加', NULL, 'sys:dictData:add', 1, NULL, 1, 1, 1, 1660202070626, 1, 1660202070626);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (113, 'dictData', 'dictDate_delete', '删除', NULL, 'sys:dictData:delete', 1, NULL, 1, 2, 1, 1660202070626, 1, 1660202070626);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (114, 'dictData', 'dictDate_detail', '信息', NULL, 'sys:dictData:detail', 1, NULL, 1, 3, 1, 1660202070626, 1, 1660202070626);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (115, 'dictData', 'dictDate_modify', '编辑', NULL, 'sys:dictData:modify', 1, NULL, 1, 4, 1, 1660202070626, 1, 1660202070626);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (116, 'dictData', 'dictDate_page', '分页', NULL, 'sys:dictData:page', 1, NULL, 1, 5, 1, 1660202070626, 1, 1660202070626);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (117, 'dictData', 'dictDate_list', '列表', NULL, 'sys:dictData:list', 1, NULL, 1, 6, 1, 1660202070626, 1, 1660202070626);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (119, 'menus', 'menus_switch', '启用/停用', NULL, 'sys:menu:switch', 1, NULL, 1, 7, 1, 1660202070626, 1, 1660202070626);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (120, NULL, 'game-type', 'Game Category', '/gameType', '', 0, 'robot', 1, 6, 9, 1661477399247, 9, 1661481259823);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (125, 'game-type', 'game-type-add', '新增', NULL, 'sys:gameType:add', 1, '', 1, 1, 9, 1661480775952, 9, 1661480775952);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (126, 'game-type', 'game-type-page', '列表', NULL, 'sys:gameType:list', 1, '', 1, 2, 9, 1661480802282, 9, 1661480802282);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (127, 'game-type', 'game-type-delete', '删除', NULL, 'sys:gameType:delete', 1, '', 1, 3, 9, 1661480821728, 9, 1661480821728);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (128, 'game-type', 'game-type-modify', '编辑', NULL, 'sys:gameType:modify', 1, '', 1, 4, 9, 1661480845993, 9, 1661480845993);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (129, 'game-type', 'game-type-detail', '信息', NULL, 'sys:gameType:detail', 1, NULL, 1, 5, 1, 1661480845993, 1, 1661480845993);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (130, 'game-type', 'game-type-select', '选取', NULL, 'sys:gameType:select', 1, NULL, 1, 6, 1, 1661480845993, 1, 1661480845993);
INSERT INTO `sys_menu` (`id`, `parent_code`, `code`, `name`, `url`, `permissions`, `type`, `icon`, `show_flag`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (131, 'game-type', 'game-type-switch', '启用/停用', NULL, 'sys:gameType:switch', 1, NULL, 1, 7, 1, 1661480845993, 1, 1661480845993);
COMMIT;

-- ----------------------------
-- Table structure for sys_merchant
-- ----------------------------
DROP TABLE IF EXISTS `sys_merchant`;
CREATE TABLE `sys_merchant` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(32) DEFAULT NULL COMMENT '商家名称',
  `leader_id` bigint(20) DEFAULT NULL COMMENT '负责人id',
  `mobile` varchar(32) DEFAULT NULL COMMENT '联系方式',
  `url` varchar(255) DEFAULT NULL COMMENT '网站地址',
  `status` tinyint(1) DEFAULT NULL COMMENT '商家状态  0：停用   1：正常',
  `delete_flag` tinyint(1) DEFAULT '0' COMMENT '删除标记  1：已删除   0：未删除',
  `created_by` bigint(20) DEFAULT NULL COMMENT '创建者',
  `created_at` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `updated_by` bigint(20) DEFAULT NULL COMMENT '修改者',
  `updated_at` bigint(20) DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_url` (`url`,`delete_flag`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8 COMMENT='系统商家表';

-- ----------------------------
-- Records of sys_merchant
-- ----------------------------
BEGIN;
INSERT INTO `sys_merchant` (`id`, `name`, `leader_id`, `mobile`, `url`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (1, '百度首页', 21, '13658107538', 'www.baidu.com', 1, 0, 2, 1658901752498, 1, 1660037684798);
INSERT INTO `sys_merchant` (`id`, `name`, `leader_id`, `mobile`, `url`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (2, '张三的公司', 6, '13333333333', 'www.zhangsan.com', 1, NULL, 2, 1658911688474, 2, 1658911688474);
INSERT INTO `sys_merchant` (`id`, `name`, `leader_id`, `mobile`, `url`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (3, '李四的公司', 7, '13666666666', 'www.lisicompany.com', 1, NULL, 2, 1658911849136, 2, 1658911849136);
INSERT INTO `sys_merchant` (`id`, `name`, `leader_id`, `mobile`, `url`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (4, '李四和余航和周易的分公司', 7, '13666666666', 'www.lisicompanyfen.com', 1, NULL, 2, 1658912083436, 2, 1658973442617);
INSERT INTO `sys_merchant` (`id`, `name`, `leader_id`, `mobile`, `url`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (5, '李四的分公司a', 7, '13666666666', 'www.lisicompanyfena.com', 1, NULL, 2, 1658912264002, 2, 1658973442629);
INSERT INTO `sys_merchant` (`id`, `name`, `leader_id`, `mobile`, `url`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (6, '李四的分公司b', 7, '13666666666', 'www.lisicompanyfena.com', 1, NULL, 2, 1658912446422, 2, 1658973442637);
INSERT INTO `sys_merchant` (`id`, `name`, `leader_id`, `mobile`, `url`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (7, '李四的分公司c', 7, '13666666666', 'www.lisicompanyfenc.com', 1, NULL, 2, 1658912911697, 2, 1658973442648);
INSERT INTO `sys_merchant` (`id`, `name`, `leader_id`, `mobile`, `url`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (8, '李四的分公司d', 7, '13666666666', 'www.lisicompanyfend.com', 1, NULL, 2, 1658912962772, 2, 1658973442663);
INSERT INTO `sys_merchant` (`id`, `name`, `leader_id`, `mobile`, `url`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (9, '王五和李四的公司', 8, '13555555555', 'www.wangwu.com', 0, NULL, 2, 1658913768916, 2, 1658973442672);
INSERT INTO `sys_merchant` (`id`, `name`, `leader_id`, `mobile`, `url`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (10, '张三和李四的分公司', 6, '13333333333', 'www.zhangsanfenb.com', 0, NULL, 2, 1658914000517, 2, 1658973442678);
INSERT INTO `sys_merchant` (`id`, `name`, `leader_id`, `mobile`, `url`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (11, '张三和李四的公司', 6, '13333333333', 'www.zhangsanfena.com', 1, NULL, 2, 1658914081845, 2, 1658973442682);
INSERT INTO `sys_merchant` (`id`, `name`, `leader_id`, `mobile`, `url`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (12, '张三的分公司', 6, '1213413', 'www.zhangsanfen.com', 0, NULL, 2, 1658914183960, 2, 1658973442696);
INSERT INTO `sys_merchant` (`id`, `name`, `leader_id`, `mobile`, `url`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (13, '张三的分公司b', 6, '13333333333', 'www.zhangsanfenb.com', 1, NULL, 17, 1658914183962, 17, 1659669832476);
INSERT INTO `sys_merchant` (`id`, `name`, `leader_id`, `mobile`, `url`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (14, '菜单管理56', 9, '18380493239', 'www.123.com', 1, NULL, 9, 1659950932468, 9, 1659951013664);
INSERT INTO `sys_merchant` (`id`, `name`, `leader_id`, `mobile`, `url`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (15, '菜单管理', 2, '18380493239', 'www.123.com', 1, NULL, 9, 1659951243389, 9, 1659951243389);
INSERT INTO `sys_merchant` (`id`, `name`, `leader_id`, `mobile`, `url`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (16, '菜单管理', 9, '17748499930', 'www.123.com', 1, NULL, 9, 1659951286461, 9, 1659951286461);
INSERT INTO `sys_merchant` (`id`, `name`, `leader_id`, `mobile`, `url`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (17, '菜单管理', 9, '18380493239', 'www.1213.com', 1, NULL, 9, 1659951409262, 9, 1659951409262);
INSERT INTO `sys_merchant` (`id`, `name`, `leader_id`, `mobile`, `url`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (18, '菜单管理', 9, '18380493239', 'www.123.com', 1, NULL, 9, 1659951764329, 9, 1659951764329);
INSERT INTO `sys_merchant` (`id`, `name`, `leader_id`, `mobile`, `url`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (22, '张三的分公司E', 44, '2', 'www.zhangsanfenE.com', 1, 0, 1, 1660117516228, 2, 1660530593520);
INSERT INTO `sys_merchant` (`id`, `name`, `leader_id`, `mobile`, `url`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (32, '张三的分公司G', 34, '12', 'www.zhangsanfenF.com', 1, 0, 1, 1660198763242, 1, 1660204754616);
INSERT INTO `sys_merchant` (`id`, `name`, `leader_id`, `mobile`, `url`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (37, '张三的分公司H', 35, '14725836912', 'www.zhangsanfenH.com', 1, 0, 1, 1660211191045, 1, 1660211214098);
INSERT INTO `sys_merchant` (`id`, `name`, `leader_id`, `mobile`, `url`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (38, '。。。。。。。。。。', 2, '13348867336', 'www.akjakja.com', 1, 0, 1, 1660285449636, 1, 1660285449636);
INSERT INTO `sys_merchant` (`id`, `name`, `leader_id`, `mobile`, `url`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (39, '@@@@@@@@@@@@@@@@@', 21, '13348867336', 'www.ljchkaca.com', 1, 0, 1, 1660285511449, 1, 1660285511449);
INSERT INTO `sys_merchant` (`id`, `name`, `leader_id`, `mobile`, `url`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (40, '123sdace@中国', 21, '13348867336', 'kcckcaclnace', 1, 0, 1, 1660285644499, 1, 1660285644499);
INSERT INTO `sys_merchant` (`id`, `name`, `leader_id`, `mobile`, `url`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (41, '---------------------', 28, '13348867336', '，，，，，，，，，，', 1, 0, 1, 1660285748661, 1, 1660286165387);
INSERT INTO `sys_merchant` (`id`, `name`, `leader_id`, `mobile`, `url`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (42, 'Merchant Name ：张三的分公司C', 28, '13348867336', 'www.123.com', 1, 0, 1, 1661739656575, 1, 1661739656575);
INSERT INTO `sys_merchant` (`id`, `name`, `leader_id`, `mobile`, `url`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (43, 'Merchant Name ：张三的分公司D', 21, '15229163679', 'www.1234.com', 1, 0, 1, 1661739974668, 1, 1661739974668);
INSERT INTO `sys_merchant` (`id`, `name`, `leader_id`, `mobile`, `url`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (44, '测试222', 9, '18988888888', 'www.bilibili.com', 1, 0, 9, 1661741027701, 9, 1661741027701);
COMMIT;

-- ----------------------------
-- Table structure for sys_merchant_game
-- ----------------------------
DROP TABLE IF EXISTS `sys_merchant_game`;
CREATE TABLE `sys_merchant_game` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `merchant_id` bigint(20) DEFAULT NULL COMMENT '商家ID',
  `game_id` bigint(20) DEFAULT NULL COMMENT '游戏ID',
  `created_by` bigint(20) DEFAULT NULL COMMENT '创建者',
  `created_at` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `updated_by` bigint(20) DEFAULT NULL COMMENT '修改者',
  `updated_at` bigint(20) DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商家与游戏对应关系表';

-- ----------------------------
-- Records of sys_merchant_game
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_merchant_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_merchant_user`;
CREATE TABLE `sys_merchant_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `merchant_id` bigint(20) DEFAULT NULL COMMENT '商家id',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `created_by` bigint(20) DEFAULT NULL COMMENT '创建者',
  `created_at` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `updated_by` bigint(20) DEFAULT NULL COMMENT '修改者',
  `updated_at` bigint(20) DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8 COMMENT='商家与用户关联表';

-- ----------------------------
-- Records of sys_merchant_user
-- ----------------------------
BEGIN;
INSERT INTO `sys_merchant_user` (`id`, `merchant_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (1, 1, 2, 2, 1658901752505, 2, 1658901752505);
INSERT INTO `sys_merchant_user` (`id`, `merchant_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (2, 1, 21, 9, 1659949739555, 9, 1659949739555);
INSERT INTO `sys_merchant_user` (`id`, `merchant_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (12, 21, 26, 1, 1660094839619, 1, 1660094839619);
INSERT INTO `sys_merchant_user` (`id`, `merchant_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (13, 22, 28, 1, 1660117516233, 1, 1660117516233);
INSERT INTO `sys_merchant_user` (`id`, `merchant_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (14, 23, 27, 1, 1660117592582, 1, 1660117592582);
INSERT INTO `sys_merchant_user` (`id`, `merchant_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (16, 22, 29, 1, 1660118509819, 1, 1660118509819);
INSERT INTO `sys_merchant_user` (`id`, `merchant_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (17, 22, 30, 1, 1660118533073, 1, 1660118533073);
INSERT INTO `sys_merchant_user` (`id`, `merchant_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (18, 22, 31, 1, 1660121011954, 1, 1660121011954);
INSERT INTO `sys_merchant_user` (`id`, `merchant_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (19, 25, 28, 1, 1660198586762, 1, 1660198586762);
INSERT INTO `sys_merchant_user` (`id`, `merchant_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (20, 26, 28, 1, 1660198601841, 1, 1660198601841);
INSERT INTO `sys_merchant_user` (`id`, `merchant_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (21, 27, 28, 1, 1660198621370, 1, 1660198621370);
INSERT INTO `sys_merchant_user` (`id`, `merchant_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (22, 28, 28, 1, 1660198681694, 1, 1660198681694);
INSERT INTO `sys_merchant_user` (`id`, `merchant_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (23, 29, 28, 1, 1660198693861, 1, 1660198693861);
INSERT INTO `sys_merchant_user` (`id`, `merchant_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (24, 30, 28, 1, 1660198707501, 1, 1660198707501);
INSERT INTO `sys_merchant_user` (`id`, `merchant_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (25, 31, 28, 1, 1660198721951, 1, 1660198721951);
INSERT INTO `sys_merchant_user` (`id`, `merchant_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (26, 32, 29, 1, 1660198763245, 1, 1660198763245);
INSERT INTO `sys_merchant_user` (`id`, `merchant_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (27, 33, 28, 1, 1660199599800, 1, 1660199599800);
INSERT INTO `sys_merchant_user` (`id`, `merchant_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (28, 34, 28, 1, 1660199612759, 1, 1660199612759);
INSERT INTO `sys_merchant_user` (`id`, `merchant_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (29, 35, 28, 1, 1660199635476, 1, 1660199635476);
INSERT INTO `sys_merchant_user` (`id`, `merchant_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (30, 36, 28, 1, 1660200199528, 1, 1660200199528);
INSERT INTO `sys_merchant_user` (`id`, `merchant_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (31, 32, 33, 1, 1660201119948, 1, 1660201119948);
INSERT INTO `sys_merchant_user` (`id`, `merchant_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (32, 32, 27, 1, 1660201239241, 1, 1660201239241);
INSERT INTO `sys_merchant_user` (`id`, `merchant_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (33, 32, 34, 1, 1660204754612, 1, 1660204754612);
INSERT INTO `sys_merchant_user` (`id`, `merchant_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (34, 37, 7, 1, 1660211191049, 1, 1660211191049);
INSERT INTO `sys_merchant_user` (`id`, `merchant_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (35, 37, 35, 1, 1660211214093, 1, 1660211214093);
INSERT INTO `sys_merchant_user` (`id`, `merchant_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (36, 38, 2, 1, 1660285449641, 1, 1660285449641);
INSERT INTO `sys_merchant_user` (`id`, `merchant_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (37, 39, 21, 1, 1660285511453, 1, 1660285511453);
INSERT INTO `sys_merchant_user` (`id`, `merchant_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (38, 40, 21, 1, 1660285644502, 1, 1660285644502);
INSERT INTO `sys_merchant_user` (`id`, `merchant_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (39, 41, 2, 1, 1660285748663, 1, 1660285748663);
INSERT INTO `sys_merchant_user` (`id`, `merchant_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (40, 41, 28, 1, 1660285767924, 1, 1660285767924);
INSERT INTO `sys_merchant_user` (`id`, `merchant_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (41, 22, 44, 2, 1660530593492, 2, 1660530593492);
INSERT INTO `sys_merchant_user` (`id`, `merchant_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (42, 42, 28, 1, 1661739656594, 1, 1661739656594);
INSERT INTO `sys_merchant_user` (`id`, `merchant_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (43, 43, 21, 1, 1661739974672, 1, 1661739974672);
INSERT INTO `sys_merchant_user` (`id`, `merchant_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (44, 44, 9, 9, 1661741027704, 9, 1661741027704);
COMMIT;

-- ----------------------------
-- Table structure for sys_nft
-- ----------------------------
DROP TABLE IF EXISTS `sys_nft`;
CREATE TABLE `sys_nft` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `number` varchar(32) DEFAULT NULL COMMENT '编号',
  `url` varchar(255) DEFAULT NULL COMMENT '图片链接',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `name` varchar(32) DEFAULT NULL COMMENT 'NFT名称',
  `game_id` bigint(20) DEFAULT NULL COMMENT '游戏id',
  `nft_type_id` bigint(20) DEFAULT NULL COMMENT '类别id',
  `price` decimal(10,2) DEFAULT NULL COMMENT '价格',
  `unit` varchar(32) DEFAULT NULL COMMENT '单位 USDC',
  `status` tinyint(1) DEFAULT '0' COMMENT '是否在售  0：否   1：是',
  `init_status` tinyint(1) DEFAULT NULL COMMENT '状态  0：正常  1：创建中  2：创建失败  3：修改中  4：修改失败  3：删除中  4：删除失败 ',
  `views` bigint(20) DEFAULT '0' COMMENT '浏览量',
  `collections` bigint(20) DEFAULT '0' COMMENT '收藏量',
  `owner_type` tinyint(1) DEFAULT '0' COMMENT '归属方类型  0：平台  1：uc用户',
  `error_msg` varchar(255) DEFAULT NULL COMMENT '错误信息',
  `owner_id` bigint(20) DEFAULT NULL COMMENT '归属人id',
  `owner_name` varchar(50) DEFAULT NULL COMMENT '归属人名称',
  `contract_address` varchar(255) DEFAULT NULL COMMENT '合约地址',
  `token_id` varchar(125) DEFAULT NULL COMMENT '令牌id',
  `token_standard` varchar(32) DEFAULT NULL COMMENT '令牌类型',
  `blockChain` varchar(32) DEFAULT NULL COMMENT '区块链',
  `metadata` varchar(32) DEFAULT NULL COMMENT '元数据',
  `creator_fees` decimal(10,2) DEFAULT NULL COMMENT '创建人报酬比例',
  `delete_flag` tinyint(1) DEFAULT '0' COMMENT '删除标记  1：已删除   0：未删除',
  `created_by` bigint(20) DEFAULT NULL COMMENT '创建者',
  `created_at` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `updated_by` bigint(20) DEFAULT NULL COMMENT '修改者',
  `updated_at` bigint(20) DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_number` (`number`,`delete_flag`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=83 DEFAULT CHARSET=utf8 COMMENT='系统NFT表';

-- ----------------------------
-- Records of sys_nft
-- ----------------------------
BEGIN;
INSERT INTO `sys_nft` (`id`, `number`, `url`, `description`, `name`, `game_id`, `nft_type_id`, `price`, `unit`, `status`, `init_status`, `views`, `collections`, `owner_type`, `error_msg`, `owner_id`, `owner_name`, `contract_address`, `token_id`, `token_standard`, `blockChain`, `metadata`, `creator_fees`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (29, '#0000030', 'https://cloudflare-ipfs.com/ipfs/QmPLRhULQRKmhqyBAStEMXKN2NhoegWJyGQXMrSkxbmPLt', '描述2333', '测试2333', 1, 1, 1234.00, 'USDC', 0, 5, 1, 0, 0, NULL, NULL, NULL, '0x32f1DA1bdF22c8445Db224C38c6ec5Ed92F24e25', '17', 'ERC-1155', 'Binance Test Chain', 'Decentralized', 5.00, NULL, 2, 1661163639433, 0, 1661412804648);
INSERT INTO `sys_nft` (`id`, `number`, `url`, `description`, `name`, `game_id`, `nft_type_id`, `price`, `unit`, `status`, `init_status`, `views`, `collections`, `owner_type`, `error_msg`, `owner_id`, `owner_name`, `contract_address`, `token_id`, `token_standard`, `blockChain`, `metadata`, `creator_fees`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (31, '#0000032', 'https://cloudflare-ipfs.com/ipfs/QmPLRhULQRKmhqyBAStEMXKN2NhoegWJyGQXMrSkxbmPLt', '测', '发', 1, 7, 12.00, 'USDC', 0, 5, NULL, NULL, 0, NULL, NULL, NULL, '0x32f1DA1bdF22c8445Db224C38c6ec5Ed92F24e25', '18', 'ERC-1155', 'Binance Test Chain', 'Decentralized', 5.00, NULL, 2, 1661163773406, 0, 1661412820486);
INSERT INTO `sys_nft` (`id`, `number`, `url`, `description`, `name`, `game_id`, `nft_type_id`, `price`, `unit`, `status`, `init_status`, `views`, `collections`, `owner_type`, `error_msg`, `owner_id`, `owner_name`, `contract_address`, `token_id`, `token_standard`, `blockChain`, `metadata`, `creator_fees`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (34, '#0000035', 'https://cloudflare-ipfs.com/ipfs/QmPLRhULQRKmhqyBAStEMXKN2NhoegWJyGQXMrSkxbmPLt', '测', '发', 1, 7, 12.00, 'USDC', 0, 5, NULL, NULL, 0, NULL, NULL, NULL, '0x32f1DA1bdF22c8445Db224C38c6ec5Ed92F24e25', '20', 'ERC-1155', 'Binance Test Chain', 'Decentralized', 5.00, NULL, 2, 1661163967746, 0, 1661412826249);
INSERT INTO `sys_nft` (`id`, `number`, `url`, `description`, `name`, `game_id`, `nft_type_id`, `price`, `unit`, `status`, `init_status`, `views`, `collections`, `owner_type`, `error_msg`, `owner_id`, `owner_name`, `contract_address`, `token_id`, `token_standard`, `blockChain`, `metadata`, `creator_fees`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (38, '#0000039', 'https://cloudflare-ipfs.com/ipfs/QmPLRhULQRKmhqyBAStEMXKN2NhoegWJyGQXMrSkxbmPLt', '描述', '测试1213', 1, 7, 2.22, 'USDC', 0, 5, NULL, NULL, 0, NULL, NULL, NULL, '0x32f1DA1bdF22c8445Db224C38c6ec5Ed92F24e25', '21', 'ERC-1155', 'Binance Test Chain', 'Decentralized', 5.00, NULL, 2, 1661216915816, 0, 1661413190752);
INSERT INTO `sys_nft` (`id`, `number`, `url`, `description`, `name`, `game_id`, `nft_type_id`, `price`, `unit`, `status`, `init_status`, `views`, `collections`, `owner_type`, `error_msg`, `owner_id`, `owner_name`, `contract_address`, `token_id`, `token_standard`, `blockChain`, `metadata`, `creator_fees`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (39, '#0000040', 'https://cloudflare-ipfs.com/ipfs/QmPLRhULQRKmhqyBAStEMXKN2NhoegWJyGQXMrSkxbmPLt', '描述', '测试1213', 1, 7, 2.22, 'USDC', 0, 5, NULL, NULL, 0, NULL, NULL, NULL, '0x32f1DA1bdF22c8445Db224C38c6ec5Ed92F24e25', '22', 'ERC-1155', 'Binance Test Chain', 'Decentralized', 5.00, NULL, 2, 1661216944744, 0, 1661413188772);
INSERT INTO `sys_nft` (`id`, `number`, `url`, `description`, `name`, `game_id`, `nft_type_id`, `price`, `unit`, `status`, `init_status`, `views`, `collections`, `owner_type`, `error_msg`, `owner_id`, `owner_name`, `contract_address`, `token_id`, `token_standard`, `blockChain`, `metadata`, `creator_fees`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (40, '#0000041', 'https://cloudflare-ipfs.com/ipfs/QmPLRhULQRKmhqyBAStEMXKN2NhoegWJyGQXMrSkxbmPLt', '描述', '测试1213', 1, 7, 2.22, 'USDC', 0, 5, NULL, NULL, 0, NULL, NULL, NULL, '0x32f1DA1bdF22c8445Db224C38c6ec5Ed92F24e25', '23', 'ERC-1155', 'Binance Test Chain', 'Decentralized', 5.00, NULL, 2, 1661216965751, 0, 1661413186573);
INSERT INTO `sys_nft` (`id`, `number`, `url`, `description`, `name`, `game_id`, `nft_type_id`, `price`, `unit`, `status`, `init_status`, `views`, `collections`, `owner_type`, `error_msg`, `owner_id`, `owner_name`, `contract_address`, `token_id`, `token_standard`, `blockChain`, `metadata`, `creator_fees`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (41, '#0000042', 'https://cloudflare-ipfs.com/ipfs/QmPLRhULQRKmhqyBAStEMXKN2NhoegWJyGQXMrSkxbmPLt', '描述', '测试1213', 1, 7, 2.22, 'USDC', 0, 6, NULL, NULL, 0, 'chain operation failed', NULL, NULL, '0x32f1DA1bdF22c8445Db224C38c6ec5Ed92F24e25', '16', 'ERC-1155', 'Binance Test Chain', 'Decentralized', 5.00, 0, 2, 1661217021786, 1, 1661306673768);
INSERT INTO `sys_nft` (`id`, `number`, `url`, `description`, `name`, `game_id`, `nft_type_id`, `price`, `unit`, `status`, `init_status`, `views`, `collections`, `owner_type`, `error_msg`, `owner_id`, `owner_name`, `contract_address`, `token_id`, `token_standard`, `blockChain`, `metadata`, `creator_fees`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (49, '#0000050', 'https://cloudflare-ipfs.com/ipfs/QmYX9krfMtMmpkq5UpV5Pd53ag7tcJ4fKNzcRrtUJUXYX5', '测试233', '测试233', 1, 7, 233.00, 'USDC', 0, 5, NULL, NULL, 0, 'Read timed out executing GET https://gateway.pinata.cloud/ipfs/Qmef8xjAdrEQYYn8ju4XYVpiMEadsC38a48RGavWwUhgg4', NULL, NULL, '0x32f1DA1bdF22c8445Db224C38c6ec5Ed92F24e25', '34', 'ERC-1155', 'Binance Test Chain', 'Decentralized', 5.00, NULL, 2, 1661238164008, 0, 1661412751881);
INSERT INTO `sys_nft` (`id`, `number`, `url`, `description`, `name`, `game_id`, `nft_type_id`, `price`, `unit`, `status`, `init_status`, `views`, `collections`, `owner_type`, `error_msg`, `owner_id`, `owner_name`, `contract_address`, `token_id`, `token_standard`, `blockChain`, `metadata`, `creator_fees`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (50, '#0000051', 'https://cloudflare-ipfs.com/ipfs/QmYX9krfMtMmpkq5UpV5Pd53ag7tcJ4fKNzcRrtUJUXYX5', '描述', '测试update', 1, 7, 222.00, 'USCD', 0, 6, NULL, NULL, 0, 'chain operation failed', NULL, NULL, '0x32f1DA1bdF22c8445Db224C38c6ec5Ed92F24e25', '35', 'ERC-1155', 'Binance Test Chain', 'Decentralized', 5.00, NULL, 2, 1661238476947, 2, 1661238476947);
INSERT INTO `sys_nft` (`id`, `number`, `url`, `description`, `name`, `game_id`, `nft_type_id`, `price`, `unit`, `status`, `init_status`, `views`, `collections`, `owner_type`, `error_msg`, `owner_id`, `owner_name`, `contract_address`, `token_id`, `token_standard`, `blockChain`, `metadata`, `creator_fees`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (51, '#0000052', 'https://cloudflare-ipfs.com/ipfs/QmYSAxmEHt3ezHXgwJCYfT3jk8YHGLQr1gTRCdC2hRdfd1', 'my first nft1', 'MyRabbit', 3, 2, 10.00, 'USDC', 0, 0, NULL, NULL, 1, NULL, 21, '0x85bdaffe9e48f643ed1f0f920dcf6fc019204013', '0x32f1DA1bdF22c8445Db224C38c6ec5Ed92F24e25', '38', 'ERC-1155', 'Binance Test Chain', 'Decentralized', 5.00, 0, 10, 1661307297508, 0, 1661569658084);
INSERT INTO `sys_nft` (`id`, `number`, `url`, `description`, `name`, `game_id`, `nft_type_id`, `price`, `unit`, `status`, `init_status`, `views`, `collections`, `owner_type`, `error_msg`, `owner_id`, `owner_name`, `contract_address`, `token_id`, `token_standard`, `blockChain`, `metadata`, `creator_fees`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (53, '#0000054', 'https://cloudflare-ipfs.com/ipfs/QmdfPQw6Ya1q6fGe7PVgETwNtJwfbhYmR5C8zPn53jVa9Z', '刀，近身武器', '刀', 3, 6, 2.10, 'USDC', 1, 0, NULL, NULL, 1, NULL, 20, '0x84f9f225bd824c4fea9d33095674f82611ee3325', '0x32f1DA1bdF22c8445Db224C38c6ec5Ed92F24e25', '40', 'ERC-1155', 'Binance Test Chain', 'Decentralized', 5.00, 0, 1, 1661307438143, 0, 1661492767601);
INSERT INTO `sys_nft` (`id`, `number`, `url`, `description`, `name`, `game_id`, `nft_type_id`, `price`, `unit`, `status`, `init_status`, `views`, `collections`, `owner_type`, `error_msg`, `owner_id`, `owner_name`, `contract_address`, `token_id`, `token_standard`, `blockChain`, `metadata`, `creator_fees`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (54, '#0000055', 'https://cloudflare-ipfs.com/ipfs/QmdfPQw6Ya1q6fGe7PVgETwNtJwfbhYmR5C8zPn53jVa9Z', '刀，近身武器', '刀', 3, 6, 2.10, 'USDC', 1, 5, NULL, NULL, 0, NULL, NULL, NULL, '0x32f1DA1bdF22c8445Db224C38c6ec5Ed92F24e25', '41', 'ERC-1155', 'Binance Test Chain', 'Decentralized', 5.00, NULL, 1, 1661307441861, 0, 1661322095410);
INSERT INTO `sys_nft` (`id`, `number`, `url`, `description`, `name`, `game_id`, `nft_type_id`, `price`, `unit`, `status`, `init_status`, `views`, `collections`, `owner_type`, `error_msg`, `owner_id`, `owner_name`, `contract_address`, `token_id`, `token_standard`, `blockChain`, `metadata`, `creator_fees`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (55, '#0000056', 'https://cloudflare-ipfs.com/ipfs/Qmc339zHseitR1x52JcHnt28CamYp3DjMDecRZ3rmiWjKM', '刀，近身防身武器', '刀', 3, 6, 2.30, 'USDC', 0, 5, NULL, NULL, 0, NULL, NULL, NULL, '0x32f1DA1bdF22c8445Db224C38c6ec5Ed92F24e25', '42', 'ERC-1155', 'Binance Test Chain', 'Decentralized', 5.00, NULL, 1, 1661308074968, 0, 1661321754149);
INSERT INTO `sys_nft` (`id`, `number`, `url`, `description`, `name`, `game_id`, `nft_type_id`, `price`, `unit`, `status`, `init_status`, `views`, `collections`, `owner_type`, `error_msg`, `owner_id`, `owner_name`, `contract_address`, `token_id`, `token_standard`, `blockChain`, `metadata`, `creator_fees`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (56, '#0000057', 'https://cloudflare-ipfs.com/ipfs/Qmbbg3f57T4MmssE9krs3fL4qkW5FQtbuLrhkExhAgqggK', 'this is a nft', 'NFT233', 3, 12, 111.00, 'USDC', 1, 0, 45, 15, 1, NULL, 20, '0x84f9f225bd824c4fea9d33095674f82611ee3325', '0x32f1DA1bdF22c8445Db224C38c6ec5Ed92F24e25', '43', 'ERC-1155', 'Binance Test Chain', 'Decentralized', 5.00, 0, 9, 1661311310431, 0, 1661492753171);
INSERT INTO `sys_nft` (`id`, `number`, `url`, `description`, `name`, `game_id`, `nft_type_id`, `price`, `unit`, `status`, `init_status`, `views`, `collections`, `owner_type`, `error_msg`, `owner_id`, `owner_name`, `contract_address`, `token_id`, `token_standard`, `blockChain`, `metadata`, `creator_fees`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (57, '#0000058', 'https://cloudflare-ipfs.com/ipfs/Qmbbg3f57T4MmssE9krs3fL4qkW5FQtbuLrhkExhAgqggK', 'this is a nftggg', 'NFT233gg', 3, 12, 111.00, 'USDC', 0, 0, NULL, NULL, 0, NULL, NULL, NULL, '0x32f1DA1bdF22c8445Db224C38c6ec5Ed92F24e25', '44', 'ERC-1155', 'Binance Test Chain', 'Decentralized', 5.00, 0, 9, 1661311320951, 0, 1661396454984);
INSERT INTO `sys_nft` (`id`, `number`, `url`, `description`, `name`, `game_id`, `nft_type_id`, `price`, `unit`, `status`, `init_status`, `views`, `collections`, `owner_type`, `error_msg`, `owner_id`, `owner_name`, `contract_address`, `token_id`, `token_standard`, `blockChain`, `metadata`, `creator_fees`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (58, '#0000059', 'https://cloudflare-ipfs.com/ipfs/QmYX9krfMtMmpkq5UpV5Pd53ag7tcJ4fKNzcRrtUJUXYX5', '侧耳', '侧耳', 1, 7, 22.00, 'USDC', 0, 5, NULL, NULL, 0, NULL, NULL, NULL, '0x32f1DA1bdF22c8445Db224C38c6ec5Ed92F24e25', '45', 'ERC-1155', 'Binance Test Chain', 'Decentralized', 5.00, NULL, 2, 1661321951972, 0, 1661322045817);
INSERT INTO `sys_nft` (`id`, `number`, `url`, `description`, `name`, `game_id`, `nft_type_id`, `price`, `unit`, `status`, `init_status`, `views`, `collections`, `owner_type`, `error_msg`, `owner_id`, `owner_name`, `contract_address`, `token_id`, `token_standard`, `blockChain`, `metadata`, `creator_fees`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (59, '#0000060', 'https://cloudflare-ipfs.com/ipfs/QmepXWiGitT2y1ozaj4DBz8vxkwwfmaj34yzWMMnTcNBRp', '213123', '测试', 9, 12, 111.00, 'USDC', 1, 0, NULL, NULL, 1, NULL, 20, '0x84f9f225bd824c4fea9d33095674f82611ee3325', '0x32f1DA1bdF22c8445Db224C38c6ec5Ed92F24e25', '46', 'ERC-1155', 'Binance Test Chain', 'Decentralized', 5.00, 0, 9, 1661410025058, 0, 1661492760305);
INSERT INTO `sys_nft` (`id`, `number`, `url`, `description`, `name`, `game_id`, `nft_type_id`, `price`, `unit`, `status`, `init_status`, `views`, `collections`, `owner_type`, `error_msg`, `owner_id`, `owner_name`, `contract_address`, `token_id`, `token_standard`, `blockChain`, `metadata`, `creator_fees`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (60, '#0000061', 'https://cloudflare-ipfs.com/ipfs/Qmc339zHseitR1x52JcHnt28CamYp3DjMDecRZ3rmiWjKM', 'nft', 'NFT215', 9, 3, 10.20, 'USDC', 0, 5, 0, 0, 0, NULL, NULL, NULL, '0x32f1DA1bdF22c8445Db224C38c6ec5Ed92F24e25', '47', 'ERC-1155', 'Binance Test Chain', 'Decentralized', 5.00, NULL, 1, 1661412542039, 0, 1661412625003);
INSERT INTO `sys_nft` (`id`, `number`, `url`, `description`, `name`, `game_id`, `nft_type_id`, `price`, `unit`, `status`, `init_status`, `views`, `collections`, `owner_type`, `error_msg`, `owner_id`, `owner_name`, `contract_address`, `token_id`, `token_standard`, `blockChain`, `metadata`, `creator_fees`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (61, '#0000062', 'https://cloudflare-ipfs.com/ipfs/Qmc339zHseitR1x52JcHnt28CamYp3DjMDecRZ3rmiWjKM', 'nft', 'NFT215', 9, 3, 10.20, 'USDC', 0, 0, 2, 0, 1, NULL, 20, '0x84f9f225bd824c4fea9d33095674f82611ee3325', '0x32f1DA1bdF22c8445Db224C38c6ec5Ed92F24e25', '48', 'ERC-1155', 'Binance Test Chain', 'Decentralized', 5.00, 0, 1, 1661412546986, 0, 1661487392305);
INSERT INTO `sys_nft` (`id`, `number`, `url`, `description`, `name`, `game_id`, `nft_type_id`, `price`, `unit`, `status`, `init_status`, `views`, `collections`, `owner_type`, `error_msg`, `owner_id`, `owner_name`, `contract_address`, `token_id`, `token_standard`, `blockChain`, `metadata`, `creator_fees`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (62, '#0000063', 'https://cloudflare-ipfs.com/ipfs/QmP4FSSACyKtxLHtM9YtggjN6qYMz39U3LTaGd8Hq1NBf4', '123123', 'NFT23', 9, 12, 123.00, 'USDC', 0, 4, 0, 0, 0, 'chain operation failed', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 9, 1661416540960, 9, 1661416540960);
INSERT INTO `sys_nft` (`id`, `number`, `url`, `description`, `name`, `game_id`, `nft_type_id`, `price`, `unit`, `status`, `init_status`, `views`, `collections`, `owner_type`, `error_msg`, `owner_id`, `owner_name`, `contract_address`, `token_id`, `token_standard`, `blockChain`, `metadata`, `creator_fees`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (63, '#0000064', 'https://cloudflare-ipfs.com/ipfs/QmP4FSSACyKtxLHtM9YtggjN6qYMz39U3LTaGd8Hq1NBf4', 'asfdsaf', 'fsaf', 9, 12, 123.00, 'USDC', 0, 6, 0, 0, 0, 'chain operation failed', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 9, 1661419613873, 9, 1661419613873);
INSERT INTO `sys_nft` (`id`, `number`, `url`, `description`, `name`, `game_id`, `nft_type_id`, `price`, `unit`, `status`, `init_status`, `views`, `collections`, `owner_type`, `error_msg`, `owner_id`, `owner_name`, `contract_address`, `token_id`, `token_standard`, `blockChain`, `metadata`, `creator_fees`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (64, '#0000065', 'https://cloudflare-ipfs.com/ipfs/QmP4FSSACyKtxLHtM9YtggjN6qYMz39U3LTaGd8Hq1NBf4', 'asfdsaf', 'fsaf', 9, 12, 123.00, 'USDC', 0, 0, 0, 0, 0, NULL, NULL, NULL, '0x32f1DA1bdF22c8445Db224C38c6ec5Ed92F24e25', '49', 'ERC-1155', 'Binance Test Chain', 'Decentralized', 5.00, 0, 9, 1661419619162, 1, 1661493665887);
INSERT INTO `sys_nft` (`id`, `number`, `url`, `description`, `name`, `game_id`, `nft_type_id`, `price`, `unit`, `status`, `init_status`, `views`, `collections`, `owner_type`, `error_msg`, `owner_id`, `owner_name`, `contract_address`, `token_id`, `token_standard`, `blockChain`, `metadata`, `creator_fees`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (65, '#0000066', 'https://cloudflare-ipfs.com/ipfs/QmP4FSSACyKtxLHtM9YtggjN6qYMz39U3LTaGd8Hq1NBf4', '123123', 'gfdgfdg', 8, 19, 123.00, 'USDC', 0, 0, 115, 5, 1, NULL, 20, '0x84f9f225bd824c4fea9d33095674f82611ee3325', '0x32f1DA1bdF22c8445Db224C38c6ec5Ed92F24e25', '50', 'ERC-1155', 'Binance Test Chain', 'Decentralized', 5.00, 0, 9, 1661419776804, 0, 1661495091641);
INSERT INTO `sys_nft` (`id`, `number`, `url`, `description`, `name`, `game_id`, `nft_type_id`, `price`, `unit`, `status`, `init_status`, `views`, `collections`, `owner_type`, `error_msg`, `owner_id`, `owner_name`, `contract_address`, `token_id`, `token_standard`, `blockChain`, `metadata`, `creator_fees`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (66, '#0000067', 'https://cloudflare-ipfs.com/ipfs/QmPXvhxX212jg6oW4JgGTB1X3KcZbdRaP1gnjg4jFd9MfJ', 'NFT。。。', 'NFT233', 8, 12, 233.00, 'USDC', 0, 6, 8, 0, 1, 'chain operation failed', 20, '0x84f9f225bd824c4fea9d33095674f82611ee3325', '0x32f1DA1bdF22c8445Db224C38c6ec5Ed92F24e25', '51', 'ERC-1155', 'Binance Test Chain', 'Decentralized', 5.00, 0, 1, 1661481892606, 0, 1661494107110);
INSERT INTO `sys_nft` (`id`, `number`, `url`, `description`, `name`, `game_id`, `nft_type_id`, `price`, `unit`, `status`, `init_status`, `views`, `collections`, `owner_type`, `error_msg`, `owner_id`, `owner_name`, `contract_address`, `token_id`, `token_standard`, `blockChain`, `metadata`, `creator_fees`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (67, '#0000068', 'https://cloudflare-ipfs.com/ipfs/QmUytT5ACuY8GeKtGLFxTGsqkWDnuwBfqtbAgRYecktT5n', 'nft...', 'NFT123', 9, 17, 123.00, 'USDC', 0, 5, 0, 0, 0, NULL, NULL, NULL, '0x32f1DA1bdF22c8445Db224C38c6ec5Ed92F24e25', '53', 'ERC-1155', 'Binance Test Chain', 'Decentralized', 5.00, NULL, 1, 1661483049511, 0, 1661483281623);
INSERT INTO `sys_nft` (`id`, `number`, `url`, `description`, `name`, `game_id`, `nft_type_id`, `price`, `unit`, `status`, `init_status`, `views`, `collections`, `owner_type`, `error_msg`, `owner_id`, `owner_name`, `contract_address`, `token_id`, `token_standard`, `blockChain`, `metadata`, `creator_fees`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (68, '#0000069', 'https://cloudflare-ipfs.com/ipfs/QmUytT5ACuY8GeKtGLFxTGsqkWDnuwBfqtbAgRYecktT5n', 'nft...', 'NFT123', 9, 17, 123.00, 'USDC', 1, 0, 8, 3, 1, NULL, 20, '0x84f9f225bd824c4fea9d33095674f82611ee3325', '0x32f1DA1bdF22c8445Db224C38c6ec5Ed92F24e25', '54', 'ERC-1155', 'Binance Test Chain', 'Decentralized', 5.00, 0, 1, 1661483067318, 0, 1661497997010);
INSERT INTO `sys_nft` (`id`, `number`, `url`, `description`, `name`, `game_id`, `nft_type_id`, `price`, `unit`, `status`, `init_status`, `views`, `collections`, `owner_type`, `error_msg`, `owner_id`, `owner_name`, `contract_address`, `token_id`, `token_standard`, `blockChain`, `metadata`, `creator_fees`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (69, '#0000070', 'https://cloudflare-ipfs.com/ipfs/QmX5nXZSJyVa4BZfv8encLW9sMd3baiHrDSWf1DDoyNSNa', 'NFT2335', 'NFT9527', 9, 12, 8.00, 'USDC', 0, 5, 1, 0, 0, NULL, NULL, NULL, '0x32f1DA1bdF22c8445Db224C38c6ec5Ed92F24e25', '55', 'ERC-1155', 'Binance Test Chain', 'Decentralized', 5.00, NULL, 1, 1661491670016, 0, 1661493622950);
INSERT INTO `sys_nft` (`id`, `number`, `url`, `description`, `name`, `game_id`, `nft_type_id`, `price`, `unit`, `status`, `init_status`, `views`, `collections`, `owner_type`, `error_msg`, `owner_id`, `owner_name`, `contract_address`, `token_id`, `token_standard`, `blockChain`, `metadata`, `creator_fees`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (70, '#0000071', 'https://cloudflare-ipfs.com/ipfs/QmX5nXZSJyVa4BZfv8encLW9sMd3baiHrDSWf1DDoyNSNa', 'NFT2335', 'NFT9527', 9, 12, 8.00, 'USDC', 1, 0, 0, 0, 0, NULL, NULL, NULL, '0x32f1DA1bdF22c8445Db224C38c6ec5Ed92F24e25', '56', 'ERC-1155', 'Binance Test Chain', 'Decentralized', 5.00, 1, 1, 1661491674464, 1, 1661491674464);
INSERT INTO `sys_nft` (`id`, `number`, `url`, `description`, `name`, `game_id`, `nft_type_id`, `price`, `unit`, `status`, `init_status`, `views`, `collections`, `owner_type`, `error_msg`, `owner_id`, `owner_name`, `contract_address`, `token_id`, `token_standard`, `blockChain`, `metadata`, `creator_fees`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (71, '#0000072', 'https://cloudflare-ipfs.com/ipfs/QmZszBFN9n1SLMwsiZ1PjhYu7TTZq11zREWeujUqfKne1e', '123', 'asdasd', 9, 10, 123.00, 'USDC', 0, 5, 0, 0, 0, NULL, NULL, NULL, '0x32f1DA1bdF22c8445Db224C38c6ec5Ed92F24e25', '57', 'ERC-1155', 'Binance Test Chain', 'Decentralized', 5.00, NULL, 9, 1661492786032, 0, 1661493524187);
INSERT INTO `sys_nft` (`id`, `number`, `url`, `description`, `name`, `game_id`, `nft_type_id`, `price`, `unit`, `status`, `init_status`, `views`, `collections`, `owner_type`, `error_msg`, `owner_id`, `owner_name`, `contract_address`, `token_id`, `token_standard`, `blockChain`, `metadata`, `creator_fees`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (72, '#0000073', 'https://cloudflare-ipfs.com/ipfs/QmPv92eaSB9gLFtR5LD68XKuerCfgTMzpXEHivpvB3X4Mt', 'NFT358', 'NFT123', 9, 3, 11.00, 'USDC', 0, 5, 0, 0, 0, NULL, NULL, NULL, '0x32f1DA1bdF22c8445Db224C38c6ec5Ed92F24e25', '58', 'ERC-1155', 'Binance Test Chain', 'Decentralized', 5.00, NULL, 1, 1661492905558, 0, 1661493734532);
INSERT INTO `sys_nft` (`id`, `number`, `url`, `description`, `name`, `game_id`, `nft_type_id`, `price`, `unit`, `status`, `init_status`, `views`, `collections`, `owner_type`, `error_msg`, `owner_id`, `owner_name`, `contract_address`, `token_id`, `token_standard`, `blockChain`, `metadata`, `creator_fees`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (73, '#0000074', 'https://cloudflare-ipfs.com/ipfs/QmPv92eaSB9gLFtR5LD68XKuerCfgTMzpXEHivpvB3X4Mt', 'NFT358', 'NFT123', 9, 3, 11.00, 'USDC', 0, 0, 2, 0, 1, NULL, 20, '0x84f9f225bd824c4fea9d33095674f82611ee3325', '0x32f1DA1bdF22c8445Db224C38c6ec5Ed92F24e25', '59', 'ERC-1155', 'Binance Test Chain', 'Decentralized', 5.00, 0, 1, 1661492913779, 0, 1661495116036);
INSERT INTO `sys_nft` (`id`, `number`, `url`, `description`, `name`, `game_id`, `nft_type_id`, `price`, `unit`, `status`, `init_status`, `views`, `collections`, `owner_type`, `error_msg`, `owner_id`, `owner_name`, `contract_address`, `token_id`, `token_standard`, `blockChain`, `metadata`, `creator_fees`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (74, '#0000075', 'https://cloudflare-ipfs.com/ipfs/QmRQ7RTbDyqnTEqTEoEWrUy3ikeuhQd5QK9m4wSFGs54hz', 'NFT123', 'NFT1', 8, 12, 12.00, 'USDC', 0, 0, 3, 0, 1, NULL, 20, '0x84f9f225bd824c4fea9d33095674f82611ee3325', '0x32f1DA1bdF22c8445Db224C38c6ec5Ed92F24e25', '60', 'ERC-1155', 'Binance Test Chain', 'Decentralized', 5.00, 0, 1, 1661494118936, 0, 1661494841245);
INSERT INTO `sys_nft` (`id`, `number`, `url`, `description`, `name`, `game_id`, `nft_type_id`, `price`, `unit`, `status`, `init_status`, `views`, `collections`, `owner_type`, `error_msg`, `owner_id`, `owner_name`, `contract_address`, `token_id`, `token_standard`, `blockChain`, `metadata`, `creator_fees`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (75, '#0000076', 'https://cloudflare-ipfs.com/ipfs/QmPv92eaSB9gLFtR5LD68XKuerCfgTMzpXEHivpvB3X4Mt', 'NFT03', 'NFT855', 8, 17, 8.00, 'USDC', 1, 0, 0, 0, 0, NULL, NULL, NULL, '0x32f1DA1bdF22c8445Db224C38c6ec5Ed92F24e25', '61', 'ERC-1155', 'Binance Test Chain', 'Decentralized', 5.00, 0, 1, 1661494985593, 1, 1661494985593);
INSERT INTO `sys_nft` (`id`, `number`, `url`, `description`, `name`, `game_id`, `nft_type_id`, `price`, `unit`, `status`, `init_status`, `views`, `collections`, `owner_type`, `error_msg`, `owner_id`, `owner_name`, `contract_address`, `token_id`, `token_standard`, `blockChain`, `metadata`, `creator_fees`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (76, '#0000077', 'https://cloudflare-ipfs.com/ipfs/QmRQ7RTbDyqnTEqTEoEWrUy3ikeuhQd5QK9m4wSFGs54hz', 'NFT253-护具6', '帽子66', 8, 2, 19.00, 'USDC', 0, 0, 0, 0, 0, NULL, NULL, NULL, '0x32f1DA1bdF22c8445Db224C38c6ec5Ed92F24e25', '62', 'ERC-1155', 'Binance Test Chain', 'Decentralized', 5.00, 0, 1, 1661495170500, 1, 1661495780469);
INSERT INTO `sys_nft` (`id`, `number`, `url`, `description`, `name`, `game_id`, `nft_type_id`, `price`, `unit`, `status`, `init_status`, `views`, `collections`, `owner_type`, `error_msg`, `owner_id`, `owner_name`, `contract_address`, `token_id`, `token_standard`, `blockChain`, `metadata`, `creator_fees`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (77, '#0000078', 'https://cloudflare-ipfs.com/ipfs/QmRQ7RTbDyqnTEqTEoEWrUy3ikeuhQd5QK9m4wSFGs54hz', 'NFT253-护具', 'NFT234', 8, 2, 19.00, 'USDC', 0, 5, 0, 0, 0, NULL, NULL, NULL, '0x32f1DA1bdF22c8445Db224C38c6ec5Ed92F24e25', '63', 'ERC-1155', 'Binance Test Chain', 'Decentralized', 5.00, NULL, 1, 1661495174247, 0, 1661495373796);
INSERT INTO `sys_nft` (`id`, `number`, `url`, `description`, `name`, `game_id`, `nft_type_id`, `price`, `unit`, `status`, `init_status`, `views`, `collections`, `owner_type`, `error_msg`, `owner_id`, `owner_name`, `contract_address`, `token_id`, `token_standard`, `blockChain`, `metadata`, `creator_fees`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (78, '#0000079', 'https://cloudflare-ipfs.com/ipfs/QmPv92eaSB9gLFtR5LD68XKuerCfgTMzpXEHivpvB3X4Mt', '剑，一款中型武器', '剑', 8, 7, 2.10, 'USDC', 1, 0, 0, 0, 0, NULL, NULL, NULL, '0x32f1DA1bdF22c8445Db224C38c6ec5Ed92F24e25', '64', 'ERC-1155', 'Binance Test Chain', 'Decentralized', 5.00, 0, 1, 1661495559966, 1, 1661495559966);
INSERT INTO `sys_nft` (`id`, `number`, `url`, `description`, `name`, `game_id`, `nft_type_id`, `price`, `unit`, `status`, `init_status`, `views`, `collections`, `owner_type`, `error_msg`, `owner_id`, `owner_name`, `contract_address`, `token_id`, `token_standard`, `blockChain`, `metadata`, `creator_fees`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (79, '#0000080', 'https://cloudflare-ipfs.com/ipfs/QmRQ7RTbDyqnTEqTEoEWrUy3ikeuhQd5QK9m4wSFGs54hz', '刀，重型防身武器', '大刀', 9, 8, 79.00, 'USDC', 1, 0, 0, 0, 0, NULL, NULL, NULL, '0x32f1DA1bdF22c8445Db224C38c6ec5Ed92F24e25', '65', 'ERC-1155', 'Binance Test Chain', 'Decentralized', 5.00, 0, 1, 1661495779546, 1, 1661500731736);
INSERT INTO `sys_nft` (`id`, `number`, `url`, `description`, `name`, `game_id`, `nft_type_id`, `price`, `unit`, `status`, `init_status`, `views`, `collections`, `owner_type`, `error_msg`, `owner_id`, `owner_name`, `contract_address`, `token_id`, `token_standard`, `blockChain`, `metadata`, `creator_fees`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (80, '#0000081', 'https://cloudflare-ipfs.com/ipfs/QmRQ7RTbDyqnTEqTEoEWrUy3ikeuhQd5QK9m4wSFGs54hz', '刀，重型防身武器', '大刀', 9, 8, 79.00, 'USDC', 1, 0, 1, 0, 0, NULL, NULL, NULL, '0x32f1DA1bdF22c8445Db224C38c6ec5Ed92F24e25', '66', 'ERC-1155', 'Binance Test Chain', 'Decentralized', 5.00, 0, 1, 1661495809809, 1, 1661495809809);
INSERT INTO `sys_nft` (`id`, `number`, `url`, `description`, `name`, `game_id`, `nft_type_id`, `price`, `unit`, `status`, `init_status`, `views`, `collections`, `owner_type`, `error_msg`, `owner_id`, `owner_name`, `contract_address`, `token_id`, `token_standard`, `blockChain`, `metadata`, `creator_fees`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (81, '#0000082', 'https://cloudflare-ipfs.com/ipfs/QmVqKj21np7AigNMq1Q2VSv5KJAQ5gQifx4oGkrNzwgEtJ', '111', 'asdas', 9, 19, 1.00, 'USDC', 0, 2, 0, 0, 0, 'chain operation failed', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 9, 1661564447595, 9, 1661564447595);
INSERT INTO `sys_nft` (`id`, `number`, `url`, `description`, `name`, `game_id`, `nft_type_id`, `price`, `unit`, `status`, `init_status`, `views`, `collections`, `owner_type`, `error_msg`, `owner_id`, `owner_name`, `contract_address`, `token_id`, `token_standard`, `blockChain`, `metadata`, `creator_fees`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (82, '#0000083', 'https://cloudflare-ipfs.com/ipfs/QmVqKj21np7AigNMq1Q2VSv5KJAQ5gQifx4oGkrNzwgEtJ', '234234', 'edrewr', 9, 12, 234.00, 'USDC', 0, 2, 0, 0, 0, 'chain operation failed', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 9, 1661565005424, 9, 1661565005424);
COMMIT;

-- ----------------------------
-- Table structure for sys_nft_properties
-- ----------------------------
DROP TABLE IF EXISTS `sys_nft_properties`;
CREATE TABLE `sys_nft_properties` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `nft_id` bigint(20) DEFAULT NULL COMMENT 'NFT的id',
  `type_id` bigint(20) DEFAULT NULL COMMENT 'NFT属性类型id',
  `name` varchar(64) DEFAULT NULL COMMENT 'NFT属性名称',
  `value` varchar(64) DEFAULT NULL COMMENT 'NFT属性值',
  `created_by` bigint(20) DEFAULT NULL COMMENT '创建者',
  `created_at` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `updated_by` bigint(20) DEFAULT NULL COMMENT '修改者',
  `updated_at` bigint(20) DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=106 DEFAULT CHARSET=utf8 COMMENT='NFT属性';

-- ----------------------------
-- Records of sys_nft_properties
-- ----------------------------
BEGIN;
INSERT INTO `sys_nft_properties` (`id`, `nft_id`, `type_id`, `name`, `value`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (21, 14, 1, NULL, '100', 9, 1661156454593, 9, 1661156454593);
INSERT INTO `sys_nft_properties` (`id`, `nft_id`, `type_id`, `name`, `value`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (22, 14, 2, NULL, '200', 9, 1661156454595, 9, 1661156454595);
INSERT INTO `sys_nft_properties` (`id`, `nft_id`, `type_id`, `name`, `value`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (23, 15, 1, NULL, '100', 9, 1661157213505, 9, 1661157213505);
INSERT INTO `sys_nft_properties` (`id`, `nft_id`, `type_id`, `name`, `value`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (24, 15, 2, NULL, '100', 9, 1661157213506, 9, 1661157213506);
INSERT INTO `sys_nft_properties` (`id`, `nft_id`, `type_id`, `name`, `value`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (25, 16, 1, NULL, '1000', 9, 1661158141860, 9, 1661158141860);
INSERT INTO `sys_nft_properties` (`id`, `nft_id`, `type_id`, `name`, `value`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (26, 16, 2, NULL, '1000', 9, 1661158141861, 9, 1661158141861);
INSERT INTO `sys_nft_properties` (`id`, `nft_id`, `type_id`, `name`, `value`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (27, 17, 1, NULL, '999', 9, 1661158173100, 9, 1661158173100);
INSERT INTO `sys_nft_properties` (`id`, `nft_id`, `type_id`, `name`, `value`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (28, 17, 2, NULL, '999', 9, 1661158173101, 9, 1661158173101);
INSERT INTO `sys_nft_properties` (`id`, `nft_id`, `type_id`, `name`, `value`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (29, 18, 1, NULL, '999', 9, 1661158236856, 9, 1661158236856);
INSERT INTO `sys_nft_properties` (`id`, `nft_id`, `type_id`, `name`, `value`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (30, 18, 2, NULL, '909', 9, 1661158236857, 9, 1661158236857);
INSERT INTO `sys_nft_properties` (`id`, `nft_id`, `type_id`, `name`, `value`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (31, 19, 3, NULL, '1111', 9, 1661158270073, 9, 1661158270073);
INSERT INTO `sys_nft_properties` (`id`, `nft_id`, `type_id`, `name`, `value`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (32, 19, 2, NULL, '2222', 9, 1661158270074, 9, 1661158270074);
INSERT INTO `sys_nft_properties` (`id`, `nft_id`, `type_id`, `name`, `value`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (33, 20, 1, NULL, '3433', 9, 1661158327140, 9, 1661158327140);
INSERT INTO `sys_nft_properties` (`id`, `nft_id`, `type_id`, `name`, `value`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (34, 20, 2, NULL, '3231', 9, 1661158327141, 9, 1661158327141);
INSERT INTO `sys_nft_properties` (`id`, `nft_id`, `type_id`, `name`, `value`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (35, 21, 1, NULL, '343', 9, 1661158355222, 9, 1661158355222);
INSERT INTO `sys_nft_properties` (`id`, `nft_id`, `type_id`, `name`, `value`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (36, 21, 2, NULL, '4343', 9, 1661158355224, 9, 1661158355224);
INSERT INTO `sys_nft_properties` (`id`, `nft_id`, `type_id`, `name`, `value`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (37, 22, 1, NULL, '3333', 9, 1661158379707, 9, 1661158379707);
INSERT INTO `sys_nft_properties` (`id`, `nft_id`, `type_id`, `name`, `value`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (38, 22, 3, NULL, '1111', 9, 1661158379708, 9, 1661158379708);
INSERT INTO `sys_nft_properties` (`id`, `nft_id`, `type_id`, `name`, `value`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (39, 24, 1, NULL, '111', 9, 1661160871252, 9, 1661160871252);
INSERT INTO `sys_nft_properties` (`id`, `nft_id`, `type_id`, `name`, `value`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (40, 25, 1, NULL, '222', 9, 1661161379783, 9, 1661161379783);
INSERT INTO `sys_nft_properties` (`id`, `nft_id`, `type_id`, `name`, `value`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (41, 26, 1, NULL, '111', 9, 1661163225919, 9, 1661163225919);
INSERT INTO `sys_nft_properties` (`id`, `nft_id`, `type_id`, `name`, `value`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (42, 26, 2, NULL, '222', 9, 1661163225921, 9, 1661163225921);
INSERT INTO `sys_nft_properties` (`id`, `nft_id`, `type_id`, `name`, `value`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (43, 27, 1, NULL, '22', 2, 1661163495902, 2, 1661163495902);
INSERT INTO `sys_nft_properties` (`id`, `nft_id`, `type_id`, `name`, `value`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (44, 28, 1, NULL, '22', 2, 1661163617269, 2, 1661163617269);
INSERT INTO `sys_nft_properties` (`id`, `nft_id`, `type_id`, `name`, `value`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (51, 35, 1, NULL, '2200', 2, 1661216659657, 2, 1661216659657);
INSERT INTO `sys_nft_properties` (`id`, `nft_id`, `type_id`, `name`, `value`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (52, 36, 1, NULL, '2200', 2, 1661216718262, 2, 1661216718262);
INSERT INTO `sys_nft_properties` (`id`, `nft_id`, `type_id`, `name`, `value`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (53, 37, 1, NULL, '2200', 2, 1661216732608, 2, 1661216732608);
INSERT INTO `sys_nft_properties` (`id`, `nft_id`, `type_id`, `name`, `value`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (68, 45, 1, NULL, '2200', 2, 1661234621979, 2, 1661234621979);
INSERT INTO `sys_nft_properties` (`id`, `nft_id`, `type_id`, `name`, `value`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (69, 46, 1, NULL, '2200', 2, 1661234685926, 2, 1661234685926);
INSERT INTO `sys_nft_properties` (`id`, `nft_id`, `type_id`, `name`, `value`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (70, 47, 1, NULL, '2200', 2, 1661234721566, 2, 1661234721566);
INSERT INTO `sys_nft_properties` (`id`, `nft_id`, `type_id`, `name`, `value`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (71, 48, 1, NULL, '2200', 2, 1661234831833, 2, 1661234831833);
INSERT INTO `sys_nft_properties` (`id`, `nft_id`, `type_id`, `name`, `value`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (81, 52, 2, NULL, '100', 10, 1661307305914, 10, 1661307305914);
INSERT INTO `sys_nft_properties` (`id`, `nft_id`, `type_id`, `name`, `value`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (82, 56, 2, NULL, '2222', 9, 1661311310457, 9, 1661311310457);
INSERT INTO `sys_nft_properties` (`id`, `nft_id`, `type_id`, `name`, `value`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (83, 57, 2, NULL, '2222', 9, 1661311320961, 9, 1661311320961);
INSERT INTO `sys_nft_properties` (`id`, `nft_id`, `type_id`, `name`, `value`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (84, 50, 1, NULL, '2222', 2, 1661311517092, 2, 1661311517092);
INSERT INTO `sys_nft_properties` (`id`, `nft_id`, `type_id`, `name`, `value`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (86, 59, 2, NULL, '433', 9, 1661410025067, 9, 1661410025067);
INSERT INTO `sys_nft_properties` (`id`, `nft_id`, `type_id`, `name`, `value`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (87, 62, 2, NULL, '123', 9, 1661416540964, 9, 1661416540964);
INSERT INTO `sys_nft_properties` (`id`, `nft_id`, `type_id`, `name`, `value`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (88, 63, 2, NULL, '123213', 9, 1661419613879, 9, 1661419613879);
INSERT INTO `sys_nft_properties` (`id`, `nft_id`, `type_id`, `name`, `value`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (89, 64, 2, NULL, '123213', 9, 1661419619166, 9, 1661419619166);
INSERT INTO `sys_nft_properties` (`id`, `nft_id`, `type_id`, `name`, `value`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (90, 65, 2, NULL, '123', 9, 1661419776808, 9, 1661419776808);
INSERT INTO `sys_nft_properties` (`id`, `nft_id`, `type_id`, `name`, `value`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (94, 73, 2, NULL, '789', 1, 1661492913784, 1, 1661492913784);
INSERT INTO `sys_nft_properties` (`id`, `nft_id`, `type_id`, `name`, `value`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (95, 74, 6, NULL, '5', 1, 1661494118941, 1, 1661494118941);
INSERT INTO `sys_nft_properties` (`id`, `nft_id`, `type_id`, `name`, `value`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (96, 75, 8, NULL, '50%', 1, 1661494985601, 1, 1661494985601);
INSERT INTO `sys_nft_properties` (`id`, `nft_id`, `type_id`, `name`, `value`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (99, 78, 1, NULL, '增加30%', 1, 1661495559970, 1, 1661495559970);
INSERT INTO `sys_nft_properties` (`id`, `nft_id`, `type_id`, `name`, `value`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (100, 79, 8, NULL, '伤害增加50%', 1, 1661495779549, 1, 1661495779549);
INSERT INTO `sys_nft_properties` (`id`, `nft_id`, `type_id`, `name`, `value`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (101, 80, 8, NULL, '伤害增加50%', 1, 1661495809813, 1, 1661495809813);
INSERT INTO `sys_nft_properties` (`id`, `nft_id`, `type_id`, `name`, `value`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (102, 82, 2, NULL, '234234', 9, 1661565005429, 9, 1661565005429);
INSERT INTO `sys_nft_properties` (`id`, `nft_id`, `type_id`, `name`, `value`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (103, 82, -1, NULL, 'werewrwe', 9, 1661565005429, 9, 1661565005429);
INSERT INTO `sys_nft_properties` (`id`, `nft_id`, `type_id`, `name`, `value`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (105, 76, -1, 'shengming', '2223', 9, 1661566124640, 9, 1661566124640);
COMMIT;

-- ----------------------------
-- Table structure for sys_nft_properties_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_nft_properties_type`;
CREATE TABLE `sys_nft_properties_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `code` varchar(32) DEFAULT NULL COMMENT 'NFT属性类别编码',
  `name` varchar(32) DEFAULT NULL COMMENT 'NFT属性类别名称',
  `delete_flag` tinyint(1) DEFAULT '0' COMMENT '删除标记  1：已删除   0：未删除',
  `created_by` bigint(20) DEFAULT NULL COMMENT '创建者',
  `created_at` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `updated_by` bigint(20) DEFAULT NULL COMMENT '修改者',
  `updated_at` bigint(20) DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_code` (`code`,`delete_flag`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COMMENT='NFT属性类别';

-- ----------------------------
-- Records of sys_nft_properties_type
-- ----------------------------
BEGIN;
INSERT INTO `sys_nft_properties_type` (`id`, `code`, `name`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (1, 'shanghai', '伤害', 0, 9, 1660790394625, 9, 1660790394625);
INSERT INTO `sys_nft_properties_type` (`id`, `code`, `name`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (2, 'shengming', '生命', 0, 9, 1660790412105, 9, 1660790412105);
INSERT INTO `sys_nft_properties_type` (`id`, `code`, `name`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (3, 'ouqi', '欧气值', 0, 9, 1660790808010, 9, 1660790816282);
INSERT INTO `sys_nft_properties_type` (`id`, `code`, `name`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (4, 'test', '测试', NULL, 9, 1660790822109, 9, 1660790824463);
INSERT INTO `sys_nft_properties_type` (`id`, `code`, `name`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (5, 'nuqi', '怒气', 0, 1, 1661303991739, 1, 1661303991739);
INSERT INTO `sys_nft_properties_type` (`id`, `code`, `name`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (6, 'lianji', '连击', 0, 1, 1661334311821, 9, 1661421905586);
INSERT INTO `sys_nft_properties_type` (`id`, `code`, `name`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (7, '123', '12312', NULL, 9, 1661421912054, 9, 1661421914704);
INSERT INTO `sys_nft_properties_type` (`id`, `code`, `name`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (8, 'baoji', '暴击', 0, 1, 1661483709234, 1, 1661483709234);
COMMIT;

-- ----------------------------
-- Table structure for sys_nft_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_nft_type`;
CREATE TABLE `sys_nft_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `parent_code` varchar(64) DEFAULT NULL COMMENT '父类别code，一级菜单为空',
  `code` varchar(64) DEFAULT NULL COMMENT '类别编码',
  `name` varchar(64) DEFAULT NULL COMMENT '类别名称',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `status` tinyint(1) DEFAULT NULL COMMENT '是否有效  0：否   1：是',
  `delete_flag` tinyint(1) DEFAULT '0' COMMENT '删除标记  1：已删除   0：未删除',
  `created_by` bigint(20) DEFAULT NULL COMMENT '创建者',
  `created_at` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `updated_by` bigint(20) DEFAULT NULL COMMENT '修改者',
  `updated_at` bigint(20) DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_code` (`code`,`delete_flag`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8 COMMENT='NFT类别表';

-- ----------------------------
-- Records of sys_nft_type
-- ----------------------------
BEGIN;
INSERT INTO `sys_nft_type` (`id`, `parent_code`, `code`, `name`, `sort`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (1, 'hero', 'clothing', '服装', 1, 1, 0, 2, 1658987567344, 2, 1658996503619);
INSERT INTO `sys_nft_type` (`id`, `parent_code`, `code`, `name`, `sort`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (2, 'clothing', 'hats', '帽子', 1, 1, 0, 2, 1658987665129, 2, 1658987665129);
INSERT INTO `sys_nft_type` (`id`, `parent_code`, `code`, `name`, `sort`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (3, 'clothing', 'pants', '裤子', 2, 1, 0, 2, 1658987699399, 2, 1658987699399);
INSERT INTO `sys_nft_type` (`id`, `parent_code`, `code`, `name`, `sort`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (4, 'clothing', 'jacket', '衣服', 3, 1, 0, 2, 1658987731337, 2, 1658987731337);
INSERT INTO `sys_nft_type` (`id`, `parent_code`, `code`, `name`, `sort`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (5, 'hero', 'weapon', '武器', 2, 1, 0, 2, 1658987769981, 2, 1658987769981);
INSERT INTO `sys_nft_type` (`id`, `parent_code`, `code`, `name`, `sort`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (6, 'weapon', 'daggers', '匕首', 1, 1, 0, 2, 1658987822312, 2, 1658987822312);
INSERT INTO `sys_nft_type` (`id`, `parent_code`, `code`, `name`, `sort`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (7, 'weapon', 'swords', '剑', 2, 1, 0, 2, 1658987883536, 2, 1658988036156);
INSERT INTO `sys_nft_type` (`id`, `parent_code`, `code`, `name`, `sort`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (8, 'weapon', 'broadsword', '大刀', 3, 1, 0, 2, 1658987883579, 2, 1658987883579);
INSERT INTO `sys_nft_type` (`id`, `parent_code`, `code`, `name`, `sort`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (9, NULL, 'game', '游戏', 1, 1, 0, 2, 1658987883579, 2, 1658987883579);
INSERT INTO `sys_nft_type` (`id`, `parent_code`, `code`, `name`, `sort`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (10, NULL, 'platform', '平台', 2, 1, 0, 2, 1658987883579, 2, 1658987883579);
INSERT INTO `sys_nft_type` (`id`, `parent_code`, `code`, `name`, `sort`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (11, 'game', 'hero', '英雄', 1, 1, 0, 2, 1658987883579, 2, 1658987883579);
INSERT INTO `sys_nft_type` (`id`, `parent_code`, `code`, `name`, `sort`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (12, 'game', 'general', '通用', 2, 1, 0, 2, 1658987883579, 2, 1658987883579);
INSERT INTO `sys_nft_type` (`id`, `parent_code`, `code`, `name`, `sort`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (13, NULL, 'test', '测试1', 0, 0, NULL, 9, 1660788243441, 9, 1660788908846);
INSERT INTO `sys_nft_type` (`id`, `parent_code`, `code`, `name`, `sort`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (14, 'test', 'maaaa', '菜单管理', 0, 0, NULL, 9, 1660788254584, 9, 1660788906405);
INSERT INTO `sys_nft_type` (`id`, `parent_code`, `code`, `name`, `sort`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (15, 'maaaa', 'menu', '测试', 0, 0, NULL, 9, 1660788267095, 9, 1660788895900);
INSERT INTO `sys_nft_type` (`id`, `parent_code`, `code`, `name`, `sort`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (16, 'game', 'liren', 'Seeds', 0, 1, NULL, 1, 1661319291878, 1, 1661319606621);
INSERT INTO `sys_nft_type` (`id`, `parent_code`, `code`, `name`, `sort`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (17, 'platform', 'Seeds', '论坛', 0, 1, 0, 1, 1661320060849, 1, 1661320111339);
INSERT INTO `sys_nft_type` (`id`, `parent_code`, `code`, `name`, `sort`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (18, NULL, 'CS论坛', '浩方', 0, 1, NULL, 1, 1661320157923, 1, 1661320164275);
INSERT INTO `sys_nft_type` (`id`, `parent_code`, `code`, `name`, `sort`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (19, 'platform', 'CS', '游戏家园', 1, 1, 0, 1, NULL, 1, 1661320605102);
INSERT INTO `sys_nft_type` (`id`, `parent_code`, `code`, `name`, `sort`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (20, NULL, 'asdas', '菜单管理', 0, 0, NULL, 9, 1661416682380, 9, 1661416710666);
INSERT INTO `sys_nft_type` (`id`, `parent_code`, `code`, `name`, `sort`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (21, 'platform', '123123', '123', 0, 0, NULL, 9, 1661416694904, 9, 1661416706442);
INSERT INTO `sys_nft_type` (`id`, `parent_code`, `code`, `name`, `sort`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (22, NULL, 'asdasd', 'asdasd', 0, 1, NULL, 9, 1661484200763, 9, 1661484205278);
INSERT INTO `sys_nft_type` (`id`, `parent_code`, `code`, `name`, `sort`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (23, 'game', '123213', '123123', 0, 1, NULL, 9, 1661484302509, 9, 1661484316612);
INSERT INTO `sys_nft_type` (`id`, `parent_code`, `code`, `name`, `sort`, `status`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (24, '123213', '3434', '123343', 0, 1, NULL, 9, 1661484311012, 9, 1661484314692);
COMMIT;

-- ----------------------------
-- Table structure for sys_org
-- ----------------------------
DROP TABLE IF EXISTS `sys_org`;
CREATE TABLE `sys_org` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `org_id` bigint(20) NOT NULL COMMENT '组织ID',
  `parent_org_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '父级组织ID',
  `org_name` varchar(64) NOT NULL COMMENT '组织名称',
  `owner_id` int(11) DEFAULT NULL COMMENT '负责人id',
  `owner_name` varchar(64) NOT NULL COMMENT '负责人',
  `comments` varchar(255) DEFAULT NULL COMMENT '备注',
  `sort` int(11) DEFAULT '0',
  `delete_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记  1：已删除   0：未删除',
  `created_by` bigint(20) NOT NULL COMMENT '创建者',
  `created_at` bigint(20) NOT NULL COMMENT '创建时间',
  `updated_by` bigint(20) NOT NULL COMMENT '修改者',
  `updated_at` bigint(20) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8 COMMENT='系统菜单表';

-- ----------------------------
-- Records of sys_org
-- ----------------------------
BEGIN;
INSERT INTO `sys_org` (`id`, `org_id`, `parent_org_id`, `org_name`, `owner_id`, `owner_name`, `comments`, `sort`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (8, 1551814073165942784, 0, '宣禾科技', 1, '管理员', '备注备注备注', 0, 0, 1, 1658816289796, 10, 1660096330310);
INSERT INTO `sys_org` (`id`, `org_id`, `parent_org_id`, `org_name`, `owner_id`, `owner_name`, `comments`, `sort`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (9, 1551814205185855488, 1551814073165942784, '研发组', 1, 'admin', '', 4, 0, 1, 1658816312488, 1, 1658816312488);
INSERT INTO `sys_org` (`id`, `org_id`, `parent_org_id`, `org_name`, `owner_id`, `owner_name`, `comments`, `sort`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (10, 1551814208377720832, 1551814073165942784, '产品组', 1, 'admin', '', 5, 0, 1, 1658816313249, 1, 1658816313249);
INSERT INTO `sys_org` (`id`, `org_id`, `parent_org_id`, `org_name`, `owner_id`, `owner_name`, `comments`, `sort`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (11, 1551814211401814016, 1551814073165942784, '测试运维组', 1, 'admin', '', 6, 0, 1, 1658816313970, 1, 1658816313970);
INSERT INTO `sys_org` (`id`, `org_id`, `parent_org_id`, `org_name`, `owner_id`, `owner_name`, `comments`, `sort`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (22, 1552526397166194688, 0, 'testOrg126', 1, '张三', '备注', 0, 0, 10, 1658986112292, 9, 1659941472185);
INSERT INTO `sys_org` (`id`, `org_id`, `parent_org_id`, `org_name`, `owner_id`, `owner_name`, `comments`, `sort`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (26, 1554649580157669376, 0, 'testOrg', 19, '贺伟', '备注', 0, 0, 10, 1659492318563, 10, 1659492318563);
INSERT INTO `sys_org` (`id`, `org_id`, `parent_org_id`, `org_name`, `owner_id`, `owner_name`, `comments`, `sort`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (33, 1557662437300375552, 0, '测试组', NULL, '', NULL, 0, 0, 1, 1660210639678, 1, 1660210639678);
INSERT INTO `sys_org` (`id`, `org_id`, `parent_org_id`, `org_name`, `owner_id`, `owner_name`, `comments`, `sort`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (34, 1557663028231671808, 0, '测试8组', NULL, '', '1', 0, 0, 1, 1660210780567, 1, 1660210780567);
INSERT INTO `sys_org` (`id`, `org_id`, `parent_org_id`, `org_name`, `owner_id`, `owner_name`, `comments`, `sort`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (35, 1557663872058527744, 0, '测试9组', NULL, '', NULL, 0, 0, 1, 1660210981752, 1, 1660210981752);
INSERT INTO `sys_org` (`id`, `org_id`, `parent_org_id`, `org_name`, `owner_id`, `owner_name`, `comments`, `sort`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (36, 1557909015743303680, 0, '测试11组', NULL, '', NULL, 0, 0, 1, 1660269428561, 1, 1660269428561);
INSERT INTO `sys_org` (`id`, `org_id`, `parent_org_id`, `org_name`, `owner_id`, `owner_name`, `comments`, `sort`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (37, 1557910593095208960, 0, 'test', NULL, '', NULL, 0, 0, 10, 1660269804636, 10, 1660269804636);
INSERT INTO `sys_org` (`id`, `org_id`, `parent_org_id`, `org_name`, `owner_id`, `owner_name`, `comments`, `sort`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (38, 1557910742873804800, 0, 'org', NULL, '', NULL, 0, 0, 10, 1660269840340, 10, 1660269840340);
INSERT INTO `sys_org` (`id`, `org_id`, `parent_org_id`, `org_name`, `owner_id`, `owner_name`, `comments`, `sort`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (39, 1560452571058016256, 0, '测试12组', NULL, '', NULL, 0, 0, 1, 1660875859408, 1, 1660875859408);
COMMIT;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `role_code` varchar(32) DEFAULT NULL COMMENT '角色编码',
  `role_name` varchar(32) DEFAULT NULL COMMENT '角色名称',
  `remark` varchar(64) DEFAULT NULL COMMENT '备注',
  `delete_flag` tinyint(1) DEFAULT '0' COMMENT '删除标记  0：已删除   1：未删除',
  `created_by` bigint(20) DEFAULT NULL COMMENT '创建者',
  `created_at` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `updated_by` bigint(20) DEFAULT NULL COMMENT '修改者',
  `updated_at` bigint(20) DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_code` (`role_code`,`delete_flag`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8 COMMENT='系统角色表';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_role` (`id`, `role_code`, `role_name`, `remark`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (1, 'developer', '观察者', '', 0, 1, 1658130368336, 1, 1658133950349);
INSERT INTO `sys_role` (`id`, `role_code`, `role_name`, `remark`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (6, 'merchant', '商家', NULL, 0, 1, 1658130368336, 1, 1658133950349);
INSERT INTO `sys_role` (`id`, `role_code`, `role_name`, `remark`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (8, '343', '343', NULL, 1, 9, 1659494422604, 9, 1659495988654);
INSERT INTO `sys_role` (`id`, `role_code`, `role_name`, `remark`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (9, '4545', '454', NULL, 1, 9, 1659494444432, 9, 1659495985654);
INSERT INTO `sys_role` (`id`, `role_code`, `role_name`, `remark`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (10, 'ceshi', '测试1', '123', 0, 9, 1659515466479, 9, 1659515466479);
INSERT INTO `sys_role` (`id`, `role_code`, `role_name`, `remark`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (11, 'ceshi2', '测试2', '123', 0, 9, 1659515480695, 9, 1659515480695);
INSERT INTO `sys_role` (`id`, `role_code`, `role_name`, `remark`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (12, 'ceshi3', '测试3', '123123', 0, 9, 1659515494865, 9, 1659515494865);
INSERT INTO `sys_role` (`id`, `role_code`, `role_name`, `remark`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (13, 'ceshi4', '测试4', 'weds', 0, 9, 1659515507830, 9, 1659515507830);
INSERT INTO `sys_role` (`id`, `role_code`, `role_name`, `remark`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (14, '234', '测试5', '234', 0, 9, 1659515507830, 9, 1659515507830);
INSERT INTO `sys_role` (`id`, `role_code`, `role_name`, `remark`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (15, 'dict', '测试6', '234', 1, 17, 1659921678055, 1, 1659937287261);
INSERT INTO `sys_role` (`id`, `role_code`, `role_name`, `remark`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (16, '123', '测试11', '123456', 1, 1, 1659937198475, 1, 1660024197659);
INSERT INTO `sys_role` (`id`, `role_code`, `role_name`, `remark`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (19, 'deng', 'yangyang', '123456', 0, 1, 1660027710561, 1, 1660114016651);
INSERT INTO `sys_role` (`id`, `role_code`, `role_name`, `remark`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (20, '123456happy', 'yangkang', NULL, 1, 1, 1660114119563, 1, 1660116687575);
INSERT INTO `sys_role` (`id`, `role_code`, `role_name`, `remark`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (22, 'wangjj250wo110', 'songqinfeng2', 'qwer', 0, 1, 1660195625656, 1, 1660196872425);
INSERT INTO `sys_role` (`id`, `role_code`, `role_name`, `remark`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (23, '邓阳', 'dengyang', NULL, 0, 1, 1660195752877, 1, 1660195752877);
INSERT INTO `sys_role` (`id`, `role_code`, `role_name`, `remark`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (24, 'yangyang520', 'songqinfeng3', NULL, 0, 1, 1660196126361, 1, 1660197161333);
INSERT INTO `sys_role` (`id`, `role_code`, `role_name`, `remark`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (25, '1', 'yangyang1', NULL, 0, 1, 1660197868453, 1, 1660197868453);
INSERT INTO `sys_role` (`id`, `role_code`, `role_name`, `remark`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (26, '2', 'yangyang2', NULL, 0, 1, 1660197877587, 1, 1660197877587);
INSERT INTO `sys_role` (`id`, `role_code`, `role_name`, `remark`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (27, '3', 'yangyang3', NULL, 0, 1, 1660197886104, 1, 1660197886104);
INSERT INTO `sys_role` (`id`, `role_code`, `role_name`, `remark`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (28, '4', 'yangyang4', NULL, 0, 1, 1660197894534, 1, 1660197894534);
INSERT INTO `sys_role` (`id`, `role_code`, `role_name`, `remark`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (29, '5', 'yangyang5', NULL, 0, 1, 1660197902158, 1, 1660197902158);
INSERT INTO `sys_role` (`id`, `role_code`, `role_name`, `remark`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (30, '6', 'yanngyang6', NULL, 0, 1, 1660197911601, 1, 1660197911601);
INSERT INTO `sys_role` (`id`, `role_code`, `role_name`, `remark`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (31, '7', 'yangyang7', NULL, 0, 1, 1660197926621, 1, 1660197926621);
INSERT INTO `sys_role` (`id`, `role_code`, `role_name`, `remark`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (32, '8', 'yangyang8', NULL, 0, 1, 1660197940913, 1, 1660197940913);
COMMIT;

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色ID',
  `menu_id` bigint(20) DEFAULT NULL COMMENT '菜单ID',
  `created_by` bigint(20) DEFAULT NULL COMMENT '创建者',
  `created_at` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `updated_by` bigint(20) DEFAULT NULL COMMENT '修改者',
  `updated_at` bigint(20) DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=102 DEFAULT CHARSET=utf8 COMMENT='系统角色与菜单对应关系表';

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
BEGIN;
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (1, 1, 1, 1, 1658133950411, 1, 1658133950411);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (2, 1, 7, 1, 1658133950422, 1, 1658133950422);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (3, 1, 14, 1, 1658133950422, 1, 1658133950422);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (31, 10, 15, 9, 1659515466506, 9, 1659515466506);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (32, 10, 16, 9, 1659515466510, 9, 1659515466510);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (33, 10, 17, 9, 1659515466513, 9, 1659515466513);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (34, 10, 18, 9, 1659515466515, 9, 1659515466515);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (35, 10, 19, 9, 1659515466518, 9, 1659515466518);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (36, 10, 20, 9, 1659515466520, 9, 1659515466520);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (37, 10, 21, 9, 1659515466522, 9, 1659515466522);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (38, 10, 22, 9, 1659515466524, 9, 1659515466524);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (39, 10, 23, 9, 1659515466527, 9, 1659515466527);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (40, 10, 26, 9, 1659515466529, 9, 1659515466529);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (41, 10, 27, 9, 1659515466531, 9, 1659515466531);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (42, 10, 28, 9, 1659515466532, 9, 1659515466532);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (43, 10, 29, 9, 1659515466535, 9, 1659515466535);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (44, 10, 30, 9, 1659515466537, 9, 1659515466537);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (45, 11, 2, 9, 1659515480701, 9, 1659515480701);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (46, 11, 3, 9, 1659515480705, 9, 1659515480705);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (47, 11, 4, 9, 1659515480708, 9, 1659515480708);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (48, 11, 5, 9, 1659515480711, 9, 1659515480711);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (49, 11, 6, 9, 1659515480714, 9, 1659515480714);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (50, 11, 52, 9, 1659515480716, 9, 1659515480716);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (51, 11, 8, 9, 1659515480718, 9, 1659515480718);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (52, 11, 9, 9, 1659515480720, 9, 1659515480720);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (53, 11, 10, 9, 1659515480722, 9, 1659515480722);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (54, 11, 11, 9, 1659515480724, 9, 1659515480724);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (55, 11, 12, 9, 1659515480725, 9, 1659515480725);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (56, 11, 13, 9, 1659515480727, 9, 1659515480727);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (57, 11, 71, 9, 1659515480729, 9, 1659515480729);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (58, 11, 15, 9, 1659515480731, 9, 1659515480731);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (59, 11, 16, 9, 1659515480733, 9, 1659515480733);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (60, 11, 17, 9, 1659515480735, 9, 1659515480735);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (61, 11, 18, 9, 1659515480737, 9, 1659515480737);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (62, 11, 19, 9, 1659515480739, 9, 1659515480739);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (63, 11, 20, 9, 1659515480741, 9, 1659515480741);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (64, 11, 21, 9, 1659515480743, 9, 1659515480743);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (65, 11, 22, 9, 1659515480745, 9, 1659515480745);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (66, 11, 23, 9, 1659515480747, 9, 1659515480747);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (67, 11, 26, 9, 1659515480749, 9, 1659515480749);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (68, 11, 27, 9, 1659515480751, 9, 1659515480751);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (69, 11, 28, 9, 1659515480752, 9, 1659515480752);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (70, 11, 29, 9, 1659515480754, 9, 1659515480754);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (71, 11, 30, 9, 1659515480755, 9, 1659515480755);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (72, 12, 73, 9, 1659515494873, 9, 1659515494873);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (73, 12, 58, 9, 1659515494875, 9, 1659515494875);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (74, 12, 59, 9, 1659515494877, 9, 1659515494877);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (75, 12, 60, 9, 1659515494879, 9, 1659515494879);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (76, 12, 61, 9, 1659515494881, 9, 1659515494881);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (77, 12, 62, 9, 1659515494883, 9, 1659515494883);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (78, 12, 63, 9, 1659515494885, 9, 1659515494885);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (79, 12, 44, 9, 1659515494887, 9, 1659515494887);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (80, 12, 45, 9, 1659515494889, 9, 1659515494889);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (81, 12, 46, 9, 1659515494891, 9, 1659515494891);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (82, 12, 47, 9, 1659515494893, 9, 1659515494893);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (83, 12, 48, 9, 1659515494895, 9, 1659515494895);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (84, 12, 49, 9, 1659515494897, 9, 1659515494897);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (85, 12, 50, 9, 1659515494898, 9, 1659515494898);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (86, 13, 8, 9, 1659515507837, 9, 1659515507837);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (87, 13, 9, 9, 1659515507838, 9, 1659515507838);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (88, 13, 10, 9, 1659515507840, 9, 1659515507840);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (89, 13, 11, 9, 1659515507842, 9, 1659515507842);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (90, 13, 12, 9, 1659515507844, 9, 1659515507844);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (91, 13, 13, 9, 1659515507847, 9, 1659515507847);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (92, 13, 71, 9, 1659515507849, 9, 1659515507849);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (93, 13, 15, 9, 1659515507850, 9, 1659515507850);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (94, 13, 16, 9, 1659515507852, 9, 1659515507852);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (95, 13, 17, 9, 1659515507853, 9, 1659515507853);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (96, 13, 18, 9, 1659515507855, 9, 1659515507855);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (97, 13, 19, 9, 1659515507856, 9, 1659515507856);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (98, 13, 20, 9, 1659515507858, 9, 1659515507858);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (99, 13, 21, 9, 1659515507860, 9, 1659515507860);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (100, 13, 22, 9, 1659515507861, 9, 1659515507861);
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (101, 13, 23, 9, 1659515507863, 9, 1659515507863);
COMMIT;

-- ----------------------------
-- Table structure for sys_role_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_user`;
CREATE TABLE `sys_role_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色ID',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `created_by` bigint(20) DEFAULT NULL COMMENT '创建者',
  `created_at` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `updated_by` bigint(20) DEFAULT NULL COMMENT '修改者',
  `updated_at` bigint(20) DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=98 DEFAULT CHARSET=utf8 COMMENT='系统角色与用户对应关系表';

-- ----------------------------
-- Records of sys_role_user
-- ----------------------------
BEGIN;
INSERT INTO `sys_role_user` (`id`, `role_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (1, 1, 2, 1, 1658135822962, 1, 1658135822962);
INSERT INTO `sys_role_user` (`id`, `role_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (6, 6, 2, 2, 1658974395005, 2, 1658974395005);
INSERT INTO `sys_role_user` (`id`, `role_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (15, 10, 20, 1, 1659937061829, 1, 1659937061829);
INSERT INTO `sys_role_user` (`id`, `role_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (16, 1, 17, 1, 1659937074106, 1, 1659937074106);
INSERT INTO `sys_role_user` (`id`, `role_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (17, 1, 16, 9, 1659944035233, 9, 1659944035233);
INSERT INTO `sys_role_user` (`id`, `role_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (19, 1, 18, 9, 1659944035240, 9, 1659944035240);
INSERT INTO `sys_role_user` (`id`, `role_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (20, 1, 20, 9, 1659944035243, 9, 1659944035243);
INSERT INTO `sys_role_user` (`id`, `role_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (22, 1, 10, 9, 1659944035249, 9, 1659944035249);
INSERT INTO `sys_role_user` (`id`, `role_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (23, 1, 12, 9, 1659944035251, 9, 1659944035251);
INSERT INTO `sys_role_user` (`id`, `role_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (24, 6, 16, 9, 1659944095823, 9, 1659944095823);
INSERT INTO `sys_role_user` (`id`, `role_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (26, 6, 17, 9, 1659944095829, 9, 1659944095829);
INSERT INTO `sys_role_user` (`id`, `role_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (27, 6, 18, 9, 1659944095832, 9, 1659944095832);
INSERT INTO `sys_role_user` (`id`, `role_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (28, 6, 20, 9, 1659944095834, 9, 1659944095834);
INSERT INTO `sys_role_user` (`id`, `role_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (30, 6, 10, 9, 1659944095839, 9, 1659944095839);
INSERT INTO `sys_role_user` (`id`, `role_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (31, 6, 12, 9, 1659944095842, 9, 1659944095842);
INSERT INTO `sys_role_user` (`id`, `role_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (32, 10, 16, 9, 1659944331763, 9, 1659944331763);
INSERT INTO `sys_role_user` (`id`, `role_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (34, 10, 17, 9, 1659944331769, 9, 1659944331769);
INSERT INTO `sys_role_user` (`id`, `role_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (35, 10, 2, 9, 1659944331771, 9, 1659944331771);
INSERT INTO `sys_role_user` (`id`, `role_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (36, 10, 18, 9, 1659944331774, 9, 1659944331774);
INSERT INTO `sys_role_user` (`id`, `role_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (38, 10, 10, 9, 1659944331780, 9, 1659944331780);
INSERT INTO `sys_role_user` (`id`, `role_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (39, 10, 12, 9, 1659944331782, 9, 1659944331782);
INSERT INTO `sys_role_user` (`id`, `role_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (41, 11, 2, 9, 1659944602931, 9, 1659944602931);
INSERT INTO `sys_role_user` (`id`, `role_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (43, 11, 10, 9, 1659944659545, 9, 1659944659545);
INSERT INTO `sys_role_user` (`id`, `role_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (44, 11, 12, 9, 1659944678566, 9, 1659944678566);
INSERT INTO `sys_role_user` (`id`, `role_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (45, 11, 16, 9, 1659944850221, 9, 1659944850221);
INSERT INTO `sys_role_user` (`id`, `role_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (46, 11, 17, 9, 1659944877187, 9, 1659944877187);
INSERT INTO `sys_role_user` (`id`, `role_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (47, 11, 18, 9, 1659944945558, 9, 1659944945558);
INSERT INTO `sys_role_user` (`id`, `role_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (48, 11, 20, 9, 1659945125259, 9, 1659945125259);
INSERT INTO `sys_role_user` (`id`, `role_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (49, 12, 1, 9, 1659945147246, 9, 1659945147246);
INSERT INTO `sys_role_user` (`id`, `role_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (50, 13, 1, 9, 1659945147248, 9, 1659945147248);
INSERT INTO `sys_role_user` (`id`, `role_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (51, 1, 1, 9, 1659945147249, 9, 1659945147249);
INSERT INTO `sys_role_user` (`id`, `role_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (52, 6, 1, 9, 1659945147250, 9, 1659945147250);
INSERT INTO `sys_role_user` (`id`, `role_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (53, 10, 1, 9, 1659945147252, 9, 1659945147252);
INSERT INTO `sys_role_user` (`id`, `role_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (54, 11, 1, 9, 1659945147253, 9, 1659945147253);
INSERT INTO `sys_role_user` (`id`, `role_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (55, 6, 21, 9, 1659949739560, 9, 1659949739560);
INSERT INTO `sys_role_user` (`id`, `role_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (60, 17, 17, 1, 1660026327973, 1, 1660026327973);
INSERT INTO `sys_role_user` (`id`, `role_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (78, 10, 23, 1, 1660034049385, 1, 1660034049385);
INSERT INTO `sys_role_user` (`id`, `role_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (79, 11, 23, 1, 1660034049387, 1, 1660034049387);
INSERT INTO `sys_role_user` (`id`, `role_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (80, 6, 26, 1, 1660034797928, 1, 1660034797928);
INSERT INTO `sys_role_user` (`id`, `role_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (84, 6, 28, 1, 1660113599672, 1, 1660113599672);
INSERT INTO `sys_role_user` (`id`, `role_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (85, 6, 27, 1, 1660117592585, 1, 1660117592585);
INSERT INTO `sys_role_user` (`id`, `role_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (87, 6, 30, 1, 1660118533075, 1, 1660118533075);
INSERT INTO `sys_role_user` (`id`, `role_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (88, 6, 31, 1, 1660121011959, 1, 1660121011959);
INSERT INTO `sys_role_user` (`id`, `role_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (89, 6, 33, 1, 1660201119951, 1, 1660201119951);
INSERT INTO `sys_role_user` (`id`, `role_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (90, 6, 34, 1, 1660204754615, 1, 1660204754615);
INSERT INTO `sys_role_user` (`id`, `role_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (91, 6, 7, 1, 1660211191053, 1, 1660211191053);
INSERT INTO `sys_role_user` (`id`, `role_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (92, 6, 35, 1, 1660211214096, 1, 1660211214096);
INSERT INTO `sys_role_user` (`id`, `role_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (95, 6, 29, 1, 1660270285276, 1, 1660270285276);
INSERT INTO `sys_role_user` (`id`, `role_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (96, 6, 44, 2, 1660530593514, 2, 1660530593514);
INSERT INTO `sys_role_user` (`id`, `role_id`, `user_id`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (97, 6, 9, 9, 1661741027709, 9, 1661741027709);
COMMIT;

-- ----------------------------
-- Table structure for sys_sequence_no
-- ----------------------------
DROP TABLE IF EXISTS `sys_sequence_no`;
CREATE TABLE `sys_sequence_no` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `type` varchar(32) DEFAULT NULL COMMENT '类型',
  `prefix` varchar(32) DEFAULT NULL COMMENT '前缀',
  `number` bigint(20) DEFAULT NULL COMMENT '号码',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `created_by` bigint(20) DEFAULT NULL COMMENT '创建者',
  `created_at` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `updated_by` bigint(20) DEFAULT NULL COMMENT '修改者',
  `updated_at` bigint(20) DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uq_type_number` (`type`,`number`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='系统序列号';

-- ----------------------------
-- Records of sys_sequence_no
-- ----------------------------
BEGIN;
INSERT INTO `sys_sequence_no` (`id`, `type`, `prefix`, `number`, `remark`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (2, 'NFT', '#', 83, NULL, 2, 1658997614611, 2, 1658997614611);
COMMIT;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `account` varchar(64) DEFAULT NULL COMMENT '用户名',
  `password` varchar(64) DEFAULT NULL COMMENT '密码',
  `real_name` varchar(64) DEFAULT NULL COMMENT '姓名',
  `url` varchar(125) DEFAULT NULL COMMENT '头像链接',
  `file_id` bigint(20) DEFAULT NULL COMMENT '头像文件id',
  `gender` tinyint(1) DEFAULT NULL COMMENT '性别   0：男   1：女    2：保密',
  `email` varchar(255) DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(64) DEFAULT NULL COMMENT '手机号',
  `dept_id` bigint(20) DEFAULT NULL COMMENT '部门ID',
  `salt` varchar(64) DEFAULT NULL COMMENT '盐值',
  `super_admin` tinyint(1) DEFAULT '0' COMMENT '超级管理员   0：否   1：是',
  `status` tinyint(1) DEFAULT NULL COMMENT '状态  0：停用   1：正常',
  `nonce` varchar(255) DEFAULT NULL COMMENT '随机数，metamask验证时使用',
  `public_address` varchar(255) DEFAULT NULL COMMENT '钱包地址，metamask',
  `metamask_flag` tinyint(1) DEFAULT '0' COMMENT 'matemask标记  0：未启用  1：启用',
  `delete_flag` tinyint(1) DEFAULT '0' COMMENT '删除标记  0：已删除   1：未删除',
  `created_by` bigint(20) DEFAULT NULL COMMENT '创建者',
  `created_at` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `updated_by` bigint(20) DEFAULT NULL COMMENT '修改者',
  `updated_at` bigint(20) DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_account` (`account`,`delete_flag`) USING BTREE,
  UNIQUE KEY `uk_mobile` (`mobile`,`delete_flag`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8 COMMENT='系统菜单表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
BEGIN;
INSERT INTO `sys_user` (`id`, `account`, `password`, `real_name`, `url`, `file_id`, `gender`, `email`, `mobile`, `dept_id`, `salt`, `super_admin`, `status`, `nonce`, `public_address`, `metamask_flag`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (1, 'admin', '86f2fb9341f670ae27d51ab3d57381ee9a3a2870116ff61cca58c403a8722e3f', '管理员', NULL, NULL, 1, NULL, '18113029713', 1551814205185855488, '9f57b2b5b7e0e6981cdd5f47', 1, 1, 'ede658a11d33378349cfb2a8', 'xxxxkk', 0, 0, 1, 1657863767998, 17, 1659670331550);
INSERT INTO `sys_user` (`id`, `account`, `password`, `real_name`, `url`, `file_id`, `gender`, `email`, `mobile`, `dept_id`, `salt`, `super_admin`, `status`, `nonce`, `public_address`, `metamask_flag`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (2, 'yuhang', 'a4077111f924622c9e8b087ce5cb077c8e3c79af661d49ee1f1b571573860f04', '余航', NULL, NULL, 1, '1462771551@qq.com', '13658107539', 1551814205185855488, 'a68068b1f378248af3a78fa3', 1, 1, NULL, NULL, 0, 0, 1, 1658134156266, 9, 1660787434555);
INSERT INTO `sys_user` (`id`, `account`, `password`, `real_name`, `url`, `file_id`, `gender`, `email`, `mobile`, `dept_id`, `salt`, `super_admin`, `status`, `nonce`, `public_address`, `metamask_flag`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (6, NULL, 'bd99e6490b20f43c3b82cf2337ee877524537a285534c725c0735a62241ad8ec', '张三', NULL, NULL, NULL, NULL, '13333333333', 1551814205185855488, '443e1bcb70da5ed6c9ecdd83', 0, 1, NULL, NULL, 0, 0, 2, 1658911688458, 2, 1658986534971);
INSERT INTO `sys_user` (`id`, `account`, `password`, `real_name`, `url`, `file_id`, `gender`, `email`, `mobile`, `dept_id`, `salt`, `super_admin`, `status`, `nonce`, `public_address`, `metamask_flag`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (7, NULL, 'ede6d64c823a9f12c3b1fc6012b2cf9a970c2fb2d36e183d8d923f77534ea102', '李四', NULL, NULL, NULL, NULL, '13666666666', 1551814205185855488, '10222f17b84dbf1c7643fafc', 0, 1, NULL, NULL, 0, 0, 2, 1658911849127, 2, 1658986534978);
INSERT INTO `sys_user` (`id`, `account`, `password`, `real_name`, `url`, `file_id`, `gender`, `email`, `mobile`, `dept_id`, `salt`, `super_admin`, `status`, `nonce`, `public_address`, `metamask_flag`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (8, NULL, NULL, '王五', NULL, NULL, NULL, NULL, '13555555555', 1551814205185855488, NULL, 0, 0, NULL, NULL, 0, 1, 2, 1658916497393, 2, 1658986534987);
INSERT INTO `sys_user` (`id`, `account`, `password`, `real_name`, `url`, `file_id`, `gender`, `email`, `mobile`, `dept_id`, `salt`, `super_admin`, `status`, `nonce`, `public_address`, `metamask_flag`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (9, 'song', '9afa788163cfc2087e0262b4322924f371c4a4de4110407c7ea31af12f6a015a', 'songqinfeng', 'http://54.168.239.215:9999/admin/public/file/download/admin?objectName=NFT/a93f9c3cf7a2454db9b12d137c574735.jpg', NULL, 0, 'wangjj250wo@qq.com', '18380493239', 1551814205185855488, '5c3b7ed25a61af0f52f3606c', 1, 1, NULL, NULL, 0, 0, 2, 1658916497393, 9, 1661754075055);
INSERT INTO `sys_user` (`id`, `account`, `password`, `real_name`, `url`, `file_id`, `gender`, `email`, `mobile`, `dept_id`, `salt`, `super_admin`, `status`, `nonce`, `public_address`, `metamask_flag`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (10, 'hewei', NULL, '贺伟', NULL, NULL, 1, NULL, '15928559050', NULL, NULL, 1, 1, NULL, NULL, 0, 0, 1, 1658916497393, 1, 1658916497393);
INSERT INTO `sys_user` (`id`, `account`, `password`, `real_name`, `url`, `file_id`, `gender`, `email`, `mobile`, `dept_id`, `salt`, `super_admin`, `status`, `nonce`, `public_address`, `metamask_flag`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (11, NULL, '65165ac38f2a4054c3671387b6d466a6758908e1d62160d399a84fd7c864b53d', '周易', NULL, NULL, NULL, NULL, '13777777777', NULL, '3473a0d906925dc5d30448cb', 0, 1, NULL, NULL, 0, 0, 2, 1658974644334, 2, 1658975475261);
INSERT INTO `sys_user` (`id`, `account`, `password`, `real_name`, `url`, `file_id`, `gender`, `email`, `mobile`, `dept_id`, `salt`, `super_admin`, `status`, `nonce`, `public_address`, `metamask_flag`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (12, 'wangjj250wo', 'c2ffed324250969276c3018894143517cdcef86231ee35d41a4a23c92a752147', 'ssss', NULL, NULL, 0, '441872022@qq.com', '17748499930', NULL, '611576cf0dde392c359cec54', 0, 1, NULL, NULL, 0, 0, 9, 1659337655351, 9, 1659337655351);
INSERT INTO `sys_user` (`id`, `account`, `password`, `real_name`, `url`, `file_id`, `gender`, `email`, `mobile`, `dept_id`, `salt`, `super_admin`, `status`, `nonce`, `public_address`, `metamask_flag`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (13, 'aa2292042', '28d14f4cfdd03ccd74f440de98e5616e63c97aa2d79af0f2cd42bb388241218d', 'chen', NULL, NULL, 1, '441871022@qq.com', '13648001528', NULL, '57799e71724bd5ad41a09fe6', 0, 1, NULL, NULL, 0, 1, 9, 1659337850395, 17, 1659677876683);
INSERT INTO `sys_user` (`id`, `account`, `password`, `real_name`, `url`, `file_id`, `gender`, `email`, `mobile`, `dept_id`, `salt`, `super_admin`, `status`, `nonce`, `public_address`, `metamask_flag`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (14, 'songqinfeng', '5efa754627eeb7437e5de9522ab78ae2eeb058d9474e12fa92f20945098fa2d4', 'songqinfeng', '', NULL, 0, 'wangjj250wo@qq.com', '18888888888', NULL, '1b909732e09d315a591e9508', 0, 1, NULL, NULL, 0, 0, 9, 1659340153169, 9, 1659417222242);
INSERT INTO `sys_user` (`id`, `account`, `password`, `real_name`, `url`, `file_id`, `gender`, `email`, `mobile`, `dept_id`, `salt`, `super_admin`, `status`, `nonce`, `public_address`, `metamask_flag`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (15, 'admin123', '4669740036042bc88d5c2ae8a0db67101a0a8ab40b2ffa54202ed28304ecb9fb', 'songqinfeng', '', NULL, 0, 'wangjj250wo@qq.com', '16666666666', NULL, 'a6ea16512ad85ac284937918', 0, 1, NULL, NULL, 0, 1, 9, 1659340352389, 9, 1659417216971);
INSERT INTO `sys_user` (`id`, `account`, `password`, `real_name`, `url`, `file_id`, `gender`, `email`, `mobile`, `dept_id`, `salt`, `super_admin`, `status`, `nonce`, `public_address`, `metamask_flag`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (17, 'deng', '86f2fb9341f670ae27d51ab3d57381ee9a3a2870116ff61cca58c403a8722e3f', '邓平', NULL, NULL, 0, '2959581551@qq.com', '13348895357', 1552526397166194688, NULL, 1, 1, NULL, NULL, 0, 0, 1, 1659435186666, 1, 1660101862084);
INSERT INTO `sys_user` (`id`, `account`, `password`, `real_name`, `url`, `file_id`, `gender`, `email`, `mobile`, `dept_id`, `salt`, `super_admin`, `status`, `nonce`, `public_address`, `metamask_flag`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (18, '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `sys_user` (`id`, `account`, `password`, `real_name`, `url`, `file_id`, `gender`, `email`, `mobile`, `dept_id`, `salt`, `super_admin`, `status`, `nonce`, `public_address`, `metamask_flag`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (19, 'dengyang', '7513d3e0a9457c97f85e62840585b844b7683b8f0daa2fac8cb00b9769f7b57e', '邓平', NULL, NULL, 0, '769151627@qq.com', '18190874069', 1551814211401814016, 'ff411099ff4609ef3915641e', 0, 1, NULL, NULL, 0, 1, 1, 1659929670403, 1, 1660101859932);
INSERT INTO `sys_user` (`id`, `account`, `password`, `real_name`, `url`, `file_id`, `gender`, `email`, `mobile`, `dept_id`, `salt`, `super_admin`, `status`, `nonce`, `public_address`, `metamask_flag`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (21, NULL, NULL, '余航', NULL, NULL, NULL, NULL, '13658107538', NULL, NULL, 0, 1, NULL, NULL, 0, 0, 9, 1659949739548, 1, 1660037684803);
INSERT INTO `sys_user` (`id`, `account`, `password`, `real_name`, `url`, `file_id`, `gender`, `email`, `mobile`, `dept_id`, `salt`, `super_admin`, `status`, `nonce`, `public_address`, `metamask_flag`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (25, 'dengyang12', 'c7593a1fd7004bf8ad76026132f085393b698c99a08fcb5aa19581125a4e50e2', '邓阳', NULL, NULL, 2, '1@qq.com', '19999999999', 1552526397166194688, 'e0ed0f062031a57d52953fca', 0, 1, NULL, NULL, 0, 1, 1, 1660031285407, 1, 1660111772044);
INSERT INTO `sys_user` (`id`, `account`, `password`, `real_name`, `url`, `file_id`, `gender`, `email`, `mobile`, `dept_id`, `salt`, `super_admin`, `status`, `nonce`, `public_address`, `metamask_flag`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (28, 'dengyang1', '3695ceb8abfd7672d37cf42806922e55b50410bcd7017f7413f06307adfff0b6', '余航', NULL, NULL, 1, '769151627@qq.com', '13348867336', 1551814211401814016, 'c7093f774491e169200df57c', 0, 1, NULL, NULL, 0, 0, 1, 1660111943651, 1, 1660111954323);
INSERT INTO `sys_user` (`id`, `account`, `password`, `real_name`, `url`, `file_id`, `gender`, `email`, `mobile`, `dept_id`, `salt`, `super_admin`, `status`, `nonce`, `public_address`, `metamask_flag`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (30, NULL, NULL, '余航', NULL, NULL, NULL, NULL, '123', NULL, NULL, 0, NULL, NULL, NULL, 0, 0, 1, 1660118533071, 1, 1660118533071);
INSERT INTO `sys_user` (`id`, `account`, `password`, `real_name`, `url`, `file_id`, `gender`, `email`, `mobile`, `dept_id`, `salt`, `super_admin`, `status`, `nonce`, `public_address`, `metamask_flag`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (31, NULL, NULL, '余航', NULL, NULL, NULL, NULL, '1', NULL, NULL, 0, NULL, NULL, NULL, 0, 0, 1, 1660121011951, 1, 1660121011951);
INSERT INTO `sys_user` (`id`, `account`, `password`, `real_name`, `url`, `file_id`, `gender`, `email`, `mobile`, `dept_id`, `salt`, `super_admin`, `status`, `nonce`, `public_address`, `metamask_flag`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (32, 'dengping', '00e734221d856dd06e620c24bc44f4c5fba52b1d4c23bf24fb02dbc9b8cb36bc', 'dy', NULL, NULL, 2, '769151627@qq.com', '13348895358', 1551814211401814016, 'aca7783db451a1a1ebf08a64', 0, 1, NULL, NULL, 0, 0, 1, 1660187176706, 1, 1660188257048);
INSERT INTO `sys_user` (`id`, `account`, `password`, `real_name`, `url`, `file_id`, `gender`, `email`, `mobile`, `dept_id`, `salt`, `super_admin`, `status`, `nonce`, `public_address`, `metamask_flag`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (33, NULL, NULL, '邓阳', NULL, NULL, NULL, NULL, '15229163679', NULL, NULL, 0, NULL, NULL, NULL, 0, 1, 1, 1660201119945, 1, 1660211079996);
INSERT INTO `sys_user` (`id`, `account`, `password`, `real_name`, `url`, `file_id`, `gender`, `email`, `mobile`, `dept_id`, `salt`, `super_admin`, `status`, `nonce`, `public_address`, `metamask_flag`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (34, NULL, NULL, '贺伟', NULL, NULL, NULL, NULL, '12', NULL, NULL, 0, NULL, NULL, NULL, 0, 0, 1, 1660204754609, 1, 1660204754609);
INSERT INTO `sys_user` (`id`, `account`, `password`, `real_name`, `url`, `file_id`, `gender`, `email`, `mobile`, `dept_id`, `salt`, `super_admin`, `status`, `nonce`, `public_address`, `metamask_flag`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (35, NULL, NULL, '李四', NULL, NULL, NULL, NULL, '14725836912', NULL, NULL, 0, NULL, NULL, NULL, 0, 0, 1, 1660211214091, 1, 1660211214091);
INSERT INTO `sys_user` (`id`, `account`, `password`, `real_name`, `url`, `file_id`, `gender`, `email`, `mobile`, `dept_id`, `salt`, `super_admin`, `status`, `nonce`, `public_address`, `metamask_flag`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (36, 'dengyangdeng', '7451e649e02e651eb5618e511305941d7847856d960aa9466ad23ed9b88a89ba', '邓阳', NULL, NULL, 2, '2959581551@qq.com', '13348895359', 1551814211401814016, 'fb450e99760641f2de442def', 0, 1, NULL, NULL, 0, 0, 1, 1660273842221, 1, 1660273842221);
INSERT INTO `sys_user` (`id`, `account`, `password`, `real_name`, `url`, `file_id`, `gender`, `email`, `mobile`, `dept_id`, `salt`, `super_admin`, `status`, `nonce`, `public_address`, `metamask_flag`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (37, 'dengyang123', 'dbcb60390acfe8e423c6e1421d7594228fbcf6f6b8b5eb50a1bc391d7b17b0f3', '邓阳', NULL, NULL, 0, '2959581551@qq.com', '13348895360', 1551814211401814016, '916ae76ecb4e690dcb996f30', 0, 1, NULL, NULL, 0, 0, 1, 1660273949690, 1, 1660273949690);
INSERT INTO `sys_user` (`id`, `account`, `password`, `real_name`, `url`, `file_id`, `gender`, `email`, `mobile`, `dept_id`, `salt`, `super_admin`, `status`, `nonce`, `public_address`, `metamask_flag`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (38, 'dengyang15', '42cffbba4e6221b58caabb359e821a7f3103473c6886e3c850fb7d5a8c5fc75b', '邓阳', NULL, NULL, 2, '2959581551@qq.com', '13348895361', 1551814208377720832, '9c7aedcf2896ae42a6f5e63d', 0, 1, NULL, NULL, 0, 0, 1, 1660274049183, 9, 1660787663300);
INSERT INTO `sys_user` (`id`, `account`, `password`, `real_name`, `url`, `file_id`, `gender`, `email`, `mobile`, `dept_id`, `salt`, `super_admin`, `status`, `nonce`, `public_address`, `metamask_flag`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (39, '邓康', '681c89417b2a2553f983ef9033e58fbf04300d5378bc4d155da350e328fceba9', '111', NULL, NULL, 0, '0@qq.com', '13348895362', 1551814211401814016, 'c7e250ed83fa2676026f55eb', 0, 1, NULL, NULL, 0, 0, 1, 1660275486595, 1, 1660275486595);
INSERT INTO `sys_user` (`id`, `account`, `password`, `real_name`, `url`, `file_id`, `gender`, `email`, `mobile`, `dept_id`, `salt`, `super_admin`, `status`, `nonce`, `public_address`, `metamask_flag`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (40, '康kseh1323', 'd70d2377a94b1a6f2d89e18fdc562e0fd59337b69369aa71b13ff9ee94506f25', '邓阳', NULL, NULL, 2, '2959581551@qq.com', '13348895363', 1551814211401814016, '94892782265f3d8b064993ea', 0, 1, NULL, NULL, 0, 0, 1, 1660278158982, 1, 1660278158982);
INSERT INTO `sys_user` (`id`, `account`, `password`, `real_name`, `url`, `file_id`, `gender`, `email`, `mobile`, `dept_id`, `salt`, `super_admin`, `status`, `nonce`, `public_address`, `metamask_flag`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (41, '阳123zhcae', 'dee90711f7c54cce81e5a113edfbd60f72557f6d8647a6bcb91c99168e4edec1', '邓阳', NULL, NULL, 2, '0@qq.com', '13348895364', 1551814211401814016, '68efc1d2a04d5647d8e01b07', 0, 1, NULL, NULL, 0, 0, 1, 1660278956541, 1, 1660278956541);
INSERT INTO `sys_user` (`id`, `account`, `password`, `real_name`, `url`, `file_id`, `gender`, `email`, `mobile`, `dept_id`, `salt`, `super_admin`, `status`, `nonce`, `public_address`, `metamask_flag`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (42, 'kangkang', '14d6df900ef92fe574e55f706b6b89e33ea9b87974c5de1f294096e2cb18970e', '邓阳', NULL, NULL, 2, '1@qq.com', '13348895365', 1551814211401814016, 'a097d79c2aa1d77410abf172', 0, 1, NULL, NULL, 0, 0, 1, 1660279028844, 1, 1660279028844);
INSERT INTO `sys_user` (`id`, `account`, `password`, `real_name`, `url`, `file_id`, `gender`, `email`, `mobile`, `dept_id`, `salt`, `super_admin`, `status`, `nonce`, `public_address`, `metamask_flag`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (43, '杨康', '69dec1975f7dee8358e9f1cffcb150d2ce4b8177483b8d9ccb491970a3462de2', '----------------------', NULL, NULL, 2, '00@qq.com', '13348895366', 1551814211401814016, '8e3a45cd60f6d47b41da9a7e', 0, 1, NULL, NULL, 0, 0, 1, 1660279486802, 1, 1660284881969);
INSERT INTO `sys_user` (`id`, `account`, `password`, `real_name`, `url`, `file_id`, `gender`, `email`, `mobile`, `dept_id`, `salt`, `super_admin`, `status`, `nonce`, `public_address`, `metamask_flag`, `delete_flag`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (44, NULL, NULL, '余航', NULL, NULL, NULL, NULL, '2', NULL, NULL, 0, NULL, NULL, NULL, 0, 0, 2, 1660530593467, 2, 1660530593467);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
