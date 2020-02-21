package com.sist.view;

import java.io.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/MainServlet")
public class MainServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public MainServlet() {
   
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out=response.getWriter();
		
		// 변경할 서블릿명 얻어오기
		String mode=request.getParameter("mode");
		if(mode==null)
			mode="1"; // 즉, 사용자가 클릭을 안했으면 기본값은 현재상영 사이트다
		int n=Integer.parseInt(mode);
		String sname="";
		switch(n)
		{
			case 1:
				sname="ReleasedServlet";
				break;
			case 2:
				sname="ScheduledServlet";
				break;
			case 3:
				sname="NewsServlet";
				break;
			case 4:
				sname="WeeklyServlet";
				break;
			case 5:
				sname="MonthlyServlet";
				break;
			case 6:
				sname="YearlyServlet";
				break;
			case 7:
				sname="MovieDetailServlet";
				break;
		}
		out.println("<html>");
		out.println("<head>");
		
		//menu 만드는 소스 w3school에서  try it yourself 긁어옴
		out.println("  <link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css\">");
		
		//메뉴를 출력해주려면 javascript가 필요해서 같은 경로로 긁어옴
		out.println("  <script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js\"></script>");
		out.println(" <script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js\"></script>");

		out.println("</head>");
		out.println("<body>");

		out.println("<nav class=\"navbar navbar-inverse\">");
		out.println("<div class=\"container-fluid\">");
		out.println(" <div class=\"navbar-header\">");
		out.println("<a class=\"navbar-brand\" href=\"MainServlet\">SIST MC</a>");
		out.println("</div>");
		out.println("<ul class=\"nav navbar-nav\">");
		out.println("<li class=\"active\"><a href=\"MainServlet\">현재상영</a></li>");
		out.println("<li class=\"dropdown\"><a class=\"dropdown-toggle\" data-toggle=\"dropdown\" href=\"#\"> BoxOffice <span class=\"caret\"></span></a>");
		out.println("<ul class=\"dropdown-menu\">");
		out.println("<li><a href=\"MainServlet?mode=4\">주간</a></li>");
		out.println("<li><a href=\"MainServlet?mode=5\">월간</a></li>");
		out.println("<li><a href=\"MainServlet?mode=6\">연간</a></li>");
		out.println("</ul>");
		out.println("</li>");
		out.println("<li><a href=\"MainServlet?mode=2\">개봉예정</a></li>");
		out.println("<li><a href=\"MainServlet?mode=3\">뉴스</a></li>");
		out.println("</ul>");
		out.println("</div>");
		out.println("</nav>");
		
	
		out.println("<div class=\"container\">");
		RequestDispatcher rd=request.getRequestDispatcher(sname); // 서블릿 명이 들어와야 함 따옴표 주의
		rd.include(request, response);
		
		
		/*
		 void include(HttpServletRequest request, HttpServletResponse response)
		 {
		 	ReleasedServlet.doGet(req,res)
		 }
		 */
		out.println("</div>");
		
		
		out.println("</body>");
		out.println("</html>");
		out.println("");
		out.println("");
		out.println("");
		out.println("");
		out.println("");
		
	}

}
