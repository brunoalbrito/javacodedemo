package cn.java.demo.jpa.entity.one;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CollectionId;
import org.hibernate.annotations.GenericGenerator;

@SuppressWarnings("unused")
@Entity
@Table(name="jpa_user")
public class User {
	
	//生成策略：采用数据库自增方式
	// @GeneratedValue(strategy=GenerationType.AUTO)
	//主键的值依赖于另一个表
	// @GeneratedValue(strategy=GenerationType.TABLE)
	// 数据库支持的生成策略
	// @GeneratedValue(strategy=GenerationType.IDENTITY)
	// @GeneratedValue
	@Id
	@GenericGenerator(name="userIdGenerator",strategy="uuid")
	@GeneratedValue(generator="userIdGenerator")
	private String id;
	
	@Column(name="user_name",length=30,nullable=false,unique=true)
	private String userName;
	
	@Column(name="password",length=30,nullable=false)
	private String password;
	
	// 年龄不持久化,即在表中没有该字段
	@Transient
	private int age;
	
	@Column(name="createTime")
	private Date createTime;
	
	@Column(name="expireTime")
	private Date expireTime;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getExpireTime() {
		return expireTime;
	}
	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", userName=" + userName + ", password=" + password + ", age=" + age + ", createTime=" + createTime + ", expireTime=" + expireTime + "]";
	}
	
	
	
	
}
