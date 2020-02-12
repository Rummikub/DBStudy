package com.sist.board;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sist.dao.BoardDAO;
import com.sist.dao.*;


@WebServlet("/BoardInsert")
public class BoardInsert extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out=response.getWriter();
		out.println("<!DOCTYPE html>");    // 5.0HTML쓰려고 한다
		out.println("<html>");
		out.println("<head>");
		out.println("<link rel=stylesheet href=\"CSS/table.css\">");
		out.println("</head>");
		out.println("<body>");
		out.println("<center>");
		out.println("<h1>글쓰기</h1>");
		
		out.println("<form method=post action=BoardInsert>"); //데이터를 보내서 처리할 때 사용 to Board Insert 
		out.println("<table id=\"table_content\" width=500>");
		
		
		out.println("<tr>");
		out.println("<th width=15% align=right>이름</th>");		
		out.println("<td width=85%>");
		out.println("<input type=text name=name size=15 required>");
		out.println("</td>");
		out.println("</tr>");
		
		out.println("<tr>");
		out.println("<th width=15% align=right>제목</th>");		
		out.println("<td width=85%>");
		out.println("<input type=text name=subject size=50 required>");
		out.println("</td>");
		out.println("</tr>");
		
		out.println("<tr>");
		out.println("<th width=15% align=right valign=top>내용</th>");		
		out.println("<td width=85%>");
		out.println("<textarea rows=8 cols=55 name=content required></textarea>");
		out.println("</td>");
		out.println("</tr>");
		
		out.println("<tr>");
		out.println("<th width=15% align=right>비밀번호</th>");		
		out.println("<td width=85%>");
		out.println("<input type=password name=pwd size=10 required>");
		out.println("</td>");
		out.println("</tr>");
		
		out.println("<tr>");		
		out.println("<td colspan=2 align=center>");
		out.println("<input type=submit value=글쓰기>");
		out.println("<input type=button value=취소 onclick=\"javascript:history.back()\">");
		out.println("</td>");
		out.println("</tr>");
		// 제목~글쓰기 까지 NOT NULL을 지정했기 때문에,오라클에 값을 넣어주고 시작해야 하는 문제...
		out.println("</table>");
		out.println("</form>");			// table 전체를 보낼 것!
		out.println("</center>");
		out.println("</body>");
		out.println("</html>");
	
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 try
		 {
			 request.setCharacterEncoding("UTF-8"); //한글이 넘어올때는 꼭 변환
		 }catch(Exception ex) {}
		 
		 //1. 사용자가 보낸 데이터 받기
		 String name=request.getParameter("name");
		 String subject=request.getParameter("subject");
		 String content=request.getParameter("content");
		 String pwd=request.getParameter("pwd");
		 
		 System.out.println("name:"+name);
		 System.out.println("subject:"+subject);
		 System.out.println("content:"+content);
		 System.out.println("pwd:"+pwd);
		 
		 BoardVO vo=new BoardVO();
		 vo.setName(name);
		 vo.setSubject(subject);
		 vo.setContent(content);
		 vo.setPwd(pwd);
		 
		 BoardDAO dao=new BoardDAO();
		 dao.boardInsert(vo);

		 
		 //2. 오라클 연동 -> DAO 
		 //이동
		 response.sendRedirect("BoardListServlet");
	}

}
