package com.sist.servlet;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sist.dao.*;


@WebServlet("/ReplyInsert")
public class ReplyInsert extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try
		{
			request.setCharacterEncoding("UTF-8");
		}catch(Exception ex){}
		
		// 사용자가 보낸 데이터 받기
		String mno=request.getParameter("mno");
		String msg=request.getParameter("msg");
		
		// id,name 
		HttpSession session=request.getSession();
		String id=(String)session.getAttribute("id");  // 로그인인 상태만 댓글을 쓸 수 있기 때문에 null생각 안해도 됨 retrurn Object는 무조건 형변환
		String name=(String)session.getAttribute("name"); 
		// DAO로 전송
		// 사용자가 보내준 값 = ?
		MusicReplyVO vo=new MusicReplyVO();
		vo.setMno(Integer.parseInt(mno));
		vo.setMsg(msg);
		vo.setId(id);
		vo.setName(name);
		
		MusicDAO dao=new MusicDAO();
		
		//둘을 연결해서 vo를 넣어줌
		dao.replyInsert(vo);
		
		// 이동 => MusicDetail로 이동
		response.sendRedirect("MusicDetail?mno="+mno);
	}

}
