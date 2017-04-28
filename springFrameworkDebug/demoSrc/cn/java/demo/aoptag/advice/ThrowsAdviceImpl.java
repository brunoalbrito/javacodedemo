package cn.java.demo.aoptag.advice;

import java.lang.reflect.Method;

import org.springframework.aop.ThrowsAdvice;

/**
 * 这个类是没有开放标签的
 * 
 * 不能抑制异常
 * 会被 org.springframework.aop.framework.adapter.ThrowsAdviceInterceptor 适配
 */
public class ThrowsAdviceImpl implements ThrowsAdvice{
	
	public void afterThrowing(ThrowableImpl0 throwableImpl0){
		System.out.println("[" + this.getClass().getSimpleName() + " , 没被适配] 出现异常了：" + throwableImpl0.getMessage());
	}

	public void afterThrowing(Method method, Object[] args, Object target, ThrowableImpl1 throwableImpl1){
		System.out.println("[" + this.getClass().getSimpleName() + " , 没被适配] 出现异常了：" + throwableImpl1.getMessage());
	}
	
	public static class ThrowableImpl0 extends Throwable{
		
	}
	
	public static class ThrowableImpl1 extends Throwable{
		
	}
	

}
