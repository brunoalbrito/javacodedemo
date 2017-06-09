<%@page import="cn.java.demo.web.util.WebUtilx"%>
<%@ include file="/WEB-INF/templates/common/common-header.jsp" %>

this is “upload-handler/upload0” template.<br />
<form action="<%=WebUtilx.getContextUrl(WebUtilx.getPathToServlet(request)+"/upload-handler/upload0", request, response)%>" method="post" enctype="multipart/form-data">
<input type="text" name="param0" value="param0Value">
<input type="file" name="filename0" />
<input type="submit" value="上传文件" />
</form>
<%@ include file="/WEB-INF/templates/common/common-footer.jsp" %>