package cn.java.jfinal.controller.admin;

import java.io.IOException;

import com.jfinal.core.Controller;

public class UserController extends Controller {
	
	/**
	 * 用户列表
	 *  http://localhost/admin/user/index
	 */
	public void index() {
		renderText("Hello JFinal World.");
	}
	
	/**
	 * 表单页面
	 */
	public void userSet()
	{
		this.renderJsp("/WEB-INF/view/admin/User-userSet.jsp");
	}
	
	/**
	 * 执行删除
	 * @throws IOException 
	 */
	public void userDelete() throws IOException
	{
//		HttpServletResponse response = this.getResponse();
//		PrintWriter printWriter = response.getWriter();
//		response.sendRedirect("/admin/user/index");
//		printWriter.flush();
//		printWriter.close();
		
		this.redirect("/admin/user/index");//重定向，浏览器端跳转
	}
	
	/**
	 * 执行添加 或者 修改
	 */
	public void userAddOrUpdate()
	{
		this.forwardAction("/admin/user/index");//转发，服务器端转发
	}
}