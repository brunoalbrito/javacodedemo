package cn.java.demo.aoptag.aspect;

public class Aspect4HelloService1 {
	public void aspectMethodBefore() {
		System.out.println("Aspect4HelloService1::aspectMethodBefore = " + System.currentTimeMillis());
	}
	
	public void aspectMethodAfter() {
		System.out.println("Aspect4HelloService1::aspectMethodAfter = " + System.currentTimeMillis());
	}
}
