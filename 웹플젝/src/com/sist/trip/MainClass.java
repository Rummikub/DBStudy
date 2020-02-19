package com.sist.trip;
// DataManager
import java.util.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.*;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

public class MainClass {
	
	public ArrayList<TripVO> tripListData()
	
	{
		ArrayList<TripVO> list=new ArrayList<TripVO>();
		
		try{
			
				TripDAO dao=new TripDAO();
				//페이징 넘기면서 새로 만들 수 있음
				
				{
					Document doc=Jsoup.connect("https://www.tripadvisor.co.kr/Restaurants").get(); // url 주소
					Elements link=doc.select("div.ui_shelf_item_detail a"); //음식점 이름 받아오기
					for(int j=0;j<link.size();j++)
					{
						try
						{
							Element elm=link.get(j);  // link 가져옴
							String mLink="https://www.tripadvisor.co.kr/Restaurants"+elm.attr("href"); //link
							Document doc2=Jsoup.connect(mLink).get(); //<- 같은 태그에 속하면 번호 줌
							System.out.println(mLink);
							// 상세보기, 같은tag 없으므로 singular
							/*
	private int pno;
	private String pname;
	private double grade;
	private String totalreview;
	private int rank;
	private String tag;
	private String addr;
	private String opnhr;
							 */
							
							
						}catch(Exception ex) {}
					}
				}
				
				
				//System.out.println("DB Insert End......");
		}catch (Exception ex) {}
	
		return list;
	}
				
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MainClass m=new MainClass();
		m.tripListData();
	}
}