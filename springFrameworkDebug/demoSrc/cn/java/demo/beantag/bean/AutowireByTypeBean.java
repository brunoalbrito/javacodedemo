package cn.java.demo.beantag.bean;

import java.util.Optional;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
//import javax.inject.Provider;

import cn.java.demo.beantag.bean.autowirebytype.FooBeanFactory;
import cn.java.demo.beantag.bean.autowirebytype.FooBeanProvider;

/**
 * 当根据参数类型查找到的bean有多个的时候，决策规则是：
 * 		1、检查有配置primary的bean，有且只能有一个设置为primary的bean； 
 * 		2、根据bean优先权，不能有两个一样优先权的bean   
 * 		3、根据参数名获取到bean
 * org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(...)
 * 		org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBeanInstance(...) // 实例化bean对象
 * 		org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.populateBean(...) // 添加bean对象
 * 			org.springframework.beans.factory.support.DefaultListableBeanFactory.resolveDependency(...)
 * @author zhouzhian
 */
public class AutowireByTypeBean {
	
	// 如下通过xml文件配置注入
	private String username;
	private FooBean fooBean;
	
	// 如下通过setter参数类型识别注入
	private ObjectFactory<FooBeanFactory> fooBeanFactoryFactory;
	private ObjectFactory<AutowireByTypeToBeInjectedBean> autowireByTypeToBeInjectedBeanFactory;
	private ObjectProvider<FooBeanProvider> fooBeanProviderProvider;
	private ObjectProvider<AutowireByTypeToBeInjectedBean> autowireByTypeToBeInjectedBeanProvider;
	private AutowireByTypeToBeInjectedBean autowireByTypeToBeInjectedBean;
	private Optional<AutowireByTypeToBeInjectedBean> autowireByTypeToBeInjectedBeanOptional;
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public FooBean getFooBean() {
		return fooBean;
	}

	public void setFooBean(FooBean fooBean) {
		this.fooBean = fooBean;
	}
	
	/**
	 * 参数类型为：Optional
	 */
	public void setBeanOptional(Optional<AutowireByTypeToBeInjectedBean> autowireByTypeToBeInjectedBeanOptional) {
		System.out.println("----------参数类型识别注入，setOptional---------------");
		this.autowireByTypeToBeInjectedBeanOptional = autowireByTypeToBeInjectedBeanOptional;
		try {
			AutowireByTypeToBeInjectedBean autowireByTypeToBeInjectedBean = this.autowireByTypeToBeInjectedBeanOptional.get();
			System.out.println(autowireByTypeToBeInjectedBean.getClass().getName());
		} catch (Exception e) {
		}
		
	}
	
	/**
	 * 参数类型为：ObjectFactory
	 */
	public void setFooBeanFactoryFactory(ObjectFactory<FooBeanFactory> fooBeanFactoryFactory) { 
		System.out.println("----------参数类型识别注入，setFooBeanFactoryFactory---------------");
		this.fooBeanFactoryFactory = fooBeanFactoryFactory; // org.springframework.beans.factory.support.DefaultListableBeanFactory.DependencyObjectProvider
		FooBeanFactory fooBeanFactory = this.fooBeanFactoryFactory.getObject();
		System.out.println(fooBeanFactory.getClass().getName());
	}
	
	/**
	 * 参数类型为：ObjectProvider
	 */
	public void setFooBeanProviderProvider(ObjectProvider<FooBeanProvider> fooBeanProviderProvider) { 
		System.out.println("----------参数类型识别注入，setFooBeanProviderProvider---------------");
		this.fooBeanProviderProvider = fooBeanProviderProvider; // org.springframework.beans.factory.support.DefaultListableBeanFactory.DependencyObjectProvider
		FooBeanProvider fooBeanProvider = this.fooBeanProviderProvider.getObject();
		System.out.println(fooBeanProvider.getClass().getName());
	}

	/**
	 * 参数类型为：ObjectFactory
	 */
	public void setAutowireByTypeToBeInjectedBeanFactory(ObjectFactory<AutowireByTypeToBeInjectedBean> autowireByTypeToBeInjectedBeanFactory) { 
		System.out.println("----------参数类型识别注入，setAutowireByTypeToBeInjectedBeanFactory---------------");
		this.autowireByTypeToBeInjectedBeanFactory = autowireByTypeToBeInjectedBeanFactory;
		
		AutowireByTypeToBeInjectedBean autowireByTypeToBeInjectedBean = this.autowireByTypeToBeInjectedBeanFactory.getObject();
		System.out.println(autowireByTypeToBeInjectedBean.getClass().getName());
	}
	
	/**
	 * 参数类型为：ObjectProvider
	 */
	public void setAutowireByTypeToBeInjectedBeanProvider(ObjectProvider<AutowireByTypeToBeInjectedBean> autowireByTypeToBeInjectedBeanProvider) { 
		System.out.println("----------参数类型识别注入，setAutowireByTypeToBeInjectedBeanProvider---------------");
		this.autowireByTypeToBeInjectedBeanProvider = autowireByTypeToBeInjectedBeanProvider; // org.springframework.beans.factory.support.DefaultListableBeanFactory.DependencyObjectProvider
		
		AutowireByTypeToBeInjectedBean autowireByTypeToBeInjectedBean = this.autowireByTypeToBeInjectedBeanProvider.getObject();
		System.out.println(autowireByTypeToBeInjectedBean.getClass().getName());
	}
	
	/**
	 * 参数类型为：自定义类型
	 */
	public void setAutowireByTypeToBeInjectedBean(AutowireByTypeToBeInjectedBean autowireByTypeToBeInjectedBean) {
		System.out.println("----------参数类型识别注入，setAutowireByTypeToBeInjectedBean---------------");
		this.autowireByTypeToBeInjectedBean = autowireByTypeToBeInjectedBean;
		System.out.println(autowireByTypeToBeInjectedBean.getClass().getName());
	}
	
	
}
