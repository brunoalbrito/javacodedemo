package cn.java.jfinal.routes;

import cn.java.jfinal.controller.front.BlogController;
import cn.java.jfinal.controller.front.IndexController;

import com.jfinal.config.Routes;

public class FrontRoutes extends Routes {
	public void config() {
		// http://localhost/index
		add("/index", IndexController.class);
		// http://localhost/blog
		add("/blog", BlogController.class);
	}
}