package cn.java.debug.jms;

import javax.jms.Destination;

import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.SimpleMessageConverter;

import cn.java.demo.jmstag.destination.MineDestination;
import cn.java.demo.jmstag.message.Email;

public class Debug {

	/*
	 	org.springframework.jms.config.JmsNamespaceHandler
	 	
	 	https://my.oschina.net/zhzhenqin/blog/86586
	 	http://blog.csdn.net/moonsheep_liu/article/details/6684948
	 */
	public static void main(String[] args) {
		JmsTemplate jmsTemplate = new JmsTemplate();
		{
//			Destination destination = new ActiveMQTopic("activeMQTopicXX");
			Destination destination = new MineDestination();
			jmsTemplate.setDefaultDestination(destination); // 目标
			jmsTemplate.setMessageConverter(new SimpleMessageConverter()); // 消息转换器
			{
				
				CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
				connectionFactory.setSessionCacheSize(10);
				connectionFactory.setCacheProducers(false);
				{
					ActiveMQConnectionFactory targetConnectionFactory = new ActiveMQConnectionFactory();
					targetConnectionFactory.setBrokerURL("tcp://localhost:61616");
					connectionFactory.setTargetConnectionFactory(targetConnectionFactory);
				}
				jmsTemplate.setConnectionFactory(connectionFactory); // 连接工厂
			}
		}
		jmsTemplate.convertAndSend(new Email("info@example.com", "Hello"));
	}
	
	/*
		execute(new SessionCallback<Object>() {
			@Override
			public Object doInJms(Session session) throws JMSException {
				doSend(session, destination_Destination对象, new MessageCreator() {
					@Override
					public Message createMessage(Session session) throws JMSException {
						//  org.springframework.jms.support.converter.SimpleMessageConverter.toMessage(message, session);
						return getRequiredMessageConverter().toMessage(message, session);
					}
				});
				return null;
			}
		}, false);
	 	
		
	 */

}
