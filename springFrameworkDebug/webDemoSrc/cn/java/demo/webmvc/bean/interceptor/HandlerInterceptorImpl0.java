package cn.java.demo.webmvc.bean.interceptor;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
		System.out.println("\n---> code in : " + this.getClass().getName());
		final boolean enableDebug = false;
		// 检查登录
		{
			HttpSession httpSession = request.getSession();
			if(httpSession != null && enableDebug){
				Object isUserLoginObj = httpSession.getAttribute("isUserLogin");
				if(isUserLoginObj==null){
					response.getWriter().write("{status=201,message='please login.'}");
					return false;
				}
				
				boolean isUserLogin = (boolean)isUserLoginObj;
				if(isUserLogin==false){
					response.getWriter().write("{status=201,message='please login.'}");
					return false;
				}
			}
		}
		
		// 校验权限
		{
			// 后面的调用方式是 ((HttpRequestHandler) handler).handleRequest(request, response); // 没有返回值
			String bestMatchingPattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
			String pathWithinMapping = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
			System.out.println("bestMatchingPattern : " + bestMatchingPattern); // 内部路由路径  “/valid-handler/*”
			System.out.println("pathWithinMapping : " + pathWithinMapping); // 用户访问路径  “/valid-handler/login”
			if("/ctrl1/method0".equals(pathWithinMapping)){
				response.getWriter().write("{status=299,message='not authorization access.'}");
				return false; // 没有权限访问
			}
			
			{
				System.out.println("request.getServletContext().getContextPath() = " + request.getServletContext().getContextPath());
				System.out.println("request.getServletContext().getRealPath(\"/\") = "+ request.getServletContext().getRealPath("/"));
				System.out.println("request.getRequestURL() = " + request.getRequestURL());
				System.out.println("request.getRequestURI() = " + request.getRequestURI());
			}
		}
		
		/*
		 	实现HttpRequestHandler接口的Handler和实现Controller接口的Handler区别是：
		 		实现HttpRequestHandler接口的Handler的执行是没有返回值的
		 		实现Controller接口的Handler的执行是有返回值的
		 		
		 */
		if(handler instanceof HttpRequestHandler){ 
			// 后面的调用方式是 ((HttpRequestHandler) handler).handleRequest(request, response); // 没有返回值
			System.out.println("handler instanceof HttpRequestHandler");
		}
		else if(handler instanceof Controller){ 
			// 后面的调用方式是 return ((Controller) handler).handleRequest(request, response); // 有返回值
			System.out.println("handler instanceof Controller");
		}
		else if(handler instanceof HandlerMethod){
			// 有返回值
			System.out.println("handler instanceof HandlerMethod");
		}
		
		{
			System.out.println("--->请求的参数");
			Enumeration enumeration = request.getParameterNames();
			while (enumeration.hasMoreElements()) {
				String paramName = (String) enumeration.nextElement();
				String[] values = request.getParameterValues(paramName);
				StringBuilder stringBuilder = new StringBuilder();
				stringBuilder.append(paramName);
				stringBuilder.append(" = ");
				for (String string : values) {
					stringBuilder.append(string);
				}
				System.out.println(stringBuilder);
			}
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
