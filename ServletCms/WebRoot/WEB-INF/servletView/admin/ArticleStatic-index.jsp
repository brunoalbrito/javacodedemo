<%@ include file="/WEB-INF/servletView/admin/Common-header.jsp" %><%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ page import="cn.java.core.helper.url.UrlHelper,cn.java.core.helper.url.JspPageHelper,java.util.HashMap,java.util.ArrayList,java.util.Date" %>
<br />
<% ArrayList itemList = (ArrayList)request.getAttribute("itemList"); %>
<div class="fixd_table clearfix">
	<table class="cd_table bde fz14">
		<thead >
			<tr height=55 align="center">
				<th width="33%">页面名称</th>
				<th width="33%">最后更新</th>
				<th width="33%">操作</th>
			</tr>
		</thead>
		<tbody>
			<% 	for(int index=0;index<itemList.size();index++){	HashMap itemInfo = (HashMap)itemList.get(index); %>
				<tr height=55 align="center">
					<td><%=JspPageHelper.getString("article_name", itemInfo) %></td>
					<td><%
					long microTime = Long.valueOf((String)itemInfo.get("add_time")) *1000;
					out.write((new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date(microTime)));
					 %></td>
					<td>
						<a href="<%=UrlHelper.url("admin","article-static", "edit", "id="+itemInfo.get("id")) %>" class="disib cd_operation"><i class="bg_a"></i>编辑</a>
					</td>
				</tr>
			<% } %>
		</tbody>
	</table>
</div>
<%@ include file="/WEB-INF/servletView/admin/Common-footer.jsp" %>