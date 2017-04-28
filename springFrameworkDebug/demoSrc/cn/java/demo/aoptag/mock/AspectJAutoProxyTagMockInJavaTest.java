package cn.java.demo.aoptag.mock;

import java.util.ArrayList;
import java.util.List;

import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.aop.aspectj.autoproxy.AspectJAwareAdvisorAutoProxyCreator;
import org.springframework.aop.config.AopConfigUtils;
import org.springframework.aop.framework.autoproxy.InfrastructureAdvisorAutoProxyCreator;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;
import org.springframework.core.Ordered;

import cn.java.demo.util.ApplicationContextUtil;

public class AspectJAutoProxyTagMockInJavaTest {



	public void test(AbstractRefreshableConfigApplicationContext context) {
		System.out.println("-----"+this.getClass().getSimpleName()+"------");
		BeanDefinitionRegistry registry = ApplicationContextUtil.tryCastTypeToBeanDefinitionRegistry(context);
		ConfigurableListableBeanFactory beanFactory = ApplicationContextUtil.tryCastTypeToConfigurableListableBeanFactory(context);
		if(registry==null || beanFactory==null){
			return;
		}
		registerBeanPostProcessorAliasHook(context); // 注册钩子
		addIncludePatterns(context); // 设置匹配表达式
	}

	/**
	 * 设置匹配表达式
	 * @param context
	 */
	private void addIncludePatterns(AbstractRefreshableConfigApplicationContext context) {
		BeanDefinitionRegistry registry = ApplicationContextUtil.tryCastTypeToBeanDefinitionRegistry(context);
		ConfigurableListableBeanFactory beanFactory = ApplicationContextUtil.tryCastTypeToConfigurableListableBeanFactory(context);
		if(registry==null || beanFactory==null){
			return;
		}
		BeanDefinition beanDef = registry.getBeanDefinition(AopConfigUtils.AUTO_PROXY_CREATOR_BEAN_NAME);

		ManagedList<TypedStringValue> includePatterns = new ManagedList<TypedStringValue>();
		TypedStringValue valueHolder = new TypedStringValue("name0");
		includePatterns.add(valueHolder);
		valueHolder = new TypedStringValue("name1");
		includePatterns.add(valueHolder);

		beanDef.getPropertyValues().add("includePatterns", includePatterns);
	}
	
	
	/**
	 * 注册 BeanPostProcessor（钩子）
	 * @param registry
	 */
	public static final String PROXY_TARGET_CLASS_ATTRIBUTE = "proxy-target-class";
	private static final String EXPOSE_PROXY_ATTRIBUTE = "expose-proxy";
	private void registerBeanPostProcessorAliasHook(AbstractRefreshableConfigApplicationContext context) {

		BeanDefinitionRegistry registry = ApplicationContextUtil.tryCastTypeToBeanDefinitionRegistry(context);
		ConfigurableListableBeanFactory beanFactory = ApplicationContextUtil.tryCastTypeToConfigurableListableBeanFactory(context);
		if(registry==null || beanFactory==null){
			return;
		}

		// 注册 BeanPostProcessor（钩子），发现Advice的方式是：通过扫描实现Advice接口的bean对象
		{
			Class cls = AnnotationAwareAspectJAutoProxyCreator.class; //  不同的类
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
				boolean proxyTargetClass = false;
				// proxyTargetClass = Boolean.valueOf(element.getAttribute(PROXY_TARGET_CLASS_ATTRIBUTE));
				if (proxyTargetClass) {
					if (registry.containsBeanDefinition(AopConfigUtils.AUTO_PROXY_CREATOR_BEAN_NAME)) {
						BeanDefinition definition = registry.getBeanDefinition(AopConfigUtils.AUTO_PROXY_CREATOR_BEAN_NAME);
						definition.getPropertyValues().add("proxyTargetClass", Boolean.TRUE);
					}
				}
			}

			{ // 有配置"expose-proxy"属性
				boolean exposeProxy = false;
				// exposeProxy = Boolean.valueOf(element.getAttribute(EXPOSE_PROXY_ATTRIBUTE));
				if (exposeProxy) {
					if (registry.containsBeanDefinition(AopConfigUtils.AUTO_PROXY_CREATOR_BEAN_NAME)) {
						BeanDefinition definition = registry.getBeanDefinition(AopConfigUtils.AUTO_PROXY_CREATOR_BEAN_NAME);
						definition.getPropertyValues().add("exposeProxy", Boolean.TRUE);
					}
				}
			}
		}

		{
			PostProcessorRegistrationDelegate.registerBeanPostProcessors(beanFactory,context);
		}

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
