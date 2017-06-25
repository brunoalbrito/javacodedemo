package cn.java.demo.data_kafka.producerlistener;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.ProducerListener;

public class KafkaProducerSendCallback implements ProducerListener{
    
	/**
     * 发送消息成功后调用
     */
    @Override
    public void onSuccess(String topic, Integer partition, Object key,
            Object value, RecordMetadata recordMetadata) {
    }

    /**
     * 发送消息错误后调用
     */
    @Override
    public void onError(String topic, Integer partition, Object key,
            Object value, Exception exception) {
        exception.printStackTrace();
    }

    /**
     * 方法返回值代表是否启动kafkaProducer监听器
     */
    @Override
    public boolean isInterestedInSuccess() {
        return true;
    }

}