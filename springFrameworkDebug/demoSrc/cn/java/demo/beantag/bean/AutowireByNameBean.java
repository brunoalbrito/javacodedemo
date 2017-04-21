package cn.java.demo.beantag.bean;

public class AutowireByNameBean {
	private String username;
	private FooBean fooBean;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public FooBean getFooBean() {
		return fooBean;
	}

	public void setFooBean(FooBean fooBean) {
		this.fooBean = fooBean;
	}

	@Override
	public String toString() {
		return "AutowireByNameBean [username=" + username + ", fooBean=" + fooBean + "]";
	}


}
