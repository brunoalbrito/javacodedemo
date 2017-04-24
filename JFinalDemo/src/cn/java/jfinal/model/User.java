package cn.java.jfinal.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Model;

public class User extends Model<User> {
	/**
	 * 全局共享的，只能用于数据库查询， 不能用于数据承载对象。数据承载需要使用new User().set(…)来实现
	 */
	public static final User dao = new User();
	
	public List<Blog> getBlogs() { 
		return Blog.dao.find("select * from blog where user_id=?", 
				get("id")); 
	} 
}