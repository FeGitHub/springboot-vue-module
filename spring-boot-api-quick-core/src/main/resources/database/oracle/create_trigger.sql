create or replace trigger person_trigger
before update on person for each row
begin
	 :new.LAST_UPDATE_TIME := sysdate;
end;
/