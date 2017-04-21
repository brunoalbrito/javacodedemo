package cn.java.demo.beantag.beandefinition_property;

public class StupidAspect4HelloService1 {
	public void aspectMethodBefore() {
		System.out.println(this.getClass().getSimpleName() + ":aspectMethodBefore = " + System.currentTimeMillis());
	}
	
	public void aspectMethodAfter() {
		System.out.println(this.getClass().getSimpleName() + ":aspectMethodAfter = " + System.currentTimeMillis());
	}
}
