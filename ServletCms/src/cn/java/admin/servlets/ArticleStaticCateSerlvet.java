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
 * 
 */
public class ArticleStaticCateSerlvet extends AdminCommonSerlvet {

	/**
	 * 列表
	 * http://localhost:8080/admin/article-cate/?act=index
	 * @throws ServletException
	 * @throws IOException
	 */
	public void indexAction() throws ServletException, IOException {

		//分页查找
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		int totalRows = 0;
		String getCountSql = "SELECT count(*) AS totalRows FROM " + DBHelper.normalTableName("article_cate") + " where status=? AND is_locked=1  ";
		try {
			connection = DBHelper.getConnect();
			statement = connection.prepareStatement(getCountSql);
			statement.setInt(1, 1);
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
			String sql = "SELECT * FROM " + DBHelper.normalTableName("article_cate") + " where status=? AND is_locked=?";// 查询列表
			try {
				connection = DBHelper.getConnect();
				statement = connection.prepareStatement(sql);
				statement.setInt(1, 1);
				statement.setInt(2, 1);
				resultSet = statement.executeQuery();
				ArrayList itemList = new ArrayList();
				while (resultSet.next()) {
					HashMap item = new HashMap();
					item.put("id", resultSet.getInt("id"));
					item.put("cateName", resultSet.getString("cate_name"));
					item.put("parentId", resultSet.getInt("parent_id"));
					item.put("status", resultSet.getString("status"));
					item.put("articleCount", resultSet.getString("article_count"));
					itemList.add(item);
				}
				itemList = this.recurseChilds(itemList,0);
				this.assign("itemList", itemList);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {// 关闭连接
				DBHelper.close(resultSet, statement, connection);
			}
		} else {
			this.assign("itemList", null);
		}
		this.display("ArticleStaticCate-index.jsp");
	}

	/**
	 * 构造递归树
	 * @param arrayList
	 * @param parentId
	 * @return
	 */
	protected ArrayList recurseChilds(ArrayList arrayList,int parentId) {
		ArrayList mArrayList = new ArrayList();
		for (int index = 0; index < arrayList.size(); index++) {
			HashMap mHashMapTemp = (HashMap) arrayList.get(index);
			if(parentId ==  (Integer)mHashMapTemp.get("parentId")){
				mArrayList.add(mHashMapTemp);
				//arrayList.remove(index); 注释掉，不然递归会出错
				ArrayList mArrayListTemp = this.recurseChilds(arrayList, (Integer)mHashMapTemp.get("id"));
				mHashMapTemp.put("childList",mArrayListTemp);
			}
		}
		return mArrayList;
	}
	
	/**
	 * 修改
	 * 
	 * @throws ServletException
	 * @throws IOException
	 */
	public void editAction() throws ServletException, IOException {
		String method = request.getMethod();
		String id = request.getParameter("id");
		if (method.equals("POST")) {
			//修改文章分类
			String sql = "UPDATE " + DBHelper.normalTableName("article_cate") + " SET cate_name=? where id=? ";
			PreparedStatement statement = null;
			Connection connection = null;
			try {
				connection = DBHelper.getConnect();
				statement = connection.prepareStatement(sql);
				statement.setString(1, request.getParameter("cate_name"));
				statement.setString(2, id);
				statement.executeUpdate();
				int affectedRowCount = statement.getUpdateCount();// 影响的行数
				System.out.println("affectedRowCount=" + affectedRowCount);
				DBHelper.close(statement, connection);
				if (affectedRowCount > 0) {
					this.ajaxHtmlReturn(1, "修改文章分类成功", "");
					return ;
				}
				else{
					this.ajaxHtmlReturn(0, "修改文章分类失败", "");
					return ;
				}
			} catch (Exception e) {
				this.ajaxHtmlReturn(0, "修改文章分类失败", "");
//				e.printStackTrace();
			} finally {// 关闭连接
				DBHelper.close(statement, connection);
			}
		} else {
			//文章分类详情
			String sql = "SELECT * FROM " + DBHelper.normalTableName("article_cate") + " WHERE id=?";// 查询列表
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
					hashMap.put("id", resultSet.getInt("id"));
					hashMap.put("cateName", resultSet.getString("cate_name"));
					hashMap.put("parentId", resultSet.getInt("parent_id"));
					hashMap.put("status", resultSet.getString("status"));
					hashMap.put("articleCount", resultSet.getString("article_count"));
					this.assign("item", hashMap);
				} else {
					this.ajaxHtmlReturn(0, "文章分类不存在", "");
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
				this.ajaxHtmlReturn(0, "文章分类不存在", "");
				return;
			} finally {// 关闭连接
				DBHelper.close(resultSet, statement, connection);
			}
			String html = this.fetch("ArticleStaticCate-edit.jsp");
			this.ajaxHtmlReturn(1, "修改文章分类", html);
		}
	}
	

}
