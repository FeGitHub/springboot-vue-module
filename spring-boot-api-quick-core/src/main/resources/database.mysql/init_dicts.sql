
-- 性别
delete from dicts where itemcode='GENDER';
insert into dicts (id,itemcode,code,name) values (uuid(),'GENDER','0','未知的性别');
insert into dicts (id,itemcode,code,name) values (uuid(),'GENDER','1','男');
insert into dicts (id,itemcode,code,name) values (uuid(),'GENDER','2','女');
insert into dicts (id,itemcode,code,name) values (uuid(),'GENDER','9','未说明的性别');
commit;


-- 婚姻
delete from dicts where itemcode='MARITAL_STATUS';
insert into dicts (id,itemcode,code,name) values (uuid(),'MARITAL_STATUS','10','未婚');
insert into dicts (id,itemcode,code,name) values (uuid(),'MARITAL_STATUS','21','初婚');
insert into dicts (id,itemcode,code,name) values (uuid(),'MARITAL_STATUS','22','再婚');
insert into dicts (id,itemcode,code,name) values (uuid(),'MARITAL_STATUS','23','复婚');
insert into dicts (id,itemcode,code,name) values (uuid(),'MARITAL_STATUS','30','丧偶');
insert into dicts (id,itemcode,code,name) values (uuid(),'MARITAL_STATUS','40','离婚');
insert into dicts (id,itemcode,code,name) values (uuid(),'MARITAL_STATUS','90','未说明的婚姻状况');
commit;
