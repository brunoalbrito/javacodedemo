<%@ page import="java.io.PrintWriter" pageEncoding="UTF-8" %>

<%
response.setHeader("Content-type", "text/html; charset=utf-8");
/* 
PrintWriter printWriter = response.getWriter();
printWriter.write("打印输出...");
printWriter.flush();
printWriter.close();
*/
out.write("打印输出...");
%>
<br />
我是模板
<br />
<%
	//case A
	String something = request.getParameter("something");
	if (something != null) {
		String str = new String(something.getBytes("ISO-8859-1"),
				"utf-8");
	}
	//case B
	request.setCharacterEncoding("UTF-8");
	String str2 = request.getParameter("something");

	//case C
	String tplDataTest = (String) request.getAttribute("tplDataTest");
	if (tplDataTest != null) {
		out.write("tplDataTest: " + tplDataTest);
	}
%>
