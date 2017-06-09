<%@ include file="/WEB-INF/templates/common/common-header.jsp" %>

this is "theme-handler/method0.jsp" file.<br />

<!-- JSTL标准库，放到类路径，会被自动扫描到 -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:out value="Hello JSTL." /><br />
${fn:toUpperCase('abc')}<br />

<!-- Spring对JSTL的值，放到类路径，会被自动扫描到 -->
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="springform" uri="http://www.springframework.org/tags/form" %>

<!-- 对RequestContext对进行获取的对象，进行转义 -->
<spring:htmlEscape defaultHtmlEscape="true" /> 

<!-- 对内容进行转移 -->
<spring:escapeBody htmlEscape="true" javaScriptEscape="false">
	被转移的内容 <div>test escapeBody </div><br />
</spring:escapeBody>
<br />
<spring:message code="module0.controller0.method0.message0" arguments="1,2,3" text="默认消息，参数是 {0},{1}..." argumentSeparator="," /> <br />
<spring:message code="module1.controller0.method0.message0" arguments="1,2,3" text="默认消息，参数是 {0},{1}..." argumentSeparator="," /> <br />
<spring:theme code="themes.message.message0" arguments="1,2,3" text="默认消息，参数是 {0},{1}..." argumentSeparator="," /> <br />
<link rel="stylesheet" type="text/css" href="<spring:theme code='themes.csslink.link0'/>" /><br />



<%@ include file="/WEB-INF/templates/common/common-footer.jsp" %>