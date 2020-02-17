package com.sist.trip;

import java.sql.*;
import java.util.*;

public class TripDAO {

		private Connection conn;
		private PreparedStatement ps;
		private final String URL="jdbc:oracle:thin:@localhost:1521:XE";
		
		//1. 드라이버 등록
		public TripDAO()
		{
			try
			{
				Class.forName("oracle.jdbc.driver.OracleDriver");
			}catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
			//2. 오라클 연결
			public void getConnection()
			{
				try
				{
					conn=DriverManager.getConnection(URL,"hr","happy");
				}catch (Exception ex) {}
			}
			//3. 오라블 해제
			public void disConnection()
			{
				try
				{
						if(ps!=null)ps.close();
						if(conn!=null)conn.close();
				}catch (Exception ex) {}
			}
			
			//4. 기능처리
			public void tripInsertData(TripVO vo)
			{
				try
				{
					getConnection(); // open
					String sql="INSERT INTO trip VALUES((SELECT NVL(MAX(PNO)+1,1)FROM trip,?,?,?,?,?,?,?)";  // 필요한 변수개수, 페이지 번호
					ps=conn.prepareStatement(sql); // connection
					ResultSet rs=ps.executeQuery();
				
					// ?에 데이터 입력
					//ps.setString(1,vo.get변수명());
					ps.setString(1, vo.getPname());
					ps.setDouble(2, vo.getGrade());
					ps.setString(3,vo.getTotalreview());
					ps.setInt(4, vo.getRank());
					ps.setString(5, vo.getTag());
					ps.setString(6, vo.getAddr());
					ps.setString(7, vo.getOpnhr());
					
					ps.executeUpdate();

				}catch (Exception ex)
				{
					ex.printStackTrace();
				}
				finally // 데이터 저장할 때 마다 닫아줘야 메모리 절약
				{
					disConnection();
				}
			}
			
			public ArrayList<TripVO> tripAllData() {
				ArrayList<TripVO> list = new ArrayList<TripVO>();
				try {
					getConnection();
					String sql= "SELECT pno,pname,grade,totalreview,rank,addr,opnhr FROM detailpage";
					ps=conn.prepareStatement(sql);
					ResultSet rs=ps.executeQuery();
					
					while(rs.next())
					{
						
						TripVO vo = new TripVO();
						vo.setPno(rs.getInt(1));
						vo.setPname(rs.getString(2));
						vo.setGrade(rs.getInt(3));
						vo.setTotalreview(rs.getString(4));
						vo.setRank(rs.getInt(5));
						vo.setTag(rs.getString(6));
						vo.setAddr(rs.getString(7));
						vo.setOpnhr(rs.getString(8));
						
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
			
			private void setTripData(TripVO vo, ResultSet rs){
				try
				{
					
				}catch (Exception ex)
				{
					System.out.println("setData():");
					ex.printStackTrace();
					
				}
			}
			
			public void printTripData(TripVO vo) {
				System.out.println("\t======================");
				System.out.println("pno: "+vo.getPno());
				System.out.println("pname: "+vo.getPname());
				System.out.println("grade: "+vo.getGrade());
				System.out.println("review개수: "+vo.getTotalreview());
				System.out.println("rank: "+vo.getRank());
				System.out.println("tag: "+vo.getTag());
				System.out.println("addr: "+vo.getAddr());
				System.out.println("openhour: "+vo.getOpnhr());	
			}
			
			public boolean hasTripVOName(String pname) {
				boolean check=true;
				try
				{
					getConnection();
					String sql="SELECT COUNT(*) FROM trip WHERE pname=?";
					ps=conn.prepareStatement(sql);
					ps.setString(1, pname);
					ResultSet rs=ps.executeQuery();
					rs.next();
					if(rs.getInt(1)==0) {
						check=false;
					}
				}catch(Exception e)
				{
					System.out.println("hasTripVOName(): ");
					e.printStackTrace();					
				}
				finally
				{
					disConnection();
				}
				return check;
			}
			
			public int noOfName(String pname) throws Exception {
				int no=0;
				try{
					getConnection();
					String sql="SELECT no FROM trip WHERE pname=?";
					ps=conn.prepareStatement(sql);
					ps.setString(1, pname);
					ResultSet rs=ps.executeQuery();
					rs.next();
					no=rs.getInt(1);
					rs.close();
				}finally {
					disConnection();
				}
				return no;
			}
		
		}

