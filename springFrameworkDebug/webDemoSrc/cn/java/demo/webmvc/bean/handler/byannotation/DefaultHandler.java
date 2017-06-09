package cn.java.demo.webmvc.bean.handler.byannotation;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.HttpRequestHandler;

import cn.java.demo.web.util.WebUtilx;

/**
 * http://localhost:8080/springwebmvc/
 */
public class DefaultHandler implements HttpRequestHandler {

	@Override
	public void handleRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		WebUtilx.setContentType(response, "text/html; charset=UTF-8");
		
		System.out.println("\n---> code in : " + this.getClass().getName());
		System.out.println("request.getServletContext().getContextPath() = " + request.getServletContext().getContextPath());
		System.out.println("request.getServletContext().getRealPath(\"/\") = "+request.getServletContext().getRealPath("/"));
		System.out.println("request.getRequestURL() = " + request.getRequestURL());
		System.out.println("request.getRequestURI() = " + request.getRequestURI());
		
		response.getWriter().write("hello , i am “" + this.getClass().getName()+"”");
	}

}
