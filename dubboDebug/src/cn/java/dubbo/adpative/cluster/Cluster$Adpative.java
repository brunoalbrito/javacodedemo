package cn.java.dubbo.adpative.cluster;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.Adaptive;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.cluster.Cluster;
import com.alibaba.dubbo.rpc.cluster.Directory;

/**
 * @author zhouzhian
 */
public class Cluster$Adpative implements Cluster{

	@Adaptive
	public <T> Invoker<T> join(Directory<T> arg0) throws RpcException {
		if (arg0 == null) throw new IllegalArgumentException("Invoker argument == null");
		if (arg0.getUrl() == null) throw new IllegalArgumentException("Invoker argument getUrl() == null");
		URL url = arg0.getUrl();
		/*
			
			mock=com.alibaba.dubbo.rpc.cluster.support.wrapper.MockClusterWrapper
			failover=com.alibaba.dubbo.rpc.cluster.support.FailoverCluster
			failfast=com.alibaba.dubbo.rpc.cluster.support.FailfastCluster
			failsafe=com.alibaba.dubbo.rpc.cluster.support.FailsafeCluster
			failback=com.alibaba.dubbo.rpc.cluster.support.FailbackCluster
			forking=com.alibaba.dubbo.rpc.cluster.support.ForkingCluster
			available=com.alibaba.dubbo.rpc.cluster.support.AvailableCluster
			switch=com.alibaba.dubbo.rpc.cluster.support.SwitchCluster
			mergeable=com.alibaba.dubbo.rpc.cluster.support.MergeableCluster
			broadcast=com.alibaba.dubbo.rpc.cluster.support.BroadcastCluster
			
		 */
		String extName = url.getParameter("cluster"); //
		if(extName == null)
			throw new IllegalStateException("Fail to get extension(Cluster) name from url(" + url.toString()+") use keys(Arrays.toString(value))");
		Cluster extension = (Cluster)ExtensionLoader.getExtensionLoader(Cluster.class).getExtension(extName); 
		return extension.join(arg0);
	}

}
