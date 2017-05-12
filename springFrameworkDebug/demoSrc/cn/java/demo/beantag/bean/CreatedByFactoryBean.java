package cn.java.demo.beantag.bean;

public class CreatedByFactoryBean {
	private int userid;
	private String username;
	private FooBean fooBean;
	
	private String property1;
	
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
	

	public void setProperty1(String property1) {
		this.property1 = property1;
	}

	public void testMethod() {
		System.out.println(this.getClass().getName()+":testMethod()");
	}

	@Override
	public String toString() {
		return "CreatedByFactoryBean [userid=" + userid + ", username=" + username + ", fooBean=" + fooBean
				+ ", property1=" + property1 + "]";
	}
	
	
	
}
