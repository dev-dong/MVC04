package kr.bit.frontcontroller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.bit.model.MemberDAO;
import kr.bit.model.MemberVO;

/**
 * Servlet implementation class MemberFrontController
 */
@WebServlet("*.do")
public class MemberFrontController extends HttpServlet {
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8"); // 회원정보가 parameter가 form에서 넘어오기 때문에 한글 데이터가 깨질 수 있다. 이를 방지
		
		// 클라이언트가 어떤 요청을 했는지 파악하기
		String url = request.getRequestURI(); // 클라이언트가 요청한 URL를 얻어온다.
		// /MVCO04가 의미하는 내용은 Context Path라고 부른다.
//		System.out.println(url);
		
		// Context Path 가지고오기
		String ctx = request.getContextPath();
		
		// 실제로 클라이언트가 요청한 명령이 무엇인지 파악
		String command = url.substring(ctx.length());
		System.out.println(command); // /memberInsert.do
		
		// 요청에 따른 분기작업 -> 복잡하다.(효율적이지 못하다)
		// 1개로 통합해서 좋지만 혼자서 작업을 처리하기엔 벅차다
		if (command.equals("/memberList.do")) { // 회원리스트보기
			MemberDAO dao = new MemberDAO();
			List<MemberVO> list = dao.memberList();
			request.setAttribute("list", list); // 객체바인딩
			// 포워딩
			RequestDispatcher rd = request.getRequestDispatcher("member/memberList.jsp");
			rd.forward(request, response);
		} else if (command.equals("/memberInsert.do")) { // 회원가입
			String id = request.getParameter("id");
			String pass = request.getParameter("pass");
			String name = request.getParameter("name");
			int age = Integer.parseInt(request.getParameter("age"));
			String email = request.getParameter("email");
			String phone = request.getParameter("phone");
			
			MemberVO vo = new MemberVO();
			vo.setId(id);
			vo.setPass(pass);
			vo.setName(name);
			vo.setAge(age);
			vo.setEmail(email);
			vo.setPhone(phone);
			
			MemberDAO dao = new MemberDAO();
			int cnt = dao.memberInsert(vo);
			if (cnt > 0) {
				// 가입성공
				response.sendRedirect("/MVC04/memberList.do");
			} else {
				// 가입실패 -> 예외객체를 만들어서 WAS에게 throw
				throw new ServletException("not Insert");
			}
		} else if (command.equals("/memberRegister.do")) { // 회원가입화면
			// 포워딩
			RequestDispatcher rd = request.getRequestDispatcher("member/memberRegister.html");
			rd.forward(request, response);	
		} else if (command.equals("/memberContent.do")) {
			int num = Integer.parseInt(request.getParameter("num"));
			MemberDAO dao = new MemberDAO();
			MemberVO vo =dao.memberContent(num);
			
			// 객체바인딩
			request.setAttribute("vo", vo);
			RequestDispatcher rd = request.getRequestDispatcher("member/memberContent.jsp");
			rd.forward(request, response);
		} else if (command.equals("/memberUpdate.do")) {
			int num=Integer.parseInt(request.getParameter("num"));
			int age=Integer.parseInt(request.getParameter("age"));
			String email=request.getParameter("email");
			String phone=request.getParameter("phone");
			MemberVO vo=new MemberVO();
			vo.setNum(num);
			vo.setAge(age);
			vo.setEmail(email);
			vo.setPhone(phone);
			MemberDAO dao=new MemberDAO();
			int cnt = dao.memberUpdate(vo);
			if(cnt>0) {
				response.sendRedirect("/MVC04/memberList.do");
			} else {
				throw new ServletException("not update");
			}
		} else if (command.equals("/memberDelete.do")) {
			int num = Integer.parseInt(request.getParameter("num"));
			
			// 2. 회원 삭제
			MemberDAO dao = new MemberDAO();
			int cnt = dao.memberDelete(num);
			if (cnt > 0) {
				response.sendRedirect("/MVC04/memberList.do");
			} else {
				throw new ServletException("could not be deleted");
			}
		}
	}
}
