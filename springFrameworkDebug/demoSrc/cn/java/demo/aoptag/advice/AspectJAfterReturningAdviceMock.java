package cn.java.demo.aoptag.advice;

import java.io.Serializable;
import java.lang.reflect.Method;

import org.springframework.aop.AfterAdvice;
import org.springframework.aop.AfterReturningAdvice;

/**
 * <aop:after-returning>
 * 模拟  org.springframework.aop.aspectj.AspectJAfterReturningAdvice 类
 * 
 * 会被 org.springframework.aop.framework.adapter.AfterReturningAdviceInterceptor 适配
 * @author zhouzhian
 *
 */
@SuppressWarnings("serial")
public class AspectJAfterReturningAdviceMock implements AfterReturningAdvice, AfterAdvice, Serializable  {

	@Override
	public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
		System.out.println("[" + this.getClass().getSimpleName() + " , 有被适配]-->目标类是："+target.getClass().getSimpleName());
		System.out.println("[" + this.getClass().getSimpleName() + " , 有被适配]-->"+method.getName()+"方法调用后");
	}

}
