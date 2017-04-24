package cn.java.jfinal.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * http://localhost/test2Servlet?username=zhouzhian
 * http://localhost/test2Servlet/?username=zhouzhian
 * 
 */
public class Test2Servlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		//输出
//		PrintWriter out = response.getWriter();
//		out.write("<html><head><body>");
//		out.write("cn.java.jfinal.servlets.Test2Servlet<br />");
//		out.write("username:" + usernameTemp + "<br />");
//		out.write("</body></head></html>");
//		out.flush();
//		out.close();
		
		request.getRequestDispatcher("/WEB-INF/servletView/test2Servlet-doPost.jsp").forward(request, response);//内部转发
		//response.sendRedirect("/WEB-INF/servletView/test2Servlet-doPost.jsp");//重定向，流量器跳转，不能访问/WEB-INF目录
	}

}
