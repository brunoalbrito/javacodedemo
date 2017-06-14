package cn.java.demo.beantag.internal;

import org.springframework.util.PatternMatchUtils;

public class InertnalUtils_PatternMatchUtils {

	public static void main(String[] args) {
		String mappedName = "insert*";
		String methodName = "insertUser";
		if( PatternMatchUtils.simpleMatch(mappedName, methodName)){
			System.out.println(methodName + " is match " + mappedName);
		}
	}

}
