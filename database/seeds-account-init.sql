
create table `t_eth_block`
(
    f_id              bigint unsigned auto_increment comment 'primary key',
    f_create_time     bigint unsigned not null default 0 comment 'create time',
    f_update_time     bigint unsigned not null default 0 comment 'update time',
    f_version         bigint unsigned not null default 0 comment 'version',
    f_block_number    bigint unsigned not null default 0 comment '',
    f_block_hash      varchar(80)     not null default '' comment '',
    f_parent_hash     varchar(80)     not null default ''  comment 'userId',
    f_tx_time         bigint unsigned default 0 comment '',
    f_status          int not null default 0  comment '',
    PRIMARY KEY (f_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COMMENT = 't_eth_block' AUTO_INCREMENT = 10000;


create table `t_chain_address`
(
    f_id              bigint unsigned auto_increment comment 'primary key',
    f_create_time     bigint unsigned not null default 0 comment 'create time',
    f_update_time     bigint unsigned not null default 0 comment 'update time',
    f_version         bigint unsigned not null default 0 comment 'version',
    f_currency        varchar(12)     not null default '' comment '',
    f_chain           varchar(12)     not null default '' comment '',
    f_user_id         bigint unsigned not null default 0  comment 'userId',
    f_address         varchar(80)     not null default '' comment '',
    f_comments        varchar(64)     default '' comment '',
    PRIMARY KEY (f_id),
    UNIQUE KEY uniq_t_chain_address_1 (f_currency, f_chain, f_address)
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COMMENT = 't_chain_address' AUTO_INCREMENT = 10000;


create table `t_user_address`
(
    f_id              bigint unsigned auto_increment comment 'primary key',
    f_create_time     bigint unsigned not null default 0 comment 'create time',
    f_update_time     bigint unsigned not null default 0 comment 'update time',
    f_version         bigint unsigned not null default 0 comment 'version',
    f_currency        varchar(12)     not null default '' comment '',
    f_chain           varchar(12)     not null default '' comment '',
    f_user_id         bigint unsigned not null default 0  comment 'userId',
    f_address         varchar(80)     not null default '' comment '',
    f_comments        varchar(64)     default '' comment '',
    PRIMARY KEY (f_id),
    UNIQUE KEY uniq_t_user_address_1 (f_currency, f_chain, f_user_id, f_address)
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COMMENT ='t_user_address' AUTO_INCREMENT = 10000;


create table `t_user_account_action_history`
(
    f_id              bigint unsigned auto_increment comment 'primary key',
    f_create_time     bigint unsigned not null default 0 comment 'create time',
    f_update_time     bigint unsigned not null default 0 comment 'update time',
    f_version         bigint unsigned not null default 0 comment 'version',
    f_action          int not null default 0  comment '',
    f_account_type    int not null default 0  comment '',
    f_user_id         bigint unsigned not null default 0  comment 'userId',
    f_currency        varchar(12)     not null default '' comment '',
    f_chain           varchar(12)     not null default '' comment '',
    f_from_address    varchar(80)     not null default '' comment '',
    f_to_address      varchar(80)     not null default '' comment '',
    f_tx_hash         varchar(80)     not null default '' comment '',
    f_block_number    bigint unsigned not null default 0 comment '',
    f_block_hash      varchar(80)     not null default '' comment '',
    f_status          int not null default 0  comment '',
    f_amount          decimal(36,18) not null default 0  comment '',
    f_fee             decimal(36,18) not null default 0  comment '',
    f_channel         int not null default 0  comment '',
    f_correlation     varchar(64)     not null default '' comment '',
    f_comments        varchar(64)     default '' comment '',
    PRIMARY KEY (f_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COMMENT ='t_user_account_action_history' AUTO_INCREMENT = 10000;


create table `t_user_account`
(
    f_id              bigint unsigned auto_increment comment 'primary key',
    f_create_time     bigint unsigned not null default 0 comment 'create time',
    f_update_time     bigint unsigned not null default 0 comment 'update time',
    f_version         bigint unsigned not null default 0 comment 'version',
    f_account_type    int not null default 0  comment '',
    f_user_id         bigint unsigned not null default 0  comment 'userId',
    f_currency        varchar(12)     not null default '' comment '',
    f_balance         decimal(36,18)  not null default 0  comment '',
    f_freeze          decimal(36,18)  not null default 0  comment '',
    PRIMARY KEY (f_id),
    UNIQUE KEY uniq_t_user_account_1 (f_account_type, f_user_id, f_currency)
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COMMENT ='t_user_account' AUTO_INCREMENT = 10000;

create table `t_user_account_snapshot`
(
    f_id              bigint unsigned auto_increment comment 'primary key',
    f_create_time     bigint unsigned not null default 0 comment 'create time',
    f_account_type    int not null default 0  comment '',
    f_user_id         bigint unsigned not null default 0  comment 'userId',
    f_currency        varchar(12)     not null default '' comment '',
    f_balance         decimal(36,18)  not null default 0  comment '',
    PRIMARY KEY (f_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COMMENT ='t_user_account_snapshot' AUTO_INCREMENT = 10000;

create table `t_user_account_value_history` (
    f_id              bigint unsigned auto_increment comment '主键',
    f_create_time     bigint unsigned  not null default 0 comment '时间',
    f_user_id         bigint unsigned  not null default 0 comment '用户',
    f_asset_total     decimal(36, 18)  not null default 0 comment '总资产',
    f_pnl             decimal(36, 18)  not null default 0 comment '盈亏',
    f_pnl_total       decimal(36, 18)  not null default 0 comment '累计盈亏',
    f_share           decimal(36, 18)  not null default 0 comment '分红',
    f_share_total     decimal(36, 18)  not null default 0 comment '累计分红',
    f_cursor_id       bigint unsigned  not null default 0 comment '源表游标',
    primary key(f_id),
    unique key u_user_id_create_time(f_user_id, f_create_time)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT = '用户权益历史';

create table `t_withdraw_rule`(
    f_currency        varchar(12) not null comment '币种',
    f_min_amount      decimal(36, 18)  not null default 0 comment '最小',
    f_max_amount      decimal(36, 18)  not null default 0 comment '最大',
    f_intraday_amount decimal(36, 18)  not null default 0 comment '日最多',
    f_fee_amount      decimal(36, 18)  not null default 0 comment '手续费',
    f_fee_type        tinyint  not null default 0 comment 'fee类型',
    f_decimals        tinyint  not null default 0 comment '精度',
    f_enable          tinyint  not null default 0 comment '是否启用：0停用，1启用',
    primary key(f_currency)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT = '提币规则';

create table `t_exchange_rule`(
    f_symbol          varchar(24) not null comment '币对',
    f_source_currency varchar(12) not null comment '币种',
    f_target_currency varchar(12) not null comment '币种',
    f_min_amount      decimal(36, 18)  not null default 0 comment '最小',
    f_max_amount      decimal(36, 18)  not null default 0 comment '最大',
    f_intraday_amount decimal(36, 18)  not null default 0 comment '日最多',
    f_enable          tinyint not null default 0 comment '是否启用：0停用，1启用',
    f_spread          varchar(64) not null default 0 comment '[{"level":xx, "spread": bps}]',
    primary key(f_symbol)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT = '兑换规则';

create table `t_action_rule`(
    f_action          varchar(64) not null comment '操作标识',
    f_enable          tinyint not null default 0 comment '是否启用：0停用，1启用',
    f_start_time      bigint unsigned not null default 0 comment 'effective time',
    primary key(f_action)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT = '动作规则';

create table `t_keyvalue_pair`(
    f_category     varchar(64) not null comment '分组',
    f_key          varchar(64) not null comment 'key',
    f_value        varchar(64) not null comment 'value',
    primary key(f_category, f_key)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT = '配置';

insert into `t_action_rule`(f_action, f_enable) values('account:stake', 1);
insert into `t_action_rule`(f_action, f_enable) values('account:burn', 1);
insert into `t_action_rule`(f_action, f_enable) values('account:transfer', 1);
insert into `t_action_rule`(f_action, f_enable) values('account:exchange', 1);
insert into `t_action_rule`(f_action, f_enable) values('account:claim:fee', 1);
insert into `t_action_rule`(f_action, f_enable) values('account:withdraw', 1);
insert into `t_action_rule`(f_action, f_enable) values('job:check-staking-account', 1);
insert into `t_action_rule`(f_action, f_enable) values('job:check-trading-account', 1);

insert into t_keyvalue_pair(f_category, f_key, f_value) values ('eth.chain', 'gas.price', '20000000000');
insert into t_keyvalue_pair(f_category, f_key, f_value) values ('eth.chain', 'gas.limit', '200000');
insert into t_keyvalue_pair(f_category, f_key, f_value) values ('asset.holding.rate', 'maxRate', '0.00375');
insert into t_keyvalue_pair(f_category, f_key, f_value) values ('asset.holding.rate', 't.kBTC', '0.10');
insert into t_keyvalue_pair(f_category, f_key, f_value) values ('asset.holding.rate', 't.kETH', '0.05');
insert into t_keyvalue_pair(f_category, f_key, f_value) values ('asset.holding.rate', 't.kBCH', '0.05');
insert into t_keyvalue_pair(f_category, f_key, f_value) values ('asset.holding.rate', 't.kLTC', '0.05');
insert into t_keyvalue_pair(f_category, f_key, f_value) values ('asset.holding.rate', 't.kEOS', '0.05');

create table `t_funding_rate`(
    f_id              bigint unsigned auto_increment comment 'primary key',
    f_create_time     bigint not null default 0 comment '',
    f_type            tinyint not null default 0 comment '0: current, 1: settlement twap',
    f_currency        varchar(12) not null comment '',
    f_rate            decimal(36,18) not null default 0  comment '',
    primary key(f_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT = 'funding rate';

alter table `t_funding_rate`
  add index idx_currency_time (f_currency, f_create_time),
  add index idx_time_currency (f_create_time, f_currency);


create table `t_dividend_summary`(
    f_id              bigint unsigned auto_increment comment 'primary key',
    f_create_time     bigint not null default 0 comment '',
    f_mcd_total       decimal(36,18) not null default 0  comment '',
    f_fee_total       decimal(36,18) not null default 0  comment '',
    f_percent         decimal(36,18) not null default 0  comment '',
    f_share           decimal(36,18) not null default 0  comment '',
    f_users           bigint not null default 0 comment '',
    primary key(f_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT = 'dividend summary';

create table `t_black_address`
(
    f_id              bigint unsigned auto_increment comment 'primary key',
    f_address         varchar(80)     not null default '' comment '',
    f_comments        varchar(64)     default '' comment '',
    PRIMARY KEY (f_id),
    UNIQUE KEY uniq_1 (f_address)
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COMMENT ='t_black_address';

--
-- init first block
--
insert into `t_eth_block` (f_id, f_block_number, f_block_hash, f_parent_hash, f_status)
    values(1000, 0, '0x40ebeba08ce8f46cfe5273dfbcc8060ab59637337d3efd6d5ff0c005677abc40',
    '0x0000000000000000000000000000000000000000000000000000000000000000', 1);

update t_eth_block
set f_block_number=2,
    f_block_hash='0xb189e9f3e5d42b832ce9e0d7881c4865fec2547e734ee6acda46517b2925e968',
    f_parent_hash='0xfa594bcb6c36486b99991bb2443f63844d83f80874e05d5ef6079ff58207b8eb',
    f_status=1
where f_id=1000;

--
-- system user 1000 (wallet, staking, trading)
--
insert into `t_user_account` (f_id, f_account_type, f_user_id, f_currency) values(1000, 0, 1000, 'ETH');
insert into `t_user_account` (f_id, f_account_type, f_user_id, f_currency) values(1001, 0, 1000, 'USDT');
insert into `t_user_account` (f_id, f_account_type, f_user_id, f_currency) values(1002, 0, 1000, 'KINE');
insert into `t_user_account` (f_id, f_account_type, f_user_id, f_currency) values(1003, 0, 1000, 'kUSD');
insert into `t_user_account` (f_id, f_account_type, f_user_id, f_currency) values(1004, 1, 1000, 'KINE');
insert into `t_user_account` (f_id, f_account_type, f_user_id, f_currency) values(1005, 1, 1000, 'MCD');

--
-- trading fee user 1001 (wallet, staking, trading)
--
insert into `t_user_account` (f_id, f_account_type, f_user_id, f_currency) values(1010, 0, 1001, 'ETH');
insert into `t_user_account` (f_id, f_account_type, f_user_id, f_currency) values(1011, 0, 1001, 'USDT');
insert into `t_user_account` (f_id, f_account_type, f_user_id, f_currency) values(1012, 0, 1001, 'KINE');
insert into `t_user_account` (f_id, f_account_type, f_user_id, f_currency) values(1013, 0, 1001, 'kUSD');
insert into `t_user_account` (f_id, f_account_type, f_user_id, f_currency) values(1014, 1, 1001, 'KINE');
insert into `t_user_account` (f_id, f_account_type, f_user_id, f_currency) values(1015, 1, 1001, 'MCD');

--
-- withdraw fee user 1002 (wallet, staking, trading)
--
insert into `t_user_account` (f_id, f_account_type, f_user_id, f_currency) values(1020, 0, 1002, 'ETH');
insert into `t_user_account` (f_id, f_account_type, f_user_id, f_currency) values(1021, 0, 1002, 'USDT');
insert into `t_user_account` (f_id, f_account_type, f_user_id, f_currency) values(1022, 0, 1002, 'KINE');
insert into `t_user_account` (f_id, f_account_type, f_user_id, f_currency) values(1023, 0, 1002, 'kUSD');
insert into `t_user_account` (f_id, f_account_type, f_user_id, f_currency) values(1024, 1, 1002, 'KINE');
insert into `t_user_account` (f_id, f_account_type, f_user_id, f_currency) values(1025, 1, 1002, 'MCD');

--
-- operation user 1003 (wallet, staking, trading)
--
insert into `t_user_account` (f_id, f_account_type, f_user_id, f_currency) values(1030, 0, 1003, 'ETH');
insert into `t_user_account` (f_id, f_account_type, f_user_id, f_currency) values(1031, 0, 1003, 'USDT');
insert into `t_user_account` (f_id, f_account_type, f_user_id, f_currency) values(1032, 0, 1003, 'KINE');
insert into `t_user_account` (f_id, f_account_type, f_user_id, f_currency) values(1033, 0, 1003, 'kUSD');
insert into `t_user_account` (f_id, f_account_type, f_user_id, f_currency) values(1034, 1, 1003, 'KINE');
insert into `t_user_account` (f_id, f_account_type, f_user_id, f_currency) values(1035, 1, 1003, 'MCD');


--
-- operation user 1004 (Minter)
--
insert into `t_user_account` (f_id, f_account_type, f_user_id, f_currency) values(1036, 0, 1004, 'ETH');
insert into `t_user_account` (f_id, f_account_type, f_user_id, f_currency) values(1037, 0, 1004, 'USDT');
insert into `t_user_account` (f_id, f_account_type, f_user_id, f_currency) values(1038, 0, 1004, 'KINE');
insert into `t_user_account` (f_id, f_account_type, f_user_id, f_currency) values(1039, 0, 1004, 'kUSD');
insert into `t_user_account` (f_id, f_account_type, f_user_id, f_currency) values(1040, 1, 1004, 'KINE');
insert into `t_user_account` (f_id, f_account_type, f_user_id, f_currency) values(1041, 1, 1004, 'MCD');


insert into `t_withdraw_rule` (f_currency, f_min_amount, f_max_amount, f_intraday_amount, f_fee_amount, f_fee_type, f_decimals, f_enable)
values ('USDT', 5, 100000, 500000, 2, 0, 4, 1);

insert into `t_withdraw_rule` (f_currency, f_min_amount, f_max_amount, f_intraday_amount, f_fee_amount, f_fee_type, f_decimals, f_enable)
values ('KINE', 5, 100000, 500000, 1, 0, 4, 1);

insert into `t_exchange_rule` (f_symbol, f_source_currency, f_target_currency, f_min_amount, f_max_amount, f_intraday_amount, f_enable, f_spread)
values ('USDT/kUSD', 'USDT', 'kUSD', 1, 100000, 500000, 1, '[]');

insert into `t_exchange_rule` (f_symbol, f_source_currency, f_target_currency, f_min_amount, f_max_amount, f_intraday_amount, f_enable, f_spread)
values ('kUSD/USDT', 'kUSD', 'USDT', 1, 100000, 500000, 1, '[]');






