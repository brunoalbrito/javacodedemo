package cn.java.common.util;

import java.io.File;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;


public class SessionFactoryUtil0 {
	protected static String getPackageDir() {
		String dirName = SessionFactoryUtil0.class.getName();
		dirName = dirName.substring(0, dirName.length() - (SessionFactoryUtil0.class.getSimpleName().length() + 1));
		dirName = dirName.replace(".", File.separator);
		return dirName;
	}

	/**
	 * @param configuration
	 * @return
	 */
	public static ServiceRegistry getServiceRegistry(Configuration configuration) {
		StandardServiceRegistryBuilder serviceRegistryBuilder = new StandardServiceRegistryBuilder(); // 创建“标准服务注册中心”
		serviceRegistryBuilder.applySettings(configuration.getProperties());
		return serviceRegistryBuilder.build();
	}

	private static Configuration getConfiguration() {
		Configuration configuration = new Configuration();
		configuration.configure("./" + getPackageDir() + "/hibernate.cfg.xml");//解析出所有XML文件的DOM树
		return configuration;
	}

	public static SessionFactory getSessionFactory(Configuration configuration) {
		StandardServiceRegistryBuilder serviceRegistryBuilder = new StandardServiceRegistryBuilder(); // 创建“标准服务注册中心”
		serviceRegistryBuilder.applySettings(configuration.getProperties());
		ServiceRegistry serviceRegistry =  serviceRegistryBuilder.build();
		return configuration.buildSessionFactory(serviceRegistry);
	}

	public static SessionFactory getSessionFactory(String configPath) {
		Configuration configuration = new Configuration();
		configuration.configure(configPath);
		StandardServiceRegistryBuilder serviceRegistryBuilder = new StandardServiceRegistryBuilder(); // 创建“标准服务注册中心”
		serviceRegistryBuilder.applySettings(configuration.getProperties());
		ServiceRegistry serviceRegistry =  serviceRegistryBuilder.build();
		return configuration.buildSessionFactory(serviceRegistry);
	}

	public static Session getSession() {
		Configuration configuration = getConfiguration();
		ServiceRegistry serviceRegistry = getServiceRegistry(configuration);
		SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);
		return sessionFactory.getCurrentSession();
	}

	private static String getCallerClassName(final Class<?> clazz) {
		final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		final String className = clazz.getName();
		boolean classFound = false;
		for (int i = 1; i < stackTrace.length; i++) {
			final StackTraceElement element = stackTrace[i];
			final String callerClassName = element.getClassName();
			// check if class name is the requested class
			if (callerClassName.equals(className)){
				classFound = true;
			}
			else if (classFound) {
				return callerClassName;
			}
		}
		return null;
	}
}
