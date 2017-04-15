package cn.java.admin.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.swing.plaf.basic.BasicScrollPaneUI.HSBChangeListener;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.java.admin.service.ArticleCateService;
import cn.java.core.helper.url.UrlHelper;
import cn.java.core.model.DBHelper;

/**
 * 友情链接
 */
public class FriendlinkSerlvet extends AdminCommonSerlvet {

	/**
	 * http://localhost:8080/admin/friendlink/?act=index
	 * 列表
	 * @throws ServletException
	 * @throws IOException
	 */
	public void indexAction() throws ServletException, IOException {

		//分页查找
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		int totalRows = 0;
		String getCountSql = "SELECT count(*) AS totalRows FROM " + DBHelper.normalTableName("friend_link") + "   ";
		try {
			// 统计总数
			connection = DBHelper.getConnect();
			statement = connection.prepareStatement(getCountSql);
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				totalRows = Integer.valueOf(resultSet.getString("totalRows"));
			}
			
			// 查找列表
			if (totalRows > 0) {
				String sql = "SELECT * FROM " + DBHelper.normalTableName("friend_link") + " ";// 查询列表
				connection = DBHelper.getConnect();
				statement = connection.prepareStatement(sql);
				resultSet = statement.executeQuery();
				ArrayList itemList = new ArrayList();
				while (resultSet.next()) {
					HashMap item = new HashMap();
					item.put("id", resultSet.getInt("id"));
					item.put("site_name", resultSet.getString("site_name"));
					item.put("site_url", resultSet.getString("site_url"));
					item.put("site_logo", resultSet.getString("site_logo"));
					item.put("add_time", resultSet.getString("add_time"));
					item.put("update_time", resultSet.getString("update_time"));
					item.put("sort_order", resultSet.getString("sort_order"));
					itemList.add(item);
				}
				this.assign("itemList", itemList);
			} 
		} catch (Exception e) {
			e.printStackTrace();
		} finally {// 关闭连接
			DBHelper.close(resultSet, statement);
		}
		
		this.display("Friendlink-index.jsp");
	}
	
	/**
	 * 删除
	 * @throws ServletException
	 * @throws IOException
	 */
	public void deleteAction() throws ServletException, IOException {
		String ids = request.getParameter("ids");
		String sql = "DELETE FROM " + DBHelper.normalTableName("friend_link") + " WHERE id IN(?)";
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = DBHelper.getConnect();
			statement = connection.prepareStatement(sql);
			statement.setString(1, ids);
			statement.executeUpdate();
			int affectedRowCount = statement.getUpdateCount();// 影响的行数
			HashMap hashMap = new HashMap();
			if (affectedRowCount > 0) {
				hashMap.put("status", 1);
				hashMap.put("info", "删除成功");
			} else {
				hashMap.put("status", 0);
				hashMap.put("info", "删除失败");
			}
			this.ajaxReturn(hashMap);
		} catch (Exception e) {
			HashMap hashMap = new HashMap();
			hashMap.put("status", 0);
			hashMap.put("info", "删除失败");
			this.ajaxReturn(hashMap);
		} finally {// 关闭连接
			DBHelper.close(statement, connection);
		}
	}

	/**
	 * 添加
	 * @throws ServletException
	 * @throws IOException
	 */
	public void addAction() throws ServletException, IOException {
		if (request.getMethod().equals("GET")) {
			String html = this.fetch("Friendlink-add.jsp");
			this.ajaxHtmlReturn(1, "添加文章分类", html);
		} else {
			String sql = "INSERT INTO " + DBHelper.normalTableName("friend_link") + "(site_name,site_url,site_logo,sort_order,add_time,update_time) VALUES(?,?,?,?,?,?)";
			Connection connection = null;
			PreparedStatement statement = null;
			try {
				connection = DBHelper.getConnect();
				statement = connection.prepareStatement(sql);
				statement.setString(1, request.getParameter("site_name"));
				statement.setString(2, request.getParameter("site_url"));
				statement.setString(3, request.getParameter("site_logo"));
				statement.setString(4, request.getParameter("sort_order"));
				statement.setInt(5, (int) (System.currentTimeMillis()/1000));
				statement.setInt(6, (int) (System.currentTimeMillis()/1000));
				statement.executeUpdate();
				int affectedRowCount = statement.getUpdateCount();// 影响的行数
				if (affectedRowCount > 0) {
					this.ajaxHtmlReturn(1, "添加成功", "");
					return ;
				}
				else{
					this.ajaxHtmlReturn(0, "添加失败", "");
					return ;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {// 关闭连接
				DBHelper.close(statement, connection);
			}
			this.ajaxHtmlReturn(0, "添加失败", "");
		}
	}
	
	
	/**
	 * 修改
	 * @throws ServletException
	 * @throws IOException
	 */
	public void editAction() throws ServletException, IOException {
		String method = request.getMethod();
		String id = request.getParameter("id");
		if (method.equals("POST")) {
			//修改文章分类
			String sql = "UPDATE " + DBHelper.normalTableName("friend_link") + " SET site_name=?,site_url=?,site_logo=?,sort_order=?,update_time=? where id = ? ";
			PreparedStatement statement = null;
			Connection connection = null;
			try {
				connection = DBHelper.getConnect();
				statement = connection.prepareStatement(sql);
				statement.setString(1, request.getParameter("site_name"));
				statement.setString(2, request.getParameter("site_url"));
				statement.setString(3, request.getParameter("site_logo"));
				statement.setString(4, request.getParameter("sort_order"));
				statement.setInt(5, (int) (System.currentTimeMillis()/1000));
				statement.setString(6, id);
				statement.executeUpdate();
				int affectedRowCount = statement.getUpdateCount();// 影响的行数
				System.out.println("affectedRowCount=" + affectedRowCount);
				if (affectedRowCount > 0) {
					this.ajaxHtmlReturn(1, "修改成功", "");
					return ;
				}
				else{
					this.ajaxHtmlReturn(0, "修改失败", "");
					return ;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {// 关闭连接
				DBHelper.close(statement, connection);
			}
			this.ajaxHtmlReturn(0, "修改失败", "");
		} else {
			//文章分类详情
			String sql = "SELECT * FROM " + DBHelper.normalTableName("friend_link") + " WHERE id=?";// 查询列表
			PreparedStatement statement = null;
			Connection connection = null;
			ResultSet resultSet = null;
			try {
				connection = DBHelper.getConnect();
				statement = connection.prepareStatement(sql);
				statement.setString(1, id);
				resultSet = statement.executeQuery();
				if (resultSet.next()) {
					HashMap item = new HashMap();
					item.put("id", resultSet.getInt("id"));
					item.put("site_name", resultSet.getString("site_name"));
					item.put("site_url", resultSet.getString("site_url"));
					item.put("site_logo", resultSet.getString("site_logo"));
					item.put("add_time", resultSet.getString("add_time"));
					item.put("update_time", resultSet.getString("update_time"));
					item.put("sort_order", resultSet.getString("sort_order"));
					this.assign("item", item);
					String html = this.fetch("Friendlink-edit.jsp");
					this.ajaxHtmlReturn(1, "记录存在", html);
					return;
				} else {
					this.ajaxHtmlReturn(0, "记录不存在", "");
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {// 关闭连接
				DBHelper.close(resultSet, statement, connection);
			}
			this.ajaxHtmlReturn(0, "记录不存在", "");
		}
	}

}
