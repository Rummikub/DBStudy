package com.sist.dao;
/*
 * 
 * 
 * 
 * 오라클에서 만들어놓은 테이블구조와 컬럼을 SEARCH 하여 가져온다.
 * DESC emp;
 * 
 *  Name                                      Null?    Type
 ----------------------------------------- -------- ----------------------------
 EMPNO                                              NUMBER        => int
 ENAME                                              VARCHAR2(10)  => String
 JOB                                                VARCHAR2(9)   => String
 MGR                                                NUMBER(4)     =>int
 HIREDATE                                           DATE  		  => date
 SAL                                                NUMBER(7,2)   => int => int로 받은 이유는 number(7,2)로 되어있다면 double로 받아야하지만 현재 emp 값은 정수로 되어있어서 int로 받아도 ok!
 COMM                                               NUMBER(7,2)   => int
 DEPTNO                                             NUMBER        => int

 *  오라클 데이터형
 *  
 *  문자형
 *    	CHAR, VARCHAR2, CLOB => String
 *  숫자형
 *  	NUMBER => NUMBER(10) = int, NUMBER(7,2) => double,int
 *  날짜형
 *      DATE => java.util.Date 
 *      
 */
import java.util.*;
public class EmpVO {   // 데이터를 읽기/쓰기까지만 하도록 만들어 놓음 
	// get -> 출력할때!
	// set -> 오라클에서 값을 가져올때!

	
	private int empno;
	private String ename;
	private String job;
	private int mgr;
	private Date hiredate;
	private int sal;
	private int comm;
	private int deptno;
	
	
	public int getEmpno() {  // 출력할떄!
		return empno;
	}
	public void setEmpno(int empno) {  // 오라클에서 값을 가져올때!
		this.empno = empno;
	}
	public String getEname() {
		return ename;
	}
	public void setEname(String ename) {
		this.ename = ename;
	}
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
	}
	public int getMgr() {
		return mgr;
	}
	public void setMgr(int mgr) {
		this.mgr = mgr;
	}
	public Date getHiredate() {
		return hiredate;
	}
	public void setHiredate(Date hiredate) {
		this.hiredate = hiredate;
	}
	public int getSal() {
		return sal;
	}
	public void setSal(int sal) {
		this.sal = sal;
	}
	public int getComm() {
		return comm;
	}
	public void setComm(int comm) {
		this.comm = comm;
	}
	public int getDeptno() {
		return deptno;
	}
	public void setDeptno(int deptno) {
		this.deptno = deptno;
	}
	
	
}


