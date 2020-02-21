package com.sist.view;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sist.dao.MovieDAO;
import com.sist.vo.MovieVO;


@WebServlet("/MonthlyServlet")
public class MonthlyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out=response.getWriter();
		
		//사용자가 보내준 값 (페이지)
		
		
		//DAO연결 ~ 현재상영 영화
		MovieDAO dao=MovieDAO.newInstance();
		ArrayList<MovieVO> list=dao.movieListData(1, 4);
		
		
		out.println("<html>");
		out.println("<head>");
		out.println("<style type=text/css>");
		//container은 이미 존재함
		out.println(".row{");
		out.println("margin:0px auto;"); //가운데 정렬 (center쓰지마라)
		out.println("width:1200px;");
		out.println("}");
		
		out.println("</style>");
		out.println("</head>");
		
		out.println("<body>");
		//row클래스 만들기
		out.println("<div class=row>");
		
		//이미지 출력
		
		for(MovieVO vo:list)
		{	
			//너무 긴 제목 자르기
			String title=vo.getTitle();
			String title2="";
			if(title.length()>20)
			{
				title2=title.substring(0,20)+"...";
			}else
			{
				title2=title;
			}
			// 4x3줄 출력 = 12개 
			out.println("<div class=\"col-md-3\">");
			out.println("<div class=\"thumbnail\">");
			out.println("<a href=\"#\">");
			out.println("<img src=\""+vo.getPoster()+"\" alt=\"Lights\" style=\"width:100%\">");
			out.println("<div class=\"caption\">");
			out.println("<p>"+title2+"</p>");
			out.println("</div>");
			out.println(" </a>");
			out.println("</div>");  
			out.println("</div>");  
	
		}
		/*
	<ul class="pagination pagination-lg">
  <li><a href="#">1</a></li>
  <li><a href="#">2</a></li>
  <li><a href="#">3</a></li>
  <li><a href="#">4</a></li>
  <li><a href="#">5</a></li>
</ul>

		 */
		out.println("</div>");
		// 2번째 row 출력

		
		
		out.println("</body>");
		
		out.println("</html>");

	}

}