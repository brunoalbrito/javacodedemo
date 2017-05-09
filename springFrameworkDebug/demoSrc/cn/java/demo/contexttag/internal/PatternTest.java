package cn.java.demo.contexttag.internal;

import java.util.regex.Pattern;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

public class PatternTest {
	
	public static void main(String[] args) {
		String expression = "[A-Z][a-z0-9A-Z]*Component.class";
		String className = "ImplOneFooComponent.class";
		Pattern pattern = Pattern.compile(expression);
		if(pattern.matcher(className).matches()){
			System.out.println(" is matches ...");
		}
		
		// ------------------
		findCandidateComponents();
	}

	public static void findCandidateComponents() {
		// String packageSearchPath = "classpath*:cn/java/demo/contexttag/[A-Z][a-z0-9A-Z]*Component.class";
		
		PathMatcher pathMatcher = new AntPathMatcher();
		String pattern = "cn/java/demo/contexttag/*Component.class";
		String path = "cn/java/demo/contexttag/FooComponent.class";
		if(pathMatcher.match(pattern,path)){
			System.out.println("pathMatcher is match...");
		}
	}
}
