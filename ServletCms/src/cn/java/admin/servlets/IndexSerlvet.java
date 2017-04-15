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
 */
public class IndexSerlvet extends AdminCommonSerlvet {
	
	/**
	 * http://localhost/admin/index/?act=index
	 */
	public void indexAction(){
		this.redirect("/admin/baseinfo/?act=index");
	}
	
	public void clearcacheAction(){
		try {
			this.display("Index-index.jsp");
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
