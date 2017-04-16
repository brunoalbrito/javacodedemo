package cn.java.demo.listener.event;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionIdListener;

public class MyHttpSessionIdListener implements HttpSessionIdListener {

	/**
	 * 修改sessionId的时候触发
	 * org.apache.catalina.session.StandardSession.tellChangedSessionId(....);
	 * 触发地点：request.changeSessionId();
	 */
	@Override
	public void sessionIdChanged(HttpSessionEvent event, String oldSessionId) {
//		event.getSession() === event.getSource()  === org.apache.catalina.session.StandardSessionFacade
	}

}
