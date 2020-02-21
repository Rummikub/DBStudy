package com.sist.dao;
import java.util.*;
import java.sql.*;

// Connection Pool
import javax.naming.*;
import javax.sql.*;
import com.sist.manager.*;


public class FoodDAO {
	private Connection conn;
	private PreparedStatement ps;
	
	//private final String URL="jdbc:oracle:thin:@localhost:1521:XE";
	
	
	//하나 설치하고 돌려쓰기 ; singleturn 
	private static FoodDAO dao;
	
	
	
	
	
	public void getConnection()
	{
		try {
		
		//conn=DriverManager.getConnection(URL,"hr","happy");
		
			
		Context init=new InitialContext();  // JNDI registry에 접근
		Context c=(Context)init.lookup("java://comp//env"); //C:에 접근
		DataSource ds=(DataSource)c.lookup("jdbc/oracle");
		
		// ODER , DataSource ds=(DataSource)c.lookup("java://comp//env/jdbc/oracle");
		conn=ds.getConnection();
		
		
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void disConnection()
	{
		try {
			if(ps!=null) ps.close();
			if(conn!=null) conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static FoodDAO newInstance()
	{
		if(dao==null)
			dao=new FoodDAO();
		return dao;
	}
	
	public void categoryInsert(CategoryVO vo)
	{
		try {
			getConnection();
			String sql="INSERT INTO cateory VALUES(?,?,?,?,?)";
			ps=conn.prepareStatement(sql);
			ps.setInt(1, vo.getCateno());
			ps.setString(2, vo.getTitle());
			ps.setString(3, vo.getSubject());
			ps.setString(4, vo.getPoster());
			ps.setString(5, vo.getLink());
			
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			disConnection();
		}
	}

	public void foodHouseInsert(FoodHouseVO vo)
	{
		try {
			getConnection();
			String sql="INSERT INTO foodhouse VALUES("
					+ "foodhouse_no_seq.nextval,?,?,?,?,?,?,?,?,?,?,?,'none')";
			
			ps=conn.prepareStatement(sql);
		
			ps.setInt(1, vo.getCno());
			ps.setString(2, vo.getTitle());
			ps.setDouble(3,vo.getScore());
			
			ps.setString(4, vo.getAddress());
			ps.setString(5, vo.getTel());
			ps.setString(6, vo.getType());
			ps.setString(7, vo.getPrice());
			ps.setString(8, vo.getImage());
			
			ps.setInt(9, vo.getGood());
			ps.setInt(10, vo.getSoso());
			ps.setInt(11, vo.getBad());
			
			//ps.setString(12,vo.getTag());
			
			
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			disConnection();
		}
	}
	
	public ArrayList<CategoryVO> categoryAllData()
	{
		ArrayList<CategoryVO> list=new ArrayList<CategoryVO>();
		try {
			getConnection();
			String sql="SELECT cateno,title,subject,poster FROM cateory ORDER BY cateno ASC";
			ps=conn.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			while(rs.next())
			{
				CategoryVO vo=new CategoryVO();
				vo.setCateno(rs.getInt(1));
				vo.setTitle(rs.getString(2));
				vo.setSubject(rs.getString(3));
				vo.setPoster(rs.getString(4));
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

	public ArrayList<FoodHouseVO> fooHouseListData(int cno)
	{
		ArrayList<FoodHouseVO> list=new ArrayList<FoodHouseVO>();
		
		try
		{
			getConnection();
			/*
			 JDBC/DBCP
			 1. 연결
			 2. SQL전송
			 3. 결과값 받기
			 4. 닫기
			 */
			String sql="SELECT image,title,score,address,no "
					+ "FROM foodhouse "
					+ "WHERE cno=?";
			ps=conn.prepareStatement(sql);
			
			ps.setInt(1, cno);
			ResultSet rs=ps.executeQuery();
			
			while(rs.next())
			{
				FoodHouseVO vo=new FoodHouseVO();
				
				// 맨 앞 데이터만 잘라오려고 해
				String img=rs.getString(1);
				vo.setImage(img.substring(0,img.indexOf("^")));
				vo.setTitle(rs.getString(2));
				vo.setScore(rs.getDouble(3));
				vo.setAddress(rs.getString(4));
				vo.setNo(rs.getInt(5));
				
				list.add(vo);

			}
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			disConnection();
		}
		return list;
	}
	
	// category data ; title , subject 
	// ArrayList = selectList()     vo = selectOne()     ; MyBatis
	public CategoryVO categoryInfoData(int cno)
	{
		CategoryVO vo=new CategoryVO();
		try
		{
			getConnection();
			String sql="SELECT title,subject "
					+ "FROM cateory "
					+ "WHERE cateno=?";
			ps=conn.prepareStatement(sql);
			ps.setInt(1, cno);
			
			ResultSet rs=ps.executeQuery();
			rs.next();
			
			vo.setTitle(rs.getString(1));
			vo.setSubject(rs.getString(2));
			
			rs.close();
					
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}finally
		{
			disConnection();
		}
		return vo;
	}
	
	// 상세보기
	public FoodHouseVO foodDetailData(int no)
	{
		FoodHouseVO vo=new FoodHouseVO();
		try
		{
			getConnection();
			String sql="SELECT image,title,score,address,tel,type,price,good,soso,bad FROM foodhouse "
					+ "WHERE no=?";
			ps=conn.prepareStatement(sql);
			ps.setInt(1, no);
			ResultSet rs=ps.executeQuery();
			rs.next();
			vo.setImage(rs.getString(1));
			vo.setTitle(rs.getString(2));
			vo.setScore(rs.getDouble(3));
			vo.setAddress(rs.getString(4));
			vo.setTel(rs.getString(5));
			vo.setType(rs.getString(6));
			vo.setPrice(rs.getString(7));
			vo.setGood(rs.getInt(8));
			vo.setSoso(rs.getInt(9));
			vo.setBad(rs.getInt(10));
			rs.close();
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			disConnection();
		}
		return vo;
	}
	
	//주소에 동이름을 잘라서 검색하면, 인근 식당을 출력할 수 있음
	public ArrayList<FoodHouseVO> foodLocationData(String loc)
	{
		ArrayList<FoodHouseVO> list=new ArrayList<FoodHouseVO>();
		try
		{
			getConnection();
							//foodhouse에 이미 no값이 들어가 있기 때문에
			String sql="SELECT image,title,score,address,tel,type,price,rownum "
					+ "FROM (SELECT image,title,score,address,tel,type,price "
					+ "FROM foodhouse "
					+ "WHERE address LIKE '%'||?||'%' "
					+ "ORDER BY score DESC) "
					+ "WHERE rownum<=5";
			ps=conn.prepareStatement(sql);
			ps.setString(1, loc);
			ResultSet rs=ps.executeQuery();
			while(rs.next())
			{
				FoodHouseVO vo=new FoodHouseVO();
				vo.setImage(rs.getString(1));
				vo.setTitle(rs.getString(2));
				vo.setScore(rs.getDouble(3));
				vo.setAddress(rs.getString(4));
				vo.setTel(rs.getString(5));
				vo.setType(rs.getString(6));
				vo.setPrice(rs.getString(7));
				list.add(vo);
			}
			rs.close();
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			disConnection();
		}
		return list;
	}
}
