create table `t_transaction`
(
    f_id              bigint unsigned auto_increment comment 'primary key',
    f_create_time     bigint unsigned not null default 0 comment 'create time',
    f_account_type    int not null default 0  comment '',
    f_user_id         bigint unsigned not null default 0  comment 'userId',
    f_batch_id         bigint unsigned not null default 0  comment 'bath operation id',
    f_ref_id         bigint unsigned not null default 0  comment 'reference  id',
    f_currency        varchar(12)     not null default '' comment '',
    f_change_amount         decimal(36,18)  not null default 0  comment '',
    PRIMARY KEY (f_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COMMENT ='transaction_minter' AUTO_INCREMENT = 10000;
