package com.sist.food;

import java.io.IOException;
import java.sql.Driver;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ReviewCrawler {

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		FoodDAO dao = new FoodDAO();
		int maxPage = 333;
		String name = "";
		int cnt = 0;
		ReviewDAO rvdao = new ReviewDAO();
		try {
			for (int i = 0; i < maxPage; i++) {
				Document listDoc = Jsoup.connect("https://www.tripadvisor.co.kr/" + "Attractions-g294197-Activities-oa"
						+ 30 * i + "-Seoul.html#FILTERED_LIST").get();
				Elements detailList = listDoc.select("div.listing_title a");
				for (int j = 0; j < detailList.size(); j++) {
					Document detailDoc = Jsoup.connect("https://www.tripadvisor.co.kr" + detailList.get(j).attr("href"))
							.get();
					int tdNo;
					try {
						name = detailDoc.selectFirst("h1#HEADING").text();
						tdNo = dao.noOfName(name);
						//System.out.println(todoNo + " : " + name);
						if(rvdao.hasReview(tdNo)) {
							System.out.println(name + " has review.");
							continue;
						}
					} catch (Exception e) {
						System.out.println("todo doesn't have "+ name);
						e.printStackTrace();
						continue;
					}

					while(true) {
						
						String html = detailDoc.html();
						
						String reviewText = html;
						String[] reviewDataList = null;
						try {
							reviewText = reviewText.substring(reviewText.indexOf("class=\"location-review-card"),
									reviewText.lastIndexOf("공유"));
							//System.out.println(reviewText);
							reviewDataList = reviewText.split("공유");
						} catch (Exception e) {
							System.out.println(name + " doesn't have review.");
							e.printStackTrace();
							break;
						}
						
						for (int k = 0; k < reviewDataList.length; k++) {
							try {
								System.out.println(i + " - " + j);
								ReviewVO rvo = new ReviewVO();
								rvo.setTdNo(tdNo);
								System.out.println(cnt + " at " + name);
								String raw = reviewDataList[k];
								String id_name = getDataByEnd(raw, "/Profile/", "</a>이 리뷰를 작성했습니다.", 200);
								String[] temp = id_name.split("\">");
								String id = temp[0];
								System.out.println(id);
								rvo.setId(id);
								String userName = temp[1];
								System.out.println(userName);
								rvo.setName(userName);
								String score = getDataByStart(raw, "rating bubble_", "\"", 10);
								System.out.println(score);
								rvo.setScore(Integer.parseInt(score)/10);
								String regdate = getDataByStart(raw, "</a>이 리뷰를 작성했습니다.", "</span>", 30).trim();
								System.out.println(regdate);
								rvo.setRegdate(regdate);
								String title = getDataByEnd(raw, "<span>", "</span></span></a>", 200);
								System.out.println(title);
								rvo.setTitle(title);
								String content = getDataByEnd(raw, "<span>", "</span></q>", 2000);
								System.out.println(content);
								rvo.setContent(content);
								String exdate = getDataByStart(raw, "체험 날짜:</span>", "</span>", 30).trim();
								System.out.println(exdate);
								rvo.setExdate(exdate);
								System.out.println("================================");
								rvdao.reviewInsert(rvo);
								cnt++;
							} catch (Exception e) {
								System.out.println("getData():");
								e.printStackTrace();
							}
						}

						String nextReviewLink="";
						try {
							nextReviewLink = getDataByEnd(html, "href=\"", "\">다음</a>", 300);
						} catch (Exception e) {
							
							System.out.println("getData():");
							e.printStackTrace();
							//break;
							

							long end = System.currentTimeMillis();
							System.out.println("Crawling end in "+((end-start)/1000)+" seconds.)");
							return;
							
						}
						detailDoc = Jsoup.connect("https://www.tripadvisor.co.kr" + nextReviewLink).get();
						
					}
					
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		System.out.println("Crawling end in "+((end-start)/1000)+" seconds.)");
	}

	public static String getDataByEnd(String raw, String start, String end, int length) throws Exception{
		raw = raw.substring(raw.lastIndexOf(end)-length, raw.lastIndexOf(end));
		raw = raw.substring(raw.lastIndexOf(start)+start.length());
		
		return raw;
	}
	
	public static String getDataByStart(String raw, String start, String end, int length) throws Exception{
		raw = raw.substring(raw.indexOf(start)+start.length(), raw.indexOf(start)+start.length()+length);
		raw = raw.substring(0, raw.indexOf(end));
		
		return raw;
	}

}
