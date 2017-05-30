package cn.java.demo.jpa.entity.test.one2many;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import cn.java.demo.jpa.entity.one2many.allsided.Department;
import cn.java.demo.jpa.entity.one2many.allsided.Employee;
import cn.java.demo.jpa.util.SessionFactoryUtil;

public class One2ManyAllSidedTest {
	public static void main(String[] args) {
		EmployeeCtrlTest.main();
		DepartmentCtrlTest.main();
	}

	public static class EmployeeCtrlTest {

		public static void main() {
			System.out.println("-------EmployeeCtrlTest-------");
			List<Employee> employeeList = insert0();
			selectOne(employeeList);
			update(employeeList);
			selectOne(employeeList);
			delete(employeeList);
			selectOne(employeeList);
			insert0();
			insert0();
			selectList(employeeList);
		}

		/**
		 * @return
		 */
		private static List<Employee> insert0() {
			System.out.println("------insert1--------");
			Session session = null;
			Transaction transaction = null;
			try {
				Department department = new Department();
				department.setDepartmentName("DepartmentName_" + System.nanoTime());
				Employee employee0 = new Employee();
				employee0.setEmployeeName("EmployeeName_" + System.nanoTime());
				Employee employee1 = new Employee();
				employee1.setEmployeeName("EmployeeName_" + System.nanoTime());
				// 一个部门有多个员工
				{
					Set<Employee> employees = new HashSet<Employee>();
					employees.add(employee0);
					employees.add(employee0);
					department.setEmployees(employees); 
				}
				// 多个员工属于一个部门
				{
					employee0.setDepartment(department);
					employee0.setDepartment(department);
				}
				
				// 添加数据
				session = SessionFactoryUtil.getSession();
				transaction = session.beginTransaction();
				session.save(department);
				session.save(employee0);
				session.save(employee1);
				transaction.commit();

				List<Employee> employeeList = new ArrayList();
				employeeList.add(employee0);
				employeeList.add(employee1);
				return employeeList;
			} finally {
				if (session != null) {
					session.close();
				}
			}
		}

		/**
		 * @param Person
		 */
		private static void update(List<Employee> employeeList) {

			System.out.println("---------update---------");
			Session session = null;
			Transaction transaction = null;

			try {
				Department department = employeeList.get(0).getDepartment();
				department.setDepartmentName(department.getDepartmentName() + "_new");

				session = SessionFactoryUtil.getSession();
				transaction = session.beginTransaction();
				session.update(department); // 修改
				transaction.commit();

			} finally {
				if (session != null) {
					session.close();
				}
			}
		}

		private static void delete(List<Employee> employeeList) {
			System.out.println("---------delete---------");
			Session session = null;
			Transaction transaction = null;

			try {
				session = SessionFactoryUtil.getSession();
				transaction = session.beginTransaction();

				{
					for (Employee employee : employeeList) {
						session.delete(employee); // 删除
					}
					session.delete(employeeList.get(0).getDepartment());
				}

				transaction.commit();
			} finally {
				if (session != null) {
					session.close();
				}
			}
		}

		private static void selectOne(List<Employee> employeeList) {
			System.out.println("------selectOne(List<Employee> employeeList)--------");
			Session session = null;
			Transaction transaction = null;
			try {
				session = SessionFactoryUtil.getSession();
				transaction = session.beginTransaction();
				for (Employee employee : employeeList) {
					Employee employeeEntity = (Employee) session.get(Employee.class, employee.getEmployeeId());
					System.out.println(employeeEntity);
					if (employeeEntity != null) {
						System.out.println("\t" + employeeEntity.getDepartment());
					}
				}
				transaction.commit();
			} finally {
				if (session != null) {
					session.close();
				}
			}
		}

		private static void selectList(List<Employee> employeeList) {
			System.out.println("---------selectList---------");
			Session session = null;
			try {
				session = SessionFactoryUtil.getSession();
				Query query = session.createQuery("from " + Employee.class.getName() + " where id <> :id ");
				query.setParameter("id", 0);
				List<Employee> employeeListTemp = (List<Employee>) query.list();
				for (int i = 0; i < employeeListTemp.size(); i++) {
					Employee employee = employeeListTemp.get(i);
					System.out.println(employee);
					if (employee != null) {
						System.out.println("\t" + employee.getDepartment());
					}
				}
			} finally {
				if (session != null) {
					session.close();
				}
			}
		}

	}

	public static class DepartmentCtrlTest {

		public static void main() {
			System.out.println("-------DepartmentCtrlTest-------");
			Department department = insert0();
			selectOne(department);
			update(department);
			selectOne(department);
			delete(department);
			selectOne(department);
			insert0();
			insert0();
			selectList(department);
		}

		private static void selectList(Department department) {
			System.out.println("---------selectList---------");
			Session session = null;
			try {
				session = SessionFactoryUtil.getSession();
				Query query = session.createQuery("from " + Department.class.getName() + " where id <> :id ");
				query.setParameter("id", 0);
				List<Department> departmentList = (List<Department>) query.list();
				for (int i = 0; i < departmentList.size(); i++) {
					Department departmentEntity = departmentList.get(i);
					System.out.println(departmentEntity);
					if (departmentEntity != null) {
						System.out.println(departmentEntity.getEmployees());
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
				Employee employee1 = new Employee();
				employee1.setEmployeeName("EmployeeName_" + System.nanoTime());
				
				// 一个部门有多个员工
				{
					Set<Employee> employees = new HashSet<Employee>();
					employees.add(employee0);
					employees.add(employee0);
					department.setEmployees(employees); 
				}
				
				// 多个员工属于一个部门
				{
					employee0.setDepartment(department);
					employee0.setDepartment(department);
				}

				// 添加数据
				session = SessionFactoryUtil.getSession();
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

				session = SessionFactoryUtil.getSession();
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
				session = SessionFactoryUtil.getSession();
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
				session = SessionFactoryUtil.getSession();
				transaction = session.beginTransaction();
				Department departmentEntity = (Department) session.get(Department.class, department.getDepartmentId());
				System.out.println(departmentEntity);
				if (departmentEntity != null) {
					System.out.println(departmentEntity.getEmployees()); // 会触发查询
				}
				transaction.commit();
			} finally {
				if (session != null) {
					session.close();
				}
			}
		}

	}
}
