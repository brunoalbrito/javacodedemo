package cn.java.demo.beantag.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;

public class NeedAwareBean implements BeanNameAware,BeanClassLoaderAware,BeanFactoryAware {

	private String beanName;
	private ClassLoader classLoader;
	private BeanFactory beanFactory;
	
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		// beanFactory === org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.this
		// beanFactory === org.springframework.beans.factory.support.DefaultListableBeanFactory
		this.beanFactory = beanFactory;
	}

	@Override
	public void setBeanClassLoader(ClassLoader classLoader) {
		// Thread.currentThread().getContextClassLoader()
		this.classLoader = classLoader;
	}
	
	@Override
	public void setBeanName(String name) {
		this.beanName = name;
	}
	
	public void testMethod() {
		System.out.println(this.getClass().getName()+":testMethod()");
		System.out.println("beanName : " + this.beanName + " , classLoader : " + this.classLoader.getClass().getName() + " , beanFactory : " + this.beanFactory.getClass().getName());
		FooBean fooBean = this.beanFactory.getBean("fooBean", FooBean.class);
		System.out.println(fooBean.toString());
	}
}
