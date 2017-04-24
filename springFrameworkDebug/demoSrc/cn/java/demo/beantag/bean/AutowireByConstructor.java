package cn.java.demo.beantag.bean;

public class AutowireByConstructor {
	
	private String username;
	private Integer id;
	
	public AutowireByConstructor(Integer id,String username) {
		super();
		this.username = username;
		this.id = id;
	}
	
	@Override
	public String toString() {
		return "AutowireByConstructor [username=" + username + ", id=" + id + "]";
	}
	


}
