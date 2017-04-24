package cn.java.demo.util;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.AbstractRefreshableApplicationContext;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;

public class ApplicationContextUtil {
	/**
	 * 转成ConfigurableListableBeanFactory类型
	 * 
	 * @param context
	 * @return
	 */
	public static ConfigurableListableBeanFactory tryCastTypeToConfigurableListableBeanFactory(
			AbstractRefreshableConfigApplicationContext context) {
		if (context instanceof AbstractRefreshableApplicationContext) {
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
	public static BeanDefinitionRegistry tryCastTypeToBeanDefinitionRegistry(
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
}
