package cn.java.demo.jpa.entity.test.one2many;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import cn.java.demo.jpa.entity.one2many.onesided.Department;
import cn.java.demo.jpa.entity.one2many.onesided.Employee;
import cn.java.demo.jpa.util.SessionFactoryUtil;

public class One2ManyOneSidedTest {

	public static void main(String[] args) {
		SessionFactoryUtil.openSessionFactory();
		Department department = insert0();
		selectOne(department);
		update(department);
		selectOne(department);
		delete(department);
		selectOne(department);
		insert0();
		insert0();
		selectList(department);
		SessionFactoryUtil.closeSessionFactory();
	}

	private static void selectList(Department department) {
		System.out.println("---------selectList---------");
		Session session = null;
		try {
			session = SessionFactoryUtil.openSession();
			Query query = session.createQuery("from " + Department.class.getName() + " where id <> :id ");
			query.setParameter("id", 0);
			List<Department> departmentList = (List<Department>) query.list();
			for (int i = 0; i < departmentList.size(); i++) {
				Department departmentEntity = departmentList.get(i);
				System.out.println(departmentEntity);
				if (departmentEntity != null) {
					System.out.println("\t" + departmentEntity.getEmployees());
				}
			}
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	/**
	 * 多个Employee属于一个Department
	 * 
	 * @return
	 */
	private static Department insert0() {
		System.out.println("------insert0--------");

		Session session = null;
		Transaction transaction = null;
		try {
			Department department = new Department();
			department.setDepartmentName("DepartmentName_" + System.nanoTime());
			Employee employee0 = new Employee();
			employee0.setEmployeeName("EmployeeName_" + System.nanoTime());
			employee0.setDepartment(department);
			Employee employee1 = new Employee();
			employee1.setEmployeeName("EmployeeName_" + System.nanoTime());
			employee1.setDepartment(department); // 多个员工属于一个部分
			
			// 添加数据
			session = SessionFactoryUtil.openSession();
			transaction = session.beginTransaction();
			session.save(department);
			session.save(employee0);
			session.save(employee1);
			transaction.commit();
			return department;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	/**
	 * 如果Classes里面没有employee，那么Employee表中的depart_id会被置空depart_id=null
	 * 
	 * @param Person
	 */
	private static void update(Department department) {

		System.out.println("---------update---------");
		Session session = null;
		Transaction transaction = null;

		try {
			department.setDepartmentName(department.getDepartmentName() + "_new");

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
			Department departmentEntity = (Department) session.get(Department.class, department.getDepartmentId());
			System.out.println(departmentEntity);
			if (departmentEntity != null) {
				System.out.println("\t" + departmentEntity.getEmployees()); // 会触发查询
			}
			transaction.commit();
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

}
