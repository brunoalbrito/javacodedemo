package cn.java.demo.data_common;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;

public class Test {
	/*
	 */
	public static void main(String[] args) throws Exception {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"classpath:cn/java/demo/data_common/applicationContext.xml");
		
		{
			context.getBean("jackson2Populator0");
		}
		
	}
}
