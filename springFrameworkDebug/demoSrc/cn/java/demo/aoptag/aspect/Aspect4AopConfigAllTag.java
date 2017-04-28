package cn.java.demo.aoptag.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;

public class Aspect4AopConfigAllTag {
	private String name;
	public Aspect4AopConfigAllTag(String name){
		this.name = name;
	}
	
	public Object aspectMethodAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		
		if(proceedingJoinPoint instanceof MethodInvocationProceedingJoinPoint){
			proceedingJoinPoint.getArgs(); // 传递的参数
			proceedingJoinPoint.getThis(); // 目标对象
			proceedingJoinPoint.getTarget(); // 目标对象
		}
		
		System.out.println("[包裹... bof] - "+this.getClass().getSimpleName()+":aspectMethodAround = " + System.currentTimeMillis());
		Object result =  proceedingJoinPoint.proceed(); // 被包裹了 
		System.out.println("[包裹... ]这是返回的结果 - "+this.getClass().getSimpleName()+":aspectMethod4Around = " + result);
		System.out.println("[包裹... eof] - "+this.getClass().getSimpleName()+":aspectMethodAround = " + System.currentTimeMillis());
		return result;
	}
	
	public void aspectMethodBefore() {
		System.out.println("[有被适配] - "+this.getClass().getSimpleName()+":aspectMethodBefore = " + System.currentTimeMillis());
	}
	
	public void aspectMethodAfter() {
		System.out.println("[没被适配] - "+this.getClass().getSimpleName()+":aspectMethodAfter = " + System.currentTimeMillis());
	}
	
	public void aspectMethodAfterReturning() {
		System.out.println("[有被适配] - "+this.getClass().getSimpleName()+":aspectMethodAfterReturning = " + System.currentTimeMillis());
	}
	
	public void aspectMethodAfterThrowing() {
		System.out.println("[没被适配] - "+this.getClass().getSimpleName()+":aspectMethodAfterThrowing = " + System.currentTimeMillis());
	}
}
