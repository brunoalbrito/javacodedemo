<%@ include file="/WEB-INF/servletView/admin/Common-header.jsp" %><%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ page import="cn.java.core.helper.url.UrlHelper,cn.java.core.helper.url.JspPageHelper,java.util.HashMap,java.util.ArrayList,java.util.Date" %>
		<% ArrayList<HashMap> itemList = (ArrayList)request.getAttribute("itemList"); %>
		<br/>
		<table class="cd_table bde fz14">
			<thead >
				<tr height=55 align="center">
					<th style="width: 20%;">ID</th>
					<th style="width: 25%;">商品分类</th>
					<th style="width: 25%;">商品数</th>
					<th style="width: 30%;">操作</th>
				</tr>
			</thead>
			<% if(itemList==null){ %>
			     <tbody align="center">
			        <tr height=55>
			          <td colspan="4">没有数据</td>
			        </tr>
			    </tbody>
		    <% }else{ %>
			<tbody align="center">
				<% for (HashMap item : itemList) { %>
				<tr height=55>
					<td><%=JspPageHelper.getString("id", item) %></td>
					<td><%=JspPageHelper.getString("cate_name", item) %></td>
					<td><%=JspPageHelper.getString("product_count", item) %></td>
					<td>
						<a href="javascript:void(0);" class="disib cd_operation itemEdit" data-id="<%=JspPageHelper.getString("id", item) %>"><i class="bg_a"></i>编辑</a>
						<a href="javascript:void(0);" class="disib cd_operation itemDel" data-id="<%=JspPageHelper.getString("id", item) %>"><i class="bg_a cd_remove"></i>删除</a>
						<a href="javascript:void(0);" class="disib cd_operation itemAddSub" data-id="<%=JspPageHelper.getString("id", item) %>"><i class="bg_a addchildren"></i>增加子类</a>
					</td>
				</tr>
				<% } %>
			</tbody>
			<% } %>
		</table>
		<div class="sm_btn cd_submit fz14">	
			<a href="javascript:void(0);" id="addClassification" >添加分类</a>
			<a href="javascript:void(0);" alt="更 新" class="ck_update" >更 新</a>
		</div>
		<div class="cd_number">
		</div>
	</form>
	<script type="text/javascript">
	var validConfig = {
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
	};
	//更新网页
	$(".ck_update").click( function () {
		window.location.href = window.location.href;
	});
	
	//修改分类
	$(".itemEdit").click( function () {
		var dataId = $(this).attr("data-id");
		var url = '<%=UrlHelper.url("admin","products-cate", "edit") %>';
		$.get(url+'&id='+dataId, {},  function(data){
			var dataTemp = data;
			if(dataTemp.status==1)
			{
				jQuery.confirmDialog({
					"title" : "编辑产品分类",
					"content" : dataTemp.data.html,
					"rightBtnFunc":function(dialogObj){
						//配置校验规则，和错误信息
						$("#formTemp").data('validate',false);
						$("#formTemp").validate(validConfig);
						var valid = $("#formTemp").valid();
						if(!valid)
						{
							return false;
						}
						
						var formObj = dialogObj.find("#formTemp");
						var postUrl = formObj.attr("action");
						var postStr = formObj.serialize();
						$.post(postUrl, postStr,function(data){
							var dataTemp = data;
							console.log(data);
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
			"content" :"确认删除选中的产品分类？",
			"rightBtnFunc":function(dialogObj){
				var url = '<%=UrlHelper.url("admin","products-cate", "delete") %>';
				$.post(url+'&id='+dataId, {"id":dataId},function(data){
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
	
	//添加子分类
	$(".itemAddSub,#addClassification").click( function () {
		var thisTemp = $(this);
		var dataId = $(this).attr("data-id");
		if(Utils.empty(dataId))
		{
			dataId = 0;
		}
		var url = '<%=UrlHelper.url("admin","products-cate", "add") %>';
		$.get(url, {"parent_id":dataId },  function(data){
			var dataTemp = data;
			if(dataTemp.status==1)
			{
				jQuery.confirmDialog({
					"title" : "添加产品分类",
					"content" : dataTemp.data.html,
					"rightBtnFunc":function(dialogObj){
						
						//配置校验规则，和错误信息
						$("#formTemp").data('validate',false);
						$("#formTemp").validate(validConfig);
						var valid = $("#formTemp").valid();
						if(!valid)
						{
							return false;
						}
						var formObj = dialogObj.find("#formTemp");
						var postUrl = formObj.attr("action");
						var postStr = formObj.serialize();
						$.post(postUrl, postStr,function(data){
							if(data.status==1)
							{
								jQuery.toast({'message':data.info}).show();
							}
							else
							{
								jQuery.toast({'message':data.info}).show();
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
	</script>
	<%@ include file="/WEB-INF/servletView/admin/Common-footer.jsp" %>