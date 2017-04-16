package cn.java.note;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.annotation.Resource.AuthenticationType;
import javax.ejb.EJB;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.PersistenceProperty;
import javax.persistence.PersistenceUnit;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.ws.WebServiceRef;

import org.apache.tomcat.InstanceManager;

public class HelloServlet extends HttpServlet {

	/**
	 * 这个方法在“过滤器链”执行前调用，进行初始化
	 */
	public void init(ServletConfig config) throws ServletException {
		// config === org.apache.catalina.core.StandardWrapperFacade
		// config.getServletContext() === org.apache.catalina.core.ApplicationContextFacade

		// ServletContext 的初始化参数
		ServletContext mServletContext = config.getServletContext();
		String servletContextParam1 = mServletContext.getInitParameter("servletContextParam1");
		System.out.println("servletContextParam1 = " + servletContextParam1);

		// Servlet 的初始化参数
		String servletInitParam1 = config.getInitParameter("servletInitParam1");
		String servletInitParam2 = config.getInitParameter("servletInitParam2");
		System.out.println("servletInitParam1 = " + servletInitParam1 + ", servletInitParam2 = " + servletInitParam2);
		super.init(config);
	}


	/**
	 * 在“过滤器链”的末尾调用
	 */
	public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
		// req == org.apache.catalina.connector.RequestFacade
		// resp == org.apache.catalina.connector.ResponseFacade
		super.service(req, res);
	}
	
	

	/**
	 * web输出
	 */
	private void webWrite(PrintWriter out,String keyName,String valueStr){
		if(valueStr!=null){
			out.write(keyName + " = \"" + valueStr + "\"\n");
		}
		else{
			out.write(keyName + " = null" + "\n");
		}
	}

	/**
	 * 控制台输出
	 */
	private void consoleWrite(PrintStream out,String keyName,String valueStr){
		if(valueStr!=null){
			out.println(keyName + " = \"" + valueStr + "\"");
		}
		else{
			out.println(keyName + " = null" + "");
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		if(req.getParameter("testinclude")!=null){
//			http://localhost:8080/test/hello?testinclude=true
			req.getRequestDispatcher("/WEB-INF/jsp/common/header.jsp").include(req, resp);
			resp.getWriter().write("this is body <br />");
			req.getRequestDispatcher("/WEB-INF/jsp/common/footer.jsp").include(req, resp);
			return;
		}
		else{
			PrintWriter out = resp.getWriter();
			out.write("HelloServlet");

			// 请求地址是： http://localhost:8080/test/hello?a=value1&b=value2

			// 读取http的头部信息---
			String cookieStr = req.getHeader("Cookie"); // H_PS_PSSID=20739_1436_18241_12005; path=/; domain=.baidu.com
			// Content-Type	multipart/form-data; boundary=---------------------------312511929827998
			// Content-Type	application/x-www-form-urlencoded; charset=UTF-8
			// Content-Type	text/html; charset=utf-8
			String contentType = req.getHeader("Content-Type"); 
			String acceptEncoding = req.getHeader("Accept-Encoding"); // gzip, deflate
			this.webWrite(out,"req.getHeader(\"Cookie\")" ,cookieStr);
			this.webWrite(out,"req.getHeader(\"Content-Type\")" ,contentType);
			// req.getHeader("Accept-Encoding") = "gzip, deflate"
			this.webWrite(out,"req.getHeader(\"Accept-Encoding\")" ,acceptEncoding);

			// 读取和设置编码类型
			// req.setCharacterEncoding("utf-8"); // 改变编码类型
			String characterEncoding = req.getCharacterEncoding(); // 头部声明的编码类型，"utf-8"
			this.webWrite(out,"req.getCharacterEncoding()" ,characterEncoding);

			// 读取Servlet的路径
			// <servlet-mapping><url-pattern>/hello/*</url-pattern></servlet-mapping> 
			// req.getServletPath() = "/hello"
			String servletPath = req.getServletPath(); 
			this.webWrite(out,"req.getServletPath()" ,servletPath);

			// 读取查询字符串
			// req.getQueryString() = "a=value1&b=value2"
			String queryString = req.getQueryString();
			this.webWrite(out,"req.getQueryString()" ,queryString);

			// 取得参数
			// req.getQueryString() = "a=value1&b=value2"
			String username = req.getParameter("username");
			this.webWrite(out,"req.getParameter(\"username\")" ,username);

			// 读取上下文地址，如：example
			// req.getContextPath() = "/test"
			String contextPath = req.getContextPath();
			this.webWrite(out,"req.getContextPath()" ,contextPath);

			// 设置属性
			req.setAttribute("key1", "value1...");
			String value1 = (String) req.getAttribute("key1");
			this.webWrite(out,"req.setAttribute(\"key1\",\"value1...\")" ,value1);

			// 应用上下文，生命周期跟服务器一样
			// servletContext == org.apache.catalina.core.ApplicationContextFacade
			// req.getServletContext().getContextPath() = "/test"
			// req.getServletContext().getRealPath("/") = "E:\Repository\EclipseRepository\tomcat8.x\webapps\test\"
			// req.getServletContext().getServletContextName() = "Welcome to Tomcat"
			// req.getServletContext().getServerInfo() = "Apache Tomcat/@VERSION@"
			// req.getServletContext().getResource("/") = "file:/E:/Repository/EclipseRepository/tomcat8.x/webapps/test/"
			ServletContext servletContext = req.getServletContext();
			this.webWrite(out,"req.getServletContext().getContextPath()" ,servletContext.getContextPath());
			this.webWrite(out,"req.getServletContext().getRealPath(\"/\")" ,servletContext.getRealPath("/"));
			this.webWrite(out,"req.getServletContext().getServletContextName()" ,servletContext.getServletContextName());
			this.webWrite(out,"req.getServletContext().getServerInfo()" ,servletContext.getServerInfo());
			this.webWrite(out,"req.getServletContext().getResource(\"/\")" ,servletContext.getResource("/").toString());


			Cookie cookie = new Cookie("cookie1", "value1");
			cookie.setHttpOnly(true);
			cookie.setPath("/");
			cookie.setComment("comment");
			cookie.setMaxAge(3600);
			resp.addCookie(cookie);

			resp.addCookie(new Cookie("cookie2", "value2"));

			// org.apache.catalina.core.ApplicationDispatcher.forward(req, resp);
			//		req.getRequestDispatcher("/hello?parmam1=value1&param2=value2").forward(req, resp);

			// 重定向
			resp.sendRedirect(req.getContextPath()+"/index.html"); 
			return;
		}
		
	}


	/**
	 * 使用实例管理器
	 * 使用实例管理器创建的对象，在对象创建完成后，实例管理器会扫描对象的注解，注入上下文的资源（如：jndi）
	 * @param req
	 * @param resp
	 * @throws IOException
	 */
	private void getUseInstanceManager(HttpServletRequest req, HttpServletResponse resp) throws Exception{
		// 获取实例管理器
		ServletContext servletContext = req.getServletContext();
		InstanceManager instanceManager = (InstanceManager) servletContext.getAttribute(InstanceManager.class.getName());

		// 创建实例
		InstanceManagerTest.MyBean1 myBean1 = (InstanceManagerTest.MyBean1) instanceManager.newInstance("cn.java.note.HelloServlet.InstanceManagerTest.MyBean1");
		InstanceManagerTest.MyBean2 myBean2 = (InstanceManagerTest.MyBean2) instanceManager.newInstance("cn.java.note.HelloServlet.InstanceManagerTest.MyBean2");
	}

	
	private static class InstanceManagerTest {
		/**
		 * 在字段上使用注解
		 * @author Administrator
		 */
		private static class MyBean1 {
			@Resource(name="jndi/mybatis",type=Object.class,authenticationType=AuthenticationType.APPLICATION,shareable=true,description="",mappedName="",lookup="")
			public Object field1;

			@EJB(name="jndi/ejb1",description="description..",beanInterface=Object.class,beanName="",mappedName="")
			public Object field2;

			@WebServiceRef(name="jndi/wsref1",type=Object.class,value=Object.class,wsdlLocation="",mappedName="")
			public Object field3;

			@PersistenceContext(name="jndi/wsref1",unitName="",type=PersistenceContextType.TRANSACTION,properties={
					@PersistenceProperty(name="property1",value="property1Value"),
					@PersistenceProperty(name="property2",value="property2Value")
			})
			public Object field4;

			@PersistenceUnit(name="jndi/wsref1",unitName="")
			public Object field5;
		}

		/**
		 * 在方法上使用注解
		 * @author Administrator
		 */
		private static class MyBean2 {

			@Resource(name="jndi/mybatis",type=Object.class,authenticationType=AuthenticationType.APPLICATION,shareable=true,description="",mappedName="",lookup="")
			public void setResource1(Object field1){

			}

			@EJB(name="jndi/ejb1",description="description..",beanInterface=Object.class,beanName="",mappedName="")
			public void setEJB1(Object field1){

			}

			@WebServiceRef(name="jndi/wsref1",type=Object.class,value=Object.class,wsdlLocation="",mappedName="")
			public void setWebServiceRef1(Object field1){

			}

			@PersistenceContext(name="jndi/wsref1",unitName="",type=PersistenceContextType.TRANSACTION,properties={
					@PersistenceProperty(name="property1",value="property1Value"),
					@PersistenceProperty(name="property2",value="property2Value")
			})
			public void setPersistenceContext1(Object field1){

			}

			@PersistenceUnit(name="jndi/wsref1",unitName="")
			public void setPersistenceUnit1(Object field1){

			}

			/**
			 * 构造函数执行后，执行本方法
			 */
			@PostConstruct()
			public void postConstruct(){

			}

			/**
			 * 在调用析构函数前，执行本方法
			 */
			@PreDestroy()
			public void preDestroy(){

			}
		}
	}
	/**
	 * 取得body的原生字符串
	 */
	private void getAndPrintBodyRaw(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		// 内容如下
		//		-----------------------------173751090716589
		//		Content-Disposition: form-data; name="fname"
		//
		//		fname1
		//		-----------------------------173751090716589
		//		Content-Disposition: form-data; name="lname"
		//
		//		lname1
		//		-----------------------------173751090716589
		//		Content-Disposition: form-data; name="file1"; filename="1.png"
		//		Content-Type: image/png
		//
		//		�PNG
		//		

		// 读取输入流
		// inputStream === org.apache.catalina.connector.CoyoteInputStream
		ServletInputStream inputStream = req.getInputStream();
		int length = 1024;
		byte[] byteBuffer = new byte[length];
		int offset = 0;
		do {
			int inputLen = inputStream.read(byteBuffer, offset, length - offset);
			if (inputLen <= 0) {
				break;
			}
			offset += inputLen;
		} while ((length - offset) > 0);
		System.out.println(new String(byteBuffer));
	}

	/**
	 * 打印http的头部
	 */
	private void getAndPrintHeader(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		//		host = "localhost:8080"
		//		user-agent = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:47.0) Gecko/20100101 Firefox/47.0"
		//		accept = "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"
		//		accept-language = "en-US,zh-CN;q=0.8,zh;q=0.5,en;q=0.3"
		//		accept-encoding = "gzip, deflate"
		//		referer = "http://localhost:8080/test/index.html"
		//		cookie = "cookie1=value1; JSESSIONID=B5B72C0DBD8A1DE4E8AF2E448F3C3298"
		//		connection = "keep-alive"
		//		content-type = "multipart/form-data; boundary=---------------------------173751090716589"
		//		content-length = "5242"

		Enumeration<String> enumeration = req.getHeaderNames();
		while(enumeration.hasMoreElements()){
			String keyName = enumeration.nextElement();
			Enumeration<String> headerEnumeration = req.getHeaders(keyName);
			String valueStr="";
			while(headerEnumeration.hasMoreElements()){
				if(valueStr==""){
					valueStr = headerEnumeration.nextElement();
				}
				else{
					valueStr = valueStr + "\n" + headerEnumeration.nextElement();
				}
			}
			this.consoleWrite(System.out, keyName, valueStr);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// req == org.apache.catalina.connector.RequestFacade
		// resp == org.apache.catalina.connector.ResponseFacade

		// Tomcat支持 Content-Type	multipart/form-data; boundary=---------------------------312511929827998  文件上传
		// Tomcat支持 Content-Type	application/x-www-form-urlencoded; charset=UTF-8 普通post提交
		// Tomcat不支持： Content-Type	text/html; charset=utf-8

		// 提交类型为"multipart/form-data"的时候，要开启<multipart-config>，否则不能使用，req.getParameter("lname");取不到任何数据
		// 当有配置<multipart-config>文件上传功能是，这个字段parts会被填充

		// 提交类型是multipart/form-data，并且没配置<multipart-config>，在Servlet中使用了req.getInputStream() 或者 req.getReader()可以取到数据
		// 提交类型是multipart/form-data，并且没配置<multipart-config>，在Servlet中使用了req.getParameter("lname")取不到数据
		// 提交类型是multipart/form-data，有配置<multipart-config>，在Servlet中使用了req.getParameter("lname")取得到数据，req.getInputStream() 或者 req.getReader()取不到数据
		// 提交类型是application/x-www-form-urlencoded，如果在Servlet中使用了req.getInputStream() 或者 req.getReader(),那么req.getParameter("lname")取不到数据
		// 提交类型是application/x-www-form-urlencoded，如果在Servlet中使用了req.getParameter("lname")，那么req.getInputStream() 或者 req.getReader()取不到数据

		// req.getParameter("lname") = "lname1"
		String lname = req.getParameter("lname");
		this.consoleWrite(System.out, "req.getParameter(\"lname\")" ,lname);

		// 打印http的头部
		this.getAndPrintHeader(req,resp);

		// 打印http的原生body内容
		this.getAndPrintBodyRaw(req,resp);


		// 应用上下文，生命周期跟服务器一样
		// servletContext == org.apache.catalina.core.ApplicationContextFacade
		ServletContext servletContext = req.getServletContext(); // 第一请求为null，第二次请求为context_value1
		System.out.println(servletContext.getAttribute("context_key1"));
		servletContext.setAttribute("context_key1", "context_value1");

		//		req.changeSessionId();//修改sessionId

		Cookie[] cookies = req.getCookies();
		for(Cookie cookie :cookies){
			String valueStr = "";
			valueStr += "domain = " + cookie.getDomain() + ";";
			valueStr += "value = " + cookie.getValue() + ";";
			valueStr += "path = " + cookie.getPath() + ";";
			valueStr += "version = " + cookie.getVersion() + ";";
			valueStr += "comment = " + cookie.getComment() + ";";
			valueStr += "version = " + cookie.getVersion() + ";";
			valueStr += "secure = " + cookie.getSecure() + ";";
			valueStr += "isHttpOnly = " + cookie.isHttpOnly() + ";";
			// 除了cookieName 和cookieValue外，其他值读取都没有意义，因为那些值是控制客户端的
			this.consoleWrite(System.out, cookie.getName(), valueStr);
		}

		// httpSession org.apache.catalina.session.StandardSessionFacade
		HttpSession httpSession = req.getSession();
		httpSession.setAttribute("userInfo", "userInfo_Value");
		httpSession.getAttribute("userInfo");


		// 设置响应内容
		resp.setCharacterEncoding("utf-8");
		resp.addCookie(new Cookie("cookie1", "value1"));
		resp.addHeader("Content-Type", "text/html; charset=utf-8");

		// 转发
		req.getRequestDispatcher("/index.html").forward(req, resp);
	}
	
	/**
	 * 转发器
	 */
	public void testApplicationDispatcher(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.sendRedirect(req.getContextPath()+"/index.html"); 
		req.getRequestDispatcher("/index.html").forward(req, resp);
		req.getRequestDispatcher("/index.html").include(req, resp);
	}
}
