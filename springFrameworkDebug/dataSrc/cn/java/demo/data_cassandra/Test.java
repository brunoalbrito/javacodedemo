package cn.java.demo.data_cassandra;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.java.demo.data_cassandra.repository.UserRepository;

public class Test {
	/*
	 	DataStax Java Driver for Apache Cassandra 3.x (客户端驱动):
	 		https://docs.datastax.com/en/developer/driver-matrix/doc/javaDrivers.html
	 */
	public static void main(String[] args) throws Exception {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"classpath:cn/java/demo/data_redis/applicationContext.xml");
		
		{
			UserRepository userRepository = context.getBean("userRepository",UserRepository.class);
			userRepository.count();
		}
		
	}
}
