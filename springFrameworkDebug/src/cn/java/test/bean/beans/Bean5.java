package cn.java.test.bean.beans;

import java.util.Optional;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
//import javax.inject.Provider;
public class Bean5 {
	private String username;
	private Bean1 bean1;
	private Optional optional1;
	private  Bean5  bean5;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Bean1 getBean1() {
		return bean1;
	}

	public void setBean1(Bean1 bean1) {
		this.bean1 = bean1;
	}

	/**
	 * org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(...)
	 * 		org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBeanInstance(...) // 实例化bean对象
	 * 		org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.populateBean(...) // 添加bean对象
	 * 			org.springframework.beans.factory.support.DefaultListableBeanFactory.resolveDependency(...)
	 * @param optional1
	 */
	public void setMethod1(Optional optional1) {
		this.optional1 = optional1;
	}
	
	private ObjectFactory objectFactory;
	public void setMethod2(ObjectFactory objectFactory) {
		this.objectFactory = objectFactory;
	}
	
	public void setMethod3(ObjectProvider objectProvider) {
	}
	
	/**
	 * 根据参数类型，自动识别注入
	 * 		当查找到的bean有多个的时候，选择规则是：1、检查有配置primary的bean ； 2、根据bean优先权   3、根据参数名获取到bean
	 * @param bean5
	 */
	public void setMethod4(Bean5 bean5) {
		this.bean5 = bean5;
	}
	
//	public void setMethod4(Provider provider) {
//	}
}
