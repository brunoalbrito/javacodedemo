package cn.java.demo.hbm.test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import cn.java.demo.hbm.entity.many2many.Student;
import cn.java.demo.hbm.entity.many2many.Teacher;
import cn.java.demo.hbm.entity.one2many.Department;
import cn.java.demo.hbm.util.SessionFactoryUtil;

public class Many2ManyTest {

	public static void main(String[] args) {
		Set<Teacher> teacherSet =  insert0();
		Teacher teacher = teacherSet.iterator().next();
		selectOne(teacher);
		update(teacher);
		selectOne(teacher);
		delete(teacher);
		selectOne(teacher);
		insert0();
		insert0();
		selectList(teacher);
	}

	private static Set<Teacher> insert0() {
		System.out.println("---------insert0---------");
		Session session = null;
		Transaction transaction = null;
		try {
			Set<Teacher> teacherSet = new HashSet<Teacher>();
			Teacher teacher1 = new Teacher();
			teacher1.setName("t1 name");
			teacherSet.add(teacher1);
			Teacher teacher2 = new Teacher();
			teacher2.setName("t1 name");
			teacherSet.add(teacher2);

			Set<Student> studentSet = new HashSet<Student>();
			Student student1 = new Student();
			student1.setName("s1");
			studentSet.add(student1);
			Student student2 = new Student();
			student2.setName("s2");
			studentSet.add(student2);

			teacher1.setStudents(studentSet);
			teacher2.setStudents(studentSet);

			session = SessionFactoryUtil.getSession();
			transaction = session.beginTransaction();
			session.save(teacher1);
			session.save(teacher2);
			session.save(student1);
			session.save(student2);
			transaction.commit();
			return teacherSet;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	private static void delete(Teacher teacher) {
		System.out.println("---------delete---------");
		Session session = null;
		Transaction transaction = null;

		try {
			session = SessionFactoryUtil.getSession();
			transaction = session.beginTransaction();
			session.delete(teacher); // 删除
			transaction.commit();
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	private static void update(Teacher teacher) {
		System.out.println("---------update---------");
		Session session = null;
		Transaction transaction = null;

		try {
			teacher.setName("teacher_new");

			session = SessionFactoryUtil.getSession();
			transaction = session.beginTransaction();
			session.update(teacher); // 修改
			transaction.commit();
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	private static void selectOne(Teacher teacher) {
		System.out.println("------selectOne(Teacher teacher)--------");
		Session session = null;
		Transaction transaction = null;
		try {

			session = SessionFactoryUtil.getSession();
			transaction = session.beginTransaction();
			Teacher teacherEntity = (Teacher) session.get(Teacher.class, teacher.getId());
			System.out.println(teacherEntity);
			if (teacherEntity != null) {
				System.out.println(teacherEntity.getStudents()); // 会触发查询
			}
			transaction.commit();
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	private static void selectList(Teacher department) {
		System.out.println("---------selectList---------");
		Session session = null;
		try {
			session = SessionFactoryUtil.getSession();
			Query query = session.createQuery("from Teacher where id <> :id ");
			query.setParameter("id", 0);
			List<Teacher> teacherList = (List<Teacher>) query.list();
			for (int i = 0; i < teacherList.size(); i++) {
				Teacher teacher = teacherList.get(i);
				System.out.println(teacher);
				if (teacher != null) {
					System.out.println(teacher.getStudents());
				}
			}
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

}
