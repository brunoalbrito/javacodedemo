<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%><div><%@ page import="cn.java.core.helper.url.UrlHelper,java.util.HashMap,java.util.ArrayList,cn.java.core.helper.url.JspPageHelper" %>
	<form id="formTemp" action="<%=UrlHelper.url("admin","article-static-cate","edit","") %>" method="post"  enctype="application/x-www-form-urlencoded">
		<ul class="lauer_classification">
			<% HashMap itemInfo = (HashMap)request.getAttribute("item"); %>
			<li> <span>分类名称：</span>
				<input type="text"  name="cate_name" value="<%=JspPageHelper.getString("cateName", itemInfo) %>" />
				<input type="hidden"  name="id" value="<%=JspPageHelper.getString("id", itemInfo) %>" />
			</li>
		</ul>
	</form>
</div>