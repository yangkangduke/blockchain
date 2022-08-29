create table t_order
(
    f_id         bigint(20) unsigned not null auto_increment comment 'primary key ID',
    f_order_id   bigint(20) unsigned not null default 0 comment '订单ID',
    f_user_id    bigint(20) unsigned not null default 0 comment 'User ID',
    f_symbol     varchar(50)         not null default '' comment '交易对',
    f_pay_amount     decimal(20, 8)      not null default 0 comment '支付数量',
    f_get_amount     decimal(20, 8)      not null default 0 comment '获取数量',
    f_price      decimal(20, 8)      not null default 0 comment '金额',
    f_fee      decimal(20, 8)      not null default 0 comment 'fee',
    f_direct     varchar(50)         not null default '' comment 'buy, sell',
    f_status     int(2)              not null default 1 comment '状态，0-失败，1-成功',
    f_created_at bigint(20) unsigned not null default '0' comment 'created timestamp',
    primary key (f_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4 COMMENT ='Order table';

alter table `t_order` add f_type int(2) not null default 0 comment '类型，0-正常订单，1-止盈单 2- 止损单';

ALTER TABLE `t_order` ADD INDEX t_order_time_ind (`f_created_at` );
ALTER TABLE `t_order` ADD INDEX t_order_time_symbol (`f_symbol`);

create table t_user_position
(
    f_id         bigint(20) unsigned not null auto_increment comment 'primary key ID',
    f_user_id    bigint(20) unsigned not null default 0 comment 'User ID',
    f_version    bigint(20) unsigned not null default 0 comment 'version',
    f_symbol     varchar(50)         not null default '' comment '交易对',
    f_position  decimal(20, 8)      not null default 0 comment '当前仓位',
    f_avg_price  decimal(20, 8)      not null default 0 comment '当前仓位VVWP',
    f_category    int(2)              not null default 0 comment '交易资产的种类',
    f_created_at bigint(20) unsigned not null default '0' comment 'created timestamp',
    f_updated_at bigint(20) unsigned not null default '0' comment 'updated timestamp',
    primary key (f_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4 COMMENT ='用户仓位';

create table t_order_stop_limit
(
    f_id         bigint(20) unsigned not null auto_increment comment 'primary key ID',
    f_order_id   bigint(20) unsigned not null default 0 comment '订单ID',
    f_ori_order_id   bigint(20) unsigned not null default 0 comment '原始订单 id',
    f_user_id    bigint(20) unsigned not null default 0 comment 'User ID',
    f_type  int(2)  not null default 0 comment '1- 止盈， 2 止损',
    f_base_currency     varchar(20)         not null default '' comment '基础currency',
    f_quote_currency     varchar(20)         not null default '' comment '询价currency',
    f_trigger_price      decimal(20, 8)      not null default 0 comment '触发金额',
    f_fee      decimal(20, 8)      not null default 0 comment '手续费',
    f_amount      decimal(20, 8)      not null default 0 comment '总数',
    f_status     int(2)              not null default 2 comment '状态，0-失败，1-成功, 2- 未触发，3- 已经撤销',
    f_direct     varchar(20)         not null default '' comment 'buy, sell',
    f_created_at bigint(20) unsigned not null default '0' comment 'created timestamp',
    f_updated_at bigint(20) unsigned not null default '0' comment 'updated timestamp',
    f_triggered_at bigint(20) unsigned not null default '0' comment '触发 timestamp',
    primary key (f_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4 COMMENT ='止盈止损';


create table t_order_setting
  (
      f_id         bigint(20) unsigned not null auto_increment comment 'primary key ID',
      f_key     varchar(50)         not null default '' comment 'key',
      f_type     varchar(50)         not null default '' comment 'type',
      f_value    decimal(20, 8)     not null default 0 comment 'value',
      f_created_at bigint(20) unsigned not null default '0' comment 'created timestamp',
      f_updated_at bigint(20) unsigned not null default '0' comment 'updated timestamp',
      primary key (f_id),
      unique key u_trade_key_type(f_key,f_type)
  ) ENGINE = InnoDB
    DEFAULT CHARSET = UTF8MB4 COMMENT ='Order Rules table';


insert into `t_order_setting` (f_key, f_type, f_value) values('min-vol-order','kBTC',0.0001);
insert into `t_order_setting` (f_key, f_type, f_value) values('min-vol-order','kETH',0.0001);
insert into `t_order_setting` (f_key, f_type, f_value) values('min-vol-order','kLTC',0.001);
insert into `t_order_setting` (f_key, f_type, f_value) values('min-vol-order','kUSD',0.01);
insert into `t_order_setting` (f_key, f_type, f_value) values('min-vol-order','kEOS',0.0001);
insert into `t_order_setting` (f_key, f_type, f_value) values('min-vol-order','kBCH',0.0001);


insert into `t_order_setting` (f_key, f_type, f_value) values('max-platform-position','kBTC',100);
insert into `t_order_setting` (f_key, f_type, f_value) values('max-platform-position','kETH',1000);
insert into `t_order_setting` (f_key, f_type, f_value) values('max-platform-position','kLTC',10000);
insert into `t_order_setting` (f_key, f_type, f_value) values('max-platform-position','kUSD',2000000);
insert into `t_order_setting` (f_key, f_type, f_value) values('max-platform-position','kEOS',3000);
insert into `t_order_setting` (f_key, f_type, f_value) values('max-platform-position','kBCH',20000);



insert into `t_order_setting` (f_key, f_type, f_value) values('max-user-position','kBTC',100);
insert into `t_order_setting` (f_key, f_type, f_value) values('max-user-position','kETH',1000);
insert into `t_order_setting` (f_key, f_type, f_value) values('max-user-position','kLTC',10000);
insert into `t_order_setting` (f_key, f_type, f_value) values('max-user-position','kUSD',2000000);
insert into `t_order_setting` (f_key, f_type, f_value) values('max-user-position','kEOS',3000);
insert into `t_order_setting` (f_key, f_type, f_value) values('max-user-position','kBCH',20000);

insert into `t_order_setting` (f_key, f_type, f_value) values('place-order-long','kBTC',1);
insert into `t_order_setting` (f_key, f_type, f_value) values('place-order-long','kETH',1);
insert into `t_order_setting` (f_key, f_type, f_value) values('place-order-long','kLTC',1);
insert into `t_order_setting` (f_key, f_type, f_value) values('place-order-long','kUSD',1);
insert into `t_order_setting` (f_key, f_type, f_value) values('place-order-long','kEOS',1);
insert into `t_order_setting` (f_key, f_type, f_value) values('place-order-long','kBCH',1);


insert into `t_order_setting` (f_key, f_type, f_value) values('place-order-put','kBTC',1);
insert into `t_order_setting` (f_key, f_type, f_value) values('place-order-put','kETH',1);
insert into `t_order_setting` (f_key, f_type, f_value) values('place-order-put','kLTC',1);
insert into `t_order_setting` (f_key, f_type, f_value) values('place-order-put','kUSD',1);
insert into `t_order_setting` (f_key, f_type, f_value) values('place-order-put','kEOS',1);
insert into `t_order_setting` (f_key, f_type, f_value) values('place-order-put','kBCH',1);