<%@ include file="/WEB-INF/templates/common/common-header.jsp" %>

this is "theme-handler/method0.jsp" file.<br />

<!-- *******************************jstl********************************** -->
<!-- JSTL标准库，放到类路径，会被自动扫描到 -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:out value="Hello JSTL." /><br />
${fn:toUpperCase('abc')}<br />

<!-- *******************************spring tag********************************** -->
<!-- Spring对JSTL的值，放到类路径，会被自动扫描到 -->
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="springform" uri="http://www.springframework.org/tags/form" %>

<!-- htmlEscape标签，对RequestContext对进行获取的对象，进行转义 -->
<spring:htmlEscape defaultHtmlEscape="true" />

<!-- escapeBody标签，对内容进行转义 -->
<spring:escapeBody htmlEscape="true" javaScriptEscape="false">
	被转义的内容 <div>test escapeBody </div><br />
</spring:escapeBody>
<br />

<!-- message标签，多语言标签 -->
<spring:message code="module0.controller0.method0.message0" arguments="1,2,3" text="默认消息，参数是 {0},{1}..." argumentSeparator="," /> <br />
<spring:message code="module1.controller0.method0.message0" arguments="1,2,3" text="默认消息，参数是 {0},{1}..." argumentSeparator="," /> <br />
<spring:message code="module3.controller0.method0.message0" text="默认消息，参数是 {0},{1}...">
	<spring:argument value="值_1" />
	<spring:argument value="值_2" />
	<spring:argument value="值_3" />
</spring:message> <br />

<!-- theme标签，多主题标签 -->
<spring:theme code="themes.message.message0" arguments="1,2,3" text="默认消息，参数是 {0},{1}..." argumentSeparator="," /> <br />
<link rel="stylesheet" type="text/css" href="<spring:theme code='themes.csslink.link0'/>" /><br />

<!-- 功能：输出值 -->

<!-- hasBindErrors标签 -->
<% 
// 把 @ ModelAttribute(name="objectName0")注解的校验结果 org.springframework.validation.Errors放入request
// 等价 request.setAttribute("errors",request.getAttribute("org.springframework.validation.BindingResult.objectName0")); 
%>
<spring:hasBindErrors name="objectName0" htmlEscape="true">
	
</spring:hasBindErrors>

<!-- url标签 -->
<!-- 生成的地址“/上下文地址/controller1/method0” -->
<a href="<spring:url value="/controller1/method0"  />">test a href</a> 
<br />
<!-- 生成的地址“/context1/controller1/method0” -->
<a href="<spring:url context="/context1" value="/controller1/method0" />">test a href</a> 
<br />
<!-- 生成的地址“/context1/controller1/method0/param0_val/param1_val” -->
<a href="<spring:url context="/context1" value="/controller1/method0/{param0}/{param1}"> 
	<spring:param name="param0" value="param0_val" />
	<spring:param name="param1" value="param1_val" />
</spring:url>">test a href</a>
<br />
<!-- 生成的地址“/context1/controller1/method0/param0_val/param1_val?param2=param2_val” -->
<a href="<spring:url context="/context1" value="/controller1/method0/{param0}/{param1}"> 
	<spring:param name="param0" value="param0_val" />
	<spring:param name="param1" value="param1_val" />
	<spring:param name="param2" value="param2_val" />
</spring:url>">test a href</a>
<br />

<!-- eval标签 -->
<%  // request.setAttribute("str", "stringValue"); %>
<spring:eval scope="request" var="str" expression="'stringValue'"  />
<!-- 尝试获取的顺序是：pageScope、request、session、context -->
<spring:eval expression="str"  />
<br />

<spring:eval scope="request" var="listTemp0" expression="{'item0','item1','item2'}"  />
<spring:eval expression="listTemp0[0]"  />
<br />

<spring:eval scope="request" var="mapTemp0" expression="{'key0':'key0_val','key1':'key1_val','key2':'key2_val'}"  />
<spring:eval expression="mapTemp0['key1']"  />
<br />

<spring:eval scope="request" var="mapTemp1" expression="{'key0':'key0_val','key1':{'key1_0':'key1_0_val','key1_1':'key1_1_val'},'key2':'key2_val'}"  />
<spring:eval expression="mapTemp1['key1']['key1_1']"  />
<br />
<!-- 规则是："类名中的每个大写字符"+"#"+"方法名" -->
${spring:mvcUrl('TJH#method1').arg(0,'pathVar0_val').arg(2,'reqParam0_val').arg(3,'reqParam1_val').build()} <br />
${spring:mvcUrl('TJH#method1').arg(0,'pathVar0_val').arg(1,'pathVar1_val').arg(2,'reqParam0_val').arg(3,'reqParam1_val').build()} <br />
${spring:mvcUrl('TJH#method1').arg(0,'pathVar0_val').arg(2,'reqParam0_val').build()} <br />


<%@ include file="/WEB-INF/templates/common/common-footer.jsp" %>