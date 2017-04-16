package cn.java.note.initializer;

import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.jasper.compiler.TldCache;
import org.apache.jasper.servlet.TldScanner;


public class Main {

	public static void test() throws Exception {
		// jsp扩展标签
		
		
		// jsp扩展标签的声明信息，通过初试化器进行初始化--------------1
		class org.apache.catalina.core.StandardContext{
			protected synchronized void startInternal() throws LifecycleException {
				fireLifecycleEvent(Lifecycle.CONFIGURE_START_EVENT, null);
			}
			
		}
		
		class org.apache.catalina.startup.ContextConfig{
			public void lifecycleEvent(LifecycleEvent event) {
				context = (Context) event.getLifecycle(); // 取得触发者 org.apache.catalina.core.StandardContext
				configureStart();
			}
			
			 protected synchronized void configureStart() {
				 webConfig();//!!!!  核心
			 }
			 
			 protected void webConfig() {
				// 解析类路径中"META-INF/services/javax.servlet.ServletContainerInitializer"文件内容
		            // 创建文件中声明的类型对象，并把创建对象转成ServletContainerInitializer类型的引用
//		        	initializerClassMap{
//		            	'MyServletContainerInitializer1_Obj'=>[],
//		            	'MyServletContainerInitializer2_Obj'=>[],
//		            }
//		        	typeInitializerMap{
//		            	'MyAnnotation1.class'=>[MyServletContainerInitializer1_Obj ],
//		            	'MyAnnotation2.class'=>[MyServletContainerInitializer2_Obj ]
//		            }
		            processServletContainerInitializers(); // 查看实现ServletContainerInitializer的初始化器
		            for (Map.Entry<ServletContainerInitializer,Set<Class<?>>> entry : initializerClassMap.entrySet()) {
	                    if (entry.getValue().isEmpty()) { // 添加Servlet容器初始化器到StandardContext
	                    	// context === org.apache.catalina.core.StandardContext
	                        context.addServletContainerInitializer(entry.getKey(), null);
	                    } else {
	                        context.addServletContainerInitializer(entry.getKey(), entry.getValue());
	                    }
	                }
			 }
			 
			 protected void processServletContainerInitializers() {
	            	// 容器初始化器
	                WebappServiceLoader<ServletContainerInitializer> loader = new WebappServiceLoader<>(context);
	                // org.apache.jasper.servlet.JasperInitializer  jasper.jar
	                // org.apache.tomcat.websocket.server.WsSci    tomcat-websocket.jar
	                // 解析类路径中"META-INF/services/javax.servlet.ServletContainerInitializer"文件内容
	                // 创建文件中声明的类型对象，并把创建对象转成ServletContainerInitializer类型的引用
	                detectedScis = loader.load(ServletContainerInitializer.class); // 检测到的 ServletContainerInitializer
	                for (ServletContainerInitializer sci : detectedScis) {
	                    initializerClassMap.put(sci, new HashSet<Class<?>>()); // 要调用的初始化器
	                }
	            }
		}

		class org.apache.catalina.core.StandardContext{
			
			
			
			 // 上下文容器初始化器
			 public void addServletContainerInitializer(
		            ServletContainerInitializer sci, Set<Class<?>> classes) {
		        initializers.put(sci, classes);
		     }
			 
			protected synchronized void startInternal() throws LifecycleException {
				
				//....
				
				fireLifecycleEvent(Lifecycle.CONFIGURE_START_EVENT, null);
				
				//....
				
				for (Map.Entry<ServletContainerInitializer, Set<Class<?>>> entry :
		                initializers.entrySet()) { // 调用容器初始化器 ，
		            	// 如：org.apache.jasper.servlet.JasperInitializer.onStartup(); 
		                try {
		                	// 执行初始化器，启动初始化器
		                    entry.getKey().onStartup(entry.getValue(),getServletContext());
		                } catch (ServletException e) {
		                    log.error(sm.getString("standardContext.sciFail"), e);
		                    ok = false;
		                    break;
		                }
		            }
				}
			}
		}
	
		class org.apache.jasper.servlet.JasperInitializer{ // 初始化器
			public void onStartup(Set<Class<?>> types, ServletContext context) throws ServletException {
				TldScanner scanner = newTldScanner(context, true, validate, blockExternal); // *.tld文件 解析对象
				// 扫描web.xml文件中 jsp标签的配置,构造tld文件的对象树
	        	// 扫描/WEB-INF/中以implicit.tld结尾的文件,构造tld文件的对象树
	            // 扫描/WEB-INF/中以.tld结尾的文件,构造tld文件的对象树
	        	// 扫描/WEB-INF/lib/ 中以  .tld结尾的文件 ,构造tld文件的对象树
	        	// 扫描WEB-INF/classes 中以  .tld结尾的文件 ,构造tld文件的对象树
	        	// 扫描类路径 中以  .tld结尾的文件 ,构造tld文件的对象树
	            scanner.scan(); 
	            
	            // add any listeners defined in TLDs
	            for (String listener : scanner.getListeners()) {
	            	// !!! context === org.apache.catalina.core.ApplicationContextFacade
	                context.addListener(listener); // 添加事件监听器
	            }
	            
	            // !!! context === org.apache.catalina.core.ApplicationContextFacade
	            // 设置到上下文环境中
	            context.setAttribute(TldCache.SERVLET_CONTEXT_ATTRIBUTE_NAME,
	                    new TldCache(context, scanner.getUriTldResourcePathMap(),
	                            scanner.getTldResourcePathTaglibXmlMap()));
			}
		}


		// jsp扩展标签的使用--------------2
		// 1、jsp页面头部的声明，如：**************
//		<%@ taglib uri="http://cn.java/jsp/core" prefix="myprefix" %>
		// 2、在web.xml文件中的配置 **************
//		<taglib>
//	        <taglib-uri>
//	           http://cn.java/jsp/core
//	        </taglib-uri>
//	        <taglib-location>
//	           /WEB-INF/jsp2/jsp2-example-taglib.tld
//	        </taglib-location>
//	    </taglib>
		// 3、.tld文件的定义   **************
//			\webapps\examples\WEB-INF\jsp2\jsp2-example-taglib.tld
//		<function>
//	        <description>Converts the string to all caps</description>
//	        <name>caps</name>
//	        <function-class>jsp2.examples.el.Functions</function-class>
//	        <function-signature>java.lang.String caps( java.lang.String )</function-signature>
//	    </function>
		// 4、.tld文件中定义的类的编写  **************
		class jsp2.examples.simpletag.ShuffleSimpleTag{ ... }
		// 5、在jsp页面的使用 **************
		<myprefix:caps("test") />
		
	}
	public static void main(String[] args) throws Exception {
		
//		 // 事件监听器
//		 javax.servlet.ServletContextAttributeListener // 在ServletContext的属性修改的时候触发
//		 javax.servlet.ServletRequestAttributeListener
//		 javax.servlet.ServletRequestListener
//		 javax.servlet.http.HttpSessionIdListener
//		 javax.servlet.http.HttpSessionAttributeListener
//
//		 // 生命周期监听器
//		 javax.servlet.ServletContextListener  // 如：在ServletContext.start() 的时候触发
//		 javax.servlet.http.HttpSessionListener

//		jar包解析
//		 
//		Manifest-Version: 1.0
//		Ant-Version: Apache Ant 1.9.6
//		Created-By: 1.7.0_80-b15 (Oracle Corporation)
//		Specification-Title: Apache Tomcat
//		Specification-Version: 8.5
//		Specification-Vendor: Apache Software Foundation
//		Implementation-Title: Apache Tomcat
//		Implementation-Version: 8.5.4
//		Implementation-Vendor: Apache Software Foundation
//		X-Compile-Source-JDK: 1.7
//		X-Compile-Target-JDK: 1.7

		URL url = new URL("jar:/a/b/c/file.jar");
		JarURLConnection jarConn = (JarURLConnection) url.openConnection();
        URL resourceURL = jarConn.getJarFileURL();
        URLConnection resourceConn = resourceURL.openConnection();
        resourceConn.setUseCaches(false);
        JarInputStream mJarInputStream = new JarInputStream(resourceConn.getInputStream());
        
        
        // 解析 /META-INF/MANIFEST.MF 文件
        Manifest manifest = mJarInputStream.getManifest();
        Attributes attributes = manifest.getMainAttributes();
        String classPathAttribute = attributes.getValue("Class-Path");
		
		
		
	}

}
