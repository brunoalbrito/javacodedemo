package cn.java.demo.beantag.bean;

import java.util.Optional;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;

import cn.java.demo.beantag.bean.autowirebytype.FooBeanFactory;
import cn.java.demo.beantag.bean.autowirebytype.FooBeanInjectSelectedByOrder;
import cn.java.demo.beantag.bean.autowirebytype.FooBeanInjectSelectedByPrimary;
import cn.java.demo.beantag.bean.autowirebytype.FooBeanInjectSelectedByQualifier;
import cn.java.demo.beantag.bean.autowirebytype.FooBeanProvider;
import cn.java.demo.beantag.qualifier.MyQualifier0;
import cn.java.demo.beantag.qualifier.MyQualifier1;

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
		System.out.println("----------Optional - 参数类型识别注入，setOptional---------------");
		this.autowireByTypeToBeInjectedBeanOptional = autowireByTypeToBeInjectedBeanOptional; // org.springframework.beans.factory.support.DefaultListableBeanFactory.OptionalDependencyFactory
		try {
			AutowireByTypeToBeInjectedBean autowireByTypeToBeInjectedBean = this.autowireByTypeToBeInjectedBeanOptional.get();
			System.out.println(autowireByTypeToBeInjectedBean.getClass().getName());
			System.out.println(autowireByTypeToBeInjectedBean);
		} catch (Exception e) {
		}
		
	}
	
	/**
	 * 参数类型为：ObjectFactory
	 */
	public void setFooBeanFactoryFactory(ObjectFactory<FooBeanFactory> fooBeanFactoryFactory) { 
		System.out.println("----------ObjectFactory-参数类型识别注入，setFooBeanFactoryFactory---------------");
		this.fooBeanFactoryFactory = fooBeanFactoryFactory; // org.springframework.beans.factory.support.DefaultListableBeanFactory.DependencyObjectProvider
		FooBeanFactory fooBeanFactory = this.fooBeanFactoryFactory.getObject();
		System.out.println(fooBeanFactory.getClass().getName());
		System.out.println(fooBeanFactory);
	}
	
	/**
	 * 参数类型为：ObjectProvider
	 */
	public void setFooBeanProviderProvider(ObjectProvider<FooBeanProvider> fooBeanProviderProvider) { 
		System.out.println("----------ObjectProvider-参数类型识别注入，setFooBeanProviderProvider---------------");
		this.fooBeanProviderProvider = fooBeanProviderProvider; // org.springframework.beans.factory.support.DefaultListableBeanFactory.DependencyObjectProvider
		FooBeanProvider fooBeanProvider = this.fooBeanProviderProvider.getObject();
		System.out.println(fooBeanProvider.getClass().getName());
		System.out.println(fooBeanProvider);
	}

	/**
	 * 参数类型为：ObjectFactory
	 */
	public void setAutowireByTypeToBeInjectedBeanFactory(ObjectFactory<AutowireByTypeToBeInjectedBean> autowireByTypeToBeInjectedBeanFactory) { 
		System.out.println("----------ObjectFactory-参数类型识别注入，setAutowireByTypeToBeInjectedBeanFactory---------------");
		this.autowireByTypeToBeInjectedBeanFactory = autowireByTypeToBeInjectedBeanFactory; // org.springframework.beans.factory.support.DefaultListableBeanFactory.DependencyObjectProvider
		
		AutowireByTypeToBeInjectedBean autowireByTypeToBeInjectedBean = this.autowireByTypeToBeInjectedBeanFactory.getObject();
		System.out.println(autowireByTypeToBeInjectedBean.getClass().getName());
		System.out.println(autowireByTypeToBeInjectedBean);
	}
	
	/**
	 * 参数类型为：ObjectProvider
	 */
	public void setAutowireByTypeToBeInjectedBeanProvider(ObjectProvider<AutowireByTypeToBeInjectedBean> autowireByTypeToBeInjectedBeanProvider) { 
		System.out.println("----------ObjectProvider-参数类型识别注入，setAutowireByTypeToBeInjectedBeanProvider---------------");
		this.autowireByTypeToBeInjectedBeanProvider = autowireByTypeToBeInjectedBeanProvider; // org.springframework.beans.factory.support.DefaultListableBeanFactory.DependencyObjectProvider
		
		AutowireByTypeToBeInjectedBean autowireByTypeToBeInjectedBean = this.autowireByTypeToBeInjectedBeanProvider.getObject();
		System.out.println(autowireByTypeToBeInjectedBean.getClass().getName());
		System.out.println(autowireByTypeToBeInjectedBean);
	}
	
	/**
	 * 自定义类型 - 根据“primary="true"”选择
	 */
	public void setFooBeanInjectSelectedByPrimaryX(FooBeanInjectSelectedByPrimary fooBeanInjectSelectedByPrimary) {
		System.out.println("----------自定义类型-根据“primary=\"true\"”选择---------------");
		System.out.println(fooBeanInjectSelectedByPrimary);
	}
	
	/**
	 * 自定义类型 - 根据“排序规则”选择
	 */
	public void setFooBeanInjectSelectedByOrderX(FooBeanInjectSelectedByOrder fooBeanInjectSelectedByOrder) {
		System.out.println("----------自定义类型-根据“排序规则”选择---------------");
		System.out.println(fooBeanInjectSelectedByOrder);
	}
	
	/**
	 * 自定义类型 - 根据“参数名”选择
	 */
//	public void setFooBeanInjectSelectedByArgNameX(FooBeanInjectSelectedByArgName fooBeanInjectSelectedByArgName0) {
//		System.out.println("----------自定义类型-根据“参数名”选择---------------");
//		System.out.println(fooBeanInjectSelectedByArgName0);
//	}
	
	/**
	 * 自定义类型 - 根据“<qualifier>”选择
	 */
	public void setFooBeanInjectSelectedByQualifierX(@Qualifier(value="fooBeanInjectSelectedByQualifier0") FooBeanInjectSelectedByQualifier fooBeanInjectSelectedByQualifier) {
		System.out.println("----------自定义类型-根据“<qualifier>”选择---------------");
		System.out.println(fooBeanInjectSelectedByQualifier);
	}
	
	/**
	 * 自定义类型 - 根据“<qualifier>”选择
	 */
	public void setFooBeanInjectSelectedByQualifierXX(@MyQualifier1(value="MyQualifier1_fooBeanInjectSelectedByQualifier0") FooBeanInjectSelectedByQualifier fooBeanInjectSelectedByQualifier) {
		System.out.println("----------自定义类型-根据“<qualifier>”注入---------------");
		System.out.println(fooBeanInjectSelectedByQualifier);
	}
	
	/**
	 * 自定义类型 - 根据“<qualifier>”选择
	 */
	public void setFooBeanInjectSelectedByQualifierXXX(@MyQualifier0(value="MyQualifier0_fooBeanInjectSelectedByQualifier0",attribute_key0="attribute_key0_value",attribute_key1="attribute_key1_value") FooBeanInjectSelectedByQualifier fooBeanInjectSelectedByQualifier) {
		System.out.println("----------自定义类型-根据“<qualifier>”注入---------------");
		System.out.println(fooBeanInjectSelectedByQualifier);
	}
	
}
