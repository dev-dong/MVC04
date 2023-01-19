package kr.bit.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.bit.model.MemberDAO;
import kr.bit.model.MemberVO;

public class MemberListController implements Controller {
	
	@Override
	public String requestHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// POJO가 해야할 일의 범위
		// 1. Model 연동
		MemberDAO dao = new MemberDAO();
		List<MemberVO> list = dao.memberList();
		
		// member/memberList.jsp는 list를 가지고가야한다. -> jst가 list를 가지고 가기 위해서는 객체바인딩이 필요하다.
		// 2. 객체바인딩
		request.setAttribute("list", list);
		
		// FrontController한테 다음 페이지는 member/memberList.jsp 입니다. next Page 정보를 return 해준다. -> FrontController는 next Page 정보만 받아서 포워딩만 하면 된다.
		// 3. 다음페이지 정보(View) String으로 return
		return "memberList";
	}
}
