<%@ include file="/WEB-INF/templates/common/common-header.jsp" %>

this is "theme-handler/form0.jsp" file.<br />

<!-- *******************************spring form tag********************************** -->
<%@ taglib prefix="springform" uri="http://www.springframework.org/tags/form" %>
	<springform:form target="_blank" servletRelativeAction="${requestContext.getContextPath()}${requestContext.getPathToServlet()}/theme-jstl-handler/springformtag0"> </springform:form>
	
	<springform:form modelAttribute="springformTagForm" id="formId" cssClass="formClass" cssStyle="height:auto;"
		  method="post" action="${requestContext.getContextPath()}${requestContext.getPathToServlet()}/theme-jstl-handler/springformtag0" enctype="application/x-www-form-urlencoded">
		 
		<springform:label path="username" for="username" cssErrorClass="usernameCls errorClas" cssClass="usernameCls">用户名：</springform:label> 
		<springform:input path="username" />
		<springform:errors path="username" delimiter="," />
		<br />
		
		<springform:label path="password" for="password">密码：</springform:label> 
		<springform:password path="password" />
		<springform:errors path="password" delimiter="," />
		<br />
		
		<springform:hidden path="csrfToken" />
		<springform:errors path="csrfToken" delimiter="," />
		<br />
		
		<springform:label path="city" for="city">所在城市：</springform:label>
		<springform:select path="city" multiple="false">
			<springform:option value="beijian" label="北京" />
			<springform:option value="shanghai" label="上海" />
		</springform:select> 
		<springform:errors path="city" delimiter="," />
		<br />
		
		<springform:label path="sex" for="sex">性别：</springform:label>
		<springform:radiobutton path="sex" label="男" value="nan"/>
		<springform:radiobutton path="sex" label="女" value="nv"/>
		<springform:errors path="sex" delimiter="," />
		<br />
		
		<springform:label path="likes" for="likes">爱好：</springform:label>
		<springform:checkbox path="likes" label="运动" value="sports" />
		<springform:checkbox path="likes" label="阅读" value="reading" />
		<springform:errors path="likes" delimiter="," />
		<br />
		
		<springform:label path="description" for="description">简介：</springform:label>
		<springform:textarea path="description" rows="3" cols="50"/> 
		<springform:errors path="description" delimiter="," />
		<br />
		
		<springform:button name="submitform" value="submitform_val">提交</springform:button>
		<br />
	</springform:form>
<%@ include file="/WEB-INF/templates/common/common-footer.jsp" %>