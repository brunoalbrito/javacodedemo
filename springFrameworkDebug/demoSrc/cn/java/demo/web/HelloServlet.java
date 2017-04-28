package cn.java.demo.web;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.support.AbstractRefreshableApplicationContext;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;

import cn.java.demo.web.bean.NeedAwareBean;

@SuppressWarnings(value={  "serial" })
public class HelloServlet extends HttpServlet   {
	ThreadLocal<ServletContext> servletContextLocal = new ThreadLocal<ServletContext>();
	
	public void init(ServletConfig config) throws ServletException {
		servletContextLocal.set(config.getServletContext());
		super.init(config);
	}
	
	public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
		super.service(req, res);
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if(servletContextLocal.get()!=null){
			ServletContext servletContext = servletContextLocal.get();
			
			// Spring默认会把XmlWebApplicationContext对象放入servletContext的 WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE 属性中
			XmlWebApplicationContext context = (XmlWebApplicationContext) servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
			
			// 测试 - 获取bean实例
			{ 
				NeedAwareBean needAwareBean0 = (NeedAwareBean) context.getBean("needAwareBean0");
				needAwareBean0.testMethod();
			}
			
			// 测试（context是共享对象，在实际的编码中，不应该动态添加bean的定义） - 动态添加bean的定义、获取bean实例
			{
				BeanDefinitionRegistry registry = tryCastTypeToBeanDefinitionRegistry(context);
				ConfigurableListableBeanFactory beanFactory = tryCastTypeToConfigurableListableBeanFactory(context);
				if(registry!=null && registry!=beanFactory){
					final String beanNameInternal0 = this.getClass().getSimpleName()+"_beanName0";
					
					// 添加bean的定义
					RootBeanDefinition beanDefinitionInternal0 = new RootBeanDefinition(NeedAwareBean.class);
					beanDefinitionInternal0.setSource(null);
					beanDefinitionInternal0.setAutowireMode(RootBeanDefinition.AUTOWIRE_BY_NAME);
					beanDefinitionInternal0.getPropertyValues().add("field1", this.getClass().getSimpleName()+"beanDefinition0_field2Value");
					beanDefinitionInternal0.setRole(BeanDefinition.ROLE_APPLICATION);
					registry.registerBeanDefinition(beanNameInternal0, beanDefinitionInternal0);
					
					// 获取bean实例
					NeedAwareBean needAwareBeanInternal0 = (NeedAwareBean)beanFactory.getBean(beanNameInternal0);
					needAwareBeanInternal0.testMethod();
				}
			}
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
	}
	
	/**
	 * 转成BeanDefinitionRegistry类型
	 * 
	 * @param context
	 * @return
	 */
	private static BeanDefinitionRegistry tryCastTypeToBeanDefinitionRegistry(
			AbstractRefreshableConfigApplicationContext context) {
		if (context instanceof AbstractRefreshableApplicationContext) {
			ConfigurableListableBeanFactory beanFactory = ((AbstractRefreshableApplicationContext) context)
					.getBeanFactory();
			if (beanFactory instanceof BeanDefinitionRegistry) { // bean工厂 === RootBeanDefinition的注册中心
				return ((BeanDefinitionRegistry) beanFactory);
			}
		}
		return null;
	}
	
	/**
	 * 转成ConfigurableListableBeanFactory类型
	 * 
	 * @param context
	 * @return
	 */
	private static ConfigurableListableBeanFactory tryCastTypeToConfigurableListableBeanFactory(
			AbstractRefreshableConfigApplicationContext context) {
		if (context instanceof AbstractRefreshableApplicationContext) {
			return ((AbstractRefreshableApplicationContext) context).getBeanFactory();
		}
		return null;
	}
}
