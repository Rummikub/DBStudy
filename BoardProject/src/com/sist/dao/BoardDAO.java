package com.sist.dao;
import java.util.*; // arraylist
import java.sql.*; // connection class, preparedStatement,resultset
/*
 * 	Connection = Oracle 연결
 *  PreparedStatement = SQL문장 전송
 *  ResultSet = 결과값 저장 ===> SELECT문 필수
 *  	= ResultSet executeQuery("SQL") =======SELECT
 *  	= executeUpdate("SQL") ================INSERT, UPDATE, DELETE
 */
public class BoardDAO {
	private Connection conn;
	private PreparedStatement ps;
	private final String URL="jdbc:oracle:thin:@localhost:1521:XE";
	
	public BoardDAO()
	{
		try
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
		}catch(Exception ex)
		{
			System.out.println(ex.getMessage());
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
	
	//기능설정 : 게시판 목록 읽기 select order by
	public ArrayList<BoardVO> boardListData(int page)
	{
		ArrayList<BoardVO> list= new ArrayList<BoardVO>();
		
		try
		{
			
			getConnection();
			String sql="SELECT no,subject,name,regdate,hit "
					+"FROM board "
					+"ORDER BY no DESC";
			
			int rowSize=10;
			int i=0;		// 10개마다 끊어주는 변수
			int j=0;  		// while문이 돌아가는 횟수
			int pageStart=(page*rowSize)-rowSize;  // 페이지수 출력위치
			
			ps=conn.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			
			while(rs.next())
			{
				// 페이지 나누는 조건
				if(i<10 && j>=pageStart)
				{
					BoardVO vo=new BoardVO();
					vo.setNo(rs.getInt(1));
					vo.setSubject(rs.getString(2));
					vo.setName(rs.getString(3));
					vo.setRegdate(rs.getDate(4));
					vo.setHit(rs.getInt(5));
					list.add(vo);
					i++;
				}
				j++;
			}
			rs.close();
			
			
		}catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
		finally
		{
			disConnection();
		}
		return list;
	}
	
	// 전체페이지 ( 필요해서 추가 한 것! )
	public int boardTotalPage()
	{
		int total=0;
		try
		{
			getConnection();
			String sql="SELECT CEIL(COUNT(*)/10.0) FROM board ";  // 페이지 구하는 함수!
			ps=conn.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			rs.next();       //데이터가 있는 위치에 커서를 갖다대라
			total=rs.getInt(1);
			rs.close();
			
		}catch (Exception ex) 
		{
			System.out.println(ex.getMessage());
		}
		finally
		{
		}
		return total;
	}
	
	//게시판 내용보기 select where
	public BoardVO boardDetailData(int no)  // 게시판 번호를 받아와야 (매개변수) 내용을 볼 수 있지
	{
		BoardVO vo=new BoardVO();
		try
		{
			getConnection();
			
			
			String sql="UPDATE board SET "  				
					+"hit=hit+1 "
					+"WHERE no=?";
			
			ps=conn.prepareStatement(sql);
			//?값
			
			ps.setInt(1, no);
			ps.executeUpdate(); // commit포함									 조회수 증가가 가장 먼저다!!!!!
			
			
			sql="SELECT no,name,subject,content,regdate,hit "
					+"FROM board "
					+"WHERE no=?";
			
			ps=conn.prepareStatement(sql);
			ps.setInt(1, no);
			
			ResultSet rs=ps.executeQuery();
			rs.next();
			
			vo.setNo(rs.getInt(1));
			vo.setName(rs.getString(2));
			vo.setSubject(rs.getString(3));
			vo.setContent(rs.getString(4));
			vo.setRegdate(rs.getDate(5));
			vo.setHit(rs.getInt(6));
			
			rs.close();
			
		}catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
		finally
		{
			disConnection();	
		}
		return vo;
	}
	//추가하기 insert
	public void boardInsert(BoardVO vo)
	{
		try
		{
			getConnection();
			String sql="INSERT INTO board(no,name,subject,content,pwd) "
					+"VALUES((SELECT NVL(MAX(no)+1,1) FROM board),?,?,?,?)";
			ps=conn.prepareStatement(sql);
			ps.setString(1, vo.getName());
			ps.setString(2, vo.getSubject());
			ps.setString(3, vo.getContent());
			ps.setString(4, vo.getPwd());
			ps.executeUpdate();//commmit()포함
			
		}catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
		finally
		{
			disConnection();
		}
	}
	//수정하기 update
	public BoardVO boardUpdateData(int no)  // 게시판 번호를 받아와야 (매개변수) 내용을 볼 수 있지
	{
		BoardVO vo=new BoardVO();
		try
		{
			getConnection();
			
			
			String sql="SELECT no,name,subject,content "
					+"FROM board "
					+"WHERE no=?";
			
			ps=conn.prepareStatement(sql);
			ps.setInt(1, no);
			
			ResultSet rs=ps.executeQuery();
			rs.next();
			
			vo.setNo(rs.getInt(1));
			vo.setName(rs.getString(2));
			vo.setSubject(rs.getString(3));
			vo.setContent(rs.getString(4));
			
			rs.close();
			
		}catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
		finally
		{
			disConnection();	
		}
		return vo;
	}
	
	//실제 수정 = UPDATE ~SET
	public boolean boardUpdate(BoardVO vo)
	{
		boolean bCheck=false;
		try
		{
			getConnection();
			String sql="SELECT pwd FROM board "
					+"WHERE no=?";
			ps=conn.prepareStatement(sql);
			ps.setInt(1, vo.getNo());
			ResultSet rs=ps.executeQuery();
			rs.next();
			String db_pwd=rs.getString(1);
			rs.close();
			
			if(db_pwd.equals(vo.getPwd()))
			{
				bCheck=true; // detail로 이동
				sql="UPDATE board SET "
					+"name=?,subject=?,content=? "
					+"WHERE no=?";
				ps=conn.prepareStatement(sql);
				
				ps.setString(1,vo.getName());
				ps.setString(2, vo.getSubject());
				ps.setString(3, vo.getContent());
				ps.setInt(4, vo.getNo());
				
				ps.executeUpdate();
			}
			else
			{
				bCheck=false; // 비번 틀렸다 메시지 날리기
			}
		}catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}finally
		{
			disConnection();
		}
		return bCheck;

	}
	//삭제하기 delete
	public boolean BoardDelete(int no,String pwd)
	{
		boolean bCheck=false;
		try
		{
			getConnection();
			String sql="SELECT pwd FROM board "
					+"WHERE no=?";
			ps=conn.prepareStatement(sql);
			ps.setInt(1, no);
			ResultSet rs=ps.executeQuery();
			rs.next();
			String db_pwd=rs.getString(1);
			rs.close();
			
			if(db_pwd.equals(pwd))
			{
				bCheck=true; // detail로 이동
				sql="DELETE FROM board "
						+"WHERER no=?";
				ps=conn.prepareStatement(sql);
				ps.setInt(1, no);
				ps.executeUpdate();
			}
			else
			{
				bCheck=false; // 비번 틀렸다 메시지 날리기
			}
		}catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}finally
		{
			disConnection();
		}
		return bCheck;

	}
	//찾기 select like
	public ArrayList<BoardVO> boardFind(String fs, String ss)
	{
		ArrayList<BoardVO> list=new ArrayList<BoardVO>();
		return list;
	}


}
