package cn.java.demo.jmstag.byannotaion.consumer;

import org.springframework.jms.annotation.JmsListener;

import cn.java.demo.jmstag.message.EmailMessage;

/**
 * 消费者，使用注解配置
 * @author zhouzhian
 */
public class TopicConsumer {
   
	@JmsListener(destination = "defaultTopicName0")
    public void consumerDefaultTopic(EmailMessage email) {
        System.out.println("Received <" + email + ">");
    }
	
	@JmsListener(destination = "fooTopicName")
	public void consumerFooTopic(EmailMessage email) {
		System.out.println("Received <" + email + ">");
	}
	
}