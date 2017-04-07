package cn.java.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.java.test.api.HelloApi;


public class Test {
	
	/**
	 * https://github.com/spring-projects
	 * http://mvnrepository.com/search?q=Spring
	 * 使用4.3.6版本
	 */
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
//		ApplicationContext context = new ClassPathXmlApplicationContext();
		HelloApi h = context.getBean("hello", HelloApi.class);
		h.sayHello();
	}
	
}
