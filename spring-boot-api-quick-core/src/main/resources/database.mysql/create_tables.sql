--  个人信息表
drop table if exists person;
create table person (
id varchar(50) not null,
create_time timestamp default current_timestamp not null,
last_update_time timestamp  default current_timestamp not null,
version_number decimal default 1 not null,
person_name varchar(30) not null,
gender char(1) not null,
birth_time date not null,
identity_number varchar(30) not null,
marital_status char(2) not null,
spouse_name varchar(30) ,
primary key (id)
);

alter table person comment '人员信息表';


 --  异常信息日志表 (只记录有异常信息的请求，包括业务异常)
drop table if exists error_log;
create table error_log (
id varchar(50) not null,
request_url varchar(200) not null,
token varchar(32) ,
request_params varchar(4000) ,
create_time timestamp default current_timestamp not null,
error_info text,
primary key (id)
);

alter table error_log comment '异常信息日志表';
alter table error_log modify id varchar(32) not null comment '主键';


-- 字典表
 drop table if exists dicts;
create table  dicts(
id varchar(50) not null,
itemcode varchar(32) not null,
code varchar(6) not null,
name varchar(32) not null,
primary key (id)
);

-- 用户表
 drop table if exists sys_user;
create table sys_user (
id varchar(50) not null,
username varchar(32) not null,
password varchar(32) not null,
primary key (id)
);



  --  接口请求日志表 (包含所有的请求接口日志)
drop table if exists token_create;
create table token_create (
id varchar(32) not null,
user_id varchar(32) not null,
user_name varchar(32) not null,
token varchar(32) ,
create_time timestamp default current_timestamp not null,
primary key (id)
);

alter table token_create comment 'token获取记录表';
alter table token_create modify id varchar(32) not null comment '主键';



 --  接口请求日志表 (包含所有的请求接口日志)
drop table if exists api_log;
create table api_log (
id varchar(50) not null,
request_ip varchar(100) not null,
request_url varchar(200) not null,
request_params varchar(4000),
token varchar(32) ,
create_time timestamp default current_timestamp not null,
primary key (id)
);



-- 存储过程日志表
DROP TABLE IF EXISTS `procedure_log`;
CREATE TABLE `procedure_log`  (
  `procedure_search_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '唯一標識',
  `object_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '数据库对象名称（表，视图，存储过程）',
  `message` varchar(4000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '日志信息',
  `creation_time` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '創建日期',
  INDEX `IDX_report_log_report_search_id`(`procedure_search_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '存储过程日志表';


-- 系统日志表
DROP TABLE IF EXISTS `system_log`;
CREATE TABLE `system_log`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键',
  `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '类型',
  `message` varchar(4000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '日志信息',
  `creation_time` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '创建日期',
  primary key (id)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '系统日志表';


-- 系统配置表
DROP TABLE IF EXISTS `config`;
CREATE TABLE `config`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键',
  `configType` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '配置类型',
  `configTypeName` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '配置类型说明',
  `propName1` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '配置说明1',
  `propVal1` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '配置数据1',
  `propName2` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '配置说明2',
  `propVal2` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '配置数据2',
  `propName3` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '配置说明3',
  `propVal3` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '配置数据3',
  `propName4` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '配置说明4',
  `propVal4` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '配置数据4',
  primary key (id)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '系统配置表';


-- 用户配置
drop table if exists user_config;
create table user_config (
id varchar(50) not null,
user_login varchar(500) not null,
user_token varchar(500) not null,
user_remark varchar(500) not null,
primary key (id)
);
