package cn.java.debug.webmvc;

public class Interceptor接口的差别_Handler实现接口的差别 {
	/*
	 	
	 	实现 HttpRequestHandler 接口的Handler和实现 Controller 接口的Handler区别是：
	 		实现HttpRequestHandler接口的Handler的执行是没有返回值的
	 		实现Controller接口的Handler的执行是有返回值的
	 		
	 	Interceptor的情况：（两种接口）
	 		org.springframework.web.servlet.HandlerInterceptor
	 		org.springframework.web.context.request.WebRequestInterceptor
	 		----
		 	1.实现HandlerInterceptor接口的拦截器
		 		不会被适配
		 		可以获取到HttpServletRequest request, 
		 		可以获取到HttpServletResponse response, 
		 		可以获取到Object handler
		 	
		 	2.实现WebRequestInterceptor接口的拦截器
		 		会被 org.springframework.web.servlet.handler.WebRequestHandlerInterceptorAdapter 适配
		 		可以获取到HttpServletRequest request, 
		 		可以获取到HttpServletResponse response
		 		不可以获取到Object handler
	 		
	 	------
	 	handler的情况：（两种接口 + 一种注解内置类）
	 		1.实现org.springframework.web.HttpRequestHandler接口的handler ---> 适配器是org.springframework.web.servlet.mvc.HttpRequestHandlerAdapter
	 			handler === org.springframework.web.HttpRequestHandler --- 没有返回值
	 		2.实现WebRequestInterceptor接口的handler ---> 适配器是org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter
	 			handler === org.springframework.web.servlet.mvc.Controller --- 有返回值
	 		3.通过注解扫描出来的handler ---> 适配器是org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter
	 			handler === org.springframework.web.method.HandlerMethod
	 */
}
