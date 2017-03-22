package cn.java.dubbo.adpative.registryfactory;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.registry.Registry;
import com.alibaba.dubbo.registry.RegistryFactory;

/**
 * @author zhouzhian
 */
public class RegistryFactory$Adpative implements RegistryFactory{

	
	/**
	 * 连接统一服务注册中心（如zookeeper），并添加监听器
	 */
	public Registry getRegistry(URL arg0) {
		if (arg0 == null) throw new IllegalArgumentException("url == null");
		URL url = arg0;
		/*
			
			dubbo=com.alibaba.dubbo.registry.dubbo.DubboRegistryFactory
			multicast=com.alibaba.dubbo.registry.multicast.MulticastRegistryFactory
			zookeeper=com.alibaba.dubbo.registry.zookeeper.ZookeeperRegistryFactory
			redis=com.alibaba.dubbo.registry.redis.RedisRegistryFactory
			
		 */
		String extName = url.getProtocol(); // dubbo
		if(extName == null)
			throw new IllegalStateException("Fail to get extension(RegistryFactory) name from url(" + url.toString()+") use keys(Arrays.toString(value))");
		RegistryFactory extension = (RegistryFactory)ExtensionLoader.getExtensionLoader(RegistryFactory.class).getExtension(extName); 
		return extension.getRegistry(arg0);
	}

}
