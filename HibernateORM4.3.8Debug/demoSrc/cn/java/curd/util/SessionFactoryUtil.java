package cn.java.curd.util;

import java.io.File;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

public class SessionFactoryUtil {

	public static String getDir() {
		String dirName = SessionFactoryUtil.class.getName();
		dirName = dirName.substring(0, dirName.length() - (SessionFactoryUtil.class.getSimpleName().length() + 1));
		dirName = dirName.replace(".", File.separator);
		return dirName;
	}

	public static Session getSession() {
		Configuration configuration = new Configuration();
		configuration.configure("./" + getDir() + "/hibernate.cfg.xml");//解析出所有XML文件的DOM树
		ServiceRegistry serviceRegistry = new ServiceRegistryBuilder()//创建对象
				.applySettings(configuration.getProperties())//hibernate.cfg.xml配置文件中的配置信息
				.buildServiceRegistry();//创建注册表
		SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);
		return sessionFactory.getCurrentSession();
	}
}
