package cn.java.demo.hbm.util;

import java.io.File;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import cn.java.common.util.SessionFactoryUtil0;

public final class SessionFactoryUtil {

	private static SessionFactory sessionFactory;


	private SessionFactoryUtil() {
	}

	public static void openSessionFactory() {
		sessionFactory = SessionFactoryUtil0.getSessionFactory("./" + getPackageDir() + "/hibernate.cfg.xml");
	}
	
	public static void closeSessionFactory() {
		if(sessionFactory==null){
			throw new RuntimeException("sessionFactory is null.");
		}
		sessionFactory.close();
	}

	
	public static SessionFactory getSessionFactory() {
		if(sessionFactory==null){
			throw new RuntimeException("sessionFactory is null.");
		}
		return sessionFactory;
	}
	
	public static Session openSession() {
		if(sessionFactory==null){
			throw new RuntimeException("sessionFactory is null.");
		}
		return sessionFactory.openSession();
	}

	
	private static String getPackageDir() {
		String dirName = SessionFactoryUtil.class.getName();
		dirName = dirName.substring(0, dirName.length() - (SessionFactoryUtil.class.getSimpleName().length() + 1));
		dirName = dirName.replace(".", File.separator);
		return dirName;
	}


}
