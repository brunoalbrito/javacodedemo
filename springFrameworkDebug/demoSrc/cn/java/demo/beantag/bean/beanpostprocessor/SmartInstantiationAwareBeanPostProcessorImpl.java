package cn.java.demo.beantag.bean.beanpostprocessor;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;

import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;

public class SmartInstantiationAwareBeanPostProcessorImpl implements SmartInstantiationAwareBeanPostProcessor {

	/**
	 * 实例化 - 前
	 */
	@Override
	public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
		return null; // 
	}
	
	/**
	 * 实例化 - 后
	 */
	@Override
	public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
		return false;
	}

	/**
	 * 识别出属性后 - 自动装配后
	 */
	@Override
	public PropertyValues postProcessPropertyValues(PropertyValues pvs, PropertyDescriptor[] pds, Object bean,
			String beanName) throws BeansException {
		return pvs;
	}

	/**
	 * 调用初始化方法 - 前
	 */
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	/**
	 * 调用初始化方法 - 后
	 */
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		return bean; 
	}

	@Override
	public Class<?> predictBeanType(Class<?> beanClass, String beanName) throws BeansException {
		return beanClass;
	}

	/**
	 * 决策构造函数
	 */
	@Override
	public Constructor<?>[] determineCandidateConstructors(Class<?> beanClass, String beanName) throws BeansException {
		return null;
	}

	/**
	 * EarlyBean引用
	 */
	@Override
	public Object getEarlyBeanReference(Object bean, String beanName) throws BeansException {
		return bean;
	}

}
