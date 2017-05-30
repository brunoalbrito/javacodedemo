package cn.java.demo.contexttag;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.java.demo.contexttag.component.api.FooComponent;
import cn.java.demo.contexttag.component.impl.NeedAutowireComponent;
import cn.java.demo.contexttag.internal.ReflectionUtilsTest;
import cn.java.demo.contexttag.internal.asm.ClassMetadataTest;
import cn.java.demo.contexttag.internal.enhancer.FooServiceMustNeedBeanFactoryEnhancerTest;
import cn.java.demo.util.ApplicationContextUtil;

public class Test {
	public static void main(String[] args) throws Exception {

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
			FooComponent implTwoFooComponent0 = (FooComponent) context.getBean("implTwoFooComponent");
			System.out.println(implTwoFooComponent0.method1());
			System.out.println(implTwoFooComponent0);
			System.out.println("identityHashCode = " + System.identityHashCode(implTwoFooComponent0));
			
			FooComponent implTwoFooComponent1 = (FooComponent) context.getBean("implTwoFooComponent");
			System.out.println(implTwoFooComponent1.method1());
			System.out.println(implTwoFooComponent1);
			System.out.println("identityHashCode = " + System.identityHashCode(implTwoFooComponent1));
		}
		
		
		System.out.println("\n----------@Autowired注解的使用--------------");
		{
			NeedAutowireComponent needAutowireComponent = (NeedAutowireComponent) context.getBean("needAutowireComponent");
			System.out.println(needAutowireComponent);
		}
		
		{
			System.out.println("\n----------自动生成子类（Enhancer）--------------");
			{
				FooServiceMustNeedBeanFactoryEnhancerTest.testFooServiceWithImplBeanFactoryAware();
				FooServiceMustNeedBeanFactoryEnhancerTest.testFooServiceWithOutImplBeanFactoryAware();
			}
			
			System.out.println("\n----------访问类的class文件（不走反射），获取信息--------------");
			{
				ClassMetadataTest.testGetClassMetadataInfoByParseBytecode();
				ClassMetadataTest.testGetClassMetadataInfoByReflectClass();
			}
			
			System.out.println("\n----------解析@Autowired注解--------------");
			{
				ReflectionUtilsTest.testReflectionAutowired();
			}
		}
	}
}
