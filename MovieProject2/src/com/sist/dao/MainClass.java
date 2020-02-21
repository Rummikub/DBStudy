package com.sist.dao;
import java.util.*;
import com.sist.dao.*;
import com.sist.vo.*;
public class MainClass {
//값을 가져오는지 확인하려고
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MovieDAO dao=MovieDAO.newInstance();
		ArrayList<MovieVO> list=dao.movieListData(1, 2); // 개봉예정작
		for(MovieVO vo:list)
		{
			System.out.println(vo.getMno()+" "+vo.getTitle());
		}
	}

}
