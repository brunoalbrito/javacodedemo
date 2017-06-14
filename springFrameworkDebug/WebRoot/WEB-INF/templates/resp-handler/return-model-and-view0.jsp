<%@ include file="/WEB-INF/templates/common/common-header.jsp" %>

<%@ page import="org.springframework.web.context.support.XmlWebApplicationContext" %>
<%@ page import="org.springframework.web.context.WebApplicationContext" %>
<%@ page import="org.springframework.web.servlet.DispatcherServlet" %>
<%@ page import="org.springframework.web.servlet.support.RequestContextUtils" %>
<%@ page import="java.util.Locale" %>
<%@ page import="org.springframework.ui.context.Theme" %>
<%@ page import="org.springframework.context.MessageSource" %>
<%@ page import="org.springframework.validation.BindingResult" %>
<%@ page import="org.springframework.validation.BeanPropertyBindingResult" %>
<%@ page import="org.springframework.validation.Errors" %>
<%@ page import="org.springframework.validation.BindException" %>
<%@ page import="cn.java.demo.webmvc.form.UserLoginForm" %>
<%@ page import="org.springframework.validation.ObjectError" %>
<%@ page import="java.util.List" %>

this is jsp template.<br />
hello , attr0 = <%=request.getAttribute("attr0") %> , attr1 = <%=request.getAttribute("attr1") %>
<%
XmlWebApplicationContext rootApplicationContext = (XmlWebApplicationContext) request.getServletContext().getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE); // 根上下文
XmlWebApplicationContext webApplicationContext = (XmlWebApplicationContext) request.getAttribute(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE); // 子上下文

// 多语言
Object[] args = { new Long(1273), "DiskOne" };
Locale locale = RequestContextUtils.getLocale(request);
System.out.println(webApplicationContext.getMessage("module0.controller0.method0.message0", args, "默认消息...", locale));  // “languages/module0.properties”
System.out.println(webApplicationContext.getMessage("module1.controller0.method0.message0", args, "默认消息...", locale));  // “languages/module1.properties”

// 多主题
Theme theme = RequestContextUtils.getTheme(request);
MessageSource messageSource = theme.getMessageSource();
System.out.println(messageSource.getMessage("themes.message.message0",null,locale)); // “themes/default.properties” 或者 “themes/theme0.xml”

// 校验错误
Errors errors = (Errors) request.getAttribute(BindingResult.MODEL_KEY_PREFIX + "userLoginForm");
if (errors != null) {
	cn.java.demo.webmvc.validator.UserLoginFormValidator.debugBeanPropertyBindingResult(errors);
}

%>
<%@ include file="/WEB-INF/templates/common/common-footer.jsp" %>