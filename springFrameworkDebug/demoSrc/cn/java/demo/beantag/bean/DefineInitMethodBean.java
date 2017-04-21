package cn.java.demo.beantag.bean;

public class DefineInitMethodBean {
	
	public void testMethod() {
		System.out.println(this.getClass().getName()+":testMethod()");
	}
	
	/**
	 * 实例化后调用初始化
	 */
	public void initMethod() {
		System.out.println(this.getClass().getName()+":initMethod()");
	}
}
