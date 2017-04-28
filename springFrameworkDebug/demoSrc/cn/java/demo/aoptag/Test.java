package cn.java.demo.aoptag;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.java.demo.aoptag.api.HelloService;
import cn.java.demo.aoptag.api.HelloService2;
import cn.java.demo.aoptag.bean.HelloServiceImpl4AopScopedProxy;
import cn.java.demo.aoptag.internal.PointcutExpressionTest;
import cn.java.demo.aoptag.mock.AopConfigTagMockInJavaTest;
import cn.java.demo.aoptag.mock.AspectJAwareAdvisorAutoProxyCreatorTest;



public class Test {

	/**
	 * https://github.com/spring-projects
	 * http://mvnrepository.com/search?q=Spring
	 * 使用4.3.6版本
	 */
	public static void main(String[] args) {
	{
			ApplicationContext context = new ClassPathXmlApplicationContext("classpath:cn/java/demo/aoptag/applicationContext.xml");
			
//			ApplicationContextUtil.refresh((AbstractRefreshableConfigApplicationContext) context);		
			{
				
				/*
				 	以CGLIB的方式：CGLIB会生成以目标类（如：HelloServiceImpl4AopScopedProxy）为接口的子类，在做类型转换的时候，HelloServiceImpl4AopScopedProxy可以转型成功，所以通常无需定义接口。
				 	以Proxy的方式：减少了生成子类的耗时，里面走的的是拦截器机制，在做类型转换的时候，HelloServiceImpl4AopScopedProxy不能转型成功，所以通常需要定义接口。
				 */
				System.out.println("\n-------------以(自定义接口 + Proxy方式 + 拦截器链)实现AOP - 使用aop:config 标签--------------");
				{
					System.out.println("------ 拦截类的所有方法 ------");
					HelloService helloServiceImpl1 = (HelloService)context.getBean("helloServiceImpl1");
					System.out.println("getClass().getName() = " + helloServiceImpl1.getClass().getName());
					System.out.println("---helloService1.method1()---");
					helloServiceImpl1.method1();
					System.out.println("---helloService1.method2()---");
					helloServiceImpl1.method2();
					
					System.out.println("------ 拦截类的某个方法 ------");
					HelloService helloServiceImpl2 = (HelloService)context.getBean("helloServiceImpl2");
//					HelloServiceImpl2 helloServiceImpl2 = (HelloServiceImpl2)context.getBean("helloServiceImpl2"); // 这个类型转换转不过去，必须以HelloService接口的形式
					System.out.println("getClass().getName() = " + helloServiceImpl2.getClass().getName());
					System.out.println("---helloService2.method1()---");
					helloServiceImpl2.method1();
					System.out.println("---helloService2.method2()---");
					helloServiceImpl2.method2();
					
				}
				
				System.out.println("\n-----------以(CGLIB生成子类 + 拦截器链)实现AOP - 使用aop:scoped-proxy 标签的使用 ----------------");
				{
					HelloServiceImpl4AopScopedProxy helloServiceImpl4AopScopedProxy0 = (HelloServiceImpl4AopScopedProxy)context.getBean("helloServiceImpl4AopScopedProxy");
					System.out.println("getClass().getName() = " + helloServiceImpl4AopScopedProxy0.getClass().getName());
					System.out.println("---helloServiceImpl4AopScopedProxy0.method1()---");
					helloServiceImpl4AopScopedProxy0.method1();
					System.out.println("---helloServiceImpl4AopScopedProxy0.method2()---");
					helloServiceImpl4AopScopedProxy0.method2();
					
					HelloService helloServiceImpl4AopScopedProxy1 = (HelloService)context.getBean("helloServiceImpl4AopScopedProxy");
					System.out.println("getClass().getName() = " + helloServiceImpl4AopScopedProxy1.getClass().getName());
					System.out.println("---helloServiceImpl4AopScopedProxy1.method1()---");
					helloServiceImpl4AopScopedProxy1.method1();
					System.out.println("---helloServiceImpl4AopScopedProxy1.method2()---");
					helloServiceImpl4AopScopedProxy1.method2();
				}
				
				System.out.println("\n--------------------测试“bean配置的advisor”-------------------------");
				{
					
					System.out.println("\n------ 使用bean标签 + aop:config + aop:advisor + aop:pointcut 标签配置aop ------");
					/*
					 	<aop:before> （有被适配）一定会被执行
					 	<aop:after>  （没被适配）一定会被执行
					 	<aop:after-returning> （有被适配）出现异常就不会被执行
					 	<aop:after-throwing>  （没被适配）出现异常才会被执行，不能抑制异常的抛出，可以作记录
					 */
					HelloService helloServiceImpl4ConfigAopWithAdvisorTag = (HelloService)context.getBean("helloServiceImpl4ConfigAopWithAdvisorTag");
					System.out.println("getClass().getName() = " + helloServiceImpl4ConfigAopWithAdvisorTag.getClass().getName());
					System.out.println("---helloServiceImpl4ConfigAopWithAdvisorTag.method1()---");
					helloServiceImpl4ConfigAopWithAdvisorTag.method1();
					System.out.println();
//					helloServiceImpl4ConfigAopWithAdvisorTag.method2(); // 测试异常的拦截
				}
				System.out.println("\n--------------------测试“xml配置的aop:around、aop:before、aop:after、aop:after-returning、aop:after-throwing”-------------------------");
				{
					
					System.out.println("\n------ 使用bean标签 + aop:config + aop:aspect + aop:pointcut + aop:before 标签配置aop ------");
					HelloService2 helloServiceImpl4ConfigAopWithAdvisorTag1 = (HelloService2)context.getBean("helloServiceImpl4ConfigAopWithAdvisorTag1");
					System.out.println("getClass().getName() = " + helloServiceImpl4ConfigAopWithAdvisorTag1.getClass().getName());
					System.out.println("---helloServiceImpl4ConfigAopWithAdvisorTag1.method3()---");
					System.out.println(helloServiceImpl4ConfigAopWithAdvisorTag1.method3());
//					helloServiceImpl4ConfigAopWithAdvisorTag1.method2(); // 测试异常的拦截
				}
				
				System.out.println("\n--------------------使用aop:aspectj-autoproxy标签（基于注解的方式配置Aspect）-------------------------");
				{
					HelloService helloServiceImpl4AspectJAutoProxyTag0 = (HelloService)context.getBean("helloServiceImpl4AspectJAutoProxyTag0");
					helloServiceImpl4AspectJAutoProxyTag0.method1();
					helloServiceImpl4AspectJAutoProxyTag0.method2();
				}
				
			}
			
			System.out.println("\n-------------测试postProcessAfterInitialization机制----------------");
			{
				// 要求在applicationContext.xml中有配置 HelloServiceImpl4NewInJava 的拦截
				AspectJAwareAdvisorAutoProxyCreatorTest.testAspectJAwareAdvisorAutoProxyCreator((AbstractRefreshableConfigApplicationContext) context);
			}
			
			System.out.println("\n-------------测试“表达式的匹配”机制----------------");
			{
				PointcutExpressionTest pointcutParserTest = new PointcutExpressionTest();
				pointcutParserTest.test((AbstractRefreshableConfigApplicationContext) context);
			}
			
		}
		
		System.out.println("\n-------------硬编码，模拟AOP的机制（在实例化任何bean之前，就要注入Advice对象，不然内部发现Advice后，会缓存Advice列表，会使Advice配置不成功）----------------");
		{
			ApplicationContext context = new ClassPathXmlApplicationContext("classpath:cn/java/demo/aoptag/applicationContextEmpty.xml");
			AopConfigTagMockInJavaTest stupidAopInJavaTest = new AopConfigTagMockInJavaTest();
			stupidAopInJavaTest.test((AbstractRefreshableConfigApplicationContext) context);
		}
		
		
		
	}

}
