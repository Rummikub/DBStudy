package com.sist.dao;
import java.util.*;
import com.sist.vo.MovieVO;
import com.sist.vo.NewsVO;

import java.sql.*;   // db와 연결

public class MovieDAO {
		private Connection conn;  // Socket
		private PreparedStatement ps; // OutputStream , BufferedReader
		private final String URL="jdbc:oracle:thin:@localhost:1521:XE";
		
		//****싱글턴 만들면 늘 new객체 생성하지 않아도 되는 장점이 있다*********
		private static MovieDAO dao;
		// 드라이버 등록
		public MovieDAO()
		{
			try
			{
				Class.forName("oracle.jdbc.driver.OracleDriver");
			}catch (Exception ex) 
			{
				ex.printStackTrace();
			}
		}
		
		//Singletone ; 접속자 별로 한개의 DAO만 사용 할 수 있음  = 동일한 주소를 가져오지 않을까 ~ 최적화의 일종
 		
		/*
		 	디자인 패턴
		 	=======
		 	싱글톤		==> Spring
		 	팩토리
		 	MV
		 	MVC
		 	옵저버
		 	어댑터
		 */
		
		//싱글턴 생성
		public static MovieDAO newInstance()
		{
			if(dao==null)
				dao=new MovieDAO();
			return dao;
		}
		
		
		//오라클 연결
		public void getConnection()
		{
			try
			{
				conn=DriverManager.getConnection(URL,"hr","happy");
			}catch(Exception e)
			{}
		}
		
		//오라클 해제
		public void disConnection()
		{
			try
			{
					if(ps!=null)ps.close();
					if(conn!=null) conn.close();
			}catch (Exception e){}
		}
		
		
		/*
		MNO      NOT NULL NUMBER(4)           -- pk
		TITLE    NOT NULL VARCHAR2(1000) 
		POSTER   NOT NULL VARCHAR2(2000) 
		SCORE             NUMBER(4,2)    
		GENRE    NOT NULL VARCHAR2(100)  
		REGDATE           VARCHAR2(100)  
		TIME              VARCHAR2(10)   
		GRADE             VARCHAR2(100)  
		DIRECTOR          VARCHAR2(200)  
		ACTOR             VARCHAR2(200)  
		STORY             CLOB           
		TYPE              NUMBER         

		 */
		
		//기능설정 - 데이터vo저장
		public void movieInsert(MovieVO vo)
		{
			 try 
			{
				getConnection();
				String sql="INSERT INTO movie VALUES("
						+"(SELECT NVL(MAX(mno)+1,1) FROM movie), "
						+"?,?,?,?,?,?,?,?,?,?,?)";
				
				ps=conn.prepareStatement(sql);
				ps.setString(1, vo.getTitle());
				ps.setString(2, vo.getPoster());
				ps.setDouble(3, vo.getScore());
				ps.setString(4, vo.getGenre());
				ps.setString(5, vo.getRegdate());
				ps.setString(6, vo.getTime());
				ps.setString(7, vo.getGrade());
				ps.setString(8, vo.getDirector());
				ps.setString(9, vo.getActor());
				ps.setString(10, vo.getStory());
				ps.setInt(11, vo.getType());
				ps.executeUpdate();
				
			} catch (Exception e)
			{
				e.printStackTrace();
			} finally 
			{
				disConnection();
			}
		}
		
		// 페이지 넘어가며 목록 출력
		public ArrayList<MovieVO> movieListData(int page,int type)
		{
			ArrayList<MovieVO> list=new ArrayList<MovieVO>();
			try {
				getConnection();
				
				int rowSize=12;
				int start=(page*rowSize)-(rowSize-1);  			// 오라클 rownum 은 1부터 시작하기 때문 12-11=1 , 24-11=13 
				int end=page*rowSize;
				
				String sql="";
				
				if(type<3)
					
				{
					
					
					
				    sql="SELECT mno,title,poster,score,regdate,num "
						+ "FROM (SELECT mno,title,poster,score,regdate,rownum as num "
						+ "FROM (SELECT mno,title,poster,score,regdate "
						+ "FROM movie WHERE type=? ORDER BY mno ASC)) "
						+ "WHERE num BETWEEN ? AND ?";
				    
						ps=conn.prepareStatement(sql);
						ps.setInt(1, type);
						ps.setInt(2, start);
						ps.setInt(3, end);
				}
				
				else
					
				{
					
					sql="SELECT mno,title,poster,score,regdate "
						+ "FROM movie WHERE type=? ORDER BY mno ASC";
						ps=conn.prepareStatement(sql);
						ps.setInt(1, type);
					
				}
				
				
				ResultSet rs=ps.executeQuery();
				
				while(rs.next())
					
				{
					MovieVO vo=new MovieVO();
					vo.setMno(rs.getInt(1));
					vo.setTitle(rs.getString(2));
					vo.setPoster(rs.getString(3));
					vo.setScore(rs.getDouble(4));
					vo.setRegdate(rs.getString(5));
					list.add(vo);
				}
				rs.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				disConnection();
			}
			return list;
		}
		
		
		//뉴스 기능 넣기
		public void newsInsert(NewsVO vo)
		{
			 try 
			{
				getConnection();
				String sql="INSERT INTO news VALUES( "
						+"?,?,?,?,?,?)";
				
				ps=conn.prepareStatement(sql);
				
				//출력되는 순서랑 sql 잘 맞춰서 넣기
				ps.setString(1, vo.getTitle());
				ps.setString(2, vo.getPoster());
				ps.setString(3, vo.getLink());
				ps.setString(4, vo.getAuthor());
				ps.setString(5, vo.getRegdate());
				ps.setString(6, vo.getContent());
		
				ps.executeUpdate();
				
			} catch (Exception e)
			{
				e.printStackTrace();
			} finally 
			{
				disConnection();
			}
		}
		
		
		//뉴스 목록 출력
		public ArrayList<NewsVO> newsListData(int page)
		{
			ArrayList<NewsVO> list=new ArrayList<NewsVO>();
			try{
				getConnection();
				int rowSize=10;
				/*
				 1p  1- 10
				 2p  11-20
				 3p  21-30  ==> rownum = 1
				 */
				int start=(rowSize*page)-(rowSize-1);
				int end=rowSize*page;
				
				
				String sql="SELECT title,poster,author,regdate,link,content,num "
						+ "FROM (SELECT title,poster,author,regdate,link,content,rownum as num "
						+ "FROM (SELECT title,poster,author,regdate,link,content "
						+ "FROM news)) "
						+ "WHERE num BETWEEN ? AND ?";
				
				ps=conn.prepareStatement(sql);
				ps.setInt(1, start);
				ps.setInt(2, end);
				
				ResultSet rs=ps.executeQuery();
				while(rs.next())
				{
					NewsVO vo=new NewsVO();
					vo.setTitle(rs.getString(1));
					vo.setPoster(rs.getString(2));
					vo.setAuthor(rs.getString(3));
					vo.setRegdate(rs.getString(4));
					vo.setLink(rs.getString(5));
					vo.setContent(rs.getString(6));
					
					list.add(vo);
				}
				rs.close();
				
			}catch(Exception e)
			{
				e.printStackTrace();
			}finally
			{
				disConnection();
			}
			return list;
		}
		
		
		// 총 몇페이지인지 알 수 없잖아  => 뉴스 총페이지 읽자
		public int newsTotalPage()
		{
			int total=0;
			try
			{
				getConnection();
				String sql="SELECT CEIL(COUNT(*)/10.0) FROM news";
				ps=conn.prepareStatement(sql);
				ResultSet rs=ps.executeQuery();
				rs.next();
				total=rs.getInt(1);
				rs.close();
				
			}catch(Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				disConnection();
			}
			return total;
		}
		
		
		/*
				MNO         NUMBER(4)      
				TITLE       VARCHAR2(1000) 
				POSTER      VARCHAR2(2000) 
				SCORE       NUMBER(4,2)    
				GENRE       VARCHAR2(100)  
				REGDATE     VARCHAR2(100)  
				TIME        VARCHAR2(10)   
				GRADE       VARCHAR2(100)  
				DIRECTOR    VARCHAR2(200)  
				ACTOR       VARCHAR2(200)  
				STORY       CLOB           
 
		 */
		
		//영화상세보기
		public MovieVO movieDetailData(int mno)
		{
			MovieVO vo=new MovieVO();
			try
			{
				getConnection();
				String sql="SELECT mno,title,poster,score,genre,regdate,time,grade,director,actor,story "
						+ "FROM movie "
						+ "WHERE mno=?";
		
				ps=conn.prepareStatement(sql);
				ps.setInt(1, mno);
				ResultSet rs=ps.executeQuery();
				rs.next();
				
				vo.setMno(rs.getInt(1));
				vo.setTitle(rs.getString(2));
				vo.setPoster(rs.getString(3));
				vo.setScore(rs.getDouble(4));
				vo.setGenre(rs.getString(5));
				vo.setRegdate(rs.getString(6));
				vo.setTime(rs.getString(7));
				vo.setGrade(rs.getString(8));
				vo.setDirector(rs.getString(9));
				vo.setActor(rs.getString(10));
				vo.setStory(rs.getString(11));
				
				rs.close();
				
				
			}catch (Exception e) 
			{
				e.printStackTrace();
			}
			finally
			{
				disConnection();
			}
			return vo;
		}
		
}
		