package cn.java.aopnote;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

public class 关于aop_config标签的数据结构 {
	public static void main(String[] args) throws Exception {
		/**
		 	
		 	------------------------------
		 	org.springframework.aop.framework.autoproxy.InfrastructureAdvisorAutoProxyCreator 权重最低
		 	org.springframework.aop.framework.autoproxy.AspectJAwareAdvisorAutoProxyCreator    <config>
		 	org.springframework.aop.framework.autoproxy.AnnotationAwareAspectJAutoProxyCreator 权重最高 <aspectj-autoproxy>
		 	------------------------------
		 	org.springframework.beans.factory.xml.DefaultBeanDefinitionDocumentReader.parseBeanDefinitions(...)
		 		org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.parseCustomElement(...) 自定义标签
				 	org.springframework.aop.config.AopNamespaceHandler.parse(..) 解析
				 		org.springframework.aop.config.ConfigBeanDefinitionParser.parse(Element element, ParserContext parserContext)
				org.springframework.beans.factory.xml.DefaultBeanDefinitionDocumentReader.parseDefaultElement(...) 默认标签
					org.springframework.beans.factory.xml.DefaultBeanDefinitionDocumentReader.processBeanDefinition(...) 装饰器
						org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.decorateBeanDefinitionIfRequired(...)
		 	---------------
		 	
		 	org.springframework.beans.factory.parsing.CompositeComponentDefinition { 
		 		 name : "aop:config",
		 		 source : null,
		 		 nestedComponents : [
		 		 	org.springframework.beans.factory.parsing.BeanComponentDefinition {
		 		 		beanDefinition : RootBeanDefinition {  // 这个对象会注册到全局bean容器中（beanName是org.springframework.aop.config.internalAutoProxyCreator常量） <aop:config>
		 		 			beanClass : org.springframework.aop.aspectj.AspectJAwareAdvisorAutoProxyCreator.class, // 这个有可能被替换掉
		 		 			source : null,
		 		 			role :  BeanDefinition.ROLE_INFRASTRUCTURE,
		 		 			propertyValues : {
		 		 				"order" : Ordered.HIGHEST_PRECEDENCE,
		 		 				// "proxyTargetClass" : true,
		 		 				// "exposeProxy" : true
		 		 			}
		 		 		},
		 		 		beanName : "org.springframework.aop.config.internalAutoProxyCreator常量",
		 		 		nestedComponents : [
			 		 		
		 		 		]
		 		 	},
		 		 	org.springframework.aop.config.AspectComponentDefinition  { // <aop:aspect id="aspectId_1" ref="aspect4HelloService1">
				 		name : "aspectId_1" // 由<aop:aspect  id="aspectId_1">  决定
				 		beanDefinitions : [　// 
				 			RootBeanDefinition = { // 这个对象会注册到全局bean容器中（beanName是自动生成的） <aop:before method="aspectMethodBefore" pointcut-ref="pointcutRefId_1" />
						 		beanClass : org.springframework.aop.aspectj.AspectJPointcutAdvisor.class,
						 		constructorArgumentValues : { // 构造函数的参数描述
						 			genericArgumentValues : [
						 				构造函数参数 ValueHolder.value : RootBeanDefinition {
			 		 						beanClass : org.springframework.aop.aspectj.AspectJMethodBeforeAdvice.class,  // 由<aop:before>决定
					 						propertyValues : { // 属性  org.springframework.beans.MutablePropertyValues
					 							aspectName :  要“接受报告的bean对象”,  // 要“接受报告的bean对象”，由<aop:aspect ref="aspect4HelloService1">决定
					 							declarationOrder : "0",
					 							returningName : "returningName0",
					 							throwingName : "throwingName0",
					 							argumentNames : "argumentNames0"
					 						}
					 						constructorArgumentValues : { // 构造函数的参数描述
					 							indexedArgumentValues : [
					 								构造函数参数0 : RootBeanDefinition{
					 										beanClass : org.springframework.aop.config.MethodLocatingFactoryBean.class,
					 										synthetic : true,
						 									propertyValues : {
						 										targetBeanName :  要“接受报告的bean对象”,  // 要“接受报告的bean对象”，由 <aop:aspect ref="aspect4HelloService1">决定
						 										methodName : "aspectMethodBefore"  // 要通知的方法, <aop:before method="aspectMethodBefore" /> 决定
						 									}
					 								},
					 								构造函数参数1 : new RuntimeBeanReference((String) "pointcutRefId_1"),  // 由<aop:before pointcut-ref="pointcutRefId_1" /> 决定
					 								构造函数参数2 : RootBeanDefinition{
					 										beanClass : org.springframework.aop.config.SimpleBeanFactoryAwareAspectInstanceFactory.class,
					 										synthetic : true,
						 									propertyValues : {
						 										targetBeanName :  要“接受报告的bean对象”, // 由<aop:aspect ref="aspect4HelloService1">决定
						 									}
					 								}
					 							]
					 						}
					 					}
						 			]
						 		}
						 	},
				 			RootBeanDefinition = { // 这个对象会注册到全局bean容器中（beanName是自动生成的） <aop:after method="aspectMethodAfter" pointcut-ref="pointcutRefId_1" />
						 		beanClass : org.springframework.aop.aspectj.AspectJPointcutAdvisor,
						 		constructorArgumentValues : { // 构造函数的参数描述
						 			genericArgumentValues : [
						 				构造函数参数ValueHolder.value : RootBeanDefinition {
			 		 						beanClass : org.springframework.aop.aspectj.AspectJAfterAdvice.class, // 由<aop:after>决定
					 						propertyValues : { // 属性  org.springframework.beans.MutablePropertyValues
					 							aspectName :  要“接受报告的bean对象”,  // 要“接受报告的bean对象”，由<aop:aspect ref="aspect4HelloService1">决定
					 							declarationOrder : "0",
					 							returningName : "returningName0",
					 							throwingName : "throwingName0",
					 							argumentNames : "argumentNames0"
					 						}
					 						constructorArgumentValues : { // 构造函数的参数描述
					 							indexedArgumentValues : [
					 								构造函数参数0 : RootBeanDefinition{
					 										beanClass : org.springframework.aop.config.MethodLocatingFactoryBean.class,
					 										synthetic : true,
						 									propertyValues : {
						 										targetBeanName :  要“接受报告的bean对象”,  // 要“接受报告的bean对象”，由<aop:aspect ref="aspect4HelloService1">决定
						 										methodName : "aspectMethodAfter"  // 要通知的方法, 由<aop:after method="aspectMethodAfter" /> 决定
						 									}
					 								},
					 								构造函数参数1 : new RuntimeBeanReference((String) "pointcutRefId_1"),  // 由<aop:after pointcut-ref="pointcutRefId_1" /> 决定
					 								构造函数参数2 : RootBeanDefinition{
					 										beanClass : org.springframework.aop.config.SimpleBeanFactoryAwareAspectInstanceFactory.class,
					 										synthetic : true,
						 									propertyValues : {
						 										aspectBeanName :  要“接受报告的bean对象”, // 由<aop:aspect ref="aspect4HelloService1">决定
						 									}
					 								}
					 							]
					 						}
					 					}
						 			]
						 		}
						 	}
				 		],
				 		beanReferences : [
				 			new RuntimeBeanReference((String) "pointcutRefId_1"),  // 由<aop:after pointcut-ref="pointcutRefId_1" /> 决定
				 		],
				 		nestedComponents : [
				 			0 ： org.springframework.aop.config.PointcutComponentDefinition { // <aop:pointcut id="pointcutRefId_1" expression="execution(public void cn.java.demo.aoptag.bean.HelloServiceImpl2.method1(..))" />
				 				pointcutBeanName : "pointcutRefId_1",  // 由<aop:pointcut id="pointcutRefId_1" >决定
				 				pointcutDefinition : RootBeanDefinition { // 这个对象会注册到全局bean容器中（beanName是pointcutRefId_1）
				 					beanClass : org.springframework.aop.aspectj.AspectJExpressionPointcut.class,
				 					scope : "prototype",
				 					synthetic : true,
				 					propertyValues: {
				 						expression : "execution(public void cn.java.demo.aoptag.bean.HelloServiceImpl2.method1(..))" // 识别特征，expression 由<aop:pointcut expression="execution(public void cn.java.demo.aoptag.bean.HelloServiceImpl2.method1(..))" >决定
				 					}
				 				}
				 				description : "Pointcut <name='" + pointcutBeanName + "', expression=[" + expression + "]>" // 识别特征， expression 由<aop:pointcut expression="execution(public void cn.java.demo.aoptag.bean.HelloServiceImpl2.method1(..))" >决定
				 			}
				 		]
				 	},
				 	org.springframework.aop.config.AspectComponentDefinition  { // <aop:aspect id="aspectId_0" ref="aspect4HelloService0">
				 		....
				 		name : "aspectId_0" // 由<aop:aspect  id="aspectId_0">  决定
				 		beanDefinitions : [
				 		],
				 		beanReferences : [
				 		],
				 		nestedComponents : [
				 		]
				 	}
			 		 
		 		 ]
		 	}
		 	
		 	----------<spring-configured>------------------
		 	BeanComponentDefinition {
 				beanDefinition : RootBeanDefinition { // 这个对象会注册到全局bean容器中（beanName是"org.springframework.context.config.internalBeanConfigurerAspect"）
		 			beanClass : org.springframework.beans.factory.aspectj.AnnotationBeanConfigurerAspect,
		 			factoryMethodName ： "aspectOf"
		 			source : null,
		 			role :  BeanDefinition.ROLE_INFRASTRUCTURE,
		 			propertyValues : {
		 			}
		 		},
 		 		beanName : "org.springframework.context.config.internalBeanConfigurerAspect常量",
 		 		nestedComponents : [
	 		 		
 		 		]
		 	}
		 	
		 	----------<aop:scoped-proxy proxy-target-class="true" />------------------
		 	BeanDefinitionHolder {
		 		beanDefinition : RootBeanDefinition { // （新的BeanDefinition）这个对象会注册到全局bean容器中（beanName是"scopedTarget.beanName0"）
		 			beanClass : org.springframework.aop.scope.ScopedProxyFactoryBean,
		 			decoratedDefinition : BeanDefinitionHolder {
		 				beanDefinition : GenericBeanDefinition { // 被劫持的BeanDefinition（原始的BeanDefinition）
		 					beanClass : cn.java.bean.FooService,
		 					.....
		 					.....
		 					attributes : {
		 						"org.springframework.aop.framework.autoproxy.AutoProxyUtils.preserveTargetClass" : true,  // 当proxy-target-class="true"有这项
		 						"org.springframework.aop.framework.autoproxy.AutoProxyUtils.originalTargetClass" : "cn.java.bean.FooService" // 当beanFactory instanceof ConfigurableListableBeanFactory时，有这一项
		 					}
		 					autowireCandidate : false , // 被强制为false
		 					primary : false , // 被强制为false
		 				}
		 				beanName : "scopedTarget.beanName0"
		 			},
		 			resource ： BeanDefinitionResource {
		 				beanDefinition : GenericBeanDefinition { // 被劫持的BeanDefinition（原始的BeanDefinition）
		 					beanClass : cn.java.bean.FooService,
		 					.....
		 					.....
		 					attributes : {
		 						"org.springframework.aop.framework.autoproxy.AutoProxyUtils.preserveTargetClass" : true // 当proxy-target-class="true"有这项
		 						"org.springframework.aop.framework.autoproxy.AutoProxyUtils.originalTargetClass" : "cn.java.bean.FooService" // 当beanFactory instanceof ConfigurableListableBeanFactory时，有这一项
		 					},
		 					autowireCandidate : false , // 被强制为false
		 					primary : false , // 被强制为false
		 				}
		 			}
		 			source : null,
		 			role :  BeanDefinition.ROLE_INFRASTRUCTURE,
		 			propertyValues : {
		 				targetBeanName ： "scopedTarget.beanName0",
		 				proxyTargetClass ： false    // 当proxy-target-class="false"有这项
		 			},
		 			autowireCandidate : ... ,
		 			primary : ... ,
		 			qualifiers ： {
		 			
		 			}
		 		},
		 		beanName : "beanName0",
		 		aliases : []
		 	}
		 	
		 	
		 */
	}
}
