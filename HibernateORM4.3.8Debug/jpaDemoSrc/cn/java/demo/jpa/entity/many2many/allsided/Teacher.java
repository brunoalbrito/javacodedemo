package cn.java.demo.jpa.entity.many2many.allsided;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;


@Entity(name="cn.java.demo.jpa.entity.many2many.allsided.Teacher")
@Table(name="jpa_many2many_allsided_teacher")
public class Teacher {
	
	@Id
	@GeneratedValue
	@Column(name = "teacher_id")
	private int teacherId;
	
	@Column(name = "teacher_name")
	private String teacherName;
	
	// 删除Teacher不一定要删除Student，所以不要配置成cascade={CascadeType.ALL}
	@ManyToMany(targetEntity=Student.class,cascade={})
	@JoinTable(
		name="jpa_many2many_allsided_teacher_student",
		joinColumns={@JoinColumn(name="teacher_id")}, // 本端
		inverseJoinColumns={@JoinColumn(name="student_id")} // 对端
	)
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
