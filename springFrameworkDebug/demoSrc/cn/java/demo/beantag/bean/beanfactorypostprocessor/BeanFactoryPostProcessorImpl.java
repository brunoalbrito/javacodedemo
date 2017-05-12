package cn.java.demo.beantag.bean.beanfactorypostprocessor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import cn.java.demo.util.ApplicationContextUtil;

public class BeanFactoryPostProcessorImpl implements BeanFactoryPostProcessor {

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		System.out.println("hook beanFactory ---> " + this.getClass().getSimpleName()+":postProcessBeanFactory() - bof");
		ApplicationContextUtil.printBeanFactoryPostProcessorsInBeanFactory(beanFactory);
		ApplicationContextUtil.printBeanPostProcessorInBeanFactory(beanFactory);
		System.out.println("hook beanFactory ---> " + this.getClass().getSimpleName()+":postProcessBeanFactory() - eof");
	}

}
