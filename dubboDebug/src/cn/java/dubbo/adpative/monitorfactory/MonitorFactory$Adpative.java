package cn.java.dubbo.adpative.monitorfactory;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.monitor.Monitor;
import com.alibaba.dubbo.monitor.MonitorFactory;
public class MonitorFactory$Adpative implements MonitorFactory{



	@Override
	public Monitor getMonitor(URL arg0) {
		if (arg0 == null) throw new IllegalArgumentException("url == null");
		URL url = arg0;
		/*
	 		dubbo=com.alibaba.dubbo.monitor.dubbo.DubboMonitorFactroy
		 */
		String extName = url.getParameter("protocol");
		if(extName == null)
			throw new IllegalStateException("Fail to get extension(ConfiguratorFactory) name from url(" + url.toString()+") use keys(Arrays.toString(value))");
		MonitorFactory extension = (MonitorFactory)ExtensionLoader.getExtensionLoader(MonitorFactory.class).getExtension(extName); 
		return extension.getMonitor(arg0);
	}

}
