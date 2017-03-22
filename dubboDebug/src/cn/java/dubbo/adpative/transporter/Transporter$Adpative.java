package cn.java.dubbo.adpative.transporter;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.remoting.ChannelHandler;
import com.alibaba.dubbo.remoting.Client;
import com.alibaba.dubbo.remoting.RemotingException;
import com.alibaba.dubbo.remoting.Server;
import com.alibaba.dubbo.remoting.Transporter;

public class Transporter$Adpative implements Transporter{

	public Server bind(URL arg0, ChannelHandler arg1) throws RemotingException {
		if (arg1 == null) throw new IllegalArgumentException("url == null");
		URL url = arg0;
		
		/*
		 	netty=com.alibaba.dubbo.remoting.transport.netty.NettyTransporter
			mina=com.alibaba.dubbo.remoting.transport.mina.MinaTransporter
			grizzly=com.alibaba.dubbo.remoting.transport.grizzly.GrizzlyTransporter
		 */
		String extName = url.getParameter("server",url.getParameter("transporter"));
		if(extName == null)
			throw new IllegalStateException("Fail to get extension(Protocol) name from url(" + url.toString()+") use keys(Arrays.toString(value))");
		Transporter extension = (Transporter)ExtensionLoader.getExtensionLoader(Transporter.class).getExtension(extName); 
		return extension.bind(arg0,arg1);
	}

//	@Adaptive({Constants.CLIENT_KEY, Constants.TRANSPORTER_KEY})
	public Client connect(URL arg0, ChannelHandler arg1) throws RemotingException {
		if (arg1 == null) throw new IllegalArgumentException("url == null");
		URL url = arg0;
		
		String extName = url.getParameter("server",url.getParameter("transporter"));
		if(extName == null)
			throw new IllegalStateException("Fail to get extension(Protocol) name from url(" + url.toString()+") use keys(Arrays.toString(value))");
		Transporter extension = (Transporter)ExtensionLoader.getExtensionLoader(Transporter.class).getExtension(extName); 
		return extension.connect(arg0,arg1);
	}

}
