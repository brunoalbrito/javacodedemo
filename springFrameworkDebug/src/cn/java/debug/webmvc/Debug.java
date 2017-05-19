package cn.java.debug.webmvc;

public class Debug {
	public static void doPost() {
		/*
			在Servlet中可以获取 XmlWebApplicationContext 对象
				ServletContext servletContext;
				org.springframework.web.context.support.XmlWebApplicationContext context = servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
			 	org.springframework.context.support.AbstractRefreshableApplicationContext
		 	
		 	
		 	扫描拦截器 Interceptors
		 	扫描处理器Handler（以“/”开头的beanName被认为是Handler）
		 	根据URL地址匹配出Handler，创建HandlerExecutionChain，匹配Handler的Interceptors，并注入到HandlerExecutionChain
		 		
		 			 
		 			 
		 			 		 	
			 	
			 	
			 	
			 	
	 	Hadler的实现方式：
	 		方式一：
	 			在Handler的类声明@RequestMapping注解  + 在Hadler的每个方法声明@RequestMapping注解
	 		方式二：
	 			在Handler的类声明@RequestMapping注解  + 在Handler实现Controller接口
		 
		 反射方法参数注解，根据配置获取响应值
		 	org.springframework.web.bind.annotation.support.HandlerMethodInvoker.resolveHandlerArguments(Method handlerMethod, Object handler, NativeWebRequest webRequest, ExtendedModelMap implicitModel) 
		 	
		  处理非标准方法的返回值
		  	org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter.ServletHandlerMethodInvoker.getModelAndView(...)
		 */
	}
	
	public static void webDotXml(String[] args){
		/**
			<!--  Spring 服务层的配置文件 -->
			<context-param>
		        <param-name>log4jConfigLocation</param-name>
		        <param-value>/WEB-INF/classes/log4j.properties</param-value>
		    </context-param>
		    <context-param>
		        <param-name>contextConfigLocation</param-name>
		        <param-value>/WEB-INF/classes/application*.xml</param-value>
		    </context-param>

		    <filter>
		        <filter-name>CharacterFilter</filter-name>
		        <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
		        <init-param>
		            <param-name>encoding</param-name>
		            <param-value>utf-8</param-value>
		        </init-param>
		        <init-param>
		            <param-name>force</param-name>
		            <param-value>true</param-value>
		        </init-param>
		    </filter>

		    <filter-mapping>
		        <filter-name>CharacterFilter</filter-name>
		        <url-pattern>/*</url-pattern>
		    </filter-mapping>

			<!--  Spring 容器启动监听器 -->
			<listener>             
				<listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
			</listener>

		 	<!--  Spring MVC 的Servlet，启动Spring MVC模块-->
		    <servlet>
		        <servlet-name>dispatcherServlet</servlet-name>
		        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
		        <load-on-startup>1</load-on-startup>
		        <init-param>
			        <param-name>contextConfigLocation</param-name>
	        		<param-value>/WEB-INF/classes/applicationContextConfig.xml</param-value>
        		</init-param>
		        <init-param>
			        <param-name>contextId</param-name>
	        		<param-value>contextId_001</param-value>
        		</init-param>
		        <init-param>
			        <param-name>namespace</param-name>
	        		<param-value>namespace_001</param-value>
        		</init-param>
		        <init-param>
			        <param-name>contextAttribute</param-name>
	        		<param-value>org.springframework.web.context.WebApplicationContext.ROOT</param-value>
        		</init-param>
		    </servlet>
		    <servlet-mapping>
		        <servlet-name>dispatcherServlet</servlet-name>
		        <url-pattern>*.do</url-pattern>
		　　</servlet-mapping>
		 */
	}
}
