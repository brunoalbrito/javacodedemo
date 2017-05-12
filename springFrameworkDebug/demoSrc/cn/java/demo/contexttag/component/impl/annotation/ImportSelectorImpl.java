package cn.java.demo.contexttag.component.impl.annotation;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.ResolvableType;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 支持注入感知对象 : BeanClassLoaderAware,BeanFactoryAware,BeanFactory,EnvironmentAware,ResourceLoaderAware
 * @author zhouzhian
 *
 */
public class ImportSelectorImpl implements ImportSelector,BeanClassLoaderAware,BeanFactoryAware,BeanFactory,EnvironmentAware,ResourceLoaderAware {

	/**
	 * @Import 注解的处理类
	 */
	@Override
	public String[] selectImports(AnnotationMetadata importingClassMetadata) {
		return null;
	}
	
	
	// ---------------感知注入 bof---------------------------
	@Override
	public void setBeanClassLoader(ClassLoader classLoader) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setEnvironment(Environment environment) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getBean(String name) throws BeansException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T getBean(Class<T> requiredType) throws BeansException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getBean(String name, Object... args) throws BeansException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T getBean(Class<T> requiredType, Object... args) throws BeansException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean containsBean(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isPrototype(String name) throws NoSuchBeanDefinitionException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isTypeMatch(String name, ResolvableType typeToMatch) throws NoSuchBeanDefinitionException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isTypeMatch(String name, Class<?> typeToMatch) throws NoSuchBeanDefinitionException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Class<?> getType(String name) throws NoSuchBeanDefinitionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getAliases(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		// TODO Auto-generated method stub
		
	}
	
	// ---------------感知注入eof---------------------------

}
