package cn.java.admin.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import cn.java.core.model.DBHelper;

public class ArticleCateService {

	/**
	 * 取得顶级分类列表
	 * @return
	 */
	public HashMap getCateById(int cateId){
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		String sql = "SELECT * FROM " + DBHelper.normalTableName("article_cate") + " where status=? AND is_locked=? AND id=?";
		try {
			connection = DBHelper.getConnect();
			statement = connection.prepareStatement(sql);
			statement.setInt(1, 1);
			statement.setInt(2, 0);
			statement.setInt(3, cateId);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				HashMap hashMap = new HashMap();
				hashMap.put("id", resultSet.getInt("id"));
				hashMap.put("cate_name", resultSet.getString("cate_name"));
				hashMap.put("parent_id", resultSet.getInt("parent_id"));
				hashMap.put("status", resultSet.getString("status"));
				hashMap.put("article_count", resultSet.getString("article_count"));
				return hashMap;
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {// 关闭连接
			DBHelper.close(resultSet, statement, connection);
		}
		return null;
	}
	
	/**
	 * 取得顶级分类列表
	 * @return
	 */
	public ArrayList getCateListByParentId(int parentId){
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		ArrayList arrayList = new ArrayList();
		String sql = "SELECT * FROM " + DBHelper.normalTableName("article_cate") + " where status=? AND is_locked=? AND parent_id=?";// 查询列表
		try {
			connection = DBHelper.getConnect();
			statement = connection.prepareStatement(sql);
			statement.setInt(1, 1);
			statement.setInt(2, 0);
			statement.setInt(3, parentId);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				HashMap hashMap = new HashMap();
				hashMap.put("id", resultSet.getInt("id"));
				hashMap.put("cate_name", resultSet.getString("cate_name"));
				hashMap.put("parent_id", resultSet.getInt("parent_id"));
				hashMap.put("status", resultSet.getString("status"));
				hashMap.put("article_count", resultSet.getString("article_count"));
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
}
