package cn.java.demo.jmstag.byannotaion.consumer;

import org.springframework.jms.annotation.JmsListener;

import cn.java.demo.jmstag.message.EmailMessage;

public class TopicConsumer {
   
	@JmsListener(destination = "defaultTopicName0", containerFactory="cachingConnectionFactory0")
    public void consumerDefaultTopic(EmailMessage email) {
        System.out.println("Received <" + email + ">");
    }
	
	@JmsListener(destination = "fooTopicName", containerFactory="cachingConnectionFactory0")
	public void consumerFooTopic(EmailMessage email) {
		System.out.println("Received <" + email + ">");
	}
	
}