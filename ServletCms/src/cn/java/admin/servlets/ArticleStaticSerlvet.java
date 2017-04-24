package cn.java.admin.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletException;

import cn.java.admin.service.ArticleCateService;
import cn.java.admin.service.ArticleService;
import cn.java.app.config.ApplicationConfig;
import cn.java.core.helper.url.UrlHelper;
import cn.java.core.model.DBHelper;

/**
 * Passport
 */
public class ArticleStaticSerlvet extends AdminCommonSerlvet {

	/**
	 * 文章列表
	 * http://localhost/admin/article/?act=index
	 * @throws ServletException
	 * @throws IOException
	 */
	public void indexAction() throws ServletException, IOException {

		//分页查找
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		int totalRows = 0;
		String getCountSql = "SELECT count(*) AS totalRows FROM " + DBHelper.normalTableName("article") + " where status=1 AND art_type=1 ";
		try {
			connection = DBHelper.getConnect();
			statement = connection.prepareStatement(getCountSql);
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				totalRows = Integer.valueOf(resultSet.getString("totalRows"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {// 关闭连接
			DBHelper.close(resultSet, statement);
		}

		if (totalRows > 0) {
			int perPageShowCount = new Integer(ApplicationConfig.getConfig("app_pagination_showRangCount"));
			int currPage = 1;
			if (request.getParameter("page") != null && (!request.getParameter("page").equals(""))) {
				currPage = new Integer(request.getParameter("page"));
			}
			// 分页查询
			String sql = "SELECT art.*,art_cate.cate_name FROM " + DBHelper.normalTableName("article") + " AS art LEFT JOIN " + DBHelper.normalTableName("article_cate") + " AS art_cate ON art.cate_id=art_cate.id "+
					" where art.status=1 AND art.art_type=1 LIMIT ? , ? "; // 查询列表
			try {
				connection = DBHelper.getConnect();
				statement = connection.prepareStatement(sql);
				statement.setInt(1, (currPage - 1) * perPageShowCount);
				statement.setInt(2, perPageShowCount);
				resultSet = statement.executeQuery();
				ArrayList arrayList = new ArrayList();
				while (resultSet.next()) {
					HashMap hashMap = new HashMap();
					hashMap.put("id", resultSet.getInt("id"));
					hashMap.put("cate_name", resultSet.getString("cate_name"));
					hashMap.put("article_name", resultSet.getString("article_name"));
					hashMap.put("add_time", resultSet.getString("add_time"));
					hashMap.put("update_time", resultSet.getString("update_time"));
					hashMap.put("view_count", resultSet.getString("view_count"));
					arrayList.add(hashMap);
				}
				this.assign("itemList", arrayList);
				this.assign("pagination", this.pagination(UrlHelper.url("admin", "article", "index"), currPage, totalRows,perPageShowCount));
			} catch (Exception e) {
				e.printStackTrace();
			} finally {// 关闭连接
				DBHelper.close(resultSet, statement, connection);
			}
		} else {
			this.assign("itemList", null);
			this.assign("pagination", "");
		}
		this.display("ArticleStatic-index.jsp");
	}

	/**
	 * 修改文章
	 * 
	 * @throws ServletException
	 * @throws IOException
	 */
	public void editAction() throws ServletException, IOException {
		String method = request.getMethod();
		String id = request.getParameter("id");
		PreparedStatement statement = null;
		Connection connection = null;
		ResultSet resultSet = null;
		if (method.equals("POST")) {
			try {
				//修改文章
				String sql = "UPDATE " + DBHelper.normalTableName("article") + " SET content=?  where id=? ";
				connection = DBHelper.getConnect();
				statement = connection.prepareStatement(sql);
				statement.setString(1, request.getParameter("content"));
				statement.setInt(2, new Integer(id));
				statement.executeUpdate();
				int affectedRowCount = statement.getUpdateCount();// 影响的行数
				if(affectedRowCount>0){
					request.getRequestDispatcher(UrlHelper.url("admin", "article-static", "index")).forward(request, response);//到文章列表页
					return ;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally {// 关闭连接
				DBHelper.close(resultSet, statement, connection);
			}
		} 
		//Service调用
		String sql = "SELECT * FROM " + DBHelper.normalTableName("article") + "  WHERE id=? AND art_type=1 ";// 查询列表
		try {
			connection = DBHelper.getConnect();
			statement = connection.prepareStatement(sql);
			statement.setString(1, id);
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				HashMap item = new HashMap();
				item.put("id", resultSet.getString("id"));
				item.put("cate_id", resultSet.getString("cate_id"));
				item.put("article_name", resultSet.getString("article_name"));
				item.put("content", resultSet.getString("content"));
				item.put("weight", resultSet.getString("weight"));
				item.put("thumb_pic", resultSet.getString("thumb_pic"));
				item.put("status", resultSet.getString("status"));
				item.put("add_time", resultSet.getString("add_time"));
				item.put("update_time", resultSet.getString("update_time"));
				item.put("view_count", resultSet.getString("view_count"));
				item.put("art_type", resultSet.getString("art_type"));
				this.assign("item", item);
				this.display("ArticleStatic-edit.jsp");
			} 
			else{
				request.getRequestDispatcher(UrlHelper.url("admin", "article-static", "index")).forward(request, response);//到文章列表页
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {// 关闭连接
			DBHelper.close(resultSet, statement, connection);
		}
	}

}
