package cn.java.dubbo.demo.consumer.test;
import java.util.Map;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.utils.ConfigUtils;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ConsumerConfig;
import com.alibaba.dubbo.config.ModuleConfig;
import com.alibaba.dubbo.config.MonitorConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;

import cn.java.dubbo.demo.DemoService;

public class ConsumerTest {

	/*
		订阅接受到通知的类型：
			1、通知的内容是“路由信息”
			  protocol://user1:pwd1@www.domain.com:8080/dir1/dir2?category=routers&enabled=true&group=group1&version=*&classifier=*
			  route://user1:pwd1@www.domain.com:8080/dir1/dir2?category=routers&enabled=true&group=group1&version=*&classifier=*
			  
			2、 通知的内容是“配置信息 ”
			  protocol://user1:pwd1@www.domain.com:8080/dir1/dir2?category=configurators&enabled=true&group=group1&version=*&classifier=*
			  override://user1:pwd1@www.domain.com:8080/dir1/dir2?category=routers&enabled=true&group=group2&version=*&classifier=*
			
			3、通知的内容是“接口信息” protocol = (registry|filter|listener|mock|injvm|dubbo|rmi|hessian|thrift|memcached|redis|http|webservice)
			  protocol://user1:pwd1@www.domain.com:8080/dir1/dir2?category=providers&enabled=true&group=group2&version=*&classifier=*
			  protocol://user1:pwd1@www.domain.com:8080/dir1/dir2?category=providers&enabled=true&group=group2&version=*&classifier=*&disabled=false
			  protocol://user1:pwd1@www.domain.com:8080/dir1/dir2?category=providers&enabled=true&group=group1&version=*&classifier=*
			  	&connections=0 为零表示共享连接并且挂上一个，不为零表示初始挂上n个连接上来
			  	&dubbo=1.0.0
			  	&heartbeat=60000 开启heartbeat
			  	&server=服务器使用netty作为底层包
			  	&client=客户端使用netty作为底层包
			  	&lazy=false是否使用lazy模式的连接
			  	&codec=使用exchange编解码方式
			  	&exchanger=使用header交换方式
			  
			  memcached://user1:pwd1@www.domain.com:8080/dir1/dir2?category=providers&enabled=true&group=group1&version=*&classifier=*&backup=127.0.0.1

	 */
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// 调用refer  ---> FailfastClusterInvoker的代理对象 ---> 调用FailfastClusterInvoker.invoke()  --> 获取暴露的RPC接口列表(列表是订阅通知过来的)并进行负载均衡选择  --> 进行RPC
		/*
		 URL reference.urls[i] = url = {
		 	protocol : "zookeeper", // 注册中心协议
		 	username : "user1",
		 	password : "pwd1",
		 	host : "127.0.0.1",
		 	port : "9090",
		 	path : "RegistryService",
			parameters : {
				<<< ApplicationConfig 反射出来的配置 >>>
				<<< RegistryConfig 反射出来的配置 >>>
				dubbo : 1.0.0,
				timestamp : currentTimeMillis,
				pid : ConfigUtils.getPid(),
				...
				refer : StringUtils.toQueryString({
					side : "consumer"
					dubbo : 1.0.0,
					timestamp : currentTimeMillis,
					pid : ConfigUtils.getPid(),
					methods : "method1,method2,method3"
					interface : "cn.java.dubbo.demo.DemoService"
					<<< ApplicationConfig 反射出来的配置 >>>
					<<< module 反射出来的配置 >>>
					<<< ConsumerConfig 反射出来的配置 >>>
					<<< ReferenceConfig 反射出来的配置 >>>
					...
					monitor : URL.encode(URL = { // 监听器
						protocol : "dubbo", // logstat 监听器的协议
					 	username : "admin",
					 	password : "pwd1",
					 	host : "127.0.0.1",
					 	port : "9091",
					 	path : "",
						parameters : {
							interface : "com.alibaba.dubbo.monitor.MonitorService"
							dubbo : 1.0.0,
							timestamp : currentTimeMillis,
							pid : ConfigUtils.getPid(),
							<<< monitor 反射出来的配置 >>>
						}
					}.toFullString()),
				})
			}
		 }
		 */
		
					 
					
		
		// 当前应用配置
		ApplicationConfig application = new ApplicationConfig();
		application.setName("yyy");

		// 连接注册中心配置
		RegistryConfig registry = new RegistryConfig();
		registry.setAddress("10.20.130.230:9090");
		registry.setUsername("aaa");
		registry.setPassword("bbb");
		registry.setProtocol("zookeeper"); // 注册中心使用的协议  如：dubbo、zookeeper、multicast、redis
		
		// 监听器
		MonitorConfig monitorConfig = new MonitorConfig();
		monitorConfig.setAddress("127.0.0.1:8080");
		monitorConfig.setUsername("admin");
		monitorConfig.setPassword("pwd1");
		monitorConfig.setProtocol("dubbo"); // 使用的协议  如：dubbo、logstat
		
		// 引用远程服务
		ReferenceConfig<DemoService> reference = new ReferenceConfig<DemoService>(); // 此实例很重，封装了与注册中心的连接以及与提供者的连接，请自行缓存，否则可能造成内存和连接泄漏
		reference.setApplication(application);
		reference.setRegistry(registry); // 多个注册中心可以用setRegistries()
		reference.setInterface(DemoService.class);
		reference.setVersion("1.0.0");
		reference.setMonitor(monitorConfig);
		reference.setProtocol("filter"); // 接口支持的协议，如：dubbo
		
		// 和本地bean一样使用 demoService
		DemoService demoService = reference.get(); // 注意：此代理对象内部封装了所有通讯细节，对象较重，请缓存复用
	}
	
	public static void debug() {
		
		/*
		-------------------------------------
		appendProperties 的功能
			appendProperties 获取方式
				1、从系统环境变量中获取dubbo.com.alibaba.dubbo.config.classnamewithsuffixconfig.propertyx
				2、读取系统环境变量dubbo.properties.file中配置的“配置文件的地址”，读取配置文件，并从配置文件中获取相关配置
		
		-------------------------------------
		 各个对象配置初始化方式
			appendProperties(com.alibaba.dubbo.config.ConsumerConfig);
			appendProperties(com.alibaba.dubbo.config.ReferenceConfig);
			appendProperties(com.alibaba.dubbo.config.ApplicationConfig);
			appendProperties(com.alibaba.dubbo.config.RegistryConfig);
			
			1. com.alibaba.dubbo.config.ConsumerConfig
			{
				dubbo.consumer[.id].timeout = 10 // setTimeout
				
				dubbo.consumer.generic // setGeneric
				dubbo.consumer.version = 1.0.0 // setVersion
				dubbo.consumer.scope = ... // setScope
				dubbo.consumer.cluster = ... // setCluster
				dubbo.consumer.proxy = ... // setProxy
				dubbo.consumer.connections = ... // setConnections
				dubbo.consumer.stub = ... // setStub
				dubbo.consumer.loadbalance = ... // setLoadbalance
			}
			
			2. com.alibaba.dubbo.config.ReferenceConfig
			{
				dubbo.reference.protocol // setProtocol
				dubbo.reference.client // setClient
				
				dubbo.reference.generic // setGeneric
				dubbo.reference.version = 1.0.0 // setVersion
				dubbo.reference.scope = ... // setScope
				dubbo.reference.cluster = ... // setCluster
				dubbo.reference.proxy = ... // setProxy
				dubbo.reference.connections = ... // setConnections
				dubbo.reference.stub = ... // setStub
				dubbo.reference.loadbalance = ... // setLoadbalance
			}
			
			3. com.alibaba.dubbo.config.ApplicationConfig 全局配置
			{
				dubbo.application.name // setName
				dubbo.application.version // setVersion
				dubbo.application.monitor // setMonitor
				dubbo.application.compiler // setCompiler
				dubbo.application.logger // setLogger
			}
			
			4. com.alibaba.dubbo.config.RegistryConfig 请求注册中心的配置
			{
				dubbo.registry.protocol = "dubbo" // setProtocol 注册中协议，如   dubbo、multicast、zookeeper、redis --影响创建类型--> com.alibaba.dubbo.registry.RegistryFactory
				dubbo.registry.address = '192.168.1.210:9090,backupip:8080|192.168.1.211:9090|192.168.1.212:9090' // setAddress
				dubbo.registry.port // setPort
				dubbo.registry.username // setUsername
				dubbo.registry.password // setPassword
				
				dubbo.registry.server="netty" // setServer  
				dubbo.registry.group = "group1,group2" // setGroup  组要在这个集合中（推送过来的数据的要求）
				dubbo.registry.transporter="zkclient" // setTransporter 如 zkclient、curator  --影响创建类型-->com.alibaba.dubbo.remoting.zookeeper.ZookeeperTransporter
				dubbo.registry.cluster = "failfast" // setCluster 调度方式 ，如mock、failover、failfast、failsafe、failback、forking、available、switch、mergeable、broadcast 
				dubbo.registry.subscribe = true // setSubscribe，订阅的模式（需要长连接的支持，如zookeeper）
				dubbo.registry.version = "1.0.0"// setVersion  版本号（推送过来的数据的要求）
				dubbo.registry.client = "zkclient" // setClient 如 zkclient、curator  --影响创建类型-->com.alibaba.dubbo.remoting.zookeeper.ZookeeperTransporter
				dubbo.registry.timeout = 100 // setTimeout
			}
		-------------------------------------
	 	appendParameters的功能：
		 	protected static void appendParameters(Map<String, String> parameters, Object config, String prefix)
		 	反射config对象的中getXXX/isXXX方法，获取值，并放入parameters
		 	注意：
		 		如果getXXX/isXXX方法上带有注解@Parameter，还可配置注入指定parameters的key中
		
		-------------------------------------
		initedParameterMap 对象的内容
			Map<String, String> initedParameterMap; // 所有的配置都合并注入到这个map对象里面 == ApplicationConfig + moduleConfig + ConsumerConfig + ReferenceConfig
		
		-------------------------------------
		URL.protocol 变动的地方
			<1>、
				RegistryConfig.protocol --- protocol首次设置
			<2>、
				ApplicationConfig + RegistryConfig ---> registryList --- protocol被改动
				URL {
					protocol : "registry",
					username : "aaa", 
					password : "bbb", 
					host : "10.20.130.230", 
					port : "9090", 
					path : , 
					parameters : {
						ApplicationConfig + RegistryConfig...
						"registry" : "dubbo", // 注册中协议，如 zookeeper、dubbo
						"refer" : StringUtils.toQueryString(ApplicationConfig + moduleConfig + ConsumerConfig + ReferenceConfig) // 全局参数
					}
				}
			<3>、
				RegistryProtocol.refer(...) --- protocol又被改动
				URL {
					protocol : "dubbo",  // 注册中协议，如 zookeeper、dubbo
					username : "aaa", 
					password : "bbb", 
					host : "10.20.130.230", 
					port : "9090", 
					path : , 
					parameters : {
						ApplicationConfig + RegistryConfig...
						"refer" : StringUtils.toQueryString(ApplicationConfig + moduleConfig + ConsumerConfig + ReferenceConfig)
					}
				}
				
			-------------------------------------
			全局参数的配置
				<1> 设置
					URL url = {
						...
						parameters : {
							...
							refer : StringUtils.toQueryString(ApplicationConfig + moduleConfig + ConsumerConfig + ReferenceConfig)
							...
						}
					}
				<2> 获取
					StringUtils.parseQueryString(url.getParameterAndDecoded(Constants.REFER_KEY));
			-------------------------------------
			URL subscribeUrl = {
				protocol : "consumer",  //
				username : null, 
				password : null, 
				host : "LocalHost", 
				port : "0", 
				path : "DemoService", 
				parameters : {
					ApplicationConfig + RegistryConfig...
					"refer" : StringUtils.toQueryString(ApplicationConfig + moduleConfig + ConsumerConfig + ReferenceConfig),
					"category" : "providers,configurators,routers"
				}
			}
		*/
	}

}
