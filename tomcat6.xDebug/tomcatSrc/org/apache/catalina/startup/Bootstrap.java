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
import java.lang.management.ManagementFactory;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;

import org.apache.catalina.Lifecycle;
import org.apache.catalina.security.SecurityClassLoad;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;


/**
 * Boostrap loader for Catalina.  This application constructs a class loader
 * for use in loading the Catalina internal classes (by accumulating all of the
 * JAR files found in the "server" directory under "catalina.home"), and
 * starts the regular execution of the container.  The purpose of this
 * roundabout approach is to keep the Catalina internal classes (and any
 * other classes they depend on, such as an XML parser) out of the system
 * class path and therefore not visible to application level classes.
 *
 * @author Craig R. McClanahan
 * @author Remy Maucherat
 *
 */

public final class Bootstrap {

    private static Log log = LogFactory.getLog(Bootstrap.class);
    
    // -------------------------------------------------------------- Constants


    protected static final String CATALINA_HOME_TOKEN = "${catalina.home}";
    protected static final String CATALINA_BASE_TOKEN = "${catalina.base}";


    // ------------------------------------------------------- Static Variables


    /**
     * Daemon object used by main.
     */
    private static Bootstrap daemon = null;


    // -------------------------------------------------------------- Variables


    /**
     * Daemon reference.
     */
    private Object catalinaDaemon = null;


    protected ClassLoader commonLoader = null;
    protected ClassLoader catalinaLoader = null;
    protected ClassLoader sharedLoader = null;


    // -------------------------------------------------------- Private Methods


    private void initClassLoaders() {
        try {
            commonLoader = createClassLoader("common", null);
            if( commonLoader == null ) {
                // no config file, default to this loader - we might be in a 'single' env.
                commonLoader=this.getClass().getClassLoader();
            }
            catalinaLoader = createClassLoader("server", commonLoader);
            sharedLoader = createClassLoader("shared", commonLoader);
        } catch (Throwable t) {
            log.error("Class loader creation threw exception", t);
            System.exit(1);
        }
    }


    private ClassLoader createClassLoader(String name, ClassLoader parent)
        throws Exception {

        String value = CatalinaProperties.getProperty(name + ".loader");//common.loader
        if ((value == null) || (value.equals("")))
            return parent;

        ArrayList repositoryLocations = new ArrayList();
        ArrayList repositoryTypes = new ArrayList();
        int i;
 
        StringTokenizer tokenizer = new StringTokenizer(value, ",");
        while (tokenizer.hasMoreElements()) {
            String repository = tokenizer.nextToken();

            // Local repository
            boolean replace = false;
            String before = repository;
            while ((i=repository.indexOf(CATALINA_HOME_TOKEN))>=0) {//${catalina.home}
                replace=true;
                if (i>0) {
                repository = repository.substring(0,i) + getCatalinaHome() 
                    + repository.substring(i+CATALINA_HOME_TOKEN.length());
                } else {
                    repository = getCatalinaHome() 
                        + repository.substring(CATALINA_HOME_TOKEN.length());
                }
            }
            while ((i=repository.indexOf(CATALINA_BASE_TOKEN))>=0) {//${catalina.base}
                replace=true;
                if (i>0) {
                repository = repository.substring(0,i) + getCatalinaBase() 
                    + repository.substring(i+CATALINA_BASE_TOKEN.length());
                } else {
                    repository = getCatalinaBase() 
                        + repository.substring(CATALINA_BASE_TOKEN.length());
                }
            }
            if (replace && log.isDebugEnabled())
                log.debug("Expanded " + before + " to " + repository);

            // Check for a JAR URL repository
            try {
                URL url=new URL(repository);
                repositoryLocations.add(repository);
                repositoryTypes.add(ClassLoaderFactory.IS_URL);
                continue;
            } catch (MalformedURLException e) {
                // Ignore
            }

            if (repository.endsWith("*.jar")) {
                repository = repository.substring
                    (0, repository.length() - "*.jar".length());
                repositoryLocations.add(repository);
                repositoryTypes.add(ClassLoaderFactory.IS_GLOB);
            } else if (repository.endsWith(".jar")) {
                repositoryLocations.add(repository);
                repositoryTypes.add(ClassLoaderFactory.IS_JAR);
            } else {
                repositoryLocations.add(repository);
                repositoryTypes.add(ClassLoaderFactory.IS_DIR);
            }
        }

        String[] locations = (String[]) repositoryLocations.toArray(new String[0]);
        Integer[] types = (Integer[]) repositoryTypes.toArray(new Integer[0]);
 
        //创建类加载器，URL类加载器，实现远程类加载
        ClassLoader classLoader = ClassLoaderFactory.createClassLoader
            (locations, types, parent);

        // Retrieving MBean server
        MBeanServer mBeanServer = null;
        if (MBeanServerFactory.findMBeanServer(null).size() > 0) {
            mBeanServer =
                (MBeanServer) MBeanServerFactory.findMBeanServer(null).get(0);
        } else {
            mBeanServer = ManagementFactory.getPlatformMBeanServer();
        }

        //把类加载器注册到bean管理工厂
        // Register the server classloader
        ObjectName objectName =
            new ObjectName("Catalina:type=ServerClassLoader,name=" + name);
        mBeanServer.registerMBean(classLoader, objectName);

        return classLoader;

    }


    /**
     * Initialize daemon.
     */
    public void init()
        throws Exception
    {

        // Set Catalina path
        setCatalinaHome();//设置环境变量 //catalina.home = System.getProperty("user.dir")
        setCatalinaBase();//设置环境变量 //catalina.base = catalina.home

        initClassLoaders();//初始化类加载器 catalinaLoader，sharedLoader
        
        //修改jvm的类加载器，使用tomcat的类加载器
        Thread.currentThread().setContextClassLoader(catalinaLoader);

        SecurityClassLoad.securityClassLoad(catalinaLoader);

        // Load our startup class and call its process() method
        if (log.isDebugEnabled())
            log.debug("Loading startup class");
        //创建Catalina实例对象
        Class startupClass =
            catalinaLoader.loadClass
            ("org.apache.catalina.startup.Catalina");
        Object startupInstance = startupClass.newInstance();//创建Catalina对象，实例化SecurityConfig，注册realm事件
       
        // Set the shared extensions class loader
        if (log.isDebugEnabled())
            log.debug("Setting startup class properties");
        String methodName = "setParentClassLoader";
        Class paramTypes[] = new Class[1];
        paramTypes[0] = Class.forName("java.lang.ClassLoader");
        Object paramValues[] = new Object[1];
        paramValues[0] = sharedLoader;
        Method method =
            startupInstance.getClass().getMethod(methodName, paramTypes);//反射匹配方法
        method.invoke(startupInstance, paramValues);//调用匹配的方法方法，注入类加载器

        catalinaDaemon = startupInstance;

    }


    /**
     * Load daemon.
     */
    private void load(String[] arguments)
        throws Exception {

        // Call the load() method
        String methodName = "load";
        Object param[];
        Class paramTypes[];
        if (arguments==null || arguments.length==0) {
            paramTypes = null;
            param = null;
        } else {
            paramTypes = new Class[1];
            paramTypes[0] = arguments.getClass();
            param = new Object[1];
            param[0] = arguments;
        }
        Method method = 
            catalinaDaemon.getClass().getMethod(methodName, paramTypes);
        if (log.isDebugEnabled())
            log.debug("Calling startup class " + method);
        method.invoke(catalinaDaemon, param);

    }


    // ----------------------------------------------------------- Main Program


    /**
     * Load the Catalina daemon.
     */
    public void init(String[] arguments)
        throws Exception {

        init();
        load(arguments);

    }


    /**
     * Start the Catalina daemon.
     */
    public void start()
        throws Exception {
        if( catalinaDaemon==null ) init();

        Method method = catalinaDaemon.getClass().getMethod("start", (Class [] )null);
        method.invoke(catalinaDaemon, (Object [])null);

    }


    /**
     * Stop the Catalina Daemon.
     */
    public void stop()
        throws Exception {

        Method method = catalinaDaemon.getClass().getMethod("stop", (Class [] ) null);
        method.invoke(catalinaDaemon, (Object [] ) null);

    }


    /**
     * Stop the standlone server.
     */
    public void stopServer()
        throws Exception {

        Method method = 
            catalinaDaemon.getClass().getMethod("stopServer", (Class []) null);
        method.invoke(catalinaDaemon, (Object []) null);

    }


   /**
     * Stop the standlone server.
     */
    public void stopServer(String[] arguments)
        throws Exception {

        Object param[];
        Class paramTypes[];
        if (arguments==null || arguments.length==0) {
            paramTypes = null;
            param = null;
        } else {
            paramTypes = new Class[1];
            paramTypes[0] = arguments.getClass();
            param = new Object[1];
            param[0] = arguments;
        }
        Method method = 
            catalinaDaemon.getClass().getMethod("stopServer", paramTypes);
        method.invoke(catalinaDaemon, param);

    }


    /**
     * Set flag.
     */
    public void setAwait(boolean await)
        throws Exception {
    	
        Class paramTypes[] = new Class[1];
        paramTypes[0] = Boolean.TYPE;
        Object paramValues[] = new Object[1];
        paramValues[0] = new Boolean(await);
        Method method = 
            catalinaDaemon.getClass().getMethod("setAwait", paramTypes);
        method.invoke(catalinaDaemon, paramValues);

    }

    public boolean getAwait()
        throws Exception
    {
        Class paramTypes[] = new Class[0];
        Object paramValues[] = new Object[0];
        Method method =
            catalinaDaemon.getClass().getMethod("getAwait", paramTypes);
        Boolean b=(Boolean)method.invoke(catalinaDaemon, paramValues);
        return b.booleanValue();
    }


    /**
     * Destroy the Catalina Daemon.
     */
    public void destroy() {

        // FIXME

    }


    /**
     * Main method, used for testing only.
     *
     * @param args Command line arguments to be processed
     */
    public static void main(String args[]) {

        if (daemon == null) {
            daemon = new Bootstrap();
            try {
            	/**
            	 * 设置环境变量 //catalina.base = catalina.home = catalinaHome
            	 * 初始化类加载器 catalinaLoader，sharedLoader
            	 * 创建Catalina对象，实例化SecurityConfig，注册realm事件
            	 * (new org.apache.catalina.startup.Catalina).setParentClassLoader(sharedLoader);
            	 */
                daemon.init();
            } catch (Throwable t) {
                t.printStackTrace();
                return;
            }
        }

        try {
            String command = "start";
            if (args.length > 0) {
                command = args[args.length - 1];
            }

            if (command.equals("startd")) {
                args[args.length - 1] = "start";
                daemon.load(args);
                daemon.start();
            } else if (command.equals("stopd")) {
                args[args.length - 1] = "stop";
                daemon.stop();
            } else if (command.equals("start")) {//启动
                daemon.setAwait(true);//(new org.apache.catalina.startup.Catalina()).setAwait(true),设置对象字段
                
                /**
					解析Dom树
 					取得DOM树的根元素Server对象，进行初始化。getServer().initialize();
 							===org.apache.catalina.core.StandardServer().initialize();
                 */
               /**
                * **************(new org.apache.catalina.startup.Catalina()).load()*********************
                * 动态设置系统环境变量：//catalina.base = catalina.home = catalinaHome
                * 设置系统环境变量 java.naming.factory.url.pkgs = org.apache.naming...和java.naming.factory.initial = org.apache.naming.java.javaURLContextFactory
                * 创建Digester对象，并进行实例化如下对象
                * 	Server、Server/GlobalNamingResources、
                * 	Server/Listener、
                * 	Server/Service、
                * 	Server/Service/Listener、
                *	Server/Service/Executor
                *
                *	Server/Service/Connector/Listener
                *
                * 查"catalina.base"/conf/server.xml
                * 使用Digester解析系统的配置文件，conf/server.xml
                * 重新定义系统输出流System.out
                * getServer().initialize();//初始化系统--因为没有实力化，所有没有执行成功 new Server().initialize(),
                * 在哪里创建了Server，没有跟踪到！！ 肯定是在这里面创建的
                */
                daemon.load(args);//根据server.xml构造对象树，并进行初始化调用initialize，其中有new socket
                /**
                 * **************(new org.apache.catalina.startup.Catalina()).start()*********************
                 * 注入运行时关闭钩子CatalinaShutdownHook。（如窗口突然关闭），触发关闭((Lifecycle) getServer()).stop();
                 * 启用运行时钩子日志
                 * getServer().await();服务监听  awaitSocket =  new ServerSocket(8005, 1,InetAddress.getByName("localhost"));
                 * ((Lifecycle) getServer()).stop(); 服务停止
                 */
                daemon.start();//启动所有服务
            } else if (command.equals("stop")) {
                daemon.stopServer(args);
            } else {
                log.warn("Bootstrap: command \"" + command + "\" does not exist.");
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }

    }

    public void setCatalinaHome(String s) {
        System.setProperty( "catalina.home", s );
    }

    public void setCatalinaBase(String s) {
        System.setProperty( "catalina.base", s );
    }


    /**
     * Set the <code>catalina.base</code> System property to the current
     * working directory if it has not been set.
     */
    private void setCatalinaBase() {

        if (System.getProperty("catalina.base") != null)
            return;
        if (System.getProperty("catalina.home") != null)
            System.setProperty("catalina.base",
                               System.getProperty("catalina.home"));
        else
            System.setProperty("catalina.base",
                               System.getProperty("user.dir"));

    }


    /**
     * Set the <code>catalina.home</code> System property to the current
     * working directory if it has not been set.
     */
    private void setCatalinaHome() {

        if (System.getProperty("catalina.home") != null)
            return;
        File bootstrapJar = 
            new File(System.getProperty("user.dir"), "bootstrap.jar");
        if (bootstrapJar.exists()) {
            try {
                System.setProperty
                    ("catalina.home", 
                     (new File(System.getProperty("user.dir"), ".."))
                     .getCanonicalPath());
            } catch (Exception e) {
                // Ignore
                System.setProperty("catalina.home",
                                   System.getProperty("user.dir"));
            }
        } else {
            System.setProperty("catalina.home",
                               System.getProperty("user.dir"));
        }

    }


    /**
     * Get the value of the catalina.home environment variable.
     */
    public static String getCatalinaHome() {
        return System.getProperty("catalina.home",
                                  System.getProperty("user.dir"));
    }


    /**
     * Get the value of the catalina.base environment variable.
     */
    public static String getCatalinaBase() {
        return System.getProperty("catalina.base", getCatalinaHome());
    }


}
