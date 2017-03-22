package cn.java.dubbo.adpative.dispather;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.remoting.ChannelHandler;
import com.alibaba.dubbo.remoting.Dispather;
public class Dispather$Adpative implements Dispather{

//	@Adaptive({Constants.DISPATHER_KEY, Constants.CHANNEL_HANDLER_KEY})
	public ChannelHandler dispath(ChannelHandler arg0, URL arg1) {
		if (arg1 == null) throw new IllegalArgumentException("url == null");
		URL url = arg1;
		/*
		 	all=com.alibaba.dubbo.remoting.transport.dispather.all.AllDispather
			direct=com.alibaba.dubbo.remoting.transport.dispather.direct.DirectDispather
			message=com.alibaba.dubbo.remoting.transport.dispather.message.MessageOnlyDispather
			execution=com.alibaba.dubbo.remoting.transport.dispather.execution.ExecutionDispather
			connection=com.alibaba.dubbo.remoting.transport.dispather.connection.ConnectionOrderedDispather
		 */
		String extName = url.getParameter("dispather",url.getParameter("channel.handler"));
		if(extName == null)
			throw new IllegalStateException("Fail to get extension(Dispather) name from url(" + url.toString()+") use keys(Arrays.toString(value))");
		Dispather extension = (Dispather)ExtensionLoader.getExtensionLoader(Dispather.class).getExtension(extName); 
		return extension.dispath(arg0,arg1);
	}


}
