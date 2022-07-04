-- uniswap v2 price cumulative
create table t_market_price_cumulative
(
    f_id               bigint(20) unsigned not null auto_increment comment 'primary key ID',
    f_block_hash       varchar(256)        not null default '' comment 'ETH Block hash',
    f_block_number     bigint(20) unsigned not null default '0' comment 'eth block number',
    f_price_cumulative text comment 'price cumulative',
    f_price_timestamp  bigint(20) unsigned not null default '0' comment 'price unix timestamp in second',
    f_created_at       bigint(20) unsigned not null default '0' comment 'created unix timestamp in millisecond',
    primary key (f_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4 COMMENT ='uniswap v2 price cumulative';

-- market price tables
create table t_market_price_kbtc
(
    f_id         bigint(20) unsigned not null auto_increment comment 'primary key ID',
    f_price      decimal(20, 8)      not null default 0 comment '交易数量',
    f_created_at bigint(20) unsigned not null default '0' comment 'created timestamp',
    primary key (f_id),
    key idx_mpkbtc_f_create_at (f_created_at)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4 COMMENT ='btc 价格，来自火币，币安，okex交易所价格的加权平均';

create table t_market_price_keth
(
    f_id         bigint(20) unsigned not null auto_increment comment 'primary key ID',
    f_price      decimal(20, 8)      not null default 0 comment '交易数量',
    f_created_at bigint(20) unsigned not null default '0' comment 'created timestamp',
    primary key (f_id),
    key idx_mpketh_f_create_at (f_created_at)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4 COMMENT ='eth 价格，来自火币，币安，okex交易所价格的加权平均';

create table t_market_price_kbch
(
    f_id         bigint(20) unsigned not null auto_increment comment 'primary key ID',
    f_price      decimal(20, 8)      not null default 0 comment '交易数量',
    f_created_at bigint(20) unsigned not null default '0' comment 'created timestamp',
    primary key (f_id),
    key idx_mpkbch_f_create_at (f_created_at)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4 COMMENT ='bch 价格，来自火币，币安，okex交易所价格的加权平均';

create table t_market_price_kltc
(
    f_id         bigint(20) unsigned not null auto_increment comment 'primary key ID',
    f_price      decimal(20, 8)      not null default 0 comment '交易数量',
    f_created_at bigint(20) unsigned not null default '0' comment 'created timestamp',
    primary key (f_id),
    key idx_mpkltc_f_create_at (f_created_at)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4 COMMENT ='ltc 价格，来自火币，币安，okex交易所价格的加权平均';

create table t_market_price_keos
(
    f_id         bigint(20) unsigned not null auto_increment comment 'primary key ID',
    f_price      decimal(20, 8)      not null default 0 comment '交易数量',
    f_created_at bigint(20) unsigned not null default '0' comment 'created timestamp',
    primary key (f_id),
    key idx_mpkeos_f_create_at (f_created_at)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4 COMMENT ='eos 价格，来自火币，币安，okex交易所价格的加权平均';

-- kline tables kbtc
create table t_market_kline_1min_kbtc
(
    f_id         bigint(20) unsigned not null auto_increment comment 'primary key ID',
    f_amount     decimal(20, 8)      not null default 0 comment '交易数量',
    f_vol        decimal(20, 8)      not null default 0 comment '交易总额，USDT计价',
    f_count      bigint(20) unsigned not null comment '交易次数',
    f_open       decimal(20, 8)      not null default 0 comment '开盘价',
    f_close      decimal(20, 8)      not null default 0 comment '收盘价',
    f_high       decimal(20, 8)      not null default 0 comment '最高价',
    f_low        decimal(20, 8)      not null default 0 comment '最低价',
    f_created_at bigint(20) unsigned not null default '0' comment 'created timestamp',
    primary key (f_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4 COMMENT ='kbtc 1分钟K线';

create table t_market_kline_5min_kbtc
(
    f_id         bigint(20) unsigned not null auto_increment comment 'primary key ID',
    f_amount     decimal(20, 8)      not null default 0 comment '交易数量',
    f_vol        decimal(20, 8)      not null default 0 comment '交易总额，USDT计价',
    f_count      bigint(20) unsigned not null comment '交易次数',
    f_open       decimal(20, 8)      not null default 0 comment '开盘价',
    f_close      decimal(20, 8)      not null default 0 comment '收盘价',
    f_high       decimal(20, 8)      not null default 0 comment '最高价',
    f_low        decimal(20, 8)      not null default 0 comment '最低价',
    f_created_at bigint(20) unsigned not null default '0' comment 'created timestamp',
    primary key (f_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4 COMMENT ='kbtc 5分钟K线';

create table t_market_kline_15min_kbtc
(
    f_id         bigint(20) unsigned not null auto_increment comment 'primary key ID',
    f_amount     decimal(20, 8)      not null default 0 comment '交易数量',
    f_vol        decimal(20, 8)      not null default 0 comment '交易总额，USDT计价',
    f_count      bigint(20) unsigned not null comment '交易次数',
    f_open       decimal(20, 8)      not null default 0 comment '开盘价',
    f_close      decimal(20, 8)      not null default 0 comment '收盘价',
    f_high       decimal(20, 8)      not null default 0 comment '最高价',
    f_low        decimal(20, 8)      not null default 0 comment '最低价',
    f_created_at bigint(20) unsigned not null default '0' comment 'created timestamp',
    primary key (f_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4 COMMENT ='kbtc 15分钟K线';

create table t_market_kline_30min_kbtc
(
    f_id         bigint(20) unsigned not null auto_increment comment 'primary key ID',
    f_amount     decimal(20, 8)      not null default 0 comment '交易数量',
    f_vol        decimal(20, 8)      not null default 0 comment '交易总额，USDT计价',
    f_count      bigint(20) unsigned not null comment '交易次数',
    f_open       decimal(20, 8)      not null default 0 comment '开盘价',
    f_close      decimal(20, 8)      not null default 0 comment '收盘价',
    f_high       decimal(20, 8)      not null default 0 comment '最高价',
    f_low        decimal(20, 8)      not null default 0 comment '最低价',
    f_created_at bigint(20) unsigned not null default '0' comment 'created timestamp',
    primary key (f_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4 COMMENT ='kbtc 30分钟K线';

create table t_market_kline_60min_kbtc
(
    f_id         bigint(20) unsigned not null auto_increment comment 'primary key ID',
    f_amount     decimal(20, 8)      not null default 0 comment '交易数量',
    f_vol        decimal(20, 8)      not null default 0 comment '交易总额，USDT计价',
    f_count      bigint(20) unsigned not null comment '交易次数',
    f_open       decimal(20, 8)      not null default 0 comment '开盘价',
    f_close      decimal(20, 8)      not null default 0 comment '收盘价',
    f_high       decimal(20, 8)      not null default 0 comment '最高价',
    f_low        decimal(20, 8)      not null default 0 comment '最低价',
    f_created_at bigint(20) unsigned not null default '0' comment 'created timestamp',
    primary key (f_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4 COMMENT ='kbtc 60分钟K线';

create table t_market_kline_4hr_kbtc
(
    f_id         bigint(20) unsigned not null auto_increment comment 'primary key ID',
    f_amount     decimal(20, 8)      not null default 0 comment '交易数量',
    f_vol        decimal(20, 8)      not null default 0 comment '交易总额，USDT计价',
    f_count      bigint(20) unsigned not null comment '交易次数',
    f_open       decimal(20, 8)      not null default 0 comment '开盘价',
    f_close      decimal(20, 8)      not null default 0 comment '收盘价',
    f_high       decimal(20, 8)      not null default 0 comment '最高价',
    f_low        decimal(20, 8)      not null default 0 comment '最低价',
    f_created_at bigint(20) unsigned not null default '0' comment 'created timestamp',
    primary key (f_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4 COMMENT ='kbtc 4小时K线';

create table t_market_kline_1day_kbtc
(
    f_id         bigint(20) unsigned not null auto_increment comment 'primary key ID',
    f_amount     decimal(20, 8)      not null default 0 comment '交易数量',
    f_vol        decimal(20, 8)      not null default 0 comment '交易总额，USDT计价',
    f_count      bigint(20) unsigned not null comment '交易次数',
    f_open       decimal(20, 8)      not null default 0 comment '开盘价',
    f_close      decimal(20, 8)      not null default 0 comment '收盘价',
    f_high       decimal(20, 8)      not null default 0 comment '最高价',
    f_low        decimal(20, 8)      not null default 0 comment '最低价',
    f_created_at bigint(20) unsigned not null default '0' comment 'created timestamp',
    primary key (f_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4 COMMENT ='kbtc 1天K线';

create table t_market_kline_1w_kbtc
(
    f_id         bigint(20) unsigned not null auto_increment comment 'primary key ID',
    f_amount     decimal(20, 8)      not null default 0 comment '交易数量',
    f_vol        decimal(20, 8)      not null default 0 comment '交易总额，USDT计价',
    f_count      bigint(20) unsigned not null comment '交易次数',
    f_open       decimal(20, 8)      not null default 0 comment '开盘价',
    f_close      decimal(20, 8)      not null default 0 comment '收盘价',
    f_high       decimal(20, 8)      not null default 0 comment '最高价',
    f_low        decimal(20, 8)      not null default 0 comment '最低价',
    f_created_at bigint(20) unsigned not null default '0' comment 'created timestamp',
    primary key (f_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4 COMMENT ='kbtc 1周K线';

create table t_market_kline_1mon_kbtc
(
    f_id         bigint(20) unsigned not null auto_increment comment 'primary key ID',
    f_amount     decimal(20, 8)      not null default 0 comment '交易数量',
    f_vol        decimal(20, 8)      not null default 0 comment '交易总额，USDT计价',
    f_count      bigint(20) unsigned not null comment '交易次数',
    f_open       decimal(20, 8)      not null default 0 comment '开盘价',
    f_close      decimal(20, 8)      not null default 0 comment '收盘价',
    f_high       decimal(20, 8)      not null default 0 comment '最高价',
    f_low        decimal(20, 8)      not null default 0 comment '最低价',
    f_created_at bigint(20) unsigned not null default '0' comment 'created timestamp',
    primary key (f_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4 COMMENT ='kbtc 1月K线';

-- kline tables keth
create table t_market_kline_1min_keth
(
    f_id         bigint(20) unsigned not null auto_increment comment 'primary key ID',
    f_amount     decimal(20, 8)      not null default 0 comment '交易数量',
    f_vol        decimal(20, 8)      not null default 0 comment '交易总额，USDT计价',
    f_count      bigint(20) unsigned not null comment '交易次数',
    f_open       decimal(20, 8)      not null default 0 comment '开盘价',
    f_close      decimal(20, 8)      not null default 0 comment '收盘价',
    f_high       decimal(20, 8)      not null default 0 comment '最高价',
    f_low        decimal(20, 8)      not null default 0 comment '最低价',
    f_created_at bigint(20) unsigned not null default '0' comment 'created timestamp',
    primary key (f_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4 COMMENT ='keth 1分钟K线';

create table t_market_kline_5min_keth
(
    f_id         bigint(20) unsigned not null auto_increment comment 'primary key ID',
    f_amount     decimal(20, 8)      not null default 0 comment '交易数量',
    f_vol        decimal(20, 8)      not null default 0 comment '交易总额，USDT计价',
    f_count      bigint(20) unsigned not null comment '交易次数',
    f_open       decimal(20, 8)      not null default 0 comment '开盘价',
    f_close      decimal(20, 8)      not null default 0 comment '收盘价',
    f_high       decimal(20, 8)      not null default 0 comment '最高价',
    f_low        decimal(20, 8)      not null default 0 comment '最低价',
    f_created_at bigint(20) unsigned not null default '0' comment 'created timestamp',
    primary key (f_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4 COMMENT ='keth 5分钟K线';

create table t_market_kline_15min_keth
(
    f_id         bigint(20) unsigned not null auto_increment comment 'primary key ID',
    f_amount     decimal(20, 8)      not null default 0 comment '交易数量',
    f_vol        decimal(20, 8)      not null default 0 comment '交易总额，USDT计价',
    f_count      bigint(20) unsigned not null comment '交易次数',
    f_open       decimal(20, 8)      not null default 0 comment '开盘价',
    f_close      decimal(20, 8)      not null default 0 comment '收盘价',
    f_high       decimal(20, 8)      not null default 0 comment '最高价',
    f_low        decimal(20, 8)      not null default 0 comment '最低价',
    f_created_at bigint(20) unsigned not null default '0' comment 'created timestamp',
    primary key (f_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4 COMMENT ='keth 15分钟K线';

create table t_market_kline_30min_keth
(
    f_id         bigint(20) unsigned not null auto_increment comment 'primary key ID',
    f_amount     decimal(20, 8)      not null default 0 comment '交易数量',
    f_vol        decimal(20, 8)      not null default 0 comment '交易总额，USDT计价',
    f_count      bigint(20) unsigned not null comment '交易次数',
    f_open       decimal(20, 8)      not null default 0 comment '开盘价',
    f_close      decimal(20, 8)      not null default 0 comment '收盘价',
    f_high       decimal(20, 8)      not null default 0 comment '最高价',
    f_low        decimal(20, 8)      not null default 0 comment '最低价',
    f_created_at bigint(20) unsigned not null default '0' comment 'created timestamp',
    primary key (f_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4 COMMENT ='keth 30分钟K线';

create table t_market_kline_60min_keth
(
    f_id         bigint(20) unsigned not null auto_increment comment 'primary key ID',
    f_amount     decimal(20, 8)      not null default 0 comment '交易数量',
    f_vol        decimal(20, 8)      not null default 0 comment '交易总额，USDT计价',
    f_count      bigint(20) unsigned not null comment '交易次数',
    f_open       decimal(20, 8)      not null default 0 comment '开盘价',
    f_close      decimal(20, 8)      not null default 0 comment '收盘价',
    f_high       decimal(20, 8)      not null default 0 comment '最高价',
    f_low        decimal(20, 8)      not null default 0 comment '最低价',
    f_created_at bigint(20) unsigned not null default '0' comment 'created timestamp',
    primary key (f_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4 COMMENT ='keth 60分钟K线';

create table t_market_kline_4hr_keth
(
    f_id         bigint(20) unsigned not null auto_increment comment 'primary key ID',
    f_amount     decimal(20, 8)      not null default 0 comment '交易数量',
    f_vol        decimal(20, 8)      not null default 0 comment '交易总额，USDT计价',
    f_count      bigint(20) unsigned not null comment '交易次数',
    f_open       decimal(20, 8)      not null default 0 comment '开盘价',
    f_close      decimal(20, 8)      not null default 0 comment '收盘价',
    f_high       decimal(20, 8)      not null default 0 comment '最高价',
    f_low        decimal(20, 8)      not null default 0 comment '最低价',
    f_created_at bigint(20) unsigned not null default '0' comment 'created timestamp',
    primary key (f_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4 COMMENT ='keth 4小时K线';

create table t_market_kline_1day_keth
(
    f_id         bigint(20) unsigned not null auto_increment comment 'primary key ID',
    f_amount     decimal(20, 8)      not null default 0 comment '交易数量',
    f_vol        decimal(20, 8)      not null default 0 comment '交易总额，USDT计价',
    f_count      bigint(20) unsigned not null comment '交易次数',
    f_open       decimal(20, 8)      not null default 0 comment '开盘价',
    f_close      decimal(20, 8)      not null default 0 comment '收盘价',
    f_high       decimal(20, 8)      not null default 0 comment '最高价',
    f_low        decimal(20, 8)      not null default 0 comment '最低价',
    f_created_at bigint(20) unsigned not null default '0' comment 'created timestamp',
    primary key (f_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4 COMMENT ='keth 1天K线';

create table t_market_kline_1w_keth
(
    f_id         bigint(20) unsigned not null auto_increment comment 'primary key ID',
    f_amount     decimal(20, 8)      not null default 0 comment '交易数量',
    f_vol        decimal(20, 8)      not null default 0 comment '交易总额，USDT计价',
    f_count      bigint(20) unsigned not null comment '交易次数',
    f_open       decimal(20, 8)      not null default 0 comment '开盘价',
    f_close      decimal(20, 8)      not null default 0 comment '收盘价',
    f_high       decimal(20, 8)      not null default 0 comment '最高价',
    f_low        decimal(20, 8)      not null default 0 comment '最低价',
    f_created_at bigint(20) unsigned not null default '0' comment 'created timestamp',
    primary key (f_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4 COMMENT ='keth 1周K线';

create table t_market_kline_1mon_keth
(
    f_id         bigint(20) unsigned not null auto_increment comment 'primary key ID',
    f_amount     decimal(20, 8)      not null default 0 comment '交易数量',
    f_vol        decimal(20, 8)      not null default 0 comment '交易总额，USDT计价',
    f_count      bigint(20) unsigned not null comment '交易次数',
    f_open       decimal(20, 8)      not null default 0 comment '开盘价',
    f_close      decimal(20, 8)      not null default 0 comment '收盘价',
    f_high       decimal(20, 8)      not null default 0 comment '最高价',
    f_low        decimal(20, 8)      not null default 0 comment '最低价',
    f_created_at bigint(20) unsigned not null default '0' comment 'created timestamp',
    primary key (f_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4 COMMENT ='keth 1月K线';

-- kline tables kbch
create table t_market_kline_1min_kbch
(
    f_id         bigint(20) unsigned not null auto_increment comment 'primary key ID',
    f_amount     decimal(20, 8)      not null default 0 comment '交易数量',
    f_vol        decimal(20, 8)      not null default 0 comment '交易总额，USDT计价',
    f_count      bigint(20) unsigned not null comment '交易次数',
    f_open       decimal(20, 8)      not null default 0 comment '开盘价',
    f_close      decimal(20, 8)      not null default 0 comment '收盘价',
    f_high       decimal(20, 8)      not null default 0 comment '最高价',
    f_low        decimal(20, 8)      not null default 0 comment '最低价',
    f_created_at bigint(20) unsigned not null default '0' comment 'created timestamp',
    primary key (f_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4 COMMENT ='kbch 1分钟K线';

create table t_market_kline_5min_kbch
(
    f_id         bigint(20) unsigned not null auto_increment comment 'primary key ID',
    f_amount     decimal(20, 8)      not null default 0 comment '交易数量',
    f_vol        decimal(20, 8)      not null default 0 comment '交易总额，USDT计价',
    f_count      bigint(20) unsigned not null comment '交易次数',
    f_open       decimal(20, 8)      not null default 0 comment '开盘价',
    f_close      decimal(20, 8)      not null default 0 comment '收盘价',
    f_high       decimal(20, 8)      not null default 0 comment '最高价',
    f_low        decimal(20, 8)      not null default 0 comment '最低价',
    f_created_at bigint(20) unsigned not null default '0' comment 'created timestamp',
    primary key (f_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4 COMMENT ='kbch 5分钟K线';

create table t_market_kline_15min_kbch
(
    f_id         bigint(20) unsigned not null auto_increment comment 'primary key ID',
    f_amount     decimal(20, 8)      not null default 0 comment '交易数量',
    f_vol        decimal(20, 8)      not null default 0 comment '交易总额，USDT计价',
    f_count      bigint(20) unsigned not null comment '交易次数',
    f_open       decimal(20, 8)      not null default 0 comment '开盘价',
    f_close      decimal(20, 8)      not null default 0 comment '收盘价',
    f_high       decimal(20, 8)      not null default 0 comment '最高价',
    f_low        decimal(20, 8)      not null default 0 comment '最低价',
    f_created_at bigint(20) unsigned not null default '0' comment 'created timestamp',
    primary key (f_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4 COMMENT ='kbch 15分钟K线';

create table t_market_kline_30min_kbch
(
    f_id         bigint(20) unsigned not null auto_increment comment 'primary key ID',
    f_amount     decimal(20, 8)      not null default 0 comment '交易数量',
    f_vol        decimal(20, 8)      not null default 0 comment '交易总额，USDT计价',
    f_count      bigint(20) unsigned not null comment '交易次数',
    f_open       decimal(20, 8)      not null default 0 comment '开盘价',
    f_close      decimal(20, 8)      not null default 0 comment '收盘价',
    f_high       decimal(20, 8)      not null default 0 comment '最高价',
    f_low        decimal(20, 8)      not null default 0 comment '最低价',
    f_created_at bigint(20) unsigned not null default '0' comment 'created timestamp',
    primary key (f_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4 COMMENT ='kbch 30分钟K线';

create table t_market_kline_60min_kbch
(
    f_id         bigint(20) unsigned not null auto_increment comment 'primary key ID',
    f_amount     decimal(20, 8)      not null default 0 comment '交易数量',
    f_vol        decimal(20, 8)      not null default 0 comment '交易总额，USDT计价',
    f_count      bigint(20) unsigned not null comment '交易次数',
    f_open       decimal(20, 8)      not null default 0 comment '开盘价',
    f_close      decimal(20, 8)      not null default 0 comment '收盘价',
    f_high       decimal(20, 8)      not null default 0 comment '最高价',
    f_low        decimal(20, 8)      not null default 0 comment '最低价',
    f_created_at bigint(20) unsigned not null default '0' comment 'created timestamp',
    primary key (f_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4 COMMENT ='kbch 60分钟K线';

create table t_market_kline_4hr_kbch
(
    f_id         bigint(20) unsigned not null auto_increment comment 'primary key ID',
    f_amount     decimal(20, 8)      not null default 0 comment '交易数量',
    f_vol        decimal(20, 8)      not null default 0 comment '交易总额，USDT计价',
    f_count      bigint(20) unsigned not null comment '交易次数',
    f_open       decimal(20, 8)      not null default 0 comment '开盘价',
    f_close      decimal(20, 8)      not null default 0 comment '收盘价',
    f_high       decimal(20, 8)      not null default 0 comment '最高价',
    f_low        decimal(20, 8)      not null default 0 comment '最低价',
    f_created_at bigint(20) unsigned not null default '0' comment 'created timestamp',
    primary key (f_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4 COMMENT ='kbch 4小时K线';

create table t_market_kline_1day_kbch
(
    f_id         bigint(20) unsigned not null auto_increment comment 'primary key ID',
    f_amount     decimal(20, 8)      not null default 0 comment '交易数量',
    f_vol        decimal(20, 8)      not null default 0 comment '交易总额，USDT计价',
    f_count      bigint(20) unsigned not null comment '交易次数',
    f_open       decimal(20, 8)      not null default 0 comment '开盘价',
    f_close      decimal(20, 8)      not null default 0 comment '收盘价',
    f_high       decimal(20, 8)      not null default 0 comment '最高价',
    f_low        decimal(20, 8)      not null default 0 comment '最低价',
    f_created_at bigint(20) unsigned not null default '0' comment 'created timestamp',
    primary key (f_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4 COMMENT ='kbch 1天K线';

create table t_market_kline_1w_kbch
(
    f_id         bigint(20) unsigned not null auto_increment comment 'primary key ID',
    f_amount     decimal(20, 8)      not null default 0 comment '交易数量',
    f_vol        decimal(20, 8)      not null default 0 comment '交易总额，USDT计价',
    f_count      bigint(20) unsigned not null comment '交易次数',
    f_open       decimal(20, 8)      not null default 0 comment '开盘价',
    f_close      decimal(20, 8)      not null default 0 comment '收盘价',
    f_high       decimal(20, 8)      not null default 0 comment '最高价',
    f_low        decimal(20, 8)      not null default 0 comment '最低价',
    f_created_at bigint(20) unsigned not null default '0' comment 'created timestamp',
    primary key (f_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4 COMMENT ='kbch 1周K线';

create table t_market_kline_1mon_kbch
(
    f_id         bigint(20) unsigned not null auto_increment comment 'primary key ID',
    f_amount     decimal(20, 8)      not null default 0 comment '交易数量',
    f_vol        decimal(20, 8)      not null default 0 comment '交易总额，USDT计价',
    f_count      bigint(20) unsigned not null comment '交易次数',
    f_open       decimal(20, 8)      not null default 0 comment '开盘价',
    f_close      decimal(20, 8)      not null default 0 comment '收盘价',
    f_high       decimal(20, 8)      not null default 0 comment '最高价',
    f_low        decimal(20, 8)      not null default 0 comment '最低价',
    f_created_at bigint(20) unsigned not null default '0' comment 'created timestamp',
    primary key (f_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4 COMMENT ='kbch 1月K线';

-- kline tables kltc
create table t_market_kline_1min_kltc
(
    f_id         bigint(20) unsigned not null auto_increment comment 'primary key ID',
    f_amount     decimal(20, 8)      not null default 0 comment '交易数量',
    f_vol        decimal(20, 8)      not null default 0 comment '交易总额，USDT计价',
    f_count      bigint(20) unsigned not null comment '交易次数',
    f_open       decimal(20, 8)      not null default 0 comment '开盘价',
    f_close      decimal(20, 8)      not null default 0 comment '收盘价',
    f_high       decimal(20, 8)      not null default 0 comment '最高价',
    f_low        decimal(20, 8)      not null default 0 comment '最低价',
    f_created_at bigint(20) unsigned not null default '0' comment 'created timestamp',
    primary key (f_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4 COMMENT ='kltc 1分钟K线';

create table t_market_kline_5min_kltc
(
    f_id         bigint(20) unsigned not null auto_increment comment 'primary key ID',
    f_amount     decimal(20, 8)      not null default 0 comment '交易数量',
    f_vol        decimal(20, 8)      not null default 0 comment '交易总额，USDT计价',
    f_count      bigint(20) unsigned not null comment '交易次数',
    f_open       decimal(20, 8)      not null default 0 comment '开盘价',
    f_close      decimal(20, 8)      not null default 0 comment '收盘价',
    f_high       decimal(20, 8)      not null default 0 comment '最高价',
    f_low        decimal(20, 8)      not null default 0 comment '最低价',
    f_created_at bigint(20) unsigned not null default '0' comment 'created timestamp',
    primary key (f_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4 COMMENT ='kltc 5分钟K线';

create table t_market_kline_15min_kltc
(
    f_id         bigint(20) unsigned not null auto_increment comment 'primary key ID',
    f_amount     decimal(20, 8)      not null default 0 comment '交易数量',
    f_vol        decimal(20, 8)      not null default 0 comment '交易总额，USDT计价',
    f_count      bigint(20) unsigned not null comment '交易次数',
    f_open       decimal(20, 8)      not null default 0 comment '开盘价',
    f_close      decimal(20, 8)      not null default 0 comment '收盘价',
    f_high       decimal(20, 8)      not null default 0 comment '最高价',
    f_low        decimal(20, 8)      not null default 0 comment '最低价',
    f_created_at bigint(20) unsigned not null default '0' comment 'created timestamp',
    primary key (f_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4 COMMENT ='kltc 15分钟K线';

create table t_market_kline_30min_kltc
(
    f_id         bigint(20) unsigned not null auto_increment comment 'primary key ID',
    f_amount     decimal(20, 8)      not null default 0 comment '交易数量',
    f_vol        decimal(20, 8)      not null default 0 comment '交易总额，USDT计价',
    f_count      bigint(20) unsigned not null comment '交易次数',
    f_open       decimal(20, 8)      not null default 0 comment '开盘价',
    f_close      decimal(20, 8)      not null default 0 comment '收盘价',
    f_high       decimal(20, 8)      not null default 0 comment '最高价',
    f_low        decimal(20, 8)      not null default 0 comment '最低价',
    f_created_at bigint(20) unsigned not null default '0' comment 'created timestamp',
    primary key (f_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4 COMMENT ='kltc 30分钟K线';

create table t_market_kline_60min_kltc
(
    f_id         bigint(20) unsigned not null auto_increment comment 'primary key ID',
    f_amount     decimal(20, 8)      not null default 0 comment '交易数量',
    f_vol        decimal(20, 8)      not null default 0 comment '交易总额，USDT计价',
    f_count      bigint(20) unsigned not null comment '交易次数',
    f_open       decimal(20, 8)      not null default 0 comment '开盘价',
    f_close      decimal(20, 8)      not null default 0 comment '收盘价',
    f_high       decimal(20, 8)      not null default 0 comment '最高价',
    f_low        decimal(20, 8)      not null default 0 comment '最低价',
    f_created_at bigint(20) unsigned not null default '0' comment 'created timestamp',
    primary key (f_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4 COMMENT ='kltc 60分钟K线';

create table t_market_kline_4hr_kltc
(
    f_id         bigint(20) unsigned not null auto_increment comment 'primary key ID',
    f_amount     decimal(20, 8)      not null default 0 comment '交易数量',
    f_vol        decimal(20, 8)      not null default 0 comment '交易总额，USDT计价',
    f_count      bigint(20) unsigned not null comment '交易次数',
    f_open       decimal(20, 8)      not null default 0 comment '开盘价',
    f_close      decimal(20, 8)      not null default 0 comment '收盘价',
    f_high       decimal(20, 8)      not null default 0 comment '最高价',
    f_low        decimal(20, 8)      not null default 0 comment '最低价',
    f_created_at bigint(20) unsigned not null default '0' comment 'created timestamp',
    primary key (f_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4 COMMENT ='kltc 4小时K线';

create table t_market_kline_1day_kltc
(
    f_id         bigint(20) unsigned not null auto_increment comment 'primary key ID',
    f_amount     decimal(20, 8)      not null default 0 comment '交易数量',
    f_vol        decimal(20, 8)      not null default 0 comment '交易总额，USDT计价',
    f_count      bigint(20) unsigned not null comment '交易次数',
    f_open       decimal(20, 8)      not null default 0 comment '开盘价',
    f_close      decimal(20, 8)      not null default 0 comment '收盘价',
    f_high       decimal(20, 8)      not null default 0 comment '最高价',
    f_low        decimal(20, 8)      not null default 0 comment '最低价',
    f_created_at bigint(20) unsigned not null default '0' comment 'created timestamp',
    primary key (f_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4 COMMENT ='kltc 1天K线';

create table t_market_kline_1w_kltc
(
    f_id         bigint(20) unsigned not null auto_increment comment 'primary key ID',
    f_amount     decimal(20, 8)      not null default 0 comment '交易数量',
    f_vol        decimal(20, 8)      not null default 0 comment '交易总额，USDT计价',
    f_count      bigint(20) unsigned not null comment '交易次数',
    f_open       decimal(20, 8)      not null default 0 comment '开盘价',
    f_close      decimal(20, 8)      not null default 0 comment '收盘价',
    f_high       decimal(20, 8)      not null default 0 comment '最高价',
    f_low        decimal(20, 8)      not null default 0 comment '最低价',
    f_created_at bigint(20) unsigned not null default '0' comment 'created timestamp',
    primary key (f_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4 COMMENT ='kltc 1周K线';

create table t_market_kline_1mon_kltc
(
    f_id         bigint(20) unsigned not null auto_increment comment 'primary key ID',
    f_amount     decimal(20, 8)      not null default 0 comment '交易数量',
    f_vol        decimal(20, 8)      not null default 0 comment '交易总额，USDT计价',
    f_count      bigint(20) unsigned not null comment '交易次数',
    f_open       decimal(20, 8)      not null default 0 comment '开盘价',
    f_close      decimal(20, 8)      not null default 0 comment '收盘价',
    f_high       decimal(20, 8)      not null default 0 comment '最高价',
    f_low        decimal(20, 8)      not null default 0 comment '最低价',
    f_created_at bigint(20) unsigned not null default '0' comment 'created timestamp',
    primary key (f_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4 COMMENT ='kltc 1月K线';

-- kline tables keos
create table t_market_kline_1min_keos
(
    f_id         bigint(20) unsigned not null auto_increment comment 'primary key ID',
    f_amount     decimal(20, 8)      not null default 0 comment '交易数量',
    f_vol        decimal(20, 8)      not null default 0 comment '交易总额，USDT计价',
    f_count      bigint(20) unsigned not null comment '交易次数',
    f_open       decimal(20, 8)      not null default 0 comment '开盘价',
    f_close      decimal(20, 8)      not null default 0 comment '收盘价',
    f_high       decimal(20, 8)      not null default 0 comment '最高价',
    f_low        decimal(20, 8)      not null default 0 comment '最低价',
    f_created_at bigint(20) unsigned not null default '0' comment 'created timestamp',
    primary key (f_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4 COMMENT ='keos 1分钟K线';

create table t_market_kline_5min_keos
(
    f_id         bigint(20) unsigned not null auto_increment comment 'primary key ID',
    f_amount     decimal(20, 8)      not null default 0 comment '交易数量',
    f_vol        decimal(20, 8)      not null default 0 comment '交易总额，USDT计价',
    f_count      bigint(20) unsigned not null comment '交易次数',
    f_open       decimal(20, 8)      not null default 0 comment '开盘价',
    f_close      decimal(20, 8)      not null default 0 comment '收盘价',
    f_high       decimal(20, 8)      not null default 0 comment '最高价',
    f_low        decimal(20, 8)      not null default 0 comment '最低价',
    f_created_at bigint(20) unsigned not null default '0' comment 'created timestamp',
    primary key (f_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4 COMMENT ='keos 5分钟K线';

create table t_market_kline_15min_keos
(
    f_id         bigint(20) unsigned not null auto_increment comment 'primary key ID',
    f_amount     decimal(20, 8)      not null default 0 comment '交易数量',
    f_vol        decimal(20, 8)      not null default 0 comment '交易总额，USDT计价',
    f_count      bigint(20) unsigned not null comment '交易次数',
    f_open       decimal(20, 8)      not null default 0 comment '开盘价',
    f_close      decimal(20, 8)      not null default 0 comment '收盘价',
    f_high       decimal(20, 8)      not null default 0 comment '最高价',
    f_low        decimal(20, 8)      not null default 0 comment '最低价',
    f_created_at bigint(20) unsigned not null default '0' comment 'created timestamp',
    primary key (f_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4 COMMENT ='keos 15分钟K线';

create table t_market_kline_30min_keos
(
    f_id         bigint(20) unsigned not null auto_increment comment 'primary key ID',
    f_amount     decimal(20, 8)      not null default 0 comment '交易数量',
    f_vol        decimal(20, 8)      not null default 0 comment '交易总额，USDT计价',
    f_count      bigint(20) unsigned not null comment '交易次数',
    f_open       decimal(20, 8)      not null default 0 comment '开盘价',
    f_close      decimal(20, 8)      not null default 0 comment '收盘价',
    f_high       decimal(20, 8)      not null default 0 comment '最高价',
    f_low        decimal(20, 8)      not null default 0 comment '最低价',
    f_created_at bigint(20) unsigned not null default '0' comment 'created timestamp',
    primary key (f_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4 COMMENT ='keos 30分钟K线';

create table t_market_kline_60min_keos
(
    f_id         bigint(20) unsigned not null auto_increment comment 'primary key ID',
    f_amount     decimal(20, 8)      not null default 0 comment '交易数量',
    f_vol        decimal(20, 8)      not null default 0 comment '交易总额，USDT计价',
    f_count      bigint(20) unsigned not null comment '交易次数',
    f_open       decimal(20, 8)      not null default 0 comment '开盘价',
    f_close      decimal(20, 8)      not null default 0 comment '收盘价',
    f_high       decimal(20, 8)      not null default 0 comment '最高价',
    f_low        decimal(20, 8)      not null default 0 comment '最低价',
    f_created_at bigint(20) unsigned not null default '0' comment 'created timestamp',
    primary key (f_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4 COMMENT ='keos 60分钟K线';

create table t_market_kline_4hr_keos
(
    f_id         bigint(20) unsigned not null auto_increment comment 'primary key ID',
    f_amount     decimal(20, 8)      not null default 0 comment '交易数量',
    f_vol        decimal(20, 8)      not null default 0 comment '交易总额，USDT计价',
    f_count      bigint(20) unsigned not null comment '交易次数',
    f_open       decimal(20, 8)      not null default 0 comment '开盘价',
    f_close      decimal(20, 8)      not null default 0 comment '收盘价',
    f_high       decimal(20, 8)      not null default 0 comment '最高价',
    f_low        decimal(20, 8)      not null default 0 comment '最低价',
    f_created_at bigint(20) unsigned not null default '0' comment 'created timestamp',
    primary key (f_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4 COMMENT ='keos 4小时K线';

create table t_market_kline_1day_keos
(
    f_id         bigint(20) unsigned not null auto_increment comment 'primary key ID',
    f_amount     decimal(20, 8)      not null default 0 comment '交易数量',
    f_vol        decimal(20, 8)      not null default 0 comment '交易总额，USDT计价',
    f_count      bigint(20) unsigned not null comment '交易次数',
    f_open       decimal(20, 8)      not null default 0 comment '开盘价',
    f_close      decimal(20, 8)      not null default 0 comment '收盘价',
    f_high       decimal(20, 8)      not null default 0 comment '最高价',
    f_low        decimal(20, 8)      not null default 0 comment '最低价',
    f_created_at bigint(20) unsigned not null default '0' comment 'created timestamp',
    primary key (f_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4 COMMENT ='keos 1天K线';

create table t_market_kline_1w_keos
(
    f_id         bigint(20) unsigned not null auto_increment comment 'primary key ID',
    f_amount     decimal(20, 8)      not null default 0 comment '交易数量',
    f_vol        decimal(20, 8)      not null default 0 comment '交易总额，USDT计价',
    f_count      bigint(20) unsigned not null comment '交易次数',
    f_open       decimal(20, 8)      not null default 0 comment '开盘价',
    f_close      decimal(20, 8)      not null default 0 comment '收盘价',
    f_high       decimal(20, 8)      not null default 0 comment '最高价',
    f_low        decimal(20, 8)      not null default 0 comment '最低价',
    f_created_at bigint(20) unsigned not null default '0' comment 'created timestamp',
    primary key (f_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4 COMMENT ='keos 1周K线';

create table t_market_kline_1mon_keos
(
    f_id         bigint(20) unsigned not null auto_increment comment 'primary key ID',
    f_amount     decimal(20, 8)      not null default 0 comment '交易数量',
    f_vol        decimal(20, 8)      not null default 0 comment '交易总额，USDT计价',
    f_count      bigint(20) unsigned not null comment '交易次数',
    f_open       decimal(20, 8)      not null default 0 comment '开盘价',
    f_close      decimal(20, 8)      not null default 0 comment '收盘价',
    f_high       decimal(20, 8)      not null default 0 comment '最高价',
    f_low        decimal(20, 8)      not null default 0 comment '最低价',
    f_created_at bigint(20) unsigned not null default '0' comment 'created timestamp',
    primary key (f_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4 COMMENT ='keos 1月K线';

---------------------
--  FX

-- kline tables usdcny
create table t_market_kline_1min_usdcny
(
    f_id         bigint(20) unsigned not null auto_increment comment 'primary key ID',
    f_amount     decimal(20, 8)      not null default 0 comment '交易数量',
    f_vol        decimal(20, 8)      not null default 0 comment '交易总额，USD计价',
    f_count      bigint(20) unsigned not null comment '交易次数',
    f_open       decimal(20, 8)      not null default 0 comment '开盘价',
    f_close      decimal(20, 8)      not null default 0 comment '收盘价',
    f_high       decimal(20, 8)      not null default 0 comment '最高价',
    f_low        decimal(20, 8)      not null default 0 comment '最低价',
    f_created_at bigint(20) unsigned not null default '0' comment 'created timestamp',
    primary key (f_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4 COMMENT ='usdcny 1分钟K线';

create table t_market_kline_5min_usdcny
(
    f_id         bigint(20) unsigned not null auto_increment comment 'primary key ID',
    f_amount     decimal(20, 8)      not null default 0 comment '交易数量',
    f_vol        decimal(20, 8)      not null default 0 comment '交易总额，USDT计价',
    f_count      bigint(20) unsigned not null comment '交易次数',
    f_open       decimal(20, 8)      not null default 0 comment '开盘价',
    f_close      decimal(20, 8)      not null default 0 comment '收盘价',
    f_high       decimal(20, 8)      not null default 0 comment '最高价',
    f_low        decimal(20, 8)      not null default 0 comment '最低价',
    f_created_at bigint(20) unsigned not null default '0' comment 'created timestamp',
    primary key (f_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4 COMMENT ='usdcny 5分钟K线';

create table t_market_kline_15min_usdcny
(
    f_id         bigint(20) unsigned not null auto_increment comment 'primary key ID',
    f_amount     decimal(20, 8)      not null default 0 comment '交易数量',
    f_vol        decimal(20, 8)      not null default 0 comment '交易总额，USDT计价',
    f_count      bigint(20) unsigned not null comment '交易次数',
    f_open       decimal(20, 8)      not null default 0 comment '开盘价',
    f_close      decimal(20, 8)      not null default 0 comment '收盘价',
    f_high       decimal(20, 8)      not null default 0 comment '最高价',
    f_low        decimal(20, 8)      not null default 0 comment '最低价',
    f_created_at bigint(20) unsigned not null default '0' comment 'created timestamp',
    primary key (f_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4 COMMENT ='usdcny 15分钟K线';

create table t_market_kline_30min_usdcny
(
    f_id         bigint(20) unsigned not null auto_increment comment 'primary key ID',
    f_amount     decimal(20, 8)      not null default 0 comment '交易数量',
    f_vol        decimal(20, 8)      not null default 0 comment '交易总额，USDT计价',
    f_count      bigint(20) unsigned not null comment '交易次数',
    f_open       decimal(20, 8)      not null default 0 comment '开盘价',
    f_close      decimal(20, 8)      not null default 0 comment '收盘价',
    f_high       decimal(20, 8)      not null default 0 comment '最高价',
    f_low        decimal(20, 8)      not null default 0 comment '最低价',
    f_created_at bigint(20) unsigned not null default '0' comment 'created timestamp',
    primary key (f_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4 COMMENT ='usdcny 30分钟K线';

create table t_market_kline_60min_usdcny
(
    f_id         bigint(20) unsigned not null auto_increment comment 'primary key ID',
    f_amount     decimal(20, 8)      not null default 0 comment '交易数量',
    f_vol        decimal(20, 8)      not null default 0 comment '交易总额，USDT计价',
    f_count      bigint(20) unsigned not null comment '交易次数',
    f_open       decimal(20, 8)      not null default 0 comment '开盘价',
    f_close      decimal(20, 8)      not null default 0 comment '收盘价',
    f_high       decimal(20, 8)      not null default 0 comment '最高价',
    f_low        decimal(20, 8)      not null default 0 comment '最低价',
    f_created_at bigint(20) unsigned not null default '0' comment 'created timestamp',
    primary key (f_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4 COMMENT ='usdcny 60分钟K线';

create table t_market_kline_4hr_usdcny
(
    f_id         bigint(20) unsigned not null auto_increment comment 'primary key ID',
    f_amount     decimal(20, 8)      not null default 0 comment '交易数量',
    f_vol        decimal(20, 8)      not null default 0 comment '交易总额，USDT计价',
    f_count      bigint(20) unsigned not null comment '交易次数',
    f_open       decimal(20, 8)      not null default 0 comment '开盘价',
    f_close      decimal(20, 8)      not null default 0 comment '收盘价',
    f_high       decimal(20, 8)      not null default 0 comment '最高价',
    f_low        decimal(20, 8)      not null default 0 comment '最低价',
    f_created_at bigint(20) unsigned not null default '0' comment 'created timestamp',
    primary key (f_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4 COMMENT ='usdcny 4小时K线';

create table t_market_kline_1day_usdcny
(
    f_id         bigint(20) unsigned not null auto_increment comment 'primary key ID',
    f_amount     decimal(20, 8)      not null default 0 comment '交易数量',
    f_vol        decimal(20, 8)      not null default 0 comment '交易总额，USDT计价',
    f_count      bigint(20) unsigned not null comment '交易次数',
    f_open       decimal(20, 8)      not null default 0 comment '开盘价',
    f_close      decimal(20, 8)      not null default 0 comment '收盘价',
    f_high       decimal(20, 8)      not null default 0 comment '最高价',
    f_low        decimal(20, 8)      not null default 0 comment '最低价',
    f_created_at bigint(20) unsigned not null default '0' comment 'created timestamp',
    primary key (f_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4 COMMENT ='usdcny 1天K线';

create table t_market_kline_1w_usdcny
(
    f_id         bigint(20) unsigned not null auto_increment comment 'primary key ID',
    f_amount     decimal(20, 8)      not null default 0 comment '交易数量',
    f_vol        decimal(20, 8)      not null default 0 comment '交易总额，USDT计价',
    f_count      bigint(20) unsigned not null comment '交易次数',
    f_open       decimal(20, 8)      not null default 0 comment '开盘价',
    f_close      decimal(20, 8)      not null default 0 comment '收盘价',
    f_high       decimal(20, 8)      not null default 0 comment '最高价',
    f_low        decimal(20, 8)      not null default 0 comment '最低价',
    f_created_at bigint(20) unsigned not null default '0' comment 'created timestamp',
    primary key (f_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4 COMMENT ='usdcny 1周K线';

create table t_market_kline_1mon_usdcny
(
    f_id         bigint(20) unsigned not null auto_increment comment 'primary key ID',
    f_amount     decimal(20, 8)      not null default 0 comment '交易数量',
    f_vol        decimal(20, 8)      not null default 0 comment '交易总额，USDT计价',
    f_count      bigint(20) unsigned not null comment '交易次数',
    f_open       decimal(20, 8)      not null default 0 comment '开盘价',
    f_close      decimal(20, 8)      not null default 0 comment '收盘价',
    f_high       decimal(20, 8)      not null default 0 comment '最高价',
    f_low        decimal(20, 8)      not null default 0 comment '最低价',
    f_created_at bigint(20) unsigned not null default '0' comment 'created timestamp',
    primary key (f_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4 COMMENT ='usdcny 1月K线';