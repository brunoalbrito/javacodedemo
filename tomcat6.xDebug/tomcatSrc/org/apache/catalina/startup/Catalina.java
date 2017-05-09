/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package org.apache.catalina.startup;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.LogManager;

import javax.management.ObjectName;

import org.apache.catalina.Container;
import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Server;
import org.apache.catalina.core.StandardServer;
import org.apache.juli.ClassLoaderLogManager;
import org.apache.tomcat.util.IntrospectionUtils;
import org.apache.tomcat.util.digester.Digester;
import org.apache.tomcat.util.digester.ObjectCreateRule;
import org.apache.tomcat.util.digester.Rule;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;


/**
 * Startup/Shutdown shell program for Catalina.  The following command line
 * options are recognized:
 * <ul>
 * <li><b>-config {pathname}</b> - Set the pathname of the configuration file
 *     to be processed.  If a relative path is specified, it will be
 *     interpreted as relative to the directory pathname specified by the
 *     "catalina.base" system property.   [conf/server.xml]
 * <li><b>-help</b>      - Display usage information.
 * <li><b>-nonaming</b>  - Disable naming support.
 * <li><b>start</b>      - Start an instance of Catalina.
 * <li><b>stop</b>       - Stop the currently running instance of Catalina.
 * </u>
 *
 * Should do the same thing as Embedded, but using a server.xml file.
 *
 * @author Craig R. McClanahan
 * @author Remy Maucherat
 *
 */

public class Catalina extends Embedded {


    // ----------------------------------------------------- Instance Variables


    /**
     * Pathname to the server configuration file.
     */
    protected String configFile = "conf/server.xml";

    // XXX Should be moved to embedded
    /**
     * The shared extensions class loader for this server.
     */
    protected ClassLoader parentClassLoader =
        Catalina.class.getClassLoader();


    /**
     * Are we starting a new server?
     */
    protected boolean starting = false;


    /**
     * Are we stopping an existing server?
     */
    protected boolean stopping = false;


    /**
     * Use shutdown hook flag.
     */
    protected boolean useShutdownHook = true;


    /**
     * Shutdown hook.
     */
    protected Thread shutdownHook = null;


    // ------------------------------------------------------------- Properties


    public void setConfig(String file) {
        configFile = file;
    }


    public void setConfigFile(String file) {
        configFile = file;
    }


    public String getConfigFile() {
        return configFile;
    }


    public void setUseShutdownHook(boolean useShutdownHook) {
        this.useShutdownHook = useShutdownHook;
    }


    public boolean getUseShutdownHook() {
        return useShutdownHook;
    }


    /**
     * Set the shared extensions class loader.
     *
     * @param parentClassLoader The shared extensions class loader.
     */
    public void setParentClassLoader(ClassLoader parentClassLoader) {

        this.parentClassLoader = parentClassLoader;

    }


    // ----------------------------------------------------------- Main Program

    /**
     * The application main program.
     *
     * @param args Command line arguments
     */
    public static void main(String args[]) {
        (new Catalina()).process(args);
    }


    /**
     * The instance main program.
     *
     * @param args Command line arguments
     */
    public void process(String args[]) {

        setAwait(true);
        setCatalinaHome();
        setCatalinaBase();
        try {
            if (arguments(args)) {
                if (starting) {
                    load(args);
                    start();
                } else if (stopping) {
                    stopServer();
                }
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }


    // ------------------------------------------------------ Protected Methods


    /**
     * Process the specified command line arguments, and return
     * <code>true</code> if we should continue processing; otherwise
     * return <code>false</code>.
     *
     * @param args Command line arguments to process
     */
    protected boolean arguments(String args[]) {

        boolean isConfig = false;

        if (args.length < 1) {
            usage();
            return (false);
        }

        for (int i = 0; i < args.length; i++) {
            if (isConfig) {
                configFile = args[i];
                isConfig = false;
            } else if (args[i].equals("-config")) {
                isConfig = true;
            } else if (args[i].equals("-nonaming")) {
                setUseNaming( false );
            } else if (args[i].equals("-help")) {
                usage();
                return (false);
            } else if (args[i].equals("start")) {
                starting = true;
                stopping = false;
            } else if (args[i].equals("stop")) {
                starting = false;
                stopping = true;
            } else {
                usage();
                return (false);
            }
        }

        return (true);

    }


    /**
     * Return a File object representing our configuration file.
     */
    protected File configFile() {

        File file = new File(configFile);//conf/server.xml
        if (!file.isAbsolute())
            file = new File(System.getProperty("catalina.base"), configFile);//"catalina.base"/conf/server.xml
        return (file);

    }


    /**
     * Create and configure the Digester we will be using for startup.
     */
    protected Digester createStartDigester() {
        long t1=System.currentTimeMillis();
        // Initialize the digester
        Digester digester = new Digester();
        digester.setValidating(false);// digester.validating = false;
        digester.setRulesValidation(true);// digester.rulesValidation = true;
       
        HashMap<Class, List<String>> fakeAttributes = new HashMap<Class, List<String>>();
        ArrayList<String> attrs = new ArrayList<String>();
        attrs.add("className");
        fakeAttributes.put(Object.class, attrs);
        /**
         * digester.fakeAttributes = fakeAttributes;
         */
        digester.setFakeAttributes(fakeAttributes);// Object.class,ArrayList<String>
        /**
         * digester.classLoader = StandardServer.class.getClassLoader())
         */
        digester.setClassLoader(StandardServer.class.getClassLoader());//定义类加载器

        /**
         * 遇到Server，如果没有设置className属性，就实例化org.apache.catalina.core.StandardServer的一个对象
         * （如果设置了className,就使用设置的Class），把这个对象赋值给Catalina的server属性
         * （调用server的setter方法）
         */
        // Configure the actions we will be using
        /**
         * pattern,className,attributeName
         * addRule(pattern,new ObjectCreateRule(className, attributeName));
         * 指明匹配模式和要创建的类
         * 
         * 		在遇到开始标签startElement的时候创建对象，并推送digester.push(instance);
         * 		
         */
        /**
	     	<Server className="org.apache.catalina.core.StandardServer">
	     	
	     	</Server>
	    	RulesBase.cache["Server"]=>array(
	    		new ObjectCreateRule("org.apache.catalina.core.StandardServer", "className")
	    	); 
	    	StandardServer instance = new org.apache.catalina.core.StandardServer()
	    	digester.push(instance);//stack.push(new org.apache.catalina.core.StandardServer());
	    	
	     */
        digester.addObjectCreate("Server",
                                 "org.apache.catalina.core.StandardServer",
                                 "className");
        /**
         * 定义了一个调用setter方法的规则：把这个值设置给谁
         */
        /**
	    	RulesBase.cache["Server"]=>array(
	    		new SetPropertiesRule()
	    	); 
	    	
	    	Object top = digester.peek();//取得栈顶元素，非弹出取得
	    	IntrospectionUtils.setProperty(top, name, value)//反射注入提取到的属性值，到栈顶对象内   port="8005" shutdown="SHUTDOWN"
	    	{
	    		top。port="8005" ;//instance.port="8005" === StandardServer.port="8005"
	    		top。shutdown="SHUTDOWN"
	    	}
	    	把在开始标签中解析到的属性，反射注入创建的对象中。
	     */
        digester.addSetProperties("Server");
        /**
         * 当移动到下一个标签中时的动作
         * 		匹配表达式，方法名，参数类型
         */
        /**
	    	RulesBase.cache["Server"]=>array(
	    		new SetNextRule("setServer", "org.apache.catalina.Server")
	    	); 
	    	//.....没有执行内容
	    	 解析到结束标签的时候，把子节点反向注入到父节点
	    	 Object child = digester.peek(0);
        	 Object parent = digester.peek(1);
	    	 IntrospectionUtils.callMethod1(parent, "setServer",
                child, "org.apache.catalina.Server", digester.getClassLoader());
         */
        digester.addSetNext("Server",
                            "setServer",
                            "org.apache.catalina.Server");
       
        
        /**
         * 匹配到server的下级节点： GlobalNamingResources，就创建一个
         * org.apache.catalina.deploy.NamingResources的实例，
         * 调用server的setGlobalNamingResources方法。其他的类似，再次不一一赘述。
         * 
         */
        digester.addObjectCreate("Server/GlobalNamingResources",
                                 "org.apache.catalina.deploy.NamingResources");
        digester.addSetProperties("Server/GlobalNamingResources");
        digester.addSetNext("Server/GlobalNamingResources",
                            "setGlobalNamingResources",
                            "org.apache.catalina.deploy.NamingResources");

        digester.addObjectCreate("Server/Listener",
                                 null, // MUST be specified in the element
                                 "className");
        digester.addSetProperties("Server/Listener");
        digester.addSetNext("Server/Listener",
                            "addLifecycleListener",
                            "org.apache.catalina.LifecycleListener");
        /**
         * 有一个方法我们不能漏掉：StandardServer下的addService方法（StandardService下的addExecutor方法、
         * StandardServic下的addConnector方法与它类似）
         * 
         * addService这个方法除了调用setter外，还做了一件重要的事情：service.initialize() service.start();这个start方法属于Lifecycle接口，在LifecycleBase里实现，
         	
         	RulesBase.cache["Server"]=>array(
	    		new ObjectCreateRule("org.apache.catalina.core.StandardService", "className"), //new 对象
	    		new SetPropertiesRule(),//反射映射属性
	    		new SetNextRule("addService", "org.apache.catalina.Service")//添加到父元素中
	    	);
         */
        digester.addObjectCreate("Server/Service",
                                 "org.apache.catalina.core.StandardService",
                                 "className");
        digester.addSetProperties("Server/Service");
        digester.addSetNext("Server/Service",
                            "addService",
                            "org.apache.catalina.Service");

        digester.addObjectCreate("Server/Service/Listener",
                                 null, // MUST be specified in the element
                                 "className");
        digester.addSetProperties("Server/Service/Listener");
        digester.addSetNext("Server/Service/Listener",
                            "addLifecycleListener",
                            "org.apache.catalina.LifecycleListener");

        //Executor
        digester.addObjectCreate("Server/Service/Executor",
                         "org.apache.catalina.core.StandardThreadExecutor",
                         "className");
        digester.addSetProperties("Server/Service/Executor");

        digester.addSetNext("Server/Service/Executor",
                            "addExecutor",
                            "org.apache.catalina.Executor");

        /**
	        RulesBase.cache["Server/Service/Connector"]=>array(
		         new ConnectorCreateRule(),
		         new SetAllPropertiesRule(new String[]{"executor"})
	        );
	
	    */
        digester.addRule("Server/Service/Connector",
                         new ConnectorCreateRule());
        digester.addRule("Server/Service/Connector", 
                         new SetAllPropertiesRule(new String[]{"executor"}));
        digester.addSetNext("Server/Service/Connector",
                            "addConnector",
                            "org.apache.catalina.connector.Connector");
        
        


        digester.addObjectCreate("Server/Service/Connector/Listener",
                                 null, // MUST be specified in the element
                                 "className");
        digester.addSetProperties("Server/Service/Connector/Listener");
        digester.addSetNext("Server/Service/Connector/Listener",
                            "addLifecycleListener",
                            "org.apache.catalina.LifecycleListener");

        // Add RuleSets for nested elements
        /**
	     	NamingRuleSet.addRuleInstances(this)
	     */
        digester.addRuleSet(new NamingRuleSet("Server/GlobalNamingResources/"));
        /**
	     	EngineRuleSet.addRuleInstances(this)
	     	{
	     		 RulesBase.cache["Server/Service/Engine"]=>array(
			        new ObjectCreateRule("org.apache.catalina.core.StandardEngine", "className"),
			        new SetPropertiesRule(),
			        new LifecycleListenerRule ("org.apache.catalina.startup.EngineConfig","engineConfigClass"),
			        new SetNextRule("setContainer", "org.apache.catalina.Container")//setContainer
		         );
		         RulesBase.cache["Server/Service/Engine/Cluster"]=>array(
			        new ObjectCreateRule(null, "className"),
			        new SetPropertiesRule(),
			        new SetNextRule("setCluster", "org.apache.catalina.Cluster")
		         );
		         RulesBase.cache["Server/Service/Engine/Listener"]=>array(
			        new ObjectCreateRule(null, "className"),
			        new SetPropertiesRule(),
			        new SetNextRule("addLifecycleListener", "org.apache.catalina.LifecycleListener")
		         );
		         
		         (new RealmRuleSet("Server/Service/Engine/")).addRuleInstances(this)
		         {	
		         	 //.....
			         RulesBase.cache["Server/Service/Engine/Realm"]=>array(
				        new ObjectCreateRule(null, "className"),
				        new SetPropertiesRule(),
				        new SetNextRule("setRealm", "org.apache.catalina.Realm")
			         );
			         RulesBase.cache["Server/Service/Engine/Realm/Realm"]=>array(
				        new ObjectCreateRule(null, "className"),
				        new SetPropertiesRule(),
				        new SetNextRule("addRealm", "org.apache.catalina.Realm")
			         );
			     }
			     
		         RulesBase.cache["Server/Service/Engine/Valve"]=>array(
			        new ObjectCreateRule(null, "className"),
			        new SetPropertiesRule(),
			        new SetNextRule("addValve", "org.apache.catalina.Valve")
		         );
		   }
	     */
        digester.addRuleSet(new EngineRuleSet("Server/Service/"));
        /**
	     	HostRuleSet.addRuleInstances(this)
	     	{	//....
	     		RulesBase.cache["Server/Service/Engine/Host"]=>array(
			        new ObjectCreateRule("org.apache.catalina.core.StandardHost", "className"),
			        new SetPropertiesRule(),
			       	new CopyParentClassLoaderRule(),
			       	new LifecycleListenerRule ("org.apache.catalina.startup.HostConfig","hostConfigClass"),//在调用start的时候，会读取配置文件
			        new SetNextRule("addChild", "org.apache.catalina.Container"),
			       
		         );
		         RulesBase.cache["Server/Service/Engine/Host/Alias"]=>array(
			        new CallMethodRule(addAlias, 0)
		         );
		         RulesBase.cache["Server/Service/Engine/Host/Cluster"]=>array(
			        new ObjectCreateRule(null, "className"),
			        new SetPropertiesRule(),
			        new SetNextRule("setCluster", "org.apache.catalina.Cluster")
		         );
		         RulesBase.cache["Server/Service/Engine/Host/Listener"]=>array(
			        new ObjectCreateRule(null, "className"),
			        new SetPropertiesRule(),
			        new SetNextRule("addLifecycleListener", "org.apache.catalina.LifecycleListener")
		         );
		         (new RealmRuleSet("Server/Service/Engine/Host/")).addRuleInstances(this)
		         {	//...
			         
			         RulesBase.cache["Server/Service/Engine/Host/Realm"]=>array(
				        new ObjectCreateRule(null, "className"),
				        new SetPropertiesRule(),
				        new SetNextRule("setRealm", "org.apache.catalina.Realm")
			         );
			         RulesBase.cache["Server/Service/Engine/Realm/Host/Realm"]=>array(
				        new ObjectCreateRule(null, "className"),
				        new SetPropertiesRule(),
				        new SetNextRule("addRealm", "org.apache.catalina.Realm")
			         );
	
		         }
		         RulesBase.cache["Server/Service/Engine/Host/Valve"]=>array(
			        new ObjectCreateRule(null, "className"),
			        new SetPropertiesRule(),
			        new SetNextRule("addValve", "org.apache.catalina.Valve")
		         );
		   }
	     */
	
        digester.addRuleSet(new HostRuleSet("Server/Service/Engine/"));
        /**
         	ContextRuleSet.addRuleInstances(this)
	     	{	//....
	     		RulesBase.cache["Server/Service/Engine/Host/Context"]=>array(
			        new ObjectCreateRule("org.apache.catalina.core.StandardContext", "className"),
			        new SetPropertiesRule(),
			        new LifecycleListenerRule ("org.apache.catalina.startup.ContextConfig","configClass")
			        new SetNextRule("addChild", "org.apache.catalina.Container"),
			       
		         );
		         RulesBase.cache["Server/Service/Engine/Host/Context/Listener"]=>array(
		         );
		         .......
         	}
         */
        digester.addRuleSet(new ContextRuleSet("Server/Service/Engine/Host/"));
        digester.addRuleSet(ClusterRuleSetFactory.getClusterRuleSet("Server/Service/Engine/Host/Cluster/"));
        digester.addRuleSet(new NamingRuleSet("Server/Service/Engine/Host/Context/"));

        // When the 'engine' is found, set the parentClassLoader.
        /**
	        RulesBase.cache["Server/Service/Engine"]=>array(
		        new SetParentClassLoaderRule(parentClassLoader)
	        );
	    */
        digester.addRule("Server/Service/Engine",
                         new SetParentClassLoaderRule(parentClassLoader));
        digester.addRuleSet(ClusterRuleSetFactory.getClusterRuleSet("Server/Service/Engine/Cluster/"));

        long t2=System.currentTimeMillis();
        if (log.isDebugEnabled())
            log.debug("Digester for server.xml created " + ( t2-t1 ));
        return (digester);

    }


    /**
     * Create and configure the Digester we will be using for shutdown.
     */
    protected Digester createStopDigester() {

        // Initialize the digester
        Digester digester = new Digester();

        // Configure the rules we need for shutting down
        digester.addObjectCreate("Server",
                                 "org.apache.catalina.core.StandardServer",
                                 "className");
        digester.addSetProperties("Server");
        digester.addSetNext("Server",
                            "setServer",
                            "org.apache.catalina.Server");

        return (digester);

    }


    public void stopServer() {
        stopServer(null);
    }

    public void stopServer(String[] arguments) {

        if (arguments != null) {
            arguments(arguments);
        }

        Server s = getServer();
        if( s == null ) {
            // Create and execute our Digester
            Digester digester = createStopDigester();
            digester.setClassLoader(Thread.currentThread().getContextClassLoader());
            File file = configFile();
            try {
                InputSource is =
                    new InputSource("file://" + file.getAbsolutePath());
                FileInputStream fis = new FileInputStream(file);
                is.setByteStream(fis);
                digester.push(this);
                digester.parse(is);
                fis.close();
            } catch (Exception e) {
                log.error("Catalina.stop: ", e);
                System.exit(1);
            }
        } else {
            // Server object already present. Must be running as a service
            if (s instanceof Lifecycle) {
                try {
                    ((Lifecycle) s).stop();
                } catch (LifecycleException e) {
                    log.error("Catalina.stop: ", e);
                }
                return;
            }
            // else fall down
        }

        // Stop the existing server
        s = getServer();
        try {
            if (s.getPort()>0) { 
                String hostAddress = InetAddress.getByName("localhost").getHostAddress();
                Socket socket = new Socket(hostAddress, getServer().getPort());
                OutputStream stream = socket.getOutputStream();
                String shutdown = s.getShutdown();
                for (int i = 0; i < shutdown.length(); i++)
                    stream.write(shutdown.charAt(i));
                stream.flush();
                stream.close();
                socket.close();
            } else {
                log.error(sm.getString("catalina.stopServer"));
                System.exit(1);
            }
        } catch (IOException e) {
            log.error("Catalina.stop: ", e);
            System.exit(1);
        }

    }


    /**
     * Set the <code>catalina.base</code> System property to the current
     * working directory if it has not been set.
     * @deprecated Use initDirs()
     */
    public void setCatalinaBase() {
        initDirs();
    }

    /**
     * Set the <code>catalina.home</code> System property to the current
     * working directory if it has not been set.
     * @deprecated Use initDirs()
     */
    public void setCatalinaHome() {
        initDirs();
    }

    /**
     * Start a new server instance.
     */
    public void load() {

        long t1 = System.nanoTime();

        initDirs();//动态设置系统环境变量：//catalina.base = catalina.home = catalinaHome

        // Before digester - it may be needed

        initNaming();//设置系统环境变量 java.naming.factory.url.pkgs = org.apache.naming...和java.naming.factory.initial = org.apache.naming.java.javaURLContextFactory

        // Create and execute our Digester
        /**
         * 创建Digester对象，并进行初始化
         * org.apache.catalina.core.StandardServer
         * 
         */
        Digester digester = createStartDigester();//
        
        /**
         * 定义配置文件流开始
         */
        InputSource inputSource = null;
        InputStream inputStream = null;
        File file = null;
        try {
            file = configFile();//当前目录下查conf/server.xml文件，如果 找不到，则查"catalina.base"/conf/server.xml
            inputStream = new FileInputStream(file);
            inputSource = new InputSource("file://" + file.getAbsolutePath());
        } catch (Exception e) {
            ;
        }
        if (inputStream == null) {
            try {
                inputStream = getClass().getClassLoader()
                    .getResourceAsStream(getConfigFile());//conf/server.xml
                inputSource = new InputSource
                    (getClass().getClassLoader()
                     .getResource(getConfigFile()).toString());
            } catch (Exception e) {
                ;
            }
        }

        // This should be included in catalina.jar
        // Alternative: don't bother with xml, just create it manually.
        if( inputStream==null ) {
            try {
                inputStream = getClass().getClassLoader()
                .getResourceAsStream("server-embed.xml");
                inputSource = new InputSource
                (getClass().getClassLoader()
                        .getResource("server-embed.xml").toString());
            } catch (Exception e) {
                ;
            }
        }
        

        if ((inputStream == null) && (file != null)) {
            log.warn("Can't load server.xml from " + file.getAbsolutePath());
            if (file.exists() && !file.canRead()) {
                log.warn("Permissions incorrect, read permission is not allowed on the file.");
            }
            return;
        }
        /**
         * 定义配置文件流结束
         */
        
        try {
            inputSource.setByteStream(inputStream);
            /**
             * 将初始对象压入digester的stack
             *  ArrayStack.push(this)
             *  this永远都没有弹出堆栈
             */
            digester.push(this); // ArrayStack.push(this)
           /**
            * 解析XML内容
            * getXMLReader().parse(input)
            * 生成解析后的配置文件server.xml的对象树
            * 并把跟标签Server对象，反射注入到 this.server
            */
            digester.parse(inputSource);//解析系统的配置文件，conf/server.xml
            inputStream.close();
        } catch (Exception e) {
            log.warn("Catalina.start using "
                               + getConfigFile() + ": " , e);
            return;
        }

        // Stream redirection
        initStreams();//重定向系统标准的输出流System.out

        // Start the new server 根据解析处理的对象树，层级进行调用initialize,进行初始化
        if (getServer() instanceof Lifecycle) {
            try {
            	/**
            	 * 初始化系统 (new org.apache.catalina.core.StandardServer()).initialize()
            	 * 往注册表Registry，注册oname=new ObjectName( "Catalina:type=Server")
            	 * 往注册表Registry，注册oname2 = new ObjectName(oname.getDomain() + ":type=StringCache");
            	 * 
            	 */
                getServer().initialize();//
            } catch (LifecycleException e) {
                if (Boolean.getBoolean("org.apache.catalina.startup.EXIT_ON_INIT_FAILURE"))
                    throw new java.lang.Error(e);
                else   
                    log.error("Catalina.start", e);
                
            }
        }

        long t2 = System.nanoTime();
        if(log.isInfoEnabled())
            log.info("Initialization processed in " + ((t2 - t1) / 1000000) + " ms");

    }


    /* 
     * Load using arguments
     */
    public void load(String args[]) {

        try {
            if (arguments(args))
                load();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    public void create() {

    }

    public void destroy() {

    }

    /**
     * Start a new server instance.
     */
    public void start() {

        if (getServer() == null) {
            load();//构造对象树，并调用initialize()进行初始化
        }

        if (getServer() == null) {
            log.fatal("Cannot start server. Server instance is not configured.");
            return;
        }

        long t1 = System.nanoTime();
        
        // Start the new server
        if (getServer() instanceof Lifecycle) {
            try {
            	//(new org.apache.catalina.core.StandardServer()).start()
                ((Lifecycle) getServer()).start();//层级调用DOM对象的各自的start方法
            } catch (LifecycleException e) {
                log.error("Catalina.start: ", e);
            }
        }

        long t2 = System.nanoTime();
        if(log.isInfoEnabled())
            log.info("Server startup in " + ((t2 - t1) / 1000000) + " ms");

        try {
            // Register shutdown hook
            if (useShutdownHook) {//使用关闭钩子
                if (shutdownHook == null) {
                    shutdownHook = new CatalinaShutdownHook();
                }
                Runtime.getRuntime().addShutdownHook(shutdownHook);//注入运行时关闭钩子CatalinaShutdownHook
                
                // If JULI is being used, disable JULI's shutdown hook since
                // shutdown hooks run in parallel and log messages may be lost
                // if JULI's hook completes before the CatalinaShutdownHook()
                LogManager logManager = LogManager.getLogManager();
                if (logManager instanceof ClassLoaderLogManager) {
                    ((ClassLoaderLogManager) logManager).setUseShutdownHook(
                            false);//启用运行时钩子日志
                }
            }
        } catch (Throwable t) {
            // This will fail on JDK 1.2. Ignoring, as Tomcat can run
            // fine without the shutdown hook.
        }

        if (await) {//true
        	/**
        	 * awaitSocket =  new ServerSocket(8005, 1,InetAddress.getByName("localhost"));
        	 */
            await();//getServer().await();服务监听
            stop();//((Lifecycle) getServer()).stop(); 服务停止
        }

    }


    /**
     * Stop an existing server instance.
     */
    public void stop() {

        try {
            // Remove the ShutdownHook first so that server.stop() 
            // doesn't get invoked twice
            if (useShutdownHook) {
                Runtime.getRuntime().removeShutdownHook(shutdownHook);

                // If JULI is being used, re-enable JULI's shutdown to ensure
                // log messages are not lost
                LogManager logManager = LogManager.getLogManager();
                if (logManager instanceof ClassLoaderLogManager) {
                    ((ClassLoaderLogManager) logManager).setUseShutdownHook(
                            true);
                }
            }
        } catch (Throwable t) {
            // This will fail on JDK 1.2. Ignoring, as Tomcat can run
            // fine without the shutdown hook.
        }

        // Shut down the server
        if (getServer() instanceof Lifecycle) {
            try {
                ((Lifecycle) getServer()).stop();
            } catch (LifecycleException e) {
                log.error("Catalina.stop", e);
            }
        }

    }


    /**
     * Await and shutdown.
     */
    public void await() {

        getServer().await();

    }


    /**
     * Print usage information for this application.
     */
    protected void usage() {

        System.out.println
            ("usage: java org.apache.catalina.startup.Catalina"
             + " [ -config {pathname} ]"
             + " [ -nonaming ] "
             + " { -help | start | stop }");

    }


    // --------------------------------------- CatalinaShutdownHook Inner Class

    // XXX Should be moved to embedded !
    /**
     * Shutdown hook which will perform a clean shutdown of Catalina if needed.
     */
    protected class CatalinaShutdownHook extends Thread {

        public void run() {
            try {
                if (getServer() != null) {
                    Catalina.this.stop();
                }
            } catch (Throwable ex) {
                log.error(sm.getString("catalina.shutdownHookFail"), ex);
            } finally {
                // If JULI is used, shut JULI down *after* the server shuts down
                // so log messages aren't lost
                LogManager logManager = LogManager.getLogManager();
                if (logManager instanceof ClassLoaderLogManager) {
                    ((ClassLoaderLogManager) logManager).shutdown();
                }
            }

        }

    }
    
    
    private static org.apache.juli.logging.Log log=
        org.apache.juli.logging.LogFactory.getLog( Catalina.class );

}


// ------------------------------------------------------------ Private Classes


/**
 * Rule that sets the parent class loader for the top object on the stack,
 * which must be a <code>Container</code>.
 */

final class SetParentClassLoaderRule extends Rule {

    public SetParentClassLoaderRule(ClassLoader parentClassLoader) {

        this.parentClassLoader = parentClassLoader;

    }

    ClassLoader parentClassLoader = null;

    public void begin(String namespace, String name, Attributes attributes)
        throws Exception {

        if (digester.getLogger().isDebugEnabled())
            digester.getLogger().debug("Setting parent class loader");

        Container top = (Container) digester.peek();
        top.setParentClassLoader(parentClassLoader);

    }


}
