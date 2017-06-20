package cn.java.demo.webmvc.customargumentresolvers;

import org.springframework.core.MethodParameter;
import org.springframework.web.servlet.mvc.method.annotation.ServletRequestMethodArgumentResolver;

public class CustomArgumentResolvers0 extends ServletRequestMethodArgumentResolver{
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return false;
	}
}
