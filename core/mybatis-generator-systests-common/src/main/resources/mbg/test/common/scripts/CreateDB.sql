drop table FieldsOnly if exists;
drop table PKOnly if exists;
drop table PKFields if exists;
drop table PKBlobs if exists;
drop table PKFieldsBlobs if exists;
drop table FieldsBlobs if exists;
drop table "awful table" if exists;
drop table BlobsOnly if exists;
drop view NameView if exists;
drop table RegexRename if exists;
drop table mbgtest.AnotherAwfulTable if exists;
drop table CompoundKey if exists;
drop schema mbgtest if exists;
drop table EnumTest if exists;
drop sequence TestSequence if exists;

create sequence TestSequence as integer start with 1;

create table FieldsOnly (
  IntegerField int,
  DoubleField double,
  FloatField float
);

create table PKOnly (
  id int not null,
  seq_num int not null,
  primary key(id, seq_num)
);

create table PKFields (
  id1 int not null,
  id2 int not null,
  firstName varchar(20),
  lastName varchar(20),
  dateField date,
  timeField time,
  timestampField timestamp,
  decimal30Field decimal(3, 0),
  decimal60Field decimal(6, 0),
  decimal100Field decimal(10, 0),
  decimal155Field decimal(15, 5),
  "wierd$Field" int,
  "birth date" date,
  stringBoolean char(1),
  primary key (id2, id1)
);

create table PKBlobs (
  id int not null,
  blob1 longvarbinary,
  blob2 longvarbinary,
  characterlob clob(5k),
  primary key (id)
);

create table PKFieldsBlobs (
  id1 int not null,
  id2 int not null,
  firstName varchar(20),
  lastName varchar(20),
  blob1 longvarbinary,
  primary key (id1, id2)
);

create table FieldsBlobs (
  firstName varchar(20),
  lastName varchar(20),
  blob1 longvarbinary,
  blob2 longvarbinary,
  blob3 binary
);

create table "awful table" (
  "CuStOmEr iD" int generated by default as identity (start with 57) not null,
  "first name" varchar(30) default 'Mabel',
  first_name varchar(30),
  firstName varchar(30),
  "last name" varchar(30),
  E_MAIL varchar(30),
  "_id1" int,
  "$id2" int,
  "id5_" int,
  "id6$" int,
  "id7$$" int,
  EmailAddress varchar(30),
  "from" varchar(30),
  active bit,
  class varchar(5),
  primary key("CuStOmEr iD")
);

-- this table should be ignored, nothing generated
create table BlobsOnly (
  blob1 longvarbinary,
  blob2 longvarbinary
);

create table RegexRename (
  CUST_ID integer not null,
  CUST_NAME varchar(30),
  CUST_ADDRESS varchar(30),
  ZIP_CODE char(5),
  primary key(CUST_ID)
);

create view NameView (id, name) as
  select CUST_ID, CUST_NAME from RegexRename;

create schema mbgtest;

create table mbgtest.AnotherAwfulTable (
  id int not null,
  "select" varchar(30),
  "insert" varchar(30),
  "update" varchar(30),
  "delete" varchar(30),
  primary key(id)
);

create table CompoundKey (
  id int not null,
  updateDate date not null,
  description varchar(30),
  primary key(id, updateDate)
);

create table EnumTest (
  id int not null,
  name varchar(20) not null,
  primary key(id)
);



-- Created by Mahiar Mody for creating and filling the test tables with data that the Jaxb Annotations plugin will use while testing.

drop table UserTutorial if exists;
drop table UserBlog if exists;
drop table UserPhotos if exists;
drop table UsersToSkills if exists;
drop table UserSkills if exists;
drop table Users if exists;


create table Users
(
  user_id INTEGER GENERATED ALWAYS AS IDENTITY(START WITH 1) not null,
  login varchar(50) not null,
  first_name varchar(100) not null,
  last_name varchar(100),
  password varchar(128) not null,
  is_active boolean default 'TRUE' not null,
  create_date timestamp default current_timestamp,
  constraint Users__user_id__PK PRIMARY KEY(user_id),
  constraint Users__login__UQ UNIQUE (login)
);

create table UserSkills
(
  skill_id smallInt GENERATED ALWAYS AS IDENTITY(START WITH 1) not null,
  skill varchar(20),
  is_active boolean default 'true' not null,
  constraint UserRole__skill_id__PK Primary Key (skill_id)
);

create table UsersToSkills
(
  user_id integer not null ,
  skill_id smallInt not null,
  constraint UsersToSkills__user_id__skill_id__PK Primary Key(user_id, skill_id),
  constraint UsersToSkills__user_id__FK Foreign key (user_id) References Users(user_id),
  constraint UsersToSkills__skill_id__FK Foreign Key (skill_id) References UserSkills(skill_id)
);

create table UserPhotos
(
  user_id integer not null,
  photo_title varchar(50),
  photo BLOB,
  photo_type varchar(10),
  constraint UserPhotos__user_id__FK Foreign Key (user_id) References Users(user_id)
);

create table UserTutorial
(
  user_tutorial_id int GENERATED ALWAYS AS IDENTITY(START WITH 1) not null,
  user_id integer not null,
  title varchar(100) not null,
  summary CLOB,
  narrative CLOB,
  video BLOB,
  video_type varchar(25),
  constraint UserTutorial__user_tutorial_id__PK Primary Key(user_tutorial_id),
  constraint UserTutorial__user_id__FK Foreign Key(user_id) references Users(user_id)
);

create table UserBlog
(
  user_id integer not null,
  blog_text CLOB,
  title varchar(40),
  constraint UserBlog__user_id__FK Foreign Key(user_id) references Users(user_id)
);



insert into Users(login, first_name, last_name, password) values ('mmody', 'Mahiar', 'Mody', '!@#$');
insert into UserSkills(skill) values ('Java');
insert into UsersToSkills(user_id, skill_id) values (1,1);
insert into UserPhotos(user_id, photo_title, photo, photo_type) values (1, 'Head Shot', X'0x440x540x33', 'png');

insert into UserTutorial(user_id, title, summary, narrative, video, video_type)
  values (1, 'MyBatis Generator Jaxb Plugin', convert('This is a large summary...', SQL_CLOB),
    convert('The narrative goes here', SQL_CLOB), X'0x440x540x33', 'flac');

insert into UserBlog(user_id, blog_text, title) values (1, convert('bla bla bla', SQL_CLOB), 'Jaxb Plugin Usage');
