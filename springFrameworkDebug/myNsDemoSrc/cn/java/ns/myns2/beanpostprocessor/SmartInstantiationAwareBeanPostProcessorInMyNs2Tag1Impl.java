package cn.java.ns.myns2.beanpostprocessor;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;

import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;

public class SmartInstantiationAwareBeanPostProcessorInMyNs2Tag1Impl implements SmartInstantiationAwareBeanPostProcessor {

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
	 * 实例化 - 前
	 */
	@Override
	public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
		System.out.println("this is "+this.getClass().getSimpleName()+":postProcessBeforeInstantiation() : beanName = "+ beanName +" , beanClass.getName() = "+beanClass.getName());
		return null; // 
	}
	
	/**
	 * 实例化 - 后
	 */
	@Override
	public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
		System.out.println("this is "+this.getClass().getSimpleName()+":postProcessAfterInstantiation() : beanName = "+ beanName +" , bean.getClass().getName() = "+bean.getClass().getName());
		return false;
	}

	/**
	 * 识别出属性后 - 自动装配后
	 */
	@Override
	public PropertyValues postProcessPropertyValues(PropertyValues pvs, PropertyDescriptor[] pds, Object bean,
			String beanName) throws BeansException {
		System.out.println("this is "+this.getClass().getSimpleName()+":postProcessPropertyValues() : beanName = "+ beanName +" , bean.getClass().getName() = "+bean.getClass().getName());
		return pvs;
	}

	/**
	 * 调用初始化方法 - 前
	 */
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		System.out.println("this is "+this.getClass().getSimpleName()+":postProcessBeforeInitialization() : beanName = "+ beanName +" , bean.getClass().getName() = "+bean.getClass().getName());
		return bean;
	}

	/**
	 * 调用初始化方法 - 后
	 */
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		System.out.println("this is "+this.getClass().getSimpleName()+":postProcessAfterInitialization() : beanName = "+ beanName +" , bean.getClass().getName() = "+bean.getClass().getName());
		return bean; 
	}

	
	/**
	 * 没有匹配到bean，用本方法进行预测
	 */
	@Override
	public Class<?> predictBeanType(Class<?> beanClass, String beanName) throws BeansException {
		System.out.println("this is "+this.getClass().getSimpleName()+":predictBeanType() : beanName = "+ beanName +" , beanClass.getName() = "+beanClass.getName());
		return beanClass;
	}

	/**
	 * 决策构造函数
	 */
	@Override
	public Constructor<?>[] determineCandidateConstructors(Class<?> beanClass, String beanName) throws BeansException {
		System.out.println("this is "+this.getClass().getSimpleName()+":determineCandidateConstructors() : beanName = "+ beanName +" , beanClass.getName() = "+beanClass.getName());
		return null;
	}

	/**
	 * EarlyBean引用
	 */
	@Override
	public Object getEarlyBeanReference(Object bean, String beanName) throws BeansException {
		System.out.println("this is "+this.getClass().getSimpleName()+":postProcessBeforeInitialization() : beanName = "+ beanName +" , bean.getClass().getName() = "+bean.getClass().getName());
		return bean;
	}

}
