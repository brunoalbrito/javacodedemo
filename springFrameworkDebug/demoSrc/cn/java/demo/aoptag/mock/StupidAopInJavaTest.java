package cn.java.demo.aoptag.mock;

import java.util.ArrayList;
import java.util.List;

import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.aspectj.AspectJPointcutAdvisor;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.aop.aspectj.autoproxy.AspectJAwareAdvisorAutoProxyCreator;
import org.springframework.aop.config.AopConfigUtils;
import org.springframework.aop.config.MethodLocatingFactoryBean;
import org.springframework.aop.config.SimpleBeanFactoryAwareAspectInstanceFactory;
import org.springframework.aop.framework.autoproxy.InfrastructureAdvisorAutoProxyCreator;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.DefaultBeanNameGenerator;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;
import org.springframework.core.Ordered;

import cn.java.demo.aoptag.api.HelloService;
import cn.java.demo.beantag.beandefinition_property.StupidAspect4HelloService1;
import cn.java.demo.util.ApplicationContextUtil;

public class StupidAopInJavaTest {
	
	public void test(AbstractRefreshableConfigApplicationContext context) {
		System.out.println("-----StupidAopInJavaTest------");
		BeanDefinitionRegistry registry = ApplicationContextUtil.tryCastTypeToBeanDefinitionRegistry(context);
		ConfigurableListableBeanFactory beanFactory = ApplicationContextUtil.tryCastTypeToConfigurableListableBeanFactory(context);
		if(registry==null || beanFactory==null){
			return;
		}
		registerBeanPostProcessorAliasHook(registry); // 注册钩子
		
		String aspectName = this.getClass().getSimpleName() + "aspect4HelloService0"; // 通知接受者
		String pointcutBeanName0 = this.getClass().getSimpleName() + "pointcutRefId_0"; //　匹配规则
		String beanName = this.getClass().getSimpleName() + "beanName0"; //　匹配规则
		
		registerBeanToBeProxy(registry, beanName); // 将被代理的bean
		registerAspectAliasAccepter(registry,aspectName); // 注册通知接受者
		registerAdviceAliasRuleAccepterRelate(registry,aspectName,pointcutBeanName0); // 注册“通知接受者 - 方法”和“规则”的关系
		registerPointcutAliasRuleMatcher(registry,pointcutBeanName0); // 注册“规则”
		testGetBeanToBeProxy(beanFactory,beanName); // 尝试获取“bean”
	}
	
	/**
	 * 注册 BeanPostProcessor（钩子）
	 * @param registry
	 */
	private void registerBeanPostProcessorAliasHook(BeanDefinitionRegistry registry){
		// 注册 BeanPostProcessor（钩子），发现Advice的方式是：通过扫描实现Advice接口的bean对象
		{
			Class cls = AspectJAwareAdvisorAutoProxyCreator.class;
			if (registry.containsBeanDefinition(AopConfigUtils.AUTO_PROXY_CREATOR_BEAN_NAME)) { // 如果已经存在
				BeanDefinition apcDefinition = registry.getBeanDefinition(AopConfigUtils.AUTO_PROXY_CREATOR_BEAN_NAME);
				if (!cls.getName().equals(apcDefinition.getBeanClassName())) {
					int currentPriority = findPriorityForClass(apcDefinition.getBeanClassName()); // 获取指定类的权重
					int requiredPriority = findPriorityForClass(cls);
					if (currentPriority < requiredPriority) { //如果新来的权重比较高，那么进行覆盖
						apcDefinition.setBeanClassName(cls.getName());
					}
				}
			}
			else{
				RootBeanDefinition beanDefinition = new RootBeanDefinition(cls);
				beanDefinition.setSource(null);
				beanDefinition.getPropertyValues().add("order", Ordered.HIGHEST_PRECEDENCE);
				beanDefinition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
				registry.registerBeanDefinition(AopConfigUtils.AUTO_PROXY_CREATOR_BEAN_NAME, beanDefinition); // !!!注册bean定义
			}
			
			{ // 有配置"proxy-target-class"属性
				if (registry.containsBeanDefinition(AopConfigUtils.AUTO_PROXY_CREATOR_BEAN_NAME)) {
					BeanDefinition definition = registry.getBeanDefinition(AopConfigUtils.AUTO_PROXY_CREATOR_BEAN_NAME);
					definition.getPropertyValues().add("proxyTargetClass", Boolean.TRUE);
				}
			}
			
			{ // 有配置"expose-proxy"属性
				if (registry.containsBeanDefinition(AopConfigUtils.AUTO_PROXY_CREATOR_BEAN_NAME)) {
					BeanDefinition definition = registry.getBeanDefinition(AopConfigUtils.AUTO_PROXY_CREATOR_BEAN_NAME);
					definition.getPropertyValues().add("exposeProxy", Boolean.TRUE);
				}
			}
		}
	}
	
	/**
	 * 定义“通知接受者”
	 * @param registry
	 * @param aspectName
	 * @param pointcutBeanName0
	 */
	private void registerAdviceAliasRuleAccepterRelate(BeanDefinitionRegistry registry,String aspectName,String pointcutBeanName0){
		{ // 定义“通知接受者0”
			String adviceMethod = "aspectMethodBefore";// 要“被通知”的方法
			
			// create the method factory bean
			RootBeanDefinition methodDefinition = new RootBeanDefinition(MethodLocatingFactoryBean.class);
			methodDefinition.getPropertyValues().add("targetBeanName", aspectName); // 要“接受报告的bean对象”
			methodDefinition.getPropertyValues().add("methodName", adviceMethod);
			methodDefinition.setSynthetic(true);
			
			// create instance factory definition
			RootBeanDefinition aspectFactoryDef =
					new RootBeanDefinition(SimpleBeanFactoryAwareAspectInstanceFactory.class);
			aspectFactoryDef.getPropertyValues().add("aspectBeanName", aspectName);
			aspectFactoryDef.setSynthetic(true);
			
			/*
				<before ...> == org.springframework.aop.aspectj.AspectJMethodBeforeAdvice
				<after ...> === org.springframework.aop.aspectj.AspectJAfterAdvice
				<after-returning ...> === org.springframework.aop.aspectj.AspectJAfterReturningAdvice
				<after-throwing ...> === org.springframework.aop.aspectj.AspectJAfterThrowingAdvice
				<around ...> === org.springframework.aop.aspectj.AspectJAfterThrowingAdvice
			 */
			RootBeanDefinition adviceDefinition = new RootBeanDefinition(org.springframework.aop.aspectj.AspectJMethodBeforeAdvice.class);
			adviceDefinition.setSource(null);
			adviceDefinition.getPropertyValues().add("aspectName", aspectName);  // 要“接受报告的bean对象”
//			adviceDefinition.getPropertyValues().add("declarationOrder", order);
//			adviceDefinition.getPropertyValues().add("returningName", adviceElement.getAttribute(RETURNING));
//			adviceDefinition.getPropertyValues().add("throwingName", adviceElement.getAttribute(THROWING));
//			adviceDefinition.getPropertyValues().add("argumentNames", adviceElement.getAttribute(ARG_NAMES));
			// 构造函数的bean信息
			ConstructorArgumentValues cav = adviceDefinition.getConstructorArgumentValues();
			cav.addIndexedArgumentValue(0, methodDefinition); // 第一个参数
			RuntimeBeanReference pointcutRef = new RuntimeBeanReference((String) pointcutBeanName0); // 引用pointcutBeanName0匹配规则
			cav.addIndexedArgumentValue(1, pointcutRef); // 第二个参数
			cav.addIndexedArgumentValue(2, aspectFactoryDef); // 第三个参数
			
			// 
			RootBeanDefinition advisorDefinition = new RootBeanDefinition(AspectJPointcutAdvisor.class);
			advisorDefinition.setSource(null);
			advisorDefinition.getConstructorArgumentValues().addGenericArgumentValue(adviceDefinition);
//			advisorDefinition.getPropertyValues().add("order", aspectElement.getAttribute(ORDER_PROPERTY));
			registry.registerBeanDefinition(generateBeanName(advisorDefinition,registry), advisorDefinition); // !!!注册bean定义，bean的名称是自动生成的
			
		}
		
		{ // 定义“通知接受者1”
			String adviceMethod = "aspectMethodAfter";// 要“被通知”的方法
			
			// create the method factory bean
			RootBeanDefinition methodDefinition = new RootBeanDefinition(MethodLocatingFactoryBean.class);
			methodDefinition.getPropertyValues().add("targetBeanName", aspectName); // 要“接受报告的bean对象”
			methodDefinition.getPropertyValues().add("methodName", adviceMethod);
			methodDefinition.setSynthetic(true);
			
			// create instance factory definition
			RootBeanDefinition aspectFactoryDef = new RootBeanDefinition(SimpleBeanFactoryAwareAspectInstanceFactory.class);
			aspectFactoryDef.getPropertyValues().add("aspectBeanName", aspectName);
			aspectFactoryDef.setSynthetic(true);
			
			// 
			RootBeanDefinition adviceDefinition = new RootBeanDefinition(org.springframework.aop.aspectj.AspectJAfterAdvice.class);
			adviceDefinition.setSource(null);
			adviceDefinition.getPropertyValues().add("aspectName", aspectName);  // 要“接受报告的bean对象”
//			adviceDefinition.getPropertyValues().add("declarationOrder", order);
//			adviceDefinition.getPropertyValues().add("returningName", adviceElement.getAttribute(RETURNING));
//			adviceDefinition.getPropertyValues().add("throwingName", adviceElement.getAttribute(THROWING));
//			adviceDefinition.getPropertyValues().add("argumentNames", adviceElement.getAttribute(ARG_NAMES));
			// 构造函数的bean信息
			ConstructorArgumentValues cav = adviceDefinition.getConstructorArgumentValues();
			cav.addIndexedArgumentValue(0, methodDefinition); // 第一个参数
			RuntimeBeanReference pointcutRef = new RuntimeBeanReference((String) pointcutBeanName0); // 引用pointcutBeanName0匹配规则
			cav.addIndexedArgumentValue(1, pointcutRef); // 第二个参数
			cav.addIndexedArgumentValue(2, aspectFactoryDef); // 第三个参数
			
			// 
			RootBeanDefinition advisorDefinition = new RootBeanDefinition(AspectJPointcutAdvisor.class); 
			advisorDefinition.setSource(null);
			advisorDefinition.getConstructorArgumentValues().addGenericArgumentValue(adviceDefinition);
//			advisorDefinition.getPropertyValues().add("order", aspectElement.getAttribute(ORDER_PROPERTY));
			registry.registerBeanDefinition(generateBeanName(advisorDefinition,registry), advisorDefinition); // !!!注册bean定义，bean的名称是自动生成的
			
		}
	}
	
	/**
	 * 定义匹配规则
	 * @param registry
	 * @param pointcutBeanName0
	 */
	private void registerPointcutAliasRuleMatcher(BeanDefinitionRegistry registry,String pointcutBeanName0){
		{ // 定义“匹配规则0”
			AbstractBeanDefinition pointcutDefinition = null;
			pointcutDefinition = new RootBeanDefinition(AspectJExpressionPointcut.class);
			pointcutDefinition.setScope(BeanDefinition.SCOPE_PROTOTYPE);
			pointcutDefinition.setSynthetic(true);
			pointcutDefinition.getPropertyValues().add("expression", "execution(* cn.java.demo.aoptag.bean.HelloServiceImpl2.method2(..))"); // 匹配表达式
			pointcutDefinition.setSource(null);
			registry.registerBeanDefinition(pointcutBeanName0, pointcutDefinition); // !!!注册bean定义
		}
		
		{ // 定义“匹配规则1”
			String pointcutBeanName1 = this.getClass().getSimpleName() + "pointcutRefId_1"; //　匹配规则
			AbstractBeanDefinition pointcutDefinition = null;
			pointcutDefinition = new RootBeanDefinition(AspectJExpressionPointcut.class);
			pointcutDefinition.setScope(BeanDefinition.SCOPE_PROTOTYPE);
			pointcutDefinition.setSynthetic(true);
			pointcutDefinition.getPropertyValues().add("expression", "execution(* cn.java.demo.aoptag.bean.HelloServiceImpl2.method2(..))"); // 匹配表达式
			pointcutDefinition.setSource(null);
			registry.registerBeanDefinition(pointcutBeanName1, pointcutDefinition); // !!!注册bean定义
		}
	}
	
	/**
	 * 通知接受者
	 * @param registry
	 * @param aspectName
	 */
	private void registerAspectAliasAccepter(BeanDefinitionRegistry registry,String aspectName){
		RootBeanDefinition beanDefinitionTemp0 = new RootBeanDefinition(StupidAspect4HelloService1.class);
		beanDefinitionTemp0.setSource(null);
		beanDefinitionTemp0.setAutowireMode(RootBeanDefinition.AUTOWIRE_BY_NAME);
		beanDefinitionTemp0.setRole(BeanDefinition.ROLE_APPLICATION);
		registry.registerBeanDefinition(aspectName, beanDefinitionTemp0);
	}
	
	/**
	 * 将被代理的bean
	 * @param registry
	 * @param aspectName
	 */
	private void registerBeanToBeProxy(BeanDefinitionRegistry registry,String beanName){
		RootBeanDefinition beanDefinitionTemp0 = new RootBeanDefinition(cn.java.demo.aoptag.bean.HelloServiceImpl3.class);
		beanDefinitionTemp0.setSource(null);
		beanDefinitionTemp0.setAutowireMode(RootBeanDefinition.AUTOWIRE_BY_NAME);
		beanDefinitionTemp0.setRole(BeanDefinition.ROLE_APPLICATION);
		beanDefinitionTemp0.setScope(RootBeanDefinition.SCOPE_PROTOTYPE);
		registry.registerBeanDefinition(beanName, beanDefinitionTemp0);
	}
	
	/**
	 * 获取被代理的bean
	 * @param registry
	 * @param beanName
	 */
	private void testGetBeanToBeProxy(ConfigurableListableBeanFactory beanFactory,String beanName){
		HelloService helloService = (HelloService)beanFactory.getBean(beanName);
		if(helloService!=null){
			helloService.method2();
		}
	}
	
	/**
	 * 生成Bean名
	 * @param beanDefinition
	 * @param registry
	 * @return
	 */
	private static String generateBeanName(BeanDefinition beanDefinition,BeanDefinitionRegistry registry) {
		BeanNameGenerator beanNameGenerator = new DefaultBeanNameGenerator();
		return beanNameGenerator.generateBeanName(beanDefinition, registry);
	}
	
	/**
	 * 获取类的权重
	 * @param className
	 * @return
	 */
	private static int findPriorityForClass(String className) {
		for (int i = 0; i < APC_PRIORITY_LIST.size(); i++) {
			Class<?> clazz = APC_PRIORITY_LIST.get(i);
			if (clazz.getName().equals(className)) {
				return i;
			}
		}
		throw new IllegalArgumentException(
				"Class name [" + className + "] is not a known auto-proxy creator class");
	}
	
	/**
	 * 获取类的权重
	 * @param className
	 * @return
	 */
	private static int findPriorityForClass(Class<?> clazz) {
		return APC_PRIORITY_LIST.indexOf(clazz);
	}
	
	/**
	 * Stores the auto proxy creator classes in escalation order.
	 */
	private static final List<Class<?>> APC_PRIORITY_LIST = new ArrayList<Class<?>>();

	/**
	 * Setup the escalation list.
	 */
	static {
		APC_PRIORITY_LIST.add(InfrastructureAdvisorAutoProxyCreator.class);
		APC_PRIORITY_LIST.add(AspectJAwareAdvisorAutoProxyCreator.class);
		APC_PRIORITY_LIST.add(AnnotationAwareAspectJAutoProxyCreator.class);
	}

}
