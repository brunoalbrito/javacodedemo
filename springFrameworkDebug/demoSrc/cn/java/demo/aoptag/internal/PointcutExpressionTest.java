package cn.java.demo.aoptag.internal;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.aspectj.weaver.patterns.NamePattern;
import org.aspectj.weaver.reflect.ShadowMatchImpl;
import org.aspectj.weaver.tools.ContextBasedMatcher;
import org.aspectj.weaver.tools.FuzzyBoolean;
import org.aspectj.weaver.tools.MatchingContext;
import org.aspectj.weaver.tools.PointcutDesignatorHandler;
import org.aspectj.weaver.tools.PointcutExpression;
import org.aspectj.weaver.tools.PointcutParameter;
import org.aspectj.weaver.tools.PointcutParser;
import org.aspectj.weaver.tools.PointcutPrimitive;
import org.aspectj.weaver.tools.ShadowMatch;
import org.springframework.aop.framework.autoproxy.ProxyCreationContext;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;
import org.springframework.util.StringUtils;

import cn.java.demo.util.ApplicationContextUtil;

public class PointcutExpressionTest {

	public void test(AbstractRefreshableConfigApplicationContext context) {
		System.out.println("-----"+this.getClass().getSimpleName()+"------");
		
		ConfigurableListableBeanFactory beanFactory = ApplicationContextUtil.tryCastTypeToConfigurableListableBeanFactory(context);
		if(beanFactory==null){
			return;
		}
		this.beanFactory = beanFactory;
		
		// ---------创建表达式匹配器-----------
		PointcutExpression pointcutExpression = null;
		{ // org.springframework.aop.aspectj.AspectJExpressionPointcut.checkReadyToMatch()
			// ---定义表达式解析器---
			ClassLoader cl = PointcutExpressionTest.class.getClassLoader();
			PointcutParser parser = PointcutParser
					.getPointcutParserSupportingSpecifiedPrimitivesAndUsingSpecifiedClassLoaderForResolution(
							SUPPORTED_PRIMITIVES, cl);
			parser.registerPointcutDesignatorHandler(new BeanNamePointcutDesignatorHandler());
			
			// ---解析表达式，并创建表达式匹配器---
			PointcutParameter[] pointcutParameters = new PointcutParameter[this.pointcutParameterNames.length]; // 参数名
			for (int i = 0; i < pointcutParameters.length; i++) {
				pointcutParameters[i] = parser.createPointcutParameter(
						this.pointcutParameterNames[i], this.pointcutParameterTypes[i]);
			}
			pointcutExpression = parser.parsePointcutExpression(replaceBooleanOperators(getExpression()),
					this.pointcutDeclarationScope, pointcutParameters); // org.aspectj.weaver.internal.tools.PointcutExpressionImpl
		}
		
		// ---------进行匹配-----------
		// 被拦截的对象信息
		FooService service = new FooService();
		Class targetClass = service.getClass();
		Method method;
		try {
			method = targetClass.getMethod("method1");
		} catch (NoSuchMethodException | SecurityException e) {
			throw new RuntimeException(e);
		}
		
		// 匹配指定类
		{ // org.springframework.aop.aspectj.AspectJExpressionPointcut.matches(Class<?> targetClass)
			boolean result = pointcutExpression.couldMatchJoinPointsInType(targetClass);
			if(result){
				System.out.println("class is match.");
			}
		}
		
		// 匹配方法
		{ // org.springframework.aop.aspectj.AspectJExpressionPointcut.matches(Method method, Class<?> targetClass)
			Method targetMethod = AopUtils.getMostSpecificMethod(method, targetClass);
			Method originalMethod = method;
			Method methodToMatch = targetMethod;
			ShadowMatch shadowMatch = pointcutExpression.matchesMethodExecution(methodToMatch);
			if (shadowMatch == null && targetMethod != originalMethod) {
				methodToMatch = originalMethod;
				shadowMatch = pointcutExpression.matchesMethodExecution(methodToMatch);
			}
			if (shadowMatch == null) {
				shadowMatch = new ShadowMatchImpl(org.aspectj.util.FuzzyBoolean.NO, null, null, null);
			}
			if (shadowMatch.alwaysMatches()) {
				System.out.println("return true.");
			}
			else if (shadowMatch.neverMatches()) {
				System.out.println("return false.");
			}
			else {
				/*
				RuntimeTestWalker walker = null;
				if (shadowMatch instanceof DefensiveShadowMatch) {
					walker = new RuntimeTestWalker(((DefensiveShadowMatch) shadowMatch).primary);
				}
				else{
					walker = new RuntimeTestWalker(shadowMatch);
				}
				return (!walker.testsSubtypeSensitiveVars() || walker.testTargetInstanceOfResidue(targetClass));
				*/
			}
		}
	}
	
	
	private static final Set<PointcutPrimitive> SUPPORTED_PRIMITIVES = new HashSet<PointcutPrimitive>();
	private String[] pointcutParameterNames = new String[0];
	private Class<?>[] pointcutParameterTypes = new Class<?>[0];
	private Class<?> pointcutDeclarationScope;
	private BeanFactory beanFactory;
	static {
		SUPPORTED_PRIMITIVES.add(PointcutPrimitive.EXECUTION);
		SUPPORTED_PRIMITIVES.add(PointcutPrimitive.ARGS);
		SUPPORTED_PRIMITIVES.add(PointcutPrimitive.REFERENCE);
		SUPPORTED_PRIMITIVES.add(PointcutPrimitive.THIS);
		SUPPORTED_PRIMITIVES.add(PointcutPrimitive.TARGET);
		SUPPORTED_PRIMITIVES.add(PointcutPrimitive.WITHIN);
		SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_ANNOTATION);
		SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_WITHIN);
		SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_ARGS);
		SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_TARGET);
	}
	
	private static class FooService {

		public void method1() {
			System.out.println(this.getClass().getName() + ":method1()");
		}
		
	}
	
	private String replaceBooleanOperators(String pcExpr) {
		String result = StringUtils.replace(pcExpr, " and ", " && ");
		result = StringUtils.replace(result, " or ", " || ");
		result = StringUtils.replace(result, " not ", " ! ");
		return result;
	}
	
	protected String getCurrentProxiedBeanName() {
		return ProxyCreationContext.getCurrentProxiedBeanName();
	}

	public String getExpression() {
		return "execution(public void cn.java.demo.aoptag.internal.PointcutExpressionTest.FooService.method1(..)) || execution(public void cn.java.demo.aoptag.bean.HelloServiceImpl2.method1(..))";
	}

	private class BeanNamePointcutDesignatorHandler implements PointcutDesignatorHandler {

		private static final String BEAN_DESIGNATOR_NAME = "bean";

		@Override
		public String getDesignatorName() {
			return BEAN_DESIGNATOR_NAME;
		}

		@Override
		public ContextBasedMatcher parse(String expression) {
			return new BeanNameContextMatcher(expression);
		}
	}
	
	private class BeanNameContextMatcher implements ContextBasedMatcher {

		private final NamePattern expressionPattern;

		public BeanNameContextMatcher(String expression) {
			this.expressionPattern = new NamePattern(expression);
		}

		@Override
		@SuppressWarnings("rawtypes")
		@Deprecated
		public boolean couldMatchJoinPointsInType(Class someClass) {
			return (contextMatch(someClass) == FuzzyBoolean.YES);
		}

		@Override
		@SuppressWarnings("rawtypes")
		@Deprecated
		public boolean couldMatchJoinPointsInType(Class someClass, MatchingContext context) {
			return (contextMatch(someClass) == FuzzyBoolean.YES);
		}

		@Override
		public boolean matchesDynamically(MatchingContext context) {
			return true;
		}

		@Override
		public FuzzyBoolean matchesStatically(MatchingContext context) {
			return contextMatch(null);
		}

		@Override
		public boolean mayNeedDynamicTest() {
			return false;
		}

		private FuzzyBoolean contextMatch(Class<?> targetType) {
			String advisedBeanName = getCurrentProxiedBeanName();
			if (advisedBeanName == null) {  // no proxy creation in progress
				// abstain; can't return YES, since that will make pointcut with negation fail
				return FuzzyBoolean.MAYBE;
			}
			if (BeanFactoryUtils.isGeneratedBeanName(advisedBeanName)) {
				return FuzzyBoolean.NO;
			}
			if (targetType != null) {
				boolean isFactory = FactoryBean.class.isAssignableFrom(targetType);
				return FuzzyBoolean.fromBoolean(
						matchesBeanName(isFactory ? BeanFactory.FACTORY_BEAN_PREFIX + advisedBeanName : advisedBeanName));
			}
			else {
				return FuzzyBoolean.fromBoolean(matchesBeanName(advisedBeanName) ||
						matchesBeanName(BeanFactory.FACTORY_BEAN_PREFIX + advisedBeanName));
			}
		}

		private boolean matchesBeanName(String advisedBeanName) {
			if (this.expressionPattern.matches(advisedBeanName)) {
				return true;
			}
			if (beanFactory != null) {
				String[] aliases = beanFactory.getAliases(advisedBeanName);
				for (String alias : aliases) {
					if (this.expressionPattern.matches(alias)) {
						return true;
					}
				}
			}
			return false;
		}
	}
}
