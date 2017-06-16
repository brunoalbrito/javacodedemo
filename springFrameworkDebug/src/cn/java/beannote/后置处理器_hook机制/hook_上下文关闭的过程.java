package cn.java.beannote.后置处理器_hook机制;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.Lifecycle;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.support.LiveBeansView;
import org.springframework.context.support.DefaultLifecycleProcessor.LifecycleGroup;
import org.springframework.context.support.DefaultLifecycleProcessor.LifecycleGroupMember;
import org.springframework.util.StringUtils;

public class hook_上下文关闭的过程 {

	public static void main(String[] args) {
		/*
		
			  《****** 关于hook的注入机制 ******》
			 org.springframework.context.support.ClassPathXmlApplicationContext
			 org.springframework.web.context.support.XmlWebApplicationContext
			 
			 org.springframework.context.support.AbstractApplicationContext.close()
			 {
			 	synchronized (this.startupShutdownMonitor) {
					AbstractApplicationContext.doClose();
					{
						if (this.active.get() && this.closed.compareAndSet(false, true)) {
	
							LiveBeansView.unregisterApplicationContext(this); // 从jmx解除注册
				
							try {
								// Publish shutdown event. 触发关闭事件
								publishEvent(new ContextClosedEvent(this));
							}
				
							// Stop all Lifecycle beans, to avoid delays during individual destruction.
							try {
								// org.springframework.context.support.DefaultLifecycleProcessor
								getLifecycleProcessor().onClose(); // 关闭声明周期处理器
								{
									DefaultLifecycleProcessor.stopBeans();
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
										for (Map.Entry<String, Lifecycle> entry : lifecycleBeans.entrySet()) {
											Lifecycle bean = entry.getValue();
											int shutdownOrder = getPhase(bean);
											LifecycleGroup group = phases.get(shutdownOrder);
											if (group == null) {
												group = new LifecycleGroup(shutdownOrder, this.timeoutPerShutdownPhase, lifecycleBeans, false); // !!!
												phases.put(shutdownOrder, group);
											}
											group.add(entry.getKey(), bean); // !!!
										}
										if (!phases.isEmpty()) {
											List<Integer> keys = new ArrayList<Integer>(phases.keySet());
											Collections.sort(keys, Collections.reverseOrder());
											for (Integer key : keys) {
												// org.springframework.context.support.DefaultLifecycleProcessor.LifecycleGroup.stop();
												phases.get(key).stop(); // !!! 停止
												{
													if (this.members.isEmpty()) {
														return;
													}
													Collections.sort(this.members, Collections.reverseOrder());
													CountDownLatch latch = new CountDownLatch(this.smartMemberCount);
													Set<String> countDownBeanNames = Collections.synchronizedSet(new LinkedHashSet<String>());
													for (LifecycleGroupMember member : this.members) {
														if (this.lifecycleBeans.containsKey(member.name)) {
															doStop(this.lifecycleBeans, member.name, latch, countDownBeanNames);
															{
																Lifecycle bean = lifecycleBeans.remove(beanName);
																if (bean != null) {
																	String[] dependentBeans = this.beanFactory.getDependentBeans(beanName);
																	for (String dependentBean : dependentBeans) { // 如果有依赖，先关闭依赖对象
																		doStop(lifecycleBeans, dependentBean, latch, countDownBeanNames);
																	}
																	try {
																		if (bean.isRunning()) {
																			if (bean instanceof SmartLifecycle) {
																				countDownBeanNames.add(beanName);
																				((SmartLifecycle) bean).stop(new Runnable() { // 传递回调函数
																					@Override
																					public void run() {
																						latch.countDown();
																						countDownBeanNames.remove(beanName);
																					}
																				});
																			}
																			else {
																				bean.stop(); // 调用方法
																			}
																		}
																		else if (bean instanceof SmartLifecycle) {
																			// don't wait for beans that aren't running
																			latch.countDown();
																		}
																	}
																}
															}
														}
														else if (member.bean instanceof SmartLifecycle) {
															// already removed, must have been a dependent
															latch.countDown();
														}
													}
													try {
														latch.await(this.timeout, TimeUnit.MILLISECONDS);
													}
												}
											}
										}
									}
									this.running = false;
								}
							}
				
							// Destroy all cached singletons in the context's BeanFactory. 销毁单例的对象
							destroyBeans();
							{
								org.springframework.context.support.AbstractApplicationContext.destroyBeans()
								{
									// org.springframework.beans.factory.support.DefaultListableBeanFactory 
									getBeanFactory().destroySingletons();
									{
										DefaultListableBeanFactory.destroySingletons()
										{
											super.destroySingletons();
											{
												synchronized (this.singletonObjects) {
													this.singletonsCurrentlyInDestruction = true;
												}
										
												String[] disposableBeanNames;
												synchronized (this.disposableBeans) {
													disposableBeanNames = StringUtils.toStringArray(this.disposableBeans.keySet());
												}
												for (int i = disposableBeanNames.length - 1; i >= 0; i--) {
													destroySingleton(disposableBeanNames[i]);
												}
										
												this.containedBeanMap.clear();
												this.dependentBeanMap.clear();
												this.dependenciesForBeanMap.clear();
										
												synchronized (this.singletonObjects) {
													//singletonObjects === java.util.concurrent.ConcurrentHashMap
													this.singletonObjects.clear();
													this.singletonFactories.clear();
													this.earlySingletonObjects.clear();
													this.registeredSingletons.clear();
													this.singletonsCurrentlyInDestruction = false;
												}
											}
											this.manualSingletonNames.clear();
											clearByTypeCache();
											{
												this.allBeanNamesByType.clear();
												this.singletonBeanNamesByType.clear();
											}
										}
									}
								}
							}
				
							// Close the state of this context itself.
							closeBeanFactory();
							{
								org.springframework.context.support.AbstractRefreshableApplicationContext.closeBeanFactory()
								{
									synchronized (this.beanFactoryMonitor) {
										this.beanFactory.setSerializationId(null);
										this.beanFactory = null;
									}
								}
							}
				
							// Let subclasses do some final clean-up if they wish...
							onClose();
							{
								空方法...
							}
				
							this.active.set(false);
						}
					}
					// If we registered a JVM shutdown hook, we don't need it anymore now:
					// We've already explicitly closed the context.
					if (this.shutdownHook != null) {
						try {
							Runtime.getRuntime().removeShutdownHook(this.shutdownHook);
						}
						catch (IllegalStateException ex) {
							// ignore - VM is already shutting down
						}
					}
				}
			 }
		*/
		
	}

}
