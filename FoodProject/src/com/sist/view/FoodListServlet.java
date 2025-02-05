package com.sist.view;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import com.sist.dao.*;
import com.sist.manager.CategoryVO;
import com.sist.manager.FoodHouseVO;

@WebServlet("/FoodListServlet")
public class FoodListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		//***********************************************************************
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out=response.getWriter();
		
		String cno=request.getParameter("cno");
		
		FoodDAO dao=FoodDAO.newInstance();
		
		//추가해야 할 데이터가 생기면 다시 db와 연결하면 됨 ! select는 메소드를 호출해야 한다
		ArrayList<FoodHouseVO> list=dao.fooHouseListData(Integer.parseInt(cno));
		//제목출력
		CategoryVO vo=dao.categoryInfoData(Integer.parseInt(cno));
		
		
		
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
		//***********************여기까지 동일 ***dao제외******************************
		
		out.println("<h1 class=text-center>"+vo.getTitle()+"</h1>");
		out.println("<h3 class=text-center style=\"color:gray\">"+vo.getSubject()+"</h3>");
		
		out.println("<table class=\"table table-striped\">");
		out.println("<tr>");
		out.println("<td>");
		
		
		int i=1;
		for(FoodHouseVO fvo:list)
		{
			out.println("<table class=\"table\">");
			out.println("<tr>");
			out.println("<td width=30% rowspan=2 class=text-center>");
			//3단계 : 상세페이지로 넘어감
			out.println("<a href=\"FoodDetailServlet?no="+fvo.getNo()+"\">");
			out.println("<img src="+fvo.getImage()+" width=180 height=180 class=img-rounded>");
			out.println("</a>");
			out.println("</td>");

			out.println("<td width=70%>");
			out.println("<h2>"+i+".&nbsp;<a href=\"FoodDetailServlet?no="+fvo.getNo()+"\">"+fvo.getTitle()+"&nbsp;&nbsp;<font color=orange>"+fvo.getScore()+"</font></h2>");
			out.println("</a>");
			out.println("</td>");
			out.println("<tr>");
			out.println("<td width=70%>"+fvo.getAddress().substring(0,fvo.getAddress().indexOf("지"))+"</td>");
			out.println("</tr>");
			
			out.println("</tr>");
			out.println("</table>");
			
			i++;
		}
		out.println("</td>");
		out.println("</tr>");
		out.println("</table>");
		
		out.println("</div>");
		out.println("</div>");
		out.println("</body>");
		out.println("</html>");

		


		
	
	}

}
