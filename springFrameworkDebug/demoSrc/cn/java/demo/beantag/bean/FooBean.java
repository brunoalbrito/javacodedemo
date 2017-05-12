package cn.java.demo.beantag.bean;

public class FooBean {

	private String filed1 = "filed1";


	public String getFiled1() {
		return filed1;
	}

	public void setFiled1(String filed1) {
		this.filed1 = filed1;
	}

	@Override
	public String toString() {
		return "FooBean [filed1=" + filed1 + "]";
	}

	
	
}
