package cn.java.dubbo.adpative.proxyfactory;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.Adaptive;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.common.extension.SPI;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.ProxyFactory;
import com.alibaba.dubbo.rpc.RpcException;

/**
 * 动态生成的代码如下
 * @author zhouzhian
 */
public class ProxyFactory$Adpative implements ProxyFactory{

	/**
	 * 用于客户端，创建服务代理
	 */
//	@Adaptive({Constants.PROXY_KEY})
	public <T> T getProxy(Invoker<T> arg0) throws RpcException {
		if (arg0 == null) throw new IllegalArgumentException("Invoker argument == null");
		if (arg0.getUrl() == null) throw new IllegalArgumentException("Invoker argument getUrl() == null");
		URL url = arg0.getUrl();
		/*
		 	stub=com.alibaba.dubbo.rpc.proxy.wrapper.StubProxyFactoryWrapper
			jdk=com.alibaba.dubbo.rpc.proxy.jdk.JdkProxyFactory
			javassist=com.alibaba.dubbo.rpc.proxy.javassist.JavassistProxyFactory
		 */
//		String extName = url.getParameter("proxy"); 
		String extName = url.getParameter(Constants.PROXY_KEY,"javassist"); // 接口中声明的注解@SPI("javassist")决定了获取不到参数默认使用的值
		if(extName == null)
			throw new IllegalStateException("Fail to get extension(ProxyFactory) name from url(" + url.toString()+") use keys(Arrays.toString(value))");
		ProxyFactory extension = (ProxyFactory)ExtensionLoader.getExtensionLoader(ProxyFactory.class).getExtension(extName); //  com.alibaba.dubbo.rpc.proxy.javassist.JavassistProxyFactory
		return extension.getProxy(arg0); // JavassistProxyFactory.getProxy(arg0)
	}

//	@Adaptive({Constants.PROXY_KEY})
	public <T> Invoker<T> getInvoker(T arg0, Class<T> arg1, URL arg2) throws RpcException {
		if (arg1 == null) throw new IllegalArgumentException("url == null");
		URL url = arg2;
		
//		String extName = url.getParameter("proxy"); 
		String extName = url.getParameter(Constants.PROXY_KEY); // ===javassist
		if(extName == null)
			throw new IllegalStateException("Fail to get extension(ProxyFactory) name from url(" + url.toString()+") use keys(Arrays.toString(value))");
		ProxyFactory extension = (ProxyFactory)ExtensionLoader.getExtensionLoader(ProxyFactory.class).getExtension(extName); //  com.alibaba.dubbo.rpc.proxy.javassist.JavassistProxyFactory
		
		return extension.getInvoker(arg0,arg1,arg2);
	}
}
