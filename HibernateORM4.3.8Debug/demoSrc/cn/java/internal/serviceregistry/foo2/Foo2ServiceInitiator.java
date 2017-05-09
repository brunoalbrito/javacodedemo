package cn.java.internal.serviceregistry.foo2;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.hibernate.boot.registry.StandardServiceInitiator;
import org.hibernate.boot.registry.internal.StandardServiceRegistryImpl;
import org.hibernate.service.Service;
import org.hibernate.service.spi.ServiceRegistryImplementor;

public class Foo2ServiceInitiator implements StandardServiceInitiator{

	public static final Foo2ServiceInitiator INSTANCE = new Foo2ServiceInitiator();

	/**
	 * 接口名
	 */
	@Override
	public Class getServiceInitiated() {
		return Foo2Service.class;
	}

	/**
	 * 实例化
	 */
	@Override
	public Service initiateService(Map configurationValues, ServiceRegistryImplementor registry) {
		// 配置
		for (Iterator iterator = configurationValues.keySet().iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			System.out.println("key = "+key+" , value = "+ configurationValues.get(key));
		}
		
		// 标准服务注册中
		if(registry instanceof StandardServiceRegistryImpl){

		}
		
		return new Foo2ServiceImpl();
	}

}
