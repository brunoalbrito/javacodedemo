package cn.java.demo.beantag.internal;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.Lifecycle;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;

import cn.java.demo.beantag.api.HelloService;
import cn.java.demo.util.ApplicationContextUtil;

public class GetBeanXTest {
	public static void testGetBeanX(
			AbstractRefreshableConfigApplicationContext context) {
		
		ApplicationContextUtil.debugPrintCodeIn(GetBeanXTest.class, "testGetBeanX(...)");
		
		BeanDefinitionRegistry registry = ApplicationContextUtil.getBeanFactoryAndTryCastTypeToBeanDefinitionRegistry(context);
		ConfigurableListableBeanFactory beanFactory = ApplicationContextUtil.getBeanFactoryAndTryCastTypeToConfigurableListableBeanFactory(context);

		if (registry == null || beanFactory == null) {
			throw new RuntimeException("registry == null || beanFactory == null");
		}
		
		// 获取指定“beanName”的对象
		{
			HelloService helloService = context.getBean("helloService", HelloService.class);
			System.out.println(helloService);
		}
		
		// 获取实现指定“接口类型”的对象
		{
			String[] beanNames = beanFactory.getBeanNamesForType(Lifecycle.class, false, false); 
			System.out.println(beanNames);
		}
	}
}
