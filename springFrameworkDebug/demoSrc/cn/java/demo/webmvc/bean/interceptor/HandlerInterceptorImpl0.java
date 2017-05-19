package cn.java.demo.webmvc.bean.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.HttpRequestHandler;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class HandlerInterceptorImpl0 implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		/*
		 	实现HttpRequestHandler接口的Handler和实现Controller接口的Handler区别是：
		 		实现HttpRequestHandler接口的Handler的执行是没有返回值的
		 		实现Controller接口的Handler的执行是有返回值的
		 		
		 */
		if(handler instanceof HttpRequestHandler){ 
			// 后面的调用方式是 ((HttpRequestHandler) handler).handleRequest(request, response); // 没有返回值
			String bestMatchingPattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
			String pathWithinMapping = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
			System.out.println(pathWithinMapping);
			if("/ctrl1/method0".equals(pathWithinMapping)){
				return false; // 没有权限访问
			}
		}
		else if(handler instanceof Controller){ 
			// 后面的调用方式是 ((Controller) handler).handleRequest(request, response); // 有返回值
			String bestMatchingPattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
			String pathWithinMapping = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		}
		else if(handler instanceof HandlerMethod){
			// 有返回值
		}
		
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	/**
	 * 
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
//		两种情况会执行本方法：
//			1、在调用preHandle()时，返回false，会直接调用本方法（此时preHandle会执行、postHandle不会执行）
//			2、在所有逻辑（preHandle会执行、postHandle会执行）正常执行后，会直接调用本方法
	}

}
