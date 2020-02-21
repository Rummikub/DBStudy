package com.sist.servlet;

import java.io.*;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.*;
import com.sist.dao.*;

@WebServlet("/MusicList")
public class MusicList extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	
			
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out=response.getWriter();
		String strPage = request.getParameter("page"); // 페이지 받기!! => 페이지에 변수가 1이 안 붙는다!!!
		if(strPage==null)  // 없다면 => 1 맨첫페이지는 NULL! 처리를 해줘야한다! 1번만 해주면 끝!!
			strPage="1";
		int curpage = Integer.parseInt(strPage); // 페이지가 없으니까 1페이지를 부여함!!
		/*
		 * 1. http://localhost/MusicProject2/MusicList ==> null값이다. => 따라서 처리를 따로 해줘야함!
		 * if(strPage==null)  // 없다면 => 1 맨첫페이지는 NULL! 처리를 해줘야한다! 
			strPage="1";
		 * 
		 * 
		 * 2. http://localhost/MusicProject2/MusicList?page=   ==> "" 공백들어온것
		 * 
		 */
		
		
		
		//DAO 연결
		MusicDAO dao = new MusicDAO();
		//받아오기 리스트로
		ArrayList<MusicVO> list=dao.musicListData(curpage); //2페이지를 줌
		
		int totalpage=dao.musicTotalPage();
		/*
		 * <div class="col-md-4">  ==> 4가 중요!   12가 되면 넘어감! 3을 주면 4칸되고 6칸을 만들고 싶으면 2를 넣고! 
		 * 
		 * 가로줄로 화면 분할 을 할때 8 , 3, 1 로 등분을 나누어 줌 12가 되면 밑으로 내려감
    			<div class="thumbnail">
      <a href="/w3images/lights.jpg">
        <img src="/w3images/lights.jpg" alt="Lights" style="width:100%">
        <div class="caption">
          <p>Lorem ipsum...</p>
        </div>
      </a>
    </div>
  </div>
		 * 
		 * 
		 */
		
		
		HttpSession session = request.getSession();
		out.println("<html>");
		out.println("<head>");
		out.println("<link rel=stylesheet href=\"css/bootstrap.min.css\">");
		out.println("<style type=text/css>");
		out.println(".row{");
		out.println("margin: 0px auto;");
		out.println("width: 800px;");
		out.println("}");
		out.println("h1{");
		out.println("text-align: center;");
		out.println("}");
		out.println("</style>");
		out.println("</head>");
		out.println("<body>");
		out.println("<h1>"+session.getAttribute("name")+"님 환영합니다</h1>"); // 키를 넣어줌!
		out.println("<div class=container>");
		out.println("<h1>Music Top200</h1>");
		out.println("<div class=row>");
		out.println("<table class=\"table table-hover\">");
		out.println("<tr class=danger>");
		out.println("<th width=10% class=text-center>순위</th>");
		out.println("<th width=10% class=text-center>등폭</th>");
		out.println("<th width=20% class=text-center></th>"); //포스터 영역
		out.println("<th width=35%>노래명</th>");
		out.println("<th width=25%>가수명</th>");
		out.println("</tr>");
		
		// 리스트 출력
		for(MusicVO vo:list){
			
			
			String temp="";
			
			
			if(vo.getState().equals("상승"))
			{
				temp="<font color=red>▲</font>&nbsp;"+vo.getIdcrement();
				
			}else if(vo.getState().equals("하강"))
			{
				temp="<font color=blue>▼</font>&nbsp;"+vo.getIdcrement();
				
			}else
			{
				temp="<font color=gray>-</font>";
				
			}
			
		
			// tr50개 생성됨
			out.println("<tr>");
			out.println("<td width=10% class=text-center>"+vo.getRank()+"</td>");
			out.println("<td width=10% class=text-center>"+temp+"</td>");
			out.println("<td width=20% class=text-center><img src="+vo.getPoster()+" width=35 height=35 class=img-circle></td>"); //포스터 영역  circle-라운드처리
			out.println("<td width=35%><a href=\"MusicDetail?mno="+vo.getMno()+"\">"+vo.getTitle()+"</a></td>");
			out.println("<td width=25%>"+vo.getSinger()+"</td>");
			out.println("</tr>");
			
			// 출력하는 위치 버튼 위치에서 페이지가 넘어가도록해야함!
		
		}

		out.println("</table>");
		out.println("<table class=\"table\">");  

		out.println("<tr>");
		out.println("<td class=text-center>");
		out.println("<a href=\"MusicList?page="+(curpage>1?curpage-1:curpage)+"\" class=\"btn btn-sm btn-success\">이전</a>");
		// 이전페이지 이기때문에 1보다 항상 커야한다 최소2!
		//페이지 넣기
		out.println(curpage+" page / "+totalpage+" pages");
		
		out.println("<a href=\"MusicList?page="+(curpage<totalpage?curpage+1:curpage)+"\" class=\"btn btn-sm btn-info\">다음</a>");
		//현재페이지가 total페이지보다 작으면 다음으로 갈수 있는 것!
		out.println("</td>");
		out.println("</tr>");
		
		out.println("</table>");
		out.println("</div>");
		out.println("</div>");
		out.println("</body>");
		out.println("</html>");
		
		
		

	}

	
	
}
