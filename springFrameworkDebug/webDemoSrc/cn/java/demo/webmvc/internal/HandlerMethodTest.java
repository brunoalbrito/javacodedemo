package cn.java.demo.webmvc.internal;

import java.lang.reflect.Method;

import org.springframework.core.Conventions;
import org.springframework.core.GenericTypeResolver;
import org.springframework.web.method.HandlerMethod;

public class HandlerMethodTest {

	public static void main(String[] args) throws Exception {
		
		Object bean = new FooService();
		
		{
			Method method = bean.getClass().getMethod("method0", null);
			HandlerMethod handlerMethod = new HandlerMethod(bean,method);
			if(handlerMethod.isVoid()){
				System.out.println("返回值是void");
			}
		}
		
		{
			Method method = bean.getClass().getMethod("method1", null);
			HandlerMethod handlerMethod = new HandlerMethod(bean,method);
			System.out.println(handlerMethod.getReturnType().getParameterType());
		}
		
		{
			Method method = bean.getClass().getMethod("method3", null);
			Class<?> containingClass = method.getReturnType().getDeclaringClass();
			Class<?> resolvedType = GenericTypeResolver.resolveReturnType(method, containingClass);
			Object returnValue = new ReturnTypex();
			String returnValueName = Conventions.getVariableNameForReturnType(method, resolvedType, returnValue);
			System.out.println(resolvedType);
			System.out.println(returnValueName);
		}
	}

	public static class FooService  {
		public void method0(){
			
		}
		
		public String method1(){
			return "";
		}
		
		public ReturnTypex method3(){
			return null;
		}
	}
	
	public static class ReturnTypex  {
		
	}
}
