package cn.java.jfinal.controller.admin;

import com.jfinal.core.Controller;

public class AdminController extends Controller {
	public void index() {
		renderText("Hello JFinal World.");
	}
}