package com.sist.retaurant;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import com.sist.retaurant.*;

public class RestaurantManager {
	/* FOODINFO
	CNO          NUMBER         
TITLE        VARCHAR2(200)  
PHOTO        VARCHAR2(1000) 
MENULINK     VARCHAR2(200)  
OPNHRLINK    VARCHAR2(200)  
	 */
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		RestaurantDAO dao=RestaurantDAO.newInstance();
		
		//전체 몇 페이지
		int maxPage = 1;
		//int maxPage = 3685;
		String title = "";
		String photo;
		String menulink;
		String opnhrlink;
		
		int cnt = 0;
		try {
			dao.getConnection();
		} catch (Exception e) {
			System.out.println("error");
		}
		
		//https://www.tripadvisor.co.kr/Restaurants-g294197-Seoul.html
		//음식점 타이틀 
		try {
			dao.getConnection();
		} catch (Exception e) {
			System.out.println("error");
		}
		try {
			for(int i=0; i<maxPage; i++)
			{
				Document listDoc = Jsoup.connect("https://www.tripadvisor.co.kr/"
						+ "Restaurants-g294197-Activities-oa"+30*i+"-Seoul.html#EATERY_LIST_CONTENTS").get();

				Elements detailList = listDoc.select("div.wQjYiB7z a");

				for(int j=0; j<detailList.size(); j++) {

					Document detailDoc = Jsoup.connect("https://www.tripadvisor.co.kr"+detailList.get(j).attr("href")).get();
					RestaurantVO vo = new RestaurantVO();	
					
					vo.setCno(i+j);
					System.out.println(i+"-"+j+" : ");	
					
					//음식점 장소 이름
					try {
						title = detailDoc.selectFirst("h1.ui_header").text();
						if(dao.hasNoName(title))
						{
							System.out.println("Retaurant has "+title);
							continue;
							
						}
		
						vo.setTitle(title);

					} catch (Exception e) {
				
						continue;


					}
					
					
					/*
					 
					// *메뉴는 빼자 => 없는 애도 있음!!! ---------------------------------------------나중엔 빼기
					try {
						menulink = detailDoc.selectFirst("").text();
					} catch (Exception e) {
						e.printStackTrace();
						
						continue;
					}
					
					*/
					
					
				
					
					//*운영시간링크 div.yF-2QEPN div.all-open-hours
					try {
						vo.setOpnhrlink(detailDoc.selectFirst(" div.yF-2QEPN div.all-open-hours").text());
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
					
			
				
					
					cnt++;
					System.out.print(cnt);
					dao.printRestaurantVOData(vo);
					//dao.infoInsert(vo);
				
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Crawling end");
	}
	
}