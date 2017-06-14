package cn.java.demo.jmstag;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;

import cn.java.demo.jmstag.message.Email;

public class Test {
	public static void main(String[] args) throws Exception {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"classpath:cn/java/demo/jmstag/applicationContext.xml");
		JmsTemplate jmsTemplate = context.getBean("jmsTemplate",JmsTemplate.class);		
		jmsTemplate.convertAndSend("mailbox", new Email("info@example.com", "Hello"));
	}
}
