package cn.java.demo.mybatis.mapper.bean;

public class FooConstructAutoMap {
	
	public FooConstructAutoMap(Integer id, String account) {
		super();
		this.id = id;
		this.account = account;
	}

	private int id;
	private String account;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public FooConstructAutoMap() {
		super();
	}
	
	@Override
	public String toString() {
		return "Foo [id=" + id + ", account=" + account + "]";
	}
	
	
	
}
