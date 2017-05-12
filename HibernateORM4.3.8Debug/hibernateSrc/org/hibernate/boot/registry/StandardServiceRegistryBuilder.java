/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * Copyright (c) 2012, Red Hat Inc. or third-party contributors as
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
package org.hibernate.boot.registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.hibernate.boot.registry.classloading.spi.ClassLoaderService;
import org.hibernate.boot.registry.internal.StandardServiceRegistryImpl;
import org.hibernate.cache.internal.RegionFactoryInitiator;
import org.hibernate.cfg.Environment;
import org.hibernate.engine.config.internal.ConfigurationServiceInitiator;
import org.hibernate.engine.jdbc.batch.internal.BatchBuilderInitiator;
import org.hibernate.engine.jdbc.connections.internal.ConnectionProviderInitiator;
import org.hibernate.engine.jdbc.connections.internal.MultiTenantConnectionProviderInitiator;
import org.hibernate.engine.jdbc.cursor.internal.RefCursorSupportInitiator;
import org.hibernate.engine.jdbc.dialect.internal.DialectFactoryInitiator;
import org.hibernate.engine.jdbc.dialect.internal.DialectResolverInitiator;
import org.hibernate.engine.jdbc.internal.JdbcServicesInitiator;
import org.hibernate.engine.jndi.internal.JndiServiceInitiator;
import org.hibernate.engine.transaction.internal.TransactionFactoryInitiator;
import org.hibernate.engine.transaction.jta.platform.internal.JtaPlatformInitiator;
import org.hibernate.engine.transaction.jta.platform.internal.JtaPlatformResolverInitiator;
import org.hibernate.id.factory.internal.MutableIdentifierGeneratorFactoryInitiator;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.integrator.spi.IntegratorService;
import org.hibernate.internal.jaxb.cfg.JaxbHibernateConfiguration;
import org.hibernate.internal.util.config.ConfigurationHelper;
import org.hibernate.jmx.internal.JmxServiceInitiator;
import org.hibernate.persister.internal.PersisterClassResolverInitiator;
import org.hibernate.persister.internal.PersisterFactoryInitiator;
import org.hibernate.service.ConfigLoader;
import org.hibernate.service.Service;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.StandardServiceInitiators;
import org.hibernate.service.internal.ProvidedService;
import org.hibernate.service.internal.SessionFactoryServiceRegistryFactoryInitiator;
import org.hibernate.service.spi.ServiceContributor;
import org.hibernate.tool.hbm2ddl.ImportSqlCommandExtractorInitiator;

/**
 * Builder for standard {@link org.hibernate.service.ServiceRegistry} instances.
 *
 * @author Steve Ebersole
 * 
 * @see StandardServiceRegistryImpl
 * @see org.hibernate.boot.registry.BootstrapServiceRegistryBuilder
 */
public class StandardServiceRegistryBuilder {
	/**
	 * The default resource name for a hibernate configuration xml file.
	 */
	public static final String DEFAULT_CFG_RESOURCE_NAME = "hibernate.cfg.xml";

	private final Map settings;
	private final List<StandardServiceInitiator> initiators = standardInitiatorList();
	private final List<ProvidedService> providedServices = new ArrayList<ProvidedService>();

	private boolean autoCloseRegistry = true;

	private final BootstrapServiceRegistry bootstrapServiceRegistry;
	private final ConfigLoader configLoader;

	/**
	 * Create a default builder.
	 */
	public StandardServiceRegistryBuilder() {
		this( new BootstrapServiceRegistryBuilder().build() );
	}

	/**
	 * Create a builder with the specified bootstrap services.
	 *
	 * @param bootstrapServiceRegistry Provided bootstrap registry to use.
	 */
	public StandardServiceRegistryBuilder(BootstrapServiceRegistry bootstrapServiceRegistry) {
		this.settings = Environment.getProperties();
		this.bootstrapServiceRegistry = bootstrapServiceRegistry;
		this.configLoader = new ConfigLoader( bootstrapServiceRegistry );
	}

	/**
	 * Used from the {@link #initiators} variable initializer
	 *
	 * @return List of standard initiators
	 */
	private static List<StandardServiceInitiator> standardInitiatorList() {
		final List<StandardServiceInitiator> initiators = new ArrayList<StandardServiceInitiator>();
		initiators.addAll( StandardServiceInitiators.LIST );
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
			serviceInitiators.add( JdbcServicesInitiator.INSTANCE );
			serviceInitiators.add( RefCursorSupportInitiator.INSTANCE );
	
			serviceInitiators.add( MutableIdentifierGeneratorFactoryInitiator.INSTANCE);
	
			serviceInitiators.add( JtaPlatformResolverInitiator.INSTANCE );
			serviceInitiators.add( JtaPlatformInitiator.INSTANCE );
			serviceInitiators.add( TransactionFactoryInitiator.INSTANCE );
	
			serviceInitiators.add( SessionFactoryServiceRegistryFactoryInitiator.INSTANCE );
	
			serviceInitiators.add( RegionFactoryInitiator.INSTANCE );
		 */
		return initiators;
	}

	public BootstrapServiceRegistry getBootstrapServiceRegistry() {
		return bootstrapServiceRegistry;
	}

	/**
	 * Read settings from a {@link java.util.Properties} file by resource name.
	 *
	 * Differs from {@link #configure()} and {@link #configure(String)} in that here we expect to read a
	 * {@link java.util.Properties} file while for {@link #configure} we read the XML variant.
	 *
	 * @param resourceName The name by which to perform a resource look up for the properties file.
	 *
	 * @return this, for method chaining
	 *
	 * @see #configure()
	 * @see #configure(String)
	 */
	@SuppressWarnings( {"unchecked"})
	public StandardServiceRegistryBuilder loadProperties(String resourceName) {
		settings.putAll( configLoader.loadProperties( resourceName ) );
		return this;
	}

	/**
	 * Read setting information from an XML file using the standard resource location.
	 *
	 * @return this, for method chaining
	 *
	 * @see #DEFAULT_CFG_RESOURCE_NAME
	 * @see #configure(String)
	 * @see #loadProperties(String)
	 */
	public StandardServiceRegistryBuilder configure() {
		return configure( DEFAULT_CFG_RESOURCE_NAME );
	}

	/**
	 * Read setting information from an XML file using the named resource location.
	 *
	 * @param resourceName The named resource
	 *
	 * @return this, for method chaining
	 *
	 * @see #loadProperties(String)
	 */
	@SuppressWarnings( {"unchecked"})
	public StandardServiceRegistryBuilder configure(String resourceName) {
		final JaxbHibernateConfiguration configurationElement = configLoader.loadConfigXmlResource( resourceName );
		for ( JaxbHibernateConfiguration.JaxbSessionFactory.JaxbProperty xmlProperty : configurationElement.getSessionFactory().getProperty() ) {
			settings.put( xmlProperty.getName(), xmlProperty.getValue() );
		}

		return this;
	}

	/**
	 * Apply a setting value.
	 *
	 * @param settingName The name of the setting
	 * @param value The value to use.
	 *
	 * @return this, for method chaining
	 */
	@SuppressWarnings( {"unchecked", "UnusedDeclaration"})
	public StandardServiceRegistryBuilder applySetting(String settingName, Object value) {
		settings.put( settingName, value );
		return this;
	}

	/**
	 * Apply a groups of setting values.
	 *
	 * @param settings The incoming settings to apply
	 *
	 * @return this, for method chaining
	 */
	@SuppressWarnings( {"unchecked", "UnusedDeclaration"})
	public StandardServiceRegistryBuilder applySettings(Map settings) {
		this.settings.putAll( settings );
		return this;
	}

	/**
	 * Adds a service initiator.
	 *
	 * @param initiator The initiator to be added
	 *
	 * @return this, for method chaining
	 */
	@SuppressWarnings( {"UnusedDeclaration"})
	public StandardServiceRegistryBuilder addInitiator(StandardServiceInitiator initiator) {
		initiators.add( initiator );
		return this;
	}

	/**
	 * Adds a user-provided service.
	 *
	 * @param serviceRole The role of the service being added
	 * @param service The service implementation
	 *
	 * @return this, for method chaining
	 */
	@SuppressWarnings( {"unchecked"})
	public StandardServiceRegistryBuilder addService(final Class serviceRole, final Service service) {
		providedServices.add( new ProvidedService( serviceRole, service ) );
		return this;
	}

	/**
	 * By default, when a ServiceRegistry is no longer referenced by any other
	 * registries as a parent it will be closed.
	 * <p/>
	 * Some applications that explicitly build "shared registries" may want to
	 * circumvent that behavior.
	 * <p/>
	 * This method indicates that the registry being built should not be
	 * automatically closed.  The caller agrees to take responsibility to
	 * close it themselves.
	 *
	 * @return this, for method chaining
	 */
	public StandardServiceRegistryBuilder disableAutoClose() {
		this.autoCloseRegistry = false;
		return this;
	}

	/**
	 * See the discussion on {@link #disableAutoClose}.  This method enables
	 * the auto-closing.
	 *
	 * @return this, for method chaining
	 */
	public StandardServiceRegistryBuilder enableAutoClose() {
		this.autoCloseRegistry = true;
		return this;
	}

	/**
	 * Build the StandardServiceRegistry.
	 *
	 * @return The StandardServiceRegistry.
	 */
	@SuppressWarnings("unchecked")
	public StandardServiceRegistry build() {
		final Map<?,?> settingsCopy = new HashMap();
		settingsCopy.putAll( settings ); // settings 是hibernate.cfg.xml配置文件中的<property>配置信息
		Environment.verifyProperties( settingsCopy );
		ConfigurationHelper.resolvePlaceHolders( settingsCopy );

		/**
		 	使用IntegratorService服务
		 		查找Integrators，并应用 xxx.prepareServices(this)
		 */
		applyServiceContributingIntegrators();
		
		/**
		 	使用ClassLoaderService服务
		 		查找ServiceContributor服务，并应用 serviceContributor.contribute( this );
		 */
		applyServiceContributors();

		/**
		 	
		 */
		return new StandardServiceRegistryImpl(
				autoCloseRegistry,
				bootstrapServiceRegistry,// BootstrapServiceRegistryImpl
				initiators,// == standardInitiatorList();一大堆列表 !!!!!!!!!!!!!
				providedServices,// === new ArrayList<ProvidedService>();
				settingsCopy // 拷贝的属性
		);
	}

	/**
	 
	 */
	@SuppressWarnings("deprecation")
	private void applyServiceContributingIntegrators() {
		/**
				bootstrapServiceRegistry === org.hibernate.boot.registry.internal.BootstrapServiceRegistryImpl
				
				bootstrapServiceRegistry.getService( IntegratorService.class ) === org.hibernate.integrator.internal.IntegratorServiceImpl
				
				getIntegrators() = {
					org.hibernate.secure.spi.JaccIntegrator,
					org.hibernate.cache.internal.CollectionCacheInvalidator,
					org.hibernate.cfg.beanvalidation.BeanValidationIntegrator
				}
				
		 */
		for ( Integrator integrator : bootstrapServiceRegistry.getService( IntegratorService.class ).getIntegrators() ) {
			if ( org.hibernate.integrator.spi.ServiceContributingIntegrator.class.isInstance( integrator ) ) {
				org.hibernate.integrator.spi.ServiceContributingIntegrator.class.cast( integrator ).prepareServices( this );
			}
		}
	}

	/**
	 	在类路径下查找实现接口TypeContributor的类列表,并调用其中的contribute方法
	 */
	private void applyServiceContributors() {
		/**
		 		bootstrapServiceRegistry === BootstrapServiceRegistryImpl
		 		bootstrapServiceRegistry.getService( ClassLoaderService.class ) === org.hibernate.boot.registry.classloading.internal.ClassLoaderServiceImpl
		 */
		final LinkedHashSet<ServiceContributor> serviceContributors =
				bootstrapServiceRegistry.getService( ClassLoaderService.class )
						.loadJavaServices( ServiceContributor.class ); // 在类路径上查找实现ServiceContributor接口类，并调用空构造函数进行实例化

		
		for ( ServiceContributor serviceContributor : serviceContributors ) {
			serviceContributor.contribute( this );
		}
	}

	/**
	 * Temporarily exposed since Configuration is still around and much code still uses Configuration.  This allows
	 * code to configure the builder and access that to configure Configuration object (used from HEM atm).
	 *
	 * @return The settings map.
	 *
	 * @deprecated Temporarily exposed since Configuration is still around and much code still uses Configuration.
	 * This allows code to configure the builder and access that to configure Configuration object (used from HEM atm).
	 */
	@Deprecated
	public Map getSettings() {
		return settings;
	}

	/**
	 * Destroy a service registry.  Applications should only destroy registries they have explicitly created.
	 *
	 * @param serviceRegistry The registry to be closed.
	 */
	public static void destroy(ServiceRegistry serviceRegistry) {
		( (StandardServiceRegistryImpl) serviceRegistry ).destroy();
	}
}
