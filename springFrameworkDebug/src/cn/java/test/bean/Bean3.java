package cn.java.test.bean;

public class Bean3 {
	private int userid;
	private String username;
	private Bean2 bean2;
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
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
	
	
}
