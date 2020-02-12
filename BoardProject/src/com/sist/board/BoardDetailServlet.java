package com.sist.board;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.sist.dao.*;

@WebServlet("/BoardDetailServlet")
public class BoardDetailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out=response.getWriter();
		//브라우저가 여기부터 읽음 (BoardListServlet의 변수를 넣어줘야..)
		String no=request.getParameter("no");
		// 오라클에서 데이터를 가져와야 함
		BoardDAO dao=new BoardDAO();
		BoardVO vo=dao.boardDetailData(Integer.parseInt(no)); 
		// HTML로 뿌려주면 (vo를)됨
		out.println("<html>");
		out.println("<head>");
		out.println("<link rel=stylesheet href=\"CSS/table.css\">");
		out.println("</head>");
		out.println("<body>");
		out.println("<center>");
		out.println("<h1>내용보기</h1>");
		
		out.println("<table id=\"table_content\" width=700>");
		out.println("<tr>");
		out.println("<th width=20%>번호</th>");
		out.println("<td width=30% align=center>"+vo.getNo()+"</td>");
		out.println("<th width=20%>작성일</th>");
		out.println("<td width=30% align=center>"+vo.getRegdate()+"</td>");		
		out.println("</tr>");
		
		out.println("<tr>");
		out.println("<th width=20%>이름</th>");
		out.println("<td width=30% align=center>"+vo.getName()+"</td>");
		out.println("<th width=20%>조회수</th>");
		out.println("<td width=30% align=center>"+vo.getHit()+"</td>");
		out.println("</tr>");
		
		out.println("<tr>");
		out.println("<th width=20%>제목</th>");
		out.println("<td colspan=3>"+vo.getSubject()+"</td>");
		out.println("</tr>");
		
		out.println("<tr>");
		out.println("<td colspan=4 align=left valign=top height=200>"+vo.getContent()+"</td>");
		out.println("</tr>");
		
		out.println("<tr>");
		out.println("<td colspan=4 align=right>");
		out.println("<a href=\"BoardUpdate?no="+vo.getNo()+"\">수정</a>&nbsp;");
		out.println("<a href=\"BoardDelete?no="+vo.getNo()+"\">삭제</a>&nbsp;");
		out.println("<a href=\"BoardListServelet\">목록</a>");
		out.println("</td>");
		out.println("</tr>");
		out.println("</table>");
	}

}
