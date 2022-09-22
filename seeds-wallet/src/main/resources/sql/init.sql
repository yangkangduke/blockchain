create table t_hot_wallet
(
    f_id         bigint(20) unsigned not null auto_increment comment 'primary key ID',
    f_address    varchar(80)         not null default '' comment '钱包地址',
    f_file_json  varchar(1024)       not null default '' comment '钱包文件json',
    f_created_at bigint(20) unsigned not null default '0' comment 'created timestamp',
    primary key (f_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4 COMMENT ='热钱包管理';

create unique index t_hot_wallet_index
    on t_hot_wallet (f_address);
