package cn.java.jfinal.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * http://localhost/testServlet/?username=zhouzhian
 */
public class Test1Servlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String usernameTemp = request.getParameter("username");
		request.setAttribute("username", usernameTemp);

		// InputStream
//		ServletInputStream servletInputStream = request.getInputStream();
//		byte[] byteBufferTemp = new byte[1024];
//		int numByteInBuffer = servletInputStream.read(byteBufferTemp);
//		StringBuilder stringBuilder = new StringBuilder();
//		System.out.println("numByteInBuffer=" + numByteInBuffer);
//		while (numByteInBuffer != -1) {
//			stringBuilder.append(new String(byteBufferTemp));
//
//		}
//		System.out.println(stringBuilder.toString());

		request.getRequestDispatcher("/test2Servlet?username=zhouzhian").forward(request, response);//内部转发
		
		//输出
//		PrintWriter out = response.getWriter();
//		out.write("<html><head><body>");
//		out.write("username:" + usernameTemp + "<br />");
//		out.write("</body></head></html>");
//
//		PrintWriter printWriter = response.getWriter();
//		printWriter.write("hello");
//		printWriter.flush();
//		printWriter.close();

	}

}
