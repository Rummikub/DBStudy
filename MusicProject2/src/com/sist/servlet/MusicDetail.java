package com.sist.servlet;
import java.io.*;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sist.dao.*;
// util은 필요x 리스트로 가져올것 x



/*
 * 웹 클래스 - 총 8개 
 * 1.자입력값 받기 => 리퀘스트
 * 2.화면 이동 응답  => 리스폰스
 * 3.값을 저장하고 싶다!=> 세션
 * 
 * 
 * 
 * 
 * 
 */
@WebServlet("/MusicDetail")
public class MusicDetail extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html;charset=UTF-8");
		//화면출력
		PrintWriter out=response.getWriter(); //브라우저가 읽는 위치 => 브라우저가 알아서 읽어갈 예정!
		
		// 보내준 값이 있으면 받고 dv연동해야함!
		// musicdetaildata(int no) ==> getMNO==>값을 받고 연동 시작
		
		String mno=request.getParameter("mno");
		
		//DAO 연동
				
		MusicDAO dao= new MusicDAO();
		MusicVO vo = dao.musicDetailData(Integer.parseInt(mno)); //
		
		ArrayList<MusicReplyVO> list=dao.replyListData(Integer.parseInt(mno));
		ArrayList<MusicVO> topList=dao.musicTop5();
		HttpSession session = request.getSession();
		out.println("<html>");
		out.println("<head>");
		out.println("<link rel=stylesheet href=\"css/bootstrap.min.css\">");
		out.println("<style type=text/css>");
		out.println(".col-md-9{");
		out.println("margin: 0px auto;");
		out.println("width: 900px;");
		out.println("}");
		out.println("h1{");
		out.println("text-align: center;");
		out.println("}");
		out.println("</style>");
		out.println("<script type=text/javascript src=\"http://code.jquery.com/jquery.js\"></script>");
		out.println("<script>");
		out.println("var i=0;");
		/*
		 * javascript의 변수 데이터형 = var
		 * var i=0; => i(int)
		 * var i=10.4=> i(double)
		 */
		out.println("$(function(){");  // window.onload()
		out.println("$('#ubtn').click(function(){");
		out.println("if(i==0){");
		out.println("$('#ubtn').val('취소');");
		
		out.println("i=1;");	
		out.println("}");
	
		out.println("else{");

		out.println("$('#ubtn').val('수정');");
		out.println("i=0;");
		out.println("}");
		out.println("})");
		out.println("});");
		out.println("</script>");
		out.println("</head>");
		
		out.println("<body>");

		out.println("<div class=container-fluid>");
		/*
		 * 
		 * <select>
		 * 
		 * WHERE no<10  => no&lt; 10 <  >    &nbsp;
		 * <select>
		 * 
		 * 
		 */
		
		
		
		
		out.println("<h1>&lt;"+vo.getTitle()+"&gt상세보기</h1>");
		out.println("<div class=col-md-9>");
		out.println("<table class=\"table table-bordered\">");
		
		
		out.println("<tr>");
		out.println("<td colspan=2 class=text-center>");
		out.println("<embed src=\"http://youtube.com/embed/"+vo.getKey()+"\" width=100% height=350>");
		out.println("</td>");
		out.println("</tr>");
		
		
		out.println("<tr>");
		out.println("<td width=10% class=text-right>");
		out.println("노래명");
		out.println("</td>");

		out.println("<td width=90% class=text-left>");
		out.println(vo.getTitle());
		out.println("</td>");
		out.println("</tr>");
		
		
		out.println("<tr>");
		out.println("<td width=10% class=text-right>");
		out.println("가수명");
		out.println("</td>");
		out.println("<td width=90% class=text-left>");
		out.println(vo.getSinger());
		out.println("</td>");
		out.println("</tr>");
		
		
		out.println("<tr>");
		out.println("<td width=10% class=text-right>");
		out.println("앨범명");
		out.println("</td>");
		out.println("<td width=90% class=text-left>");
		out.println(vo.getAlbum());
		out.println("</td>");
		out.println("</tr>");
		
		out.println("<tr>");
		out.println("<td colspan=2 class=text-right>");
		out.println("<a href=\"MusicList\" class=\"btn btn-lg btn-success\">목록</a>");
		out.println("</td>");
		out.println("</tr>");
		out.println("</table>");
		out.println("<div style=\"height=20px\"></div>");
		
		
		//댓글출력
		// 댓글입력
		HttpSession session1=request.getSession();// 세션얻어오기
		//request ; session cookie 갖고온다
		String id=(String)session1.getAttribute("id");
				
		if(list.size()<1)
		{
			out.println("<table class=\"table table-striped\">");
			out.println("<tr>");
			out.println("<td class=text-center>");
			out.println("<h3>댓글이 존재하지 않는다</h3>");
			out.println("</td>");
			out.println("</tr>");
			out.println("</table>");
		}
		else 
		{
			out.println("<table class=\"table table-hover\">");
			for(MusicReplyVO rvo:list)
			{
				out.println("<tr>");
				out.println("<td class=text-left>");
				String temp="";
				if(rvo.getSex().equals("남자"))
					temp="a1.jpg";
				else
					temp="a2.jpg";
				out.println("<img src=\"image/"+temp+"\" width=25 height=25>");
				out.println("&nbsp;"+rvo.getName()+"("+rvo.getDbDay()+")");
				out.println("</td>");
				out.println("<td class=text-right>");
				/*
				 * String id=null;
				 * if(id.equals("aaa"))
				 * 	   -- 주소가 없는데 값을 달라고 해서 오류남
				 */
				
				if(rvo.getId().equals(id))
				{
					out.println("<input type=button class=\"btn btn-xs btn-primary\" value=수정 id=ubtn data="+rvo.getName()+">");
					out.println("<a href=\"ReplyDelete?no="+rvo.getNo()+"&mno="+mno+"\" class=\"btn btn-xs btn-danger\">삭제</a>");
				}
				
				
				out.println("</td>");
				out.println("</tr>");
				
				out.println("<tr>");
				out.println("<td colspan=2><pre style=\"white-space:pre-wrap;border:none;background:white\">"+rvo.getMsg()+"</pre></td>");
				out.println("</tr>");
				
				
				//수정하기 위한 ta 
			
				out.println("<tr id=\"m"+rvo.getNo()+"\" style=\"display:none\">");
				out.println("<td colspan=2>");
				out.println("<textarea rows=3 cols=80 style=\"float:left\" required name=msg>"+rvo.getMsg()+"</textarea>");
				out.println("<input type=hidden value="+mno+" name=mno>");
				out.println("<input type=submit value=댓글수정 style=\"height=70px;float:left\" class=\"btn btn-primary\">");
				out.println("</td>");
				out.println("</tr>");

			}
			out.println("</table>");
		}
		
		//메뉴처리, 로그인
		if(id!=null) 
		{
			
			out.println("<form method=post action=\"ReplyInsert\">");	
			out.println("<table class=\"table table-striped\">");
			out.println("<tr>");
			out.println("<td>");
			out.println("<textarea rows=3 cols=80 style=\"float:left\" required name=msg></textarea>");
			// 세션에 어떤 값이 필요한지 알아내야 햄
			out.println("<input type=hidden value="+mno+" name=mno>");
			out.println("<input type=submit value=댓글쓰기 style=\"height=70px;float:left\" class=\"btn btn-primary\">");
			out.println("</td>");
			out.println("</tr>");
			out.println("</table>");
			out.println("</form>");
		
		}

		out.println("</div>");
		
		
		out.println("<div class=col-md-3>");
		out.println("<table class=\"table table-striped\">");
		out.println("<caption>인기순위5</caption> "); // 테이블 제목 줄 때 사용
		
		
		for(MusicVO tvo:topList)
		{
			out.println("<tr>");
			out.println("<td>"+tvo.getRank()+"</td>");
			out.println("<td>");
			out.println("<img src=\""+tvo.getPoster()+"\" width=35 heigth=35>");
			out.println("</td>");
			out.println("<td>"+tvo.getTitle()+"</td>");
			out.println("</tr>");
		}
		out.println("</table>");
		
		out.println("</div>");
		out.println("</div>");
		out.println("</body>");
		out.println("</html>");
		
		
		
		//연동후  값을 받아서 화면 출력!
		
		
		
		
		
	}

}
