package cn.java.internal.serviceregistry;

import java.io.File;
import java.util.Map.Entry;
import java.util.Properties;

import org.hibernate.boot.registry.BootstrapServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.registry.internal.BootstrapServiceRegistryImpl;
import org.hibernate.boot.registry.internal.StandardServiceRegistryImpl;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.jdbc.spi.JdbcServices;
import org.hibernate.integrator.internal.IntegratorServiceImpl;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.integrator.spi.IntegratorService;
import org.hibernate.service.spi.ServiceBinding;
import org.hibernate.service.spi.ServiceInitiator;

import cn.java.internal.serviceregistry.foo1.Foo1Service;
import cn.java.internal.serviceregistry.foo1.Foo1ServiceImpl;
import cn.java.internal.serviceregistry.foo2.Foo2Service;
import cn.java.internal.serviceregistry.foo2.Foo2ServiceInitiator;

public class Test {


	public static void main(String[] args) {
		Configuration configuration = new Configuration();
		configuration.configure("./" + getDir() + "/hibernate.cfg.xml");

		testSetAndGetConfigurationProperties(configuration); // 属性配置

		// serviceRegistryBuilder === org.hibernate.boot.registry.internal.BootstrapServiceRegistryImpl
		StandardServiceRegistryBuilder serviceRegistryBuilder = new StandardServiceRegistryBuilder(); // 创建“Standard服务构建器”
		testGetInternalServiceFromBootstrapServiceRegistry(serviceRegistryBuilder.getBootstrapServiceRegistry()); // 从“Bootstrap服务注册中心”获取服务对象
		testSetCustomServiceIntoStandardServiceRegistryBuilder(serviceRegistryBuilder); //  设置“Standard服务构建器”的属性
		serviceRegistryBuilder.applySettings(configuration.getProperties()); // 设置“Standard服务构建器”的属性
		StandardServiceRegistry standardServiceRegistry = serviceRegistryBuilder.build(); // 创建“StandardServiceRegistry服务注册中心”
		testGetCustomServiceFromStandardServiceRegistry(standardServiceRegistry);// 从“Standard服务注册中心”获取服务对象
		
//		testGetInternalServiceFromStandardServiceRegistry(standardServiceRegistry);// 从“Standard服务注册中心”获取服务对象
//		SessionFactory sessionFactory = configuration.buildSessionFactory(standardServiceRegistry);
//		Session session = sessionFactory.getCurrentSession();
	}

	/**
	 * 从“Standard服务注册中心”获取服务对象
	 * @param standardServiceRegistry
	 */
	private static void testGetInternalServiceFromStandardServiceRegistry(StandardServiceRegistry standardServiceRegistry) {
		// standardServiceRegistry === org.hibernate.boot.registry.internal.StandardServiceRegistryImpl
		
		// 敏捷代码
		{
			JdbcServices service = standardServiceRegistry.getService(JdbcServices.class);
			Dialect dialect = service.getDialect();
		}
		
		// 详情代码
		{
			if(standardServiceRegistry instanceof StandardServiceRegistryImpl){
				StandardServiceRegistryImpl standardServiceRegistryImpl = (StandardServiceRegistryImpl)standardServiceRegistry;
				ServiceBinding<JdbcServices> serviceBinding =  standardServiceRegistryImpl.locateServiceBinding(JdbcServices.class);
				JdbcServices service = serviceBinding.getService();
				if (service == null) {
					ServiceInitiator serviceInitiator = serviceBinding.getServiceInitiator();
					// serviceBinding.getLifecycleOwner() === org.hibernate.boot.registry.internal.StandardServiceRegistryImpl
					service = serviceBinding.getLifecycleOwner().initiateService(serviceInitiator); // 操作初始化器 --> 调用初始化器的initiateService()方法
					serviceBinding.setService(service); // 操作创建出来的对象 --> 设置到serviceBinding
					serviceBinding.getLifecycleOwner().injectDependencies(serviceBinding); // 操作创建出来的对象 --> 依赖注入，识别注解@InjectService
					serviceBinding.getLifecycleOwner().configureService(serviceBinding); // 操作创建出来的对象 --> 注入属性（如果实现了接口 Configurable）
					serviceBinding.getLifecycleOwner().startService(serviceBinding); // 操作创建出来的对象 --> 启动服务（如果实现了接口Startable）、注册到jmx（如果实现了接口Manageable）
				}
				Dialect dialect = service.getDialect();
			}
		}
		
	}
	
	/**
	 * 从StandardServiceRegistry获取服务
	 * @param standardServiceRegistry
	 */
	private static void testGetCustomServiceFromStandardServiceRegistry(StandardServiceRegistry standardServiceRegistry) {
		Foo1Service foo1Service = standardServiceRegistry.getService(Foo1Service.class);
		foo1Service.method1();
//		Foo2Service foo2Service = standardServiceRegistry.getService(Foo2ServiceInitiator.INSTANCE.getServiceInitiated());
//		foo2Service.method1();
	}

	
	/**
	 * 设置“Standard服务构建器”的属性  -- 托管服务给StandardServiceRegistry
	 * @param serviceRegistryBuilder
	 */
	private static void testSetCustomServiceIntoStandardServiceRegistryBuilder(StandardServiceRegistryBuilder standardServiceRegistryBuilder) {
		
		{
			standardServiceRegistryBuilder.addInitiator(Foo2ServiceInitiator.INSTANCE); // 有初始化器
		}
		
		{
			standardServiceRegistryBuilder.addService(Foo1Service.class, new Foo1ServiceImpl()); // 没有初始化器
		}
	}

	/**
	 * 从“Bootstrap服务注册中心”获取服务对象
	 * @param bootstrapServiceRegistry
	 */
	private static void testGetInternalServiceFromBootstrapServiceRegistry(BootstrapServiceRegistry bootstrapServiceRegistry) {
		// bootstrapServiceRegistry === org.hibernate.boot.registry.internal.BootstrapServiceRegistryImpl

		{
			// 敏捷代码
			{
				IntegratorService integratorService =  bootstrapServiceRegistry.getService( IntegratorService.class );
				if(integratorService instanceof IntegratorServiceImpl){
					IntegratorServiceImpl integratorServiceImpl = (IntegratorServiceImpl)integratorService;
					for ( Integrator integrator : integratorServiceImpl.getIntegrators() ) {

					}
				}
			}
						
			// 详情代码
			if(bootstrapServiceRegistry instanceof BootstrapServiceRegistryImpl){
				BootstrapServiceRegistryImpl bootstrapServiceRegistryImpl = (BootstrapServiceRegistryImpl)bootstrapServiceRegistry;
				ServiceBinding<IntegratorService> serviceBinding =  bootstrapServiceRegistryImpl.locateServiceBinding(IntegratorService.class);
				IntegratorService integratorService =  serviceBinding.getService();

				if(integratorService instanceof IntegratorServiceImpl){
					IntegratorServiceImpl integratorServiceImpl = (IntegratorServiceImpl)integratorService;
					for ( Integrator integrator : integratorServiceImpl.getIntegrators() ) {

					}
				}
			}
		}

		// bootstrapServiceRegistry.getService(IntegratorService.class) == org.hibernate.integrator.internal.IntegratorServiceImpl
		// bootstrapServiceRegistry.getService(ClassLoaderService.class) == org.hibernate.boot.registry.classloading.internal.ClassLoaderServiceImpl
		// bootstrapServiceRegistry.getService(StrategySelector.class) == org.hibernate.boot.registry.selector.internal.StrategySelectorImpl
	}

	/**
	 * 设置属性、获取属性
	 * @param configuration
	 */
	private static void testSetAndGetConfigurationProperties(Configuration configuration) {

		// 获取属性
		System.out.println("---------Environment.getProperties()-------------");
		Properties properties = Environment.getProperties();
		for (Entry<Object, Object> entry : properties.entrySet()) {
//			System.out.println("key = " + entry.getKey() + ", value = " + entry.getValue());
			System.out.println(entry.getKey() + " = " + entry.getValue());
		}

		// 获取属性
		System.out.println("---------configuration.getProperties()-------------");
		properties = configuration.getProperties();
		for (Entry<Object, Object> entry : properties.entrySet()) {
//			System.out.println("key = " + entry.getKey() + ", value = " + entry.getValue());
			System.out.println(entry.getKey() + " = " + entry.getValue());
		}

		// 设置属性
		configuration.setProperty("propertyName0", "propertyName0_value");
		System.out.println(configuration.getProperty("propertyName0"));

		// 删除属性
		properties = configuration.getProperties();
		properties.remove("propertyName0");
		configuration.setProperties(properties);
	}

	private static String getDir() {
		Class clazz = Test.class;
		String dirName = clazz.getName();
		dirName = dirName.substring(0, dirName.length() - (clazz.getSimpleName().length() + 1));
		dirName = dirName.replace(".", File.separator);
		return dirName;
	}
}
