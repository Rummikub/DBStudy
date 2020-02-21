package com.sist.dao;
/*
NO      NOT NULL NUMBER       
MNO              NUMBER       
ID               VARCHAR2(20) 
NAME             VARCHAR2(51) 
MSG     NOT NULL CLOB         
REGDATE          DATE  				--> TO_CHAR(REGDATE,'YYYY-MM-DD HH24:MI:SS')  ==> STRING날짜 변수가 별도로 필요함
============================
이름(날짜,시간)			수정삭제댓글
============================
댓글

============================
*타 유저의 화면
============================
이름(날짜,시간)				  댓글
============================
댓글

============================
 */
import java.util.*;
public class MusicReplyVO {
	
	private int no;
	private int mno;
	private String id;
	private String name;
	private String msg;
	private Date regdate;
	private String dbDay; // String 변수 별도로 변환된 날짜가필요해 잊지마잊미아
	private String sex;
	//프로그램에서 필요한 변수 설정
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public int getMno() {
		return mno;
	}
	public void setMno(int mno) {
		this.mno = mno;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Date getRegdate() {
		return regdate;
	}
	public void setRegdate(Date regdate) {
		this.regdate = regdate;
	}
	public String getDbDay() {
		return dbDay;
	}
	public void setDbDay(String dbDay) {
		this.dbDay = dbDay;
	}
	
}
