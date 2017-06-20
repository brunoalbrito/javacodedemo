package cn.java.demo.webmvc.customreturnvaluehandlers;

import org.springframework.core.MethodParameter;
import org.springframework.web.servlet.mvc.method.annotation.ModelAndViewMethodReturnValueHandler;

public class CustomReturnValueHandlers0 extends ModelAndViewMethodReturnValueHandler{
	
	@Override
	public boolean supportsReturnType(MethodParameter returnType) {
		return false;
	}
}
