<!-- 注释 bof -->
<%-- 这是注释，不会被输出 --%>
<!-- 注释 eof -->

<% out.write("Hello World test JSP ..."); %><br />

<!-- -------------------------- JSP页面的指令 bof（非xml写法、xml写法） --------------------------  -->
<!-- 1.《指令page》，支持的属性检查在，org.apache.jasper.compiler.Validator.DirectiveVisitor.visit(PageDirective n)  -->
<!-- 《指令page》，使用import属性 -->
<%@ page import="java.io.PrintStream,java.text.SimpleDateFormat" %>
<jsp:directive.page import="java.io.IOException,javax.servlet.jsp.JspWriter" />
<br /><br />

<!-- 《指令page》，使用info属性 -->
<%@ page info="i am '/test/jsp/index.jsp'" %>
<jsp:directive.page info="i am '/test/jsp/index.jsp'" />
<br /><br />

<!-- 2.《指令include》，对属性的校验在，org.apache.jasper.compiler.Validator.DirectiveVisitor.visit(IncludeDirective n)  -->
<!-- 《指令include》（静态引入，即：代码会拷贝在一起） -->
<%@ include file="/WEB-INF/jsp/common/header.jsp" %>
<jsp:directive.include file="/WEB-INF/jsp/common/banner.jsp" />
<br /><br />

<!-- 
	3.《指令taglib》，声明myprefix前缀对应的库信息
	myprefix ----- http://cn.java.janchou/jsp/core(web.xml) -----　/WEB-INF/taglib/janchou-taglib.tld
-->
<%@ taglib uri="http://cn.java.janchou/jsp/core" prefix="myprefix" %>
<!-- -------------------------- 指令 eof--------------------------  -->

<!-- -------------------------- 非指令 bof（非xml写法、xml写法）--------------------------  -->
<!-- 1.《声明代码块》 bof-->
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
// declaration method2 定义方法
public String method2(){
	 return "method2...\n";
}
//declaration method3 定义方法
public static void method3(){
	 System.out.println("method3 Declarations...\n");
}
</jsp:declaration>
<!-- 《声明代码块》 eof-->
 
<!-- 2.《表达式》  bof-->
<%=request.getAttribute("username")  %><br />
<%=this.method2() /* 调用方法 */ %><br />
<%="hello world"  %><br />
<jsp:expression>"hello world"</jsp:expression>
<!-- 《表达式》  eof-->
<br /><br />

<!-- 3.《脚本 》 bof-->
<% out.write("i am scriptlet ...");  %><br />
<jsp:scriptlet>out.write("i am scriptlet ...");</jsp:scriptlet>
<!-- 《脚本》  eof-->
<br /><br />

<!-- 4.《文本标签》  bof--> 
jsp:text...
<jsp:text>jsp:text...</jsp:text>
<!-- 《文本标签 》 eof-->
<br /><br />

<!-- 5.《EL表达式》 $ { } bof-->
${param.username}<br />
${pageScope.username}<br />
${requestScope.username}<br />
${sessionScope.username}<br />
${applicationScope.username}<br />
${header["User-Agent"]}<br />
${cookie.userCountry}<br />
${pageContext.request.method} <br />
 
<!-- 《EL表达式》 $ { }  eof-->
<br /><br />

<!-- 6.《保留表达式》 # { }  bof-->
<!-- 
	deferred-syntax-allowed-as-literal=true时，下面的语法是可以合法的（但不做任何执行，可获取jsp渲染结果，再用模板引擎进行渲染）
	deferred-syntax-allowed-as-literal=false时，# { } 是el表达式，但是在做el解析的时候，又回抛出异常，这是tomcat的bug
	JSP 2.1规范对JSP 2.0和Java Server Faces 1.1中的表达式语言进行了统一。
	在JSP 2.1中，字符序列 # {被保留给表达式语言使用，你不能在模板本中使用字符序列 # {。
	如果JSP页面运行在JSP 2.1之前版本的容器中，则没有这个限制。
	对于JSP 2.1的容器，如果在模板文本中需要出现字符序列 # {，那么可以将该属性设置为true。
-->
#{param.username}<br />
#{pageScope.username}<br />
#{requestScope.username}<br />
#{sessionScope.username}<br />
#{applicationScope.username}<br />
#{header["User-Agent"]}<br />
#{cookie.userCountry}<br />
#{pageContext.request.method} <br />
<!-- 《保留表达式》 # { }  eof-->
<br /><br />

<!-- 7.《JSP页面的行为》  bof-->
<!--
	 行为代码的解析：org.apache.jasper.compiler.Parser.parseStandardAction(Node parent) 
	 行为代码的校验：org.apache.jasper.compiler.Validator.ValidateVisitor.visit(IncludeAction n)
	 行为代码的生成：org.apache.jasper.compiler.Generator.GenerateVisitor.visit(IncludeAction n) 
-->
<br />
<!-- 【动态引入行为】 即：代码不会拷贝在一起，是动态执行
解析结果为：org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "/WEB-INF/jsp/common/menu.jsp" + "?" + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("param1", request.getCharacterEncoding())+ "=" + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("value1", request.getCharacterEncoding()), out, true);
-->
<jsp:include page="/WEB-INF/jsp/common/menu.jsp" flush="false">
     <jsp:param name="param1" value="value1" />
</jsp:include>

<br />
<!-- 【跳转行为】
解析结果为：pageContext.forward("/jsp/index.jsp" + "?" + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("forword_param1", request.getCharacterEncoding())+ "=" + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("forword_param1_value", request.getCharacterEncoding()));
-->
<% if(request.getParameter("forword_param1")==null){ %>
	<jsp:forward page="/jsp/index.jsp">
	     <jsp:param name="forword_param1" value="forword_param1_value" />
	</jsp:forward>
<% }else{ %>
	<%=request.getParameter("forword_param1") %>
<% } %>
<br />
<!-- 【jsp:useBean、jsp:setProperty、jsp:getProperty的使用】
< jsp:useBean>解析结果为：
	cn.java.note.bean.JspPageBean bean1InRequest = null;
    bean1InRequest = (cn.java.note.bean.JspPageBean) _jspx_page_context.getAttribute("bean1InRequest", javax.servlet.jsp.PageContext.REQUEST_SCOPE);
    if (bean1InRequest == null){
      bean1InRequest = new cn.java.note.bean.JspPageBean();
      _jspx_page_context.setAttribute("bean1InRequest", bean1InRequest, javax.servlet.jsp.PageContext.REQUEST_SCOPE);
    }

< jsp:setProperty>解析结果为： （检查顺序是：1、pageContext==page 2、request 3、session 4、ApplicationContextFacade==application）
      org.apache.jasper.runtime.JspRuntimeLibrary.introspecthelper(_jspx_page_context.findAttribute("bean1InRequest"), "username", "this is username1", null, null, false);

< jsp:getProperty>解析结果为：（检查顺序是：1、pageContext==page 2、request 3、session 4、ApplicationContextFacade==application）
	out.write(org.apache.jasper.runtime.JspRuntimeLibrary.toString((((cn.java.note.bean.JspPageBean)_jspx_page_context.findAttribute("bean1InRequest")).getUsername())));
-->
<jsp:directive.page import="cn.java.demo.bean.JspPageBean" />
<jsp:useBean id="bean1InPage" class="cn.java.demo.bean.JspPageBean" scope="page" />
<jsp:useBean id="bean1InRequest" class="cn.java.demo.bean.JspPageBean" scope="request" />
<jsp:useBean id="bean1InSession" class="cn.java.demo.bean.JspPageBean" scope="session" />
<jsp:useBean id="bean1InApplication" class="cn.java.demo.bean.JspPageBean" scope="application" />

<jsp:setProperty name="bean1InRequest" property="username" value="this is username1" />
<jsp:getProperty name="bean1InRequest" property="username"/>


<!-- 【jsp:plugin的使用】
解析结果为：
	  java.lang.String _jspx_temp0 = "100";
      java.lang.String _jspx_temp1 = "100";
      out.write("<object classid=\"clsid:8AD9C840-044E-11D1-B3E9-00805F499D93\" name=\"\"" + " width=\"" + _jspx_temp0 + "\"" + " height=\"" + _jspx_temp1 + "\"" + " hspace=\"\" vspace=\"\" align=\"\" codebase=\"\">");
      out.write("\n");
      out.write("<param name=\"java_code\" value=\"\">");
      out.write("\n");
      out.write("<param name=\"java_codebase\" value=\"\">");
      out.write("\n");
      out.write("<param name=\"java_archive\" value=\"\">");
      out.write("\n");
      out.write("<param name=\"type\" value=\"application/x-java-applet;version=\">");
      out.write("\n");
      out.write( "<param name=\"param1\" value=\"" + "value1" + "\">" );
      out.write("\n");
      out.write("<comment>");
      out.write("\n");
      out.write("<EMBED type=\"application/x-java-applet;version=\" name=\"\"" + " width=\"" + _jspx_temp0 + "\"" + " height=\"" + _jspx_temp1 + "\"" + " hspace=\"\" vspace=\"\" align=\"\" pluginspage=\"\" java_code=\"\" java_codebase=\"\" java_archive=\"\"");
      out.write( " param1=\"" + "value1" + "\"" );
      out.write("/>");
      out.write("\n");
      out.write("<noembed>");
      out.write("\n");
      out.write("fallback....");
      out.write("\n");
      out.write("</noembed>");
      out.write("\n");
      out.write("</comment>");
      out.write("\n");
      out.write("</object>");
生成的HTML代码：	
	<object classid="clsid:8AD9C840-044E-11D1-B3E9-00805F499D93" name="" width="100" height="100" hspace="" vspace="" align="" codebase="">
		<param name="java_code" value="">
		<param name="java_codebase" value="">
		<param name="java_archive" value="">
		<param name="type" value="application/x-java-applet;version=">
		<param name="param1" value="value1">
		<comment>
			<EMBED type="application/x-java-applet;version=" name="" width="100" height="100" hspace="" vspace="" align="" pluginspage="" java_code="" java_codebase="" java_archive="" param1="value1"/>
			<noembed>
				fallback....
			</noembed>
		</comment>
	</object>
-->
<jsp:plugin>
	<jsp:attribute name="type">applet</jsp:attribute>
	<jsp:attribute name="code"></jsp:attribute>
	<jsp:attribute name="codebase"></jsp:attribute>
	<jsp:attribute name="align"></jsp:attribute>
	<jsp:attribute name="archive"></jsp:attribute>
	<jsp:attribute name="height">100</jsp:attribute>
	<jsp:attribute name="hspace"></jsp:attribute>
	<jsp:attribute name="jreversion"></jsp:attribute>
	<jsp:attribute name="name"></jsp:attribute>
	<jsp:attribute name="vspace"></jsp:attribute>
	<jsp:attribute name="width">100</jsp:attribute>
	<jsp:attribute name="nspluginurl"></jsp:attribute>
	<jsp:attribute name="iepluginurl"></jsp:attribute>
	<jsp:body>
		<jsp:params>
			<jsp:param name="param1" value="value1" />
		</jsp:params>
		<jsp:fallback>fallback....</jsp:fallback>
	</jsp:body>
</jsp:plugin>
<!-- 【jsp:element的使用】
解析结果为：   
	java.lang.String _jspx_temp2 = "attribute1Value";
	java.lang.String _jspx_temp3 = "attribute2Value";
	out.write("<" + "element1" + " attribute2=\"" + _jspx_temp3 + "\"" + " attribute1=\"" + _jspx_temp2 + "\"" + ">");
	java.lang.String _jspx_temp4 = "attribute1Value";
	java.lang.String _jspx_temp5 = "attribute2Value";
	out.write("<" + "element1Sub1" + " attribute2=\"" + _jspx_temp5 + "\"" + " attribute1=\"" + _jspx_temp4 + "\"" + "/>");
	out.write("</" + "element1" + ">"); 
生成的HTML代码：	
	<element1 attribute2="attribute2Value" attribute1="attribute1Value"><element1Sub1 attribute2="attribute2Value" attribute1="attribute1Value"/></element1>
-->
<jsp:element name="element1">
	<jsp:attribute name="attribute1">attribute1Value</jsp:attribute>
	<jsp:attribute name="attribute2">attribute2Value</jsp:attribute>
	<jsp:body>
		<jsp:element name="element1Sub1">
			<jsp:attribute name="attribute1">attribute1Value</jsp:attribute>
			<jsp:attribute name="attribute2">attribute2Value</jsp:attribute>
		</jsp:element>
	</jsp:body>
</jsp:element>
<!-- 《行为》  eof-->
<br /><br />

<!-- 《自定义标签》  bof-->
<myprefix:echo str="hello tag..." /> <br />
<myprefix:date timestamp="1472065970" format="yyyy-MM-dd HH:mm:ss" timeZone="PRC" /> <br />
<myprefix:date timestamp="1472065970" format="yyyy-MM-dd HH:mm:ss" /> <br />
<myprefix:date timestamp="1472065970" format="yyyy-MM-dd" /> <br />
<!-- 《自定义标签》  eof-->
<br /><br />
<!-- -------------------------- 非指令 eof--------------------------  -->

