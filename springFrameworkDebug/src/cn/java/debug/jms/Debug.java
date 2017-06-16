package cn.java.debug.jms;

import javax.jms.Destination;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.SimpleMessageConverter;

import cn.java.demo.jmstag.message.EmailMessage;

public class Debug {

	/*
	 	org.springframework.jms.config.JmsNamespaceHandler
	 		
	 		
		如下类，实现了org.springframework.context.SmartLifecycle接口
		{
			org.springframework.jms.listener.DefaultMessageListenerContainer
			org.springframework.jms.listener.SimpleMessageListenerContainer
			org.springframework.jms.config.JmsListenerEndpointRegistry
		}
			

	 	https://my.oschina.net/zhzhenqin/blog/86586
	 	http://blog.csdn.net/moonsheep_liu/article/details/6684948
	 */
	public static void main(String[] args) {
		JmsTemplate jmsTemplate = new JmsTemplate();
		{
//			Destination destination = new ActiveMQTopic("activeMQTopicXX");
			Destination destination = new ActiveMQQueue("activeMQTopicXX");
			jmsTemplate.setDefaultDestination(destination); // 目标
			jmsTemplate.setMessageConverter(new SimpleMessageConverter()); // 消息转换器
			
			{
				
				CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
				connectionFactory.setSessionCacheSize(10);
				connectionFactory.setCacheProducers(false);
				{
					ActiveMQConnectionFactory targetConnectionFactory = new ActiveMQConnectionFactory();
					targetConnectionFactory.setBrokerURL("tcp://localhost:61616");
					targetConnectionFactory.setUserName("userName");
					targetConnectionFactory.setPassword("password");
					connectionFactory.setTargetConnectionFactory(targetConnectionFactory);
				}
				jmsTemplate.setConnectionFactory(connectionFactory); // 连接工厂
			}
			
		}
		jmsTemplate.convertAndSend("emailQueue",new EmailMessage("info@example.com", "Hello"));
	}
	
	
}
