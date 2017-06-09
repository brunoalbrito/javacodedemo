package cn.java.demo.webmvc.form;

public class RestForm {
	private Integer restId;
	private String restName;
	public Integer getRestId() {
		return restId;
	}
	public void setRestId(Integer restId) {
		this.restId = restId;
	}
	public String getRestName() {
		return restName;
	}
	public void setRestName(String restName) {
		this.restName = restName;
	}
	@Override
	public String toString() {
		return "RestForm [restId=" + restId + ", restName=" + restName + "]";
	}
	

	
}
