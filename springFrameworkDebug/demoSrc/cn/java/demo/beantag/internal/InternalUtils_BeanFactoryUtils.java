package cn.java.demo.beantag.internal;

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;

import cn.java.demo.beantag.bean.autowirebytype.FooBeanInjectSelectedByOrder;
import cn.java.demo.util.ApplicationContextUtil;

public class InternalUtils_BeanFactoryUtils {
	
	public static void testBeanFactoryUtils(AbstractRefreshableConfigApplicationContext context) {
		
		BeanDefinitionRegistry registry = ApplicationContextUtil.getBeanFactoryAndTryCastTypeToBeanDefinitionRegistry(context);
		ConfigurableListableBeanFactory beanFactory = ApplicationContextUtil.getBeanFactoryAndTryCastTypeToConfigurableListableBeanFactory(context);

		if (registry == null || beanFactory == null) {
			throw new RuntimeException("registry == null || beanFactory == null");
		}
		
		try {
			testTransformedBeanName();
			testBeanNamesForTypeIncludingAncestors(beanFactory);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}
	
	public static void testTransformedBeanName() throws Exception {
		String beanName = "beanName0";
		String beanNameToRegister = BeanFactoryUtils.transformedBeanName(beanName);
		System.out.println(beanNameToRegister);
	}
	
	public static void testBeanNamesForTypeIncludingAncestors(ConfigurableListableBeanFactory listableBeanFactory) throws Exception {
		boolean includeNonSingletons = true;
		boolean allowEagerInit = true;
		String[] candidateNames = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(
				listableBeanFactory, FooBeanInjectSelectedByOrder.class, true, allowEagerInit);
	}
	
	public static void testGetBeanNamesForType(ConfigurableListableBeanFactory listableBeanFactory) throws Exception {
		boolean includeNonSingletons = true;
		boolean allowEagerInit = true;
		String[] result = listableBeanFactory.getBeanNamesForType(FooBeanInjectSelectedByOrder.class, includeNonSingletons, allowEagerInit);
	}
}
