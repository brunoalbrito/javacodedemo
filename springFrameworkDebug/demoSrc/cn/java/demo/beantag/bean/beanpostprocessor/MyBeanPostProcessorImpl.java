package cn.java.demo.beantag.bean.beanpostprocessor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class MyBeanPostProcessorImpl implements BeanPostProcessor {

	/**
	 * 执行bean的“初始化”方法“前”调用
	 */
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		System.out.println("this is "+this.getClass().getSimpleName()+":postProcessBeforeInitialization() : beanName = "+ beanName +" , bean.getClass().getName() = "+bean.getClass().getName());
		return bean;
	}

	/**
	 * 执行bean的“初始化”方法“后”调用
	 */
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		System.out.println("this is "+this.getClass().getSimpleName()+":postProcessAfterInitialization() : beanName = "+ beanName +" , bean.getClass().getName() = "+bean.getClass().getName());
		return bean;
	}

}
