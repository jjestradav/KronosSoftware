create database KRONOS;
use KRONOS;

create table T_STATE (
ID int,
DESCRIPTION varchar(45) NOT NULL, 
constraint PK_STATE primary key (ID)
);

create table T_TYPE (
ID int auto_increment, 
DESCRIPTION varchar(25) NOT NULL,
constraint PK_TYPE primary key (ID)
);

create table T_ACT(
INDX int auto_increment,
SESSIONTYPE varchar(45),
SESSIONDATE date NOT NULL, 
URL varchar(100),
PUBLIC tinyint,
ACTIVE tinyint,
constraint PK_ACT primary key (INDX),
constraint UK_SESSIONDATE unique key (SESSIONDATE)
);

create table T_DEPARTMENT (
ID int auto_increment, 
NAME varchar(100) NOT NULL,
constraint PK_DEPARMENT primary key(ID),
constraint UK_NAMED unique key(NAME)
);

create table T_TEMPUSER(
INDX int auto_increment,
NAME varchar(45) NOT NULL,
EMAIL varchar(45) NOT NULL,
constraint PK_TEMPUSER primary key (INDX),
constraint UK_EMAIL unique key(EMAIL)
); 

create table T_USER(
ID int auto_increment, 
DEPARTMENT int NOT NULL,
PASSWORD varchar(100) NOT NULL,
TEMPUSER varchar(45) NOT NULL,
STATUS tinyint NOT NULL,
ISBOSS tinyint NOT NULL DEFAULT false,
constraint PK_USER primary key (ID),
constraint FK_TEMPUSER foreign key (TEMPUSER) references T_TEMPUSER(EMAIL),
constraint FK_DEPARTMENT foreign key (DEPARTMENT) references T_DEPARTMENT(ID)
);


create table T_ACCORD(
INDX int auto_increment,
ACCNUMBER varchar(45),
INCORDATE date,
INCORTIME time,
DEADLINE date,
SESSIONDATE date,
OBSERVATIONS longtext,
PUBLIC tinyint,
NOTIFIED tinyint,
STATE int,
TYPE_ID int,
USER_ID varchar(45),
constraint PK_ACCORD primary key (INDX),
constraint FK_SESSIONDATE foreign key (SESSIONDATE) references T_ACT(SESSIONDATE),
constraint FK_TYPE foreign key (TYPE_ID) references T_TYPE(ID),
constraint FK_STATE foreign key (STATE) references T_STATE(ID),
constraint UK_ACCNUMBER unique key (ACCNUMBER),
constraint FK_USER foreign key (USER_ID) references T_USER(TEMPUSER)
);


create table T_ACCPDF(
ACCORD varchar(45),
URL varchar(100),
FINALRESPONSE boolean,
ISAPPROVED int,
CAN_DELETE boolean,
constraint FK_ACCORD foreign key (ACCORD) references T_ACCORD(ACCNUMBER)
);



create table T_USERACC(
TEMPUSER varchar(45),
ACCORD varchar(45),
constraint FK_TEMPUSER1 foreign key (TEMPUSER) references T_TEMPUSER(EMAIL),
constraint FK_ACCORD1 foreign key (ACCORD) references T_ACCORD(ACCNUMBER),
constraint PK_T_USERACC PRIMARY KEY(TEMPUSER,ACCORD)
);

create table T_NOTIFICATION(
ACCORD varchar(45),
USER varchar(45),
constraint FK_ACCORD2 foreign key(ACCORD) references T_ACCORD(ACCNUMBER),
constraint FK_USERNOTI foreign key (USER) references T_USER(TEMPUSER)
);


create table T_ACCDPRMNT(
DEPARTMENT int,
ACCORD varchar(45),
constraint FK_DEPARTMENT1 foreign key (DEPARTMENT) references T_DEPARTMENT(ID),
constraint FK_ACCORD3 foreign key (ACCORD) references T_ACCORD(ACCNUMBER)
);

create table T_ROLE(
ID int auto_increment,
NAME varchar(45),
constraint PK_ROLE primary key(ID),
constraint UK_NAME unique key(NAME)
);

create table T_USER_ROLE(
USER_ID varchar(45),
ROLE_NAME varchar(45),
constraint PK_ROLE primary key(USER_ID,ROLE_NAME),
constraint FK_USER_ROLE foreign key(USER_ID) references T_USER(TEMPUSER),
constraint FK_ROLE_UR foreign key(ROLE_NAME) references T_ROLE(NAME)

);

create table T_FORGOT_PASSWORD(
USER_ID varchar(45),
TOKEN varchar(6),
DATE_TIME datetime,
constraint FK_T_FORGOT foreign key (USER_ID) references T_USER(TEMPUSER)
);


USE `KRONOS`;
DELIMITER ;
CREATE TABLE IF NOT EXISTS `KRONOS`.`T_DELETEDACCORDS`(
ACCORDNUMBER varchar(45) NOT NULL,
USER varchar(45) NOT NULL,
Date DATETIME NOT NULL	
);




