package cn.java.demo.beantag.bean;

public class AutowireByTypeToBeInjectedBean {


	private Integer id;
	private String username;

	public AutowireByTypeToBeInjectedBean(Integer id, String username) {
		super();
		this.id = id;
		this.username = username;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}


}
