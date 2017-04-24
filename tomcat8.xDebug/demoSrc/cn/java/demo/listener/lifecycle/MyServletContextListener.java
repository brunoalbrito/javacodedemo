package cn.java.demo.listener.lifecycle;

import javax.servlet.ServletContextEvent;

/**
 * 服务器启动、关闭
 * 
 * 启动上下文、或者销毁上下文的时候触发
 * @author Administrator
 *
 */
public class MyServletContextListener implements javax.servlet.ServletContextListener {


	/**
	 * 触发地点：org.apache.catalina.core.StandardContext.startInternal()
	 */
	@Override
	public void contextInitialized(ServletContextEvent event) {
//		event.getSource() == event.getServletContext() == org.apache.catalina.core.ApplicationContextFacade  
	}
	
	/**
	 * 触发地点：org.apache.catalina.core.StandardContext.stopInternal()
	 */
	@Override
	public void contextDestroyed(ServletContextEvent event) {
//		event.getSource() == event.getServletContext() == org.apache.catalina.core.ApplicationContextFacade  

	}

}
