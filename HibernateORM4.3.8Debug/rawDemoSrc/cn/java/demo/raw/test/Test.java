package cn.java.demo.raw.test;

import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

import cn.java.demo.raw.util.SessionFactoryUtil;

public class Test {

	final private static String TABLE_NAME = "tb_user";

	public static void main(String[] args) {
		SessionFactoryUtil.openSessionFactory();
		
		Integer userId = 1;
		System.out.println("------insert-------");
		insert(userId);
		selectOne(userId);

		System.out.println("------update-------");
		update(userId);
		selectOne(userId);

		System.out.println("------delete-------");
		delete(userId);
		selectOne(userId);

		System.out.println("------selectList----");
		insert(1);
		insert(2);
		selectList();
		delete(1);
		delete(2);

		System.out.println("------selectJoin----");
		insert(1);
		insert(2);
		selectJoin();
		delete(1);
		delete(2);
		
		SessionFactoryUtil.closeSessionFactory();

	}

	/**
	 * 添加
	 */
	public static void insert(Integer userId) {
		Session session = SessionFactoryUtil.getSession();
		session.beginTransaction();
		SQLQuery sqlQuery = session
				.createSQLQuery("INSERT INTO " + TABLE_NAME + "(id,username,password) VALUES(?,?,?)");
		sqlQuery.setInteger(0, userId);
		sqlQuery.setString(1, "username1");
		sqlQuery.setString(2, "password1");
		int affectRawCount = sqlQuery.executeUpdate();
		session.getTransaction().commit();
		System.out.println("insert affectRawCount = " + affectRawCount);
		session.close();
	}

	/**
	 * 修改
	 */
	public static void update(Integer userId) {
		Session session = SessionFactoryUtil.getSession();
		session.beginTransaction();
		SQLQuery sqlQuery = session.createSQLQuery("UPDATE " + TABLE_NAME + " SET username='username_new' WHERE id=?");
		sqlQuery.setInteger(0, userId);
		int affectRawCount = sqlQuery.executeUpdate();
		session.getTransaction().commit();
		System.out.println("update affectRawCount = " + affectRawCount);
		session.close();
	}

	/**
	 * 查询
	 */
	@SuppressWarnings("unused")
	public static void selectOne(Integer userId) {
		Session session = SessionFactoryUtil.getSession();
		SQLQuery sqlQuery = session.createSQLQuery("SELECT * FROM " + TABLE_NAME + " WHERE id=?");
		sqlQuery.setInteger(0, userId);
		sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List list = sqlQuery.list();
		if (list != null && (list.size() > 0)) {
			Object object = (Object) list.get(0);
			System.out.println(object);
			if(false){
				Map map = (Map) list.get(0);
				System.out.println("map.get(\"username\") = " + map.get("username"));
			}
		}
		session.close();
	}

	/**
	 * 查询
	 */
	public static void selectJoin() {
		Session session = SessionFactoryUtil.getSession();
		SQLQuery sqlQuery = session
				.createSQLQuery("SELECT user0.id AS user0_id,user0.username,user1.id AS user1_id FROM " + TABLE_NAME
						+ " AS user0 LEFT JOIN " + TABLE_NAME + " user1 ON user0.id = user1.id");
		sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List list = sqlQuery.list();
		for (Object object : list) {
			System.out.println(object);
		}
		session.close();
	}

	/**
	 * 查询
	 */
	public static void selectList() {
		Session session = SessionFactoryUtil.getSession();
		SQLQuery sqlQuery = session.createSQLQuery("SELECT * FROM " + TABLE_NAME + "");
		sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List list = sqlQuery.list();
		for (Object object : list) {
			System.out.println(object);
		}
		session.close();
	}

	/**
	 * 删除
	 */
	public static void delete(Integer userId) {
		Session session = SessionFactoryUtil.getSession();
		session.beginTransaction();
		SQLQuery sqlQuery = session.createSQLQuery("delete FROM " + TABLE_NAME + " WHERE id=?");
		sqlQuery.setParameter(0, userId);
		int affectRawCount = sqlQuery.executeUpdate();
		session.getTransaction().commit();
		System.out.println("delete affectRawCount = " + affectRawCount);
		session.close();
	}

}
