package com.sist.todo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ToDoDAO {

	private Connection conn;
	private PreparedStatement ps;
	private ResultSet rs;
	private final String URL = "jdbc:oracle:thin:@localhost:1521:XE";

	public ToDoDAO() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (Exception e) {
			System.out.print("TodoDAO():");
			e.printStackTrace();
		}
	}

	public void getConnection() {
		try {
			conn = DriverManager.getConnection(URL, "hr", "happy");
		} catch (Exception e) {
			System.out.print("getConnection():");
			e.printStackTrace();
		}
	}

	public void disConnection() {
		try {
			if (ps != null)
				ps.close();
			if (conn != null)
				conn.close();
		} catch (Exception e) {
			System.out.print("disConnection():");
			e.printStackTrace();
		}

	}

	public void toDoInsert(ToDoVO vo) {
		try {
			getConnection();
			String sql = "INSERT INTO todo VALUES((SELECT NVL(MAX(no)+1,1) FROM todo),?,?,?,?,?,?)";
			ps = conn.prepareStatement(sql);
			ps.setString(1, vo.getName());
			ps.setDouble(2, vo.getScore());
			ps.setString(3, vo.getTags());
			ps.setString(4, vo.getAddr());
			ps.setDouble(5, vo.getMapX());
			ps.setDouble(6, vo.getMapY());
			ps.executeQuery();
		} catch (Exception e) {
			System.out.print("ToDoInsert():");
			e.printStackTrace();
		} finally {
			disConnection();
		}
	}

	public ArrayList<ToDoVO> toDoAllData() {
		ArrayList<ToDoVO> list = new ArrayList<ToDoVO>();
		try {
			getConnection();
			String sql = "SELECT * FROM todo";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()) {
				ToDoVO vo = new ToDoVO();
				setToDoVOData(vo, rs);
				list.add(vo);
			}
			rs.close();
		} catch (Exception e) {
			System.out.print("ToDoAllData():");
			e.printStackTrace();
		} finally {
			disConnection();
		}
		return list;
	}

	private void setToDoVOData(ToDoVO vo, ResultSet rs) {
		try {
			vo.setNo(rs.getInt(1));
			vo.setName(rs.getString(2));
			vo.setScore(rs.getDouble(3));
			vo.setTags(rs.getString(4));
			vo.setAddr(rs.getString(5));
			vo.setMapX(rs.getDouble(6));
			vo.setMapY(rs.getDouble(7));
		} catch (Exception e) {
			System.out.print("setToDoVOData():");
			e.printStackTrace();
		}
	}

	public void printToDoVOData(ToDoVO vo) {
		System.out.println("\t========================");
		System.out.println("no : "+vo.getNo());
		System.out.println("name : "+vo.getName());
		System.out.println("score : "+vo.getScore());
		System.out.println("tags : "+vo.getTags());
		System.out.println("addr : "+vo.getAddr());
		System.out.println("mapX : "+vo.getMapX());
		System.out.println("mapY : "+vo.getMapY());
		System.out.println("================================");
	}

	public boolean hasToDoVOName(String name) {
		boolean check = true;
		try {
			getConnection();
			String sql = "SELECT COUNT(*) FROM todo WHERE name = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, name);
			rs = ps.executeQuery();
			rs.next();
			if(rs.getInt(1)==0) {
				check = false;
			}
			rs.close();
		} catch (Exception e) {
			System.out.println("hasToDoVOName():");
			e.printStackTrace();
		} finally {
			disConnection();
		}
		return check;
	}

	public int noOfName(String name) throws Exception {
		int no = 0;
		try {
			getConnection();
			String sql = "SELECT no FROM todo WHERE name = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, name);
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
