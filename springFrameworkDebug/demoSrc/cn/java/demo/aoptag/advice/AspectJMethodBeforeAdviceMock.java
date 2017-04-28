package cn.java.demo.aoptag.advice;

import java.lang.reflect.Method;

import org.springframework.aop.MethodBeforeAdvice;

/**
 * <aop:before>
 * 模拟 org.springframework.aop.aspectj.AspectJMethodBeforeAdvice 类
 * 
 * 会被 org.springframework.aop.framework.adapter.MethodBeforeAdviceInterceptor 适配
 * @author zhouzhian
 *
 */
public class AspectJMethodBeforeAdviceMock implements MethodBeforeAdvice {

	@Override
	public void before(Method method, Object[] args, Object target) throws Throwable {
		System.out.println("[" + this.getClass().getSimpleName() + " , 有被适配]--> method.getName() = " + method.getName()+" , args = " + args + " , target = " + target);
	}

}
