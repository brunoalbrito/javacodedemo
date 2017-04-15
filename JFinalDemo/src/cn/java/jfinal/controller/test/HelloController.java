package cn.java.jfinal.controller.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.java.jfinal.interceptor.Demo1Interceptor;
import cn.java.jfinal.interceptor.Demo2Interceptor;
import cn.java.jfinal.interceptor.Demo3Interceptor;
import cn.java.jfinal.model.Blog;
import cn.java.jfinal.model.User;
import cn.java.jfinal.service.BlogService;

import com.jfinal.aop.Before;
import com.jfinal.aop.ClearInterceptor;
import com.jfinal.aop.ClearLayer;
import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jfinal.plugin.ehcache.IDataLoader;
import com.jfinal.plugin.spring.Inject;
import com.jfinal.upload.UploadFile;

@Before(Demo1Interceptor.class)
// 配置一个Controller级别的拦截器
public class HelloController extends Controller {

	
	/**
	 * 配置 me.setUrlParaSeparator("=");
	 * http://localhost/hello/index?username=zhouzhian
	 * 
	 * 配置 me.setUrlParaSeparator("-");
	 * http://localhost/hello/index?username-zhouzhian
	 * @throws IOException 
	 * @throws ServletException 
	 */
	public void index() throws IOException, ServletException {
		//取得参数
		String username = this.getPara("username");
		System.out.println("username:"+username);//username:zhouzhian
		
		String keyName = this.getPara(0);
		System.out.println("keyName:"+keyName);//keyName:index
		
		//设置模板数据
		this.setAttr("tplDataTest", "要放到模板的数据");
		
		boolean isUsePrototypeCode = false;
		if(isUsePrototypeCode)//使用原生JSP代码输出
		{
			//输出头
//			this.getResponse().setContentType("text/html; charset=utf-8");
			this.getResponse().setHeader("Content-type", "text/html; charset=utf-8");
//			System.out.println(this.getResponse().getContentType());
			
			//renderText
			HttpServletResponse response = this.getResponse();
			PrintWriter printWriter = response.getWriter();
			printWriter.write("打印输出...");
			printWriter.flush();
			printWriter.close();
		
			//renderJsp  所谓的MVC的VIEW层渲染
//			HttpServletRequest request = this.getRequest();
//			request.getRequestDispatcher("/WEB-INF/view/test/Hello-index.jsp").forward(request, response);
			
		}
		else //所谓的MVC
		{
			//输出JSON数据
			//this.renderJson("{\"message\":\"Please input password!\"}");
			
//			HttpServletRequest request = this.getRequest();
//			request.setAttribute("blogList","blogListx");
//			request.setAttribute("user","userx");
//			this.renderJson(new String[]{"blogList", "user"});
			
			//输出文本
//			renderText("Hello JFinal World.");
			
			//使用MVC跳转
			//this.render("/WEB-INF/view/test/Hello-index.jsp");//render使用配置文件中配置的渲染 。如：me.setViewType(ViewType.JSP);
			this.renderJsp("/WEB-INF/view/test/Hello-index.jsp");
		}
		
	}
	
	/**
	 * http://localhost/hello http://localhost/hello/index
	 */
	public void index2() {

		// HttpServletRequest request = this.getRequest();
		// HttpServletResponse response = this.getResponse();

		// 参数
		String username = this.getPara("username");

		// cookie
		String language = this.getCookie("language");
		// this.setCookie("language", "zh-cn", 3600,"/");
		this.removeCookie("language");

		// session
		boolean isLogined = this.getSessionAttr("isLogined");
		this.setSessionAttr("isLogined", true);
		this.removeSessionAttr("isLogined");

		// 文件上传
		// 如果客户端请求为multipart request（form表单使用了enctype="multipart/form-data"），
		// 必须先调用getFile系列方法才 能使getPara系列方法正常工作
		UploadFile uploadFile = this.getFile("imgs");
		String saveDirectory = uploadFile.getSaveDirectory();
		String fileName = uploadFile.getFileName();
		String originalFileName = uploadFile.getOriginalFileName();
		File fileTemp = uploadFile.getFile();
		try {
			FileInputStream fileInputStream = new FileInputStream(fileTemp);
			File fileDes = new File("./des.jpg");
			FileOutputStream fileOutputStream = new FileOutputStream(fileDes);
			byte[] bufferTemp = new byte[1024];
			int byteLen = fileInputStream.read(bufferTemp);
			while (byteLen != -1) {
				fileOutputStream.write(bufferTemp);
				byteLen = fileInputStream.read(bufferTemp);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		// 生成token
		// this.createToken();

		// 重定向
		// this.redirect301("http://www.baidu.com/");
		// 转发
		// this.forwardAction("/hello/actionName0");
		this.setAttr("viewDataUsername", username);// HttpServletRequest.setAttribute(String,
													// Object)

		renderText("Hello JFinal World.");
	}

	/***************************************************
	 * 渲染器 *************************************************
	 */
	public void renderDemo() {
		// Render render = this.getRender();
		this.render("/other_path/test.html");
		this.renderFreeMarker("/other_path/test.html");
		this.renderJsp("test.html");
		// this.renderJson(“users”, userList);
		this.renderFile("test.zip");
	}

	/***************************************************
	 * 路由，访问路径 *************************************************
	 */
	/**
	 * 
	 * http://localhost/hello/actionName0
	 */
	public void actionName0() {
		renderText("Hello JFinal World.");
	}

	/**
	 * 原来访问地址 http://localhost/hello/actionName1 变为了
	 * http://localhost/actionName1
	 */
	@ActionKey("/actionName1")
	public void actionName1() {

	}

	/**************************************************
	 * 拦截器 *************************************************
	 */

	@Before({ Demo2Interceptor.class, Demo3Interceptor.class })
	// 配置多个action级别的拦截器
	public void actionName2() {
		renderText("配置一个action级别的拦截器");
	}

	@Before(Demo3Interceptor.class)
	// 配置一个action级别的拦截器
	public void actionName3() {
		renderText("配置多个action级别的拦截器");
	}

	@ClearInterceptor
	public void actionName4() {
		renderText("清除上一级别(Controller级)的拦截器");
	}

	@ClearInterceptor(ClearLayer.ALL)
	public void actionName5() {
		renderText("清除所有级别(Global级与Controller级)的拦截器");
	}

	/**************************************************
	 * 数据库CURD *************************************************
	 */
	public void curdDemo() {
		// 创建name属性为James,age属性为25的User对象并添加到数据库
		new User().set("name", "James").set("age", 25).save();

		// 创建name属性为James,age属性为25的User对象并添加到数据库
		new User().set("name", "James").set("age", 25).save();

		// 删除id值为25的User
		User.dao.deleteById(25);

		// 查询id值为25的User将其name属性改为James并更新到数据库
		User.dao.findById(25).set("name", "James").update();

		// 查询id值为25的user, 且仅仅取name与age两个字段的值
		User user = User.dao.findById(25, "name, age");

		// 获取user的name属性
		String userName = user.getStr("name");

		// 获取user的age属性
		Integer userAge = user.getInt("age");

		// 查询所有年龄大于18岁的user
		List<User> users = User.dao.find("select * from user where age>18");

		// 分页查询年龄大于18的user,当前页号为1,每页10个user
		Page<User> userPage = User.dao.paginate(1, 10, "select * ",
				"from user	where age > ?", 18);
		
		//使用缓存
		List<User> userList = User.dao.findByCache("cacheName", "key","select * from user"); 
				setAttr("blogList", userList).render("list.html"); 

		//关联查询
		String sql = "select b.*, u.user_name from blog b inner join user u on b.user_id=u.id where b.id=?"; 
		User user2 = User.dao.findFirst(sql, 123); 
		String name = user2.getStr("user_name");
	}

	/**
	 * Record相当于一个通用的Model
	 */
	public void curdRecordDemo() {
		// 创建name属性为James,age属性为25的record对象并添加到数据库
		Record user = new Record().set("name", "James").set("age", 25);
		Db.save("user", user);

		// 删除id值为25的user表中的记录
		Db.deleteById("user", 25);

		// 查询id值为25的Record将其name属性改为James并更新到数据库
		user = Db.findById("user", 25).set("name", "James");
		Db.update("user", user);

		// 查询id值为25的user, 且仅仅取name与age两个字段的值
		user = Db.findById("user", 25, "name, age");

		// 获取user的name属性
		String userName = user.getStr("name");

		// 获取user的age属性
		Integer userAge = user.getInt("age");

		// 查询所有年龄大于18岁的user
		List<Record> users = Db.find("select * from user where age > 18");

		// 分页查询年龄大于18的user,当前页号为1,每页10个user
		Page<Record> userPage = Db.paginate(1, 10, "select *",
				"from user where age > ?", 18);
	
	}

	/**
	 * 事务
	 */
	public void transactionDemo() {
		boolean succeed = Db.tx(new IAtom() {
			public boolean run() throws SQLException {
				int count = Db.update(
						"update account set cash = cash - ? where	id = ?", 100,
						123);
				int count2 = Db.update(
						"update account set cash = cash + ? where id = ?", 100,
						456);
				return count == 1 && count2 == 1;
			}
		});
	}
	
	
	/**
	 * 关于Oracle的数据库操作
	 */
	public void oracleCURDDemo()
	{
		//Oracle数据库会自动将属性名(字段名)转换成大写，所以需要手动指定主键名为大写
		//Oracle并未直接支持自增主键。要让Oracle支持自动主键主要分为两步：一是创建序列，二是在model中使用这个序列
		
		/**
		 CREATE SEQUENCE MY_SEQ 
		  INCREMENT BY 1 
		  MINVALUE 1 
		  MAXVALUE 9999999999999999 
		  START WITH 1 
		  CACHE 20; 
		 */
		// 创建User并使用序列 
		User user = new User().set("id", "my_seq.nextval").set("age", 18); 
		user.save(); 
		// 获取id值 
		Integer id = user.get("id"); 
		
		//***************************************
		//			多数据源
		//***************************************
		// 查询 dsMysql数据源中的 user 
		List<Record> users = Db.use("mysql").find("select * from user"); 
		// 查询 dsOracle数据源中的 blog 
		List<Record> blogs = Db.use("oracle").find("select * from blog");
		
	}
	
	/**
	 * 缓存
	 */
	public void cacheDemo()
	{
		//取得缓存内容   get(String cacheName, Object key)
		List<Blog> blogList = CacheKit.get("blog", "blogList"); 
		if (blogList == null) { 
		  blogList = Blog.dao.find("select * from blog"); 
		  // 放入缓存  put(String cacheName,Object  key, Object  value)
		  CacheKit.put("blog", "blogList", blogList); 
		} 
		this.setAttr("blogList", blogList); 
		this.render("blog.html"); 
		
		// 重载 get方法
		List<Blog> blogList2 = CacheKit.get("blog", "blogList", new 
				IDataLoader(){ 
			public Object load() { 
				return Blog.dao.find("select * from blog"); 
			}}); 
		setAttr("blogList", blogList2); 
		render("blog.html"); 
	}
	
	//依赖注入
	@Inject.BY_NAME 
	private BlogService blogService; 
	
	
}