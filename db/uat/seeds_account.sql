/*
 Navicat Premium Data Transfer

 Source Server         : 34.84.153.188-seeds-uat
 Source Server Type    : MySQL
 Source Server Version : 80028
 Source Host           : 34.84.153.188:3306
 Source Schema         : seeds_account

 Target Server Type    : MySQL
 Target Server Version : 80028
 File Encoding         : 65001

 Date: 21/09/2023 14:44:51
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for ac_action_control
-- ----------------------------
DROP TABLE IF EXISTS `ac_action_control`;
CREATE TABLE `ac_action_control` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key',
  `create_time` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'create time',
  `update_time` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'update time',
  `version` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'version',
  `type` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'type',
  `name` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'name',
  `key` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'key',
  `value` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'value',
  `comments` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'comments',
  `status` tinyint unsigned NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='操作控制';

-- ----------------------------
-- Records of ac_action_control
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for ac_address_collect_his
-- ----------------------------
DROP TABLE IF EXISTS `ac_address_collect_his`;
CREATE TABLE `ac_address_collect_his` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key',
  `create_time` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'create time',
  `update_time` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'update time',
  `version` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'version',
  `order_id` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'order id',
  `chain` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'chain',
  `from_address` varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'type',
  `to_address` varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'name',
  `tx_to_address` varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'chain tx to address',
  `currency` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'key',
  `amount` decimal(36,18) NOT NULL DEFAULT '0.000000000000000000' COMMENT 'amount',
  `gas_price` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'gasPrice',
  `gas_limit` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'gasLimit',
  `tx_fee` decimal(36,18) NOT NULL DEFAULT '0.000000000000000000' COMMENT 'tx fee amount',
  `block_number` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'block number',
  `block_hash` varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'block hash',
  `tx_hash` varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'tx hash',
  `nonce` varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'tx nonce',
  `tx_value` varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'chain transaction value',
  `comments` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'comments',
  `status` tinyint unsigned NOT NULL DEFAULT '1' COMMENT '1. 提交上链 2. 链上执行完毕，待安全确认 3.链上确认 4 链上失败 5 链上取消 6 原tx被取消，replace tx 替代成功 ',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='归集历史';

-- ----------------------------
-- Records of ac_address_collect_his
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for ac_address_collect_order_his
-- ----------------------------
DROP TABLE IF EXISTS `ac_address_collect_order_his`;
CREATE TABLE `ac_address_collect_order_his` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key',
  `create_time` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'create time',
  `update_time` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'update time',
  `version` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'version',
  `type` tinyint unsigned NOT NULL DEFAULT '1' COMMENT 'type',
  `address` varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'address',
  `currency` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'currency',
  `amount` decimal(36,18) NOT NULL DEFAULT '0.000000000000000000' COMMENT 'amount',
  `gas_price` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'gas price',
  `fee_amount` decimal(36,18) NOT NULL DEFAULT '0.000000000000000000' COMMENT 'fee amount',
  `status` tinyint unsigned NOT NULL DEFAULT '1' COMMENT '3 processing, 1 completed',
  `chain` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'chain',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='归集订单历史';

-- ----------------------------
-- Records of ac_address_collect_order_his
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for ac_blacklist_address
-- ----------------------------
DROP TABLE IF EXISTS `ac_blacklist_address`;
CREATE TABLE `ac_blacklist_address` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key',
  `create_time` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'create time',
  `update_time` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'update time',
  `version` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'version',
  `chain` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'chain',
  `type` tinyint unsigned NOT NULL DEFAULT '1' COMMENT '1 充币 2提币',
  `address` varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'address',
  `reason` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'reason',
  `status` tinyint unsigned NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='Ethereum黑地址';

-- ----------------------------
-- Records of ac_blacklist_address
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for ac_chain_block
-- ----------------------------
DROP TABLE IF EXISTS `ac_chain_block`;
CREATE TABLE `ac_chain_block` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key',
  `create_time` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'create time',
  `update_time` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'update time',
  `version` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'version',
  `chain` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'chain',
  `block_number` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'block number',
  `block_hash` varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'block hash',
  `block_time` bigint unsigned DEFAULT '0' COMMENT 'block timestamp',
  `parent_hash` varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'parent block hash',
  `status` tinyint unsigned NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='Ethereum块跟踪';

-- ----------------------------
-- Records of ac_chain_block
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for ac_chain_contract
-- ----------------------------
DROP TABLE IF EXISTS `ac_chain_contract`;
CREATE TABLE `ac_chain_contract` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key',
  `create_time` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'create time',
  `update_time` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'update time',
  `version` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'version',
  `chain` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'chain',
  `currency` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'currency',
  `address` varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'block hash',
  `decimals` tinyint unsigned NOT NULL DEFAULT '1' COMMENT 'decimals',
  `transfer_sign` varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'transfer sign',
  `is_staking_asset` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '是否为质押资产',
  `status` tinyint unsigned NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uniq_t_chain_contract_1` (`chain`,`currency`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='链上合约';

-- ----------------------------
-- Records of ac_chain_contract
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for ac_chain_deposit_address
-- ----------------------------
DROP TABLE IF EXISTS `ac_chain_deposit_address`;
CREATE TABLE `ac_chain_deposit_address` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key',
  `create_time` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'create time',
  `update_time` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'update time',
  `version` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'version',
  `chain` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'chain',
  `address` varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'address',
  `user_id` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'user id',
  `status` tinyint unsigned NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uniq_t_deposit_address_1` (`chain`,`address`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=100074 DEFAULT CHARSET=utf8mb3 COMMENT='Ethereum地址 充币地址（交易所分配）';

-- ----------------------------
-- Records of ac_chain_deposit_address
-- ----------------------------
BEGIN;
INSERT INTO `ac_chain_deposit_address` (`id`, `create_time`, `update_time`, `version`, `chain`, `address`, `user_id`, `status`) VALUES (100054, 1680871511784, 1680871511784, 1, '1', '0x49744da72f1fc6d8aa395c73dcad33171941af03', 0, 1);
INSERT INTO `ac_chain_deposit_address` (`id`, `create_time`, `update_time`, `version`, `chain`, `address`, `user_id`, `status`) VALUES (100055, 1680871511784, 1680871511784, 1, '1', '0x8abcc3ee387d82675048f60b2064090d32d97b8a', 0, 1);
INSERT INTO `ac_chain_deposit_address` (`id`, `create_time`, `update_time`, `version`, `chain`, `address`, `user_id`, `status`) VALUES (100056, 1680871511784, 1680871511784, 1, '1', '0x65ec8a0a0fbda1103cb6c213a85639ecde4da55b', 0, 1);
INSERT INTO `ac_chain_deposit_address` (`id`, `create_time`, `update_time`, `version`, `chain`, `address`, `user_id`, `status`) VALUES (100057, 1680871511784, 1680871511784, 1, '1', '0x6ac181ee92a2ecb96a905c695ffc2ddfc906b418', 0, 1);
INSERT INTO `ac_chain_deposit_address` (`id`, `create_time`, `update_time`, `version`, `chain`, `address`, `user_id`, `status`) VALUES (100058, 1680871511784, 1680871511784, 1, '1', '0x6f5df27fb8cc24d3a7dddaff14a64fa0378169c4', 0, 1);
INSERT INTO `ac_chain_deposit_address` (`id`, `create_time`, `update_time`, `version`, `chain`, `address`, `user_id`, `status`) VALUES (100059, 1680871511784, 1680871511784, 1, '1', '0x3fc053e5ea418f519ce7a803bb601e0e489573a8', 0, 1);
INSERT INTO `ac_chain_deposit_address` (`id`, `create_time`, `update_time`, `version`, `chain`, `address`, `user_id`, `status`) VALUES (100060, 1680871511784, 1680871511784, 1, '1', '0x2847f0fd4231a8fbc398c61b386d8f394db58bde', 0, 1);
INSERT INTO `ac_chain_deposit_address` (`id`, `create_time`, `update_time`, `version`, `chain`, `address`, `user_id`, `status`) VALUES (100061, 1680871511784, 1680871511784, 1, '1', '0xa2f3e8a9333cef7ff2c828a0a81064863323e4cb', 0, 1);
INSERT INTO `ac_chain_deposit_address` (`id`, `create_time`, `update_time`, `version`, `chain`, `address`, `user_id`, `status`) VALUES (100062, 1680871511784, 1680871511784, 1, '1', '0xd34ed2b72a7d1ffcdd2815eef618fc4db95ecd99', 0, 1);
INSERT INTO `ac_chain_deposit_address` (`id`, `create_time`, `update_time`, `version`, `chain`, `address`, `user_id`, `status`) VALUES (100063, 1680871511784, 1680871511784, 1, '1', '0xac9faa51a9121246be32762f0f9ada8342452e8e', 0, 1);
INSERT INTO `ac_chain_deposit_address` (`id`, `create_time`, `update_time`, `version`, `chain`, `address`, `user_id`, `status`) VALUES (100064, 1680871512118, 1680871512118, 1, '3', 'THqAsLSyLtALWyjftWMskc7Q9asUhfBufT', 0, 1);
INSERT INTO `ac_chain_deposit_address` (`id`, `create_time`, `update_time`, `version`, `chain`, `address`, `user_id`, `status`) VALUES (100065, 1680871512118, 1680871512118, 1, '3', 'TM8Xi6CGVZsuotQwou44nSVvTosAuFen6i', 0, 1);
INSERT INTO `ac_chain_deposit_address` (`id`, `create_time`, `update_time`, `version`, `chain`, `address`, `user_id`, `status`) VALUES (100066, 1680871512118, 1680871512118, 1, '3', 'TXbgSRJtgqPCFkm6GyPVdnRnp5SAQM2G4n', 0, 1);
INSERT INTO `ac_chain_deposit_address` (`id`, `create_time`, `update_time`, `version`, `chain`, `address`, `user_id`, `status`) VALUES (100067, 1680871512118, 1680871512118, 1, '3', 'TNTdbHCd2cZwrJbkBKbqbSv1FcTP1u3bnM', 0, 1);
INSERT INTO `ac_chain_deposit_address` (`id`, `create_time`, `update_time`, `version`, `chain`, `address`, `user_id`, `status`) VALUES (100068, 1680871512118, 1680871512118, 1, '3', 'TMTdzW41mMHQRdWVZMyXrJBvwK25cEx6Jk', 0, 1);
INSERT INTO `ac_chain_deposit_address` (`id`, `create_time`, `update_time`, `version`, `chain`, `address`, `user_id`, `status`) VALUES (100069, 1680871512118, 1680871512118, 1, '3', 'TAcqyzikZqJHPWzPhh2VoyaWcvhgjaJezP', 0, 1);
INSERT INTO `ac_chain_deposit_address` (`id`, `create_time`, `update_time`, `version`, `chain`, `address`, `user_id`, `status`) VALUES (100070, 1680871512118, 1680871512118, 1, '3', 'TBWKQm4BEjjw4ZdgLnHwbMqmmMMP8jM3JA', 0, 1);
INSERT INTO `ac_chain_deposit_address` (`id`, `create_time`, `update_time`, `version`, `chain`, `address`, `user_id`, `status`) VALUES (100071, 1680871512118, 1680871512118, 1, '3', 'TGHXt5ZXj5k2rEo4nK9FEnA1ZHWSoRRfRi', 0, 1);
INSERT INTO `ac_chain_deposit_address` (`id`, `create_time`, `update_time`, `version`, `chain`, `address`, `user_id`, `status`) VALUES (100072, 1680871512118, 1680871512118, 1, '3', 'TYxfSmSb6bx3ssPosg5yeNiXxD7q54UBKU', 0, 1);
INSERT INTO `ac_chain_deposit_address` (`id`, `create_time`, `update_time`, `version`, `chain`, `address`, `user_id`, `status`) VALUES (100073, 1680871512118, 1680871512118, 1, '3', 'TEE3Jzr7AhyzNVsyzTrHSx9NS99JwXDjBC', 0, 1);
COMMIT;

-- ----------------------------
-- Table structure for ac_chain_deposit_withdraw_his
-- ----------------------------
DROP TABLE IF EXISTS `ac_chain_deposit_withdraw_his`;
CREATE TABLE `ac_chain_deposit_withdraw_his` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key',
  `create_time` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'create time',
  `update_time` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'update time',
  `version` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'version',
  `user_id` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'user id',
  `chain` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'chain',
  `from_address` varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'from address',
  `to_address` varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'to address',
  `tx_to_address` varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'chain tx to address',
  `currency` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'currency',
  `internal` tinyint unsigned NOT NULL DEFAULT '0',
  `action` tinyint unsigned NOT NULL DEFAULT '1' COMMENT '1 deposit, 2 withdraw',
  `amount` decimal(36,18) NOT NULL DEFAULT '0.000000000000000000' COMMENT 'amount',
  `fee_amount` decimal(36,18) NOT NULL DEFAULT '0.000000000000000000' COMMENT 'fee amount',
  `gas_price` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'gasPrice',
  `gas_limit` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'gasLimit',
  `tx_fee` decimal(36,18) NOT NULL DEFAULT '0.000000000000000000' COMMENT 'tx fee amount',
  `block_number` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'block number',
  `block_hash` varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'block hash',
  `tx_hash` varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'tx hash',
  `nonce` varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'tx nonce',
  `tx_value` varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'chain transaction value',
  `is_replace` tinyint unsigned NOT NULL DEFAULT '0' COMMENT 'tx 是否被replaced',
  `manual` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '是否运营介入',
  `blacklist` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '是否是黑地址',
  `comments` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'approve or reject comments',
  `status` tinyint unsigned NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='充提历史';

-- ----------------------------
-- Records of ac_chain_deposit_withdraw_his
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for ac_chain_txn_data
-- ----------------------------
DROP TABLE IF EXISTS `ac_chain_txn_data`;
CREATE TABLE `ac_chain_txn_data` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key',
  `create_time` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'create time',
  `update_time` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'update time',
  `version` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'version',
  `app_type` tinyint unsigned NOT NULL DEFAULT '1' COMMENT '1.链上兑换历史 2.链上分红划转历史3.mcd上报4.链上replacement transaction 5提币 6 热钱包划转 7 资金归集 8 gas划转',
  `app_id` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'app id',
  `data` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT 'tx data',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_ctd_update_time` (`update_time`) USING BTREE,
  KEY `idx_ctd_app_type` (`app_type`) USING BTREE,
  KEY `idx_ctd_app_id` (`app_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='chain transaction data';

-- ----------------------------
-- Records of ac_chain_txn_data
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for ac_chain_txn_replace
-- ----------------------------
DROP TABLE IF EXISTS `ac_chain_txn_replace`;
CREATE TABLE `ac_chain_txn_replace` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key',
  `create_time` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'create time',
  `update_time` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'update time',
  `version` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'version',
  `chain` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'chain',
  `app_type` tinyint unsigned NOT NULL DEFAULT '1' COMMENT '1-提币, 2-热钱包划转, 3-资金归集, 4-GAS 划转',
  `app_id` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'app id',
  `gas_price` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'gasPrice',
  `gas_limit` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'gasLimit',
  `gas_used` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'gasUsed',
  `tx_fee` decimal(36,18) NOT NULL DEFAULT '0.000000000000000000' COMMENT 'tx fee amount',
  `block_number` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'block number',
  `block_hash` varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'block hash',
  `tx_hash` varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'tx hash',
  `nonce` varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'tx nonce',
  `tx_value` varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'chain transaction value',
  `status` tinyint unsigned NOT NULL DEFAULT '1' COMMENT '1-提交上链，2-链上执行完毕，待安全确认（e.g. 12个块安全确认）3-链上确认，4-链上失败，5-链上取消',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_ctr_update_time` (`update_time`) USING BTREE,
  KEY `idx_ctr_app_type` (`app_type`) USING BTREE,
  KEY `idx_ctr_app_id` (`app_id`) USING BTREE,
  KEY `idx_ctr_status` (`status`) USING BTREE,
  KEY `idx_ctr_hash` (`tx_hash`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='chain transaction replacement';

-- ----------------------------
-- Records of ac_chain_txn_replace
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for ac_deposit_rule
-- ----------------------------
DROP TABLE IF EXISTS `ac_deposit_rule`;
CREATE TABLE `ac_deposit_rule` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key',
  `create_time` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'create time',
  `update_time` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'update time',
  `version` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'version',
  `chain` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'chain',
  `currency` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'currency',
  `min_amount` decimal(36,18) NOT NULL DEFAULT '0.000000000000000000' COMMENT 'min amount',
  `decimals` tinyint unsigned NOT NULL DEFAULT '1',
  `auto_amount` decimal(36,18) NOT NULL DEFAULT '1.000000000000000000' COMMENT 'auto deposit amount',
  `status` tinyint unsigned NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='充提规则';

-- ----------------------------
-- Records of ac_deposit_rule
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for ac_nft_forward_auction
-- ----------------------------
DROP TABLE IF EXISTS `ac_nft_forward_auction`;
CREATE TABLE `ac_nft_forward_auction` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `nft_id` bigint DEFAULT NULL COMMENT 'NFT的id',
  `user_id` bigint DEFAULT NULL COMMENT '拥有NFT的用户id',
  `price` decimal(10,2) DEFAULT NULL COMMENT '初始最低价格',
  `currency` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '单位',
  `settlement_time` bigint DEFAULT NULL COMMENT '结算时间',
  `create_time` bigint DEFAULT '0' COMMENT '创建时间',
  `update_time` bigint DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='NFT的正向拍卖表';

-- ----------------------------
-- Records of ac_nft_forward_auction
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for ac_nft_offer
-- ----------------------------
DROP TABLE IF EXISTS `ac_nft_offer`;
CREATE TABLE `ac_nft_offer` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `nft_id` bigint DEFAULT NULL COMMENT 'NFT的id',
  `user_id` bigint DEFAULT NULL COMMENT '出价用户id',
  `action_history_id` bigint DEFAULT NULL COMMENT '账号交易历史记录id',
  `price` decimal(10,2) DEFAULT NULL COMMENT '价格',
  `currency` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '单位',
  `status` tinyint(1) DEFAULT NULL COMMENT '状态 0：竞价中 1：已接受 2：已拒绝 3：已过期',
  `expire_time` bigint DEFAULT NULL COMMENT '过期时间',
  `difference` decimal(10,2) DEFAULT NULL COMMENT '差异',
  `delete_flag` tinyint(1) DEFAULT '0' COMMENT '删除标记  0：未删除',
  `create_time` bigint DEFAULT '0' COMMENT '创建时间',
  `update_time` bigint DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='NFT的Offer管理表';

-- ----------------------------
-- Records of ac_nft_offer
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for ac_nft_reverse_auction
-- ----------------------------
DROP TABLE IF EXISTS `ac_nft_reverse_auction`;
CREATE TABLE `ac_nft_reverse_auction` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `nft_id` bigint DEFAULT NULL COMMENT 'NFT的id',
  `user_id` bigint DEFAULT NULL COMMENT '拥有NFT的用户id',
  `price` decimal(10,2) DEFAULT NULL COMMENT '初始最高价格',
  `currency` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '单位',
  `interval_unit` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '间隔时间单位 m,h,d,s',
  `interval_time` bigint DEFAULT NULL COMMENT '间隔时间',
  `drop_point` decimal(10,2) DEFAULT NULL COMMENT '下降百分比',
  `create_time` bigint DEFAULT '0' COMMENT '创建时间',
  `update_time` bigint DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='NFT的反向拍卖表';

-- ----------------------------
-- Records of ac_nft_reverse_auction
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for ac_system_config
-- ----------------------------
DROP TABLE IF EXISTS `ac_system_config`;
CREATE TABLE `ac_system_config` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key',
  `create_time` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'create time',
  `update_time` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'update time',
  `version` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'version',
  `type` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'type',
  `name` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'name',
  `key` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'key',
  `value` varchar(1200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'value',
  `comments` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'comments',
  `status` tinyint unsigned NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uniq_t_system_config_1` (`type`,`key`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='全局配置';

-- ----------------------------
-- Records of ac_system_config
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for ac_system_wallet_address
-- ----------------------------
DROP TABLE IF EXISTS `ac_system_wallet_address`;
CREATE TABLE `ac_system_wallet_address` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key',
  `create_time` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'create time',
  `update_time` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'update time',
  `version` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'version',
  `type` tinyint unsigned NOT NULL DEFAULT '0',
  `address` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'name',
  `tag` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'key',
  `comments` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'comments',
  `chain` tinyint unsigned NOT NULL DEFAULT '1' COMMENT 'chain',
  `status` tinyint unsigned NOT NULL DEFAULT '1' COMMENT '1启用 2停用',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uniq_t_system_wallet_address_1` (`type`,`address`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='系统使用的钱包地址';

-- ----------------------------
-- Records of ac_system_wallet_address
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for ac_user_account
-- ----------------------------
DROP TABLE IF EXISTS `ac_user_account`;
CREATE TABLE `ac_user_account` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key',
  `create_time` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'create time',
  `update_time` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'update time',
  `version` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'version',
  `user_id` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'user id',
  `currency` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'currency',
  `available` decimal(36,18) NOT NULL DEFAULT '0.000000000000000000' COMMENT 'available',
  `freeze` decimal(36,18) NOT NULL DEFAULT '0.000000000000000000' COMMENT 'freeze amount',
  `locked` decimal(36,18) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uniq_t_wallet_account_1` (`user_id`,`currency`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='钱包账户';

-- ----------------------------
-- Records of ac_user_account
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for ac_user_account_action_his
-- ----------------------------
DROP TABLE IF EXISTS `ac_user_account_action_his`;
CREATE TABLE `ac_user_account_action_his` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key',
  `create_time` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'create time',
  `update_time` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'update time',
  `version` tinyint unsigned NOT NULL DEFAULT '0' COMMENT 'version',
  `action` smallint NOT NULL DEFAULT '0',
  `source` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `to_user_id` bigint DEFAULT NULL,
  `from_user_id` bigint DEFAULT NULL COMMENT 'toUserId',
  `user_id` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'userId',
  `currency` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `amount` decimal(36,18) NOT NULL DEFAULT '0.000000000000000000' COMMENT 'amount',
  `status` tinyint unsigned NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_uaah_user_id` (`user_id`) USING BTREE,
  KEY `idx_uaah_currency` (`currency`) USING BTREE,
  KEY `idx_uaah_update_time` (`update_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='账户行为历史记录';

-- ----------------------------
-- Records of ac_user_account_action_his
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for ac_withdraw_limit_rule
-- ----------------------------
DROP TABLE IF EXISTS `ac_withdraw_limit_rule`;
CREATE TABLE `ac_withdraw_limit_rule` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key',
  `create_time` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'create time',
  `update_time` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'update time',
  `version` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'version',
  `currency` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'currency',
  `min_amount` decimal(36,18) NOT NULL DEFAULT '0.000000000000000000' COMMENT 'min amount',
  `max_amount` decimal(36,18) NOT NULL DEFAULT '0.000000000000000000' COMMENT 'max amount',
  `intraday_amount` decimal(36,18) NOT NULL DEFAULT '0.000000000000000000' COMMENT 'intrady amount',
  `auto_amount` decimal(36,18) NOT NULL DEFAULT '1.000000000000000000' COMMENT 'auto withdraw amount',
  `zero_fee_on_internal` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'false' COMMENT '是否内部提币免手续费',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='提币限额规则';

-- ----------------------------
-- Records of ac_withdraw_limit_rule
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for ac_withdraw_rule
-- ----------------------------
DROP TABLE IF EXISTS `ac_withdraw_rule`;
CREATE TABLE `ac_withdraw_rule` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key',
  `create_time` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'create time',
  `update_time` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'update time',
  `version` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'version',
  `chain` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'chain',
  `currency` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'currency',
  `min_amount` decimal(36,18) NOT NULL DEFAULT '0.000000000000000000' COMMENT 'min amount',
  `max_amount` decimal(36,18) NOT NULL DEFAULT '0.000000000000000000' COMMENT 'max amount',
  `intraday_amount` decimal(36,18) NOT NULL DEFAULT '0.000000000000000000' COMMENT 'intrady amount',
  `fee_type` tinyint unsigned NOT NULL DEFAULT '1' COMMENT 'fee type',
  `fee_amount` decimal(36,18) NOT NULL DEFAULT '0.000000000000000000' COMMENT 'fee amount',
  `decimals` tinyint NOT NULL DEFAULT '1',
  `auto_amount` decimal(36,18) NOT NULL DEFAULT '1.000000000000000000' COMMENT 'auto withdraw amount',
  `zero_fee_on_internal` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'false' COMMENT '是否内部提币面手续费',
  `status` tinyint unsigned NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='提币规则';

-- ----------------------------
-- Records of ac_withdraw_rule
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for ac_withdraw_rule_user
-- ----------------------------
DROP TABLE IF EXISTS `ac_withdraw_rule_user`;
CREATE TABLE `ac_withdraw_rule_user` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key',
  `create_time` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'create time',
  `update_time` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'update time',
  `version` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'version',
  `user_id` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'user id',
  `chain` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'chain',
  `currency` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'currency',
  `max_amount` decimal(36,18) NOT NULL DEFAULT '0.000000000000000000' COMMENT 'max amount',
  `intraday_amount` decimal(36,18) NOT NULL DEFAULT '0.000000000000000000' COMMENT 'intrady amount',
  `auto_amount` decimal(36,18) NOT NULL DEFAULT '1.000000000000000000' COMMENT 'auto withdraw amount',
  `comments` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'comments',
  `status` tinyint unsigned NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='提币用户规则';

-- ----------------------------
-- Records of ac_withdraw_rule_user
-- ----------------------------
BEGIN;
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
