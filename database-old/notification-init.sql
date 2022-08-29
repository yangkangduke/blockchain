drop table t_msg_template;
drop table t_msg_mail;
drop table t_msg_sms;

create table `t_msg_template`
(
    f_id              bigint unsigned auto_increment comment 'primary key',
    f_create_time     bigint unsigned not null default 0 comment 'create time',
    f_update_time     bigint unsigned not null default 0 comment 'update time',
    f_version         bigint unsigned not null default 0 comment 'version',
    f_name            varchar(100)     not null default '' comment '模版名称',
    f_subject         varchar(100)    not null default '' comment '标题',
    f_content         varchar(300)    not null default '' comment '内容',
    f_is_html         int not null default 0  comment '0文本 1 html',
    f_channel         int not null default 0  comment '0邮件 1 短信',
    f_msg_type_code   int not null default 0 comment '业务类型代码',
    f_msg_type_name   varchar(100) not null default '' comment '业务类型名称',
    f_type            int not null default 0 comment '1普通模版，2外置模版',
    f_language        varchar(10) not null default '' comment '1中文，2英文',
    f_status          int not null default 0  comment '0未生效，1已使用，2已弃用',
    PRIMARY KEY (f_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COMMENT = 't_msg_template' AUTO_INCREMENT = 1;

create table `t_msg_mail`
(
    f_id              bigint unsigned auto_increment comment 'primary key',
    f_create_time     bigint unsigned not null default 0 comment 'create time',
    f_update_time     bigint unsigned not null default 0 comment 'update time',
    f_version         bigint unsigned not null default 0 comment 'version',
    f_user_id         bigint unsigned not null default 0  comment 'userId',
    f_template_id     bigint unsigned not null default 0  comment '模版编号',
    f_subject         varchar(200)    not null default '' comment '标题',
    f_content         varchar(500)    not null default '' comment '内容',
    f_email_to        varchar(200)    not null default '' comment '收件人',
    f_cc              varchar(200)    not null default '' comment '抄送',
    f_bcc             varchar(200)    not null default '' comment '密抄',
    f_is_html         int not null default 0  comment '0文本 1 html',
    f_status          int not null default 0  comment '0未发送 1已发送 2已失败',
    PRIMARY KEY (f_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COMMENT = 't_msg_mail' AUTO_INCREMENT = 1;

create table `t_msg_sms`
(
    f_id              bigint unsigned auto_increment comment 'primary key',
    f_create_time     bigint unsigned not null default 0 comment 'create time',
    f_update_time     bigint unsigned not null default 0 comment 'update time',
    f_version         bigint unsigned not null default 0 comment 'version',
    f_user_id         bigint unsigned not null default 0  comment 'userId',
    f_template_id     bigint unsigned not null default 0  comment '模版编号',
    f_content         varchar(300)    not null default '' comment '内容',
    f_phone           varchar(300)    not null default '' comment '手机号',
    f_status          int not null default 0  comment '0未发送，1已发送，2发送失败',
    PRIMARY KEY (f_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COMMENT = 't_msg_sms' AUTO_INCREMENT = 1;

insert into t_msg_template(f_create_time, f_update_time, f_version, f_name,f_subject, f_content,f_is_html,f_channel,f_msg_type_code,f_msg_type_name,f_type,f_language,f_status)
values (1598697904067,1598697904067, 1, '质押爆仓预警','质押爆仓提醒', '您的质押率为{ratio}',0,1,27,'STAKING_EXPLOSION_REMINDER',1, 'zh-cn',1);
insert into t_msg_template(f_create_time, f_update_time, f_version, f_name,f_subject, f_content,f_is_html,f_channel,f_msg_type_code,f_msg_type_name,f_type,f_language,f_status)
values (1598697904067,1598697904067, 1, 'StakingExplosioinReminder','Staking Explosion Reminder', 'Your current staking ratio is {ratio}',0,1,27,'STAKING_EXPLOSION_REMINDER',1, 'en-us',1);
insert into t_msg_template(f_create_time, f_update_time, f_version, f_name,f_subject, f_content,f_is_html,f_channel,f_msg_type_code,f_msg_type_name,f_type,f_language,f_status)
values (1598697904067,1598697904067, 1, '质押爆仓通知','质押爆仓通知', '您的质押率不足，质押账户已爆仓',0,1,28,'STAKING_EXPLOSION_NOTICE',1, 'zh-cn',1);
insert into t_msg_template(f_create_time, f_update_time, f_version, f_name,f_subject, f_content,f_is_html,f_channel,f_msg_type_code,f_msg_type_name,f_type,f_language,f_status)
values (1598697904067,1598697904067, 1, 'StakingExplosioinNotice','Staking Explosion Notice', 'Your staking asset is already sold out due to insufficient staking ratio.',0,1,28,'STAKING_EXPLOSION_NOTICE',1, 'en-us',1);

insert into t_msg_template(f_create_time, f_update_time, f_version, f_name,f_subject, f_content,f_is_html,f_channel,f_msg_type_code,f_msg_type_name,f_type,f_language,f_status)
values (1598697904067,1598697904067, 1, '负债爆仓预警','负债爆仓提醒', '您的负债质押率为{ratio}',0,1,29,'TRADING_EXPLOSION_REMINDER',1, 'zh-cn',1);
insert into t_msg_template(f_create_time, f_update_time, f_version, f_name,f_subject, f_content,f_is_html,f_channel,f_msg_type_code,f_msg_type_name,f_type,f_language,f_status)
values (1598697904067,1598697904067, 1, 'DebtExplosioinReminder','Debt Explosion Reminder', 'Your current debt ratio is {ratio}',0,1,29,'TRADING_EXPLOSION_REMINDER',1, 'en-us',1);
insert into t_msg_template(f_create_time, f_update_time, f_version, f_name,f_subject, f_content,f_is_html,f_channel,f_msg_type_code,f_msg_type_name,f_type,f_language,f_status)
values (1598697904067,1598697904067, 1, '负债爆仓通知','负债爆仓通知', '您的负债率不足，交易账户已爆仓',0,1,30,'TRADING_EXPLOSION_NOTICE',1, 'zh-cn',1);
insert into t_msg_template(f_create_time, f_update_time, f_version, f_name,f_subject, f_content,f_is_html,f_channel,f_msg_type_code,f_msg_type_name,f_type,f_language,f_status)
values (1598697904067,1598697904067, 1, 'DebtExplosioinNotice','Debt Explosion Notice', 'Your asset in trading account is already sold out due to insufficient debt ratio.',0,1,30,'TRADING_EXPLOSION_NOTICE',1, 'en-us',1);

insert into t_msg_template(f_create_time, f_update_time, f_version, f_name,f_subject, f_content,f_is_html,f_channel,f_msg_type_code,f_msg_type_name,f_type,f_language,f_status)
values (1598697904067,1598697904067, 1, '质押爆仓预警','705317', '您的质押率为{1}',0,2,27,'STAKING_EXPLOSION_REMINDER', 1, 'zh-cn',1);
insert into t_msg_template(f_create_time, f_update_time, f_version, f_name,f_subject, f_content,f_is_html,f_channel,f_msg_type_code,f_msg_type_name,f_type,f_language,f_status)
values (1598697904067,1598697904067, 1, '质押爆仓通知','706028', '您的质押率不足，质押账户已爆仓',0,2,28,'STAKING_EXPLOSION_NOTICE',1, 'zh-cn',1);

insert into t_msg_template(f_create_time, f_update_time, f_version, f_name,f_subject, f_content,f_is_html,f_channel,f_msg_type_code,f_msg_type_name,f_type,f_language,f_status)
values (1598697904067,1598697904067, 1, '负债爆仓预警','705315', '您的负债质押率为{1}',0,2,29,'TRADING_EXPLOSION_REMINDER',1, 'zh-cn',1);
insert into t_msg_template(f_create_time, f_update_time, f_version, f_name,f_subject, f_content,f_is_html,f_channel,f_msg_type_code,f_msg_type_name,f_type,f_language,f_status)
values (1598697904067,1598697904067, 1, '负债爆仓通知','706030', '您的负债率不足，交易账户已爆仓',0,2,30,'TRADING_EXPLOSION_NOTICE',1, 'zh-cn',1);


insert into t_msg_template(f_create_time, f_update_time, f_version, f_name,f_subject, f_content,f_is_html,f_channel,f_msg_type_code,f_msg_type_name,f_type,f_language,f_status)
values (1598697904067,1598697904067, 1, '登录验证码','登录验证码', '登录验证码：{authCode}，您正在尝试登录，请于10分钟内填写，如非本人操作，请忽略本消息',0,1,1,'LOGIN', 1, 'zh-cn',1);
insert into t_msg_template(f_create_time, f_update_time, f_version, f_name,f_subject, f_content,f_is_html,f_channel,f_msg_type_code,f_msg_type_name,f_type,f_language,f_status)
values (1598697904067,1598697904067, 1, '关闭GA安全项验证码','关闭GA安全项验证码', '安全项设置验证码：{authCode}，您正在尝试关闭GA验证，请于10分钟内填写，如非本人操作，请忽略本消息。',0,1,20,'VERIFY_SETTING_POLICY_DISABLE_GA_POLICY', 1, 'zh-cn',1);
insert into t_msg_template(f_create_time, f_update_time, f_version, f_name,f_subject, f_content,f_is_html,f_channel,f_msg_type_code,f_msg_type_name,f_type,f_language,f_status)
values (1598697904067,1598697904067, 1, '提币验证码','提币验证码', '提货验证码：{authCode}，您正在尝试提货，请于10分钟内填写，如非本人操作，请忽略本消息。',0,1,21,'VERIFY_SETTING_POLICY_WITHDRAW', 1, 'zh-cn',1);
insert into t_msg_template(f_create_time, f_update_time, f_version, f_name,f_subject, f_content,f_is_html,f_channel,f_msg_type_code,f_msg_type_name,f_type,f_language,f_status)
values (1598697904067,1598697904067, 1, '登录验证码','711172', '登录验证码：{1}，您正在尝试登录，请于10分钟内填写，如非本人操作，请忽略本短信。',0,2,1,'LOGIN',1, 'zh-cn',1);
insert into t_msg_template(f_create_time, f_update_time, f_version, f_name,f_subject, f_content,f_is_html,f_channel,f_msg_type_code,f_msg_type_name,f_type,f_language,f_status)
values (1598697904067,1598697904067, 1, '关闭GA安全项验证码','711174', '安全项设置验证码：{1}，您正在尝试关闭GA验证，请于10分钟内填写，如非本人操作，请忽略本短信。',0,2,20,'VERIFY_SETTING_POLICY_DISABLE_GA_POLICY',1, 'zh-cn',1);
insert into t_msg_template(f_create_time, f_update_time, f_version, f_name,f_subject, f_content,f_is_html,f_channel,f_msg_type_code,f_msg_type_name,f_type,f_language,f_status)
values (1598697904067,1598697904067, 1, '提币验证码','711211', '提货验证码：{1}，您正在尝试提货，请于10分钟内填写，如非本人操作，请忽略本短信。',0,2,21,'VERIFY_SETTING_POLICY_WITHDRAW',1, 'zh-cn',1);

insert into t_msg_template(f_create_time, f_update_time, f_version, f_name,f_subject, f_content,f_is_html,f_channel,f_msg_type_code,f_msg_type_name,f_type,f_language,f_status)
values (1598697904067,1598697904067, 1, 'Login Auth code','Login Auth code', 'Login auth code:{authCode}',0,1,1,'LOGIN',1, 'en-us',1);
insert into t_msg_template(f_create_time, f_update_time, f_version, f_name,f_subject, f_content,f_is_html,f_channel,f_msg_type_code,f_msg_type_name,f_type,f_language,f_status)
values (1598697904067,1598697904067, 1, 'Close GA Auth Code','Close GA Auth Code', 'Glose GA auth code:{authCode}',0,1,20,'VERIFY_SETTING_POLICY_DISABLE_GA_POLICY',1, 'en-us',1);
insert into t_msg_template(f_create_time, f_update_time, f_version, f_name,f_subject, f_content,f_is_html,f_channel,f_msg_type_code,f_msg_type_name,f_type,f_language,f_status)
values (1598697904067,1598697904067, 1, 'Withdraw Auth Code','Withdraw Auth Code', 'Withdraw auth code:{authCode}',0,1,21,'VERIFY_SETTING_POLICY_WITHDRAW',1, 'en-us',1);
-- VERIFY_SETTING_POLICY_WITHDRAW

insert into t_msg_template(f_create_time, f_update_time, f_version, f_name,f_subject, f_content,f_is_html,f_channel,f_msg_type_code,f_msg_type_name,f_type,f_language,f_status)
values (1598697904067,1598697904067, 1, '注册验证码','注册验证码', '注册验证码：{authCode}，您正在尝试注册，请于10分钟内填写，如非本人操作，请忽略本消息',0,1,3,'REGISTER', 1, 'zh-cn',1);
insert into t_msg_template(f_create_time, f_update_time, f_version, f_name,f_subject, f_content,f_is_html,f_channel,f_msg_type_code,f_msg_type_name,f_type,f_language,f_status)
values (1598697904067,1598697904067, 1, '注册验证码','712272', '注册验证码：{1}，您正在尝试注册，请于10分钟内填写，如非本人操作，请忽略本短信。',0,2,3,'REGISTER',1, 'zh-cn',1);
insert into t_msg_template(f_create_time, f_update_time, f_version, f_name,f_subject, f_content,f_is_html,f_channel,f_msg_type_code,f_msg_type_name,f_type,f_language,f_status)
values (1598697904067,1598697904067, 1, 'Register Auth Code','Register Auth Code', 'Register auth code:{authCode}',0,1,3,'REGISTER',1, 'en-us',1);



insert into t_msg_template(f_create_time, f_update_time, f_version, f_name,f_subject, f_content,f_is_html,f_channel,f_msg_type_code,f_msg_type_name,f_type,f_language,f_status)
values (1598697904067,1598697904067, 1, 'Login Auth Code','712354', 'Login authorization code:{1}. You are trying to login. please fill in within 10 mins',0,2,1,'LOGIN',1, 'en-us',1);
insert into t_msg_template(f_create_time, f_update_time, f_version, f_name,f_subject, f_content,f_is_html,f_channel,f_msg_type_code,f_msg_type_name,f_type,f_language,f_status)
values (1598697904067,1598697904067, 1, 'Registration Auth Code','712353', 'Registration authorization code:{1}. You are trying to register. please fill in within 10 mins',0,2,3,'REGISTER',1, 'en-us',1);
insert into t_msg_template(f_create_time, f_update_time, f_version, f_name,f_subject, f_content,f_is_html,f_channel,f_msg_type_code,f_msg_type_name,f_type,f_language,f_status)
values (1598697904067,1598697904067, 1, 'Security Setting-Close GA','712355', 'Security setting authorization code:{1}. You are trying to close GA. please fill in within 10 mins',0,2,20,'VERIFY_SETTING_POLICY_DISABLE_GA_POLICY',1, 'en-us',1);
insert into t_msg_template(f_create_time, f_update_time, f_version, f_name,f_subject, f_content,f_is_html,f_channel,f_msg_type_code,f_msg_type_name,f_type,f_language,f_status)
values (1598697904067,1598697904067, 1, 'Pickup Auth Code','712357', 'Pickup authorization code:{1}. You are trying to Pickup. please fill in within 10 mins',0,2,21,'VERIFY_SETTING_POLICY_WITHDRAW',1, 'en-us',1);
