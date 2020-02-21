package com.sist.dao;
//지금은 회원가입, 댓글을 dao에서 한번에 메소드로 만들지만 '재사용'에 용이하게 각자 클래스 만드는 것이 더욱 유리
import java.sql.*;
import java.util.*;


public class MusicDAO {
	private Connection conn;
	private PreparedStatement ps;
	private final String URL="jdbc:oracle:thin:@localhost:1521:XE";
	
	public MusicDAO()
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
	
	//4.기능설정
	//**주의**리스트목록에 무엇을 출력하는지 유의해야 ~ 상세보기에서 ?뒤에 무엇이 오는지 알 수 있음
	// 순위,state,idcrement,poster,title,singer,album 
	public ArrayList<MusicVO> musicListData(int page)  // page기준으로 나뉜다
	{
		ArrayList<MusicVO> list=new ArrayList<MusicVO>();
		try
		{
			getConnection();
			String sql="SELECT rank,state,idcrement,poster,title,singer,album,mno FROM music_genie "
					+"ORDER BY rank ASC";
			ps=conn.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			int rowSize=15;  			// page수
			int pageStart=(page*rowSize)-rowSize;
			int i=0;
			int j=0;
			
			while(rs.next())
			{
				if(i<rowSize && j>=pageStart)
				{
					MusicVO vo=new MusicVO();
					vo.setRank(rs.getInt(1));
					vo.setState(rs.getString(2));
					vo.setIdcrement(rs.getInt(3));
					vo.setPoster(rs.getString(4));
					vo.setTitle(rs.getString(5));
					vo.setSinger(rs.getString(6));
					vo.setAlbum(rs.getString(7));
					vo.setMno(rs.getInt(8));
					
					list.add(vo);
					i++;
				}
				j++;
			}
		
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
	//**페이지 나뉘면 무조건 총페이지 갖고 들어가야됨 = ceil !!!!!! 50개씩 끊을 거니까
	public int musicTotalPage()
	{
		int total=0;
		try
		{
			getConnection();
			String sql="SELECT CEIL(COUNT(*)/50.0) FROM music_genie";
			ps=conn.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			rs.next();
			total=rs.getInt(1);
			rs.close();
		}catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}finally
		{
			disConnection();
		}
		return total;
	}
	
	//상세보기 -- **rank <-얘로 찾을거야.....,state,idcrement,title,singer,poster,key
	public MusicVO musicDetailData(int no)
	{
		
		MusicVO vo=new MusicVO();
		/*
		 CURD 프로그램 짜려면  DML 을 익혀놔라
		 SELECT
		  - SELECT ~ FROM ~ [ WHERE ~ GROUP BY ~ HAVING ~ ORDER BY ]
		 INSERT 
		  - INSERT INTO t명(COL1..) VALUES(값1...) => DEFAULT가 많을 때
		 UPDATE 
		  - UPDATE t명  SET COL1=값1, COL2=값2... WHERE 조건
		 DELETE 
		  - DELETE FROM T명 WHERE 조건
		 */
		try
		{
			getConnection();
			//조회수 증가
			
			String sql="UPDATE music_genie SET "
					+"hit=hit+1 "
					+"WHERE mno=?";
			
			ps=conn.prepareStatement(sql);
			ps.setInt(1, no);
			
			/*
			 ps.executeUpdate  		==> 오라클의 데이터가 변경 (INSERT,UPDATE,DELETE)
			 >auto commit
			 ps.executeQuery  		==> 데이터의 변경이 없는 상태 (SELECT ; 검색)
			 */
			ps.executeUpdate();
			
			//데이터출력해주기
			sql="SELECT rank,state,idcrement,title,singer,poster,key,mno,album "
					+"FROM music_genie "
					+"WHERE mno=?";
			ps=conn.prepareStatement(sql);
			ps.setInt(1, no);
			ResultSet rs=ps.executeQuery();
			rs.next();
			vo.setRank(rs.getInt(1));
			vo.setState(rs.getString(2));
			vo.setIdcrement(rs.getInt(3));
			vo.setTitle(rs.getString(4));
			vo.setSinger(rs.getString(5));
			vo.setPoster(rs.getString(6));
			vo.setKey(rs.getString(7));
			vo.setMno(rs.getInt(8));
			vo.setAlbum(rs.getString(9));
			
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
	//로그인
	public String isLogin(String id,String pwd)
	{
		String result="";
		try
		{
			getConnection();
			String sql="SELECT COUNT(*) FROM music_member "
					+"WHERE id=?";    // 0이면 없는것이고 1이면 아이디가있는 것이야 => 중복체크
			ps=conn.prepareStatement(sql);
			ps.setString(1, id);
			ResultSet rs=ps.executeQuery();
			
			// id유무
			rs.next();
			int count=rs.getInt(1);
			rs.close();
			
			if(count==0)
			{
				result="NOID";
			}else
			{
				sql="SELECT pwd,name,sex FROM music_member "
				 +"WHERE id=?";
				
				ps=conn.prepareStatement(sql);
				ps.setString(1, id);
			 
				rs=ps.executeQuery();
				
				rs.next();
				String db_pwd=rs.getString(1);
				String name=rs.getString(2);
				String sex=rs.getString(3);
				rs.close();
				
				if(db_pwd.equals(pwd))
				{
					result=name+"|"+sex;
				}
				else
				{
					result="NOPWD";
				}
			}
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}finally
		{
			disConnection();
		}
		return result;
	}


	//댓글보기 SELECT => WHERE 
	public ArrayList<MusicReplyVO> replyListData(int mno)
	{
		ArrayList<MusicReplyVO> list=new ArrayList<MusicReplyVO>();
		try
		{
			getConnection();
			String sql="SELECT no,id,name,msg,TO_CHAR(regdate,'YYYY-MM-DD-HH24:MI:SS'), "
					+"(SELECT sex FROM music_member mm WHERE mm.id=mr.id) "
					+"FROM music_reply mr "
					+"WHERE mno=?";
			ps=conn.prepareStatement(sql);
			ps.setInt(1, mno);
			
			ResultSet rs=ps.executeQuery();
			while(rs.next())
			{
				MusicReplyVO vo=new MusicReplyVO();
				vo.setNo(rs.getInt(1));
				vo.setId(rs.getString(2));
				vo.setName(rs.getString(3));
				vo.setMsg(rs.getString(4));
				vo.setDbDay(rs.getString(5));
				vo.setSex(rs.getString(6));
				list.add(vo);
			}
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}finally
		{
			disConnection();
		}
		return list;
	}
	
	//댓글쓰기 INSERT
	public void replyInsert(MusicReplyVO vo)
	{
		try
		{
			getConnection();
			String sql="INSERT INTO music_reply VALUES(mr_no_seq.nextval,?,?,?,?,SYSDATE)";
			
			System.out.println(vo.getMsg());
			System.out.println(vo.getMno());
			System.out.println(vo.getName());
			System.out.println(vo.getId());
			
			ps=conn.prepareStatement(sql);
			ps.setInt(1, vo.getMno());
			ps.setString(2, vo.getId());
			ps.setString(3, vo.getName());
			ps.setString(4, vo.getMsg());
			ps.executeUpdate();
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}finally
		{
			disConnection();
		}
	}
	//댓글삭제 DELETE
		public void replyDelete(int no)
		{
			try
			{
				getConnection();
				String sql="DELETE FROM music_reply WHERE no=?";

				ps=conn.prepareStatement(sql);
				ps.setInt(1, no);
				ps.executeUpdate();
			}catch(Exception ex)
			{
				ex.printStackTrace();
			}finally
			{
				disConnection();
			}
	}
	
	
	//댓글수정 UPDATE  (생략)
		
		//hit수가 많은 제목;5개 (인라인뷰) 조회수 증가
		public ArrayList<MusicVO> musicTop5()
		{
			ArrayList<MusicVO> list=new ArrayList<>();
			try
			{
				getConnection();  				//----rownum; 게시판 이전/다음 게시글 볼때 사용하더라~
			String sql="SELECT poster,title,no,rownum "
					+ "FROM (SELECT poster,title,RANK() OVER(ORDER BY hit DESC) as no "
					+ "FROM music_genie ORDER BY hit DESC) "
					+ "WHERE rownum<=5"; 
			
			ps=conn.prepareStatement(sql);
			//실행
			ResultSet rs=ps.executeQuery();
			while(rs.next())
			{
				MusicVO vo=new MusicVO();
				vo.setPoster(rs.getString(1));
				vo.setTitle(rs.getString(2));
				vo.setRank(rs.getInt(3));
				list.add(vo);
			}
			rs.close();
			
			}catch (Exception ex)
			{
				ex.printStackTrace();
			}finally
			{
				disConnection();
			}
			return list;
		}

}
