package cn.java.mybatis.mapper.bean;

public class FooConstructAnnotaionMap {
	
	public FooConstructAnnotaionMap(Integer id, String account) {
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
	public FooConstructAnnotaionMap() {
		super();
	}
	
	@Override
	public String toString() {
		return "Foo [id=" + id + ", account=" + account + "]";
	}
	
	
	
}
