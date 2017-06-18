package cn.java.demo.jmstag.activemq.byannotaion.consumer;

import org.springframework.jms.annotation.JmsListener;

import cn.java.demo.jmstag.activemq.message.EmailMessage;

/**
 * 消费者，使用注解配置
 * @author zhouzhian
 */
public class QueueConsumer {
   
	@JmsListener(destination = "defaultQuqueNameOfActiveMQ_0", containerFactory="cachingConnectionFactoryOfActiveMQ_0")
    public void consumerDefaultQueue(EmailMessage email) {
        System.out.println("Received <" + email + ">");
    }
	
	@JmsListener(destination = "emailQuqueNameOfActiveMQ_0", containerFactory="cachingConnectionFactoryOfActiveMQ_0")
	public void consumerEmailQueue(EmailMessage email) {
		System.out.println("Received <" + email + ">");
	}
	
	@JmsListener(destination = "fooQueueNameOfActiveMQ_0", containerFactory="cachingConnectionFactoryOfActiveMQ_0")
	public void consumerFooQueue(EmailMessage email) {
		System.out.println("Received <" + email + ">");
	}
	
}