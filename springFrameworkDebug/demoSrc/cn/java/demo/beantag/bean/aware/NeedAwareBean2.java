package cn.java.demo.beantag.bean.aware;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.EmbeddedValueResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.SystemEnvironmentPropertySource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jndi.JndiPropertySource;
import org.springframework.util.StringValueResolver;
import org.springframework.web.context.support.ServletConfigPropertySource;
import org.springframework.web.context.support.ServletContextPropertySource;
import org.springframework.web.context.support.StandardServletEnvironment;
import org.springframework.web.context.support.XmlWebApplicationContext;

public class NeedAwareBean2 implements EnvironmentAware,EmbeddedValueResolverAware,ResourceLoaderAware,ApplicationEventPublisherAware,MessageSourceAware,ApplicationContextAware {

	/*
	 	以下在  org.springframework.context.support.ApplicationContextAwareProcessor.postProcessBeforeInitialization(...) 注入
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if(applicationContext instanceof XmlWebApplicationContext){

		}
		else if(applicationContext instanceof ClassPathXmlApplicationContext){

		}
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {
		if(messageSource instanceof XmlWebApplicationContext){

		}
		else if(messageSource instanceof ClassPathXmlApplicationContext){

		}
	}

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		if(applicationEventPublisher instanceof XmlWebApplicationContext){

		}
		else if(applicationEventPublisher instanceof ClassPathXmlApplicationContext){

		}
	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		if(resourceLoader instanceof XmlWebApplicationContext){

		}
		else if(resourceLoader instanceof ClassPathXmlApplicationContext){

		}
	}

	@Override
	public void setEmbeddedValueResolver(StringValueResolver resolver) {
		if(resolver instanceof EmbeddedValueResolver){

		}
	}

	@Override
	public void setEnvironment(Environment environment) {
		if(environment instanceof StandardServletEnvironment){
			StandardServletEnvironment standardServletEnvironment = (StandardServletEnvironment) environment;
			
			System.out.println("-------获取“属性源”---------");
			{
				MutablePropertySources mutablePropertySources = standardServletEnvironment.getPropertySources();
				System.out.println("-------在“所有属性源”中查找---------");
				{
					for (PropertySource propertySource : mutablePropertySources) {
						
						if(propertySource instanceof JndiPropertySource){ // 在jndi中获取属性值，org.springframework.jndi.JndiLocatorDelegate
							
						}
						else if(propertySource instanceof ServletConfigPropertySource){ // 从Servlet的配置中获取(是web应用)， javax.servlet.ServletConfig
							
						}
						else if(propertySource instanceof SystemEnvironmentPropertySource){ // 在系统属性在系统环境变量中获取属性值，  java.lang.System.getenv()
							
						}
						else if(propertySource instanceof MapPropertySource){ // 在系统属性在系统环境变量中获取属性值， java.lang.System.getProperties()
							
						}
						else if(propertySource instanceof ServletContextPropertySource){ // 从Servlet的上下文中获取(是web应用)，javax.servlet.ServletContext
							
						}
						
						String propertyName = "property0";
						if(propertySource.containsProperty(propertyName)){
							propertySource.getProperty(propertyName);
						}
					}
				}
				
				System.out.println("-------在“某个属性源”中查找---------");
				{
					if(mutablePropertySources.contains("propertySource0")){
						PropertySource propertySource = mutablePropertySources.get("propertySource0");
						String propertyName = "property0";
						if(propertySource.containsProperty(propertyName)){
							propertySource.getProperty(propertyName);
						}
					}
				}
				
			}
			
			System.out.println("-------在“所有属性源”中查找---------");
			{
				String propertyName = "property0";
				if(standardServletEnvironment.containsProperty(propertyName)){
					System.out.println(standardServletEnvironment.getProperty(propertyName));
				}
			}
		}
	}
}
