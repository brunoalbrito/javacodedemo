package cn.java.demo.hbm.test;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import cn.java.curd.entity.User;
import cn.java.curd.util.SessionFactoryUtil2;
import cn.java.demo.hbm.entity.one2one.IdCard;
import cn.java.demo.hbm.entity.one2one.Person;
import cn.java.demo.hbm.util.SessionFactoryUtil;

public class One2OneTest {

	public static void main(String[] args) {
		SessionFactoryUtil.openSessionFactory();
		Person person = insert();
		selectOne(person);
		update(person);
		selectOne(person);
		delete(person);
		selectOne(person);
		insert();
		insert();
		selectList(person);
		SessionFactoryUtil.closeSessionFactory();
	}
	
	private static Person insert() {
		System.out.println("---------insert---------");
		Session session = null;
		Transaction transaction = null;

		try {
			session = SessionFactoryUtil.openSession();
			
			// 构造数据
			IdCard idCard = new IdCard();
			idCard.setUsefulLife(new Date());
			Person person = new Person();
			person.setName("person0");
			person.setIdCard(idCard); // 双向关联
			idCard.setPerson(person); // 双向关联

			// 开启事务、保存数据
			transaction = session.beginTransaction();
			
			// 保存
			session.save(person);
			session.save(idCard);
			
			transaction.commit();
			return person;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	private static void delete(Person person) {
		System.out.println("---------delete---------");
		Session session = null;
		Transaction transaction = null;

		try {
			session = SessionFactoryUtil.openSession();
			transaction = session.beginTransaction();
			session.delete(person);  // 删除
			transaction.commit();
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	private static void update(Person person) {
		System.out.println("---------update---------");
		Session session = null;
		Transaction transaction = null;

		try {
			person.setName("person_new");
			
			session = SessionFactoryUtil.openSession();
			transaction = session.beginTransaction();
			session.update(person); // 修改
			transaction.commit();
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	private static void selectOne(Person person) {
		System.out.println("---------selectOne---------");
		Session session = null;
		Transaction transaction = null;

		try {
			session = SessionFactoryUtil.openSession();
			transaction = session.beginTransaction();
			
			// 获取数据
			// 会执行关联查询 ： select ... from Person person0_ left outer join ... where person0_.id=?
			Person personTemp = (Person) session.get(Person.class, person.getId());
			System.out.println(personTemp);
			// 会执行简单查询
			IdCard idCard = (IdCard) session.get(IdCard.class, person.getId());
			System.out.println(idCard);

			transaction.commit();

		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	private static void selectList(Person person) {
		System.out.println("---------selectList---------");
		Session session = null;
		try {
			session = SessionFactoryUtil.openSession();
			Query query = session.createQuery("from Person where id <> :id "); 
			query.setParameter("id", 0);
			List<Person> personList = (List<Person>) query.list();
			// 会触发关联查询 - 对SQL优化来说不利
			for (int i = 0; i < personList.size(); i++) {
				Person personEntity = personList.get(i); 
				System.out.println(personEntity);
			}
			
		} finally {
			// 使资源得到释放
			if (session != null) {
				session.close();
			}
		}
	}

	

}
