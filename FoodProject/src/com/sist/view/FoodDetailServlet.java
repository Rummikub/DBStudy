package com.sist.view;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import com.sist.dao.*;
import com.sist.manager.FoodHouseVO;
@WebServlet("/FoodDetailServlet")
public class FoodDetailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
		response.setContentType("text/html;charset=UTF-8");  //text/html;charset=ISO-8859 -> default
		PrintWriter out=response.getWriter(); // 브라우저가 읽어갈 위치 지정 (메모리)
		
		//1 요청값 받기
		String no=request.getParameter("no");
		//2 요청에 해당되는 DAO
		FoodDAO dao=FoodDAO.newInstance();
		FoodHouseVO vo=dao.foodDetailData(Integer.parseInt(no));
		String temp=vo.getAddress();
		
		temp=temp.substring(temp.lastIndexOf("구")+1,temp.lastIndexOf("동")+1);
		ArrayList<FoodHouseVO> list=dao.foodLocationData(temp);

	
		
		//3 HTML로 출력
		out.println("<html>");
		out.println("<head>");
		out.println("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css\">");
		out.println("<style type=text/css>");
		out.println(".row{");
		out.println("margin:0px auto;");
		out.println("width:1200px;");
		out.println("}");
		out.println("</style>");
		out.println("</head>");
		out.println("<body>");
		out.println("<div class=container-fluid>");  // 위에  width 지워야 적용됨
		out.println("<div class=row>");
		
		//div는 인라인 속성 ~ 12가 넘어가면 안됨 (12까지 출력되니까)
		out.println("<div class=col-md-8>");
		
		out.println("<table class=\"table\">");
		out.println("<tr>");
		StringTokenizer st=new StringTokenizer(vo.getImage(),"^");
		while(st.hasMoreTokens())  // 있는 개수 5 만큼 돌아감
		{
			out.println("<td>");
			out.println("<img src=\""+st.nextToken()+"\" width=100%>");
			out.println("</td>");
		}
		out.println("</tr>");
		out.println("</table>");
		
		out.println("<table class=\"table\">");
		
		out.println("<tr>");
		out.println("<td colspan=2>");
		out.println("<h2>"+vo.getTitle()+"&nbsp;<font color=orange>"+vo.getScore()+"</font></h2>");
		out.println("</td>");
		out.println("</tr>");
			
		out.println("<tr>");
		out.println("<td width=15% class=text-right>주소</td>");
		out.println("<td width=85% class=text-left>");
		out.println(vo.getAddress());
		out.println("</td>");
		out.println("</tr>");
		
		
		out.println("<tr>");
		out.println("<td width=15% class=text-right>전화번호</td>");
		out.println("<td width=85% class=text-left>");
		out.println(vo.getTel());
		out.println("</td>");
		out.println("</tr>");
		
		out.println("<tr>");
		out.println("<td width=15% class=text-right>음식종류</td>");
		out.println("<td width=85% class=text-left>");
		out.println(vo.getType());
		out.println("</td>");
		out.println("</tr>");
		
		out.println("<tr>");
		out.println("<td width=15% class=text-right>가격대</td>");
		out.println("<td width=85% class=text-left>");
		out.println(vo.getPrice());
		out.println("</td>");
		out.println("</tr>");
		
		out.println("<tr>");
		out.println("<td colspan=2 class=text-center>");
		out.println("맛있다("+vo.getGood()+") | ");
		out.println("괜찮다("+vo.getSoso()+") | ");
		out.println("별로("+vo.getBad()+")");
		out.println("</td>");
		out.println("</tr>");
		
		out.println("<tr>");
		out.println("<td colspan=2 class=text-right>");
		out.println("<a href=\"#\" class=\"btn btn-xs btn-danger\">찜</a>");
		out.println("<a href=\"#\" class=\"btn btn-xs btn-success\">예약</a>");
		out.println("<a href=\"javascript:history.back()\" class=\"btn btn-xs btn-primary\">목록</a>");
		out.println("</td>");
		out.println("</tr>");
		
		
		out.println("</table>");
		out.println("</div>");
		out.println("<div class=col-md-4>");
		
		
		out.println("<table class=\"table\">");
		//table 제목주는 태그 = caption
		out.println("<caption><h3>주변 인기 식당</caption>");
		
		out.println("<tr>");
		out.println("<td>");
		
		
		for(FoodHouseVO fvo:list)
		{
			out.println("<table class=\"table\">");
			out.println("<tr>");
			out.println("<td class=text-center width=30% rowspan=4>");
			out.println("<img src="+fvo.getImage().substring(0,fvo.getImage().indexOf("^"))+" width=100%>");
			out.println("</td>");
			
			out.println("<td width=70%>");
			out.println(fvo.getTitle()+"&nbsp;"+fvo.getScore());
			out.println("</td>");
			out.println("</tr>");
			
			out.println("<tr>");
			out.println("<td width=70%>");
			out.println("음식종류: "+fvo.getType());
			out.println("</td>");
			out.println("</tr>");

			out.println("<tr>");
			out.println("<td width=70%>");
			out.println("전화번호: "+fvo.getTel());
			out.println("</td>");
			out.println("</tr>");
			
			out.println("<tr>");
			out.println("<td width=70%>");
			out.println("가격대: "+fvo.getPrice());
			out.println("</td>");
			out.println("</tr>");
			
			out.println("</tr>");
			out.println("</table>");
		}
	
		
		
		out.println("</td>");
		out.println("</tr>");
		out.println("</table>");
		out.println("</div>");
		out.println("</div>");
		out.println("</div>");
		
		out.println("</body>");
		out.println("</html>");
		
	}

}
