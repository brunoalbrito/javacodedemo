package cn.java.test.bean;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.java.test.bean.api.HelloService;
import cn.java.test.bean.beans.Bean1;
import cn.java.test.bean.beans.Bean2;
import cn.java.test.bean.beans.Bean3;


public class Test {

	/**
	 * https://github.com/spring-projects
	 * http://mvnrepository.com/search?q=Spring
	 * 使用4.3.6版本
	 */
	public static void main(String[] args) {
//		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:cn/java/test/bean/applicationContext.xml");
		HelloService helloService = context.getBean("helloService", HelloService.class);
		helloService.sayHello();
		
		{ // bean的使用
			Bean1 bean1 = context.getBean("bean1", Bean1.class);
			bean1.testMethod();

			bean1 = context.getBean("bean1Alias0", Bean1.class);
			bean1.testMethod();
		}

		{ // bean别名
			Bean2 bean2 = context.getBean("bean2", Bean2.class);
			bean2.testMethod();

			bean2 = context.getBean("bean2Alias0", Bean2.class);
			bean2.testMethod();

			bean2 = context.getBean("bean2Alias1", Bean2.class);
			bean2.testMethod();
		}
		
		{ // 使用工厂的方式创建bean
			Bean3 bean3_0 = context.getBean("bean3_0", Bean3.class);
			bean3_0.testMethod();
			
			Bean3 bean3_1 = (Bean3) context.getBean("bean3_1");
			bean3_1.testMethod();
			System.out.println(bean3_1.getUsername());
			
			Bean3 bean3_2 = (Bean3)context.getBean("bean3_2");
			bean3_2.testMethod();
			System.out.println(bean3_2.getUsername());
			bean3_2.getBean2().testMethod();
		}
	}

}
