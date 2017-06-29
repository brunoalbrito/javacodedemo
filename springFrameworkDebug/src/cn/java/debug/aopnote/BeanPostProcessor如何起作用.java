package cn.java.debug.aopnote;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.weaver.tools.JoinPointMatch;
import org.springframework.aop.Advisor;
import org.springframework.aop.ProxyMethodInvocation;
import org.springframework.aop.aspectj.AspectJProxyUtils;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.framework.InterceptorAndDynamicMethodMatcher;
import org.springframework.aop.framework.adapter.AdvisorAdapterRegistry;
import org.springframework.aop.framework.adapter.GlobalAdvisorAdapterRegistry;
import org.springframework.aop.interceptor.ExposeInvocationInterceptor;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.core.BridgeMethodResolver;

public class BeanPostProcessor如何起作用 {
	/*
	 	-------------------------------------
	 	在获取bean的时候起作用（返回代理对象）
	 	-------------------------------------
	 		org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator.postProcessAfterInitialization(Object bean, String beanName)
	 		{
	 			org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator.wrapIfNecessary(Object bean, String beanName, Object cacheKey)
	 			{
	 				// 扩展内建的类：isInfrastructureClass
					// 要创建的是“接受通知的对象”，那么就要跳过了，不然陷入死循环
					if (isInfrastructureClass(bean.getClass()) || shouldSkip(bean.getClass(), beanName)) {
						this.advisedBeans.put(cacheKey, Boolean.FALSE);
						return bean;
					}
					Object[] specificInterceptors = AbstractAdvisorAutoProxyCreator.getAdvicesAndAdvisorsForBean(bean.getClass(), beanName, null); // 获取指定类的“接受通知者”
					{
						org.springframework.aop.framework.autoproxy.AbstractAdvisorAutoProxyCreator.getAdvicesAndAdvisorsForBean(Class<?> beanClass, String beanName, TargetSource targetSource)
						{
							List<Advisor> advisors = AbstractAdvisorAutoProxyCreator.findEligibleAdvisors(beanClass, beanName); // 获取符合条件的“通知接受者”列表
							{
								List<Advisor> candidateAdvisors = AbstractAdvisorAutoProxyCreator.findCandidateAdvisors(); // 扫描所有“通知接受者”列表
								{
									return this.advisorRetrievalHelper.findAdvisorBeans(); // 扫描bean容器，找出实现Advisor接口的bean列表，如果没实例化，就进行实例化
									{
										advisorNames = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(this.beanFactory, Advisor.class, true, false); // 扫描bean容器，找出实现Advisor接口的bean列表，如果没实例化，就进行实例化
										List<Advisor> advisors = new LinkedList<Advisor>();
										for (String name : advisorNames) {
											advisors.add(this.beanFactory.getBean(name, Advisor.class)); // 进行实例化
										}
										return advisors;
									}
								}
								List<Advisor> eligibleAdvisors = findAdvisorsThatCanApply(candidateAdvisors, beanClass, beanName); // 匹配“通知接受者”列表
								{
									ProxyCreationContext.setCurrentProxiedBeanName(beanName);
									try {
										return AopUtils.findAdvisorsThatCanApply(candidateAdvisors, beanClass);
										{
											for (Advisor candidate : candidateAdvisors) { // candidate === org.springframework.aop.aspectj.AspectJPointcutAdvisor
												if (candidate instanceof IntroductionAdvisor) {
													// already processed
													continue;
												}
												if (canApply(candidate, clazz, hasIntroductions){
													PointcutAdvisor pca = (PointcutAdvisor) advisor; 
													// advisor === org.springframework.aop.aspectj.AspectJPointcutAdvisor 使用xml配置
													// advisor === org.springframework.aop.aspectj.annotation.InstantiationModelAwarePointcutAdvisorImpl 使用注解配置
													return canApply(pca.getPointcut(), targetClass, hasIntroductions);
													{
														// pc === org.springframework.aop.support.ComposablePointcut 使用xml配置
														// pc === org.springframework.aop.aspectj.annotation.InstantiationModelAwarePointcutAdvisorImpl.PerTargetInstantiationModelPointcut 使用注解配置
														// pc.getClassFilter() === org.springframework.aop.aspectj.AspectJExpressionPointcut 《匹配类》
														if (!pc.getClassFilter().matches(targetClass)) {
															return false;
														}
												
														// methodMatcher === org.springframework.aop.support.MethodMatchers.IntersectionMethodMatcher 使用xml配置
														// methodMatcher === org.springframework.aop.aspectj.annotation.InstantiationModelAwarePointcutAdvisorImpl.PerTargetInstantiationModelPointcut 使用注解配置
														MethodMatcher methodMatcher = pc.getMethodMatcher(); 
														if (methodMatcher == MethodMatcher.TRUE) {
															// No need to iterate the methods if we're matching any method anyway...
															return true;
														}
												
														IntroductionAwareMethodMatcher introductionAwareMethodMatcher = null;
														if (methodMatcher instanceof IntroductionAwareMethodMatcher) {
															introductionAwareMethodMatcher = (IntroductionAwareMethodMatcher) methodMatcher;
														}
												
														Set<Class<?>> classes = new LinkedHashSet<Class<?>>(ClassUtils.getAllInterfacesForClassAsSet(targetClass)); // 取得目标类的所有接口
														classes.add(targetClass);
														for (Class<?> clazz : classes) {
															Method[] methods = ReflectionUtils.getAllDeclaredMethods(clazz); // 获取所有类的成员方法
															for (Method method : methods) {
																if ((introductionAwareMethodMatcher != null &&
																		introductionAwareMethodMatcher.matches(method, targetClass, hasIntroductions)) ||
																		methodMatcher.matches(method, targetClass)) { // 《匹配方法》 org.springframework.aop.support.MethodMatchers.IntersectionMethodMatcher
																	return true;
																}
															}
														}
												
														return false;
													}
												
												}) {//!!!
													eligibleAdvisors.add(candidate);
												}
											}
											return eligibleAdvisors;
										}
									}
									finally {
										ProxyCreationContext.setCurrentProxiedBeanName(null);
									}
								}
								extendAdvisors(eligibleAdvisors); // 方法
								{
									org.springframework.aop.aspectj.autoproxy.AspectJAwareAdvisorAutoProxyCreator.extendAdvisors(List<Advisor> candidateAdvisors)
									{
										AspectJProxyUtils.makeAdvisorChainAspectJCapableIfNecessary(candidateAdvisors);
										{
											// Don't add advisors to an empty list; may indicate that proxying is just not required
											if (!advisors.isEmpty()) {
												boolean foundAspectJAdvice = false;
												for (Advisor advisor : advisors) {
													// Be careful not to get the Advice without a guard, as
													// this might eagerly instantiate a non-singleton AspectJ aspect
													if (isAspectJAdvice(advisor)) {
														foundAspectJAdvice = true;
													}
												}
												if (foundAspectJAdvice && !advisors.contains(ExposeInvocationInterceptor.ADVISOR)) { // 自动添加一个advisor，且添加在首位
													advisors.add(0, ExposeInvocationInterceptor.ADVISOR);
													return true;
												}
											}
											return false;
										}
									}
								}
								if (!eligibleAdvisors.isEmpty()) {
									eligibleAdvisors = sortAdvisors(eligibleAdvisors); // 对结果进行排序
								}
								return eligibleAdvisors;
							}
							return advisors.toArray();
						}
					}
					if (specificInterceptors != DO_NOT_PROXY) {
						this.advisedBeans.put(cacheKey, Boolean.TRUE);
						Object proxy = AbstractAutoProxyCreator.createProxy(bean.getClass(), beanName, specificInterceptors, new SingletonTargetSource(bean));// 创建代理
						{
							Advisor[] advisors = buildAdvisors(beanName, specificInterceptors); // 通用“通知接收器”列表 + 特殊“通知接收器”列表
							{
								Advisor[] commonInterceptors = resolveInterceptorNames(); // 实例化通用拦截器列表
								List<Object> allInterceptors = new ArrayList<Object>();
								if (specificInterceptors != null) {
									allInterceptors.addAll(Arrays.asList(specificInterceptors)); // 合并“拦截器列表”
									if (commonInterceptors.length > 0) {
										if (this.applyCommonInterceptorsFirst) {
											allInterceptors.addAll(0, Arrays.asList(commonInterceptors));
										}
										else {
											allInterceptors.addAll(Arrays.asList(commonInterceptors));
										}
									}
								}
								Advisor[] advisors = new Advisor[allInterceptors.size()];
								for (int i = 0; i < allInterceptors.size(); i++) {
									advisors[i] = this.advisorAdapterRegistry.wrap(allInterceptors.get(i)); // 包装
								}
								return advisors;
							}
							for (Advisor advisor : advisors) {
								proxyFactory.addAdvisor(advisor);
							}
							return proxyFactory.getProxy(getProxyClassLoader()); // !!!
							{
							 	// org.springframework.aop.framework.JdkDynamicAopProxy.getProxy(classLoader);
							 	// org.springframework.aop.framework.ObjenesisCglibAopProxy.getProxy(classLoader);
								return createAopProxy().getProxy(classLoader);
								{
									JdkDynamicAopProxy.getProxy(ClassUtils.getDefaultClassLoader());
									{
										Class<?>[] proxiedInterfaces = AopProxyUtils.completeProxiedInterfaces(this.advised, true); // 被代理的接口列表
										findDefinedEqualsAndHashCodeMethods(proxiedInterfaces); //  // 有定义equals方法、有定义hashCode方法
										return Proxy.newProxyInstance(classLoader, proxiedInterfaces, this);
									}
								}
							}
						}
						this.proxyTypes.put(cacheKey, proxy.getClass());
						return proxy;
					}
	 			}
	 		}
	 	-------------------------------------
	 	在调用bean的时候起作用（代理调用）：
	 	-------------------------------------
	 		org.springframework.aop.framework.JdkDynamicAopProxy.invoke(Object proxy, Method method, Object[] args)
	 		{
	 			target = targetSource.getTarget();  // cn.java.FooService对象
				if (target != null) {
					targetClass = target.getClass();
				}
				// advised === org.springframework.aop.framework.ProxyFactory
				// Get the interception chain for this method.
				List<Object> chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, targetClass); // 符合条件的“拦截器链条”
				{
					org.springframework.aop.framework.AdvisedSupport.getInterceptorsAndDynamicInterceptionAdvice(Method method, Class<?> targetClass)
					{
						MethodCacheKey cacheKey = new MethodCacheKey(method);
						List<Object> cached = this.methodCache.get(cacheKey);
						if (cached == null) {
							// org.springframework.aop.framework.DefaultAdvisorChainFactory
							cached = this.advisorChainFactory.getInterceptorsAndDynamicInterceptionAdvice(this, method, targetClass);
							{
								//!! config === org.springframework.aop.framework.ProxyFactory
								List<Object> interceptorList = new ArrayList<Object>(config.getAdvisors(){
									return ProxyFactory.advisorArray;
								}.length); // “通知接受者”列表
								
								// registry == org.springframework.aop.framework.adapter.DefaultAdvisorAdapterRegistry
								AdvisorAdapterRegistry registry = GlobalAdvisorAdapterRegistry.getInstance(); 
								
								for (Advisor advisor : config.getAdvisors()) { 
									if (advisor instanceof PointcutAdvisor) {
										// Add it conditionally.
										PointcutAdvisor pointcutAdvisor = (PointcutAdvisor) advisor;
										if (config.isPreFiltered() || pointcutAdvisor.getPointcut().getClassFilter().matches(actualClass)) { // 《类匹配》成功
											MethodInterceptor[] interceptors = registry.getInterceptors(advisor); // !!!! 获取 Advisor的拦截器列表，里面给advice会做适配
											{
												List<MethodInterceptor> interceptors = new ArrayList<MethodInterceptor>(3);
												// advisor === org.springframework.aop.aspectj.AspectJPointcutAdvisor 
												Advice advice = advisor.getAdvice();//!!!
												
												// 可能是自定义的 advice === cn.java.demo.aoptag.advice.AspectJMethodBeforeAdviceMock
												// <before ...> advice == org.springframework.aop.aspectj.AspectJMethodBeforeAdvice
												// <after ...> advice === org.springframework.aop.aspectj.AspectJAfterAdvice
												// <after-returning ...> advice === org.springframework.aop.aspectj.AspectJAfterReturningAdvice
												// <after-throwing ...> advice=== org.springframework.aop.aspectj.AspectJAfterThrowingAdvice
												// <around ...> advice === org.springframework.aop.aspectj.AspectJAroundAdvice
												 
												if (advice instanceof MethodInterceptor) {
													// advice === org.springframework.aop.aspectj.AspectJAfterAdvice   <aop:after>
													interceptors.add((MethodInterceptor) advice);
												}
											 	// adapters = {
											 	//	 org.springframework.aop.framework.adapter.MethodBeforeAdviceAdapter,
											 	//	 org.springframework.aop.framework.adapter.AfterReturningAdviceAdapter,
											 	//	 org.springframework.aop.framework.adapter.ThrowsAdviceAdapter,
											 	// }
												 	
												for (AdvisorAdapter adapter : this.adapters) {
													if (adapter.supportsAdvice(advice)) { // !!! 给advice会做适配
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
											// org.springframework.aop.support.ComposablePointcut.getMethodMatcher();
											// mm === org.springframework.aop.support.MethodMatchers.IntersectionMethodMatcher 
											MethodMatcher mm = pointcutAdvisor.getPointcut().getMethodMatcher(); 
											if (MethodMatchers.matches(mm, method, actualClass, hasIntroductions)) { // 《方法匹配》成功
												if (mm.isRuntime()) {
													// Creating a new object instance in the getInterceptors() method
													// isn't a problem as we normally cache created chains.
													for (MethodInterceptor interceptor : interceptors) {
														interceptorList.add(new InterceptorAndDynamicMethodMatcher(interceptor, mm));
													}
												}
												else {
													interceptorList.addAll(Arrays.asList(interceptors));
												}
											}
										}
									}
								}
								return interceptorList;
							}
							this.methodCache.put(cacheKey, cached);
						}
						return cached;
					}
				}
				
				invocation = new ReflectiveMethodInvocation(proxy, target, method, args, targetClass, chain); // 创建调用器
				{
					this.proxy = proxy;
					this.target = target;
					this.targetClass = targetClass;
					this.method = BridgeMethodResolver.findBridgedMethod(method);
					this.arguments = AopProxyUtils.adaptArgumentsIfNecessary(method, args);
					this.interceptorsAndDynamicMethodMatchers = chain; // 拦截器列表
				}
				
				// Proceed to the joinpoint through the interceptor chain.
				retVal = invocation.proceed(); // 执行结果，这个方法会被递归执行
				{
					//	We start with an index of -1 and increment early.
					if (this.currentInterceptorIndex == this.interceptorsAndDynamicMethodMatchers.size() - 1) { // 执行到链条的末尾
						return invokeJoinpoint(); // 调用目标方法
					}
			
					Object interceptorOrInterceptionAdvice =
							this.interceptorsAndDynamicMethodMatchers.get(++this.currentInterceptorIndex); // 获取下一个通知者
					if (interceptorOrInterceptionAdvice instanceof InterceptorAndDynamicMethodMatcher) { // 拦截器实现了InterceptorAndDynamicMethodMatcher接口
						// Evaluate dynamic method matcher here: static part will already have
						// been evaluated and found to match.
						InterceptorAndDynamicMethodMatcher dm =
								(InterceptorAndDynamicMethodMatcher) interceptorOrInterceptionAdvice;
						if (dm.methodMatcher.matches(this.method, this.targetClass, this.arguments)) {
							return dm.interceptor.invoke(this);
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
						return ((MethodInterceptor) interceptorOrInterceptionAdvice).invoke(this); // !!! 执行拦截器链条，递归调用
						{
							// !!! mi === org.springframework.aop.framework.ReflectiveMethodInvocation
							org.springframework.aop.framework.adapter.MethodBeforeAdviceInterceptor.invoke(MethodInvocation mi) // <before ...> 有被适配 1
							{
								this.advice.before(mi.getMethod(), mi.getArguments(), mi.getThis() );
								{
									org.springframework.aop.aspectj.AspectJMethodBeforeAdvice.before(mi.getMethod(), mi.getArguments(), mi.getThis() );
									{
										AbstractAspectJAdvice.invokeAdviceMethod(getJoinPointMatch(), null, null);
										{
											return invokeAdviceMethodWithGivenArgs(argBinding(getJoinPoint(), jpMatch, returnValue, ex));
										}
									}
								}
								
								return mi.proceed();
								{
									org.springframework.aop.aspectj.AspectJAfterAdvice.invoke(MethodInvocation mi) // <after ...> 没被适配 2
									{
										try {
											return mi.proceed(); // 让拦截器链往下走
											{
												org.springframework.aop.framework.adapter.AfterReturningAdviceInterceptor.invoke(MethodInvocation mi) // <after-returning ...> 有被适配 3
												{
													Object retVal = mi.proceed();
													{
														org.springframework.aop.aspectj.AspectJAfterThrowingAdvice.invoke(MethodInvocation mi) // <after-throwing ...>,没被适配 4
														{
															try {
																return mi.proceed();
																{
																	org.springframework.aop.aspectj.AspectJAroundAdvice.invoke(MethodInvocation mi) // <around ...>,没被适配 5
																	{
																		if (!(mi instanceof ProxyMethodInvocation)) {
																			throw new IllegalStateException("MethodInvocation is not a Spring ProxyMethodInvocation: " + mi);
																		}
																		// mi === org.springframework.aop.framework.ReflectiveMethodInvocation
																		// mi === org.springframework.aop.framework.CglibAopProxy.CglibMethodInvocation
																		ProxyMethodInvocation pmi = (ProxyMethodInvocation) mi; 
																		ProceedingJoinPoint pjp = lazyGetProceedingJoinPoint(pmi);
																		JoinPointMatch jpm = getJoinPointMatch(pmi); // 获取表达式的匹配器
																		return invokeAdviceMethod(pjp, jpm, null, null);
																	}
																}
															}
															catch (Throwable ex) {
																if (shouldInvokeOnThrowing(ex)) {
																	invokeAdviceMethod(getJoinPointMatch(), null, ex);
																}
																throw ex;
															}
														}
													}
													this.advice.afterReturning(retVal, mi.getMethod(), mi.getArguments(), mi.getThis());
													return retVal;
												}
											}
										}
										finally {
											invokeAdviceMethod(getJoinPointMatch(), null, null); // 先走再处理
											{
												return invokeAdviceMethodWithGivenArgs(argBinding(getJoinPoint(), jpMatch, returnValue, ex));
												{
													...
												}
											}
										}
									}
								}
							}
						}
					}
				}
				return retVal; // 执行结果
	 		}
	 	
	 */

}
