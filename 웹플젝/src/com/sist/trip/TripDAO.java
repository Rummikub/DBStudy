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
			public void tripAllData(TripVO vo)
			{
				try
				{
					getConnection(); // open
					String sql="INSERT INTO trip VALUES("
											+"(SELECT NVL(MAX(MNO)+1,1)FROM trip, ?,)";  // 필요한 변수개수, 페이지 번호
					ps=conn.prepareStatement(sql); // connection
					
					// ?에 데이터 입력
					//ps.setString(1,vo.get변수명());
					
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
		}

