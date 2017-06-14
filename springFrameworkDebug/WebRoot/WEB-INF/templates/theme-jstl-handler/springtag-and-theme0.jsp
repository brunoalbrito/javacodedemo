<%@ include file="/WEB-INF/templates/common/common-header.jsp" %>

this is "theme-handler/form0.jsp" file.<br />
<!-- *******************************spring tag********************************** -->
<!-- Spring对JSTL的值，放到类路径，会被自动扫描到 -->
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="springform" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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

<br />


<spring:hasBindErrors name="springTagForm">
	
</spring:hasBindErrors>
<spring:nestedPath path="springTagForm.">
	<form id="formId" class="formClass" style="height:auto;" action="${requestContext.getContextPath()}${requestContext.getPathToServlet()}/theme-jstl-handler/springtag-and-theme0" 
		method="post" enctype="application/x-www-form-urlencoded">
		<label for="username">用户名：</label>
		<spring:bind path="username"  ignoreNestedPath="false" htmlEscape="false">
			<input id="username" name="username" value="<spring:transform  value="${status.getValue()}"  />" type="text">
			<c:if test="${status.isError()}">${status.getErrorMessagesAsString(',')}</c:if>
		</spring:bind>
		<br />
		
		<label for="password">密码：</label>
		<spring:bind path="password"  ignoreNestedPath="false" htmlEscape="false">
			<input id="password" name="password" value="" type="password">
			<c:if test="${status.isError()}">${status.getErrorMessagesAsString(',')}</c:if>
		</spring:bind>
		<br />
		
		<spring:bind path="csrfToken"  ignoreNestedPath="false" htmlEscape="false">
			<input id="csrfToken" name="csrfToken" value="<spring:transform  value="${status.getValue()}"  />" type="hidden">
			<c:if test="${status.isError()}">${status.getErrorMessagesAsString(',')}</c:if>
		</spring:bind>
		<br />
		
		<spring:bind path="city"  ignoreNestedPath="false" htmlEscape="false">
			<label for="city">所在城市：</label>
			<select id="city" name="city">
				<option value="<spring:transform  value="beijian" />" <c:if test="${status.getValue()}=='beijian'">selected="selected"</c:if>>北京</option>
				<option value="<spring:transform  value="shanghai" />" <c:if test="${status.getValue()}=='shanghai'">selected="selected"</c:if>>上海</option>
			</select>
			<c:if test="${status.isError()}">${status.getErrorMessagesAsString(',')}</c:if>
		</spring:bind>
		<br />
		
		<spring:bind path="sex"  ignoreNestedPath="false" htmlEscape="false">
			<label for="sex">性别：</label>
			<input id="sex1" name="sex" value="nan" type="radio" <c:if test="${status.getValue()}=='nan'">checked="checked"</c:if>><label for="sex1">男</label>
			<input id="sex2" name="sex" value="nv" type="radio" <c:if test="${status.getValue()}=='nv'">checked="checked"</c:if>><label for="sex2">女</label>
			<c:if test="${status.isError()}">${status.getErrorMessagesAsString(',')}</c:if>
		</spring:bind>
		<br />
		
		<spring:bind path="likes"  ignoreNestedPath="false" htmlEscape="false">
			<label for="likes">爱好：</label>
			<input id="likes1" name="likes" value="sports" type="checkbox" <c:if test="${status.getValue().contains('sports')}">checked="checked"</c:if>> <label for="likes1">运动</label>
			<input id="likes2" name="likes" value="reading" type="checkbox" <c:if test="${status.getValue().contains('reading')}">checked="checked"</c:if>> <label for="likes2">阅读</label>
			<c:if test="${status.isError()}">${status.getErrorMessagesAsString(',')}</c:if>
		</spring:bind>
		<br />
		
		<spring:bind path="description"  ignoreNestedPath="false" htmlEscape="false">
			<label for="description">简介：</label>
			<textarea id="description" name="description" rows="3" cols="50"><spring:transform  value="${status.getValue()}"  /></textarea>
			<c:if test="${status.isError()}">${status.getErrorMessagesAsString(',')}</c:if>
		</spring:bind>
		<br />
		<button id="submitform" name="submitform" type="submit" value="submitform_val">提交</button>
	</form>
</spring:nestedPath>


<%@ include file="/WEB-INF/templates/common/common-footer.jsp" %>