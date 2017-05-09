package cn.java.demo.aoptag.mock;

import org.springframework.aop.scope.ScopedProxyFactoryBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;

import cn.java.demo.aoptag.bean.HelloServiceImpl4AopScopedProxyTagMockInJava;
import cn.java.demo.util.ApplicationContextUtil;

/**
 * 使用CGLIB生成目标的类的子类
 * @author zhouzhian
 *
 */
public class AopScopedProxyTagMockInJavaTest {
	
	public void test(AbstractRefreshableConfigApplicationContext context) {
		System.out.println("-----AopScopedProxyTagMockInJavaTest------");
		BeanDefinitionRegistry registry = ApplicationContextUtil.getBeanFactoryAndTryCastTypeToBeanDefinitionRegistry(context);
		ConfigurableListableBeanFactory beanFactory = ApplicationContextUtil.getBeanFactoryAndTryCastTypeToConfigurableListableBeanFactory(context);
		if(registry==null || beanFactory==null){
			return;
		}
		
		// 原始信息
		String originalBeanName = this.getClass().getSimpleName()+"beanName0";
		String[] aliases = {};
		RootBeanDefinition targetDefinition = new RootBeanDefinition(HelloServiceImpl4AopScopedProxyTagMockInJava.class);
		targetDefinition.setSource(null);
		targetDefinition.setAutowireMode(RootBeanDefinition.AUTOWIRE_BY_NAME);
		targetDefinition.setRole(BeanDefinition.ROLE_APPLICATION);
		targetDefinition.setScope(RootBeanDefinition.SCOPE_PROTOTYPE);
		
		// 进行包装
		{
			String targetBeanName = getTargetBeanName(originalBeanName); //  targetBeanName = "scopedTarget.beanName0"
			RootBeanDefinition proxyDefinition = new RootBeanDefinition(ScopedProxyFactoryBean.class);
			proxyDefinition.setDecoratedDefinition(new BeanDefinitionHolder(targetDefinition, targetBeanName));
			proxyDefinition.setOriginatingBeanDefinition(targetDefinition);
			proxyDefinition.setSource(null);
			proxyDefinition.setRole(targetDefinition.getRole());
			
			proxyDefinition.getPropertyValues().add("targetBeanName", targetBeanName);
			
			// Copy autowire settings from original bean definition.
			proxyDefinition.setAutowireCandidate(targetDefinition.isAutowireCandidate());
			proxyDefinition.setPrimary(targetDefinition.isPrimary());
			if (targetDefinition instanceof AbstractBeanDefinition) {
				proxyDefinition.copyQualifiersFrom((AbstractBeanDefinition) targetDefinition);
			}
			
			// The target bean should be ignored in favor of the scoped proxy.
			targetDefinition.setAutowireCandidate(false);
			targetDefinition.setPrimary(false);
			
			// Register the target bean as separate bean in the factory.
			registry.registerBeanDefinition(targetBeanName, targetDefinition); // 注册bean，bean名为 "scopedTarget.beanName0"
			
			BeanDefinitionHolder bdHolder = new BeanDefinitionHolder(proxyDefinition, originalBeanName, aliases);
			
			// Register the final decorated instance.
			BeanDefinitionReaderUtils.registerBeanDefinition(bdHolder, registry); // 注册bean，bean名为 "beanName0"
		}
		
	}
	
	private static final String TARGET_NAME_PREFIX = "scopedTarget.";
	/**
	 * Generates the bean name that is used within the scoped proxy to reference the target bean.
	 * @param originalBeanName the original name of bean
	 * @return the generated bean to be used to reference the target bean
	 */
	public static String getTargetBeanName(String originalBeanName) {
		return TARGET_NAME_PREFIX + originalBeanName;
	}
		
}
