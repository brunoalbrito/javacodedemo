package cn.java.beannote.后置处理器_hook机制;

public class hook_获取bean的过程_调用bean后置处理器_埋点位置 {

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
		 	
		埋点信息：（不是扩展硬编码合成（!mbd.isSynthetic()）的bean才会应用 BeanPostProcessors）
			// org.springframework.beans.factory.support.DefaultListableBeanFactory
			org.springframework.beans.factory.support.AbstractBeanFactory.getBean(String name, Class<T> requiredType)
			{
				org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(String name, Class<T> requiredType, Object[] args, boolean typeCheckOnly)
				{
					org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(String beanName, RootBeanDefinition mbd, Object[] args)
					{
						org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor.postProcessBeforeInstantiation(beanClass, beanName); // --埋点-- 实例化前（钩子）
						Object beanInstance = org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(beanName, mbdToUse, args);
						{
							// --- createBeanInstance --- bof --- 
							instanceWrapper = org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBeanInstance(beanName, mbd, args); // 实例化对象
							{
								if (mbd.getFactoryMethodName() != null)  { // 工厂方法名
									return org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.instantiateUsingFactoryMethod(beanName, mbd, args);  // 使用工厂创建
								}
								
								// Need to determine the constructor... 自动感知出构造函数
								Constructor<?>[] ctors = org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.determineConstructorsFromBeanPostProcessors(beanClass, beanName);
								{
									org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor.determineCandidateConstructors(beanClass, beanName);// --埋点-- 决策构造函数（钩子）
								}
								if (ctors != null ||
										mbd.getResolvedAutowireMode() == RootBeanDefinition.AUTOWIRE_CONSTRUCTOR || “自动装配” --- 感知“构造函数”，注入依赖的bean对象
										mbd.hasConstructorArgumentValues() || !ObjectUtils.isEmpty(args))  { // 有配置“构造函数”的信息，有提供构造函数参数
									return org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.autowireConstructor(beanName, mbd, ctors, args);
								}
						
								// No special handling: simply use no-arg constructor. 使用无参构造函数实例化bean
								return org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.instantiateBean(beanName, mbd);
								{
									// 实例化对象， org.springframework.beans.factory.support.CglibSubclassingInstantiationStrategy.instantiate(mbd, beanName, parent);
									beanInstance = getInstantiationStrategy().instantiate(mbd, beanName, parent);
									BeanWrapper bw = new BeanWrapperImpl(beanInstance);
									initBeanWrapper(bw);
									return bw;
								}
							}
							// --- createBeanInstance --- eof --- 
							 
							final Object bean = (instanceWrapper != null ? instanceWrapper.getWrappedInstance() : null); // 实例化对象
							
							org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor.postProcessMergedBeanDefinition(mbd, beanType, beanName); // --埋点-- 合并 BeanDefinition（钩子）
							
							// --- populateBean --- bof --- 
							Object exposedObject = bean;  // 实例对象
							org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.populateBean(beanName, mbd, instanceWrapper); // 填充属性
							{
								PropertyValues pvs = mbd.getPropertyValues(); // xml配置的属性值
								
								if (!mbd.isSynthetic() && hasInstantiationAwareBeanPostProcessors()) { //　有hook
									for (BeanPostProcessor bp : getBeanPostProcessors()) {
										if (bp instanceof InstantiationAwareBeanPostProcessor) {
											InstantiationAwareBeanPostProcessor ibp = (InstantiationAwareBeanPostProcessor) bp;
											if (!org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor.postProcessAfterInstantiation(bw.getWrappedInstance(), beanName)) { // --埋点-- 实例化后（钩子）
												continueWithPropertyPopulation = false;
												break;
											}
										}
									}
								}
								
								// 自动装配 --- 依赖注入
								if (mbd.getResolvedAutowireMode() == RootBeanDefinition.AUTOWIRE_BY_NAME ||
										mbd.getResolvedAutowireMode() == RootBeanDefinition.AUTOWIRE_BY_TYPE) { // “自动装配”类型
									MutablePropertyValues newPvs = new MutablePropertyValues(pvs);
					
									// Add property values based on autowire by name if applicable.
									if (mbd.getResolvedAutowireMode() == RootBeanDefinition.AUTOWIRE_BY_NAME) { // “自动装配” --- 感知setter方法的“属性名”，注入依赖的bean对象
										autowireByName(beanName, mbd, bw, newPvs);
										{
											String[] propertyNames = unsatisfiedNonSimpleProperties(mbd, bw); // 不在xml文件的属性、不是简单类型
											for (String propertyName : propertyNames) {
												if (containsBean(propertyName)) { // 存在bean
													Object bean = getBean(propertyName);
													newPvs.add(propertyName, bean);
													registerDependentBean(propertyName, beanName); // 注册依赖关系
												}
												else {
												}
											}
										}
									}
					
									// Add property values based on autowire by type if applicable.
									if (mbd.getResolvedAutowireMode() == RootBeanDefinition.AUTOWIRE_BY_TYPE) { // “自动装配” --- 感知setter方法的“参数类型”，注入依赖的bean对象 !!!
										autowireByType(beanName, mbd, bw, newPvs); // org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.autowireByType(...)
										{
											String[] propertyNames = unsatisfiedNonSimpleProperties(mbd, bw); // bean对象的属性列表
											for (String propertyName : propertyNames) {
											PropertyDescriptor pd = bw.getPropertyDescriptor(propertyName); // 某个属性的信息
											if (Object.class != pd.getPropertyType()) { // 属性的类型
												MethodParameter methodParam = BeanUtils.getWriteMethodParameter(pd); // setter的参数信息
												// Do not allow eager init for type matching in case of a prioritized post-processor.
												boolean eager = !PriorityOrdered.class.isAssignableFrom(bw.getWrappedClass()); // bean对象不继承自PriorityOrdered，就是饥渴模式
												DependencyDescriptor desc = new AutowireByTypeDependencyDescriptor(methodParam, eager);
												Object autowiredArgument = DefaultListableBeanFactory.resolveDependency(descriptor === desc, beanName, autowiredBeanNames, converter); // !!! 解析参数类型--- org.springframework.beans.factory.support.DefaultListableBeanFactory.resolveDependency(...)
												{
													if (javaUtilOptionalClass == descriptor.getDependencyType()) {// !!! 参数类型是 Optional
														return new OptionalDependencyFactory().createOptionalDependency(descriptor, requestingBeanName); // 返回bean实例
													}
													else if (ObjectFactory.class == descriptor.getDependencyType() || // !!! 参数类型是 ObjectFactory
															ObjectProvider.class == descriptor.getDependencyType()) { // !!! 参数类型是 ObjectProvider
														return new DependencyObjectProvider(descriptor, requestingBeanName);
													}
													else if (javaxInjectProviderClass == descriptor.getDependencyType()) {  // !!! 参数类型 javax.inject.Provider
														return new Jsr330ProviderFactory().createDependencyProvider(descriptor, requestingBeanName);
													}
													else { // 其他参数类型
														Object result = getAutowireCandidateResolver().getLazyResolutionProxyIfNecessary(
																descriptor, requestingBeanName); // org.springframework.beans.factory.support.SimpleAutowireCandidateResolver
														if (result == null) {
															result = DefaultListableBeanFactory.doResolveDependency(descriptor, requestingBeanName, autowiredBeanNames, typeConverter);// !!!
															{
																...
																Class<?> type = descriptor.getDependencyType(); // 方法参数类型，如： Optional、Bean1、
																Object value = getAutowireCandidateResolver().getSuggestedValue(descriptor); //推荐值 org.springframework.beans.factory.support.SimpleAutowireCandidateResolver
																if (value != null) { // value === null
																}
																
																Object multipleBeans = DefaultListableBeanFactory.resolveMultipleBeans(descriptor, beanName, autowiredBeanNames, typeConverter); // 参数类型是 Array/Collection/Map
																if (multipleBeans != null) { // multipleBeans === null
																	return multipleBeans;
																}
																
																// !!!!!  --- 根据autowire-candidate="true"配置选择“参与自动装配的bean”列表   0、根据setter方法上配置的注解是否在<qualifier>标签中声明进行选择（默认不支持 - 要自行扩展，详见 CustomAutowireConfigurer）
																Map<String, Object> matchingBeans = DefaultListableBeanFactory.findAutowireCandidates(beanName, type, descriptor); // “成为自动装配候选人”的列表
																{
																	String[] candidateNames = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(this, requiredType, true, descriptor.isEager()); // 根据bean的类型获取符合条件的bean列表
																}
																
																if (matchingBeans.isEmpty()) {
																	if (descriptor.isRequired()) { // 必须
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
																	autowiredBeanName = DefaultListableBeanFactory.determineAutowireCandidate(matchingBeans, descriptor); // 选择规则是：1、检查有配置primary的bean ； 2、根据bean优先权order（默认不支持 - 要自行扩展）   3、根据参数名获取到bean（默认不支持 - 要自行扩展）
																	{
																		Class<?> requiredType = descriptor.getDependencyType();
																		String primaryCandidate = determinePrimaryCandidate(candidates, requiredType); // 检查有配置primary的bean
																		if (primaryCandidate != null) {
																			return primaryCandidate;
																		}
																		String priorityCandidate = determineHighestPriorityCandidate(candidates, requiredType); // 根据优先权order获取bean
																		if (priorityCandidate != null) {
																			return priorityCandidate;
																		}
																		// Fallback
																		for (Map.Entry<String, Object> entry : candidates.entrySet()) {
																			String candidateName = entry.getKey();
																			Object beanInstance = entry.getValue();
																			if ((beanInstance != null && this.resolvableDependencies.containsValue(beanInstance)) ||
																					matchesBeanName(candidateName, descriptor.getDependencyName())) { // 根据参数名进行匹配bean
																				// descriptor.getDependencyName() --- 始终返回null  --- AutowireByTypeDependencyDescriptor.getDependencyName()
																				return candidateName;
																			}
																		}
																		return null;
																	}
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
							
								org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.applyPropertyValues(beanName, mbd, bw, pvs); // 设置属性值  bw === org.springframework.beans.BeanWrapperImpl
								{
									BeanDefinitionValueResolver valueResolver = new BeanDefinitionValueResolver(this, beanName, mbd, converter);
									for (PropertyValue pv : original) { // 属性列表
										String propertyName = pv.getName(); // 属性名
										Object originalValue = pv.getValue(); // 属性值
										// org.springframework.beans.factory.support.BeanDefinitionValueResolver
										Object resolvedValue = valueResolver.resolveValueIfNecessary(pv, originalValue); // 解析依赖值
										{
											// value === originalValue
											if (value instanceof RuntimeBeanReference) { // 是运行时引用，获取bean对象
												RuntimeBeanReference ref = (RuntimeBeanReference) value;
												return resolveReference(argName, ref);
											}
											else if (value instanceof RuntimeBeanNameReference) {
												String refName = ((RuntimeBeanNameReference) value).getBeanName();
												refName = String.valueOf(doEvaluate(refName));
												if (!this.beanFactory.containsBean(refName)) { // 不存在bean名称就出错
													throw new BeanDefinitionStoreException(
															"Invalid bean name '" + refName + "' in bean reference for " + argName);
												}
												return refName;
											}
											else if (value instanceof BeanDefinitionHolder) {
												// Resolve BeanDefinitionHolder: contains BeanDefinition with name and aliases.
												BeanDefinitionHolder bdHolder = (BeanDefinitionHolder) value;
												return resolveInnerBean(argName, bdHolder.getBeanName(), bdHolder.getBeanDefinition());
											}
											else if (value instanceof BeanDefinition) {
												// Resolve plain BeanDefinition, without contained name: use dummy name.
												BeanDefinition bd = (BeanDefinition) value;
												String innerBeanName = "(inner bean)" + BeanFactoryUtils.GENERATED_BEAN_NAME_SEPARATOR +
														ObjectUtils.getIdentityHexString(bd);
												return resolveInnerBean(argName, innerBeanName, bd);
											}
											else if (value instanceof ManagedArray) {
											}
											else if (value instanceof ManagedList) {
											}
											else if (value instanceof ManagedSet) {
											}
											else if (value instanceof ManagedMap) {
											}
											else if (value instanceof ManagedProperties) {
											}
											else if (value instanceof TypedStringValue) {
											}
											else {
												return evaluate(value);
												{
													if (value instanceof String) {
														return doEvaluate((String) value);
														{
															// org.springframework.beans.factory.support.DefaultListableBeanFactory.evaluateBeanDefinitionString(value, this.beanDefinition);
															return this.beanFactory.evaluateBeanDefinitionString(value, this.beanDefinition);
															{
																// beanExpressionResolver === org.springframework.context.expression.StandardBeanExpressionResolver
																if (this.beanExpressionResolver == null) { 
																	return value;
																}
																Scope scope = (beanDefinition != null ? getRegisteredScope(beanDefinition.getScope()) : null);
																return this.beanExpressionResolver.evaluate(value, new BeanExpressionContext(this, scope));
															}
														}
													}
													else if (value instanceof String[]) {
														String[] values = (String[]) value;
														boolean actuallyResolved = false;
														Object[] resolvedValues = new Object[values.length];
														for (int i = 0; i < values.length; i++) {
															String originalValue = values[i];
															Object resolvedValue = doEvaluate(originalValue);
															if (resolvedValue != originalValue) {
																actuallyResolved = true;
															}
															resolvedValues[i] = resolvedValue;
														}
														return (actuallyResolved ? resolvedValues : values);
													}
													else {
														return value;
													}
												}
											}
										}
										Object convertedValue = resolvedValue;
										boolean convertible = bw.isWritableProperty(propertyName) &&
												!PropertyAccessorUtils.isNestedOrIndexedProperty(propertyName);
										if (convertible) {
											convertedValue = convertForProperty(resolvedValue, propertyName, bw, converter); // !!!类型转换
										}
										if (resolvedValue == originalValue) {
											if (convertible) {
												pv.setConvertedValue(convertedValue);
											}
											deepCopy.add(pv);
										}
										else if (convertible && originalValue instanceof TypedStringValue &&
												!((TypedStringValue) originalValue).isDynamic() &&
												!(convertedValue instanceof Collection || ObjectUtils.isArray(convertedValue))) {
											pv.setConvertedValue(convertedValue);
											deepCopy.add(pv);
										}
										else {
											resolveNecessary = true;
											deepCopy.add(new PropertyValue(pv, convertedValue));
										}
									}
									bw.setPropertyValues(new MutablePropertyValues(deepCopy)); // 设置值
								}
							}
							// --- populateBean --- eof --- 
							 
							// --- initializeBean --- bof ---  
							exposedObject = org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.initializeBean(beanName, exposedObject, mbd); // 注入感知对象、调用初始化方法
							{
								invokeAwareMethods(beanName, bean); // 给bean对注入感知对象
								{
									if (bean instanceof Aware) {
										if (bean instanceof BeanNameAware) {
											((BeanNameAware) bean).setBeanName(beanName); // 调用感知方法
										}
										if (bean instanceof BeanClassLoaderAware) {
											((BeanClassLoaderAware) bean).setBeanClassLoader(getBeanClassLoader());
										}
										if (bean instanceof BeanFactoryAware) {
											((BeanFactoryAware) bean).setBeanFactory(AbstractAutowireCapableBeanFactory.this);
										}
									}
								}
								
								org.springframework.beans.factory.config.BeanPostProcessor.postProcessBeforeInitialization(result, beanName); // --埋点--  初始化前（钩子）
							
								invokeInitMethods(beanName, wrappedBean, mbd); // 调用bean的初始化方法
								{
									if (isInitializingBean && (mbd == null || !mbd.isExternallyManagedInitMethod("afterPropertiesSet"))) { // bean实现了InitializingBean接口
										((InitializingBean) bean).afterPropertiesSet();
									}
									if (mbd != null) {
										String initMethodName = mbd.getInitMethodName();
										if (initMethodName != null && !(isInitializingBean && "afterPropertiesSet".equals(initMethodName)) &&
												!mbd.isExternallyManagedInitMethod(initMethodName)) {
											invokeCustomInitMethod(beanName, bean, mbd); // 调用init-method="xxx"配置的初始化方法
										}
									}
								}
								
								org.springframework.beans.factory.config.BeanPostProcessor.postProcessAfterInitialization(result, beanName); // --埋点--  初始化后（钩子），aop在这边做的代理劫持
							}
							// --- initializeBean --- bof ---  
							 
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
