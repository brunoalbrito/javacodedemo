package cn.java.demo.contexttag.internal;

import java.io.File;
import java.util.regex.Pattern;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;

public class PathMatcherTest {
	
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
		{
			
			String pattern = "cn/java/demo/contexttag/*Component.class";
			String path = "cn/java/demo/contexttag/FooComponent.class";
			if(pathMatcher.match(pattern,path)){
				System.out.println("pathMatcher is match...");
			}
		}
		
		{
			String fullPattern = "D:/javacodedemo/springFrameworkDebug/WebRoot/WEB-INF/classes/cn/java/demo/contexttag/component/impl/*Component.class";
			String path = "D:\\javacodedemo\\springFrameworkDebug\\WebRoot\\WEB-INF\\classes\\cn\\java\\demo\\contexttag\\component\\impl\\ImplOneFooComponent.class";
			String currPath = StringUtils.replace(path, File.separator, "/");
			System.out.println(fullPattern);
			System.out.println(currPath);
			if(pathMatcher.match(fullPattern,currPath)){
				System.out.println("pathMatcher is match...");
			}
		}
	}
}
