<%@ page language="java" pageEncoding="utf-8"%>
<% //@ page contentType="text/html; charset=utf-8" %>
<% response.setHeader("Content-type", "text/html; charset=utf-8"); %>
<html>
<head>
<title>Servlet的 mvc</title>
</head>
<body>
	<form name="uploadForm" method="post" enctype="multipart/form-data"
		action="/uploadServlet">
		Name:<input type="text" name="username" /> <br /> File1:<input
			type="file" name="file1" /> <br /> File2:<input type="file"
			name="file2" /> <br /> <input type="submit" name="submit" value="上传">
		<input type="reset" name="reset" value="重置">
	</form>
</body>
</html>