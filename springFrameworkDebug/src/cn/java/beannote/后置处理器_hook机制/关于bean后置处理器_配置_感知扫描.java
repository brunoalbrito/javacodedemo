package cn.java.beannote.后置处理器_hook机制;

import java.lang.reflect.Constructor;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.support.PostProcessorRegistrationDelegate;
import org.springframework.util.ObjectUtils;

public class 关于bean后置处理器_配置_感知扫描 {

	public static void main(String[] args) {
		/*
		
			 // 关于BeanPostProcessors的注入机制
			 org.springframework.context.support.AbstractApplicationContext.refresh()
			 {
			 
			 	ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory(); // 创建bean工厂，加载bean的定义文件
			 
			 	org.springframework.context.support.AbstractApplicationContext.prepareBeanFactory(ConfigurableListableBeanFactory beanFactory) // 添加默认的 BeanPostProcessor
			 	{
			 		beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this)); // this === org.springframework.web.context.support.XmlWebApplicationContext
			 		...
			 		beanFactory.addBeanPostProcessor(new ApplicationListenerDetector(this)); // this === org.springframework.web.context.support.XmlWebApplicationContext
			 	}
			 	...
			 	
			 	// 扫描applicationContext.xml中实现了BeanFactoryPostProcessor接口的bean，并实例化和调用bean的postProcessBeanDefinitionRegistry(...)方法
			 	org.springframework.context.support.AbstractApplicationContext.invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory)
			 	{
			 		org.springframework.context.support.PostProcessorRegistrationDelegate.invokeBeanFactoryPostProcessors(beanFactory, getBeanFactoryPostProcessors());
			 		{
			 		
			 			if (beanFactory instanceof BeanDefinitionRegistry) { // !!! org.springframework.beans.factory.support.DefaultListableBeanFactory
				 			BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;
					 		String[] postProcessorNames = beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false); // 扫描 - 用户在applicationContext.xml配置的 BeanDefinitionRegistryPostProcessor类型的bean列表
					 		for (String ppName : postProcessorNames) {
					 			BeanDefinitionRegistryPostProcessor postProcessor = beanFactory.getBean(ppName, BeanDefinitionRegistryPostProcessor.class);
					 			postProcessor.postProcessBeanDefinitionRegistry(registry);
					 		}
				 		}
				 		
			 			String[] postProcessorNames = beanFactory.getBeanNamesForType(BeanFactoryPostProcessor.class, true, false); // 扫描 - 用户在applicationContext.xml配置的 BeanFactoryPostProcessor类型的bean列表
				 		for (String ppName : postProcessorNames) {
				 			BeanFactoryPostProcessor postProcessor = beanFactory.getBean(ppName, BeanFactoryPostProcessor.class);
				 			postProcessor.postProcessBeanFactory(beanFactory);
				 		}
			 		}
			 	}
			 	...
			 	
			 	// 扫描applicationContext.xml中实现了BeanPostProcessor接口的bean，并实例化添加到beanFactory
			 	org.springframework.context.support.AbstractApplicationContext.registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) 
			 	{
			 		org.springframework.context.support.PostProcessorRegistrationDelegate.registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory, AbstractApplicationContext applicationContext)
			 		{
			 			String[] postProcessorNames = beanFactory.getBeanNamesForType(BeanPostProcessor.class, true, false); // 扫描 - 实现BeanPostProcessor接口的bean
			 			// postProcessorNames = [
					 	// 	 org.springframework.aop.config.internalAutoProxyCreator
					 	// ]
			 			beanFactory.addBeanPostProcessor(new BeanPostProcessorChecker(beanFactory, beanProcessorTargetCount));
			 		}
			 	}
			 }
		*/
		
	}

}
