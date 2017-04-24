package cn.java.demo.beantag.bean;

public class CreatedByFactoryBean {
	private int userid;
	private String username;
	private FooBean fooBean;
	
	public int getUserid() {
		return userid;
	}
	
	public String getUsername() {
		return username;
	}
	
	public FooBean getFooBean() {
		return fooBean;
	}
	
	public CreatedByFactoryBean() {
		super();
	}
	
	public CreatedByFactoryBean(int userid, String username) {
		super();
		this.userid = userid;
		this.username = username;
	}
	
	public CreatedByFactoryBean(int userid, String username, FooBean fooBean) {
		super();
		this.userid = userid;
		this.username = username;
		this.fooBean = fooBean;
	}
	
	public void testMethod() {
		System.out.println(this.getClass().getName()+":testMethod()");
	}
	
}
