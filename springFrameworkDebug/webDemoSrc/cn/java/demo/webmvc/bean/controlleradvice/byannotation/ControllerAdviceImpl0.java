package cn.java.demo.webmvc.bean.controlleradvice.byannotation;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.validation.DataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.support.WebRequestDataBinder;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ExtendedServletRequestDataBinder;

import cn.java.demo.webmvc.form.UserLoginForm;

@ControllerAdvice(basePackages={"cn.java.controller0","cn.java.controller1."},assignableTypes={},annotations={})
public class ControllerAdviceImpl0 {
	
	@ModelAttribute
	public void modelAttribute0(
			ServletRequest servletRequest,HttpSession httpSession,InputStream inputStream,HttpServletRequest request,
			ServletResponse servletResponse,Writer writer,OutputStream outputStream,HttpServletResponse reponse,
			WebRequest webRequest) throws Exception{
		
		boolean result = false;
		if(result){
			{
				if(webRequest instanceof NativeWebRequest){ // webRequest = org.springframework.web.context.request.ServletWebRequest
					NativeWebRequest nativeWebRequest = (NativeWebRequest) webRequest;
					HttpServletRequest httpServletRequest = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
					HttpServletResponse httpServletResponse = nativeWebRequest.getNativeResponse(HttpServletResponse.class);
					
					// input
					{
						// header
						httpServletRequest.getHeader("key0");
						// get/post
						httpServletRequest.getParameter("key0");
						// session 
						httpServletRequest.getSession().getAttribute("key0");
						httpServletRequest.getSession().setAttribute("key0", "value0");
						// cookie
						httpServletRequest.getCookies();
						// attribute
						httpServletRequest.getAttribute("key0");
						httpServletRequest.setAttribute("key0", "value0");
						// InputStream
						httpServletRequest.getInputStream();
						// forward
						httpServletRequest.getRequestDispatcher("/index.html").forward(httpServletRequest, httpServletResponse);
					}
					
					// output
					{
						// CharacterEncoding
						httpServletResponse.setCharacterEncoding("utf-8");
						// header
						httpServletResponse.addHeader("key0", "value0");
						// cookie
						httpServletResponse.addCookie(null);
						// Writer
						httpServletResponse.getWriter().write("{json:json}");
					}
				}
				
				{
					// input
					{
						// header
						webRequest.getHeader("key0");
						// get/post
						servletRequest.getParameter("key0");
						webRequest.getParameter("key0");
						// session 
						httpSession.getAttribute("key0");
						webRequest.getAttribute("key0", WebRequest.SCOPE_SESSION);
						httpSession.getId();
						webRequest.getSessionId();
						// cookie
						
						// attribute
						webRequest.getAttribute("key0", WebRequest.SCOPE_REQUEST);
						servletRequest.getAttribute("key0");
						// InputStream
						servletRequest.getInputStream();
						// forward
						servletRequest.getRequestDispatcher("/index.html").forward(servletRequest, servletResponse);
					}
					
					// output
					{
						servletResponse.setCharacterEncoding("utf-8");
					}
				}
			}
		}
	}
	
	@ModelAttribute
	public void modelAttribute1(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) throws Exception{
	}
	
	/**
	 * 带@InitBinder注解的方法，必须返回void
	 */
	@InitBinder
	public void initBinder0(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse,DataBinder dataBinder){
		if(dataBinder instanceof ExtendedServletRequestDataBinder){
			
		}
		if(dataBinder instanceof WebRequestDataBinder){
			
		}
		if((dataBinder.getTarget()!=null) && (dataBinder.getTarget() instanceof UserLoginForm))
		{
			
		}
		System.out.println("dataBinder.getTarget() = "+dataBinder.getTarget());
		System.out.println("dataBinder.getObjectName() = "+dataBinder.getObjectName());
	}
	
	@InitBinder
	public void initBinder1(DataBinder dataBinder){
		
	}
	

}
