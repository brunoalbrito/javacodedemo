package cn.java.debug.aopnote;

import java.util.ArrayList;
import java.util.List;

import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.aspectj.AspectJMethodBeforeAdvice;
import org.springframework.aop.aspectj.AspectJPointcutAdvisor;
import org.springframework.aop.config.AspectComponentDefinition;
import org.springframework.aop.config.MethodLocatingFactoryBean;
import org.springframework.aop.config.PointcutComponentDefinition;
import org.springframework.aop.config.SimpleBeanFactoryAwareAspectInstanceFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanReference;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

public class Debug {
	public static void debug() {
		/*
		 	org.springframework.aop.config.AopNamespaceHandler 命名空间处理器
		 */
	}
	
	
	
	public static AbstractBeanDefinition createAdviceDefinition(String aspectName,int order,List<BeanReference> beanReferences,RootBeanDefinition methodDef, RootBeanDefinition aspectFactoryDef){
		RootBeanDefinition adviceDefinition = new RootBeanDefinition(AspectJMethodBeforeAdvice.class); // <aop:before>
		adviceDefinition.setSource(null);
		adviceDefinition.getPropertyValues().add("aspectName", aspectName);
		adviceDefinition.getPropertyValues().add("declarationOrder", order);

		ConstructorArgumentValues cav = adviceDefinition.getConstructorArgumentValues();
		cav.addIndexedArgumentValue(0, methodDef);  // 第一个参数

		Object pointcut = "pointcutRefId_0";
		RuntimeBeanReference pointcutRef = new RuntimeBeanReference((String) pointcut);// !!! 使用哪个切入点
		cav.addIndexedArgumentValue(1, pointcutRef); // 第二个参数
		beanReferences.add(pointcutRef);

		cav.addIndexedArgumentValue(2, aspectFactoryDef); // 第三个参数

		return adviceDefinition;
	}

	/**
	 * 接收器
	 * @param aspectName
	 * @param methodName
	 * @param beanReferences
	 * @return
	 */
	public static AbstractBeanDefinition parseAdvice(String aspectName,String methodName,List<BeanReference> beanReferences){
		
		int order = 0;
		beanReferences.add(new RuntimeBeanReference(aspectName));

		// create the method factory bean
		RootBeanDefinition methodDefinition = new RootBeanDefinition(MethodLocatingFactoryBean.class);
		methodDefinition.getPropertyValues().add("targetBeanName", aspectName); // 切面bean对象
		methodDefinition.getPropertyValues().add("methodName",methodName);
		methodDefinition.setSynthetic(true);

		// create instance factory definition
		RootBeanDefinition aspectFactoryDef =
				new RootBeanDefinition(SimpleBeanFactoryAwareAspectInstanceFactory.class);
		aspectFactoryDef.getPropertyValues().add("aspectBeanName", aspectName);
		aspectFactoryDef.setSynthetic(true);

		// register the pointcut  接收器的定义 
		AbstractBeanDefinition adviceDef =  createAdviceDefinition(aspectName,order,beanReferences,methodDefinition,aspectFactoryDef);

		// configure the advisor
		RootBeanDefinition advisorDefinition = new RootBeanDefinition(AspectJPointcutAdvisor.class); // 创建bean定义
		advisorDefinition.setSource(null);
		advisorDefinition.getConstructorArgumentValues().addGenericArgumentValue(adviceDef);
		
		// register the final advisor
//		parserContext.getReaderContext().registerWithGeneratedName(advisorDefinition); // 注册bean定义
		return advisorDefinition;
	}
	
	public static void main(String[] args){
		/*
			 <aop:config>
	            <aop:aspect id="aspectId_0" ref="aspect4HelloService0">
	                <aop:pointcut id="pointcutRefId_0" expression="execution(*cn.java.test.aop.beans.HelloService*(..))" /><!-- 定义切入点 -->
	                <aop:before method="aspectMethodBefore" pointcut-ref="pointcutRefId_0" /> <!-- 定义报告接收者 -->
	                <aop:after method="aspectMethodAfter" pointcut-ref="pointcutRefId_0" />
	            </aop:aspect>
	        </aop:config>
		 */
		//------接收器的定义、切面组件（接收器+切入点）-------------
		String aspectId = "aspectId_0";
		List<BeanDefinition> beanDefinitions = new ArrayList<BeanDefinition>();
		List<BeanReference> beanReferences = new ArrayList<BeanReference>();
		String aspectName = "aspect4HelloService0"; // 接受报告的类
		String methodName = "aspectMethodBefore"; // 接受报告的方法
		AbstractBeanDefinition advisorDefinition = parseAdvice(aspectName,methodName,beanReferences);
		beanDefinitions.add(advisorDefinition);
		
		BeanDefinition[] beanDefArray = beanDefinitions.toArray(new BeanDefinition[beanDefinitions.size()]);
		BeanReference[] beanRefArray = beanDefinitions.toArray(new BeanReference[beanDefinitions.size()]);
		Object source = null;
		AspectComponentDefinition aspectComponentDefinition =  new AspectComponentDefinition(aspectId, beanDefArray, beanRefArray, source);
		
		//------切入点的定义-------------
		String expression = "execution(*cn.java.test.aop.beans.HelloService*(..))";
		RootBeanDefinition beanDefinition = new RootBeanDefinition(AspectJExpressionPointcut.class);
		beanDefinition.setScope(BeanDefinition.SCOPE_PROTOTYPE);
		beanDefinition.setSynthetic(true);
		beanDefinition.getPropertyValues().add("expression",expression );
		AbstractBeanDefinition pointcutDefinition = beanDefinition;
		pointcutDefinition.setSource(null);
//		parserContext.registerComponent(
//				new PointcutComponentDefinition(pointcutBeanName, pointcutDefinition, expression)); // 维护和父级标签的关系
//		String pointcutBeanName = "pointcutRefId_0"; // 切面ID
//		parserContext.getRegistry().registerBeanDefinition(pointcutBeanName, pointcutDefinition);
		
		
	}
}
