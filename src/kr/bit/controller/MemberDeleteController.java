package kr.bit.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.bit.model.MemberDAO;

public class MemberDeleteController implements Controller {
	@Override
	public String requestHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// Context Path 가져오기
		String ctx = request.getContextPath(); // MVC04
		int num = Integer.parseInt(request.getParameter("num"));
		
		// 2. 회원 삭제
		MemberDAO dao = new MemberDAO();
		int cnt = dao.memberDelete(num);
		String nextPage = null;
		if (cnt > 0) {
			nextPage = "redirect:"+ ctx + "/memberList.do";
		} else {
			throw new ServletException("could not be deleted");
		}
		return nextPage;
	}
}
