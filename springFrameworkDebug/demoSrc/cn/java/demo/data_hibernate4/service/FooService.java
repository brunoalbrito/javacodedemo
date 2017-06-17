package cn.java.demo.data_hibernate4.service;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
public class FooService {
	
	private SessionFactory hibernate4SessionFactory;
	public void setHibernate4SessionFactory(SessionFactory hibernate4SessionFactory) {
		this.hibernate4SessionFactory = hibernate4SessionFactory;
	}


	public void testMethod() {
		if(hibernate4SessionFactory==null){
			throw new RuntimeException("sessionFactory is null.");
		}
		Session session = hibernate4SessionFactory.openSession();
		
		{
			session.beginTransaction();
			SQLQuery sqlQuery = session
					.createSQLQuery("INSERT INTO tb_user(id,username,password) VALUES(?,?,?)");
			sqlQuery.setInteger(0, 1);
			sqlQuery.setString(1, "username1");
			sqlQuery.setString(2, "password1");
			int affectRawCount = sqlQuery.executeUpdate();
			session.getTransaction().commit();
			System.out.println("insert affectRawCount = " + affectRawCount);
		}
		
		session.close();
	}
	
}
