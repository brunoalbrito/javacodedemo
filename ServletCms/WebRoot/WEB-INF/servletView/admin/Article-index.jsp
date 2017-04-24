<%@ include file="/WEB-INF/servletView/admin/Common-header.jsp" %><%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ page import="cn.java.core.helper.url.UrlHelper,cn.java.core.helper.url.JspPageHelper,java.util.HashMap,java.util.ArrayList,java.util.Date" %>
<br />
<table class="cd_table bde fz14">
	<thead >
		<tr height=55 align="center">
			<th width="10%">ID</th>
			<th width="10%">选择</th>
			<th width="21%">文章标题</th>
			<th width="12%">添加时间</th>
			<th width="12%">信息分类</th>
			<th width="10%">点击</th>
			<th width="15%">操作</th>
		</tr>
	</thead>
	<tbody align="center">
		<% if(request.getAttribute("itemList")!=null){ 
			ArrayList itemList = (ArrayList)request.getAttribute("itemList");
			for(int index=0;index<itemList.size();index++){
				HashMap itemInfo = (HashMap)itemList.get(index);
		%>
			<tr height=55>
				<td><%=JspPageHelper.getString("id", itemInfo) %></td>
				<td><input type="checkbox" class="bde" data-id="<%=JspPageHelper.getString("id", itemInfo) %>"></td>				
				<td><%=JspPageHelper.getString("article_name", itemInfo) %></td>
				<td><%
						long microTime = Long.valueOf((String)itemInfo.get("add_time")) * 1000;
						out.write((new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date(microTime)));
					 %></td>
				<td><%=JspPageHelper.getString("cate_name", itemInfo) %></td>
				<td><%=JspPageHelper.getString("view_count", itemInfo) %></td>
				<td>					
					<a href="<%=UrlHelper.url("admin","article", "edit", "id="+itemInfo.get("id")) %>" class="disib cd_operation"><i class="bg_a"></i>编辑</a>
					<a href="javascript:void(0);" class="disib cd_operation"><i class="bg_a cd_preview"></i>预览</a>
				</td>
			</tr>
		<%	 } 
		} %>
	</tbody>
</table>

<div class="sm_btn cd_submit fz14">
	<a href="javascript:void(0);" class="ck_all">全 选</a>
	<a href="javascript:void(0);" class="ck_update">更 新</a>
	<a href="javascript:void(0);" class="ck_remove">删 除</a>
	<a href="<%=UrlHelper.url("admin","article", "add") %>">添加文章</a>	
	<a href="<%=UrlHelper.url("admin","article", "recycle-bin") %>">文章回收站</a>
</div>
<div class="cd_number">
<%=request.getAttribute("pagination") %>
</div>
<script type="text/javascript">
//批量选中
$(".ck_all").click( function () {
	$(".cd_table tbody tr input:checkbox").attr("checked","checked");
});

//更新网页
$(".ck_update").click( function () {
	window.location.href = window.location.href;
});

//批量删除
$(".ck_remove").click( function () {
	var idListStr = "";
	$(".cd_table tbody tr input:checked").each(function(i){
		idListStr = idListStr + "," + $(this).attr("data-id");
	 });
	if(Utils.empty(idListStr))
	{
		$.toast({'message':'没有选中文章!'}).show();
		return false;
	}
	idListStr = idListStr.substring(1);
	//对话框
	jQuery.confirmDelDialog({
		"title" : "确认删除？",
		"content" :"确认删除选中的文章？",
		"rightBtnFunc":function(dialogObj){
			var postUrl = '<%=UrlHelper.url("admin","article", "delete") %>';
			$.post(postUrl+'&ids='+idListStr, {},function(data){
				var dataTemp = data;
				if(dataTemp.status==1)
				{
					jQuery.toast({'message':dataTemp.info}).show();
					$(".cd_table tbody tr input:checked").parents("tr").remove();
				}
				else
				{
					jQuery.toast({'message':dataTemp.info}).show();
				}
			}, "json");
		}
	});
});
</script>
<%@ include file="/WEB-INF/servletView/admin/Common-footer.jsp" %>