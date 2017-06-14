package cn.java.debug.tx;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.NoRollbackRuleAttribute;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.springframework.util.StringUtils;

public class Debug {
	public static void main(String[] args) {
		/*
		 	http\://www.springframework.org/schema/tx=org.springframework.transaction.config.TxNamespaceHandler
		 	
		 	registerBeanDefinitionParser("advice", new TxAdviceBeanDefinitionParser());
			registerBeanDefinitionParser("annotation-driven", new AnnotationDrivenBeanDefinitionParser());
			registerBeanDefinitionParser("jta-transaction-manager", new JtaTransactionManagerBeanDefinitionParser());
			
			隔离级别的定义：org.springframework.transaction.TransactionDefinition
			
		 */
		{
//			<tx:advice id="advice0" transaction-manager="transactionManagerx">
//		        <tx:attributes>
//					<tx:method name="getDao" propagation="NOT_SUPPORTED" isolation="REPEATABLE_READ" timeout="-1" readOnly="false" rollback-for="a,b,c" no-rollback-for="a,b,c"/>  --- org.springframework.transaction.interceptor.RuleBasedTransactionAttribute
//		        	....
//		        </tx:attributes>
//	        </tx:advice>
			// <tx:advice id="baseServiceAdvice" transaction-manager="transactionManager">
			// 
			
			// 注册TransactionInterceptor对象到BeanDefinition容器
			ManagedMap transactionAttributeMap = new ManagedMap<TypedStringValue, RuleBasedTransactionAttribute>(1);
			{
				TypedStringValue nameHolder = new TypedStringValue("method0");
				nameHolder.setSource(null);
				
				RuleBasedTransactionAttribute attribute = new RuleBasedTransactionAttribute();
				attribute.setPropagationBehaviorName(RuleBasedTransactionAttribute.PREFIX_PROPAGATION + "NOT_SUPPORTED");
				attribute.setIsolationLevelName(RuleBasedTransactionAttribute.PREFIX_ISOLATION + "REPEATABLE_READ");
				attribute.setTimeout(Integer.parseInt("60"));
				attribute.setReadOnly(Boolean.valueOf("true"));
				{
					List<RollbackRuleAttribute> rollbackRules = new LinkedList<RollbackRuleAttribute>(); // 回滚规则
					{
						String[] exceptionTypeNames = StringUtils.commaDelimitedListToStringArray("rollbackForValue0,rollbackForValue1");
						for (String typeName : exceptionTypeNames) {
							rollbackRules.add(new RollbackRuleAttribute(StringUtils.trimWhitespace(typeName)));
						}
					}
					
					{
						String[] exceptionTypeNames = StringUtils.commaDelimitedListToStringArray("noRollbackForValue0,noRollbackForValue1");
						for (String typeName : exceptionTypeNames) {
							rollbackRules.add(new NoRollbackRuleAttribute(StringUtils.trimWhitespace(typeName)));
						}
					}
				}
				transactionAttributeMap.put(nameHolder, attribute);
			}
			{
				TypedStringValue nameHolder = new TypedStringValue("method1");
				nameHolder.setSource(null);
				
				RuleBasedTransactionAttribute attribute = new RuleBasedTransactionAttribute();
				attribute.setPropagationBehaviorName(RuleBasedTransactionAttribute.PREFIX_PROPAGATION + "NOT_SUPPORTED");
				attribute.setIsolationLevelName(RuleBasedTransactionAttribute.PREFIX_ISOLATION + "REPEATABLE_READ");
				attribute.setTimeout(Integer.parseInt("60"));
				attribute.setReadOnly(Boolean.valueOf("true"));
				{
					List<RollbackRuleAttribute> rollbackRules = new LinkedList<RollbackRuleAttribute>(); // 回滚规则
					{
						String[] exceptionTypeNames = StringUtils.commaDelimitedListToStringArray("rollbackForValue0,rollbackForValue1");
						for (String typeName : exceptionTypeNames) {
							rollbackRules.add(new RollbackRuleAttribute(StringUtils.trimWhitespace(typeName)));
						}
					}
					
					{
						String[] exceptionTypeNames = StringUtils.commaDelimitedListToStringArray("noRollbackForValue0,noRollbackForValue1");
						for (String typeName : exceptionTypeNames) {
							rollbackRules.add(new NoRollbackRuleAttribute(StringUtils.trimWhitespace(typeName)));
						}
					}
				}
				transactionAttributeMap.put(nameHolder, attribute);
			}
			
			NameMatchTransactionAttributeSource nameMatchTransactionAttributeSource = new NameMatchTransactionAttributeSource(); // 用于匹配是否需要被事务管理
			nameMatchTransactionAttributeSource.setNameMap(transactionAttributeMap);
			
			TransactionInterceptor transactionInterceptor = new TransactionInterceptor(); // aop里面的“通知接受者”
			transactionInterceptor.setTransactionManagerBeanName("transactionManagerx");
			transactionInterceptor.setTransactionAttributeSource(nameMatchTransactionAttributeSource);
		}
		
		
		{
			// <tx:annotation-driven transaction-manager="transactionManagerx" mode="proxy" /> 启动aop的功能，并注册aop的自定义Advisor对象到BeanDefinition容器
			
			/*
			 	adviceBeanName(接受通知的对象，beanName自动生成) == org.springframework.transaction.interceptor.TransactionInterceptor  // aop里面的“通知接受者”
			 	{
			 		transactionManagerBeanName : "transactionManagerx"
			 		transactionAttributeSource : org.springframework.transaction.annotation.AnnotationTransactionAttributeSource 对象  
			 	}
			 	
			 	advisor(beanName自动生成) = org.springframework.transaction.interceptor.BeanFactoryTransactionAttributeSourceAdvisor
			 	{
			 		transactionAttributeSource : org.springframework.transaction.annotation.AnnotationTransactionAttributeSource 对象 // 用于匹配是否需要被事务管理
			 		adviceBeanName ：adviceBeanName(接受通知的对象，beanName自动生成)
			 	}
			 	
			 */
		}
	}
}
