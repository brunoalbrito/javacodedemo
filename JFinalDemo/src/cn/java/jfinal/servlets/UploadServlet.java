package cn.java.jfinal.servlets;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;

/**
 * http://blog.csdn.net/hzc543806053/article/details/7524491
 * 
 * @author Administrator
 *
 */
public class UploadServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String filePath; // 文件存放目录
	private String tempPath; // 临时文件目录

	/**
	 * 初始化配置参数
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		// 从配置文件中获得初始化参数
		filePath = config.getInitParameter("filepath");
		tempPath = config.getInitParameter("temppath");

		ServletContext context = getServletContext();

		filePath = context.getRealPath(filePath);
		tempPath = context.getRealPath(tempPath);

		// 如果路径不存在，则创建路径
		File pathFile = new File(filePath);
		File pathTemp = new File(tempPath);
		if (!pathFile.exists()) {
			pathFile.mkdirs();
		}
		if (!pathTemp.exists()) {
			pathTemp.mkdirs();
		}
		System.out.println("文件存放目录、临时文件目录准备完毕 ...");
	}

	/**
	 * 执行上传
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws IOException, ServletException {
		
		req.setCharacterEncoding("utf-8");  //设置编码  
		
		PrintWriter pw = res.getWriter();
		try {
			DiskFileItemFactory diskFactory = new DiskFileItemFactory();
			
			//如果没以下两行设置的话，上传大的 文件 会占用 很多内存， 
			// threshold 极限、临界值，即硬盘缓存 1G
			diskFactory.setSizeThreshold(1000 * 1024 * 1024);
			// repository 贮藏室，即临时文件目录
			diskFactory.setRepository(new File(tempPath));

			//高水平的API文件上传处理  
			ServletFileUpload upload = new ServletFileUpload(diskFactory);
			// 设置允许上传的最大文件大小 1G
			upload.setSizeMax(1000 * 1024 * 1024);
			// 解析HTTP请求消息头
			List<FileItem> fileItems = upload
					.parseRequest(new ServletRequestContext(req));
			
			Iterator<FileItem> iter = fileItems.iterator();
			while (iter.hasNext()) {
				FileItem item = (FileItem) iter.next();
				//如果获取的 表单信息是普通的 文本 信息  
				if (item.isFormField()) {
					System.out.println("处理表单内容 ...");
					processFormField(item, pw);
					
//					//获取表单的属性名字  
//	                String name = item.getFieldName();  
//					//获取用户具体输入的字符串 
//                    String value = item.getString() ; 
//                    req.setAttribute(name, value);  
					
				} else {//对传入的非 简单的字符串进行处理 ，比如说二进制的 图片，电影这些  
					System.out.println("处理上传的文件 ...");
					processUploadFile(item, pw);
				}
			}// end while()

			pw.close();
		} catch (Exception e) {
			System.out.println("使用 fileupload 包时发生异常 ...");
			e.printStackTrace();
		}// end try ... catch ...
	}// end doPost()

	/**
	 * 处理表单内容
	 * @param item
	 * @param pw
	 * @throws Exception
	 */
	private void processFormField(FileItem item, PrintWriter pw)
			throws Exception {
		String name = item.getFieldName();
		String value = item.getString();
		pw.println(name + " : " + value + "\r\n");
	}

	/**
	 * 处理上传的文件
	 * @param item
	 * @param pw
	 * @throws Exception
	 */
	private void processUploadFile(FileItem item, PrintWriter pw)
			throws Exception {
		// 此时的文件名包含了完整的路径，得注意加工一下
		String filename = item.getName();
		System.out.println("完整的文件名：" + filename);
		int index = filename.lastIndexOf("\\");
		filename = filename.substring(index + 1, filename.length());

		long fileSize = item.getSize();

		if ("".equals(filename) && fileSize == 0) {
			System.out.println("文件名为空 ...");
			return;
		}

		File uploadFile = new File(filePath + "/" + filename);
		if (!uploadFile.exists()) {
			uploadFile.createNewFile();
		}
		item.write(uploadFile);
		pw.println(filename + " 文件保存完毕 ...");
		pw.println("文件大小为 ：" + fileSize + "\r\n");
	}

	/**
	 * 上传页面
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		request.getRequestDispatcher("/WEB-INF/servletView/uploadServlet-doGet.jsp").forward(request, response);//内部转发
	}

}
