package cn.java.demo.jmstag.activemq.producorservice;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jms.core.JmsTemplate;

public class TopicProducerService implements ApplicationContextAware{
	private static final String JMS_TEMPLATE_NAME = "topicTemplateOfActiveMQ";
	private ApplicationContext applicationContext;
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	
	public void convertAndSend(String destinationName, final Object message){
		JmsTemplate jmsTemplate = this.applicationContext.getBean(JMS_TEMPLATE_NAME,JmsTemplate.class);
		jmsTemplate.convertAndSend(destinationName, message);
	}
}
