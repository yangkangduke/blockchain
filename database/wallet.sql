create table t_wallet_location
(
    f_id         bigint(20) unsigned not null auto_increment comment 'primary key ID',
    f_address    varchar(80)     not null default '' comment '钱包地址',
    f_location   varchar(128)    not null default '' comment '钱包文件名称',
    f_password   varchar(256)    not null default '' comment '加密密码',
    f_created_at bigint(20) unsigned not null default '0' comment 'created timestamp',
    primary key (f_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4 COMMENT ='钱包地址与钱包文件名称的管理';