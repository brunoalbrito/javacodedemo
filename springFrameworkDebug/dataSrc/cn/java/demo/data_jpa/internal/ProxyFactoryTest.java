package cn.java.demo.data_jpa.internal;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.core.NamedThreadLocal;
import org.springframework.data.repository.util.ClassUtils;

public class ProxyFactoryTest {

	/*
	 * 参考自 org.springframework.data.repository.core.support.RepositoryFactorySupport.getRepository(Class<T> repositoryInterface, Object customImplementation)
	 */
	public static void main(String[] args) {
		Object target = new ThreeInterface();
		ProxyFactory result = new ProxyFactory();
		result.setTarget(target);
		result.setInterfaces(new Class[] { OneInterface.class, TwoInterface.class });
		result.addAdvisor(ExposeInvocationInterceptor.ADVISOR);
		result.addAdvice(new QueryExecutorMethodInterceptor(target));
		OneInterface oneInterface = (OneInterface) result.getProxy(ProxyFactoryTest.class.getClassLoader()); // 创建代理对象
		oneInterface.testMethod();
	}
	
	
	public static interface OneInterface {
		public void testMethod();
	}
	
	public static interface TwoInterface {
		
	}
	
	public static class ThreeInterface implements OneInterface, TwoInterface {
		public void testMethod(){
			System.out.println("hello, i am ThreeInterface.");
		}
	}

	/**
	 * 拦截器
	 */
	public static class ExposeInvocationInterceptor implements MethodInterceptor{
		private static final ThreadLocal<MethodInvocation> invocation =
				new NamedThreadLocal<MethodInvocation>("Current AOP method invocation");
		public static final ExposeInvocationInterceptor INSTANCE = new ExposeInvocationInterceptor();
		@SuppressWarnings("serial")
		public static final Advisor ADVISOR = new DefaultPointcutAdvisor(INSTANCE) {
			@Override
			public String toString() {
				return ExposeInvocationInterceptor.class.getName() +".ADVISOR";
			}
		};
		private ExposeInvocationInterceptor() {
		}
		
		@Override
		public Object invoke(MethodInvocation methodInvocation) throws Throwable {
			MethodInvocation oldInvocation = invocation.get();
			invocation.set(methodInvocation);
			try {
				return methodInvocation.proceed(); // 执行链条上的下一环节
			}
			finally {
				invocation.set(oldInvocation);
			}
		}
		
	}
	
	/**
	 * 拦截器
	 */
	public static class QueryExecutorMethodInterceptor implements MethodInterceptor {
		private final Object target;

		public QueryExecutorMethodInterceptor(Object target) {
			this.target = target;
		}

		@Override
		public Object invoke(MethodInvocation invocation) throws Throwable {
			Method method = invocation.getMethod();
			Object[] arguments = invocation.getArguments();
			// MethodParameter parameter = new MethodParameter(method, -1);
			try {
				return method.invoke(target, arguments);
			} catch (Exception e) {
				ClassUtils.unwrapReflectionException(e);
			}

			throw new IllegalStateException("Should not occur!");

		}

	}

}
