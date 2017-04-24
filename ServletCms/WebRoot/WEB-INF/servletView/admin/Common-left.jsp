<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%><%@ page import="cn.java.core.helper.url.UrlHelper" %><!-- 左侧菜单bof -->
<ul class="slide_nav">
	<li><i class="inf_set"></i>
		<div class="set_up">
			<h1 title="基础设置">基础设置</h1>
			<ol>
				<li title="系统基本参数"><a
					href="<%=UrlHelper.url("admin","baseinfo","index") %>">系统基本参数</a></li>
				<li title="清除缓存"><a
					href="<%=UrlHelper.url("admin","baseinfo","clearcache") %>">清除网站缓存</a></li>
			</ol>
		</div></li>
	<li><i class="inf_sec"></i>
		<div class="set_up">
			<h1>内容管理</h1>
			<ol>
				<li><a
					href="<%=UrlHelper.url("admin","article-cate","index") %>">普通文章分类</a></li>
				<li><a
					href="<%=UrlHelper.url("admin","article","index") %>">普通文章管理</a></li>
				<li><a
					href="<%=UrlHelper.url("admin","article-static-cate","index") %>">固定文章分类</a></li>
				<li><a
					href="<%=UrlHelper.url("admin","article-static","index") %>">固定文章管理</a></li>
				<li><a
					href="<%=UrlHelper.url("admin","friendlink","index") %>">友情链接</a></li>
			</ol>
		</div></li>
	<li><i class="inf_sec"></i>
		<div class="set_up">
			<h1>产品管理</h1>
			<ol>
				<li><a
					href="<%=UrlHelper.url("admin","products-cate","index") %>">产品分类</a></li>
				<li><a
					href="<%=UrlHelper.url("admin","products","index") %>">产品管理</a></li>
			</ol>
		</div></li>
	<li><i class="inf_fif"></i>
		<div class="set_up">
			<h1>其他</h1>
			<ol>
				<li><a
					href="<%=UrlHelper.url("admin","auth","userList") %>userList')) ?>">管理员管理</a></li>
				<li><a
					href="<%=UrlHelper.url("admin","auth","userGroupList") %>">
						管理员分组</a></li>
			</ol>
		</div></li>
</ul>
<!-- 左侧菜单eof -->