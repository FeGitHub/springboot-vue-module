
-- ----------------------------
-- Procedure structure for build_demo_data
-- ----------------------------
DROP PROCEDURE IF EXISTS `build_demo_data`;
delimiter ;;
CREATE  PROCEDURE `build_demo_data`()
BEGIN
-- ----------------------------
--  构建临时表数据
-- ----------------------------
   insert into  temp_tab(
	 field_a,
	 field_b
	 )
	 select
	 'A'  as field_a,
	 'B'  as field_b;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for demo_generate_temporary_table
-- ----------------------------
DROP PROCEDURE IF EXISTS `demo_generate_temporary_table`;
delimiter ;;
CREATE  PROCEDURE `demo_generate_temporary_table`()
BEGIN
-- ----------------------------
--  构建临时表
-- ----------------------------
 DROP TEMPORARY TABLE IF EXISTS temp_tab;
 create TEMPORARY table temp_tab
    (
		    field_a varchar(200) COMMENT '字段a',
        field_b varchar(200) COMMENT '字段b'

    ) ENGINE = innoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_general_ci COMMENT ='Y00临时统计表';


END
;;
delimiter ;

-- ----------------------------
-- Function structure for func_str_is_blank
-- ----------------------------
DROP FUNCTION IF EXISTS `func_str_is_blank`;
delimiter ;;
CREATE  FUNCTION `func_str_is_blank`(i_str varchar(4000)) RETURNS tinyint(1)
begin
    if i_str is null or i_str = '' then
        return 1;
    else
        return 0;
    end if;
end
;;
delimiter ;

-- ----------------------------
-- Procedure structure for operator_procedure_demo
-- ----------------------------
DROP PROCEDURE IF EXISTS `operator_procedure_demo`;
delimiter ;;
CREATE  PROCEDURE `operator_procedure_demo`()
BEGIN
-- ----------------------------
--   存储过程测试用例
-- ----------------------------
       DECLARE uuid VARCHAR(32);
			  -- 打开日志记录
			  set @show_procedure_log = 1;
				-- 唯一性主键生成
        SET  uuid = REPLACE(UUID(), '-', '');
				-- 记录日志
        call pro_procedure_log(uuid,'operator_procedure_demo','日志记录开始');
				-- 构建临时表
			  call demo_generate_temporary_table();
				-- 构建数据
				call build_demo_data();
				-- 查询数据
			  call query_demo_data();
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for pro_procedure_log
-- ----------------------------
DROP PROCEDURE IF EXISTS `pro_procedure_log`;
delimiter ;;
CREATE  PROCEDURE `pro_procedure_log`(IN i_procedure_search_id varchar(32), IN i_object_name varchar(50), IN i_message varchar(4000))
LABEL_PROC:
begin
    if func_str_is_blank(i_procedure_search_id) or func_str_is_blank(i_object_name) or func_str_is_blank(i_message) then
        LEAVE LABEL_PROC;
    end if;

    if @show_procedure_log = 1 then
        -- i_type參數超出預設範圍
        insert into procedure_log(procedure_search_id, object_name, message) values (i_procedure_search_id, i_object_name, i_message);
    end if;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for query_demo_data
-- ----------------------------
DROP PROCEDURE IF EXISTS `query_demo_data`;
delimiter ;;
CREATE  PROCEDURE `query_demo_data`()
BEGIN
-- ----------------------------
--  构建临时表
-- ----------------------------
   select  * from   temp_tab;
END
;;
delimiter ;


