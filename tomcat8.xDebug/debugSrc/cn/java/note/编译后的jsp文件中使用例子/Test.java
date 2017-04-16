package cn.java.note.编译后的jsp文件中使用例子;

import org.apache.jasper.runtime.JspRuntimeLibrary;

public class Test {


	public static void testGetReadMethod() throws Exception {
		Class<?> bean = cn.java.note.bean.JspPageBean.class;
		String property = "username";
		java.lang.reflect.Method meth = JspRuntimeLibrary
                .getReadMethod(bean, property);
		String methodName = meth.getName();
	}
	
	public static void testToString() throws Exception {
		org.apache.jasper.runtime.JspRuntimeLibrary.toString("");
		
	}
	
	public static void main(String[] args) throws Exception {
		/*
		1. 执行EL表达式
			out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${param.username}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null));
			out.write("<br />\r\n");
			out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageScope.username}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null));
			out.write("<br />\r\n");
			out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${requestScope.username}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null));
			out.write("<br />\r\n");
			out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${sessionScope.username}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null));
			out.write("<br />\r\n");
			out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${applicationScope.username}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null));
			out.write("<br />\r\n");
			out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${header[\"User-Agent\"]}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null));
			out.write("<br />\r\n");
			out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${cookie.userCountry}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null));
			out.write("<br />\r\n");
			out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.method}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null));
		 2.<jsp:include>标签生成如下
			org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "/WEB-INF/jsp/common/menu.jsp" + "?" + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("param1", request.getCharacterEncoding())+ "=" + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("value1", request.getCharacterEncoding()), out, false);
		
		 3.<jsp:setProperty>标签生成如下
			org.apache.jasper.runtime.JspRuntimeLibrary.introspecthelper(_jspx_page_context.findAttribute("bean1InRequest"), "username", "this is username1", null, null, false);
			
		 4.<jsp:getProperty>标签生成如下
		 	out.write(org.apache.jasper.runtime.JspRuntimeLibrary.toString((((cn.java.note.bean.JspPageBean)_jspx_page_context.findAttribute("bean1InRequest")).getUsername())));
		 */
		
		
		
	}
}
