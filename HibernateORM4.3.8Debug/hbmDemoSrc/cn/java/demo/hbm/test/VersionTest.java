package cn.java.demo.hbm.test;

import java.util.Date;

import org.hibernate.Session;
import org.hibernate.Transaction;

import cn.java.demo.hbm.entity.one.User;
import cn.java.demo.hbm.entity.one.UserName;
import cn.java.demo.hbm.util.SessionFactoryUtil;

public class VersionTest {

	public static void main(String args[]) {
		SessionFactoryUtil.openSessionFactory();
		User user = addUser();
		System.out.println("----------");
		update(user.getId());
		SessionFactoryUtil.closeSessionFactory();
	}

	static void update(int id) {

		Session session1 = null;
		session1 = SessionFactoryUtil.openSession();
		Transaction transaction1 = session1.beginTransaction();
		User user1 = (User) session1.get(User.class, id);

		Session session2 = null;
		session2 = SessionFactoryUtil.openSession();
		Transaction transaction2 = session2.beginTransaction();
		User user2 = (User) session2.get(User.class, id);

		user1.getUserName().setFirstName("new1 firstName");
		user2.getUserName().setFirstName("new2 firstName");

		transaction2.commit();
		transaction1.commit();

		session1.close();
		session2.close();
	}

	public static User addUser() {
		User user = new User();
		user.setBirthday(new Date());
		UserName n = new UserName();
		n.setFirstName("firstName0");
		n.setLastName("lastName0");
		user.setUserName(n);
		
		Session session = null;
		try {
			session = SessionFactoryUtil.openSession();
			session.save(user);
			System.out.println(user.getUserName());
		} finally {
			if (session != null)
				session.close();
		}
		return user;
	}

}
