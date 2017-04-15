<%@ include file="/WEB-INF/servletView/admin/Common-header.jsp" %><%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ page import="cn.java.core.helper.url.UrlHelper,cn.java.core.helper.url.JspPageHelper,java.util.HashMap,java.util.ArrayList" %>

<!-- 文件上传 -->
<link rel="stylesheet" href="<%=UrlHelper.baseUrl() %>/assets/fileupload/css/jquery.fileupload.css">		
<script src="<%=UrlHelper.baseUrl() %>/assets/fileupload/js/vendor/jquery.ui.widget.js"></script>				
<script src="<%=UrlHelper.baseUrl() %>/assets/fileupload/js/jquery.iframe-transport.js"></script>
<script src="<%=UrlHelper.baseUrl() %>/assets/fileupload/js/jquery.fileupload.js"></script>
<% ArrayList<HashMap> itemList = (ArrayList)request.getAttribute("itemList"); %>
  <br />
  <table class="cd_table link_table bde fz14">
    <thead >
      <tr height=55 align="center">
        <th width="10%">ID</th>
        <th width="10%">选择</th>
        <th width="20%">网址</th>
        <th width="16%">网站名称</th>
        <th width="18%">网站logo</th>
        <th width="8%">更新时间</th>
        <th width="16%">操作</th>
      </tr>
    </thead>
    <% if(itemList==null){ %>
	     <tbody align="center">
	        <tr height=55>
	          <td colspan="7">没有数据</td>
	        </tr>
	    </tbody>
    <% }else{ %>
	     <tbody align="center">
	      <% for (HashMap item : itemList) { %>
	        <tr height=55>
	          <td><%=JspPageHelper.getString("id", item) %></td>
	          <td><input type="checkbox" class="bde"  data-id="<%=JspPageHelper.getString("id", item) %>"> </td>
	          <td><a href="<%=JspPageHelper.getString("site_url", item) %>" class="link_url" target="_blank"><%=JspPageHelper.getString("site_url", item) %></a> </td>
	          <td><%=JspPageHelper.getString("site_name", item) %></td>
	          <td><img src="<%=JspPageHelper.thumbImgAuto(item.get("site_logo").toString()) %>" alt="<%=JspPageHelper.getString("site_name", item) %>" alt="logo"> </td>
	          <td><%=JspPageHelper.timestampFormat(item.get("update_time").toString()) %> </td>
	          <td><a href="javascript:void(0);"  data-id="<%=JspPageHelper.getString("id", item) %>" class="disib cd_operation entryEditBtn"><i class="bg_a"></i>编辑</a> </td>
	        </tr>
	      <% } %>
	    </tbody>
    <% } %>
  </table>
  <div class="sm_btn cd_submit fz14">
    <a class="ck_all">全 选</a>
    <a href="javascript:(function(){ window.location.href = window.location.href; })();" alt="更 新" class="ck_update" >更 新</a>
    <a class="ck_remove batchDel">删除</a>
    <a class="ck_add entryAdd" id="">添加</a>
  </div>

<script type="text/javascript">

	//批量删友情链接
	$(".batchDel").click( function () {
		var idListStr = "";
		$(".cd_table tbody tr input:checked").each(function(i){
			idListStr = idListStr + "," + $(this).attr("data-id");
		 });
		if(Utils.empty(idListStr))
		{
			$.toast({'message':'没有选中友情链接!'}).show();
			return false;
		}
		idListStr = idListStr.substring(1);
		
		//对话框
		jQuery.confirmDelDialog({
			"title" : "确认删除？",
			"content" :"确认删除选中的友情链接？",
			"rightBtnFunc":function(dialogObj){
				var postUrl = '<%=UrlHelper.url("admin","friendlink", "delete") %>';
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
	
	//修改友情链接
	$(".entryEditBtn").click( function () {
		var dataId = $(this).attr("data-id");
		var url = '<%=UrlHelper.url("admin","friendlink", "edit") %>';
		$.get(url+'&id='+dataId, {},  function(data){
			var dataTemp = data;
			if(dataTemp.status==1)
			{
				jQuery.confirmDialog({
					"title" : "编辑友情链接",
					"content" : dataTemp.data.html,
					"rightBtnFunc":function(dialogObj){
						var formObj = dialogObj.find("#formTemp");
						var postUrl = formObj.attr("action");
						var postStr = formObj.serialize();
						$.post(postUrl, postStr,function(datax){
							if(datax.status==1)
							{
								jQuery.toast({'message':datax.info}).show();
							}
							else
							{
								jQuery.toast({'message':datax.info}).show();
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
	
	var formValidConfig = {
		ignore: "",
		rules: {
			site_url: {
				required: true,
				url:true,
			},
			site_name: {
				required: true,
				minlength: 2
			},
			site_logo: {
				required: true,
			},
		},
		messages: {
			site_url: {
				required: "请输入网址！",
				url:"不是有效的URL地址",
			},
			site_name: {
				required: "请输入网站名称！",
				minlength: "网站名称最少要两个字符！"
			},
			site_logo: {
				required: "请上传网站logo！",
			},
		}
	};
	//添加友情链接
	$(".entryAdd").click( function () {
		var url = '<%=UrlHelper.url("admin","friendlink", "add") %>';
		$.get(url, { },  function(data){
			var dataTemp = data;
			if(dataTemp.status==1)
			{
				jQuery.confirmDialog({
					"title" : "添加友情链接",
					"content" : dataTemp.data.html,
					"rightBtnFunc":function(dialogObj){
						//配置校验规则，和错误信息
						$("#formTemp").data("validator",false); // 注销原来的校验器
						$("#formTemp").validate(formValidConfig); // 配置新的校验规则
						var valid = $("#formTemp").valid(); // 校验
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

</script>


<%@ include file="/WEB-INF/servletView/admin/Common-footer.jsp" %>