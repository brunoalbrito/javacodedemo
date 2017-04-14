package cn.java.note.all;

public class 关于SpringMvcDebug {
	public static void doPost() {
//		在Servlet中可以获取 XmlWebApplicationContext 对象
//		ServletContext servletContext;
//		org.springframework.web.context.support.XmlWebApplicationContext context = servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
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
		        <param-name>contextConfigLocation</param-name>
        		<param-value>/WEB-INF/classes/applicationContextConfig.xml</param-value>
		    </servlet>
		    <servlet-mapping>
		        <servlet-name>dispatcherServlet</servlet-name>
		        <url-pattern>*.do</url-pattern>
		　　</servlet-mapping>
		 */
	}
}
