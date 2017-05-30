package cn.java.web.filter;

import java.io.File;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import cn.java.common.util.SessionFactoryUtil0;

public final class SessionFactoryThreadLocalUtil {

	private static ThreadLocal<Session> sessionThreadLocal  = new ThreadLocal<Session>();
	private static SessionFactory sessionFactory;

	static {
		sessionFactory = SessionFactoryUtil0.getSessionFactory("./" + getPackageDir() + "/hibernate.cfg.xml");
	}

	public static Session getThreadLocalSession() {
		if(sessionThreadLocal.get()==null){
			sessionThreadLocal.set(sessionFactory.openSession());
		}
		return sessionThreadLocal.get();
	}
	
	public static void closeThreadLocalSession() {
		if(sessionThreadLocal.get()!=null){
			sessionThreadLocal.get().close();
		}
	}

	private static String getPackageDir() {
		String dirName = SessionFactoryThreadLocalUtil.class.getName();
		dirName = dirName.substring(0, dirName.length() - (SessionFactoryThreadLocalUtil.class.getSimpleName().length() + 1));
		dirName = dirName.replace(".", File.separator);
		return dirName;
	}


}
