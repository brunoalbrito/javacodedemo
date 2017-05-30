package cn.java.demo.aoptag.aspect.configbyxml;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;

public class Aspect4AroundTag {
	
	public Object aspectMethod4Around(ProceedingJoinPoint joinPoint) throws Throwable{
		Object result = null;
		System.out.println("方法调用前 - "+this.getClass().getSimpleName()+":aspectMethod4Around = " + System.currentTimeMillis());
//		joinPoint === org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint
		if(joinPoint instanceof MethodInvocationProceedingJoinPoint){
			MethodInvocationProceedingJoinPoint methodInvocationProceedingJoinPoint = (MethodInvocationProceedingJoinPoint)joinPoint;
			Object[] args = methodInvocationProceedingJoinPoint.getArgs(); // 传递的参数
			Object object = methodInvocationProceedingJoinPoint.getTarget(); // 目标对象
			result = methodInvocationProceedingJoinPoint.proceed(); // 克隆methodInvocation，从头进行执行
		}
		System.out.println("方法调用后 - "+this.getClass().getSimpleName()+":aspectMethod4Around = " + System.currentTimeMillis());
		return result;
	}
}
