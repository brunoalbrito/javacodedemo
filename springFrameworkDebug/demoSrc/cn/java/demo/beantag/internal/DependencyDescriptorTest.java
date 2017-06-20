package cn.java.demo.beantag.internal;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.AutowireCandidateResolver;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.support.SimpleAutowireCandidateResolver;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.ResolvableType;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

public class DependencyDescriptorTest {

	public static void main(String[] args) {
		testDependencyDescriptor();
	}
	
	public static void testDependencyDescriptor() {
		FooService fooService = new FooService();
		String beanName = "fooService";
		BeanWrapper bw = new BeanWrapperImpl(fooService);
		
		System.out.println("-------resolveDependency(bw,\"fooString\")-------");
		resolveDependency(beanName,bw,"fooString");
		System.out.println("-------resolveDependency(bw,\"fooArray\")-------");
		resolveDependency(beanName,bw,"fooArray");
		System.out.println("-------resolveDependency(bw,\"fooList\")-------");
		resolveDependency(beanName,bw,"fooList");
		System.out.println("-------resolveDependency(bw,\"fooMap\")-------");
		resolveDependency(beanName,bw,"fooMap");
		
	}
	
	private static boolean isAutowireCandidate(String beanName, RootBeanDefinition mbd,
			DependencyDescriptor descriptor, AutowireCandidateResolver resolver) {
		String beanDefinitionName = BeanFactoryUtils.transformedBeanName(beanName);
		return resolver.isAutowireCandidate(
				new BeanDefinitionHolder(mbd, beanName, getAliases(beanDefinitionName)), descriptor);
	}
	
	public static String[] getAliases(String name) {
		List<String> aliases = new ArrayList<String>();
		return StringUtils.toStringArray(aliases);
	}
	
	
	
	private static void resolveDependency(String beanName,BeanWrapper bw,String propertyName) {
		PropertyDescriptor pd = bw.getPropertyDescriptor(propertyName);
		if (Object.class != pd.getPropertyType()) {
			MethodParameter methodParam = BeanUtils.getWriteMethodParameter(pd);
			{
				System.out.println("methodParam.getMethod().getName() = " + methodParam.getMethod().getName());
				System.out.println("methodParam.getParameterName() = " + methodParam.getParameterName());
			}
			
			boolean eager = !PriorityOrdered.class.isAssignableFrom(bw.getWrappedClass()); // 不继承自PriorityOrdered，就是饥渴模式
			DependencyDescriptor descriptor = new AutowireByTypeDependencyDescriptor(methodParam, eager);
			
			{
				ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
				descriptor.initParameterNameDiscovery(parameterNameDiscoverer); // 参数名识别器
			}
			
			// 解析类型
			{
				{
					
					System.out.println("descriptor.getDependencyType() = " + descriptor.getDependencyType());
					System.out.println("descriptor.getField() = " + descriptor.getField());
					System.out.println("descriptor.getDependencyName() = " + descriptor.getDependencyName()); // 没值
					{
						Class<?> type = descriptor.getDependencyType();
						
						// 多bean的类型
						{
							if (type.isArray()) {
								Class<?> componentType = type.getComponentType();
								ResolvableType resolvableType = descriptor.getResolvableType();
								Class<?> resolvedArrayType = resolvableType.resolve();
								System.out.println("componentType = " + componentType+" , resolvedArrayType = " + resolvedArrayType);
							}
							else if (Collection.class.isAssignableFrom(type) && type.isInterface()) {
								Class<?> elementType = descriptor.getResolvableType().asCollection().resolveGeneric();
								System.out.println("elementType = " + elementType);
							}
							else if (Map.class == type) {
								ResolvableType mapType = descriptor.getResolvableType().asMap();
								Class<?> keyType = mapType.resolveGeneric(0);
								if (String.class == keyType) {
								}
								Class<?> valueType = mapType.resolveGeneric(1);
								if (valueType != null) {
								}
								System.out.println("keyType = " + keyType+" , valueType = " + valueType);
							}
						}
						
						// 非多bean的类型
						{
							if(!(type.isArray() || (type.isInterface() && (Collection.class.isAssignableFrom(type) || Map.class.isAssignableFrom(type))))){
								DependencyDescriptor fallbackDescriptor = descriptor.forFallbackMatch();
								System.out.println("fallbackDescriptor.getDependencyType() = " + fallbackDescriptor.getDependencyType());
								System.out.println("fallbackDescriptor.getField() = " + fallbackDescriptor.getField());
								System.out.println("fallbackDescriptor.getDependencyName() = " + fallbackDescriptor.getDependencyName()); // 有值：如fooStringArgName
							}
						}
					}
				}
				
				if (javaUtilOptionalClass == descriptor.getDependencyType()) {// !!! 参数类型是 Optional
				}
				else if (ObjectFactory.class == descriptor.getDependencyType() || // !!! 参数类型是 ObjectFactory
						ObjectProvider.class == descriptor.getDependencyType()) { // !!! 参数类型是 ObjectProvider
				}
				else if (javaxInjectProviderClass == descriptor.getDependencyType()) {  // !!! 参数类型 javax.inject.Provider
				}
				else { // 其他参数类型
				}
			}
			
			// 自动装配
			{
				AutowireCandidateResolver autowireCandidateResolver = new SimpleAutowireCandidateResolver();
				isAutowireCandidate(beanName, new RootBeanDefinition(bw.getWrappedClass()), descriptor, autowireCandidateResolver);
			}
		}
		
	}
	
	
	private static Class<?> javaUtilOptionalClass = null;

	private static Class<?> javaxInjectProviderClass = null;

	static {
		try {
			javaUtilOptionalClass =
					ClassUtils.forName("java.util.Optional", DefaultListableBeanFactory.class.getClassLoader());
		}
		catch (ClassNotFoundException ex) {
			// Java 8 not available - Optional references simply not supported then.
		}
		try {
			javaxInjectProviderClass =
					ClassUtils.forName("javax.inject.Provider", DefaultListableBeanFactory.class.getClassLoader());
		}
		catch (ClassNotFoundException ex) {
			// JSR-330 API not available - Provider interface simply not supported then.
		}
	}
	
	private static class FooService{
		private Integer fooInteger;
		private String fooString;
		private String[] fooArray;
		private List<String> fooList;
		private Map<String,FooService> fooMap;
		
		public Integer getFooInteger() {
			return fooInteger;
		}
		public void setFooInteger(Integer fooInteger) {
			this.fooInteger = fooInteger;
		}
		public String getFooString() {
			return fooString;
		}
		public void setFooString(String fooStringArgName) {
			this.fooString = fooStringArgName;
		}
		public String[] getFooArray() {
			return fooArray;
		}
		public void setFooArray(String[] fooArray) {
			this.fooArray = fooArray;
		}
		public List<String> getFooList() {
			return fooList;
		}
		public void setFooList(List<String> fooList) {
			this.fooList = fooList;
		}
		public Map<String, FooService> getFooMap() {
			return fooMap;
		}
		public void setFooMap(Map<String, FooService> fooMap) {
			this.fooMap = fooMap;
		}
		
		
	}
	
	private static class AutowireByTypeDependencyDescriptor extends DependencyDescriptor {

		public AutowireByTypeDependencyDescriptor(MethodParameter methodParameter, boolean eager) {
			super(methodParameter, false, eager);
		}

		@Override
		public String getDependencyName() {
			return null;
		}
	}

}
