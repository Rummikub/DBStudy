package com.sist.dao;
import java.util.*;
public class MainClass {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EmpDAO dao=new EmpDAO();
		ArrayList<EmpVO> list=dao.empAllData();
		
		for(EmpVO vo:list)
		{
			System.out.println(vo.getEname());
			System.out.println(vo.getDvo().getDname());
		}
		// 메인에서는 tomcat이 작동 안됨   ==> 서블렛을 작동시켜야지만 tomcat 작동해
	}

}
