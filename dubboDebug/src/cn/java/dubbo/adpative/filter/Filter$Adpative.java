package cn.java.dubbo.adpative.filter;
import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcException;

/**
 * @author zhouzhian
 */
public class Filter$Adpative implements Filter{

	@Override
	public Result invoke(Invoker<?> arg0, Invocation arg1) throws RpcException {
		if (arg0 == null) throw new IllegalArgumentException("Invoker argument == null");
		if (arg0.getUrl() == null) throw new IllegalArgumentException("Invoker argument getUrl() == null");
		URL url = arg0.getUrl();
		/*
		 	echo=com.alibaba.dubbo.rpc.filter.EchoFilter
			generic=com.alibaba.dubbo.rpc.filter.GenericFilter
			genericimpl=com.alibaba.dubbo.rpc.filter.GenericImplFilter
			token=com.alibaba.dubbo.rpc.filter.TokenFilter
			accesslog=com.alibaba.dubbo.rpc.filter.AccessLogFilter
			activelimit=com.alibaba.dubbo.rpc.filter.ActiveLimitFilter
			classloader=com.alibaba.dubbo.rpc.filter.ClassLoaderFilter
			context=com.alibaba.dubbo.rpc.filter.ContextFilter
			consumercontext=com.alibaba.dubbo.rpc.filter.ConsumerContextFilter
			exception=com.alibaba.dubbo.rpc.filter.ExceptionFilter
			executelimit=com.alibaba.dubbo.rpc.filter.ExecuteLimitFilter
			deprecated=com.alibaba.dubbo.rpc.filter.DeprecatedFilter
			compatible=com.alibaba.dubbo.rpc.filter.CompatibleFilter
			timeout=com.alibaba.dubbo.rpc.filter.TimeoutFilter
			monitor=com.alibaba.dubbo.monitor.support.MonitorFilter --- @Activate(group = {Constants.PROVIDER, Constants.CONSUMER 是哪些组的Activate入口 }) ---- 统计监听器
			validation=com.alibaba.dubbo.validation.filter.ValidationFilter --- @Activate(group = { Constants.CONSUMER, Constants.PROVIDER }, value = Constants.VALIDATION_KEY, order = 10000) ----
			cache=com.alibaba.dubbo.cache.filter.CacheFilter
			trace=com.alibaba.dubbo.rpc.protocol.dubbo.filter.TraceFilter ---  @Activate(group = Constants.PROVIDER)
			future=com.alibaba.dubbo.rpc.protocol.dubbo.filter.FutureFilter
		 */
		String extName = url.getProtocol();
		if(extName == null)
			throw new IllegalStateException("Fail to get extension(Dispather) name from url(" + url.toString()+") use keys(Arrays.toString(value))");
		Filter extension = (Filter)ExtensionLoader.getExtensionLoader(Filter.class).getExtension(extName); 
		return extension.invoke(arg0,arg1);
	}

}
