package cn.java.demo.webmvc.form;

public class CurdHandlerForm {
	private Integer fooId;
	private String fooName;
	public Integer getFooId() {
		return fooId;
	}
	public void setFooId(Integer fooId) {
		this.fooId = fooId;
	}
	public String getFooName() {
		return fooName;
	}
	public void setFooName(String fooName) {
		this.fooName = fooName;
	}
	@Override
	public String toString() {
		return "CurdHandlerForm [fooId=" + fooId + ", fooName=" + fooName + "]";
	}
	
}
