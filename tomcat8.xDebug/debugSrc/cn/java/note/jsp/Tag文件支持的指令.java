package cn.java.note.jsp;

public class Tag文件支持的指令 {

	public static void main(String[] args) {
//		<%-- 注释  --%>
		
	 // 可在标签页面(*.tag)页面使用的指令
	//  <%@ include file="/a/b/c/file.jsp" %> // 会继续解析子目录的指令
	//  <%@ taglib uri="http://cn.java/jsp/core" prefix="myprefix" %>  
	//  <%@ tag import="cn.java.test2.*,cn.java.test.Hello" %>
	//  <%@ attribute attr1="value1" attr2="value2" %>
	//  <%@ variable attr1="value1" attr2="value2" %>
		
	  // 可在标签页面(*.tag)页面使用的指令
	//  <jsp:directive.include file="/a/b/c/file.jsp" /> // 会继续解析子目录的指令
	//  <jsp:directive.tag import="cn.java.test2.*,cn.java.test.Hello" />
	//  <jsp:directive.attribute attr1="value1" attr2="value2" />
	//  <jsp:directive.variable attr1="value1" attr2="value2" />
	}

}
