create table t_symbol(
    f_id         bigint(20) unsigned not null auto_increment comment 'primary key ID',
    f_base_currency     varchar(20)         not null default '' comment 'base currency',
    f_quote_currency     varchar(20)         not null default '' comment 'quote currency',
    f_status     int(2)              not null default 0 comment '1 上线， 0 已经下线',
    f_exchange     int(2)         not null default 0 comment '1 可以交易， 0 停止交易',
    f_created_at bigint(20) unsigned not null default '0' comment 'created timestamp',
    f_updated_at bigint(20) unsigned not null default '0' comment 'update timestamp',
    primary key (f_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4 COMMENT ='交易对';

  insert into `t_symbol` (f_base_currency,f_quote_currency,f_status,f_exchange) values ('kBTC','kUSD',1,1);
  insert into `t_symbol` (f_base_currency,f_quote_currency,f_status,f_exchange) values ('kETH','kUSD',1,1);
  insert into `t_symbol` (f_base_currency,f_quote_currency,f_status,f_exchange) values ('kBCH','kUSD',1,1);
  insert into `t_symbol` (f_base_currency,f_quote_currency,f_status,f_exchange) values ('kLTC','kUSD',1,1);
  insert into `t_symbol` (f_base_currency,f_quote_currency,f_status,f_exchange) values ('kEOS','kUSD',1,1);


create table t_currency
(
    f_id                bigint(20) unsigned not null auto_increment comment 'primary key ID',
    f_name              varchar(20)         not null default '' comment 'base currency',
    f_display_name      varchar(200)         not null default '' comment 'display currency',
    f_code              int(2)         not null default '' comment 'quote currency',
    f_status            int(2)              not null default 0 comment '1 上线， 0 已经下线',
    f_exchange          int(2)         not null default 0 comment '1 可以交易， 0 停止交易',
    f_created_at        bigint(20) unsigned not null default '0' comment 'created timestamp',
    f_updated_at        bigint(20) unsigned not null default '0' comment 'update timestamp',
    primary key (f_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4 COMMENT ='币种信息';