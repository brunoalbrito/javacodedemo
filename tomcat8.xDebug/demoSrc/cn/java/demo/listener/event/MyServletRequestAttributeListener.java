package cn.java.demo.listener.event;

import javax.servlet.ServletRequestAttributeEvent;
import javax.servlet.ServletRequestAttributeListener;

public class MyServletRequestAttributeListener implements ServletRequestAttributeListener {

	/**
	 * 添加属性的时候触发
	 * 触发地点：request.setAttribute("key1","value1");
	 */
	@Override
	public void attributeAdded(ServletRequestAttributeEvent event) {
//		event.getServletContext() == event.getSource() == org.apache.catalina.core.ApplicationContextFacade
//		event.getName() == "key1"
//		event.getValue() == "value1"
//		event.getServletRequest() == org.apache.catalina.connector.RequestFacade == request
		System.out.println(this.getClass().getCanonicalName() + "::attributeAdded");
	}

	/**
	 * 删除属性的时候触发
	 * 触发地点：request.removeAttribute("key1")
	 */
	@Override
	public void attributeRemoved(ServletRequestAttributeEvent event) {
//		event.getServletContext() == event.getSource() == org.apache.catalina.core.ApplicationContextFacade
//		event.getName() == "key1"
//		event.getValue() == "旧值"
//		event.getServletRequest() == org.apache.catalina.connector.RequestFacade == request
	}
	
	/**
	 * 替换、修改属性的时候触发
	 * 触发地点：request.setAttribute("key1","value1_new");
	 */
	@Override
	public void attributeReplaced(ServletRequestAttributeEvent event) {
//		event.getServletContext() == event.getSource() == org.apache.catalina.core.ApplicationContextFacade
//		event.getName() == "key1"
//		event.getValue() == "旧值"
//		event.getServletRequest() == org.apache.catalina.connector.RequestFacade == request
	}

}
