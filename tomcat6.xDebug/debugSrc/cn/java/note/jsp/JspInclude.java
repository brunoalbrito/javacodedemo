package cn.java.note.jsp;

import javax.servlet.RequestDispatcher;
import javax.servlet.jsp.JspFactory;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import org.apache.jasper.runtime.ServletResponseWrapperInclude;

public class JspInclude {

	public static void main(String[] args) {
		/**
		
		//<jsp:include page="/WEB-INF/servletView/admin/Common-footer.jsp" flush="true"/>
		String fileFullPath = "/WEB-INF/servletView/admin/Common-footer.jsp";
		JspFactory mJspFactoryImpl = new org.apache.jasper.runtime.JspFactoryImpl();//JspFactory.getDefaultFactory();
		PageContext mPageContext = mJspFactoryImpl.getPageContext(this, request, response, null, true, 8192, true);
		//jsp页面中的out对象  
		JspWriter out = mPageContext.getOut();//org.apache.jasper.runtime.JspWriterImpl
		//jsp页面中的 response 对象  ServletResponseWrapperInclude
		
		
		// 方法一：页面中jsp:include
		org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response,fileFullPath, out, false);
		
		//方法二：
//		org.apache.catalina.connector.RequestFacade
//		org.apache.catalina.connector.ResponseFacade
//		mRequestDispatcher === org.apache.catalina.connector.Request.getRequestDispatcher(){
//			org.apache.catalina.core.StandardContext.getServletContext().getRequestDispatcher(fileFullPath){
//	    	org.apache.catalina.core.ApplicationContext.getRequestDispatcher(fileFullPath){
//	    		mRequestDispatcher = new ApplicationDispatcher(org.apache.catalina.core.StandardWrapper, uriCC.toString(), wrapperPath, pathInfo, queryString, null);
//	    	}
//		}
//		mRequestDispatcher.include(request, new ServletResponseWrapperInclude(response, out));
		RequestDispatcher mRequestDispatcher = request.getRequestDispatcher(fileFullPath);
		mRequestDispatcher.include(request, new ServletResponseWrapperInclude(response, out));// ApplicationDispatcher
		
		//在JSP页面中的  response =  new org.apache.jasper.runtime.ServletResponseWrapperInclude(response, out);
		 
		 
		*/
	}

}
