package cn.java.note;
import org.apache.catalina.startup.Bootstrap;
public class Main {

	public static void main(String[] args) {
		Bootstrap.main(args);

//				Bootstrap.main(new String[]{"stop"});
	}

	public static void pipeLink() {
//		org.apache.catalina.connector.CoyoteAdapter;
//			org.apache.catalina.core.StandardEngineValve;
//				org.apache.catalina.core.StandardHostValve;
//					org.apache.catalina.core.StandardContextValve;
//						org.apache.catalina.core.StandardWrapperValve;
		
	}
	
	public static void serverLink() {
//				org.apache.catalina.core.StandardServer;
//				org.apache.catalina.core.StandardService;
//					org.apache.catalina.connector.Connector;
//					org.apache.catalina.core.StandardEngine;
//					org.apache.catalina.startup.EngineConfig;
//						org.apache.catalina.core.StandardHost;
//						org.apache.catalina.startup.HostConfig;
//							org.apache.catalina.core.StandardContext;
//							org.apache.catalina.startup.ContextConfig;
//		 						org.apache.catalina.core.StandardWrapper
//									org.apache.jasper.servlet.JspServlet
//									cn.java.note.HelloServlet

	}

	public static void xmlRuleSet() {
//		---------------解析web.xml文件----------------
//		 org.apache.catalina.startup.ContextConfig.lifecycleEvent(LifecycleEvent event)
//		 org.apache.catalina.startup.ContextConfig.configureStart()
//		 org.apache.catalina.startup.ContextConfig.webConfig()
//		 org.apache.catalina.startup.ContextConfig.getContextWebXmlSource()
//		 《解析web.xml文件》
//		 org.apache.tomcat.util.descriptor.web.WebXml
//		 org.apache.tomcat.util.descriptor.web.WebRuleSet  // web.xml文件的解析规则
		
//		---------------解析xxx.tld文件----------------
//		org.apache.jasper.servlet.JasperInitializer  jasper.jar
//		------
//		org.apache.catalina.core.StandardContext.startInternal() 
//		org.apache.jasper.servlet.JasperInitializer.onStartup()
//		 《解析xxx.tld文件》
//		org.apache.tomcat.util.descriptor.tld.TaglibXml
//		org.apache.tomcat.util.descriptor.tld.TldRuleSet // xxx.tld文件的解析规则
		
		
	}

}
