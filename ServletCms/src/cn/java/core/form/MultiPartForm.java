package cn.java.core.form;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.eclipse.jetty.util.security.Credential.MD5;

import cn.java.core.helper.encrypt.MD5Util;

public class MultiPartForm {

	public static void debug(String msg){
		System.out.println(msg);
	}
	protected static boolean isRandomFilename = true;
	public static FieldHashMap parseForm(HttpServletRequest request, String tempPath, String saveDirPath) {
		FieldHashMap fieldHashMap = new FieldHashMap();
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
			List<FileItem> fileItems = upload.parseRequest(new ServletRequestContext(request));

			Iterator<FileItem> iter = fileItems.iterator();
			while (iter.hasNext()) {
				FileItem item = (FileItem) iter.next();
				if (item.isFormField()) {//如果获取的 表单信息是普通的 文本 信息  
					debug("处理表单内容(字段) ...");
					String name = item.getFieldName();
					String value = item.getString();
					debug("fileName:" + name + " value:" + value);
					fieldHashMap.put(name, value);
				} else {//对传入的非 简单的字符串进行处理 ，比如说二进制的 图片，电影这些  
					debug("..............处理上传的文件..............");
					String name = item.getFieldName();
					String filename = item.getName();// 此时的文件名包含了完整的路径，得注意加工一下
					int index = filename.lastIndexOf("\\");
					filename = filename.substring(index + 1, filename.length());
					int dotIndex = filename.lastIndexOf(".");
					String fileExt = "";
					if(dotIndex!=-1){
						fileExt = "."+filename.substring(dotIndex + 1, filename.length());
					}
					long fileSize = item.getSize();//文件大小
					if ("".equals(filename) && fileSize == 0) {
						debug("文件名为空 ...");
					} else {
						debug("完整的文件名：" + filename);
						if(isRandomFilename){
//							filename = UUID.randomUUID().toString();
							filename = filename + System.currentTimeMillis();
							filename = MD5Util.string2MD5(filename)+fileExt;
						}
						File uploadFile = new File(saveDirPath + "/" + filename);
						if (!uploadFile.exists()) {
							uploadFile.createNewFile();//创建目标文件
						}
						item.write(uploadFile);//把数据写入目标文件
						debug(filename + " 文件保存完毕 ...");
						debug("文件大小为 ：" + fileSize + "\r\n");
					}
					fieldHashMap.put(name,filename);
				}
			}// end while()
			debug(fieldHashMap.toString());
		} catch (Exception e) {
			debug("使用 fileupload 包时发生异常 ...");
			e.printStackTrace();
		}
		return fieldHashMap;
	}

}
