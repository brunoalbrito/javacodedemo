package cn.java.总纲;

import org.springframework.transaction.annotation.Transactional;

public class Test {

	public static void main(String[] args) {
		/*
		 	《bean命名空间》：
		 		spring-core-4.3.6.RELEASE.jar、spring-beans-4.3.6.RELEASE.jar、
		 		spring-expression-4.3.6.RELEASE.jar、spring-aspects-4.3.6.RELEASE.jar
			 	hook机制是：
			 		原生支持 - 无需hook
			 	bean信息的定义 - 使用xml配置
				 	别名（<bean name="">）
				 	父BeanDefinition（<bean parent="">）
				 	工厂创建bean（<bean factory-bean="" factory-method="">）
				 	注入 - 自动装配 - 构造函数识别注入（<constructor-arg> <bean autowire="constructor">）
				 	注入 - 自动装配- 属性识别注入（<property> / <bean autowire="byName">）
			 		注入 - 自动装配 - 类型识别注入（ <bean primary="false"> <bean autowire="byType">）
			 		劫持 - getter方法劫持（<lookup-method >）
			 		劫持 - setter方法劫持（<replaced-method >）
			 		初始化 - （<bean init-method="initMethod">）
			 		注入 - 感知 - 实现感知接口 （implements BeanNameAware,BeanClassLoaderAware,BeanFactoryAware ）
			 		定义自己的scope，指定scope创建bean（<bean scope="singletonScope0">）
			 		hook感知（实现BeanPostProcessor接口的hook用来hook关于bean的流程、实现BeanFactoryPostProcessor接口的hook用来hook关于beanFactory的流程）
			 		由PropertyPlaceholderConfigurer支持的动态属性值配置（value="${beanPlaceholderTest0.property1}"）
		 			实现ApplicationListener接口，用于感知应用的事件
		 	
		 	《context命名空间》：
		 		spring-context-4.3.6.RELEASE.jar
		 		hook机制是：
		 			context标签在触发“命名空间的指定标签处理器”的时候，“命名空间的指定标签处理器”会扫描指定目录的下的，符合指定条件的Component类，并注册一些BeanPostProcessor
			 			BeanFactory级别的hook - ConfigurationClassPostProcessor
			 			bean级别的hook - AutowiredAnnotationBeanPostProcessor、RequiredAnnotationBeanPostProcessor、CommonAnnotationBeanPostProcessor、PersistenceAnnotationBeanPostProcessor、EventListenerMethodProcessor、DefaultEventListenerFactory
		 			
		 		bean信息的定义 - 使用注解配置
		 			@Component(value="implOneFooComponent") // beanName == "implOneFooComponent"
					@Scope(scopeName=ConfigurableBeanFactory.SCOPE_PROTOTYPE,proxyMode=ScopedProxyMode.DEFAULT) // 当 proxyMode=ScopedProxyMode.TARGET_CLASS , BeanDefinition 被代理
					@Lazy(value=false)
					@Primary()
					@DependsOn(value={})
					@Role(value=BeanDefinition.ROLE_APPLICATION)
					@Description(value="this is Description..")
				
				bean依赖注入 - 使用注解配置
					@Autowired
					
		 		扫描器的定义 - 使用xml配置
		 			<context:component-scan>
		 			
		 		扫描器的定义 - 使用注解配置
					@PropertySource(name="propertySource0",value={"classpath:cn/java/demo/contexttag/property0.properties"},ignoreResourceNotFound=true,encoding="UTF-8",factory=PropertySourceFactory.class) 
					@Configuration(value="cn.java.demo.contexttag.component.impl.annotation.ConfigurationCondition4RegisterBean") // 限制ComponentScan
					@ComponentScan(basePackages={"cn.java.demo.contexttag.bean,cn.java.demo.contexttag.component.implx"},
					basePackageClasses={},
					nameGenerator=org.springframework.context.annotation.AnnotationBeanNameGenerator.class,
					scopeResolver=org.springframework.context.annotation.AnnotationScopeMetadataResolver.class,
					scopedProxy=ScopedProxyMode.DEFAULT,
					resourcePattern="*.class",
					useDefaultFilters=true,
					includeFilters={
							@ComponentScan.Filter(type=FilterType.ANNOTATION,classes={org.springframework.stereotype.Component.class}),
							@ComponentScan.Filter(type=FilterType.ASSIGNABLE_TYPE,classes={cn.java.demo.contexttag.component.api.FooComponent.class}),
							@ComponentScan.Filter(type=FilterType.REGEX,pattern={"[A-Z][a-z0-9A-Z]*Component"}),
							@ComponentScan.Filter(type=FilterType.CUSTOM,classes={cn.java.demo.contexttag.filter.FooIncludeTypeFilter.class})
					},
					excludeFilters={
							@ComponentScan.Filter(type=FilterType.CUSTOM,classes={cn.java.demo.contexttag.filter.FooExcludeTypeFilter.class})
					},
					lazyInit=false)
					@Import(value={cn.java.demo.contexttag.component.impl.annotation.ImportSelectorImpl.class})
					@ImportResource(locations={},reader=BeanDefinitionReader.class)
				 	class Foo{
				 		@Bean()
						public String method2(){
							return "";
						}
				 	}	
		 	
		 	《aop命名空间》：
		 		spring-aop-4.3.6.RELEASE.jar、spring-aspects-4.3.6.RELEASE.jar
	 			hook机制是：
	 				aop的“命名空间的指定标签处理器”在处理指定标签的时候，会自动注册bean级别的hook - org.springframework.aop.aspectj.autoproxy.AspectJAwareAdvisorAutoProxyCreator
	 			aop的配置 - 使用xml配置
		 			<aop:scoped-proxy proxy-target-class="true" /> BeanDefinition装饰器机制 - 劫持 BeanDefinition 的信息，以CGLIB生成子类
		 			<aop:config>
		 			
	 			aop的配置 - 使用注解配置
	 				<aop:include name="aspect4AspectJAutoProxyTag[0-9]" /> 正则表达式，用来匹配通知者的beanName，bean的必须scope="prototype"
	 				@Aspect(...)、@Before(...)、@After(...)、@Around(...)、@AfterReturning(...)、@AfterThrowing(...)
		 	《web》：
		 		spring-web-4.3.6.RELEASE.jar
		 		定义
					<listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
				使用 
					XmlWebApplicationContext context = servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
		 	
		 	《webmvc》：
		 		spring-webmvc-4.3.6.RELEASE.jar
		 		stax2-api-4.0.0.jar、woodstox-core-asl-4.4.1.jar、jasperreports-6.3.0.jar
		 		validation-api-1.1.0.Final.jar
		 		定义
					<servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
				
				handler可以使用的注解
					类上使用的注解：
						@RequestMapping、@SessionAttributes注解 
					方法上使用的注解：
						@RequestMapping、@InitBinder(参数校验器)、@ModelAttribute（用于设置model）注解
					参数上使用的注解：
						// param0 配置了获取方式，默认值，校验器
						@RequestParam(name="param0",required=true,defaultValue="defaultValue0") @Value(value="defaultValue") @Validated(value={ValidatorRegForm.class})Object param0,
						// param1 配置了获取方式，默认值，校验器
				 		@RequestHeader(name="param0",required=true,defaultValue="defaultValue0") @Validated(value={ValidatorRegForm.class})Object param1,
				 		@RequestBody() Object param2,
				 		@CookieValue(name="param0",required=true,defaultValue="defaultValue0") Object param3,
				 		@PathVariable() Object param4,
				 		@ModelAttribute() Object param5
				handler可以实现的接口
					org.springframework.web.HttpRequestHandler
					org.springframework.web.servlet.mvc.Controller
					org.springframework.web.servlet.mvc.LastModified
			《tx》：	
				spring-tx-4.3.6.RELEASE.jar
				hook机制是：
					走的是AOP的机制，所以要得到AOP的支持
					
				tx的配置 - 使用xml配置
					<tx:advice id="advice0" transaction-manager="transactionManagerx">
					
				tx的配置 - 使用注解配置
					<tx:annotation-driven transaction-manager="transactionManagerx" mode="proxy" />
					@Transactional
					<aop:config>
			《jms》
				spring-jms-4.3.6.RELEASE.jar、activemq-all-5.10.0.jar
				
		 */
	}

}
