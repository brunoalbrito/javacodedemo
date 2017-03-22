package cn.java.note.util;

public class TestParseToken {

	public static void main(String[] args) {
		String text = "prestr ${param1} afterstr";
		parse(text);
		text = "prestr ${parampre\\${param_middle\\}paramafter} afterstr";
		parse(text);
	}
	
	public static void parse(String text) {
		String openToken = "${";
		String closeToken = "}";

		final StringBuilder builder = new StringBuilder();
		final StringBuilder expression = new StringBuilder();
		if (text != null && text.length() > 0) {
			char[] src = text.toCharArray();
			int offset = 0;
			// search open token
			int start = text.indexOf(openToken, offset); // 开始标签
			while (start > -1) {
				if (start > 0 && src[start - 1] == '\\') { // 转义 \${
					// this open token is escaped. remove the backslash and continue.
					builder.append(src, offset, start - offset - 1).append(openToken);
					offset = start + openToken.length();
				} else {
					// found open token. let's search close token.  ${param1.\${param_sub\}}
					expression.setLength(0);
					builder.append(src, offset, start - offset);
					
					offset = start + openToken.length();
					int end = text.indexOf(closeToken, offset); // 结束标签
					while (end > -1) {
						if (end > offset && src[end - 1] == '\\') { // \}
							// this close token is escaped. remove the backslash and continue.
							expression.append(src, offset, end - offset - 1).append(closeToken);
							System.out.println(expression);
							offset = end + closeToken.length();
							end = text.indexOf(closeToken, offset);
						} else {
							expression.append(src, offset, end - offset);
							offset = end + closeToken.length();
							break;
						}
					}
					if (end == -1) {
						// close token was not found.
						builder.append(src, start, src.length - start);
						offset = src.length;
					} else {
						System.out.println("---" + expression.toString());
//	            		builder.append(handler.handleToken(expression.toString()));
						offset = end + closeToken.length();
					}
				}
				start = text.indexOf(openToken, offset);
			}
			if (offset < src.length) {
				builder.append(src, offset, src.length - offset);
			}
		}
		System.out.println(builder.toString());
	}

}
