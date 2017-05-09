package cn.java.debug;
import java.io.FileInputStream;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.xml.XmlConfiguration;

public class Debug {

	public static void main(String[] args) throws Exception {
		
		String resourceBase = "./webRoot";
		String webXmlPath = "./webRoot/WEB-INF/web.xml";
		String contextPath = "/testContext";
		
		Server server = new Server();
		// 设置配置文件
		{
			String xmlConfigPath = "./cn/java/debug/etc/jetty.xml";
			XmlConfiguration configuration = new XmlConfiguration(new FileInputStream(xmlConfigPath));
			configuration.configure(server); // 设置Server的属性
		}

		// 设置处理器
		{
			ContextHandlerCollection handler = new ContextHandlerCollection();
	        WebAppContext webapp = new WebAppContext();
	        webapp.setContextPath(contextPath);
	        webapp.setDefaultsDescriptor("./cn/java/debug/etc/webdefault.xml");
	        webapp.setResourceBase(resourceBase);
            webapp.setDescriptor(webXmlPath);
            handler.addHandler(webapp);
            
            server.setHandler(handler);
		}
		
		// 启动
		server.start();
		
		System.out.println("current thread:"
                + server.getThreadPool().getThreads() + "| idle thread:"
                + server.getThreadPool().getIdleThreads());
		server.join();
	}
	

}
