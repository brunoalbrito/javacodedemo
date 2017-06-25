package cn.java.demo.data_kafka;

import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.java.demo.data_kafka.producorservice.KafkaProducerService;

public class KafkaTest {
	
	/*
	 * http://www.cnblogs.com/wangb0402/p/6187796.html
	 */
	public static void main(String[] args) throws Exception {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"classpath:cn/java/demo/data_kafka/applicationContextKafka.xml");

		String topic = "orderTopic";
        String message = "test";
        String ifPartition = "0";
        Integer partitionNum = 3;
        String role = "test";//用来生成key
        KafkaProducerService kafkaProducerService = context.getBean("kafkaProducerService",KafkaProducerService.class);
        Map<String,Object> res =  kafkaProducerService.sndMesForTemplate(topic, message, ifPartition, partitionNum, role);
        {
        	System.out.println("测试结果如下：===============");
        	String resMessage = (String)res.get("message");
        	String code = (String)res.get("code");
        	
        	System.out.println("code:"+code);
        	System.out.println("message:"+resMessage);
        }
		// 关闭上下文
		if (context instanceof AbstractApplicationContext)

		{
			AbstractApplicationContext abstractApplicationContext = (AbstractApplicationContext) context;
			abstractApplicationContext.close();
		}

	}

	
}
