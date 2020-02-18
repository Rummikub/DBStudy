package com.sist.todo;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ToDoCrawler {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ToDoDAO dao = new ToDoDAO();
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
						+ "Attractions-g294197-Activities-oa"+30*i+"-Seoul.html#FILTERED_LIST").get();

				Elements detailList = listDoc.select("div.listing_title a");

				for(int j=0; j<detailList.size(); j++) {

					Document detailDoc = Jsoup.connect("https://www.tripadvisor.co.kr"+detailList.get(j).attr("href")).get();
					ToDoVO vo = new ToDoVO();
					

					vo.setNo(i+j);
					System.out.println(i+"-"+j+" : ");

					
					try {
						name = detailDoc.selectFirst("h1#HEADING").text();
						if(dao.hasToDoVOName(name)) {
							System.out.println("ToDo has "+name);
							continue;

						}

						vo.setName(name);

					} catch (Exception e) {
					
						continue;


					}
					
					try {
						tags = detailDoc.selectFirst("span.is-hidden-mobile div.detail").text();
						if(tags.contains("더 보기")) {
							tags = tags.replace("더 보기", "");
						}
						vo.setTags(tags.trim());
					} catch (Exception e) {
						continue;
					}
					
					try {
						vo.setAddr(detailDoc.selectFirst("div.is-hidden-mobile span.detail").text());
					} catch (Exception e) {
						continue;
					}
					
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
					cnt++;
					System.out.print(cnt);
					dao.printToDoVOData(vo);
					dao.toDoInsert(vo);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Crawling end");
	}
	
}
