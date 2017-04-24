package cn.java.jfinal.routes;

import cn.java.jfinal.controller.admin.AdminController;
import cn.java.jfinal.controller.admin.UserController;

import com.jfinal.config.Routes;

public class AdminRoutes extends Routes {
	public void config() {
		// http://localhost/admin
		// http://localhost/admin/index
		add("/admin", AdminController.class);
		
		// http://localhost/admin/user
		add("/admin/user", UserController.class);
	}
}