package cn.java.demo.data_memcache;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;

public class Test {
	public static void main(String[] args) throws Exception {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"classpath:cn/java/demo/data_redis/applicationContext.xml");
		
		{
			MemCachedClient memcachedClient = context.getBean("memcachedClient",MemCachedClient.class);//　消息模板
		}
		
	}
}
