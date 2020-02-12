package com.sist.board;
/*
 * 	class A
 * {
 * 		void a(){삼각형}
 * 		void b(){사각형}
 * }
 *  A aa=new A();
 *  if(method==get)
 *  	aa.doGet();
 *  else if(method==post)
 *  	aa.doPost();
 */
import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sist.dao.BoardDAO;
import com.sist.dao.BoardVO;

@WebServlet("/BoardDelete")
public class BoardDelete extends HttpServlet {
	private static final long serialVersionUID = 1L;

	//http://localhost/BoardProject/BoardDelete?no=12
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out=response.getWriter();
		//어떤 게시물을 삭제할 것인지 지정
		String no=request.getParameter("no");
		
		out.println("<!DOCTYPE html>");    // 5.0HTML쓰려고 한다
		out.println("<html>");
		out.println("<head>");
		out.println("<link rel=stylesheet href=\"CSS/table.css\">");
		out.println("</head>");
		out.println("<body>");
		out.println("<center>");
		out.println("<h1>삭제하기</h1>");
		
		out.println("<form method=post action=BoardDelete>"); //데이터를 보내서 처리할 때 사용 to Board Insert 
		out.println("<table id=\"table_content\" width=300>");
		
		
		out.println("<tr>");
		out.println("<th width=35% align=right>비밀번호</th>");		
		out.println("<td width=65%>");
		out.println("<input type=password name=pwd size=15 required>");
		out.println("<input type=hidden name=no value="+no+">");
		out.println("</td>");
		out.println("</tr>");
		/*
		 * 	submit => input, select ,textarea 만 넘어감 (입력값으로 받은 애들만 지울 수 있다)
		 */
		out.println("<tr>");		
		out.println("<td colspan=2 align=center>");
		out.println("<input type=submit value=삭제하기>");
		out.println("<input type=button value=취소 onclick=\"javascript:history.back()\">");
		out.println("</td>");
		out.println("</tr>");
		
		out.println("</table>");
		out.println("</center>");
		out.println("</body>");
		out.println("</html>");
		
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out=response.getWriter();
		
		//저장된 값=>DAO로 전송
		String no=request.getParameter("no");
		String pwd=request.getParameter("pwd");

		
		//처리 =DAO
		BoardDAO dao=new BoardDAO();
		boolean bCheck=dao.BoardDelete(Integer.parseInt(no),pwd);
		//상세보기로 이동
		if(bCheck==true)
		{
			response.sendRedirect("BoardListServlet");
		}else
		{
			out.println("<html>");
			out.println("<head>");
			out.println("<script type=\"text/javascript\">");
			out.println("alert(\"비밀번호가 틀리다\");");
			out.println("history.back();");  // 바로 전 창으로 이동
			out.println("</script>");
			out.println("</head>");
			out.println("</html>");
		}

	}


}
