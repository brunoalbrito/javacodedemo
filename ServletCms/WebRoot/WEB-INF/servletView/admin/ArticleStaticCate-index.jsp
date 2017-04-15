<%@ include file="/WEB-INF/servletView/admin/Common-header.jsp" %><%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ page import="cn.java.core.helper.url.UrlHelper,cn.java.core.helper.url.JspPageHelper,java.util.HashMap,java.util.ArrayList" %>
<table class="cd_table infor_table bde fz14">
		<thead>
			<tr height="55" align="center">
				<th width="10%">ID</th>
				<th width="20%">信息分类</th>
				<th width="20%">文章数</th>
				<th width="20%">操作</th>
			</tr>
		</thead>
		<tbody align="center">
		<% ArrayList itemList = (ArrayList)request.getAttribute("itemList");
			for(int index=0;index<itemList.size();index++){
			HashMap itemInfo = (HashMap)itemList.get(index);%>
				<tr height="55">
						<td><%=JspPageHelper.getString("id", itemInfo) %></td>
						<td><%=JspPageHelper.getString("cateName", itemInfo) %></td>
						<td><%=JspPageHelper.getString("articleCount", itemInfo) %></td>
						<td>
							<a class="disib cd_operation itemEdit" href="javascript:void(0);" data-id="<%=JspPageHelper.getString("id", itemInfo) %>"><i class="bg_a"></i>编辑</a> 
						</td>
				</tr>
			<% 
				} 
			%>
		</tbody>
	</table>
	<div class="sm_btn cd_submit fz14">
		<a href="javascript:void(0);" alt="更 新" class="ck_update" >更 新</a>
		<a href="<%=UrlHelper.url("admin","article","add","") %>" alt="添加文章" class="ck_update" >添加文章</a>
	</div>
	<div class="cd_number">
	</div>

<script type="text/javascript">
	//更新网页
	$(".ck_update").click( function () {
		window.location.href = window.location.href;
	});
	
	
	//修改分类
	$(".itemEdit").click( function () {
		var dataId = $(this).attr("data-id");
		var urlBase = '<%=UrlHelper.url("admin","article-static-cate","edit","") %>';
		$.get(urlBase+'&id='+dataId, { },  function(data){
			var dataTemp = data;
			if(dataTemp.status==1)
			{
				jQuery.confirmDialog({
					"title" : "编辑信息分类",
					"content" : dataTemp.data.html,
					"rightBtnFunc":function(dialogObj){
						var formObj = dialogObj.find("#formTemp");
						var postUrl = formObj.attr("action");
						var postStr = formObj.serialize();
						$.post(postUrl, postStr,function(data){
							var dataTemp = data;
							if(dataTemp.status==1)
							{
								jQuery.toast({'message':dataTemp.info}).show();
							}
							else
							{
								jQuery.toast({'message':dataTemp.info}).show();
							}
						}, "json");
					},
					
				});
			}
			else
			{
				jQuery.toast({'message':dataTemp.info}).show();
			}
		 });
	});
</script>
</script><%@ include file="/WEB-INF/servletView/admin/Common-footer.jsp" %>