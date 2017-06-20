package cn.java.demo.webmvc.bean.controlleradvice.byannotation;

import java.io.IOException;
import java.lang.reflect.Type;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

public class RequestBodyAdviceImpl0 implements RequestBodyAdvice {

	@Override
	public boolean supports(MethodParameter methodParameter, Type targetType,
			Class<? extends HttpMessageConverter<?>> converterType) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter,
			Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
			Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
			Class<? extends HttpMessageConverter<?>> converterType) {
		// TODO Auto-generated method stub
		return null;
	}

}
