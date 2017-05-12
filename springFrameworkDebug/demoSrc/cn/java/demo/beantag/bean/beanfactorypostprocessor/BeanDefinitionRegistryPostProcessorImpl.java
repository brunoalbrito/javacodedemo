package cn.java.demo.beantag.bean.beanfactorypostprocessor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

import cn.java.demo.util.ApplicationContextUtil;

public class BeanDefinitionRegistryPostProcessorImpl implements BeanDefinitionRegistryPostProcessor {

	

	/**
	 * 
	 */
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		System.out.println("hook beanFactory ---> " + this.getClass().getSimpleName()+":postProcessBeanFactory() - bof");
		ApplicationContextUtil.printBeanFactoryPostProcessorsInBeanFactory(beanFactory);
		ApplicationContextUtil.printBeanPostProcessorInBeanFactory(beanFactory);
		System.out.println("hook beanFactory ---> " + this.getClass().getSimpleName()+":postProcessBeanFactory() - eof");
	}

	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
		System.out.println("hook registry ---> " + this.getClass().getSimpleName()+":postProcessBeanDefinitionRegistry() - bof");
		ApplicationContextUtil.printBeanDefinitionInRegistry(registry);
		System.out.println("hook registry ---> " + this.getClass().getSimpleName()+":postProcessBeanDefinitionRegistry() - eof");
	}

}
