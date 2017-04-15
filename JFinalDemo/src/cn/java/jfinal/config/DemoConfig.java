package cn.java.jfinal.config;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cn.java.jfinal.controller.test.HelloController;
import cn.java.jfinal.handler.Demo0Handler;
import cn.java.jfinal.handler.Demo1Handler;
import cn.java.jfinal.interceptor.Demo0Interceptor;
import cn.java.jfinal.model.Blog;
import cn.java.jfinal.model.User;
import cn.java.jfinal.routes.AdminRoutes;
import cn.java.jfinal.routes.FrontRoutes;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.Config;
import com.jfinal.plugin.activerecord.Table;
import com.jfinal.plugin.activerecord.dialect.Dialect;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.jfinal.render.ViewType;

/*
 在使用JSP访问的时候，会处理JSP解析出错。原因是JRE的路径设置出错。
 
在eclipse——>Window——>prefence——>java——>Installed JREs

PS:网上的解决方案我试了不成功，主要原因就是因为jre配置环境的错误，在选择jre环境的时候，有两个选择，
	一个是java根目录下的jre（选择这个会不成功），
	一个是在jdk根目录下的jre（选择这个成功）！！
 
 
 <filter> 
 <filter-name>jfinal</filter-name> 
 <filter-class>com.jfinal.core.JFinalFilter</filter-class> 
 <init-param> 
 <param-name>configClass</param-name> 
 <param-value>cn.java.jfinal.DemoConfig</param-value> 
 </init-param> 
 </filter> 

 <filter-mapping> 
 <filter-name>jfinal</filter-name> 
 <url-pattern>/*</url-pattern> 
 </filter-mapping> 
 */
/**
 * com.jfinal.core.JFinalFilter
 * 
 * @author zhazhou
 * 
 * com.jfinal.core.JFinal
 *
 */
public class DemoConfig extends JFinalConfig {
	
	/**
	 * 配置常量
	 */
	@Override
	public void configConstant(Constants me) {
		//me.setDevMode(true);//调试模式
		me.setViewType(ViewType.JSP); // JFinal支持JSP、FreeMarker、Velocity三种常用视图,不进行配置时的缺省配置为FreeMarker
		me.setUrlParaSeparator("=");//变量的分割符号
		me.setEncoding("UTF-8");
//		me.setI18n("zh-cn", Locale.CHINESE, 60);
	}

	/**
	 * 配置路由
	 */
	@Override
	public void configRoute(Routes me) {
		me.add(new FrontRoutes()); // 前端路由
		me.add(new AdminRoutes()); // 后端路由
		// http://localhost/hello
		// http://localhost/hello/index
		me.add("/hello", HelloController.class); //
	}

	/**
	 * 数据库的方言
	 */
	private void testDialect()
	{
		MysqlDialect mysqlDialect = new MysqlDialect();
		mysqlDialect.getDefaultPrimaryKey();//取得默认主键
		mysqlDialect.forTableBuilderDoBuild("表名");//"select * from `" + tableName + "` where 1 = 2"; 取得表的结构
//		mysqlDialect.forModelSave(table, attrs, sql, paras);
//		mysqlDialect.forDbSave(sql, paras, tableName, record);
//		mysqlDialect.fillStatement(pst, paras);
//		mysqlDialect.forModelFindById(table, columns);
		
		/*
		new User().set("age", 25)
		{
			if (getTable().hasColumnLabel("age")) {// 表存在字段   columnTypeMap.containsKey(columnLabel);
				attrs.put("age", value);
				getModifyFlag().add("age");	// 标明要修改的字段
				return (M)this;
			}
		}
		.save();
		{
			Config config = getConfig();
			Table table = getTable();
			
			StringBuilder sql = new StringBuilder();
			List<Object> paras = new ArrayList<Object>();
			
			config.dialect.forModelSave(table, attrs, sql, paras);//构造SQL语句,包含占位符
			pst = conn.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
			config.dialect.fillStatement(pst, paras);//填充占位符
			result = pst.executeUpdate();
			getGeneratedKey(pst, table);//ResultSet rs = pst.getGeneratedKeys();
			getModifyFlag().clear();//清空要修改的字段
		}
		
		*/
	}
	
	/**
	 * 配置插件
	 * com.jfinal.core.Config.startPlugins是这样启动插件的：迭代调用Plugin的start方法
	 */
	@Override
	public void configPlugin(Plugins me) {
		
		/**
		 * Mysql数据库
		 * 
		*/	 
////		loadPropertyFile("your_app_config.txt");
//		String jdbcUrl = "jdbc:mysql://localhost/db_name";
//		String jdbcUserName = "root";
//		String jdbcPassword = "";
////		jdbcUrl = getProperty("jdbcUrl");
////		jdbcUserName = getProperty("user");
////		jdbcPassword = getProperty("password");
//		
//		// C3p0 数据库连接池插件
//		C3p0Plugin c3p0Plugin = new C3p0Plugin(jdbcUrl, jdbcUserName,
//				jdbcPassword);//c3p0Plugin。start();
//		me.add(c3p0Plugin);
//		// ActiveRecord 数据库访问插件
//		ActiveRecordPlugin arp = new ActiveRecordPlugin("main",c3p0Plugin);//arp。start();取得表结构，
//		arp.setDialect(new MysqlDialect()); //mysql数据库方言，针对每种数据库，作调整，如生成不同的SQL语句等
//		me.add(arp);
//		arp.addMapping("user", User.class);// 建立数据库表名到Model的映射关系,表的主键名为默认为“id”
//		arp.addMapping("blog", Blog.class);// 建立数据库表名到Model的映射关系,表的主键名为默认为“id”
////		arp.addMapping("user", "user_id", User.class);// 如果主键名称为 “user_id”则需要手动指定
	
		
		
		/*****************************************
		 * 			oracle数据库
		 * ***************************************
		 */
//		C3p0Plugin c3p0Plugin4Oracle = new C3p0Plugin(jdbcUrl, jdbcUserName,
//				jdbcPassword); 
//		//配置Oracle驱动 
//		c3p0Plugin4Oracle. setDriverClass("oracle.jdbc.driver.OracleDriver"); 
//		me.add(c3p0Plugin4Oracle); 
//		ActiveRecordPlugin activeRecordPlugin4Oracle = new ActiveRecordPlugin(c3p0Plugin4Oracle); 
//		me.add(activeRecordPlugin4Oracle); 
//		// 配置Oracle方言 
//		activeRecordPlugin4Oracle.setDialect(new OracleDialect()); 
//		// 配置属性名(字段名)大小写不敏感容器工厂 
//		activeRecordPlugin4Oracle.setContainerFactory(new CaseInsensitiveContainerFactory()); 
//		activeRecordPlugin4Oracle.addMapping("user", "user_id", User.class); 
		
		/*****************************************************
		 * 				多数据源
		 * *****************************************************
		 */
//		// mysql 数据源 
//		C3p0Plugin dsMysql = new C3p0Plugin(jdbcUrl, jdbcUserName,
//				jdbcPassword); 
//		me.add(dsMysql); 
//		  
//		// mysql ActiveRecrodPlugin 实例，并指定configName为 mysql 
//		ActiveRecordPlugin arpMysql = new ActiveRecordPlugin("mysql", dsMysql); 
//		me.add(arpMysql); 
//		arpMysql.setCache(new EhCache()); 
//		arpMysql.addMapping("user", User.class); 
//		  
//		// oracle 数据源 
//		C3p0Plugin dsOracle = new C3p0Plugin(jdbcUrl, jdbcUserName,
//				jdbcPassword); 
//		me.add(dsOracle); 
//		  
//		// oracle ActiveRecrodPlugin 实例，并指定configName为 oracle 
//		ActiveRecordPlugin arpOracle = new ActiveRecordPlugin("oracle", dsOracle);
//		me.add(arpOracle); 
//		arpOracle.setDialect(new OracleDialect()); 
//		arpOracle.setTransactionLevel(8); 
//		arpOracle.addMapping("blog", Blog.class); 
		
		//缓存
//		me.add(new EhCachePlugin()); 
		
		/**
		 * spring  IOC AOP 
		 * 
		 * 	创建SpringPlugin 对象时未指定构造方法中的形参，SpringPlugin 将自动去
		 *	WebRoot/WEB-INF 目录下寻找applicationContext.xml作为配置文件进行初始化
		 */
//		me.add(new SpringPlugin()); 
	}

	
	/**
	 * AOP 
	 * 
	 * 配置拦截器 Interceptor 配置粒度分为 Global、Controller、Action三个层次
	 */
	@Override
	public void configInterceptor(Interceptors me) {
		// 此处配置的拦截器将拦截所有Action
		me.add(new Demo0Interceptor());

	}

	/**
	 * 配置处理器
	 */
	@Override
	public void configHandler(Handlers me) {
		me.add(new Demo1Handler());
		me.add(new Demo0Handler());
	}

	/**
	 * 系统启动完成后回调afterJFinalStart()方法
	 */
	@Override
	public void afterJFinalStart() {
		super.afterJFinalStart();
		System.out.println("afterJFinalStart......");
	}

	/**
	 * 系统关闭前回调 beforeJFinalStop()方法
	 */
	@Override
	public void beforeJFinalStop() {
		// TODO Auto-generated method stub
		super.beforeJFinalStop();
		System.out.println("beforeJFinalStop......");
	}

}
