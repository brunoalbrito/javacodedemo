package cn.java.test.bean;

import java.io.Serializable;

public class User implements Serializable{
	private int  id;
	private String username;
	public String getUsername() {
		return username;
	}
	public String getUsername(String prefix) {
		return prefix + username;
	}
	public String testMethod0(String parma0) {
		return parma0 ;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
	public User() {
		super();
	}
	public User(int id, String username) {
		super();
		this.id = id;
		this.username = username;
	}
	
}