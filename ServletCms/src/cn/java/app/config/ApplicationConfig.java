package cn.java.app.config;

import java.util.HashMap;

public class ApplicationConfig {
	public static String getConfig(String keyName) {
		HashMap hashMap = new HashMap();
		
		//应用信息
		hashMap.put("app_mode", "development");//开发模式
		
		//数据库配置
		hashMap.put("app_db_driverName", "com.mysql.jdbc.Driver");
		hashMap.put("app_db_user", "");
		hashMap.put("app_db_pwd", "");
		hashMap.put("app_db_connectUrl", "jdbc:mysql://localhost:3306/pinphpdemo?user=root&password=&useUnicode=true&characterEncoding=UTF8");
		hashMap.put("app_db_tablePrefix", "bizcn_");
		
		//文件上传信息
		hashMap.put("app_upload_realSaveDir", "/upload/images");//实际目录
		hashMap.put("app_upload_realThumbSaveDir", "/upload/thumb_img");//实际目录
		hashMap.put("app_upload_tempSaveDir", "/upload/tempdir");//临时目录
		hashMap.put("app_upload_picThumbWidth", "350");//缩略图宽度
		hashMap.put("app_upload_picThumbHeigth", "350");//缩略图高度
		
		//请求编码
		hashMap.put("app_request_characterEncoding","utf-8");
		
		//模板根目录
		hashMap.put("app_tplRootDir_admin","/WEB-INF/servletView/admin/");//管理员端的
		hashMap.put("app_tplRootDir_home","/WEB-INF/servletView/home/");
		hashMap.put("app_tplRootDir_member","/WEB-INF/servletView/member/");
		
		//FNE
		hashMap.put("app_pagination_perPageShowCount","3");
		hashMap.put("app_pagination_showRangCount","5");
		
		if (hashMap.get(keyName) != null) {
			return (String) hashMap.get(keyName);
		}
		return "";
	}
}
