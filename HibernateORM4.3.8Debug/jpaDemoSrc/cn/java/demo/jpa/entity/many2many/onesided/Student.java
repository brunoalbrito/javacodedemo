package cn.java.demo.jpa.entity.many2many.onesided;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity(name="cn.java.demo.jpa.entity.many2many.onesided.Student")
@Table(name="jpa_many2many_onesided_student")
public class Student {
	@Id
	@GeneratedValue
	@Column(name = "student_id")
	private int studentId;
	
	@Column(name = "student_name")
	private String studentName;

	// 删除Student不一定要删除Teacher，所以不要配置成cascade={CascadeType.ALL}
	@ManyToMany(targetEntity=Teacher.class,cascade={})
	@JoinTable(
		name="jpa_many2many_onesided_teacher_student",
		joinColumns={@JoinColumn(name="student_id")},//本端
		inverseJoinColumns={@JoinColumn(name="teacher_id")}//对端
	)
	private Set<Teacher> teachers;

	public int getStudentId() {
		return studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public Set<Teacher> getTeachers() {
		return teachers;
	}

	public void setTeachers(Set<Teacher> teachers) {
		this.teachers = teachers;
	}

	@Override
	public String toString() {
		return "Student [studentId=" + studentId + ", studentName=" + studentName + "]";
	}

	

}












