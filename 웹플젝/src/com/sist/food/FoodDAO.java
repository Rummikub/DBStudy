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
		
		//음식점 정보 row단위 삽입
		public void foodInsert(FoodVO vo) {
			try {
				getConnection();
				String sql = "INSERT INTO restaurant VALUES((SELECT NVL(MAX(no)+1,1) FROM restaurant),?,?,?,?,?,?,?,?,rNo,id,title,bubble,regdate,content,expdate)";
				ps = conn.prepareStatement(sql);
				ps.setString(1, vo.getName());
				ps.setString(2, vo.getTags());
				ps.setString(3, vo.getAddr());
				ps.setString(4, vo.getTel());
				ps.setString(5, vo.getOpenHours());
				ps.setString(6, vo.getPrice());
				ps.setDouble(7, vo.getLat());
				ps.setDouble(8, vo.getLng());
				ps.executeQuery();
			} catch (Exception e) {
				System.out.print("Food Detail Info Insert():");
				e.printStackTrace();
			} finally {
				disConnection();
			}
		}

		//VO
		public ArrayList<FoodVO> foodAllData() {
			ArrayList<FoodVO> list = new ArrayList<FoodVO>();
			try {
				getConnection();
				String sql = "SELECT * FROM restaurant";
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
				vo.setTags(rs.getString(3));
				vo.setAddr(rs.getString(4));
				vo.setTel(rs.getString(5));
				vo.setOpenHours(rs.getString(6));
				vo.setPrice(rs.getString(7));
				vo.setLat(rs.getDouble(8));
				vo.setLng(rs.getDouble(9));
				
			} catch (Exception e) {
				System.out.print("setFoodVOData():");
				e.printStackTrace();
			}
		}

		//잘 들어갔는지 출력
		public void printFoodVOData(FoodVO vo) {
			System.out.println("\t============================");
			System.out.println("no : "+vo.getNo());
			System.out.println("name : "+vo.getName());
			System.out.println("tags : "+vo.getTags());
			System.out.println("주소 : "+vo.getAddr());
			System.out.println("tel : "+vo.getTel());
			System.out.println("운영시간 : "+vo.getOpenHours());
			System.out.println("가격 : "+vo.getPrice());
			System.out.println("X : "+vo.getLat());
			System.out.println("Y : "+vo.getLng());
			System.out.println("================================");
		}

		public boolean hasFoodVOName(String name) {
			boolean check = true;
			try {
				getConnection();
				String sql = "SELECT COUNT(*) FROM resaurant WHERE name = ?";
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
				String sql = "SELECT no FROM restaurant WHERE name = ?";
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
		
		//Review
		public void reviewInsert(FoodVO vo) {
			try {
				getConnection();
				String sql = "INSERT INTO review VALUES((SELECT NVL(MAX(no)+1,1) FROM restaurant),name,tags,addr,tel,openhours,price,lat,lng,?,?,?,?,?,?,?)";
				ps = conn.prepareStatement(sql);
				ps.setInt(1, vo.getrNo());
				ps.setString(2, vo.getId());
				ps.setString(3, vo.getTitle());
				ps.setString(4, vo.getBubble());
				ps.setString(5, vo.getRegdate());
				ps.setString(6, vo.getContent());
				ps.setString(7, vo.getExpdate());
				ps.executeUpdate();
			} catch (Exception e) {
				System.out.println("reviewInsert():");
				e.printStackTrace();
			} finally {
				disConnection();
			}
		}

		public boolean hasReview(int rNo) {
			boolean bCheck = false;

			try {
				getConnection();
				String sql = "SELECT CASE WHEN EXISTS (SELECT 1 FROM review WHERE rNo = ?) THEN 1 ELSE 0 END FROM DUAL";
				ps = conn.prepareStatement(sql);
				ps.setInt(1, rNo);
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
				String sql = "SELECT DISTINCT(id) FROM restaurant";
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
	

	