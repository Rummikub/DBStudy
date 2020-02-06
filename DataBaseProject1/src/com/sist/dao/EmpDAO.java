package com.sist.dao;
// 오라클 연동 -> mybatis는 이 부분을 포함하고 있다.(연결기능포함)
/* 오라클 연동 => SQL(오라클실행) => Java
 * 
 * 연동순서
 * 1. 드라이버 등록
 * 2. 연결
 * 3. 해제
 * 4. 기능 설정(메소드 多) ==> SQL 전송, 결과값 받기
 * 
 * SQL-DML  (CURD - 게시판 만들어보기!)
 * 
 * 
 * 			1. SELECT : 데이터 검색후 가져옴
 *   			형식 )  SELECT * , 컬럼명 (...)
 *   				  FROM 테이블명(SELECT, View)
 *   				  [ 
 *   					WHERE 조건절(컬럼명 연산자 값)
 *   						ename LIKE '%a';
 *   
 *   					GROUP BY 컬럼명(함수 ex TO_CHAR)
 *   					HAVING 그룹 조건 ==> 집합 함수사용 가능!(WHERE 에서 사용 불가능)
 *   					ORDER BY(컬럼명, 함수) ASC|DESC
 *  					]
 *  			=연산자 알고있어야..
 *  				1. 산술연산자 => 각 개인의 통계(+,-,*,/(실수로 나온다))
 *  				2. 비교연산자=> 맞으면 가져옴 => =, !=(<>, ^=), <=,>=
 * 					3. 논리연산자 AND, OR
 *  				4. 대입연산자 =(UPDATE에서 많이 사용!)
 *  				5. BETWEEN ~ AND => 페이지 나누기, 예약기간
 *  				6. IN => 포함된 데이터를 추출(다중 조건) => CHECKBOX, 필터?
 *  				7. NULL => 연산처리중에 NULL 이 있는 경우에는 연산처리 불가능
 *  						IS NULL, IS NOT NULL
 *  				8. NOT=> 부정을 나타낼때
 *  						NOT LIKE, NOT IN, NOT REGEXP_LIKE 
 *  				
 *  			=내장 함수 알고있어야..
 *  				1. 단일행 함수
 *  					=문자관련 함수
 *  						=> LENGTH : 문자 갯수를 
 *  						=> SUBSTR : 원하는 부분 추출 => 시작번호 1, 문자 갯수
 *  						=> INSTR : 원하는 문장의 위치
 *  						=> RPAD, LPAD => 비밀번호, 아이디 찾기 => 출력
 *  					=숫자관련 함수
 *  						=> ROUND : 반올림
 *  						=> TRUNK : 버림
 *  						=> CEIL : 올림(총페이지)
 *  					=날짜관련 함수
 *  						=> SYSDATE : 시스템의 시간을 불러옴 - 'DAY' => 요일?
 *  						=> MONTHS_BETWEEN :기간의 개월수
 *  					=일반 함수
 *  						=> NVL : NULL값을 원하는 값으로 변경
 *  				2. 집합 함수
 *  					=COUNT : row의 갯수 출력(로그인, 아이디중복체크)
 *  					=MAX :자동 증가번호 만들때(메뉴,,,?)
 *  					=SUM, AVG
 *  					=RANK, DENSE_RANK ***
 *  					=CUBE, ROLLUP
 *  ------------------------------------------------------------------------기본 OF 기본
 *  
 * 			2. INSERT : 데이터 추가 
 * 			3. UPDATE : 데이터 수정
 * 			4. DELETE : 데이터 삭제
 * 
 * 
 *  넘어가는 값은 항상 URL주소 안에 있고, 
 *  사용자한테 받은 영화넘버를 매개변수로 받아서 값을 가져옴.
 *  예매좌석같은 경우 BETWEEN을 사용하여 색상변화..?
 *  동시선택시 실시간으로 ajax를 이용해 처리함
 *  JS, AJAX
 *  HOBER, HOVER? BOOTSTRAP 수정 가능한 정도까지만..
 *  
 *  
 *  JS, JQEURY, JAVA, 
 *  
 *   1. INNER JOIN
 *   2. OUTER JOIN,  LEFT OUTER JOIN, RIGHT OUTER JOIN
 *   3. FULL OUTER JOIN
 *  	
 *   
 * 
 */
import java.util.*;
import java.sql.*;

public class EmpDAO {

	// 연결도구 Connection (Socket) 
	private Connection conn;
	// 입출력 => inputStream, outputStream
	private PreparedStatement ps;
	//URL 주소
	private final String URL="jdbc:oracle:thin:@localhost:1521:XE";
	
	//드라이버 등록 => 한번수행 => 생성자
	public EmpDAO()
	{
		try
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			
		}catch(Exception ex) 
		{
			// 드라이버 연결 문제시 해결
			ex.printStackTrace();
			
		}
		
	}
	
	
	//연결 - 클래스화시키기! -> jar파일로! 라이브러리화!-> Database는  보지 못하게 노출이 안되도록 만들어야한다.-> but,developesql는 누구나 볼수있다.ip만 바꾸면 가능!
	
	public void getConnection()
	{
		try
		{
			//id,pwd넘기기
		conn=DriverManager.getConnection(URL,"hr", "happy");
		//conn hr/happy
		  
			
		}catch(Exception ex){}
	}
	
	
	//해제 
	public void disConnection()
	{
		
		try
		{	
			if(ps!=null) ps.close();
			if(conn!=null) conn.close();
			
		}catch(Exception ex) {}
		
	}
	
	
	//기능수행 1
	// 값 받을때 값이 리스트면 리스트 한개면 한개
	// SELECT => 전체데이터 읽기 ==> ARRAYLIST로 
	
	public ArrayList<EmpVO> empAllData()
	{
		ArrayList<EmpVO> list = new ArrayList<EmpVO>();
		
		try
		{
			
			// 연결
			getConnection();
			// sql 문장 제작
			String sql="SELECT * " // 공백유위
					+"FROM emp "
					+"ORDER BY empno"; // 정렬한 상태로 가져오기 // 매번 데이터 가져올때 order by 해서 가져오기.// order는 속도 느려져 ==> index로 바꿀 예정!
			
			// 오라클에서 전송함
			ps=conn.prepareStatement(sql); 
			// 결과값 받기 - 울컴에 저장해놓기
			ResultSet rs=ps.executeQuery(); // 저장해 놓고 실행하기 executequery
			// ps를 실행하여 resultset rs에 (next.() 커서를 옮기면서 값을 읽음-> 값이 없으면 false되고 빠져나감.)
			// 맨위에서부터 읽으면 .next() - 
			// 맨 아래부터 읽으면 .previous()로
			while(rs.next()) //횟수가 모르기때문에 while문 이용!
			{
				//한줄읽어서 저장 empVO // 8개를 읽
				EmpVO vo = new EmpVO();
				vo.setEmpno(rs.getInt(1));
				vo.setEname(rs.getString(2));
				vo.setJob(rs.getString(3));
				vo.setMgr(rs.getInt(4));
				vo.setHiredate(rs.getDate(5));
				vo.setSal(rs.getInt(6));
				vo.setComm(rs.getInt(7));
				vo.setDeptno(rs.getInt(8));
				
				list.add(vo);
				
			}
			rs.close();
			
			
			// list에 첨부
			
			
		}catch(Exception ex)
		{
			// 에러처리
			ex.printStackTrace();
		}
		finally
		{
			//연결해제
			disConnection();
			
		}
		
		return list;
		
		
	}
	
	
	
	// SELECT => 원하는 컬럼만 읽기 ==> VO 한개만
	// SELECT => WHERE 문장 이용 -스캐너로 4번을 받아서 4번만 가져오기?
	public EmpVO empDetailData(int empno)   // 매개변수는 항상 중복이 안되는 값으로 가져와야한다. primary key -거의 int형!
	{	
		EmpVO vo=new EmpVO();
		
		try
		{
			
			// 연결
			getConnection();
			// sql 문장 제작
			String sql="SELECT * " // 공백유위
					+"FROM emp "
					+"WHERE empno=?"; // 정렬한 상태로 가져오기 // 매번 데이터 가져올때 order by 해서 가져오기.// order는 속도 느려져 ==> index로 바꿀 예정!
			
			// 오라클에서 전송함
			
			ps=conn.prepareStatement(sql); 
			ps.setInt(1,  empno); // ps에 값을 채워주기!!
			// 결과값 받기 - 울컴에 저장해놓기
			ResultSet rs=ps.executeQuery(); // 저장해 놓고 실행하기 executequery
			// ps를 실행하여 resultset rs에 (next.() 커서를 옮기면서 값을 읽음-> 값이 없으면 false되고 빠져나감.)
			// 맨위에서부터 읽으면 .next() - 
			// 맨 아래부터 읽으면 .previous()로
			
				rs.next(); // 커서를 데이터 있는 곳에 바꿔놓음
			
				
				vo.setEmpno(rs.getInt(1));
				vo.setEname(rs.getString(2));
				vo.setJob(rs.getString(3));
				vo.setMgr(rs.getInt(4));
				vo.setHiredate(rs.getDate(5));
				vo.setSal(rs.getInt(6));
				vo.setComm(rs.getInt(7));
				vo.setDeptno(rs.getInt(8));
				
			
			rs.close();
			
			
			
		}catch(Exception ex)
		{
			// 에러처리
			ex.printStackTrace();
		}
		finally
		{
			//연결해제
			disConnection();
			
		}
		
		return vo;	
	}
	
	
	
	
	// SELECT => 연산자 사용 방법   리턴형/ 매개변수이용하여  empFindData()
	
	public ArrayList<EmpVO> empFindData(String ename)
	{
			ArrayList<EmpVO> list = new ArrayList<EmpVO>();
		
		try
		{
			
			// 연결
			getConnection();
			// sql 문장 제작
			String sql="SELECT * " // 공백유위
					+"FROM emp "
					+"WHERE ename LIKE '%'||?||'%' "   // 값이 들어오는 곳은 ? 으로 한다. 사용자가 입력한 값 ==> preparedstatement
									// '%"+ename+"%' 값과 동시에 들어가는 것 statement
					+"ORDER BY empno"; // 정렬한 상태로 가져오기 // 매번 데이터 가져올때 order by 해서 가져오기.// order는 속도 느려져 ==> index로 바꿀 예정!
			
			// 오라클에서 전송함
			ps=conn.prepareStatement(sql);
			ps.setString(1,  ename); // ps에 값을 채워주기!!
			
			
			// 결과값 받기 - 울컴에 저장해놓기
			ResultSet rs=ps.executeQuery(); // 저장해 놓고 실행하기 executequery
			// ps를 실행하여 resultset rs에 (next.() 커서를 옮기면서 값을 읽음-> 값이 없으면 false되고 빠져나감.)
			// 맨위에서부터 읽으면 .next() - 
			// 맨 아래부터 읽으면 .previous()로
			while(rs.next()) //횟수가 모르기때문에 while문 이용!
			{
				//한줄읽어서 저장 empVO // 8개를 읽
				EmpVO vo = new EmpVO();
				vo.setEmpno(rs.getInt(1));
				vo.setEname(rs.getString(2));
				vo.setJob(rs.getString(3));
				vo.setMgr(rs.getInt(4));
				vo.setHiredate(rs.getDate(5));
				vo.setSal(rs.getInt(6));
				vo.setComm(rs.getInt(7));
				vo.setDeptno(rs.getInt(8));
				
				list.add(vo);
				
			}
			rs.close();
			
			
			// list에 첨부
			
			
		}catch(Exception ex)
		{
			// 에러처리
			ex.printStackTrace();
		}
		finally
		{
			//연결해제
			disConnection();
			
		}
		
		return list;
		
		
		
	}
	
	public ArrayList<EmpVO> empRangeData(int year) // 사용자가 보내는 값
	{
			ArrayList<EmpVO> list = new ArrayList<EmpVO>();
		
		try
		{
			
			// 연결
			getConnection();
			// sql 문장 제작
			String sql="SELECT * " // 공백유위
					+"FROM emp "
					+"WHERE hiredate BETWEEN ? AND ? "   // 값이 들어오는 곳은 ? 으로 한다. 사용자가 입력한 값 ==> preparedstatement
									// '%"+ename+"%' 값과 동시에 들어가는 것 statement
					+"ORDER BY hiredate"; // 정렬한 상태로 가져오기 // 매번 데이터 가져올때 order by 해서 가져오기.// order는 속도 느려져 ==> index로 바꿀 예정!
			
			// 오라클에서 전송함
			ps=conn.prepareStatement(sql);
			ps.setString(1, year+"/01/01"); // 첫번째? // ps에 값을 채워주기!!
			ps.setString(2, year+"/12/31"); //두번쨰?
			// 받은 값을 오라클에 보내는 곳.!
			
			// 결과값 받기 - 울컴에 저장해놓기
			ResultSet rs=ps.executeQuery(); // 저장해 놓고 실행하기 executequery
			// ps를 실행하여 resultset rs에 (next.() 커서를 옮기면서 값을 읽음-> 값이 없으면 false되고 빠져나감.)
			// 맨위에서부터 읽으면 .next() - 
			// 맨 아래부터 읽으면 .previous()로
			while(rs.next()) //횟수가 모르기때문에 while문 이용!
			{
				//한줄읽어서 저장 empVO // 8개를 읽
				EmpVO vo = new EmpVO();
				vo.setEmpno(rs.getInt(1));
				vo.setEname(rs.getString(2));
				vo.setJob(rs.getString(3));
				vo.setMgr(rs.getInt(4));
				vo.setHiredate(rs.getDate(5));
				vo.setSal(rs.getInt(6));
				vo.setComm(rs.getInt(7));
				vo.setDeptno(rs.getInt(8));
				
				list.add(vo);
				
			}
			rs.close();
			
			
			// list에 첨부
			
			
		}catch(Exception ex)
		{
			// 에러처리
			ex.printStackTrace();
		}
		finally
		{
			//연결해제
			disConnection();
			
		}
		
		return list;
		
		
		
	}
	
	
	
	// SELECT => 함수이용
	// SELECT => GROUP BY
	
	
	
}
