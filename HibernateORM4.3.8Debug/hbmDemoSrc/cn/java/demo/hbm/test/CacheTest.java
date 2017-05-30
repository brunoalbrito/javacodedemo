package cn.java.demo.hbm.test;

import java.util.Date;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.stat.Statistics;

import cn.java.demo.hbm.entity.one.User;
import cn.java.demo.hbm.entity.one.UserName;
import cn.java.demo.hbm.util.SessionFactoryUtil;

public class CacheTest {

	public static void main(String args[]) {
		User user = addUser();
		System.out.println("----------");
		getUser(user.getId());
		Statistics st = SessionFactoryUtil.getSessionFactory().getStatistics();
		System.out.println("hit=" + st.getSecondLevelCacheHitCount());
		System.out.println("miss=" + st.getSecondLevelCacheMissCount());
		System.out.println("put=" + st.getSecondLevelCachePutCount());
	}
	
	private static User getUser(int id) {
		Session session = null;
		User user = null;
		SessionFactoryUtil.getSessionFactory().evict(User.class);
		try {
			session = SessionFactoryUtil.getSession();
			user = (User) session.get(User.class, id);
			System.out.println(user);
			//session.evict(user);
			//String hql="from Objcet";
			//session.clear();

			user = (User) session.get(User.class, id);
			session.clear();
			//user=(User)session.load(userClass,id);
			//Query query=s.createQuery("from User where id="+id);
			//query.setCacheable(true);
			//user=(User)query.uniqueResult();
			System.out.println(user.getUserName());
		} finally {
			if (session != null)
				session.close();
		}

		try {
			session = SessionFactoryUtil.getSession();
			user = (User) session.get(User.class, id);
			System.out.println(user.getUserName());
		} finally {
			if (session != null)
				session.close();
		}
		return user;
	}

	private static User addUser() {
		User user = new User();
		user.setBirthday(new Date());
		
		UserName n = new UserName();
		n.setFirstName("firstName0");
		n.setLastName("lastName0");
		user.setUserName(n);
		
		Session session = null;
		Transaction transaction = null;
		try {
			session = SessionFactoryUtil.getSession();
			transaction = session.beginTransaction();
			session.save(user);
			transaction.commit();
			System.out.println(user.getUserName());
		} finally {
			if (session != null)
				session.close();
		}
		return user;

	}

}
