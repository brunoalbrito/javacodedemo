package cn.java.demo.beantag.internal;

import org.springframework.util.StringUtils;

public class InternalUtils_StringUtils {

	public static void main(String[] args) {
		System.out.println(StringUtils.commaDelimitedListToStringArray("a,b,c"));
		System.out.println(StringUtils.trimWhitespace(" a b c "));
	}

}
