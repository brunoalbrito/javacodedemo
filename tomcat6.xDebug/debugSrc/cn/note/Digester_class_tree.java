package cn.note;

import org.apache.catalina.startup.ConnectorCreateRule;
import org.apache.catalina.startup.CopyParentClassLoaderRule;
import org.apache.catalina.startup.LifecycleListenerRule;
import org.apache.catalina.startup.SetAllPropertiesRule;
import org.apache.catalina.startup.SetNextNamingRule;
import org.apache.catalina.startup.SetParentClassLoaderRule;
import org.apache.tomcat.util.digester.CallMethodRule;
/*
Service.getContainer() == org.apache.catalina.core.StandardEngine
// Set JSP factory
Class.forName("org.apache.jasper.compiler.JspRuntimeContext",true, this.getClass().getClassLoader());

public class Digester_class_tree {
		RulesBase.cache["Server"]=>array(
    		new ObjectCreateRule("org.apache.catalina.core.StandardServer", "className"),//创建本对象
    		new SetPropertiesRule(),//把XML标签的属性，和本对象进行一一管理映射反射注入
    		new SetNextRule("setServer", "org.apache.catalina.Server")//调用父节点的setServer方法，把本对象注入进去
    	); 
		
		/**
		 * Server/GlobalNamingResources
		 */
		RulesBase.cache["Server/GlobalNamingResources"]=>array(
	    		new ObjectCreateRule("org.apache.catalina.deploy.NamingResources", null),
	    		new SetPropertiesRule(),
	    		new SetNextRule("setGlobalNamingResources", "org.apache.catalina.deploy.NamingResources")
	    ); 
		
		/**
		 * Server/Listener
		 * 监听Server
		 * 
		 */
		RulesBase.cache["Server/Listener"]=>array(
	    		new ObjectCreateRule(null, "className"),
	    		new SetPropertiesRule(),
	    		new SetNextRule("addLifecycleListener", "org.apache.catalina.LifecycleListener")
	    ); 
		
		/**
		 * Server/Service
		 */
		RulesBase.cache["Server/Service"]=>array(
	    		new ObjectCreateRule("org.apache.catalina.core.StandardService", "className"),
	    		new SetPropertiesRule(),
	    		new SetNextRule("addService", "org.apache.catalina.Service")
	    ); 
		
		/**
		 * Server/Service/Listener
		 * 
		 * 监听Service
		 */
		RulesBase.cache["Server/Service/Listener"]=>array(
	    		new ObjectCreateRule(null, "className"),
	    		new SetPropertiesRule(),
	    		new SetNextRule("addLifecycleListener", "org.apache.catalina.LifecycleListener")
	    ); 
		
		/**
		 * Server/Service/Executor标签
		 */
		RulesBase.cache["Server/Service/Executor"]=>array(
	    		new ObjectCreateRule("org.apache.catalina.core.StandardThreadExecutor", "className"),
	    		new SetPropertiesRule(),
	    		new SetNextRule("addExecutor", "org.apache.catalina.Executor")
	    ); 
		
		/**
		 * Server/Service/Connector标签
		 */
		RulesBase.cache["Server/Service/Connector"]=>array(
				new ConnectorCreateRule(),
				new SetAllPropertiesRule(new String[]{"executor"}),
	    		new SetNextRule("addConnector", "org.apache.catalina.connector.Connector")
	    ); 

		/**
		 * Server/Service/Connector/Listener
		 * 监听Connector
		 * 
		 */
		RulesBase.cache["Server/Service/Connector/Listener"]=>array(
	    		new ObjectCreateRule(null, "className"),
	    		new SetPropertiesRule(),
	    		new SetNextRule("addLifecycleListener", "org.apache.catalina.LifecycleListener")
	    ); 
		
		/**
		 * Server/GlobalNamingResources/标签下的子标签
		 */
		RulesBase.cache["Server/GlobalNamingResources/Ejb"]=>array(
	    		new ObjectCreateRule("org.apache.catalina.deploy.ContextEjb",null),
	    		new SetAllPropertiesRule(),
	    		new SetNextNamingRule("addEjb","org.apache.catalina.deploy.ContextEjb")
	    ); 
		
		RulesBase.cache["Server/GlobalNamingResources/Environment"]=>array(
	    		new ObjectCreateRule("org.apache.catalina.deploy.ContextEnvironment",null),
	    		new SetAllPropertiesRule(),
	    		new SetNextNamingRule("addEnvironment","org.apache.catalina.deploy.ContextEnvironment")
	    ); 
		RulesBase.cache["Server/GlobalNamingResources/LocalEjb"]=>array(
	    		new ObjectCreateRule("org.apache.catalina.deploy.ContextLocalEjb",null),
	    		new SetAllPropertiesRule(),
	    		new SetNextNamingRule("addLocalEjb","org.apache.catalina.deploy.ContextLocalEjb")
	    ); 
		RulesBase.cache["Server/GlobalNamingResources/Resource"]=>array(
	    		new ObjectCreateRule("org.apache.catalina.deploy.ContextResource",null),
	    		new SetAllPropertiesRule(),
	    		new SetNextNamingRule("addResource","org.apache.catalina.deploy.ContextResource")
	    ); 
		RulesBase.cache["Server/GlobalNamingResources/ResourceEnvRef"]=>array(
	    		new ObjectCreateRule("org.apache.catalina.deploy.ContextResourceEnvRef",null),
	    		new SetAllPropertiesRule(),
	    		new SetNextNamingRule("addResourceEnvRef","org.apache.catalina.deploy.ContextResourceEnvRef")
	    ); 
		RulesBase.cache["Server/GlobalNamingResources/ServiceRef"]=>array(
	    		new ObjectCreateRule("org.apache.catalina.deploy.ContextService",null),
	    		new SetAllPropertiesRule(),
	    		new SetNextNamingRule("addService","org.apache.catalina.deploy.ContextService")
	    ); 
		RulesBase.cache["Server/GlobalNamingResources/Transaction"]=>array(
	    		new ObjectCreateRule("org.apache.catalina.deploy.ContextTransaction",null),
	    		new SetAllPropertiesRule(),
	    		new SetNextNamingRule("setTransaction","org.apache.catalina.deploy.ContextTransaction")
	    ); 
		
		/**
		 * Server/Service/Engine
		 * 
		 */
		RulesBase.cache["Server/Service/Engine"]=>array(
				new ObjectCreateRule("org.apache.catalina.core.StandardEngine", "className"),
	    		new SetAllPropertiesRule(),
	    		new LifecycleListenerRule("org.apache.catalina.startup.EngineConfig","engineConfigClass"),//监听器，引擎配置文件  ，容器配置文件
	    		new SetNextRule("setContainer", "org.apache.catalina.Container")
	    ); 
		
		/**
		 * Server/Service/Engine标签下的子标签
		 */
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
		RulesBase.cache["Server/Service/Engine/Valve"]=>array(
	    		new ObjectCreateRule(null, "className"),
	    		new SetPropertiesRule(),
	    		new SetNextRule("addValve", "org.apache.catalina.Valve")
	    );
		
		/**
		 * Server/Service/Engine/Host
		 */
		RulesBase.cache["Server/Service/Engine/Host"]=>array(
	    		new ObjectCreateRule("org.apache.catalina.core.StandardHost", "className"),//影响到虚拟主机配置
	    		new SetPropertiesRule(),
	    		new CopyParentClassLoaderRule(),
	    		new LifecycleListenerRule ("org.apache.catalina.startup.HostConfig","hostConfigClass"),//配置文件，主机配置文件
	    		new SetNextRule("addChild", "org.apache.catalina.Container")
	    ); 
		
		RulesBase.cache["Server/Service/Engine/Host/Alias"]=>array(
				new CallMethodRule("addAlias", 0)
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
		
		RulesBase.cache["Server/Service/Engine/Host/Realm"]=>array(
	    		new ObjectCreateRule(null, "className"),
	    		new SetPropertiesRule(),
	    		new SetNextRule("setRealm", "org.apache.catalina.Realm")
	    ); 
		RulesBase.cache["Server/Service/Engine/Host/Realm/Realm"]=>array(
	    		new ObjectCreateRule(null, "className"),
	    		new SetPropertiesRule(),
	    		new SetNextRule("addRealm", "org.apache.catalina.Realm")
	    ); 
		RulesBase.cache["Server/Service/Engine/Host/Valve"]=>array(
	    		new ObjectCreateRule(null, "className"),
	    		new SetPropertiesRule(),
	    		new SetNextRule("addValve", "org.apache.catalina.Valve")
	    );
		
		/**
		 * Server/Service/Engine/Host/Context
		 */
		RulesBase.cache["Server/Service/Engine/Host/Context"]=>array(
	    		new ObjectCreateRule("org.apache.catalina.core.StandardContext", "className"),//...........影响到上下文的映射
	    		new SetPropertiesRule(),
	    		/**
	    		 * 这个事件监听器：触发读取、解析了文件：/WEB-INF/web.xml和conf/web.xml
	    		 * 
	    		 */
	    		new LifecycleListenerRule ("org.apache.catalina.startup.ContextConfig","configClass"),//配置文件,上下文配置文件
	    		new SetNextRule("addChild", "org.apache.catalina.Container")
	    ); 
		
		RulesBase.cache["Server/Service/Engine/Host/Context/InstanceListener"]=>array(
				new CallMethodRule("addInstanceListener", 0)
		);
		RulesBase.cache["Server/Service/Engine/Host/Context/Listener"]=>array(
	    		new ObjectCreateRule(null, "className"),
	    		new SetPropertiesRule(),
	    		new SetNextRule("addLifecycleListener", "org.apache.catalina.LifecycleListener")
	    );
		RulesBase.cache["Server/Service/Engine/Host/Context/Loader"]=>array(
	    		new ObjectCreateRule("org.apache.catalina.loader.WebappLoader", "className"),
	    		new SetPropertiesRule(),
	    		new SetNextRule("setLoader", "org.apache.catalina.Loader")
	    );
		
		/**
		 * Server/Service/Engine/Host/Context/Manager
		 */
		RulesBase.cache["Server/Service/Engine/Host/Context/Manager"]=>array(
	    		new ObjectCreateRule("org.apache.catalina.session.StandardManager", "className"),
	    		new SetPropertiesRule(),
	    		new SetNextRule("setManager", "org.apache.catalina.Manager")
	    );
		RulesBase.cache["Server/Service/Engine/Host/Context/Manager/Store"]=>array(
	    		new ObjectCreateRule(null, "className"),
	    		new SetPropertiesRule(),
	    		new SetNextRule("setStore", "org.apache.catalina.Store")
	    );
		
		RulesBase.cache["Server/Service/Engine/Host/Context/Parameter"]=>array(
	    		new ObjectCreateRule("org.apache.catalina.deploy.ApplicationParameter", null),
	    		new SetPropertiesRule(),
	    		new SetNextRule("addApplicationParameter", "org.apache.catalina.deploy.ApplicationParameter")
	    );
		RulesBase.cache["Server/Service/Engine/Host/Context/Realm"]=>array(
	    		new ObjectCreateRule(null, "className"),
	    		new SetPropertiesRule(),
	    		new SetNextRule("setRealm", "org.apache.catalina.Realm")
	    ); 
		RulesBase.cache["Server/Service/Engine/Host/Context/Realm/Realm"]=>array(
	    		new ObjectCreateRule(null, "className"),
	    		new SetPropertiesRule(),
	    		new SetNextRule("addRealm", "org.apache.catalina.Realm")
	    ); 
		RulesBase.cache["Server/Service/Engine/Host/Context/Resources"]=>array(
	    		new ObjectCreateRule("org.apache.naming.resources.FileDirContext", null),
	    		new SetPropertiesRule(),
	    		new SetNextRule("setResources", "javax.naming.directory.DirContext")
	    );
		RulesBase.cache["Server/Service/Engine/Host/Context/ResourceLink"]=>array(
	    		new ObjectCreateRule("org.apache.catalina.deploy.ContextResourceLink", null),
	    		new SetPropertiesRule(),
	    		new SetNextNamingRule("addResourceLink","org.apache.catalina.deploy.ContextResourceLink")
	    );
		
		RulesBase.cache["Server/Service/Engine/Host/Context/Valve"]=>array(
	    		new ObjectCreateRule(null, "className"),
	    		new SetPropertiesRule(),
	    		new SetNextNamingRule("addValve","org.apache.catalina.Valve")
	    );
		RulesBase.cache["Server/Service/Engine/Host/Context/WatchedResource"]=>array(
				new CallMethodRule("addWatchedResource", 0)
		);
		RulesBase.cache["Server/Service/Engine/Host/Context/WrapperLifecycle"]=>array(
				new CallMethodRule("addWrapperLifecycle", 0)
		);
		RulesBase.cache["Server/Service/Engine/Host/Context/WrapperListener"]=>array(
				new CallMethodRule("addWrapperListener", 0)
		);
		
		/**
		 * Server/Service/Engine/Host/Cluster标签
		 */
		RulesBase.cache["Server/Service/Engine/Host/Cluster/Manager"]=>array(
	    		new ObjectCreateRule(null, "className"),
	    		new SetPropertiesRule(),
	    		new SetNextRule("setManagerTemplate", "org.apache.catalina.ha.ClusterManager")
	    );
		RulesBase.cache["Server/Service/Engine/Host/Cluster/Channel"]=>array(
	    		new ObjectCreateRule(null, "className"),
	    		new SetPropertiesRule(),
	    		new SetNextRule("setChannel", "org.apache.catalina.tribes.Channel")
	    );

		RulesBase.cache["Server/Service/Engine/Host/Cluster/Channel/Membership"]=>array(
	    		new ObjectCreateRule(null, "className"),
	    		new SetPropertiesRule(),
	    		new SetNextRule("setMembershipService", "org.apache.catalina.tribes.MembershipService")
	    );
		RulesBase.cache["Server/Service/Engine/Host/Cluster/Channel/Sender"]=>array(
	    		new ObjectCreateRule(null, "className"),
	    		new SetPropertiesRule(),
	    		new SetNextRule("setChannelSender", "org.apache.catalina.tribes.ChannelSender")
	    );
		RulesBase.cache["Server/Service/Engine/Host/Cluster/Channel/Sender/Transport"]=>array(
	    		new ObjectCreateRule(null, "className"),
	    		new SetPropertiesRule(),
	    		new SetNextRule("setTransport", "org.apache.catalina.tribes.transport.MultiPointSender")
	    );
		RulesBase.cache["Server/Service/Engine/Host/Cluster/Channel/Receiver"]=>array(
	    		new ObjectCreateRule(null, "className"),
	    		new SetPropertiesRule(),
	    		new SetNextRule("setChannelReceiver", "org.apache.catalina.tribes.ChannelReceiver")
	    );
		RulesBase.cache["Server/Service/Engine/Host/Cluster/Channel/Interceptor"]=>array(
	    		new ObjectCreateRule(null, "className"),
	    		new SetPropertiesRule(),
	    		new SetNextRule("addInterceptor", "org.apache.catalina.tribes.ChannelInterceptor")
	    );
		RulesBase.cache["Server/Service/Engine/Host/Cluster/Channel/Interceptor/Member"]=>array(
	    		new ObjectCreateRule(null, "className"),
	    		new SetPropertiesRule(),
	    		new SetNextRule("addStaticMember", "org.apache.catalina.tribes.Member")
	    );
		RulesBase.cache["Server/Service/Engine/Host/Cluster/Valve"]=>array(
	    		new ObjectCreateRule(null, "className"),
	    		new SetPropertiesRule(),
	    		new SetNextRule("addValve", "org.apache.catalina.Valve")
	    );
		RulesBase.cache["Server/Service/Engine/Host/Cluster/Deployer"]=>array(
	    		new ObjectCreateRule(null, "className"),
	    		new SetPropertiesRule(),
	    		new SetNextRule("setClusterDeployer", "org.apache.catalina.ha.ClusterDeployer")
	    );
		RulesBase.cache["Server/Service/Engine/Host/Cluster/Listener"]=>array(
	    		new ObjectCreateRule(null, "className"),
	    		new SetPropertiesRule(),
	    		new SetNextRule("addLifecycleListener", "org.apache.catalina.LifecycleListener")
	    );
		RulesBase.cache["Server/Service/Engine/Host/Cluster/ClusterListener"]=>array(
	    		new ObjectCreateRule(null, "className"),
	    		new SetPropertiesRule(),
	    		new SetNextRule("addClusterListener", "org.apache.catalina.ha.ClusterListener")
	    );
		
		RulesBase.cache["Server/Service/Engine/Host/Context/Ejb"]=>array(
	    		new ObjectCreateRule("org.apache.catalina.deploy.ContextEjb",null),
	    		new SetAllPropertiesRule(),
	    		new SetNextNamingRule("addEjb","org.apache.catalina.deploy.ContextEjb")
	    ); 
		RulesBase.cache["Server/Service/Engine/Host/Context/Environment"]=>array(
	    		new ObjectCreateRule("org.apache.catalina.deploy.ContextEnvironment",null),
	    		new SetAllPropertiesRule(),
	    		new SetNextNamingRule("addEnvironment","org.apache.catalina.deploy.ContextEnvironment")
	    ); 
		RulesBase.cache["Server/Service/Engine/Host/Context/LocalEjb"]=>array(
	    		new ObjectCreateRule("org.apache.catalina.deploy.ContextLocalEjb",null),
	    		new SetAllPropertiesRule(),
	    		new SetNextNamingRule("addLocalEjb","org.apache.catalina.deploy.ContextLocalEjb")
	    ); 
		RulesBase.cache["Server/Service/Engine/Host/Context/Resource"]=>array(
	    		new ObjectCreateRule("org.apache.catalina.deploy.ContextResource",null),
	    		new SetAllPropertiesRule(),
	    		new SetNextNamingRule("addResource","org.apache.catalina.deploy.ContextResource")
	    ); 
		RulesBase.cache["Server/Service/Engine/Host/Context/ResourceEnvRef"]=>array(
	    		new ObjectCreateRule("org.apache.catalina.deploy.ContextResourceEnvRef",null),
	    		new SetAllPropertiesRule(),
	    		new SetNextNamingRule("addResourceEnvRef","org.apache.catalina.deploy.ContextResourceEnvRef")
	    ); 
		RulesBase.cache["Server/Service/Engine/Host/Context/ServiceRef"]=>array(
	    		new ObjectCreateRule("org.apache.catalina.deploy.ContextService",null),
	    		new SetAllPropertiesRule(),
	    		new SetNextNamingRule("addService","org.apache.catalina.deploy.ContextService")
	    ); 
		RulesBase.cache["Server/Service/Engine/Host/Context/Transaction"]=>array(
	    		new ObjectCreateRule("org.apache.catalina.deploy.ContextTransaction",null),
	    		new SetAllPropertiesRule(),
	    		new SetNextNamingRule("setTransaction","org.apache.catalina.deploy.ContextTransaction")
	    ); 
		
		/**
		 * Server/Service/Engine标签
		 */
		RulesBase.cache["Server/Service/Engine"]=>array(
				new SetParentClassLoaderRule(parentClassLoader)
	    ); 
		/**
		 * Server/Service/Engine/Cluster标签
		 */
		RulesBase.cache["Server/Service/Engine/Cluster/Manager"]=>array(
	    		new ObjectCreateRule(null, "className"),
	    		new SetPropertiesRule(),
	    		new SetNextRule("setManagerTemplate", "org.apache.catalina.ha.ClusterManager")
	    );
		RulesBase.cache["Server/Service/Engine/Cluster/Channel"]=>array(
	    		new ObjectCreateRule(null, "className"),
	    		new SetPropertiesRule(),
	    		new SetNextRule("setChannel", "org.apache.catalina.tribes.Channel")
	    );
		RulesBase.cache["Server/Service/Engine/Cluster/Channel/Membership"]=>array(
	    		new ObjectCreateRule(null, "className"),
	    		new SetPropertiesRule(),
	    		new SetNextRule("setMembershipService", "org.apache.catalina.tribes.MembershipService")
	    );
		RulesBase.cache["Server/Service/Engine/Cluster/Channel/Sender"]=>array(
	    		new ObjectCreateRule(null, "className"),
	    		new SetPropertiesRule(),
	    		new SetNextRule("setChannelSender", "org.apache.catalina.tribes.ChannelSender")
	    );
		RulesBase.cache["Server/Service/Engine/Cluster/Channel/Sender/Transport"]=>array(
	    		new ObjectCreateRule(null, "className"),
	    		new SetPropertiesRule(),
	    		new SetNextRule("setTransport", "org.apache.catalina.tribes.transport.MultiPointSender")
	    );
		RulesBase.cache["Server/Service/Engine/Cluster/Channel/Receiver"]=>array(
	    		new ObjectCreateRule(null, "className"),
	    		new SetPropertiesRule(),
	    		new SetNextRule("setChannelReceiver", "org.apache.catalina.tribes.ChannelReceiver")
	    );
		RulesBase.cache["Server/Service/Engine/Cluster/Channel/Interceptor"]=>array(
	    		new ObjectCreateRule(null, "className"),
	    		new SetPropertiesRule(),
	    		new SetNextRule("addInterceptor", "org.apache.catalina.tribes.ChannelInterceptor")
	    );
		RulesBase.cache["Server/Service/Engine/Cluster/Channel/Interceptor/Member"]=>array(
	    		new ObjectCreateRule(null, "className"),
	    		new SetPropertiesRule(),
	    		new SetNextRule("addStaticMember", "org.apache.catalina.tribes.Member")
	    );
		RulesBase.cache["Server/Service/Engine/Cluster/Valve"]=>array(
	    		new ObjectCreateRule(null, "className"),
	    		new SetPropertiesRule(),
	    		new SetNextRule("addValve", "org.apache.catalina.Valve")
	    );
		RulesBase.cache["Server/Service/Engine/Cluster/Deployer"]=>array(
	    		new ObjectCreateRule(null, "className"),
	    		new SetPropertiesRule(),
	    		new SetNextRule("setClusterDeployer", "org.apache.catalina.ha.ClusterDeployer")
	    );
		RulesBase.cache["Server/Service/Engine/Cluster/Listener"]=>array(
	    		new ObjectCreateRule(null, "className"),
	    		new SetPropertiesRule(),
	    		new SetNextRule("addLifecycleListener", "org.apache.catalina.LifecycleListener")
	    );
		RulesBase.cache["Server/Service/Engine/Cluster/ClusterListener"]=>array(
	    		new ObjectCreateRule(null, "className"),
	    		new SetPropertiesRule(),
	    		new SetNextRule("addClusterListener", "org.apache.catalina.ha.ClusterListener")
	    );
}
