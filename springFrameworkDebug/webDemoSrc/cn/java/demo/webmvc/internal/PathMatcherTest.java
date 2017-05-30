package cn.java.demo.webmvc.internal;

import java.util.Map;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

public class PathMatcherTest {

	public static void main(String[] args) {
		PathMatcher pathMatcher = new AntPathMatcher();
		
		// 合并表达式
		{
			String typeLevelPattern = "/ctrl1";
			String methodLevelPattern = "/method0";
			System.out.println(pathMatcher.combine(typeLevelPattern, methodLevelPattern));
			System.out.println(pathMatcher.combine("/ctrl1/*", "/method0"));
			System.out.println(pathMatcher.combine("/ctrl1/*.html", "/method0"));
			System.out.println(pathMatcher.combine("/ctrl1/**", "/method0"));
			System.out.println(pathMatcher.combine("/ctrl1/**.html", "/method0"));
		}
		
		// 是否是表达式
		{
			if(pathMatcher.isPattern("/dir1")){
				System.out.println("'/dir1' isPattern ...");
			}
			if(pathMatcher.isPattern("/dir1/*")){ // 存在  * 或者 ？，就是表达式
				System.out.println("'/dir1/*' isPattern ...");
			}
		}
		
		// 提取地址中的变量
		{
			Map<String, String> uriVariables;
			String bestPattern = "/dir1/{foo}/{key0}"; // 匹配的表达式
			String lookupPath = "/dir1/foovalue/key00;key000=value000;key001=value001"; // 被匹配的地址
			uriVariables = pathMatcher.extractUriTemplateVariables(bestPattern, lookupPath); // 模板变量
			System.out.println(uriVariables);
			// {foo=foovalue, key0=key00;key000=value000;key001=value001}
		}
		
		//是否匹配
		{
			String pattern = "/dir1/dir2/*";
			String path = "/dir1/dir2/dir3";
			if(pathMatcher.match(pattern, path)){
				System.out.println("pattern = "+pattern+", path = "+path+" is match .");
			}
		}
	}

}
