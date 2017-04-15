package cn.java.jfinal.model;

import com.jfinal.plugin.activerecord.Model;

public class Blog extends Model<Blog>{ 
	public static final Blog dao = new Blog(); 

	public User getUser() { 
		return User.dao.findById(get("user_id")); 
	} 
} 
