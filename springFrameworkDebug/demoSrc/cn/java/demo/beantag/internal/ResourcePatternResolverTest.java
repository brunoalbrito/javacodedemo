package cn.java.demo.beantag.internal;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import cn.java.demo.util.ApplicationContextUtil;

/**
 * 查找符合特征的资源文件
 * @author zhouzhian
 *
 */
public class ResourcePatternResolverTest {
	public static void testResourcePatternResolver(AbstractRefreshableConfigApplicationContext context) {
		BeanDefinitionRegistry registry = ApplicationContextUtil
				.getBeanFactoryAndTryCastTypeToBeanDefinitionRegistry(context);
		ConfigurableListableBeanFactory beanFactory = ApplicationContextUtil
				.getBeanFactoryAndTryCastTypeToConfigurableListableBeanFactory(context);
		if (registry == null || beanFactory == null) {
			throw new RuntimeException("registry == null || beanFactory == null");
		}
		try {
			testPathMatchingResourcePatternResolver();
			testAbstractApplicationContextResourcePatternResolver(context);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	private static void testAbstractApplicationContextResourcePatternResolver(AbstractRefreshableConfigApplicationContext context)
			throws Exception {
		if (context instanceof AbstractApplicationContext) {
			AbstractApplicationContext abstractApplicationContext = (AbstractApplicationContext) context;
			String locationPattern = "classpath*:/cn/java/demo/contexttag/component/impl/*Component.class";
			Resource[] resources = abstractApplicationContext.getResources(locationPattern);
			printResources(resources);
		}
	}

	private static void printResources(Resource[] resources) throws Exception {
		for (Resource resource : resources) {
			System.out.println(resource.getFile().getName());
		}
	}

	private static void testPathMatchingResourcePatternResolver() throws Exception {
		ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
		{
			String packageSearchPath = "classpath*:/cn/java/demo/contexttag/component/impl/*Component.class";
			Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
			printResources(resources);
		}
	}

}
