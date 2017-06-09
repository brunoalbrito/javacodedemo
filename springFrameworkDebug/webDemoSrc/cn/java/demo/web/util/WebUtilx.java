package cn.java.demo.web.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.util.HtmlUtils;
import org.springframework.web.util.UriTemplate;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.UrlPathHelper;
import org.springframework.web.util.WebUtils;

public class WebUtilx {
	
	public static final String X_REQUESTED_WITH = "X-Requested-With";
	private static final UrlPathHelper URL_PATH_HELPER = new UrlPathHelper();
	
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
	
	public static UrlPathHelper getUrlPathHelper() {
		return URL_PATH_HELPER;
	}
	
	/**
	 * 上下文路径，如“空” 或者 “/webapp1”
	 */
	public static String getContextPath(HttpServletRequest request) {
		return URL_PATH_HELPER.getOriginatingContextPath(request);
	}
	
	/**
	 * 相对上下文路径的地址，如“空” 或者 “/webapp1/front-servlet/servlet1”
	 */
	public static String getContextUrl(String relativeUrl,HttpServletRequest request,HttpServletResponse response) {
		String url = getContextPath(request) + relativeUrl;
		if (response != null) {
			url = response.encodeURL(url);
		}
		return url;
	}
	/**
	 * 相对上下文路径的地址，如“空” 或者 “/webapp1/front-servlet/servlet1”
	 */
	public static String getContextUrl(String relativeUrl,WebRequest webRequest) {
		if(!(webRequest instanceof ServletWebRequest)){
			throw new RuntimeException("webRequest is not instanceof ServletWebRequest");
		}
		ServletWebRequest servletWebRequest = (ServletWebRequest)webRequest;
		return getContextUrl(relativeUrl,servletWebRequest.getNativeRequest(HttpServletRequest.class),servletWebRequest.getNativeResponse(HttpServletResponse.class));
	}
	
	/**
	 * 相对上下文路径的地址，如 “/webapp1/front-servlet/servlet1/param0/param1”
	 */
	public static String getContextUrl(String relativeUrl, Map<String, ?> params,HttpServletRequest request,HttpServletResponse response) {
		String url = getContextPath(request) + relativeUrl;
		UriTemplate template = new UriTemplate(url);
		url = template.expand(params).toASCIIString();
		if (response != null) {
			url = response.encodeURL(url);
		}
		return url;
	}
	
	/**
	 * 相对上下文路径的地址，如 “/front-servlet”
	 */
	public static String getPathToServlet(WebRequest webRequest) {
		if(!(webRequest instanceof ServletWebRequest)){
			throw new RuntimeException("webRequest is not instanceof ServletWebRequest");
		}
		ServletWebRequest servletWebRequest = (ServletWebRequest)webRequest;
		return getPathToServlet(servletWebRequest.getNativeRequest(HttpServletRequest.class));
	}
	/**
	 * 相对上下文路径的地址，如 “/front-servlet”
	 */
	public static String getPathToServlet(HttpServletRequest request) {
		String path = URL_PATH_HELPER.getOriginatingContextPath(request);
		if (StringUtils.hasText(URL_PATH_HELPER.getPathWithinServletMapping(request))) {
			path += URL_PATH_HELPER.getOriginatingServletPath(request);
		}
		return path;
	}
	
	public static String getRequestUri(HttpServletRequest request) {
		return URL_PATH_HELPER.getOriginatingRequestUri(request);
	}
	
	public static String getQueryString(HttpServletRequest request) {
		return URL_PATH_HELPER.getOriginatingQueryString(request);
	}
	
	public static String htmlEscape(String content,HttpServletResponse response) {
		return HtmlUtils.htmlEscape(content, response.getCharacterEncoding());
	}
	public static String htmlEscape(String content) {
		return HtmlUtils.htmlEscape(content);
	}
	
	public static String encode(String source, String encoding) throws UnsupportedEncodingException{
		return UriUtils.encode(source,encoding);
	}
	public static String decode(String source, String encoding) throws UnsupportedEncodingException{
		return UriUtils.decode(source,encoding);
	}
	
	/**
	 * 获取上传文件的文件名
	 * part.getSubmittedFileName() 在不同平台下有兼容性的问题
	 * 在jetty中没有实现part.getSubmittedFileName()，方法是part.getContentDispositionFilename()
	 * 在tomcat中有实现方法part.getSubmittedFileName()
	 */
	public static String getSubmittedFileName(Part part){
		String disposition = part.getHeader("content-disposition");
		String filename = extractFilename(disposition,"filename="); // 文件名
		if (filename == null) {
			filename = extractFilename(disposition, "filename*=");
			if (filename == null) {
				return null;
			}
			int index = filename.indexOf("'");
			if (index != -1) {
				Charset charset = null;
				try {
					charset = Charset.forName(filename.substring(0, index));
				}
				catch (IllegalArgumentException ex) {
					// ignore
				}
				filename = filename.substring(index + 1);
				// Skip language information..
				index = filename.indexOf("'");
				if (index != -1) {
					filename = filename.substring(index + 1);
				}
				if (charset != null) {
					filename = new String(filename.getBytes(Charset.forName("us-ascii")), charset);
				}
			}
			return filename;
		}
		return filename;
	}
	
	private static String extractFilename(String contentDisposition, String key) {
		if (contentDisposition == null) {
			return null;
		}
		int startIndex = contentDisposition.indexOf(key); // key === "filename="
		if (startIndex == -1) {
			return null;
		}
		String filename = contentDisposition.substring(startIndex + key.length());
		if (filename.startsWith("\"")) {
			int endIndex = filename.indexOf("\"", 1);
			if (endIndex != -1) {
				return filename.substring(1, endIndex);
			}
		}
		else {
			int endIndex = filename.indexOf(";");
			if (endIndex != -1) {
				return filename.substring(0, endIndex);
			}
		}
		return filename;
	}
}
