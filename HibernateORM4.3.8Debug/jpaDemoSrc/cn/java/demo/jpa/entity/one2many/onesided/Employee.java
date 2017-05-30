package cn.java.demo.jpa.entity.one2many.onesided;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 学生
 */
@Entity(name="cn.java.demo.jpa.entity.one2many.onesided.Employee")
@Table(name="jpa_one2many_onesided_employee")
public class Employee {
	@Id
	@Column(name="employee_id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int employeeId;
	
	@Column(name="employee_name")
	private String employeeName;
	
	// 删除Employee不一定要删除Department，所以不要配置成cascade={CascadeType.ALL}
	@ManyToOne(targetEntity=Department.class,cascade={},optional=true)
	@JoinColumn(name="department_id")
	private Department department;

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	@Override
	public String toString() {
		return "Employee [employeeId=" + employeeId + ", employeeName=" + employeeName + "]";
	}
	
	
}
