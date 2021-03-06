package cn.java.demo.jpa.entity.test.inherit;

import java.util.List;
import java.util.Random;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import cn.java.demo.jpa.entity.inherit.unionsubclass.Animal;
import cn.java.demo.jpa.entity.inherit.unionsubclass.Bird;
import cn.java.demo.jpa.entity.inherit.unionsubclass.Pig;
import cn.java.demo.jpa.util.SessionFactoryUtil;


public class UnionSubclassTest {

	public static void main(String[] args) {
		SessionFactoryUtil.openSessionFactory();
		Pig pig = insert0();
		selectOne(pig);
		update(pig);
		selectOne(pig);
		delete(pig);
		selectOne(pig);
		insert0();
		insert1();
		selectListFromPig(pig);
		selectListFromAnimal(pig);
		SessionFactoryUtil.closeSessionFactory();
	}
	
	private static Pig insert0() {
		System.out.println("------insert0--------");
		Session session = null;
		Transaction transaction = null;
		try {
			Pig pig = new Pig();
			pig.setAnimalName("animalName_"+System.nanoTime());
			pig.setWeigth(new Random().nextInt(5));
			// 添加数据
			session = SessionFactoryUtil.openSession();
			transaction = session.beginTransaction();
			session.save(pig);
			transaction.commit();
			return pig;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	private static Bird insert1() {
		System.out.println("------insert1--------");
		Session session = null;
		Transaction transaction = null;
		try {
			Bird bird = new Bird();
			bird.setAnimalName("animalName_"+System.nanoTime());
			bird.setHeight(new Random().nextInt(10));
			// 添加数据
			session = SessionFactoryUtil.openSession();
			transaction = session.beginTransaction();
			session.save(bird);
			transaction.commit();
			return bird;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	private static void selectOne(Pig pig) {
		System.out.println("------selectOne--------");
		Session session = null;
		Transaction transaction = null;
		try {

			session = SessionFactoryUtil.openSession();
			transaction = session.beginTransaction();
			Pig pigEntity = (Pig) session.get(Pig.class, pig.getAnimalId());
			System.out.println(pigEntity);
			transaction.commit();
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	private static void delete(Pig pig) {
		System.out.println("---------delete---------");
		Session session = null;
		Transaction transaction = null;
		try {
			session = SessionFactoryUtil.openSession();
			transaction = session.beginTransaction();
			session.delete(pig); // 删除
			transaction.commit();
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	private static void update(Pig pig) {

		System.out.println("---------update---------");
		Session session = null;
		Transaction transaction = null;

		try {
			pig.setAnimalName(pig.getAnimalName()+"_new");

			session = SessionFactoryUtil.openSession();
			transaction = session.beginTransaction();
			session.update(pig); // 修改
			transaction.commit();
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	private static void selectListFromPig(Pig pig) {
		System.out.println("---------selectListFromPig---------");
		Session session = null;
		try {
			session = SessionFactoryUtil.openSession();
			Query query = session.createQuery("from "+Pig.class.getName()+" where id <> :id ");
			query.setParameter("id", "0");
			List<Pig> pigList = (List<Pig>) query.list();
			for (int i = 0; i < pigList.size(); i++) {
				Pig pigEntity = pigList.get(i);
				System.out.println(pigEntity);
			}
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	/*
	 * 使用的是联合查询
	 * select * from ( select * from hbm_inherit_unionsubclass_pig 
	 * 					union select * from hbm_inherit_unionsubclass_bird ) animal0_
	 *		where animal0_.animal_id<>?
	 */
	private static void selectListFromAnimal(Pig pig) {
		System.out.println("---------selectListFromAnimal---------");
		Session session = null;
		try {
			session = SessionFactoryUtil.openSession();
			Query query = session.createQuery("from "+Animal.class.getName()+" where id <> :id ");
			query.setParameter("id", "0");
			List<Animal> animalList = (List<Animal>) query.list();
			for (int i = 0; i < animalList.size(); i++) {
				Animal animalEntity = animalList.get(i);
				System.out.println(animalEntity);
			}
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
}
