package cn.java.curd.test;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import cn.java.curd.entity.Student;
import cn.java.curd.entity.Teacher;

public class Helloworld {

	public static void main(String[] args) {
		testAnnotataion();
		testXml();

	}

	/**
	 * 使用XML配置
	 */
	public static void testXml() {
		Student s = new Student();
		s.setId(1);
		s.setName("zhangsan");
		s.setAge(8);

		//		SessionFactory sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
		Configuration configuration = new Configuration();
		configuration.configure();//解析出所有XML文件的DOM树
		/**
		   	服务注册表
		 */
		ServiceRegistry serviceRegistry = new ServiceRegistryBuilder()//创建对象
				.applySettings(configuration.getProperties())//hibernate.cfg.xml配置文件中的配置信息
				.buildServiceRegistry();//创建注册表
		SessionFactory sessionFactory = configuration
				.buildSessionFactory(serviceRegistry);
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		session.save(s);
		session.getTransaction().commit();
	}

	/**
	 * 使用注解
	 */
	public static void testAnnotataion() {
		Teacher t = new Teacher();
		t.setName("t1");
		t.setTitle("middle");

		//		SessionFactory sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
		Configuration configuration = new Configuration();
		configuration.configure();
		ServiceRegistry serviceRegistry = new ServiceRegistryBuilder()
				.applySettings(configuration.getProperties())
				.buildServiceRegistry();
		SessionFactory sessionFactory = configuration
				.buildSessionFactory(serviceRegistry);

		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		session.save(t);
		session.getTransaction().commit();
	}

	public static void test1() {

		Teacher teacher = new Teacher();
		teacher.setId(1);
		teacher.setName("t1");
		teacher.setTitle("middle");

		SessionFactory sessionFactory = new AnnotationConfiguration()
				.addFile("hibernate/hibernate.cfg.xml").configure()
				.addPackage("cn.java.hibernate.entity")
				.addAnnotatedClass(Teacher.class).buildSessionFactory();

		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			session.save(teacher);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null)
				transaction.rollback();
		} finally {
			session.close();
		}
	}
}
