package com.sist.food;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ReviewDAO {

	private Connection conn;
	private PreparedStatement ps;
	private ResultSet rs;
	private final String URL = "jdbc:oracle:thin:211.238.142.200:1521:XE";

	public ReviewDAO() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (Exception e) {
			System.out.print("ReviewDAO():");
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

	public void reviewInsert(ReviewVO vo) {
		try {
			getConnection();
			String sql = "INSERT INTO review VALUES((SELECT NVL(MAX(no)+1,1) FROM review),?,?,?,?,?,?,?,?)";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, vo.getTdNo());
			ps.setString(2, vo.getId());
			ps.setString(3, vo.getName());
			ps.setInt(4, vo.getScore());
			ps.setString(5, vo.getRegdate());
			ps.setString(6, vo.getTitle());
			ps.setString(7, vo.getContent());
			ps.setString(8, vo.getExdate());
			ps.executeUpdate();
		} catch (Exception e) {
			System.out.println("reviewInsert():");
			e.printStackTrace();
		} finally {
			disConnection();
		}
	}

	public boolean hasReview(int tdNo) {
		boolean bCheck = false;

		try {
			getConnection();
			String sql = "SELECT CASE WHEN EXISTS (SELECT 1 FROM review WHERE tdNo = ?) THEN 1 ELSE 0 END FROM DUAL";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, tdNo);
			ResultSet rs = ps.executeQuery();
			rs.next();
			if(rs.getInt(1)==1) bCheck = true;
			rs.close();
		} catch (Exception e) {
			System.out.println("hasReview():");
			e.printStackTrace();
			return true;
		} finally {
			disConnection();
		}

		return bCheck;
	}

	public ArrayList<String> getAllUserIdList() {
		ArrayList<String> list = new ArrayList<String>();

		try {
			getConnection();
			String sql = "SELECT DISTINCT(id) FROM review";
			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while(rs.next()) {
				String id = rs.getString(1);
				list.add(id);
				System.out.println("add "+id+" to list.");
			}

			rs.close();
		} catch (Exception e) {
			System.out.println("getAllUserNameList():");
			e.printStackTrace();
		} finally {
			disConnection();
		}

		return list;
	}

}