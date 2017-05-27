package cn.java.demo.webmvc.bean.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

public class WebRequestInterceptorImpl0 implements WebRequestInterceptor {

	@Override
	public void preHandle(WebRequest request) throws Exception {
		System.out.println("\n---> code in : " + this.getClass().getName());
		if(request instanceof NativeWebRequest){
			NativeWebRequest nativeWebRequest = (NativeWebRequest) request;
			System.out.println("request.getAttribute(\"sessionAttribute0\", RequestAttributes.SCOPE_SESSION) = " + request.getAttribute("sessionAttribute0", RequestAttributes.SCOPE_SESSION));
			System.out.println("request.getAttribute(\"requestAttribute0\", RequestAttributes.SCOPE_REQUEST) = " + request.getAttribute("requestAttribute0", RequestAttributes.SCOPE_REQUEST));
			
			{
				HttpServletRequest httpServletRequest = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
				@SuppressWarnings("unused")
				HttpServletResponse httpServletResponse = nativeWebRequest.getNativeResponse(HttpServletResponse.class);
				System.out.println("request.getServletContext().getContextPath() = " + httpServletRequest.getServletContext().getContextPath());
				System.out.println("request.getServletContext().getRealPath(\"/\") = "+httpServletRequest.getServletContext().getRealPath("/"));
				System.out.println("request.getRequestURL() = " + httpServletRequest.getRequestURL());
				System.out.println("request.getRequestURI() = " + httpServletRequest.getRequestURI());
			}
		}
	}

	@Override
	public void postHandle(WebRequest request, ModelMap model) throws Exception {

	}

	@Override
	public void afterCompletion(WebRequest request, Exception ex) throws Exception {

	}

}
