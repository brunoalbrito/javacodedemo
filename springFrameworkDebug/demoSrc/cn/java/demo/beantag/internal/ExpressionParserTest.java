package cn.java.demo.beantag.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanExpressionContext;
import org.springframework.beans.factory.config.BeanExpressionResolver;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.expression.StandardBeanExpressionResolver;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.web.context.request.RequestScope;

import cn.java.demo.util.ApplicationContextUtil;

public class ExpressionParserTest {

	public void note() {
		/*
		 * org.springframework.expression.common.CompositeStringExpression.
		 * getValue(EvaluationContext context)
		 * org.springframework.expression.common.LiteralExpression.getValue(
		 * context, String.class) --- 简单文本
		 * org.springframework.expression.spel.standard.SpelExpression.getValue(
		 * context, String.class) --- 抽象语法树
		 */
	}

	/**
	 * 解析表达式  -- 返回字符串
	 * 
	 * @param context
	 * @param scope
	 */
	public static void testStandardBeanExpressionResolverResultString(
			AbstractRefreshableConfigApplicationContext context) {
		System.out.println("--------表达式解析器---------");
		ConfigurableListableBeanFactory beanFactory = ApplicationContextUtil.getBeanFactoryAndTryCastTypeToConfigurableListableBeanFactory(context);
		if (beanFactory == null) {
			return;
		}

		Scope scope = beanFactory.getRegisteredScope(BeanDefinition.SCOPE_SINGLETON); // 在单例作用域里面获取对象
		BeanExpressionResolver beanExpressionResolver = new StandardBeanExpressionResolver(beanFactory.getBeanClassLoader());
		Object result = beanExpressionResolver.evaluate("bean_#{(1+2)-(2+(2*1/2))}",new BeanExpressionContext(beanFactory, scope));
		System.out.println(result);
	}

	/**
	 * 解析表达式  -- 返回bean实例
	 * @param context
	 */
	public static void testStandardBeanExpressionResolverResultBeanObject(
				AbstractRefreshableConfigApplicationContext context) {
		System.out.println("------表达式解析器，使用获取bean对象，获取对象属性-------");
		BeanDefinitionRegistry registry = ApplicationContextUtil.getBeanFactoryAndTryCastTypeToBeanDefinitionRegistry(context);
		ConfigurableListableBeanFactory beanFactory = ApplicationContextUtil.getBeanFactoryAndTryCastTypeToConfigurableListableBeanFactory(context);

		if (registry == null || beanFactory == null) {
			return;
		}

		{ // 创建一个实例
			String beanName0 = "testStandardBeanExpressionResolverResultBeanObject_0";
			RootBeanDefinition beanDefinition0 = new RootBeanDefinition(FooService.class);
			beanDefinition0.setSource(null);
			beanDefinition0.setScope(BeanDefinition.SCOPE_SINGLETON);
			beanDefinition0.setAutowireMode(RootBeanDefinition.AUTOWIRE_BY_NAME);
			beanDefinition0.getPropertyValues().add("field1", Ordered.HIGHEST_PRECEDENCE);
			beanDefinition0.getPropertyValues().add("field2", "beanDefinition0_field2Value");
			beanDefinition0.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
			registry.registerBeanDefinition(beanName0, beanDefinition0);
		}

		Scope scope = beanFactory.getRegisteredScope(BeanDefinition.SCOPE_SINGLETON); // 在单例作用域里面获取对象
		BeanExpressionResolver beanExpressionResolver = new StandardBeanExpressionResolver(	beanFactory.getBeanClassLoader());
		
		// 如下返回字符串
		Object result =  beanExpressionResolver.evaluate("testStandardBeanExpressionResolverResultBeanObject_0",new BeanExpressionContext(beanFactory, scope));
		System.out.println("testStandardBeanExpressionResolverResultBeanObject_0返回字符串  = " + result);
		result =  beanExpressionResolver.evaluate("#{'testStandardBeanExpressionResolverResultBeanObject_0'}",new BeanExpressionContext(beanFactory, scope));
		System.out.println("#{'testStandardBeanExpressionResolverResultBeanObject_0'}返回字符串  = " + result);
		result =  beanExpressionResolver.evaluate("#{\"testStandardBeanExpressionResolverResultBeanObject_0\"}",new BeanExpressionContext(beanFactory, scope));
		System.out.println("#{\"testStandardBeanExpressionResolverResultBeanObject_0\"}返回字符串  = " + result);
		
		// 如下返回对象
		FooService fooService =  (FooService)beanExpressionResolver.evaluate("#{beanFactory.testStandardBeanExpressionResolverResultBeanObject_0}",new BeanExpressionContext(beanFactory, scope));
		System.out.println("#{beanFactory.testStandardBeanExpressionResolverResultBeanObject_0}返回对象 ， " + fooService.getField2());
		fooService =  (FooService)beanExpressionResolver.evaluate("#{testStandardBeanExpressionResolverResultBeanObject_0}",new BeanExpressionContext(beanFactory, scope));
		System.out.println("#{testStandardBeanExpressionResolverResultBeanObject_0}返回对象 ， " + fooService.getField2());
		
		// 访问对象的属性
		result = beanExpressionResolver.evaluate("#{testStandardBeanExpressionResolverResultBeanObject_0.map['key1']}",new BeanExpressionContext(beanFactory, scope));
		System.out.println(result);
		
		result = beanExpressionResolver.evaluate("#{testStandardBeanExpressionResolverResultBeanObject_0.list[0]}",new BeanExpressionContext(beanFactory, scope));
		System.out.println(result);
		
		result = beanExpressionResolver.evaluate("#{testStandardBeanExpressionResolverResultBeanObject_0.field2}",new BeanExpressionContext(beanFactory, scope));
		System.out.println(result);
	}
	
	private static class FooService {
		private int field1;
		private String field2;
		private List list;
		private Map map;
		private FooService fooService;
		
		public Map getMap() {
			return new HashMap(){
				{
					put("key1","key1_value");
					put("key2","key2_value");
				}
			};
		}

		public void setMap(Map map) {
			this.map = map;
		}

		

		public List getList() {
			return new ArrayList(){
				{
					add("listItem1");
					add("listItem2");
				}
			};
		}

		public void setList(List list) {
			this.list = list;
		}

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

		public void method1() {
			System.out.println("---> this is in " + this.getClass().getName() + ":method1()");
		}

		public void method2() {
			System.out.println("---> this is in " + this.getClass().getName() + ":method2()");
		}
	}

}
