package cn.java.demo.hbm.test;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import cn.java.demo.hbm.entity.one.User;
import cn.java.demo.hbm.entity.one.UserName;
import cn.java.demo.hbm.util.SessionFactoryUtil;

public class OneTest {

	public static void main(String[] args) {
		SessionFactoryUtil.openSessionFactory();
		User user = insert();
		selectOne(user);
		selectOneByCriteria(user);
		selectOneByQuery(user);
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
	
	private static void selectOne(User user) {
		System.out.println("-----selectOne-------");
		Session session = null;
		Transaction transaction = null;
		
		try {
			session = SessionFactoryUtil.getSession();
			transaction = session.beginTransaction();
			User userTemp = (User) session.get(User.class, user.getId()); // 获取数据
			System.out.println(userTemp);
			transaction.commit();
		} finally {
			if (session != null)
				session.close();
		}
	}
	
	private static void selectOneByCriteria(User user) {
		System.out.println("-----selectOneByCriteria-------");
		Session session = null;
		try {
			session = SessionFactoryUtil.getSession();
			Criteria criteria = session.createCriteria(User.class);
			criteria.add(Restrictions.eq("id", user.getId()));
			User userTemp = (User) criteria.uniqueResult();
			System.out.println(userTemp);
		} finally {
			if (session != null)
				session.close();
		}
	}
	private static void selectOneByQuery(User user) {
		System.out.println("-----selectOneByQuery-------");
		Session session = null;
		try {
			session = SessionFactoryUtil.getSession();
			String hql = "from User as user where user.id = :id AND  user.id = ?0";
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

	private static User insert() {
		System.out.println("-----insert-------");
		User user = new User();
		user.setBirthday(new Date());
		UserName userName = new UserName();
		userName.setFirstName("firstName0");
		userName.setLastName("lastName0");
		user.setUserName(userName);
		
		Session session = null;
		Transaction transaction = null;
		try {
			session = SessionFactoryUtil.getSession();
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
			session = SessionFactoryUtil.getSession();
			transaction = session.beginTransaction();
			session.delete(user);  // 删除
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
			
			UserName userName = new UserName();
			userName.setFirstName("firstName0_new");
			userName.setLastName("lastName0_new");
			user.setUserName(userName);
			
			session = SessionFactoryUtil.getSession();
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
			session = SessionFactoryUtil.getSession();
			Query query = null;
			
			if(true){
				query = session.createQuery("from User where id <> :id AND first_name <> :first_name"); 
				query.setParameter("id", 100);
				query.setParameter("first_name", user.getUserName().getFirstName()+"_null");
			}
			else if(false){
				query = session.createQuery("from User where id <> ?0 AND first_name <> :first_name"); 
				query.setParameter("0", 100);
				query.setParameter("first_name", user.getUserName().getFirstName()+"_null");
			}
			else{
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
