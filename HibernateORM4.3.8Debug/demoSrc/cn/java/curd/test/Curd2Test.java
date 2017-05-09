package cn.java.curd.test;

import java.sql.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import cn.java.curd.entity.User;
import cn.java.curd.util.SessionFactoryUtil2;

public class Curd2Test {

	public void testSave1() {
		Session session = null;
		Transaction tx = null;
		User user = null;
		try {
			session = SessionFactoryUtil2.getSession();
			tx = session.beginTransaction();

			//Transient状态
			user = new User();
			user.setUsername("李四");
			user.setAge(123);

			//persistent状态,当属性发生改变的时候，hibernate会自动和数据库同步
			session.save(user);

			user.setUsername("王五");//会修改数据库的内容，这就是持久状态
			//session.update(user);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			session.close();
		}

		//detached状态
		user.setUsername("张三");//托管状态
		try {
			session = SessionFactoryUtil2.getSession();
			session.beginTransaction();
			//persistent状态
			session.update(user);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.close();
		}
	}

	/**
	 * 查询+修改
	 */
	public void testReadByGetMethod1() {
		Session session = null;
		try {
			session = SessionFactoryUtil2.getSession();
			session.beginTransaction();

			//马上发出查询sql，加载User对象
			User user = (User) session.get(User.class,
					"402880d01b9bf210011b9bf2a2ff0001");
			System.out.println("user.name=" + user.getUsername());

			//persistent状态,当属性发生改变的时候，hibernate会自动和数据库同步
			user.setUsername("龙哥");
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.close();
		}
	}

	/**
	 * 查询
	 */
	public void testReadByGetMethod2() {
		Session session = null;
		try {
			session = SessionFactoryUtil2.getSession();
			session.beginTransaction();

			//采用get加载数据，如果数据库中不存在相应的数据，返回null
			User user = (User) session.get(User.class, "asdfsafsdfdsf");

			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.close();
		}
	}

	/**
	 * 懒查询+修改
	 */
	public void testReadByLoadMethod1() {
		Session session = null;
		try {
			session = SessionFactoryUtil2.getSession();
			session.beginTransaction();

			//不会发出查询sql，因为load方法实现了lazy（懒加载或延迟加载）
			//延迟加载：只有真正使用这个对象的时候，才加载（发出sql语句）
			//hibernate延迟加载实现原理是代理方式
			User user = (User) session.load(User.class,
					"402880d01b9bf210011b9bf2a2ff0001");
			System.out.println("user.name=" + user.getUsername());

			//persistent状态,当属性发生改变的时候，hibernate会自动和数据库同步
			user.setUsername("发哥");
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.close();
		}
	}

	/**
	 * 懒查询
	 */
	public void testReadByLoadMethod2() {
		Session session = null;
		try {
			session = SessionFactoryUtil2.getSession();
			session.beginTransaction();

			//采用load加载数据，如果数据库中没有相应的数据
			//那么抛出ObjectNotFoundException
			User user = (User) session.load(User.class, "55555555");

			System.out.println(user.getUsername());

			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
			//throw new java.lang.RuntimeException();
		} finally {
			session.close();
		}
	}

	/**
	 * 修改数据
	 */
	public void testUpdate1() {
		Session session = null;
		try {
			session = SessionFactoryUtil2.getSession();
			session.beginTransaction();

			//手动构造的detached状态的对象
			User user = new User();
			user.setId(123);
			user.setUsername("德华");

			session.update(user);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.close();
		}
	}

	/**
	 * 删除
	 */
	public void testDelete1() {
		Session session = null;
		try {
			session = SessionFactoryUtil2.getSession();
			session.beginTransaction();

			//   //手动构造的detached状态的对象
			//   User user = new User();
			//   user.setId("402880d01b9be8dc011b9be9b23d0001");
			//   user.setName("德华");
			//   session.delete(user);

			User user = (User) session.load(User.class,
					"402880d01b9be8dc011b9be9b23d0001");
			session.delete(user);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.close();
		}

		//transient状态
	}

	public void testLimitQuery() {
		Session session = null;
		try {
			session = SessionFactoryUtil2.getSession();

			//Query 查询语句，select * 可以省略，也可以大小写不论，
			//但是，from 后面的就一定要大小写区分，因为它后面接的是 实体类
			Query query = session.createQuery("from User");

			//分页
			query.setFirstResult(0);//从第零条记录开始
			query.setMaxResults(2);//每页2条记录

			//返回一个List集合，hibernate的优点就是，不用再向集合中add这样添加元素了，Query已经自动提交了
			List userList = query.list();
			for (Iterator it = userList.iterator(); it.hasNext();) {
				User user = (User) it.next();
				System.out.println(user.getId());
				System.out.println(user.getUsername());
				System.out.println(user.getAge());
			}

			session.beginTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.beginTransaction().rollback();
		} finally {
			session.close();
		}
	}
}
