package cn.java.demo.txtag;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.java.demo.txtag.bean.byannotation.service.FooOneByAnnotaionService;
import cn.java.demo.txtag.bean.byxml.service.FooOneByXmlService;

public class Test {
	public static void main(String[] args) throws Exception {

		ApplicationContext context = new ClassPathXmlApplicationContext(
				"classpath:cn/java/demo/txtag/applicationContext.xml");
		
		System.out.println("\n----------使用XML配置--------------");
		{
			FooOneByXmlService fooOneByXmlService = (FooOneByXmlService) context.getBean("fooOneByXmlService");
			System.out.println("fooOneByXmlService.insertFooInfo() = " + fooOneByXmlService.insertFooInfo());
		}
		
		System.out.println("\n----------使用注解配置--------------");
		{
			FooOneByAnnotaionService fooOneByAnnotaionService = (FooOneByAnnotaionService) context.getBean("fooOneByAnnotaionService");
			System.out.println("fooOneByAnnotaionService.insertFooInfo() = " + fooOneByAnnotaionService.insertFooInfo());
		}
		
		
	}
}
