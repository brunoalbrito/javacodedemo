package cn.java.beannote.scope;

import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.Scope;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

public class 自定义Scope的注入机制 {

	public static void main(String[] args) {
		// 保留的scope是：singleton、prototype
		/*
		  org.springframework.context.support.AbstractApplicationContext.refresh()
		  {
			  org.springframework.context.support.AbstractApplicationContext.invokeBeanFactoryPostProcessors(...)
			  {
			  	org.springframework.context.support.PostProcessorRegistrationDelegate.invokeBeanFactoryPostProcessors(...)
			 	{
			 		for (BeanFactoryPostProcessor postProcessor : postProcessors)  // postProcessor === org.springframework.beans.factory.config.CustomScopeConfigurer
			 		{
			 			org.springframework.beans.factory.config.CustomScopeConfigurer.postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
			 			{
			 				if (this.scopes != null) {
								for (Map.Entry<String, Object> entry : this.scopes.entrySet()) {
									String scopeKey = entry.getKey();
									Object value = entry.getValue();
									if (value instanceof Scope) {
										beanFactory.registerScope(scopeKey, (Scope) value); // 注册自己的scope，被保留的scope是：singleton、prototype
									}
									else if (value instanceof Class) {
										Class<?> scopeClass = (Class<?>) value;
										Assert.isAssignable(Scope.class, scopeClass);
										beanFactory.registerScope(scopeKey, (Scope) BeanUtils.instantiateClass(scopeClass));
									}
									else if (value instanceof String) {
										Class<?> scopeClass = ClassUtils.resolveClassName((String) value, this.beanClassLoader);
										Assert.isAssignable(Scope.class, scopeClass);
										beanFactory.registerScope(scopeKey, (Scope) BeanUtils.instantiateClass(scopeClass));
									}
									else {
										throw new IllegalArgumentException("Mapped value [" + value + "] for scope key [" +
												scopeKey + "] is not an instance of required type [" + Scope.class.getName() +
												"] or a corresponding Class or String value indicating a Scope implementation");
									}
								}
							}
			 			}
			 		}
			 	}
			  }
		  }
		 	
		 */
	}

}
