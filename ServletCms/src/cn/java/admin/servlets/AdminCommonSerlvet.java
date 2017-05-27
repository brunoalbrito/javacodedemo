package cn.java.admin.servlets;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSONObject;

import magick.MagickException;
import cn.java.app.config.ApplicationConfig;
import cn.java.core.form.FieldHashMap;
import cn.java.core.form.MultiPartForm;
import cn.java.core.helper.captcha.Captcha;
import cn.java.core.helper.image.thumbnails.ThumbnailsHelper;
import cn.java.core.helper.url.UrlHelper;
import cn.java.core.model.DBHelper;
import cn.java.core.servlet.CoreSerlvet;

/**
	验证码√、
	缓存√、文件上传√、事务 √、分页√、跳转√
	多主题('theme'=>'theme1')√、多语言('language'=>'zh_cn')√、
	校验√、CRUD√、
	HTML的javascript的转义
	Json_encode Json_decode
	Csv上传、下载
	邮件发送
*/

/**
 * 
 * 
 */
public class AdminCommonSerlvet extends CoreSerlvet {

	protected void initial() {
		//		tplRootDir = "/WEB-INF/servletView/admin/";
		tplRootDir = ApplicationConfig.getConfig("app_tplRootDir_admin");
		this.initDbConnect();

	}

	/**
	 * 加载驱动
	 */
	protected void initDbConnect() {
		try {
			DBHelper.loadDriver();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * /admin/art/?act=captcha
	 * @throws IOException
	 */
	public void captchaAction() throws IOException {
		Captcha captcha = Captcha.getCaptchaAndOutput(getRequest(), getResponse());
		HttpSession session = getRequest().getSession();
		session.setAttribute("code", captcha.getCode());
	}

	/**
	 * 分页
	 * 
	 * @param path
	 * @param currPage
	 * @param totalCount
	 * @return
	 */
	protected String pagination(String path, int currPage, int totalCount) {
		int perPageShowCount = Integer.valueOf(ApplicationConfig.getConfig("app_pagination_perPageShowCount"));
		int showRangCount = Integer.valueOf(ApplicationConfig.getConfig("app_pagination_showRangCount"));
		return this.pagination(path, currPage, totalCount, perPageShowCount, showRangCount);
	}
	
	/**
	 * 分页
	 * 
	 * @param path
	 * @param currPage
	 * @param totalCount
	 * @return
	 */
	protected String pagination(String path, int currPage, int totalCount,int perPageShowCount) {
		int showRangCount = Integer.valueOf(ApplicationConfig.getConfig("app_pagination_showRangCount"));
		return this.pagination(path, currPage, totalCount, perPageShowCount, showRangCount);
	}


	/**
	 * 文件上传
	 * @throws UnsupportedEncodingException 
	 * 
	 * @throws ServletException
	 * @throws IOException
	 * @throws MagickException 
	 */
	public void uploadAction()  {
		JSONObject mJSONObject = new JSONObject();
		try {
			FieldHashMap fieldHashMap = this.processUpload();
			String saveDirPath = ApplicationConfig.getConfig("app_upload_realSaveDir");
			
			JSONObject mJSONObject2 = new JSONObject();
			mJSONObject2.put("homeUrl", UrlHelper.baseUrl());
			mJSONObject2.put("picSrc", saveDirPath+"/"+fieldHashMap.get("file"));
			
			mJSONObject.put("status", 1);
			mJSONObject.put("info", "成功！");
			mJSONObject.put("data", mJSONObject2);
			
		} catch (Exception e) {
			mJSONObject.put("status", 0);
			mJSONObject.put("info", "上传失败！");
		}
		try {
			this.jsonHeader();
			getResponse().getWriter().write(mJSONObject.toJSONString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 处理文件上传
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	protected FieldHashMap processUpload() throws UnsupportedEncodingException {
		ServletContext context = getServletContext();
		String saveDirPath = context.getRealPath(ApplicationConfig.getConfig("app_upload_realSaveDir"));//保存实际图片的目录
		String tempDirPath = context.getRealPath(ApplicationConfig.getConfig("app_upload_tempSaveDir"));//保存临时图片的目录
		// 如果路径不存在，则创建路径
		File saveDirPathFile = new File(saveDirPath);
		File tempDirPathFile = new File(tempDirPath);
		if (!saveDirPathFile.exists()) {//保存实际图片的目录
			saveDirPathFile.mkdirs();
		}
		
		if (!tempDirPathFile.exists()) {//保存临时图片的目录
			tempDirPathFile.mkdirs();
		}
		//设置编码  
		getRequest().setCharacterEncoding(ApplicationConfig.getConfig("app_request_characterEncoding"));
		//解析上传的数据
		FieldHashMap fieldHashMap = MultiPartForm.parseForm(getRequest(), tempDirPath, saveDirPath);
//		System.out.println(fieldHashMap.toString());
		return fieldHashMap;
	}
	
	/**
	 * 创建缩略图
	 * @param fieldHashMap
	 * @throws IOException
	 */
	public void createThumbPic(FieldHashMap fieldHashMap) throws IOException{
		ServletContext context = getServletContext();
		String saveThumbDirPath = context.getRealPath(ApplicationConfig.getConfig("app_upload_realThumbSaveDir"));//保存缩略图的目录
		int thumbPicWidth = Integer.valueOf(ApplicationConfig.getConfig("app_upload_picThumbWidth"));
		int thumbPicHeigth = Integer.valueOf(ApplicationConfig.getConfig("app_upload_picThumbHeigth"));
		//生成缩略图
		String srcFullPath = saveThumbDirPath+"/"+(String) fieldHashMap.get("file");
		String thumbFullPath = saveThumbDirPath+"/"+(String) fieldHashMap.get("file");
		
		//存放缩略图的目录
		File thumbDirPathFile = new File(saveThumbDirPath);
		if (!thumbDirPathFile.exists()) {//保存缩略图的目录
			thumbDirPathFile.mkdirs();
		}
		
		//创建目标文件
		File uploadFile = new File(thumbFullPath);
		if (!uploadFile.exists()) {
			uploadFile.createNewFile();//创建目标文件
		}
		
//		JmagickHelper.createThumbnail(srcFullPath, thumbFullPath,thumbPicWidth);//这个需要安装依赖包
		ThumbnailsHelper.createThumbnail(srcFullPath, thumbFullPath, thumbPicWidth, thumbPicHeigth);
		
	}
	
}
