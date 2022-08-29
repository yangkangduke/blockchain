/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.1.101
 Source Server Type    : MySQL
 Source Server Version : 50736
 Source Host           : 192.168.1.101:3306
 Source Schema         : seeds_uc

 Target Server Type    : MySQL
 Target Server Version : 50736
 File Encoding         : 65001

 Date: 29/08/2022 16:12:36
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_action_rule
-- ----------------------------
DROP TABLE IF EXISTS `t_action_rule`;
CREATE TABLE `t_action_rule` (
  `f_action` varchar(64) NOT NULL COMMENT '操作标识',
  `f_enable` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否启用：0停用，1启用',
  `f_start_time` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'effective time',
  PRIMARY KEY (`f_action`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='动作规则';

-- ----------------------------
-- Records of t_action_rule
-- ----------------------------
BEGIN;
INSERT INTO `t_action_rule` (`f_action`, `f_enable`, `f_start_time`) VALUES ('account:burn', 1, 0);
INSERT INTO `t_action_rule` (`f_action`, `f_enable`, `f_start_time`) VALUES ('account:claim:fee', 1, 0);
INSERT INTO `t_action_rule` (`f_action`, `f_enable`, `f_start_time`) VALUES ('account:exchange', 1, 0);
INSERT INTO `t_action_rule` (`f_action`, `f_enable`, `f_start_time`) VALUES ('account:stake', 1, 0);
INSERT INTO `t_action_rule` (`f_action`, `f_enable`, `f_start_time`) VALUES ('account:transfer', 1, 0);
INSERT INTO `t_action_rule` (`f_action`, `f_enable`, `f_start_time`) VALUES ('account:withdraw', 1, 0);
INSERT INTO `t_action_rule` (`f_action`, `f_enable`, `f_start_time`) VALUES ('job:check-staking-account', 1, 0);
INSERT INTO `t_action_rule` (`f_action`, `f_enable`, `f_start_time`) VALUES ('job:check-trading-account', 1, 0);
COMMIT;

-- ----------------------------
-- Table structure for t_black_address
-- ----------------------------
DROP TABLE IF EXISTS `t_black_address`;
CREATE TABLE `t_black_address` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key',
  `f_address` varchar(80) NOT NULL DEFAULT '',
  `f_comments` varchar(64) DEFAULT '',
  PRIMARY KEY (`f_id`),
  UNIQUE KEY `uniq_1` (`f_address`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='t_black_address';

-- ----------------------------
-- Records of t_black_address
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_chain_address
-- ----------------------------
DROP TABLE IF EXISTS `t_chain_address`;
CREATE TABLE `t_chain_address` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key',
  `f_create_time` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'create time',
  `f_update_time` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'update time',
  `f_version` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'version',
  `f_currency` varchar(12) NOT NULL DEFAULT '',
  `f_chain` varchar(12) NOT NULL DEFAULT '',
  `f_user_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'userId',
  `f_address` varchar(80) NOT NULL DEFAULT '',
  `f_comments` varchar(64) DEFAULT '',
  PRIMARY KEY (`f_id`),
  UNIQUE KEY `uniq_t_chain_address_1` (`f_currency`,`f_chain`,`f_address`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='t_chain_address';

-- ----------------------------
-- Records of t_chain_address
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_currency
-- ----------------------------
DROP TABLE IF EXISTS `t_currency`;
CREATE TABLE `t_currency` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key ID',
  `f_name` varchar(20) NOT NULL DEFAULT '' COMMENT 'base currency',
  `f_code` int(11) NOT NULL DEFAULT '0' COMMENT 'quote currency',
  `f_status` int(11) NOT NULL DEFAULT '0' COMMENT '1 上线， 0 已经下线',
  `f_exchange` int(11) NOT NULL DEFAULT '0' COMMENT '1 可以交易， 0 停止交易',
  `f_created_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'created timestamp',
  `f_updated_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'update timestamp',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='币种信息';

-- ----------------------------
-- Records of t_currency
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_dividend_summary
-- ----------------------------
DROP TABLE IF EXISTS `t_dividend_summary`;
CREATE TABLE `t_dividend_summary` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key',
  `f_create_time` bigint(20) NOT NULL DEFAULT '0',
  `f_mcd_total` decimal(36,18) NOT NULL DEFAULT '0.000000000000000000',
  `f_fee_total` decimal(36,18) NOT NULL DEFAULT '0.000000000000000000',
  `f_percent` decimal(36,18) NOT NULL DEFAULT '0.000000000000000000',
  `f_share` decimal(36,18) NOT NULL DEFAULT '0.000000000000000000',
  `f_users` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='dividend summary';

-- ----------------------------
-- Records of t_dividend_summary
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_eth_block
-- ----------------------------
DROP TABLE IF EXISTS `t_eth_block`;
CREATE TABLE `t_eth_block` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key',
  `f_create_time` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'create time',
  `f_update_time` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'update time',
  `f_version` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'version',
  `f_block_number` bigint(20) unsigned NOT NULL DEFAULT '0',
  `f_block_hash` varchar(80) NOT NULL DEFAULT '',
  `f_parent_hash` varchar(80) NOT NULL DEFAULT '' COMMENT 'userId',
  `f_tx_time` bigint(20) unsigned DEFAULT '0',
  `f_status` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1001 DEFAULT CHARSET=utf8 COMMENT='t_eth_block';

-- ----------------------------
-- Records of t_eth_block
-- ----------------------------
BEGIN;
INSERT INTO `t_eth_block` (`f_id`, `f_create_time`, `f_update_time`, `f_version`, `f_block_number`, `f_block_hash`, `f_parent_hash`, `f_tx_time`, `f_status`) VALUES (1000, 0, 0, 0, 2, '0xb189e9f3e5d42b832ce9e0d7881c4865fec2547e734ee6acda46517b2925e968', '0xfa594bcb6c36486b99991bb2443f63844d83f80874e05d5ef6079ff58207b8eb', 0, 1);
COMMIT;

-- ----------------------------
-- Table structure for t_exchange_rule
-- ----------------------------
DROP TABLE IF EXISTS `t_exchange_rule`;
CREATE TABLE `t_exchange_rule` (
  `f_symbol` varchar(24) NOT NULL COMMENT '币对',
  `f_source_currency` varchar(12) NOT NULL COMMENT '币种',
  `f_target_currency` varchar(12) NOT NULL COMMENT '币种',
  `f_min_amount` decimal(36,18) NOT NULL DEFAULT '0.000000000000000000' COMMENT '最小',
  `f_max_amount` decimal(36,18) NOT NULL DEFAULT '0.000000000000000000' COMMENT '最大',
  `f_intraday_amount` decimal(36,18) NOT NULL DEFAULT '0.000000000000000000' COMMENT '日最多',
  `f_enable` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否启用：0停用，1启用',
  `f_spread` varchar(64) NOT NULL DEFAULT '0' COMMENT '[{"level":xx, "spread": bps}]',
  PRIMARY KEY (`f_symbol`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='兑换规则';

-- ----------------------------
-- Records of t_exchange_rule
-- ----------------------------
BEGIN;
INSERT INTO `t_exchange_rule` (`f_symbol`, `f_source_currency`, `f_target_currency`, `f_min_amount`, `f_max_amount`, `f_intraday_amount`, `f_enable`, `f_spread`) VALUES ('kUSD/USDT', 'kUSD', 'USDT', 1.000000000000000000, 100000.000000000000000000, 500000.000000000000000000, 1, '[]');
INSERT INTO `t_exchange_rule` (`f_symbol`, `f_source_currency`, `f_target_currency`, `f_min_amount`, `f_max_amount`, `f_intraday_amount`, `f_enable`, `f_spread`) VALUES ('USDT/kUSD', 'USDT', 'kUSD', 1.000000000000000000, 100000.000000000000000000, 500000.000000000000000000, 1, '[]');
COMMIT;

-- ----------------------------
-- Table structure for t_funding_rate
-- ----------------------------
DROP TABLE IF EXISTS `t_funding_rate`;
CREATE TABLE `t_funding_rate` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key',
  `f_create_time` bigint(20) NOT NULL DEFAULT '0',
  `f_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: current, 1: settlement twap',
  `f_currency` varchar(12) NOT NULL,
  `f_rate` decimal(36,18) NOT NULL DEFAULT '0.000000000000000000',
  PRIMARY KEY (`f_id`),
  KEY `idx_currency_time` (`f_currency`,`f_create_time`),
  KEY `idx_time_currency` (`f_create_time`,`f_currency`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='funding rate';

-- ----------------------------
-- Records of t_funding_rate
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_keyvalue_pair
-- ----------------------------
DROP TABLE IF EXISTS `t_keyvalue_pair`;
CREATE TABLE `t_keyvalue_pair` (
  `f_category` varchar(64) NOT NULL COMMENT '分组',
  `f_key` varchar(64) NOT NULL COMMENT 'key',
  `f_value` varchar(64) NOT NULL COMMENT 'value',
  PRIMARY KEY (`f_category`,`f_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='配置';

-- ----------------------------
-- Records of t_keyvalue_pair
-- ----------------------------
BEGIN;
INSERT INTO `t_keyvalue_pair` (`f_category`, `f_key`, `f_value`) VALUES ('asset.holding.rate', 'maxRate', '0.00375');
INSERT INTO `t_keyvalue_pair` (`f_category`, `f_key`, `f_value`) VALUES ('asset.holding.rate', 't.kBCH', '0.05');
INSERT INTO `t_keyvalue_pair` (`f_category`, `f_key`, `f_value`) VALUES ('asset.holding.rate', 't.kBTC', '0.10');
INSERT INTO `t_keyvalue_pair` (`f_category`, `f_key`, `f_value`) VALUES ('asset.holding.rate', 't.kEOS', '0.05');
INSERT INTO `t_keyvalue_pair` (`f_category`, `f_key`, `f_value`) VALUES ('asset.holding.rate', 't.kETH', '0.05');
INSERT INTO `t_keyvalue_pair` (`f_category`, `f_key`, `f_value`) VALUES ('asset.holding.rate', 't.kLTC', '0.05');
INSERT INTO `t_keyvalue_pair` (`f_category`, `f_key`, `f_value`) VALUES ('eth.chain', 'gas.limit', '200000');
INSERT INTO `t_keyvalue_pair` (`f_category`, `f_key`, `f_value`) VALUES ('eth.chain', 'gas.price', '20000000000');
COMMIT;

-- ----------------------------
-- Table structure for t_market_kline_15min_kbch
-- ----------------------------
DROP TABLE IF EXISTS `t_market_kline_15min_kbch`;
CREATE TABLE `t_market_kline_15min_kbch` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key ID',
  `f_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易数量',
  `f_vol` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易总额，USDT计价',
  `f_count` bigint(20) unsigned NOT NULL COMMENT '交易次数',
  `f_open` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '开盘价',
  `f_close` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '收盘价',
  `f_high` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最高价',
  `f_low` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最低价',
  `f_created_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'created timestamp',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='kbch 15分钟K线';

-- ----------------------------
-- Records of t_market_kline_15min_kbch
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_market_kline_15min_kbtc
-- ----------------------------
DROP TABLE IF EXISTS `t_market_kline_15min_kbtc`;
CREATE TABLE `t_market_kline_15min_kbtc` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key ID',
  `f_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易数量',
  `f_vol` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易总额，USDT计价',
  `f_count` bigint(20) unsigned NOT NULL COMMENT '交易次数',
  `f_open` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '开盘价',
  `f_close` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '收盘价',
  `f_high` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最高价',
  `f_low` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最低价',
  `f_created_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'created timestamp',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='kbtc 15分钟K线';

-- ----------------------------
-- Records of t_market_kline_15min_kbtc
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_market_kline_15min_keos
-- ----------------------------
DROP TABLE IF EXISTS `t_market_kline_15min_keos`;
CREATE TABLE `t_market_kline_15min_keos` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key ID',
  `f_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易数量',
  `f_vol` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易总额，USDT计价',
  `f_count` bigint(20) unsigned NOT NULL COMMENT '交易次数',
  `f_open` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '开盘价',
  `f_close` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '收盘价',
  `f_high` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最高价',
  `f_low` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最低价',
  `f_created_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'created timestamp',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='keos 15分钟K线';

-- ----------------------------
-- Records of t_market_kline_15min_keos
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_market_kline_15min_keth
-- ----------------------------
DROP TABLE IF EXISTS `t_market_kline_15min_keth`;
CREATE TABLE `t_market_kline_15min_keth` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key ID',
  `f_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易数量',
  `f_vol` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易总额，USDT计价',
  `f_count` bigint(20) unsigned NOT NULL COMMENT '交易次数',
  `f_open` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '开盘价',
  `f_close` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '收盘价',
  `f_high` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最高价',
  `f_low` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最低价',
  `f_created_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'created timestamp',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='keth 15分钟K线';

-- ----------------------------
-- Records of t_market_kline_15min_keth
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_market_kline_15min_kltc
-- ----------------------------
DROP TABLE IF EXISTS `t_market_kline_15min_kltc`;
CREATE TABLE `t_market_kline_15min_kltc` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key ID',
  `f_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易数量',
  `f_vol` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易总额，USDT计价',
  `f_count` bigint(20) unsigned NOT NULL COMMENT '交易次数',
  `f_open` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '开盘价',
  `f_close` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '收盘价',
  `f_high` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最高价',
  `f_low` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最低价',
  `f_created_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'created timestamp',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='kltc 15分钟K线';

-- ----------------------------
-- Records of t_market_kline_15min_kltc
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_market_kline_15min_usdcny
-- ----------------------------
DROP TABLE IF EXISTS `t_market_kline_15min_usdcny`;
CREATE TABLE `t_market_kline_15min_usdcny` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key ID',
  `f_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易数量',
  `f_vol` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易总额，USDT计价',
  `f_count` bigint(20) unsigned NOT NULL COMMENT '交易次数',
  `f_open` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '开盘价',
  `f_close` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '收盘价',
  `f_high` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最高价',
  `f_low` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最低价',
  `f_created_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'created timestamp',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='usdcny 15分钟K线';

-- ----------------------------
-- Records of t_market_kline_15min_usdcny
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_market_kline_1day_kbch
-- ----------------------------
DROP TABLE IF EXISTS `t_market_kline_1day_kbch`;
CREATE TABLE `t_market_kline_1day_kbch` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key ID',
  `f_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易数量',
  `f_vol` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易总额，USDT计价',
  `f_count` bigint(20) unsigned NOT NULL COMMENT '交易次数',
  `f_open` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '开盘价',
  `f_close` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '收盘价',
  `f_high` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最高价',
  `f_low` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最低价',
  `f_created_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'created timestamp',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='kbch 1天K线';

-- ----------------------------
-- Records of t_market_kline_1day_kbch
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_market_kline_1day_kbtc
-- ----------------------------
DROP TABLE IF EXISTS `t_market_kline_1day_kbtc`;
CREATE TABLE `t_market_kline_1day_kbtc` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key ID',
  `f_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易数量',
  `f_vol` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易总额，USDT计价',
  `f_count` bigint(20) unsigned NOT NULL COMMENT '交易次数',
  `f_open` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '开盘价',
  `f_close` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '收盘价',
  `f_high` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最高价',
  `f_low` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最低价',
  `f_created_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'created timestamp',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='kbtc 1天K线';

-- ----------------------------
-- Records of t_market_kline_1day_kbtc
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_market_kline_1day_keos
-- ----------------------------
DROP TABLE IF EXISTS `t_market_kline_1day_keos`;
CREATE TABLE `t_market_kline_1day_keos` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key ID',
  `f_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易数量',
  `f_vol` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易总额，USDT计价',
  `f_count` bigint(20) unsigned NOT NULL COMMENT '交易次数',
  `f_open` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '开盘价',
  `f_close` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '收盘价',
  `f_high` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最高价',
  `f_low` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最低价',
  `f_created_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'created timestamp',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='keos 1天K线';

-- ----------------------------
-- Records of t_market_kline_1day_keos
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_market_kline_1day_keth
-- ----------------------------
DROP TABLE IF EXISTS `t_market_kline_1day_keth`;
CREATE TABLE `t_market_kline_1day_keth` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key ID',
  `f_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易数量',
  `f_vol` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易总额，USDT计价',
  `f_count` bigint(20) unsigned NOT NULL COMMENT '交易次数',
  `f_open` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '开盘价',
  `f_close` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '收盘价',
  `f_high` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最高价',
  `f_low` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最低价',
  `f_created_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'created timestamp',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='keth 1天K线';

-- ----------------------------
-- Records of t_market_kline_1day_keth
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_market_kline_1day_kltc
-- ----------------------------
DROP TABLE IF EXISTS `t_market_kline_1day_kltc`;
CREATE TABLE `t_market_kline_1day_kltc` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key ID',
  `f_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易数量',
  `f_vol` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易总额，USDT计价',
  `f_count` bigint(20) unsigned NOT NULL COMMENT '交易次数',
  `f_open` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '开盘价',
  `f_close` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '收盘价',
  `f_high` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最高价',
  `f_low` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最低价',
  `f_created_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'created timestamp',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='kltc 1天K线';

-- ----------------------------
-- Records of t_market_kline_1day_kltc
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_market_kline_1day_usdcny
-- ----------------------------
DROP TABLE IF EXISTS `t_market_kline_1day_usdcny`;
CREATE TABLE `t_market_kline_1day_usdcny` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key ID',
  `f_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易数量',
  `f_vol` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易总额，USDT计价',
  `f_count` bigint(20) unsigned NOT NULL COMMENT '交易次数',
  `f_open` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '开盘价',
  `f_close` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '收盘价',
  `f_high` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最高价',
  `f_low` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最低价',
  `f_created_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'created timestamp',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='usdcny 1天K线';

-- ----------------------------
-- Records of t_market_kline_1day_usdcny
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_market_kline_1min_kbch
-- ----------------------------
DROP TABLE IF EXISTS `t_market_kline_1min_kbch`;
CREATE TABLE `t_market_kline_1min_kbch` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key ID',
  `f_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易数量',
  `f_vol` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易总额，USDT计价',
  `f_count` bigint(20) unsigned NOT NULL COMMENT '交易次数',
  `f_open` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '开盘价',
  `f_close` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '收盘价',
  `f_high` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最高价',
  `f_low` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最低价',
  `f_created_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'created timestamp',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='kbch 1分钟K线';

-- ----------------------------
-- Records of t_market_kline_1min_kbch
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_market_kline_1min_kbtc
-- ----------------------------
DROP TABLE IF EXISTS `t_market_kline_1min_kbtc`;
CREATE TABLE `t_market_kline_1min_kbtc` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key ID',
  `f_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易数量',
  `f_vol` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易总额，USDT计价',
  `f_count` bigint(20) unsigned NOT NULL COMMENT '交易次数',
  `f_open` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '开盘价',
  `f_close` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '收盘价',
  `f_high` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最高价',
  `f_low` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最低价',
  `f_created_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'created timestamp',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='kbtc 1分钟K线';

-- ----------------------------
-- Records of t_market_kline_1min_kbtc
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_market_kline_1min_keos
-- ----------------------------
DROP TABLE IF EXISTS `t_market_kline_1min_keos`;
CREATE TABLE `t_market_kline_1min_keos` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key ID',
  `f_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易数量',
  `f_vol` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易总额，USDT计价',
  `f_count` bigint(20) unsigned NOT NULL COMMENT '交易次数',
  `f_open` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '开盘价',
  `f_close` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '收盘价',
  `f_high` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最高价',
  `f_low` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最低价',
  `f_created_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'created timestamp',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='keos 1分钟K线';

-- ----------------------------
-- Records of t_market_kline_1min_keos
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_market_kline_1min_keth
-- ----------------------------
DROP TABLE IF EXISTS `t_market_kline_1min_keth`;
CREATE TABLE `t_market_kline_1min_keth` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key ID',
  `f_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易数量',
  `f_vol` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易总额，USDT计价',
  `f_count` bigint(20) unsigned NOT NULL COMMENT '交易次数',
  `f_open` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '开盘价',
  `f_close` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '收盘价',
  `f_high` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最高价',
  `f_low` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最低价',
  `f_created_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'created timestamp',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='keth 1分钟K线';

-- ----------------------------
-- Records of t_market_kline_1min_keth
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_market_kline_1min_kltc
-- ----------------------------
DROP TABLE IF EXISTS `t_market_kline_1min_kltc`;
CREATE TABLE `t_market_kline_1min_kltc` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key ID',
  `f_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易数量',
  `f_vol` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易总额，USDT计价',
  `f_count` bigint(20) unsigned NOT NULL COMMENT '交易次数',
  `f_open` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '开盘价',
  `f_close` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '收盘价',
  `f_high` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最高价',
  `f_low` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最低价',
  `f_created_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'created timestamp',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='kltc 1分钟K线';

-- ----------------------------
-- Records of t_market_kline_1min_kltc
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_market_kline_1min_usdcny
-- ----------------------------
DROP TABLE IF EXISTS `t_market_kline_1min_usdcny`;
CREATE TABLE `t_market_kline_1min_usdcny` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key ID',
  `f_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易数量',
  `f_vol` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易总额，USD计价',
  `f_count` bigint(20) unsigned NOT NULL COMMENT '交易次数',
  `f_open` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '开盘价',
  `f_close` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '收盘价',
  `f_high` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最高价',
  `f_low` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最低价',
  `f_created_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'created timestamp',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='usdcny 1分钟K线';

-- ----------------------------
-- Records of t_market_kline_1min_usdcny
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_market_kline_1mon_kbch
-- ----------------------------
DROP TABLE IF EXISTS `t_market_kline_1mon_kbch`;
CREATE TABLE `t_market_kline_1mon_kbch` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key ID',
  `f_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易数量',
  `f_vol` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易总额，USDT计价',
  `f_count` bigint(20) unsigned NOT NULL COMMENT '交易次数',
  `f_open` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '开盘价',
  `f_close` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '收盘价',
  `f_high` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最高价',
  `f_low` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最低价',
  `f_created_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'created timestamp',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='kbch 1月K线';

-- ----------------------------
-- Records of t_market_kline_1mon_kbch
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_market_kline_1mon_kbtc
-- ----------------------------
DROP TABLE IF EXISTS `t_market_kline_1mon_kbtc`;
CREATE TABLE `t_market_kline_1mon_kbtc` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key ID',
  `f_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易数量',
  `f_vol` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易总额，USDT计价',
  `f_count` bigint(20) unsigned NOT NULL COMMENT '交易次数',
  `f_open` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '开盘价',
  `f_close` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '收盘价',
  `f_high` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最高价',
  `f_low` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最低价',
  `f_created_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'created timestamp',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='kbtc 1月K线';

-- ----------------------------
-- Records of t_market_kline_1mon_kbtc
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_market_kline_1mon_keos
-- ----------------------------
DROP TABLE IF EXISTS `t_market_kline_1mon_keos`;
CREATE TABLE `t_market_kline_1mon_keos` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key ID',
  `f_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易数量',
  `f_vol` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易总额，USDT计价',
  `f_count` bigint(20) unsigned NOT NULL COMMENT '交易次数',
  `f_open` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '开盘价',
  `f_close` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '收盘价',
  `f_high` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最高价',
  `f_low` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最低价',
  `f_created_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'created timestamp',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='keos 1月K线';

-- ----------------------------
-- Records of t_market_kline_1mon_keos
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_market_kline_1mon_keth
-- ----------------------------
DROP TABLE IF EXISTS `t_market_kline_1mon_keth`;
CREATE TABLE `t_market_kline_1mon_keth` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key ID',
  `f_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易数量',
  `f_vol` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易总额，USDT计价',
  `f_count` bigint(20) unsigned NOT NULL COMMENT '交易次数',
  `f_open` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '开盘价',
  `f_close` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '收盘价',
  `f_high` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最高价',
  `f_low` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最低价',
  `f_created_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'created timestamp',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='keth 1月K线';

-- ----------------------------
-- Records of t_market_kline_1mon_keth
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_market_kline_1mon_kltc
-- ----------------------------
DROP TABLE IF EXISTS `t_market_kline_1mon_kltc`;
CREATE TABLE `t_market_kline_1mon_kltc` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key ID',
  `f_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易数量',
  `f_vol` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易总额，USDT计价',
  `f_count` bigint(20) unsigned NOT NULL COMMENT '交易次数',
  `f_open` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '开盘价',
  `f_close` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '收盘价',
  `f_high` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最高价',
  `f_low` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最低价',
  `f_created_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'created timestamp',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='kltc 1月K线';

-- ----------------------------
-- Records of t_market_kline_1mon_kltc
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_market_kline_1mon_usdcny
-- ----------------------------
DROP TABLE IF EXISTS `t_market_kline_1mon_usdcny`;
CREATE TABLE `t_market_kline_1mon_usdcny` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key ID',
  `f_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易数量',
  `f_vol` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易总额，USDT计价',
  `f_count` bigint(20) unsigned NOT NULL COMMENT '交易次数',
  `f_open` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '开盘价',
  `f_close` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '收盘价',
  `f_high` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最高价',
  `f_low` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最低价',
  `f_created_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'created timestamp',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='usdcny 1月K线';

-- ----------------------------
-- Records of t_market_kline_1mon_usdcny
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_market_kline_1w_kbch
-- ----------------------------
DROP TABLE IF EXISTS `t_market_kline_1w_kbch`;
CREATE TABLE `t_market_kline_1w_kbch` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key ID',
  `f_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易数量',
  `f_vol` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易总额，USDT计价',
  `f_count` bigint(20) unsigned NOT NULL COMMENT '交易次数',
  `f_open` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '开盘价',
  `f_close` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '收盘价',
  `f_high` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最高价',
  `f_low` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最低价',
  `f_created_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'created timestamp',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='kbch 1周K线';

-- ----------------------------
-- Records of t_market_kline_1w_kbch
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_market_kline_1w_kbtc
-- ----------------------------
DROP TABLE IF EXISTS `t_market_kline_1w_kbtc`;
CREATE TABLE `t_market_kline_1w_kbtc` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key ID',
  `f_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易数量',
  `f_vol` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易总额，USDT计价',
  `f_count` bigint(20) unsigned NOT NULL COMMENT '交易次数',
  `f_open` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '开盘价',
  `f_close` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '收盘价',
  `f_high` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最高价',
  `f_low` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最低价',
  `f_created_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'created timestamp',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='kbtc 1周K线';

-- ----------------------------
-- Records of t_market_kline_1w_kbtc
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_market_kline_1w_keos
-- ----------------------------
DROP TABLE IF EXISTS `t_market_kline_1w_keos`;
CREATE TABLE `t_market_kline_1w_keos` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key ID',
  `f_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易数量',
  `f_vol` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易总额，USDT计价',
  `f_count` bigint(20) unsigned NOT NULL COMMENT '交易次数',
  `f_open` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '开盘价',
  `f_close` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '收盘价',
  `f_high` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最高价',
  `f_low` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最低价',
  `f_created_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'created timestamp',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='keos 1周K线';

-- ----------------------------
-- Records of t_market_kline_1w_keos
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_market_kline_1w_keth
-- ----------------------------
DROP TABLE IF EXISTS `t_market_kline_1w_keth`;
CREATE TABLE `t_market_kline_1w_keth` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key ID',
  `f_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易数量',
  `f_vol` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易总额，USDT计价',
  `f_count` bigint(20) unsigned NOT NULL COMMENT '交易次数',
  `f_open` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '开盘价',
  `f_close` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '收盘价',
  `f_high` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最高价',
  `f_low` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最低价',
  `f_created_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'created timestamp',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='keth 1周K线';

-- ----------------------------
-- Records of t_market_kline_1w_keth
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_market_kline_1w_kltc
-- ----------------------------
DROP TABLE IF EXISTS `t_market_kline_1w_kltc`;
CREATE TABLE `t_market_kline_1w_kltc` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key ID',
  `f_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易数量',
  `f_vol` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易总额，USDT计价',
  `f_count` bigint(20) unsigned NOT NULL COMMENT '交易次数',
  `f_open` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '开盘价',
  `f_close` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '收盘价',
  `f_high` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最高价',
  `f_low` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最低价',
  `f_created_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'created timestamp',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='kltc 1周K线';

-- ----------------------------
-- Records of t_market_kline_1w_kltc
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_market_kline_1w_usdcny
-- ----------------------------
DROP TABLE IF EXISTS `t_market_kline_1w_usdcny`;
CREATE TABLE `t_market_kline_1w_usdcny` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key ID',
  `f_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易数量',
  `f_vol` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易总额，USDT计价',
  `f_count` bigint(20) unsigned NOT NULL COMMENT '交易次数',
  `f_open` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '开盘价',
  `f_close` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '收盘价',
  `f_high` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最高价',
  `f_low` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最低价',
  `f_created_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'created timestamp',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='usdcny 1周K线';

-- ----------------------------
-- Records of t_market_kline_1w_usdcny
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_market_kline_30min_kbch
-- ----------------------------
DROP TABLE IF EXISTS `t_market_kline_30min_kbch`;
CREATE TABLE `t_market_kline_30min_kbch` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key ID',
  `f_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易数量',
  `f_vol` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易总额，USDT计价',
  `f_count` bigint(20) unsigned NOT NULL COMMENT '交易次数',
  `f_open` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '开盘价',
  `f_close` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '收盘价',
  `f_high` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最高价',
  `f_low` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最低价',
  `f_created_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'created timestamp',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='kbch 30分钟K线';

-- ----------------------------
-- Records of t_market_kline_30min_kbch
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_market_kline_30min_kbtc
-- ----------------------------
DROP TABLE IF EXISTS `t_market_kline_30min_kbtc`;
CREATE TABLE `t_market_kline_30min_kbtc` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key ID',
  `f_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易数量',
  `f_vol` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易总额，USDT计价',
  `f_count` bigint(20) unsigned NOT NULL COMMENT '交易次数',
  `f_open` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '开盘价',
  `f_close` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '收盘价',
  `f_high` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最高价',
  `f_low` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最低价',
  `f_created_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'created timestamp',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='kbtc 30分钟K线';

-- ----------------------------
-- Records of t_market_kline_30min_kbtc
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_market_kline_30min_keos
-- ----------------------------
DROP TABLE IF EXISTS `t_market_kline_30min_keos`;
CREATE TABLE `t_market_kline_30min_keos` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key ID',
  `f_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易数量',
  `f_vol` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易总额，USDT计价',
  `f_count` bigint(20) unsigned NOT NULL COMMENT '交易次数',
  `f_open` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '开盘价',
  `f_close` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '收盘价',
  `f_high` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最高价',
  `f_low` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最低价',
  `f_created_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'created timestamp',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='keos 30分钟K线';

-- ----------------------------
-- Records of t_market_kline_30min_keos
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_market_kline_30min_keth
-- ----------------------------
DROP TABLE IF EXISTS `t_market_kline_30min_keth`;
CREATE TABLE `t_market_kline_30min_keth` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key ID',
  `f_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易数量',
  `f_vol` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易总额，USDT计价',
  `f_count` bigint(20) unsigned NOT NULL COMMENT '交易次数',
  `f_open` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '开盘价',
  `f_close` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '收盘价',
  `f_high` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最高价',
  `f_low` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最低价',
  `f_created_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'created timestamp',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='keth 30分钟K线';

-- ----------------------------
-- Records of t_market_kline_30min_keth
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_market_kline_30min_kltc
-- ----------------------------
DROP TABLE IF EXISTS `t_market_kline_30min_kltc`;
CREATE TABLE `t_market_kline_30min_kltc` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key ID',
  `f_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易数量',
  `f_vol` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易总额，USDT计价',
  `f_count` bigint(20) unsigned NOT NULL COMMENT '交易次数',
  `f_open` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '开盘价',
  `f_close` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '收盘价',
  `f_high` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最高价',
  `f_low` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最低价',
  `f_created_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'created timestamp',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='kltc 30分钟K线';

-- ----------------------------
-- Records of t_market_kline_30min_kltc
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_market_kline_30min_usdcny
-- ----------------------------
DROP TABLE IF EXISTS `t_market_kline_30min_usdcny`;
CREATE TABLE `t_market_kline_30min_usdcny` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key ID',
  `f_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易数量',
  `f_vol` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易总额，USDT计价',
  `f_count` bigint(20) unsigned NOT NULL COMMENT '交易次数',
  `f_open` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '开盘价',
  `f_close` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '收盘价',
  `f_high` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最高价',
  `f_low` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最低价',
  `f_created_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'created timestamp',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='usdcny 30分钟K线';

-- ----------------------------
-- Records of t_market_kline_30min_usdcny
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_market_kline_4hr_kbch
-- ----------------------------
DROP TABLE IF EXISTS `t_market_kline_4hr_kbch`;
CREATE TABLE `t_market_kline_4hr_kbch` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key ID',
  `f_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易数量',
  `f_vol` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易总额，USDT计价',
  `f_count` bigint(20) unsigned NOT NULL COMMENT '交易次数',
  `f_open` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '开盘价',
  `f_close` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '收盘价',
  `f_high` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最高价',
  `f_low` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最低价',
  `f_created_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'created timestamp',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='kbch 4小时K线';

-- ----------------------------
-- Records of t_market_kline_4hr_kbch
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_market_kline_4hr_kbtc
-- ----------------------------
DROP TABLE IF EXISTS `t_market_kline_4hr_kbtc`;
CREATE TABLE `t_market_kline_4hr_kbtc` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key ID',
  `f_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易数量',
  `f_vol` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易总额，USDT计价',
  `f_count` bigint(20) unsigned NOT NULL COMMENT '交易次数',
  `f_open` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '开盘价',
  `f_close` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '收盘价',
  `f_high` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最高价',
  `f_low` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最低价',
  `f_created_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'created timestamp',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='kbtc 4小时K线';

-- ----------------------------
-- Records of t_market_kline_4hr_kbtc
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_market_kline_4hr_keos
-- ----------------------------
DROP TABLE IF EXISTS `t_market_kline_4hr_keos`;
CREATE TABLE `t_market_kline_4hr_keos` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key ID',
  `f_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易数量',
  `f_vol` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易总额，USDT计价',
  `f_count` bigint(20) unsigned NOT NULL COMMENT '交易次数',
  `f_open` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '开盘价',
  `f_close` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '收盘价',
  `f_high` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最高价',
  `f_low` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最低价',
  `f_created_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'created timestamp',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='keos 4小时K线';

-- ----------------------------
-- Records of t_market_kline_4hr_keos
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_market_kline_4hr_keth
-- ----------------------------
DROP TABLE IF EXISTS `t_market_kline_4hr_keth`;
CREATE TABLE `t_market_kline_4hr_keth` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key ID',
  `f_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易数量',
  `f_vol` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易总额，USDT计价',
  `f_count` bigint(20) unsigned NOT NULL COMMENT '交易次数',
  `f_open` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '开盘价',
  `f_close` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '收盘价',
  `f_high` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最高价',
  `f_low` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最低价',
  `f_created_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'created timestamp',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='keth 4小时K线';

-- ----------------------------
-- Records of t_market_kline_4hr_keth
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_market_kline_4hr_kltc
-- ----------------------------
DROP TABLE IF EXISTS `t_market_kline_4hr_kltc`;
CREATE TABLE `t_market_kline_4hr_kltc` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key ID',
  `f_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易数量',
  `f_vol` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易总额，USDT计价',
  `f_count` bigint(20) unsigned NOT NULL COMMENT '交易次数',
  `f_open` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '开盘价',
  `f_close` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '收盘价',
  `f_high` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最高价',
  `f_low` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最低价',
  `f_created_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'created timestamp',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='kltc 4小时K线';

-- ----------------------------
-- Records of t_market_kline_4hr_kltc
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_market_kline_4hr_usdcny
-- ----------------------------
DROP TABLE IF EXISTS `t_market_kline_4hr_usdcny`;
CREATE TABLE `t_market_kline_4hr_usdcny` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key ID',
  `f_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易数量',
  `f_vol` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易总额，USDT计价',
  `f_count` bigint(20) unsigned NOT NULL COMMENT '交易次数',
  `f_open` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '开盘价',
  `f_close` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '收盘价',
  `f_high` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最高价',
  `f_low` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最低价',
  `f_created_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'created timestamp',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='usdcny 4小时K线';

-- ----------------------------
-- Records of t_market_kline_4hr_usdcny
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_market_kline_5min_kbch
-- ----------------------------
DROP TABLE IF EXISTS `t_market_kline_5min_kbch`;
CREATE TABLE `t_market_kline_5min_kbch` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key ID',
  `f_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易数量',
  `f_vol` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易总额，USDT计价',
  `f_count` bigint(20) unsigned NOT NULL COMMENT '交易次数',
  `f_open` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '开盘价',
  `f_close` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '收盘价',
  `f_high` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最高价',
  `f_low` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最低价',
  `f_created_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'created timestamp',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='kbch 5分钟K线';

-- ----------------------------
-- Records of t_market_kline_5min_kbch
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_market_kline_5min_kbtc
-- ----------------------------
DROP TABLE IF EXISTS `t_market_kline_5min_kbtc`;
CREATE TABLE `t_market_kline_5min_kbtc` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key ID',
  `f_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易数量',
  `f_vol` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易总额，USDT计价',
  `f_count` bigint(20) unsigned NOT NULL COMMENT '交易次数',
  `f_open` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '开盘价',
  `f_close` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '收盘价',
  `f_high` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最高价',
  `f_low` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最低价',
  `f_created_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'created timestamp',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='kbtc 5分钟K线';

-- ----------------------------
-- Records of t_market_kline_5min_kbtc
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_market_kline_5min_keos
-- ----------------------------
DROP TABLE IF EXISTS `t_market_kline_5min_keos`;
CREATE TABLE `t_market_kline_5min_keos` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key ID',
  `f_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易数量',
  `f_vol` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易总额，USDT计价',
  `f_count` bigint(20) unsigned NOT NULL COMMENT '交易次数',
  `f_open` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '开盘价',
  `f_close` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '收盘价',
  `f_high` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最高价',
  `f_low` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最低价',
  `f_created_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'created timestamp',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='keos 5分钟K线';

-- ----------------------------
-- Records of t_market_kline_5min_keos
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_market_kline_5min_keth
-- ----------------------------
DROP TABLE IF EXISTS `t_market_kline_5min_keth`;
CREATE TABLE `t_market_kline_5min_keth` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key ID',
  `f_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易数量',
  `f_vol` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易总额，USDT计价',
  `f_count` bigint(20) unsigned NOT NULL COMMENT '交易次数',
  `f_open` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '开盘价',
  `f_close` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '收盘价',
  `f_high` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最高价',
  `f_low` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最低价',
  `f_created_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'created timestamp',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='keth 5分钟K线';

-- ----------------------------
-- Records of t_market_kline_5min_keth
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_market_kline_5min_kltc
-- ----------------------------
DROP TABLE IF EXISTS `t_market_kline_5min_kltc`;
CREATE TABLE `t_market_kline_5min_kltc` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key ID',
  `f_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易数量',
  `f_vol` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易总额，USDT计价',
  `f_count` bigint(20) unsigned NOT NULL COMMENT '交易次数',
  `f_open` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '开盘价',
  `f_close` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '收盘价',
  `f_high` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最高价',
  `f_low` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最低价',
  `f_created_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'created timestamp',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='kltc 5分钟K线';

-- ----------------------------
-- Records of t_market_kline_5min_kltc
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_market_kline_5min_usdcny
-- ----------------------------
DROP TABLE IF EXISTS `t_market_kline_5min_usdcny`;
CREATE TABLE `t_market_kline_5min_usdcny` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key ID',
  `f_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易数量',
  `f_vol` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易总额，USDT计价',
  `f_count` bigint(20) unsigned NOT NULL COMMENT '交易次数',
  `f_open` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '开盘价',
  `f_close` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '收盘价',
  `f_high` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最高价',
  `f_low` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最低价',
  `f_created_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'created timestamp',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='usdcny 5分钟K线';

-- ----------------------------
-- Records of t_market_kline_5min_usdcny
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_market_kline_60min_kbch
-- ----------------------------
DROP TABLE IF EXISTS `t_market_kline_60min_kbch`;
CREATE TABLE `t_market_kline_60min_kbch` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key ID',
  `f_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易数量',
  `f_vol` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易总额，USDT计价',
  `f_count` bigint(20) unsigned NOT NULL COMMENT '交易次数',
  `f_open` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '开盘价',
  `f_close` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '收盘价',
  `f_high` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最高价',
  `f_low` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最低价',
  `f_created_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'created timestamp',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='kbch 60分钟K线';

-- ----------------------------
-- Records of t_market_kline_60min_kbch
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_market_kline_60min_kbtc
-- ----------------------------
DROP TABLE IF EXISTS `t_market_kline_60min_kbtc`;
CREATE TABLE `t_market_kline_60min_kbtc` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key ID',
  `f_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易数量',
  `f_vol` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易总额，USDT计价',
  `f_count` bigint(20) unsigned NOT NULL COMMENT '交易次数',
  `f_open` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '开盘价',
  `f_close` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '收盘价',
  `f_high` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最高价',
  `f_low` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最低价',
  `f_created_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'created timestamp',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='kbtc 60分钟K线';

-- ----------------------------
-- Records of t_market_kline_60min_kbtc
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_market_kline_60min_keos
-- ----------------------------
DROP TABLE IF EXISTS `t_market_kline_60min_keos`;
CREATE TABLE `t_market_kline_60min_keos` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key ID',
  `f_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易数量',
  `f_vol` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易总额，USDT计价',
  `f_count` bigint(20) unsigned NOT NULL COMMENT '交易次数',
  `f_open` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '开盘价',
  `f_close` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '收盘价',
  `f_high` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最高价',
  `f_low` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最低价',
  `f_created_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'created timestamp',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='keos 60分钟K线';

-- ----------------------------
-- Records of t_market_kline_60min_keos
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_market_kline_60min_keth
-- ----------------------------
DROP TABLE IF EXISTS `t_market_kline_60min_keth`;
CREATE TABLE `t_market_kline_60min_keth` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key ID',
  `f_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易数量',
  `f_vol` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易总额，USDT计价',
  `f_count` bigint(20) unsigned NOT NULL COMMENT '交易次数',
  `f_open` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '开盘价',
  `f_close` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '收盘价',
  `f_high` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最高价',
  `f_low` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最低价',
  `f_created_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'created timestamp',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='keth 60分钟K线';

-- ----------------------------
-- Records of t_market_kline_60min_keth
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_market_kline_60min_kltc
-- ----------------------------
DROP TABLE IF EXISTS `t_market_kline_60min_kltc`;
CREATE TABLE `t_market_kline_60min_kltc` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key ID',
  `f_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易数量',
  `f_vol` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易总额，USDT计价',
  `f_count` bigint(20) unsigned NOT NULL COMMENT '交易次数',
  `f_open` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '开盘价',
  `f_close` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '收盘价',
  `f_high` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最高价',
  `f_low` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最低价',
  `f_created_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'created timestamp',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='kltc 60分钟K线';

-- ----------------------------
-- Records of t_market_kline_60min_kltc
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_market_kline_60min_usdcny
-- ----------------------------
DROP TABLE IF EXISTS `t_market_kline_60min_usdcny`;
CREATE TABLE `t_market_kline_60min_usdcny` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key ID',
  `f_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易数量',
  `f_vol` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易总额，USDT计价',
  `f_count` bigint(20) unsigned NOT NULL COMMENT '交易次数',
  `f_open` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '开盘价',
  `f_close` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '收盘价',
  `f_high` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最高价',
  `f_low` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '最低价',
  `f_created_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'created timestamp',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='usdcny 60分钟K线';

-- ----------------------------
-- Records of t_market_kline_60min_usdcny
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_market_price_cumulative
-- ----------------------------
DROP TABLE IF EXISTS `t_market_price_cumulative`;
CREATE TABLE `t_market_price_cumulative` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key ID',
  `f_block_hash` varchar(256) NOT NULL DEFAULT '' COMMENT 'ETH Block hash',
  `f_block_number` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'eth block number',
  `f_price_cumulative` text COMMENT 'price cumulative',
  `f_price_timestamp` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'price unix timestamp in second',
  `f_created_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'created unix timestamp in millisecond',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='uniswap v2 price cumulative';

-- ----------------------------
-- Records of t_market_price_cumulative
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_market_price_kbch
-- ----------------------------
DROP TABLE IF EXISTS `t_market_price_kbch`;
CREATE TABLE `t_market_price_kbch` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key ID',
  `f_price` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易数量',
  `f_created_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'created timestamp',
  PRIMARY KEY (`f_id`),
  KEY `idx_mpkbch_f_create_at` (`f_created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='bch 价格，来自火币，币安，okex交易所价格的加权平均';

-- ----------------------------
-- Records of t_market_price_kbch
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_market_price_kbtc
-- ----------------------------
DROP TABLE IF EXISTS `t_market_price_kbtc`;
CREATE TABLE `t_market_price_kbtc` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key ID',
  `f_price` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易数量',
  `f_created_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'created timestamp',
  PRIMARY KEY (`f_id`),
  KEY `idx_mpkbtc_f_create_at` (`f_created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='btc 价格，来自火币，币安，okex交易所价格的加权平均';

-- ----------------------------
-- Records of t_market_price_kbtc
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_market_price_keos
-- ----------------------------
DROP TABLE IF EXISTS `t_market_price_keos`;
CREATE TABLE `t_market_price_keos` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key ID',
  `f_price` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易数量',
  `f_created_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'created timestamp',
  PRIMARY KEY (`f_id`),
  KEY `idx_mpkeos_f_create_at` (`f_created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='eos 价格，来自火币，币安，okex交易所价格的加权平均';

-- ----------------------------
-- Records of t_market_price_keos
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_market_price_keth
-- ----------------------------
DROP TABLE IF EXISTS `t_market_price_keth`;
CREATE TABLE `t_market_price_keth` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key ID',
  `f_price` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易数量',
  `f_created_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'created timestamp',
  PRIMARY KEY (`f_id`),
  KEY `idx_mpketh_f_create_at` (`f_created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='eth 价格，来自火币，币安，okex交易所价格的加权平均';

-- ----------------------------
-- Records of t_market_price_keth
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_market_price_kltc
-- ----------------------------
DROP TABLE IF EXISTS `t_market_price_kltc`;
CREATE TABLE `t_market_price_kltc` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key ID',
  `f_price` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '交易数量',
  `f_created_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'created timestamp',
  PRIMARY KEY (`f_id`),
  KEY `idx_mpkltc_f_create_at` (`f_created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='ltc 价格，来自火币，币安，okex交易所价格的加权平均';

-- ----------------------------
-- Records of t_market_price_kltc
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_msg_mail
-- ----------------------------
DROP TABLE IF EXISTS `t_msg_mail`;
CREATE TABLE `t_msg_mail` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key',
  `f_create_time` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'create time',
  `f_update_time` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'update time',
  `f_version` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'version',
  `f_user_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'userId',
  `f_template_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '模版编号',
  `f_subject` varchar(200) NOT NULL DEFAULT '' COMMENT '标题',
  `f_content` varchar(500) NOT NULL DEFAULT '' COMMENT '内容',
  `f_email_to` varchar(200) NOT NULL DEFAULT '' COMMENT '收件人',
  `f_cc` varchar(200) NOT NULL DEFAULT '' COMMENT '抄送',
  `f_bcc` varchar(200) NOT NULL DEFAULT '' COMMENT '密抄',
  `f_is_html` int(11) NOT NULL DEFAULT '0' COMMENT '0文本 1 html',
  `f_status` int(11) NOT NULL DEFAULT '0' COMMENT '0未发送 1已发送 2已失败',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='t_msg_mail';

-- ----------------------------
-- Records of t_msg_mail
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_msg_sms
-- ----------------------------
DROP TABLE IF EXISTS `t_msg_sms`;
CREATE TABLE `t_msg_sms` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key',
  `f_create_time` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'create time',
  `f_update_time` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'update time',
  `f_version` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'version',
  `f_user_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'userId',
  `f_template_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '模版编号',
  `f_content` varchar(300) NOT NULL DEFAULT '' COMMENT '内容',
  `f_phone` varchar(300) NOT NULL DEFAULT '' COMMENT '手机号',
  `f_status` int(11) NOT NULL DEFAULT '0' COMMENT '0未发送，1已发送，2发送失败',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='t_msg_sms';

-- ----------------------------
-- Records of t_msg_sms
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_msg_template
-- ----------------------------
DROP TABLE IF EXISTS `t_msg_template`;
CREATE TABLE `t_msg_template` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key',
  `f_create_time` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'create time',
  `f_update_time` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'update time',
  `f_version` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'version',
  `f_name` varchar(100) NOT NULL DEFAULT '' COMMENT '模版名称',
  `f_subject` varchar(100) NOT NULL DEFAULT '' COMMENT '标题',
  `f_content` varchar(300) NOT NULL DEFAULT '' COMMENT '内容',
  `f_is_html` int(11) NOT NULL DEFAULT '0' COMMENT '0文本 1 html',
  `f_channel` int(11) NOT NULL DEFAULT '0' COMMENT '0邮件 1 短信',
  `f_msg_type_code` int(11) NOT NULL DEFAULT '0' COMMENT '业务类型代码',
  `f_msg_type_name` varchar(100) NOT NULL DEFAULT '' COMMENT '业务类型名称',
  `f_type` int(11) NOT NULL DEFAULT '0' COMMENT '1普通模版，2外置模版',
  `f_language` varchar(10) NOT NULL DEFAULT '' COMMENT '1中文，2英文',
  `f_status` int(11) NOT NULL DEFAULT '0' COMMENT '0未生效，1已使用，2已弃用',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8 COMMENT='t_msg_template';

-- ----------------------------
-- Records of t_msg_template
-- ----------------------------
BEGIN;
INSERT INTO `t_msg_template` (`f_id`, `f_create_time`, `f_update_time`, `f_version`, `f_name`, `f_subject`, `f_content`, `f_is_html`, `f_channel`, `f_msg_type_code`, `f_msg_type_name`, `f_type`, `f_language`, `f_status`) VALUES (1, 1598697904067, 1598697904067, 1, '质押爆仓预警', '质押爆仓提醒', '您的质押率为{ratio}', 0, 1, 27, 'STAKING_EXPLOSION_REMINDER', 1, 'zh-cn', 1);
INSERT INTO `t_msg_template` (`f_id`, `f_create_time`, `f_update_time`, `f_version`, `f_name`, `f_subject`, `f_content`, `f_is_html`, `f_channel`, `f_msg_type_code`, `f_msg_type_name`, `f_type`, `f_language`, `f_status`) VALUES (2, 1598697904067, 1598697904067, 1, 'StakingExplosioinReminder', 'Staking Explosion Reminder', 'Your current staking ratio is {ratio}', 0, 1, 27, 'STAKING_EXPLOSION_REMINDER', 1, 'en-us', 1);
INSERT INTO `t_msg_template` (`f_id`, `f_create_time`, `f_update_time`, `f_version`, `f_name`, `f_subject`, `f_content`, `f_is_html`, `f_channel`, `f_msg_type_code`, `f_msg_type_name`, `f_type`, `f_language`, `f_status`) VALUES (3, 1598697904067, 1598697904067, 1, '质押爆仓通知', '质押爆仓通知', '您的质押率不足，质押账户已爆仓', 0, 1, 28, 'STAKING_EXPLOSION_NOTICE', 1, 'zh-cn', 1);
INSERT INTO `t_msg_template` (`f_id`, `f_create_time`, `f_update_time`, `f_version`, `f_name`, `f_subject`, `f_content`, `f_is_html`, `f_channel`, `f_msg_type_code`, `f_msg_type_name`, `f_type`, `f_language`, `f_status`) VALUES (4, 1598697904067, 1598697904067, 1, 'StakingExplosioinNotice', 'Staking Explosion Notice', 'Your staking asset is already sold out due to insufficient staking ratio.', 0, 1, 28, 'STAKING_EXPLOSION_NOTICE', 1, 'en-us', 1);
INSERT INTO `t_msg_template` (`f_id`, `f_create_time`, `f_update_time`, `f_version`, `f_name`, `f_subject`, `f_content`, `f_is_html`, `f_channel`, `f_msg_type_code`, `f_msg_type_name`, `f_type`, `f_language`, `f_status`) VALUES (5, 1598697904067, 1598697904067, 1, '负债爆仓预警', '负债爆仓提醒', '您的负债质押率为{ratio}', 0, 1, 29, 'TRADING_EXPLOSION_REMINDER', 1, 'zh-cn', 1);
INSERT INTO `t_msg_template` (`f_id`, `f_create_time`, `f_update_time`, `f_version`, `f_name`, `f_subject`, `f_content`, `f_is_html`, `f_channel`, `f_msg_type_code`, `f_msg_type_name`, `f_type`, `f_language`, `f_status`) VALUES (6, 1598697904067, 1598697904067, 1, 'DebtExplosioinReminder', 'Debt Explosion Reminder', 'Your current debt ratio is {ratio}', 0, 1, 29, 'TRADING_EXPLOSION_REMINDER', 1, 'en-us', 1);
INSERT INTO `t_msg_template` (`f_id`, `f_create_time`, `f_update_time`, `f_version`, `f_name`, `f_subject`, `f_content`, `f_is_html`, `f_channel`, `f_msg_type_code`, `f_msg_type_name`, `f_type`, `f_language`, `f_status`) VALUES (7, 1598697904067, 1598697904067, 1, '负债爆仓通知', '负债爆仓通知', '您的负债率不足，交易账户已爆仓', 0, 1, 30, 'TRADING_EXPLOSION_NOTICE', 1, 'zh-cn', 1);
INSERT INTO `t_msg_template` (`f_id`, `f_create_time`, `f_update_time`, `f_version`, `f_name`, `f_subject`, `f_content`, `f_is_html`, `f_channel`, `f_msg_type_code`, `f_msg_type_name`, `f_type`, `f_language`, `f_status`) VALUES (8, 1598697904067, 1598697904067, 1, 'DebtExplosioinNotice', 'Debt Explosion Notice', 'Your asset in trading account is already sold out due to insufficient debt ratio.', 0, 1, 30, 'TRADING_EXPLOSION_NOTICE', 1, 'en-us', 1);
INSERT INTO `t_msg_template` (`f_id`, `f_create_time`, `f_update_time`, `f_version`, `f_name`, `f_subject`, `f_content`, `f_is_html`, `f_channel`, `f_msg_type_code`, `f_msg_type_name`, `f_type`, `f_language`, `f_status`) VALUES (9, 1598697904067, 1598697904067, 1, '质押爆仓预警', '705317', '您的质押率为{1}', 0, 2, 27, 'STAKING_EXPLOSION_REMINDER', 1, 'zh-cn', 1);
INSERT INTO `t_msg_template` (`f_id`, `f_create_time`, `f_update_time`, `f_version`, `f_name`, `f_subject`, `f_content`, `f_is_html`, `f_channel`, `f_msg_type_code`, `f_msg_type_name`, `f_type`, `f_language`, `f_status`) VALUES (10, 1598697904067, 1598697904067, 1, '质押爆仓通知', '706028', '您的质押率不足，质押账户已爆仓', 0, 2, 28, 'STAKING_EXPLOSION_NOTICE', 1, 'zh-cn', 1);
INSERT INTO `t_msg_template` (`f_id`, `f_create_time`, `f_update_time`, `f_version`, `f_name`, `f_subject`, `f_content`, `f_is_html`, `f_channel`, `f_msg_type_code`, `f_msg_type_name`, `f_type`, `f_language`, `f_status`) VALUES (11, 1598697904067, 1598697904067, 1, '负债爆仓预警', '705315', '您的负债质押率为{1}', 0, 2, 29, 'TRADING_EXPLOSION_REMINDER', 1, 'zh-cn', 1);
INSERT INTO `t_msg_template` (`f_id`, `f_create_time`, `f_update_time`, `f_version`, `f_name`, `f_subject`, `f_content`, `f_is_html`, `f_channel`, `f_msg_type_code`, `f_msg_type_name`, `f_type`, `f_language`, `f_status`) VALUES (12, 1598697904067, 1598697904067, 1, '负债爆仓通知', '706030', '您的负债率不足，交易账户已爆仓', 0, 2, 30, 'TRADING_EXPLOSION_NOTICE', 1, 'zh-cn', 1);
INSERT INTO `t_msg_template` (`f_id`, `f_create_time`, `f_update_time`, `f_version`, `f_name`, `f_subject`, `f_content`, `f_is_html`, `f_channel`, `f_msg_type_code`, `f_msg_type_name`, `f_type`, `f_language`, `f_status`) VALUES (13, 1598697904067, 1598697904067, 1, '登录验证码', '登录验证码', '登录验证码：{authCode}，您正在尝试登录，请于10分钟内填写，如非本人操作，请忽略本消息', 0, 1, 1, 'LOGIN', 1, 'zh-cn', 1);
INSERT INTO `t_msg_template` (`f_id`, `f_create_time`, `f_update_time`, `f_version`, `f_name`, `f_subject`, `f_content`, `f_is_html`, `f_channel`, `f_msg_type_code`, `f_msg_type_name`, `f_type`, `f_language`, `f_status`) VALUES (14, 1598697904067, 1598697904067, 1, '关闭GA安全项验证码', '关闭GA安全项验证码', '安全项设置验证码：{authCode}，您正在尝试关闭GA验证，请于10分钟内填写，如非本人操作，请忽略本消息。', 0, 1, 20, 'VERIFY_SETTING_POLICY_DISABLE_GA_POLICY', 1, 'zh-cn', 1);
INSERT INTO `t_msg_template` (`f_id`, `f_create_time`, `f_update_time`, `f_version`, `f_name`, `f_subject`, `f_content`, `f_is_html`, `f_channel`, `f_msg_type_code`, `f_msg_type_name`, `f_type`, `f_language`, `f_status`) VALUES (15, 1598697904067, 1598697904067, 1, '提币验证码', '提币验证码', '提货验证码：{authCode}，您正在尝试提货，请于10分钟内填写，如非本人操作，请忽略本消息。', 0, 1, 21, 'VERIFY_SETTING_POLICY_WITHDRAW', 1, 'zh-cn', 1);
INSERT INTO `t_msg_template` (`f_id`, `f_create_time`, `f_update_time`, `f_version`, `f_name`, `f_subject`, `f_content`, `f_is_html`, `f_channel`, `f_msg_type_code`, `f_msg_type_name`, `f_type`, `f_language`, `f_status`) VALUES (16, 1598697904067, 1598697904067, 1, '登录验证码', '711172', '登录验证码：{1}，您正在尝试登录，请于10分钟内填写，如非本人操作，请忽略本短信。', 0, 2, 1, 'LOGIN', 1, 'zh-cn', 1);
INSERT INTO `t_msg_template` (`f_id`, `f_create_time`, `f_update_time`, `f_version`, `f_name`, `f_subject`, `f_content`, `f_is_html`, `f_channel`, `f_msg_type_code`, `f_msg_type_name`, `f_type`, `f_language`, `f_status`) VALUES (17, 1598697904067, 1598697904067, 1, '关闭GA安全项验证码', '711174', '安全项设置验证码：{1}，您正在尝试关闭GA验证，请于10分钟内填写，如非本人操作，请忽略本短信。', 0, 2, 20, 'VERIFY_SETTING_POLICY_DISABLE_GA_POLICY', 1, 'zh-cn', 1);
INSERT INTO `t_msg_template` (`f_id`, `f_create_time`, `f_update_time`, `f_version`, `f_name`, `f_subject`, `f_content`, `f_is_html`, `f_channel`, `f_msg_type_code`, `f_msg_type_name`, `f_type`, `f_language`, `f_status`) VALUES (18, 1598697904067, 1598697904067, 1, '提币验证码', '711211', '提货验证码：{1}，您正在尝试提货，请于10分钟内填写，如非本人操作，请忽略本短信。', 0, 2, 21, 'VERIFY_SETTING_POLICY_WITHDRAW', 1, 'zh-cn', 1);
INSERT INTO `t_msg_template` (`f_id`, `f_create_time`, `f_update_time`, `f_version`, `f_name`, `f_subject`, `f_content`, `f_is_html`, `f_channel`, `f_msg_type_code`, `f_msg_type_name`, `f_type`, `f_language`, `f_status`) VALUES (19, 1598697904067, 1598697904067, 1, 'Login Auth code', 'Login Auth code', 'Login auth code:{authCode}', 0, 1, 1, 'LOGIN', 1, 'en-us', 1);
INSERT INTO `t_msg_template` (`f_id`, `f_create_time`, `f_update_time`, `f_version`, `f_name`, `f_subject`, `f_content`, `f_is_html`, `f_channel`, `f_msg_type_code`, `f_msg_type_name`, `f_type`, `f_language`, `f_status`) VALUES (20, 1598697904067, 1598697904067, 1, 'Close GA Auth Code', 'Close GA Auth Code', 'Glose GA auth code:{authCode}', 0, 1, 20, 'VERIFY_SETTING_POLICY_DISABLE_GA_POLICY', 1, 'en-us', 1);
INSERT INTO `t_msg_template` (`f_id`, `f_create_time`, `f_update_time`, `f_version`, `f_name`, `f_subject`, `f_content`, `f_is_html`, `f_channel`, `f_msg_type_code`, `f_msg_type_name`, `f_type`, `f_language`, `f_status`) VALUES (21, 1598697904067, 1598697904067, 1, 'Withdraw Auth Code', 'Withdraw Auth Code', 'Withdraw auth code:{authCode}', 0, 1, 21, 'VERIFY_SETTING_POLICY_WITHDRAW', 1, 'en-us', 1);
INSERT INTO `t_msg_template` (`f_id`, `f_create_time`, `f_update_time`, `f_version`, `f_name`, `f_subject`, `f_content`, `f_is_html`, `f_channel`, `f_msg_type_code`, `f_msg_type_name`, `f_type`, `f_language`, `f_status`) VALUES (22, 1598697904067, 1598697904067, 1, '注册验证码', '注册验证码', '注册验证码：{authCode}，您正在尝试注册，请于10分钟内填写，如非本人操作，请忽略本消息', 0, 1, 3, 'REGISTER', 1, 'zh-cn', 1);
INSERT INTO `t_msg_template` (`f_id`, `f_create_time`, `f_update_time`, `f_version`, `f_name`, `f_subject`, `f_content`, `f_is_html`, `f_channel`, `f_msg_type_code`, `f_msg_type_name`, `f_type`, `f_language`, `f_status`) VALUES (23, 1598697904067, 1598697904067, 1, '注册验证码', '712272', '注册验证码：{1}，您正在尝试注册，请于10分钟内填写，如非本人操作，请忽略本短信。', 0, 2, 3, 'REGISTER', 1, 'zh-cn', 1);
INSERT INTO `t_msg_template` (`f_id`, `f_create_time`, `f_update_time`, `f_version`, `f_name`, `f_subject`, `f_content`, `f_is_html`, `f_channel`, `f_msg_type_code`, `f_msg_type_name`, `f_type`, `f_language`, `f_status`) VALUES (24, 1598697904067, 1598697904067, 1, 'Register Auth Code', 'Register Auth Code', 'Register auth code:{authCode}', 0, 1, 3, 'REGISTER', 1, 'en-us', 1);
INSERT INTO `t_msg_template` (`f_id`, `f_create_time`, `f_update_time`, `f_version`, `f_name`, `f_subject`, `f_content`, `f_is_html`, `f_channel`, `f_msg_type_code`, `f_msg_type_name`, `f_type`, `f_language`, `f_status`) VALUES (25, 1598697904067, 1598697904067, 1, 'Login Auth Code', '712354', 'Login authorization code:{1}. You are trying to login. please fill in within 10 mins', 0, 2, 1, 'LOGIN', 1, 'en-us', 1);
INSERT INTO `t_msg_template` (`f_id`, `f_create_time`, `f_update_time`, `f_version`, `f_name`, `f_subject`, `f_content`, `f_is_html`, `f_channel`, `f_msg_type_code`, `f_msg_type_name`, `f_type`, `f_language`, `f_status`) VALUES (26, 1598697904067, 1598697904067, 1, 'Registration Auth Code', '712353', 'Registration authorization code:{1}. You are trying to register. please fill in within 10 mins', 0, 2, 3, 'REGISTER', 1, 'en-us', 1);
INSERT INTO `t_msg_template` (`f_id`, `f_create_time`, `f_update_time`, `f_version`, `f_name`, `f_subject`, `f_content`, `f_is_html`, `f_channel`, `f_msg_type_code`, `f_msg_type_name`, `f_type`, `f_language`, `f_status`) VALUES (27, 1598697904067, 1598697904067, 1, 'Security Setting-Close GA', '712355', 'Security setting authorization code:{1}. You are trying to close GA. please fill in within 10 mins', 0, 2, 20, 'VERIFY_SETTING_POLICY_DISABLE_GA_POLICY', 1, 'en-us', 1);
INSERT INTO `t_msg_template` (`f_id`, `f_create_time`, `f_update_time`, `f_version`, `f_name`, `f_subject`, `f_content`, `f_is_html`, `f_channel`, `f_msg_type_code`, `f_msg_type_name`, `f_type`, `f_language`, `f_status`) VALUES (28, 1598697904067, 1598697904067, 1, 'Pickup Auth Code', '712357', 'Pickup authorization code:{1}. You are trying to Pickup. please fill in within 10 mins', 0, 2, 21, 'VERIFY_SETTING_POLICY_WITHDRAW', 1, 'en-us', 1);
COMMIT;

-- ----------------------------
-- Table structure for t_order
-- ----------------------------
DROP TABLE IF EXISTS `t_order`;
CREATE TABLE `t_order` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key ID',
  `f_order_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '订单ID',
  `f_user_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'User ID',
  `f_symbol` varchar(50) NOT NULL DEFAULT '' COMMENT '交易对',
  `f_pay_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '支付数量',
  `f_get_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '获取数量',
  `f_price` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '金额',
  `f_fee` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT 'fee',
  `f_direct` varchar(50) NOT NULL DEFAULT '' COMMENT 'buy, sell',
  `f_status` int(11) NOT NULL DEFAULT '1' COMMENT '状态，0-失败，1-成功',
  `f_created_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'created timestamp',
  `f_type` int(11) NOT NULL DEFAULT '0' COMMENT '类型，0-正常订单，1-止盈单 2- 止损单',
  PRIMARY KEY (`f_id`),
  KEY `t_order_time_ind` (`f_created_at`),
  KEY `t_order_time_symbol` (`f_symbol`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Order table';

-- ----------------------------
-- Records of t_order
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_order_setting
-- ----------------------------
DROP TABLE IF EXISTS `t_order_setting`;
CREATE TABLE `t_order_setting` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key ID',
  `f_key` varchar(50) NOT NULL DEFAULT '' COMMENT 'key',
  `f_type` varchar(50) NOT NULL DEFAULT '' COMMENT 'type',
  `f_value` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT 'value',
  `f_created_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'created timestamp',
  `f_updated_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'updated timestamp',
  PRIMARY KEY (`f_id`),
  UNIQUE KEY `u_trade_key_type` (`f_key`,`f_type`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8 COMMENT='Order Rules table';

-- ----------------------------
-- Records of t_order_setting
-- ----------------------------
BEGIN;
INSERT INTO `t_order_setting` (`f_id`, `f_key`, `f_type`, `f_value`, `f_created_at`, `f_updated_at`) VALUES (1, 'min-vol-order', 'kBTC', 0.00010000, 0, 0);
INSERT INTO `t_order_setting` (`f_id`, `f_key`, `f_type`, `f_value`, `f_created_at`, `f_updated_at`) VALUES (2, 'min-vol-order', 'kETH', 0.00010000, 0, 0);
INSERT INTO `t_order_setting` (`f_id`, `f_key`, `f_type`, `f_value`, `f_created_at`, `f_updated_at`) VALUES (3, 'min-vol-order', 'kLTC', 0.00100000, 0, 0);
INSERT INTO `t_order_setting` (`f_id`, `f_key`, `f_type`, `f_value`, `f_created_at`, `f_updated_at`) VALUES (4, 'min-vol-order', 'kUSD', 0.01000000, 0, 0);
INSERT INTO `t_order_setting` (`f_id`, `f_key`, `f_type`, `f_value`, `f_created_at`, `f_updated_at`) VALUES (5, 'min-vol-order', 'kEOS', 0.00010000, 0, 0);
INSERT INTO `t_order_setting` (`f_id`, `f_key`, `f_type`, `f_value`, `f_created_at`, `f_updated_at`) VALUES (6, 'min-vol-order', 'kBCH', 0.00010000, 0, 0);
INSERT INTO `t_order_setting` (`f_id`, `f_key`, `f_type`, `f_value`, `f_created_at`, `f_updated_at`) VALUES (7, 'max-platform-position', 'kBTC', 100.00000000, 0, 0);
INSERT INTO `t_order_setting` (`f_id`, `f_key`, `f_type`, `f_value`, `f_created_at`, `f_updated_at`) VALUES (8, 'max-platform-position', 'kETH', 1000.00000000, 0, 0);
INSERT INTO `t_order_setting` (`f_id`, `f_key`, `f_type`, `f_value`, `f_created_at`, `f_updated_at`) VALUES (9, 'max-platform-position', 'kLTC', 10000.00000000, 0, 0);
INSERT INTO `t_order_setting` (`f_id`, `f_key`, `f_type`, `f_value`, `f_created_at`, `f_updated_at`) VALUES (10, 'max-platform-position', 'kUSD', 2000000.00000000, 0, 0);
INSERT INTO `t_order_setting` (`f_id`, `f_key`, `f_type`, `f_value`, `f_created_at`, `f_updated_at`) VALUES (11, 'max-platform-position', 'kEOS', 3000.00000000, 0, 0);
INSERT INTO `t_order_setting` (`f_id`, `f_key`, `f_type`, `f_value`, `f_created_at`, `f_updated_at`) VALUES (12, 'max-platform-position', 'kBCH', 20000.00000000, 0, 0);
INSERT INTO `t_order_setting` (`f_id`, `f_key`, `f_type`, `f_value`, `f_created_at`, `f_updated_at`) VALUES (13, 'max-user-position', 'kBTC', 100.00000000, 0, 0);
INSERT INTO `t_order_setting` (`f_id`, `f_key`, `f_type`, `f_value`, `f_created_at`, `f_updated_at`) VALUES (14, 'max-user-position', 'kETH', 1000.00000000, 0, 0);
INSERT INTO `t_order_setting` (`f_id`, `f_key`, `f_type`, `f_value`, `f_created_at`, `f_updated_at`) VALUES (15, 'max-user-position', 'kLTC', 10000.00000000, 0, 0);
INSERT INTO `t_order_setting` (`f_id`, `f_key`, `f_type`, `f_value`, `f_created_at`, `f_updated_at`) VALUES (16, 'max-user-position', 'kUSD', 2000000.00000000, 0, 0);
INSERT INTO `t_order_setting` (`f_id`, `f_key`, `f_type`, `f_value`, `f_created_at`, `f_updated_at`) VALUES (17, 'max-user-position', 'kEOS', 3000.00000000, 0, 0);
INSERT INTO `t_order_setting` (`f_id`, `f_key`, `f_type`, `f_value`, `f_created_at`, `f_updated_at`) VALUES (18, 'max-user-position', 'kBCH', 20000.00000000, 0, 0);
INSERT INTO `t_order_setting` (`f_id`, `f_key`, `f_type`, `f_value`, `f_created_at`, `f_updated_at`) VALUES (19, 'place-order-long', 'kBTC', 1.00000000, 0, 0);
INSERT INTO `t_order_setting` (`f_id`, `f_key`, `f_type`, `f_value`, `f_created_at`, `f_updated_at`) VALUES (20, 'place-order-long', 'kETH', 1.00000000, 0, 0);
INSERT INTO `t_order_setting` (`f_id`, `f_key`, `f_type`, `f_value`, `f_created_at`, `f_updated_at`) VALUES (21, 'place-order-long', 'kLTC', 1.00000000, 0, 0);
INSERT INTO `t_order_setting` (`f_id`, `f_key`, `f_type`, `f_value`, `f_created_at`, `f_updated_at`) VALUES (22, 'place-order-long', 'kUSD', 1.00000000, 0, 0);
INSERT INTO `t_order_setting` (`f_id`, `f_key`, `f_type`, `f_value`, `f_created_at`, `f_updated_at`) VALUES (23, 'place-order-long', 'kEOS', 1.00000000, 0, 0);
INSERT INTO `t_order_setting` (`f_id`, `f_key`, `f_type`, `f_value`, `f_created_at`, `f_updated_at`) VALUES (24, 'place-order-long', 'kBCH', 1.00000000, 0, 0);
INSERT INTO `t_order_setting` (`f_id`, `f_key`, `f_type`, `f_value`, `f_created_at`, `f_updated_at`) VALUES (25, 'place-order-put', 'kBTC', 1.00000000, 0, 0);
INSERT INTO `t_order_setting` (`f_id`, `f_key`, `f_type`, `f_value`, `f_created_at`, `f_updated_at`) VALUES (26, 'place-order-put', 'kETH', 1.00000000, 0, 0);
INSERT INTO `t_order_setting` (`f_id`, `f_key`, `f_type`, `f_value`, `f_created_at`, `f_updated_at`) VALUES (27, 'place-order-put', 'kLTC', 1.00000000, 0, 0);
INSERT INTO `t_order_setting` (`f_id`, `f_key`, `f_type`, `f_value`, `f_created_at`, `f_updated_at`) VALUES (28, 'place-order-put', 'kUSD', 1.00000000, 0, 0);
INSERT INTO `t_order_setting` (`f_id`, `f_key`, `f_type`, `f_value`, `f_created_at`, `f_updated_at`) VALUES (29, 'place-order-put', 'kEOS', 1.00000000, 0, 0);
INSERT INTO `t_order_setting` (`f_id`, `f_key`, `f_type`, `f_value`, `f_created_at`, `f_updated_at`) VALUES (30, 'place-order-put', 'kBCH', 1.00000000, 0, 0);
COMMIT;

-- ----------------------------
-- Table structure for t_order_stop_limit
-- ----------------------------
DROP TABLE IF EXISTS `t_order_stop_limit`;
CREATE TABLE `t_order_stop_limit` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key ID',
  `f_order_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '订单ID',
  `f_ori_order_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '原始订单 id',
  `f_user_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'User ID',
  `f_type` int(11) NOT NULL DEFAULT '0' COMMENT '1- 止盈， 2 止损',
  `f_base_currency` varchar(20) NOT NULL DEFAULT '' COMMENT '基础currency',
  `f_quote_currency` varchar(20) NOT NULL DEFAULT '' COMMENT '询价currency',
  `f_trigger_price` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '触发金额',
  `f_fee` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '手续费',
  `f_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '总数',
  `f_status` int(11) NOT NULL DEFAULT '2' COMMENT '状态，0-失败，1-成功, 2- 未触发，3- 已经撤销',
  `f_direct` varchar(20) NOT NULL DEFAULT '' COMMENT 'buy, sell',
  `f_created_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'created timestamp',
  `f_updated_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'updated timestamp',
  `f_triggered_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '触发 timestamp',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='止盈止损';

-- ----------------------------
-- Records of t_order_stop_limit
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_symbol
-- ----------------------------
DROP TABLE IF EXISTS `t_symbol`;
CREATE TABLE `t_symbol` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key ID',
  `f_base_currency` varchar(20) NOT NULL DEFAULT '' COMMENT 'base currency',
  `f_quote_currency` varchar(20) NOT NULL DEFAULT '' COMMENT 'quote currency',
  `f_status` int(11) NOT NULL DEFAULT '0' COMMENT '1 上线， 0 已经下线',
  `f_exchange` int(11) NOT NULL DEFAULT '0' COMMENT '1 可以交易， 0 停止交易',
  `f_created_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'created timestamp',
  `f_updated_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'update timestamp',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='交易对';

-- ----------------------------
-- Records of t_symbol
-- ----------------------------
BEGIN;
INSERT INTO `t_symbol` (`f_id`, `f_base_currency`, `f_quote_currency`, `f_status`, `f_exchange`, `f_created_at`, `f_updated_at`) VALUES (1, 'kBTC', 'kUSD', 1, 1, 0, 0);
INSERT INTO `t_symbol` (`f_id`, `f_base_currency`, `f_quote_currency`, `f_status`, `f_exchange`, `f_created_at`, `f_updated_at`) VALUES (2, 'kETH', 'kUSD', 1, 1, 0, 0);
INSERT INTO `t_symbol` (`f_id`, `f_base_currency`, `f_quote_currency`, `f_status`, `f_exchange`, `f_created_at`, `f_updated_at`) VALUES (3, 'kBCH', 'kUSD', 1, 1, 0, 0);
INSERT INTO `t_symbol` (`f_id`, `f_base_currency`, `f_quote_currency`, `f_status`, `f_exchange`, `f_created_at`, `f_updated_at`) VALUES (4, 'kLTC', 'kUSD', 1, 1, 0, 0);
INSERT INTO `t_symbol` (`f_id`, `f_base_currency`, `f_quote_currency`, `f_status`, `f_exchange`, `f_created_at`, `f_updated_at`) VALUES (5, 'kEOS', 'kUSD', 1, 1, 0, 0);
COMMIT;

-- ----------------------------
-- Table structure for t_transaction
-- ----------------------------
DROP TABLE IF EXISTS `t_transaction`;
CREATE TABLE `t_transaction` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key',
  `f_create_time` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'create time',
  `f_account_type` int(11) NOT NULL DEFAULT '0',
  `f_user_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'userId',
  `f_batch_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'bath operation id',
  `f_ref_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'reference  id',
  `f_currency` varchar(12) NOT NULL DEFAULT '',
  `f_change_amount` decimal(36,18) NOT NULL DEFAULT '0.000000000000000000',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='transaction_minter';

-- ----------------------------
-- Records of t_transaction
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_uc_country
-- ----------------------------
DROP TABLE IF EXISTS `t_uc_country`;
CREATE TABLE `t_uc_country` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `f_area_code` varchar(50) NOT NULL DEFAULT '0' COMMENT '地区代码',
  `f_name_cn` varchar(100) NOT NULL DEFAULT '' COMMENT '地区中文名称',
  `f_name_en` varchar(100) NOT NULL DEFAULT '' COMMENT '地区英文名称',
  `f_created_at` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建时间',
  `f_updated_at` bigint(20) NOT NULL DEFAULT '0' COMMENT '操作时间',
  PRIMARY KEY (`f_id`),
  KEY `idx_area_code` (`f_area_code`)
) ENGINE=InnoDB AUTO_INCREMENT=198 DEFAULT CHARSET=utf8 COMMENT='国家列表';

-- ----------------------------
-- Records of t_uc_country
-- ----------------------------
BEGIN;
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (1, '00355', '阿尔巴尼亚', 'Albania', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (2, '00213', '阿尔及利亚', 'Algeria', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (3, '00376', '安道尔共和国', 'Andorra', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (4, '00244', '安哥拉', 'Angola', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (5, '001264', '安圭拉岛', 'Anguilla', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (6, '001268', '安提瓜和巴布达', 'Antigua and Barbuda', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (7, '0054', '阿根廷', 'Argentina', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (8, '00374', '亚美尼亚', 'Armenia', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (9, '00297', '阿鲁巴', 'Aruba', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (10, '00247', '阿森松', 'Ascension', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (11, '0061', '澳大利亚', 'Australia', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (12, '00672', '澳大利亚海外领地', 'Australian overseas territories', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (13, '0043', '奥地利', 'Austria', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (14, '00994', '阿塞拜疆', 'Azerbaijan', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (15, '001242', '巴哈马', 'Bahamas', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (16, '00973', '巴林', 'Bahrain', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (17, '001246', '巴巴多斯', 'Barbados', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (18, '0032', '比利时', 'Belgium', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (19, '00501', '伯利兹', 'Belize', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (20, '00229', '贝宁', 'Benin', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (21, '001441', '百慕大群岛', 'Bermuda', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (22, '00975', '不丹', 'Bhutan', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (23, '00387', '波斯尼亚和黑塞哥维那', 'Bosnia and Herzegovina', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (24, '00267', '博茨瓦纳', 'Botswana', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (25, '0055', '巴西', 'Brazil', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (26, '001284', '英属维尔京群岛', 'British Virgin Islands', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (27, '00673', '文莱', 'Brunei', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (28, '00359', '保加利亚', 'Bulgaria', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (29, '00226', '布基纳法索', 'Burkina Faso', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (30, '00257', '布隆迪', 'Burundi', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (31, '00855', '柬埔寨', 'Cambodia', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (32, '001', '加拿大', 'Canada', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (33, '00238', '佛得角', 'Cape Verde', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (34, '001345', '开曼群岛', 'Cayman Islands.', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (35, '0056', '智利', 'Chile', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (36, '0086', '中国', 'China', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (37, '0057', '哥伦比亚', 'Colombia', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (38, '00269', '科摩罗群岛', 'Comoros Islands', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (39, '00682', '库克群岛', 'Cook Islands.', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (40, '00506', '哥斯达黎加', 'Costa Rica', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (41, '00385', '克罗地亚', 'Croatia', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (42, '00357', '塞浦路斯', 'Cyprus', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (43, '00420', '捷克', 'Czech Republic', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (44, '0045', '丹麦', 'Denmark', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (45, '00246', '迭戈加西亚群岛', 'Diego Garcia', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (46, '00253', '吉布提', 'Djibouti', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (47, '001809', '多米尼加共和国', 'Dominican Republic', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (48, '0020', '埃及', 'Egypt', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (49, '00503', '萨尔瓦多', 'El Salvador', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (50, '009714', '迪拜酋长国', 'Emirate of Dubai', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (51, '00372', '爱沙尼亚', 'Estonia', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (52, '00251', '埃塞俄比亚', 'Ethiopia', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (53, '00500', '福克兰群岛', 'Falkland Islands', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (54, '00298', '法罗群岛', 'Faroe Islands', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (55, '00679', '斐济', 'Fiji', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (56, '00358', '芬兰', 'Finland', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (57, '0033', '法国', 'France', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (58, '00594', '法属圭亚那', 'French Guiana', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (59, '00689', '法属玻利尼西亚', 'French Polynesia', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (60, '00241', '加蓬', 'Gabon', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (61, '00220', '冈比亚', 'Gambia', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (62, '00995', '格鲁吉亚', 'Georgia', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (63, '0049', '德国', 'Germany', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (64, '00233', '加纳', 'Ghana', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (65, '00350', '直布罗陀', 'Gibraltar', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (66, '0030', '希腊', 'Greece', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (67, '00299', '格陵兰岛', 'Greenland', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (68, '001473', '格林纳达', 'Grenada', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (69, '00590', '瓜德罗普', 'Guadeloupe', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (70, '001671', '关岛', 'Guam', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (71, '00502', '危地马拉', 'Guatemala', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (72, '00245', '几内亚比绍', 'Guinea-Bissau', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (73, '00592', '圭亚那', 'Guyana', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (74, '00509', '海地', 'Haiti', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (75, '00504', '洪都拉斯', 'Honduras', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (76, '00852', '中国香港', 'Hong Kong (China)', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (77, '0036', '匈牙利', 'Hungary', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (78, '00354', '冰岛', 'Iceland', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (79, '0091', '印度', 'India', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (80, '0062', '印度尼西亚', 'Indonesia', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (81, '00353', '爱尔兰', 'Ireland', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (82, '00972', '以色列', 'Israel', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (83, '0039', '意大利', 'Italy', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (84, '001876', '牙买加', 'Jamaica', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (85, '00962', '约旦', 'Jordan', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (86, '007', '哈萨克斯坦', 'Kazakhstan', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (87, '00254', '肯尼亚', 'Kenya', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (88, '00686', '基里巴斯', 'Kiribati', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (89, '0082', '韩国', 'Korea', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (90, '00965', '科威特', 'Kuwait', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (91, '00996', '吉尔吉斯坦', 'Kyrgyzstan', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (92, '00856', '老挝', 'Laos', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (93, '00371', '拉脱维亚', 'Latvia', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (94, '00266', '莱索托', 'Lesotho', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (95, '00423', '列支敦士登', 'Liechtenstein', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (96, '00370', '立陶宛', 'Lithuania', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (97, '00352', '卢森堡', 'Luxembourg', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (98, '00389', '马其顿', 'Macedonia', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (99, '00261', '马达加斯加', 'Madagascar', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (100, '00265', '马拉维', 'Malawi', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (101, '0060', '马来西亚', 'Malaysia', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (102, '00960', '马尔代夫', 'Maldives', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (103, '00223', '马里', 'Mali', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (104, '00356', '马耳他', 'Malta', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (105, '00223', '马里亚那群岛', 'Mariana Islands', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (106, '00692', '马绍尔群岛', 'Marshall Islands', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (107, '00596', '马提尼克', 'Martinique', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (108, '00222', '毛里塔尼亚', 'Mauritania', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (109, '00230', '毛里求斯', 'Mauritius', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (110, '0052', '墨西哥', 'Mexico', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (111, '00691', '密克罗尼西亚', 'Micronesia', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (112, '00373', '摩尔多瓦', 'Moldova', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (113, '00377', '摩纳哥', 'Monaco', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (114, '00976', '蒙古', 'Mongolia', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (115, '00382', '黑山', 'Montenegro', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (116, '001664', '蒙特塞拉特岛', 'Montserrat', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (117, '00212', '摩洛哥', 'Morocco', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (118, '00258', '莫桑比克', 'Mozambique', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (119, '00264', '纳米比亚', 'Namibia', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (120, '00674', '瑙鲁', 'Nauru', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (121, '00977', '尼泊尔', 'Nepal', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (122, '00599', '荷属安的列斯', 'Netheriands Antilles', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (123, '0031', '荷兰', 'Netherlands', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (124, '00687', '新喀里多尼亚', 'New Caledonia', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (125, '0064', '新西兰', 'New Zealand', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (126, '00505', '尼加拉瓜', 'Nicaragua', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (127, '00227', '尼日尔', 'Niger', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (128, '00234', '尼日利亚', 'Nigeria', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (129, '00683', '纽埃岛', 'Niue', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (130, '0047', '挪威', 'Norway', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (131, '00968', '阿曼', 'Oman', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (132, '00680', '帕劳', 'Palau', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (133, '00970', '巴勒斯坦', 'Palestine', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (134, '00507', '巴拿马', 'Panama', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (135, '00675', '巴布亚新几内亚', 'Papua New Guinea', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (136, '00595', '巴拉圭', 'Paraguay', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (137, '0051', '秘鲁', 'Peru', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (138, '0063', '菲律宾', 'Philippines', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (139, '0048', '波兰', 'Poland', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (140, '00351', '葡萄牙', 'Portugal', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (141, '001', '波多黎各', 'Puerto Rico', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (142, '00974', '卡塔尔', 'Qatar', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (143, '00262', '留尼旺', 'Reunion', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (144, '0040', '罗马尼亚', 'Romania', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (145, '007', '俄罗斯', 'Russia', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (146, '001758', '圣卢西亚', 'Saint Lucia', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (147, '00508', '圣皮埃尔和密克隆群岛', 'Saint Pierre and Miquelon', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (148, '001784', '圣文森特岛', 'Saint Vincent', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (149, '00684', '东萨摩亚(美)', 'Samoa Eastern', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (150, '00685', '西萨摩亚', 'Samoa Western', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (151, '00378', '圣马力诺', 'San Marino', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (152, '00239', '圣多美和普林西比', 'Sao Tome and Principe', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (153, '00966', '沙特阿拉伯', 'Saudi Arabia', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (154, '00221', '塞内加尔', 'Senegal', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (155, '00381', '塞尔维亚', 'Serbia', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (156, '00248', '塞舌尔', 'Seychelles', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (157, '00232', '塞拉利昂', 'Sierra Leone', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (158, '0065', '新加坡', 'Singapore', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (159, '00421', '斯洛伐克', 'Slovakia', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (160, '00386', '斯洛文尼亚', 'Slovenia', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (161, '00677', '所罗门群岛', 'Solomon Islands', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (162, '0027', '南非', 'South Africa', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (163, '0034', '西班牙', 'Spain', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (164, '0094', '斯里兰卡', 'Sri Lanka', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (165, '00290', '圣赫勒拿岛', 'St.Helena', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (166, '001758', '圣卢西亚', 'St.Lucia', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (167, '001784', '圣文森特', 'St.Vincent', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (168, '00597', '苏里南', 'Suriname', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (169, '00268', '斯威士兰', 'Swaziland', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (170, '0046', '瑞典', 'Sweden', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (171, '0041', '瑞士', 'Switzerland', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (172, '00886', '中国台湾', 'Taiwan (China)', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (173, '00992', '塔吉克斯坦', 'Tajikistan', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (174, '00255', '坦桑尼亚', 'Tanzania', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (175, '0066', '泰国', 'Thailand', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (176, '00228', '多哥', 'Togo', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (177, '00690', '托克劳群岛', 'Tokelau', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (178, '00676', '汤加', 'Tonga', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (179, '001868', '特立尼达和多巴哥', 'Trinidad and Tobago', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (180, '00216', '突尼斯', 'Tunisia', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (181, '0090', '土耳其', 'Turkey', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (182, '00993', '土库曼斯坦', 'Turkmenistan', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (183, '00688', '图瓦卢', 'Tuvalu', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (184, '00256', '乌干达', 'Uganda', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (185, '00380', '乌克兰', 'Ukraine', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (186, '00971', '阿拉伯联合酋长国', 'United Arab Emirates', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (187, '0044', '英国', 'United Kingdom', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (188, '00598', '乌拉圭', 'Uruguay', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (189, '00998', '乌兹别克斯坦', 'Uzbekistan', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (190, '00678', '瓦努阿图', 'Vanuatu', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (191, '00379', '梵蒂冈城', 'Vatican City', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (192, '0058', '委内瑞拉', 'Venezuela', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (193, '0084', '越南', 'Vietnam', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (194, '00681', '瓦利斯和富图纳', 'Wallis and Futuna', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (195, '00338', '南斯拉夫', 'Yugoslavia', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (196, '00243', '扎伊尔', 'Zaire', 0, 0);
INSERT INTO `t_uc_country` (`f_id`, `f_area_code`, `f_name_cn`, `f_name_en`, `f_created_at`, `f_updated_at`) VALUES (197, '00260', '赞比亚', 'Zambia', 0, 0);
COMMIT;

-- ----------------------------
-- Table structure for t_uc_country_language
-- ----------------------------
DROP TABLE IF EXISTS `t_uc_country_language`;
CREATE TABLE `t_uc_country_language` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `f_area_code` varchar(50) NOT NULL COMMENT '地区码',
  `f_language` varchar(100) NOT NULL COMMENT '语言，如zh-cn',
  `f_created_at` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建时间',
  `f_updated_at` bigint(20) NOT NULL DEFAULT '0' COMMENT '操作时间',
  PRIMARY KEY (`f_id`),
  UNIQUE KEY `uniq_idx_area_code` (`f_area_code`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='国家或地区语言映射表';

-- ----------------------------
-- Records of t_uc_country_language
-- ----------------------------
BEGIN;
INSERT INTO `t_uc_country_language` (`f_id`, `f_area_code`, `f_language`, `f_created_at`, `f_updated_at`) VALUES (1, '0086', 'zh-cn', 0, 0);
COMMIT;

-- ----------------------------
-- Table structure for t_uc_file
-- ----------------------------
DROP TABLE IF EXISTS `t_uc_file`;
CREATE TABLE `t_uc_file` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `f_uid` bigint(20) unsigned NOT NULL COMMENT '用户的id',
  `f_uuid` varchar(200) NOT NULL COMMENT '文件的uuid',
  `f_filename` varchar(200) NOT NULL DEFAULT '' COMMENT '文件名',
  `f_created_at` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建时间',
  PRIMARY KEY (`f_id`),
  KEY `idx_file_uid` (`f_uid`),
  KEY `idx_file_uuid` (`f_uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='公共文件表';

-- ----------------------------
-- Records of t_uc_file
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_uc_kyc_detail
-- ----------------------------
DROP TABLE IF EXISTS `t_uc_kyc_detail`;
CREATE TABLE `t_uc_kyc_detail` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `f_uid` bigint(20) unsigned NOT NULL COMMENT '所属的用户id',
  `f_country_code` varchar(50) NOT NULL COMMENT '国家代码',
  `f_first_name` varchar(200) NOT NULL DEFAULT '' COMMENT '用户的名',
  `f_last_name` varchar(200) NOT NULL DEFAULT '' COMMENT '用户的姓',
  `f_identity` varchar(200) NOT NULL DEFAULT '' COMMENT '用户的证件-身份证，护照，或其他合法证件号',
  `f_identity_file_uuid` varchar(200) NOT NULL COMMENT '用户证件文件的uuid',
  `f_created_at` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建时间',
  `f_updated_at` bigint(20) NOT NULL DEFAULT '0' COMMENT '操作时间',
  `f_status` smallint(6) NOT NULL COMMENT 'kyc状态 1待审核，2审核通过 3审核拒绝',
  `f_comment` varchar(200) NOT NULL COMMENT '备注',
  `f_operator_id` smallint(6) NOT NULL COMMENT '审核人编号',
  `f_operator_name` varchar(200) NOT NULL COMMENT '审核人姓名',
  PRIMARY KEY (`f_id`),
  KEY `idx_kyc_identity` (`f_identity`),
  KEY `idx_kyc_uid` (`f_uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='kyc信息存储表';

-- ----------------------------
-- Records of t_uc_kyc_detail
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_uc_user
-- ----------------------------
DROP TABLE IF EXISTS `t_uc_user`;
CREATE TABLE `t_uc_user` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Primary Key',
  `f_password` varchar(200) NOT NULL COMMENT 'user password',
  `f_state` smallint(6) NOT NULL COMMENT '用户状态， 0-无效状态，1-正常，2-冻结，3-注销',
  `f_type` smallint(6) NOT NULL COMMENT '用户类型， 0-无效类型，1-普通用户',
  `f_country_code` varchar(20) DEFAULT NULL COMMENT '国家代码(手机用)',
  `f_nationality` varchar(20) NOT NULL COMMENT '国籍(必选)',
  `f_phone` varchar(50) NOT NULL DEFAULT '' COMMENT '手机号, 全世界不会一样',
  `f_email` varchar(50) NOT NULL DEFAULT '' COMMENT '邮箱',
  `f_ga_secret` varchar(200) NOT NULL DEFAULT '' COMMENT 'ga 密钥',
  `f_salt` varchar(50) NOT NULL DEFAULT '' COMMENT '盐',
  `f_updated_at` bigint(20) NOT NULL DEFAULT '0' COMMENT 'update time',
  `f_created_at` bigint(20) NOT NULL DEFAULT '0' COMMENT 'create time',
  PRIMARY KEY (`f_id`),
  KEY `idx_user_phone` (`f_phone`),
  KEY `idx_user_email` (`f_email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='user table';

-- ----------------------------
-- Records of t_uc_user
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_user_account
-- ----------------------------
DROP TABLE IF EXISTS `t_user_account`;
CREATE TABLE `t_user_account` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key',
  `f_create_time` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'create time',
  `f_update_time` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'update time',
  `f_version` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'version',
  `f_account_type` int(11) NOT NULL DEFAULT '0',
  `f_user_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'userId',
  `f_currency` varchar(12) NOT NULL DEFAULT '',
  `f_balance` decimal(36,18) NOT NULL DEFAULT '0.000000000000000000',
  `f_freeze` decimal(36,18) NOT NULL DEFAULT '0.000000000000000000',
  PRIMARY KEY (`f_id`),
  UNIQUE KEY `uniq_t_user_account_1` (`f_account_type`,`f_user_id`,`f_currency`)
) ENGINE=InnoDB AUTO_INCREMENT=1042 DEFAULT CHARSET=utf8 COMMENT='t_user_account';

-- ----------------------------
-- Records of t_user_account
-- ----------------------------
BEGIN;
INSERT INTO `t_user_account` (`f_id`, `f_create_time`, `f_update_time`, `f_version`, `f_account_type`, `f_user_id`, `f_currency`, `f_balance`, `f_freeze`) VALUES (1000, 0, 0, 0, 0, 1000, 'ETH', 0.000000000000000000, 0.000000000000000000);
INSERT INTO `t_user_account` (`f_id`, `f_create_time`, `f_update_time`, `f_version`, `f_account_type`, `f_user_id`, `f_currency`, `f_balance`, `f_freeze`) VALUES (1001, 0, 0, 0, 0, 1000, 'USDT', 0.000000000000000000, 0.000000000000000000);
INSERT INTO `t_user_account` (`f_id`, `f_create_time`, `f_update_time`, `f_version`, `f_account_type`, `f_user_id`, `f_currency`, `f_balance`, `f_freeze`) VALUES (1002, 0, 0, 0, 0, 1000, 'KINE', 0.000000000000000000, 0.000000000000000000);
INSERT INTO `t_user_account` (`f_id`, `f_create_time`, `f_update_time`, `f_version`, `f_account_type`, `f_user_id`, `f_currency`, `f_balance`, `f_freeze`) VALUES (1003, 0, 0, 0, 0, 1000, 'kUSD', 0.000000000000000000, 0.000000000000000000);
INSERT INTO `t_user_account` (`f_id`, `f_create_time`, `f_update_time`, `f_version`, `f_account_type`, `f_user_id`, `f_currency`, `f_balance`, `f_freeze`) VALUES (1004, 0, 0, 0, 1, 1000, 'KINE', 0.000000000000000000, 0.000000000000000000);
INSERT INTO `t_user_account` (`f_id`, `f_create_time`, `f_update_time`, `f_version`, `f_account_type`, `f_user_id`, `f_currency`, `f_balance`, `f_freeze`) VALUES (1005, 0, 0, 0, 1, 1000, 'MCD', 0.000000000000000000, 0.000000000000000000);
INSERT INTO `t_user_account` (`f_id`, `f_create_time`, `f_update_time`, `f_version`, `f_account_type`, `f_user_id`, `f_currency`, `f_balance`, `f_freeze`) VALUES (1010, 0, 0, 0, 0, 1001, 'ETH', 0.000000000000000000, 0.000000000000000000);
INSERT INTO `t_user_account` (`f_id`, `f_create_time`, `f_update_time`, `f_version`, `f_account_type`, `f_user_id`, `f_currency`, `f_balance`, `f_freeze`) VALUES (1011, 0, 0, 0, 0, 1001, 'USDT', 0.000000000000000000, 0.000000000000000000);
INSERT INTO `t_user_account` (`f_id`, `f_create_time`, `f_update_time`, `f_version`, `f_account_type`, `f_user_id`, `f_currency`, `f_balance`, `f_freeze`) VALUES (1012, 0, 0, 0, 0, 1001, 'KINE', 0.000000000000000000, 0.000000000000000000);
INSERT INTO `t_user_account` (`f_id`, `f_create_time`, `f_update_time`, `f_version`, `f_account_type`, `f_user_id`, `f_currency`, `f_balance`, `f_freeze`) VALUES (1013, 0, 0, 0, 0, 1001, 'kUSD', 0.000000000000000000, 0.000000000000000000);
INSERT INTO `t_user_account` (`f_id`, `f_create_time`, `f_update_time`, `f_version`, `f_account_type`, `f_user_id`, `f_currency`, `f_balance`, `f_freeze`) VALUES (1014, 0, 0, 0, 1, 1001, 'KINE', 0.000000000000000000, 0.000000000000000000);
INSERT INTO `t_user_account` (`f_id`, `f_create_time`, `f_update_time`, `f_version`, `f_account_type`, `f_user_id`, `f_currency`, `f_balance`, `f_freeze`) VALUES (1015, 0, 0, 0, 1, 1001, 'MCD', 0.000000000000000000, 0.000000000000000000);
INSERT INTO `t_user_account` (`f_id`, `f_create_time`, `f_update_time`, `f_version`, `f_account_type`, `f_user_id`, `f_currency`, `f_balance`, `f_freeze`) VALUES (1020, 0, 0, 0, 0, 1002, 'ETH', 0.000000000000000000, 0.000000000000000000);
INSERT INTO `t_user_account` (`f_id`, `f_create_time`, `f_update_time`, `f_version`, `f_account_type`, `f_user_id`, `f_currency`, `f_balance`, `f_freeze`) VALUES (1021, 0, 0, 0, 0, 1002, 'USDT', 0.000000000000000000, 0.000000000000000000);
INSERT INTO `t_user_account` (`f_id`, `f_create_time`, `f_update_time`, `f_version`, `f_account_type`, `f_user_id`, `f_currency`, `f_balance`, `f_freeze`) VALUES (1022, 0, 0, 0, 0, 1002, 'KINE', 0.000000000000000000, 0.000000000000000000);
INSERT INTO `t_user_account` (`f_id`, `f_create_time`, `f_update_time`, `f_version`, `f_account_type`, `f_user_id`, `f_currency`, `f_balance`, `f_freeze`) VALUES (1023, 0, 0, 0, 0, 1002, 'kUSD', 0.000000000000000000, 0.000000000000000000);
INSERT INTO `t_user_account` (`f_id`, `f_create_time`, `f_update_time`, `f_version`, `f_account_type`, `f_user_id`, `f_currency`, `f_balance`, `f_freeze`) VALUES (1024, 0, 0, 0, 1, 1002, 'KINE', 0.000000000000000000, 0.000000000000000000);
INSERT INTO `t_user_account` (`f_id`, `f_create_time`, `f_update_time`, `f_version`, `f_account_type`, `f_user_id`, `f_currency`, `f_balance`, `f_freeze`) VALUES (1025, 0, 0, 0, 1, 1002, 'MCD', 0.000000000000000000, 0.000000000000000000);
INSERT INTO `t_user_account` (`f_id`, `f_create_time`, `f_update_time`, `f_version`, `f_account_type`, `f_user_id`, `f_currency`, `f_balance`, `f_freeze`) VALUES (1030, 0, 0, 0, 0, 1003, 'ETH', 0.000000000000000000, 0.000000000000000000);
INSERT INTO `t_user_account` (`f_id`, `f_create_time`, `f_update_time`, `f_version`, `f_account_type`, `f_user_id`, `f_currency`, `f_balance`, `f_freeze`) VALUES (1031, 0, 0, 0, 0, 1003, 'USDT', 0.000000000000000000, 0.000000000000000000);
INSERT INTO `t_user_account` (`f_id`, `f_create_time`, `f_update_time`, `f_version`, `f_account_type`, `f_user_id`, `f_currency`, `f_balance`, `f_freeze`) VALUES (1032, 0, 0, 0, 0, 1003, 'KINE', 0.000000000000000000, 0.000000000000000000);
INSERT INTO `t_user_account` (`f_id`, `f_create_time`, `f_update_time`, `f_version`, `f_account_type`, `f_user_id`, `f_currency`, `f_balance`, `f_freeze`) VALUES (1033, 0, 0, 0, 0, 1003, 'kUSD', 0.000000000000000000, 0.000000000000000000);
INSERT INTO `t_user_account` (`f_id`, `f_create_time`, `f_update_time`, `f_version`, `f_account_type`, `f_user_id`, `f_currency`, `f_balance`, `f_freeze`) VALUES (1034, 0, 0, 0, 1, 1003, 'KINE', 0.000000000000000000, 0.000000000000000000);
INSERT INTO `t_user_account` (`f_id`, `f_create_time`, `f_update_time`, `f_version`, `f_account_type`, `f_user_id`, `f_currency`, `f_balance`, `f_freeze`) VALUES (1035, 0, 0, 0, 1, 1003, 'MCD', 0.000000000000000000, 0.000000000000000000);
INSERT INTO `t_user_account` (`f_id`, `f_create_time`, `f_update_time`, `f_version`, `f_account_type`, `f_user_id`, `f_currency`, `f_balance`, `f_freeze`) VALUES (1036, 0, 0, 0, 0, 1004, 'ETH', 0.000000000000000000, 0.000000000000000000);
INSERT INTO `t_user_account` (`f_id`, `f_create_time`, `f_update_time`, `f_version`, `f_account_type`, `f_user_id`, `f_currency`, `f_balance`, `f_freeze`) VALUES (1037, 0, 0, 0, 0, 1004, 'USDT', 0.000000000000000000, 0.000000000000000000);
INSERT INTO `t_user_account` (`f_id`, `f_create_time`, `f_update_time`, `f_version`, `f_account_type`, `f_user_id`, `f_currency`, `f_balance`, `f_freeze`) VALUES (1038, 0, 0, 0, 0, 1004, 'KINE', 0.000000000000000000, 0.000000000000000000);
INSERT INTO `t_user_account` (`f_id`, `f_create_time`, `f_update_time`, `f_version`, `f_account_type`, `f_user_id`, `f_currency`, `f_balance`, `f_freeze`) VALUES (1039, 0, 0, 0, 0, 1004, 'kUSD', 0.000000000000000000, 0.000000000000000000);
INSERT INTO `t_user_account` (`f_id`, `f_create_time`, `f_update_time`, `f_version`, `f_account_type`, `f_user_id`, `f_currency`, `f_balance`, `f_freeze`) VALUES (1040, 0, 0, 0, 1, 1004, 'KINE', 0.000000000000000000, 0.000000000000000000);
INSERT INTO `t_user_account` (`f_id`, `f_create_time`, `f_update_time`, `f_version`, `f_account_type`, `f_user_id`, `f_currency`, `f_balance`, `f_freeze`) VALUES (1041, 0, 0, 0, 1, 1004, 'MCD', 0.000000000000000000, 0.000000000000000000);
COMMIT;

-- ----------------------------
-- Table structure for t_user_account_action_history
-- ----------------------------
DROP TABLE IF EXISTS `t_user_account_action_history`;
CREATE TABLE `t_user_account_action_history` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key',
  `f_create_time` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'create time',
  `f_update_time` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'update time',
  `f_version` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'version',
  `f_action` int(11) NOT NULL DEFAULT '0',
  `f_account_type` int(11) NOT NULL DEFAULT '0',
  `f_user_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'userId',
  `f_currency` varchar(12) NOT NULL DEFAULT '',
  `f_chain` varchar(12) NOT NULL DEFAULT '',
  `f_from_address` varchar(80) NOT NULL DEFAULT '',
  `f_to_address` varchar(80) NOT NULL DEFAULT '',
  `f_tx_hash` varchar(80) NOT NULL DEFAULT '',
  `f_block_number` bigint(20) unsigned NOT NULL DEFAULT '0',
  `f_block_hash` varchar(80) NOT NULL DEFAULT '',
  `f_status` int(11) NOT NULL DEFAULT '0',
  `f_amount` decimal(36,18) NOT NULL DEFAULT '0.000000000000000000',
  `f_fee` decimal(36,18) NOT NULL DEFAULT '0.000000000000000000',
  `f_channel` int(11) NOT NULL DEFAULT '0',
  `f_correlation` varchar(64) NOT NULL DEFAULT '',
  `f_comments` varchar(64) DEFAULT '',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='t_user_account_action_history';

-- ----------------------------
-- Records of t_user_account_action_history
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_user_account_snapshot
-- ----------------------------
DROP TABLE IF EXISTS `t_user_account_snapshot`;
CREATE TABLE `t_user_account_snapshot` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key',
  `f_create_time` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'create time',
  `f_account_type` int(11) NOT NULL DEFAULT '0',
  `f_user_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'userId',
  `f_currency` varchar(12) NOT NULL DEFAULT '',
  `f_balance` decimal(36,18) NOT NULL DEFAULT '0.000000000000000000',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='t_user_account_snapshot';

-- ----------------------------
-- Records of t_user_account_snapshot
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_user_account_value_history
-- ----------------------------
DROP TABLE IF EXISTS `t_user_account_value_history`;
CREATE TABLE `t_user_account_value_history` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `f_create_time` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '时间',
  `f_user_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '用户',
  `f_asset_total` decimal(36,18) NOT NULL DEFAULT '0.000000000000000000' COMMENT '总资产',
  `f_pnl` decimal(36,18) NOT NULL DEFAULT '0.000000000000000000' COMMENT '盈亏',
  `f_pnl_total` decimal(36,18) NOT NULL DEFAULT '0.000000000000000000' COMMENT '累计盈亏',
  `f_share` decimal(36,18) NOT NULL DEFAULT '0.000000000000000000' COMMENT '分红',
  `f_share_total` decimal(36,18) NOT NULL DEFAULT '0.000000000000000000' COMMENT '累计分红',
  `f_cursor_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '源表游标',
  PRIMARY KEY (`f_id`),
  UNIQUE KEY `u_user_id_create_time` (`f_user_id`,`f_create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户权益历史';

-- ----------------------------
-- Records of t_user_account_value_history
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_user_address
-- ----------------------------
DROP TABLE IF EXISTS `t_user_address`;
CREATE TABLE `t_user_address` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key',
  `f_create_time` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'create time',
  `f_update_time` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'update time',
  `f_version` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'version',
  `f_currency` varchar(12) NOT NULL DEFAULT '',
  `f_chain` varchar(12) NOT NULL DEFAULT '',
  `f_user_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'userId',
  `f_address` varchar(80) NOT NULL DEFAULT '',
  `f_comments` varchar(64) DEFAULT '',
  PRIMARY KEY (`f_id`),
  UNIQUE KEY `uniq_t_user_address_1` (`f_currency`,`f_chain`,`f_user_id`,`f_address`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='t_user_address';

-- ----------------------------
-- Records of t_user_address
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_user_position
-- ----------------------------
DROP TABLE IF EXISTS `t_user_position`;
CREATE TABLE `t_user_position` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key ID',
  `f_user_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'User ID',
  `f_version` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'version',
  `f_symbol` varchar(50) NOT NULL DEFAULT '' COMMENT '交易对',
  `f_position` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '当前仓位',
  `f_avg_price` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '当前仓位VVWP',
  `f_category` int(11) NOT NULL DEFAULT '0' COMMENT '交易资产的种类',
  `f_created_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'created timestamp',
  `f_updated_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'updated timestamp',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户仓位';

-- ----------------------------
-- Records of t_user_position
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_wallet_location
-- ----------------------------
DROP TABLE IF EXISTS `t_wallet_location`;
CREATE TABLE `t_wallet_location` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key ID',
  `f_address` varchar(80) NOT NULL DEFAULT '' COMMENT '钱包地址',
  `f_location` varchar(128) NOT NULL DEFAULT '' COMMENT '钱包文件名称',
  `f_password` varchar(256) NOT NULL DEFAULT '' COMMENT '加密密码',
  `f_created_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'created timestamp',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='钱包地址与钱包文件名称的管理';

-- ----------------------------
-- Records of t_wallet_location
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_withdraw_rule
-- ----------------------------
DROP TABLE IF EXISTS `t_withdraw_rule`;
CREATE TABLE `t_withdraw_rule` (
  `f_currency` varchar(12) NOT NULL COMMENT '币种',
  `f_min_amount` decimal(36,18) NOT NULL DEFAULT '0.000000000000000000' COMMENT '最小',
  `f_max_amount` decimal(36,18) NOT NULL DEFAULT '0.000000000000000000' COMMENT '最大',
  `f_intraday_amount` decimal(36,18) NOT NULL DEFAULT '0.000000000000000000' COMMENT '日最多',
  `f_fee_amount` decimal(36,18) NOT NULL DEFAULT '0.000000000000000000' COMMENT '手续费',
  `f_fee_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT 'fee类型',
  `f_decimals` tinyint(4) NOT NULL DEFAULT '0' COMMENT '精度',
  `f_enable` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否启用：0停用，1启用',
  PRIMARY KEY (`f_currency`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='提币规则';

-- ----------------------------
-- Records of t_withdraw_rule
-- ----------------------------
BEGIN;
INSERT INTO `t_withdraw_rule` (`f_currency`, `f_min_amount`, `f_max_amount`, `f_intraday_amount`, `f_fee_amount`, `f_fee_type`, `f_decimals`, `f_enable`) VALUES ('KINE', 5.000000000000000000, 100000.000000000000000000, 500000.000000000000000000, 1.000000000000000000, 0, 4, 1);
INSERT INTO `t_withdraw_rule` (`f_currency`, `f_min_amount`, `f_max_amount`, `f_intraday_amount`, `f_fee_amount`, `f_fee_type`, `f_decimals`, `f_enable`) VALUES ('USDT', 5.000000000000000000, 100000.000000000000000000, 500000.000000000000000000, 2.000000000000000000, 0, 4, 1);
COMMIT;

-- ----------------------------
-- Table structure for uc_currency
-- ----------------------------
DROP TABLE IF EXISTS `uc_currency`;
CREATE TABLE `uc_currency` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key ID',
  `name` varchar(20) NOT NULL DEFAULT '' COMMENT '名称',
  `code` varchar(12) NOT NULL DEFAULT '0' COMMENT '编码',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '1 上线， 0 已经下线',
  `exchange` int(11) NOT NULL DEFAULT '0' COMMENT '1 可以交易， 0 停止交易',
  `created_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '创建时间',
  `updated_at` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '更新时间',
  PRIMARY KEY (`id`,`code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='币种信息';

-- ----------------------------
-- Records of uc_currency
-- ----------------------------
BEGIN;
INSERT INTO `uc_currency` (`id`, `name`, `code`, `status`, `exchange`, `created_at`, `updated_at`) VALUES (1, 'USDC', 'USDC', 1, 1, 0, 0);
COMMIT;

-- ----------------------------
-- Table structure for uc_file
-- ----------------------------
DROP TABLE IF EXISTS `uc_file`;
CREATE TABLE `uc_file` (
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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='文件管理表';

-- ----------------------------
-- Records of uc_file
-- ----------------------------
BEGIN;
INSERT INTO `uc_file` (`id`, `object_name`, `bucket_name`, `file_name`, `type`, `file_size`, `created_by`, `updated_by`, `created_at`, `updated_at`, `delete_flag`) VALUES (3, '01d3810669b4446c8486a6a6d89df39a.jpeg', 'seeds', '头像.jpeg', 'jpeg', 116846, NULL, NULL, 1659678817589, 1659678817589, 1);
COMMIT;

-- ----------------------------
-- Table structure for uc_security_strategy
-- ----------------------------
DROP TABLE IF EXISTS `uc_security_strategy`;
CREATE TABLE `uc_security_strategy` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `uid` bigint(20) unsigned NOT NULL COMMENT '用户的id',
  `need_auth` tinyint(4) NOT NULL COMMENT '是否开启了验证',
  `auth_type` smallint(6) NOT NULL COMMENT '验证的类型ClientAuthTypeEnum 1-metamask, 2-email, 3-ga',
  `created_at` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建时间',
  `updated_at` bigint(20) NOT NULL DEFAULT '0' COMMENT '操作时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_security_strategy_uid` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8 COMMENT='安全策略表';

-- ----------------------------
-- Records of uc_security_strategy
-- ----------------------------
BEGIN;
INSERT INTO `uc_security_strategy` (`id`, `uid`, `need_auth`, `auth_type`, `created_at`, `updated_at`) VALUES (1, 1, 1, 2, 1659070519644, 1659070519644);
INSERT INTO `uc_security_strategy` (`id`, `uid`, `need_auth`, `auth_type`, `created_at`, `updated_at`) VALUES (3, 7, 1, 2, 1659401777889, 1659401777889);
INSERT INTO `uc_security_strategy` (`id`, `uid`, `need_auth`, `auth_type`, `created_at`, `updated_at`) VALUES (4, 9, 1, 2, 1659446910653, 1659446910653);
INSERT INTO `uc_security_strategy` (`id`, `uid`, `need_auth`, `auth_type`, `created_at`, `updated_at`) VALUES (5, 10, 1, 2, 1659447362818, 1659447362818);
INSERT INTO `uc_security_strategy` (`id`, `uid`, `need_auth`, `auth_type`, `created_at`, `updated_at`) VALUES (6, 1, 0, 3, 1659588233987, 1659588233987);
INSERT INTO `uc_security_strategy` (`id`, `uid`, `need_auth`, `auth_type`, `created_at`, `updated_at`) VALUES (10, 17, 1, 1, 1660179793544, 1660179793544);
INSERT INTO `uc_security_strategy` (`id`, `uid`, `need_auth`, `auth_type`, `created_at`, `updated_at`) VALUES (11, 18, 1, 2, 1660203562739, 1660203562739);
INSERT INTO `uc_security_strategy` (`id`, `uid`, `need_auth`, `auth_type`, `created_at`, `updated_at`) VALUES (12, 19, 1, 1, 1661221797481, 1661221797481);
INSERT INTO `uc_security_strategy` (`id`, `uid`, `need_auth`, `auth_type`, `created_at`, `updated_at`) VALUES (13, 20, 1, 1, 1661239822034, 1661239822034);
INSERT INTO `uc_security_strategy` (`id`, `uid`, `need_auth`, `auth_type`, `created_at`, `updated_at`) VALUES (14, 20, 1, 2, 1661241560764, 1661241560765);
INSERT INTO `uc_security_strategy` (`id`, `uid`, `need_auth`, `auth_type`, `created_at`, `updated_at`) VALUES (15, 21, 1, 1, 1661322146717, 1661322146717);
INSERT INTO `uc_security_strategy` (`id`, `uid`, `need_auth`, `auth_type`, `created_at`, `updated_at`) VALUES (16, 23, 1, 2, 1661494857351, 1661494857351);
COMMIT;

-- ----------------------------
-- Table structure for uc_user
-- ----------------------------
DROP TABLE IF EXISTS `uc_user`;
CREATE TABLE `uc_user` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Primary Key',
  `nickname` varchar(50) NOT NULL COMMENT '昵称',
  `password` varchar(200) DEFAULT NULL COMMENT '密码',
  `state` smallint(6) DEFAULT NULL COMMENT '用户状态， 0-无效状态，1-正常，2-冻结，3-注销',
  `public_address` varchar(255) DEFAULT NULL COMMENT '钱包地址，metamask',
  `nonce` varchar(255) DEFAULT NULL COMMENT '随机数，metamask登陆时使用',
  `type` smallint(6) DEFAULT NULL COMMENT '用户类型， 0-无效类型，1-普通用户',
  `country_code` varchar(20) DEFAULT NULL COMMENT '国家代码(手机用)',
  `nationality` varchar(20) DEFAULT NULL COMMENT '国籍(必选)',
  `phone` varchar(50) DEFAULT '' COMMENT '手机号, 全世界不会一样',
  `email` varchar(50) DEFAULT '' COMMENT '邮箱',
  `ga_secret` varchar(200) DEFAULT '' COMMENT 'ga 密钥',
  `salt` varchar(50) DEFAULT '' COMMENT '盐',
  `updated_at` bigint(20) DEFAULT '0' COMMENT 'update time',
  `created_at` bigint(20) NOT NULL DEFAULT '0' COMMENT 'create time',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_user_phone` (`phone`),
  KEY `idx_user_email` (`email`),
  KEY `idx_user_public_address` (`public_address`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8 COMMENT='用户表';

-- ----------------------------
-- Records of uc_user
-- ----------------------------
BEGIN;
INSERT INTO `uc_user` (`id`, `nickname`, `password`, `state`, `public_address`, `nonce`, `type`, `country_code`, `nationality`, `phone`, `email`, `ga_secret`, `salt`, `updated_at`, `created_at`) VALUES (1, '819628513@qq.com', 'c4b3f6b3a2c793e7a1b81821edd891127aac938d49d7c65372678f6dd79c8150', 1, NULL, NULL, 1, NULL, NULL, '', 'yangkang.duke@gmail.com', 'DHMUVFF72JFFPJ2L', 'fde1759916fe84cab5e52134', 1659594216508, 1659068404982);
INSERT INTO `uc_user` (`id`, `nickname`, `password`, `state`, `public_address`, `nonce`, `type`, `country_code`, `nationality`, `phone`, `email`, `ga_secret`, `salt`, `updated_at`, `created_at`) VALUES (7, '232598146@qq.com', '5185e1ca8358538d0331a7c076b640a18a42f26381f64faccb39580bc9dce874', 1, NULL, NULL, 1, NULL, NULL, '', '232598146@qq.com', '', '6d601634f90630d5686419a9', 1659401777889, 1659401777889);
INSERT INTO `uc_user` (`id`, `nickname`, `password`, `state`, `public_address`, `nonce`, `type`, `country_code`, `nationality`, `phone`, `email`, `ga_secret`, `salt`, `updated_at`, `created_at`) VALUES (8, '222', NULL, 1, '222', '87c4798f13f6866220834288', 1, NULL, NULL, '', '', '', '', 1659424388815, 1659424388815);
INSERT INTO `uc_user` (`id`, `nickname`, `password`, `state`, `public_address`, `nonce`, `type`, `country_code`, `nationality`, `phone`, `email`, `ga_secret`, `salt`, `updated_at`, `created_at`) VALUES (9, '179816463@qq.com', '7beed8417575c32a6d8235d83b375a8b96d946a5859e98bc4fbab80e713909c3', 1, NULL, NULL, 1, NULL, NULL, '', '179816463@qq.com', '', '84c7aa06d73d960e47d27bc2', 1659446910653, 1659446910653);
INSERT INTO `uc_user` (`id`, `nickname`, `password`, `state`, `public_address`, `nonce`, `type`, `country_code`, `nationality`, `phone`, `email`, `ga_secret`, `salt`, `updated_at`, `created_at`) VALUES (10, '13247711709@163.com', 'f8c72e2b440887e10fc7e5c0b525354da96b5cb4f0f58949518d2b65205b2879', 1, NULL, NULL, 1, NULL, NULL, '', '13247711709@163.com', '', 'd1b9d5968466ecf664aaf2f9', 1659447362818, 1659447362818);
INSERT INTO `uc_user` (`id`, `nickname`, `password`, `state`, `public_address`, `nonce`, `type`, `country_code`, `nationality`, `phone`, `email`, `ga_secret`, `salt`, `updated_at`, `created_at`) VALUES (11, '0xeBE98F7eab8eb6fdd36b5158aa45b1D566c23d91', NULL, 1, '0xeBE98F7eab8eb6fdd36b5158aa45b1D566c23d91', '6f41f83cbbdefa5143e965dd', 1, NULL, NULL, '', '', '', '', 1659488633434, 1659488633434);
INSERT INTO `uc_user` (`id`, `nickname`, `password`, `state`, `public_address`, `nonce`, `type`, `country_code`, `nationality`, `phone`, `email`, `ga_secret`, `salt`, `updated_at`, `created_at`) VALUES (17, '0x40141cf4756a72df8d8f81c1e0c2ad403c127b9e', NULL, 1, '0x40141cf4756a72df8d8f81c1e0c2ad403c127b9e', 'a64e8520bef0b6417a6c35e7', 1, NULL, NULL, '', '', '', '', 1660179986091, 1660179793544);
INSERT INTO `uc_user` (`id`, `nickname`, `password`, `state`, `public_address`, `nonce`, `type`, `country_code`, `nationality`, `phone`, `email`, `ga_secret`, `salt`, `updated_at`, `created_at`) VALUES (18, '952517823@qq.com', 'fe798f08ff0d5d207cc284d8f253b5e140fdf8bbf1f61e0e10b2ad373bf8f13c', 1, NULL, NULL, 1, NULL, NULL, '', '952517823@qq.com', '', 'a1ef753fd63807a87dd6ce18', 1660203562739, 1660203562739);
INSERT INTO `uc_user` (`id`, `nickname`, `password`, `state`, `public_address`, `nonce`, `type`, `country_code`, `nationality`, `phone`, `email`, `ga_secret`, `salt`, `updated_at`, `created_at`) VALUES (19, '0x804e8183f337fc0a60e7d94bc4009b744fed253c', NULL, 1, '0x804e8183f337fc0a60e7d94bc4009b744fed253c', 'ccdc68021e219064521c9713', 1, NULL, NULL, '', '', '', '', 1661221797481, 1661221797481);
INSERT INTO `uc_user` (`id`, `nickname`, `password`, `state`, `public_address`, `nonce`, `type`, `country_code`, `nationality`, `phone`, `email`, `ga_secret`, `salt`, `updated_at`, `created_at`) VALUES (20, '0x84f9f225bd824c4fea9d33095674f82611ee3325', 'cc644680c120ed67e95d1f64a119961b4b73a860a7ab0ce13a2dd2141347eabb', 1, '0x84f9f225bd824c4fea9d33095674f82611ee3325', 'e77cefa15893516ecf6981b1', 1, NULL, NULL, '', 'lake1355@gmail.com', '', 'cdcbdc50c90ce01a70098a9a', 1661481929893, 1661239822034);
INSERT INTO `uc_user` (`id`, `nickname`, `password`, `state`, `public_address`, `nonce`, `type`, `country_code`, `nationality`, `phone`, `email`, `ga_secret`, `salt`, `updated_at`, `created_at`) VALUES (21, '0x85bdaffe9e48f643ed1f0f920dcf6fc019204013', NULL, 1, '0x85bdaffe9e48f643ed1f0f920dcf6fc019204013', '3f0d3ed0bf1ff98fb4e4f384', 1, NULL, NULL, '', '', '', '', 1661572431646, 1661322146717);
INSERT INTO `uc_user` (`id`, `nickname`, `password`, `state`, `public_address`, `nonce`, `type`, `country_code`, `nationality`, `phone`, `email`, `ga_secret`, `salt`, `updated_at`, `created_at`) VALUES (22, '0x8ce37a461fddcf0efcde0e51fd455b992c8d06dd', NULL, 1, '0x40141cf4756a72df8d8f81c1e0c2cc05c1fc4f8d0313e6df7090', NULL, 1, NULL, NULL, '', 'yangyang@163.com', '', '0c23e95fd137ea96c4ef24366b7e6f1f', 0, 0);
INSERT INTO `uc_user` (`id`, `nickname`, `password`, `state`, `public_address`, `nonce`, `type`, `country_code`, `nationality`, `phone`, `email`, `ga_secret`, `salt`, `updated_at`, `created_at`) VALUES (23, 'yangyang@163.com', '5773cf5f0fdf844821ec160c49f99d9a8110ce812c7eca8c40ed86d42249850b', 1, NULL, NULL, 1, NULL, NULL, '', '769151627@qq.com', '', 'fb476d3930ce29d7db4b6686', 1661494857351, 1661494857351);
COMMIT;

-- ----------------------------
-- Table structure for uc_user_account
-- ----------------------------
DROP TABLE IF EXISTS `uc_user_account`;
CREATE TABLE `uc_user_account` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key',
  `create_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建时间',
  `update_time` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '修改时间',
  `version` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '版本控制',
  `account_type` int(11) NOT NULL DEFAULT '0' COMMENT '账号类型 1 现货 ',
  `user_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '用户id',
  `currency` varchar(12) NOT NULL DEFAULT '' COMMENT '币种',
  `balance` decimal(36,18) NOT NULL DEFAULT '0.000000000000000000' COMMENT '账户余额',
  `freeze` decimal(36,18) NOT NULL DEFAULT '0.000000000000000000' COMMENT '冻结金额',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uniq_t_user_account_1` (`account_type`,`user_id`,`currency`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='用户账户表';

-- ----------------------------
-- Records of uc_user_account
-- ----------------------------
BEGIN;
INSERT INTO `uc_user_account` (`id`, `create_time`, `update_time`, `version`, `account_type`, `user_id`, `currency`, `balance`, `freeze`) VALUES (1, 1661241441236, 0, 0, 1, 20, 'usdc', 39.600000000000000000, 0.000000000000000000);
INSERT INTO `uc_user_account` (`id`, `create_time`, `update_time`, `version`, `account_type`, `user_id`, `currency`, `balance`, `freeze`) VALUES (2, 1661241441236, 1661241441236, 0, 1, 21, 'usdc', 90.000000000000000000, 0.000000000000000000);
INSERT INTO `uc_user_account` (`id`, `create_time`, `update_time`, `version`, `account_type`, `user_id`, `currency`, `balance`, `freeze`) VALUES (3, 1661241441236, 0, 0, 1, 23, 'usdc', 999.000000000000000000, 0.000000000000000000);
COMMIT;

-- ----------------------------
-- Table structure for uc_user_account_action_history
-- ----------------------------
DROP TABLE IF EXISTS `uc_user_account_action_history`;
CREATE TABLE `uc_user_account_action_history` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key',
  `create_time` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '创建时间',
  `update_time` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '修改时间',
  `version` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '版本控制',
  `action` int(11) NOT NULL DEFAULT '0' COMMENT '操作 1冲币 2提币 3 购买NFT',
  `account_type` int(11) NOT NULL DEFAULT '0' COMMENT '账号类型 1 现货',
  `user_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '用户id',
  `currency` varchar(12) NOT NULL DEFAULT '' COMMENT '币种',
  `chain` varchar(12) NOT NULL DEFAULT '' COMMENT '链',
  `from_address` varchar(80) NOT NULL DEFAULT '' COMMENT '发送端地址',
  `to_address` varchar(80) NOT NULL DEFAULT '' COMMENT '接收端地址',
  `tx_hash` varchar(80) NOT NULL DEFAULT '' COMMENT '哈希值',
  `block_number` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '区块号',
  `block_hash` varchar(80) NOT NULL DEFAULT '' COMMENT '区块的hash值',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '状态 1-进行中 2-成功 3-失败',
  `amount` decimal(36,18) NOT NULL DEFAULT '0.000000000000000000' COMMENT '金额',
  `fee` decimal(36,18) NOT NULL DEFAULT '0.000000000000000000' COMMENT '手续费',
  `channel` int(11) NOT NULL DEFAULT '0' COMMENT '频道',
  `correlation` varchar(64) NOT NULL DEFAULT '' COMMENT 'correlation',
  `comments` varchar(64) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=68 DEFAULT CHARSET=utf8 COMMENT='用户账号交易历史表';

-- ----------------------------
-- Records of uc_user_account_action_history
-- ----------------------------
BEGIN;
INSERT INTO `uc_user_account_action_history` (`id`, `create_time`, `update_time`, `version`, `action`, `account_type`, `user_id`, `currency`, `chain`, `from_address`, `to_address`, `tx_hash`, `block_number`, `block_hash`, `status`, `amount`, `fee`, `channel`, `correlation`, `comments`) VALUES (6, 1661242170901, 0, 0, 0, 1, 20, 'usdc', '', '0x40141cf4756a72df8d8f81c1e0c25fa739cb98243d9d9302d928', '0x40141cf4756a72df8d8f81c1e0c25fa739cb98243d9d9302d928', '', 0, '', 0, 1000.000000000000000000, 0.100000000000000000, 0, '', '冲币');
INSERT INTO `uc_user_account_action_history` (`id`, `create_time`, `update_time`, `version`, `action`, `account_type`, `user_id`, `currency`, `chain`, `from_address`, `to_address`, `tx_hash`, `block_number`, `block_hash`, `status`, `amount`, `fee`, `channel`, `correlation`, `comments`) VALUES (58, 1661438046692, 0, 0, 3, 1, 21, 'usdc', '', '', '0x85bdaffe9e48f643ed1f0f920dcf6fc019204013', '', 0, '', 2, 10.000000000000000000, 0.000000000000000000, 0, '', '');
INSERT INTO `uc_user_account_action_history` (`id`, `create_time`, `update_time`, `version`, `action`, `account_type`, `user_id`, `currency`, `chain`, `from_address`, `to_address`, `tx_hash`, `block_number`, `block_hash`, `status`, `amount`, `fee`, `channel`, `correlation`, `comments`) VALUES (59, 1661479506167, 0, 0, 3, 1, 20, 'usdc', '', '', '0x84f9f225bd824c4fea9d33095674f82611ee3325', '', 0, '', 2, 111.000000000000000000, 0.000000000000000000, 0, '', '');
INSERT INTO `uc_user_account_action_history` (`id`, `create_time`, `update_time`, `version`, `action`, `account_type`, `user_id`, `currency`, `chain`, `from_address`, `to_address`, `tx_hash`, `block_number`, `block_hash`, `status`, `amount`, `fee`, `channel`, `correlation`, `comments`) VALUES (60, 1661479728657, 0, 0, 3, 1, 20, 'usdc', '', '', '0x84f9f225bd824c4fea9d33095674f82611ee3325', '', 0, '', 2, 111.000000000000000000, 0.000000000000000000, 0, '', '');
INSERT INTO `uc_user_account_action_history` (`id`, `create_time`, `update_time`, `version`, `action`, `account_type`, `user_id`, `currency`, `chain`, `from_address`, `to_address`, `tx_hash`, `block_number`, `block_hash`, `status`, `amount`, `fee`, `channel`, `correlation`, `comments`) VALUES (61, 1661483735200, 0, 0, 3, 1, 20, 'usdc', '', '', '0x84f9f225bd824c4fea9d33095674f82611ee3325', '', 0, '', 2, 123.000000000000000000, 0.000000000000000000, 0, '', '');
INSERT INTO `uc_user_account_action_history` (`id`, `create_time`, `update_time`, `version`, `action`, `account_type`, `user_id`, `currency`, `chain`, `from_address`, `to_address`, `tx_hash`, `block_number`, `block_hash`, `status`, `amount`, `fee`, `channel`, `correlation`, `comments`) VALUES (62, 1661487392152, 0, 0, 3, 1, 20, 'usdc', '', '', '0x84f9f225bd824c4fea9d33095674f82611ee3325', '', 0, '', 2, 10.200000000000000000, 0.000000000000000000, 0, '', '');
INSERT INTO `uc_user_account_action_history` (`id`, `create_time`, `update_time`, `version`, `action`, `account_type`, `user_id`, `currency`, `chain`, `from_address`, `to_address`, `tx_hash`, `block_number`, `block_hash`, `status`, `amount`, `fee`, `channel`, `correlation`, `comments`) VALUES (63, 1661491514607, 0, 0, 3, 1, 20, 'usdc', '', '', '0x84f9f225bd824c4fea9d33095674f82611ee3325', '', 0, '', 2, 233.000000000000000000, 0.000000000000000000, 0, '', '');
INSERT INTO `uc_user_account_action_history` (`id`, `create_time`, `update_time`, `version`, `action`, `account_type`, `user_id`, `currency`, `chain`, `from_address`, `to_address`, `tx_hash`, `block_number`, `block_hash`, `status`, `amount`, `fee`, `channel`, `correlation`, `comments`) VALUES (64, 1661491569515, 0, 0, 3, 1, 20, 'usdc', '', '', '0x84f9f225bd824c4fea9d33095674f82611ee3325', '', 0, '', 2, 2.100000000000000000, 0.000000000000000000, 0, '', '');
INSERT INTO `uc_user_account_action_history` (`id`, `create_time`, `update_time`, `version`, `action`, `account_type`, `user_id`, `currency`, `chain`, `from_address`, `to_address`, `tx_hash`, `block_number`, `block_hash`, `status`, `amount`, `fee`, `channel`, `correlation`, `comments`) VALUES (65, 1661491599539, 0, 0, 3, 1, 20, 'usdc', '', '', '0x84f9f225bd824c4fea9d33095674f82611ee3325', '', 0, '', 2, 123.000000000000000000, 0.000000000000000000, 0, '', '');
INSERT INTO `uc_user_account_action_history` (`id`, `create_time`, `update_time`, `version`, `action`, `account_type`, `user_id`, `currency`, `chain`, `from_address`, `to_address`, `tx_hash`, `block_number`, `block_hash`, `status`, `amount`, `fee`, `channel`, `correlation`, `comments`) VALUES (66, 1661494841095, 0, 0, 3, 1, 20, 'usdc', '', '', '0x84f9f225bd824c4fea9d33095674f82611ee3325', '', 0, '', 2, 12.000000000000000000, 0.000000000000000000, 0, '', '');
INSERT INTO `uc_user_account_action_history` (`id`, `create_time`, `update_time`, `version`, `action`, `account_type`, `user_id`, `currency`, `chain`, `from_address`, `to_address`, `tx_hash`, `block_number`, `block_hash`, `status`, `amount`, `fee`, `channel`, `correlation`, `comments`) VALUES (67, 1661495115990, 0, 0, 3, 1, 20, 'usdc', '', '', '0x84f9f225bd824c4fea9d33095674f82611ee3325', '', 0, '', 2, 11.000000000000000000, 0.000000000000000000, 0, '', '');
COMMIT;

-- ----------------------------
-- Table structure for uc_user_address
-- ----------------------------
DROP TABLE IF EXISTS `uc_user_address`;
CREATE TABLE `uc_user_address` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key',
  `create_time` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'create time',
  `update_time` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'update time',
  `version` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '版本',
  `currency` varchar(12) NOT NULL DEFAULT '',
  `chain` varchar(12) NOT NULL DEFAULT '',
  `user_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '用户id',
  `address` varchar(80) NOT NULL DEFAULT '' COMMENT '地址',
  `comments` varchar(64) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uniq_t_user_address_1` (`currency`,`chain`,`user_id`,`address`)
) ENGINE=InnoDB AUTO_INCREMENT=10004 DEFAULT CHARSET=utf8 COMMENT='用户地址';

-- ----------------------------
-- Records of uc_user_address
-- ----------------------------
BEGIN;
INSERT INTO `uc_user_address` (`id`, `create_time`, `update_time`, `version`, `currency`, `chain`, `user_id`, `address`, `comments`) VALUES (10000, 1659073525430, 1659073525430, 0, '', '', 1, '', '');
INSERT INTO `uc_user_address` (`id`, `create_time`, `update_time`, `version`, `currency`, `chain`, `user_id`, `address`, `comments`) VALUES (10001, 1661241441236, 0, 0, 'usdc', '', 20, '0x40141cf4756a72df8d8f81c1e0c25fa739cb98243d9d9302d928', '');
INSERT INTO `uc_user_address` (`id`, `create_time`, `update_time`, `version`, `currency`, `chain`, `user_id`, `address`, `comments`) VALUES (10002, 1661241441236, 1661241441236, 0, 'usdc', '', 21, '0x85bdaffe9e48f643ed1f0f920dcf6fc019204013', '');
INSERT INTO `uc_user_address` (`id`, `create_time`, `update_time`, `version`, `currency`, `chain`, `user_id`, `address`, `comments`) VALUES (10003, 1661241441236, 0, 0, 'usdc', '', 22, '0x40141cf4756a72df8d8f81c1e0c2cc05c1fc4f8d0313e6df7090', '');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
