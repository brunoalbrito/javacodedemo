package cn.java.demo.aoptag.aspect;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect(value="pertarget(execution(public void cn.java.demo.aoptag.bean.HelloServiceImpl4AspectJAutoProxyTag.*(..)))")
public class Aspect4AspectJAutoProxyTag {
	
	/* 拦截 HelloServiceImpl4AspectJAutoProxyTag.method1() 方法的调用 */
	@Before(value="execution(public void cn.java.demo.aoptag.bean.HelloServiceImpl4AspectJAutoProxyTag.method1(..))")/*,argNames=""*/
	public void beforeHelloServiceImpl4AspectJAutoProxyTagMethod1() {
		System.out.println("方法调用前 - "+this.getClass().getSimpleName()+":beforeHelloServiceImpl4AspectJAutoProxyTagMethod1 = " + System.currentTimeMillis());
	}
	
	@After(value="execution(public void cn.java.demo.aoptag.bean.HelloServiceImpl4AspectJAutoProxyTag.method1(..))")/*,argNames=""*/
	public void afterHelloServiceImpl4AspectJAutoProxyTagMethod1() {
		System.out.println("方法调用后 - "+this.getClass().getSimpleName()+":afterHelloServiceImpl4AspectJAutoProxyTagMethod1 = " + System.currentTimeMillis());
	}
	
	/* 拦截 HelloServiceImpl4AspectJAutoProxyTag.method2() 方法的调用 */
	@Before(value="execution(public void cn.java.demo.aoptag.bean.HelloServiceImpl4AspectJAutoProxyTag.method2(..))")/*,argNames=""*/
	public void beforeHelloServiceImpl4AspectJAutoProxyTagMethod2() {
		System.out.println("方法调用前 - "+this.getClass().getSimpleName()+":beforeHelloServiceImpl4AspectJAutoProxyTagMethod2 = " + System.currentTimeMillis());
	}
	
	@After(value="execution(public void  cn.java.demo.aoptag.bean.HelloServiceImpl4AspectJAutoProxyTag.method2(..))")/*,argNames=""*/
	public void afterHelloServiceImpl4AspectJAutoProxyTagMethod2() {
		System.out.println("方法调用前 - "+this.getClass().getSimpleName()+":afterHelloServiceImpl4AspectJAutoProxyTagMethod2 = " + System.currentTimeMillis());
	}
	
//	@Around(value="",argNames="")
//	public void aspectAround() {
//		System.out.println("方法调用后 - "+this.getClass().getSimpleName()+":aspectMethodAfter = " + System.currentTimeMillis());
//	}
//	
//	@AfterReturning(value="",argNames="")
//	public void aspectAfterReturning() {
//		System.out.println("方法调用后 - "+this.getClass().getSimpleName()+":aspectMethodAfter = " + System.currentTimeMillis());
//	}
//	
//	@AfterThrowing(value="",argNames="")
//	public void aspectAfterThrowing() {
//		System.out.println("方法调用后 - "+this.getClass().getSimpleName()+":aspectMethodAfter = " + System.currentTimeMillis());
//	}
	
}
