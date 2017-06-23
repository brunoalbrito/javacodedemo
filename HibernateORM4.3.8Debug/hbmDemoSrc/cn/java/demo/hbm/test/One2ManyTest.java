package cn.java.demo.hbm.test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import cn.java.demo.hbm.entity.one2many.Department;
import cn.java.demo.hbm.entity.one2many.Employee;
import cn.java.demo.hbm.util.SessionFactoryUtil;

public class One2ManyTest {

	public static void main(String[] args) {
		SessionFactoryUtil.openSessionFactory();
		Department department = insert0();
		selectOne(department);
		update(department);
		selectOne(department);
		delete(department);
		selectOne(department);
		insert0();
		insert1();
		selectList(department);
		SessionFactoryUtil.closeSessionFactory();
	}

	private static void selectList(Department department) {
		System.out.println("---------selectList---------");
		Session session = null;
		try {
			session = SessionFactoryUtil.openSession();
			Query query = session.createQuery("from Department where id <> :id ");
			query.setParameter("id", 0);
			List<Department> departmentList = (List<Department>) query.list();
			for (int i = 0; i < departmentList.size(); i++) {
				Department dept = departmentList.get(i);
				System.out.println(dept);
				if (dept != null) {
					System.out.println(dept.getEmps());
				}
			}
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	/**
	 * Employee关联Department
	 * @return
	 */
	private static Department insert0() {
//		------insert0--------
//		Hibernate: insert into hbm_department (name) values (?)
//		Hibernate: insert into hbm_employee (name, depart_id) values (?, ?)
//		Hibernate: insert into hbm_employee (name, depart_id) values (?, ?)
		System.out.println("------insert0--------");
		Session session = null;
		Transaction transaction = null;
		try {
			Department depart = new Department();
			depart.setName("Department_"+System.nanoTime());
			Employee emp1 = new Employee();
			emp1.setName("Employee_"+System.nanoTime());
			emp1.setDepart(depart); // 一个员工属于一个部门
			Employee emp2 = new Employee();
			emp2.setName("Employee_"+System.nanoTime());
			emp2.setDepart(depart);

			// 添加数据
			session = SessionFactoryUtil.openSession();
			transaction = session.beginTransaction();
			session.save(depart);
			session.save(emp1);
			session.save(emp2);
			transaction.commit();
			return depart;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	/**
	 * Department关联Employee （这种性能不好）
	 * 
	 * @return
	 */
	private static Department insert1() {
//		------insert1--------
//		Hibernate: insert into hbm_department (name) values (?)
//		Hibernate: insert into hbm_employee (name, depart_id) values (?, ?)
//		Hibernate: insert into hbm_employee (name, depart_id) values (?, ?)
//		Hibernate: update hbm_employee set depart_id=? where id=?
//		Hibernate: update hbm_employee set depart_id=? where id=?
		System.out.println("------insert1--------");
		Session session = null;
		Transaction transaction = null;
		try {
			Employee emp1 = new Employee();
			emp1.setName("Employee_"+System.nanoTime());
			Employee emp2 = new Employee();
			emp2.setName("Employee_"+System.nanoTime());
			Set<Employee> emps = new HashSet<Employee>();
			emps.add(emp1);
			emps.add(emp2);
			Department depart = new Department();
			depart.setName("Department_"+System.nanoTime());
			depart.setEmps(emps); // 一个部门有多个员工

			// 添加数据
			session = SessionFactoryUtil.openSession();
			transaction = session.beginTransaction();
			session.save(depart);
			session.save(emp1);
			session.save(emp2);
			transaction.commit();
			return depart;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	/**
	 * 如果department里面没有employee，那么Employee表中的depart_id会被置空depart_id=null
	 * 
	 * @param department
	 */
	private static void update(Department department) {

		System.out.println("---------update---------");
		Session session = null;
		Transaction transaction = null;

		try {
			department.setName(department.getName()+"_new");

			session = SessionFactoryUtil.openSession();
			transaction = session.beginTransaction();
			session.update(department); // 修改
			transaction.commit();
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	private static void delete(Department department) {
		System.out.println("---------delete---------");
		Session session = null;
		Transaction transaction = null;

		try {
			session = SessionFactoryUtil.openSession();
			transaction = session.beginTransaction();
			session.delete(department); // 删除
			transaction.commit();
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	private static void selectOne(Department department) {
		System.out.println("------selectOne(Department department)--------");
		Session session = null;
		Transaction transaction = null;
		try {

			session = SessionFactoryUtil.openSession();
			transaction = session.beginTransaction();
			Department depart = (Department) session.get(Department.class, department.getId());
			System.out.println(depart);
			if (depart != null) {
				System.out.println(depart.getEmps()); // 会触发查询
			}
			transaction.commit();
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	private static void selectOne(Employee employee) {
		System.out.println("------selectOne(Employee employee)--------");
		Session session = null;
		Transaction transaction = null;
		try {
			session = SessionFactoryUtil.openSession();
			transaction = session.beginTransaction();
			Employee emp = (Employee) session.get(Employee.class, employee.getId());
			System.out.println(emp);
			if (emp != null) {
				System.out.println(emp.getDepart());
			}
			transaction.commit();
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

}
