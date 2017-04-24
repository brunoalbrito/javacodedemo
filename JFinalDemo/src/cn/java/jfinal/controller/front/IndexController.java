package cn.java.jfinal.controller.front;

import com.jfinal.core.Controller;

public class IndexController extends Controller {
	
	public void index() {
		renderText("Hello JFinal World.");
	}

}