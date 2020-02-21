package com.sist.retaurant;
import java.util.*;
import java.sql.*;

//싱글턴 사용
import javax.sql.*;

import com.sist.food.FoodVO;

import javax.naming.*;


public class RestaurantDAO {
	
	
	private Connection conn;
	private PreparedStatement ps;
	private ResultSet rs;
	private final String URL = "jdbc:oracle:thin:@localhost:1521:XE";
	
	//SingleTon
	private static RestaurantDAO dao;
	
	public RestaurantDAO()
	{
		try
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");
		}catch (Exception ex) 
		{
			ex.printStackTrace();
		}
	}
	
	//Single Ton 생성
	public static RestaurantDAO newInstance()
	{
		if(dao==null)
			dao=new RestaurantDAO();
		return dao;
	}
		
	
	public void getConnection()
	{

		try
		{
			conn=DriverManager.getConnection(URL,"hr","happy");
		}catch(Exception e)
		{}
	}
	
	
	public void disConnection()
	{
		try {
			if(ps!=null) ps.close();
			if(conn!=null) conn.close();
		} catch (Exception e) {
			System.out.println("disconnection():");
			e.printStackTrace();
		}
	}
	
	public void infoInsert(RestaurantVO vo) {
		try {
			getConnection();
			String sql = "INSERT INTO restaurant VALUES((SELECT NVL(MAX(cno)+1,1) FROM restaurant),?,?,?,?)";
			ps = conn.prepareStatement(sql);
			ps.setString(1, vo.getTitle());
			ps.setString(2, vo.getPhoto());
			ps.setString(3, vo.getMenulink());
			ps.setString(4, vo.getOpnhrlink());

			ps.executeQuery();
		} catch (Exception e) {
			System.out.print("Restaurant Detail Info Insert():");
			e.printStackTrace();
		} finally {
			disConnection();
		}
	}

	public ArrayList<RestaurantVO> restaurantAllData() {
		ArrayList<RestaurantVO> list = new ArrayList<RestaurantVO>();
		try {
			getConnection();
			String sql = "SELECT * FROM restaurant";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()) {
				RestaurantVO vo = new RestaurantVO();
				setRestaurantVOData(vo, rs);
				list.add(vo);
			}
			rs.close();
		} catch (Exception e) {
			System.out.print("RestaurantAllData():");
			e.printStackTrace();
		} finally {
			disConnection();
		}
		return list;
	}
	
	private void setRestaurantVOData(RestaurantVO vo, ResultSet rs) {
		try {
			vo.setCno(rs.getInt(1));
			vo.setTitle(rs.getString(2));
			vo.setPhoto(rs.getString(3));
			vo.setMenulink(rs.getString(4));
			vo.setOpnhrlink(rs.getString(5));
		;
		} catch (Exception e) {
			System.out.print("setRestaurantVOData():");
			e.printStackTrace();
		}
	}

	public void printRestaurantVOData(RestaurantVO vo) {
		System.out.println("\t========================");
		System.out.println("cno : "+vo.getCno());
		System.out.println("음식점이름 : "+vo.getTitle());
		System.out.println("리뷰사진 : "+vo.getPhoto());
		System.out.println("메뉴링크 : "+vo.getMenulink());
		System.out.println("운영시간 : "+vo.getOpnhrlink());
		System.out.println("================================");
	}
	public boolean hasNoName(String title) {
		boolean check = true;
		try {
			getConnection();
			String sql = "SELECT COUNT(*) FROM restaurant WHERE title = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, title);
			rs = ps.executeQuery();
			rs.next();
			if(rs.getInt(1)==0) {
				check = false;
			}
			rs.close();
		} catch (Exception e) {
			System.out.println("Restaurant has no Name():");
			e.printStackTrace();
		} finally {
			disConnection();
		}
		return check;
	}

	public int noOfName(String title) throws Exception {
		int no = 0;
		try {
			getConnection();
			String sql = "SELECT no FROM restaurant WHERE title = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, title);
			ResultSet rs = ps.executeQuery();
			rs.next();
			no = rs.getInt(1);
			rs.close();
		} finally {
			disConnection();
		}
		return no;
	}
	
  
}
