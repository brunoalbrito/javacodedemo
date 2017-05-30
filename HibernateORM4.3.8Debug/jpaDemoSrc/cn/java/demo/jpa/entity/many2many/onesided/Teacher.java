package cn.java.demo.jpa.entity.many2many.onesided;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;


@Entity(name="cn.java.demo.jpa.entity.many2many.onesided.Teacher")
@Table(name="jpa_many2many_onesided_teacher")
public class Teacher {
	
	@Id
	@GeneratedValue
	@Column(name = "teacher_id")
	private int teacherId;
	
	@Column(name = "teacher_name")
	private String teacherName;
	
	// mappedBy="teachers"表示由Student维护中间表，后期的CURD，要通过Student对象进行操作
	@ManyToMany(targetEntity=Student.class,mappedBy="teachers",cascade={CascadeType.ALL})
	private Set<Student> students;

	public int getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(int teacherId) {
		this.teacherId = teacherId;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public Set<Student> getStudents() {
		return students;
	}

	public void setStudents(Set<Student> students) {
		this.students = students;
	}

	@Override
	public String toString() {
		return "Teacher [teacherId=" + teacherId + ", teacherName=" + teacherName + "]";
	}

	
}
