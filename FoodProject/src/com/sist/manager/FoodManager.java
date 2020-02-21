package com.sist.manager;
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.sist.dao.FoodDAO;
public class FoodManager {
/*
  <div class="top_list_slide">
      <ul class="list-toplist-slider" style="width: 531px;">
        <li>
          <img class="center-croping" alt="분짜 맛집 베스트 35곳 사진"
data-lazy="https://mp-seoul-image-production-s3.mangoplate.com/keyword_search/meta/pictures/ll_ikc83wddw3t9a.png?fit=around|600:400&amp;crop=600:400;*,*&amp;output-format=jpg&amp;output-quality=80"/>

           <a href="/top_lists/1649_bun_cha"
 onclick="trackEvent('CLICK_TOPLIST', {&quot;section_position&quot;:0,&quot;section_title&quot;:&quot;믿고 보는 맛집 리스트&quot;,&quot;position&quot;:0,&quot;link_key&quot;:&quot;P5AXQ5F&quot;});">
                <figure class="ls-item">
                  <figcaption class="info">
                    <div class="info_inner_wrap">
                      <span class="title">분짜 맛집 베스트 35곳</span>
                      <p class="desc">"갑분짜: 갑자기 분짜 먹고 싶다."</p>
                      <p class="hash">
                          <span>#분짜 </span>
                          <span>#분짜 맛집 </span>
                          <span>#분짜맛집 </span>
                          <span>#베트남 </span>
                          <span>#베트남요리 </span>
                          <span>#베트남 요리 </span>
                          <span>#베트남음식 </span>
                          <span>#베트남 음식 </span>
                      </p>
                    </div>
                  </figcaption>
                </figure>
              </a>
            </li> 
 */
	public ArrayList<CategoryVO> categoryAllData()
	{
		ArrayList<CategoryVO> list=new ArrayList<CategoryVO>();
		
		try
		{
			//1단계  : 메인 페이지 모든 html가져오기
			Document doc=Jsoup.connect("https://www.mangoplate.com/").get();
			Elements title=doc.select("div.info_inner_wrap span.title");  // 카테고리 타이틀
			Elements poster=doc.select("ul.list-toplist-slider img.center-croping");
			Elements subject=doc.select("div.info_inner_wrap p.desc");  // 부제목
			Elements link=doc.select("ul.list-toplist-slider li a");  // 2단계로 넘어가는 링크
			
			for(int i=0; i<12;i++)
			{
				System.out.println(title.get(i).text());
				System.out.println(subject.get(i).text());
				System.out.println(link.get(i).attr("href"));
				System.out.println(poster.get(i).attr("data-lazy"));
				
				CategoryVO vo=new CategoryVO();
				vo.setCateno(i+1);
				vo.setTitle(title.get(i).text());
				vo.setSubject(subject.get(i).text());
				vo.setLink("https://www.mangoplate.com"+link.get(i).attr("href"));  // '/'에는 서버주소가 생략된 것
				
				String temp=poster.get(i).attr("data-lazy");
				temp=temp.replace("&", "@");
				vo.setPoster(temp);
				
				list.add(vo);
			}
			
		}catch(Exception ex) 
		{
			ex.printStackTrace();
		}
		
		return list;
	}
	
	public ArrayList<FoodHouseVO> foodAllData()
	{
		ArrayList<FoodHouseVO> list=new ArrayList<FoodHouseVO>();
		int k=1;
		
		// 30번 ,75번 건너뛰어라
		try {
			
			ArrayList<CategoryVO> cList=categoryAllData();
			
			//2단계 : 카테고리 (12바퀴)
			for(CategoryVO cvo:cList)
			{
				Document doc=Jsoup.connect(cvo.getLink()).get();
				Elements link=doc.select("div.info span.title a");
				
				//카테고리 이름 확인용
				System.out.println(cvo.getTitle());
				for(int j=0;j<link.size();j++)
				{  
					try{
					//3단계 : 상세보기 (10바퀴)
					String url="https://www.mangoplate.com"+link.get(j).attr("href");
					Document doc2=Jsoup.connect(url).get();
					//plural ( 한페이지에 5장의 이미지가 있음 )
					Elements poster=doc2.select("figure.list-photo img.center-croping");
					
					// 이미지를 한번에 묶어서 저장하고 ^ 로 다섯개의 이미지 구분
					String img="";
					for(int a=0;a<poster.size();a++)
					{
						System.out.println(poster.get(a).attr("src"));
						img+=poster.get(a).attr("src")+"^";
					}
					
					// 이미지 한개씩 잘라오기! -> String Tokenizer로 잘라오면 되지 않겠니!
					img=img.substring(0,img.lastIndexOf("^"));  
					
					
					System.out.println("********************************************");
					//singular
					Element title=doc2.selectFirst("span.title h1.restaurant_name");
					Element score=doc2.selectFirst("span.title strong.rate-point span");
					Element address=doc2.select("table.info tr td").get(0);
					Element tel=doc2.select("table.info tr td").get(1);
					Element type=doc2.select("table.info tr td").get(2);
					Element price=doc2.select("table.info tr td").get(3);
					
					//평가 출력
					Element temp=doc2.selectFirst("script#reviewCountInfo");
					
					System.out.println("k= "+k);
					System.out.println(title.text());
					System.out.println(score.text());
					System.out.println(address.text());
					System.out.println(tel.text());
					System.out.println(type.text());
					System.out.println(price.text());
					System.out.println(temp.data()); // 자바스크립트는 텍스트 아니고 데이터로 갖고와야 함
					System.out.println("================================================");
					//Element image; (위에있음)
					
					FoodHouseVO vo=new FoodHouseVO();
					//카테고리 번호를 여기다 넣어주는 것!
					vo.setCno(cvo.getCateno());
					//no는 DB에서 시퀀스 줬음!!!! ************************************************
					vo.setTitle(title.text());
					vo.setScore(Double.parseDouble(score.text()));
					vo.setAddress(address.text());
					vo.setTel(tel.text());
					vo.setType(type.text());
					vo.setPrice(price.text());
					
					//JSON parser이용하기
					vo.setImage(img);
					JSONParser parser=new JSONParser();
					JSONArray arr=(JSONArray)parser.parse(temp.data());
					
		//<script id="reviewCountInfo" type="application/json">[{"action_value":1,"count":0},{"action_value":2,"count":9},{"action_value":3,"count":30}]</script>
					//JSON이 어떤값을 받아가는지 궁금해  [] 를 받았음! 
					
					/*
					 [] = arr
					 {} = object
					 */
					System.out.println(""+arr.toJSONString());
					// 한번에 한 데이터만 저장할 수 있는데 지금 변수 3개나 있어!************* => 배열로 묶어줘야 함
					long[] data=new long[3];
					// 3바퀴 돎
					for(int b=0; b<arr.size(); b++)
					{
						// {}하나를 가져와 -JSONObject
						JSONObject obj=(JSONObject)arr.get(b);
						System.out.println("Object=> "+obj.toJSONString());
						
						//Json에는 int형이 없어서 long으로 받아야 된대
						long count=(long)obj.get("count");
						data[b]=count;
						//속성 ; "count" 값:n 
						System.out.println("count= "+count);
						
					}
					vo.setBad((int)data[0]);
					vo.setSoso((int)data[1]);
					vo.setGood((int)data[2]);
					
					list.add(vo);
					k++;
					}catch(Exception ex){}
			    }
			}
		} catch (Exception ex) {ex.printStackTrace();}
		return list;
	}
	public static void main(String[] args) {
		FoodManager fm=new FoodManager();
		ArrayList<FoodHouseVO> list=fm.foodAllData();
		FoodDAO dao=FoodDAO.newInstance();
		int k=1;
		for(FoodHouseVO vo:list)
		{
			dao.foodHouseInsert(vo);
			System.out.println("k="+k);
			try
			{
				Thread.sleep(100);
			}catch(Exception e) {}
			k++;
		}
		System.out.println("Save ENd..");
	/*	ArrayList<CategoryVO> list=fm.categoryAllData();
		FoodDAO dao=FoodDAO.newInstance();
		int k=1;
		for(CategoryVO vo:list)
		{
			dao.categoryInsert(vo);
			System.out.println("k= "+k);
			k++;
		}
		System.out.println("Save End...");
	}*/
  }
}
