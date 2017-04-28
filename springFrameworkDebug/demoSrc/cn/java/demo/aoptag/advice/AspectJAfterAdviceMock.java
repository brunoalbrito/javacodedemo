package cn.java.demo.aoptag.advice;

import java.io.Serializable;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.AfterAdvice;
import org.springframework.aop.framework.ReflectiveMethodInvocation;
/**
 * <aop:after>
 * 
 * 模拟 org.springframework.aop.aspectj.AspectJAfterAdvice 类
 * 
 * 不会被适配
 * 
 * @author zhouzhian
 *
 */
@SuppressWarnings("serial")
public class AspectJAfterAdviceMock implements MethodInterceptor, AfterAdvice, Serializable {

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		if(invocation instanceof ReflectiveMethodInvocation){
			ReflectiveMethodInvocation reflectiveMethodInvocation = (ReflectiveMethodInvocation) invocation;
			reflectiveMethodInvocation.getProxy(); // 代理对象
			reflectiveMethodInvocation.getThis(); // 目标对象
			reflectiveMethodInvocation.getMethod();// 要执行的方法
			reflectiveMethodInvocation.getArguments();// 要传递的参数
		}
		try {
			return invocation.proceed(); // 让拦截器链往下走
		}
		finally {
			System.out.println("[" + this.getClass().getSimpleName() + ",没被适配]-->"+invocation.getMethod().getName()+"方法调用后");
		}
	}

}
