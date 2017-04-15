package cn.java.note.知识线;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;

import javax.management.ObjectName;
import javax.naming.Reference;
import javax.naming.StringRefAddr;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequestAttributeListener;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionIdListener;
import javax.servlet.http.HttpSessionListener;

import org.apache.catalina.Context;
import org.apache.catalina.Server;
import org.apache.catalina.deploy.NamingResourcesImpl;
import org.apache.naming.NamingContext;
import org.apache.naming.ResourceRef;
import org.apache.tomcat.util.descriptor.web.ContextResource;
import org.apache.tomcat.util.modeler.Registry;
import org.apache.tomcat.util.net.NioSelectorPool;

public class InitLoadStart {
	/*
		---------
		创建、设置线程的类加载器
		---------
		 使用Digester解析配置文件conf/server.xml成对象树
			 1.使用 SAXParser 解析、事件解析
			 2.
			 	开始元素，创建对象，推入栈
			 	结束元素，弹出对象
		---------
		Stream redirection 系统的输出和错误输出进行重定向
		---------
		使用 MBeanServerFactory 管理对象（jmx）
		---------
		线程池执行器
	 */


	public static void debug(){
		/*
		 	
    		org.apache.catalina.startup.Bootstrap.main(){
    			-----------------------------------------------
	    		------------------- init ---------------------
	    		-----------------------------------------------
    			1.org.apache.catalina.startup.Bootstrap.init()
	    		-----------------------------------------------
	    		------------------- load ---------------------
	    		-----------------------------------------------
	    		2.org.apache.catalina.startup.Bootstrap.load(){
		    		org.apache.catalina.startup.Catalina.load(){
		    			org.apache.catalina.core.StandardServer.init(){
		    				触发StandardServer的事件 before_init
						 	org.apache.catalina.deploy.NamingResourcesImpl.init()
						 	for (int i = 0; i < services.length; i++) {
						 		org.apache.catalina.core.StandardService.init() {
						 			
						 			org.apache.catalina.core.StandardEngine.init() { // 启动线程池执行器
						 				BlockingQueue<Runnable> startStopQueue = new LinkedBlockingQueue<>();
								        startStopExecutor = new ThreadPoolExecutor(
								                getStartStopThreadsInternal(),
								                getStartStopThreadsInternal(), 10, TimeUnit.SECONDS,
								                startStopQueue,
								                new StartStopThreadFactory(getName() + "-startStop-"));
								        startStopExecutor.allowCoreThreadTimeOut(true);
						 			}
						 			
						 			for (Executor executor: executors) {
							 			org.apache.catalina.core.StandardThreadExecutor.init()
						 			}
						 			
						 			org.apache.catalina.mapper.MapperListener.init()
						 			
									for (Connector connector: connectors) {
									 	org.apache.catalina.connector.Connector.init(){ 协议 protocol="HTTP/1.1"
									 		org.apache.coyote.http11.Http11NioProtocol.init(){
									 			org.apache.tomcat.util.net.NioEndpoint.init(){
										 			org.apache.tomcat.util.net.NioEndpoint.bind() {
										 			
										 				serverSock = ServerSocketChannel.open();
												        socketProperties.setProperties(serverSock.socket());
												        
												        // 端口
												        InetSocketAddress addr = (getAddress()!=null?new InetSocketAddress(getAddress(),getPort()):new InetSocketAddress(getPort()));
												        serverSock.socket().bind(addr,getBacklog());
												        serverSock.configureBlocking(true); //mimic APR behavior 是否是阻塞模式
												        serverSock.socket().setSoTimeout(getSocketProperties().getSoTimeout());
												
												        // Initialize thread count defaults for acceptor, poller
												        if (acceptorThreadCount == 0) {
												            // FIXME: Doesn't seem to work that well with multiple accept threads
												            acceptorThreadCount = 1;
												        }
												        if (pollerThreadCount <= 0) {
												            //minimum one poller thread
												            pollerThreadCount = 1;
												        }
												        stopLatch = new CountDownLatch(pollerThreadCount);
												
												        // Initialize SSL if needed  初始化ssl
												        initialiseSsl();
												
												        selectorPool.open();
										 			}
									 			}
									 		}
									 	}
									 	org.apache.catalina.connector.Connector.init(){ 协议 protocol="AJP/1.3"
									 		org.apache.coyote.ajp.AjpNioProtocol.init(){
						 						org.apache.tomcat.util.net.NioEndpoint.init(){
						 							org.apache.tomcat.util.net.NioEndpoint.bind(){
						 							
						 								serverSock = ServerSocketChannel.open();
												        socketProperties.setProperties(serverSock.socket());
												        
												        // 端口
												        InetSocketAddress addr = (getAddress()!=null?new InetSocketAddress(getAddress(),getPort()):new InetSocketAddress(getPort()));
												        serverSock.socket().bind(addr,getBacklog());
												        serverSock.configureBlocking(true); //mimic APR behavior 是否是阻塞模式
												        serverSock.socket().setSoTimeout(getSocketProperties().getSoTimeout());
												
												        // Initialize thread count defaults for acceptor, poller
												        if (acceptorThreadCount == 0) {
												            // FIXME: Doesn't seem to work that well with multiple accept threads
												            acceptorThreadCount = 1;
												        }
												        if (pollerThreadCount <= 0) {
												            //minimum one poller thread
												            pollerThreadCount = 1;
												        }
												        stopLatch = new CountDownLatch(pollerThreadCount);
												
												        // Initialize SSL if needed  初始化ssl
												        initialiseSsl();
												
												        selectorPool.open();
						 							}
						 						}
						 					}
									 	}
						 			}
			
						 		}
						 	}
						 	触发StandardServer的事件 after_init
					 	}
				 	}
	    		}
	    		-----------------------------------------------
	    		------------------- start ---------------------
	    		-----------------------------------------------
	    		3.org.apache.catalina.startup.Bootstrap.start(){
	    			org.apache.catalina.startup.Catalina.start(){
		    			org.apache.catalina.core.StandardServer.start(){
			    			------ 触发StandardServer的事件 before_start ------
			    			------ 触发StandardServer的事件 configure_start ------
			    			org.apache.catalina.core.NamingContextListener."configure_start"(){ // StandardServer 级别的 NamingContext监听器
			    				// ....
			    			}
			    			------ 触发StandardServer的事件 start ------
			    			globalNamingResources.start(){ // globalNamingResources = org.apache.catalina.deploy.NamingResourcesImpl
			    				org.apache.catalina.deploy.NamingResourcesImpl.start()
			    			}
		    				for (int i = 0; i < services.length; i++) {
		    					org.apache.catalina.core.StandardService.start() {
		    						触发StandardService的事件 start
		    						org.apache.catalina.startup.EngineConfig.start() // StandardEngine节点监听器
		    						org.apache.catalina.core.StandardEngine.start()
		    						
		    						for (Executor executor: executors) {
							 			org.apache.catalina.core.StandardThreadExecutor.start()
							 			触发StandardThreadExecutor的事件 start
						 			}
						 			
						 			触发MapperListener的事件 start
						 			// 查找默认虚拟主机;让 mapperListener成为StandardEngine、StandardHost、StandardContext的监听器;注册路径信息到mapper中
						 			org.apache.catalina.mapper.MapperListener.start(){
						 				org.apache.catalina.core.StandardHost.start(){ // webapps目录 StandardHost.getAppBaseFile() === /home/tomcat/webapps
						 					org.apache.catalina.startup.HostConfig.start(){ 
						 						org.apache.catalina.startup.HostConfig.deployApps(){
						 							org.apache.catalina.core.StandardContext.start(){ // 根目录下的应用  === /home/tomcat/webapps/webapp1
						 								//...
						 								cookieProcessor = new Rfc6265CookieProcessor(); // 定义cookie处理器
						 								
						 								postWorkDirectory(); // 部署工作目录，如  /home/tomcat/work/Catalina/localhost/webapp1
							 							
							 							if (getNamingContextListener() == null) { //
											            	// 创建名称上下文监听器
											                NamingContextListener ncl = new NamingContextListener();
											                ncl.setName(getNamingContextName());
											                ncl.setExceptionOnFailedWrite(getJndiExceptionOnFailedWrite());
											                // 把名称上下文监听器注入 org.apache.catalina.core.StandardContext
											                addLifecycleListener(ncl);
											                
											                setNamingContextListener(ncl); // 设置"名称上下文"监听器!!!
											            }
							 							
							 							fireLifecycleEvent(Lifecycle.CONFIGURE_START_EVENT, null); { // 
							 								org.apache.catalina.startup.ContextConfig.configureStart() { // 事件监听器1
								 								org.apache.catalina.startup.ContextConfig.webConfig(){
										 							// 读取并解析“/home/tomcat/conf/web.xml”，读取并解析"/home/tomcat/conf/Catalina/localhost/web.xml.default"
        															defaults.add(getDefaultWebXmlFragment(webXmlParser));  // WebXml对象1
										 							
										 							WebXml webXml = createWebXml(); // WebXml对象2
															        // 取得输入流 "d:/a/b/c/tomcat/webapps/dir1/WEB-INF/web.xml"
															        InputSource contextWebXml = getContextWebXmlSource();
															        // 解析规则定义在 org.apache.tomcat.util.descriptor.web.WebRuleSet.addRuleInstances()
															        if (!webXmlParser.parseWebXml(contextWebXml, webXml, false)) { // 解析文件 ，数据存入webXml
															            ok = false;
															        }
										 							
										 							// context === org.apache.catalina.core.StandardContext
															        // org.apache.catalina.core.StandardContext.getServletContext();
															        // org.apache.catalina.core.ApplicationContext.getFacade();
															        // sContext === org.apache.catalina.core.ApplicationContextFacade
															        ServletContext sContext = context.getServletContext();
										 							
								 							        if (ok) {
															//        	 org.apache.jasper.servlet.JasperInitializer  jasper.jar
															//           org.apache.tomcat.websocket.server.WsSci    tomcat-websocket.jar
															        	
															//        	 解析类路径中"META-INF/services/javax.servlet.ServletContainerInitializer"文件内容
															//          创建文件中声明的类型对象，并把创建对象转成ServletContainerInitializer类型的引用
															//        	initializerClassMap{
															//            	'MyServletContainerInitializer1_Obj'=>[],
															//            	'MyServletContainerInitializer2_Obj'=>[],
															//          }
															//        	typeInitializerMap{
															//            	'MyAnnotation1.class'=>[MyServletContainerInitializer1_Obj ],
															//            	'MyAnnotation2.class'=>[MyServletContainerInitializer2_Obj ]
															//          }
															            processServletContainerInitializers(); { // 查看实现ServletContainerInitializer的初始化器
															           		List<ServletContainerInitializer> detectedScis;
															           		// ......
															           		// WebappServiceLoader用于识别jar包,或者类路径中"META-INF/services/javax.servlet.ServletContainerInitializer"文件内容
																            WebappServiceLoader<ServletContainerInitializer> loader = new WebappServiceLoader<>(context);
																            // org.apache.jasper.servlet.JasperInitializer  jasper.jar
																            // org.apache.tomcat.websocket.server.WsSci    tomcat-websocket.jar
																            // 解析类路径中"META-INF/services/javax.servlet.ServletContainerInitializer"文件内容
																            // 创建文件中声明的类型对象，并把创建对象转成ServletContainerInitializer类型的引用
																            detectedScis = loader.load(ServletContainerInitializer.class); // 检测到的 ServletContainerInitializer
															            	for (ServletContainerInitializer sci : detectedScis) { // sci是对象
																            	// 查看是否有注解  @HandlesTypes(MyAnnotation1.class)
	                															ht = sci.getClass().getAnnotation(HandlesTypes.class); 
															            	}
															            }
															        }
														            ...
														            webXml.merge(defaults); // 合并全局配置
														            // 使用 org.apache.jasper.servlet.JspServlet 包装  <jsp-file>/a/b/c/file.jsp</jsp-file>的文件
														            ContextConfig.convertJsps(webXml);
														            // context === org.apache.catalina.core.StandardContext
														            // 把解析处理的内容设置到 context = StandardContext中 ，如：context.addFilterMap(filterMap);
														            ContextConfig.configureContext(webXml) { //!!!!!
														            	...
														            	读取web.xml解析处理的对象webXml的属性设置到StandardContext对象中
														            	（中途要把ServletDef对象转成 org.apache.catalina.core.StandardWrapper 对象）
														            	StandardContext.addChild(wrapper){
														            		wrapper.start();
														            	}
														            	触发在org.apache.catalina.core.NamingContextListener事件处理器中，内部调用了createNamingContext()创建 envCtx
														            } 
										 							
										 							if (ok) {
															        	// org.apache.jasper.servlet.JasperInitializer  jasper.jar
															            // org.apache.tomcat.websocket.server.WsSci    tomcat-websocket.jar
															        	
															        	// 解析类路径中"META-INF/services/javax.servlet.ServletContainerInitializer"文件内容
															            // 创建文件中声明的类型对象，并把创建对象转成ServletContainerInitializer类型的引用
															//        	initializerClassMap{
															//            	'MyServletContainerInitializer1_Obj'=>[],
															//            	'MyServletContainerInitializer2_Obj'=>[],
															//            }
															//        	typeInitializerMap{
															//            	'MyAnnotation1.class'=>[MyServletContainerInitializer1_Obj ],
															//            	'MyAnnotation2.class'=>[MyServletContainerInitializer2_Obj ]
															//            }
															            for (Map.Entry<ServletContainerInitializer,Set<Class<?>>> entry : initializerClassMap.entrySet()) {
															                if (entry.getValue().isEmpty()) { // 添加Servlet容器初始化器到StandardContext
															                    context.addServletContainerInitializer(
															                            entry.getKey(), null);
															                } else {
															                    context.addServletContainerInitializer(
															                            entry.getKey(), entry.getValue());
															                }
															            }
															        }
										 							
								 								}
							 								}
							 								
							 								org.apache.catalina.core.NamingContextListener.configure_start();{ // "configure_start" , 事件监听器2 , StandardContext级别的 NamingContext监听器
							 									container = event.getLifecycle();
        														if (container instanceof Context) {
														            namingResources = ((Context) container).getNamingResources();  // org.apache.catalina.deploy.NamingResourcesImpl
														            token = ((Context) container).getNamingToken();
														        } else if (container instanceof Server) {
														            namingResources = ((Server) container).getGlobalNamingResources();
														            token = ((Server) container).getNamingToken();
														        } else {
														            return;
														        }
            													Hashtable<String, Object> contextEnv = new Hashtable<>();
							 									// getName() === "/Catalina/localhost/webapp1"
							 									namingContext = new NamingContext(contextEnv, getName()); // 创建名称上下文
							 									NamingContextListener.createNamingContext(){
							 										// ....
							 										if (container instanceof Server) { // 走这里
															            compCtx = namingContext; // org.apache.naming.NamingContext
															            envCtx = namingContext;
															        } else { // org.apache.catalina.core.StandardContext
															            compCtx = namingContext.createSubcontext("comp");  // comp/env
															            envCtx = compCtx.createSubcontext("env");
															        }
															        if (namingResources == null) { // 不成立
															            namingResources = new NamingResourcesImpl();
															            namingResources.setContainer(container);
															        }
															        ...
															        // Resources ， 如 Mysql资源
															        ContextResource[] resources = namingResources.findResources();
															        for (i = 0; i < resources.length; i++) {
															            addResource(resources[i]);{
															        //	    	<Resource  
																	//		        name="jndi/mybatis"  
																	//		        type="javax.sql.DataSource"  
																	//		        driverClassName="com.mysql.jdbc.Driver"  
																	//		        maxIdle="2"  
																	//		        maxWait="5000"  
																	//		        username="root"  
																	//		        password="123456"  
																	//		        url="jdbc:mysql://localhost:3306/appdb"  
																	//		        maxActive="4"/>  
																	    	// ContextResource 就是 <Resource></Resource> 的描述信息
																	        // Create a reference to the resource.
																	        Reference ref = new ResourceRef(resource.getType(), resource.getDescription(),
																	          		resource.getScope(), resource.getAuth(),
																	             	resource.getSingleton());
																	        Iterator<String> params = resource.listProperties();
																	        while (params.hasNext()) {
																	            String paramName = params.next();
																	            String paramValue = (String) resource.getProperty(paramName);
																	            StringRefAddr refAddr = new StringRefAddr(paramName, paramValue);
																	            ref.add(refAddr);
																	        }
																	        createSubcontexts(envCtx, resource.getName());  // comp/env/resource1
																	        envCtx.bind(resource.getName(), ref);
																	        if ("javax.sql.DataSource".equals(ref.getClassName()) &&
																	                resource.getSingleton()) { // 
																	            ObjectName on = createObjectName(resource);
																	            Object actualResource = envCtx.lookup(resource.getName());
																	            Registry.getRegistry(null, null).registerComponent(actualResource, on, null);
																	            objectNames.put(resource.getName(), on);
																	        }
															            }
															        }
															        ...

							 									}
							 								} 
							 							}
							 							
						 								//...
						 								if (pipeline instanceof Lifecycle) {
										                    ((Lifecycle) pipeline).start();  // 启动管道
										                }
										                if (manager == null) {
										                	...
											                contextManager = new StandardManager(); // 上下文管理器，Session管理器
										                	...
										                }
										                
										                if (contextManager != null) {
										                	...
											                setManager(contextManager); // 设置上下文管理器，Session管理器
										                }
										                
										                if (ok ) {
											                if (getInstanceManager() == null) { // 如果实例管理器为空，实例管理器用来管理实例，如：org.apache.jasper.servlet.JspServlet，cn.java.MyServlet
											                    javax.naming.Context context = null; //
											                    if (isUseNaming() && getNamingContextListener() != null) {
											                    	// 在org.apache.catalina.core.NamingContextListener事件处理器中，内部调用了createNamingContext()创建 envCtx
											                    	// org.apache.catalina.core.NamingContextListener.getEnvContext();
											                    	// context === org.apache.naming.NamingContext ...  注册的StandardContext的资源
											                        context = getNamingContextListener().getEnvContext();
											                    }
											                    Map<String, Map<String, String>> injectionMap = buildInjectionMap(
											                            getIgnoreAnnotations() ? new NamingResourcesImpl(): getNamingResources());
											                    // 设置实例管理器 org.apache.catalina.core.DefaultInstanceManager，JNDI资源是通过“注解”注入的
											                    setInstanceManager(new DefaultInstanceManager(context,
											                            injectionMap, this, this.getClass().getClassLoader()));
											                }
											                getServletContext().setAttribute(
											                        InstanceManager.class.getName(), getInstanceManager());
											                InstanceManagerBindings.bind(getLoader().getClassLoader(), getInstanceManager());
											            }
											            
											            for (Map.Entry<ServletContainerInitializer, Set<Class<?>>> entry :
											                initializers.entrySet()) { // 调用容器初始化器 ，
											            	// org.apache.jasper.servlet.JasperInitializer  jasper.jar
											                // org.apache.tomcat.websocket.server.WsSci    tomcat-websocket.jar
											            	 org.apache.jasper.servlet.JasperInitializer.onStartup(); { // --- 
											            	 	// 扫描web.xml文件中 <jsp-config> 标签的配置,构造tld文件的对象树
													        	// 扫描/WEB-INF/tags/implicit.tld文件,构造tld文件的对象树
													            // 扫描/WEB-INF/中以.tld结尾的文件,构造tld文件的对象树
													        	// 扫描/WEB-INF/lib/ 中以  .tld结尾的文件 ,构造tld文件的对象树
													        	// 扫描/WEB-INF/classes/META-INF 中以  .tld结尾的文件 ,构造tld文件的对象树
													        	// 扫描上下文类加载器类路径路径 中以  .tld结尾的文件 ,构造tld文件的对象树
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
											            	 org.apache.tomcat.websocket.server.WsSci.onStartup(); { // --- websocket的支持
											            	 	....
											            	 	servletContext.addListener(new WsSessionListener(sc));
											            	 	....
											            	 }
											            	// getServletContext() === org.apache.catalina.core.ApplicationContextFacade
											                try {
											                	// 执行初始化器
											                    entry.getKey().onStartup(entry.getValue(),getServletContext());
											                } catch (ServletException e) {
											                    log.error(sm.getString("standardContext.sciFail"), e);
											                    ok = false;
											                    break;
											                }
											            }
            
											            if (ok) {
											            	// 启动监听器，
											                if (!listenerStart()) { // 整理、归类上下文中web.xml中配置的监听器，并触发“生命周期监听器”，触发contextInitialized()方法
											                    String listeners[] = findApplicationListeners(); // 上下文中web.xml配置的监听器<listener></listener>
														        Object results[] = new Object[listeners.length];
														        for (int i = 0; i < results.length; i++) {
														        	results[i] = getInstanceManager().newInstance(listener); // 创建监听器对象!!!!!
														        }
														        // Sort listeners in two arrays 把监听器分类
														        ArrayList<Object> eventListeners = new ArrayList<>();
														        ArrayList<Object> lifecycleListeners = new ArrayList<>();
														        for (int i = 0; i < results.length; i++) {
														            if ((results[i] instanceof ServletContextAttributeListener)
														                || (results[i] instanceof ServletRequestAttributeListener)
														                || (results[i] instanceof ServletRequestListener)
														                || (results[i] instanceof HttpSessionIdListener)
														                || (results[i] instanceof HttpSessionAttributeListener)) { // 事件监听器
														                eventListeners.add(results[i]);
														            }
														            if ((results[i] instanceof ServletContextListener)
														                || (results[i] instanceof HttpSessionListener)) { // 生命周期监听器
														                lifecycleListeners.add(results[i]);
														            }
														        }
														        for (Object eventListener: getApplicationEventListeners()) { // 合并原来的
														            eventListeners.add(eventListener);
														        }
														        setApplicationEventListeners(eventListeners.toArray()); // 事件监听器
														        for (Object lifecycleListener: getApplicationLifecycleListeners()) { // 合并原来的
														            lifecycleListeners.add(lifecycleListener);
														            if (lifecycleListener instanceof ServletContextListener) {
														                noPluggabilityListeners.add(lifecycleListener);
														            }
														        }
														        setApplicationLifecycleListeners(lifecycleListeners.toArray());  // 生命周期监听器
														        Object instances[] = getApplicationLifecycleListeners(); // 生命周期监听器
														        if (instances == null || instances.length == 0) {
														            return ok;
														        }
														        ServletContextEvent event = new ServletContextEvent(getServletContext());
														        for (int i = 0; i < instances.length; i++) { // 触发“生命周期监听器”
														        	if (!(instances[i] instanceof ServletContextListener))
														                continue;
														            ServletContextListener listener = (ServletContextListener) instances[i];
														            if (noPluggabilityListeners.contains(listener)) {
														                listener.contextInitialized(tldEvent);
														            } else {
														                listener.contextInitialized(event); // 调用事件监听器的 contextInitialized方法
														            }
														        }
											                }
											            }
											            
											            try {
											                // Start manager
											                Manager manager = getManager();
											                if (manager instanceof Lifecycle) {
											                	// org.apache.catalina.session.StandardManager
											                    ((Lifecycle) manager).start(); // 启动上下文管理器，即启动Session管理器
											                }
											            } catch(Exception e) {
											                log.error(sm.getString("standardContext.managerFail"), e);
											                ok = false;
											            }
											
											            // Configure and call application filters
											            if (ok) {
											            	 // 启动过滤器，会把org.apache.tomcat.util.descriptor.web.FilterDef 转成对象 org.apache.catalina.core.ApplicationFilterConfig
											                filterStart(){
											                    ApplicationFilterConfig filterConfig = new ApplicationFilterConfig(this, entry.getValue());{ // this === StandardContext
											                    	 getFilter(){
											                    	 	// Identify the class loader we will be using
																        String filterClass = filterDef.getFilterClass();
																        // org.apache.catalina.core.DefaultInstanceManager
																        this.filter = (Filter) getInstanceManager().newInstance(filterClass); // 创建过滤器对象
											                    	  	initFilter();{ // 初始化过滤器
											                    	  		filter.init(this);  // this === ApplicationFilterConfig
											                    	  	}
											                    	 }
											                    }
						                    					filterConfigs.put(name, filterConfig);
											                }
											            }
											            // Load and initialize all "load on startup" servlets
											            if (ok) {
											                loadOnStartup(findChildren()){ // 启动 servlets
											                	TreeMap<Integer, ArrayList<Wrapper>> map = new TreeMap<>();
														        for (int i = 0; i < children.length; i++) {
														            Wrapper wrapper = (Wrapper) children[i]; // org.apache.catalina.core.StandardWrapper
														            int loadOnStartup = wrapper.getLoadOnStartup();
														            if (loadOnStartup < 0)  // 启动次序
														                continue;
														            Integer key = Integer.valueOf(loadOnStartup); // 启动次序
														            ArrayList<Wrapper> list = map.get(key);
														            if (list == null) {
														                list = new ArrayList<>();
														                map.put(key, list);
														            }
														            list.add(wrapper);
														        }
														        // Load the collected "load on startup" servlets
														        for (ArrayList<Wrapper> list : map.values()) {
														            for (Wrapper wrapper : list) { // org.apache.catalina.core.StandardWrapper
														                try {
														                    wrapper.load(); { // 调用StandardWrapper的load()方法
														                    	instance = loadServlet();{
															                    	//!!!实例管理器 org.apache.catalina.core.StandardContext
																		            // instanceManager === org.apache.catalina.core.DefaultInstanceManager
																		            InstanceManager instanceManager = ((StandardContext)getParent()).getInstanceManager();
																		            // 创建Servlet对象，反射Servlet对象内部的注解，如：@Resource声明的资源，并进行依赖注入到Servlet对象中!!!
																	            	// 如：servletClass === org.apache.jasper.servlet.JspServlet
																	            	// 如：servletClass === org.apache.catalina.servlets.DefaultServlet
																	            	// 如: servletClass === cn.java.note.HelloServlet
																	                servlet = (Servlet) instanceManager.newInstance(servletClass);
																	                
																	                // 处理Servlet的安全注解
																		            processServletSecurityAnnotation(servlet.getClass());
																		
																					if ((servlet instanceof ContainerServlet) &&
																		                    (isContainerProvidedServlet(servletClass) ||
																		                            ((Context) getParent()).getPrivileged() )) {
																		                ((ContainerServlet) servlet).setWrapper(this); // 容器Servlet实例，设置对父节点的依赖
																		            }
																		            
																		            // 调用Servlet的初始化方法init
																		            initServlet(servlet);{
																		            	// 调用Servlet的初始化方法init
																		            	// facade = new StandardWrapperFacade(this);
																		                servlet.init(facade);
																		            }
																		            
																		            fireContainerEvent("load", this);{ // 触发容器事件 ContainerListener
														                    			
														                    		}
														                    	}
														                    	if (!instanceInitialized) {
																		            initServlet(instance);
																		        }
														                    }
														                } catch (ServletException e) {
														                }
														            }
														        }
											                }
											            }
											            
											            // 触发事件监听器 org.apache.catalina.startup.ContextConfig
            											setState(LifecycleState.STARTING); // 触发“生命周期监听器” "start"
						 							}
						 						}
						 					}
						 					...
						 				}
						 			}
						 			
						 			for (Connector connector: connectors) {
						 				org.apache.catalina.connector.Connector.start(){ // protocol="HTTP/1.1"
						 					触发Connector的事件 start
						 					org.apache.coyote.http11.Http11NioProtocol.start(){
						 						org.apache.tomcat.util.net.NioEndpoint.start(){
						 							if (!running) {
											            running = true;
											            paused = false;
											
											            // 同步栈
											            processorCache = new SynchronizedStack<>(SynchronizedStack.DEFAULT_SIZE,
											                    socketProperties.getProcessorCache());
											            eventCache = new SynchronizedStack<>(SynchronizedStack.DEFAULT_SIZE,
											                            socketProperties.getEventCache());
											            nioChannels = new SynchronizedStack<>(SynchronizedStack.DEFAULT_SIZE,
											                    socketProperties.getBufferPool());
											
											            // Create worker collection
											            if ( getExecutor() == null ) {
											                createExecutor(); { //!!!
											                	internalExecutor = true;
														        TaskQueue taskqueue = new TaskQueue();
														        TaskThreadFactory tf = new TaskThreadFactory(getName() + "-exec-", daemon, getThreadPriority());
														        executor = new ThreadPoolExecutor(getMinSpareThreads(), getMaxThreads(), 60, TimeUnit.SECONDS,taskqueue, tf);
														        taskqueue.setParent( (ThreadPoolExecutor) executor);
											                }
											            }
											
											            initializeConnectionLatch(); // 初始化连接数量阈值
											
														--------------------------------------------
														---------------Socket连接“处理器”-------------
														--------------------------------------------
											            // Start poller threads
											            pollers = new Poller[getPollerThreadCount()]; // 启动pollers
											            for (int i=0; i<pollers.length; i++) {
											                pollers[i] = new Poller();
											                Thread pollerThread = new Thread(pollers[i], getName() + "-ClientPoller-"+i);
											                pollerThread.setPriority(threadPriority);
											                pollerThread.setDaemon(true);
											                pollerThread.start(); {
											                	public void run() {
												            	 	while (true) {
												            	 		hasEvents = events(); // 取得事件
												            	 			//either we timed out or we woke up, process events first
															                if ( keyCount == 0 ) hasEvents = (hasEvents | events());
															
															                // 迭代Select键列表
															                Iterator<SelectionKey> iterator =
															                    keyCount > 0 ? selector.selectedKeys().iterator() : null;
															                while (iterator != null && iterator.hasNext()) {
															                    SelectionKey sk = iterator.next();
															                    NioSocketWrapper attachment = (NioSocketWrapper)sk.attachment();
															                    // Attachment may be null if another thread has called
															                    // cancelledKey()
															                    if (attachment == null) {
															                        iterator.remove();
															                    } else {
															                        iterator.remove();
															                        processKey(sk, attachment); { // !!!!处理用户的socket请求
															                        	processSocket(attachment, SocketEvent.OPEN_READ, true) { // !!!处理用户的socket请求
														                                    socketWrapper = attachment;
														                                    sc = createSocketProcessor(socketWrapper, event); {  // 创建Socket处理器
														                                    	return new SocketProcessor(socketWrapper, event); // org.apache.tomcat.util.net.NioEndpoint.SocketProcessor
														                                    }
														                                    sc.run(); { //!!!
														                                    	protected void doRun() { // 任务处理器
														                                    		NioChannel socket = socketWrapper.getSocket();
																						            SelectionKey key = socket.getIOChannel().keyFor(socket.getPoller().getSelector());
																						            //	getHandler() === org.apache.coyote.AbstractProtocol.ConnectionHandler
																			                        state = getHandler().process(socketWrapper, event); { // !!!!-------
																			                        	S socket = wrapper.getSocket();

            																							Processor processor = connections.get(socket);//!!!
            																							
            																							if (processor == null) {
																						//                	org.apache.coyote.http11.Http11NioProtocol.createProcessor();
																						//                	processor = org.apache.coyote.http11.Http11Processor
																						                    processor = getProtocol().createProcessor();//-----------
																						                    register(processor); // 把processor注册到MServer服务器
																						                }
																						                // !!! org.apache.coyote.http11.Http11Processor.process(wrapper, status)
                    																					state = processor.process(wrapper, status); {
                    																						state = service(socketWrapper); {　//!!!!处理服务-----
                    																							// org.apache.coyote.RequestInfo
																										        RequestInfo rp = request.getRequestProcessor();//!!!
																										        rp.setStage(org.apache.coyote.Constants.STAGE_PARSE); // 解析
																										
																										        // Setting up the I/O
																										        setSocketWrapper(socketWrapper);
																										        inputBuffer.init(socketWrapper);
																										        outputBuffer.init(socketWrapper);
																										        
																										        while (!getErrorState().isError() && keepAlive && !isAsync() &&
																									                upgradeToken == null && !endpoint.isPaused()) {
																									                // 解析请求行，解析出协议类型  protocol/queryString/requestURI/method，如protocol是HTTP/2.0 ------
																									                if (!inputBuffer.parseRequestLine(keptAlive)) { 
																									                    if (inputBuffer.getParsingRequestLinePhase() == -1) {
																									                        return SocketState.UPGRADING;
																									                    } else if (handleIncompleteRequestLineRead()) {
																									                        break;
																									                    }
																									                }
																									                request.getMimeHeaders().setLimit(endpoint.getMaxHeaderCount()); // 最多的mime头数量 ----
																									                Enumeration<String> connectionValues = request.getMimeHeaders().values("Connection"); // 取得Connection的值 -----
																									                prepareRequest(); { // !!!预处理请求头，解析出主机和端口等 -----
																										                long contentLength = request.getContentLengthLong(); // 内容长度
																										                MessageBytes valueMB = headers.getValue("host"); // 主机名称
																										                parseHost(valueMB); // 解析主机名和端口
																									                }
																									                
																									                // !!! org.apache.catalina.connector.CoyoteAdapter
																									                // org.apache.coyote.Request == req,  org.apache.coyote.Response == res
																									                getAdapter().service(req, res); { //!!!!!!!!!!! ---------
																									                	Request request = (org.apache.catalina.connector.Request) req.getNote(ADAPTER_NOTES);
																												        Response response = (org.apache.catalina.connector.Response) res.getNote(ADAPTER_NOTES);
																												        if (request == null) {
																												
																												            // Create objects  是走到这边了！！！
																												        	// org.apache.catalina.connector.Connector.createRequest();
																												            request = connector.createRequest(); //!!!!! 
																												            request.setCoyoteRequest(req);
																												            response = connector.createResponse();
																												            response.setCoyoteResponse(res);
																												
																												            // Link objects 相互关联
																												            request.setResponse(response);
																												            response.setRequest(request);
																												
																												            // Set as notes 设置req中
																												            req.setNote(ADAPTER_NOTES, request);
																												            res.setNote(ADAPTER_NOTES, response);
																												
																												            // Set query string encoding
																												            req.getParameters().setQueryStringEncoding
																												                (connector.getURIEncoding()); // 设置查询字符串的编码
																												
																												        }
																												        if (connector.getXpoweredBy()) { // 发送头
																												            response.addHeader("X-Powered-By", POWERED_BY);
																												        }
																												        req.getRequestProcessor().setWorkerThreadName(THREAD_NAME.get());
																												        postParseSuccess = postParseRequest(req, request, res, response); { //!!!!   解析scheme、查找路由、sessionID
																												        boolean mapRequired = true;
																												        while (mapRequired) { // 映射路由
																												        	// org.apache.catalina.mapper.Mapper
																												            connector.getService().getMapper().map(serverName, decodedURI,
																												                    version, request.getMappingData()); // 映射出路由 -------------------并创建出相应对象!!!!
																												            if (request.getContext() == null) { // 没有匹配到上下文
																												                res.setStatus(404);
																												                res.setMessage("Not found");
																												                // No context, so use host
																												                Host host = request.getHost();
																												                // Make sure there is a host (might not be during shutdown)
																												                if (host != null) {
																												                    host.logAccess(request, response, 0, true);
																												                }
																												                return false;
																												            }
																															String sessionID;
																												            if (request.getServletContext().getEffectiveSessionTrackingModes()
																												                    .contains(SessionTrackingMode.URL)) { // 通过地址获取SessionId
																												
																												                // Get the session ID if there was one 取得URL中参数 jsessionid 的值
																												                sessionID = request.getPathParameter(
																												                        SessionConfig.getSessionUriParamName(
																												                                request.getContext())); // mappingData.context;
																												                if (sessionID != null) {
																												                    request.setRequestedSessionId(sessionID);
																												                    request.setRequestedSessionURL(true);
																												                }
																												            }
																												            parseSessionCookiesId(request); // 解析出sessionID
																												            parseSessionSslId(request); // 解析出sessionID
																												            sessionID = request.getRequestedSessionId(); // 取得sessionID
																												            MessageBytes redirectPathMB = request.getMappingData().redirectPath;  // 重定向
																												            if (!connector.getAllowTrace()
																												                && req.method().equalsIgnoreCase("TRACE")) { // TRACE请求方式，暴露的方法
																												            	Wrapper wrapper = request.getWrapper();
																													            String header = null;
																													            if (wrapper != null) {
																													                String[] methods = wrapper.getServletMethods();
																													                if (methods != null) {
																													                    for (int i=0; i<methods.length; i++) {
																													                        if ("TRACE".equals(methods[i])) {
																													                            continue;
																													                        }
																													                        if (header == null) {
																													                            header = methods[i];
																													                        } else {
																													                            header += ", " + methods[i];
																													                        }
																													                    }
																													                }
																													            }
																													            res.setStatus(405);
																													            res.addHeader("Allow", header);
																													            res.setMessage("TRACE method is not allowed");
																													            request.getContext().logAccess(request, response, 0, true);
																													            return false;
																												            }
																												            doConnectorAuthenticationAuthorization(req, request); // 连接授权验证
																												        	return true;																				        
																												        }
																												        // request.getMappingData() 内部存储路由映射对象和StandardWrapper对象(StandardWrapper是Servlet的包装器)
																														if (postParseSuccess) {
																												            //check valves if we support async
																												            request.setAsyncSupported(connector.getService().getContainer().getPipeline().isAsyncSupported());
																												            // Calling the container
																												            // 执行管道链条
																												            // org.apache.catalina.core.StandardService --- getService()
																												            // org.apache.catalina.core.StandardEngine --- getContainer()
																												            // org.apache.catalina.core.StandardPipeline --- getPipeline()
																												            // org.apache.catalina.core.StandardEngineValve --- getFirst()
																												            connector.getService().getContainer().getPipeline().getFirst().invoke(request, response); { //!!!!!! 管道机制是什么？
																												            	org.apache.catalina.core.StandardEngineValve.invoke(request, response){
																												            		// org.apache.catalina.core.StandardHostValve.invoke(request, response)
        																															host.getPipeline().getFirst().invoke(request, response){
																												            			...
																												            			Context context = request.getContext(); // 映射到的上下文
																												            			if (!asyncAtStart && !context.fireRequestInitEvent(request)) { // 触发请求事件
																															                // Don't fire listeners during async processing (the listener
																															                // fired for the request that called startAsync()).
																															                // If a request init listener throws an exception, the request
																															                // is aborted.
																															                return;
																															            }
																												            			...
																												            			// org.apache.catalina.core.StandardContextValve.invoke(request, response)
                    																													context.getPipeline().getFirst().invoke(request, response);{ //!!!!
																												            				MessageBytes requestPathMB = request.getRequestPathMB();
																																	        if ((requestPathMB.startsWithIgnoreCase("/META-INF/", 0))
																																	                || (requestPathMB.equalsIgnoreCase("/META-INF"))
																																	                || (requestPathMB.startsWithIgnoreCase("/WEB-INF/", 0))
																																	                || (requestPathMB.equalsIgnoreCase("/WEB-INF"))) { // 访问被限制的WEB-INF/META-INF目录
																																	            response.sendError(HttpServletResponse.SC_NOT_FOUND);
																																	            return;
																																	        }
																																	        // Select the Wrapper to be used for this Request
																																	        Wrapper wrapper = request.getWrapper(); // Servlet包装器对象org.apache.catalina.core.StandardWrapper
																																	        if (wrapper == null || wrapper.isUnavailable()) {
																																	            response.sendError(HttpServletResponse.SC_NOT_FOUND);
																																	            return;
																																	        }
																																	        // 执行Servlet请求 !!!! wrapper === org.apache.catalina.core.StandardWrapper
																																	        // org.apache.catalina.core.StandardWrapperValve.invoke(request, response);
																																	        wrapper.getPipeline().getFirst().invoke(request, response);{
																																	        	StandardWrapper wrapper = (StandardWrapper) getContainer();
																																		        Servlet servlet = null;
																																		        // context === org.apache.catalina.core.StandardContext
																																		        Context context = (Context) wrapper.getParent();
																																		        // org.apache.catalina.core.StandardWrapper
																																		    	// !!!!! 加载Servlet类,创建Servlet对象，反射Servlet对象内部的注解，如：@Resource声明的资源，并进行依赖注入到Servlet对象中!!!
																																		        // !!!!! 调用Servlet对象的init方法
																																		        servlet = wrapper.allocate();
																																		        {
																																		        	// !!!!! 加载Servlet类,创建Servlet对象，反射Servlet对象内部的注解，如：@Resource声明的资源，并进行依赖注入到Servlet对象中!!!
																														                            // !!!!! 调用Servlet对象的init方法
																														                            instance = loadServlet(); { // 创建Servlet对象
																														                            	//!!!实例管理器 org.apache.catalina.core.StandardContext
																																			            // instanceManager === org.apache.catalina.core.DefaultInstanceManager
																																			            InstanceManager instanceManager = ((StandardContext)getParent()).getInstanceManager();
																																			            // 创建Servlet对象，反射Servlet对象内部的注解，如：@Resource声明的资源，并进行依赖注入到Servlet对象中!!!
																																		            	// 如：servletClass === org.apache.jasper.servlet.JspServlet
																																		            	// 如：servletClass === org.apache.catalina.servlets.DefaultServlet
																																		            	// 如: servletClass === cn.java.note.HelloServlet
																																		                servlet = (Servlet) instanceManager.newInstance(servletClass);
																																		                
																																		                // 处理Servlet的安全注解
																																			            processServletSecurityAnnotation(servlet.getClass());
																																			
																																						if ((servlet instanceof ContainerServlet) &&
																																			                    (isContainerProvidedServlet(servletClass) ||
																																			                            ((Context) getParent()).getPrivileged() )) {
																																			                ((ContainerServlet) servlet).setWrapper(this); // 容器Servlet实例，设置对父节点的依赖
																																			            }
																																			            
																																			            // 调用Servlet的初始化方法init
																																			            initServlet(servlet);{
																																			            	// 调用Servlet的初始化方法init
																																			            	// facade = new StandardWrapperFacade(this);
																																			                servlet.init(facade);
																																			            }
																																			            
																																			            fireContainerEvent("load", this);{ // 触发容器事件 ContainerListener
																															                    			
																															                    		}
																														                            }
																														                            if (!instanceInitialized) {
																																                        initServlet(instance);
																																                    }
																																		        }
																																		        // !!! 创建过滤器链条
																																		        // Create the filter chain for this request
																																		        ApplicationFilterChain filterChain = ApplicationFilterFactory.createFilterChain(request, wrapper, servlet);
																																		        // 执行过滤器链!!!! ，Servlet在过滤器链条的末端，过滤器执行后，就调用servlet.service(....)
																																		    	// org.apache.catalina.connector.RequestFacade
																																		    	// org.apache.catalina.connector.ResponseFacade
																																		        filterChain.doFilter(request.getRequest(), response.getResponse());
																																		        if (filterChain != null) {
																																		            filterChain.release(); // 释放过滤器链资源
																																		        }
																																		        if ((servlet != null) && (wrapper.getAvailable() == Long.MAX_VALUE)) { // 释放StandardWrapper对象
																																		            wrapper.unload();  // 超过指定次数，就进行卸载，调用Servlet的destroy方法
																																		        }
																																	        }
																												            			}
																												            			if (!request.isAsync() && (!asyncAtStart || !response.isErrorReportRequired())) {
																															                context.fireRequestDestroyEvent(request); // 触发销毁事件
																															            }
																												            		}
																												            	}
																												            
																												            }
																												        }
																												        
																												        request.finishRequest();
                																										response.finishResponse(); // 输出数据,并关闭输出流																						                	
																									                }
																									            }
                    																						}
                    																					}
																			                        }
														                                    	}
														                                    }
														                                }
															                        }
															                    }
															                }//while
												            	 		}
												            	 	}
													            }
											                }
											            }
											
														----------------------------------------------------------------
														-------Socket连接“接收器”，接收后会转给后端的“Socket处理器”处理----
														----------------------------------------------------------------
											            startAcceptorThreads(); {　// 启动接受线程
											            	int count = getAcceptorThreadCount(); // 接受网络请求的线程数量
													        acceptors = new Acceptor[count];
													
													        for (int i = 0; i < count; i++) {
													            acceptors[i] = createAcceptor(); // 创建连接接收器 org.apache.tomcat.util.net.NioEndpoint.createAcceptor
													            String threadName = getName() + "-Acceptor-" + i;
													            acceptors[i].setThreadName(threadName);
													            Thread t = new Thread(acceptors[i], threadName);
													            t.setPriority(getAcceptorThreadPriority());
													            t.setDaemon(getDaemon());
													            t.start(); { // 启动接受线程 socket = serverSock.accept();
													            	public void run() {
													            	 	while (true) {
														            		socket = serverSock.accept(); // 阻塞接受socket
														            		if (running && !paused) {
														                        setSocketOptions(socket) {// !!! 委托给 org.apache.tomcat.util.net.NioEndpoint.Poller 进行工作
														                            socket.configureBlocking(false); // 非阻塞
																		            Socket sock = socket.socket();
																		            socketProperties.setProperties(sock);
																		            channel = new NioChannel(socket, bufhandler);//!!!!
																		            getPoller0().register(channel); { // org.apache.tomcat.util.net.NioEndpoint.Poller.register(channel); 轮询算法获取一个Poller
														                        		socket.setPoller(this);
																			            NioSocketWrapper ka = new NioSocketWrapper(socket, NioEndpoint.this);
																			            socket.setSocketWrapper(ka);
																			            ka.setPoller(this);
																			            ka.setReadTimeout(getSocketProperties().getSoTimeout());
																			            ka.setWriteTimeout(getSocketProperties().getSoTimeout());
																			            ka.setKeepAliveLeft(NioEndpoint.this.getMaxKeepAliveRequests());
																			            ka.setSecure(isSSLEnabled());
																			            ka.setReadTimeout(getSoTimeout());
																			            ka.setWriteTimeout(getSoTimeout());
																			            PollerEvent r = eventCache.pop(); // 取得事件
																			            ka.interestOps(SelectionKey.OP_READ);//this is what OP_REGISTER turns into.
																			            if ( r==null) r = new PollerEvent(socket,ka,OP_REGISTER); // 创建事件
																			            else r.reset(socket,ka,OP_REGISTER); // 重置事件
																			            addEvent(r); {  // 添加事件!!!! events.offer(r);
																			            	private void addEvent(PollerEvent event) {
																					            events.offer(event);
																					            if ( wakeupCounter.incrementAndGet() == 0 ) selector.wakeup();
																					        }
																			            }
														                        	}
														                        }
														                    }
														                 }
													            	}
													            }
													        }
											            }
											        }
						 						}
						 					}
						 				}
						 				org.apache.catalina.connector.Connector.start(){ // protocol="AJP/1.3"
						 					org.apache.coyote.ajp.AjpNioProtocol.start(){
						 						org.apache.tomcat.util.net.NioEndpoint.start(){
						 							if (!running) {
											            running = true;
											            paused = false;
											
											            // 同步栈
											            processorCache = new SynchronizedStack<>(SynchronizedStack.DEFAULT_SIZE,
											                    socketProperties.getProcessorCache());
											            eventCache = new SynchronizedStack<>(SynchronizedStack.DEFAULT_SIZE,
											                            socketProperties.getEventCache());
											            nioChannels = new SynchronizedStack<>(SynchronizedStack.DEFAULT_SIZE,
											                    socketProperties.getBufferPool());
											
											            // Create worker collection
											            if ( getExecutor() == null ) {
											                createExecutor(); { //!!!
											                	internalExecutor = true;
														        TaskQueue taskqueue = new TaskQueue();
														        TaskThreadFactory tf = new TaskThreadFactory(getName() + "-exec-", daemon, getThreadPriority());
														        executor = new ThreadPoolExecutor(getMinSpareThreads(), getMaxThreads(), 60, TimeUnit.SECONDS,taskqueue, tf);
														        taskqueue.setParent( (ThreadPoolExecutor) executor);
											                }
											            }
											
											            initializeConnectionLatch(); // 初始化连接数量阈值
											
											            // Start poller threads
											            pollers = new Poller[getPollerThreadCount()]; // 启动pollers
											            for (int i=0; i<pollers.length; i++) {
											                pollers[i] = new Poller();
											                Thread pollerThread = new Thread(pollers[i], getName() + "-ClientPoller-"+i);
											                pollerThread.setPriority(threadPriority);
											                pollerThread.setDaemon(true);
											                pollerThread.start();
											            }
											
											            startAcceptorThreads(); // 启动接受线程
											        }
						 						}
						 					}
						 				}
						 			}
		    					}
		    				}
			    			触发StandardServer的事件 after_start
		    			}
	    			}
	    		}
		 	}
		 	----------------------------
		 	

		 */
	}

	public static void socket1() throws Exception{
		// ----------------------------
		ServerSocketChannel serverSock = null;
		serverSock = ServerSocketChannel.open();
		int backlog = 100;
		Integer soTimeout = Integer.valueOf(20000);
		/*
	    serverSock.setReceiveBufferSize(rxBufSize.intValue());
    	serverSock.setPerformancePreferences(
                performanceConnectionTime.intValue(),
                performanceLatency.intValue(),
                performanceBandwidth.intValue());
    	serverSock.setReuseAddress(soReuseAddress.booleanValue());
    	serverSock.setSoTimeout(soTimeout.intValue());
		 */
		InetSocketAddress addr = new InetSocketAddress(InetAddress.getByName("127.0.0.1"),8080);
		serverSock.socket().bind(addr,backlog);
		serverSock.configureBlocking(true); //mimic APR behavior
		serverSock.socket().setSoTimeout(soTimeout.intValue());
	
        
		// ----------------------------
		NioSelectorPool selectorPool = new NioSelectorPool();
		selectorPool.open();
		
		// 接受socket连接
		{
			SocketChannel socket = serverSock.accept(); 
			socket.configureBlocking(false); // 非阻塞
	        Socket sock = socket.socket();
		}
	}
}
