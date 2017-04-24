package cn.java.jfinal.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Test3DebugServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// 取得Application对象
		ServletContext servletContext = request.getServletContext();
		servletContext.setAttribute("application_key", "application_value");

		// 取得Session对象
		HttpSession httpSession = request.getSession();
		httpSession.setAttribute("session_key", "session_value");

		// 取得Cookies对象
		request.getCookies();
		response.addCookie(new Cookie("cookie_key", "cookie_value"));

		// 取得Request对象
		request.setAttribute("request_key", "request_value");

		// 取得PrintWriter对象
		PrintWriter out = response.getWriter();
		out.write("hello world!!");
		out.flush();
		out.close();
	}

}
