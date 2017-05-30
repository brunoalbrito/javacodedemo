package cn.java.demo.beantag.bean.initmehtod;

public class DemoInitMethodBean {
	
	public void testMethod() {
		System.out.println(this.getClass().getName()+":testMethod()");
	}
	
	/**
	 * 实例化后调用初始化
	 */
	public void initMethod() {
		System.out.println("---------init-method=\"xxx\"配置的应用--------------");
		System.out.println(this.getClass().getName()+":initMethod()");
	}
}
