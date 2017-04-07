package cn.java.note.all;

import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.util.ClassUtils;
import org.springframework.util.xml.DomUtils;

public class GenericBeanDefinitionTest {
	
	public static void main(String[] args) throws Exception {
		String parentName = "cn.java.note.web.GenericBeanDefinitionTest.ParentBean";
		String className = "cn.java.note.web.GenericBeanDefinitionTest.ChildrenBean";
		ClassLoader classLoader = GenericBeanDefinitionTest.class.getClassLoader();
		
		// 
		GenericBeanDefinition bd = new GenericBeanDefinition();
		bd.setParentName(parentName); // 父类的名称
		bd.setBeanClass(ClassUtils.forName(className, classLoader));
		
		bd.setScope("");
		bd.setAbstract(false);
		bd.setLazyInit(false);
		bd.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_NO);
		bd.setDependencyCheck(AbstractBeanDefinition.DEPENDENCY_CHECK_NONE);
		bd.setDependsOn("");
		bd.setAutowireCandidate(false);
		bd.setPrimary(false);
		bd.setInitMethodName("initMethod1");
		bd.setDestroyMethodName("destroyMethod1");
		bd.setFactoryMethodName("factoryMethod1");
		bd.setFactoryBeanName("factoryBean1");
		bd.setDescription("this is description...");
		
//		bd.addMetadataAttribute(attribute);
//		bd.getMethodOverrides().addOverride(override);
//		bd.getMethodOverrides().addOverride(override);
//		bd.getConstructorArgumentValues().addIndexedArgumentValue(index, valueHolder);
//		bd.getPropertyValues().addPropertyValue(pv);
//		bd.addQualifier(qualifier);
	}

	public static class ParentBean {

	}

	public static class ChildrenBean extends ParentBean {

	}
}
