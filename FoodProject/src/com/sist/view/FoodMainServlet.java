package com.sist.view;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.*;
import com.sist.dao.*;
import com.sist.manager.*;


@WebServlet("/FoodMainServlet")
public class FoodMainServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out=response.getWriter();
			
			FoodDAO dao=FoodDAO.newInstance();
			ArrayList<CategoryVO> list=dao.categoryAllData();
			
			
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
			
			out.println("<h1>믿고 보는 맛집 리스트</h1>");
			
			// 4X3
			
			int i=0;
			String color="";
			for(CategoryVO vo:list) 
			{
				i=i%3;
				if(i==0)
					color="panel panel-danger";
				else if(i==1)
					color="panel panel-primary";
				else if(i==2)
					color="panel panel-success";
				else if(i==3)
					color="panel panel-info";
				out.println("<div class=\"col-md-4\">");
				out.println("<div class=\"panel "+color+"\">");
				out.println("<div class=\"panel-heading\">"+vo.getTitle()+"><br>"+vo.getSubject()+"</div>");
				out.println("<div class=\"panel-body\">");
				
				//page넘기기 -> categorypage로 (서블릿명 주의!) ~ getParameter(cno)
				out.println("<a href=\"FoodListServlet?cno="+vo.getCateno()+"\">");
				out.println("<img src=\""+vo.getPoster()+"\" width=100% class=img-rounded>");
				out.println("</a>");
				out.println("</div>");
				out.println("</div>");
				out.println("</div>");
				
				i++;
			}
			out.println("</div>");
			out.println("</div>");
			out.println("</container>");
			out.println("</body>");
			out.println("</html>");
	}

}
