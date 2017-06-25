package cn.java.demo.orm_hibernate4.dao.impl;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import cn.java.demo.orm_hibernate4.dao.FooDao;

public class FooDaoImpl implements FooDao {
	private SessionFactory hibernate4SessionFactory;

	public void setHibernate4SessionFactory(SessionFactory hibernate4SessionFactory) {
		this.hibernate4SessionFactory = hibernate4SessionFactory;
	}

	public int insert() {
		if (hibernate4SessionFactory == null) {
			throw new RuntimeException("sessionFactory is null.");
		}
		int affectRawCount = 0;
		Session session = null;
		try {
			session = hibernate4SessionFactory.openSession();
			{
				session.beginTransaction();
				SQLQuery sqlQuery = session.createSQLQuery("INSERT INTO tb_user(id,username,password) VALUES(?,?,?)");
				sqlQuery.setInteger(0, 1);
				sqlQuery.setString(1, "username1");
				sqlQuery.setString(2, "password1");
				affectRawCount = sqlQuery.executeUpdate();
				session.getTransaction().commit();
				System.out.println("insert affectRawCount = " + affectRawCount);
			}
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return affectRawCount;

	}
}
