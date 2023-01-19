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

import kr.bit.controller.Controller;
import kr.bit.controller.MemberContentController;
import kr.bit.controller.MemberDeleteController;
import kr.bit.controller.MemberInsertController;
import kr.bit.controller.MemberListController;
import kr.bit.controller.MemberRegisterController;
import kr.bit.controller.MemberUpdateController;
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
		Controller controller = null;
		String nextPage=null;
		
		// 핸들러매핑 -> HandlerMapping
		HandlerMapping mapping = new HandlerMapping();
		controller = mapping.getController(command);
		nextPage = controller.requestHandler(request, response);
		
		// forward인지 redirect인지 구별 - redirect인 경우는 문자열 redirect:를 붙인다.
		if (nextPage != null) {
			if (nextPage.indexOf("redirect:") != -1) {
				// redirect:/MVC04/memberList.do -> : 기준으로 문자열을 잘라서 뒤쪽 경로를 redirect 해야한다.
				response.sendRedirect(nextPage.split(":")[1]); // redirect
			} else {
				// forward
				RequestDispatcher rd = request.getRequestDispatcher(ViewResolver.makeView(nextPage));
				rd.forward(request, response);
			}
		}
	}
}
