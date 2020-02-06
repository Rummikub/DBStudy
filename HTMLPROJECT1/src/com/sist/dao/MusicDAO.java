package com.sist.dao;

import java.sql.*;
import java.util.*;



public class MusicDAO {

	private Connection conn; 
	private PreparedStatement ps; 
	
	
	private final String URL = "jdbc:oracle:thin:@localhost:1521:XE";
	

	public MusicDAO()
	{
		try 
		{
		Class.forName("oracle.jdbc.driver.OracleDriver");
		
			
		}catch(Exception ex) 
		{
			
		ex.printStackTrace();	
			
		}
		
		
	}
	
	
	public void getConnection()
	{
		
		try
		{
		
			conn=DriverManager.getConnection(URL, "hr", "happy");
			
			
		}catch(Exception ex) {}
		
	}
	
	

	public void disConnection()
	{
		
		try
		{
			if(ps!=null) ps.close();
			if(conn!=null) conn.close();
			
			
		}catch(Exception ex) {}
		
	}
	
	
	
	public void musicInsert(MusicVO vo) 
	{
		
		
		
		try 
		{
			
			getConnection();
			
			String sql = "INSERT INTO music_genie VALUES("   // music_genie = *   music_genie(mno)
						+"(SELECT NVL(MAX(mno)+1, 1) FROM music_genie),?,?,?,?,?,?,?,?)"; // SUBQUERY
			
			// INSERT INTO music_genie VALUES(        )
		
			ps=conn.prepareStatement(sql);
			
		/*
		    private int rank;
			private String title;
			private String singer;
			private String album;
			private String poster;
			private int idcliment;
			private String state;
			private String key;

		 */
			
			ps.setInt(1, vo.getRank());
			ps.setString(2, vo.getTitle());  
			ps.setString(3, vo.getSinger());
			ps.setString(4, vo.getAlbum());
			ps.setString(5, vo.getPoster());
			ps.setInt(6, vo.getIdcliment());
			ps.setString(7, vo.getState());
			ps.setString(8, vo.getKey());
			
			
			
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
		
		
		
	}
	
	
	public ArrayList<MusicVO> musiclistData()
	{
		
		ArrayList<MusicVO> list= new ArrayList<MusicVO>();
		try
		{
			
			getConnection();
			String sql= "SELECT mno,title,singer,poster,album, idcliment,state FROM music_genie "
					+"ORDER BY rank ASC";
			ps=conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next())
			{
				
				MusicVO vo = new MusicVO();
				vo.setMno(rs.getInt(1));
				vo.setTitle(rs.getString(2));
				vo.setSinger(rs.getString(3));
				vo.setPoster(rs.getString(4));
				vo.setAlbum(rs.getString(5));
				vo.setIdcliment(rs.getInt(6));
				vo.setState(rs.getString(7));
				
				list.add(vo);
				
			}
			rs.close();
			
		}catch(Exception ex)
		{
			
			ex.printStackTrace();
			
		}finally
		{
			
			disConnection();
			
		}
		
		return list;
	}
	
	
	
}
