package cn.java.aopnote;

import java.io.Serializable;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.Advisor;
import org.springframework.aop.AfterAdvice;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.ProxyMethodInvocation;
import org.springframework.aop.ThrowsAdvice;
import org.springframework.aop.aspectj.AspectJAfterAdvice;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.aspectj.AspectJMethodBeforeAdvice;
import org.springframework.aop.aspectj.AspectJPointcutAdvisor;
import org.springframework.aop.config.MethodLocatingFactoryBean;
import org.springframework.aop.config.SimpleBeanFactoryAwareAspectInstanceFactory;
import org.springframework.aop.framework.adapter.AdvisorAdapter;
import org.springframework.aop.framework.adapter.AdvisorAdapterRegistry;
import org.springframework.aop.framework.adapter.UnknownAdviceTypeException;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.util.Assert;

public class 关于最后执行目标的对_拦截器链AdviceInterceptorChainTest {

	/**
	 * 这是一个递归调用
	 * 		在调用目标方法前，执行的是拦截器链条
	 * @param args
	 * @throws Throwable 
	 */
	public static void main(String[] args) throws Throwable {
		/*
		 	在里面开始执行链条
		 	org.springframework.aop.framework.JdkDynamicAopProxy.invoke(...) 
		 	org.springframework.aop.framework.ObjenesisCglibAopProxy.invoke(...) 
		 	
		 	org.springframework.aop.framework.JdkDynamicAopProxy.invoke(Object proxy, Method method, Object[] args)
		 		org.springframework.aop.framework.AdvisedSupport.getInterceptorsAndDynamicInterceptionAdvice(Method method, Class<?> targetClass)
		 			org.springframework.aop.framework.DefaultAdvisorChainFactory.getInterceptorsAndDynamicInterceptionAdvice(Advised config, Method method, Class<?> targetClass)
		 			{
		 				//!! config === org.springframework.aop.framework.ProxyFactory

		 				List<Object> interceptorList = new ArrayList<Object>(config.getAdvisors().length); // “通知接受者”列表

		 				// registry == org.springframework.aop.framework.adapter.DefaultAdvisorAdapterRegistry
						AdvisorAdapterRegistry registry = GlobalAdvisorAdapterRegistry.getInstance(); 
		 				for (Advisor advisor : config.getAdvisors()) 
		 				{ 
		 					MethodInterceptor[] interceptors = registry.getInterceptors(advisor);
							// org.springframework.aop.support.ComposablePointcut.getMethodMatcher();
							// mm === org.springframework.aop.support.MethodMatchers.IntersectionMethodMatcher 
							MethodMatcher mm = pointcutAdvisor.getPointcut().getMethodMatcher(); 
							if (MethodMatchers.matches(mm, method, actualClass, hasIntroductions)) 
							{ // 方法匹配成功
								if (mm.isRuntime()) 
								{
									// Creating a new object instance in the getInterceptors() method
									// isn't a problem as we normally cache created chains.
									for (MethodInterceptor interceptor : interceptors) 
									{
										interceptorList.add(new InterceptorAndDynamicMethodMatcher(interceptor, mm));
									}
								}
								else {
									interceptorList.addAll(Arrays.asList(interceptors));
								}
							}
		 				}
		 				return interceptorList;
		 			}
		 */

	}

	public static void test1() throws Throwable {
		// 拦截器列表
		List interceptors = new ArrayList<MethodInterceptor>(3);
		interceptors.add(new MethodBeforeAdviceInterceptor(null));
		interceptors.add(new AfterReturningAdviceInterceptor(null));
		interceptors.add(new ThrowsAdviceInterceptor(null));

		Object proxy = null; // 代理对象
		Object target = null; // 目标对象
		Method method = null; // 被调用的方法
		Object[] arguments= null; // 被调用的方法的参数值
		Class<?> targetClass = null; // 实际对象的类
		MethodInvocation invocation = new ReflectiveMethodInvocation(proxy,target,method, arguments,targetClass, interceptors);
		Object retVal = invocation.proceed(); // 执行结果
	}
	
	public static void test2() throws Throwable {
		// 拦截器列表
		List<Advisor> advisorList  = new ArrayList();
		{
			// 模拟1
			{
				MethodLocatingFactoryBean methodLocatingFactoryBean = new MethodLocatingFactoryBean();
				methodLocatingFactoryBean.setTargetBeanName("aspectBeanName0"); // 要“接受报告的bean对象”
				methodLocatingFactoryBean.setMethodName("aspectMethodBefore");
				
				SimpleBeanFactoryAwareAspectInstanceFactory aspectFactory = new SimpleBeanFactoryAwareAspectInstanceFactory();
				aspectFactory.setAspectBeanName("aspectBeanName0");
				
				AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
				pointcut.setExpression("execution(* cn.java.demo.aoptag.bean.HelloServiceImpl4MockAopInJava.method2(..))");
				
				AspectJMethodBeforeAdvice aspectJMethodBeforeAdvice = new AspectJMethodBeforeAdvice(methodLocatingFactoryBean.getObject(),pointcut,aspectFactory);
				aspectJMethodBeforeAdvice.setAspectName("aspectBeanName0");
				
				AspectJPointcutAdvisor aspectJPointcutAdvisor = new AspectJPointcutAdvisor(aspectJMethodBeforeAdvice);
				advisorList.add(aspectJPointcutAdvisor);
			}
			
			// 模拟2
			{
				MethodLocatingFactoryBean methodLocatingFactoryBean = new MethodLocatingFactoryBean();
				methodLocatingFactoryBean.setTargetBeanName("aspectBeanName0"); // 要“接受报告的bean对象”
				methodLocatingFactoryBean.setMethodName("aspectMethodAfter");
				
				SimpleBeanFactoryAwareAspectInstanceFactory aspectFactory = new SimpleBeanFactoryAwareAspectInstanceFactory();
				aspectFactory.setAspectBeanName("aspectBeanName0");
				
				AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
				pointcut.setExpression("execution(* cn.java.demo.aoptag.bean.HelloServiceImpl4MockAopInJava.method2(..))");
				
				AspectJAfterAdvice aspectJAfterAdvice = new AspectJAfterAdvice(methodLocatingFactoryBean.getObject(),pointcut,aspectFactory);
				aspectJAfterAdvice.setAspectName("aspectBeanName0");
				
				AspectJPointcutAdvisor aspectJPointcutAdvisor = new AspectJPointcutAdvisor(aspectJAfterAdvice);
				advisorList.add(aspectJPointcutAdvisor);
			}
		}
		DefaultAdvisorAdapterRegistry registry = new DefaultAdvisorAdapterRegistry();
		List<Object> interceptorList = new ArrayList<Object>(advisorList.size());
		for (Advisor advisor : advisorList) {
			MethodInterceptor[] interceptors = registry.getInterceptors(advisor);
			interceptorList.addAll(Arrays.asList(interceptors));
		}
		List<Object> chain = interceptorList;
		Object proxy = null; // 代理对象
		Object target = null; // 目标对象
		Method method = null; // 被调用的方法
		Object[] arguments= null; // 被调用的方法的参数值
		Class<?> targetClass = null; // 实际对象的类
		MethodInvocation invocation = new ReflectiveMethodInvocation(proxy,target,method, arguments,targetClass, chain);
		Object retVal = invocation.proceed(); // 执行结果
	}


	private static class DefaultAdvisorAdapterRegistry {
		private final List<AdvisorAdapter> adapters = new ArrayList<AdvisorAdapter>(3);
		/**
		 * Create a new DefaultAdvisorAdapterRegistry, registering well-known adapters.
		 */
		public DefaultAdvisorAdapterRegistry() {
			registerAdvisorAdapter(new MethodBeforeAdviceAdapter());
			registerAdvisorAdapter(new AfterReturningAdviceAdapter());
			registerAdvisorAdapter(new ThrowsAdviceAdapter());
		}

		public void registerAdvisorAdapter(AdvisorAdapter adapter) {
			this.adapters.add(adapter);
		}

		public MethodInterceptor[] getInterceptors(Advisor advisor) {
			List<MethodInterceptor> interceptors = new ArrayList<MethodInterceptor>(3);
			// advisor === org.springframework.aop.aspectj.AspectJPointcutAdvisor 
			Advice advice = advisor.getAdvice();//!!!
			if (advice instanceof MethodInterceptor) {
				// advice === org.springframework.aop.aspectj.AspectJAfterAdvice   <aop:after>
				interceptors.add((MethodInterceptor) advice);
			}
			/*
			 	
			 	如下三种适配器：
			 	adapters = {
			 		org.springframework.aop.framework.adapter.MethodBeforeAdviceAdapter,
			 		org.springframework.aop.framework.adapter.AfterReturningAdviceAdapter,
			 		org.springframework.aop.framework.adapter.ThrowsAdviceAdapter,
			 	}
			 */
			for (AdvisorAdapter adapter : this.adapters) {
				if (adapter.supportsAdvice(advice)) { // !!!
					// advisor === org.springframework.aop.aspectj.AspectJMethodBeforeAdvice   <aop:before>
					interceptors.add(adapter.getInterceptor(advisor));
					// interceptors.add(new org.springframework.aop.framework.adapter.MethodBeforeAdviceInterceptor(advisor.getAdvice()));
				}
			}
			if (interceptors.isEmpty()) {
				throw new UnknownAdviceTypeException(advisor.getAdvice());
			}
			return interceptors.toArray(new MethodInterceptor[interceptors.size()]);
		}

		class MethodBeforeAdviceAdapter implements AdvisorAdapter, Serializable {

			@Override
			public boolean supportsAdvice(Advice advice) {
				return (advice instanceof MethodBeforeAdvice);
			}

			@Override
			public MethodInterceptor getInterceptor(Advisor advisor) {
				MethodBeforeAdvice advice = (MethodBeforeAdvice) advisor.getAdvice();
				return new MethodBeforeAdviceInterceptor(advice);
			}

		}

		class AfterReturningAdviceAdapter implements AdvisorAdapter, Serializable {

			@Override
			public boolean supportsAdvice(Advice advice) {
				return (advice instanceof AfterReturningAdvice);
			}

			@Override
			public MethodInterceptor getInterceptor(Advisor advisor) {
				AfterReturningAdvice advice = (AfterReturningAdvice) advisor.getAdvice();
				return new AfterReturningAdviceInterceptor(advice);
			}

		}

		class ThrowsAdviceAdapter implements AdvisorAdapter, Serializable {

			@Override
			public boolean supportsAdvice(Advice advice) {
				return (advice instanceof ThrowsAdvice);
			}

			@Override
			public MethodInterceptor getInterceptor(Advisor advisor) {
				return new ThrowsAdviceInterceptor(advisor.getAdvice());
			}

		}

	}



	public static class ReflectiveMethodInvocation implements ProxyMethodInvocation, Cloneable {

		protected final Object proxy;
		protected final Object target;
		protected final Method method;
		protected Object[] arguments;
		private final Class<?> targetClass;

		/**
		 * List of MethodInterceptor and InterceptorAndDynamicMethodMatcher
		 * that need dynamic checks.
		 */
		protected final List<?> interceptorsAndDynamicMethodMatchers;
		/**
		 * Index from 0 of the current interceptor we're invoking.
		 * -1 until we invoke: then the current interceptor.
		 */
		private int currentInterceptorIndex = -1;

		protected ReflectiveMethodInvocation(
				Object proxy, Object target, Method method, Object[] arguments,
				Class<?> targetClass, List<Object> interceptorsAndDynamicMethodMatchers) {

			this.proxy = proxy;
			this.target = target;
			this.targetClass = targetClass;
			this.method = BridgeMethodResolver.findBridgedMethod(method);
			//			this.arguments = AopProxyUtils.adaptArgumentsIfNecessary(method, arguments);
			this.interceptorsAndDynamicMethodMatchers = interceptorsAndDynamicMethodMatchers;
		}

		/**
		 * Invoke the joinpoint using reflection.
		 * Subclasses can override this to use custom invocation.
		 * @return the return value of the joinpoint
		 * @throws Throwable if invoking the joinpoint resulted in an exception
		 */
		protected Object invokeJoinpoint() throws Throwable {
			return AopUtils.invokeJoinpointUsingReflection(this.target, this.method, this.arguments);
		}

		/**
		 * 这个方法会被递归执行
		 */
		public Object proceed() throws Throwable {
			//	We start with an index of -1 and increment early.
			if (this.currentInterceptorIndex == this.interceptorsAndDynamicMethodMatchers.size() - 1) { // 执行到链条的末尾
				return invokeJoinpoint(); // 调用目标方法
			}

			Object interceptorOrInterceptionAdvice =
					this.interceptorsAndDynamicMethodMatchers.get(++this.currentInterceptorIndex); // 获取某个通知者
			if (interceptorOrInterceptionAdvice instanceof InterceptorAndDynamicMethodMatcher) {
				// Evaluate dynamic method matcher here: static part will already have
				// been evaluated and found to match.
				InterceptorAndDynamicMethodMatcher dm =
						(InterceptorAndDynamicMethodMatcher) interceptorOrInterceptionAdvice;
				if (dm.methodMatcher.matches(this.method, this.targetClass, this.arguments)) {
					return dm.interceptor.invoke(this); // 执行拦截器链条
				}
				else {
					// Dynamic matching failed.
					// Skip this interceptor and invoke the next in the chain.
					return proceed();
				}
			}
			else {
				// It's an interceptor, so we just invoke it: The pointcut will have
				// been evaluated statically before this object was constructed.
				return ((MethodInterceptor) interceptorOrInterceptionAdvice).invoke(this);
			}
		}

		//------------------------以下为ProxyMethodInvocation的实现------------------------------------
		@Override
		public Method getMethod() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object[] getArguments() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object getThis() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public AccessibleObject getStaticPart() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object getProxy() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public MethodInvocation invocableClone() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public MethodInvocation invocableClone(Object... arguments) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setArguments(Object... arguments) {
			// TODO Auto-generated method stub

		}

		@Override
		public void setUserAttribute(String key, Object value) {
			// TODO Auto-generated method stub

		}

		@Override
		public Object getUserAttribute(String key) {
			// TODO Auto-generated method stub
			return null;
		}
	}

	static class InterceptorAndDynamicMethodMatcher {

		final MethodInterceptor interceptor;

		final MethodMatcher methodMatcher;

		public InterceptorAndDynamicMethodMatcher(MethodInterceptor interceptor, MethodMatcher methodMatcher) {
			this.interceptor = interceptor;
			this.methodMatcher = methodMatcher;
		}

	}

	/**
	 * 目标方法执行前 - 执行
	 * @author zhouzhian
	 */
	public static class MethodBeforeAdviceInterceptor implements MethodInterceptor, Serializable {
		private MethodBeforeAdvice advice;

		/**
		 * Create a new MethodBeforeAdviceInterceptor for the given advice.
		 * @param advice the MethodBeforeAdvice to wrap
		 */
		public MethodBeforeAdviceInterceptor(MethodBeforeAdvice advice) {
			Assert.notNull(advice, "Advice must not be null");
			this.advice = advice;
		}


		@Override
		public Object invoke(MethodInvocation mi) throws Throwable {
			this.advice.before(mi.getMethod(), mi.getArguments(), mi.getThis() );
			return mi.proceed();
		}
	}

	public static class AfterReturningAdviceInterceptor implements MethodInterceptor, AfterAdvice, Serializable {

		private final AfterReturningAdvice advice;


		/**
		 * Create a new AfterReturningAdviceInterceptor for the given advice.
		 * @param advice the AfterReturningAdvice to wrap
		 */
		public AfterReturningAdviceInterceptor(AfterReturningAdvice advice) {
			Assert.notNull(advice, "Advice must not be null");
			this.advice = advice;
		}

		@Override
		public Object invoke(MethodInvocation mi) throws Throwable {
			Object retVal = mi.proceed();
			this.advice.afterReturning(retVal, mi.getMethod(), mi.getArguments(), mi.getThis());
			return retVal;
		}
	}

	public static class ThrowsAdviceInterceptor implements MethodInterceptor, AfterAdvice {
		private static final String AFTER_THROWING = "afterThrowing";

		private static final Log logger = LogFactory.getLog(ThrowsAdviceInterceptor.class);


		private final Object throwsAdvice;

		/** Methods on throws advice, keyed by exception class */
		private final Map<Class<?>, Method> exceptionHandlerMap = new HashMap<Class<?>, Method>();

		public ThrowsAdviceInterceptor(Object throwsAdvice) {
			Assert.notNull(throwsAdvice, "Advice must not be null");
			this.throwsAdvice = throwsAdvice;

			Method[] methods = throwsAdvice.getClass().getMethods();
			for (Method method : methods) {
				if (method.getName().equals(AFTER_THROWING) &&
						(method.getParameterTypes().length == 1 || method.getParameterTypes().length == 4) &&
						Throwable.class.isAssignableFrom(method.getParameterTypes()[method.getParameterTypes().length - 1])
						) {
					// Have an exception handler
					this.exceptionHandlerMap.put(method.getParameterTypes()[method.getParameterTypes().length - 1], method);
					if (logger.isDebugEnabled()) {
						logger.debug("Found exception handler method: " + method);
					}
				}
			}

			if (this.exceptionHandlerMap.isEmpty()) {
				throw new IllegalArgumentException(
						"At least one handler method must be found in class [" + throwsAdvice.getClass() + "]");
			}
		}

		@Override
		public Object invoke(MethodInvocation mi) throws Throwable {
			try {
				return mi.proceed();
			}
			catch (Throwable ex) {
				Method handlerMethod = getExceptionHandler(ex);
				if (handlerMethod != null) {
					invokeHandlerMethod(mi, ex, handlerMethod);
				}
				throw ex;
			}
		}

		private void invokeHandlerMethod(MethodInvocation mi, Throwable ex, Method method) throws Throwable {
			Object[] handlerArgs;
			if (method.getParameterTypes().length == 1) {
				handlerArgs = new Object[] { ex };
			}
			else {
				handlerArgs = new Object[] {mi.getMethod(), mi.getArguments(), mi.getThis(), ex};
			}
			try {
				method.invoke(this.throwsAdvice, handlerArgs);
			}
			catch (InvocationTargetException targetEx) {
				throw targetEx.getTargetException();
			}
		}

		public int getHandlerMethodCount() {
			return this.exceptionHandlerMap.size();
		}

		/**
		 * Determine the exception handle method. Can return null if not found.
		 * @param exception the exception thrown
		 * @return a handler for the given exception type
		 */
		private Method getExceptionHandler(Throwable exception) {
			Class<?> exceptionClass = exception.getClass();
			if (logger.isTraceEnabled()) {
				logger.trace("Trying to find handler for exception of type [" + exceptionClass.getName() + "]");
			}
			Method handler = this.exceptionHandlerMap.get(exceptionClass);
			while (handler == null && exceptionClass != Throwable.class) {
				exceptionClass = exceptionClass.getSuperclass();
				handler = this.exceptionHandlerMap.get(exceptionClass);
			}
			if (handler != null && logger.isDebugEnabled()) {
				logger.debug("Found handler for exception of type [" + exceptionClass.getName() + "]: " + handler);
			}
			return handler;
		}
	}

}
