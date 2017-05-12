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
		 	5、执行registry级别的hook（ConfigurationClassPostProcessor），迭代所有BeanDefinition，识别注解，根据注解信息做相关操作
		 	6、获取bean的过程，会执行bean级别的hook（AutowiredAnnotationBeanPostProcessor....），识别@Autowired注解，进行注入，
		 			@Autowired注入机制是：根据参数类型查找容器符合条件的bean，当有多个bean都符合条件时，选择规则是：1、检查有配置primary的bean ； 2、根据bean优先权   3、根据参数名获取到bean
		 		在字段上的注解 ---> org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor.AutowiredFieldElement.inject(...)
		 		在方法上的注解 ---> org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor.AutowiredMethodElement.inject(...)
		 	7、获取bean的过程，会执行bean级别的hook（RequiredAnnotationBeanPostProcessor....），只是起依赖起检查作用
		 	8、获取bean的过程，会执行bean级别的hook（CommonAnnotationBeanPostProcessor....），识别@WebServiceRef、@EJB、@Resource注解，进行注入，
		 		@WebServiceRef ---> org.springframework.context.annotation.CommonAnnotationBeanPostProcessor.WebServiceRefElement.inject(...)
		 		@EJB ---> org.springframework.context.annotation.CommonAnnotationBeanPostProcessor.EjbRefElement.inject(...)
		 		@Resource ---> org.springframework.context.annotation.CommonAnnotationBeanPostProcessor.ResourceElement.inject(...)
		 	9、获取bean的过程，会执行bean级别的hook（PersistenceAnnotationBeanPostProcessor....），识别@Entity注解，进行注入
		 	
		 	
		 	
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
		 	
		 	----------BeanPostProcessor------------
		 	org.springframework.context.annotation.ConfigurationClassPostProcessor.ImportAwareBeanPostProcessor，hook - 劫持bean
	 */
	}
}
