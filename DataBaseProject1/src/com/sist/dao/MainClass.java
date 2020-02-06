package com.sist.dao;
// 가져다 사용하는 부분!!
import java.util.*;

public class MainClass {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		EmpDAO dao = new EmpDAO();
		ArrayList<EmpVO> list=dao.empAllData();
		
		//출력
		for(EmpVO vo:list)
		{
			System.out.println(vo.getEmpno()+" "
					+vo.getEname()+" "
					+vo.getJob()+" "
					+vo.getMgr()+" "
					+vo.getHiredate().toString()+" "
					+vo.getSal()+" "
					+vo.getComm()+" "
					+vo.getDeptno());
		}
		
		Scanner scan = new Scanner(System.in);
		
		System.out.println("사번을 입력하세요: ");
		int empno=scan.nextInt();
		EmpVO vo= dao.empDetailData(empno);	
		System.out.println("========검색결과 ======");
		
		System.out.println(vo.getEmpno()+" "
				+vo.getEname()+" "
				+vo.getJob()+" "
				+vo.getMgr()+" "
				+vo.getHiredate().toString()+" "
				+vo.getSal()+" "
				+vo.getComm()+" "
				+vo.getDeptno());
		
		
		
		
		
		
		
		
		
		
		// 알파벳으로 이름 검색
		System.out.println("대문자 알파벳을 입력하세요: ");
		String ename= scan.next();
		list =dao.empFindData(ename.toUpperCase());  // 이름을 모두 대문자로  변화시킥
		System.out.println("========검색결과 ======");
		
		for(EmpVO vo1:list)
		{
			System.out.println(vo1.getEmpno()+" "
					+vo1.getEname()+" "
					+vo1.getJob()+" "
					+vo1.getMgr()+" "
					+vo1.getHiredate().toString()+" "
					+vo1.getSal()+" "
					+vo1.getComm()+" "
					+vo1.getDeptno());
		}
		
		
		
		
		// 년도로 검색
		System.out.println("년도를 입력하세요:");
		int year = scan.nextInt();
		list=dao.empRangeData(year);
		System.out.println("=======년도 검색 결과 ============");
		
		for(EmpVO vo2:list)
		{
			System.out.println(vo2.getEmpno()+" "
					+vo2.getEname()+" "
					+vo2.getJob()+" "
					+vo2.getMgr()+" "
					+vo2.getHiredate().toString()+" "
					+vo2.getSal()+" "
					+vo2.getComm()+" "
					+vo2.getDeptno());
		}
		
		
		
		
		/*System.out.println(vo.getEmpno()+" "
				+vo.getEname()+" "
				+vo.getJob()+" "
				+vo.getMgr()+" "
				+vo.getHiredate().toString()+" "
				+vo.getSal()+" "
				+vo.getComm()+" "
				+vo.getDeptno());
		*/
		
		// int year 받기!!   
		
	}

}
