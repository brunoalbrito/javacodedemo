package cn.java.demo.webmvc.bean.handlermethodargumentresolver;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.ServletRequestMethodArgumentResolver;

import cn.java.demo.web.util.WebUtilx;

public class ServletRequestMethodArgumentResolverX extends ServletRequestMethodArgumentResolver {

	
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return super.supportsParameter(parameter);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		Class<?> paramType = parameter.getParameterType();
		if (WebRequest.class.isAssignableFrom(paramType)) {
			return webRequest;
		}
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		
		// ajax的识别
		{
			request.setAttribute("isAjax", false);
			if (WebUtilx.isAjaxRequest(request)) {
				request.setAttribute("isAjax", true);
			}
		}
		
		return super.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
	}

}
