package cn.java.demo.jmstag.activemq.byannotaion.consumer;

import org.springframework.jms.annotation.JmsListener;

import cn.java.demo.jmstag.activemq.message.EmailMessage;

/**
 * 消费者，使用注解配置
 * @author zhouzhian
 */
public class TopicConsumer {
   
	@JmsListener(destination = "defaultTopicNameOfActiveMQ_0")
    public void consumerDefaultTopic(EmailMessage email) {
        System.out.println("Received <" + email + ">");
    }
	
	@JmsListener(destination = "fooTopicNameOfActiveMQ_0")
	public void consumerFooTopic(EmailMessage email) {
		System.out.println("Received <" + email + ">");
	}
	
}