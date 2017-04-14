package cn.java.test.aop.aspect;

public class Aspect4HelloService2 {
	public void aspectMethodBefore() {
		System.out.println("Aspect4HelloService2::aspectMethodBefore = " + System.currentTimeMillis());
	}
	
	public void aspectMethodAfter() {
		System.out.println("Aspect4HelloService2::aspectMethodAfter = " + System.currentTimeMillis());
	}
}
