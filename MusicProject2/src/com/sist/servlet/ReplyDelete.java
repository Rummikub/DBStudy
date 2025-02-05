package com.sist.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.sist.dao.*;


@WebServlet("/ReplyDelete")
public class ReplyDelete extends HttpServlet {
	private static final long serialVersionUID = 1L;


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		//사용자 요청 데이터 받기
		String no=request.getParameter("no");
		String mno=request.getParameter("mno");
		
		//DAO연결
		MusicDAO dao=new MusicDAO();
		dao.replyDelete(Integer.parseInt(no));
		//MusicDetail로 이동
		response.sendRedirect("MusicDetail?mno="+mno);
	}

}
