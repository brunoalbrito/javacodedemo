package cn.java.demo.jpa.entity.test;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import cn.java.demo.jpa.entity.one.User;
import cn.java.demo.jpa.util.SessionFactoryUtil;

public class CrudTest {

	public static void main(String[] args) {
		SessionFactoryUtil.openSessionFactory();
		User user = insert();
		selectOne(user);
		{
			CriteriaTest.selectOneByCriteria(user);
			CriteriaTest.selectListByCriteria(user);
			CreateQueryTest.selectOneByQuery(user);
			CreateQueryTest.selectListByQuery(user);
			NamedQueryTest.selectOneByQuery(user);
			NamedQueryTest.selectListByQuery(user);
		}
		update(user);
		selectOne(user);
		delete(user);
		selectOne(user);

		insert();
		insert();
		insert();
		selectList(user);
		SessionFactoryUtil.closeSessionFactory();
	}

	public static class NamedQueryTest {
		public static void selectOneByQuery(User user) {
			System.out.println("-----NamedQueryTest::selectOneByQuery()-------");
			Session session = null;
			try {
				session = SessionFactoryUtil.openSession();
				String sql = "select * from jpa_user as user where user.id = :id AND user.id = ?0";
				Query query = session.createSQLQuery(sql).addEntity(User.class);
				;
				query.setParameter("id", user.getId());
				query.setParameter("0", user.getId());
				User userTemp = (User) query.uniqueResult();
				System.out.println(userTemp);
			} finally {
				if (session != null)
					session.close();
			}
		}

		public static void selectListByQuery(User user) {
			System.out.println("-----NamedQueryTest::selectOneByQuery-------");
			Session session = null;
			try {
				session = SessionFactoryUtil.openSession();
				String sql = "select * from jpa_user as user where user.id = :id AND user.id = ?0";
				Query query = session.createSQLQuery(sql).addEntity(User.class);
				;
				query.setParameter("id", user.getId());
				query.setParameter("0", user.getId());
				query.setFirstResult(0);
				query.setMaxResults(10);
				List<User> list = query.list(); // 结果列表
				for (User userTemp : list) {
					System.out.println(userTemp);
				}
			} finally {
				if (session != null)
					session.close();
			}
		}
	}

	public static class CreateQueryTest {

		public static void selectOneByQuery(User user) {
			System.out.println("-----CreateQueryTest::selectOneByQuery()-------");
			Session session = null;
			try {
				session = SessionFactoryUtil.openSession();
				String hql = "from User as user where user.id = :id AND user.id = ?0";
				Query query = session.createQuery(hql);
				query.setParameter("id", user.getId());
				query.setParameter("0", user.getId());
				User userTemp = (User) query.uniqueResult();
				System.out.println(userTemp);
			} finally {
				if (session != null)
					session.close();
			}
		}

		public static void selectListByQuery(User user) {
			System.out.println("-----CreateQueryTest::selectListByQuery()-------");
			Session session = null;
			try {
				session = SessionFactoryUtil.openSession();
				String hql = "from User as user where user.id = :id AND user.id = ?0";
				Query query = session.createQuery(hql);
				query.setParameter("id", user.getId());
				query.setParameter("0", user.getId());
				query.setFirstResult(0);
				query.setMaxResults(10);
				List<User> list = query.list(); // 结果列表
				for (User userTemp : list) {
					System.out.println(userTemp);
				}
			} finally {
				if (session != null)
					session.close();
			}
		}
	}

	public static class CriteriaTest {
		public static void selectOneByCriteria(User user) {
			System.out.println("-----CriteriaTest::selectOneByCriteria()-------");
			Session session = null;
			try {
				session = SessionFactoryUtil.openSession();
				Criteria criteria = session.createCriteria(User.class);
				criteria.add(Restrictions.eq("id", user.getId()));
				User userTemp = (User) criteria.uniqueResult(); // 一行记录
				System.out.println(userTemp);
			} finally {
				if (session != null)
					session.close();
			}
		}

		public static void selectListByCriteria(User user) {
			System.out.println("-----CriteriaTest::selectListByCriteria()-------");
			Session session = null;
			try {
				session = SessionFactoryUtil.openSession();
				DetachedCriteria detachedCriteria = DetachedCriteria.forClass(User.class);//不是从Session的，离线
				detachedCriteria.add(Restrictions.eq("id", user.getId())); //查询条件
				// detachedCriteria.add(Restrictions.isNotEmpty("userName"));
				Criteria criteria = detachedCriteria.getExecutableCriteria(session);
				criteria.setFirstResult(0);
				criteria.setMaxResults(10);
				List<User> list = criteria.list(); // 多行记录
				for (User userTemp : list) {
					System.out.println(userTemp);
				}
			} finally {
				if (session != null)
					session.close();
			}
		}
	}

	private static void selectOne(User user) {
		System.out.println("-----selectOne-------");
		Session session = null;
		Transaction transaction = null;

		try {
			session = SessionFactoryUtil.openSession();
			transaction = session.beginTransaction();
			User userTemp = (User) session.get(User.class, user.getId()); // 获取数据
			System.out.println(userTemp);
			transaction.commit();
		} finally {
			if (session != null)
				session.close();
		}
	}

	private static User insert() {
		System.out.println("-----insert-------");
		User user = new User();
		user.setUserName("username" + System.nanoTime());
		user.setPassword("Password0");
		Session session = null;
		Transaction transaction = null;
		try {
			session = SessionFactoryUtil.openSession();
			transaction = session.beginTransaction();
			session.save(user);
			transaction.commit();
			System.out.println(user);
		} finally {
			if (session != null)
				session.close();
		}
		return user;
	}

	private static void delete(User user) {
		System.out.println("-----delete-------");
		Session session = null;
		Transaction transaction = null;

		try {
			session = SessionFactoryUtil.openSession();
			transaction = session.beginTransaction();
			session.delete(user); // 删除
			transaction.commit();
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	private static void update(User user) {
		System.out.println("-----update-------");
		Session session = null;
		Transaction transaction = null;

		try {
			user.setUserName("UserName_new");
			session = SessionFactoryUtil.openSession();
			transaction = session.beginTransaction();
			session.update(user); // 修改
			transaction.commit();
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	private static void selectList(User user) {
		System.out.println("-----selectList-------");
		Session session = null;
		try {
			session = SessionFactoryUtil.openSession();
			Query query = null;

			if (true) {
				query = session.createQuery("from User where id <> :id AND user_name <> :user_name");
				query.setParameter("id", "100");
				query.setParameter("user_name", user.getUserName() + "_null");
			} else if (false) {
				query = session.createQuery("from User where id <> ?0 AND user_name <> :user_name");
				query.setParameter("0", "100");
				query.setParameter("user_name", user.getUserName() + "_null");
			} else {
				query = session.createQuery("from User");
			}
			List<User> userList = (List<User>) query.list();
			for (int i = 0; i < userList.size(); i++) {
				User userEntity = userList.get(i);
				System.out.println(userEntity);
			}
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

}
