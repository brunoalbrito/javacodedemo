package cn.java.test.bean.beans;

public class Bean3 {
	private int userid;
	private String username;
	private Bean2 bean2;
	
	public int getUserid() {
		return userid;
	}
	
	public String getUsername() {
		return username;
	}
	
	public Bean2 getBean2() {
		return bean2;
	}
	
	public Bean3() {
		super();
	}
	
	public Bean3(int userid, String username) {
		super();
		this.userid = userid;
		this.username = username;
	}
	
	public Bean3(int userid, String username, Bean2 bean2) {
		super();
		this.userid = userid;
		this.username = username;
		this.bean2 = bean2;
	}
	
	public void testMethod() {
		System.out.println("Bean3:testMethod()");
	}
	
}
