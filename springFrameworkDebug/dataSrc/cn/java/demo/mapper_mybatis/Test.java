package cn.java.demo.mapper_mybatis;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.java.demo.mapper_mybatis.service.impl.FooServiceImpl;


public class Test {
	
	public static void main(String[] args) throws Exception {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"classpath:cn/java/demo/mapper_mybatis/applicationContext.xml");
		FooServiceImpl fooService = context.getBean("fooService",FooServiceImpl.class);
		fooService.testMethod();
	}
}
