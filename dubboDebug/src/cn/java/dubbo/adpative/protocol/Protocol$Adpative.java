package cn.java.dubbo.adpative.protocol;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.Adaptive;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.rpc.Exporter;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Protocol;
import com.alibaba.dubbo.rpc.RpcException;
/**
 * *********************************************
 * ExtensionLoader.getExtensionLoader(Protocol.class).getExtension(extName)　// 通过扩展加载器获取"指定扩展"，如：dubbo=com.alibaba.dubbo.rpc.protocol.dubbo.DubboProtocol
 * ExtensionLoader.getExtensionLoader(Protocol.class).getAdaptiveExtension()　// 通过扩展加载器获取"自适应扩展"，如：adaptive=com.alibaba.dubbo.common.compiler.support.AdaptiveCompiler
 * *********************************************
 * xxx.getExtension(extName)获取扩展的流程：
 * 		1、new扩展对象；
 * 		2、识别扩展对象中的set方法，进行依赖注入
 * *********************************************
 * 本类(自适应扩展)作用：自适应调用，自动识别要调用哪个类的方法
 * 
 * 扩展加载器如果没有识别到“自适应扩展”，那么会动态生成Adpative类、并动态编译Adpative类
 * 
 * @author zhouzhian
 */
public class Protocol$Adpative implements Protocol{

	@Override
	public int getDefaultPort() {
		throw new UnsupportedOperationException("method getDefaultPort of interface Protocol is not adaptive method!");
	}

	/**
	 * 自动实现的方法
	 */
	@Adaptive
	public <T> Exporter<T> export(Invoker<T> arg0) throws RpcException {
		if (arg0 == null) throw new IllegalArgumentException("Invoker argument == null");
		if (arg0.getUrl() == null) throw new IllegalArgumentException("Invoker argument getUrl() == null");
		URL url = arg0.getUrl();
		/*
		 	registry=com.alibaba.dubbo.registry.integration.RegistryProtocol
			filter=com.alibaba.dubbo.rpc.protocol.ProtocolFilterWrapper  过滤器包装
			listener=com.alibaba.dubbo.rpc.protocol.ProtocolListenerWrapper 监听器包装
			mock=com.alibaba.dubbo.rpc.support.MockProtocol
			injvm=com.alibaba.dubbo.rpc.protocol.injvm.InjvmProtocol
			dubbo=com.alibaba.dubbo.rpc.protocol.dubbo.DubboProtocol
			rmi=com.alibaba.dubbo.rpc.protocol.rmi.RmiProtocol
			hessian=com.alibaba.dubbo.rpc.protocol.hessian.HessianProtocol
			com.alibaba.dubbo.rpc.protocol.http.HttpProtocol
			com.alibaba.dubbo.rpc.protocol.webservice.WebServiceProtocol
			thrift=com.alibaba.dubbo.rpc.protocol.thrift.ThriftProtocol
			memcached=memcom.alibaba.dubbo.rpc.protocol.memcached.MemcachedProtocol
			redis=com.alibaba.dubbo.rpc.protocol.redis.RedisProtocol
		 */
		String extName = url.getProtocol(); 
		if(extName == null)
			throw new IllegalStateException("Fail to get extension(Protocol) name from url(" + url.toString()+") use keys(Arrays.toString(value))");
		Protocol extension = (Protocol)ExtensionLoader.getExtensionLoader(Protocol.class).getExtension(extName);
		return extension.export(arg0);
	}

	/**
	 * 自动实现的方法
	 */
	@Adaptive
	public <T> Invoker<T> refer(Class<T> arg0, URL arg1) throws RpcException {
		// arg0 === cn.java.dubbo.demo.DemoService
		if (arg1 == null) throw new IllegalArgumentException("url == null");
		URL url = arg1;
		
		String extName = url.getProtocol(); 
		if(extName == null)
			throw new IllegalStateException("Fail to get extension(Protocol) name from url(" + url.toString()+") use keys(Arrays.toString(value))");
		Protocol extension = (Protocol)ExtensionLoader.getExtensionLoader(Protocol.class).getExtension(extName); 
		return extension.refer(arg0,arg1);
	}

	@Override
	public void destroy() {
		throw new UnsupportedOperationException("method destroy of interface Protocol is not adaptive method!");
	}

}
