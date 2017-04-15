<!-- 注释 bof -->
<%-- 这是注释，不会被输出 --%>
<!-- 注释 eof -->
<% out.write("Hello World test JSP ..."); %><br />
<!-- 指令 bof -->
<%@ page import="java.io.PrintStream,java.text.SimpleDateFormat" %>
<jsp:directive.page import="java.io.IOException,javax.servlet.jsp.JspWriter" />
<br /><br />

<%@ page info="i am '/test/jsp/index.jsp'" %>
<jsp:directive.page info="i am '/test/jsp/index.jsp'" />
<br /><br />

<!-- 静态引入 -->
<%@ include file="/WEB-INF/jsp/common/header.jsp" %>
<jsp:directive.include file="/WEB-INF/jsp/common/banner.jsp" />
<br /><br />

<%@ taglib uri="http://cn.java.janchou/jsp/core" prefix="myprefix" %>
<!-- 指令 eof -->
 
<!-- 声明代码块 bof-->
<%!
// 
static {
	System.out.println("static block...\n");
}
// declaration method1
public void method1(){
	System.out.println("method1 Declarations1...\n");
}
%>
<jsp:declaration>
// declaration method2
public String method2(){
	 return "method2...\n";
}
//declaration method3
public static void method3(){
	 System.out.println("method3 Declarations...\n");
}
</jsp:declaration>
 <!-- 声明代码块 eof-->
 
<!-- 表达式  bof-->
<%=request.getAttribute("username")  %><br />
<%=this.method2()  %><br />
<%="hello world"  %><br />
<jsp:expression>"hello world"</jsp:expression>
<!-- 表达式  eof-->
<br /><br />

<!-- 脚本  bof-->
<% out.write("i am scriptlet ...");  %><br />
<jsp:scriptlet>out.write("i am scriptlet ...");</jsp:scriptlet>
<!-- 脚本  eof-->
<br /><br />

<!-- 文本标签  bof--> 
<jsp:text>jsp:text...</jsp:text>
<!-- 文本标签  eof-->
<br /><br />

<!-- EL表达式 $ { } bof-->
${param.username}<br />
${pageScope.username}<br />
${requestScope.username}<br />
${sessionScope.username}<br />
${applicationScope.username}<br />
${header["User-Agent"]}<br />
${cookie.userCountry}<br />
${pageContext.request.method} <br />
 
<!-- EL表达式 $ { }  eof-->
<br /><br />

<!-- EL表达式 # { } bof-->

<!-- EL表达式 # { }  eof-->
<br /><br />

<!-- 行为  bof-->
org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "/WEB-INF/jsp/common/menu.jsp" + "?" + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("param1", request.getCharacterEncoding())+ "=" + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("value1", request.getCharacterEncoding()), out, true);
<br />
<!-- 动态引入行为 -->
<jsp:include page="/WEB-INF/jsp/common/menu.jsp" flush="false">
     <jsp:param name="param1" value="value1" />
</jsp:include>

<br />
<!-- 跳转行为 -->
<% if(request.getParameter("forword_param1")==null){ %>
	<jsp:forward page="/jsp/index.jsp">
	     <jsp:param name="forword_param1" value="forword_param1_value" />
	</jsp:forward>
<% }else{ %>
	<%=request.getParameter("forword_param1") %>
<% } %>
<!-- 行为  eof-->
<br /><br />

<!-- 自定义标签  bof-->
<myprefix:echo str="hello tag..." /> <br />
<myprefix:date timestamp="1472065970" format="yyyy-MM-dd HH:mm:ss" timeZone="PRC" /> <br />
<myprefix:date timestamp="1472065970" format="yyyy-MM-dd HH:mm:ss" /> <br />
<myprefix:date timestamp="1472065970" format="yyyy-MM-dd" /> <br />
<!-- 自定义标签  eof-->
<br /><br />


