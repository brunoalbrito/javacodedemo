package cn.java.demo.webmvc.internal;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.web.bind.ServletRequestParameterPropertyValues;
import org.springframework.web.context.request.ServletWebRequest;

public class BeanWrapperInMvcTest {
	
	public static void testPropertyAccessorFactory(){
		ServletWebRequestX servletWebRequestX = new ServletWebRequestX(null);
		BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(servletWebRequestX);
		beanWrapper.setExtractOldValueForEditor(true);
		beanWrapper.setAutoGrowNestedPaths(true);
		beanWrapper.setAutoGrowCollectionLimit(256);
		
		{
			ServletRequest request = null;
			MutablePropertyValues mpvs = new ServletRequestParameterPropertyValues(request); 
			
			boolean ignoreUnknownFields = true;
			boolean ignoreInvalidFields = false;
			
			// 把mpvs的属性值设置到beanWrapper
			beanWrapper.setPropertyValues(mpvs, ignoreUnknownFields, ignoreInvalidFields);
		}
	}
	
	private static class ServletWebRequestX extends ServletWebRequest{

		public ServletWebRequestX(HttpServletRequest request) {
			super(request);
		}
		
	}
}
