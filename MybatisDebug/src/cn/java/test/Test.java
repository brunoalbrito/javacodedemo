package cn.java.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import cn.java.entity.User;
import cn.java.mapper.UserMapper;

public class Test {

	public static void main(String[] args) {
		new Test().testMain1();
	}

	public void testMain1() {
		SqlSession session = openSession();
		UserMapper userMapper = session.getMapper(UserMapper.class);
		testDelete(userMapper);
		testUpdate(userMapper);
		testInsert(userMapper);
		testSelect(userMapper);
		if (null != session) {
			session.commit();
			session.close();
		}
	}

	private void testDelete(UserMapper userMapper) {
		System.out.println(userMapper.deleteUser(10000));
	}

	private void testUpdate(UserMapper userMapper) {
		User user = new User();
		user.setId("10000");
		user.setPassword("aaaaa");
		System.out.println(userMapper.updateUser(user));
	}

	private void testInsert(UserMapper userMapper) {
		User user = new User();
		user.setId("10000");
		user.setPassword("aaaaa");
		System.out.println(user.toString());
		System.out.println(userMapper.insertUser2(user));
		// 批量插入
		User u1 = new User();
		u1.setId("10000");
		u1.setPassword("aaaaa");
		User u2 = new User();
		u2.setId("10000");
		u2.setPassword("aaaaa");
		List<User> l = new ArrayList<User>();
		l.add(u1);
		l.add(u2);
		userMapper.insertBatch(l);
	}

	private void testSelect(UserMapper userMapper) {
		User user = userMapper.selectUserById1("10003");
		System.out.println(user.toString());
		user = userMapper.selectUserById2("10000");
		System.out.println(user.toString());
		List<User> list = userMapper.selectUserAll();
		for (User u : list) {
			System.out.println(u.toString());
		}

		List<User> list2 = userMapper.selectUserByNameLike("展");
		for (User u : list2) {
			System.out.println(u.toString());
		}
	}

	private SqlSession openSession() {
		InputStream is;
		try {
			is = Resources.getResourceAsStream("mybatis-config.xml");
			SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
			return sqlSessionFactory.openSession();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
