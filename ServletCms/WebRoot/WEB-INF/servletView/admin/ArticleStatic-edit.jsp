<%@ include file="/WEB-INF/servletView/admin/Common-header.jsp" %><%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ page import="cn.java.core.helper.url.UrlHelper,cn.java.core.helper.url.JspPageHelper,java.util.HashMap,java.util.ArrayList,java.util.Date" %>
<br />
<%
HashMap itemInfo = (HashMap)request.getAttribute("item");
%>
<div class="ct_mangement details_main">
	<!-- 富文本框插件 -->
	<link rel="stylesheet" href="<%=UrlHelper.baseUrl() %>/assets/kindeditor/themes/default/default.css">
	<script src="<%=UrlHelper.baseUrl() %>/assets/kindeditor/kindeditor-min.js"></script>
	<script src="<%=UrlHelper.baseUrl() %>/assets/kindeditor/lang/zh_CN.js"></script>
	<form class="content" action="<%=UrlHelper.url("admin", "article-static", "edit","&id="+itemInfo.get("id")) %>" method="post"  enctype="application/x-www-form-urlencoded">
		<input type="hidden" name="id" value="<?php echo $item['id']; ?>" />
		<div class="information bde mt10"  style="display:block;">
			<ul class="cm_parameter fz14">
				<li class="clearfix">
					<p>文章标题：</p>
					<input type="text" class="cm_title"  readonly="readonly" value="<%=JspPageHelper.getString("article_name", itemInfo) %>">
					<b class="colorr">*</b>
				</li>
			</ul>
			<div class="cm_content fz14">
				<p>文章内容：</p>
				<div class="cm_kindeditor">
					<textarea  name="content"  style="width:803px;height:300px;"><%=JspPageHelper.getString("content", itemInfo) %></textarea>
				</div>
			</div>
			<div class="sm_btn">
				<input type="submit" value="确定">		
				<input type="reset" value="重置">	
			</div>
		</div>
	</form>
	<script type="text/javascript">
		//富文本框插件
		KindEditor.ready(function(K) {
			K.create('textarea[name="content"]', {
				autoHeightMode : true,
				afterCreate : function() {
					//this.loadPlugin('autoheight');
					}
				});
			});
	</script>
</div>	
<%@ include file="/WEB-INF/servletView/admin/Common-footer.jsp" %>