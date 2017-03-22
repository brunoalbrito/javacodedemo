package cn.java.dubbo.demo.provider.test;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.Adaptive;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.common.extension.SPI;
import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.ServiceConfig;
import com.alibaba.dubbo.rpc.Protocol;
import com.alibaba.dubbo.rpc.ProxyFactory;

import cn.java.dubbo.demo.DemoService;
import cn.java.dubbo.demo.provider.DemoServiceImpl;

public class ProviderTest {

	
	public static void main(String[] args) {
		System.out.println(DemoService.class.getName());
	}
	public static void test(String[] args) {
		 
		/*
		 	
		 	扩展加载器
		 	
		 	扩展接口的定义和特点
			 	扩展接口中“方法声明的注解”@Adaptive({Constants.PROXY_KEY})决定了XxxAdpative自适应类中方法调用url.getParameter(Constants.PROXY_KEY,"javassist")获取参数时，使用的键名 Constants.PROXY_KEY
			 	扩展接口中“类声明的注解”@SPI("javassist")决定了XxxAdpative自适应类中方法调用url.getParameter(Constants.PROXY_KEY,"javassist")获取不到参数时，使用的默认值javassist
		 	
			扩展加载器的应用功能
		 		通过扩展加载器获取“自适应扩展”，	如 ExtensionLoader.getExtensionLoader(Protocol.class).getAdaptiveExtension();
		 		通过扩展加载器获取“指定扩展”，	如 ExtensionLoader.getExtensionLoader(Protocol.class).getExtension(extName);
		 	
		 	获取自适应扩展的机制（即：getAdaptiveExtension()）
		 		识别扩展接口的的注解 @SPI 获取默认值 cachedDefaultName
			 	在类路径下依次搜寻 META-INF/dubbo/internal/cn.java.extensions.MyExtension、 META-INF/dubbo/cn.java.extensions.MyExtension、META-INF/services/cn.java.extensions.MyExtension文件做如下处理
			 	{
			 		读取文件中的每一行，“=”左侧作为模块名，右侧作为类名
			 		“加载扩展类A”，如果类有声明注解 @Adaptive，那么这个类作为“自适应类”（一个扩展只能有一个“自适应类”） cachedAdaptiveClass=
			 		尝试获取“加载扩展类A”的以扩展接口作为参数的构造函数，如果存在这个构造函数，那么记录此类为包裹类cachedWrapperClasses（如 :ProtocolFilterWrapper、ProtocolListenerWrapper）
			 		包括扩展中的模块名和类型信息，保存进 extensionClasses.put(n, clazz);	cachedClasses.set(extensionClasses);
			 	}
			 	如果 cachedAdaptiveClass 为null，那么反射接口的信息，生成 Xxx$Adpative 类并进行编译，返回Adpative实例
			 	反射Adpative实例的方法，识别setXXX的方法，注入依赖
			 	返回Adpative实例
			 	
		 	获取指定扩展的机制（即：getExtension()）
		 		识别扩展接口的的注解 @SPI 获取默认值 cachedDefaultName
			 	在类路径下依次搜寻 META-INF/dubbo/internal/cn.java.extensions.MyExtension、 META-INF/dubbo/cn.java.extensions.MyExtension、META-INF/services/cn.java.extensions.MyExtension文件做如下处理
			 	{
			 		读取文件中的每一行，“=”左侧作为模块名，右侧作为类名
			 		“加载扩展类A”，如果类有声明注解 @Adaptive，那么这个类作为“自适应类”（一个扩展只能有一个“自适应类”） cachedAdaptiveClass=
			 		尝试获取“加载扩展类A”的以扩展接口作为参数的构造函数，如果存在这个构造函数，那么记录此类为包裹类cachedWrapperClasses（如 :ProtocolFilterWrapper、ProtocolListenerWrapper）
			 		包括扩展中的模块名和类型信息，保存进 extensionClasses.put(n, clazz);	cachedClasses.set(extensionClasses);
			 	}
			 	反射extension实例的方法，识别setXXX的方法，进行注入依赖
			 	如果此扩展存在包裹类，那么迭代包裹对象列表{
				 	创建“包裹对象”（如ProtocolFilterWrapper、ProtocolListenerWrapper），对 extension实例进行包裹
				 	反射“包裹对象”的方法，识别setXXX的方法，注入依赖
			 	}
		 		返回“包裹对象或者扩展对象”
		 	
		 	Protocol扩展
		 		- Protocol$Adpative 如果没有定义并配置，会根据接口信息动态生成自适应类，本类的作用的是动态识别配置参数调用调用实际扩展
		 		- RegistryProtocol
		 		- DubboProtocol
		 		- ThriftProtocol
		 		- RmiProtocol
		 		- MemcachedProtocol
		 		- RedisProtocol
		 		- ....
		 
		 	调用export ---> 注册接口信息到注册中心  ---> 启动守护进程接受客户端的请求
		 	
		 	// 注册到zookeeper的地址
			/group/ServiceInterface/category/URL.encode(url.toFullString())
			/dubbo/cn.java.dubbo.demo.DemoService/providers/URL.encode("dubbo://user1:pwd1@www.service1.com:8089/contextpath?side=provider&dubbo=1.0.0&timestamp=12333&pid=...&methods=...&token=...&dynamic...")
			
			 	
		 	URL url = {
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
					export : (URL = {
						 	protocol : "dubbo", // 接口协议
						 	username : "user1",
						 	password : "pwd1",
						 	host : "www.service1.com",
						 	port : "8089",
						 	path : "contextpath", //　上下文路径
							parameters : {
								side : "provider"
								dubbo : 1.0.0,
								timestamp : currentTimeMillis,
								pid : ConfigUtils.getPid(),
								<<< ApplicationConfig 反射出来的配置 >>>
								<<< module 反射出来的配置 >>>
								<<< ProviderConfig 反射出来的配置 >>>
								<<< ProtocolConfig 反射出来的配置 >>>
								<<< ServiceConfig 反射出来的配置 >>>
								interface : "cn.java.dubbo.demo.DemoService"
								methods : "method1,method2,method3"
								token : UUID.randomUUID().toString()
								dynamic ： registryURL.getParameter("dynamic")
								monitor ：(URL = {
									protocol : "dubbo", // logstat 监听器的协议
								 	username : "admin",
								 	password : "pwd1",
								 	host : "127.0.0.1",
								 	port : "9091",
								 	path : "",
									parameters : {
										interface : "MonitorService"
										dubbo : 1.0.0,
										timestamp : currentTimeMillis,
										pid : ConfigUtils.getPid(),
										<<< monitor 反射出来的配置 >>>
									}
								}.toFullString())
							}
						}.toFullString())
				}
			}
			
		 */
		// 服务实现
		DemoService demoService = new DemoServiceImpl();
		 
		// 当前应用配置
		ApplicationConfig application = new ApplicationConfig();
		application.setName("xxx");
		
		// 连接注册中心配置
		String registryProtocol = "zookeeper"; // 居然在这边定义API支持的协议
		RegistryConfig registry = new RegistryConfig();
		registry.setAddress("10.20.130.230:9090");
		registry.setUsername("aaa");
		registry.setPassword("bbb");
		registry.setProtocol(registryProtocol);
		registry.setParameters(new HashMap<String, String>(){
			{
				put(Constants.PROXY_KEY, "jdk"); // 使用哪种代理工厂创建代理 （jdk=JdkProxyFactory，javassist=JavassistProxyFactory）
				// 注册的位置 /group1/ServiceInterface/providers/url.toFullString()
				put(Constants.GROUP_KEY, "group1"); 
				put(Constants.CATEGORY_KEY, Constants.DEFAULT_CATEGORY); 
			}
		});
		
		// 服务提供者协议配置  dubbo://127.0.0.1:8089/dir1/DemoService?
		String serviceProtocol = "dubbo"; 
		ProtocolConfig protocol = new ProtocolConfig(); // com.alibaba.dubbo.rpc.protocol.dubbo.DubboProtocol
		protocol.setName(serviceProtocol); // ???
		protocol.setHost("127.0.0.1");
		protocol.setPort(8089);
		protocol.setThreads(200);
		protocol.setContextpath("dir1");
		
		// 注意：ServiceConfig为重对象，内部封装了与注册中心的连接，以及开启服务端口
		// 服务提供者暴露服务配置
		ServiceConfig<DemoService> service = new ServiceConfig<DemoService>(); // 此实例很重，封装了与注册中心的连接，请自行缓存，否则可能造成内存和连接泄漏
		service.setApplication(application);
		service.setRegistry(registry); // "服务"使用的注册中心
		service.setProtocol(protocol); // "服务"提供的协议
		service.setInterface(DemoService.class); // "服务"的接口信息
		service.setRef(demoService); // "服务"对象
		service.setScope(Constants.SCOPE_REMOTE); // "服务"以远程接口的方式暴露
		service.setProxy("jdk");
		
		// group/path:version:port
		// group1/DemoService:1.0.0:12345
		service.setGroup("group1");
		service.setPath(DemoService.class.getName());
		service.setVersion("1.0.0");
		 
		/**
			System.setProperty("dubbo.provider.host","127.0.0.1");
			System.setProperty("dubbo.provider.port","666");
		 */
		// 暴露及注册服务
		service.export();
	}
	
	public static void debug() {
		/*
		 	appendProperties(com.alibaba.dubbo.config.ProviderConfig);
		 	appendProperties(com.alibaba.dubbo.config.ApplicationConfig);
		 	appendProperties(com.alibaba.dubbo.config.RegistryConfig);
		 	appendProperties(com.alibaba.dubbo.config.ProtocolConfig);
		 	appendProperties(com.alibaba.dubbo.config.ServiceConfig);
		 	
		 	com.alibaba.dubbo.config.ProviderConfig
		 	{
		 		dubbo.provider.contextpath
		 	}
		 	
		 	com.alibaba.dubbo.config.ApplicationConfig
		 	{
		 	
		 	}
		 	
		 	com.alibaba.dubbo.config.RegistryConfig
		 	{
			 	dubbo.registry.address = "192.168.1.210:8081,backupip:8081|192.168.1.211:8081"
			 	dubbo.registry.protocol = "dubbo"
		 	}
		 	
		 	com.alibaba.dubbo.config.ProtocolConfig
		 	{
		 		dubbo.protocol.name = "dubbo" 协议名称
		 		dubbo.protocol.host = "127.0.0.1"
		 		dubbo.protocol.port = "20880"
		 		dubbo.protocol.contextpath = "20880"
		 	}
		 	
		 	com.alibaba.dubbo.config.ServiceConfig
		 	{
		 		dubbo.service.scope = "none"
		 		dubbo.service.path = "demoService"
		 	}
		 	
		 	
		 	map = {
		 		"side" ： "provider"
		 		"dubbo" ： "2.0.0"
		 		"timestamp" ： System.currentTimeMillis()
		 		com.alibaba.dubbo.config.ApplicationConfig中所有具备getXXX的属性
		 		module中所有具备getXXX的属性
		 		com.alibaba.dubbo.config.ProviderConfig中所有具备getXXX的属性
		 		com.alibaba.dubbo.config.ProtocolConfig中所有具备getXXX的属性
		 		com.alibaba.dubbo.config.ServiceConfig中所有具备getXXX的属性
		 		"methods" : "method1,method2,method3"
		 		"token" : UUID.randomUUID().toString()
		 		
		 	}
		 	
		 	
		 	
		 	
		 	
		 	override=com.alibaba.dubbo.rpc.cluster.configurator.override.OverrideConfiguratorFactory
			absent=com.alibaba.dubbo.rpc.cluster.configurator.absent.AbsentConfiguratorFactory
		 */
	}
	

}
