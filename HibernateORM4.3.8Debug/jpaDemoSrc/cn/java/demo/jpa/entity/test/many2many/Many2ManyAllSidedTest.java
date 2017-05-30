package cn.java.demo.jpa.entity.test.many2many;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import cn.java.demo.jpa.entity.many2many.allsided.Student;
import cn.java.demo.jpa.entity.many2many.allsided.Teacher;
import cn.java.demo.jpa.util.SessionFactoryUtil;

/**
 * 关系由多方面维护
 */
public class Many2ManyAllSidedTest {

	public static void main(String[] args) {
		TeacherCtrlTest.main(args);
		StudentCtrlTest.main(args);
	}

	/**
	 * Teacher维护关系
	 */
	public static class TeacherCtrlTest {
		public static void main(String[] args) {
			System.out.println("------------TeacherCtrlTest------------");
			Set<Teacher> teacherSet = insert0();
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
				teacher1.setTeacherName("teacherName_" + System.nanoTime());
				teacherSet.add(teacher1);
				Teacher teacher2 = new Teacher();
				teacher2.setTeacherName("teacherName_" + System.nanoTime());
				teacherSet.add(teacher2);

				Set<Student> studentSet = new HashSet<Student>();
				Student student1 = new Student();
				student1.setStudentName("studentName_" + System.nanoTime());
				studentSet.add(student1);
				Student student2 = new Student();
				student2.setStudentName("studentName_" + System.nanoTime());
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
				teacher.setTeacherName(teacher.getTeacherName() + "_new");
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
				Teacher teacherEntity = (Teacher) session.get(Teacher.class, teacher.getTeacherId());
				System.out.println(teacherEntity);
				if (teacherEntity != null) {
					System.out.println("\t" + teacherEntity.getStudents()); // 会触发查询
				}
				transaction.commit();
			} finally {
				if (session != null) {
					session.close();
				}
			}
		}

		private static void selectList(Teacher teacher) {
			System.out.println("---------selectList---------");
			Session session = null;
			try {
				session = SessionFactoryUtil.getSession();
				Query query = session.createQuery("from " + Teacher.class.getName() + " where id <> :id ");
				query.setParameter("id", 0);
				List<Teacher> teacherList = (List<Teacher>) query.list();
				for (int i = 0; i < teacherList.size(); i++) {
					Teacher teacherEntity = teacherList.get(i);
					System.out.println();
					System.out.println(teacherEntity);
					if (teacherEntity != null) {
						System.out.println("\t" + teacherEntity.getStudents());
					}
				}
			} finally {
				if (session != null) {
					session.close();
				}
			}
		}
	}

	/**
	 * Student维护关系
	 */
	public static class StudentCtrlTest {
		public static void main(String[] args) {
			System.out.println("------------StudentCtrlTest------------");
			Set<Student> studentSet = insert0();
			Student student = studentSet.iterator().next();
			selectOne(student);
			update(student);
			selectOne(student);
			delete(student);
			selectOne(student);
			insert0();
			insert0();
			selectList(student);
		}

		private static Set<Student> insert0() {
			System.out.println("---------insert0---------");
			Session session = null;
			Transaction transaction = null;
			try {
				Set<Teacher> teacherSet = new HashSet<Teacher>();
				Teacher teacher1 = new Teacher();
				teacher1.setTeacherName("teacherName_" + System.nanoTime());
				teacherSet.add(teacher1);
				Teacher teacher2 = new Teacher();
				teacher2.setTeacherName("teacherName_" + System.nanoTime());
				teacherSet.add(teacher2);

				Set<Student> studentSet = new HashSet<Student>();
				Student student1 = new Student();
				student1.setStudentName("studentName_" + System.nanoTime());
				studentSet.add(student1);
				Student student2 = new Student();
				student2.setStudentName("studentName_" + System.nanoTime());
				studentSet.add(student2);

				student1.setTeachers(teacherSet);
				student2.setTeachers(teacherSet);

				session = SessionFactoryUtil.getSession();
				transaction = session.beginTransaction();
				session.save(teacher1);
				session.save(teacher2);
				session.save(student1);
				session.save(student2);
				transaction.commit();
				return studentSet;
			} finally {
				if (session != null) {
					session.close();
				}
			}
		}

		private static void delete(Student student) {
			System.out.println("---------delete---------");
			Session session = null;
			Transaction transaction = null;
			try {
				session = SessionFactoryUtil.getSession();
				transaction = session.beginTransaction();
				session.delete(student); // 删除
				transaction.commit();
			} finally {
				if (session != null) {
					session.close();
				}
			}
		}

		private static void update(Student student) {
			System.out.println("---------update---------");
			Session session = null;
			Transaction transaction = null;
			try {
				student.setStudentName(student.getStudentName() + "_new");
				session = SessionFactoryUtil.getSession();
				transaction = session.beginTransaction();
				session.update(student); // 修改
				transaction.commit();
			} finally {
				if (session != null) {
					session.close();
				}
			}
		}

		private static void selectOne(Student student) {
			System.out.println("------selectOne(Student student)--------");
			Session session = null;
			Transaction transaction = null;
			try {
				session = SessionFactoryUtil.getSession();
				transaction = session.beginTransaction();
				Student studentEntity = (Student) session.get(Student.class, student.getStudentId());
				System.out.println(studentEntity);
				if (studentEntity != null) {
					System.out.println("\t" + studentEntity.getTeachers()); // 会触发查询
				}
				transaction.commit();
			} finally {
				if (session != null) {
					session.close();
				}
			}
		}

		private static void selectList(Student student) {
			System.out.println("---------selectList---------");
			Session session = null;
			try {
				session = SessionFactoryUtil.getSession();
				Query query = session.createQuery("from " + Student.class.getName() + " where id <> :id ");
				query.setParameter("id", 0);
				List<Student> studentList = (List<Student>) query.list();
				for (int i = 0; i < studentList.size(); i++) {
					Student studentEntity = studentList.get(i);
					System.out.println();
					System.out.println(studentEntity);
					if (studentEntity != null) {
						System.out.println("\t" + studentEntity.getTeachers());
					}
				}
			} finally {
				if (session != null) {
					session.close();
				}
			}
		}
	}

}
