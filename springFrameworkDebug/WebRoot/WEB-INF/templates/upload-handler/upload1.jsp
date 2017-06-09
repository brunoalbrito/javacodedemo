<%@page import="cn.java.demo.web.util.WebUtilx"%>
<%@ include file="/WEB-INF/templates/common/common-header.jsp" %>

this is “upload-handler/upload1” template.<br />
<form action="<%=WebUtilx.getContextUrl(WebUtilx.getPathToServlet(request)+"/upload-handler/upload1", request, response)%>" method="post" enctype="multipart/form-data">
<input type="text" name="param1" value="param1Value">
<input type="file" name="filename1" />
<input type="submit" value="上传文件" />
</form>
<%@ include file="/WEB-INF/templates/common/common-footer.jsp" %>