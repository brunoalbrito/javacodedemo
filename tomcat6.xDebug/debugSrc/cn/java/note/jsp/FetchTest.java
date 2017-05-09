package cn.java.note.jsp;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Wrapper;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspFactory;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import org.apache.catalina.util.RequestUtil;
import org.apache.jasper.security.SecurityClassLoad;
import org.apache.tomcat.util.buf.CharChunk;
import org.apache.tomcat.util.buf.MessageBytes;
import org.apache.tomcat.util.http.mapper.MappingData;

public class FetchTest extends HttpServlet {

	public static void main(String[] args) {
		//org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "date.jsp", out, true);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/index.jsp").forward(request, response);
		
		
//		request.getRequestDispatcher("/index.jsp").include(request, response);
		
	}

	protected void doPostTest(HttpServletRequest request, HttpServletResponse response) throws IOException {
		PrintWriter out =  response.getWriter();
		out.write("输出内容1");
		out.write("输出内容2");
		out.write("输出内容3");
		testJspRuntimeInclude(request, response,"/index.jsp",out);
		out.write("输出内容4");
		out.write("输出内容5");
		out.write("输出内容6");
	}
	
	protected void testJspRuntimeInclude(HttpServletRequest request, HttpServletResponse response,String path,PrintWriter out){
		out.write("要被引入的页面");
	}
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	protected void fetch(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		//JSP工厂
		org.apache.jasper.runtime.JspFactoryImpl factory = new org.apache.jasper.runtime.JspFactoryImpl();
		SecurityClassLoad.securityClassLoad(factory.getClass().getClassLoader());
		JspFactory.setDefaultFactory(factory);

		JspFactory jspxFactory = JspFactory.getDefaultFactory(); // JspFactoryImpl
		PageContext pageContext = jspxFactory.getPageContext(this, request, response, null, false, 8192, true);//org.apache.jasper.runtime.PageContextImpl
		JspWriter out = pageContext.getOut();//org.apache.jasper.runtime.JspWriterImpl
		org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "date.jsp", out, true);
		
		
		//展开如下
//		org.apache.catalina.connector.RequestFacade
		//org.apache.catalina.core.ApplicationContext.getRequestDispatcher(...)
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("/index.jsp");
		requestDispatcher.include(request, response);
		//requestDispatcher.include(request, new ServletResponseWrapperInclude(response, out));
	}

	protected RequestDispatcher getRequestDispatcher(String path) {
		return null;
//		RequestDispatcher requestDispatcher = null;
//		// Validate the path argument
//		if (path == null)
//			return (null);
//		if (!path.startsWith("/"))//必须以 “/” 开头
//			throw new IllegalArgumentException("applicationContext.requestDispatcher.iae");
//
//		// Get query string
//		String queryString = null;
//		int pos = path.indexOf('?');
//		if (pos >= 0) {// /index.jsp?key1=value1
//			queryString = path.substring(pos + 1);
//			path = path.substring(0, pos);
//		}
//
//		path = RequestUtil.normalize(path);
//		if (path == null)
//			return (null);
//
//		pos = path.length();
//		DispatchData dispatchData = new DispatchData();
//		MessageBytes uriMB = dispatchData.uriMB;
//		uriMB.recycle();
//		MappingData mappingData = dispatchData.mappingData;
//		CharChunk uriCC = uriMB.getCharChunk();
//		try {
//			uriCC.append(context.getPath(), 0, context.getPath().length());
//			/*
//			 * Ignore any trailing path params (separated by ';') for mapping
//			 * purposes
//			 */
//			int semicolon = path.indexOf(';');
//			if (pos >= 0 && semicolon > pos) {
//				semicolon = -1;
//			}
//			uriCC.append(path, 0, semicolon > 0 ? semicolon : pos);
//			context.getMapper().map(uriMB, mappingData);
//			if (mappingData.wrapper == null) {
//				return (null);
//			}
//			/*
//			 * Append any trailing path params (separated by ';') that were
//			 * ignored for mapping purposes, so that they're reflected in the
//			 * RequestDispatcher's requestURI
//			 */
//			if (semicolon > 0) {
//				uriCC.append(path, semicolon, pos - semicolon);
//			}
//		} catch (Exception e) {
//			return (null);
//		}
//
//		Wrapper wrapper = (Wrapper) mappingData.wrapper;
//		String wrapperPath = mappingData.wrapperPath.toString();
//		String pathInfo = mappingData.pathInfo.toString();
//
//		mappingData.recycle();
//		return new ApplicationDispatcher(wrapper, uriCC.toString(), wrapperPath, pathInfo, "key1=value1&key2=value2", null);
	}

}
