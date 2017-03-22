package cn.java.dubbo.adpative.configuratorfactory;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.rpc.cluster.Configurator;
import com.alibaba.dubbo.rpc.cluster.ConfiguratorFactory;
public class ConfiguratorFactory$Adpative implements ConfiguratorFactory{


//	@Adaptive("protocol")
	public Configurator getConfigurator(URL arg0) {
		if (arg0 == null) throw new IllegalArgumentException("url == null");
		URL url = arg0;
		/*
		 	override=com.alibaba.dubbo.rpc.cluster.configurator.override.OverrideConfiguratorFactory
			absent=com.alibaba.dubbo.rpc.cluster.configurator.absent.AbsentConfiguratorFactory
		 */
		String extName = url.getParameter("protocol");
		if(extName == null)
			throw new IllegalStateException("Fail to get extension(ConfiguratorFactory) name from url(" + url.toString()+") use keys(Arrays.toString(value))");
		ConfiguratorFactory extension = (ConfiguratorFactory)ExtensionLoader.getExtensionLoader(ConfiguratorFactory.class).getExtension(extName); 
		return extension.getConfigurator(arg0);
	}


}
