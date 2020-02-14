package com.sist.dao;
//page가 나뉘면 무조건 총페이지를 갖고 들어온다
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
			String sql="SELECT rank,state,idcrement,poster,title,singer,album FROM music_genie "
					+"ORDER BY rank ASC";
			ps=conn.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			int rowSize=50;  			// page수
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
		try
		{
			getConnection();
			String sql="SELECT rank,state,idcrement,title,singer,poster,key "
					+"FROM music_genie "
					+"WHERE rank=?";
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
}
