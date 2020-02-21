package com.sist.student.dao;
import java.util.*;
import java.sql.*;
public class StudentDAO {
	
	private Connection conn;
	private PreparedStatement ps;
	private final String URL="jdbc:oracle:thin:@localhost:1521:XE";
	
	//1. 드라이버 등록
	public StudentDAO()
	{
		try
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
		//2. 오라클 연결
		public void getConnection()
		{
			try
			{
				conn=DriverManager.getConnection(URL,"hr","happy");
			}catch (Exception ex) {}
		}
		//3. 오라블 해제
		public void disConnection()
		{
			try
			{
					if(ps!=null)ps.close();
					if(conn!=null)conn.close();
			}catch (Exception ex) {}
		}
		
		//4. 기능처리
		public ArrayList<StudentVO> stdAllData(int page) // page넘어갈때 문제를 해결하기 위해 매개변수로 이걸 잡음
		{
			ArrayList<StudentVO> list=new ArrayList<StudentVO>();
			
			try
			{
				getConnection(); // open
				String sql="SELECT hakbun,name,kor,eng,math "
						+"FROM student "
						+"ORDER BY hakbun ASC";  
				
				/*SUBQUERY
				 String sql="SELECT hakbun,name,kor,eng,math,num "
						+"FROM (SELECT hakbun,name,kor,eng,math,rownum as num "
						+"FROM (SELECT hakbun,name,kor,eng,math "
						+"FROM student ORDER BY 1)) "
						+"WHERE num BETWEEN 1 AND 10";  
				  
				 */
				ps=conn.prepareStatement(sql); // connection
				ResultSet rs=ps.executeQuery();
				
				int i=0;
				int j=0;
				int pageStart=(page*10)-10;  //10개씩 끊어서 페이징 하기  rowCount
				
			
				while(rs.next())
				{
					
					if(i<10 && j>=pageStart) 
					{
						StudentVO vo=new StudentVO();
						vo.setHakbun(rs.getInt(1));  //"hakbun"도 가능 넘버링은 함수때문에 만든 것
						vo.setName(rs.getString(2));
						vo.setKor(rs.getInt(3));
						vo.setEng(rs.getInt(4));
						vo.setMath(rs.getInt(5));
						
						list.add(vo);
						
						i++;
					}
					j++;
				}
				rs.close();
				// ?에 데이터 입력
				//ps.setString(1,vo.get변수명());

				
				ps.executeUpdate();

			}catch (Exception ex)
			{
				ex.printStackTrace();
			}
			finally // 데이터 저장할 때 마다 닫아줘야 메모리 절약
			{
				disConnection();
			}
			
			return list;
		}
		
		public void stdInsert(StudentVO vo)
		{
			try
			{
				getConnection();
				String sql="INSERT INTO student(hakbun,name,kor,eng,math,sex) "
						+ "VALUES(std_hakbun_seq.nextval,?,?,?,?,?)";
				
				ps=conn.prepareStatement(sql);
				ps.setString(1, vo.getName());
				ps.setInt(2, vo.getKor());
				ps.setInt(3, vo.getEng());
				ps.setInt(4, vo.getMath());
				ps.setString(5, vo.getSex());
				
				ps.executeUpdate();
			}catch(Exception ex)
			{
				ex.printStackTrace();
			}finally
			{
				disConnection();
			}
		}
		
		public int stdRowCount()
		{
			int count=0;
			try
			{
				getConnection();
				String sql="SELECT COUNT(*) FROM student";  //순차적으로 출력
				ps=conn.prepareStatement(sql);
				ResultSet rs=ps.executeQuery();
				rs.next();
				count=rs.getInt(1);
				rs.close();
			}catch(Exception ex)
			{
				ex.printStackTrace();
			}finally
			{
				disConnection();
			}
			return count;
		}
		
		public void stdDelete(int hakbun) //pk
		{
			try
			{
				getConnection();
				String sql="DELETE FROM student "
						+"WHERE hakbun=?";
				
				ps=conn.prepareStatement(sql);
				ps.setInt(1,hakbun);
				
				ps.executeUpdate();
			}catch(Exception ex)
			{
				ex.printStackTrace();
			}finally
			{
				disConnection();
			}
		}
		
}
