package cn.java.debug.debug;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import cn.java.curd.entity.Student;

public class Debug {

	/*
	 	1、读取hibernate.cfg.xml文件，在中间遇到<mapping>标签，进而读取mapping文件
	 */
	public static void main(String[] args) {
		Student student = new Student();
		student.setId(1);
		student.setName("zhangsan");
		student.setAge(8);

		Configuration configuration = new Configuration();
		configuration.configure();//
		
		// 创建“标准服务注册中心”
		StandardServiceRegistryBuilder serviceRegistryBuilder = new StandardServiceRegistryBuilder();
		serviceRegistryBuilder.applySettings(configuration.getProperties());
		ServiceRegistry serviceRegistry = serviceRegistryBuilder.build();
		
		// 
		SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry); // org.hibernate.internal.SessionFactoryImpl
		Session session = sessionFactory.getCurrentSession();
		
		//Transaction result = transactionCoordinator.getTransaction(); 
		//result.begin();
		session.beginTransaction();
		session.save(student);
		session.getTransaction().commit();
		
//		session.getTransaction().begin();
//		session.save(student);
//		session.getTransaction().commit();
	}

}
