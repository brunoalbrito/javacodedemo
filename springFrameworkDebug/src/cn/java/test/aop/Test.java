package cn.java.test.aop;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.java.test.aop.api.HelloService;



public class Test {

	/**
	 * https://github.com/spring-projects
	 * http://mvnrepository.com/search?q=Spring
	 * 使用4.3.6版本
	 */
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:cn/java/test/aop/applicationContext.xml");
		HelloService helloService1 = (HelloService)context.getBean("helloServiceImpl1");
		helloService1.method1();
		System.out.println("--------------------------------------");
		helloService1.method2();
		System.out.println("--------------------------------------");
		
		HelloService helloService2 = (HelloService)context.getBean("helloServiceImpl2");
		helloService2.method1();
		System.out.println("--------------------------------------");
		helloService2.method2();
		System.out.println("--------------------------------------");
	}

}
