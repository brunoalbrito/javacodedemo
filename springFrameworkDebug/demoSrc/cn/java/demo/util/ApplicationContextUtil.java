package cn.java.demo.util;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.AbstractRefreshableApplicationContext;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;
import org.springframework.core.PriorityOrdered;

public class ApplicationContextUtil {
	/**
	 * 转成ConfigurableListableBeanFactory类型
	 * 
	 * @param context
	 * @return
	 */
	public static ConfigurableListableBeanFactory getBeanFactoryAndTryCastTypeToConfigurableListableBeanFactory(
			AbstractRefreshableConfigApplicationContext context) {
		if (context instanceof AbstractRefreshableApplicationContext) {
			// org.springframework.beans.factory.support.DefaultListableBeanFactory
			return ((AbstractRefreshableApplicationContext) context).getBeanFactory();
		}
		return null;
	}

	/**
	 * 刷新
	 * @param context
	 */
	public static void refresh(AbstractRefreshableConfigApplicationContext context) {
		if (context instanceof AbstractApplicationContext) {
			((AbstractApplicationContext) context).refresh();
		}
	}


	/**
	 * 转成BeanDefinitionRegistry类型
	 * 
	 * @param context
	 * @return
	 */
	public static BeanDefinitionRegistry getBeanFactoryAndTryCastTypeToBeanDefinitionRegistry(
			AbstractRefreshableConfigApplicationContext context) {
		if (context instanceof AbstractRefreshableApplicationContext) {
			ConfigurableListableBeanFactory beanFactory = ((AbstractRefreshableApplicationContext) context)
					.getBeanFactory();
			if (beanFactory instanceof BeanDefinitionRegistry) { // bean工厂 === RootBeanDefinition的注册中心
				return ((BeanDefinitionRegistry) beanFactory);
			}
		}
		return null;
	}

	/**
	 * 打印所有的BeanDefinition
	 * @param context
	 */
	public static void printBeanDefinitionInRegistry(
				AbstractRefreshableConfigApplicationContext context) {
		
		BeanDefinitionRegistry beanDefinitionRegistry = getBeanFactoryAndTryCastTypeToBeanDefinitionRegistry(context);
		if(beanDefinitionRegistry==null){
			return;
		}
	}
	
	/**
	 * 打印所有的BeanDefinition
	 * @param context
	 */
	public static void printBeanDefinitionInRegistry(
			BeanDefinitionRegistry registry) {
		System.out.println("\n----------已经注册的BeanDefinition--------------");
		String[] beanDefinitionNames = registry.getBeanDefinitionNames();
		for (String beanName : beanDefinitionNames) {
			BeanDefinition beanDefinition = registry.getBeanDefinition(beanName);
			if(beanDefinition instanceof AbstractBeanDefinition){
				AbstractBeanDefinition abstractBeanDefinition = (AbstractBeanDefinition)beanDefinition;
				System.out.println("beanName = "+beanName+" , abstractBeanDefinition.getBeanClassName() == " + abstractBeanDefinition.getBeanClassName());
			}
		}
	}
	
	/**
	 * 用于劫持registry和beanFactory
	 * 实现BeanFactoryPostProcessors接口的bean
	 * @param context
	 */
	public static void printBeanFactoryPostProcessorsInBeanFactory(
			AbstractRefreshableConfigApplicationContext context) {
		ConfigurableListableBeanFactory beanFactory = getBeanFactoryAndTryCastTypeToConfigurableListableBeanFactory(context);
		if(beanFactory==null){
			return;
		}
		printBeanFactoryPostProcessorsInBeanFactory(beanFactory);
	}
	
	/**
	 * 用于劫持registry和beanFactory
	 * 实现BeanFactoryPostProcessors接口的bean
	 * @param context
	 */
	public static void printBeanFactoryPostProcessorsInBeanFactory(
			ConfigurableListableBeanFactory beanFactory) {
		// org.springframework.context.support.PostProcessorRegistrationDelegate.invokeBeanFactoryPostProcessors(...)
		System.out.println("\n----------实现BeanDefinitionRegistryPostProcessor接口的bean（hook）--------------");
		if (beanFactory instanceof BeanDefinitionRegistry) {
			String[] postProcessorNames =
					beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false); // 实现BeanDefinitionRegistryPostProcessor接口的bean
			for (String ppName : postProcessorNames) {
				System.out.println("BeanDefinitionRegistryPostProcessor-postProcessorName = " + ppName);
				// BeanDefinitionRegistryPostProcessor postProcessor = beanFactory.getBean(ppName, BeanDefinitionRegistryPostProcessor.class);
				// postProcessor.postProcessBeanDefinitionRegistry(registry);
			}
		}
		
		String[] postProcessorNames =
				beanFactory.getBeanNamesForType(BeanFactoryPostProcessor.class, true, false); // 实现BeanFactoryPostProcessor接口的bean
		System.out.println("\n----------实现BeanFactoryPostProcessor接口的bean（hook）--------------");
		for (String ppName : postProcessorNames) {
			System.out.println("BeanFactoryPostProcessor-postProcessorName = " + ppName);
			// BeanFactoryPostProcessor postProcessor = beanFactory.getBean(ppName, BeanFactoryPostProcessor.class);
			// postProcessor.postProcessBeanFactory(beanFactory);
		}
	}
	
	/**
	 * 用于劫持bean
	 * 实现BeanPostProcessor接口的bean
	 * @param context
	 */
	public static void printBeanPostProcessorInBeanFactory(
			AbstractRefreshableConfigApplicationContext context) {
		ConfigurableListableBeanFactory beanFactory = getBeanFactoryAndTryCastTypeToConfigurableListableBeanFactory(context);
		if(beanFactory==null){
			return;
		}
		// org.springframework.context.support.PostProcessorRegistrationDelegate.registerBeanPostProcessors(...)
		printBeanPostProcessorInBeanFactory(beanFactory);
	}
	
	/**
	 * 用于劫持bean
	 * 实现BeanPostProcessor接口的bean
	 * @param context
	 */
	public static void printBeanPostProcessorInBeanFactory(
				ConfigurableListableBeanFactory beanFactory) {

		System.out.println("\n----------实现BeanPostProcessor接口的bean（hook）--------------");
		// beanFactory.getBeanPostProcessorCount()
		String[] postProcessorNames = beanFactory.getBeanNamesForType(BeanPostProcessor.class, true, false); // 查找applicationContext.xml中配置的，实现BeanPostProcessor接口的bean
		for (String ppName : postProcessorNames) {
			System.out.println("BeanPostProcessor-postProcessorName = " + ppName);
			// BeanPostProcessor postProcessor = beanFactory.getBean(ppName, BeanPostProcessor.class);
			// beanFactory.addBeanPostProcessor(postProcessor);
		}
	}

}
