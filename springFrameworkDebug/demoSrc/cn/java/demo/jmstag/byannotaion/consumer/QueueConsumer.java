package cn.java.demo.jmstag.byannotaion.consumer;

import org.springframework.jms.annotation.JmsListener;

import cn.java.demo.jmstag.message.EmailMessage;

/**
 * 消费者，使用注解配置
 * @author zhouzhian
 */
public class QueueConsumer {
   
	@JmsListener(destination = "defaultQuqueName0", containerFactory="cachingConnectionFactory0")
    public void consumerDefaultQueue(EmailMessage email) {
        System.out.println("Received <" + email + ">");
    }
	
	@JmsListener(destination = "emailQuqueName0", containerFactory="cachingConnectionFactory0")
	public void consumerEmailQueue(EmailMessage email) {
		System.out.println("Received <" + email + ">");
	}
	
	@JmsListener(destination = "fooQueueName0", containerFactory="cachingConnectionFactory0")
	public void consumerFooQueue(EmailMessage email) {
		System.out.println("Received <" + email + ">");
	}
	
}