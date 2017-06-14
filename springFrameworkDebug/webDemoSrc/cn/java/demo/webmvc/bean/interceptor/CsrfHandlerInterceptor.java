package cn.java.demo.webmvc.bean.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

public interface CsrfHandlerInterceptor extends HandlerInterceptor {
	public static final String CSRF_TOKEN_KEYNAME_REQUEST_ATTRIBUTE_NAME = CsrfHandlerInterceptor.class.getName() + ".CSRF_KEYNAME";
	public static final String CSRF_TOKEN_VALUE_REQUEST_ATTRIBUTE_NAME = CsrfHandlerInterceptor.class.getName() + ".CSRF_VALUE";
}
