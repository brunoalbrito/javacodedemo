package cn.java.demo.beantag;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.LifecycleProcessor;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.DefaultLifecycleProcessor;

import cn.java.demo.beantag.api.HelloService;
import cn.java.demo.beantag.bean.AliasTestBean;
import cn.java.demo.beantag.bean.AutowireByConstructor;
import cn.java.demo.beantag.bean.AutowireByNameBean;
import cn.java.demo.beantag.bean.AutowireByTypeBean;
import cn.java.demo.beantag.bean.ConfiguredLookupMethodBean;
import cn.java.demo.beantag.bean.ConfiguredReplacedMethodBean;
import cn.java.demo.beantag.bean.CreatedByFactoryBean;
import cn.java.demo.beantag.bean.FooBean;
import cn.java.demo.beantag.bean.StandardBean;
import cn.java.demo.beantag.bean.UseCustomScopeBean;
import cn.java.demo.beantag.bean.UseInstancePoolScopeBean;
import cn.java.demo.beantag.bean.applicationlistener.FooEventTriggerBean;
import cn.java.demo.beantag.bean.aware.NeedAwareBean;
import cn.java.demo.beantag.bean.initmehtod.DemoInitMethodBean;
import cn.java.demo.beantag.bean.initmehtod.DemoInitializingBean;
import cn.java.demo.beantag.bean.lookupmethod.Property1;
import cn.java.demo.beantag.bean.methodreplacer.MethodReplacerImpl;
import cn.java.demo.beantag.beandefinition_property.StupidRootBeanDefinitionInJavaTest;
import cn.java.demo.beantag.internal.BeanNameGeneratorTest;
import cn.java.demo.beantag.internal.ExpressionParserTest;
import cn.java.demo.beantag.internal.GetBeanXTest;
import cn.java.demo.beantag.internal.I18nTest;
import cn.java.demo.beantag.internal.InternalUtils_AnnotationUtilsTest;
import cn.java.demo.beantag.internal.InternalUtils_BeanUtilsTest;
import cn.java.demo.beantag.internal.InternalUtils_ClassUtils;
import cn.java.demo.beantag.internal.InternalUtils_ObjectUtilsTest;
import cn.java.demo.beantag.internal.InternalUtils_ReflectionUtilsTest;
import cn.java.demo.beantag.internal.TypeConverterTest;
import cn.java.demo.util.ApplicationContextUtil;


public class Test {

	/**
	 * https://github.com/spring-projects
	 * http://mvnrepository.com/search?q=Spring
	 * 使用4.3.6版本
	 */
	public static void main(String[] args) {
//		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:cn/java/demo/beantag/applicationContext.xml");
	
		System.out.println("################################################################################################");
		
		{ // 一些钩子bean
			ApplicationContextUtil.printBeanDefinitionInRegistry((AbstractRefreshableConfigApplicationContext) context);
			ApplicationContextUtil.printBeanFactoryPostProcessorsInBeanFactory((AbstractRefreshableConfigApplicationContext) context);
			ApplicationContextUtil.printBeanPostProcessorInBeanFactory((AbstractRefreshableConfigApplicationContext) context);
		}
		
		System.out.println("-------------bean的使用-------------------");
		{
			HelloService helloService = context.getBean("helloService", HelloService.class);
			helloService.sayHello();
		}
		
		System.out.println("-------------bean的parent属性的使用-------------------");
		{
			HelloService helloServiceChild = context.getBean("helloServiceChild", HelloService.class);
			helloServiceChild.sayHello();
		}
		
		System.out.println("-------------bean的使用，使用构造函数装配-------------------");
		{ // bean的使用
			FooBean fooBean = context.getBean("fooBean", FooBean.class);
			fooBean = context.getBean("fooBeanAlias0", FooBean.class);
			System.out.println(fooBean.toString());
		}

		System.out.println("-------------bean配置了“别名”-------------------");
		{ // bean配置了“别名”
			AliasTestBean aliasTestBean = context.getBean("aliasTestBean", AliasTestBean.class);
			aliasTestBean.testMethod();

			aliasTestBean = context.getBean("aliasTestBeanAlias0", AliasTestBean.class);
			aliasTestBean.testMethod();

			aliasTestBean = context.getBean("aliasTestBeanAlias1", AliasTestBean.class);
			aliasTestBean.testMethod();
		}
		
		System.out.println("-------------bean配置了“使用工厂的方式创建”-------------------");
		{ // bean配置了“使用工厂的方式创建”
			CreatedByFactoryBean createdByFactoryBean0 = context.getBean("createdByFactoryBean0", CreatedByFactoryBean.class);
			createdByFactoryBean0.testMethod();
			
			CreatedByFactoryBean createdByFactoryBean1 = (CreatedByFactoryBean) context.getBean("createdByFactoryBean1");
			createdByFactoryBean1.testMethod();
			
			CreatedByFactoryBean createdByFactoryBean2 = (CreatedByFactoryBean)context.getBean("createdByFactoryBean2");
			createdByFactoryBean2.testMethod();
			createdByFactoryBean2.getFooBean().getFiled1();
		}
		
		System.out.println("-------------bean配置了“自动装配”-------------------");
		{ // bean配置了“自动装配”
			System.out.println("--- 方式1.通过“setter的属性名”自动装配  ---");
			AutowireByNameBean autowireByNameBean = context.getBean("autowireByNameBean", AutowireByNameBean.class);
			System.out.println(autowireByNameBean);
			
			System.out.println("--- 方式2.通过“构造函数”自动装配  --- ");
			// 1、在配置文件中指定构造函数的参数值
			AutowireByConstructor autowireByConstructor0 = context.getBean("autowireByConstructor0", AutowireByConstructor.class);
			System.out.println(autowireByConstructor0);
			// 2、在代码中指定构造函数的参数值
			AutowireByConstructor autowireByConstructor1 = (AutowireByConstructor) context.getBean("autowireByConstructor1",1,"username is assign in java - AutowireByConstructor");
			System.out.println(autowireByConstructor1);

			System.out.println("--- 方式3.通过“setter的参数类型”自动装配  --- ");
			AutowireByTypeBean autowireByTypeBean0 = context.getBean("autowireByTypeBean0", AutowireByTypeBean.class);
			System.out.println(autowireByTypeBean0.getUsername());
			System.out.println(autowireByTypeBean0.getFooBean().getFiled1());
			
		}
		
		System.out.println("-------------bean配置了“lookup-method”（劫持）-------------------");
		{ // bean配置了“lookup-method”，劫持getter方法 -- 这个会使用cglib生成子类
			ConfiguredLookupMethodBean configuredLookupMethodBean = context.getBean("configuredLookupMethodBean", ConfiguredLookupMethodBean.class);
			Property1 property1 = configuredLookupMethodBean.getProperty1(1, "test lookup-method"); // 调用这个方法的时候会被劫持，返回的是“劫持者”
			System.out.println(property1.getClass().getName());
			System.out.println(property1.getMyName());
		}
		
		System.out.println("-------------bean配置了“replaced-method”（劫持）-------------------");
		{ // bean配置了“replaced-method”，劫持setter方法 -- 这个会使用cglib生成子类
			ConfiguredReplacedMethodBean configuredReplacedMethodBean = context.getBean("configuredReplacedMethodBean", ConfiguredReplacedMethodBean.class);
			configuredReplacedMethodBean.setProperty1(1, "test replaced-method.."); // 调用这个方法的时候会被劫持，设置的数据被设置到了“劫持者”里面
			MethodReplacerImpl methodReplacerBeanNameId = context.getBean("methodReplacerBeanNameId", MethodReplacerImpl.class); // 获取“劫持者”
			System.out.println(methodReplacerBeanNameId.getProperty1().toString());
		}
		
		System.out.println("-------------bean配置了“初始化方法”-------------------");
		{ // bean配置了“初始化方法”
			DemoInitMethodBean demoInitMethodBean = context.getBean("demoInitMethodBean", DemoInitMethodBean.class);
			demoInitMethodBean.testMethod();
			DemoInitializingBean demoInitializingBean = context.getBean("demoInitializingBean",DemoInitializingBean.class);
		}
		
		System.out.println("-------------bean实现了“感知接口”-------------------");
		{ // bean实现了“感知接口”
			NeedAwareBean needAwareBean = context.getBean("needAwareBean", NeedAwareBean.class);
			needAwareBean.testMethod();
		}
		
		System.out.println("-------------实现自己的scope，使用自己的scope对象创建bean-------------------");
		{ // 实现自己的scope，使用自己的scope对象创建bean
			
			// Spring会自动扫描：实现了BeanFactoryPostProcessor接口
			
			UseCustomScopeBean useCustomScopeBean0 = context.getBean("useCustomScopeBean0", UseCustomScopeBean.class);
			
			UseCustomScopeBean useCustomScopeBean1 = context.getBean("useCustomScopeBean1", UseCustomScopeBean.class);
			
			// 对象池，每个bean名可以创建n个对象放入池中
			UseInstancePoolScopeBean useInstancePoolScopeBean0 = context.getBean("useInstancePoolScopeBean0", UseInstancePoolScopeBean.class);
			System.out.println(useInstancePoolScopeBean0.getScope().getInstanceCounter("useInstancePoolScopeBean0"));
			UseInstancePoolScopeBean useInstancePoolScopeBean1 = context.getBean("useInstancePoolScopeBean0", UseInstancePoolScopeBean.class);
			System.out.println(useInstancePoolScopeBean1.getScope().getInstanceCounter("useInstancePoolScopeBean0"));
			UseInstancePoolScopeBean useInstancePoolScopeBean2 = context.getBean("useInstancePoolScopeBean0", UseInstancePoolScopeBean.class);
			System.out.println(useInstancePoolScopeBean2.getScope().getInstanceCounter("useInstancePoolScopeBean0"));
		}
		
		System.out.println("-------------定义自己的 BeanPostProcessor（Hook钩子机制）-------------------");
		{ // 定义自己的 BeanPostProcessor
			
			// Spring会自动扫描，实现BeanPostProcessor接口的bean
			
			// 机制：实例化后的bean对象，传给MyBeanPostProcessorImpl（在感知注入后，在初始化方法被调用前后），像AOP就是在里面的postProcessAfterInitialization做的篡改
			System.out.println("...查看配置文件...");
		}
		
		System.out.println("-------------动态注入RootBeanDefinition-------------------");
		{
			StupidRootBeanDefinitionInJavaTest stupidRootBeanDefinitionInJavaTest = new StupidRootBeanDefinitionInJavaTest();
			stupidRootBeanDefinitionInJavaTest.testHelloWorld((AbstractRefreshableConfigApplicationContext) context);
			stupidRootBeanDefinitionInJavaTest.testRuntimeBeanReferenceX((AbstractRefreshableConfigApplicationContext) context);
			stupidRootBeanDefinitionInJavaTest.testRuntimeBeanNameReferenceX((AbstractRefreshableConfigApplicationContext) context);
			stupidRootBeanDefinitionInJavaTest.testBeanDefinitionHoldertX((AbstractRefreshableConfigApplicationContext) context);
			stupidRootBeanDefinitionInJavaTest.testBeanDefinitionX((AbstractRefreshableConfigApplicationContext) context);
		}
		

		System.out.println("-------------BeanDefinition动态配置-------------------");
		{
			CreatedByFactoryBean beanPlaceholderTest0 = context.getBean("beanPlaceholderTest0", CreatedByFactoryBean.class);
			System.out.println(beanPlaceholderTest0);
			
			StandardBean beanPlaceholderTest1 = context.getBean("beanPlaceholderTest1", StandardBean.class);
			System.out.println(beanPlaceholderTest1);
		}
		
		System.out.println("-------------使用ClassPathXmlApplicationContext的事件管理器-------------------");
		{
			FooEventTriggerBean fooEventTriggerBean = context.getBean("fooEventTriggerBean", FooEventTriggerBean.class);
			fooEventTriggerBean.testTriggerEvent();
		}
		
		System.out.println("-------------手动启动“不能自动启动bean”-------------------");
		{
			ConfigurableListableBeanFactory beanFactory = ApplicationContextUtil.getBeanFactoryAndTryCastTypeToConfigurableListableBeanFactory((AbstractRefreshableConfigApplicationContext) context);
			LifecycleProcessor lifecycleProcessor = (LifecycleProcessor)beanFactory.getSingleton(AbstractApplicationContext.LIFECYCLE_PROCESSOR_BEAN_NAME);
			if(lifecycleProcessor instanceof DefaultLifecycleProcessor){
				DefaultLifecycleProcessor defaultLifecycleProcessor = (DefaultLifecycleProcessor)lifecycleProcessor;
				defaultLifecycleProcessor.start(); // 启动那些不能自动启动的
			}
		}
		
		System.out.println("-------------其他-------------------");
		{ // 其他
			
			
			System.out.println("******类型转换器");
			TypeConverterTest.testStandardTypeConverterTest();
			
			System.out.println("******获取bean的方式");
			GetBeanXTest.testGetBeanX((AbstractRefreshableConfigApplicationContext) context);
			
			System.out.println("******使用表达式访问bean对象");
			ExpressionParserTest.testStandardBeanExpressionResolverResultString((AbstractRefreshableConfigApplicationContext) context);
			ExpressionParserTest.testStandardBeanExpressionResolverResultBeanObject((AbstractRefreshableConfigApplicationContext) context);
			
			{
				System.out.println("******BeanUtils工具类");
				InternalUtils_BeanUtilsTest.testInstantiateClass((AbstractRefreshableConfigApplicationContext) context);
				InternalUtils_BeanUtilsTest.testIsPresent((AbstractRefreshableConfigApplicationContext) context);
				
				System.out.println("******ClassUtils工具类");
				InternalUtils_ClassUtils.testClassUtils((AbstractRefreshableConfigApplicationContext) context);
				
				System.out.println("******ObjectUtils工具类");
				InternalUtils_ObjectUtilsTest.testObjectUtils((AbstractRefreshableConfigApplicationContext) context);
				
				System.out.println("******AnnotationUtils工具类");
				InternalUtils_AnnotationUtilsTest.testAnnotationUtils((AbstractRefreshableConfigApplicationContext) context);
				
				System.out.println("******ReflectionUtils工具类");
				InternalUtils_ReflectionUtilsTest.testReflectionUtils((AbstractRefreshableConfigApplicationContext) context);
			}
			
			System.out.println("******自动生成beanName");
			BeanNameGeneratorTest.testBeanNameGenerator((AbstractRefreshableConfigApplicationContext) context);
			
			System.out.println("******国际化");
			I18nTest.testI18n((AbstractRefreshableConfigApplicationContext) context);
		}
		
	}

}
