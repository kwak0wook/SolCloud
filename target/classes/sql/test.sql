
CREATE TABLE sc_member(
	m_id varchar2(20 char) primary key,
	m_name varchar2(20 char) not null,
	m_pw varchar2(20 char) not null,
	m_zipcode varchar2(500 char) not null, /*우편번호*/
	m_address1 varchar2(1000 char) not null, /*주소*/
	m_address2 varchar2(1000 char) not null, /*상세주소*/
	m_question varchar2(1000 char) not null, /*질문*/
	m_answer varchar2(1000 char) not null, /*답변*/
	m_directory varchar2(1000 char) not null /*고유 디렉토리*/
);

SELECT * FROM (SELECT * FROM sc_notice WHERE no_writer = 'test' ORDER BY no_date DESC) WHERE ROWNUM <= 10;


insert into SC_MEMBER values('admin','admin','admin','admin','admin','admin','admin','admin','admin');
	select * from sc_member;
create table sc_board_admin ( 
	f_name varchar2(1000 char),
	f_location varchar2(1000 char),
	f_icon varchar2(1000 char),
	f_size number(8,2),
	f_share number,
	f_date date
)

insert into sc_notice values (1, '요청', '승환이 영상', '승환이 영상', '승환이', 'test', sysdate, 1);
		
ALTER TABLE sc_notice MODIFY no_content null;
ALTER TABLE sc_notice MODIFY (no_class number);
drop table sc_notice purge;

create table sc_notice (
	no_num number primary key,
	no_class number not null,
	no_title varchar2(100 char) not null,
	no_content varchar2(3000 char),
	no_file varchar2(300 char),
	no_writer varchar2(30 char) not null,
	no_date date not null,
	no_viewcnt number not null
)

create table sc_reply (
	re_num number primary key,
	re_nonum number not null,
	re_content not null,
	re_writer not null,
	re_date not null
)

create sequence notice_seq
	start with 1
	increment BY 1
	nomaxvalue;

create sequence reply_seq
	start with 1
	increment BY 1
	nomaxvalue;
	
	
