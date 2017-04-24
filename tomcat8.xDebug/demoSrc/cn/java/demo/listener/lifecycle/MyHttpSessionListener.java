package cn.java.demo.listener.lifecycle;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Session的生命周期
 * @author Administrator
 *
 */
public class MyHttpSessionListener implements HttpSessionListener {


	/**
	 * 触发地点1：
	 * 		request.changeSessionId();
	 * 		org.apache.catalina.session.StandardSession.setId(String id)
	 * 		org.apache.catalina.session.StandardSession.tellNew()
	 */
	@Override
	public void sessionCreated(HttpSessionEvent event) {
//		event.getSource() == event.getSource() ==  org.apache.catalina.session.StandardSessionFacade
	}

	/**
	 * 触发地点：org.apache.catalina.session.StandardSession.expire(boolean notify)
	 */
	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
//		event.getSource() == event.getSource() ==  org.apache.catalina.session.StandardSessionFacade
	}

}
