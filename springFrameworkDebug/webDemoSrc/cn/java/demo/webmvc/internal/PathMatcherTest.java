package cn.java.demo.webmvc.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

		System.out.println("--->是否是表达式");
		// 是否是表达式
		{
			if (pathMatcher.isPattern("/dir1")) {
				System.out.println("'/dir1' isPattern ...");
			}
			if (pathMatcher.isPattern("/dir1/*")) { // 存在 * 或者 ？，就是表达式
				System.out.println("'/dir1/*' isPattern ...");
			}
		}

		System.out.println("--->提取地址中的变量");
		// 提取地址中的变量
		{
			Map<String, String> uriVariables;
			String bestPattern = "/dir1/{foo}/{key0}"; // 匹配的表达式
			String lookupPath = "/dir1/foovalue/key00;key000=value000;key001=value001"; // 被匹配的地址
			uriVariables = pathMatcher.extractUriTemplateVariables(bestPattern, lookupPath); // 模板变量
			System.out.println(uriVariables);
			// {foo=foovalue, key0=key00;key000=value000;key001=value001}
		}

		// 是否匹配
		System.out.println("--->是否匹配");
		{
			{
				String pattern = "/dir1/dir2/*"; // ---> Pattern.compile(".*")
				String path = "/dir1/dir2/dir3";
				if (pathMatcher.match(pattern, path)) {
					System.out.println("pattern = " + pattern + ", path = " + path + " is match .");
				}
				path = "/dir1/dir2/dir3/dir4";
				if (pathMatcher.match(pattern, path)) {
					System.out.println("pattern = " + pattern + ", path = " + path + " is match .");
				}
				pattern = "/dir1/dir2/*/*";
				path = "/dir1/dir2/dir3/dir4";
				if (pathMatcher.match(pattern, path)) {
					System.out.println("pattern = " + pattern + ", path = " + path + " is match .");
				}
			}

			{
				String pattern = "/dir1/dir2/?"; // ---> Pattern.compile(".")
				String path = "/dir1/dir2";
				if (pathMatcher.match(pattern, path)) {
					System.out.println("pattern = " + pattern + ", path = " + path + " is match .");
				}
				path = "/dir1/dir2/param0";
				if (pathMatcher.match(pattern, path)) {
					System.out.println("pattern = " + pattern + ", path = " + path + " is match .");
				}
			}
			{
				String pattern = "";
				String path = "";

				{
					pattern = "/dir1/dir2/{param0}"; // ==等价==> "/dir1/dir2/{param0:.*}";   --->    Pattern.compile(".*")
					path = "/dir1/dir2/param0";
					if (pathMatcher.match(pattern, path)) {
						System.out.println("pattern = " + pattern + ", path = " + path + " is match .");
					}
				}

				{
					pattern = "/dir1/dir2/{variableName0:[a-z\\-0-9]*}"; // --->
																			// Pattern.compile("[a-z\\-0-9]*")
					path = "/dir1/dir2/param0";
					if (pathMatcher.match(pattern, path)) {
						System.out.println("pattern = " + pattern + ", path = " + path + " is match .");
					}
				}
			}
			
			{
				String pattern = "/rest-handler/list/{length:[0-9]*}/{offset:[0-9]*}";
//				pattern = "/rest-handler/list/{length}/{offset}";
				String path = "/rest-handler/list/1/2";
				if (pathMatcher.match(pattern, path)) {
					System.out.println("pattern = " + pattern + ", path = " + path + " is match .");
				}
				
				path = "/rest-handler/list/1";
				if (pathMatcher.match(pattern, path)) {
					System.out.println("pattern = " + pattern + ", path = " + path + " is match .");
				}
				
				path = "/rest-handler/list/";
				if (pathMatcher.match(pattern, path)) {
					System.out.println("pattern = " + pattern + ", path = " + path + " is match .");
				}
			}
		}

		System.out.println("--->对匹配到的地址进行排序");
		// 对匹配到的地址进行排序
		{
			String lookupPath = "/dir1/dir2/dir3";

			List<String> matches = new ArrayList<String>();
			matches.add("/dir1/dir2/*");
			matches.add("/dir1/dir2/dir3");

			Collections.sort(matches, pathMatcher.getPatternComparator(lookupPath));
			System.out.println(matches);
		}

		test();
		
		System.out.println("--------AntPathStringMatcher----------------");
		{
			AntPathStringMatcher antPathStringMatcher = new AntPathStringMatcher("{variableName0:[a-z\\-0-9]*}",true);
			Map<String, String> uriTemplateVariables = new HashMap<String, String>();
			antPathStringMatcher.matchStrings("abc-123", uriTemplateVariables);
			System.out.println(uriTemplateVariables);
		}
		
	}
	
	public static void test() {
		System.out.println("---------------Pattern------------------");
		Pattern pattern = null;
		String str = "abc-123";
		pattern = Pattern.compile("([a-z" + Pattern.quote("-") + "0-9]*)", Pattern.CASE_INSENSITIVE);
		pattern = Pattern.compile("([a-z\\-0-9]*)", Pattern.CASE_INSENSITIVE);

		Matcher matcher = pattern.matcher(str);
		// System.out.println(matcher.matches());
		if (matcher.matches()) {
			// System.out.println(matcher.groupCount());
			for (int i = 1; i <= matcher.groupCount(); i++) {
				String value = matcher.group(i);
				System.out.println(value);
			}
		}
	}

	protected static class AntPathStringMatcher {

		private static final Pattern GLOB_PATTERN = Pattern
				.compile("\\?|\\*|\\{((?:\\{[^/]+?\\}|[^/{}]|\\\\[{}])+?)\\}");

		private static final String DEFAULT_VARIABLE_PATTERN = "(.*)";

		private final Pattern pattern;

		private final List<String> variableNames = new LinkedList<String>();

		public AntPathStringMatcher(String pattern) {
			this(pattern, true);
		}

		public AntPathStringMatcher(String pattern, boolean caseSensitive) {
			StringBuilder patternBuilder = new StringBuilder();
			Matcher matcher = GLOB_PATTERN.matcher(pattern);
			int end = 0;
			while (matcher.find()) {
				patternBuilder.append(quote(pattern, end, matcher.start()));
				String match = matcher.group();
				if ("?".equals(match)) {
					patternBuilder.append('.');
				} else if ("*".equals(match)) {
					patternBuilder.append(".*");
				} else if (match.startsWith("{") && match.endsWith("}")) {
					int colonIdx = match.indexOf(':');
					if (colonIdx == -1) {
						patternBuilder.append(DEFAULT_VARIABLE_PATTERN);
						this.variableNames.add(matcher.group(1));
					} else {
						String variablePattern = match.substring(colonIdx + 1, match.length() - 1);
						patternBuilder.append('(');
						patternBuilder.append(variablePattern);
						patternBuilder.append(')');
						String variableName = match.substring(1, colonIdx);
						this.variableNames.add(variableName);
					}
				}
				end = matcher.end();
			}
			patternBuilder.append(quote(pattern, end, pattern.length()));
			this.pattern = (caseSensitive ? Pattern.compile(patternBuilder.toString())
					: Pattern.compile(patternBuilder.toString(), Pattern.CASE_INSENSITIVE));
		}

		private String quote(String s, int start, int end) {
			if (start == end) {
				return "";
			}
			return Pattern.quote(s.substring(start, end));
		}

		/**
		 * Main entry point.
		 * 
		 * @return {@code true} if the string matches against the pattern, or
		 *         {@code false} otherwise.
		 */
		public boolean matchStrings(String str, Map<String, String> uriTemplateVariables) {
			Matcher matcher = this.pattern.matcher(str);
			if (matcher.matches()) {
				if (uriTemplateVariables != null) {
					// SPR-8455
					if (this.variableNames.size() != matcher.groupCount()) {
						throw new IllegalArgumentException("The number of capturing groups in the pattern segment "
								+ this.pattern + " does not match the number of URI template variables it defines, "
								+ "which can occur if capturing groups are used in a URI template regex. "
								+ "Use non-capturing groups instead.");
					}
					for (int i = 1; i <= matcher.groupCount(); i++) {
						String name = this.variableNames.get(i - 1);
						String value = matcher.group(i); // 地址
						uriTemplateVariables.put(name, value);
					}
				}
				return true;
			} else {
				return false;
			}
		}
	}

	

}
