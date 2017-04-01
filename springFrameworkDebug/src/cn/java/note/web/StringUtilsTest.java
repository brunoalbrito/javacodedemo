package cn.java.note.web;

import org.springframework.util.PatternMatchUtils;
import org.springframework.util.StringUtils;

public class StringUtilsTest {
	public static void main(String[] args) throws Exception {
		String[] list = StringUtils.tokenizeToStringArray("a,b,c", ",; ");
		
		String candidatePattern = "*beanName1,beanName1*";
		String beanName= "beanName1";
		String[] patterns = StringUtils.commaDelimitedListToStringArray(candidatePattern);
		if(PatternMatchUtils.simpleMatch(patterns, beanName)){
			
		}
	}
}
