<%@ page language="java"  pageEncoding="UTF-8"%>
<!-- <form action="/admin/test/?act=upload" method="post" enctype="multipart/form-data"> -->
<form action="/admin/test/?act=test" method="post">
	-------------text---------<br/>
	<input type="text" name="username" value="zhouzhianz"><br/>
	<input type="text" name="list" value="1"><br/> 
	<input type="text" name="list" value="2"><br/>
	<input type="text" name="list" value=""><br/>
	-------------text---------<br/>
	<input type="text" name="config.SITE_LOGO" value="SITE_LOGO_XXX"><br/>
	<input type="text" name="config.SITE_BANNER.0" value="config.SITE_BANNER.0XXX"><br/>
	<input type="text" name="config.SITE_BANNER.1" value="config.SITE_BANNER.1XXX"><br/>
	<input type="text" name="config.SITE_BANNER.2" value="config.SITE_BANNER.2XXX"><br/>
	-------------radio---------<br/>
	<input type="radio" name="sex" value="1" checked="checked">男
	<input type="radio" name="sex" value="2">女<br/>
	-------------checkbox---------<br/>
	<input type="checkbox" name="like1" value="1" checked="checked" />喜好1<br/>
	<input type="checkbox" name="like1" value="2" checked="checked" />喜好2<br/>
	<input type="checkbox" name="like1" value="3" />喜好3<br/>
	<input type="checkbox" name="like1" value="4" checked="checked" />喜好4<br/>
	-------------select---------<br/>
	<select name="age">
		<option value="1">1</option>
		<option value="1">2</option>
		<option value="1">3</option>
	</select><br/>
	-------------file---------<br/>
	<input type="file" name="file"><br/>
	
	<input type="submit" value="提交" /><br/>
	<img id="imgObj"  src="/admin/test/?act=captcha"/> 
	<a href="#" onclick="changeImg()">换一张</a>   
</form>
<script type="text/javascript">
 function changeImg(){     
    var imgSrc = document.getElementById("imgObj");    
    var timestamp = (new Date()).valueOf();
    var newSrc = '/admin/test/?act=captcha&timestamp='+timestamp;//为了使每次生成图片不一致，即不让浏览器读缓存，所以需要加上时间戳    
    imgSrc.setAttribute("src",newSrc);
}     
</script>  
<%=request.getAttribute("pagination") %>
<style type="text/css">
.current{font-weight: bold;}
</style>
