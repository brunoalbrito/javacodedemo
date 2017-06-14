<%@ include file="/WEB-INF/templates/common/common-header.jsp" %>

<%@ page import="cn.java.demo.webmvc.bean.interceptor.CsrfHandlerInterceptor" %>

this is “csrf-handler/method0” template.<br />
<form id="formId" class="formClass" style="height:auto;" action="${requestContext.getContextPath()}${requestContext.getPathToServlet()}/csrf-handler/method0" 
		method="post" enctype="application/x-www-form-urlencoded">
		<%=request.getAttribute(CsrfHandlerInterceptor.class.getName() + ".CSRF_KEYNAME")%><br />
		<%=request.getAttribute(CsrfHandlerInterceptor.class.getName() + ".CSRF_VALUE")%><br />
	<input type="hidden" name="<%=request.getAttribute(CsrfHandlerInterceptor.class.getName() + ".CSRF_KEYNAME")%>" value="<%=request.getAttribute(CsrfHandlerInterceptor.class.getName() + ".CSRF_VALUE")%>" />
	<button id="submitform" name="submitform" type="submit" value="submitform_val">提交</button>
</form>
<%@ include file="/WEB-INF/templates/common/common-footer.jsp" %>