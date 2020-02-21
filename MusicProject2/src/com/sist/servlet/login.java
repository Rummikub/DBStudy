package com.sist.servlet;

import java.io.*;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sist.dao.MusicDAO;

import sun.util.locale.StringTokenIterator;

@WebServlet("/login")
public class login extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out=response.getWriter();
		
		out.println("<html>");
		out.println("<head>");
		out.println("<link rel=stylesheet href=\"css/bootstrap.min.css\">");
		out.println("<style type=text/css>");
		out.println(".row {");
		out.println("margin:0px auto;"); // 가운데 정렬
		out.println("width:350px");
		out.println("}");
		out.println("h1 {");
		out.println("text-align:center");
		out.println("}");
		out.println("</style>");
		out.println("</head>"); 
		out.println("<body>");
		
		out.println("<div class=container>");    // container-fluid>"); 여백없이 꽉채움
		out.println("<h1>Login</h1>");
		out.println("<div class=row>");
		out.println("<table class=\"table table-hover\">");
		
		out.println("<form method=POST action=login>");  //login의 doPost를 호출하라는 메소드
		out.println("<tr>");
		out.println("<td width=20% class=text-right>ID</td>");
		out.println("<td width=80% class=text-left><input type=text name=id size=15></td>");
		out.println("</tr>");

		out.println("<tr>");
		out.println("<td width=20% class=text-right>Password</td>");
		out.println("<td width=80% class=text-left><input type=text name=pwd size=15></td>");
		out.println("</tr>");
		/*
		 * xs
		 * md
		 * sm -- 중간 사이즈 
		 * lg
		 */
		out.println("<tr>");
		out.println("<td colspan=2 class=text-center><input type=submit class=\"btn btn-md btn-primary\" value=login></td>");
		out.println("</tr>");

		out.println("</table>");
		out.println("</form>");
		out.println("</div>");  //.row를 닫음
		out.println("</div>");  // .container
		out.println("<body>");
		out.println("</html>");
	}

	// [콜백함수]main,doGet,doPost - 그동안은 서버를 우리가 만들었잖아, 근데 tomcat이 웹서버라서 우리가 구축할 필요 없이도 session,response,request를 알아서 호출해주는 고얏!우린 제어문만 넣어주면 됨!
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//로그인 처리
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out=response.getWriter();
		
		//로그인에 대한 결과값을 html에 보내줘야 함 => css,JavaScript
		MusicDAO dao=new MusicDAO();
		
		//사용자의 입력값을 받아와야 맞는지 틀리는지 알 수 있겠지
		String id=request.getParameter("id");   //위의 input태그의 name값을 써옴 (id/pwd)
		String pwd=request.getParameter("pwd");
		String result=dao.isLogin(id, pwd);
		
		if(result.equals("NOID"))
		{
			out.println("<script>");
			out.println("alert(\"ID가 존재하지 않는다\");");
			out.println("history.back();"); // 다시 로긴창으로 넘어가
			out.println("</script>");
			
			
		}else if(result.equals("NOPWD"))
		{
			out.println("<script>");
			out.println("alert(\"비밀번호가 틀리다\");");
			out.println("history.back();"); // 다시 로긴창으로 
			out.println("</script>");
			
		}else
		{	
			//*****사용자의 일부 정보를 Server안에 저장(한공간 static 개념)할때 사용하는 것이 Session (id는 창이 넘어가면서 지워지기 때문!) =>gc (1)30분 후 (2)로그아웃 (3)프로그램종료
			HttpSession session=request.getSession(); 
			//id,name을 세션에 저장해보자 (Map형식) == (key,값) 			
			session.setAttribute("id", id);
			StringTokenizer st=new StringTokenizer(result, "|");
			session.setAttribute("name", st.nextToken());
			session.setAttribute("sex", st.nextToken());
			
			//파일 이동
			response.sendRedirect("MusicList");
			
			
		}
	}

}
