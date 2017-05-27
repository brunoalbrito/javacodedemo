package cn.java.admin.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.java.admin.service.ArticleCateService;
import cn.java.core.helper.url.UrlHelper;
import cn.java.core.model.DBHelper;

/**
 * @param <V>
 * 
 */
public class ProductsCateSerlvet<V> extends AdminCommonSerlvet {

	/**
	 * 列表
	 * http://localhost:8080/admin/products-cate/?act=index
	 * @throws ServletException
	 * @throws IOException
	 */
	public void indexAction() throws ServletException, IOException {

		//分页查找
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		int totalRows = 0;
		String getCountSql = "SELECT count(*) AS totalRows FROM " + DBHelper.normalTableName("products_cate") + " where status=? ";
		try {
			connection = DBHelper.getConnect();
			statement = connection.prepareStatement(getCountSql);
			statement.setInt(1, 1);
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				totalRows = Integer.valueOf(resultSet.getString("totalRows"));
			}
			if (totalRows > 0) {
				String sql = "SELECT * FROM " + DBHelper.normalTableName("products_cate") + " where status=? ";// 查询列表
				connection = DBHelper.getConnect();
				statement = connection.prepareStatement(sql);
				statement.setInt(1, 1);
				resultSet = statement.executeQuery();
				ArrayList arrayList = new ArrayList();
				while (resultSet.next()) {
					HashMap hashMap = new HashMap();
					hashMap.put("id", resultSet.getInt("id"));
					hashMap.put("cate_name", resultSet.getString("cate_name"));
					hashMap.put("parent_id", resultSet.getInt("parent_id"));
					hashMap.put("status", resultSet.getString("status"));
					hashMap.put("product_count", resultSet.getString("product_count"));
					arrayList.add(hashMap);
				}
				arrayList = this.recurseChilds(arrayList,0);
				this.assign("itemList", arrayList);
				this.display("ProductsCate-index.jsp");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {// 关闭连接
			DBHelper.close(resultSet, statement, connection);
		}
	}

	/**
	 * 构造递归树
	 * @param arrayList
	 * @param parentId
	 * @return
	 */
	protected ArrayList recurseChilds(ArrayList arrayList,int parentId) {
		ArrayList arrayListTemp = new ArrayList();
		for (int index = 0; index < arrayList.size(); index++) {
			HashMap mHashMapTemp = (HashMap) arrayList.get(index);
			if(parentId ==  (Integer)mHashMapTemp.get("parent_id")){
				arrayListTemp.add(mHashMapTemp);
				//arrayList.remove(index); 注释掉，不然递归会出错
				ArrayList subArrayList = this.recurseChilds(arrayList, (Integer)mHashMapTemp.get("id"));
				arrayListTemp.addAll(subArrayList);
			}
		}
		return arrayListTemp;
	}

	/**
	 * Select组件的数据
	 * @param arrayList
	 * @param parentId
	 * @return
	 */
	protected ArrayList getSelectList() {
		//分页查找
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = DBHelper.getConnect();
			String sql = "SELECT * FROM " + DBHelper.normalTableName("products_cate") + " where status=? ";// 查询列表
			connection = DBHelper.getConnect();
			statement = connection.prepareStatement(sql);
			statement.setInt(1, 1);
			resultSet = statement.executeQuery();
			ArrayList arrayList = new ArrayList();
			while (resultSet.next()) {
				HashMap hashMap = new HashMap();
				hashMap.put("id", resultSet.getInt("id"));
				hashMap.put("cate_name", resultSet.getString("cate_name"));
				hashMap.put("parent_id", resultSet.getInt("parent_id"));
				hashMap.put("status", resultSet.getString("status"));
				hashMap.put("product_count", resultSet.getString("product_count"));
				arrayList.add(hashMap);
			}
			return this.getSelectListTree(arrayList,0,0);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {// 关闭连接
			DBHelper.close(resultSet, statement, connection);
		}
		return null;
	}

	/**
	 * 构造递归树
	 * @param arrayList
	 * @param parentId
	 * @return
	 */
	protected ArrayList getSelectListTree(ArrayList arrayList,int parentId,int level) {
		ArrayList arrayListTemp = new ArrayList();
		for (int index = 0; index < arrayList.size(); index++) {
			HashMap mHashMapTemp = (HashMap) arrayList.get(index);
			if(parentId ==  (Integer)mHashMapTemp.get("parent_id")){
				StringBuilder builder = new StringBuilder(level*2); 
				for(int i=0; i<level*2; i++){ 
					builder.append("-"); 
				} 
				mHashMapTemp.put("cate_name_x", builder.toString()+mHashMapTemp.get("cate_name"));
				arrayListTemp.add(mHashMapTemp);
				ArrayList subArrayList = this.getSelectListTree(arrayList, (Integer)mHashMapTemp.get("id"),level+1);
				arrayListTemp.addAll(subArrayList);
			}
		}
		return arrayListTemp;
	}

	/**
	 * 删除
	 * 
	 * @throws ServletException
	 * @throws IOException
	 */
	public void deleteAction() throws ServletException, IOException {
		String id = getRequest().getParameter("id");
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		HashMap hashMap = new HashMap();
		String sql = "";
		try {
			sql = "SELECT count(*) as refCount FROM " + DBHelper.normalTableName("products") + " where cate_id=? ";// 查询列表
			connection = DBHelper.getConnect();
			statement = connection.prepareStatement(sql);
			statement.setString(1, id);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				if(resultSet.getInt("refCount")>0){
					hashMap.put("status", 0);
					hashMap.put("info", "分类下有产品，请先删除产品");
					this.ajaxReturn(hashMap);
					return;
				}
			}
			sql = "SELECT count(*) as refCount FROM " + DBHelper.normalTableName("products_cate") + " where parent_id=? ";// 查询列表
			connection = DBHelper.getConnect();
			statement = connection.prepareStatement(sql);
			statement.setString(1, id);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				if(resultSet.getInt("refCount")>0){
					hashMap.put("status", 0);
					hashMap.put("info", "有子分类，请先删除子分类");
					this.ajaxReturn(hashMap);
					return;
				}
			}
			sql = "DELETE FROM " + DBHelper.normalTableName("products_cate") + " WHERE id in(?)";
			connection = DBHelper.getConnect();
			statement = connection.prepareStatement(sql);
			statement.setString(1, id);
			statement.executeUpdate();
			int affectedRowCount = statement.getUpdateCount();// 影响的行数
			if (affectedRowCount > 0) {
				hashMap.put("status", 1);
				hashMap.put("info",  "删除成功");
			} else {
				hashMap.put("status", 0);
				hashMap.put("info", "删除失败");
			}
			this.ajaxReturn(hashMap);
			return;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {// 关闭连接
			DBHelper.close(statement, connection);
		}
		hashMap.put("status", 0);
		hashMap.put("info", "删除失败");
		this.ajaxReturn(hashMap);
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
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			if (getRequest().getMethod().equals("GET")) {
				Integer parentId = new Integer(getRequest().getParameter("parent_id"));
				if(parentId==null||parentId==0){
					this.assign("selectList", this.getSelectList());
				}
				else{
					String sql = "SELECT * FROM " + DBHelper.normalTableName("products_cate") + " where id=? ";// 查询列表
					connection = DBHelper.getConnect();
					statement = connection.prepareStatement(sql);
					statement.setInt(1, parentId);
					ResultSet resultSet = statement.executeQuery();
					while (resultSet.next()) {
						HashMap item = new HashMap();
						item.put("id", resultSet.getInt("id"));
						item.put("cate_name", resultSet.getString("cate_name"));
						item.put("parent_id", resultSet.getInt("parent_id"));
						item.put("status", resultSet.getString("status"));
						item.put("product_count", resultSet.getString("product_count"));
						this.assign("parentOne", item);
						break;
					}
				}
				String html = this.fetch("ProductsCate-add.jsp");
				this.ajaxHtmlReturn(1, "添加文章分类", html);
				return ;
			} else {
				String sql = "INSERT INTO " + DBHelper.normalTableName("products_cate") + "(cate_name,parent_id,status) VALUES(?,?,1)";
				connection = DBHelper.getConnect();
				statement = connection.prepareStatement(sql);
				statement.setString(1, getRequest().getParameter("cate_name"));
				statement.setString(2, getRequest().getParameter("parent_id"));
				statement.executeUpdate();
				int affectedRowCount = statement.getUpdateCount();// 影响的行数
				if (affectedRowCount > 0) {
					this.ajaxHtmlReturn(1, "成功", "");
					return ;
				}
				this.ajaxHtmlReturn(0, "失败", "");
				return ;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {// 关闭连接
			DBHelper.close(statement, connection);
		}
		this.ajaxHtmlReturn(0, "失败", "");
	}


	/**
	 * 修改
	 * 
	 * @throws ServletException
	 * @throws IOException
	 */
	public void editAction() throws ServletException, IOException {
		String method = getRequest().getMethod();
		String id = getRequest().getParameter("id");
		if (method.equals("POST")) {
			//修改文章分类
			String sql = "UPDATE " + DBHelper.normalTableName("products_cate") + " SET cate_name=?,parent_id=?,status=1 where id=? ";
			PreparedStatement statement = null;
			Connection connection = null;
			try {
				connection = DBHelper.getConnect();
				statement = connection.prepareStatement(sql);
				statement.setString(1, getRequest().getParameter("cate_name"));
				statement.setString(2, getRequest().getParameter("parent_id"));
				statement.setString(3, id);
				statement.executeUpdate();
				int affectedRowCount = statement.getUpdateCount();// 影响的行数
				System.out.println("affectedRowCount=" + affectedRowCount);
				if (affectedRowCount > 0) {
					this.ajaxHtmlReturn(1, "修改文章分类成功", "");
					return ;
				}
				else{
					this.ajaxHtmlReturn(0, "修改文章分类失败", "");
					return ;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {// 关闭连接
				DBHelper.close(statement, connection);
			}
		} else {
			//文章分类详情
			String sql = "SELECT * FROM " + DBHelper.normalTableName("products_cate") + " WHERE id=?";// 查询列表
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
					hashMap.put("cate_name", resultSet.getString("cate_name"));
					hashMap.put("parent_id", resultSet.getInt("parent_id"));
					hashMap.put("status", resultSet.getString("status"));
					hashMap.put("product_count", resultSet.getString("product_count"));
					this.assign("item", hashMap);
					if(resultSet.getInt("parent_id")!=0){
						this.assign("selectList", this.getSelectList());
					}
					String html = this.fetch("ProductsCate-edit.jsp");
					this.ajaxHtmlReturn(1, "修改文章分类", html);
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {// 关闭连接
				DBHelper.close(resultSet, statement, connection);
			}
			this.ajaxHtmlReturn(0, "文章分类不存在", "");
			return;
		}
	}
}
