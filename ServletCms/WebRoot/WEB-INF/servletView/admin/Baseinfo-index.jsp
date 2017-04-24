<%@ include file="/WEB-INF/servletView/admin/Common-header.jsp" %><%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ page import="cn.java.core.helper.url.UrlHelper,cn.java.core.helper.url.JspPageHelper,java.util.HashMap" %>
<%if(!JspPageHelper.isEmpty("isClearCache", request)){ %>
	<div class="information cache">
			<p>如果您的网站出现与模板不同步或者其他显示异常问题，请尝试清除缓存</p>
			<a href="<%=UrlHelper.url("admin","baseinfo","clearcache","isClearCache=true") %>" type="button">清除缓存</a>
	</div>
<% }else{ %>
<form action="<%=UrlHelper.url("admin","baseInfo","index","") %>" method="post"  enctype="application/x-www-form-urlencoded">
<div class="information" style="display:block;">
	<br/>
	<ul class="sm_parameter">
		<li class="sm_project">
			<p class="sm_item">参数说明</p>
			<p>参数值</p>
		</li>
		<% HashMap configTemp = JspPageHelper.getHashMap("config", request); %>
		<li class="sm_logo clearfix">
			<p class="sm_item">公司LOGO</p>
			<div class="floatl clearfix">
				<input id="picPreviewInput" type="hidden" name="config.SITE_LOGO" value="<%=JspPageHelper.getString("SITE_LOGO", configTemp) %>" >
				<img src="<% if(JspPageHelper.isEmpty("SITE_LOGO", configTemp)){ %><%=UrlHelper.baseUrl() %>/assets/cms-admin/images/img.png <% }else{ %><%=JspPageHelper.thumbImgAuto(JspPageHelper.getString("SITE_LOGO", configTemp)) %><%} %> " alt="公司LOGO" id="picPreview">	
				  <label   class="bde disib  fileinput-button" style="overflow:visible; line-height: 30px; border: 0 none;"> 上传
					  <input id="fileupload" class="logoUploadBtn" type="file" name="file" >
			      </label>
			</div>
		</li>
		<li class="sm_bannerset clearfix">
			<p class="sm_item">Banner设置</p>
			<ol class="set_item_banner clearfix">
				<li class="clearfix bannerEntry">
					<input type="text" readonly="readonly" name="config.SITE_BANNER.0" value="<%=JspPageHelper.getString("SITE_BANNER.0", configTemp) %>" class="bannerEntryInput">
					<a href="javascript:void(0);" class="bde set_br_view"></a>
					<img src="<% if(JspPageHelper.isEmpty("SITE_BANNER.0", configTemp)){ %><%=JspPageHelper.thumbImgAuto(JspPageHelper.getString("SITE_BANNER.0", configTemp)) %><% } %>" alt="banner" class="hover_view">
					<label for="sFile1" class="fz12 fileinput-button">
						上传
					  	<input  class="mUploadBtnCls4Banner" type="file" name="file" style="width:30px;" >
					</label>
				</li>
				<li class="clearfix bannerEntry">
					<input type="text" readonly="readonly" name="config.SITE_BANNER.1" value="<%=JspPageHelper.getString("SITE_BANNER.1", configTemp) %>" class="bannerEntryInput">
					<a href="javascript:void(0);" class="bde set_br_view"></a>
					<img src="<% if(JspPageHelper.isEmpty("SITE_BANNER.1", configTemp)){ %><%=JspPageHelper.thumbImgAuto(JspPageHelper.getString("SITE_BANNER.1", configTemp)) %><% } %>" alt="banner" class="hover_view">
					<label for="sFile1" class="fz12 fileinput-button">
						上传
					  	<input  class="mUploadBtnCls4Banner" type="file" name="file" style="width:30px;" >
					</label>
				</li>
				<li class="clearfix bannerEntry">
					<input type="text" readonly="readonly" name="config.SITE_BANNER.2"  value="<%=JspPageHelper.getString("SITE_BANNER.2", configTemp) %>" class="bannerEntryInput">
					<a href="javascript:void(0);" class="bde set_br_view"></a>
					<img src="<% if(JspPageHelper.isEmpty("SITE_BANNER.2", configTemp)){ %><%=JspPageHelper.thumbImgAuto(JspPageHelper.getString("SITE_BANNER.2", configTemp)) %><% } %>" alt="banner" class="hover_view">
					<label for="sFile1" class="fz12 fileinput-button">
						上传
					  	<input  class="mUploadBtnCls4Banner" type="file" name="file" style="width:30px;" >
					</label>
					<!-- 点击添加新增一个banner 
						<a href="javascript:void(0);" class="set_br_add">添加</a>
					-->
				</li>
			</ol>
		</li>
		<li>
			<p class="sm_item">网站标题</p>
			<p><input type="text" name="config.SITE_TITLE" value="<%=JspPageHelper.getString("SITE_TITLE", configTemp) %>" ></p>
		</li>
		<li>
			<p class="sm_item">站点关键字</p>
			<p><input type="text" name="config.SITE_KEYWORDS" value="<%=JspPageHelper.getString("SITE_KEYWORDS", configTemp) %>"></p>
		</li>
		<li>
			<p class="sm_item">站点描述</p>
			<p><input type="text" name="config.SITE_DESCRIPTION" value="<%=JspPageHelper.getString("SITE_DESCRIPTION", configTemp) %>"></p>
		</li>
		<li>
			<p class="sm_item">页脚版本信息</p>
			<p><input type="text" name="config.SITE_COPYRIGHT" value="<%=JspPageHelper.getString("SITE_COPYRIGHT", configTemp) %>"></p>
		</li>
		<li>
			<p class="sm_item">网站类型</p>
			<p><input type="text" name="config.SITE_TYPE" value="<%=JspPageHelper.getString("SITE_TYPE", configTemp) %>"></p>
		</li>
		<li>
			<p class="sm_item">网站备案号</p>
			<p><input type="text" name="config.SITE_RECORD" value="<%=JspPageHelper.getString("SITE_RECORD", configTemp) %>"></p>
		</li>
		<li>
			<p class="sm_item">邮件服务器</p>
			<p><input type="text" name="config.SMTP_HOSTS" value="<%=JspPageHelper.getString("SMTP_HOSTS", configTemp) %>"></p>
		</li>
		<li>
			<p class="sm_item">邮箱用户名</p>
			<p><input type="text" name="config.SMTP_USERNAME" value="<%=JspPageHelper.getString("SMTP_USERNAME", configTemp) %>"></p>
		</li>
		<li>
			<p class="sm_item">邮箱密码</p>
			<p><input type="text" name="config.SMTP_PASSWORD" value="<%=JspPageHelper.getString("SMTP_PASSWORD", configTemp) %>"></p>
		</li>
	</ul>
	<div class="sm_btn">
		<input type="submit" value="确定">		
		<input type="reset" value="重置">	
	</div>
</div>
</form>
<%} %>
<link rel="stylesheet" type="text/css" href="<%=UrlHelper.baseUrl() %>/assets/fileupload/css/jquery.fileupload.css" />
<script type="text/javascript" src="<%=UrlHelper.baseUrl() %>/assets/fileupload/js/vendor/jquery.ui.widget.js"></script>
<script type="text/javascript" src="<%=UrlHelper.baseUrl() %>/assets/fileupload/js/jquery.iframe-transport.js"></script>
<script type="text/javascript" src="<%=UrlHelper.baseUrl() %>/assets/fileupload/js/jquery.fileupload.js"></script>
<script type="text/javascript">
//预览
$(document).on('mouseover','.set_br_view',function(){
	$(this).next('img').show();
	$(document).on('mouseout','.hover_view',function(){
		$(this).hide();
	})
});
  $('#fileupload,.mUploadBtnCls4Banner').fileupload({
      url: '<%=UrlHelper.url("admin","baseInfo","index","") %>',
      dataType: 'json',
      formData: {'is_upload': '1'},
      done: function (e, data) {
      	if(!Utils.empty(data.result))
      	{
      		var isBannerPic = false;
      		var inputBtn  = $(e.target);
      		if(inputBtn.attr('class').indexOf("mUploadBtnCls4Banner")!=-1)
      		{
      			isBannerPic = true;
      		}
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
      				fullPicUrl = picInfo.picSrc;
      			}
      			if(isBannerPic)
      			{
      				inputBtn.parents(".bannerEntry").find(".hover_view").attr("src",fullPicUrl);
      				inputBtn.parents(".bannerEntry").find(".bannerEntryInput").val(picInfo.picSrc);
      			}
      			else{
      				$("#picPreview").attr("src",fullPicUrl);
	      			$("#picPreviewInput").val(picInfo.picSrc);	
      			}
      		}
      		else
      		{
      			$.toast({'message':temp.info}).show();
      		}
      	}
      },
  });
</script>
<%@ include file="/WEB-INF/servletView/admin/Common-footer.jsp" %>