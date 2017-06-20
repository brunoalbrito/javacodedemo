package cn.java.demo.beantag.bean_factory_post_processor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.CustomAutowireConfigurer;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

/**
 * 劫持BeanFactory
 * @author zhouzhian
 *
 */
public class CustomBeanFactoryConfigurer extends CustomAutowireConfigurer {

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		
		/*
		 	里面有对<qualifier>标签功能的支持
		 	机制是：
		 		1、autowire-candidate="true"
				2、目标bean中的setter方法上有存在注解，并且在<qualifier>支持的类型中存在
		 */
		super.postProcessBeanFactory(beanFactory);
		
		// 权重比较器
		if(beanFactory instanceof DefaultListableBeanFactory){
			DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory)beanFactory;
			{
				defaultListableBeanFactory.setDependencyComparator(new PriorityOrderComparator()); // 在byType类型匹配的时候，命中多个bean的时候，支持“order”选择机制
			}
			
			// 
//			{
//				defaultListableBeanFactory.setAutowireCandidateResolver(new SimpleAutowireCandidateResolverExtend());  // 在byType类型匹配的时候，命中多个bean的时候，支持“变量名”选择机制
//			}
		}
		
	}

}
