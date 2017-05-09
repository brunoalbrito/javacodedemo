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
package org.apache.catalina.connector;

import java.util.Iterator;
import java.util.Set;

import javax.management.MBeanServer;
import javax.management.MBeanServerNotification;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.ObjectInstance;
import javax.management.ObjectName;

import org.apache.catalina.ContainerEvent;
import org.apache.catalina.ContainerListener;
import org.apache.catalina.Host;
import org.apache.catalina.core.StandardContext;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;


import org.apache.tomcat.util.buf.MessageBytes;
import org.apache.tomcat.util.http.mapper.Mapper;
import org.apache.tomcat.util.http.mapper.MappingData;
import org.apache.tomcat.util.modeler.Registry;

import org.apache.tomcat.util.res.StringManager;


/**
 * Mapper listener.
 *
 * @author Remy Maucherat
 * @author Costin Manolache
 */
public class MapperListener
    implements NotificationListener, ContainerListener
 {


    private static Log log = LogFactory.getLog(MapperListener.class);


    // ----------------------------------------------------- Instance Variables
    /**
     * Associated mapper.
     */
    protected Mapper mapper = null;
    
    /**
     * Associated connector.
     */
    protected Connector connector = null;

    /**
     * MBean server.
     */
    protected MBeanServer mBeanServer = null;


    /**
     * The string manager for this package.
     */
    private StringManager sm =
        StringManager.getManager(Constants.Package);

    // It should be null - and fail if not set
    private String domain="*";// Catalina
    private String engine="*";

    // ----------------------------------------------------------- Constructors


    /**
     * Create mapper listener.
     */
    public MapperListener(Mapper mapper, Connector connector) {
        this.mapper = mapper;
        this.connector = connector;
    }


    // --------------------------------------------------------- Public Methods

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;//Catalina
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    /**
     * Initialize associated mapper.
     * 映射：
     * 	默认主机
     * 	虚拟主机映射
     *  虚拟主机上下文映射
     *  
     */
    public void init() {

        try {

            mBeanServer = Registry.getRegistry(null, null).getMBeanServer();

            /**
             * 这边很重要....................................
             * mapper.setDefaultHostName(defaultHost);//localhost
             */
            registerEngine();

            // Query hosts  查询hosts对象列表  ..........................................................................................
            String onStr = domain + ":type=Host,*";//Catalina:type=Host,*
            ObjectName objectName = new ObjectName(onStr);
            Set set = mBeanServer.queryMBeans(objectName, null);//取得虚拟主机列表
            Iterator iterator = set.iterator();
            while (iterator.hasNext()) {//迭代对虚拟主机设置别名
                ObjectInstance oi = (ObjectInstance) iterator.next();
                /**
                 *  mapper.addHost(name, aliases, objectName);
                 */
                registerHost(oi.getObjectName());//注册虚拟主机映射................
            }


            // Query contexts  查询上下文件对象列表 ..........................................................................................
            onStr = "*:j2eeType=WebModule,*";//  *:j2eeType=WebModule,*  这个JMX对象是在什么时候注册的
            objectName = new ObjectName(onStr);
            set = mBeanServer.queryMBeans(objectName, null);
            iterator = set.iterator();
            while (iterator.hasNext()) {//迭代对上下文件进行配置
                ObjectInstance oi = (ObjectInstance) iterator.next();//
                /**
                 	
                 	
                 	因为调用：
                 	org.apache.catalina.core.StandardEngine.start(){   super.start(); }逐层调用
	                 	org.apache.catalina.core.StandardContext.start();
	                 		org.apache.catalina.core.StandardContext.preRegisterJMX();
                 				org.apache.catalina.core.StandardContext.createObjectName();//onameStr="j2eeType=WebModule,name=" + name + suffix;
                	所以有了如下的JMX对象：
				     		 objectName === Catalina:j2eeType=WebModule,name=//localhost/,J2EEApplication=none,J2EEServer=none
							 objectName === Catalina:j2eeType=WebModule,name=//localhost/docs,J2EEApplication=none,J2EEServer=none
							 objectName === Catalina:j2eeType=WebModule,name=//localhost/examples,J2EEApplication=none,J2EEServer=none
							 objectName === Catalina:j2eeType=WebModule,name=//localhost/host-manager,J2EEApplication=none,J2EEServer=none
							 objectName === Catalina:j2eeType=WebModule,name=//localhost/manager,J2EEApplication=none,J2EEServer=none
     				
     					相依的对象是：org.apache.catalina.core.StandardContext



     				mapper.addContext(hostName, contextName, context, welcomeFiles, resources);
                 */
                registerContext(oi.getObjectName());//注册上下文..................
            }

            // Query wrappers
            onStr = "*:j2eeType=Servlet,*";   //这个JMX对象是什么时候注册的？？？
            objectName = new ObjectName(onStr);
            set = mBeanServer.queryMBeans(objectName, null);//查询出所有  
            iterator = set.iterator();
            while (iterator.hasNext()) {//迭代对Servlet进行配置
                ObjectInstance oi = (ObjectInstance) iterator.next();
                /**
                 	org.apache.catalina.core.StandardWrapper 就是  <servlet>标签解析处理的对象
                 	
                 	
                 	因为调用：
		                 org.apache.catalina.core.StandardContext.start(){   super.start(); } 逐层调用
		                 	org.apache.catalina.core.StandardWrapper.start() 
		                  		org.apache.catalina.core.StandardWrapper.registerJMX(StandardContext ctx)中注册的JMX对象
	                                    所以有了如下的JMX对象：  		
				      Catalina:j2eeType=Servlet,name=default,WebModule=//localhost/,J2EEApplication=none,J2EEServer=none
				      Catalina:j2eeType=Servlet,name=default,WebModule=//localhost/docs,J2EEApplication=none,J2EEServer=none
				      ....
				      Catalina:j2eeType=Servlet,name=jsp,WebModule=//localhost/,J2EEApplication=none,J2EEServer=none
				      Catalina:j2eeType=Servlet,name=jsp,WebModule=//localhost/docs,J2EEApplication=none,J2EEServer=none
				      ....
				   
				   mapper.addWrapper(hostName, contextName, mappings[i], wrapper,jspWildCard);
				   
				   注册<servlet-mapping>标签和<servlet>标签的关系
                 */
                registerWrapper(oi.getObjectName());//注册包装器
            }

            onStr = "JMImplementation:type=MBeanServerDelegate";
            objectName = new ObjectName(onStr);
            mBeanServer.addNotificationListener(objectName, this, null, null);

        } catch (Exception e) {
            log.warn("Error registering contexts",e);
        }

    }

    /**
     * unregister this from JMImplementation:type=MBeanServerDelegate
     */
    public void destroy() {
        if (mBeanServer == null) {
            return;
        }
        try {
            ObjectName objectName = new ObjectName(
                    "JMImplementation:type=MBeanServerDelegate");
            mBeanServer.removeNotificationListener(objectName, this);
        } catch (Exception e) {
            log.warn("Error unregistering MBeanServerDelegate", e);
        }
    }

    // ------------------------------------------- NotificationListener Methods


    public void handleNotification(Notification notification,
                                   java.lang.Object handback) {

        if (notification instanceof MBeanServerNotification) {
            ObjectName objectName = 
                ((MBeanServerNotification) notification).getMBeanName();
            String j2eeType = objectName.getKeyProperty("j2eeType");
            String engineName = null;
            if (j2eeType != null) {
                if ((j2eeType.equals("WebModule")) || 
                    (j2eeType.equals("Servlet"))) {
                    if (mBeanServer.isRegistered(objectName)) {
                        try {
                            engineName = (String)
                                mBeanServer.getAttribute(objectName, "engineName");
                        } catch (Exception e) {
                            // Ignore  
                        }
                    }
                }
            }

            // At deployment time, engineName is always = null.
            if ( (!"*".equals(domain)) &&
                 ( !domain.equals(objectName.getDomain()) ) &&
                 ( (!domain.equals(engineName) ) &&
                   (engineName != null) ) )  {
                return;
            }
            if(log.isDebugEnabled())
                log.debug( "Handle " + objectName  + " type : " + notification.getType());    
            if (notification.getType().equals
                (MBeanServerNotification.REGISTRATION_NOTIFICATION)) {
                String type=objectName.getKeyProperty("type");
                if( "Host".equals( type ) && domain.equals(objectName.getDomain())) {
                    try {
                        registerHost(objectName);
                    } catch (Exception e) {
                        log.warn("Error registering Host " + objectName, e);  
                    }
                }
    
                if (j2eeType != null) {
                    if (j2eeType.equals("WebModule")) {
                        try {
                            registerContext(objectName);
                        } catch (Throwable t) {
                            log.warn("Error registering Context " + objectName,t);
                        }
                    } else if (j2eeType.equals("Servlet")) {
                        try {
                            registerWrapper(objectName);
                        } catch (Throwable t) {
                            log.warn("Error registering Wrapper " + objectName,t);
                        }
                    }
                }
            } else if (notification.getType().equals
                       (MBeanServerNotification.UNREGISTRATION_NOTIFICATION)) {
                String type=objectName.getKeyProperty("type");
                if( "Host".equals( type )&& domain.equals(objectName.getDomain())) {
                    try {
                        unregisterHost(objectName);
                    } catch (Exception e) {
                        log.warn("Error unregistering Host " + objectName,e);  
                    }
                }
 
                if (j2eeType != null) {
                    if (j2eeType.equals("WebModule")) {
                        try {
                            unregisterContext(objectName);
                        } catch (Throwable t) {
                            log.warn("Error unregistering webapp " + objectName,t);
                        }
                    }
                }
            }
        }

    }


    // --------------------------------------------- Container Listener methods

    public void containerEvent(ContainerEvent event) {

        if (event.getType() == Host.ADD_ALIAS_EVENT) {
            mapper.addHostAlias(((Host) event.getSource()).getName(),
                    event.getData().toString());
        } else if (event.getType() == Host.REMOVE_ALIAS_EVENT) {
            mapper.removeHostAlias(event.getData().toString());
        }
    }

    
    // ------------------------------------------------------ Protected Methods

    private void registerEngine()
        throws Exception
    {
        ObjectName engineName = new ObjectName
            (domain + ":type=Engine");//Catalina:type=Engine
        if ( ! mBeanServer.isRegistered(engineName)) return;
        
        String defaultHost = 
            (String) mBeanServer.getAttribute(engineName, "defaultHost");//默认主机   ，取得Engine标签中的defaultHost值
        
        ObjectName hostName = new ObjectName
            (domain + ":type=Host," + "host=" + defaultHost);//主机名  Catalina:type=Host,host=localhost
        
        if (!mBeanServer.isRegistered(hostName)) {//如果没有找到，在全局中查找

            // Get the hosts' list
            String onStr = domain + ":type=Host,*";//泛匹配所有的虚拟主机
            ObjectName objectName = new ObjectName(onStr);
            Set set = mBeanServer.queryMBeans(objectName, null);//查找所有MBean
            Iterator iterator = set.iterator();
            String[] aliases;
            boolean isRegisteredWithAlias = false;
            
            while (iterator.hasNext()) {//迭代虚拟主机列表

                if (isRegisteredWithAlias) break;
            
                ObjectInstance oi = (ObjectInstance) iterator.next();//host标签对象
                hostName = oi.getObjectName();
                aliases = (String[])
                    mBeanServer.invoke(hostName, "findAliases", null, null);

                for (int i=0; i < aliases.length; i++){//虚拟主机是否有配置别名
                    if (aliases[i].equalsIgnoreCase(defaultHost)){//如虚拟主机不在，那么在Host标签查找虚拟主机别名是defaultHost
                        isRegisteredWithAlias = true;
                        break;
                    }
                }
            }
            
            if (!isRegisteredWithAlias && log.isWarnEnabled())
                log.warn(sm.getString("mapperListener.unknownDefaultHost", defaultHost));
        }
        // This should probably be called later 
        if( defaultHost != null ) {
        	/**
        	 * 重要步骤
        	 */
            mapper.setDefaultHostName(defaultHost);//localhost
        }
    }

    /**
     * Register host.
     * 	注册虚拟主机
     */
    private void registerHost(ObjectName objectName)
        throws Exception {
        String name=objectName.getKeyProperty("host");//
        if( name != null ) {        

            Host host =
                (Host) connector.getService().getContainer().findChild(name);//取得host标签对象

            String[] aliases = host.findAliases();//取得别名标签
            mapper.addHost(name, aliases, objectName);//添加新的虚拟主机
            host.addContainerListener(this);//添加上下文监听器
            if(log.isDebugEnabled())
                log.debug(sm.getString
                     ("mapperListener.registerHost", name, domain));
        }
    }


    /**
     * Unregister host.
     */
    private void unregisterHost(ObjectName objectName)
        throws Exception {
        String name=objectName.getKeyProperty("host");
        if( name != null ) { 
            Host host =
                (Host) connector.getService().getContainer().findChild(name);
        
            mapper.removeHost(name);
            if (host != null) {
                host.removeContainerListener(this);
            }
            if(log.isDebugEnabled())
                log.debug(sm.getString
                        ("mapperListener.unregisterHost", name, domain));
        }
    }


    /**
     * Register context.
     * 注册上下文
     * objectName === Catalina:j2eeType=WebModule,name=//localhost/,J2EEApplication=none,J2EEServer=none
     * objectName === Catalina:j2eeType=WebModule,name=//localhost/docs,J2EEApplication=none,J2EEServer=none
     */
    private void registerContext(ObjectName objectName)
        throws Exception {

        String name = objectName.getKeyProperty("name");//取得对象属性 //localhost/和//localhost/docs
        
        // If the domain is the same with ours or the engine 
        // name attribute is the same... - then it's ours
        String targetDomain=objectName.getDomain();//Catalina
        if( ! domain.equals( targetDomain )) {
            try {
                targetDomain = (String) mBeanServer.getAttribute
                    (objectName, "engineName");  //
            } catch (Exception e) {
                // Ignore
            }
            if( ! domain.equals( targetDomain )) {
                // not ours
                return;
            }
        }

        String hostName = null;
        String contextName = null;
        if (name.startsWith("//")) {
            name = name.substring(2);
        }
        //name = "localhost"
        //name = "localhost/dirname"
        int slash = name.indexOf("/");
        if (slash != -1) {
            hostName = name.substring(0, slash);//  localhost  空
            contextName = name.substring(slash);//  /docs 		/
        } else {
            return;
        }
        // Special case for the root context
        if (contextName.equals("/")) {
            contextName = "";
        }

        if(log.isDebugEnabled())
             log.debug(sm.getString
                  ("mapperListener.registerContext", contextName));
        //查找映射对象 
        Object context = 
            mBeanServer.invoke(objectName, "findMappingObject", null, null);//取得jmx中的对象，并进行调用findMappingObject方法
            //mBeanServer.getAttribute(objectName, "mappingObject");
        //查找静态资源
        javax.naming.Context resources = (javax.naming.Context)
            mBeanServer.invoke(objectName, "findStaticResources", null, null);//取得jmx中的对象，并进行调用findStaticResources方法
            //mBeanServer.getAttribute(objectName, "staticResources");
        //查找欢迎文件 
        String[] welcomeFiles = (String[])
            mBeanServer.getAttribute(objectName, "welcomeFiles");//取得jmx中的对象，并进行调用welcomeFiles方法

        /**
         * 虚拟主机localhost，上下名称 /dirname，上下文件路径，欢迎文件，静态资源文件
         */
        mapper.addContext(hostName, contextName, context, 
                          welcomeFiles, resources);

    }


    /**
     * Unregister context.
     */
    private void unregisterContext(ObjectName objectName)
        throws Exception {

        String name = objectName.getKeyProperty("name");

        // If the domain is the same with ours or the engine 
        // name attribute is the same... - then it's ours
        String targetDomain=objectName.getDomain();
        if( ! domain.equals( targetDomain )) {
            try {
                targetDomain = (String) mBeanServer.getAttribute
                    (objectName, "engineName");
            } catch (Exception e) {
                // Ignore
            }
            if( ! domain.equals( targetDomain )) {
                // not ours
                return;
            }
        }

        String hostName = null;
        String contextName = null;
        if (name.startsWith("//")) {
            name = name.substring(2);
        }
        int slash = name.indexOf("/");
        if (slash != -1) {
            hostName = name.substring(0, slash);
            contextName = name.substring(slash);
        } else {
            return;
        }
        // Special case for the root context
        if (contextName.equals("/")) {
            contextName = "";
        }

        // Don't un-map a context that is paused
        MessageBytes hostMB = MessageBytes.newInstance();
        hostMB.setString(hostName);
        MessageBytes contextMB = MessageBytes.newInstance();
        contextMB.setString(contextName);
        MappingData mappingData = new MappingData();
        mapper.map(hostMB, contextMB, mappingData);
        if (mappingData.context instanceof StandardContext &&
                ((StandardContext)mappingData.context).getPaused()) {
            return;
        } 

        if(log.isDebugEnabled())
            log.debug(sm.getString
                  ("mapperListener.unregisterContext", contextName));

        mapper.removeContext(hostName, contextName);

    }


    /**
     * Register wrapper.
     * 
     * Catalina:j2eeType=Servlet,name=default,WebModule=//localhost/,J2EEApplication=none,J2EEServer=none
     * Catalina:j2eeType=Servlet,name=jsp,WebModule=//localhost/,J2EEApplication=none,J2EEServer=none
     * Catalina:j2eeType=Servlet,name=default,WebModule=//localhost/docs,J2EEApplication=none,J2EEServer=none
     */
    private void registerWrapper(ObjectName objectName)
        throws Exception {
    
        // If the domain is the same with ours or the engine 
        // name attribute is the same... - then it's ours
        String targetDomain=objectName.getDomain();
        if( ! domain.equals( targetDomain )) {
            try {
                targetDomain=(String) mBeanServer.getAttribute(objectName, "engineName");
            } catch (Exception e) {
                // Ignore
            }
            if( ! domain.equals( targetDomain )) {
                // not ours
                return;
            }
        }

        String wrapperName = objectName.getKeyProperty("name");//default 、 jsp
        String name = objectName.getKeyProperty("WebModule");//  //localhost/

        String hostName = null;
        String contextName = null;
        //name="//localhost/"
        //name="//localhost/dirname"
        //name="//localhost/"
        if (name.startsWith("//")) {
            name = name.substring(2);
        }
        int slash = name.indexOf("/");
        if (slash != -1) {
            hostName = name.substring(0, slash);// localhost 和  localhost 
            contextName = name.substring(slash);// / 和  docs
        } else {
            return;
        }
        // Special case for the root context
        if (contextName.equals("/")) {
            contextName = "";
        }
        if(log.isDebugEnabled())
            log.debug(sm.getString
                  ("mapperListener.registerWrapper", 
                   wrapperName, contextName));

        String[] mappings = (String[])
            mBeanServer.invoke(objectName, "findMappings", null, null);//查找映射项目 <servlet-mapping>
        Object wrapper = 
            mBeanServer.invoke(objectName, "findMappingObject", null, null);//查找处理对象（org.apache.catalina.core.StandardWrapper）

        for (int i = 0; i < mappings.length; i++) {//某个Servlet的<servlet-mapping>列表
            boolean jspWildCard = (wrapperName.equals("jsp")
                                   && mappings[i].endsWith("/*"));//如果是JSP处理模块
            //localhost,/,<servlet-mapping>的配置，org.apache.jasper.servlet.JspServlet，false
            mapper.addWrapper(hostName, contextName, mappings[i], wrapper,
                              jspWildCard);
        }

    }




}
