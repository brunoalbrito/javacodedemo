/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * Copyright (c) 2011, Red Hat Inc. or third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Inc.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 */
package org.hibernate.service.internal;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.hibernate.boot.registry.BootstrapServiceRegistry;
import org.hibernate.cfg.Environment;
import org.hibernate.engine.jdbc.spi.JdbcServices;
import org.hibernate.internal.CoreLogging;
import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.internal.util.collections.CollectionHelper;
import org.hibernate.internal.util.config.ConfigurationHelper;
import org.hibernate.jmx.spi.JmxService;
import org.hibernate.service.Service;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.UnknownServiceException;
import org.hibernate.service.spi.InjectService;
import org.hibernate.service.spi.Manageable;
import org.hibernate.service.spi.ServiceBinding;
import org.hibernate.service.spi.ServiceException;
import org.hibernate.service.spi.ServiceInitiator;
import org.hibernate.service.spi.ServiceRegistryAwareService;
import org.hibernate.service.spi.ServiceRegistryImplementor;
import org.hibernate.service.spi.Startable;
import org.hibernate.service.spi.Stoppable;

/**
 * Basic implementation of the ServiceRegistry and ServiceRegistryImplementor contracts
 *
 * @author Steve Ebersole
 */
public abstract class AbstractServiceRegistryImpl
		implements ServiceRegistryImplementor, ServiceBinding.ServiceLifecycleOwner {

	private static final CoreMessageLogger log = CoreLogging.messageLogger( AbstractServiceRegistryImpl.class );

	public static final String ALLOW_CRAWLING = "hibernate.service.allow_crawling";

	private final ServiceRegistryImplementor parent;
	private final boolean allowCrawling;

	private final ConcurrentServiceBinding<Class,ServiceBinding> serviceBindingMap = new ConcurrentServiceBinding<Class,ServiceBinding>();
	private ConcurrentServiceBinding<Class,Class> roleXref;

	// IMPL NOTE : the list used for ordered destruction.  Cannot used map above because we need to
	// iterate it in reverse order which is only available through ListIterator
	// assume 20 services for initial sizing
	private final List<ServiceBinding> serviceBindingList = CollectionHelper.arrayList( 20 );

	private boolean autoCloseRegistry;
	private Set<ServiceRegistryImplementor> childRegistries;

	@SuppressWarnings( {"UnusedDeclaration"})
	protected AbstractServiceRegistryImpl() {
		this( (ServiceRegistryImplementor) null );
	}

	@SuppressWarnings( {"UnusedDeclaration"})
	protected AbstractServiceRegistryImpl(boolean autoCloseRegistry) {
		this( (ServiceRegistryImplementor) null, autoCloseRegistry );
	}

	protected AbstractServiceRegistryImpl(ServiceRegistryImplementor parent) {
		this( parent, true );
	}

	protected AbstractServiceRegistryImpl(
			ServiceRegistryImplementor parent,
			boolean autoCloseRegistry) {
		this.parent = parent;
		this.allowCrawling = ConfigurationHelper.getBoolean( ALLOW_CRAWLING, Environment.getProperties(), true );

		this.autoCloseRegistry = autoCloseRegistry;
		this.parent.registerChild( this );
	}

	public AbstractServiceRegistryImpl(BootstrapServiceRegistry bootstrapServiceRegistry) {
		this( bootstrapServiceRegistry, true );
	}

	public AbstractServiceRegistryImpl(
			BootstrapServiceRegistry bootstrapServiceRegistry,//BootstrapServiceRegistryImpl
			boolean autoCloseRegistry) {//true
		if ( ! ServiceRegistryImplementor.class.isInstance( bootstrapServiceRegistry ) ) {
			throw new IllegalArgumentException( "ServiceRegistry parent needs to implement ServiceRegistryImplementor" );
		}
		/**
			 this.parent =   BootstrapServiceRegistryImpl
		 */
		this.parent = (ServiceRegistryImplementor) bootstrapServiceRegistry;
		/**
		  	取得配置文件hibernate.properties中的"hibernate.service.allow_crawling"的配置项目
		 */
		this.allowCrawling = ConfigurationHelper.getBoolean( ALLOW_CRAWLING, Environment.getProperties(), true );

		/**
		 	autoCloseRegistry = true
		 */
		this.autoCloseRegistry = autoCloseRegistry;
		this.parent.registerChild( this );
	}

	@SuppressWarnings({ "unchecked" })
	protected <R extends Service> void createServiceBinding(ServiceInitiator<R> initiator) {
		//!!!!!!!!!!!!
		final ServiceBinding serviceBinding = new ServiceBinding( this, initiator );
		serviceBindingMap.put( initiator.getServiceInitiated(), serviceBinding );
	}

	protected <R extends Service> void createServiceBinding(ProvidedService<R> providedService) {
		ServiceBinding<R> binding = locateServiceBinding( providedService.getServiceRole(), false );
		if ( binding == null ) {
			binding = new ServiceBinding<R>( this, providedService.getServiceRole(), providedService.getService() );
			serviceBindingMap.put( providedService.getServiceRole(), binding );
		}
		registerService( binding, providedService.getService() );
	}

	@Override
	@SuppressWarnings( {"unchecked"})
	public ServiceRegistry getParentServiceRegistry() {
		return parent;
	}

	/**
	 	如：
	 		serviceRole = JdbcServices.class
	 		serviceRole = ClassLoaderService.class
	 */
	@Override
	@SuppressWarnings({ "unchecked" })
	public <R extends Service> ServiceBinding<R> locateServiceBinding(Class<R> serviceRole) {
		return locateServiceBinding( serviceRole, true );
	}

	/**
	 	如：
	 		serviceRole = JdbcServices.class
	 		serviceRole = ClassLoaderService.class
	 
	 */
	@SuppressWarnings({ "unchecked" })
	protected <R extends Service> ServiceBinding<R> locateServiceBinding(Class<R> serviceRole, boolean checkParent) {
		
		/**
		 	有这么些初始化类
			 	serviceInitiators.add( ConfigurationServiceInitiator.INSTANCE );
				serviceInitiators.add( ImportSqlCommandExtractorInitiator.INSTANCE );
		
				serviceInitiators.add( JndiServiceInitiator.INSTANCE );
				serviceInitiators.add( JmxServiceInitiator.INSTANCE );
		
				serviceInitiators.add( PersisterClassResolverInitiator.INSTANCE );
				serviceInitiators.add( PersisterFactoryInitiator.INSTANCE );
		
				serviceInitiators.add( ConnectionProviderInitiator.INSTANCE );
				serviceInitiators.add( MultiTenantConnectionProviderInitiator.INSTANCE );
				serviceInitiators.add( DialectResolverInitiator.INSTANCE );
				serviceInitiators.add( DialectFactoryInitiator.INSTANCE );
				serviceInitiators.add( BatchBuilderInitiator.INSTANCE );
				serviceInitiators.add( JdbcServicesInitiator.INSTANCE );  //  ！！！！！！！！！！ serviceBindingMap.put( JdbcServices.class, new ServiceBinding( this, JdbcServicesInitiator.INSTANCE ) );
				serviceInitiators.add( RefCursorSupportInitiator.INSTANCE );
		
				serviceInitiators.add( MutableIdentifierGeneratorFactoryInitiator.INSTANCE);
		
				serviceInitiators.add( JtaPlatformResolverInitiator.INSTANCE );
				serviceInitiators.add( JtaPlatformInitiator.INSTANCE );
				serviceInitiators.add( TransactionFactoryInitiator.INSTANCE );
		
				serviceInitiators.add( SessionFactoryServiceRegistryFactoryInitiator.INSTANCE );
		
				serviceInitiators.add( RegionFactoryInitiator.INSTANCE );
				
				for(....)
				{
				   final ServiceBinding serviceBinding = new ServiceBinding( this, initiator );
				   serviceBindingMap.put( initiator.getServiceInitiated(), serviceBinding );
				}
		 */
		ServiceBinding<R> serviceBinding = serviceBindingMap.get( serviceRole );
		if ( serviceBinding == null && checkParent && parent != null ) {
			/**
					  parent = org.hibernate.boot.registry.internal.BootstrapServiceRegistryImpl
			 */
			// look in parent
			serviceBinding = parent.locateServiceBinding( serviceRole );
		}

		if ( serviceBinding != null ) {
			return serviceBinding;
		}

		/**
		 	取得配置文件hibernate.properties中的"hibernate.service.allow_crawling"的配置项目
		 */
		if ( !allowCrawling ) {
			return null;
		}

		/**
		 * 空
		 */
		// look for a previously resolved alternate registration
		if ( roleXref != null ) {
			final Class alternative = roleXref.get( serviceRole );
			if ( alternative != null ) {
				return serviceBindingMap.get( alternative );
			}
		}

		// perform a crawl looking for an alternate registration
		for ( ServiceBinding binding : serviceBindingMap.values() ) {
			if ( serviceRole.isAssignableFrom( binding.getServiceRole() ) ) {
				// we found an alternate...
				log.alternateServiceRole( serviceRole.getName(), binding.getServiceRole().getName() );
				registerAlternate( serviceRole, binding.getServiceRole() );
				return binding;
			}

			if ( binding.getService() != null && serviceRole.isInstance( binding.getService() ) ) {
				// we found an alternate...
				log.alternateServiceRole( serviceRole.getName(), binding.getServiceRole().getName() );
				registerAlternate( serviceRole, binding.getServiceRole() );
				return binding;
			}
		}

		return null;
	}

	private void registerAlternate(Class alternate, Class target) {
		if ( roleXref == null ) {
			roleXref = new ConcurrentServiceBinding<Class,Class>();
		}
		roleXref.put( alternate, target );
	}

	/**
	 	serviceRegistry.getService( JdbcServices.class ).getDialect();
	 	如：
	 		serviceRole = JdbcServices.class
	 		serviceRole = ClassLoaderService.class
	 */
	@Override
	public <R extends Service> R getService(Class<R> serviceRole) {
		/**
		 * 取得服务绑定
		 */
		final ServiceBinding<R> serviceBinding = locateServiceBinding( serviceRole );
		if ( serviceBinding == null ) {
			throw new UnknownServiceException( serviceRole );
		}

		/**
		 * 取得服务
		 */
		R service = serviceBinding.getService();// !!!!!!!!!!!
		if ( service == null ) {// 如果“服务”未实例化
			//!!!!!! 
			//创建服务对象 
			//给服务对象，反射注解注入依赖 injectServices(...)
			//给服务对象注入配置 configure(...)
			//启动服务对象 .start(...);
			service = initializeService( serviceBinding );
		}

		return service; // org.hibernate.engine.jdbc.internal.JdbcServicesImpl
	}

	protected <R extends Service> void registerService(ServiceBinding<R> serviceBinding, R service) {
		serviceBinding.setService( service );//service
		synchronized ( serviceBindingList ) {
			serviceBindingList.add( serviceBinding );
		}
	}

	private <R extends Service> R initializeService(ServiceBinding<R> serviceBinding) {
		if ( log.isTraceEnabled() ) {
			log.tracev( "Initializing service [role={0}]", serviceBinding.getServiceRole().getName() );
		}

		/*
		 	R service === org.hibernate.engine.jdbc.internal.JdbcServicesImpl
		 */
		// PHASE 1 : create service
		R service = createService( serviceBinding );
		if ( service == null ) {
			return null;
		}

		/*
		 	serviceBinding.getLifecycleOwner() === org.hibernate.boot.registry.internal.StandardServiceRegistryImpl
		 */
		// PHASE 2 : inject service (***potentially recursive***)
		serviceBinding.getLifecycleOwner().injectDependencies( serviceBinding ); // 依赖注入

		// PHASE 3 : configure service   StandardServiceRegistryImpl.configureService( serviceBinding );
		serviceBinding.getLifecycleOwner().configureService( serviceBinding ); // 注入属性

		// PHASE 4 : Start service  ......
		serviceBinding.getLifecycleOwner().startService( serviceBinding ); // 启动服务

		return service;
	}

	@SuppressWarnings( {"unchecked"})
	protected <R extends Service> R createService(ServiceBinding<R> serviceBinding) {
		//!!!!!!!! serviceBinding = new ServiceBinding( StandardServiceRegistryImpl, JdbcServicesInitiator.INSTANCE )
		final ServiceInitiator<R> serviceInitiator = serviceBinding.getServiceInitiator(); // 获取初始器
		if ( serviceInitiator == null ) {
			// this condition should never ever occur
			throw new UnknownServiceException( serviceBinding.getServiceRole() );
		}

		try {
			
			/*
			 	serviceInitiator  === org.hibernate.engine.jdbc.internal.JdbcServicesInitiator
			 	serviceInitiator.getLifecycleOwner() === org.hibernate.boot.registry.internal.StandardServiceRegistryImpl
			 	R service === org.hibernate.engine.jdbc.internal.JdbcServicesImpl
			 	
			 */
			R service = serviceBinding.getLifecycleOwner().initiateService( serviceInitiator );
			// IMPL NOTE : the register call here is important to avoid potential stack overflow issues
			//		from recursive calls through #configureService
			registerService( serviceBinding, service );
			return service;
		}
		catch ( ServiceException e ) {
			throw e;
		}
		catch ( Exception e ) {
			throw new ServiceException( "Unable to create requested service [" + serviceBinding.getServiceRole().getName() + "]", e );
		}
	}

	@Override
	public <R extends Service> void injectDependencies(ServiceBinding<R> serviceBinding) {
		final R service = serviceBinding.getService();

		/*
		 	service === org.hibernate.engine.jdbc.internal.JdbcServicesImpl
		 */
		applyInjections( service );//!!!!!依赖注入

		if ( ServiceRegistryAwareService.class.isInstance( service ) ) {
			( (ServiceRegistryAwareService) service ).injectServices( this );
		}
	}

	private <R extends Service> void applyInjections(R service) {
		try {
			/*
			 	service === org.hibernate.engine.jdbc.internal.JdbcServicesImpl
			 */
			for ( Method method : service.getClass().getMethods() ) {//!!!!!! 反射注解
				InjectService injectService = method.getAnnotation( InjectService.class );
				if ( injectService == null ) {
					continue;
				}

				processInjection( service, method, injectService );
			}
		}
		catch (NullPointerException e) {
            log.error( "NPE injecting service deps : " + service.getClass().getName() );
		}
	}

	@SuppressWarnings({ "unchecked" })
	private <T extends Service> void processInjection(T service, Method injectionMethod, InjectService injectService) {
		if ( injectionMethod.getParameterTypes() == null || injectionMethod.getParameterTypes().length != 1 ) {
			throw new ServiceDependencyException(
					"Encountered @InjectService on method with unexpected number of parameters"
			);
		}

		/*
		 	service === org.hibernate.engine.jdbc.internal.JdbcServicesImpl
		 	dependentServiceRole === org.hibernate.engine.jdbc.spi.JdbcServices.class
		 */
		Class dependentServiceRole = injectService.serviceRole();
		if ( dependentServiceRole == null || dependentServiceRole.equals( Void.class ) ) {
			dependentServiceRole = injectionMethod.getParameterTypes()[0];
		}

		// todo : because of the use of proxies, this is no longer returning null here...

		final Service dependantService = getService( dependentServiceRole );
		if ( dependantService == null ) {
			if ( injectService.required() ) {
				throw new ServiceDependencyException(
						"Dependency [" + dependentServiceRole + "] declared by service [" + service + "] not found"
				);
			}
		}
		else {
			try {
				injectionMethod.invoke( service, dependantService );
			}
			catch ( Exception e ) {
				throw new ServiceDependencyException( "Cannot inject dependency service", e );
			}
		}
	}

	@Override
	@SuppressWarnings({ "unchecked" })
	public <R extends Service> void startService(ServiceBinding<R> serviceBinding) {
		if ( Startable.class.isInstance( serviceBinding.getService() ) ) {
			( (Startable) serviceBinding.getService() ).start();
		}

		if ( Manageable.class.isInstance( serviceBinding.getService() ) ) {
			getService( JmxService.class ).registerService(
					(Manageable) serviceBinding.getService(),
					serviceBinding.getServiceRole()
			);
		}
	}

	private boolean active = true;

	public boolean isActive() {
		return active;
	}

	@Override
    @SuppressWarnings( {"unchecked"})
	public void destroy() {
		if ( !active ) {
			return;
		}

		active = false;
		try {
			synchronized (serviceBindingList) {
				ListIterator<ServiceBinding> serviceBindingsIterator = serviceBindingList.listIterator(
						serviceBindingList.size()
				);
				while ( serviceBindingsIterator.hasPrevious() ) {
					final ServiceBinding serviceBinding = serviceBindingsIterator.previous();
					serviceBinding.getLifecycleOwner().stopService( serviceBinding );
				}
				serviceBindingList.clear();
			}
			serviceBindingMap.clear();
		}
		finally {
			parent.deRegisterChild( this );
		}
	}

	@Override
	public <R extends Service> void stopService(ServiceBinding<R> binding) {
		final Service service = binding.getService();
		if ( Stoppable.class.isInstance( service ) ) {
			try {
				( (Stoppable) service ).stop();
			}
			catch ( Exception e ) {
				log.unableToStopService( service.getClass(), e.toString() );
			}
		}
	}

	@Override
	public void registerChild(ServiceRegistryImplementor child) {
		if ( childRegistries == null ) {
			childRegistries = new HashSet<ServiceRegistryImplementor>();
		}
		if ( !childRegistries.add( child ) ) {
			log.warnf(
					"Child ServiceRegistry [%s] was already registered; this will end badly later...",
					child
			);
		}
	}

	@Override
	public void deRegisterChild(ServiceRegistryImplementor child) {
		if ( childRegistries == null ) {
			throw new IllegalStateException( "No child ServiceRegistry registrations found" );
		}
		childRegistries.remove( child );
		if ( childRegistries.isEmpty() ) {
			if ( autoCloseRegistry ) {
				log.debug(
						"Implicitly destroying ServiceRegistry on de-registration " +
								"of all child ServiceRegistries"
				);
				destroy();
			}
			else {
				log.debug(
						"Skipping implicitly destroying ServiceRegistry on de-registration " +
								"of all child ServiceRegistries"
				);
			}
		}
	}
}
