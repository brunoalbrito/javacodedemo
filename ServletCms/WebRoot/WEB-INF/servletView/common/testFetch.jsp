<%@ page import="java.io.PrintWriter"%>
<%@ page import="org.apache.jasper.runtime.JspWriterImpl" %>
<%@ page import="org.apache.jasper.runtime.ServletResponseWrapperInclude" %>
<html><head></head><body>
<div class="hellClass">xxxx</div>
<% out.write("111"); %>
<% out.write("222");  /*    if(true) throw Exception("333"); */%>
<%="333" %>
<%-- <jsp:include page="/WEB-INF/servletView/admin/Common-footer.jsp" flush="true"/> --%>
<%
if(response instanceof ServletResponseWrapperInclude){//真
	out.write("response is instanceof ServletResponseWrapperInclude ;"); 
}
if(out instanceof JspWriterImpl){ //真
	out.write("out is instanceof JspWriterImpl ;"); 
}
%>
</body></html>
<%
/* 
	out.write("<html><head></head><body>\r\n");
	out.write("<div class=\"hellClass\">xxxx</div>\r\n");
	out.write("111"); 
	out.write('\r');
	out.write('\n');
	response.getWriter().write("222"); if(true) throw Exception("333"); 
	out.write('\r');
	out.write('\n');
	out.print("333" );
	out.write('\r');
	out.write('\n');
	org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "/WEB-INF/servletView/admin/Common-footer.jsp", out, true);
	out.write("\r\n");
	out.write("</body></html>"); 
*/
%>