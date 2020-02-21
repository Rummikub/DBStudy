package com.sist.manager;

import java.text.SimpleDateFormat;
import java.util.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.sist.dao.MovieDAO;
import com.sist.vo.MovieVO;
import com.sist.vo.NewsVO;
/*
 *  <div class="aaa"> 
 *   		<p> aaa </p>    ====> div.aaa p  , 마크업명. 태그명
 *   </div>
 *   
 *   (Element = 내가 가져 오려는 tag명 ≒ VO)  합 = Elements ≒ ArrayList  같은 tag를 갖고 있다
 *   
   	<div class="wrap_movie">
			<div class="info_tit">
			====			========
														<em class="ico_movie ico_15rating">15세이상관람가</em>
													<a href="/moviedb/main?movieId=122091" class="name_movie #title">남산의 부장들</a>
 													===
 													div.info_tit em ==> 관람등급
 													div.wrap_movie span.info_state       ==> 예매율
 		try
 		{
			 		 for(;;;;)
			 		 {
						 		 try
						 		 {
						 		 
						 		 }catch   // 예외가 있더라도 정상 수행 할 수 있으려면 for 안에 try catch를 줘야한다
			 		 }
 		 }catch 
 		 											
 													
 */
public class MovieManager {
	
	//movie,news 에 동시에 쓰려고 멤버변수로 잡을래!
	MovieDAO dao=MovieDAO.newInstance();
	
	
	public ArrayList<MovieVO> movieListData()   
	
	{
	
		ArrayList<MovieVO> list=new ArrayList<MovieVO>();
		
		//*************싱글톤 객체 생성 (import해와야 함)
		
		try {
			
			//************************몇개 데이터 들어가는지 확인해보려고 
			 int k=1;
			//for(int i=1;i<=7;i++)
			{	
				
					Document doc=Jsoup.connect("https://movie.daum.net/boxoffice/yearly").get(); 		  // Ctrl+enter로 path잡아주기!
					Elements link=doc.select("div.info_tit a");		  // tag를 가져온다   ~ for 문 돌리려면 pl.
					for(int j=0;j<link.size();j++)
					{
						
						try
						{
						Element elem=link.get(j);  // link를 가져온다
						//      System.out.println(i+"-"+elem.attr("href"));        				 //    ~~    attribute = 링크  text= '남산의 부장들'
						String mLink="http://movie.daum.net"+elem.attr("href");
						Document doc2=Jsoup.connect(mLink).get();  						// 링크 타고 상세보기로 가기 위함
						
						//상세보기, 같은 tag없으므로 singular
					   //  System.out.println(doc2);
						
					  Element title=doc2.select("div.subject_movie strong.tit_movie").get(0);   
					  System.out.println(title.text());
					  
					  Element score=doc2.selectFirst("div.subject_movie em.emph_grade");// 1st 태그값을 받아오겠다 
					  System.out.println(score.text());
					 
					  Element genre=doc2.select("dl.list_movie dd.txt_main").get(0); // 같은 태그의 0번째를 받아옴
					  System.out.println(genre.text());
					  
					  Element regdate=doc2.select("dl.list_movie dd.txt_main").get(1); 
					  System.out.println(regdate.text());
					  
					  Element time=doc2.select("dl.list_movie  dd").get(3); // dd의 3번째 

					  
					  String temp=time.text();   // time 이랑 grade가 한줄에 출력되니까 ,로 잘라서 출력
					  StringTokenizer st=new StringTokenizer(temp,",");
					  String strTime=st.nextToken();
					  String strGrade=st.nextToken().trim(); // 공백 잘라버리기
					  
					  System.out.println(strTime);
					  System.out.println(strGrade);
					 
					  Element director=doc2.select("dl.list_movie dd.type_ellipsis").get(0); 
					  System.out.println(director.text());	
					  
					  Element actor=doc2.select("dl.list_movie dd.type_ellipsis").get(1); 
					  System.out.println(actor.text());			
					  
					 Element story=doc2.selectFirst("div.desc_movie p");
					 System.out.println(story.text());
					 
					 Element poster=doc2.selectFirst("a.area_poster img.img_summary");
					 System.out.println(poster.text());
					 
					 MovieVO vo=new MovieVO();
					 vo.setTitle(title.text());
					 vo.setScore(Double.parseDouble(score.text()));
					 vo.setGrade(strGrade);
					 vo.setTime(strTime);
					 vo.setActor(actor.text());
					 vo.setDirector(director.text());
					 vo.setGenre(genre.text());
					 vo.setRegdate(regdate.text());
					 vo.setStory(story.text());
					 vo.setPoster(poster.attr("src"));
					 
					 //현재상영
					 vo.setType(5);
					 
					 dao.movieInsert(vo);
					 
					 
					 System.out.println("k= " +k);
					 k++;
					 
					}catch (Exception ex) {}
						
				}
					
			}
			System.out.println("DataBase Insert End........"); // DB에 넣어주는 행위가 종료되었다
		}catch(Exception ex) {}
		
		return list; // 메소드니까 리턴형 必
	
	}
	//뉴스 모든 정보 읽어오기
	public ArrayList<NewsVO> newsAllData()
	{
		ArrayList<NewsVO> list=new ArrayList<NewsVO>();
		//매일 새로운 값을 가져오고 싶을 떄
		Date date=new Date();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		String today=sdf.format(date);
		try {

			
			for(int i=1;i<=18;i++)
			{
			//해당 페이지 소스 긁어오기 -> doc
			Document doc=Jsoup.connect("https://movie.daum.net/magazine/new?tab=nws&regdate="+today+"&page="+i).get();
			
			Elements title=doc.select("ul.list_line strong.tit_line a");
			
			Elements poster=doc.select("ul.list_line a.thumb_line span.thumb_img");

			Elements link=doc.select("ul.list_line strong.tit_line a");

			Elements temp=doc.select("span.cont_line span.state_line");

			Elements content=doc.select("span.cont_line a.desc_line");
			
				
				for(int j=0; j<title.size();j++)
				{
					
					System.out.println(title.get(j).text());
					
					String ss=poster.get(j).attr("style");
					ss=ss.substring(ss.indexOf("(")+1,ss.lastIndexOf(")"));
					System.out.println(ss);
					
					System.out.println(poster.get(j).attr("style"));
					System.out.println(link.get(j).attr("href"));
					String str=temp.get(j).text();
					String author=str.substring(0,str.indexOf("・"));
					String regdate=str.substring(str.lastIndexOf("자")+1);
					
					
					System.out.println(author);
					System.out.println(regdate);
		
					System.out.println(content.get(j).text());
					System.out.println("===============================================");
					
					//db에 넣자
					NewsVO vo=new NewsVO();
					vo.setTitle(title.get(j).text());
					vo.setLink(link.get(j).attr("href"));
					vo.setContent(content.get(j).text());
					vo.setPoster(ss);
					vo.setRegdate(regdate);
					vo.setAuthor(author);
					
									
					//오라클 전송
					dao.newsInsert(vo);
				}
			}//try
			System.out.println("Save End....");
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return list;
	}
		


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MovieManager m=new MovieManager();
		//m.movieListData();
		m.newsAllData();
	}

}