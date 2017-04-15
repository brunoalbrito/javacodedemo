package cn.java.admin.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.ServletException;

import org.eclipse.jetty.util.security.Credential.MD5;

import cn.java.core.model.DBHelper;

/**
 * /admin/baseinfo/?act=index
 */
public class BaseinfoSerlvet extends AdminCommonSerlvet {
	
	/**
	 * http://localhost/admin/baseinfo/?act=index
	 */
	public void indexAction(){
		try {
			this.display("Baseinfo-index.jsp");
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void clearcacheAction(){
		if("true".equals(request.getParameter("isClearCache"))){
			this.redirect("/admin/baseinfo/?act=index");
		}
		else{
			try {
				request.setAttribute("isClearCache",true);
				this.display("Baseinfo-index.jsp");
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
