package cn.java.noteagain;


/**
	
	StandardHost代表主机
	StandardContext代表应用目录：里存放的是/WEB-INF/web.xml的配置信息，以及目录的名称：如example
	ApplicationContext代表应用目录：是StandardContext的内部对象，存放的数据和 StandardContext 类似，但是比StandardContext少
	StandardWrapper代表web.xml文件中的Servlet标签的信息
	
	参数：
		org.apache.catalina.core.StandardWrapperFacade
		org.apache.catalina.connector.RequestFacade
		org.apache.catalina.connector.ResponseFacade
		
	
	路由：
		会通过路由匹配到StandardHost、StandardContext、StandardWrapper

 */


public class FinalNote1 {

	public static void note1() throws Exception {
		/*
		**********************************************************
								启动的过程
		**********************************************************
		// 创建 对象
		Bootstrap daemon = new org.apache.catalina.startup.Bootstrap();
		
		// 初始化设置环境变量，创建org.apache.catalina.startup.Catalina对象
		daemon.init();
		
		//调用org.apache.catalina.startup.Catalina对象的load方法
		daemon.load(null);
		{
			//1、构造server.xml的Digester的对象树
			
			//2、调用Digester的对象树initialize()方法，创建 serverSocket
			 
		}
		
		//调用org.apache.catalina.startup.Catalina对象的start方法，
		daemon.start();
		{
			org.apache.catalina.core.StandardServer.start() 
			{
				org.apache.catalina.core.StandardService.start() 
				{
					//调用Digester的对象树start()方法
					org.apache.catalina.core.StandardEngine.start() 
					{
						org.apache.catalina.core.StandardHost.start() 
						{
							触发事件org.apache.catalina.startup.HostConfig.start();
							{
								//...........................................................
								//....................动态添加(发布)虚拟目录....................
								//...........................................................
								//这里面会有三种方式检查到达发布了多少应用: 1、应用的配置文件或者2、应用目录或者3、war应用发布应用
								//创建org.apache.catalina.core.StandardContext对象，并添加到host节点上
								org.apache.catalina.startup.HostConfig.deployApps()
								{
									Context context = (Context) Class.forName(contextClass).newInstance();// contextClass === org.apache.catalina.core.StandardContext
									if (context instanceof Lifecycle) {//添加上下文事件监听器
									        Class clazz = Class.forName(host.getConfigClass());
									        LifecycleListener listener =
									        (LifecycleListener) clazz.newInstance();
									        ((Lifecycle) context).addLifecycleListener(listener);
									}
									context.setPath("example");//空
            						context.setDocBase("example");// ROOT
									host.addChild(context);//把上下文添加到虚拟主机中，这里面会触发 ((Lifecycle) context).start();方法
									//org.apache.catalina.core.StandardHost.addChild(Container child)  // child === org.apache.catalina.core.StandardContext 代表发布的目录
									{
										org.apache.catalina.core.ContainerBase.addChild(Container child)
										{
											org.apache.catalina.core.ContainerBase.addChildInternal(Container child)
											{
												child.setParent('org.apache.catalina.core.StandardHost');//存储了对父类StandardHost的依赖
												children.put(child.getName(), child);
												if (started && startChildren && (child instanceof Lifecycle)) 
												{
													//调用StandardContext的start方法-----------
													((Lifecycle) child).start();
													//org.apache.catalina.core.StandardContext.start();
													{
										            	// 内部解析conf/context.xml配置文件，进行解析
										                org.apache.catalina.core.StandardContext.init();
										                {
										                	LifecycleListener config = new org.apache.catalina.startup.ContextConfig();
										                	StandardContext.addLifecycleListener(config);// 添加事件监听器............
										                }
										                
														// 预先注册JMX对象
														//	objectName === Catalina:j2eeType=WebModule,name=//localhost/,J2EEApplication=none,J2EEServer=none
														//	objectName === Catalina:j2eeType=WebModule,name=//localhost/docs,J2EEApplication=none,J2EEServer=none
														//	objectName === Catalina:j2eeType=WebModule,name=//localhost/examples,J2EEApplication=none,J2EEServer=none
														//  objectName === Catalina:j2eeType=WebModule,name=//localhost/host-manager,J2EEApplication=none,J2EEServer=none
														//	objectName === Catalina:j2eeType=WebModule,name=//localhost/manager,J2EEApplication=none,J2EEServer=none    
														//   ......
												        preRegisterJMX();//预先创建JMX objectName对象
												        
														StandardContext.setAvailable(false);
        												StandardContext.setConfigured(false);
												        
												        // 工作目录  work/Catalina/localhost/上下文名称（如：examples）
												        // 创建org.apache.catalina.core.ApplicationContext对象
														StandardContext.postWorkDirectory();
														{
															String workDir = getWorkDir();
															if (workDir == null || workDir.length() == 0)
															{
																workDir = "work" + File.separator + engineName +
																 	File.separator + hostName + File.separator + temp;//   work/Catalina/localhost/上下文名称（如：examples）
																setWorkDir(workDir);
															}
															File dir = new File(workDir);
															dir.mkdirs();//目录不存在，创建目录
															if (StandardContext.context == null) 
															{
																//创建org.apache.catalina.core.ApplicationContext对象........................
													            StandardContext.getServletContext();
													            {
													            	//context变量在StandardContext类下，-----------------------
													                ApplicationContext context = new org.apache.catalina.core.ApplicationContext(getBasePath(), this);
													            	return (context.getFacade());
													            }
													            
													            StandardContext.context.setAttribute(Globals.WORK_DIR_ATTR, dir);//javax.servlet.context.tempdir 设置临时目录
        														StandardContext.context.setAttributeReadOnly(Globals.WORK_DIR_ATTR);//javax.servlet.context.tempdir  设置目录权限
													        }
														}
														
														//以下代码块的 context ，在 ContextConfig 类内部,表示StandardContext---------------------------
														lifecycle.fireLifecycleEvent(START_EVENT, null);//事件监听器的“开始事件”处理器
														//org.apache.catalina.startup.ContextConfig.lifecycleEvent()
														{
															Context ContextConfig.context = (Context) event.getLifecycle();//事件触发地   context === StandardContext
															if (event.getType().equals(Lifecycle.START_EVENT)) 
															{
																// 使用Digester，解析配置文件：
											                  	// conf/web.xml
											                  	// conf/Catalina/localhost/web.xml.default
											                  	// /WEB-INF/web.xml
											                  	//把配置信息，反向注入org.apache.catalina.core.StandardContext
		                										lifecycle.fireLifecycleEvent(START_EVENT, null);//触发事件处理器 org.apache.catalina.startup.ContextConfig.lifecycleEvent(LifecycleEvent event)
		                										//org.apache.catalina.startup.ContextConfig.start()
		                										{
		                											// 使用Digester，解析配置文件：
		                											// 	conf/web.xml
		                											// 	conf/Catalina/localhost/web.xml.default
		                											// 把配置信息，反向注入org.apache.catalina.core.StandardContext
		                											ContextConfig.defaultWebConfig();
		                											{
		                												stream = new FileInputStream(file);//读取“conf/web.xml”配置文件
		                												//使用Digester，解析配置文件： “conf/web.xml”
		                												//把配置信息，反向注入org.apache.catalina.core.StandardContext
		                												ContextConfig.processDefaultWebConfig(webDigester, stream, source);
		                												{
		                													 digester.push(context);//上下文件路径
		                													 digester.parse(source);
		                												}
		                												
		                												String resourceName = ContextConfig.getHostConfigPath(Constants.HostWebXml);//  Catalina/localhost/web.xml.default
		        														file = new File(getConfigBase(), resourceName);//  conf/Catalina/localhost/web.xml.default
		        														source = new InputSource("file://" + file.getAbsolutePath());
		                												stream = new FileInputStream(file);
		                												
		                												//使用Digester，解析配置文件conf/Catalina/localhost/web.xml.default
		                												//把配置信息，反向注入org.apache.catalina.core.StandardContext
		                												ContextConfig.processDefaultWebConfig(webDigester, stream, source);
		                												{
		                													digester.push(context);//上下文件路径
		                													digester.parse(source);
		                												}
		                											}
		                											
		                											//用Digester解析网站的配置文件/WEB-INF/web.xml
		                											//把配置信息反向注入：org.apache.catalina.core.StandardContext
		                											ContextConfig.applicationWebConfig();
		                											{
		                												// context === org.apache.catalina.core.StandardContext 触发事件的时候注入context的
		                												ServletContext servletContext = ContextConfig.context.getServletContext(); 
		                												//读取配置文件：  /WEB-INF/web.xml
		                												stream = servletContext.getResourceAsStream(Constants.ApplicationWebXml);
		                												//配置文件位置：/WEB-INF/web.xml
		                												url = servletContext.getResource(Constants.ApplicationWebXml);
		                												InputSource is = new InputSource(url.toExternalForm());
		                												is.setByteStream(stream);
		                												
		                												//用Digester解析网站的配置文件/WEB-INF/web.xml
		                												//把配置信息反向注入：org.apache.catalina.core.StandardContext
		                												webDigester.push(context);
		                												webDigester.parse(is);
		                											}
		                										}
	                										}
														}
									                   

														StandardContext.contextManager = new StandardManager();//创建标准管理器
														StandardContext.getServletContext().setAttribute(Globals.RESOURCES_ATTR, getResources());//org.apache.catalina.resources设置资源路径
														
														// System.out.println("上下文件路径："+getPath());//  /examples  /host-manager
												        // System.out.println("默认匹配的文件："+welcomeFiles[0]);//  index.html   index.html
												        StandardContext.mapper.setContext(getPath(), welcomeFiles, resources);//保存映射内容
												        {
												        	
												        }
												        
												        StandardContext.getServletContext().setAttribute(AnnotationProcessor.class.getName(), annotationProcessor);//保存注解信息
                
												        StandardContext.postWelcomeFiles();//org.apache.catalina.WELCOME_FILES 保存欢迎文件
												        {
												        	getServletContext().setAttribute("org.apache.catalina.WELCOME_FILES",welcomeFiles);
												        }
												        
												        // Set up the context init params  合并，保存上下文初始化参数，保存到org.apache.catalina.core.ApplicationContext 
            											StandardContext.mergeParameters();
            											{
            												Map<String,String> mergedParams = new HashMap<String,String>();
            												ApplicationParameter params[] = findApplicationParameters();//应用参数
            												for (int i = 0; i < params.length; i++) 
            												{
            													mergedParams.put(params[i].getName(), params[i].getValue());
            												}
            												for (Map.Entry<String,String> entry : mergedParams.entrySet()) 
            												{
            													StandardContext.context.setInitParameter(entry.getKey(), entry.getValue());// context === org.apache.catalina.core.ApplicationContext
            												}
            											}
            											
            											// 启动监听器
									            	 	//org.apache.catalina.core.StandardContext.findApplicationListeners()
										            	// 	添加ApplicationContext，并触发ApplicationContext事件监听器 
										            	// 	<web-app>
											            //	 	<listener>
											            // 	 		<listener-class>cn.java.listener.ClassName</listener-class>
											            // 	 	</listener>
										            	// 	</web-app>
										                if (!StandardContext.listenerStart()) {//
										                    log.error( "Error listenerStart");
										                    ok = false;
										                }
										                
										            	//  org.apache.catalina.core.ApplicationFilterConfig
										            	// <web-app>
												        //     <filter>
														//		<filter-name>myFilter</filter-name>  
												        //		<filter-class>cn.java.MyFilter</filter-class> 
														//	 </filter>
												        //     <filter>....</filter>
										             	// </web-app>
										            	//  filterConfig = new ApplicationFilterConfig(this, "cn.java.MyFilter"); //包装了过滤器类
										                //  filterConfigs.put(name, filterConfig);
										                  	
										                // 	把过滤器添加到filterConfigs， <web-app><filter>....</filter></web-app>
										                if (!StandardContext.filterStart()) {//
										                    log.error( "Error filterStart");
										                    ok = false;
										                }
										                
										            	// Digester的描述是：
											            //	 digester.addRule(prefix + "web-app/servlet", new WrapperCreateRule());
													    //   digester.addSetNext(prefix + "web-app/servlet","addChild","org.apache.catalina.Container");
													    // 	启动要首次启动，就直接启动的<servlet>
										                StandardContext.loadOnStartup(findChildren());//
										                
													    //         注册对象（应用目录，如：example，doc）
														//          objectName === Catalina:j2eeType=WebModule,name=//localhost/,J2EEApplication=none,J2EEServer=none
														//		  objectName === Catalina:j2eeType=WebModule,name=//localhost/docs,J2EEApplication=none,J2EEServer=none
														//		  ....
														//	注册JMX对象（<servlet>标签）
														//		  Catalina:j2eeType=Servlet,name=default,WebModule=//localhost/docs,J2EEApplication=none,J2EEServer=none
														//				  ....
														//		  Catalina:j2eeType=Servlet,name=jsp,WebModule=//localhost/,J2EEApplication=none,J2EEServer=none
														//				  ....
												        // JMX registration
												        StandardContext.registerJMX();//注册JMX对象
												        {
												        	 // 根据web.xml的Digester描述，可以知道findChildren返回的结果是web.xml配置文件中<servlet>标签信息
												        	 // 描述为digester.addSetNext(prefix + "web-app/servlet","addChild","org.apache.catalina.Container");注入的对象是StandardContext
												        	 Container children[] = findChildren();//<servlet>标签
												             for (int i=0; children!=null && i<children.length; i++) {
												                ((StandardWrapper)children[i]).registerJMX( this );//注册Wrapper对象
												             }
												        }
													}
													
													
												}
											}
										}
									}
									HostConfig.addWatchedResources(deployedApp, dir.getAbsolutePath(), context);//目录监控
								}
							}
						}
					}
					
					org.apache.catalina.connector.Connector.start()//在org.apache.catalina.core.StandardService.start() 调用
					{
						//会启动Socket监听
						org.apache.coyote.http11.Http11Protocol().start()
						{
				        	 // 创建几个接收线程，并把在init中new的ServerSocket传递进去， new Acceptor()
				        	 // 通过accept()等待连接，返回socket  Acceptor.run()
				        	 // 根据socket进行相应的处理 processSocket(Socket socket)
				        	 // 
				        	 // 1、开启接受处理线程Thread，处理器是：new org.apache.tomcat.util.net.JIoEndpoint.Acceptor()
				        	 // org.apache.tomcat.util.net.JIoEndpoint.start
				            endpoint.start();//org.apache.tomcat.util.net.JIoEndpoint.start();
				            {
				            	for (int i = 0; i < acceptorThreadCount; i++) {//创建一个接受者
					                Thread acceptorThread = new Thread(new Acceptor(), getName() + "-Acceptor-" + i);
					                {
					                	org.apache.tomcat.util.net.JIoEndpoint.Acceptor.run()
					                	{
					                		Socket socket = serverSocketFactory.acceptSocket(serverSocket);//====等价serverSocket.accept(); 会阻塞住
					                		//处理客户端的请求 ---------处理socket请求开始
					                		if (!processSocket(socket)
					                		{
					                			if (executor == null) {//执行这边
					                				getWorkerThread().assign(socket);//取得线程，调用线程的方法assign，  取得线程，并唤醒所有线程进行争抢CPU时间片
					                			} else {
									            	 // process();
									            	 // 在这边处理
									            	 // SocketProcessor是socket处理器
									            	 // SocketProcessor是内部类
									                executor.execute(new SocketProcessor(socket));
									                //org.apache.tomcat.util.net.JIoEndpoint.SocketProcessor.run()
									                {
									                	
									                	handler.process(socket);
									                	//org.apache.coyote.http11.Http11Protocol.Http11ConnectionHandler.process(Socket socket)
													 	{
													 		//构造函数中  this.endpoint = endpoint;
													 		//org.apache.coyote.http11.Http11Protocol.Http11ConnectionHandler.createProcessor()中processor.setAdapter(proto.adapter);
													 		org.apache.coyote.http11.Http11Processor.process(Socket theSocket)
													 		{
													 			 // org.apache.coyote.http11.InternalInputBuffer 输入流
																 // org.apache.coyote.http11.InternalOutputBuffer 输出流
													 		     inputBuffer.setInputStream(socket.getInputStream());//socket输入流
        														 outputBuffer.setOutputStream(socket.getOutputStream());//socket输出流
        														 
        														 // InternalInputBuffer inputBuffer
											                 	 // 解析出socket流，解析出method、URI、queryString、protocol
											                 	 // 赋值到request对象中
        														 inputBuffer.parseRequestLine();//解析请求的数据...........
        														 
        														 // headers
												                 // MimeHeaderField
												                 // 解析其他字段
        														 inputBuffer.parseHeaders();//解析请求的头...
        														 
													 			 inputBuffer.parseHeaders();//解析请求的头.......
											//		 			 	request.protocol()   请求协议 http  https
											//	        	        request.method()  请求方式 GET POST
											//	        	        MimeHeaders headers = request.getMimeHeaders();//取得头部信息  如text/html
											//	        	        headers.getValue("connection")//是否持久连接
											//	        	        headers.getValue("expect")
											//	        	        headers.getValue("user-agent");
											//	        	        request.requestURI().getByteChunk();
											//	        	        headers.getValue("transfer-encoding");
											//	        	        request.getContentLengthLong();
											//	        	        headers.getValue("host");
													 			 prepareRequest();//请求数据处理
													 			 
													 			 org.apache.catalina.connector.CoyoteAdapter.service(request, response)
													 			 {
													 			 	//重新包装
													 			 	org.apache.catalina.connector.Request request = (Request) req.getNote(ADAPTER_NOTES);//req.notes[1]
											        				org.apache.catalina.connector.Response response = (Response) res.getNote(ADAPTER_NOTES);//req.notes[1]
											        				
											        				org.apache.catalina.connector.CoyoteAdapter.postParseRequest(Request req, Request request, Response res, Response response)
											        				{
											        					//填充映射路径，这个怎么取
											        					this.connector.getMapper().map(serverName, decodedURI,request.getMappingData());
											        					
											        					//设置上下文件，应用的上下文
											        					request.setContext((Context) request.getMappingData().context);
											        					{
											        					
											        					}
											        					
											        					//设置包装器  web.xml文件中的Servlet对象
											        					request.setWrapper((Wrapper) request.getMappingData().wrapper);
											        					{
											        					
											        					}
											        					
											        					String sessionID = request.getPathParameter(Globals.SESSION_PARAMETER_NAME);//jsessionid
											         					//从req取得cookie的，如果存在jsessionid的cookie信息，就把COOKIE ID设置进入request
											        					parseSessionCookiesId(req, request);
											        				}
											        				
											        				//----------------------如果以上内容解析成功那么，会执行如下部分----------------------
											        				
											        				//链条式层级调用
											        				{
											        					
											        					
											        					//第一   container // === new org.apache.catalina.core.StandardEngine()
														            	//第二   getPipeline() === standardPipeline =  // === new org.apache.catalina.core.StandardPipeline(StandardEngine)// 类的聚合变量
														            	//第三   getFirst() === valve = standardPipeline.getFirst() // === org.apache.catalina.core.StandardEngineValve(); //在StandardEngine构造函数调用 standardPipeline.setBasic()设置的
														            	//第四   getFirst().invoke(request, response); // === StandardEngineValve.invoke(request, response)
														            	 connector.getContainer().getPipeline().getFirst().invoke(request, response);
														            	 {
																	          //host ==== org.apache.catalina.core.StandardHost 这个
																	          //getPipeline() = host.getPipeline() ==== new StandardPipeline(StandardHost);
																	          //getFirst() === standardHostValve === new org.apache.catalina.core.StandardHostValve()
																	          //getFirst().invoke(request, response);
																	          host.getPipeline().getFirst().invoke(request, response);
																	          {
																		           //上下文调用
																			       // context = org.apache.catalina.core.StandardContext
																			       // getPipeline() === new StandardPipeline(StandardContext);//类的聚合变量
																			       // getFirst() === new StandardContextValve()//在StandardContext的构造函数赋值
																			       // getFirst().invoke(request, response); //===StandardContextValve.invoke(request, response)
																		           context.getPipeline().getFirst().invoke(request, response);
																		           org.apache.catalina.core.StandardContextValve.invoke()
																		           {
																		           		
																				    	// 禁止任何直接访问META-INF和WEB-INF的行为
																				        // Disallow any direct access to resources under WEB-INF or META-INF
																				        MessageBytes requestPathMB = request.getRequestPathMB();
																				        if ((requestPathMB.startsWithIgnoreCase("/META-INF/", 0))
																				            || (requestPathMB.equalsIgnoreCase("/META-INF"))
																				            || (requestPathMB.startsWithIgnoreCase("/WEB-INF/", 0))
																				            || (requestPathMB.equalsIgnoreCase("/WEB-INF"))) {
																				            notFound(response);
																				            return;
																				        }
																				        
																				        Wrapper wrapper = request.getWrapper();// 这个就是web.xml文件中 <servlet>标签的信息
																				        
																				        //取得上下文的监听器列表
																				        // context === StandardContext
																				        //在调用时:StandardPipeline.setBasic(new StandardContextValve());会执行
																		          	    //{
																		          	    // 	StandardContextValve.setContainer(StandardContext);
																		          	    // 	StandardPipeline.basic = new StandardContextValve();	
																		          	    //}设置的
																				        Object instances[] = context.getApplicationEventListeners();//=== StandardContext.getApplicationEventListeners();
																				        
																				        //触发监听器--初始化事件..........................
																				        event = new ServletRequestEvent(((StandardContext) container).getServletContext(), //触发者
																				        // create pre-service event
																				        for (int i = 0; i < instances.length; i++) {
																				        	//触发监听器
																				        	ServletRequestListener listener = (ServletRequestListener) instances[i];
																				            listener.requestInitialized(event);//触发事件
																				        }
																				        
																				        // .............执行Servlet.............
																				        // wrapper = org.apache.catalina.core.StandardWrapper
																				        // getPipeline() = new StandardPipeline(StandardWrapper);
																				        // getFirst() = StandardWrapperValve();
																				        wrapper.getPipeline().getFirst().invoke(request, response);
																				        {
																				        	org.apache.catalina.core.StandardWrapperValve.invoke();
																				        	{
																				        		StandardWrapper wrapper = (StandardWrapper) getContainer();//Digester解析出他们的关系
																						      
																						        Servlet servlet = null;
																						        Context context = (Context) wrapper.getParent();//取得上下文目录对象org.apache.catalina.core.StandardContext
																						        //创建Servlet实体
																						        servlet = wrapper.allocate();
																						        {
																						        	org.apache.catalina.core.StandardWrapper.loadServlet() 
																						        	{
																						        		 String actualClass = servletClass;
																						        		 if ((actualClass == null) && (jspFile != null)) //如果访问的是JSP页面.................
																						        		 {
																						        		 	 Wrapper jspWrapper = (Wrapper)((Context) getParent()).findChild(Constants.JSP_SERVLET_NAME);
																						        		 	 if (jspWrapper != null) 
																						        		 	 {
																						        		 	 	actualClass = jspWrapper.getServletClass();
																						        		 	 	String paramNames[] = jspWrapper.findInitParameters();
																							                    for (int i = 0; i < paramNames.length; i++) {
																							                        if (parameters.get(paramNames[i]) == null) {
																							                            parameters.put
																							                                (paramNames[i], 
																							                                 jspWrapper.findInitParameter(paramNames[i]));
																							                        }
																							                    }
																						        		 	 }
																						        		 }
																						        		 Loader loader = StandardWrapper.getLoader();
																						        		 ClassLoader classLoader = loader.getClassLoader();
																						        		 classClass = classLoader.loadClass(actualClass);//加载Servlet类
																						        		 servlet = (Servlet) classClass.newInstance();//实例化Servlet.................1
																						        		 StandardWrapperFacade facade = new org.apache.catalina.core.StandardWrapperFacade($StandardWrapper);
																						        		 servlet.init(facade);//Servlet初始化.................2
																						        	}
																						        }
																						        response.sendAcknowledgement();
																						        
																						        //过滤器工厂
																						        ApplicationFilterFactory factory = ApplicationFilterFactory.getInstance();
																						        
																						        //创建过滤器链条,在链条的末尾才调用的Servlet
																						        ApplicationFilterChain filterChain = factory.createFilterChain(request, wrapper, servlet);
																						        
																						        //如果包装的是JSP文件
																						        String jspFile = wrapper.getJspFile();// wrapper是<servlet>解析处理的描述
																						        if (jspFile != null)//如果包装的是JSP文件
																					            	request.setAttribute(Globals.JSP_FILE_ATTR, jspFile);
																					            else
																					            	request.removeAttribute(Globals.JSP_FILE_ATTR);
											            	
																						        //.............触发过滤器链条，在链条的末尾才调用的Servlet.............3
																						        //参数又做了一层的封装，减少了内部API的数量
																						        //HttpServletRequest  == request.getRequest(); == org.apache.catalina.connector.Request.getRequest() == new RequestFacade(org.apache.catalina.connector.Request)
																						        //HttpServletResponse == response.getResponse(); == org.apache.catalina.connector.Response.getResponse() == new ResponseFacade(org.apache.catalina.connector.Response);
																						        filterChain.doFilter(request.getRequest(), response.getResponse());
																						        {
																						        
																						        }
																						        
																						        //释放过滤器链条
																						        filterChain.release();
																						        
																						        //释放Servlet
																						        wrapper.deallocate(servlet);
																						        
																						        //释放wrapper
																						        wrapper.unload();
																				        	}
																				        }
																				        
																				        //触发监听器---销毁事件
																				        event = new ServletRequestEvent(((StandardContext) container).getServletContext(), 
																				        // create pre-service event
																				        for (int i = 0; i < instances.length; i++) {
																				        	//触发监听器
																				        	ServletRequestListener listener = (ServletRequestListener) instances[i];
																				            listener.requestDestroyed(event);//请求销毁
																				        }
																		           }
																	          }
														            	}
											
											
											        				}
													 			 }
													 		}
													 	//---------处理socket请求结束
									                }
									            }
					                		
					                		}) 
					                		{
					                			socket.close();
					                		}
					                	}
					                }
					                acceptorThread.setPriority(threadPriority);//线程权重
					                acceptorThread.setDaemon(daemon);//true
					                acceptorThread.start();
					            }
				            }
						}
					}
					
					//-----------------初始化路由路径-----------------
				 		if( this.domain != null ) {//Catalina
					        	 * mapperListener = new MapperListener(mapper, this);
					            mapperListener.setDomain( domain );//Catalina
					            //mapperListener.setEngine( service.getContainer().getName() );
					             * *******************
					             * 		构造映射路径和处理Servlet的对象树
					             * *******************
					             * 把默认虚拟主机信息设置到Mapper中，mapper.setDefaultHostName(defaultHost);
					             * 迭代Host的Dom对象  Catalina:type=Host,*
					             * 		把所有的Host标签（并处理了Alias别名标签）的映射信息添加到Mapper对象中 mapper.addHost(name, aliases, objectName);
					             * 		host.addContainerListener(this);//添加上下文监听器
					             * 
					             * 迭代上下文列表，*:j2eeType=WebModule,*，即访问目录路径。。。
					             * 		  把上下文的配置信息添加到Host对象里面
					             * 		 host.contextList.contexts = newContexts;//添加新的上下文
					             * 
					             * 迭代包装器：  "*:j2eeType=Servlet,*"
					             * 		这是JSP的核心了，主要是作为Servlet和JSP的最前端  （org.apache.jasper.servlet.JspServlet和org.apache.catalina.servlets.DefaultServlet）
					             *		  把包装器的配置信息添加到Host对象里面
					             *		addWrapper(context, path, wrapper, jspWildCard);
					             *		context.wildcardWrappers = newWrappers;//context == host.contextList[i]
					             *		context.extensionWrappers = newWrappers;
					             *		context.defaultWrapper = newWrapper;
					             *		context.exactWrappers = newWrappers;
					             *
					             * 把mapperListener设为JMX的广播包的接收者  JMImplementation:type=MBeanServerDelegate,可能做为回调。。。。。。。。。。。。。
					             * 		objectName = new ObjectName("JMImplementation:type=MBeanServerDelegate");
					             * 		mBeanServer.addNotificationListener(objectName, mapperListener, null, null);
					             
					             处理映射关系：
					             org.apache.catalina.core.StandardContext 就是目录  如:example  或者  doc
					
					             org.apache.catalina.core.StandardWrapper 就是  <servlet>标签解析处理的对象
								
								 org.apache.catalina.connector.MapperListener.mapper 中保存，映射关系
					            
					             //localhost,/,<servlet-mapping>的配置，org.apache.jasper.servlet.JspServlet，false
					             mapper.addWrapper(hostName, contextName, mappings[i], wrapper,
					                              jspWildCard);
					            mapperListener.init();//
				 		}
				 	}					
				}
			}
		}
		*/
	}
}
