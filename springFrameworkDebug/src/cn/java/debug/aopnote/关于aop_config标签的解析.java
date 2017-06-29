package cn.java.debug.aopnote;

import org.springframework.aop.config.AopConfigUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.ParserContext;

/*
	<aop:config>
	<aop:aspectj-autoproxy>
	<aop:scoped-proxy>
	<aop:spring-configured>
	
	<aop:config proxy-target-class="false" expose-proxy="false">
		<aop:pointcut id="" expression="" />
		<aop:advisor id="" advice-ref="" pointcut="" pointcut-ref="" order="" />
		<aop:aspect id="" ref="" order="">
			<aop:pointcut id="" expression=""></aop:pointcut>
			<aop:declare-parents types-matching="" implement-interface="" default-impl="" delegate-ref="" />
			<aop:before pointcut="" pointcut-ref="" method="" arg-names="" />
			<aop:after pointcut="" pointcut-ref="" method="" arg-names="" />
			<aop:after-returning pointcut="" pointcut-ref="" method="" arg-names="" />
			<aop:after-throwing   pointcut="" pointcut-ref="" method="" arg-names="" />
			<aop:around pointcut="" pointcut-ref="" method="" arg-names="" ></aop:around>
		</aop:aspect>
	</aop:config>
	
	<aop:aspectj-autoproxy proxy-target-class="false" expose-proxy="false">
		<aop:include name="" />
	</aop:aspectj-autoproxy>
	
	<aop:scoped-proxy  proxy-target-class="true" />
	
	<aop:spring-configured 废弃/>
	
	org.springframework.aop.config.AopNamespaceHandler 标签处理器
*/
public class 关于aop_config标签的解析 {
	
	/**
	 * <aop:config>
	 */
	private static class AopConfig标签 {
		
		public static final String AUTO_PROXY_CREATOR_BEAN_NAME =
				"org.springframework.aop.config.internalAutoProxyCreator";
		public static void debug() {
			/*
			 	// registry == org.springframework.beans.factory.support.DefaultListableBeanFactory 
			 	
			 	Class<?> cls = AspectJAwareAdvisorAutoProxyCreator.class;
				RootBeanDefinition beanDefinition = new RootBeanDefinition(cls);
				beanDefinition.setSource(null);
				beanDefinition.getPropertyValues().add("order", Ordered.HIGHEST_PRECEDENCE);
				beanDefinition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
				registry.registerBeanDefinition(AUTO_PROXY_CREATOR_BEAN_NAME, beanDefinition); // 注册bean的定义
			 */
			/*
			 	<aop:config proxy-target-class="false" expose-proxy="false">
			 		<aop:pointcut id="" expression="" /> --- 匹配条件 --- org.springframework.aop.aspectj.AspectJExpressionPointcut
			 		<aop:advisor id="" advice-ref="" pointcut="" pointcut-ref="" order="" />  --- org.springframework.aop.support.DefaultBeanFactoryPointcutAdvisor
			 		<aop:aspect id="" ref="" order="">
			 			<aop:pointcut id="" expression=""></aop:pointcut>  ---- org.springframework.aop.aspectj.AspectJExpressionPointcut
			 			<aop:declare-parents types-matching="" implement-interface="" default-impl="" delegate-ref="" />
			 			<aop:before pointcut="" pointcut-ref="" method="" arg-names="" /> --- 符合<aop:before pointcut-ref="">条件的bean调用，就通知<aop:aspect ref="">指定对象的<aop:before method=""> --- org.springframework.aop.aspectj.AspectJPointcutAdvisor
			 			<aop:after pointcut="" pointcut-ref="" method="" arg-names="" />  --- org.springframework.aop.aspectj.AspectJPointcutAdvisor
			 			<aop:after-returning pointcut="" pointcut-ref="" method="" arg-names="" />
			 			<aop:after-throwing   pointcut="" pointcut-ref="" method="" arg-names="" />
			 			<aop:around pointcut="" pointcut-ref="" method="" arg-names="" ></aop:around>
			 		</aop:aspect>
			 	</aop:config>
			 	
			 	
			 */
			/*
	 			
	 			
	 			// 一个<aop:before>标签对应一个 org.springframework.aop.aspectj.AspectJPointcutAdvisor 对象
	 			// <aop:before>标签对应的advice是：org.springframework.aop.aspectj.AspectJMethodBeforeAdvice
	 			advisor(自动生成的beanName) = org.springframework.aop.aspectj.AspectJPointcutAdvisor
				{
					pointcut : org.springframework.aop.aspectj.AspectJExpressionPointcut对象
					{
						expression : "execution(* cn.java.demo.aoptag.bean.HelloServiceImpl4MockAopInJava.method2(..))"
					},
					advice ： org.springframework.aop.aspectj.AspectJMethodBeforeAdvice
					{
						aspectName : aspectName4AcceptAdvice_通知接受者的beanName,
						aspectJBeforeAdviceMethod: org.springframework.aop.config.MethodLocatingFactoryBean对象
						{
							targetBeanName : aspectName4AcceptAdvice_通知接受者的beanName,
							methodName : adviceMethod_要“被通知”的方法名
						},
						pointcut : org.springframework.aop.aspectj.AspectJExpressionPointcut对象
						{
							expression : "execution(* cn.java.demo.aoptag.bean.HelloServiceImpl4MockAopInJava.method2(..))"
						},
						aspectInstanceFactory : org.springframework.aop.config.SimpleBeanFactoryAwareAspectInstanceFactory对象
						{
							aspectBeanName : aspectName4AcceptAdvice_通知接受者的beanName
						},
					}
				}
				
				// <aop:advisor id="aspectJPointcutAdvisor00" pointcut-ref="aspectJExpressionPointcut01" advice-ref="aspectJMethodBeforeAdviceMock"  order="1" />
				// 一个<aop:advisor>标签，对应一个 org.springframework.aop.support.DefaultBeanFactoryPointcutAdvisor 对象
				advisor(定义的beanName) = org.springframework.aop.support.DefaultBeanFactoryPointcutAdvisor 
				{
					adviceBeanName : aspectName4AcceptAdvice_通知接受者的beanName
					pointcut : org.springframework.aop.aspectj.AspectJExpressionPointcut对象
					{
						expression : "execution(* cn.java.demo.aoptag.bean.HelloServiceImpl4MockAopInJava.method2(..))"
					}
				}
			 */
		}
		
		/**
		 * 使用类代理---1
		 * @param registry
		 */
		public static void forceAutoProxyCreatorToUseClassProxying(BeanDefinitionRegistry registry) {
			if (registry.containsBeanDefinition(AUTO_PROXY_CREATOR_BEAN_NAME)) {
				BeanDefinition definition = registry.getBeanDefinition(AUTO_PROXY_CREATOR_BEAN_NAME);
				definition.getPropertyValues().add("proxyTargetClass", Boolean.TRUE);
			}
		}
		
		/**
		 * 使用类代理---2
		 * @param registry
		 */
		public static void forceAutoProxyCreatorToExposeProxy(BeanDefinitionRegistry registry) {
			if (registry.containsBeanDefinition(AUTO_PROXY_CREATOR_BEAN_NAME)) {
				BeanDefinition definition = registry.getBeanDefinition(AUTO_PROXY_CREATOR_BEAN_NAME);
				definition.getPropertyValues().add("exposeProxy", Boolean.TRUE);
			}
		}
		
		/**
		 * 注册成组件到 ParserContext---3
		 * @param beanDefinition
		 * @param parserContext
		 */
		private static void registerComponentIfNecessary(BeanDefinition beanDefinition, ParserContext parserContext) {
			if (beanDefinition != null) {
				BeanComponentDefinition componentDefinition =
						new BeanComponentDefinition(beanDefinition, AopConfigUtils.AUTO_PROXY_CREATOR_BEAN_NAME);
				// org.springframework.beans.factory.xml.ParserContext
				parserContext.registerComponent(componentDefinition); // 注册成组件到 ParserContext
			}
		}
	}
	
	
}
