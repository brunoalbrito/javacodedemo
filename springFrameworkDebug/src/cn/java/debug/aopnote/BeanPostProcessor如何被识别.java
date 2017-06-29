package cn.java.debug.aopnote;

public class BeanPostProcessor如何被识别 {
	
	public static void 切入机制(String[] args) {
		/*
			*************** 使用“后置处理器 BeanPostProcessor”作为切入渠道 ****************** 
			aop的BeanPostProcessors：
				org.springframework.aop.aspectj.autoproxy.AspectJAwareAdvisorAutoProxyCreator(proxyTargetClass=false; optimize=false; opaque=false; exposeProxy=false; frozen=false)
			
			
			关于bean如何被aop支持：
		 		在解析xml文件的时候，识别到指定标签的“命名空间”
		 		根据“命名空间”获取“命名空间处理器”
		 		使用“命名空间处理器”解析，“命名空间处理器”根据标签类型调用“标签解析器”进行解析
		 		“<aop:config>标签解析器”解析：会内部注册一个“实现BeanPostProcessors接口的bean”
		 		Spring核心会自动扫描applicationContex.xml中“实现BeanPostProcessors接口的bean”，添加为beanFactory.addBeanPostProcessor(...),至此bean被aop支持了
		 	
		 	关于aop如何起作用：	
		 		用户调用getBean()获取bean
		 		Spring核心实例化“bean对象”
		 		Spring填充“bean对象”的属性（根据属性名称、根据参数类型[三种决策方案]）
		 		Spring检查“bean对象”实现的感知接口，注入感知
		 		Spring迭代调用beanProcessor.postProcessBeforeInitialization(result, beanName)
		 		Spring调用“bean对象”的“初始化方法”
		 		Spring迭代调用beanProcessor.postProcessAfterInitialization(result, beanName)，aop就在这边做的手脚
		 		
		 	org.springframework.beans.factory.xml.DefaultBeanDefinitionDocumentReader.parseBeanDefinitions(...)
		 	{
		 		org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.parseCustomElement(...)
		 		{
		 			org.springframework.aop.config.AopNamespaceHandler.parse(ele, new ParserContext(this.readerContext, this, null)); // <aop:xxx>
		 			{
		 				org.springframework.aop.config.ConfigBeanDefinitionParser.parse(Element element, ParserContext parserContext); // <aop:config>
		 				org.springframework.aop.config.AspectJAutoProxyBeanDefinitionParser.parse(Element element, ParserContext parserContext);  // <aop:aspectj-autoproxy>
		 				org.springframework.aop.config.ScopedProxyBeanDefinitionDecorator.parse(Element element, ParserContext parserContext); // <aop:scoped-proxy>
		 				org.springframework.aop.config.SpringConfiguredBeanDefinitionParser.parse(Element element, ParserContext parserContext);  // <aop:spring-configured>
		 			}
		 		}
		 	}
		 		
		 			
			 
			 //　每个对象实例化后，都会应用一下如下的BeanPostProcessors（在bean感知注入后，在初始化方法被调用前后）
			 getBeanPostProcessors() = {
				0 : org.springframework.context.support.ApplicationContextAwareProcessor, // 注入感知
				1 : org.springframework.context.support.PostProcessorRegistrationDelegate.BeanPostProcessorChecker
				2 : org.springframework.aop.aspectj.autoproxy.AspectJAwareAdvisorAutoProxyCreator(proxyTargetClass=false; optimize=false; opaque=false; exposeProxy=false; frozen=false),
				3 : org.springframework.context.support.ApplicationListenerDetector // 注入感知
			 }
			 
			 // 关于BeanPostProcessors的注入机制
			 org.springframework.context.support.AbstractApplicationContext.refresh()
			 {
			 	org.springframework.context.support.AbstractApplicationContext.prepareBeanFactory(ConfigurableListableBeanFactory beanFactory) // 添加默认的 BeanPostProcessor
			 	{
			 		beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this)); // this === org.springframework.web.context.support.XmlWebApplicationContext
			 		...
			 		beanFactory.addBeanPostProcessor(new ApplicationListenerDetector(this)); // this === org.springframework.web.context.support.XmlWebApplicationContext
			 	}
			 	...
			 	org.springframework.context.support.AbstractApplicationContext.registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) // 注册 applicationContext.xml中配置的BeanPostProcessor（实现BeanPostProcessor接口的bean）
			 	{
			 		org.springframework.context.support.PostProcessorRegistrationDelegate.registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory, AbstractApplicationContext applicationContext)
			 		{
			 			String[] postProcessorNames = beanFactory.getBeanNamesForType(BeanPostProcessor.class, true, false); // 扫描 - 实现BeanPostProcessor接口的bean
			 			// postProcessorNames = [
					 	// 	 org.springframework.aop.config.internalAutoProxyCreator
					 	// ]
			 			beanFactory.addBeanPostProcessor(new BeanPostProcessorChecker(beanFactory, beanProcessorTargetCount));
			 		}
			 	}
			 }
			 
			 // 关于bean被代理劫持的机制
			 org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(String beanName, RootBeanDefinition mbd, Object[] args)
			 {
			 	...
 				populateBean(beanName, mbd, instanceWrapper);//!!! 填充bean !!!
				if (exposedObject != null) {
				 	// org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.initializeBean(String beanName, Object bean, RootBeanDefinition mbd)
					exposedObject = initializeBean(beanName, exposedObject, mbd); // 调用bean的感知方法、调用bean的初始化方法、在这里面可能创建代理对象（AOP就是在这边做劫持的，进行代理包装）
					{
						...
						invokeAwareMethods(beanName, bean); // 给bean对注入感知对象
						...
						if (mbd == null || !mbd.isSynthetic()) { // mbd 不是合成的
							wrappedBean = applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName);
							{
								Object result = existingBean;
								for (BeanPostProcessor beanProcessor : getBeanPostProcessors()) {
									result = beanProcessor.postProcessBeforeInitialization(result, beanName);
									if (result == null) {
										return result;
									}
								}
								return result;
							}
						}
						
						...
						invokeInitMethods(beanName, wrappedBean, mbd); // 调用bean的初始化方法
						...
						
						if (mbd == null || !mbd.isSynthetic()) {
							wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
							{
								Object result = existingBean;
								for (BeanPostProcessor beanProcessor : getBeanPostProcessors()) {
									result = beanProcessor.postProcessAfterInitialization(result, beanName);
									if (result == null) {
										return result;
									}
								}
								return result;
							}
						}
					}
				}
				...
			 }
			 
		 */
	}
}
