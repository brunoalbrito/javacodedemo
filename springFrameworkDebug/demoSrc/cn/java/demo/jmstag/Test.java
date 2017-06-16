package cn.java.demo.jmstag;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;

import cn.java.demo.jmstag.message.EmailMessage;
import cn.java.demo.jmstag.producor.QueueProducer;
import cn.java.demo.jmstag.producor.TopicProducer;

public class Test {
	public static void main(String[] args) throws Exception {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"classpath:cn/java/demo/jmstag/applicationContext.xml");
		
		// 生产者往“队列中心”发送数据
		{
			JmsTemplate jmsTemplate = context.getBean("queueTemplate",JmsTemplate.class);//　消息模板
			// 把EmailMessage对象发送到emailQueue队列中
			jmsTemplate.convertAndSend("emailQueue", new EmailMessage("info@example.com", "Hello")); 
		}
		
		// 生产者往“队列中心”发送数据
		{
//			
			QueueProducer queueProducer = context.getBean("queueProducer",QueueProducer.class);
			queueProducer.convertAndSend("emailQueue", new EmailMessage("info@example.com", "Hello"));
		}
		
		// 生产者往“订阅中心”发送数据
		{
//			
			TopicProducer topicProducer = context.getBean("topicProducer",TopicProducer.class);
			topicProducer.convertAndSend("emailQueue", new EmailMessage("info@example.com", "Hello"));
		}
		
		Thread.currentThread().sleep(50*1000); // 等待50秒，让消费者执行完任务
		
		// 关闭上下文
		if(context instanceof AbstractApplicationContext){
			AbstractApplicationContext abstractApplicationContext = (AbstractApplicationContext)context;
			abstractApplicationContext.close();
		}
		
	}
}
