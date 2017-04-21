package cn.java.ns.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.java.demo.beantag.api.HelloService;


public class Test {

	/**
	 * https://github.com/spring-projects
	 * http://mvnrepository.com/search?q=Spring
	 * 使用4.3.6版本
	 */
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:cn/java/ns/test/applicationContext.xml");
	
		System.out.println("################################################################################################");
		
		System.out.println("-------------自定义“命名空间处理器”的使用-------------------");
		
		System.out.println("-------------bean的使用-------------------");
		HelloService helloService = context.getBean("helloService", HelloService.class);
		helloService.sayHello();
	}

}
