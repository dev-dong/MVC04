package kr.bit.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.bit.model.MemberDAO;
import kr.bit.model.MemberVO;

public class MemberContentController implements Controller {
	@Override
	public String requestHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int num = Integer.parseInt(request.getParameter("num"));
		MemberDAO dao = new MemberDAO();
		MemberVO vo =dao.memberContent(num);
		
		// 객체바인딩
		request.setAttribute("vo", vo);
		
		// 이 url이 웹상에 나타나지 않는다 why? 포워드 기법이기 때문이다. 경로가 servlet에 멈춰있고 내부적으로 요청과 응답이 되기 때문에
		//return "/WEB-INF/member/memberContent.jsp"; // 만약 폴더가 바뀌면 이런부분은 다 찾아서 수정해야한다.
		return "memberContent";
	}
}
