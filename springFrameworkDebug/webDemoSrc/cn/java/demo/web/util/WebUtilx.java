package cn.java.demo.web.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.WebUtils;

public class WebUtilx {
	
	public static final String X_REQUESTED_WITH = "X-Requested-With";
	
	/**
	 * 获取根XmlWebApplicationContext对象
	 * @param request
	 * @return
	 */
	public static XmlWebApplicationContext getRootXmlWebApplicationContext(HttpServletRequest request){
		return ((XmlWebApplicationContext)request.getServletContext().getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE));
	}
	public static WebApplicationContext getRootXmlWebApplicationContextAlias(HttpServletRequest request){
		return (WebApplicationContext)WebApplicationContextUtils.getWebApplicationContext(request.getServletContext());
	}
	
	/**
	 * 获取Session
	 * @param request
	 * @param name
	 * @return
	 */
	public static Object getSessionAttribute(HttpServletRequest request, String name){
		return WebUtils.getSessionAttribute(request, name);
	}
	
	/**
	 * 设置Session
	 * @param request
	 * @param name
	 * @param value
	 */
	public static void setSessionAttribute(HttpServletRequest request, String name, Object value) {
		WebUtils.setSessionAttribute(request, name,value);
	}
	
	/**
	 * 获取SessionId
	 * @param request
	 * @return
	 */
	public static String getSessionId(HttpServletRequest request) {
		return WebUtils.getSessionId(request);
	}
	
	/**
	 * 获取Cookie
	 * @param request
	 * @param name
	 * @return
	 */
	public static Cookie getCookie(HttpServletRequest request, String name) {
		return WebUtils.getCookie(request, name);
	}
	
	/**
	 * 获取实际地址
	 * @param servletContext
	 * @param path
	 * @return
	 * @throws FileNotFoundException
	 */
	public static String getRealPath(ServletContext servletContext, String path) throws FileNotFoundException {
		return WebUtils.getRealPath(servletContext, path);
	}
	
	/**
	 * 是跨域请求
	 * @param request
	 * @return
	 */
	public static boolean isCorsRequest(HttpServletRequest request) {
		return CorsUtils.isCorsRequest(request);
	}
	
	public static boolean isPreFlightRequest(HttpServletRequest request) {
		return CorsUtils.isPreFlightRequest(request);
	}
	
	/**
	 * 被允许的请求源
	 * @param request
	 * @param allowedOrigins
	 * @return
	 */
	public static boolean isValidOrigin(HttpRequest request, Collection<String> allowedOrigins) {
		return WebUtils.isValidOrigin(request,allowedOrigins);
	}
	
	/**
	 * 是Ajax请求
	 * @param request
	 * @return
	 */
	public static boolean isAjaxRequest(HttpServletRequest request) {
		String xRequestedWith = request.getHeader(WebUtilx.X_REQUESTED_WITH);
		return ((xRequestedWith != null) && ("XMLHttpRequest".equals(xRequestedWith)));
	}
	
	/**
	 * 是Flash请求
	 * @param request
	 * @return
	 */
	public static boolean isFlashRequest(HttpServletRequest request) {
		String userAgent = request.getHeader(HttpHeaders.USER_AGENT);
		return ((userAgent != null) && ((userAgent.indexOf("Shockwave")!=-1) || userAgent.indexOf("Flash")!=-1));
	}
	
	/**
	 * 设置json的头
	 * @param response
	 * @throws IOException
	 */
	public static void addJsonHeader(HttpServletResponse response) throws IOException {
		response.addHeader("Content-type", "text/json");
	}
	
	/**
	 * 设置Response容器编码
	 */
	public static void setCharacterEncoding(HttpServletResponse response,String characterEncoding) throws IOException {
		if(characterEncoding==null){
			characterEncoding = "UTF-8";
		}
		response.setCharacterEncoding(characterEncoding);
	}
	
	/**
	 * 设置内容类型
	 * 		和setContentType函数是等幂的
	 * 	contentType = "text/html; charset=UTF-8";
	 */
	public static void setContentTypeIdempotent(HttpServletResponse response,String contentType) throws IOException {
		if(contentType.indexOf(";")!=-1){
			String[] contentTypeSplit = contentType.split(";");
			if(contentTypeSplit[0]!=null){
				response.addHeader("Content-type", contentTypeSplit[0].trim());
			}
			if(contentTypeSplit[1]!=null){
				setCharacterEncoding(response,contentTypeSplit[1].trim());
			}
		}
	}
	
	/**
	 * 设置内容类型
	 */
	public static void setContentType(HttpServletResponse response,String contentType) throws IOException {
		if(contentType==null){
			contentType = "text/html; charset=UTF-8";
		}
		response.setContentType(contentType);
	}
	
	/**
	 * 自动响应登录
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public static void autoResponseLogin(HttpServletRequest request,HttpServletResponse response) throws IOException {
		if(isAjaxRequest(request)){
			setCharacterEncoding(response,"UTF-8");
			addJsonHeader(response);
			response.getWriter().write("{status=201,message='please login.'}");
		}
		else{
		}
	}
}
