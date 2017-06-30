package cn.java.debug.data_jpa;

import org.springframework.data.repository.core.support.AbstractRepositoryMetadata;

public class Debug {

	public static void main(String[] args) {
		/*
		 	命名空间处理器
		 		http\://www.springframework.org/schema/data/jpa=org.springframework.data.jpa.repository.config.JpaRepositoryNameSpaceHandler
		 	schema文档
		 		http\://www.springframework.org/schema/data/jpa/spring-jpa-1.8.xsd=org/springframework/data/jpa/repository/config/spring-jpa-1.8.xsd
		
		 	{
		 		"org.springframework.data.jpa.repository.config.JpaRepositoryConfigExtension#0" : org.springframework.data.jpa.repository.config.JpaRepositoryConfigExtension
		 	}
		 	
		 	{
				"emBeanDefinitionRegistrarPostProcessor" : org.springframework.data.jpa.repository.support.EntityManagerBeanDefinitionRegistrarPostProcessor
				"jpaMappingContext" : org.springframework.data.jpa.repository.config.JpaMetamodelMappingContextFactoryBean
				"org.springframework.context.annotation.internalPersistenceAnnotationProcessor" : org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor
				"jpaContext" : org.springframework.data.jpa.repository.support.DefaultJpaContext
			}
				 	
		 	-------------------
			创建Repository的过程
			-------------------
			org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport.afterPropertiesSet()
			{
				this.factory = createRepositoryFactory(); // org.springframework.data.jpa.repository.support.JpaRepositoryFactory
				{
					org.springframework.data.repository.core.support.TransactionalRepositoryFactoryBeanSupport.createRepositoryFactory()
					{
						RepositoryFactorySupport factory = doCreateRepositoryFactory();
						{
							org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean.doCreateRepositoryFactory() // 每个UserRepository都用JpaRepositoryFactoryBean包装
							{
								return JpaRepositoryFactoryBean.createRepositoryFactory(entityManager); // !!!!! entityManager　是配置指定的<jpa:repositories entity-manager-factory-ref="entityManagerFactory0">
								{
									return new JpaRepositoryFactory(entityManager); //!!!
								}
							}
						}
						factory.addRepositoryProxyPostProcessor(exceptionPostProcessor);
						factory.addRepositoryProxyPostProcessor(txPostProcessor);
				
						return factory;
					}
				}
				this.factory.setQueryLookupStrategyKey(queryLookupStrategyKey);
				this.factory.setNamedQueries(namedQueries);
				this.factory.setEvaluationContextProvider(evaluationContextProvider);
				this.factory.setRepositoryBaseClass(repositoryBaseClass);
				this.factory.setBeanClassLoader(classLoader);
				this.factory.setBeanFactory(beanFactory);
		
				if (publisher != null) {
					this.factory.addRepositoryProxyPostProcessor(new EventPublishingRepositoryProxyPostProcessor(publisher));
				}
		
				this.repositoryMetadata = this.factory.getRepositoryMetadata(repositoryInterface);
		
				if (!lazyInit) {
					initAndReturn();
				}
			}
			
			org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport.getObject()
			{
				org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport.initAndReturn()
				{
					if (this.repository == null) {
						this.repository = this.factory.getRepository(repositoryInterface, customImplementation);
						{
							org.springframework.data.jpa.repository.support.JpaRepositoryFactory.getRepository(repositoryInterface, customImplementation);
							{
								org.springframework.data.repository.core.support.RepositoryFactorySupport.getRepository(Class<T> repositoryInterface, Object customImplementation)
								{
									// repositoryInterface === cn.java.dao.UserRepository
									RepositoryMetadata metadata = getRepositoryMetadata(repositoryInterface); 
									{
										return AbstractRepositoryMetadata.getMetadata(repositoryInterface);
									}
									Class<?> customImplementationClass = null == customImplementation ? null : customImplementation.getClass();
									RepositoryInformation information = getRepositoryInformation(metadata, customImplementationClass);
									{
										Class<?> repositoryBaseClass = this.repositoryBaseClass == null ? getRepositoryBaseClass(metadata){
											org.springframework.data.jpa.repository.support.JpaRepositoryFactory.getRepositoryBaseClass(RepositoryMetadata metadata)
											{
												if (isQueryDslExecutor(metadata.getRepositoryInterface()){
													return QUERY_DSL_PRESENT && QueryDslPredicateExecutor.class.isAssignableFrom(repositoryInterface);
												}) {
													return QueryDslJpaRepository.class;
												} else {
													return SimpleJpaRepository.class;
												}
											}
										}	: this.repositoryBaseClass;
										// repositoryBaseClass == org.springframework.data.jpa.repository.support.SimpleJpaRepository
										repositoryInformation = new DefaultRepositoryInformation(metadata, repositoryBaseClass, customImplementationClass);
										repositoryInformationCache.put(cacheKey, repositoryInformation);
										return repositoryInformation;
									}
									validate(information, customImplementation);
							
									Object target = getTargetRepository(information); // !!!! ----target === org.springframework.data.jpa.repository.support.SimpleJpaRepository
									{
										// information === org.springframework.data.repository.core.support.DefaultRepositoryInformation
										SimpleJpaRepository<?, ?> repository = getTargetRepository(information, entityManager);
										{
											// information === org.springframework.data.repository.core.support.DefaultRepositoryInformation
											JpaEntityInformation<?, Serializable> entityInformation = getEntityInformation(information.getDomainType()); // "cn.java.dao.UserRepository"
									
											return getTargetRepositoryViaReflection(information, entityInformation, entityManager);
											{
												Class<?> baseClass = information.getRepositoryBaseClass(); // org.springframework.data.jpa.repository.support.SimpleJpaRepository
												Constructor<?> constructor = ReflectionUtils.findConstructor(baseClass, constructorArguments);
										
												if (constructor == null) {
										
													List<Class<?>> argumentTypes = new ArrayList<Class<?>>(constructorArguments.length);
										
													for (Object argument : constructorArguments) {
														argumentTypes.add(argument.getClass());
													}
										
													throw new IllegalStateException(String.format(
															"No suitable constructor found on %s to match the given arguments: %s. Make sure you implement a constructor taking these",
															baseClass, argumentTypes));
												}
										
												return (R) BeanUtils.instantiateClass(constructor, constructorArguments);
											}
										}
										repository.setRepositoryMethodMetadata(crudMethodMetadataPostProcessor.getCrudMethodMetadata());
								
										return repository;
									}
							
									// Create proxy
									ProxyFactory result = new ProxyFactory();
									result.setTarget(target);
									result.setInterfaces(new Class[] { repositoryInterface, Repository.class });
							
									result.addAdvice(SurroundingTransactionDetectorMethodInterceptor.INSTANCE);  // 拦截器1
									result.addAdvisor(ExposeInvocationInterceptor.ADVISOR);  // 拦截器2
							
									if (TRANSACTION_PROXY_TYPE != null) {
										result.addInterface(TRANSACTION_PROXY_TYPE);  // 拦截器3
									}
							
									for (RepositoryProxyPostProcessor processor : postProcessors) {
										processor.postProcess(result, information);
									}
							
									if (IS_JAVA_8) {
										result.addAdvice(new DefaultMethodInvokingMethodInterceptor());  // 拦截器4
									}
							
									result.addAdvice(new QueryExecutorMethodInterceptor(information, customImplementation, target));  // 拦截器5--调用实际方法
							
									return (T) result.getProxy(classLoader); // 创建代理对象
								}
							}
						}
					}
					return this.repository;
				}
			}
			
			-------------------
			调用Repository的过程
			-------------------
			org.springframework.data.repository.core.support.RepositoryFactorySupport.QueryExecutorMethodInterceptor.invoke(MethodInvocation invocation) 
			{
				Object result = doInvoke(invocation);
				{
					Method method = invocation.getMethod();
					Object[] arguments = invocation.getArguments();
		
					if (isCustomMethodInvocation(invocation)) { // 自定义实现类 cn.java.dao.UserRepositoryImpl的方法
		
						Method actualMethod = repositoryInformation.getTargetClassMethod(method);
						return executeMethodOn(customImplementation, actualMethod, arguments);
						{
							try {
								return method.invoke(target, parameters); // 调用目标方法
							} catch (Exception e) {
								ClassUtils.unwrapReflectionException(e);
							}
						}
					}
		
					if (hasQueryFor(method)) {
						return queries.get(method).execute(arguments);
					}
		
					// Lookup actual method as it might be redeclared in the interface
					// and we have to use the repository instance nevertheless
					
					// !!!repositoryInformation === org.springframework.data.repository.core.support.DefaultRepositoryInformation
					// !!!自定义类 cn.java.dao.UserRepositoryImpl 中查找、基础类 org.springframework.data.jpa.repository.support.SimpleJpaRepository 中查找
					Method actualMethod = repositoryInformation.getTargetClassMethod(method); 
					return executeMethodOn(target, actualMethod, arguments);
					{
						try {
							return method.invoke(target, parameters); // 调用目标方法 ，如：SimpleJpaRepository.getOne(..)  SimpleJpaRepository.findAll(..)  SimpleJpaRepository.save(..)  SimpleJpaRepository.delete(..)
						} catch (Exception e) {
							ClassUtils.unwrapReflectionException(e);
						}
					}
				}

				// Looking up the TypeDescriptor for the return type - yes, this way o.O
				Method method = invocation.getMethod();
				MethodParameter parameter = new MethodParameter(method, -1);
				TypeDescriptor methodReturnTypeDescriptor = TypeDescriptor.nested(parameter, 0);
	
				return resultHandler.postProcessInvocationResult(result, methodReturnTypeDescriptor);
			}
			
			
			---------------------------------------------
			 执行机制是：
			{	
			 	在执行<jpa:repositories ...>命名空间处理器的时候
			 	{
			 		1、注册JpaRepositoryConfigExtension到BeanDefinition容器
			 		{
		 				"org.springframework.data.jpa.repository.config.JpaRepositoryConfigExtension#0" : org.springframework.data.jpa.repository.config.JpaRepositoryConfigExtension
				 	}
				 	
				 	2、注册如下到BeanDefinition容器
				 	{
						"emBeanDefinitionRegistrarPostProcessor" : org.springframework.data.jpa.repository.support.EntityManagerBeanDefinitionRegistrarPostProcessor
						"jpaMappingContext" : org.springframework.data.jpa.repository.config.JpaMetamodelMappingContextFactoryBean
						"org.springframework.context.annotation.internalPersistenceAnnotationProcessor" : org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor
						"jpaContext" : org.springframework.data.jpa.repository.support.DefaultJpaContext
					}
					
				 	3、
				 	{
					 	会根据配置路径base-package="..."，扫描出来的“Repository接口”，
					 	迭代接口，把每个“Repository接口”包装成 JpaRepositoryFactoryBean对象，
					 	{
					 		// 一个entity会被包装成如下（并注册到BeanDefinition容器，beanName是由@Component觉得或者自动以短名(如：userRepository)作为beanName）
						 	org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean(cn.java.dao.UserRepository)
						 	{
						 		queryLookupStrategyKey : ""
						 		lazyInit : ""
						 		repositoryBaseClass : ""
						 		namedQueries :  “classpath*:META-INF/jpa-named-queries.properties”的实例
						 		customImplementation :  自定义xxxImpl的实例,如容器中有注册userRepositoryImpl == cn.java.dao.UserRepositoryImpl
						 		evaluationContextProvider : org.springframework.data.repository.query.ExtensionAwareEvaluationContextProvider
						 		------
						 		transactionManager : "transactionManagerx"
							 	entityManager : 引用org.springframework.orm.jpa.SharedEntityManagerCreator.createSharedEntityManager("entityManagerFactory0")对象
							 	mappingContext :引用jpaMappingContext对象
							 	------
							 	enableDefaultTransactions : false
							 	------
							 	factoryBeanObjectType ：cn.java.dao.UserRepository
						 	}
					 	}
					 	并注册到BeanDefinition容器
				 	}
			 	}
			 }
		 
		 */
	}

}
