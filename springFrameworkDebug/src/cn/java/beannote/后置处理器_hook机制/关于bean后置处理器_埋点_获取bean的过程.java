package cn.java.beannote.后置处理器_hook机制;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.AutowireByTypeDependencyDescriptor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory.OptionalDependencyFactory;
import org.springframework.core.MethodParameter;
import org.springframework.core.PriorityOrdered;
import org.springframework.util.ObjectUtils;

public class 关于bean后置处理器_埋点_获取bean的过程 {

	public static void main(String[] args) {
		/*
		
		继承树：
			org.springframework.beans.factory.config.BeanPostProcessor
				-org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor
				-org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor
					-org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor
		
		种类：
			org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor 实例化前后
		 	org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor 决策构造函数
		 	org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor 合并 BeanDefinition
		 	org.springframework.beans.factory.config.BeanPostProcessor 调用初始化方法前后
		 	
		埋点信息：（不是合成（!mbd.isSynthetic()）的bean才会应用 BeanPostProcessors）
			// org.springframework.beans.factory.support.DefaultListableBeanFactory
			org.springframework.beans.factory.support.AbstractBeanFactory.getBean(String name, Class<T> requiredType)
			{
				org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(String name, Class<T> requiredType, Object[] args, boolean typeCheckOnly)
				{
					org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(String beanName, RootBeanDefinition mbd, Object[] args)
					{
						org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor.postProcessBeforeInstantiation(beanClass, beanName); // --埋点-- 实例化前（钩子）
						Object beanInstance = doCreateBean(beanName, mbdToUse, args);
						{
							instanceWrapper = createBeanInstance(beanName, mbd, args); // 实例化对象
							{
								if (mbd.getFactoryMethodName() != null)  { // 工厂方法名
									return instantiateUsingFactoryMethod(beanName, mbd, args);  // 使用工厂创建
								}
								
								// Need to determine the constructor... 自动感知出构造函数
								Constructor<?>[] ctors = determineConstructorsFromBeanPostProcessors(beanClass, beanName);
								{
									org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor.determineCandidateConstructors(beanClass, beanName);// --埋点-- 决策构造函数（钩子）
								}
								if (ctors != null ||
										mbd.getResolvedAutowireMode() == RootBeanDefinition.AUTOWIRE_CONSTRUCTOR ||
										mbd.hasConstructorArgumentValues() || !ObjectUtils.isEmpty(args))  {
									return autowireConstructor(beanName, mbd, ctors, args);
								}
						
								// No special handling: simply use no-arg constructor.
								return instantiateBean(beanName, mbd);
							}
							
							final Object bean = (instanceWrapper != null ? instanceWrapper.getWrappedInstance() : null); // 实例化对象
							
							org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor.postProcessMergedBeanDefinition(mbd, beanType, beanName); // --埋点-- 合并 BeanDefinition（钩子）
							
							Object exposedObject = bean;  // 实例对象
							populateBean(beanName, mbd, instanceWrapper); // 填充属性
							{
								org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor.postProcessAfterInstantiation(bw.getWrappedInstance(), beanName) // --埋点-- 实例化后（钩子）
								
								
								// 自动装配 --- 依赖注入
								if (mbd.getResolvedAutowireMode() == RootBeanDefinition.AUTOWIRE_BY_NAME ||
										mbd.getResolvedAutowireMode() == RootBeanDefinition.AUTOWIRE_BY_TYPE) { // “自动装配”类型
									MutablePropertyValues newPvs = new MutablePropertyValues(pvs);
					
									// Add property values based on autowire by name if applicable.
									if (mbd.getResolvedAutowireMode() == RootBeanDefinition.AUTOWIRE_BY_NAME) { // 感知setter方法的属性名，获取依赖的bean对象
										autowireByName(beanName, mbd, bw, newPvs);
									}
					
									// Add property values based on autowire by type if applicable.
									if (mbd.getResolvedAutowireMode() == RootBeanDefinition.AUTOWIRE_BY_TYPE) { // 感知setter方法的参数类型，获取依赖的bean对象 !!!
										autowireByType(beanName, mbd, bw, newPvs); // org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.autowireByType(...)
										{
											String[] propertyNames = unsatisfiedNonSimpleProperties(mbd, bw);
											for (String propertyName : propertyNames) {
											PropertyDescriptor pd = bw.getPropertyDescriptor(propertyName);
											if (Object.class != pd.getPropertyType()) {
												MethodParameter methodParam = BeanUtils.getWriteMethodParameter(pd); // setter的参数类型
												// Do not allow eager init for type matching in case of a prioritized post-processor.
												boolean eager = !PriorityOrdered.class.isAssignableFrom(bw.getWrappedClass()); // 不继承自PriorityOrdered，就是饥渴模式
												DependencyDescriptor desc = new AutowireByTypeDependencyDescriptor(methodParam, eager);
												Object autowiredArgument = resolveDependency(desc, beanName, autowiredBeanNames, converter); // !!! 解析参数类型--- org.springframework.beans.factory.support.DefaultListableBeanFactory.resolveDependency(...)
												{
													if (javaUtilOptionalClass == descriptor.getDependencyType()) {// !!! 参数类型是 Optional
														return new OptionalDependencyFactory().createOptionalDependency(descriptor, requestingBeanName); // 返回bean实例
													}
													.....
													else { // 其他参数类型
														Object result = getAutowireCandidateResolver().getLazyResolutionProxyIfNecessary(
																descriptor, requestingBeanName); // org.springframework.beans.factory.support.SimpleAutowireCandidateResolver
														if (result == null) {
															result = doResolveDependency(descriptor, requestingBeanName, autowiredBeanNames, typeConverter);// !!!
															{
																...
																Class<?> type = descriptor.getDependencyType(); // 方法参数类型，如： Optional、Bean1、
																Object value = getAutowireCandidateResolver().getSuggestedValue(descriptor); // org.springframework.beans.factory.support.SimpleAutowireCandidateResolver
													
																Object multipleBeans = resolveMultipleBeans(descriptor, beanName, autowiredBeanNames, typeConverter); // 参数类型是 Array/Collection/Map
																if (multipleBeans != null) {
																	return multipleBeans;
																}
																Map<String, Object> matchingBeans = findAutowireCandidates(beanName, type, descriptor);
																if (matchingBeans.isEmpty()) {
																	if (descriptor.isRequired()) {
																		raiseNoMatchingBeanFound(type, descriptor.getResolvableType(), descriptor);// !!!!
																		{
																			checkBeanNotOfRequiredType(type, descriptor); //!!!
																			{
																				for (String beanName : this.beanDefinitionNames) {
																					Class<?> beanType = (beanInstance != null ? beanInstance.getClass() : predictBeanType(beanName, mbd)); // !!! predictBeanType
																					{
																						Class<?> predicted = org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor.predictBeanType(targetType, beanName); //  --埋点--  predictBeanType调用（钩子）                                             
																						if (predicted != null && (typesToMatch.length != 1 || FactoryBean.class != typesToMatch[0] ||
																								FactoryBean.class.isAssignableFrom(predicted))) {
																							return predicted;
																						}
																					}
																				}
																			}
																		}
																	}
																	return null;
																}
																...
																if (matchingBeans.size() > 1) { // 根据类型匹配到的bean超过一个
																	autowiredBeanName = determineAutowireCandidate(matchingBeans, descriptor); // 选择规则是：1、检查有配置primary的bean ； 2、根据bean优先权   3、根据参数名获取到bean
																	instanceCandidate = matchingBeans.get(autowiredBeanName);
																}
																...
																return (instanceCandidate instanceof Class ?  // 如果instanceCandidate实现了 Class接口
																			descriptor.resolveCandidate(autowiredBeanName, type, this) : instanceCandidate); // !!!!
															}
														}
														return result;
													}
												
												}
												if (autowiredArgument != null) {
													pvs.add(propertyName, autowiredArgument);
												}
											}
										}
									}
					
									pvs = newPvs;
								}
								
								org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor.postProcessPropertyValues(pvs, filteredPds, bw.getWrappedInstance(), beanName); // --埋点-- 属性依赖识别完成（钩子）
							
								applyPropertyValues(beanName, mbd, bw, pvs); // 设置属性值  bw === org.springframework.beans.BeanWrapperImpl
							}
							
							exposedObject = initializeBean(beanName, exposedObject, mbd); // 注入感知对象、调用初始化方法
							{
								invokeAwareMethods(beanName, bean); // 给bean对注入感知对象
								org.springframework.beans.factory.config.BeanPostProcessor.postProcessBeforeInitialization(result, beanName); // --埋点--  初始化前（钩子）
								invokeInitMethods(beanName, wrappedBean, mbd); // 调用bean的初始化方法
								org.springframework.beans.factory.config.BeanPostProcessor.postProcessAfterInitialization(result, beanName); // --埋点--  初始化后（钩子），aop在这边做的代理劫持
							}
							
							if (earlySingletonExposure) {
								Object earlySingletonReference = getSingleton(beanName, false);
								{
									return org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor.getEarlyBeanReference(exposedObject, beanName); // --埋点--  EarlyBean引用（钩子）
								}
							}
							
							return exposedObject;
						}
						return beanInstance;
					}
				}
			}
			
		*/
		
	}

}
