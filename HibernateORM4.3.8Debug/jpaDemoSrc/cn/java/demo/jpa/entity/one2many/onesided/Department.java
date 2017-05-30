package cn.java.demo.jpa.entity.one2many.onesided;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


/**
 * 班级
 */
@Entity(name="cn.java.demo.jpa.entity.one2many.onesided.Department")
@Table(name="jpa_one2many_onesided_department")
public class Department {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="department_id")
	private int departmentId;
	
	@Column(name="department_name")
	private String departmentName;
	
	// 删除Department一定要删除Employee，所以要配置成cascade={CascadeType.ALL}
	// mappedBy在哪一端，在哪一端就不维护关系，它成为了关系的被管理端。至少要一段维护关系
	// 如果采用泛型，可以不采用targetEntity属性
	@OneToMany(targetEntity=Employee.class,mappedBy="department",cascade={CascadeType.ALL})
	private Set<Employee> employees;

	public int getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public Set<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(Set<Employee> employees) {
		this.employees = employees;
	}

	@Override
	public String toString() {
		return "Department [departmentId=" + departmentId + ", departmentName=" + departmentName + "]";
	}

	
}
