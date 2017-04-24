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
 * http://www.domain.com/admin/passport/?act=add
 */
public class PassportSerlvet extends AdminCommonSerlvet {

	/**
	 * 退出
	 * @throws ServletException
	 * @throws IOException
	 */
	public void logout() throws ServletException, IOException {
		HashMap hashTable = (HashMap) request.getSession().getAttribute("adminInfo");
		if (hashTable != null) {
			request.removeAttribute("adminInfo");
		}
		//进入登录页面
		request.getRequestDispatcher("/admin/index/login").forward(request, response);
	}

	

	/**
	 * 登录
	 * 
	 * @throws ServletException
	 * @throws IOException
	 */
	public void login() throws ServletException, IOException {
		if (request.getMethod().equals("GET")) {
			this.display("admin/Passport-login.jsp");
		} else {
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			String sql = "SELECT * FROM " + DBHelper.normalTableName("admin") + " WHERE username=? LIMIT 1";// 查找用户
			PreparedStatement statement = null;
			Connection connection = null;
			ResultSet resultSet = null;
			try {
				connection = DBHelper.getConnect();
				statement = connection.prepareStatement(sql);
				statement.setString(1, username);
				resultSet = statement.executeQuery();
				if (resultSet.next()) {
					String passwordTemp = resultSet.getString("password");
					DBHelper.close(resultSet, statement, connection);
					if (passwordTemp == MD5.digest(password)) {
						request.getRequestDispatcher("/admin/Index/index").forward(request, response);//进入后台
					} else {
						this.display("admin/Passport-login.jsp");
					}
				} else {
					this.display("admin/Passport-login.jsp");
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {// 关闭连接
				DBHelper.close(resultSet, statement, connection);
			}
		}
	}

	/**
	 * 注册
	 * 
	 * @throws ServletException
	 * @throws IOException
	 */
	public void register() throws ServletException, IOException {
		if (request.getMethod().equals("POST")) {
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			String sql = "SELECT * FROM " + DBHelper.normalTableName("admin") + " WHERE username=?  LIMIT 1";// 查找用户
			PreparedStatement statement = null;
			Connection connection = null;
			ResultSet resultSet = null;
			try {
				connection = DBHelper.getConnect();
				statement = connection.prepareStatement(sql);
				statement.setString(1, username);
				resultSet = statement.executeQuery();
				if (resultSet.next()) {//用户已经存在
					DBHelper.close(resultSet, statement, connection);
					this.display("admin/Passport-register.jsp");
				} else {//进行注册
					sql = "INSERT INTO " + DBHelper.normalTableName("admin") + "(username,password,addtime) VALUES(?,?,?)";
					try {
						String passwordTemp = MD5.digest(password);
						long addTime = System.currentTimeMillis() / 1000;
						statement = connection.prepareStatement(sql);
						statement.setString(1, username);
						statement.setString(2, passwordTemp);
						//statement.setString(3, String.valueOf(addTime));
						statement.executeUpdate();
						int affectedRowCount = statement.getUpdateCount();// 影响的行数
						if (affectedRowCount > 0) {
							DBHelper.close(resultSet, statement, connection);

							//进入会员中心
							request.getRequestDispatcher("/admin/Index/index").forward(request, response);//进入后台
						}
					} catch (SQLException e) {
						e.printStackTrace();
					} finally {// 关闭连接
						DBHelper.close(resultSet, statement, connection);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {// 关闭连接
				DBHelper.close(resultSet, statement, connection);
			}
		} else {
			this.display("admin/Passport-register.jsp");
		}
	}
	
	/**
	 * 设置登录状态
	 * 
	 * @param resultSet
	 * @throws SQLException
	 */
	private void setLoginInfo(ResultSet resultSet) throws SQLException {
		HashMap hashTable = new HashMap();
		hashTable.put("username", resultSet.getString("username"));
		hashTable.put("password", resultSet.getString("password"));
		hashTable.put("realname", resultSet.getString("realname"));
		request.getSession().setAttribute("adminInfo", hashTable);
	}

	/**
	 * 设置登录状态
	 * 
	 * @param resultSet
	 * @throws SQLException
	 */
	private void setLoginInfo(String username, String password, String realname) {
		HashMap hashTable = new HashMap();
		hashTable.put("username", username);
		hashTable.put("password", password);
		hashTable.put("realname", realname);
		request.getSession().setAttribute("adminInfo", hashTable);
	}
}
