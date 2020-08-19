--  个人信息表
CREATE TABLE PERSON (
ID VARCHAR2(32) NOT NULL,
CREATE_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
LAST_UPDATE_TIME TIMESTAMP  DEFAULT CURRENT_TIMESTAMP NOT NULL,
VERSION_NUMBER NUMBER ,
PERSON_NAME VARCHAR2(30) NOT NULL,
GENDER CHAR(1) NOT NULL,
BIRTH_TIME DATE NOT NULL,
IDENTITY_NUMBER VARCHAR2(30) NOT NULL,
MARITAL_STATUS CHAR(2) NOT NULL,
SPOUSE_NAME VARCHAR2(30) ,
PRIMARY KEY (ID)
);

-- Add comments to the table
comment on table PERSON
  is '人员信息表';
-- Add comments to the columns
comment on column PERSON.CREATE_TIME
  is '创建时间';
comment on column PERSON.ID
  is '唯一标识';
  comment on column PERSON.LAST_UPDATE_TIME
  is '最后更新时间';
comment on column PERSON.VERSION_NUMBER
  is '乐观锁版本号';
comment on column PERSON.PERSON_NAME
  is '姓名';
  comment on column PERSON.GENDER
  is '性别编码';
comment on column PERSON.BIRTH_TIME
  is '出生时间';
comment on column PERSON.IDENTITY_NUMBER
  is '身份证号码';
comment on column PERSON.MARITAL_STATUS
  is '婚姻状况编码';
comment on column PERSON.SPOUSE_NAME
  is '配偶姓名';


-- 字典表
CREATE TABLE  DICTS(
ID VARCHAR2(32) NOT NULL,
ITEMCODE VARCHAR2(32) NOT NULL,
CODE VARCHAR2(6) NOT NULL,
NAME VARCHAR2(32) NOT NULL,
PRIMARY KEY (ID)
);
comment on column DICTS.ID
  is '主键';
comment on column DICTS.ITEMCODE
  is '字典类型';
  comment on column DICTS.CODE
  is '字典代码值';
  comment on column DICTS.NAME
  is '字典中文';