package cn.java.admin.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

import cn.java.core.model.DBHelper;

public class ArticleService {

	/**
	 * 添加记录
	 * 
	 * @param data
	 * @return
	 */
	public HashMap add(HashMap<String, String> data) {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = DBHelper.getConnect();
			String cateId = (String) data.get("cate_id");
			if (!this.checkCate(cateId, connection)) {//分类不存在
				HashMap result = new HashMap();
				result.put("status", 0);
				result.put("info", "插入失败,分类不存在.");
				result.put("data", null);
				return result;
			} else {//分类存在
				String sql = "INSERT INTO " + DBHelper.normalTableName("article") + "(cate_id,article_name,content,weight,thumb_pic,add_time,art_type) VALUES(?,?,?,?,?,?,?)";
				statement = connection.prepareStatement(sql);
				statement.setInt(1, new Integer(cateId));
				statement.setString(2, data.get("article_name"));
				statement.setString(3, data.get("content"));
				statement.setInt(4, new Integer(data.get("weight")));
				statement.setString(5, data.get("thumb_pic"));
				statement.setString(6, String.valueOf(System.currentTimeMillis() / 1000));
				statement.setInt(7, 0);
				statement.executeUpdate();
				int affectedRowCount = statement.getUpdateCount();// 影响的行数
				if (affectedRowCount > 0) {
					HashMap result = new HashMap();
					result.put("status", 1);
					result.put("info", "插入成功.");
					result.put("data", null);
					return result;
				} 
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {// 关闭连接
			DBHelper.close(statement, connection);
		}
		HashMap result = new HashMap();
		result.put("status", 0);
		result.put("info", "插入失败.");
		result.put("data", data);
		return result;
	}
	
	/**
	 * 修改数据
	 * 
	 * @param data
	 * @return
	 */
	public HashMap update(HashMap<String, String> data) {
		//修改文章
		PreparedStatement statement = null;
		Connection connection = null;
		String cateId = (String) data.get("cate_id");
		try {
			if (!this.checkCate(cateId, connection)) {//分类不存在
				HashMap result = new HashMap();
				result.put("status", 0);
				result.put("info", "修改失败");
				result.put("data", data);
				return result;

			} else {//分类存在
				String sql = "UPDATE " + DBHelper.normalTableName("article") + " SET cate_id=?,article_name=?,content=?,weight=?,thumb_pic=?,update_time=?,art_type=? where id=? ";
				connection = DBHelper.getConnect();
				statement = connection.prepareStatement(sql);
				statement.setInt(1, new Integer(cateId));
				statement.setString(2, data.get("article_name"));
				statement.setString(3, data.get("content"));
				statement.setInt(4, new Integer(data.get("weight")));
				statement.setString(5, data.get("thumb_pic"));
				statement.setString(6, String.valueOf(System.currentTimeMillis() / 1000));
				statement.setInt(7, 0);
				statement.setInt(8, new Integer(data.get("id")));
				statement.executeUpdate();
				
				int affectedRowCount = statement.getUpdateCount();// 影响的行数
				System.out.println("affectedRowCount=" + affectedRowCount);
				if (affectedRowCount > 0) {
					HashMap result = new HashMap();
					result.put("status", 1);
					result.put("info", "修改成功");
					result.put("data", null);
					return result;

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {// 关闭连接
			DBHelper.close(statement, connection);
		}
		HashMap result = new HashMap();
		result.put("status", 0);
		result.put("info", "修改失败");
		result.put("data", null);
		return result;
	}


	/**
	 * 删除记录
	 * 
	 * @param data
	 * @return
	 */
	public HashMap delete(String ids) {
		String sql = "DELETE FROM " + DBHelper.normalTableName("article") + " WHERE id IN(?)";
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = DBHelper.getConnect();
			statement = connection.prepareStatement(sql);
			statement.setString(1, ids);
			statement.executeUpdate();
			int affectedRowCount = statement.getUpdateCount();// 影响的行数
			if (affectedRowCount > 0) {
				HashMap result = new HashMap();
				result.put("status", 1);
				result.put("info", "删除成功");
				result.put("data", null);
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {// 关闭连接
			DBHelper.close(statement, connection);
		}
		HashMap result = new HashMap();
		result.put("status", 0);
		result.put("info", "删除失败");
		result.put("data", null);
		return result;
	}

	
	/**
	 * 查找记录
	 * 
	 * @param where
	 * @param whereList
	 * @return
	 */
	public HashMap findOne(HashMap data) {
		//文章详情
		String sql = "SELECT * FROM " + DBHelper.normalTableName("article") + "  WHERE id=? AND art_type=0 ";// 查询列表
		PreparedStatement statement = null;
		Connection connection = null;
		ResultSet resultSet = null;
		try {
			connection = DBHelper.getConnect();
			statement = connection.prepareStatement(sql);
			statement.setString(1, (String) data.get("id"));
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
				
				HashMap result = new HashMap();
				result.put("status", 1);
				result.put("info", "查找成功");
				result.put("data", item);
				return result;
			} 
		} catch (Exception e) {
			e.printStackTrace();
		} finally {// 关闭连接
			DBHelper.close(resultSet, statement, connection);
		}
		
		HashMap result = new HashMap();
		result.put("status", 0);
		result.put("info", "查找失败");
		result.put("data", null);
		return result;
	}

	/**
	 * 检查分类是否存在
	 * 
	 * @param cateId
	 * @param connection
	 * @return
	 */
	protected boolean checkCate(String cateId, Connection connection) {
		//分页查找
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		int totalRows = 0;
		String getCountSql = "select count(*) as totalRows from " + DBHelper.normalTableName("article_cate") + " where id=?";
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
}
