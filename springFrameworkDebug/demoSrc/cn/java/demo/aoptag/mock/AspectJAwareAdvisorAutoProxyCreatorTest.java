package cn.java.demo.aoptag.mock;

import org.springframework.aop.aspectj.autoproxy.AspectJAwareAdvisorAutoProxyCreator;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;

import cn.java.demo.aoptag.api.HelloService;
import cn.java.demo.aoptag.bean.HelloServiceImpl4NewInJava;
import cn.java.demo.util.ApplicationContextUtil;

/**
 * 测试hook机制
 * @author zhouzhian
 *
 */
public class AspectJAwareAdvisorAutoProxyCreatorTest {
	
	/**
	 * 测试被拦截
	 * 	 
	 * @param beanFactory
	 */
	public static void testAspectJAwareAdvisorAutoProxyCreator(AbstractRefreshableConfigApplicationContext context) {
		
		BeanFactory beanFactory  = ApplicationContextUtil.tryCastTypeToConfigurableListableBeanFactory(context);
		
		AspectJAwareAdvisorAutoProxyCreator aspectJAwareAdvisorAutoProxyCreator = new AspectJAwareAdvisorAutoProxyCreator();
		aspectJAwareAdvisorAutoProxyCreator.setBeanFactory(beanFactory);
		String beanName = "helloServiceImpl4NewInJava"; // 在applicationContext.xml要有helloServiceImpl1名称的Bean配置，因为要用到这个bean的BeanDefinition
		HelloServiceImpl4NewInJava bean = new HelloServiceImpl4NewInJava(); // 手动实例化
		
		/*
		 	会扫描实现接口org.springframework.aop.Advisor的bean，并且进行实例化
			然后根据目标类的信息，进行匹配出所有符合规则的Advisor
			创建代理，返回代理对象
			调用代理的时候，调用的是拦截器链，在最后执行"被代理的对象"
		 */
		Object beanProxy = aspectJAwareAdvisorAutoProxyCreator.postProcessAfterInitialization(bean,beanName); // 初始化后的钩子
		HelloService helloService = (HelloService)beanProxy;
		helloService.method1();
		
		// bean = (HelloService)beanFactory.getBean(beanName); // 这个获取处理的是代理对象
		
	}
}
