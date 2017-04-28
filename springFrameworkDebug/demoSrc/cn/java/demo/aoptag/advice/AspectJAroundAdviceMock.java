package cn.java.demo.aoptag.advice;

import java.io.Serializable;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.aop.ProxyMethodInvocation;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;

/**
 * 模拟 org.springframework.aop.aspectj.AspectJAroundAdvice
 * @author zhouzhian
 *
 */
@SuppressWarnings("serial")
public class AspectJAroundAdviceMock implements MethodInterceptor, Serializable{

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		if (!(invocation instanceof ProxyMethodInvocation)) {
			throw new IllegalStateException("MethodInvocation is not a Spring ProxyMethodInvocation: " + invocation);
		}
		// mi === org.springframework.aop.framework.ReflectiveMethodInvocation
		// mi === org.springframework.aop.framework.CglibAopProxy.CglibMethodInvocation
		ProxyMethodInvocation pmi = (ProxyMethodInvocation) invocation; 
		ProceedingJoinPoint pjp = lazyGetProceedingJoinPoint(pmi);
		System.out.println("[" + this.getClass().getSimpleName() + " , 没被适配..包裹]方法调用前 - "+this.getClass().getSimpleName()+":aspectMethod4Around = " + System.currentTimeMillis());
		Object result =  pjp.proceed(); // 被包裹了 
		System.out.println("[" + this.getClass().getSimpleName() + " , 没被适配..包裹]方法调用后 - "+this.getClass().getSimpleName()+":aspectMethod4Around = " + System.currentTimeMillis());
		return result;
	}
	
	protected ProceedingJoinPoint lazyGetProceedingJoinPoint(ProxyMethodInvocation rmi) {
		return new MethodInvocationProceedingJoinPoint(rmi);
	}

}
