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
public class ArticleCateSerlvet extends AdminCommonSerlvet {

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
		try {
			int totalRows = 0;
			String getCountSql = "SELECT count(*) AS totalRows FROM " + DBHelper.normalTableName("article_cate") + " where status=? AND is_locked=0  ";

			connection = DBHelper.getConnect();
			statement = connection.prepareStatement(getCountSql);
			statement.setInt(1, 1);
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				totalRows = Integer.valueOf(resultSet.getString("totalRows"));
			}
			System.out.println("total:" + totalRows);// 统计数量

			if (totalRows > 0) {
				String sql = "SELECT * FROM " + DBHelper.normalTableName("article_cate") + " where status=? AND is_locked=?";// 查询列表
				connection = DBHelper.getConnect();
				statement = connection.prepareStatement(sql);
				statement.setInt(1, 1);
				statement.setInt(2, 0);
				resultSet = statement.executeQuery();
				ArrayList arrayList = new ArrayList();
				while (resultSet.next()) {
					HashMap hashMap = new HashMap();
					hashMap.put("id", resultSet.getInt("id"));
					hashMap.put("cateName", resultSet.getString("cate_name"));
					hashMap.put("parentId", resultSet.getInt("parent_id"));
					hashMap.put("status", resultSet.getString("status"));
					hashMap.put("articleCount", resultSet.getString("article_count"));
					arrayList.add(hashMap);
				}
				arrayList = this.recurseChilds(arrayList,0);

				this.assign("itemList", arrayList);

			} else {
				this.assign("itemList", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.assign("itemList", null);
		} finally {// 关闭连接
			DBHelper.close(resultSet, statement, connection);
		}
		this.display("ArticleCate-index.jsp");
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
	 * 删除
	 * 
	 * @throws ServletException
	 * @throws IOException
	 */
	public void deleteAction() throws ServletException, IOException {
		String id = request.getParameter("id");
		String sql = "DELETE FROM " + DBHelper.normalTableName("article_cate") + " WHERE id=?";
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
			//			e.printStackTrace();
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
	 * 
	 * http://localhost:8080/admin/article-cate/?act=add
	 * 
	 * @throws ServletException
	 * @throws IOException
	 */
	public void addAction() throws ServletException, IOException {
		if (request.getMethod().equals("GET")) {
			this.assign("cateItemList", this.getTopCateList());
			String html = this.fetch("ArticleCate-add.jsp");
			this.ajaxHtmlReturn(1, "添加文章分类", html);
		} else {
			String sql = "INSERT INTO " + DBHelper.normalTableName("article_cate") + "(cate_name,parent_id,status) VALUES(?,?,1)";
			Connection connection = null;
			PreparedStatement statement = null;
			try {
				connection = DBHelper.getConnect();
				statement = connection.prepareStatement(sql);
				statement.setString(1, request.getParameter("cate_name"));
				statement.setString(2, request.getParameter("parent_id"));
				statement.executeUpdate();
				int affectedRowCount = statement.getUpdateCount();// 影响的行数
				DBHelper.close(statement, connection);
				HashMap hashMap = new HashMap();
				if (affectedRowCount > 0) {
					System.out.println("影响的行数："+affectedRowCount);
					this.ajaxHtmlReturn(1, "添加文章分类成功", "");
					return ;
				}
				else{
					this.ajaxHtmlReturn(0, "添加文章分类失败", "");
					return ;
				}
			} catch (Exception e) {
				this.ajaxHtmlReturn(0, "添加文章分类失败", "");
				//				e.printStackTrace();
			} finally {// 关闭连接
				DBHelper.close(statement, connection);
			}
		}
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
			String sql = "UPDATE " + DBHelper.normalTableName("article_cate") + " SET cate_name=?,parent_id=?,status=1 where id=? ";
			PreparedStatement statement = null;
			Connection connection = null;
			try {
				connection = DBHelper.getConnect();
				statement = connection.prepareStatement(sql);
				statement.setString(1, request.getParameter("cate_name"));
				statement.setString(2, request.getParameter("parent_id"));
				statement.setString(3, id);
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
			this.assign("cateItemList", this.getTopCateList());
			String html = this.fetch("ArticleCate-edit.jsp");
			this.ajaxHtmlReturn(1, "修改文章分类", html);
		}
	}

	/**
	 * 取得顶级分类列表
	 * @return
	 */
	protected ArrayList getTopCateList(){
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		ArrayList arrayList = new ArrayList();
		String sql = "SELECT * FROM " + DBHelper.normalTableName("article_cate") + " where status=? AND is_locked=? AND parent_id=0";// 查询列表
		try {
			connection = DBHelper.getConnect();
			statement = connection.prepareStatement(sql);
			statement.setInt(1, 1);
			statement.setInt(2, 0);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				HashMap hashMap = new HashMap();
				hashMap.put("id", resultSet.getInt("id"));
				hashMap.put("cateName", resultSet.getString("cate_name"));
				hashMap.put("parentId", resultSet.getInt("parent_id"));
				hashMap.put("status", resultSet.getString("status"));
				hashMap.put("articleCount", resultSet.getString("article_count"));
				arrayList.add(hashMap);
			}
			return arrayList;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {// 关闭连接
			DBHelper.close(resultSet, statement, connection);
		}
		return arrayList;
	}


	/**
	 * JSON返回子分类
	 * 子分类列表
	 */
	public void ajaxGetSubCateListAction(){
		this.jsonHeader();
		String parentId = request.getParameter("id");
		try {
			ArticleCateService mArticleCateService = new ArticleCateService();
			ArrayList<HashMap> cateList = mArticleCateService.getCateListByParentId(new Integer(parentId));
			JSONObject mJSONObject = new JSONObject();
			JSONArray mJSONArray = new JSONArray();
			for (HashMap hashMap : cateList) {
				JSONObject cateInfoTemp = new JSONObject();
				cateInfoTemp.put("id", hashMap.get("id"));
				cateInfoTemp.put("cate_name", hashMap.get("cate_name"));
				mJSONArray.add(cateInfoTemp);
			}
			JSONObject mJSONObject2 = new JSONObject();
			mJSONObject2.put("subCateList", mJSONArray);

			mJSONObject.put("status", 1);
			mJSONObject.put("info", "成功！");
			mJSONObject.put("data", mJSONObject2);
			response.getWriter().write(mJSONObject.toJSONString());
		} catch (Exception e) {
		}
	}

}
