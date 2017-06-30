package cn.java.debug.data_common;

import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.init.Jackson2ResourceReader;
import org.springframework.data.repository.init.ResourceReaderRepositoryPopulator;
import org.springframework.data.repository.init.UnmarshallingResourceReader;
import org.springframework.oxm.Unmarshaller;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Debug {

	public static void main(String[] args) {
		/*
		 	命名空间处理器
			 	http\://www.springframework.org/schema/data/repository=org.springframework.data.repository.config.RepositoryNameSpaceHandler
				http\://www.springframework.org/schema/data/keyvalue=org.springframework.data.keyvalue.repository.config.KeyValueRepositoryNameSpaceHandler
		 	schema文档
		 		http\://www.springframework.org/schema/data/repository/spring-repository-1.11.xsd=org/springframework/data/repository/config/spring-repository-1.11.xsd
		 
		 	org.springframework.data.repository.init.Jackson2RepositoryPopulatorFactoryBean
		 
		 
		 	org.springframework.data.repository.init.UnmarshallerRepositoryPopulatorFactoryBean
		 	
		 	
		 	<repository:jackson2-populator>
		 	
		 	机制：
		 		<repository:jackson2-populator>标签处理器，会在上下文注册Jackson2RepositoryPopulatorFactoryBean对象
		 		Jackson2RepositoryPopulatorFactoryBean对象实现了ApplicationListener接口
		 		在启动上下文的最后会触发ContextRefreshedEvent事件，调用Jackson2RepositoryPopulatorFactoryBean接口的onApplicationEvent方法
		 		onApplicationEvent会创建Repositories对象，Repositories自动扫描上下文中实现 RepositoryFactoryInformation 接口的bean，并根据domainType（如：cn.java.demo.data_common.entity.FooEntity）进行归类
		 		用DefaultRepositoryInvokerFactory包装Repositories
		 		迭代资源列表，
		 		{
			 		Jackson2RepositoryPopulatorFactoryBean使用reader读取resources指定的"文件中的对象"信息object，
			 		根据对象的Class，通过DefaultRepositoryInvokerFactory匹配出对应的FooOneEntityRepositoryFactoryInformationImpl，用ReflectionRepositoryInvoker包装FooOneEntityRepositoryFactoryInformationImpl，
			 		调用保存方法ReflectionRepositoryInvoker.invokeSave(object);
		 		
		 		}
		 	--------------------------------	
		 		创建initializer
		 	--------------------------------	
		 	// Jackson2RepositoryPopulatorFactoryBean实例化后会调用afterPropertiesSet() 
		 	org.springframework.beans.factory.config.AbstractFactoryBean.afterPropertiesSet() 	
		 	{
		 		if (isSingleton()) {
					this.initialized = true;
					this.singletonInstance = createInstance();//!!!
					{
						org.springframework.data.repository.init.AbstractRepositoryPopulatorFactoryBean.createInstance()
						{
							ResourceReaderRepositoryPopulator initializer = new ResourceReaderRepositoryPopulator(getResourceReader(){
								org.springframework.data.repository.init.Jackson2RepositoryPopulatorFactoryBean.getResourceReader()
								{
									return new Jackson2ResourceReader(mapper);
								}
							});
							initializer.setResources(resources);
							initializer.setApplicationEventPublisher(context);
					
							this.populator = initializer;
					
							return initializer;
						}
					}
					this.earlySingletonInstance = null;
				}
		 	}
		 	--------------------------------	
		 		读取文件、调用save方法
		 	--------------------------------	
		 	// 在上下文加载完成后，会触发ContextRefreshedEvent事件
		 	org.springframework.data.repository.init.AbstractRepositoryPopulatorFactoryBean.onApplicationEvent(ContextRefreshedEvent event)
		 	{
		 		if (event.getApplicationContext().equals(context)) {
					Repositories repositories = new Repositories(event.getApplicationContext()); // 实例化的Repositories时候，会自动扫描上下文中实现RepositoryFactoryInformation接口的bean进行归类
					{
						this.beanFactory = factory;
						this.repositoryFactoryInfos = new HashMap<Class<?>, RepositoryFactoryInformation<Object, Serializable>>();
						this.repositoryBeanNames = new HashMap<Class<?>, String>();
				
						Repositories.populateRepositoryFactoryInformation(factory); // !!!
						{
							for (String name : BeanFactoryUtils.beanNamesForTypeIncludingAncestors(factory, RepositoryFactoryInformation.class,
								false, false)) {// 扫描实现RepositoryFactoryInformation接口的bean
								Repositories.cacheRepositoryFactory(name);
								{
									RepositoryFactoryInformation repositoryFactoryInformation = beanFactory.getBean(name,
											RepositoryFactoryInformation.class);
									Class<?> domainType = ClassUtils
											.getUserClass(repositoryFactoryInformation.getRepositoryInformation().getDomainType()); // "cn.java.demo.data_common.entity.FooEntity"
							
									RepositoryInformation information = repositoryFactoryInformation.getRepositoryInformation();
									Set<Class<?>> alternativeDomainTypes = information.getAlternativeDomainTypes();
									String beanName = BeanFactoryUtils.transformedBeanName(name);
							
									Set<Class<?>> typesToRegister = new HashSet<Class<?>>(alternativeDomainTypes.size() + 1);
									typesToRegister.add(domainType);
									typesToRegister.addAll(alternativeDomainTypes);
							
									for (Class<?> type : typesToRegister) {
										this.repositoryFactoryInfos.put(type, repositoryFactoryInformation);
										this.repositoryBeanNames.put(type, beanName);
									}
								}
							}
						}
					}
					populator.populate(repositories); // 使用reader读取resources指定的"文件中的对象"信息，保存到指定位置
					{
						org.springframework.data.repository.init.ResourceReaderRepositoryPopulator.populate(Repositories repositories)
						{
							RepositoryInvokerFactory invokerFactory = new DefaultRepositoryInvokerFactory(repositories);
							{
								this(repositories, new DefaultFormattingConversionService());
								{
									this.repositories = repositories;
									this.conversionService = conversionService;
									this.invokers = new HashMap<Class<?>, RepositoryInvoker>();
								}
							}
							for (Resource resource : resources) {
					
								LOGGER.info(String.format("Reading resource: %s", resource));
					
								Object result = readObjectFrom(resource); // 读取指定"文件中的对象"
								{
									return reader.readFrom(resource, classLoader);
									{
										return org.springframework.data.repository.init.Jackson2ResourceReader.readFrom(resource, classLoader);
									}
								}
					
								if (result instanceof Collection) {
									for (Object element : (Collection<?>) result) {
										if (element != null) {
											ResourceReaderRepositoryPopulator.persist(element, invokerFactory);
										} else {
											LOGGER.info("Skipping null element found in unmarshal result!");
										}
									}
								} else {
									ResourceReaderRepositoryPopulator.persist(result, invokerFactory); // 读取指定"文件中的对象"信息，保存到指定位置
									{
										// invoker === org.springframework.data.repository.support.ReflectionRepositoryInvoker
										// invoker === org.springframework.data.repository.support.PagingAndSortingRepositoryInvoker
										// invoker === org.springframework.data.repository.support.CrudRepositoryInvoker
										RepositoryInvoker invoker = invokerFactory.getInvokerFor(object.getClass()); 
										{
											org.springframework.data.repository.support.DefaultRepositoryInvokerFactory.getInvokerFor(object.getClass()); 
											{
												RepositoryInvoker invoker = invokers.get(domainType);

												if (invoker != null) {
													return invoker;
												}
										
												invoker = DefaultRepositoryInvokerFactory.prepareInvokers(domainType); // !!!!
												{
													// repositories === org.springframework.data.repository.support.Repositories
													// repository === cn.java.demo.data_common.repository_factory_information.FooOneEntityRepositoryFactoryInformationImpl
													// information === cn.java.demo.data_common.repository_factory_information.DefaultRepositoryInformation
													
													Object repository = repositories.getRepositoryFor(domainType);
													Assert.notNull(repository, String.format("No repository found for domain type: %s", domainType));
													RepositoryInformation information = repositories.getRepositoryInformationFor(domainType);
											
													return DefaultRepositoryInvokerFactory.createInvoker(information, repository); // !!!!
													{
														if (repository instanceof PagingAndSortingRepository) {
															return new PagingAndSortingRepositoryInvoker((PagingAndSortingRepository<Object, Serializable>) repository,
																	information, conversionService);
														} else if (repository instanceof CrudRepository) {
															return new CrudRepositoryInvoker((CrudRepository<Object, Serializable>) repository, information,
																	conversionService);
														} else {
															return new ReflectionRepositoryInvoker(repository, information, conversionService);
														}
													}
												}
												invokers.put(domainType, invoker);
										
												return invoker;
											}
										}
										LOGGER.debug(String.format("Persisting %s using repository %s", object, invoker));
										invoker.invokeSave(object); // 调用save方法
									}
								}
							}
					
							if (publisher != null) {
								publisher.publishEvent(new RepositoriesPopulatedEvent(this, repositories));
							}
						}
					}
				}
		 	}
		 */
		ObjectMapper mapper = new ObjectMapper();
		Resource[] resources  = null;
		ApplicationContext context = null;
		
		{
			ResourceReaderRepositoryPopulator initializer = new ResourceReaderRepositoryPopulator(new Jackson2ResourceReader(mapper));
			initializer.setResources(resources);
			initializer.setApplicationEventPublisher(context);
		}
		
		{
			Unmarshaller unmarshaller = null;
			ResourceReaderRepositoryPopulator initializer = new ResourceReaderRepositoryPopulator(new UnmarshallingResourceReader(unmarshaller));
			initializer.setResources(resources);
			initializer.setApplicationEventPublisher(context);
		}
	}

}
