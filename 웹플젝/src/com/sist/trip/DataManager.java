package com.sist.trip;
import java.io.*;
import java.util.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DataManager {


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TripDAO dao = new TripDAO();
		int maxPage = 32;
		String name = "";
		String tags;
		String addr;
		double mapX;
		double mapY;
		int cnt = 0;
		try {
			dao.getConnection();
		} catch (Exception e) {
			System.out.println("error");
		}
		try {
			for(int i=0; i<maxPage; i++) {
				Document listDoc = Jsoup.connect("https://www.tripadvisor.co.kr/"
						+ "Restaurants").get();
				Elements detailList = listDoc.select("div.ui_shelf_item_detail a");
				for(int j=0; j<detailList.size(); j++) {
					Document detailDoc = Jsoup.connect("https://www.tripadvisor.co.kr"+detailList.get(j).attr("href")).get();
					TripVO vo = new TripVO();
					
					vo.setPno(i+j);
					System.out.println(i+"-"+j+" : ");
					
					try {
						name = detailDoc.selectFirst("h1#HEADING").text();
						if(dao.hasTripVOName(name)) {
							System.out.println("ToDo has "+name);
							continue;
						}
						vo.setPname(name);
					} catch (Exception e) {}
					
					try {
						vo.setAddr(detailDoc.selectFirst("div.is-hidden-mobile span.detail").text());
					} catch (Exception e) {
						e.printStackTrace();
					}
					dao.printTripData(vo);
					dao.tripInsertData(vo);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Crawling end");
	}
	
}
