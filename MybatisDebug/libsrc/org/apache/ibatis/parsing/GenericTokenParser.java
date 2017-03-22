/**
 *    Copyright 2009-2015 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.parsing;

/**
 * @author Clinton Begin
 */
public class GenericTokenParser {

  private final String openToken;
  private final String closeToken;
  private final TokenHandler handler;

  public GenericTokenParser(String openToken, String closeToken, TokenHandler handler) {
    this.openToken = openToken;
    this.closeToken = closeToken;
    this.handler = handler;
  }

  public String parse(String text) {
//	text == "select * from user where user_name like \"%\"#{name}\"%\""
//	text == "select * from user where user_name like \"%\"${name}\"%\""
	// 如 "prestr ${parampre\${param_middle\}paramafter} afterstr";
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
              expression.append(src, offset, end - offset - 1).append(closeToken); // param1\${param_sub}
              offset = end + closeToken.length();
              end = text.indexOf(closeToken, offset); // 另一个 结束标签
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
            builder.append(handler.handleToken(expression.toString()));
            offset = end + closeToken.length();
          }
        }
        start = text.indexOf(openToken, offset);
      }
      if (offset < src.length) {
        builder.append(src, offset, src.length - offset);
      }
    }
    return builder.toString();
  }
}
