package cn.java.internal.serviceregistry.foo2;

import java.util.Iterator;
import java.util.Map;

import org.hibernate.boot.registry.internal.StandardServiceRegistryImpl;
import org.hibernate.engine.jdbc.spi.JdbcServices;
import org.hibernate.engine.jndi.spi.JndiService;
import org.hibernate.service.spi.Configurable;
import org.hibernate.service.spi.InjectService;
import org.hibernate.service.spi.Manageable;
import org.hibernate.service.spi.ServiceRegistryAwareService;
import org.hibernate.service.spi.ServiceRegistryImplementor;
import org.hibernate.service.spi.Startable;

import cn.java.internal.serviceregistry.foo1.Foo1Service;

public class Foo2ServiceImpl implements Foo2Service,ServiceRegistryAwareService,Configurable,Startable,Manageable {

	@Override
	public void method1() {
		System.out.println(this.getClass().getName() + ":method1()");
	}

	
	/**
	 * 接口ServiceRegistryAwareService的实现方法
	 */
	@Override
	public void injectServices(ServiceRegistryImplementor serviceRegistry) {
		if(serviceRegistry instanceof StandardServiceRegistryImpl){
			
		}
	}

	/**
	 * 接口Configurable的实现方法
	 */
	@Override
	public void configure(Map configurationValues) {
		// 配置
		for (Iterator iterator = configurationValues.keySet().iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			System.out.println("key = "+key+" , value = "+ configurationValues.get(key));
		}
	}
	
	/**
	 * 接口Startable的实现方法
	 */
	@Override
	public void start() {
		
	}
	

	/**
	 * 接口Manageable的实现方法
	 * 		使用JmxService接口注册到jmx
	 */
	@Override
	public String getManagementDomain() {
		return null;
	}

	/**
	 * 接口Manageable的实现方法
	 * 		使用JmxService接口注册到jmx
	 */
	@Override
	public String getManagementServiceType() {
		return null;
	}

	/**
	 * 接口Manageable的实现方法
	 * 		使用JmxService接口注册到jmx
	 */
	@Override
	public Object getManagementBean() {
		return null;
	}


	// ------------------注入依赖-------------------------------
	/**
	 * 注入JdbcServices服务
	 */
	@InjectService(serviceRole=JdbcServices.class,required=true)
	public void injectJdbcServices(JdbcServices jdbcServices) {
//		jdbcServices.getDialect();
	}
	
	/**
	 * 注入服务JndiService
	 */
	@InjectService(serviceRole=JndiService.class,required=true)
	public void injectJndiService(JndiService jndiService) {
	}
	
	/**
	 * 注入服务Foo1Service
	 */
	@InjectService(serviceRole=Foo1Service.class,required=true)
	public void injectFoo1Service(Foo1Service foo1Service) {
		foo1Service.method1();
	}


}
