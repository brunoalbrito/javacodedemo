package cn.java.dubbo.note;

import com.alibaba.dubbo.config.support.Parameter;

public class Test {

	
	public static void main(String[] args) {
		/*
		  	----------------
		  	发布接口
		  	----------------
		  		1、向注册中心订阅接口信息 
		  	
		  	----------------
		  	调用
		  	----------------
		  		1、向注册中心订阅接口信息 
		  		2、调用，根据（调用策略+负载均衡器）进行调用
		  		
		  	调用策略
			  	mock=com.alibaba.dubbo.rpc.cluster.support.wrapper.MockClusterWrapper
				failover=com.alibaba.dubbo.rpc.cluster.support.FailoverCluster 失败转移，当出现失败，重试其它服务器，通常用于读操作，但重试会带来更长延迟。
				failfast=com.alibaba.dubbo.rpc.cluster.support.FailfastCluster 快速失败，只发起一次调用，失败立即报错，通常用于非幂等性的写操作。
				failsafe=com.alibaba.dubbo.rpc.cluster.support.FailsafeCluster 失败安全，出现异常时，直接忽略，通常用于写入审计日志等操作。 
				failback=com.alibaba.dubbo.rpc.cluster.support.FailbackCluster 失败自动恢复，后台记录失败请求，定时重发，通常用于消息通知操作。
				forking=com.alibaba.dubbo.rpc.cluster.support.ForkingCluster 并行调用，只要一个成功即返回，通常用于实时性要求较高的操作，但需要浪费更多服务资源。
				available=com.alibaba.dubbo.rpc.cluster.support.AvailableCluster
				switch=com.alibaba.dubbo.rpc.cluster.support.SwitchCluster 支持在多个Invoker（注册中心Invoker，内部Wrapper了多个Invoker）之间切换。
				mergeable=com.alibaba.dubbo.rpc.cluster.support.MergeableCluster
				broadcast=com.alibaba.dubbo.rpc.cluster.support.BroadcastCluster
		 */
	}

	@Parameter(key = "config-key1",excluded=true)
	public String getConfigKey1() { // 指定key
		return "";
	}
	
	@Parameter(excluded=true)
	public String getConfigKey2() { // 自动转换 getConfigKey2---> config-key2
		return "";
	}

}
