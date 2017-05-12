package cn.java.总纲;

public class Test {

	public static void main(String[] args) {
		/*
		 	《bean命名空间》：
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
		 		
		 	《context命名空间》：
		 		bean信息的定义 - 使用注解配置
		 			@Component(value="implOneFooComponent") // beanName == "implOneFooComponent"
					@Scope(scopeName=ConfigurableBeanFactory.SCOPE_PROTOTYPE,proxyMode=ScopedProxyMode.DEFAULT) // 当 proxyMode=ScopedProxyMode.TARGET_CLASS , BeanDefinition 被代理
					@Lazy(value=false)
					@Primary()
					@DependsOn(value={})
					@Role(value=BeanDefinition.ROLE_APPLICATION)
					@Description(value="this is Description..")
					
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
		 			aop的配置 - 使用xml配置
			 			<aop:scoped-proxy proxy-target-class="true" /> 劫持 BeanDefinition 的信息，以CGLIB生成子类
			 			<aop:config>
			 			
		 			aop的配置 - 使用注解配置
		 				<aop:include name="aspect4AspectJAutoProxyTag[0-9]" /> 正则表达式，用来匹配通知者的beanName，bean的必须scope="prototype"
		 				@Aspect(...)、@Before(...)、@After(...)、@Around(...)、@AfterReturning(...)、@AfterThrowing(...)
		 */
	}

}
