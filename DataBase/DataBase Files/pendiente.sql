-- Este es un borrador, falta conciderar el asunto de las comisiones , ahora hay que incluir la parte de la tabla de notificaciones.... tiene problemas con eso

USE `KRONOS`;
DROP procedure IF EXISTS pendingAccordsDepartment;
DELIMITER $$
USE `KRONOS`$$
create procedure pendingAccordsDepartment(in _department varchar(1))
begin
select ACCNUMBER, INCORDATE, 
DEADLINE, SESSIONDATE, TYPE_ID,T_TYPE.DESCRIPTION AS TYPE_DESC, OBSERVATIONS, PUBLIC, NOTIFIED,  STATE,T_STATE.DESCRIPTION AS STATE_DESC,  T_ACCPDF.URL , T_ACCPDF.FINALRESPONSE AS FINALRESPONSE from T_ACCORD, T_ACCPDF,T_STATE, T_TYPE where T_ACCORD.ACCNUMBER= T_ACCPDF.ACCORD AND  T_ACCORD.STATE = 2 AND T_ACCORD.STATE=T_STATE.ID AND T_ACCORD.TYPE_ID=T_TYPE.ID AND  T_ACCORD.TYPE_ID= _department;
end$$
DELIMITER ;
