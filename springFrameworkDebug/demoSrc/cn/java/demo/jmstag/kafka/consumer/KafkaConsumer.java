package cn.java.demo.jmstag.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.MessageListener;

/**
 * kafka监听器启动
 * 自动监听是否有消息需要消费
 * @author wangb
 *
 */
public class KafkaConsumer implements MessageListener<String, String> {
   
	/**
     * 监听器自动执行该方法
     *     消费消息
     *     自动提交offset
     *     执行业务代码
     *     （high level api 不提供offset管理，不能指定offset进行消费）
     */
    @Override
    public void onMessage(ConsumerRecord<String, String> record) {
        String topic = record.topic();
        String key = record.key();
        String value = record.value();
        long offset = record.offset();
        int partition = record.partition();
    }

}