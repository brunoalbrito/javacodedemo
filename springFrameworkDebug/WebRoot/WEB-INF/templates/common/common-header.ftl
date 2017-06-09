<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="zh-CN"  xml:lang="zh-CN">
    <head>
        <meta charset="utf-8">
        
        
        <link rel="stylesheet" type="text/css" href="${requestContext.contextPath}/assets/jquery-validation/css/style.css" />
    </head>
    <body>
    this is header. <br />
    <#noparse>${requestContext}</#noparse> ---> org.springframework.web.servlet.support.RequestContext <br />
    <#noparse>${requestContext.contextPath}</#noparse> ---> ${requestContext.contextPath} <br />
    <#noparse>${requestContext.getContextUrl("relativeUrl")}</#noparse> ---> ${requestContext.getContextUrl("relativeUrl")} <br />
    <#noparse>${requestContext.getContextUrl("relativeUrl",{"param0":"param0val"})}</#noparse> ---> ${requestContext.getContextUrl("relativeUrl",{"param0":"param0val"})} <br />
    <#noparse>${requestContext.pathToServlet}</#noparse> ---> ${requestContext.pathToServlet} <br />
    <#noparse>${requestContext.requestUri}</#noparse> ---> ${requestContext.requestUri} <br />
    <#noparse>${requestContext.queryString!("空")}</#noparse> ---> ${requestContext.queryString!("空")} <br />
    <#noparse>${requestContext.getContextUrl("${requestContext.pathToServlet}/ctrl0-handler/method0")}</#noparse> ---> ${requestContext.getContextUrl("${requestContext.pathToServlet}/ctrl0-handler/method0")} <br />
    
    <!--
	模板中数据的访问是通过：freemarker.ext.servlet.AllHttpScopesHashModel
		访问顺序是：
			用户赋值的 -- > request中的attributes--- > Session中的attributes --- > Application中的attributes
		其中还注入的对象：
			JspTaglibs、Application(访问的是attributes)、Session(访问的是attributes)、Request(访问的是attributes)、RequestParameters(访问的是attributes)
	-->
	attr0 = ${Request["attr0"]!("null")} = ${Request.attr0!("null")} = ${attr0!("null")}<br />
	<#--
	${Application.attr0!("null")}<br />
	${Session.attr0!("null")}<br />
	${Request.attr0!("null")}<br />
	${RequestParameters.attr0!("null")}<br />
	-->

	<br />