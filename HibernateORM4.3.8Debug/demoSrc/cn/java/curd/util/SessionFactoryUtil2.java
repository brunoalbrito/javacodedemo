package cn.java.curd.util;

import java.io.File;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public final class SessionFactoryUtil2 {

	private static SessionFactory sessionFactory;

	private SessionFactoryUtil2() {

	}
	
	public static String getDir() {
		String dirName = SessionFactoryUtil.class.getName();
		dirName = dirName.substring(0, dirName.length() - (SessionFactoryUtil.class.getSimpleName().length() + 1));
		dirName = dirName.replace(".", File.separator);
		return dirName;
	}

	//耗时资源放到代码块中，只执行一次
	static {
		Configuration cfg = new Configuration();
//		cfg.configure();// 用来完成Hibernate的初始化---用来读取配置文件中的信息，可以指定配置文件的位置
		// 得到sessionFactoroy的工厂对象，相当于DirverManager，
		cfg.configure("./" + getDir() + "/hibernate.cfg.xml");
		sessionFactory = cfg.buildSessionFactory();
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public static Session getSession() {
		return sessionFactory.openSession();
	}

}