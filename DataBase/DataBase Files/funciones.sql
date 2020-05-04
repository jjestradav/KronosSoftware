USE `KRONOS`;
DROP procedure IF EXISTS insertAccord;
DELIMITER $$
USE `KRONOS`$$
CREATE PROCEDURE insertAccord (IN accNumber VARCHAR(45), IN incorDate DATE, IN incorTime TIME, 
IN deadLine DATE, IN sessionDate DATE, IN type_id
int, IN observations longtext, IN publics TINYINT(4),
IN notified TINYINT(4), IN states INT, IN user_id VARCHAR(45))
BEGIN
INSERT INTO T_ACCORD (ACCNUMBER, INCORDATE,INCORTIME, 
DEADLINE, SESSIONDATE, OBSERVATIONS, PUBLIC, NOTIFIED,  STATE, TYPE_ID, USER_ID) 
VALUES (accNumber, incorDate, incorTime, deadLine, sessionDate, observations, publics, notified, states, type_id, user_id);
commit; 
end$$ 
DELIMITER ;


USE `KRONOS`;
DROP procedure IF EXISTS insertAccPdf;
DELIMITER $$
USE `KRONOS`$$
create procedure insertAccPdf(
in accord varchar(45), in url varchar(100),in final tinyint, in isApp int, in canDelete tinyint)
begin
insert into T_ACCPDF (ACCORD, URL, FINALRESPONSE, ISAPPROVED,CAN_DELETE) values (accord, url, final,isApp,canDelete);
commit;  
end$$ 
DELIMITER ;


USE `KRONOS`;
DROP procedure IF EXISTS insertAccNotification;
DELIMITER $$
USE `KRONOS`$$
create procedure insertAccNotification(
in accord varchar(45))
begin
insert into T_NOTIFICATION (ACCORD) values (accord);
commit;  
end$$ 
DELIMITER ;


USE `KRONOS`;
DROP procedure IF EXISTS insertTempUser;
DELIMITER $$
USE `KRONOS`$$
create procedure insertTempUser(
in namee varchar(45), in email varchar(45))
begin
insert into T_TEMPUSER (NAME, EMAIL) values (namee, email);
commit; 
end$$
DELIMITER ;

USE `KRONOS`;
DROP procedure IF EXISTS insertUserAcc;
DELIMITER $$
USE `KRONOS`$$
create procedure insertUserAcc(
in tempuser varchar(45), in accord varchar(45))
begin
insert into T_USERACC (TEMPUSER, ACCORD) values (tempuser, accord);
commit; 
end$$
DELIMITER ;



USE `KRONOS`;
DROP procedure IF EXISTS insertForgotPassword;
DELIMITER $$
USE `KRONOS`$$
create procedure insertForgotPassword(
in _user varchar(45), in _token varchar(6), in _datetime datetime)
begin
insert into T_FORGOT_PASSWORD (USER_ID, TOKEN, DATE_TIME) values (_user, _token, _datetime);
commit; 
end$$
DELIMITER ;


USE `KRONOS`;
DROP procedure IF EXISTS deleteForgotPassword;
DELIMITER $$
USE `KRONOS`$$
create procedure deleteForgotPassword(
in _user varchar(45), in _token varchar(6))
begin
delete from T_FORGOT_PASSWORD WHERE USER_ID=_user AND TOKEN=_token;
commit; 
end$$
DELIMITER ;


USE `KRONOS`;
DROP procedure IF EXISTS getRecentToken;
DELIMITER $$
USE `KRONOS`$$
create procedure getRecentToken(
in _user varchar(45))
begin
SELECT TOKEN from T_FORGOT_PASSWORD WHERE USER_ID=_user ORDER BY DATE_TIME DESC LIMIT 1;
end$$
DELIMITER ;


USE `KRONOS`;
DROP procedure IF EXISTS updatePassword;
DELIMITER $$
USE `KRONOS`$$
create procedure updatePassword(
in _user varchar(45), in _password varchar(100))
begin
UPDATE T_USER SET PASSWORD=_password where TEMPUSER=_user;
commit;
end$$
DELIMITER ;





USE `KRONOS`;
DROP procedure IF EXISTS searchAccordType;
DELIMITER $$
USE `KRONOS`$$
create procedure searchAccordType(in type_id int)
begin
select ACCNUMBER, INCORDATE, 
DEADLINE, SESSIONDATE, TYPE_ID,T_TYPE.DESCRIPTION AS TYPE_DESC, OBSERVATIONS, PUBLIC, NOTIFIED,  STATE,T_STATE.DESCRIPTION AS STATE_DESC,  T_ACCPDF.URL, T_ACCPDF.FINALRESPONSE AS FINALRESPONSE, T_ACCPDF.ISAPPROVED AS ISAPPROVED, T_ACCPDF.CAN_DELETE AS CAN_DELETE from T_ACCORD, T_ACCPDF,T_STATE, T_TYPE where T_ACCORD.ACCNUMBER= T_ACCPDF.ACCORD AND T_ACCORD.STATE=T_STATE.ID AND T_ACCORD.TYPE_ID=T_TYPE.ID AND T_ACCORD.TYPE_ID = type_id;
end$$
DELIMITER ;

USE `KRONOS`;
DROP procedure IF EXISTS searchAccordNumber;
DELIMITER $$
USE `KRONOS`$$
create procedure searchAccordNumber(in accNumber varchar(45)
)
begin
select ACCNUMBER, INCORDATE, 
DEADLINE, SESSIONDATE, TYPE_ID,T_TYPE.DESCRIPTION AS TYPE_DESC, OBSERVATIONS, PUBLIC, NOTIFIED,  STATE,T_STATE.DESCRIPTION AS STATE_DESC,  T_ACCPDF.URL, T_ACCPDF.FINALRESPONSE AS FINALRESPONSE, T_ACCPDF.ISAPPROVED AS ISAPPROVED, T_ACCPDF.CAN_DELETE AS CAN_DELETE   from T_ACCORD, T_ACCPDF,T_STATE, T_TYPE where T_ACCORD.ACCNUMBER= T_ACCPDF.ACCORD AND T_ACCORD.STATE=T_STATE.ID AND T_ACCORD.TYPE_ID=T_TYPE.ID AND T_ACCORD.ACCNUMBER LIKE CONCAT('%',accNumber,'%');
end$$
DELIMITER ; 




USE `KRONOS`;
DROP procedure IF EXISTS getAccord;
DELIMITER $$
USE `KRONOS`$$
create procedure getAccord(in accNumber varchar(45)
)
begin
select ACCNUMBER, INCORDATE, 
DEADLINE, SESSIONDATE, TYPE_ID,T_TYPE.DESCRIPTION AS TYPE_DESC, OBSERVATIONS, PUBLIC, NOTIFIED,  STATE,T_STATE.DESCRIPTION AS STATE_DESC,  T_ACCPDF.URL, T_ACCPDF.FINALRESPONSE AS FINALRESPONSE, T_ACCPDF.ISAPPROVED AS ISAPPROVED, T_ACCPDF.CAN_DELETE AS CAN_DELETE  from T_ACCORD, T_ACCPDF,T_STATE, T_TYPE where T_ACCORD.ACCNUMBER= T_ACCPDF.ACCORD AND T_ACCORD.STATE=T_STATE.ID AND T_ACCORD.TYPE_ID=T_TYPE.ID AND T_ACCORD.ACCNUMBER=accNumber;
end$$
DELIMITER ; 


USE `KRONOS`;
DROP procedure IF EXISTS getAccordByUser;
DELIMITER $$
USE `KRONOS`$$
create procedure getAccordByUser(in _user varchar(45))
begin
select T_ACCORD.ACCNUMBER, T_ACCORD.INCORDATE, 
T_ACCORD.DEADLINE, T_ACCORD.SESSIONDATE, T_ACCORD.TYPE_ID,T_TYPE.DESCRIPTION AS TYPE_DESC, T_ACCORD.OBSERVATIONS, T_ACCORD.PUBLIC, T_ACCORD.NOTIFIED,  T_ACCORD.STATE,T_STATE.DESCRIPTION AS STATE_DESC,  T_ACCPDF.URL, T_ACCPDF.FINALRESPONSE AS FINALRESPONSE, T_ACCPDF.ISAPPROVED AS ISAPPROVED, T_ACCPDF.CAN_DELETE AS CAN_DELETE  from T_ACCORD, T_ACCPDF,T_STATE, T_TYPE, T_NOTIFICATION
where T_NOTIFICATION.USER=_user AND T_NOTIFICATION.ACCORD=T_ACCORD.ACCNUMBER AND T_ACCORD.ACCNUMBER= T_ACCPDF.ACCORD AND
T_ACCORD.STATE=T_STATE.ID AND T_ACCORD.TYPE_ID=T_TYPE.ID AND T_ACCORD.STATE=3;
end$$
DELIMITER ; 

USE `KRONOS`;
DROP procedure IF EXISTS getResponsablesUserName;
DELIMITER $$
USE `KRONOS`$$
create procedure getResponsablesUserName(in _accord varchar(45))
begin
select T_NOTIFICATION.USER from T_NOTIFICATION, T_ACCORD 
where T_NOTIFICATION.ACCORD=_accord AND T_ACCORD.ACCNUMBER=_accord AND T_ACCORD.STATE!=0 AND T_ACCORD.STATE!=4 AND T_ACCORD.STATE!=1;
end$$
DELIMITER ; 






USE `KRONOS`;
DROP procedure IF EXISTS searchAccordIncorDate;
DELIMITER $$
USE `KRONOS`$$
create procedure searchAccordIncorDate(in incorDate  date
)
begin
select ACCNUMBER, INCORDATE, 
DEADLINE, SESSIONDATE, TYPE_ID,T_TYPE.DESCRIPTION AS TYPE_DESC, OBSERVATIONS, PUBLIC, NOTIFIED,  STATE,T_STATE.DESCRIPTION AS STATE_DESC,  T_ACCPDF.URL, T_ACCPDF.FINALRESPONSE AS FINALRESPONSE,T_ACCPDF.ISAPPROVED AS ISAPPROVED, T_ACCPDF.CAN_DELETE AS CAN_DELETE from T_ACCORD, T_ACCPDF,T_STATE, T_TYPE where T_ACCORD.ACCNUMBER= T_ACCPDF.ACCORD AND T_ACCORD.STATE=T_STATE.ID AND T_ACCORD.TYPE_ID=T_TYPE.ID AND T_ACCORD.INCORDATE = incorDate;
end$$
DELIMITER ; 

USE `KRONOS`;
DROP procedure IF EXISTS searchAccordsessionDate;
DELIMITER $$
USE `KRONOS`$$
create procedure searchAccordsessionDate(in sessionDate date
)
begin
select ACCNUMBER, INCORDATE, 
DEADLINE, SESSIONDATE, TYPE_ID,T_TYPE.DESCRIPTION AS TYPE_DESC, OBSERVATIONS, PUBLIC, NOTIFIED,  STATE,T_STATE.DESCRIPTION AS STATE_DESC,  T_ACCPDF.URL, T_ACCPDF.FINALRESPONSE AS FINALRESPONSE, T_ACCPDF.ISAPPROVED AS ISAPPROVED, T_ACCPDF.CAN_DELETE AS CAN_DELETE   from T_ACCORD, T_ACCPDF,T_STATE, T_TYPE where T_ACCORD.ACCNUMBER= T_ACCPDF.ACCORD AND T_ACCORD.STATE=T_STATE.ID AND T_ACCORD.TYPE_ID=T_TYPE.ID AND T_ACCORD.SESSIONDATE = sessionDate;
end$$
DELIMITER ; 

USE `KRONOS`;
DROP procedure IF EXISTS deleteAccord;
DELIMITER $$
USE `KRONOS`$$
create procedure deleteAccord(in accord varchar(45), in _user varchar(45) 
)
begin
delete from T_USERACC where T_USERACC.ACCORD = accord;
commit;
delete from T_ACCPDF where T_ACCPDF.ACCORD = accord;
commit;
INSERT INTO T_DELETEDACCORDS values (accord,_user,CURRENT_TIMESTAMP());
commit;
delete from T_ACCORD where T_ACCORD.ACCNUMBER = accord;
commit;
end$$
DELIMITER ; 



USE `KRONOS`;
DROP procedure IF EXISTS searchAllAccords;
DELIMITER $$
USE `KRONOS`$$
create procedure searchAllAccords()
begin
select ACCNUMBER, INCORDATE, 
DEADLINE, SESSIONDATE, TYPE_ID,T_TYPE.DESCRIPTION AS TYPE_DESC, OBSERVATIONS, PUBLIC, NOTIFIED,  STATE,T_STATE.DESCRIPTION AS STATE_DESC,  T_ACCPDF.URL , T_ACCPDF.FINALRESPONSE AS FINALRESPONSE, T_ACCPDF.ISAPPROVED AS ISAPPROVED, T_ACCPDF.CAN_DELETE AS CAN_DELETE  from T_ACCORD, T_ACCPDF,T_STATE, T_TYPE where T_ACCORD.ACCNUMBER= T_ACCPDF.ACCORD AND T_ACCORD.STATE=T_STATE.ID AND T_ACCORD.TYPE_ID=T_TYPE.ID;
end$$
DELIMITER ; 


USE `KRONOS`;
DROP procedure IF EXISTS searchAllAccords_NotCompleted;
DELIMITER $$
USE `KRONOS`$$
create procedure searchAllAccords_NotCompleted()
begin
select ACCNUMBER, INCORDATE, 
DEADLINE, SESSIONDATE, TYPE_ID,T_TYPE.DESCRIPTION AS TYPE_DESC, OBSERVATIONS, PUBLIC, NOTIFIED,  STATE,T_STATE.DESCRIPTION AS STATE_DESC,  T_ACCPDF.URL , T_ACCPDF.FINALRESPONSE AS FINALRESPONSE, T_ACCPDF.ISAPPROVED AS ISAPPROVED, T_ACCPDF.CAN_DELETE AS CAN_DELETE  from T_ACCORD, T_ACCPDF,T_STATE, T_TYPE where T_ACCORD.ACCNUMBER= T_ACCPDF.ACCORD AND  T_ACCORD.STATE != 0 AND T_ACCORD.STATE=T_STATE.ID AND T_ACCORD.TYPE_ID=T_TYPE.ID ;
end$$
DELIMITER ; 


USE `KRONOS`;
DROP procedure IF EXISTS searchTempUser;
DELIMITER $$
USE `KRONOS`$$
create procedure searchTempUser( in email varchar(45))
begin
select T_TEMPUSER.NAME, T_TEMPUSER.EMAIL  from T_TEMPUSER where T_TEMPUSER.EMAIL= email; 
end$$
DELIMITER ; 



USE `KRONOS`;
DROP procedure IF EXISTS updateAccord;
DELIMITER $$
USE `KRONOS`$$
create procedure updateAccord( in _accNumber varchar(45), in _deadline date, in _sessionDate date, in _observations longtext, in _public tinyint, in _notified tinyint, in _state int(11), in _type char(1), in _user varchar(45)) 
begin
UPDATE T_ACCORD SET DEADLINE=_deadline, SESSIONDATE=_sessionDate, OBSERVATIONS=_observations,PUBLIC=_public , NOTIFIED=_notified, STATE=_state, 
TYPE_ID=_type, USER_ID=_user WHERE ACCNUMBER=_accNumber;
end$$
DELIMITER ; 


USE `KRONOS`;
DROP procedure IF EXISTS updateAccordSessionDate;
DELIMITER $$
USE `KRONOS`$$
create procedure updateAccordSessionDate( in accord varchar(45), in newdate date)
begin
update T_ACCORD set SESSIONDATE = newdate where ACCNUMBER = accord;   
end$$
DELIMITER ; 

USE `KRONOS`;
DROP procedure IF EXISTS updateAccordDeadline;
DELIMITER $$
USE `KRONOS`$$
create procedure updateAccordDeadline( in accord varchar(45), in newdeadline date)
begin
update T_ACCORD set DEADLINE = newdeadline where ACCNUMBER = accord;
end$$
DELIMITER ; 

USE `KRONOS`;
DROP procedure IF EXISTS updateAccordType;
DELIMITER $$
USE `KRONOS`$$
create procedure updateAccordType( in accord varchar(45), in newtype char(1))
begin
update T_ACCORD set TYPE_ID = newtype where ACCNUMBER = accord;
end$$
DELIMITER ; 

USE `KRONOS`;
DROP procedure IF EXISTS updateAccordState;
DELIMITER $$
USE `KRONOS`$$
create procedure updateAccordState( in accord varchar(45), in newstate int)
begin
update T_ACCORD set STATE = newstate where ACCNUMBER = accord;
end$$
DELIMITER ;  


USE `KRONOS`;
DROP procedure IF EXISTS updateAccordObservations;
DELIMITER $$
USE `KRONOS`$$
create procedure updateAccordObservations( in accord varchar(45), in new_observations longtext)
begin
update T_ACCORD set OBSERVATIONS = new_observations where ACCNUMBER = accord;
end$$
DELIMITER ;  


USE `KRONOS`;
DROP procedure IF EXISTS updateAccordUser;
DELIMITER $$
USE `KRONOS`$$
create procedure updateAccordUser( in accord varchar(45), in newuser varchar(45))
begin
update T_USERACC set TEMPUSER = newuser where ACCORD = accord;
end$$
DELIMITER ; 




USE `KRONOS`;
DROP procedure IF EXISTS updateAccordUrls;
DELIMITER $$
USE `KRONOS`$$
create procedure updateAccordUrls( in accord varchar(45), in newurl varchar(100), in anturl varchar(100))
begin
update T_ACCPDF set URL=newurl where ACCORD=accord and URL=anturl; 
end$$
DELIMITER ; 

USE `KRONOS`;
DROP procedure IF EXISTS updateAccordUrlResponse;
DELIMITER $$
USE `KRONOS`$$
create procedure updateAccordUrlResponse( in accord varchar(45), in url varchar(100), in finalResponse bool)
begin
update T_ACCPDF set FINALRESPONSE=finalResponse where ACCORD=accord and URL=anturl; 
end$$
DELIMITER ;


USE `KRONOS`;
DROP procedure IF EXISTS getUserRole;
DELIMITER $$
USE `KRONOS`$$
create procedure getUserRole( in _user varchar(45), in _password varchar(100))
begin
SELECT USER_NAME, ROLE_NAME, T_USER.DEPARTMENT FROM T_USERROLE,T_USER WHERE T_USERROLE.USER_NAME=_user AND (T_USER.TEMPUSER=_user
AND T_USER.PASSWORD=_password); 
end$$
DELIMITER ;


USE `KRONOS`;
DROP procedure IF EXISTS getUser;
DELIMITER $$
USE `KRONOS`$$
create procedure getUser( in _user varchar(45), in _password varchar(100))
begin
SELECT TEMPUSER, PASSWORD, DEPARTMENT FROM T_USER WHERE TEMPUSER=_user AND PASSWORD=_password; 
end$$
DELIMITER ;



USE `KRONOS`;
DROP procedure IF EXISTS getUserByEmail;
DELIMITER $$
USE `KRONOS`$$
create procedure getUserByEmail( in _user varchar(45))
begin
 select T_TEMPUSER.NAME AS NAME, T_TEMPUSER.EMAIL AS EMAIL, T_DEPARTMENT.ID AS DEP_ID, T_DEPARTMENT.NAME AS DEP_NAME, STATUS, ISBOSS from T_TEMPUSER, T_DEPARTMENT, T_USER 
WHERE T_USER.TEMPUSER=_user AND T_USER.TEMPUSER=T_TEMPUSER.EMAIL AND T_USER.DEPARTMENT=T_DEPARTMENT.ID;
end$$
DELIMITER ;




USE `KRONOS`;
DROP procedure IF EXISTS searchAllStates;
DELIMITER $$
USE `KRONOS`$$
create procedure searchAllStates()
begin
select  ID , DESCRIPTION from T_STATE;
end$$
DELIMITER ; 



USE `KRONOS`;
DROP procedure IF EXISTS searchAllTypes;
DELIMITER $$
USE `KRONOS`$$
create procedure searchAllTypes()
begin
select  ID , DESCRIPTION from T_TYPE;
end$$
DELIMITER ; 
                                                                                                    
USE `KRONOS`;
DROP procedure IF EXISTS searchExpiredAccords;
DELIMITER $$
USE `KRONOS`$$
create procedure searchExpiredAccords(in actual date, in _limit date)
begin
select ACCNUMBER, INCORDATE, 
DEADLINE, SESSIONDATE, TYPE_ID,T_TYPE.DESCRIPTION AS TYPE_DESC, OBSERVATIONS, PUBLIC, NOTIFIED,  STATE,T_STATE.DESCRIPTION AS STATE_DESC,  T_ACCPDF.URL, T_ACCPDF.FINALRESPONSE AS FINALRESPONSE, T_ACCPDF.ISAPPROVED AS ISAPPROVED, T_ACCPDF.CAN_DELETE AS CAN_DELETE  from T_ACCORD, T_ACCPDF,T_STATE, T_TYPE where T_ACCORD.ACCNUMBER= T_ACCPDF.ACCORD AND T_ACCORD.STATE=T_STATE.ID AND T_ACCORD.TYPE_ID=T_TYPE.ID AND T_ACCORD.DEADLINE <=actual AND T_ACCORD.DEADLINE >=_limit;
end$$
DELIMITER ;


USE `KRONOS`;
DROP procedure IF EXISTS todayDeadlineAccords;
DELIMITER $$
USE `KRONOS`$$
create procedure todayDeadlineAccords(in actual date)
begin
select ACCNUMBER, INCORDATE, 
DEADLINE, SESSIONDATE, TYPE_ID,T_TYPE.DESCRIPTION AS TYPE_DESC, OBSERVATIONS, PUBLIC, NOTIFIED,  STATE,T_STATE.DESCRIPTION AS STATE_DESC,  T_ACCPDF.URL, T_ACCPDF.FINALRESPONSE AS FINALRESPONSE, T_ACCPDF.ISAPPROVED AS ISAPPROVED, T_ACCPDF.CAN_DELETE AS CAN_DELETE   from T_ACCORD, T_ACCPDF,T_STATE, T_TYPE where T_ACCORD.ACCNUMBER= T_ACCPDF.ACCORD AND T_ACCORD.STATE=T_STATE.ID AND T_ACCORD.TYPE_ID=T_TYPE.ID AND T_ACCORD.DEADLINE=actual;
end$$
DELIMITER ;

USE `KRONOS`;
DROP procedure IF EXISTS isActInDB;
DELIMITER $$
USE `KRONOS`$$
create procedure isActInDB(in mydate date)
begin
SELECT SESSIONDATE from T_ACT where SESSIONDATE=mydate;
end$$
DELIMITER ;

USE `KRONOS`;
DROP procedure IF EXISTS notAssignedAccords;
DELIMITER $$
USE `KRONOS`$$
create procedure notAssignedAccords()
begin
select ACCNUMBER, INCORDATE, 
DEADLINE, SESSIONDATE, TYPE_ID,T_TYPE.DESCRIPTION AS TYPE_DESC, OBSERVATIONS, PUBLIC, NOTIFIED,  STATE,T_STATE.DESCRIPTION AS STATE_DESC,  T_ACCPDF.URL, T_ACCPDF.FINALRESPONSE AS FINALRESPONSE, T_ACCPDF.ISAPPROVED AS ISAPPROVED, T_ACCPDF.CAN_DELETE AS CAN_DELETE  
from T_ACCORD, T_ACCPDF,T_STATE, T_TYPE 
where T_ACCORD.ACCNUMBER= T_ACCPDF.ACCORD AND T_ACCORD.STATE=T_STATE.ID AND T_ACCORD.TYPE_ID=T_TYPE.ID 
AND ACCNUMBER not in(select ACCORD from T_NOTIFICATION)
AND STATE=2
AND TYPE_ID='A';
end$$
DELIMITER ;



USE `KRONOS`;
DROP procedure IF EXISTS alreadyAssignedAccords;
DELIMITER $$
USE `KRONOS`$$
create procedure alreadyAssignedAccords()
begin
select ACCNUMBER, INCORDATE, 
DEADLINE, SESSIONDATE, TYPE_ID,T_TYPE.DESCRIPTION AS TYPE_DESC, OBSERVATIONS, PUBLIC, NOTIFIED,  STATE,T_STATE.DESCRIPTION AS STATE_DESC,  T_ACCPDF.URL, T_ACCPDF.FINALRESPONSE AS FINALRESPONSE, T_ACCPDF.ISAPPROVED AS ISAPPROVED, T_ACCPDF.CAN_DELETE AS CAN_DELETE   
from T_ACCORD, T_ACCPDF,T_STATE, T_TYPE 
where T_ACCORD.ACCNUMBER= T_ACCPDF.ACCORD AND T_ACCORD.STATE=T_STATE.ID AND T_ACCORD.TYPE_ID=T_TYPE.ID 
AND ACCNUMBER  in(select ACCORD from T_NOTIFICATION);
end$$
DELIMITER ;


USE `KRONOS`;
DROP procedure IF EXISTS resposableUsers;
DELIMITER $$
USE `KRONOS`$$
create procedure resposableUsers(in _accord varchar(45))
begin
select T_NOTIFICATION.ACCORD,T_NOTIFICATION.USER, T_DEPARTMENT.ID AS DEP_ID, T_DEPARTMENT.NAME AS DEP_NAME FROM T_NOTIFICATION,T_DEPARTMENT,T_USER
WHERE T_NOTIFICATION.ACCORD=_accord AND T_NOTIFICATION.USER=T_USER.TEMPUSER AND T_USER.DEPARTMENT=T_DEPARTMENT.ID;
end$$
DELIMITER ;


USE `KRONOS`;
DROP procedure IF EXISTS insertTempAct;
DELIMITER $$
USE `KRONOS`$$
create procedure insertTempAct(in mydate date)
begin
INSERT INTO T_ACT (SESSIONDATE) values (mydate);
COMMIT;
end$$
DELIMITER ;

USE `KRONOS`;
DROP procedure IF EXISTS insertNotification;
DELIMITER $$
USE `KRONOS`$$
create procedure insertNotification(in _accord varchar(45), in _user varchar(45))
begin
INSERT INTO T_NOTIFICATION (ACCORD,USER) values (_accord,_user);
COMMIT;
end$$
DELIMITER ;


USE `KRONOS`;
DROP procedure IF EXISTS updateAccordState;
DELIMITER $$
USE `KRONOS`$$
create procedure updateAccordState( in accord varchar(45), in newstate int)
begin
update T_ACCORD set STATE = newstate where ACCNUMBER = accord;
end$$
DELIMITER ;  


USE `KRONOS`;
DROP procedure IF EXISTS insertACCDPRMTNT;
DELIMITER $$
USE `KRONOS`$$
create procedure insertACCDPRMTNT(in _accord varchar(45), in _department int)
begin
INSERT INTO T_ACCDPRMNT (ACCORD,DEPARTMENT) values (_accord,_department);
COMMIT;
end$$
DELIMITER ;




USE `KRONOS`;
DROP procedure IF EXISTS deleteNotification;
DELIMITER $$
USE `KRONOS`$$
create procedure deleteNotification(in _accord varchar(45), in _user varchar(45))
begin
DELETE FROM T_NOTIFICATION WHERE ACCORD=_accord and USER=_user;
COMMIT;
end$$
DELIMITER ;


USE `KRONOS`;
DROP procedure IF EXISTS isInNotification;
DELIMITER $$
USE `KRONOS`$$
create procedure isInNotification(in _accord varchar(45), in _user varchar(45))
begin
SELECT ACCORD,USER FROM T_NOTIFICATION WHERE ACCORD=_accord AND USER=_user;
end$$
DELIMITER ;


USE `KRONOS`;
DROP procedure IF EXISTS deletePdf;
DELIMITER $$
USE `KRONOS`$$
create procedure deletePdf(
in accordNum varchar(45), in path varchar(100))
begin
delete from T_ACCPDF where ACCORD=accordNum and URL= path; 
commit;  
end$$ 
DELIMITER ;

USE `KRONOS`;
DROP procedure IF EXISTS emailInfo;
DELIMITER $$
USE `KRONOS`$$
create procedure emailInfo(
in today date, in limt date)
begin
  select ACCNUMBER, INCORDATE, DEADLINE
  from T_ACCORD, T_TYPE where T_ACCORD.INCORDATE <= today and T_ACCORD.INCORDATE >=limt and T_TYPE.DESCRIPTION='Administración Municipal';
end$$ 
DELIMITER ;

USE `KRONOS`;
DROP procedure IF EXISTS searchAllDepartments;
DELIMITER $$
USE `KRONOS`$$
create procedure searchAllDepartments()
begin
 select ID, NAME from T_DEPARTMENT where ID != 1;
end $$
DELIMITER ;


USE `KRONOS`;
DROP procedure IF EXISTS searchTempUsersByAccord;
DELIMITER $$
USE `KRONOS`$$
create procedure searchTempUsersByAccord(in _accord varchar(45))
begin
 select T_TEMPUSER.NAME as NAME , T_USERACC.TEMPUSER as EMAIL from T_TEMPUSER,T_USERACC where T_TEMPUSER.EMAIL=T_USERACC.TEMPUSER
AND T_USERACC.ACCORD=_accord;
end $$
DELIMITER ;

USE `KRONOS`;
DROP procedure IF EXISTS searchAllRoles;
DELIMITER $$
USE `KRONOS`$$
create procedure searchAllRoles()
begin
 select ID, NAME from T_ROLE;
end $$
DELIMITER ;
                                          
DROP procedure IF EXISTS updatePdf;
DELIMITER $$
USE `KRONOS`$$
create procedure updatePdf(in _accord varchar(45), in _url varchar(100), in final tinyint)
begin
UPDATE T_ACCPDF SET FINALRESPONSE=final WHERE ACCORD=_accord AND URL=_url;
COMMIT;
end$$
DELIMITER ;




USE `KRONOS`;
DROP procedure IF EXISTS searchUserByEmail;
DELIMITER $$
USE `KRONOS`$$
create procedure searchUserByEmail(in _email varchar(45))
begin
 select T_TEMPUSER.NAME AS NAME, T_TEMPUSER.EMAIL AS EMAIL, T_DEPARTMENT.ID AS DEP_ID, T_DEPARTMENT.NAME AS DEP_NAME, STATUS, ISBOSS from T_TEMPUSER, T_DEPARTMENT, T_USER 
WHERE T_USER.TEMPUSER=_email AND T_USER.TEMPUSER=T_TEMPUSER.EMAIL AND T_USER.DEPARTMENT=T_DEPARTMENT.ID;
end $$
DELIMITER ;


USE `KRONOS`;
DROP procedure IF EXISTS getDepartmentByEmail;
DELIMITER $$
USE `KRONOS`$$
create procedure getDepartmentByEmail(in _email varchar(45))
begin
 select T_DEPARTMENT.ID,T_DEPARTMENT.NAME from T_DEPARTMENT, T_USER where T_DEPARTMENT.ID=T_USER.DEPARTMENT AND T_USER.TEMPUSER=_email;
end $$
DELIMITER ;




USE `KRONOS`;
DROP procedure IF EXISTS searchUserByDepartment;
DELIMITER $$
USE `KRONOS`$$
create procedure searchUserByDepartment(in _id int)
begin
 select T_TEMPUSER.NAME AS NAME, T_TEMPUSER.EMAIL AS EMAIL, T_DEPARTMENT.ID AS DEP_ID, T_DEPARTMENT.NAME AS DEP_NAME, STATUS from T_TEMPUSER, T_DEPARTMENT, T_USER 
WHERE T_USER.TEMPUSER=T_TEMPUSER.EMAIL AND T_USER.DEPARTMENT=_id AND T_USER.DEPARTMENT=T_DEPARTMENT.ID;
end $$
DELIMITER ;


USE `KRONOS`;
DROP procedure IF EXISTS searchBossByDepartment;
DELIMITER $$
USE `KRONOS`$$
create procedure searchBossByDepartment(in _id int)
begin
 select T_TEMPUSER.NAME AS NAME, T_TEMPUSER.EMAIL AS EMAIL, T_DEPARTMENT.ID AS DEP_ID, T_DEPARTMENT.NAME AS DEP_NAME, STATUS, ISBOSS from T_TEMPUSER, T_DEPARTMENT, T_USER 
WHERE T_USER.TEMPUSER=T_TEMPUSER.EMAIL AND T_USER.DEPARTMENT=_id AND T_USER.DEPARTMENT=T_DEPARTMENT.ID AND T_USER.ISBOSS=true;
end $$
DELIMITER ;


USE `KRONOS`;
DROP procedure IF EXISTS searchUsersBoss;
DELIMITER $$
USE `KRONOS`$$
create procedure searchUsersBoss(in _email varchar(45), in _id int)
begin
 select T_TEMPUSER.NAME AS NAME, T_TEMPUSER.EMAIL AS EMAIL, T_DEPARTMENT.ID AS DEP_ID, T_DEPARTMENT.NAME AS DEP_NAME, STATUS from T_TEMPUSER, T_DEPARTMENT, T_USER 
WHERE T_USER.TEMPUSER!=_email AND T_USER.TEMPUSER=T_TEMPUSER.EMAIL AND T_USER.DEPARTMENT=_id AND T_USER.DEPARTMENT=T_DEPARTMENT.ID;
end $$
DELIMITER ;


USE `KRONOS`;
DROP procedure IF EXISTS searchUserRoleByDepartment;
DELIMITER $$
USE `KRONOS`$$
create procedure searchUserRoleByDepartment(in _id int)
begin
 select T_TEMPUSER.NAME AS NAME, T_TEMPUSER.EMAIL AS EMAIL, T_DEPARTMENT.ID AS DEP_ID, T_DEPARTMENT.NAME AS DEP_NAME, STATUS ,T_USER_ROLE.ROLE_NAME AS ROLE_NAME from T_TEMPUSER, T_DEPARTMENT, T_USER, T_USER_ROLE
WHERE T_USER.TEMPUSER=T_TEMPUSER.EMAIL AND T_USER.DEPARTMENT=_id AND T_USER.DEPARTMENT=T_DEPARTMENT.ID AND T_USER.TEMPUSER=T_USER_ROLE.USER_ID AND T_USER.DEPARTMENT!=1;
end $$
DELIMITER ;



USE `KRONOS`;
DROP procedure IF EXISTS searchBossUserRoleByDepartment;
DELIMITER $$
USE `KRONOS`$$
create procedure searchBossUserRoleByDepartment(in _email varchar(45),in _id int)
begin
 select T_TEMPUSER.NAME AS NAME, T_TEMPUSER.EMAIL AS EMAIL, T_DEPARTMENT.ID AS DEP_ID, T_DEPARTMENT.NAME AS DEP_NAME, STATUS ,T_USER_ROLE.ROLE_NAME AS ROLE_NAME from T_TEMPUSER, T_DEPARTMENT, T_USER, T_USER_ROLE
WHERE T_USER.TEMPUSER!=_email AND T_USER.TEMPUSER=T_TEMPUSER.EMAIL AND T_USER.DEPARTMENT=_id AND T_USER.DEPARTMENT=T_DEPARTMENT.ID AND T_USER.TEMPUSER=T_USER_ROLE.USER_ID AND T_USER.DEPARTMENT!=1;
end $$
DELIMITER ;




USE `KRONOS`;
DROP procedure IF EXISTS searchUserRoleByDepartment;
DELIMITER $$
USE `KRONOS`$$
create procedure searchUserRoleByDepartment(in _id int)
begin
 select T_TEMPUSER.NAME AS NAME, T_TEMPUSER.EMAIL AS EMAIL, T_DEPARTMENT.ID AS DEP_ID, T_DEPARTMENT.NAME AS DEP_NAME, STATUS ,T_USER_ROLE.ROLE_NAME AS ROLE_NAME from T_TEMPUSER, T_DEPARTMENT, T_USER, T_USER_ROLE
WHERE T_USER.TEMPUSER=T_TEMPUSER.EMAIL AND T_USER.DEPARTMENT=_id AND T_USER.DEPARTMENT=T_DEPARTMENT.ID AND T_USER.TEMPUSER=T_USER_ROLE.USER_ID AND T_USER.DEPARTMENT!=1;
end $$
DELIMITER ;

USE `KRONOS`;
DROP procedure IF EXISTS searchUserByStatus;
DELIMITER $$
USE `KRONOS`$$
create procedure searchUserByStatus(in _status tinyint)
begin
 select T_TEMPUSER.NAME AS NAME, T_TEMPUSER.EMAIL AS EMAIL, T_DEPARTMENT.ID AS DEP_ID, T_DEPARTMENT.NAME AS DEP_NAME, STATUS from T_TEMPUSER, T_DEPARTMENT, T_USER 
WHERE T_USER.TEMPUSER=T_TEMPUSER.EMAIL AND STATUS=_status AND T_USER.DEPARTMENT=T_DEPARTMENT.ID;
end $$
DELIMITER ;


USE `KRONOS`;
DROP procedure IF EXISTS searchAllUsers;
DELIMITER $$
USE `KRONOS`$$
create procedure searchAllUsers()
begin
 select T_TEMPUSER.NAME AS NAME, T_TEMPUSER.EMAIL AS EMAIL, T_DEPARTMENT.ID AS DEP_ID, T_DEPARTMENT.NAME AS DEP_NAME, STATUS from T_TEMPUSER, T_DEPARTMENT, T_USER 
WHERE T_USER.TEMPUSER=T_TEMPUSER.EMAIL AND T_USER.DEPARTMENT=T_DEPARTMENT.ID;
end $$
DELIMITER ;


USE `KRONOS`;
DROP procedure IF EXISTS searchAllUserRole;
DELIMITER $$
USE `KRONOS`$$
create procedure searchAllUserRole()
begin
 select T_TEMPUSER.NAME AS NAME, T_TEMPUSER.EMAIL AS EMAIL, T_DEPARTMENT.ID AS DEP_ID, T_DEPARTMENT.NAME AS DEP_NAME, STATUS ,T_USER_ROLE.ROLE_NAME AS ROLE_NAME from T_TEMPUSER, T_DEPARTMENT, T_USER, T_USER_ROLE
WHERE T_USER.TEMPUSER=T_TEMPUSER.EMAIL AND T_USER.DEPARTMENT=T_DEPARTMENT.ID AND T_USER.TEMPUSER=T_USER_ROLE.USER_ID AND T_USER.DEPARTMENT!=1;
end $$
DELIMITER ;






USE `KRONOS`;
DROP procedure IF EXISTS insertUser;
DELIMITER $$
USE `KRONOS`$$
create procedure insertUser(in  _department int, in  _password varchar(100), in  _tempUser varchar(45), in _status tinyint) 
begin
	insert into T_USER (DEPARTMENT, PASSWORD, TEMPUSER, STATUS) values (_department, _password, _tempUser, _status);
commit;
end $$
DELIMITER ;



USE `KRONOS`;
DROP procedure IF EXISTS searchDepartment;
DELIMITER $$
USE `KRONOS`$$
create procedure searchDepartment(in _name varchar(100))
begin
 select ID, NAME from T_DEPARTMENT where NAME=_name and ID != 1;
end $$
DELIMITER ;







USE `KRONOS`;
DROP procedure IF EXISTS insertDepartment;
DELIMITER $$
USE `KRONOS`$$
create procedure insertDepartment(in _name varchar(100))
begin 
 insert into T_DEPARTMENT (NAME) values (_name);
 commit;
end $$
DELIMITER ;

USE `KRONOS`;
DROP procedure IF EXISTS insertRole;
DELIMITER $$
USE `KRONOS`$$
create procedure insertRole(in _name varchar(45))
begin 
 insert into T_ROLE (NAME) values (_name);
 commit;
end$$
DELIMITER ;


USE `KRONOS`;
DROP procedure IF EXISTS searchRole;
DELIMITER $$
USE `KRONOS`$$
create procedure searchRole(in _name varchar(45))
begin
 select ID, NAME from T_ROLE where NAME=_name;
end$$
DELIMITER ;


USE `KRONOS`;
DROP procedure IF EXISTS insertUserRole;
DELIMITER $$
USE `KRONOS`$$
create procedure insertUserRole(in _id_user varchar(100), in _name_role varchar(45))
begin
 insert into T_USER_ROLE (USER_ID, ROLE_NAME) values (_id_user, _name_role);
 commit;
end$$
DELIMITER ;


USE `KRONOS`;
DROP procedure IF EXISTS searchAllUsersWithoutRole;
DELIMITER $$
USE `KRONOS`$$
create procedure searchAllUsersWithoutRole()
 begin
 select T_USER.TEMPUSER from T_USER where T_USER.TEMPUSER NOT IN (select T_USER_ROLE.USER_ID from T_USER_ROLE);
end$$
DELIMITER ;


USE `KRONOS`;
DROP procedure IF EXISTS haveFinalResponse;
DELIMITER $$
USE `KRONOS`$$
create procedure haveFinalResponse(in _accord varchar(45))
begin
SELECT ACCORD,URL,FINALRESPONSE FROM T_ACCPDF WHERE ACCORD=_accord AND FINALRESPONSE=true;
end$$
DELIMITER ;

USE `KRONOS`;
DROP procedure IF EXISTS changeAccordState;
DELIMITER $$
USE `KRONOS`$$
create procedure changeAccordState(in _state int, in _today date)
begin
update T_ACCORD set STATE = _state where T_ACCORD.DEADLINE < _today and T_ACCORD.STATE != 0 and T_ACCORD.STATE != 4 and T_ACCORD.STATE != 1;
commit;
end$$
DELIMITER ; 

USE `KRONOS`;
DROP procedure IF EXISTS pendingAccordsDepartment;
DELIMITER $$
USE `KRONOS`$$
create procedure pendingAccordsDepartment(in _department varchar(1))
begin
select ACCNUMBER, INCORDATE, 
DEADLINE, SESSIONDATE, TYPE_ID,T_TYPE.DESCRIPTION AS TYPE_DESC, OBSERVATIONS, PUBLIC, NOTIFIED,  STATE,T_STATE.DESCRIPTION AS STATE_DESC,  T_ACCPDF.URL , T_ACCPDF.FINALRESPONSE AS FINALRESPONSE, T_ACCPDF.ISAPPROVED AS ISAPPROVED, T_ACCPDF.CAN_DELETE AS CAN_DELETE  from T_ACCORD, T_ACCPDF,T_STATE, T_TYPE where T_ACCORD.ACCNUMBER= T_ACCPDF.ACCORD AND T_ACCORD.STATE=T_STATE.ID AND (T_ACCORD.STATE=2 OR T_ACCORD.STATE=3) AND T_ACCORD.TYPE_ID=T_TYPE.ID AND  T_ACCORD.TYPE_ID= _department;
end$$
DELIMITER ;



USE `KRONOS`;
DROP procedure IF EXISTS aMSearchAccords;
DELIMITER $$
USE `KRONOS`$$
create procedure aMSearchAccords()
begin
select ACCNUMBER, INCORDATE, 
DEADLINE, SESSIONDATE, TYPE_ID,T_TYPE.DESCRIPTION AS TYPE_DESC, OBSERVATIONS, PUBLIC, NOTIFIED,  STATE,T_STATE.DESCRIPTION AS STATE_DESC,  T_ACCPDF.URL, T_ACCPDF.FINALRESPONSE AS FINALRESPONSE, T_ACCPDF.ISAPPROVED AS ISAPPROVED, T_ACCPDF.CAN_DELETE AS CAN_DELETE   from T_ACCORD, T_ACCPDF,T_STATE, T_TYPE where T_ACCORD.ACCNUMBER= T_ACCPDF.ACCORD AND T_ACCORD.STATE=T_STATE.ID AND T_ACCORD.TYPE_ID='A';
end$$
DELIMITER ;  

USE `KRONOS`;
DROP procedure IF EXISTS sessionDates;
DELIMITER $$
USE `KRONOS`$$
create procedure sessionDates()
begin
select SESSIONDATE from T_ACT where SESSIONTYPE IS NULL;
end$$
DELIMITER ;

USE `KRONOS`;
DROP procedure IF EXISTS addAct;
DELIMITER $$
USE `KRONOS`$$
create procedure addAct(in _sessionType varchar(45), in _sessionDate date, in _url varchar(100), in _public tinyint, in _active tinyint)
begin
update T_ACT set T_ACT.SESSIONTYPE = _sessionType, T_ACT.URL = _url, T_ACT.PUBLIC = _public, T_ACT.ACTIVE = _active where SESSIONDATE = _sessionDate;
end$$
DELIMITER ;


USE `KRONOS`;
DROP procedure IF EXISTS getActByDate;
DELIMITER $$
USE `KRONOS`$$
create procedure getActByDate(in _sessiondate date)
begin
select SESSIONTYPE, SESSIONDATE, URL, T_ACT.PUBLIC, ACTIVE from T_ACT where SESSIONDATE=_sessiondate;
end$$
DELIMITER ;

USE `KRONOS`;
DROP procedure IF EXISTS getActsByDate;
DELIMITER $$
USE `KRONOS`$$
create procedure getActsByDate(in _sessiondate date)
begin
select SESSIONTYPE, SESSIONDATE, URL, T_ACT.PUBLIC, ACTIVE from T_ACT where SESSIONDATE=_sessiondate and ACTIVE=1;
end$$
DELIMITER ;


USE `KRONOS`;
DROP procedure IF EXISTS searchAllActs;
DELIMITER $$
USE `KRONOS`$$
create procedure searchAllActs()
begin 
select SESSIONTYPE, SESSIONDATE, URL, T_ACT.PUBLIC, ACTIVE from T_ACT where ACTIVE=1;
end$$
DELIMITER ;

USE `KRONOS`;
DROP procedure IF EXISTS getActByType;
DELIMITER $$
USE `KRONOS`$$
create procedure getActByType(in _type varchar(45))
begin
select SESSIONTYPE, SESSIONDATE, URL, T_ACT.PUBLIC, ACTIVE from T_ACT where SESSIONTYPE= _type and ACTIVE=1;
end$$
DELIMITER ;

USE `KRONOS`;
DROP procedure IF EXISTS updatePdfAct;
DELIMITER $$
USE `KRONOS`$$
create procedure updatePdfAct(in _date date, in _url varchar(100))
begin 
update T_ACT set T_ACT.URL = _url where SESSIONDATE= _date;
end$$
DELIMITER ;

USE `KRONOS`;
DROP procedure IF EXISTS updateAct;
DELIMITER $$
USE `KRONOS`$$
create procedure updateAct(in _type varchar(45), in _sessiondate date, in _public tinyint, in _active tinyint)
begin
update T_ACT set T_ACT.SESSIONTYPE = _type, T_ACT.ACTIVE = _active, T_ACT.PUBLIC=_public where SESSIONDATE=_sessiondate;
end$$
DELIMITER ;

 



USE `KRONOS`;
DROP procedure IF EXISTS updateAccordState;
DELIMITER $$
USE `KRONOS`$$
create procedure updateAccordState(in _accord varchar(45) ,in _state int)
begin
update T_ACCORD set STATE = _state where ACCNUMBER=_accord;
end$$
DELIMITER ; 

USE `KRONOS`;
DROP procedure IF EXISTS getBossAccords;
DELIMITER $$
USE `KRONOS`$$
create procedure getBossAccords(in _user varchar(45))
begin
select T_ACCORD.ACCNUMBER, T_ACCORD.INCORDATE, 
T_ACCORD.DEADLINE, T_ACCORD.SESSIONDATE, T_ACCORD.TYPE_ID,T_TYPE.DESCRIPTION AS TYPE_DESC, T_ACCORD.OBSERVATIONS, T_ACCORD.PUBLIC, T_ACCORD.NOTIFIED,  T_ACCORD.STATE,T_STATE.DESCRIPTION AS STATE_DESC,  T_ACCPDF.URL, T_ACCPDF.FINALRESPONSE AS FINALRESPONSE, T_ACCPDF.ISAPPROVED AS ISAPPROVED, T_ACCPDF.CAN_DELETE AS CAN_DELETE  from T_ACCORD, T_ACCPDF,T_STATE, T_TYPE, T_ACCDPRMNT,T_USER
where T_USER.TEMPUSER=_user AND T_USER.DEPARTMENT=T_ACCDPRMNT.DEPARTMENT AND T_ACCDPRMNT.ACCORD=T_ACCORD.ACCNUMBER AND T_ACCORD.ACCNUMBER= T_ACCPDF.ACCORD AND
T_ACCORD.STATE=T_STATE.ID AND T_ACCORD.TYPE_ID=T_TYPE.ID AND T_ACCORD.STATE=3;
end$$
DELIMITER ; 


USE `KRONOS`;
DROP procedure IF EXISTS getFinishedBossAccords;
DELIMITER $$
USE `KRONOS`$$
create procedure getFinishedBossAccords(in _user varchar(45))
begin
select T_ACCORD.ACCNUMBER, T_ACCORD.INCORDATE, 
T_ACCORD.DEADLINE, T_ACCORD.SESSIONDATE, T_ACCORD.TYPE_ID,T_TYPE.DESCRIPTION AS TYPE_DESC, T_ACCORD.OBSERVATIONS, T_ACCORD.PUBLIC, T_ACCORD.NOTIFIED,  T_ACCORD.STATE,T_STATE.DESCRIPTION AS STATE_DESC,  T_ACCPDF.URL, T_ACCPDF.FINALRESPONSE AS FINALRESPONSE, T_ACCPDF.ISAPPROVED AS ISAPPROVED, T_ACCPDF.CAN_DELETE AS CAN_DELETE  from T_ACCORD, T_ACCPDF,T_STATE, T_TYPE, T_ACCDPRMNT,T_USER
where T_USER.TEMPUSER=_user AND T_USER.DEPARTMENT=T_ACCDPRMNT.DEPARTMENT AND T_ACCDPRMNT.ACCORD=T_ACCORD.ACCNUMBER AND T_ACCORD.ACCNUMBER= T_ACCPDF.ACCORD AND
T_ACCORD.STATE=T_STATE.ID AND T_ACCORD.TYPE_ID=T_TYPE.ID AND (T_ACCORD.STATE=0 OR T_ACCORD.STATE=4);
end$$
DELIMITER ; 



USE `KRONOS`;
DROP procedure IF EXISTS getAllBossAccords;
DELIMITER $$
USE `KRONOS`$$
create procedure getAllBossAccords(in _user varchar(45))
begin
select T_ACCORD.ACCNUMBER, T_ACCORD.INCORDATE, 
T_ACCORD.DEADLINE, T_ACCORD.SESSIONDATE, T_ACCORD.TYPE_ID,T_TYPE.DESCRIPTION AS TYPE_DESC, T_ACCORD.OBSERVATIONS, T_ACCORD.PUBLIC, T_ACCORD.NOTIFIED,  T_ACCORD.STATE,T_STATE.DESCRIPTION AS STATE_DESC,  T_ACCPDF.URL, T_ACCPDF.FINALRESPONSE AS FINALRESPONSE,T_ACCPDF.ISAPPROVED AS ISAPPROVED, T_ACCPDF.CAN_DELETE AS CAN_DELETE  from T_ACCORD, T_ACCPDF,T_STATE, T_TYPE, T_ACCDPRMNT,T_USER
where T_USER.TEMPUSER=_user AND T_USER.DEPARTMENT=T_ACCDPRMNT.DEPARTMENT AND T_ACCDPRMNT.ACCORD=T_ACCORD.ACCNUMBER AND T_ACCORD.ACCNUMBER= T_ACCPDF.ACCORD AND
T_ACCORD.STATE=T_STATE.ID AND T_ACCORD.TYPE_ID=T_TYPE.ID;
end$$
DELIMITER ; 





USE `KRONOS`;
DROP procedure IF EXISTS getAllTypesId;
DELIMITER $$
USE `KRONOS`$$
create procedure getAllTypesId()
begin
select T_TYPE.ID from T_TYPE order by T_TYPE.ID;
end$$
DELIMITER ; 

USE `KRONOS`;
DROP procedure  IF EXISTS deactivatedActs;
DELIMITER $$
USE `KRONOS`$$
create procedure deactivatedActs()
begin
select SESSIONTYPE, SESSIONDATE, URL, T_ACT.PUBLIC, ACTIVE from T_ACT where ACTIVE=0;
end$$
DELIMITER ; 

USE `KRONOS`;
DROP procedure IF EXISTS lastIdInType;
DELIMITER $$
USE `KRONOS`$$
create procedure lastIdInType()
begin
select MAX(ID) from T_TYPE;
end$$
DELIMITER ;


alter table T_ROLE auto_increment = 1;
alter table T_DEPARTMENT auto_increment = 1;

insert into T_TYPE (DESCRIPTION) values ('Administración Municipal');
insert into T_TYPE (DESCRIPTION) values ('Auditoria Interna');
insert into T_TYPE (DESCRIPTION) values ('Lic');
insert into T_TYPE (DESCRIPTION) values ('Obras');
insert into T_TYPE (DESCRIPTION) values ('Plan Regulador');
insert into T_TYPE (DESCRIPTION) values ('Hacienda');
insert into T_TYPE (DESCRIPTION) values ('Juridicos');
insert into T_TYPE (DESCRIPTION) values ('Sociales');
insert into T_TYPE (DESCRIPTION) values ('Ambiente');
insert into T_TYPE (DESCRIPTION) values ('Varios');


insert into T_STATE (ID, DESCRIPTION)values (0, 'Cumplido');
insert into T_STATE (ID, DESCRIPTION)values (1, 'Incumplido');
insert into T_STATE (ID, DESCRIPTION)values (2, 'Pendiente');
insert into T_STATE (ID, DESCRIPTION)values (3, 'Recibido');
insert into T_STATE (ID, DESCRIPTION)values (4, 'Desestimado');
insert into T_STATE (ID, DESCRIPTION)values (5, 'Vencido');

insert into T_TEMPUSER(NAME,EMAIL) values ('SUPERUSER','superuser@superuser.com');
insert into T_TEMPUSER(NAME,EMAIL) values ('Concejo Municipal','concejomunicipal@sanpablo.go.cr');
insert into T_DEPARTMENT (NAME) values ('SUPERUSER'); # 1
insert into T_DEPARTMENT (NAME) values ('Concejo Municipal'); #2
insert into T_DEPARTMENT (NAME) values ('Alcaldia'); #3
insert into T_DEPARTMENT (NAME) values ('Desarrollo Urbano');#4
insert into T_DEPARTMENT (NAME) values ('Hacienda Municipal'); #5
insert into T_DEPARTMENT (NAME) values ('Servicios Públicos'); #6
insert into T_DEPARTMENT (NAME) values ('Prooveduría'); #7
insert into T_DEPARTMENT (NAME) values ('Recursos Humanos'); #8


insert into T_USER (TEMPUSER,PASSWORD,DEPARTMENT,STATUS) values ('superuser@superuser.com','$2y$12$N6OQ0DsyRhYOq/m9AK7GzePyjLGsmiUM3ax0z3xIAFF40vjTgL73q',1,1);

insert into T_USER (TEMPUSER,PASSWORD,DEPARTMENT,STATUS) values ('concejomunicipal@sanpablo.go.cr','$2a$10$iCDiliiLJjGNB93sNBc.be6suYV/B.2KeklGnEnuRsDzKC2l79bV2',2,1);
insert into T_USER (TEMPUSER,PASSWORD,DEPARTMENT,STATUS) values ('alcaldia@sanpablo.go.cr','$2a$10$VJnqKpTeW7FLaSf6/eI6..4w6IAUOVUoVSicZbkGDgpHV2ajFaAny',3,1);
insert into T_ROLE (NAME) values ('superuser');
insert into T_ROLE (NAME) values ('Concejo Municipal');
insert into T_ROLE (NAME) values ('Secretaria de Alcaldia');
insert into T_ROLE (NAME) values('Infraestructura Privada');
insert into T_ROLE (NAME) values('Infraestructura Pública');
insert into T_ROLE (NAME) values('Dirección de Desarrollo humano');
insert into T_ROLE (NAME) values('Dirección de hacienda Muninicipal');
insert into T_ROLE (NAME) values('Dirección de Servicios Públicos');
insert into T_ROLE (NAME) values('Inspector');
insert into T_ROLE (NAME) values('Planificación y Ordenamiento territorial');
insert into T_ROLE (NAME) values('Contabilidad');
insert into T_ROLE (NAME) values('Auxiliar de Contabilidad');
insert into T_ROLE (NAME) values('Gestión de Cobro');
insert into T_ROLE (NAME) values('Tesorería');
insert into T_ROLE (NAME) values('Cajas');
insert into T_ROLE (NAME) values('Valoración de Bienes Inmuebles');
insert into T_ROLE (NAME) values('Perito');
insert into T_ROLE (NAME) values('Archivo Central');
insert into T_ROLE (NAME) values('Centro de Atención Adulto Mayor Miraflores');
insert into T_ROLE (NAME) values('Centro de Atención Adulto Mayor R_d_Ricardo');
insert into T_ROLE (NAME) values('CECUDI');
insert into T_ROLE (NAME) values('Cementerio');
insert into T_ROLE (NAME) values('Centro de Conocimiento (Biblioteca)');
insert into T_ROLE (NAME) values('Centro Cultural');
insert into T_ROLE (NAME) values('Desarrollo social inclusivo');
insert into T_ROLE (NAME) values('Gestión Ambiental');
insert into T_ROLE (NAME) values('Plataforma de Servicios');
insert into T_ROLE (NAME) values('Plataformista');
insert into T_ROLE (NAME) values('Policía Municipal');
insert into T_ROLE (NAME) values('Proveeduría');
insert into T_ROLE (NAME) values('Subproveedora');
insert into T_ROLE (NAME) values('Bodega');
insert into T_ROLE (NAME) values('Planillas');
insert into T_ROLE (NAME) values('Salud Ocupacional');
insert into T_ROLE (NAME) values('Alcalde');
insert into T_ROLE (NAME) values('Vice Alcalde 1');
insert into T_ROLE (NAME) values('Director de asesoria Legal interna');
insert into T_ROLE (NAME) values('Asesoria Legal interna');
insert into T_ROLE (NAME) values('Tecnologías de Información');
insert into T_ROLE (NAME) values('Planificación, Presupuesto y Control');
insert into T_ROLE (NAME) values('Control Interno');
insert into T_ROLE (NAME) values('Recursos Humanos');

insert into T_USER_ROLE (USER_ID,ROLE_NAME) values ('concejomunicipal@sanpablo.go.cr','Concejo Municipal');
insert into T_USER_ROLE (USER_ID,ROLE_NAME) values ('alcaldia@sanpablo.go.cr','Secretaria de Alcaldia');
insert into T_USER_ROLE (USER_ID,ROLE_NAME) values ('superuser@superuser.com','superuser');

#Direccion de desarrorllo urbano
insert into T_TEMPUSER(NAME,EMAIL) values ('Santiago Baizán Hidalgo','desarrollourbano@sanpablo.go.cr');
insert into T_TEMPUSER(NAME,EMAIL) values ('Allan Alfaro Arias','infraestructuraprivada@sanpablo.go.cr');
insert into T_TEMPUSER(NAME,EMAIL) values ('Oscar Campos Garita','infraestructurapublica@sanpablo.go.cr');
insert into T_TEMPUSER(NAME,EMAIL) values ('Miguel Cortés Sánchez','planot@sanpablo.go.cr');
insert into T_TEMPUSER(NAME,EMAIL) values ('Jorge Duarte Ramírez','visados@sanpablo.go.cr');

insert into T_USER (TEMPUSER,PASSWORD,DEPARTMENT,STATUS,ISBOSS) values ('desarrollourbano@sanpablo.go.cr','$2y$12$GXf2OJ06OcHRFoujC7DscewFa8AYqrhlnSd6xWsyb0Z/m5YoNKKDu',4,1,1); # pass:desarrollourbano
insert into T_USER_ROLE (USER_ID,ROLE_NAME) values('desarrollourbano@sanpablo.go.cr','Dirección de Desarrollo humano');

insert into T_USER (TEMPUSER,PASSWORD,DEPARTMENT,STATUS) values ('infraestructuraprivada@sanpablo.go.cr','$2y$12$RYOpOCvwQrJV65JXK5qrD.X/ZYVsm8sVO5nNzBx7dB7QDwlMyhhPe',4,1); # pass:infraestructuraprivada
insert into T_USER_ROLE (USER_ID,ROLE_NAME) values('infraestructuraprivada@sanpablo.go.cr','Infraestructura Privada');

insert into T_USER (TEMPUSER,PASSWORD,DEPARTMENT,STATUS) values ('infraestructurapublica@sanpablo.go.cr','$2y$12$T3Vb33ERjSk6bdczS7M0i.72Mve0z544LAs1Lo3Hj4da8pAIgKdqO',4,1); # pass:infraestructurapublica
insert into T_USER_ROLE (USER_ID,ROLE_NAME) values('infraestructurapublica@sanpablo.go.cr','Infraestructura Pública');

insert into T_USER (TEMPUSER,PASSWORD,DEPARTMENT,STATUS) values ('planot@sanpablo.go.cr','$2y$12$toG1PoV6sDm8PSvXBA6fk.FgFNNbZb19OvSP2nLRWeRBGgjSmTFC.',4,1); # pass:planot
insert into T_USER_ROLE (USER_ID,ROLE_NAME) values('planot@sanpablo.go.cr','Planificación y Ordenamiento territorial');

insert into T_USER (TEMPUSER,PASSWORD,DEPARTMENT,STATUS) values ('visados@sanpablo.go.cr','$2y$12$0DALv6Kh4s5teatCpOPgmeAA3EbEbm3RYbdtPfn4dHvkmoypMRewK',4,1); # pass:visados
insert into T_USER_ROLE (USER_ID,ROLE_NAME) values('visados@sanpablo.go.cr','Planificación y Ordenamiento territorial');


# Dirección de Hacienda Municipal

insert into T_TEMPUSER(NAME,EMAIL) values ('Marjorie Montoya Gamboa','haciendamunicipal@sanpablo.go.cr');
insert into T_TEMPUSER(NAME,EMAIL) values ('Gilberth Chávez','contabilidad@sanpablo.go.cr');
insert into T_TEMPUSER(NAME,EMAIL) values ('Alonso Valerio','auxiliarcontabilidad@go.cr');
insert into T_TEMPUSER(NAME,EMAIL) values ('Carolina González Valerio','gestiondecobros@sanpablo.go.cr');
insert into T_TEMPUSER(NAME,EMAIL) values ('Johnny Cabalceta Ramírez','asistente.licencias@sanpablo.go.cr');
insert into T_TEMPUSER(NAME,EMAIL) values ('Yael Solano Méndez','tesoreria@sanpablo.go.cr');
insert into T_TEMPUSER(NAME,EMAIL) values ('Juan Carlos Zúñiga Jimenez','valoracionbienesinmuebles@sanpablo.go.cr');
insert into T_TEMPUSER(NAME,EMAIL) values ('Marcial Alpízar Gutiérrez','perito.a@sanpablo.go.cr');
insert into T_TEMPUSER(NAME,EMAIL) values ('Carlo Arias Villalobos','perito.b@sanpablo.go.cr');

insert into T_USER (TEMPUSER,PASSWORD,DEPARTMENT,STATUS,ISBOSS) values ('haciendamunicipal@sanpablo.go.cr','$2y$12$xKiBSkIQYGrIcwZbWKquPesSlc88TQFMdv8f.x2.L5psgGW7T7Hr6',5,1,1); # pass:haciendamunicipal
insert into T_USER_ROLE (USER_ID,ROLE_NAME) values('haciendamunicipal@sanpablo.go.cr','Dirección de hacienda Muninicipal');

insert into T_USER (TEMPUSER,PASSWORD,DEPARTMENT,STATUS) values ('contabilidad@sanpablo.go.cr','$2y$12$cV2PKROtqybto5TUqJizbOXOF7I5UX04g8n6ByCPRrRbBBZQYQajW',5,1); # pass:contabilidad
insert into T_USER_ROLE (USER_ID,ROLE_NAME) values('contabilidad@sanpablo.go.cr','Contabilidad');

insert into T_USER (TEMPUSER,PASSWORD,DEPARTMENT,STATUS) values ('auxiliarcontabilidad@go.cr','$2y$12$BX.plgUXeQwNWHYPTOp6lexiUOkAMFvhmdRJtPxDS8ftKTZ5kDCdO',5,1); # pass:auxiliarcontabilidad
insert into T_USER_ROLE (USER_ID,ROLE_NAME) values('auxiliarcontabilidad@go.cr','Auxiliar de Contabilidad');

insert into T_USER (TEMPUSER,PASSWORD,DEPARTMENT,STATUS) values ('gestiondecobros@sanpablo.go.cr','$2y$12$uKQG2afsHC0YzG31cQ6WWuDCsbrwBCKINOeeAaN6QYiBiHJFEz2cS',5,1); # pass:gestiondecobros
insert into T_USER_ROLE (USER_ID,ROLE_NAME) values('gestiondecobros@sanpablo.go.cr','Gestión de Cobro');

insert into T_USER (TEMPUSER,PASSWORD,DEPARTMENT,STATUS) values ('asistente.licencias@sanpablo.go.cr','$2y$12$3Xo2qB3jzTnOIEzISWy6fedQviOzz06Fvw39HV0hr1OfnW5WidQ3K',5,1); # pass:asistentelicencias
insert into T_USER_ROLE (USER_ID,ROLE_NAME) values('asistente.licencias@sanpablo.go.cr','Gestión de Cobro');

insert into T_USER (TEMPUSER,PASSWORD,DEPARTMENT,STATUS) values 
('tesoreria@sanpablo.go.cr','$2y$12$ouEFyuz/OSgnXwGJvUJefOVWD0vMZZUNlKDhXodUvi.Lp/X2cD0eG',5,1); # pass:tesoreria
insert into T_USER_ROLE (USER_ID,ROLE_NAME) values('tesoreria@sanpablo.go.cr','Tesorería');

insert into T_USER (TEMPUSER,PASSWORD,DEPARTMENT,STATUS) values 
('valoracionbienesinmuebles@sanpablo.go.cr','$2y$12$ppPziOR/CmJMYR2lPRPwGOlTLK.mqNLX/Quzmp0dVz40QcDOhXl5u',5,1); # pass:valoracionbienesinmuebles
insert into T_USER_ROLE (USER_ID,ROLE_NAME) values('valoracionbienesinmuebles@sanpablo.go.cr','Valoración de Bienes Inmuebles');

insert into T_USER (TEMPUSER,PASSWORD,DEPARTMENT,STATUS) values
 ('perito.a@sanpablo.go.cr','$2y$12$YADFgLFA.Vv6yqunsaondeCqUKx7wtb9hmcA5MKl//92464/6VTRu',5,1); # pass:peritoa
insert into T_USER_ROLE (USER_ID,ROLE_NAME) values('perito.a@sanpablo.go.cr','Perito');

insert into T_USER (TEMPUSER,PASSWORD,DEPARTMENT,STATUS) values
 ('perito.b@sanpablo.go.cr','$2y$12$Kb5P5AMYk/ojENbjI9BXX.z1IzEbwaMcUSF0HIghxy6QIdRY4hQIa',5,1); # pass:peritob
insert into T_USER_ROLE (USER_ID,ROLE_NAME) values('perito.b@sanpablo.go.cr','Perito');


#####- Servicios Publicos #####-

insert into T_TEMPUSER(NAME,EMAIL) values('Mauricio González González','serviciospublicos@sanpablo.go.cr');
insert into T_TEMPUSER(NAME,EMAIL) values('José Silva Castillo','archivocentral@sanpablo.go.cr');
insert into T_TEMPUSER(NAME,EMAIL) values('Lizbeth Rodríguez Calderón','centroatencionadultomayor@sanpablo.go.cr');
insert into T_TEMPUSER(NAME,EMAIL) values('Karla Araya Núñez','cecudi@sanpablo.go.cr');
insert into T_TEMPUSER(NAME,EMAIL) values('Vanessa Valverde','cementerio@sanpablo.go.cr');
insert into T_TEMPUSER(NAME,EMAIL) values('Jennifer Conejo Vásquez','centrodeconocimiento@sanpablo.go.cr');
insert into T_TEMPUSER(NAME,EMAIL) values('Sergio Salazar Rivera','administradorcentrocultural@sanpablo.go.cr');
insert into T_TEMPUSER(NAME,EMAIL) values('Ileana Rojas Hernández','centrocultural@sanpablo.go.cr');
insert into T_TEMPUSER(NAME,EMAIL) values('Yamileth Monterey López','desarrollosocialinclusivo@sanpablo.go.cr');
insert into T_TEMPUSER(NAME,EMAIL) values('David González Ovares','gestionambiental@sanpablo.go.cr');
insert into T_TEMPUSER(NAME,EMAIL) values('Marcela Sancho Villalobos','plataforma@sanpablo.go.cr');
insert into T_TEMPUSER(NAME,EMAIL) values('Maureen Gutiérrez','plataforma.a@sanpablo.go.cr');
insert into T_TEMPUSER(NAME,EMAIL) values('Carlos García Arguedas','plataforma.b@sanpablo.go.cr');
insert into T_TEMPUSER(NAME,EMAIL) values('Patricia Zúñiga','plataforma.c@sanpablo.go.cr');
insert into T_TEMPUSER(NAME,EMAIL) values('Luis Moncada Espinoza','policiamunicipal@sanpablo.go.cr');



insert into T_USER (TEMPUSER,PASSWORD,DEPARTMENT,STATUS,ISBOSS) values
 ('serviciospublicos@sanpablo.go.cr','$2y$12$sv9mXszFFms8Vsp2gtJ21eJcJKq/dd7aTdB35Q6QHmqtynRdAsruW',6,1,1); # pass:serviciospublicos
insert into T_USER_ROLE (USER_ID,ROLE_NAME) values('serviciospublicos@sanpablo.go.cr','Dirección de Servicios Públicos');

insert into T_USER (TEMPUSER,PASSWORD,DEPARTMENT,STATUS) values
 ('archivocentral@sanpablo.go.cr','$2y$12$9knVYUk7M6gUmt9XIdHO/OMWZURJOmlAp8hcX/pNCtoRhiz6xOSqm',6,1); # pass:archivocentral
insert into T_USER_ROLE (USER_ID,ROLE_NAME) values('archivocentral@sanpablo.go.cr','Archivo Central');

insert into T_USER (TEMPUSER,PASSWORD,DEPARTMENT,STATUS) values
 ('centroatencionadultomayor@sanpablo.go.cr','$2y$12$qPjPC8ijCMmOezlddMxA5e5tSiQ/ekgB7DMBiLj.L9xr6/kOv2MvC',6,1); # pass:centroatencionadultomayor
insert into T_USER_ROLE (USER_ID,ROLE_NAME) values('centroatencionadultomayor@sanpablo.go.cr','Centro de Atención Adulto Mayor Miraflores');

insert into T_USER (TEMPUSER,PASSWORD,DEPARTMENT,STATUS) values
 ('cecudi@sanpablo.go.cr','$2y$12$I9z7f6EL6mv6HYp37TEGOOetQLGKo9ld8VGvFAt4JxkCStmb6yhii',6,1); # pass:cecudi
insert into T_USER_ROLE (USER_ID,ROLE_NAME) values('cecudi@sanpablo.go.cr','CECUDI');

insert into T_USER (TEMPUSER,PASSWORD,DEPARTMENT,STATUS) values
 ('cementerio@sanpablo.go.cr','$2y$12$6cQe24xuooaWHhDAI27ouu67ZKWcSwYizRsflPK34/oBSlvKoZRCy',6,1); # pass:cementerio
insert into T_USER_ROLE (USER_ID,ROLE_NAME) values('cementerio@sanpablo.go.cr','Cementerio');

insert into T_USER (TEMPUSER,PASSWORD,DEPARTMENT,STATUS) values
 ('centrodeconocimiento@sanpablo.go.cr','$2y$12$XUhvsKDnttd8plXdBtHSY./YTy/GcOdZ16upoEj/EUAxdb6Tg.Qje',6,1); # pass:centrodeconocimiento
insert into T_USER_ROLE (USER_ID,ROLE_NAME) values('centrodeconocimiento@sanpablo.go.cr','Centro de Conocimiento (Biblioteca)');

insert into T_USER (TEMPUSER,PASSWORD,DEPARTMENT,STATUS) values
 ('administradorcentrocultural@sanpablo.go.cr','$2y$12$H0OPp7/F4r3fzwYdZg.feuCs4nqk8mBUeZUi.AkGEsBWgXWMD4Cqa',6,1); # pass:administradorcentrocultural
insert into T_USER_ROLE (USER_ID,ROLE_NAME) values('administradorcentrocultural@sanpablo.go.cr','Centro Cultural');

insert into T_USER (TEMPUSER,PASSWORD,DEPARTMENT,STATUS) values
 ('desarrollosocialinclusivo@sanpablo.go.cr','$2y$12$j7Oe/V2VvZF1rFx5GMBwDesCdlGfFk4nMTA.dOv3Hzd6Yig4TfmF.',6,1); # pass:desarrollosocialinclusivo
insert into T_USER_ROLE (USER_ID,ROLE_NAME) values('desarrollosocialinclusivo@sanpablo.go.cr','Desarrollo social inclusivo');

insert into T_USER (TEMPUSER,PASSWORD,DEPARTMENT,STATUS) values
 ('gestionambiental@sanpablo.go.cr','$2y$12$/aRrJ2B6P5U1Z7cf6R.a9OMoZXuD/Jj8rezPd.2VYgkbQaQy4NcTC',6,1); # pass:gestionambiental
insert into T_USER_ROLE (USER_ID,ROLE_NAME) values('gestionambiental@sanpablo.go.cr','Gestión Ambiental');

insert into T_USER (TEMPUSER,PASSWORD,DEPARTMENT,STATUS) values
 ('plataforma@sanpablo.go.cr','$2y$12$/zUT.lk5a4HgdlnsdZteL.of/cq0vcrZ2FKBp87vCrLH7NO6K67ei',6,1); # pass:plataforma
insert into T_USER_ROLE (USER_ID,ROLE_NAME) values('plataforma@sanpablo.go.cr','Plataforma de Servicios');

insert into T_USER (TEMPUSER,PASSWORD,DEPARTMENT,STATUS) values
 ('plataforma.a@sanpablo.go.cr','$2y$12$vbOYKP4afxEYdzZvDGgyuegCt3iwXTDEAHrFGjqW6Udn9A3ojTlfq',6,1); # pass:plataformaa
insert into T_USER_ROLE (USER_ID,ROLE_NAME) values('plataforma.a@sanpablo.go.cr','Plataformista');

insert into T_USER (TEMPUSER,PASSWORD,DEPARTMENT,STATUS) values
 ('plataforma.b@sanpablo.go.cr','$2y$12$3pm1JS0KZ9Fq6p3td1c0PONKBlIIzq832CbOIobqa4GXYftMT3zY2',6,1); # pass:plataformab
insert into T_USER_ROLE (USER_ID,ROLE_NAME) values('plataforma.b@sanpablo.go.cr','Plataformista');

insert into T_USER (TEMPUSER,PASSWORD,DEPARTMENT,STATUS) values
 ('plataforma.c@sanpablo.go.cr','$2y$12$3yoRAm7cqUjuunPtngwQQuBavPzCPi08eabm6VlKCZSeXO.rOdhr6',6,1); # pass:plataformac
insert into T_USER_ROLE (USER_ID,ROLE_NAME) values('plataforma.c@sanpablo.go.cr','Plataformista');

insert into T_USER (TEMPUSER,PASSWORD,DEPARTMENT,STATUS) values
 ('policiamunicipal@sanpablo.go.cr','$2y$12$C1F.BejEy.AJjkK6JU3mSuJ2HPFyTaaYgIvA1uRs/kmIxnAMoiXhS',6,1); # pass:policiamunicipal
insert into T_USER_ROLE (USER_ID,ROLE_NAME) values('policiamunicipal@sanpablo.go.cr','Policía Municipal');










#### Proveduria #####
insert into T_TEMPUSER(NAME,EMAIL) values('Oscar Hidalgo Mena','proveeduria1@sanpablo.go.cr');
insert into T_TEMPUSER(NAME,EMAIL) values('Adriana Benavides Vargas','proveeduria2@sanpablo.go.cr');

insert into T_USER (TEMPUSER,PASSWORD,DEPARTMENT,STATUS,ISBOSS) values
 ('proveeduria1@sanpablo.go.cr','$2y$12$glSbeA/7oQ4ppjMjQH7I1.LmnqL7GJMMhJWXpBPAr90LaWvjQW3Hy',7,1,1); #pass:proveeduria1
insert into T_USER_ROLE (USER_ID,ROLE_NAME) values('proveeduria1@sanpablo.go.cr','Proveeduría');

insert into T_USER (TEMPUSER,PASSWORD,DEPARTMENT,STATUS) values
 ('proveeduria2@sanpablo.go.cr','$2y$12$Kx0iSLCqJXyIzVqDub0x0.bu89dBP1l8bD4/uVWPtWw4Mgs0YRrla',7,1); #pass:proveeduria2
insert into T_USER_ROLE (USER_ID,ROLE_NAME) values('proveeduria2@sanpablo.go.cr','Subproveedora');




##Recursos Humanos####

insert into T_TEMPUSER(NAME,EMAIL) values('Diana Arias','recursoshumanos@sanpablo.go.cr');
insert into T_TEMPUSER(NAME,EMAIL) values('Planillas','nomina@sanpablo.go.cr');
insert into T_TEMPUSER(NAME,EMAIL) values('Jorge Slon Jaikel','saludocupacional@sanpablo.go.cr');

insert into T_USER (TEMPUSER,PASSWORD,DEPARTMENT,STATUS,ISBOSS) values
 ('recursoshumanos@sanpablo.go.cr','$2y$12$erDRhHdAorRpNTfzRddr6uX1D.BBhBbbhyyY6g0C.lQMCWwYRy8G6',8,1,1); #pass:recursoshumanos
insert into T_USER_ROLE (USER_ID,ROLE_NAME) values('recursoshumanos@sanpablo.go.cr','Recursos Humanos');

insert into T_USER (TEMPUSER,PASSWORD,DEPARTMENT,STATUS) values
 ('nomina@sanpablo.go.cr','$2y$12$J3UeKhGvzHuskW9b0Gq3LObvXcaB85XHLZvIvOv.vgCgHHPEXa/ke',8,1); #pass:nomina
insert into T_USER_ROLE (USER_ID,ROLE_NAME) values('nomina@sanpablo.go.cr','Planillas');

insert into T_USER (TEMPUSER,PASSWORD,DEPARTMENT,STATUS) values
 ('saludocupacional@sanpablo.go.cr','$2y$12$BE3FgDmRKoMT2i2xuY2ZiODIxcvu7kj8Kq5zAjmZwufmEfDRX7m.W',8,1); #pass:saludocupacional
insert into T_USER_ROLE (USER_ID,ROLE_NAME) values('saludocupacional@sanpablo.go.cr','Salud Ocupacional');




insert into T_TEMPUSER(NAME,EMAIL) values('Luis Fernando Vargas Mora','asesorialegal@sanpablo.go.cr');
insert into T_TEMPUSER(NAME,EMAIL) values('Joseph Granda Vargas','informatica@sanpablo.go.cr');
# insert into T_TEMPUSER(NAME,EMAIL) values('Gilberth Acuña Cerdas','planificacionpresupuestocontrol@sanpablo.go.cr');
insert into T_TEMPUSER(NAME,EMAIL) values('Ismael Salazar Oviedo','controlinterno@sanpablo.go.cr');

insert into T_USER (TEMPUSER,PASSWORD,DEPARTMENT,STATUS) values
 ('asesorialegal@sanpablo.go.cr','$2y$12$w7UGsHJM0NfW/xQROeg8x.xjX.S1XYs7F1gFccHcvqy.b6k7NjOxK',3,1); #pass:asesorialegal
insert into T_USER_ROLE (USER_ID,ROLE_NAME) values('asesorialegal@sanpablo.go.cr','Director de asesoria Legal interna');

insert into T_USER (TEMPUSER,PASSWORD,DEPARTMENT,STATUS) values
 ('informatica@sanpablo.go.cr','$2y$12$B3VstPqR8DzS4xcVMv.2ZukKtskcNW5cDxL4vYN0XqHi4MgSOq0uC',3,1); #pass:informatica
insert into T_USER_ROLE (USER_ID,ROLE_NAME) values('informatica@sanpablo.go.cr','Tecnologías de Información');

#insert into T_USER (TEMPUSER,PASSWORD,DEPARTMENT,STATUS) values
# ('planificacionpresupuestocontrol@sanpablo.go.cr','$2y$12$ywJ1xt5cpjgt3.JBKHr9teDSyq8xAlK4oi2uyTxnJmyBe.hd9IWeq',3,1); 
#  pass:planificacionpresupuestocontrol
#insert into T_USER_ROLE (USER_ID,ROLE_NAME) values('planificacionpresupuestocontrol@sanpablo.go.cr','Planificación, Presupuesto y Control');

insert into T_USER (TEMPUSER,PASSWORD,DEPARTMENT,STATUS) values
 ('controlinterno@sanpablo.go.cr','$2y$12$PP.ROST1M6niteGtzRK/f.qpbOOAkLk8eZXonQgJC1uKE9QdIlpsu',3,1); #pass:controlinterno
insert into T_USER_ROLE (USER_ID,ROLE_NAME) values('controlinterno@sanpablo.go.cr','Control Interno');


