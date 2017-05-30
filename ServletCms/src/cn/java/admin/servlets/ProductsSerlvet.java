package cn.java.admin.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;

import cn.java.core.helper.url.UrlHelper;
import cn.java.core.model.DBHelper;

/**
 * Passport
 */
public class ProductsSerlvet extends AdminCommonSerlvet {

	/**
	 * 检查分类是否存在
	 * @param cateId
	 * @param connection
	 * @return
	 */
	protected boolean checkCate(String cateId, Connection connection) {
		//分页查找
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		int totalRows = 0;
		String getCountSql = "select count(*) as totalRows from " + DBHelper.normalTableName("products_cate") + " where id=?";
		try {
			connection = DBHelper.getConnect();
			statement = connection.prepareStatement(getCountSql);
			statement.setString(1, cateId);
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				totalRows = Integer.valueOf(resultSet.getString("totalRows"));
			}
			System.out.println("total:" + totalRows);// 统计数量
		} catch (Exception e) {
			e.printStackTrace();
		} finally {// 关闭连接
			DBHelper.close(resultSet, statement);
		}
		if (totalRows > 0)
			return true;
		else
			return false;
	}


	/**
	 * 添加 http://www.domain.com/admin/art/?act=add
	 * 
	 * @throws ServletException
	 * @throws IOException
	 */
	public void add() throws ServletException, IOException {
		if (getRequest().getMethod().equals("GET")) {
			this.display("Pro-add.jsp");
		} else {
			Connection connection = null;
			PreparedStatement statement = null;
			try {
				connection = DBHelper.getConnect();
				String cateId = getRequest().getParameter("cate_id");
				if (!this.checkCate(cateId, connection)) {//分类不存在
					HashMap hashMap = new HashMap();
					hashMap.put("product_name", getRequest().getParameter("product_name"));
					this.assign("item", hashMap);
					this.display("Pro-add.jsp");
				} else {//分类存在
					String sql = "INSERT INTO " + DBHelper.normalTableName("products") + "(product_name,cate_id,addtime) VALUES(?,?,?)";
					statement = connection.prepareStatement(sql);
					statement.setString(1, getRequest().getParameter("product_name"));
					statement.setString(2, cateId);
					statement.setString(3, String.valueOf(System.currentTimeMillis() / 1000));
					statement.executeUpdate();
					int affectedRowCount = statement.getUpdateCount();// 影响的行数
					if (affectedRowCount > 0) {
						DBHelper.close(statement, connection);
						getRequest().getRequestDispatcher(UrlHelper.url("admin", "pro", "index")).forward(getRequest(), getResponse());//到文章列表页
					} else {
						this.display("Pro-add.jsp");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {// 关闭连接
				DBHelper.close(statement, connection);
			}
		}
	}

	/**
	 * 列表
	 * 
	 * @throws ServletException
	 * @throws IOException
	 */
	public void index() throws ServletException, IOException {

		//分页查找
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		int totalRows = 0;
		String getCountSql = "select count(*) as totalRows from " + DBHelper.normalTableName("products") + " where status=?";
		try {
			connection = DBHelper.getConnect();
			statement = connection.prepareStatement(getCountSql);
			statement.setInt(1, 1);
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				totalRows = Integer.valueOf(resultSet.getString("totalRows"));
			}
			System.out.println("total:" + totalRows);// 统计数量
		} catch (Exception e) {
			e.printStackTrace();
		} finally {// 关闭连接
			DBHelper.close(resultSet, statement);
		}

		if (totalRows > 0) {
			int perPageShowCount = 10;
			int page = 1;
			if (getRequest().getParameter("page") != null && (!getRequest().getParameter("page").equals(""))) {
				page = Integer.valueOf(getRequest().getParameter("page"));
			}
			String sql = "select * from " + DBHelper.normalTableName("products") + " where status=? LIMIT ?,?";// 查询列表
			try {
				connection = DBHelper.getConnect();
				statement = connection.prepareStatement(sql);
				statement.setInt(1, 1);
				statement.setInt(2, page - 1);
				statement.setInt(3, perPageShowCount);
				resultSet = statement.executeQuery();
				ArrayList arrayList = new ArrayList();
				while (resultSet.next()) {
					HashMap hashMap = new HashMap();
					hashMap.put("id", resultSet.getString("id"));
					hashMap.put("product_name", resultSet.getString("product_name"));
					hashMap.put("cate_id", resultSet.getString("cate_id"));
					hashMap.put("addtime", resultSet.getString("addtime"));
					arrayList.add(hashMap);
				}
				this.assign("itemList", arrayList);//列表
				this.assign("pagination", this.pagination(UrlHelper.url("admin", "pro", "index"), page, totalRows));//分页导航
			} catch (Exception e) {
				e.printStackTrace();
			} finally {// 关闭连接
				DBHelper.close(resultSet, statement, connection);
			}
		} else {
			this.assign("itemList", null);
		}
		this.display("Pro-index.jsp");

	}

	/**
	 * 删除
	 * 
	 * @throws ServletException
	 * @throws IOException
	 */
	public void delete() throws ServletException, IOException {
		String id = getRequest().getParameter("id");
		String sql = "DELETE FROM " + DBHelper.normalTableName("products") + " WHERE id=?";
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = DBHelper.getConnect();
			statement = connection.prepareStatement(sql);
			statement.setString(1, id);
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
			e.printStackTrace();
		} finally {// 关闭连接
			DBHelper.close(statement, connection);
		}
	}

	/**
	 * 修改
	 * 
	 * @throws ServletException
	 * @throws IOException
	 */
	public void update() throws ServletException, IOException {
		String method = getRequest().getMethod();
		String id = getRequest().getParameter("id");
		if (method.equals("POST")) {
			//修改文章
			PreparedStatement statement = null;
			Connection connection = null;
			try {
				String cateId = getRequest().getParameter("cate_id");
				if (!this.checkCate(cateId, connection)) {//分类不存在
					HashMap hashMap = new HashMap();
					hashMap.put("product_name", getRequest().getParameter("product_name"));
					this.assign("item", hashMap);
					this.display("Pro-add.jsp");
				} else {//分类存在
					String sql = "UPDATE " + DBHelper.normalTableName("products") + " SET product_name=?,cate_id=? where id=? ";
					connection = DBHelper.getConnect();
					statement = connection.prepareStatement(sql);
					statement.setString(1, getRequest().getParameter("product_name"));
					statement.setString(2, cateId);
					statement.setString(3, id);
					statement.executeUpdate();
					int affectedRowCount = statement.getUpdateCount();// 影响的行数
					System.out.println("affectedRowCount=" + affectedRowCount);
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {// 关闭连接
				DBHelper.close(statement, connection);
			}
		} else {
			//文章详情
			String sql = "SELECT a.*,ac.cate_name FROM " + DBHelper.normalTableName("products") + " as a LEFT JOIN " + DBHelper.normalTableName("products_cate")
					+ " AS ac ON a.cate_id = ac.id WHERE id=?";// 查询列表
			PreparedStatement statement = null;
			Connection connection = null;
			ResultSet resultSet = null;
			try {
				connection = DBHelper.getConnect();
				statement = connection.prepareStatement(sql);
				statement.setString(1, id);
				resultSet = statement.executeQuery();
				if (resultSet.next()) {
					HashMap hashMap = new HashMap();
					hashMap.put("id", resultSet.getString("id"));
					hashMap.put("product_name", resultSet.getString("product_name"));
					hashMap.put("cate_name", resultSet.getString("cate_name"));
					hashMap.put("cate_id", resultSet.getString("cate_id"));
					hashMap.put("addtime", resultSet.getString("addtime"));
					this.assign("item", hashMap);
				} else {
					this.assign("item", null);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {// 关闭连接
				DBHelper.close(resultSet, statement, connection);
			}
			this.display("Pro-update.jsp");
		}
	}

}
