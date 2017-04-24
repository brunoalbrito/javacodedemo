package cn.java.jfinal.interceptor;

import com.jfinal.aop.InterceptorStack;

/**
 * InterceptorStack 可将多个拦截器组合成一个拦截器，当需要对某一组拦截器重复配置使用的时候可以减少配置代码量
 *
 *  @Before(ManagerInterceptor.class)
 */
public class ManagerInterceptor extends InterceptorStack {
	public void config() {
		addInterceptors(new Demo0Interceptor(), new Demo1Interceptor());
	}
}