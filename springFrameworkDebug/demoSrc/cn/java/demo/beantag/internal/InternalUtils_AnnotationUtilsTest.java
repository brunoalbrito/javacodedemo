package cn.java.demo.beantag.internal;

import java.lang.reflect.Method;

import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

public class InternalUtils_AnnotationUtilsTest {
	public static void testAnnotationUtils(AbstractRefreshableConfigApplicationContext context)  {
		try {
			Method method = FooService.class.getMethod("method0", null);
			if (ReflectionUtils.isEqualsMethod(method)) {
			}
			if (ReflectionUtils.isHashCodeMethod(method)) {
			}
			if (ReflectionUtils.isToStringMethod(method)) {
			}
			// AnnotationUtils.getValue(ann);
		} catch (Exception e) {
		}
		
	}
	
	public static class FooService {
		public void method0(){
			
		}
	}
}
