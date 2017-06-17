package cn.java.demo.data_mybatis;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.java.demo.data_mybatis.service.FooService;


public class Test {
	
	public static void main(String[] args) throws Exception {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"classpath:cn/java/demo/data_mybatis/applicationContext.xml");
		FooService fooService = context.getBean("fooService",FooService.class);
		fooService.testMethod();
	}
}
