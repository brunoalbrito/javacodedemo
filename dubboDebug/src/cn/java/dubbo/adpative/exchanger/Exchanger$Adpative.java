package cn.java.dubbo.adpative.exchanger;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.remoting.RemotingException;
import com.alibaba.dubbo.remoting.exchange.ExchangeClient;
import com.alibaba.dubbo.remoting.exchange.ExchangeHandler;
import com.alibaba.dubbo.remoting.exchange.ExchangeServer;
import com.alibaba.dubbo.remoting.exchange.Exchanger;

public class Exchanger$Adpative implements Exchanger{


	public ExchangeServer bind(URL arg0, ExchangeHandler arg1) throws RemotingException {
		if (arg0 == null) throw new IllegalArgumentException("url == null");
		URL url = arg0;
		/*
		  header=com.alibaba.dubbo.remoting.exchange.support.header.HeaderExchanger
		 */
		String extName = url.getParameter("exchanger");
		if(extName == null)
			throw new IllegalStateException("Fail to get extension(Protocol) name from url(" + url.toString()+") use keys(Arrays.toString(value))");
		Exchanger extension = (Exchanger)ExtensionLoader.getExtensionLoader(Exchanger.class).getExtension(extName); 
		return extension.bind(arg0,arg1);
	}

//	@Adaptive({Constants.EXCHANGER_KEY})
	public ExchangeClient connect(URL arg0, ExchangeHandler arg1) throws RemotingException {
		if (arg1 == null) throw new IllegalArgumentException("url == null");
		URL url = arg0;
		
		String extName = url.getParameter("exchanger");
		if(extName == null)
			throw new IllegalStateException("Fail to get extension(Protocol) name from url(" + url.toString()+") use keys(Arrays.toString(value))");
		Exchanger extension = (Exchanger)ExtensionLoader.getExtensionLoader(Exchanger.class).getExtension(extName); 
		return extension.connect(arg0,arg1);
	}

}
