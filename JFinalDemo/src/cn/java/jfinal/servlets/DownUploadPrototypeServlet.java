package cn.java.jfinal.servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DownUploadPrototypeServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse response)
			throws ServletException, IOException {
		OutputStream o = response.getOutputStream();
		byte b[] = new byte[1024];
		// the file to download.  服务器的中的文件
		File fileLoad = new File("d:/temp", "test.rar");
		// the dialogbox of download file.  用户下载的文件名
		response.setHeader("Content-disposition", "attachment;filename="
				+ "test.rar");
		// set the MIME type.  下载的文件类型
		response.setContentType("application/x-tar");

		// get the file length. 文件大小
		long fileLength = fileLoad.length();
		String length = String.valueOf(fileLength);
		response.setHeader("Content_Length", length);
		// download the file.
		FileInputStream in = new FileInputStream(fileLoad);
		int n = 0;
		while ((n = in.read(b)) != -1) {
			o.write(b, 0, n);//输出流
		}
	}
}
