package com.sist.trip;
// 가져다 사용하는 부분!!
import java.util.*;

public class MainClass {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		TripDAO dao = new TripDAO();
		ArrayList<TripVO> list=dao.tripAllData();
		
		//출력
		for(TripVO vo:list)
		{
			/*
			System.out.println(vo.getEmpno()+" "
					+vo.getEname()+" "
					+vo.getJob()+" "
					+vo.getMgr()+" "
					+vo.getHiredate().toString()+" "
					+vo.getSal()+" "
					+vo.getComm()+" "
					+vo.getDeptno());
					*/
		}
		
	}
}