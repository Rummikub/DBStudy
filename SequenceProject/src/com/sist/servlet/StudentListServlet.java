package com.sist.servlet;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sist.student.dao.StudentDAO;
import com.sist.student.dao.StudentVO;

import java.util.*;

// 		/list.jsp?no=10  물음표 뒤(URI)는 읽지 않아(URL에 포함되지 않음)
@WebServlet("/list.do") // 사용자가 변경해 줄 수 있어 보통 do 씀
public class StudentListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out=response.getWriter();
		
		String strPage=request.getParameter("page");
		if(strPage==null)
			strPage="1";
		int curpage=Integer.parseInt(strPage);
		StudentDAO dao=new StudentDAO();
		ArrayList<StudentVO> list=dao.stdAllData(curpage);
		
		out.println("<html>");
		out.println("<body>");
		out.println("<center>");
		out.println("<h1>성적관리</h1>");
		out.println("<table border=1 width=500>");
		out.println("<tr>");
		out.println("<th>번호</th>");
		out.println("<th>이름</th>");
		out.println("<th>국어</th>");
		out.println("<th>영어</th>");
		out.println("<th>수학</th>");
		out.println("<th></th>");
		out.println("</tr>");
		
		//출력하는 위치 ( 데이터를 출력하려고 가져왔을 거잖니)
		
		int count=dao.stdRowCount();
		
		
		count=count-((curpage*10)-10);  // 순차적으로 처리
		
		int totalpage=(int)Math.ceil(count/10.0);  //페이징 + double -> int
		
		for(StudentVO vo:list)  // 사람 개수만큼 루프 돌림
		{
			out.println("<tr>");
			out.println("<td>"+(count--)+"</td>"); //URL에 보여주는 no는 하나씩 감소하면서 간다
			out.println("<td>"+vo.getName()+"</td>");
			out.println("<td>"+vo.getKor()+"</td>");
			out.println("<td>"+vo.getEng()+"</td>");
			out.println("<td>"+vo.getMath()+"</td>");
			out.println("<td><a href=delete.do?hakbun="+vo.getHakbun()+">삭제</a></td>");
			out.println("</tr>");
		}
			
		out.println("</table>");
		out.println("<table width=500>");
		out.println("<tr>");
		out.println("<td align=center>");
		out.println("<a href=list.do?page="+(curpage>1?curpage-1:curpage)+">이전</a>");
		out.println(curpage+" page / "+totalpage+" pages");
		out.println("<a href=list.do?page="+(curpage<totalpage?curpage+1:curpage)+">다음</a>");
		out.println("</td>");
		out.println("</table>");
		out.println("</center>");
		out.println("</body>");
		out.println("</html>");
	}

}