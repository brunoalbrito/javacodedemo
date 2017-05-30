package cn.java.curd.demo.demo0;

import java.io.File;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import cn.java.curd.entity.Student;
import cn.java.curd.entity.User;
import cn.java.curd.util.SessionFactoryUtil;
import cn.java.curd.util.SessionFactoryUtil2;

public class Test {
	
	public static void main(String[] args) {
		
		Student student = new Student();
		student.setId(1);
		student.setName("zhangsan");
		student.setAge(8);

		Session session = SessionFactoryUtil.getSession();
		try {
			session.getTransaction().begin();
			session.save(student);//保存
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
		}

	}

	public static void curdDemo() {
		User user = new User();
		user.setAge(26);
		user.setId(1);
		user.setUsername("xxx");
		add0(user);
		deleteWithRawSql();
		updateWithRawSql();
		selectOne0(1);
		selectOne1(1);
		selectList1();
		selectList0();
	}

	/**
	 * 添加用户
	 * 
	 * @param user
	 */
	public static void add0(User user) {

		Session session = null;
		Transaction tx = null;
		try {
			session = SessionFactoryUtil2.getSession();
			tx = session.beginTransaction();// 开启事务
			session.save(user);
			tx.commit();
		} finally {
			// 使资源得到释放
			if (session != null) {
				session.close();
			}
		}
	}

	/**
	 * 查询用户
	 * 
	 * @param id
	 * @return
	 */
	public static User selectOne0(int id) {
		Session session = null;
		try {
			session = SessionFactoryUtil2.getSession();
			Class clazz = User.class;//通过映射文件查找class对象
			//只能是主键id获取对象值，不能以其他的表字段名获取对象值，否则，只能用HQL语句查询。
			User user = (User) session.get(clazz, id);
			return user;
		} finally {
			// 使资源得到释放
			if (session != null) {
				session.close();
			}
		}
	}

	/**
	 * 查询用户,懒加载
	 * @param id
	 * @return
	 */
	public static User selectOne1(int id) {
		Session session = null;
		try {
			session = SessionFactoryUtil2.getSession();
			Class clazz = User.class;//通过映射文件查找class对象
			//只能是主键id获取对象值，不能以其他的表字段名获取对象值，否则，只能用HQL语句查询。
			//懒加载,运行后没有立刻访问相应的数据库，返回的是代理对象，永远不可能为空，当第一次使用该对象的时候，才去访问
			User user = (User) session.load(clazz, id);
			return user;
		} finally {
			// 使资源得到释放
			if (session != null) {
				session.close();
			}
		}
	}

	/**
	 * 查找用户列表
	 */
	public static void selectList1() {
		Session session = null;
		try {
			session = SessionFactoryUtil2.getSession();
			session.beginTransaction();
			Query query = session
					.createQuery("from User where id=? and age=:tage"); //在条件语句中，可以用？和:两种方式。两者方式各有不同。
			query.setParameter(0, 2); // 从0开始，而不是从1开始。采用？方式。
			//query.setInteger(0, 2);                // 上面的query.setParameter(0, 2)也可以用query.setInteger(0, 2)代替，效果一样 
			query.setParameter("tage", 27); // 采用：方式。前面参数名与之前的：后面的参数名必须一致。
			//query.setInteger("tage", 27);          // 上面的query.setParameter("tage", 27)也可以用query.setInteger("tage", 27)代替，效果一样 
			List<User> listUser = (List<User>) query.list();

		} finally {
			// 使资源得到释放
			if (session != null) {
				session.close();
			}
		}
	}

	/**
	 * 查找用户列表
	 */
	public static void selectList0() {
		Session session = null;
		try {
			session = SessionFactoryUtil2.getSession();
			session.beginTransaction();
			Query query = session.createQuery("from User");
			List<User> listUser = (List<User>) query.list();
			for (User user : listUser) {

				System.out.println("Id:" + user.getId() + " UserName:"
						+ user.getUsername() + " Age:" + user.getAge());

			}
		} finally {
			// 使资源得到释放
			if (session != null) {
				session.close();
			}
		}
	}

	/**
	 * 修改用户数据
	 */
	public static void updateWithRawSql() {
		int count;
		Session session = SessionFactoryUtil2.getSession();
		session.beginTransaction();
		Query query = session
				.createQuery("update User set username='xxx' where id=?");
		query.setInteger(0, 2);
		count = query.executeUpdate();
		session.getTransaction().commit();
		System.out.println("更新" + count + "条记录");
	}

	/**
	 * 删除用户
	 */
	public static void deleteWithRawSql() {
		int count;
		Session session = SessionFactoryUtil2.getSession();
		session.beginTransaction();
		Query query = session.createQuery("delete Teacher where id=?");
		query.setParameter(0, 1);
		count = query.executeUpdate(); // 删除记录采用Update方式
		session.getTransaction().commit();
		System.out.println("删除" + count + "条teacher记录");

	}
	
	public static class SessionFactoryUtil {

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
}
