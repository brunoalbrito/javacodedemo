package cn.java.listener.event;

import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

public class MyHttpSessionAttributeListener implements HttpSessionAttributeListener {

	/**
	 * 添加属性的时候触发
	 * 触发地点：request.getSession().setAttribute("key1","value1");
	 */
	@Override
	public void attributeAdded(HttpSessionBindingEvent event) {
//		event.getSource() === event.getSession() == org.apache.catalina.session.StandardSessionFacade
//		event.getName() == "key1"
//		event.getValue() == "value1"
	}

	/**
	 * 删除属性的时候触发
	 * 触发地点：request.getSession().removeAttribute("key1")
	 */
	@Override
	public void attributeRemoved(HttpSessionBindingEvent event) {
//		event.getSource() === event.getSession() == org.apache.catalina.session.StandardSessionFacade
//		event.getName() == "key1"
//		event.getValue() == "旧值"
	}

	/**
	 * 替换、修改属性的时候触发
	 * 触发地点：request.getSession().setAttribute("key1","value1_new");
	 */
	@Override
	public void attributeReplaced(HttpSessionBindingEvent event) {
//		event.getSource() === event.getSession() == org.apache.catalina.session.StandardSessionFacade
//		event.getName() == "key1"
//		event.getValue() == "旧值"
	}

}
