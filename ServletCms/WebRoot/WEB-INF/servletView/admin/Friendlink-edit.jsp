<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ page import="cn.java.core.helper.url.UrlHelper,cn.java.core.helper.url.JspPageHelper,java.util.HashMap,java.util.ArrayList" %>
<div class="layer_editor">
 <% HashMap item = (HashMap)request.getAttribute("item"); %>
 <form id="formTemp" enctype="application/x-www-form-urlencoded" method="post" action="<%=UrlHelper.url("admin","friendlink", "edit") %>&id=<%=item.get("id") %>">
    <input type="hidden" value="<%=item.get("id") %>" name="id">
    <div> <span>网址：</span> <input type="text" value="<%=item.get("site_url") %>" name="site_url"> </div>
    <div> <span>网站名称：</span> <input type="text" value="<%=item.get("site_name") %>" name="site_name">  </div>
    <div class="layer_ed_img"> <span>网站logo：</span>
    	   <input type="text" value="<%=item.get("site_logo") %>" name="site_logo">
	      <label   class="bde disib cm_browse fileinput-button" style="overflow:visible;"> 浏 览
			  <input id="fileupload" type="file" name="file" >
	      </label>
	      <p class="fz14">（限制大小和格式）</p>
	      <img id="picPreview" src="<%=JspPageHelper.thumbImgAuto(item.get("site_logo").toString()) %>" alt="图片预览" class="layer_ed_logo fz12"> 
    </div>
    <div class="layer_position"> <span>排列位置：</span> <input type="text" value="<%=item.get("sort_order") %>" name="sort_order"> 
      &nbsp; <strong class="fz12">从数字1开始排列</strong> </div>
  </form>
  
  <script type="text/javascript">
	  $('#fileupload').fileupload({
	      url: '<%=UrlHelper.url("admin","friendlink", "upload") %>',
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
	      			$("#picPreview").attr("src",fullPicUrl);
	      			$("input[name='site_logo']").val(picInfo.picSrc);
	      		}
	      		else
	      		{
	      			$.toast({'message':temp.info}).show();
	      		}
	      	}
	      },
	  });
  </script>
</div>