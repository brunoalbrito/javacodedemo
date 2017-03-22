package cn.java.dubbo.adpative.zookeepertransporter;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.remoting.zookeeper.ZookeeperClient;
import com.alibaba.dubbo.remoting.zookeeper.ZookeeperTransporter;

public class ZookeeperTransporter$Adpative implements ZookeeperTransporter{

//	@Adaptive({Constants.CLIENT_KEY, Constants.TRANSPORTER_KEY})
	public ZookeeperClient connect(URL arg0) {
		if (arg0 == null) throw new IllegalArgumentException("url == null");
		URL url = arg0;
		/*
		 	zkclient=com.alibaba.dubbo.remoting.zookeeper.zkclient.ZkclientZookeeperTransporter
			curator=com.alibaba.dubbo.remoting.zookeeper.curator.CuratorZookeeperTransporter
			
		 */
		String extName = url.getParameter("client",url.getParameter("transporter"));
		if(extName == null)
			throw new IllegalStateException("Fail to get extension(Protocol) name from url(" + url.toString()+") use keys(Arrays.toString(value))");
		ZookeeperTransporter extension = (ZookeeperTransporter)ExtensionLoader.getExtensionLoader(ZookeeperTransporter.class).getExtension(extName); 
		return extension.connect(arg0);
	}

}
