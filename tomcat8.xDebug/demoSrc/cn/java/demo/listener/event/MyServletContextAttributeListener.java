package cn.java.demo.listener.event;

import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;

public class MyServletContextAttributeListener implements ServletContextAttributeListener {

	
	/**
	 * 添加属性的时候触发
	 * 触发地点：request.getServletContext().setAttribute("key1","value1");
	 */
	@Override
	public void attributeAdded(ServletContextAttributeEvent event) {
//		event.getServletContext() == event.getSource() == org.apache.catalina.core.ApplicationContextFacade  
//		event.getName() == "key1"
//		event.getValue() == "value1"
	}

	
	/**
	 * 删除属性的时候触发
	 * 触发地点：request.getServletContext().removeAttribute("key1")
	 */
	@Override
	public void attributeRemoved(ServletContextAttributeEvent event) {
//		event.getServletContext() == event.getSource() == org.apache.catalina.core.ApplicationContextFacade  
//		event.getName() == "key1"
//		event.getValue() == "旧值"
	}


	/**
	 * 替换、修改属性的时候触发
	 * 触发地点：request.getServletContext().setAttribute("key1","value1_new");
	 */
	@Override
	public void attributeReplaced(ServletContextAttributeEvent event) {

//		event.getServletContext() == event.getSource() == org.apache.catalina.core.ApplicationContextFacade  
//		event.getName() == "key1"
//		event.getValue() == "旧值"
	}

}
