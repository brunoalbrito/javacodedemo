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
public class ArticleSerlvet extends AdminCommonSerlvet {

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
		try {
			int totalRows = 0;
			String getCountSql = "SELECT count(*) AS totalRows FROM " + DBHelper.normalTableName("article") + " where status=1 AND art_type=0 ";
			connection = DBHelper.getConnect();
			statement = connection.prepareStatement(getCountSql);
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				totalRows = Integer.valueOf(resultSet.getString("totalRows"));
			}

			if (totalRows > 0) {
				int perPageShowCount = new Integer(ApplicationConfig.getConfig("app_pagination_showRangCount"));
				int currPage = 1;
				if (getRequest().getParameter("page") != null && (!getRequest().getParameter("page").equals(""))) {
					currPage = new Integer(getRequest().getParameter("page"));
				}
				// 分页查询
				String sql = "SELECT art.*,art_cate.cate_name FROM " + DBHelper.normalTableName("article") + " AS art LEFT JOIN " + DBHelper.normalTableName("article_cate") + " AS art_cate ON art.cate_id=art_cate.id "+
						" where art.status=1 AND art.art_type=0 LIMIT ? , ? "; // 查询列表

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

			} else {
				this.assign("itemList", null);
				this.assign("pagination", "");
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.assign("itemList", null);
			this.assign("pagination", "");
		} finally {// 关闭连接
			DBHelper.close(resultSet, statement, connection);
		}
		this.display("Article-index.jsp");
	}


	/**
	 * 添加文章
	 * @throws ServletException
	 * @throws IOException
	 */
	public void addAction() throws ServletException, IOException {
		ArticleCateService mArticleCateService = new ArticleCateService();
		if (getRequest().getMethod().equals("GET")) {
			//一级分类
			ArrayList<HashMap> levelOneCateList = mArticleCateService.getCateListByParentId(0);
			this.assign("item",null);
			this.assign("haveTwoLevel",null);
			this.assign("levelOneCateId",null);
			this.assign("levelOneCateList",levelOneCateList);
			this.assign("levelTwoCateList",null);
			this.display("Article-add.jsp");
		} else {
			//构造数据
			HashMap<String, String> item = new HashMap();
			item.put("article_name", getRequest().getParameter("article_name"));
			item.put("weight", getRequest().getParameter("weight"));
			item.put("thumb_pic", getRequest().getParameter("thumb_pic"));
			item.put("cate_id", getRequest().getParameter("cate_id"));
			item.put("content", getRequest().getParameter("content"));

			//Service调用，插入数据
			ArticleService artService = new ArticleService();
			HashMap result = artService.add(item);

			//Service调用结果
			if((Integer) result.get("status")==1){
				getRequest().getRequestDispatcher(UrlHelper.url("admin", "article", "index")).forward(getRequest(), getResponse());//到文章列表页
			} else {//插入失败
				ArrayList<HashMap> levelOneCateList = mArticleCateService.getCateListByParentId(0);
				this.assign("item", (HashMap) result.get("data"));
				this.assign("haveTwoLevel",null);
				this.assign("levelOneCateId",null);
				this.assign("levelOneCateList",levelOneCateList);
				this.assign("levelTwoCateList",null);
				this.display("Article-add.jsp");
			}
		}
	}


	/**
	 * 修改文章
	 * 
	 * @throws ServletException
	 * @throws IOException
	 */
	public void editAction() throws ServletException, IOException {
		String method = getRequest().getMethod();
		String id = getRequest().getParameter("id");
		if (method.equals("POST")) {
			//构造数据
			HashMap data = new HashMap();
			data.put("id", id);
			data.put("article_name", getRequest().getParameter("article_name"));
			data.put("weight", getRequest().getParameter("weight"));
			data.put("thumb_pic", getRequest().getParameter("thumb_pic"));
			data.put("cate_id", getRequest().getParameter("cate_id"));
			data.put("content", getRequest().getParameter("content"));

			//Service调用
			ArticleService artService = new ArticleService();
			HashMap result = artService.update(data);

			//Service调用结果
			if((Integer) result.get("status")==1){//修改成功
				getRequest().getRequestDispatcher(UrlHelper.url("admin", "article", "index")).forward(getRequest(), getResponse());//到文章列表页
			}
			else{
				this.assign("item",data);
				this.display("Art-edit.jsp");
			}
		} else {
			//构造数据
			HashMap data = new HashMap();
			data.put("id", id);

			//Service调用
			ArticleService artService = new ArticleService();
			HashMap result = artService.findOne(data);
			//Service调用结果
			int status = new Integer(result.get("status").toString());
			HashMap itemInfo = (HashMap)result.get("data");
			if(status>0){//查找到
				this.assign("item", itemInfo);
				//父分类信息
				ArticleCateService mArticleCateService = new ArticleCateService();
				ArrayList<HashMap> levelOneCateList = mArticleCateService.getCateListByParentId(0);
				//分类信息
				int cateId = new Integer(itemInfo.get("cate_id").toString());
				HashMap cateInfo = mArticleCateService.getCateById(cateId);
				//如果有父级别分类
				if(cateInfo.containsKey("parent_id")){
					ArrayList levelTwoCateList = mArticleCateService.getCateListByParentId((Integer)cateInfo.get("parent_id"));
					this.assign("haveTwoLevel",true);
					this.assign("levelTwoCateList",levelTwoCateList);
					this.assign("levelOneCateId",cateInfo.get("parent_id"));

				}
				else{
					this.assign("haveTwoLevel",false);
					this.assign("levelTwoCateList",null);
					this.assign("levelOneCateId",itemInfo.get("cate_id"));
				}
				this.assign("levelOneCateList",levelOneCateList);
				this.display("Article-edit.jsp");
			}
			else{
				getRequest().getRequestDispatcher(UrlHelper.url("admin", "article", "index")).forward(getRequest(), getResponse());//到文章列表页
			}

		}
	}


	/**
	 * 删除
	 * 
	 * @throws ServletException
	 * @throws IOException
	 */
	public void deleteAction() throws ServletException, IOException {
		//Service调用
		ArticleService articleService = new ArticleService();
		HashMap result = articleService.delete(getRequest().getParameter("ids"));

		//Service调用结果
		this.ajaxReturn(result);
	}

}
