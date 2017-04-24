package cn.java.jfinal.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;

/**
 * 
 * Interceptor 是JFinal  AOP 的实现方式，通过实现Interceptor 接口以及使用@Before annotation 可以细粒度地进行配置
 *
 */
public class Demo3Interceptor implements Interceptor {
	public void intercept(ActionInvocation ai) {
		System.out.println("Before action invoking...Demo3Interceptor");
		ai.invoke();
		System.out.println("After action invoking...Demo3Interceptor");
	}
}