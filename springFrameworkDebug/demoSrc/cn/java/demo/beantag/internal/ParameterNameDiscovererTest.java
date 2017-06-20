package cn.java.demo.beantag.internal;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.StandardReflectionParameterNameDiscoverer;

public class ParameterNameDiscovererTest {

	public static void main(String[] args) {
		System.out.println("--------------testLocalVariableTableParameterNameDiscoverer----------------");
		testLocalVariableTableParameterNameDiscoverer();
		System.out.println("--------------testStandardReflectionParameterNameDiscoverer----------------");
		testStandardReflectionParameterNameDiscoverer();
		System.out.println("--------------testDefaultParameterNameDiscoverer----------------");
		testDefaultParameterNameDiscoverer();
	}
	
	/**
	 * 使用java8的特性
	 */
	public static void testStandardReflectionParameterNameDiscoverer() {
		StandardReflectionParameterNameDiscoverer parameterNameDiscoverer = new StandardReflectionParameterNameDiscoverer();
		invokeGetParameterNames(parameterNameDiscoverer);
	}
	
	/**
	 * 
	 * @param parameterNameDiscoverer
	 */
	public static void invokeGetParameterNames(ParameterNameDiscoverer parameterNameDiscoverer) {
		for (Method method : FooService.class.getMethods()) {
			if(!method.getName().startsWith("set")){
				continue;
			}
			String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
			StringBuilder sb = new StringBuilder();
			sb.append(method.getName());
			if(parameterNames!=null){
				sb.append(" - ");
				sb.append("" + parameterNames.length);
				sb.append(" - ");
				int i = 0;
				for (String parameterName : parameterNames) {
					if(i!=0){
						sb.append(",");
					}
					sb.append(parameterName);
					i++;
				}
			}
			System.out.println(sb.toString());
		}
	}
	
	/**
	 * 本地变量解析（解析字节码文件）
	 */
	public static void testLocalVariableTableParameterNameDiscoverer() {
		LocalVariableTableParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
		invokeGetParameterNames(parameterNameDiscoverer);
	}
	
	/**
	 * 聚合模式
	 */
	public static void testDefaultParameterNameDiscoverer() {
		ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
		invokeGetParameterNames(parameterNameDiscoverer);
	}

	private static class FooService {
		private Integer fooInteger;
		private String fooString;
		private String[] fooArray;
		private List<String> fooList;
		private Map<String, FooService> fooMap;
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
		
		public void setFooMapX(Map<String, FooService> fooMapz) {
		}
		
		public void setFooServiceX(FooService fooService) {
		}

	}
}
