package cn.java.demo.beantag.internal;

import org.springframework.util.PatternMatchUtils;
import org.springframework.util.StringUtils;

public class InternalUtils_StringUtils {

	public static void main(String[] args) {
		{
			System.out.println(StringUtils.commaDelimitedListToStringArray("a,b,c"));
		}
		{
			System.out.println(StringUtils.trimWhitespace(" a b c "));
		}
		{
			String candidatePattern = "*beanName1,beanName1*";
			String beanName= "beanName1";
			String[] patterns = StringUtils.commaDelimitedListToStringArray(candidatePattern);
			if(PatternMatchUtils.simpleMatch(patterns, beanName)){
				
			}
		}
	}

}
