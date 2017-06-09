<%@ include file="/WEB-INF/templates/common/common-header.jsp" %>
<%@ page import="java.io.*" %>
<%@ page import="org.springframework.web.context.support.XmlWebApplicationContext" %>
<%@ page import="org.springframework.web.servlet.DispatcherServlet" %>
<%@ page import="org.springframework.core.env.PropertySourcesPropertyResolver" %>
this is "exception/index.jsp" file.<br />
<% 
XmlWebApplicationContext webApplicationContext = (XmlWebApplicationContext) request.getAttribute(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE); // 子上下文
PropertySourcesPropertyResolver propertySourcesPropertyResolver = webApplicationContext.getBean("propertySourcesPropertyResolver",PropertySourcesPropertyResolver.class);
Boolean springProjectDebug = propertySourcesPropertyResolver.getProperty("SPRING_PROJECT_DEBUG", Boolean.class);
System.out.print(springProjectDebug);
if(springProjectDebug.equals(Boolean.TRUE)){
	out.write("<pre>");	
	// 异常模板
	Exception exception = (Exception)request.getAttribute("exception");
	ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	PrintStream printStream = new PrintStream(byteArrayOutputStream);
	exception.printStackTrace(printStream);
	String exceptionStr = byteArrayOutputStream.toString();
	out.write(exceptionStr);
	out.write("</pre>");	
}
else{
	out.write("出错了..");	
}
%>
<%@ include file="/WEB-INF/templates/common/common-footer.jsp" %>