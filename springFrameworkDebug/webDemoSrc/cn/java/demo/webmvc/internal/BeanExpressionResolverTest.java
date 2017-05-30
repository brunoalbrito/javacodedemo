package cn.java.demo.webmvc.internal;

import org.springframework.beans.factory.config.BeanExpressionContext;
import org.springframework.beans.factory.config.BeanExpressionResolver;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;
import org.springframework.web.context.request.RequestScope;

import cn.java.demo.util.ApplicationContextUtil;

public class BeanExpressionResolverTest {
	
	public static void testBeanExpressionResolver(
			AbstractRefreshableConfigApplicationContext context) {
		BeanDefinitionRegistry registry = ApplicationContextUtil.getBeanFactoryAndTryCastTypeToBeanDefinitionRegistry(context);
		ConfigurableListableBeanFactory beanFactory = ApplicationContextUtil.getBeanFactoryAndTryCastTypeToConfigurableListableBeanFactory(context);

		if (registry == null || beanFactory == null) {
			return;
		}
		
		String value = "attr0";
		
		// beanFactory == org.springframework.beans.factory.support.DefaultListableBeanFactory
		String placeholdersResolved = beanFactory.resolveEmbeddedValue(value);
		BeanExpressionResolver exprResolver = beanFactory.getBeanExpressionResolver(); // org.springframework.context.expression.StandardBeanExpressionResolver
		if (exprResolver == null) {
			System.out.println("没有解析器");
		}
		else{
			BeanExpressionContext expressionContext = new BeanExpressionContext(beanFactory, new RequestScope());
			Object object = exprResolver.evaluate(placeholdersResolved, expressionContext);
			System.out.println("表达式执行结果"+object); // === request.getAttribute("attr0")
		}
	}
}
