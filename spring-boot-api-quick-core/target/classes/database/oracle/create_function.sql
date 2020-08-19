CREATE OR REPLACE FUNCTION fn_getDictName(p_type IN VARCHAR2, p_key IN VARCHAR2) RETURN varchar2
IS dic_name VARCHAR2(32);
BEGIN
  SELECT NAME INTO dic_name FROM DICTS t WHERE t.ITEMCODE=p_type AND CODE=p_key;
RETURN (dic_name);
END fn_getDictName;
/