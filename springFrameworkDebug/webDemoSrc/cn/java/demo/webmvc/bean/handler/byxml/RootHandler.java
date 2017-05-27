package cn.java.demo.webmvc.bean.handler.byxml;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.HttpRequestHandler;

/**
 * beanName配置成 “/”
 */
public class RootHandler implements HttpRequestHandler {

	@Override
	public void handleRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
	}

}
