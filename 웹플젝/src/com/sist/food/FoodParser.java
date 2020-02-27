package com.sist.food;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.sist.todo.ToDoVO;

public class FoodParser {
	/* REVIEW
	NO         NUMBER         
	TDNO       NUMBER         
	ID         VARCHAR2(200)  
	NAME       VARCHAR2(1000) 
	SCORE      NUMBER         
	REGDATE    VARCHAR2(100)  
	TITLE      VARCHAR2(500)  
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FoodDAO dao = new FoodDAO();
		
		
		//전체 몇 페이지
		int maxPage = 333;
		String name = "";          
		String tag;
		String addr;
		double mapX;
		double mapY;
		String tel;
		String opnhr;
		
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
			for(int i=1; i<maxPage; i++)
			{
				Document listDoc = Jsoup.connect("https://www.tripadvisor.co.kr/"
						+ "Restaurants-g294197-Activities-oa"+30*i+"-Seoul.html#EATERY_LIST_CONTENTS").get();

				Elements detailList = listDoc.select("div.wQjYiB7z a");

				for(int j=0; j<detailList.size(); j++) {

					Document detailDoc = Jsoup.connect("https://www.tripadvisor.co.kr"+detailList.get(j).attr("href")).get();
					FoodVO vo = new FoodVO();		
					 
					vo.setNo(i+j);
					System.out.println(i+"-"+j+" : ");	
					
					//음식점 장소 이름
					try {
						name = detailDoc.selectFirst("h1.ui_header").text();
						if(dao.hasFoodVOName(name))
						{
							System.out.println(name);
							continue;
							
						}
		
						vo.setName(name);

					} catch (Exception e) {
						
						continue;


					}
				
					
					
					//태그
					try {
						tag = detailDoc.selectFirst("div.header_links").text();
						if(tag.contains("더 보기")) {
							tag = tag.replace("더 보기", "");
						}
						vo.setTag(tag.trim());
						System.out.println(tag);
					} catch (Exception e) {
						//continue;
					}
					
					//주소
					try {
						vo.setAddr(detailDoc.selectFirst("div.blRow span.detail").text());
					} catch (Exception e) {
						
						continue;
					}
					
					//전화번호
					try {
						vo.setTel(detailDoc.selectFirst("a.ui_link_container span.detail").text());
					} catch (Exception e) {
						
						continue;
					}
					
					
					//경도,위도
					try {
						String html = detailDoc.html();
						int idx = html.indexOf("\"name\":\""+name+"\"");
						html = html.substring(idx);
						idx = html.indexOf("coords");
						html = html.substring(idx);
						idx = html.indexOf("},{");
						html = html.substring(0, idx);
						String[] maps = html.split("\"");
						String map = maps[maps.length-1];
						maps = map.split(",");
						mapX = Double.parseDouble(maps[0]);
						mapY = Double.parseDouble(maps[1]);
						vo.setMapX(mapX);
						vo.setMapY(mapY);
					} catch (Exception e) {
						
						continue;
					}
					
					//별점
					try {
						String temp=detailDoc.selectFirst("div.ratingContainer span").attr("alt");
						String grade=temp.substring(temp.indexOf("중 ")+1);
						vo.setGrade(grade);
	
					} catch (Exception e) {
						
						continue;
					}
					
				
					
					//운영시간
					try {
						vo.setOpnhr(detailDoc.select("div.locationHoursInner span").get(3).text());
					} catch (Exception e) {
						
						continue;
					}
					
					
					cnt++;
					System.out.print(cnt);
					dao.printFoodVOData(vo);
					dao.foodInsert(vo);
				
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Crawling end");
	}
	
}