package cn.java.test;

import org.apache.commons.lang.StringEscapeUtils;

public class StringEscapeUtilsTest {
	public static void main(String[] args) throws Exception {
		String str = "<html><head></head><body><div class=\"classCls\">123 中国</div></body></html>";
		System.out.println("用escapeJava方法转义之后的字符串为:" + StringEscapeUtils.escapeJava(str));
		System.out.println("用unescapeJava方法反转义之后的字符串为:" + StringEscapeUtils.unescapeJava(StringEscapeUtils.escapeJava(str)));

		System.out.println("用escapeHtml方法转义之后的字符串为:" + StringEscapeUtils.escapeHtml(str));
		System.out.println("用unescapeHtml方法反转义之后的字符串为:" + StringEscapeUtils.unescapeHtml(StringEscapeUtils.escapeHtml(str)));

		System.out.println("用escapeXml方法转义之后的字符串为:" + StringEscapeUtils.escapeXml(str));
		System.out.println("用unescapeXml方法反转义之后的字符串为:" + StringEscapeUtils.unescapeXml(StringEscapeUtils.escapeXml(str)));

		System.out.println("用escapeJavaScript方法转义之后的字符串为:" + StringEscapeUtils.escapeJavaScript(str));
		System.out.println("用unescapeJavaScript方法反转义之后的字符串为:" + StringEscapeUtils.unescapeJavaScript(StringEscapeUtils.escapeJavaScript(str)));

//				用escapeJava方法转义之后的字符串为:<html><head></head><body><div class=\"classCls\">123 \u4E2D\u56FD</div></body></html>
//				用unescapeJava方法反转义之后的字符串为:<html><head></head><body><div class="classCls">123 中国</div></body></html>
//				用escapeHtml方法转义之后的字符串为:&lt;html&gt;&lt;head&gt;&lt;/head&gt;&lt;body&gt;&lt;div class=&quot;classCls&quot;&gt;123 &#20013;&#22269;&lt;/div&gt;&lt;/body&gt;&lt;/html&gt;
//				用unescapeHtml方法反转义之后的字符串为:<html><head></head><body><div class="classCls">123 中国</div></body></html>
//				用escapeXml方法转义之后的字符串为:&lt;html&gt;&lt;head&gt;&lt;/head&gt;&lt;body&gt;&lt;div class=&quot;classCls&quot;&gt;123 &#20013;&#22269;&lt;/div&gt;&lt;/body&gt;&lt;/html&gt;
//				用unescapeXml方法反转义之后的字符串为:<html><head></head><body><div class="classCls">123 中国</div></body></html>
//				用escapeJavaScript方法转义之后的字符串为:<html><head><\/head><body><div class=\"classCls\">123 \u4E2D\u56FD<\/div><\/body><\/html>
//				用unescapeJavaScript方法反转义之后的字符串为:<html><head></head><body><div class="classCls">123 中国</div></body></html>
	}
	
}
