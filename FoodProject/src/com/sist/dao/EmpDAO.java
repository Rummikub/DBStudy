package com.sist.dao;
import java.util.*;
import java.sql.*;

//이게 무어람...?
import javax.sql.*;
import javax.naming.*;

public class EmpDAO {
	private Connection conn;
	private PreparedStatement ps;
	
	//single turn
	private static EmpDAO dao;
	
	// *************커넥션이 이미 다섯개  생성되어 있을 것!   - server.xml에 코딩함
	//Connection 객체 얻어오기
	public void getConnection()
	{
		try 
		{
			Context init=new InitialContext();  // JNDI(Java Naming Directory Interface)
			// 탐색기를 연다 > 폴더에 접근 > 파일에 접근
												//C드라이브 접근  (실제저장)폴더 이름을 URL형태로 줘서 다른 사람은 사용 못하게 제공해주는 default폴더
			//lookup ; setLookup("별칭",클래스주소)
			Context c=(Context)init.lookup("java://comp//env");
												//+ 실제 커넥션 => 오라클 정보와 관련된 모든 정보를 전송(DataSource)
			DataSource ds=(DataSource)c.lookup("jdbc/oracle");   //server.xml의 name
			conn=ds.getConnection();
			//**주소값을 넘겨받는다*****
				
			
		} catch (Exception e) {

		}
	}
	
	//*****반환
	public void disConnection()
	{
		try
		{
			if(ps!=null) ps.close();
			if(conn!=null) conn.close();
			
		}catch(Exception e) {}
	}
	
	//기능 처리
	public ArrayList<EmpVO> empAllData()
	{
		
		ArrayList<EmpVO> list=new ArrayList<EmpVO>();
		try 
		{
			
			getConnection();
		/*	String sql="SELECT empno,ename,job,hiredate,sal,dname,loc "
					+"FROM emp,dept "
					+"WHERE emp.deptno=dept.deptno";
					
					// 아래의 VIEW랑 같은 역할을 하니까, 앞으로는 뷰를 써라
					*/ 
			String sql="SELECT * FROM emp_dept";
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
				
				//deptVo이름이 dvo이고 우린 조인을 했지.=> 테이블마다 vo따로 있어야 관리하기 편해
				vo.getDvo().setDname(rs.getString(6));
				vo.getDvo().setLoc(rs.getString(7));
				
				list.add(vo);
			}
			
			
		} catch (Exception e) 
		{	
			e.printStackTrace();
		} finally
		{
			disConnection();
			//*******반환
		}
		return list;
	}
}
 