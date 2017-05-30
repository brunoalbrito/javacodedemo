package cn.java.demo.jpa.util;

import java.io.File;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import cn.java.common.util.SessionFactoryUtil0;

public final class SessionFactoryUtil {

	private static SessionFactory sessionFactory;


	private SessionFactoryUtil() {
	}

	static {
		sessionFactory = SessionFactoryUtil0.getSessionFactory("./" + getPackageDir() + "/hibernate.cfg.xml");
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public static Session getSession() {
		return sessionFactory.openSession();
	}

	
	private static String getPackageDir() {
		String dirName = SessionFactoryUtil.class.getName();
		dirName = dirName.substring(0, dirName.length() - (SessionFactoryUtil.class.getSimpleName().length() + 1));
		dirName = dirName.replace(".", File.separator);
		return dirName;
	}


}
