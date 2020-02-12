package com.sist.board;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sist.dao.BoardDAO;
import com.sist.dao.BoardVO;


@WebServlet("/BoardFind")
public class BoardFind extends HttpServlet {
	private static final long serialVersionUID = 1L;


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out=response.getWriter();
		
		//사용자가 보내준 값을 저장
		try
		{
			request.setCharacterEncoding("UTF-8");
			
		}catch(Exception ex) {}
		//저장된 값=>DAO로 전송  
		//fs, ss
		String no=request.getParameter("no");
		String name=request.getParameter("name");
		String subject=request.getParameter("subject");
		String content=request.getParameter("content");
		String pwd=request.getParameter("pwd");
		
		BoardVO vo=new BoardVO();
		vo.setNo(Integer.parseInt(no));
		vo.setName(name);
		vo.setSubject(subject);
		vo.setContent(content);
		vo.setPwd(pwd);
		
		//처리 =DAO
		BoardDAO dao=new BoardDAO();
		boolean bCheck=dao.boardUpdate(vo);
		//상세보기로 이동
		if(bCheck==true)
		{
			response.sendRedirect("BoardDetailServlet?no="+no);
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
