package cn.java.contextnote;

import org.springframework.beans.factory.config.BeanDefinition;

public class Debug {
	public static void main(String[] args) {
		/*
		 	http\://www.springframework.org/schema/context=org.springframework.context.config.ContextNamespaceHandler
			http\://www.springframework.org/schema/jee=org.springframework.ejb.config.JeeNamespaceHandler
			http\://www.springframework.org/schema/lang=org.springframework.scripting.config.LangNamespaceHandler
			http\://www.springframework.org/schema/task=org.springframework.scheduling.config.TaskNamespaceHandler
			http\://www.springframework.org/schema/cache=org.springframework.cache.config.CacheNamespaceHandler
		 */
		
		/*
		 	<context:component-scan base-package="cn.java.note.contexttag.bean" />
		 	
		 	org.springframework.context.config.ContextNamespaceHandler.parse(...)
		 	{
		 		org.springframework.context.annotation.ComponentScanBeanDefinitionParser.parse(...)
		 		{
		 		
		 		}
		 	}
		 	
		 	1、用 <context:include-filter>、<context:exclude-filter>过滤掉
		 	2、迭代符合条件的Component、获取Scope名、生成beanName名
		 	3、注册BeanDefinition到容器中
		 	4、注册一些内置的BeanDefinition
		 	
		 	
		 	
		 */
		
		/*
		 	RootBeanDefinition = { // beanName是“org.springframework.context.annotation.internalConfigurationAnnotationProcessor”，hook - 劫持BeanFactory
		 		beanClass : org.springframework.context.annotation.ConfigurationClassPostProcessor.class,
		 		role : BeanDefinition.ROLE_INFRASTRUCTURE
		 	}
		 	
		 	RootBeanDefinition = { // beanName是“org.springframework.context.annotation.internalAutowiredAnnotationProcessor”，hook - 劫持bean
		 		beanClass : org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor.class,
		 		role : BeanDefinition.ROLE_INFRASTRUCTURE
		 	}
		 	
		 	RootBeanDefinition = { // beanName是“org.springframework.context.annotation.internalRequiredAnnotationProcessor”，hook - 劫持bean
		 		beanClass : org.springframework.beans.factory.annotation.RequiredAnnotationBeanPostProcessor.class,
		 		role : BeanDefinition.ROLE_INFRASTRUCTURE
		 	}
		 	RootBeanDefinition = { // beanName是“org.springframework.context.annotation.internalCommonAnnotationProcessor”  // JSR-250 support ，hook - 劫持bean
		 		beanClass : org.springframework.beans.factory.annotation.CommonAnnotationBeanPostProcessor.class,
		 		role : BeanDefinition.ROLE_INFRASTRUCTURE
		 	}
		 	RootBeanDefinition = { // beanName是“org.springframework.context.annotation.internalPersistenceAnnotationProcessor”  // JPA support
		 		beanClass : org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor.class,
		 		role : BeanDefinition.ROLE_INFRASTRUCTURE
		 	}
		 	RootBeanDefinition = { // beanName是“org.springframework.context.event.internalEventListenerProcessor” 
		 		beanClass : org.springframework.context.event.EventListenerMethodProcessor.class,
		 		role : BeanDefinition.ROLE_INFRASTRUCTURE
		 	}
		 	RootBeanDefinition = { // beanName是“org.springframework.context.event.internalEventListenerFactory”  
		 		beanClass : org.springframework.context.event.DefaultEventListenerFactory.class,
		 		role : BeanDefinition.ROLE_INFRASTRUCTURE
		 	}
	 */
	}
}
