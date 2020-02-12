package com.sist.dao;

import java.util.*;
import java.util.Date;
import java.sql.*;

public class EmpDAO {
	// RECORD = 데이터베이스 프로그램, COLUMN여러줄을 묶어준 것 
	
	private Connection conn;
	private PreparedStatement ps;
	private final String URL="jdbc:oracle:thin:@localhost:1521:XE";
	
	//DAO 생성자 (드라이버 검색)
	public EmpDAO()
	{
		try
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	//연결
	public void getConnection()
	{
		try
		{
			conn=DriverManager.getConnection(URL,"hr","happy");
		}catch(Exception ex) {}
	}
	//해제
	public void disConnection()
	{
		try
		{
			if(ps!=null)ps.close();
			if(conn!=null) conn.close();
			
		}catch(Exception ex) {}
		
	}
	
	//기능설정 = 메인 JOIN
	public ArrayList<EmpVO> empJoinData()
	{
		ArrayList<EmpVO> list=new ArrayList<EmpVO>();
		try
		{
			getConnection();
			String sql="SELECT empno,ename,job,hiredate,sal,dname,loc,grade "
					+ "FROM emp,dept,salgrade "
					+ "WHERE emp.deptno=dept.deptno "
					+ "AND sal BETWEEN losal AND hisal";
			ps=conn.prepareStatement(sql);
			
			ResultSet rs=ps.executeQuery();
			while(rs.next())
			{
				EmpVO vo=new EmpVO();
				vo.setEmpno(rs.getInt(1));
				vo.setEname(rs.getString(2));
				vo.setJob(rs.getString(3));
				vo.setHiredate(rs.getDate(4));
				vo.setSal(rs.getInt(5));
				vo.getDvo().setDname(rs.getString(6));
				vo.getDvo().setLoc(rs.getString(7));
				vo.getSvo().setGrade(rs.getInt(8));
		
				list.add(vo);

			}

			
			ps.executeUpdate();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			disConnection();
		}
		
		  return list;	
	}


}
