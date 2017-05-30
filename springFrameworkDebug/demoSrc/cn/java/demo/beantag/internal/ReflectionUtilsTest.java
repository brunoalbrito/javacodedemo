package cn.java.demo.beantag.internal;

import java.lang.reflect.Method;

import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;
import org.springframework.util.ReflectionUtils;

public class ReflectionUtilsTest {
	
	public static void testReflectionUtils(AbstractRefreshableConfigApplicationContext context)  {
		try {
			Method method = FooService.class.getMethod("method0", null);
			if (ReflectionUtils.isEqualsMethod(method)) {
			}
			if (ReflectionUtils.isHashCodeMethod(method)) {
			}
			if (ReflectionUtils.isToStringMethod(method)) {
			}
		} catch (Exception e) {
		}
		
	}
	
	public static class FooService {
		public void method0(){
			
		}
	}
}
