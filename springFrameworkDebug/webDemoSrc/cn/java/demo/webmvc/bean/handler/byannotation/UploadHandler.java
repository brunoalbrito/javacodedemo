package cn.java.demo.webmvc.bean.handler.byannotation;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.springframework.util.MultiValueMap;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import cn.java.demo.web.util.WebUtilx;


/**
 * 处理上传
 * @author zhouzhian
 *
 */
@RequestMapping(
	path={"/upload-handler"},
	method={RequestMethod.GET,RequestMethod.POST}
)
public class UploadHandler {
	
	private String getSaveFileName(HttpServletRequest request,String originalFilename) throws Exception {
		// 文件名
		String fileName = "";
		{
			fileName = UUID.randomUUID().toString();
			if(originalFilename.lastIndexOf(".")>0){
				String extension = originalFilename.substring(originalFilename.lastIndexOf(".")+1,originalFilename.length());
				fileName = fileName+"."+ extension;
			}
		}
		
		// 保存路径
		String saveDirRealpath = request.getServletContext().getResource("/upload").getFile();
		File saveDirRealpathFile = new File(saveDirRealpath);
		if(saveDirRealpathFile.exists() && saveDirRealpathFile.isDirectory()){
			return saveDirRealpath+"/"+fileName;
		}
		else{
			if(!saveDirRealpathFile.mkdirs()){
				throw new RuntimeException(saveDirRealpath + " dir make failure.");
			}
			return saveDirRealpath+"/"+fileName;
		}
	}
	
	/**
	 * 文件上传
	 * /upload-handler/upload0
	 */
	@RequestMapping(path={"/upload0"},method={RequestMethod.POST,RequestMethod.GET})
	public String upload0(HttpServletRequest request,HttpServletResponse response) throws Exception{ 
		if(!request.getMethod().equals("POST")){
			return "upload-handler/upload0";
		}
		else{
			// “非上传文件字段”的获取方式
			{
				request.getParameter("param0"); // 
				System.out.println("request.getParameter(\"param0\") = " + request.getParameter("param0"));
			}
			
			// “上传文件字段”的获取方式
			if(request instanceof StandardMultipartHttpServletRequest){
				StandardMultipartHttpServletRequest multipartRequest = (StandardMultipartHttpServletRequest)request;
				// 处理文件上传列表
				MultiValueMap<String, MultipartFile> multiFileMap = multipartRequest.getMultiFileMap();
				
				// 多值
				{
					for (Entry<String, List<MultipartFile>> entry : multiFileMap.entrySet()) {
						entry.getKey();
						if(entry.getValue().size()>0){
							MultipartFile multipartFile = entry.getValue().get(0);
							if(!"".equals(multipartFile.getOriginalFilename().trim())){ // 有文件名，才有文件内容
//								multipartFile.getInputStream(); // 上传文件的输入流
								// multipartFile.getOriginalFilename() = 新建文本文档.txt ,multipartFile.getName() = filename0
								String originalFilename = multipartFile.getOriginalFilename();
								String saveFileName = getSaveFileName(request,originalFilename);
								System.out.println("saveFileName = " + saveFileName);
								StreamUtils.copy(multipartFile.getInputStream(),new FileOutputStream(new File(saveFileName)));
								System.out.println("multipartFile.getOriginalFilename() = " + multipartFile.getOriginalFilename() + " ,multipartFile.getName() = " + multipartFile.getName());
							}
						}
					}
				}

				// 单值
				{
					Map<String, MultipartFile> singleValueMap = multiFileMap.toSingleValueMap();
					for (Entry<String, MultipartFile> entry : singleValueMap.entrySet()) {
						entry.getKey();
						MultipartFile multipartFile = entry.getValue();
						if(!"".equals(multipartFile.getOriginalFilename().trim())){ // 有文件名，才有文件内容
							System.out.println("multipartFile.getOriginalFilename() = " + multipartFile.getOriginalFilename() + " ,multipartFile.getName() = " + multipartFile.getName());
//							multipartFile.getInputStream();
						}
					}
				}
			}
			return "upload-handler/upload0";
		}
		
	}
	
	/**
	 * 文件上传 - 原生的上传方式
	 * /upload-handler/upload1
	 */
	@RequestMapping(path={"/upload1"},method={RequestMethod.POST,RequestMethod.GET})
	public String upload1(HttpServletRequest request,HttpServletResponse response) throws Exception{ 
		if (("post".equals(request.getMethod().toLowerCase())) && ((request.getContentType() != null && request.getContentType().toLowerCase().startsWith("multipart/")))) {
			
			// “非上传文件字段”的获取方式
			{
				request.getParameter("param1"); // 
				System.out.println("request.getParameter(\"param1\") = " + request.getParameter("param1"));
			}
			
			// “上传文件字段”的获取方式
			{
				Collection<Part> parts = request.getParts();
				for (Part part : parts) {
					String originalFilename = WebUtilx.getSubmittedFileName(part); // part.getSubmittedFileName() 在不同平台下有兼容性的问题
					if (originalFilename != null) { // 是“文件上传字段”；其他普通变量还是通过request.getParameter("lname")获取
//						part.getSize(); // 文件大小
//						part.getInputStream(); // 文件输入流
//						part.getName(); // 字段名
//						part.delete(); // 删除临时目录的的文件
						String saveFileName = getSaveFileName(request,originalFilename);
						System.out.println("saveFileName = " + saveFileName);
						StreamUtils.copy(part.getInputStream(),new FileOutputStream(new File(saveFileName)));
						System.out.println("WebUtilx.getSubmittedFileName(part) = " + WebUtilx.getSubmittedFileName(part) + " ,part.getName() = " + part.getName());
					}
				}
			}
		}
		
		return "upload-handler/upload1";
	}
	

}
