package cn.java.beannote.关于bean;

import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.util.ClassUtils;
import org.springframework.util.xml.DomUtils;

public class 创建RootBeanDefinition信息 {
	
	
	/**
	 * 构造函数参数类型为：GenericBeanDefinition
	 * @param beanDefinition
	 * @throws Exception
	 */
	public static void testConstructorArgumentValues(GenericBeanDefinition beanDefinition) throws Exception {
		ClassLoader classLoader = 创建RootBeanDefinition信息.class.getClassLoader();
		String className = "cn.java.note.web.GenericBeanDefinitionTest.ChildrenBean";
		
		GenericBeanDefinition beanDefinition4Param0 = new GenericBeanDefinition();
		beanDefinition4Param0.setParentName(null); // 父类的名称
		beanDefinition4Param0.setBeanClass(ClassUtils.forName(className, classLoader));
		
		beanDefinition4Param0.setScope("");
		beanDefinition4Param0.setAbstract(false);
		beanDefinition4Param0.setLazyInit(false);
		beanDefinition4Param0.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_NO);
		beanDefinition4Param0.setDependencyCheck(AbstractBeanDefinition.DEPENDENCY_CHECK_NONE);
		beanDefinition4Param0.setDependsOn("");
		beanDefinition4Param0.setAutowireCandidate(false);
		beanDefinition4Param0.setPrimary(false);
		beanDefinition4Param0.setInitMethodName("initMethod1");
		beanDefinition4Param0.setDestroyMethodName("destroyMethod1");
		beanDefinition4Param0.setFactoryMethodName("factoryMethod1");
		beanDefinition4Param0.setFactoryBeanName("factoryBean1");
		beanDefinition4Param0.setDescription("this is description...");
		ConstructorArgumentValues constructorArgumentValues = beanDefinition.getConstructorArgumentValues();
		constructorArgumentValues.addIndexedArgumentValue(0, beanDefinition4Param0);
	}
	
	/**
	 * 构造函数参数类型为：RuntimeBeanReference
	 * @param beanDefinition
	 * @throws Exception
	 */
	public static void testRuntimeBeanReference(GenericBeanDefinition beanDefinition) throws Exception {
		RuntimeBeanReference pointcutRef = new RuntimeBeanReference((String) "beanName1");
		ConstructorArgumentValues constructorArgumentValues = beanDefinition.getConstructorArgumentValues();
		constructorArgumentValues.addIndexedArgumentValue(1, pointcutRef);
	}
	
	public static void main(String[] args) throws Exception {
		String parentName = "cn.java.note.web.GenericBeanDefinitionTest.ParentBean";
		String className = "cn.java.note.web.GenericBeanDefinitionTest.ChildrenBean";
		ClassLoader classLoader = 创建RootBeanDefinition信息.class.getClassLoader();
		
		// 
		GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
		beanDefinition.setParentName(parentName); // 父类的名称
		beanDefinition.setBeanClass(ClassUtils.forName(className, classLoader));
		
		beanDefinition.setScope("");
		beanDefinition.setAbstract(false);
		beanDefinition.setLazyInit(false);
		beanDefinition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_NO);
		beanDefinition.setDependencyCheck(AbstractBeanDefinition.DEPENDENCY_CHECK_NONE);
		beanDefinition.setDependsOn("");
		beanDefinition.setAutowireCandidate(false);
		beanDefinition.setPrimary(false);
		beanDefinition.setInitMethodName("initMethod1");
		beanDefinition.setDestroyMethodName("destroyMethod1");
		beanDefinition.setFactoryMethodName("factoryMethod1");
		beanDefinition.setFactoryBeanName("factoryBean1");
		beanDefinition.setDescription("this is description...");
		
//		beanDefinition.addMetadataAttribute(attribute);
//		beanDefinition.getMethodOverrides().addOverride(override);
//		beanDefinition.getMethodOverrides().addOverride(override);
//		beanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(index, valueHolder);
//		beanDefinition.getPropertyValues().addPropertyValue(pv);
//		beanDefinition.addQualifier(qualifier);
		
		// 构造函数参数类型为：GenericBeanDefinition
		testConstructorArgumentValues(beanDefinition);
		
		// 构造函数参数类型为：RuntimeBeanReference
		testRuntimeBeanReference(beanDefinition);
		
		
	}

	public static class ParentBean {

	}

	public static class ChildrenBean extends ParentBean {

	}
}
