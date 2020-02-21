package com.sist.food;


	import java.sql.Connection;
	import java.sql.DriverManager;
	import java.sql.PreparedStatement;
	import java.sql.ResultSet;
	import java.util.ArrayList;

import com.sist.food.FoodVO;

	public class FoodDAO {
		private Connection conn;
		private PreparedStatement ps;
		private ResultSet rs;
		private final String URL = "jdbc:oracle:thin:@211.238.142.200:1521:XE";

		public FoodDAO() {
			try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
			} catch (Exception e) {
				System.out.print("FoodDAO():");
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
		
	
		public void foodInsert(FoodVO vo) {
			try {
				getConnection();
				String sql = "INSERT INTO foodinfo VALUES((SELECT NVL(MAX(no)+1,1) FROM foodinfo),?,?,?,?,?,?,?,?)";
				ps = conn.prepareStatement(sql);
				ps.setString(1, vo.getName());
				ps.setString(2, vo.getGrade());
				ps.setString(3, vo.getTag());
				ps.setString(4, vo.getAddr());
				ps.setDouble(5, vo.getMapX());
				ps.setDouble(6, vo.getMapY());
				ps.setString(7, vo.getTel());
				ps.setString(8, vo.getOpnhr());
				ps.executeQuery();
			} catch (Exception e) {
				System.out.print("Food Detail Info Insert():");
				e.printStackTrace();
			} finally {
				disConnection();
			}
		}

		public ArrayList<FoodVO> foodAllData() {
			ArrayList<FoodVO> list = new ArrayList<FoodVO>();
			try {
				getConnection();
				String sql = "SELECT * FROM foodinfo";
				ps = conn.prepareStatement(sql);
				rs = ps.executeQuery();
				while(rs.next()) {
					FoodVO vo = new FoodVO();
					setFoodVOData(vo, rs);
					list.add(vo);
				}
				rs.close();
			} catch (Exception e) {
				System.out.print("FoodAllData():");
				e.printStackTrace();
			} finally {
				disConnection();
			}
			return list;
		}
		
		private void setFoodVOData(FoodVO vo, ResultSet rs) {
			try {
				vo.setNo(rs.getInt(1));
				vo.setName(rs.getString(2));
				vo.setGrade(rs.getString(3));
				vo.setTag(rs.getString(4));
				vo.setAddr(rs.getString(5));
				vo.setMapX(rs.getDouble(6));
				vo.setMapY(rs.getDouble(7));
				vo.setTel(rs.getString(8));
				vo.setOpnhr(rs.getString(9));
			} catch (Exception e) {
				System.out.print("setFoodVOData():");
				e.printStackTrace();
			}
		}

		public void printFoodVOData(FoodVO vo) {
			System.out.println("\t========================");
			System.out.println("no : "+vo.getNo());
			System.out.println("name : "+vo.getName());
			System.out.println("grade : "+vo.getGrade());
			System.out.println("tag : "+vo.getTag());
			System.out.println("addr : "+vo.getAddr());
			System.out.println("mapX : "+vo.getMapX());
			System.out.println("mapY : "+vo.getMapY());
			System.out.println("tel : "+vo.getTel());
			System.out.println("openhr : "+vo.getOpnhr());
			System.out.println("================================");
		}

		public boolean hasFoodVOName(String name) {
			boolean check = true;
			try {
				getConnection();
				String sql = "SELECT COUNT(*) FROM foodinfo WHERE name = ?";
				ps = conn.prepareStatement(sql);
				ps.setString(1, name);
				rs = ps.executeQuery();
				rs.next();
				if(rs.getInt(1)==0) {
					check = false;
				}
				rs.close();
			} catch (Exception e) {
				System.out.println("hasFoodName():");
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
				String sql = "SELECT no FROM foodinfo WHERE name = ?";
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
	

	