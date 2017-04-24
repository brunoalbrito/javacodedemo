package cn.java.home.servlets;

import cn.java.core.model.DBHelper;
import cn.java.core.servlet.CoreSerlvet;

/**
 */
public class HomeCommonSerlvet extends CoreSerlvet {

	protected void initial() {
		tplRootDir = "/WEB-INF/servletView/home/";
		this.initDbConnect();

	}
	protected void initDbConnect() {
		try {
			DBHelper.loadDriver();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
}
