package cn.java.demo.contexttag;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.java.demo.contexttag.component.api.FooComponent;
import cn.java.demo.contexttag.internal.asm.ClassMetadataTest;
import cn.java.demo.util.ApplicationContextUtil;

public class Test {
	public static void main(String[] args) {

		ApplicationContext context = new ClassPathXmlApplicationContext(
				"classpath:cn/java/demo/contexttag/applicationContext.xml");
		
		{ // 一些钩子bean
			ApplicationContextUtil.printBeanDefinitionInRegistry((AbstractRefreshableConfigApplicationContext) context);
			ApplicationContextUtil.printBeanFactoryPostProcessorsInBeanFactory((AbstractRefreshableConfigApplicationContext) context);
			ApplicationContextUtil.printBeanPostProcessorInBeanFactory((AbstractRefreshableConfigApplicationContext) context);
		}
		
		System.out.println("\n----------自动扫描 - BeanDefinition为原型模式--------------");
		{
			FooComponent implOneFooComponent0 = (FooComponent) context.getBean("implOneFooComponent");
			System.out.println(implOneFooComponent0.method1());
			System.out.println(implOneFooComponent0);
			System.out.println("identityHashCode = " + System.identityHashCode(implOneFooComponent0));
			
			FooComponent implOneFooComponent1 = (FooComponent) context.getBean("implOneFooComponent");
			System.out.println(implOneFooComponent1.method1());
			System.out.println(implOneFooComponent1);
			System.out.println("identityHashCode = " + System.identityHashCode(implOneFooComponent1));
		}
		
		System.out.println("\n----------自动扫描 - BeanDefinition为单例模式--------------");
		{
			FooComponent implTowFooComponent0 = (FooComponent) context.getBean("implTowFooComponent");
			System.out.println(implTowFooComponent0.method1());
			System.out.println(implTowFooComponent0);
			System.out.println("identityHashCode = " + System.identityHashCode(implTowFooComponent0));
			
			FooComponent implTowFooComponent1 = (FooComponent) context.getBean("implTowFooComponent");
			System.out.println(implTowFooComponent1.method1());
			System.out.println(implTowFooComponent1);
			System.out.println("identityHashCode = " + System.identityHashCode(implTowFooComponent1));
		}
		
		System.out.println("\n----------访问类的class文件（不走反射），获取信息--------------");
		{
			ClassMetadataTest.testClassFileVisitor();
			ClassMetadataTest.testStandardAnnotationMetadata();
		}
	}
}
