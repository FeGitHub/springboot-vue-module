
-- 性别
delete DICTS where ITEMCODE='GENDER';
insert into DICTS (ID,ITEMCODE,CODE,NAME) values (sys_guid(),'GENDER','0','未知的性别');
insert into DICTS (ID,ITEMCODE,CODE,NAME) values (sys_guid(),'GENDER','1','男');
insert into DICTS (ID,ITEMCODE,CODE,NAME) values (sys_guid(),'GENDER','2','女');
insert into DICTS (ID,ITEMCODE,CODE,NAME) values (sys_guid(),'GENDER','9','未说明的性别');
commit;


-- 婚姻
delete DICTS where ITEMCODE='MARITAL_STATUS';
insert into DICTS (ID,ITEMCODE,CODE,NAME) values (sys_guid(),'MARITAL_STATUS','10','未婚');
insert into DICTS (ID,ITEMCODE,CODE,NAME) values (sys_guid(),'MARITAL_STATUS','21','初婚');
insert into DICTS (ID,ITEMCODE,CODE,NAME) values (sys_guid(),'MARITAL_STATUS','22','再婚');
insert into DICTS (ID,ITEMCODE,CODE,NAME) values (sys_guid(),'MARITAL_STATUS','23','复婚');
insert into DICTS (ID,ITEMCODE,CODE,NAME) values (sys_guid(),'MARITAL_STATUS','30','丧偶');
insert into DICTS (ID,ITEMCODE,CODE,NAME) values (sys_guid(),'MARITAL_STATUS','40','离婚');
insert into DICTS (ID,ITEMCODE,CODE,NAME) values (sys_guid(),'MARITAL_STATUS','90','未说明的婚姻状况');
commit;