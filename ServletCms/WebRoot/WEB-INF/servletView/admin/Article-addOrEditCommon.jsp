<%@ include file="/WEB-INF/servletView/admin/Common-header.jsp" %><%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ page import="cn.java.core.helper.url.UrlHelper,cn.java.core.helper.url.JspPageHelper,java.util.HashMap,java.util.ArrayList,java.util.Date" %>
<br />
	<div class="ct_mangement details_main">
			<!-- 富文本框插件 -->
			<link rel="stylesheet" href="<%=UrlHelper.baseUrl() %>/assets/kindeditor/themes/default/default.css">
			<script src="<%=UrlHelper.baseUrl() %>/assets/kindeditor/kindeditor-min.js"></script>
			<script src="<%=UrlHelper.baseUrl() %>/assets/kindeditor/lang/zh_CN.js"></script>
			<!-- 文件上传 -->
			<link rel="stylesheet" href="<%=UrlHelper.baseUrl() %>/assets/fileupload/css/jquery.fileupload.css">		
			<script src="<%=UrlHelper.baseUrl() %>/assets/fileupload/js/vendor/jquery.ui.widget.js"></script>				
			<script src="<%=UrlHelper.baseUrl() %>/assets/fileupload/js/jquery.iframe-transport.js"></script>
			<script src="<%=UrlHelper.baseUrl() %>/assets/fileupload/js/jquery.fileupload.js"></script>
				<form id="formTemp" class="content" action="<%
				HashMap itemInfo = null;
				if(request.getAttribute("item")==null){ 
					out.write(UrlHelper.url("admin", "article", "add"));
				}else{
					itemInfo = (HashMap)request.getAttribute("item");
					out.write(UrlHelper.url("admin", "article", "edit","id="+itemInfo.get("id")));
				 } %>" method="post"  enctype="application/x-www-form-urlencoded">
					<input type="hidden" name="id" value="<%=JspPageHelper.getString("id", itemInfo) %>" />
					<div class="information bde mt10"  style="display:block;">
						<ul class="cm_parameter fz14">
							<li class="clearfix">
								<p>文章标题：</p>
								<input type="text" class="cm_title"  name="article_name" value="<%=JspPageHelper.getString("article_name", itemInfo) %>">
								<b class="colorr">*</b>
							</li>
							<li>
								<p>权重：</p>
								<input type="text" class="cm_title cm_weight" name="weight" value="<%=JspPageHelper.getString("weight", itemInfo) %>">
								<span class="colorb">（越小越靠前）</span>
							</li>
							<li>
								<p>缩略图：</p>
								<input type="text" name="thumb_pic"  class="cm_title cm_weight thumb_pic" value="<%=JspPageHelper.getString("thumb_pic", itemInfo) %>"  >
									<span class="fileinput-button cm_browse tac colorw" style="margin-top: 10px; float: left;" > 
								        <span>浏 览</span>
								        <input id="fileupload" type="file" name="file" >
								    </span>
								<span class="colorb">大小和格式限制</span>
							</li>
							<li>
								<p>文章分类：</p>
								<% if(itemInfo!=null){ %>
									<input type="hidden" name="old_cate_id" value="<%=JspPageHelper.getString("cate_id", itemInfo) %>" />
								<% } %>
								<select name="top_cate_id" id="top_cate_id">
									<option value="0">请选择分类</option>
									<%
										ArrayList<HashMap> levelOneCateList = (ArrayList)request.getAttribute("levelOneCateList");
									 	for(HashMap cateInfoTemp : levelOneCateList){
											if(request.getAttribute("haveTwoLevel")!=null){ //有子级分类
									%>
											<option value="<%=JspPageHelper.getString("id", cateInfoTemp) %>" <% if(cateInfoTemp.get("id").toString().equals(request.getAttribute("levelOneCateId").toString())){ %>selected="selected"<% } %>><%=JspPageHelper.getString("cate_name", cateInfoTemp) %></option>
											<%}else{ %>
											<option value="<%=JspPageHelper.getString("id", cateInfoTemp) %>" <% if(itemInfo!=null&&cateInfoTemp.get("id").toString().equals(itemInfo.get("cate_id").toString())){ %>selected="selected"<% } %> ><%=JspPageHelper.getString("cate_name", cateInfoTemp) %></option>
									<%
											}
										}
									 %>
								</select>
								<b class="colorr">*</b>
							</li>
							<li class="subCateContainer" <% if(request.getAttribute("levelTwoCateList")==null){ %>style="display: none;"<% } %> >
								<% if(request.getAttribute("levelTwoCateList")!=null){ //没有子级分类  %>
									<p>二级分类：</p>
									<select name="cate_id"  id="cate_id">
										<%
											ArrayList<HashMap> levelTwoCateList = (ArrayList)request.getAttribute("levelTwoCateList");
										 	for(HashMap cateInfoTemp : levelTwoCateList){
										%>
											<option value="<%=JspPageHelper.getString("id", cateInfoTemp) %>" <% if(cateInfoTemp.get("id").toString().equals(itemInfo.get("cate_id").toString())){ %>selected="selected"<% } %> ><%=JspPageHelper.getString("cate_name", cateInfoTemp) %></option>
										<%  } %>
									</select>
									<b class="colorr">*</b>
								<% } %>
							</li>
						</ul>
						<div class="cm_content fz14">
							<p>文章内容：</p>
							<div class="cm_kindeditor" style="height: auto;border-bottom: none;">
								<textarea  name="content" style="width:803px;height:300px;" ><%=JspPageHelper.getString("content", itemInfo) %></textarea>
							</div>
						</div>
						<div class="sm_btn">
							<input type="submit" value="确定">		
							<input type="reset" value="重置">	
						</div>
					</div>
				</form>
		</div>		
			<script type="text/javascript">
					//富文本框插件
					var editor;
					KindEditor.ready(function(K) {
						editor = K.create('textarea[name="content"]', {
							autoHeightMode : true,
							afterCreate : function() {
								this.loadPlugin('autoheight');
								}
							});
						});
					
					$(document).ready(function() {
						$("#top_cate_id").change(function() {
							var cateId = $("option:selected",this).val();
							var postUrl = '<%=UrlHelper.url("admin", "article-cate", "ajaxGetSubCateList")%>';
							$.post(postUrl+"&id="+cateId,{},function(data){
								if(data.status==1)
								{
									var subCateList = data.data.subCateList;
									if(subCateList.length>0){
										var htmlTemp = '<p>二级分类：</p><select name="cate_id"  id="cate_id">';
										for(var index in subCateList)
										{
											var itemTemp = subCateList[index];
											htmlTemp += '<option value="'+itemTemp['id']+'">'+itemTemp['cate_name']+'</option>';
										}
										htmlTemp = htmlTemp+'</select><b class="colorr">*</b>';
									    $(".subCateContainer").html(htmlTemp);
									}
									else{
										var htmlTemp = "";
										htmlTemp = '<p>二级分类：</p><select id="cate_id" name="cate_id" disabled="disabled"><option selected="selected" value="'+cateId+'">没有二分类</option></select>';
										$(".subCateContainer").html(htmlTemp);
									}
								}
								else
								{
									var htmlTemp = "";
									htmlTemp = '<p>二级分类：</p><select id="cate_id" name="cate_id" disabled="disabled"><option selected="selected" value="'+cateId+'">没有二分类</option></select>';
									$(".subCateContainer").html(htmlTemp);
								}
							}, "json");
							$(".subCateContainer").show();
							return false;
						});
						
						//添加自定义校验方法
						$.validator.addMethod("editorContentNeed",function(value, element){
							var editorText = editor.text();
							return $.trim( editorText ).length > 0;
						},"请输入文章内容！");
						
						//配置校验规则，和错误信息
						$("#formTemp").validate({
							ignore: "",
							rules: {
								article_name: {
									required: true,
									minlength: 2
								},
								weight: {
									digits: true
								},
								thumb_pic: "required",
								top_cate_id: {
									required: true,
									min:1
								},
								content: {
									editorContentNeed: true
								}
							},
							messages: {
								article_name: {
									required: "请输入文章标题！",
									minlength: "文章标题最少要两个字符！"
								},
								weight: {
									digits: "必须是阿拉伯数字！",
								},
								thumb_pic: "请上传图片！",
								top_cate_id: {
									required:"请选择分类！",
									min:"请选择分类！",
								},
							}
						});
						
						$("#formTemp").submit( function () {
							var valid = $("#formTemp").valid();
							if(!valid)
							{
								return false;
							}
							return true;
						});
						
					});
					
					
					 $('#fileupload').fileupload({
					        url: '<%=UrlHelper.url("admin", "article-cate", "upload")%>',
					        dataType: 'json',
					        formData: {'is_upload': '1'},
					        done: function (e, data) {
					        	if(!Utils.empty(data.result))
					        	{
					        		var temp = data.result;
					        		if(temp.status==1)
					        		{
					        			var picInfo = temp.data;
					        			var fullPicUrl = "";
					        			if(!Utils.empty(picInfo.homeUrl))
					        			{
					        				fullPicUrl = picInfo.homeUrl + picInfo.picSrc;
					        			}
					        			else
					        			{
					        				fullPicUrl = "." + picInfo.picSrc;
					        			}
					        			$("input[name='thumb_pic']").val(picInfo.picSrc);
					        		}
					        		else
					        		{
					        			alert(temp.info);	
					        		}
					        	}
					        	
					        },
					    });
				</script>
<%@ include file="/WEB-INF/servletView/admin/Common-footer.jsp" %>