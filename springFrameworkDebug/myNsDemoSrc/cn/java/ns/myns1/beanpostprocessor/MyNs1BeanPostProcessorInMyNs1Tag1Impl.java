package cn.java.ns.myns1.beanpostprocessor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class MyNs1BeanPostProcessorInMyNs1Tag1Impl implements BeanPostProcessor {
	
	private int property1;
	private String property2;
	
	public int getProperty1() {
		return property1;
	}

	public void setProperty1(int property1) {
		this.property1 = property1;
	}

	public String getProperty2() {
		return property2;
	}

	public void setProperty2(String property2) {
		this.property2 = property2;
	}
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
