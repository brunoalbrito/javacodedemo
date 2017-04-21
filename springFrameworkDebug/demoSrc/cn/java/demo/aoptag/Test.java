package cn.java.demo.aoptag;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.java.demo.aoptag.api.HelloService;
import cn.java.demo.aoptag.mock.StupidAopInJavaTest;
import cn.java.demo.util.ApplicationContextUtil;



public class Test {

	/**
	 * https://github.com/spring-projects
	 * http://mvnrepository.com/search?q=Spring
	 * 使用4.3.6版本
	 */
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:cn/java/demo/aoptag/applicationContext.xml");
		
		{
			StupidAopInJavaTest stupidAopInJavaTest = new StupidAopInJavaTest();
			stupidAopInJavaTest.test((AbstractRefreshableConfigApplicationContext) context);
		}
//		ApplicationContextUtil.refresh((AbstractRefreshableConfigApplicationContext) context);		
		
		System.out.println("-------------拦截类的所有方法-------------------------");
		HelloService helloService1 = (HelloService)context.getBean("helloServiceImpl1");
		System.out.println("---helloService1.method1()---");
		helloService1.method1();
		System.out.println("---helloService1.method2()---");
		helloService1.method2();
		
		System.out.println("-------------拦截类的某个方法-------------------------");
		HelloService helloService2 = (HelloService)context.getBean("helloServiceImpl2");
		System.out.println("---helloService2.method1()---");
		helloService2.method1();
		System.out.println("---helloService2.method2()---");
		helloService2.method2();
		
		
		
	}

}
