package kr.bit.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Controller {
	// 모든 POJO가 가지고 있어야되는 메서드
	// return Type은 String -> POJO가 FrontController한테 next page 정보를 넘기기 위해서
	public String requestHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
	
}
