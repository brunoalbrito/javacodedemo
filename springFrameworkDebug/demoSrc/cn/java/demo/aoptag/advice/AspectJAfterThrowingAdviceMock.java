package cn.java.demo.aoptag.advice;

import java.io.Serializable;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.AfterAdvice;

/**
 * <aop:after-throwing>
 * 
 * 模拟 org.springframework.aop.aspectj.AspectJAfterThrowingAdvice
 * 
 * @author zhouzhian
 *
 */
@SuppressWarnings("serial")
public class AspectJAfterThrowingAdviceMock implements MethodInterceptor, AfterAdvice, Serializable {

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		try {
			return invocation.proceed();
		}
		catch (Throwable ex) {
			System.out.println("[" + this.getClass().getSimpleName() + " , 没被适配] 出现异常了：" + ex.getMessage());
			throw ex;
		}
	}

}
