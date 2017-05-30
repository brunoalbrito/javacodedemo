package cn.java.demo.aoptag.aspect.configbyxml;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;

public class Aspect4AopConfigAllTag {
	private String name;
	public Aspect4AopConfigAllTag(String name){
		this.name = name;
	}
	
	/**
	 * ProceedingJoinPoint参数只支持Around类型
	 * @param proceedingJoinPoint
	 * @return
	 * @throws Throwable
	 */
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
	
	public void aspectMethodBefore0() {
		System.out.println("[有被适配] - "+this.getClass().getSimpleName()+":aspectMethodBefore0 = " + System.currentTimeMillis());
	}
	
	/**
	 * 获取调用参数
	 * @param joinPoint
	 */
	public void aspectMethodBefore1(JoinPoint joinPoint) {
		if(joinPoint instanceof MethodInvocationProceedingJoinPoint){
			joinPoint.getArgs(); // 传递的参数
			joinPoint.getThis(); // 目标对象
			joinPoint.getTarget(); // 目标对象
			
			// 获取调用参数
			{
				// signature === org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint.MethodSignatureImpl
				Signature signature = joinPoint.getSignature();
				StringBuilder stringBuilder = new StringBuilder();
				stringBuilder.append("[调用前 - 获取调用信息... bof] - ");
				stringBuilder.append("joinPoint.getArgs() = ");
				stringBuilder.append(joinPoint.getArgs());
				stringBuilder.append(" , joinPoint.getThis() = ");
				stringBuilder.append(joinPoint.getThis());
				stringBuilder.append(" , joinPoint.getTarget() = ");
				stringBuilder.append(joinPoint.getTarget());
				{
					stringBuilder.append(" , joinPoint.getSignature().getName() = 调用的方法名 = ");
					stringBuilder.append(joinPoint.getSignature().getName());
				}
				if(signature instanceof MethodSignature){
					stringBuilder.append("\n\t--- signature instanceof MethodSignature ---\n");
					MethodSignature methodSignature = (MethodSignature)signature;
					stringBuilder.append("\t\t methodSignature.getMethod() = ");
					stringBuilder.append(methodSignature.getMethod());
					stringBuilder.append(" , methodSignature.getReturnType() = ");
					stringBuilder.append(methodSignature.getReturnType());
				}
				if(signature instanceof CodeSignature){
					stringBuilder.append("\n\t--- signature instanceof CodeSignature ---\n");
					CodeSignature codeSignature = (CodeSignature)signature;
					stringBuilder.append("\t\t codeSignature.getParameterTypes() = ");
					stringBuilder.append(codeSignature.getParameterTypes());
					stringBuilder.append(" , codeSignature.getParameterNames() = ");
					stringBuilder.append(codeSignature.getParameterNames());
					stringBuilder.append(" , codeSignature.getExceptionTypes() = ");
					stringBuilder.append(codeSignature.getExceptionTypes());
				}
				System.out.println(stringBuilder);
			}
		}
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
