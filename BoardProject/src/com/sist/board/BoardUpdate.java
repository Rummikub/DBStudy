package com.sist.board;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.sist.dao.*;

@WebServlet("/BoardUpdate")
public class BoardUpdate extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out=response.getWriter();
	
		/*  ** 사용자 요청 번호 받기
		 * http://localhost/BoardProject/BoardDetailServlet?no=13
		 */
		//no값을 감춰주면서 보내줘야 함
		String no=request.getParameter("no");
		BoardDAO dao=new BoardDAO();
		BoardVO vo=dao.boardUpdateData(Integer.parseInt(no));
		
		out.println("<!DOCTYPE html>");    // 5.0HTML쓰려고 한다
		out.println("<html>");
		out.println("<head>");
		out.println("<link rel=stylesheet href=\"CSS/table.css\">");
		out.println("</head>");
		out.println("<body>");
		out.println("<center>");
		out.println("<h1>수정하기</h1>");
		
		out.println("<form method=post action=BoardUpdate>"); //데이터를 보내서 처리할 때 사용 to Board Insert 
		out.println("<table id=\"table_content\" width=500>");
		
		
		out.println("<tr>");
		out.println("<th width=15% align=right>이름</th>");		
		out.println("<td width=85%>");
		out.println("<input type=text name=name size=15 required value="+vo.getName()+">");
		//no값을 감춘 상태에서 보내주어야 한다
		out.println("<input type=hidden name=no value="+vo.getNo()+">");
		out.println("</td>");
		out.println("</tr>");
		
		out.println("<tr>");
		out.println("<th width=15% align=right>제목</th>");		
		out.println("<td width=85%>");
		out.println("<input type=text name=subject size=50 required value=\""+vo.getSubject()+"\">");
		// 제목의 경우 공백까지 포함한 데이터를 긁어와야 되기 때문에 \"+제목+\" => 큰따옴표를 붙여야 한다
		out.println("</td>");
		out.println("</tr>");
		
		out.println("<tr>");
		out.println("<th width=15% align=right valign=top>내용</th>");		
		out.println("<td width=85%>");
		out.println("<textarea rows=8 cols=55 name=content required>"+vo.getContent()+"</textarea>");
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
		out.println("<input type=submit value=수정하기>");
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
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out=response.getWriter();
		
		//사용자가 보내준 값을 저장
		try
		{
			request.setCharacterEncoding("UTF-8");
			
		}catch(Exception ex) {}
		//저장된 값=>DAO로 전송
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
