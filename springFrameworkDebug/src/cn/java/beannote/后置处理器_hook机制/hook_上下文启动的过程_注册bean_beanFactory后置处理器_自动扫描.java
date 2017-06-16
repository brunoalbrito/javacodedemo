package cn.java.beannote.后置处理器_hook机制;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.Aware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.Lifecycle;
import org.springframework.context.LifecycleProcessor;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.Phased;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.context.support.DefaultLifecycleProcessor;
import org.springframework.context.support.LiveBeansView;
import org.springframework.context.weaving.LoadTimeWeaverAware;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.StringValueResolver;

public class hook_上下文启动的过程_注册bean_beanFactory后置处理器_自动扫描 {

	public static void main(String[] args) {
		/*
		
			  《****** 关于hook的注入机制 ******》
			 org.springframework.context.support.ClassPathXmlApplicationContext
			 org.springframework.web.context.support.XmlWebApplicationContext
			 
			 org.springframework.context.support.AbstractApplicationContext.refresh()
			 {
			 
			 	AbstractApplicationContext.prepareRefresh(); // ------1
			 	{
			 		// Initialize any placeholder property sources in the context environment
					initPropertySources(); // 第二次初始化属性源，org.springframework.web.context.support.AbstractRefreshableWebApplicationContext
			
					// Validate that all properties marked as required are resolvable
					// see ConfigurablePropertyResolver#setRequiredProperties
					getEnvironment().validateRequiredProperties(); // 检查需要的属性
			 	}
			 	
			 	ConfigurableListableBeanFactory beanFactory = AbstractApplicationContext.obtainFreshBeanFactory(); // 创建bean工厂，加载bean的定义文件   ------2
			 	{
			 		refreshBeanFactory(); // 创建bean工厂,加载bean的定义文件 applicationContext.xml
					ConfigurableListableBeanFactory beanFactory = getBeanFactory();
					return beanFactory;
			 	}
			 
			 	// ----------添加默认的 BeanPostProcessor
			 	AbstractApplicationContext.prepareBeanFactory(ConfigurableListableBeanFactory beanFactory) // 添加默认的 BeanPostProcessor   ------3
			 	{
			 		// this === org.springframework.web.context.support.XmlWebApplicationContext  《系统默认》
			 		beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this){
			 			public Object postProcessBeforeInitialization(final Object bean, String beanName) throws BeansException { // 注册感知接口
			 				ApplicationContextAwareProcessor.invokeAwareInterfaces(bean);
			 				{
				 				if (bean instanceof Aware) {
									if (bean instanceof EnvironmentAware) {
										((EnvironmentAware) bean).setEnvironment(this.applicationContext.getEnvironment());
									}
									if (bean instanceof EmbeddedValueResolverAware) {
										((EmbeddedValueResolverAware) bean).setEmbeddedValueResolver(this.embeddedValueResolver);
									}
									if (bean instanceof ResourceLoaderAware) {
										((ResourceLoaderAware) bean).setResourceLoader(this.applicationContext);
									}
									if (bean instanceof ApplicationEventPublisherAware) {
										((ApplicationEventPublisherAware) bean).setApplicationEventPublisher(this.applicationContext);
									}
									if (bean instanceof MessageSourceAware) {
										((MessageSourceAware) bean).setMessageSource(this.applicationContext);
									}
									if (bean instanceof ApplicationContextAware) {
										((ApplicationContextAware) bean).setApplicationContext(this.applicationContext);
									}
								}
			 				}
			 				return bean;
			 			}
			 		}); 
			 		...
			 		beanFactory.addBeanPostProcessor(new ApplicationListenerDetector(this){
				 		
				 		public void postProcessMergedBeanDefinition(RootBeanDefinition beanDefinition, Class<?> beanType, String beanName) {
							if (this.applicationContext != null) {
								this.singletonNames.put(beanName, beanDefinition.isSingleton());
							}
						}
						
			 			public Object postProcessAfterInitialization(Object bean, String beanName) {
			 				if (this.applicationContext != null && bean instanceof ApplicationListener) { // 如果ApplicationListener实现了接口
			 					// potentially not detected as a listener by getBeanNamesForType retrieval
								Boolean flag = this.singletonNames.get(beanName);
								if (Boolean.TRUE.equals(flag)) { // 是单例的
									// singleton bean (top-level or inner): register on the fly
									this.applicationContext.addApplicationListener((ApplicationListener<?>) bean);
								}
								else if (Boolean.FALSE.equals(flag)) {
									this.singletonNames.remove(beanName);
								}
			 				}
			 				return bean;
			 			}
			 		});
			 	}
			 	...
			 	try {
				 	// Allows post-processing of the bean factory in context subclasses.
					postProcessBeanFactory(beanFactory); //  ------4
					{
						当this===XmlWebApplicationContext
						{
							org.springframework.web.context.support.AbstractRefreshableWebApplicationContext.postProcessBeanFactory(beanFactory)
						}
						
						当this===XmlWebApplicationContext
						{
							AbstractApplicationContext.onRefresh() // 空方法
						}
					}
					
				 	// 扫描applicationContext.xml中实现了BeanFactoryPostProcessor接口的bean，并实例化和调用bean的postProcessBeanDefinitionRegistry(...)方法 ------5
				 	org.springframework.context.support.AbstractApplicationContext.invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory)
				 	{
				 		org.springframework.context.support.PostProcessorRegistrationDelegate.invokeBeanFactoryPostProcessors(beanFactory, getBeanFactoryPostProcessors());
				 		{
				 		
				 			if (beanFactory instanceof BeanDefinitionRegistry) { // !!! org.springframework.beans.factory.support.DefaultListableBeanFactory
					 			BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;
						 		String[] postProcessorNames = beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false); // 扫描 - 用户在applicationContext.xml配置的 BeanDefinitionRegistryPostProcessor类型的bean列表
						 		for (String ppName : postProcessorNames) {
						 			BeanDefinitionRegistryPostProcessor postProcessor = beanFactory.getBean(ppName, BeanDefinitionRegistryPostProcessor.class);
						 			postProcessor.postProcessBeanDefinitionRegistry(registry); // 直接调用postProcessor的方法
						 		}
					 		}
					 		
				 			String[] postProcessorNames = beanFactory.getBeanNamesForType(BeanFactoryPostProcessor.class, true, false); // 扫描 - 用户在applicationContext.xml配置的 BeanFactoryPostProcessor类型的bean列表
					 		for (String ppName : postProcessorNames) {
					 			BeanFactoryPostProcessor postProcessor = beanFactory.getBean(ppName, BeanFactoryPostProcessor.class);
					 			postProcessor.postProcessBeanFactory(beanFactory); // 直接调用postProcessor的方法
					 		}
				 		}
				 	}
				 	...
				 	
				 	// 扫描applicationContext.xml中实现了BeanPostProcessor接口的bean，并实例化添加到beanFactory ------6
				 	org.springframework.context.support.AbstractApplicationContext.registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) 
				 	{
				 		org.springframework.context.support.PostProcessorRegistrationDelegate.registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory, AbstractApplicationContext applicationContext)
				 		{
				 			String[] postProcessorNames = beanFactory.getBeanNamesForType(BeanPostProcessor.class, true, false); // 扫描 - 实现BeanPostProcessor接口的bean
				 			// postProcessorNames = [
						 	// 	 org.springframework.aop.config.internalAutoProxyCreator
						 	// ]
						 	
						 	// org.springframework.context.support.PostProcessorRegistrationDelegate.BeanPostProcessorChecker
				 			beanFactory.addBeanPostProcessor(new BeanPostProcessorChecker(beanFactory, beanProcessorTargetCount)); // 《系统默认》
				 			
				 			for (String ppName : postProcessorNames) {
				 				if (beanFactory.isTypeMatch(ppName, PriorityOrdered.class)) { // 实现PriorityOrdered接口
									BeanPostProcessor pp = beanFactory.getBean(ppName, BeanPostProcessor.class); // 实例化
									priorityOrderedPostProcessors.add(pp);
									if (pp instanceof MergedBeanDefinitionPostProcessor) {
										internalPostProcessors.add(pp);
									}
								}
								else if (beanFactory.isTypeMatch(ppName, Ordered.class)) { // 实现Ordered接口
									orderedPostProcessorNames.add(ppName);
								}
								else {
									nonOrderedPostProcessorNames.add(ppName);
								}
				 			}
				 			
							registerBeanPostProcessors(beanFactory, priorityOrderedPostProcessors); // 添加 BeanPostProcessors
				 			registerBeanPostProcessors(beanFactory, orderedPostProcessors); // 添加 BeanPostProcessors
				 			registerBeanPostProcessors(beanFactory, nonOrderedPostProcessors); // 添加 BeanPostProcessors
				 			
				 			beanFactory.addBeanPostProcessor(new ApplicationListenerDetector(applicationContext)); // 《系统默认》
				 		}
				 	}
				 	
				 	// Initialize message source for this context.
					AbstractApplicationContext.initMessageSource(); // 初始化消息源，在“国际化”中会用到 ------7
					
					// Initialize event multicaster for this context.
					AbstractApplicationContext.initApplicationEventMulticaster(); // 创建应用事件多播器，可以添加自己的“实现了ApplicationListener接口应用监听器”------8
					{
						this.applicationEventMulticaster = new SimpleApplicationEventMulticaster(beanFactory);
						beanFactory.registerSingleton(APPLICATION_EVENT_MULTICASTER_BEAN_NAME, this.applicationEventMulticaster);
					}
					
					// Initialize other special beans in specific context subclasses.
					onRefresh(); // 识别themeSource对象------9
					{
						当this===XmlWebApplicationContext
						{
							org.springframework.web.context.support.AbstractRefreshableWebApplicationContext.onRefresh()
						}
						
						当this===ClassPathXmlApplicationContext
						{
							AbstractApplicationContext.onRefresh() // 空方法
						}
					}
					
					// Check for listener beans and register them.
					AbstractApplicationContext.registerListeners(); // ------10
					{
						// Register statically specified listeners first.
						for (ApplicationListener<?> listener : getApplicationListeners()) {
							getApplicationEventMulticaster().addApplicationListener(listener); // 添加“监听器”
						}
				
						// Do not initialize FactoryBeans here: We need to leave all regular beans
						// uninitialized to let post-processors apply to them!
						String[] listenerBeanNames = getBeanNamesForType(ApplicationListener.class, true, false); // 扫描实现ApplicationListener接口的bean对象
						for (String listenerBeanName : listenerBeanNames) {
							getApplicationEventMulticaster().addApplicationListenerBean(listenerBeanName);
						}
				
						// Publish early application events now that we finally have a multicaster...
						Set<ApplicationEvent> earlyEventsToProcess = this.earlyApplicationEvents;
						this.earlyApplicationEvents = null;
						if (earlyEventsToProcess != null) {
							for (ApplicationEvent earlyEvent : earlyEventsToProcess) {
								getApplicationEventMulticaster().multicastEvent(earlyEvent);
							}
						}
					}
					
					// Instantiate all remaining (non-lazy-init) singletons.
					AbstractApplicationContext.finishBeanFactoryInitialization(beanFactory); // 设置bean工厂的一些属性  ------11
					{
						// Initialize conversion service for this context.
						if (beanFactory.containsBean(CONVERSION_SERVICE_BEAN_NAME) &&
								beanFactory.isTypeMatch(CONVERSION_SERVICE_BEAN_NAME, ConversionService.class)) {
							beanFactory.setConversionService(
									beanFactory.getBean(CONVERSION_SERVICE_BEAN_NAME, ConversionService.class));
						}
				
						// Register a default embedded value resolver if no bean post-processor
						// (such as a PropertyPlaceholderConfigurer bean) registered any before:
						// at this point, primarily for resolution in annotation attribute values.
						if (!beanFactory.hasEmbeddedValueResolver()) {
							beanFactory.addEmbeddedValueResolver(new StringValueResolver() {
								@Override
								public String resolveStringValue(String strVal) {
									return getEnvironment().resolvePlaceholders(strVal);
								}
							});
						}
				
						// Initialize LoadTimeWeaverAware beans early to allow for registering their transformers early.
						String[] weaverAwareNames = beanFactory.getBeanNamesForType(LoadTimeWeaverAware.class, false, false);
						for (String weaverAwareName : weaverAwareNames) { // 在感知“加载时间接口LoadTimeWeaverAware”
							getBean(weaverAwareName);
						}
				
						// Stop using the temporary ClassLoader for type matching.
						beanFactory.setTempClassLoader(null);
				
						// Allow for caching all bean definition metadata, not expecting further changes.
						beanFactory.freezeConfiguration();
				
						// Instantiate all remaining (non-lazy-init) singletons.
						beanFactory.preInstantiateSingletons(); // 预先实例一些单例对象
					}
					
					// Last step: publish corresponding event.
					AbstractApplicationContext.finishRefresh(); // ------12
					{
						// Initialize lifecycle processor for this context.
						AbstractApplicationContext.initLifecycleProcessor();
						{
							ConfigurableListableBeanFactory beanFactory = getBeanFactory();
							if (beanFactory.containsLocalBean(LIFECYCLE_PROCESSOR_BEAN_NAME)) {
								this.lifecycleProcessor =
										beanFactory.getBean(LIFECYCLE_PROCESSOR_BEAN_NAME, LifecycleProcessor.class);
							}
							else {
								DefaultLifecycleProcessor defaultProcessor = new DefaultLifecycleProcessor();
								defaultProcessor.setBeanFactory(beanFactory);
								this.lifecycleProcessor = defaultProcessor;
								beanFactory.registerSingleton(LIFECYCLE_PROCESSOR_BEAN_NAME, this.lifecycleProcessor); // !!!
							}
						}
				
						// Propagate refresh to lifecycle processor first.
						getLifecycleProcessor().onRefresh();
						{
							DefaultLifecycleProcessor.onRefresh();
							{
								DefaultLifecycleProcessor.startBeans(autoStartupOnly === true); // 只启动“自动启动”
								{
									Map<String, Lifecycle> lifecycleBeans = getLifecycleBeans();
									{
										Map<String, Lifecycle> beans = new LinkedHashMap<String, Lifecycle>();
										String[] beanNames = this.beanFactory.getBeanNamesForType(Lifecycle.class, false, false); // 扫描实现Lifecycle接口的bean对象
										for (String beanName : beanNames) {
											String beanNameToRegister = BeanFactoryUtils.transformedBeanName(beanName);
											boolean isFactoryBean = this.beanFactory.isFactoryBean(beanNameToRegister);
											String beanNameToCheck = (isFactoryBean ? BeanFactory.FACTORY_BEAN_PREFIX + beanName : beanName);
											if ((this.beanFactory.containsSingleton(beanNameToRegister) &&
													(!isFactoryBean || Lifecycle.class.isAssignableFrom(this.beanFactory.getType(beanNameToCheck)))) ||
													SmartLifecycle.class.isAssignableFrom(this.beanFactory.getType(beanNameToCheck))) {
												Lifecycle bean = this.beanFactory.getBean(beanNameToCheck, Lifecycle.class);
												if (bean != this) {
													beans.put(beanNameToRegister, bean);
												}
											}
										}
										return beans;
									}
									Map<Integer, LifecycleGroup> phases = new HashMap<Integer, LifecycleGroup>();
									for (Map.Entry<String, ? extends Lifecycle> entry : lifecycleBeans.entrySet()) {
										Lifecycle bean = entry.getValue();
										if (!autoStartupOnly || (bean instanceof SmartLifecycle && ((SmartLifecycle) bean).isAutoStartup())) {
											int phase = getPhase(bean);
											{
												return (bean instanceof Phased ? ((Phased) bean).getPhase() : 0);
											}
											
											LifecycleGroup group = phases.get(phase);
											if (group == null) {
												group = new LifecycleGroup(phase, this.timeoutPerShutdownPhase, lifecycleBeans, autoStartupOnly);
												phases.put(phase, group);
											}
											group.add(entry.getKey(), bean);
											{
												if (bean instanceof SmartLifecycle) {
													this.smartMemberCount++;
												}
												this.members.add(new LifecycleGroupMember(name, bean));
											}
										}
									}
									if (!phases.isEmpty()) {
										List<Integer> keys = new ArrayList<Integer>(phases.keySet());
										Collections.sort(keys);
										for (Integer key : keys) {
											// org.springframework.context.support.DefaultLifecycleProcessor.LifecycleGroup.start()
											phases.get(key).start(); // 启动
											{
												if (this.members.isEmpty()) {
													return;
												}
												Collections.sort(this.members);
												for (LifecycleGroupMember member : this.members) {
													if (this.lifecycleBeans.containsKey(member.name)) {
														doStart(this.lifecycleBeans, member.name, this.autoStartupOnly);
														{
															Lifecycle bean = lifecycleBeans.remove(beanName);
															if (bean != null && !this.equals(bean)) {
																String[] dependenciesForBean = this.beanFactory.getDependenciesForBean(beanName);
																for (String dependency : dependenciesForBean) { // 如果有依赖，进行递归
																	doStart(lifecycleBeans, dependency, autoStartupOnly);
																}
																if (!bean.isRunning() &&
																		(!autoStartupOnly || !(bean instanceof SmartLifecycle) || ((SmartLifecycle) bean).isAutoStartup())) {
																	try {
																		bean.start(); // !!!!
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
								this.running = true;
							}
						}
				
						// Publish the final event.
						publishEvent(new ContextRefreshedEvent(this)); // 触发事件
				
						// Participate in LiveBeansView MBean, if active.
						LiveBeansView.registerApplicationContext(this); // 注册到jmx中
					}
			 	}
			 }
		*/
		
	}

}
