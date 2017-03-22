package cn.java.dubbo.adpative.codec;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.Adaptive;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.remoting.Channel;
import com.alibaba.dubbo.remoting.Codec;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.cluster.Cluster;
import com.alibaba.dubbo.rpc.cluster.Directory;

/**
 * 编解码器 - 自适应入口
 * @author zhouzhian
 */
public class Codec$Adpative implements Codec{

	@Adaptive
	public <T> Invoker<T> join(Directory<T> arg0) throws RpcException {
		if (arg0 == null) throw new IllegalArgumentException("Invoker argument == null");
		if (arg0.getUrl() == null) throw new IllegalArgumentException("Invoker argument getUrl() == null");
		URL url = arg0.getUrl();
		
		String extName = url.getParameter("cluster"); //
		if(extName == null)
			throw new IllegalStateException("Fail to get extension(Cluster) name from url(" + url.toString()+") use keys(Arrays.toString(value))");
		Cluster extension = (Cluster)ExtensionLoader.getExtensionLoader(Cluster.class).getExtension(extName); 
		return extension.join(arg0);
	}

//	@Adaptive({Constants.CODEC_KEY})
	public void encode(Channel arg0, OutputStream arg1, Object arg2) throws IOException {
		if (arg0 == null) throw new IllegalArgumentException("Invoker argument == null");
		if (arg0.getUrl() == null) throw new IllegalArgumentException("Invoker argument getUrl() == null");
		URL url = arg0.getUrl();
		/*
			transport=com.alibaba.dubbo.remoting.transport.codec.TransportCodec
			telnet=com.alibaba.dubbo.remoting.telnet.codec.TelnetCodec
			exchange=com.alibaba.dubbo.remoting.exchange.codec.ExchangeCodec
			dubbo=com.alibaba.dubbo.rpc.protocol.dubbo.DubboCountCodec
			thrift=com.alibaba.dubbo.rpc.protocol.thrift.ThriftCodec
		 */
		String extName = url.getParameter("codec"); //
		if(extName == null)
			throw new IllegalStateException("Fail to get extension(Codec) name from url(" + url.toString()+") use keys(Arrays.toString(value))");
		Codec extension = (Codec)ExtensionLoader.getExtensionLoader(Codec.class).getExtension(extName); 
		extension.encode(arg0,arg1,arg2);
	}

//	@Adaptive({Constants.CODEC_KEY})
	public Object decode(Channel arg0, InputStream arg1) throws IOException {
		if (arg0 == null) throw new IllegalArgumentException("Invoker argument == null");
		if (arg0.getUrl() == null) throw new IllegalArgumentException("Invoker argument getUrl() == null");
		
		URL url = arg0.getUrl();
		String extName = url.getParameter("codec"); //
		if(extName == null)
			throw new IllegalStateException("Fail to get extension(Codec) name from url(" + url.toString()+") use keys(Arrays.toString(value))");
		Codec extension = (Codec)ExtensionLoader.getExtensionLoader(Codec.class).getExtension(extName); 
		return extension.decode(arg0,arg1);
	}

}
