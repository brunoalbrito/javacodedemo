package cn.java.demo.aoptag.aspect;

public class Aspect4AopConfigTag {
	private String name;
	public Aspect4AopConfigTag(String name){
		this.name = name;
	}
	public void aspectMethodBefore() {
		System.out.println("方法调用前 - "+this.getClass().getSimpleName()+":aspectMethodBefore = " + System.currentTimeMillis());
	}
	
	public void aspectMethodAfter() {
		System.out.println("方法调用后 - "+this.getClass().getSimpleName()+":aspectMethodAfter = " + System.currentTimeMillis());
	}
}
