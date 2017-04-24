<%@ include file="/WEB-INF/servletView/admin/Common-header.jsp" %><%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ page import="cn.java.core.helper.url.UrlHelper,cn.java.core.helper.url.JspPageHelper,java.util.HashMap,java.util.ArrayList" %>
<table class="cd_table infor_table bde fz14">
		<thead>
			<tr height="55" align="center">
				<th width="10%">ID</th>
				<th width="20%">信息分类</th>
				<th width="20%">二级分类</th>
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
						<td>------</td>
						<td><%=JspPageHelper.getString("articleCount", itemInfo) %></td>
						<td>
							<a class="disib cd_operation itemEdit" href="javascript:void(0);" data-id="<%=JspPageHelper.getString("id", itemInfo) %>"><i class="bg_a"></i>编辑</a> 
							<a class="disib cd_operation itemDel" href="javascript:void(0);" data-id="<%=JspPageHelper.getString("id", itemInfo) %>"><i class="bg_a cd_remove"></i>删除</a>
						</td>
				</tr>
				<% if(itemInfo.get("childList")!=null){ 
					ArrayList childList = (ArrayList)itemInfo.get("childList");
					for(int index4Child=0;index4Child<childList.size();index4Child++){
						HashMap itemInfo4Child = (HashMap)childList.get(index4Child);%>
						<tr height="55">
							<td><%=JspPageHelper.getString("id", itemInfo4Child) %></td>
							<td><%=JspPageHelper.getString("cateName", itemInfo) %></td>
							<td><%=JspPageHelper.getString("cateName", itemInfo4Child) %></td>
							<td><%=JspPageHelper.getString("articleCount", itemInfo4Child) %></td>
							<td>
								<a class="disib cd_operation itemEdit" href="javascript:void(0);" data-id="<%=JspPageHelper.getString("id", itemInfo4Child) %>"><i class="bg_a"></i>编辑</a> 
								<a class="disib cd_operation itemDel" href="javascript:void(0);" data-id="<%=JspPageHelper.getString("id", itemInfo4Child) %>"><i class="bg_a cd_remove"></i>删除</a>
							</td>
						</tr>
					<% }
				 }
			 }%>
		</tbody>
	</table>
	<div class="sm_btn cd_submit fz14">
		<a id="addClassification">添加分类</a>
		<a href="javascript:void(0);" alt="更 新" class="ck_update" >更 新</a>
	</div>
	<div class="cd_number">
	</div>

<script type="text/javascript">
	//更新网页
	$(".ck_update").click( function () {
		window.location.href = window.location.href;
	});
	
	//添加子分类
	$(".itemAddSub,#addClassification").click( function () {
		var thisTemp = $(this);
		var dataId = $(this).attr("data-id");
		if(Utils.empty(dataId))
		{
			dataId = 0;
		}
		$.get("<%=UrlHelper.url("admin","article-cate","add","") %>", {"parent_id":dataId },  function(data){
			var dataTemp = data;
			if(dataTemp.status==1)
			{
				jQuery.confirmDialog({
					"title" : "添加信息分类",
					"content" : dataTemp.data.html,
					"rightBtnFunc":function(dialogObj){
						
						//配置校验规则，和错误信息
						$("#formTemp").validate({
							ignore: "",
							rules: {
								cate_name: {
									required: true,
									minlength: 2
								},
							},
							messages: {
								cate_name: {
									required: "请输入分类名称！",
									minlength: "分类名称最少要两个字符！"
								},
							}
						});
						var valid = $("#formTemp").valid();
						if(!valid)
						{
							return false;
						}
						
						var formObj = dialogObj.find("#formTemp");
						var postUrl = formObj.attr("action");
						var postStr = formObj.serialize();
						$.post(postUrl, postStr,function(data){
							var dataTemp2 = data;
							if(dataTemp2.status==1)
							{
								jQuery.toast({'message':dataTemp2.info}).show();
							}
							else
							{
								jQuery.toast({'message':dataTemp2.info}).show();
							}
						}, "json");
					}
				});
			}
			else
			{
				jQuery.toast({'message':dataTemp.info}).show();
			}
		 });
	});
	
	//修改分类
	$(".itemEdit").click( function () {
		var dataId = $(this).attr("data-id");
		var urlBase = '<%=UrlHelper.url("admin","article-cate","edit","") %>';
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
	
	//删除分类
	$(".itemDel").click( function () {
		var thisTemp = $(this);
		var dataId = $(this).attr("data-id");
		//对话框
		jQuery.confirmDelDialog({
			"title" : "确认删除？",
			"content" :"确认删除选中的信息分类？",
			"rightBtnFunc":function(dialogObj){
				var urlBase = '<%=UrlHelper.url("admin","article-cate","delete","") %>';
				$.post(urlBase+'&id='+dataId, {},function(data){
					var dataTemp = data;
					if(dataTemp.status==1)
					{
						jQuery.toast({'message':dataTemp.info}).show();
						thisTemp.parents("tr").remove();
					}
					else
					{
						jQuery.toast({'message':dataTemp.info}).show();
					}
				}, "json");
			}
		});
	});
</script><%@ include file="/WEB-INF/servletView/admin/Common-footer.jsp" %>