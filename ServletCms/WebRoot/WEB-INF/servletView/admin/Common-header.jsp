<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%><%@ page import="cn.java.core.helper.url.UrlHelper,java.util.HashMap" %><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="zh-CN"  xml:lang="zh-CN">
    <head>
        <meta charset="utf-8">
        <title>自助建站</title>
        <link rel="stylesheet" type="text/css" href="<%=UrlHelper.baseUrl() %>/assets/jquery-validation/css/style.css" />
        <link rel="stylesheet" type="text/css" href="<%=UrlHelper.baseUrl() %>/assets/cms-admin/css/style.css" />
        <link rel="stylesheet" type="text/css" href="<%=UrlHelper.baseUrl() %>/assets/cms-admin/css/layer.css" />
		<script type="text/javascript" src="<%=UrlHelper.baseUrl() %>/assets/base/js/jquery-1.11.2.js"></script>
		<script type="text/javascript" src="<%=UrlHelper.baseUrl() %>/assets/base/js/utils.js"></script>
		<script type="text/javascript" src="<%=UrlHelper.baseUrl() %>/assets/jquery-validation/js/jquery.validate.js"></script>
		<script type="text/javascript" src="<%=UrlHelper.baseUrl() %>/assets/cms-admin/js/management.js"></script>
		<script type="text/javascript" src="<%=UrlHelper.baseUrl() %>/assets/cms-admin/js/jquery.dialogx.js"></script>
		<script type="text/javascript" src="<%=UrlHelper.baseUrl() %>/assets/cms-admin/js/jquery.toastx.js"></script>
    </head>
    <body>
    	<div class="wrapper">
			<div class="header clearfix">
				<div class="logo bg_img"></div>
				<div class="header_nav clearfix">
					<a href="<%=UrlHelper.url("admin","index","index") %>" class="management">
						<i class="bg_img"></i>
						<span>管理主页</span>
					</a>
					<a href="javascript:;" class="home">
						<i class="bg_img"></i>
						<span>网站主页</span>
					</a>
					<a href="http://www.bizcn.com/" class="website" target="_blank">
						<i class="bg_img"></i>
						<span>商务中国官网</span>
					</a>
				</div>	
				<div class="personal clearfix">
					<a href="<%=UrlHelper.url("admin","index","index") %>" class="user_name">
						<i class="bg_img"></i>
						<span>
						<% 
							HttpSession mHttpSession = request.getSession();
							HashMap mHashMap = (HashMap)mHttpSession.getAttribute("admin_info");
							if(mHashMap!=null && mHashMap.containsKey("username")){
								out.print(mHashMap.get("username"));
							}
							else{
								out.write("miss login");
							}
						%>
						</span>
					</a>
					<a href="<%=UrlHelper.url("admin","passport","logout") %>" class="cancellation">
						<i class="bg_img"></i>
						<span>注销登录</span>
					</a>
				</div>
			</div>
			
			<div class="layout">
				<div class="main">
					<% if(request.getAttribute("breadcrumbStr")!=null && !"".equals(request.getAttribute("breadcrumbStr"))){ %>
						<div class="main_title">
							<i class="title_icon bg_img"></i>
							<%=request.getAttribute("breadcrumbStr") %>
						</div>
					<% } %>
			       <%@ include file="/WEB-INF/servletView/admin/Common-left.jsp" %>
