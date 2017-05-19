package cn.java.demo.contexttag.component.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Role;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import cn.java.demo.contexttag.bean.BeanToBeInject;

@Component(value="needAutowireComponent") 
@Scope(scopeName=ConfigurableBeanFactory.SCOPE_SINGLETON,proxyMode=ScopedProxyMode.DEFAULT) 
@Lazy(value=false)
@Primary()
@DependsOn(value={})
@Role(value=BeanDefinition.ROLE_APPLICATION)
@Description(value="this is Description..")
public class NeedAutowireComponent  {
	
	// @Autowired注入机制是：根据参数类型查找容器符合条件的bean，当有多个bean都符合条件时，选择规则是：1、检查有配置primary的bean ； 2、根据bean优先权order   3、根据参数名获取到bean
	
	// -----------通过字段注入--------------------
	@Autowired(required=true)
	public BeanToBeInject beanToBeInject0;
	
	// -----------通过属性注入--------------------
	private BeanToBeInject beanToBeInject1;
	private BeanToBeInject beanToBeInject2;
	private BeanToBeInject beanToBeInject3;
	@Autowired(required=true)
	public void setBeanToBeInject1(BeanToBeInject beanToBeInject1) {
		this.beanToBeInject1 = beanToBeInject1;
	}
	
	@Autowired(required=true)
	public void getBeanToBeInject2(BeanToBeInject beanToBeInject2) {
		this.beanToBeInject2 = beanToBeInject2;
	}
	
	@Autowired(required=true)
	public void method(BeanToBeInject beanToBeInject3) {
		this.beanToBeInject3 = beanToBeInject3;
	}

	@Override
	public String toString() {
		return "NeedAutowireComponent [beanToBeInject0=" + beanToBeInject0 + ", beanToBeInject1=" + beanToBeInject1
				+ ", beanToBeInject2=" + beanToBeInject2 + ", beanToBeInject3=" + beanToBeInject3 + "]";
	}
	
	
	
}
