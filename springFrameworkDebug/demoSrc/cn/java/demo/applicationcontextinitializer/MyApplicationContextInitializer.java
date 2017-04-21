package cn.java.demo.applicationcontextinitializer;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.support.StandardServletEnvironment;

//		<init-param>
//	        <param-name>globalInitializerClasses</param-name>
//	        <param-value>cn.java.test.applicationcontextinitializer.MyApplicationContextInitializer,cn.java.test.applicationcontextinitializer.MyApplicationContextInitializer</param-value>
//	    </init-param>
//	    <init-param>
//		    <param-name>contextInitializerClasses</param-name>
//		    <param-value>cn.java.test.applicationcontextinitializer.MyApplicationContextInitializer,cn.java.test.applicationcontextinitializer.MyApplicationContextInitializer</param-value>
//	    </init-param>
public class MyApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

	/**
	 * applicationContext ===  org.springframework.web.context.support.XmlWebApplicationContext
	 */
	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		
		if(applicationContext instanceof ConfigurableWebApplicationContext){
			ConfigurableWebApplicationContext applicationContextTemp = (ConfigurableWebApplicationContext)applicationContext;
//			ServletContext servletContext = applicationContextTemp.getServletContext();
		}
		
		if(applicationContext instanceof AbstractRefreshableConfigApplicationContext){
			AbstractRefreshableConfigApplicationContext applicationContextTemp = (AbstractRefreshableConfigApplicationContext)applicationContext;
			applicationContextTemp.setAllowBeanDefinitionOverriding(false);
			applicationContextTemp.setAllowCircularReferences(false);
		}
		StandardServletEnvironment environment = (StandardServletEnvironment) applicationContext.getEnvironment();
	}

}
