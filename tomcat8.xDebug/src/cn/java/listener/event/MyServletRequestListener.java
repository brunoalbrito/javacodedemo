package cn.java.listener.event;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;

public class MyServletRequestListener implements ServletRequestListener {

	/**
	 * 请求销毁
	 * 触发地点：org.apache.catalina.core.StandardHostValve.invoke(Request request, Response response)
	 */
	@Override
	public void requestDestroyed(ServletRequestEvent event) {
//		event.getServletContext() == event.getSource() == org.apache.catalina.core.ApplicationContextFacade  
//		event.getServletRequest() == org.apache.catalina.connector.Request
		
	}

	/**
	 * 请求初始化
	 * 触发地点：org.apache.catalina.core.StandardHostValve.invoke(Request request, Response response)
	 */
	@Override
	public void requestInitialized(ServletRequestEvent event) {
//		event.getServletContext() == event.getSource() == org.apache.catalina.core.ApplicationContextFacade  
//		event.getServletRequest() == org.apache.catalina.connector.Request
	}

}
