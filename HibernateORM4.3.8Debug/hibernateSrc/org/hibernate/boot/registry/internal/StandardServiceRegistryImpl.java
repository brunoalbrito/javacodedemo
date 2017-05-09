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
package org.hibernate.boot.registry.internal;

import java.util.List;
import java.util.Map;

import org.hibernate.boot.registry.BootstrapServiceRegistry;
import org.hibernate.boot.registry.StandardServiceInitiator;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.engine.jdbc.internal.JdbcServicesInitiator;
import org.hibernate.service.Service;
import org.hibernate.service.internal.AbstractServiceRegistryImpl;
import org.hibernate.service.internal.ProvidedService;
import org.hibernate.service.spi.Configurable;
import org.hibernate.service.spi.ServiceBinding;
import org.hibernate.service.spi.ServiceInitiator;

/**
 * Standard Hibernate implementation of the standard service registry.
 *
 * @author Steve Ebersole
 */
public class StandardServiceRegistryImpl extends AbstractServiceRegistryImpl implements StandardServiceRegistry {
	private final Map configurationValues;

	/**
	 * Constructs a StandardServiceRegistryImpl.  Should not be instantiated directly; use
	 * {@link org.hibernate.boot.registry.StandardServiceRegistryBuilder} instead
	 *
	 * @param bootstrapServiceRegistry The bootstrap service registry.
	 * @param serviceInitiators Any StandardServiceInitiators provided by the user to the builder
	 * @param providedServices Any standard services provided directly to the builder
	 * @param configurationValues Configuration values
	 *
	 * @see org.hibernate.boot.registry.StandardServiceRegistryBuilder
	 */
	@SuppressWarnings( {"unchecked"})
	public StandardServiceRegistryImpl(
			BootstrapServiceRegistry bootstrapServiceRegistry,
			List<StandardServiceInitiator> serviceInitiators,
			List<ProvidedService> providedServices,
			Map<?, ?> configurationValues) {
		this( true, bootstrapServiceRegistry, serviceInitiators, providedServices, configurationValues );
	}

	/**
	 * Constructs a StandardServiceRegistryImpl.  Should not be instantiated directly; use
	 * {@link org.hibernate.boot.registry.StandardServiceRegistryBuilder} instead
	 *
	 * @param autoCloseRegistry See discussion on
	 * {@link org.hibernate.boot.registry.StandardServiceRegistryBuilder#disableAutoClose}
	 * @param bootstrapServiceRegistry The bootstrap service registry.
	 * @param serviceInitiators Any StandardServiceInitiators provided by the user to the builder
	 * @param providedServices Any standard services provided directly to the builder
	 * @param configurationValues Configuration values
	 *
	 * @see org.hibernate.boot.registry.StandardServiceRegistryBuilder
	 */
	@SuppressWarnings( {"unchecked"})
	public StandardServiceRegistryImpl(//
			boolean autoCloseRegistry,//true
			BootstrapServiceRegistry bootstrapServiceRegistry,//BootstrapServiceRegistryImpl
			List<StandardServiceInitiator> serviceInitiators,// == standardInitiatorList();一大堆列表 !!!!!!!!!!!!!
			List<ProvidedService> providedServices,// === new ArrayList<ProvidedService>();
			Map<?, ?> configurationValues) {// 拷贝的属性
		/**
		 			bootstrapServiceRegistry = BootstrapServiceRegistryImpl 
		 */
		super( bootstrapServiceRegistry, autoCloseRegistry );

		/**
		 * 配置文件属性的拷贝
		 */
		this.configurationValues = configurationValues;

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
			serviceInitiators.add( JdbcServicesInitiator.INSTANCE );  //  ！！！！！！！！！！   serviceInitiators.add( new JdbcServicesInitiator() );
			serviceInitiators.add( RefCursorSupportInitiator.INSTANCE );
	
			serviceInitiators.add( MutableIdentifierGeneratorFactoryInitiator.INSTANCE);
	
			serviceInitiators.add( JtaPlatformResolverInitiator.INSTANCE );
			serviceInitiators.add( JtaPlatformInitiator.INSTANCE );
			serviceInitiators.add( TransactionFactoryInitiator.INSTANCE );
	
			serviceInitiators.add( SessionFactoryServiceRegistryFactoryInitiator.INSTANCE );
	
			serviceInitiators.add( RegionFactoryInitiator.INSTANCE );
		 */
		// process initiators
		for ( ServiceInitiator initiator : serviceInitiators ) {
			/**
			 	如：initiator ===  org.hibernate.engine.jdbc.internal.JdbcServicesInitiator


			   final ServiceBinding serviceBinding = new ServiceBinding( this, initiator );
			   serviceBindingMap.put( initiator.getServiceInitiated(), serviceBinding );
			 */
			createServiceBinding( initiator );
		}

		/**
		 * 
		 */
		// then, explicitly provided service instances
		for ( ProvidedService providedService : providedServices ) {
			createServiceBinding( providedService );
		}
	}

	@Override
	public <R extends Service> R initiateService(ServiceInitiator<R> serviceInitiator) {
		
		/*
		 	serviceInitiator === org.hibernate.engine.jdbc.internal.JdbcServicesInitiator
		 	return  org.hibernate.engine.jdbc.internal.JdbcServicesImpl;


		 */
		// todo : add check/error for unexpected initiator types?
		return ( (StandardServiceInitiator<R>) serviceInitiator ).initiateService( configurationValues, this );
	}

	@Override
	public <R extends Service> void configureService(ServiceBinding<R> serviceBinding) {
		if ( Configurable.class.isInstance( serviceBinding.getService() ) ) {
			( (Configurable) serviceBinding.getService() ).configure( configurationValues );
		}
	}
}
