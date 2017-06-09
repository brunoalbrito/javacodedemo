package cn.java.demo.webmvc.bean.handler.byannotation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 处理上传
 * 
 * @author zhouzhian
 *
 */
@RequestMapping(path = { "/download-handler" }, method = { RequestMethod.GET, RequestMethod.POST })
public class DownloadHandler {

	/**
	 * /download-handler/download
	 * 文件下载
	 */
	@RequestMapping(path = { "/download" }, method = { RequestMethod.POST })
	public void download(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String fileName = "test.csv";
		
		{
			String fileRealpath = request.getServletContext().getResource("/download/"+fileName).getFile();
			response.setCharacterEncoding("utf-8");
			response.setContentType("multipart/form-data");
			response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
			try {
				File file = new File(fileRealpath);
				System.out.println(file.getAbsolutePath());
				InputStream inputStream = new FileInputStream(file);
				OutputStream os = response.getOutputStream();
				byte[] b = new byte[1024];
				int length;
				while ((length = inputStream.read(b)) > 0) {
					os.write(b, 0, length);
				}
				inputStream.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
