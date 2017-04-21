package cn.java.beannote.后置处理器_hook机制;

import java.lang.reflect.Constructor;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.util.ObjectUtils;

public class 关于bean后置处理器_配置方式 {

	public static void main(String[] args) {
		/*
		
			// 关于BeanPostProcessors的注入机制
			 org.springframework.context.support.AbstractApplicationContext.refresh()
			 {
			 	org.springframework.context.support.AbstractApplicationContext.prepareBeanFactory(ConfigurableListableBeanFactory beanFactory) // 添加默认的 BeanPostProcessor
			 	{
			 		beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this)); // this === org.springframework.web.context.support.XmlWebApplicationContext
			 		...
			 		beanFactory.addBeanPostProcessor(new ApplicationListenerDetector(this)); // this === org.springframework.web.context.support.XmlWebApplicationContext
			 	}
			 	...
			 	org.springframework.context.support.AbstractApplicationContext.registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) // 注册 applicationContext.xml中配置的BeanPostProcessor（实现BeanPostProcessor接口的bean）
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
