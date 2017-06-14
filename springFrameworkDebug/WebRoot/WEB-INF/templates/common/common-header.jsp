<%@page import="org.springframework.web.servlet.support.RequestContext"%>
<%@page import="cn.java.demo.web.util.WebUtilx"%>
<%@ page import="java.util.HashMap" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="zh-CN"  xml:lang="zh-CN">
    <head>
        <meta charset="utf-8">
        <link rel="stylesheet" type="text/css" href="<%=request.getServletContext().getContextPath() %>/assets/jquery-validation/css/style.css" />
    </head>
    <body>

this is header.<br />
方式一（工具类）： <br />
request.getContextPath() ---> <%=request.getContextPath()%>

WebUtilx.getContextUrl("relativeUrl", request, response) ---> 
		<%=WebUtilx.getContextUrl("relativeUrl", request, response)%> <br />
		
WebUtilx.getContextUrl("relativeUrl", params, request, response) --->
		<% 
			HashMap params = new HashMap(); 
			params.put("param0","param0val");
			out.write(WebUtilx.getContextUrl("relativeUrl",params, request, response));
			pageContext.setAttribute("params", params);
		%> <br />
		
WebUtilx.getPathToServlet(request) --->
		<%=WebUtilx.getPathToServlet(request)%> <br />
		
WebUtilx.getRequestUri(request) --->
		<%=WebUtilx.getRequestUri(request) %> <br />
		
request.getQueryString() --->
		<%=request.getQueryString() %> <br />
		
WebUtilx.getContextUrl(WebUtilx.getPathToServlet(request)+"/ctrl0-handler/method0", request, response) ---> 
		<%=WebUtilx.getContextUrl(WebUtilx.getPathToServlet(request)+"/ctrl0-handler/method0", request, response)%> <br />
方式二（封装成对象）： <br />
<%
	RequestContext requestContext = (RequestContext)request.getAttribute("requestContext");
	out.write("requestContext.getContextPath()  ---> "); out.write(requestContext.getContextPath()); out.write("<br /> ");
	out.write("requestContext.getContextUrl(\"relativeUrl\")  ---> "); out.write(requestContext.getContextUrl("relativeUrl")); out.write("<br /> ");
	out.write("requestContext.getContextUrl(\"relativeUrl\",params)  ---> "); out.write(requestContext.getContextUrl("relativeUrl",params)); out.write("<br /> ");
	out.write("requestContext.getPathToServlet()  ---> "); out.write(requestContext.getPathToServlet()); out.write("<br /> ");
	out.write("requestContext.getRequestUri()  ---> "); out.write(requestContext.getRequestUri()); out.write("<br /> ");
	out.write("requestContext.getQueryString()  ---> "); out.write(requestContext.getQueryString()); out.write("<br /> ");
	out.write("requestContext.getContextUrl(requestContext.getPathToServlet()+\"/ctrl0-handler/method0\")  ---> "); out.write(requestContext.getContextUrl(requestContext.getPathToServlet()+"/ctrl0-handler/method0")); out.write("<br /> ");
%>
方式三（EL表达式）： <br />	
\${requestContext.getContextPath()}  ---> ${requestContext.getContextPath()}<br />	
\${requestContext.getContextUrl("relativeUrl")}  ---> ${requestContext.getContextUrl("relativeUrl")}<br />	
\${requestContext.getContextUrl("relativeUrl",params)}  ---> ${requestContext.getContextUrl("relativeUrl",params)}<br />	
\${requestContext.getPathToServlet()}  ---> ${requestContext.getPathToServlet()}<br />	
\${requestContext.getRequestUri()}  ---> ${requestContext.getRequestUri()}<br />	
\${requestContext.getQueryString()}  ---> ${requestContext.getQueryString()}<br />	
\${requestContext.getContextPath()}\${requestContext.getPathToServlet()}/ctrl0-handler/method0  ---> ${requestContext.getContextPath()}${requestContext.getPathToServlet()}/ctrl0-handler/method0<br />
<br /> <br />
		
			