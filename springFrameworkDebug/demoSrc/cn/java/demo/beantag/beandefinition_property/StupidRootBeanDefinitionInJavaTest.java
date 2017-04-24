package cn.java.demo.beantag.beandefinition_property;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.RuntimeBeanNameReference;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;
import org.springframework.core.Ordered;

import cn.java.demo.util.ApplicationContextUtil;

public class StupidRootBeanDefinitionInJavaTest {
	
	
	public void note(AbstractRefreshableConfigApplicationContext context){
		/**
			org.springframework.beans.factory.support.BeanDefinitionValueResolver.resolveValueIfNecessary(Object argName, Object value)
		 */
	}
	
	/**
	 * 测试 - 开始
	 * @param context
	 */
	public void testHelloWorld(AbstractRefreshableConfigApplicationContext context){
		
		BeanDefinitionRegistry registry = ApplicationContextUtil.tryCastTypeToBeanDefinitionRegistry(context);
		ConfigurableListableBeanFactory beanFactory = ApplicationContextUtil.tryCastTypeToConfigurableListableBeanFactory(context);
		
		if(registry==null || beanFactory==null ){
			return;
		}
		
		final String beanNameTemp0 = "testHelloWorld_beanNameTemp0";
		final String beanName = "testHelloWorld";
		
		// 注册
		{
			RootBeanDefinition beanDefinitionTemp0 = new RootBeanDefinition(FooService.class);
			beanDefinitionTemp0.setSource(null);
			beanDefinitionTemp0.setAutowireMode(RootBeanDefinition.AUTOWIRE_BY_NAME);
			beanDefinitionTemp0.getPropertyValues().add("field1", Ordered.HIGHEST_PRECEDENCE);
			beanDefinitionTemp0.getPropertyValues().add("field2", "testHelloWorld_beanDefinition0_field2Value");
			beanDefinitionTemp0.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
			registry.registerBeanDefinition(beanNameTemp0, beanDefinitionTemp0);
			
			RootBeanDefinition beanDefinition1 = new RootBeanDefinition(FooService.class);
			beanDefinition1.setSource(null);
			beanDefinition1.setAutowireMode(RootBeanDefinition.AUTOWIRE_BY_NAME);  // 感知setter方法的属性名，进行注入
			beanDefinition1.getPropertyValues().add("field1", 1);
			beanDefinition1.getPropertyValues().add("field2", "testHelloWorld_beanDefinition1_field2Value");
			beanDefinition1.getPropertyValues().add("fooService", new RuntimeBeanReference(beanNameTemp0)); // 引用 beanName0
			beanDefinition1.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
			beanDefinition1.setScope(BeanDefinition.SCOPE_SINGLETON); // 单例
			registry.registerBeanDefinition(beanName, beanDefinition1);
		}
		
		// 使用
		{
			FooService fooService0 = (FooService)beanFactory.getBean(beanName);
			if(fooService0!=null){
				System.out.println(fooService0.getField2());
			}
		}
		
	}
	
	
	/**
	 * 动态引用 - 不支持表达式
	 * 		注意：RuntimeBeanReference的返回结果是bean实例
	 * @param context
	 */
	public void testRuntimeBeanReferenceX(AbstractRefreshableConfigApplicationContext context){
		
		BeanDefinitionRegistry registry = ApplicationContextUtil.tryCastTypeToBeanDefinitionRegistry(context);
		ConfigurableListableBeanFactory beanFactory = ApplicationContextUtil.tryCastTypeToConfigurableListableBeanFactory(context);
		
		if(registry==null || beanFactory==null ){
			return;
		}
		
		final String beanNameTemp0 = "testRuntimeBeanReferencePropertyValue_beanNameTemp0";
		final String beanName = "testRuntimeBeanReferencePropertyValue";
		
		// 注册
		{
			RootBeanDefinition beanDefinitionTemp0 = new RootBeanDefinition(FooService.class);
			beanDefinitionTemp0.setSource(null);
			beanDefinitionTemp0.setAutowireMode(RootBeanDefinition.AUTOWIRE_BY_NAME);
			beanDefinitionTemp0.getPropertyValues().add("field1", Ordered.HIGHEST_PRECEDENCE);
			beanDefinitionTemp0.getPropertyValues().add("field2", "testHelloWorld_beanDefinition0_field2Value");
			beanDefinitionTemp0.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
			registry.registerBeanDefinition(beanNameTemp0, beanDefinitionTemp0);
			
			RootBeanDefinition beanDefinition1 = new RootBeanDefinition(FooService.class);
			beanDefinition1.setSource(null);
			beanDefinition1.setAutowireMode(RootBeanDefinition.AUTOWIRE_BY_NAME);  // 感知setter方法的属性名，进行注入
			beanDefinition1.getPropertyValues().add("field1", 1);
			beanDefinition1.getPropertyValues().add("field2", "testRuntimeBeanReferencePropertyValue_beanDefinition1_field2Value");
			beanDefinition1.getPropertyValues().add("fooService", new RuntimeBeanReference(beanNameTemp0)); // 引用 beanName0
			beanDefinition1.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
			beanDefinition1.setScope(BeanDefinition.SCOPE_SINGLETON); // 单例
			registry.registerBeanDefinition(beanName, beanDefinition1);
		}
		
		// 使用
		{
			System.out.println("-----testRuntimeBeanReferencePropertyValue------");
			FooService fooService1 = (FooService)beanFactory.getBean(beanName);
			if(fooService1!=null){
				System.out.println(fooService1.getField2());
				System.out.println(fooService1.getFooService().getField2());
			}
		}
	}
	
	/**
	 * 动态引用  -- 支持表达式 
	 * 		注意：RuntimeBeanNameReference的返回结果是bean的名称，并保证该bean存在。返回的不是bean的实例，而是名称
	 * @param context
	 */
	public void testRuntimeBeanNameReferenceX(AbstractRefreshableConfigApplicationContext context){
		
		BeanDefinitionRegistry registry = ApplicationContextUtil.tryCastTypeToBeanDefinitionRegistry(context);
		ConfigurableListableBeanFactory beanFactory = ApplicationContextUtil.tryCastTypeToConfigurableListableBeanFactory(context);
		
		if(registry==null || beanFactory==null ){
			return;
		}
		
		final String beanNameTemp0 = "testRuntimeBeanNameReferencePropertyValue_beanNameTemp0";
		final String beanNameTemp1 = "testRuntimeBeanNameReferencePropertyValue_beanNameTemp1";
		final String beanName = "testRuntimeBeanNameReferencePropertyValue";
		// 注册
		{
			// 注册bean定义1
			RootBeanDefinition beanDefinitionTemp0 = new RootBeanDefinition(FooService.class);
			beanDefinitionTemp0.setSource(null);
			beanDefinitionTemp0.setAutowireMode(RootBeanDefinition.AUTOWIRE_BY_NAME);
			beanDefinitionTemp0.getPropertyValues().add("field1", Ordered.HIGHEST_PRECEDENCE);
			beanDefinitionTemp0.getPropertyValues().add("field2", "testRuntimeBeanNameReferencePropertyValueTemp_beanDefinitionTemp0_field2Value");
			beanDefinitionTemp0.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
			registry.registerBeanDefinition(beanNameTemp0, beanDefinitionTemp0);
			
			// 注册bean定义2
			RootBeanDefinition beanDefinitionTemp1 = new RootBeanDefinition(FooService.class);
			beanDefinitionTemp1.setSource(null);
			beanDefinitionTemp1.setAutowireMode(RootBeanDefinition.AUTOWIRE_BY_NAME);
			beanDefinitionTemp1.getPropertyValues().add("field1", Ordered.HIGHEST_PRECEDENCE);
			beanDefinitionTemp1.getPropertyValues().add("field2", beanNameTemp0); // field2存放的是beanNameTemp0在bean容器中的名称
			beanDefinitionTemp1.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
			registry.registerBeanDefinition(beanNameTemp1, beanDefinitionTemp1);
			
			// 注册bean定义3
			RootBeanDefinition beanDefinition1 = new RootBeanDefinition(FooService.class);
			beanDefinition1.setSource(null);
			beanDefinition1.setAutowireMode(RootBeanDefinition.AUTOWIRE_BY_NAME);  // 感知setter方法的属性名，进行注入
			beanDefinition1.getPropertyValues().add("field1", 100);
			// #{testRuntimeBeanReferencePropertyValueTemp.field2} 使用beanDefinition0的属性field2值（要求以该返回值命名的bean存在），作为 beanDefinition1的属性field2的值
			beanDefinition1.getPropertyValues().add("field2", new RuntimeBeanNameReference("#{"+beanNameTemp1+".field2}")); // 返回值是 “testRuntimeBeanNameReferencePropertyValue_beanNameTemp0”，在容器中有注册
			beanDefinition1.getPropertyValues().add("fooService", new RuntimeBeanReference(beanNameTemp1)); 
			beanDefinition1.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
			beanDefinition1.setScope(BeanDefinition.SCOPE_SINGLETON); // 单例
			registry.registerBeanDefinition(beanName, beanDefinition1);
			/*
			 	RuntimeBeanNameReference 和 RuntimeBeanReference 的差异：
			 		RuntimeBeanNameReference的返回结果是bean的名称，并保证该bean存在。返回的不是bean的实例，而是名称。
			 		RuntimeBeanReference的返回结果是bean实例。
			 */
		}
		
		// 使用
		{
			System.out.println("-----testRuntimeBeanNameReferencePropertyValue------");
			FooService fooService1 = (FooService)beanFactory.getBean(beanName);
			if(fooService1!=null){
				System.out.println(fooService1.getField2());
				System.out.println(fooService1.getFooService().getField2());
			}
		}
	}
	
	/**
	 * 动态注册RootBeanDefinition，创建的是临时对象，即：通过beanFactory.getBean(...)不可以见
	 * @param context
	 */
	public void testBeanDefinitionHoldertX(AbstractRefreshableConfigApplicationContext context){
		
		BeanDefinitionRegistry registry = ApplicationContextUtil.tryCastTypeToBeanDefinitionRegistry(context);
		ConfigurableListableBeanFactory beanFactory = ApplicationContextUtil.tryCastTypeToConfigurableListableBeanFactory(context);
		
		if(registry==null || beanFactory==null ){
			return;
		}
		
		final String beanNameTemp0 = "testBeanDefinitionHoldertPropertyValue_beanDefinitionTemp0";
		final String beanName = "testBeanDefinitionHoldertPropertyValue";
		// 注册
		{
			RootBeanDefinition beanDefinitionTemp0 = new RootBeanDefinition(FooService.class);
			beanDefinitionTemp0.setSource(null);
			beanDefinitionTemp0.setAutowireMode(RootBeanDefinition.AUTOWIRE_BY_NAME);
			beanDefinitionTemp0.getPropertyValues().add("field1", Ordered.HIGHEST_PRECEDENCE);
			beanDefinitionTemp0.getPropertyValues().add("field2", "testBeanDefinitionHoldertPropertyValue_beanDefinitionTemp0_field2Value");
			beanDefinitionTemp0.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
			
			RootBeanDefinition beanDefinition2 = new RootBeanDefinition(FooService.class);
			beanDefinition2.setSource(null);
			beanDefinition2.setAutowireMode(RootBeanDefinition.AUTOWIRE_BY_NAME);  // 感知setter方法的属性名，进行注入
			beanDefinition2.getPropertyValues().add("field1", 1);
			beanDefinition2.getPropertyValues().add("field2", "testBeanDefinitionHoldertPropertyValue_beanDefinition2_field2Value");
			// 创建传的bean不是单例的,并且是临时对象, 添加RootBeanDefinition信息，并声明创建是bean的名称beanDefinitionTemp0
			beanDefinition2.getPropertyValues().add("fooService", new BeanDefinitionHolder(beanDefinitionTemp0,beanNameTemp0)); // !!!
			beanDefinition2.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
			registry.registerBeanDefinition(beanName, beanDefinition2);
			
		}
		
		// 使用
		{
			System.out.println("-----testBeanDefinitionHoldertPropertyValue------");
			FooService fooService2 = (FooService)beanFactory.getBean(beanName);
			if(fooService2!=null){
				System.out.println(fooService2.getField2());
				System.out.println(fooService2.getFooService().getField2());
			}
			
			// 创建处理的是临时对象，获取不到对象，会报出异常
//			FooService fooServiceTemp = (FooService)beanFactory.getBean(beanNameTemp0);
//			System.out.println(fooServiceTemp.getField2());	
		}
	}
	
	/**
	 * 
	 * @param context
	 */
	public void testBeanDefinitionX(AbstractRefreshableConfigApplicationContext context){
		
		BeanDefinitionRegistry registry = ApplicationContextUtil.tryCastTypeToBeanDefinitionRegistry(context);
		ConfigurableListableBeanFactory beanFactory = ApplicationContextUtil.tryCastTypeToConfigurableListableBeanFactory(context);
		
		if(registry==null || beanFactory==null ){
			return;
		}
		
		final String beanName = "testBeanDefinitionPropertyValue";
		
		// 注册
		{
			RootBeanDefinition beanDefinitionTemp0 = new RootBeanDefinition(FooService.class);
			beanDefinitionTemp0.setSource(null);
			beanDefinitionTemp0.setAutowireMode(RootBeanDefinition.AUTOWIRE_BY_NAME);
			beanDefinitionTemp0.getPropertyValues().add("field1", Ordered.HIGHEST_PRECEDENCE);
			beanDefinitionTemp0.getPropertyValues().add("field2", "testBeanDefinitionPropertyValue_beanDefinitionTemp0_field2Value");
			beanDefinitionTemp0.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
			
			RootBeanDefinition beanDefinition2 = new RootBeanDefinition(FooService.class);
			beanDefinition2.setSource(null);
			beanDefinition2.setAutowireMode(RootBeanDefinition.AUTOWIRE_BY_NAME);  // 感知setter方法的属性名，进行注入
			beanDefinition2.getPropertyValues().add("field1", 1);
			beanDefinition2.getPropertyValues().add("field2", "testBeanDefinitionPropertyValue_beanDefinition2_field2Value");
			// 创建传的bean不是单例的,并且是临时对象, 添加RootBeanDefinition信息，并声明创建是bean的名称beanDefinitionTemp0
			beanDefinition2.getPropertyValues().add("fooService", beanDefinitionTemp0); // !!!!
			beanDefinition2.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
			registry.registerBeanDefinition(beanName, beanDefinition2);
			
		}
		
		// 使用
		{
			System.out.println("-----testBeanDefinitionPropertyValue------");
			FooService fooService2 = (FooService)beanFactory.getBean(beanName);
			if(fooService2!=null){
				System.out.println(fooService2.getField2());
				System.out.println(fooService2.getFooService().getField2());
			}
			
		}
	}

	
	
	private static class FooService {
		private int field1;
		private String field2;
		private FooService fooService;
	
		public FooService getFooService() {
			return fooService;
		}

		public void setFooService(FooService fooService) {
			this.fooService = fooService;
		}

		public int getField1() {
			return field1;
		}

		public void setField1(int field1) {
			this.field1 = field1;
		}

		public String getField2() {
			return field2;
		}

		public void setField2(String field2) {
			this.field2 = field2;
		}

		public void method1(){
			System.out.println("---> this is in "+this.getClass().getName()+":method1()");
		}
		
		public void method2(){
			System.out.println("---> this is in "+this.getClass().getName()+":method2()");
		}
	}
}
