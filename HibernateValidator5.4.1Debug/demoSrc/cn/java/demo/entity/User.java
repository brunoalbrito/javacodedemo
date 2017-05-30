package cn.java.demo.entity;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class User {

	@NotNull(message="用户名不能为空！")
	private String userName;

	@NotNull
	@Size(min = 2, max = 14)
	private String password;

	@Min(2)
	private int age;

	public User(String userName, String password, int age) {
		this.userName = userName;
		this.password = password;
		this.age = age;
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


}