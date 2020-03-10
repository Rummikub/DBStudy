package com.sist.food;

import java.io.*;
import java.util.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
		
		
		public class FoodParser {

			FoodVO vo=new FoodVO();
			public static void main(String[] args) {
				long start = System.currentTimeMillis();
				
				int maxPage = 3685;
				int cnt=0;
				//FoodDAO dao=new FoodDAO();
				
				for(int page=0; page<maxPage; page++) {
					
					
					try {
						Document pageDoc = Jsoup.connect("https://www.tripadvisor.co.kr/Restaurants-g294197-oa"+30*page+"-Seoul.html").get();
						
						Element detailList = pageDoc.getElementsByAttributeValueStarting("class", "restaurants-list-List__wrapper").first();
						Elements detailItems = detailList.getElementsByAttribute("data-test");
						for(int nth=0; nth<detailItems.size(); nth++) {
							Document detailDoc = Jsoup.connect("https://www.tripadvisor.co.kr"+detailItems.get(nth).selectFirst("a").attr("href")).get();
							
							
							
							// name - 광고하는 음식점은 계속 겹치기 때문에 이미 데이터 수집한 곳이면 다음으로 넘어가야 함.
							String name;
							try {
								name = detailDoc.selectFirst("h1.ui_header").text();
								System.out.println((page+1) + " page " + (nth+1) + " th Restaurant : " + name + "\t================================");
							} catch (Exception e) {
								System.out.println((page+1) + " page " + (nth+1) + " th Restaurant doesn't have name.");
								continue;
							}
							
							// tags
							try {
								String tags = detailDoc.selectFirst("div.header_links").text();
								System.out.println(tags);
							} catch (Exception e) {
								System.out.println(name + " doesn't have any tags.");
							}
							
							// address
							try {
								String locality = detailDoc.selectFirst("span.locality").text();
								String street = detailDoc.selectFirst("span.street-address").text();
								String extended = detailDoc.selectFirst("span.extended-address").text();
								String post = detailDoc.selectFirst("span.postal-code").text();
								System.out.println(locality+" "+street+" "+extended+" "+post);
							} catch (Exception e) {
								System.out.println(name + " doesn't have address.");
							}
							
							// tel
							try {
								String tel = detailDoc.selectFirst("div.phone").select("span").last().text().replace("+82 ", "0");
								System.out.println(tel);
							} catch (Exception e) {
								System.out.println(name + " doesn't have telephone number.");
							}					
							
							// openhours
							try {
								String openHours = detailDoc.getElementsByAttributeValueStarting("class", "public-location-hours-LocationHours__hoursOpenerText").select("span").get(2).text();
								if(!openHours.contains("-")) throw new Exception();
								System.out.println(openHours);
							} catch (Exception e) {
								System.out.println(name + " doesn't have open hours.");
							}
							
							// price
							try {
								Elements details = detailDoc.getElementsByAttributeValueContaining("class", "detailsSummary").first().select("div");
								String info = details.get(1).text();
								if(info.contains("가격대")) {
									String price = info.substring(4);
									System.out.println(price);
								} else throw new Exception();
							} catch (Exception e) {
								System.out.println(name + " doesn't have price info.");
							}
							
							// lat,lng
							try {
								String lat_lng = detailDoc.html().substring(detailDoc.html().indexOf(",\"location\":{")+15);
								double lat = Double.parseDouble(getDataByStart(lat_lng, "\":", ",", 20));
								double lng = Double.parseDouble(getDataByStart(lat_lng, "longitude\":", ",", 20));
								System.out.println(lat + " / " + lng);
							} catch (Exception e) {
								System.out.println((page+1) + " page " + (nth+1) + " th Restaurant doesn't have lat, lng.");
								continue;
							} finally {
								System.out.println("================================================================");
								if(true) continue;
							}
							
							// review
							int rvcnt = 0;
							while(true) {
								try {
									String reviewDoc = detailDoc.selectFirst("#REVIEWS").html();
									String[] reviews = reviewDoc.split("class=\"info_text");
									for(int i=1; i<reviews.length; i++) {
										rvcnt++;
										String review = reviews[i];
										System.out.println((page+1) + " page " + (nth+1) + " th Restaurant : " + name + "'s " + rvcnt + " th review.\t================");
										
										// id
										String id = review.substring(review.indexOf("<div>")+5, review.indexOf("</div>")).trim();
										System.out.println(id);
										
										// bubble
										int bubble = Integer.parseInt(getDataByStart(review, " bubble_", "\"", 5))/10;
										System.out.println(bubble);
										
										// regdate
										String regdate = getDataByStart(review, "ratingDate\" title=\"", "\"", 20);
										System.out.println(regdate);
										
										// title
										String title = getDataByEnd(review, ">", "</span></a>", 500);
										System.out.println(title);
										
										// content
										String content = getDataByStart(review, "<div class=\"entry\">", "</div>", 3000).trim();
										String[] temp = content.split("<");
										content = "";
										for(int j=1; j<temp.length; j++) {
											content += temp[j].substring(temp[j].indexOf(">")+1);
										}
										content = content.replace("...", " ");
										content = content.replace("더 보기", "");
										System.out.println(content);
										
										// expdate
										try {
											String expdate = getDataByStart(review, "방문 날짜:</span> ", "</div>", 500).trim();
											System.out.println(expdate);
										} catch (Exception e) {
											System.out.println("This review doesn't have expdate.");
										}
										System.out.println("================================================================");
									} // current page review for end
									// next review page
									String nextReviewLink = getDataByEnd(detailDoc.html(), "href=\"", "\">다음</a>", 200);
									detailDoc = Jsoup.connect("https://www.tripadvisor.co.kr" + nextReviewLink).get();
								} catch (Exception e) {
									System.out.println((page+1) + " page " + (nth+1) + " th Restaurant : " + name + " doesn't have any reviews.");
									break;
								}
							} // review end
							System.out.println("================================================================");
						} // detailItems for end
						
						cnt++;
	
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					} // get pageDoc try catch
				} // page for end
				long end = System.currentTimeMillis();
				System.out.println("Crawling end in " + ((end - start) / 1000) + " seconds.");
			} // main end
			
			public static String getDataByEnd(String raw, String start, String end, int length) throws Exception {
				String trim = "";
				for(int i=0; i<length; i++){
					trim += " ";
				}
				raw = trim + raw + trim;
				raw = raw.substring(raw.lastIndexOf(end) - length, raw.lastIndexOf(end));
				raw = raw.substring(raw.lastIndexOf(start) + start.length());

				return raw;
			}

			public static String getDataByStart(String raw, String start, String end, int length) throws Exception {
				if(raw.indexOf(start)<0) throw new Exception();
				String trim = "";
				for(int i=0; i<length; i++){
					trim += " ";
				}
				raw = trim + raw + trim;
				raw = raw.substring(raw.indexOf(start) + start.length(), raw.indexOf(start) + start.length() + length);
				raw = raw.substring(0, raw.indexOf(end));

				return raw;
			}

		}