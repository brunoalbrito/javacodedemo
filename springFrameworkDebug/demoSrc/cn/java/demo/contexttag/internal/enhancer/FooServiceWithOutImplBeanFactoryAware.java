package cn.java.demo.contexttag.internal.enhancer;

public class FooServiceWithOutImplBeanFactoryAware {
	public String getBean0() {
		return "getBean0 in "+this.getClass().getSimpleName();
	}
	
	public String getBean1() {
		return "getBean0 in "+this.getClass().getSimpleName();
	}
	
}
