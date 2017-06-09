package cn.java.demo.beantag.internal;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.DefaultBeanNameGenerator;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;
import org.springframework.core.Ordered;

import cn.java.demo.util.ApplicationContextUtil;

public class BeanNameGeneratorTest {

	public static void testBeanNameGenerator(AbstractRefreshableConfigApplicationContext context) {
		
		BeanDefinitionRegistry registry = ApplicationContextUtil.getBeanFactoryAndTryCastTypeToBeanDefinitionRegistry(context);
		ConfigurableListableBeanFactory beanFactory = ApplicationContextUtil.getBeanFactoryAndTryCastTypeToConfigurableListableBeanFactory(context);
		if (registry == null || beanFactory == null) {
			return;
		}
		
		// 创建一个bean定义
		RootBeanDefinition beanDefinition0 = null;
		{ 
			String beanName0 = "BeanNameGeneratorTest_FooService_0";
			beanDefinition0 = new RootBeanDefinition(FooService.class);
			beanDefinition0.setSource(null);
			beanDefinition0.setScope(BeanDefinition.SCOPE_SINGLETON);
			beanDefinition0.setAutowireMode(RootBeanDefinition.AUTOWIRE_BY_NAME);
			beanDefinition0.getPropertyValues().add("field1", Ordered.HIGHEST_PRECEDENCE);
			beanDefinition0.getPropertyValues().add("field2", "beanDefinition0_field2Value");
			beanDefinition0.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
			registry.registerBeanDefinition(beanName0, beanDefinition0);
		}
		
		// 生成bean名称
		BeanNameGenerator beanNameGenerator = new DefaultBeanNameGenerator();
		String beanName = beanNameGenerator.generateBeanName(beanDefinition0,registry); // 自动生成beanName
		System.out.println("beanName = "+beanName);
	}
	
	private static class FooService {
		
	}
}
