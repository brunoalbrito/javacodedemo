package cn.java.debug.内部的配置;

import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.engine.jdbc.dialect.spi.DialectFactory;
import org.hibernate.engine.jdbc.spi.JdbcServices;

public class StandardServiceRegistryImpl注册了大量内部信息 {
	/*
 	
	 	org.hibernate.boot.registry.internal.StandardServiceRegistryImpl
	
	 	serviceBindingMap = {
		 	serviceInitiators.add( ConfigurationServiceInitiator.INSTANCE );
			serviceInitiators.add( ImportSqlCommandExtractorInitiator.INSTANCE );
	
			serviceInitiators.add( JndiServiceInitiator.INSTANCE );
			serviceInitiators.add( JmxServiceInitiator.INSTANCE );
	
			serviceInitiators.add( PersisterClassResolverInitiator.INSTANCE );
			serviceInitiators.add( PersisterFactoryInitiator.INSTANCE );
	
			serviceInitiators.add( ConnectionProviderInitiator.INSTANCE ); // ConnectionProvider.class
			serviceInitiators.add( MultiTenantConnectionProviderInitiator.INSTANCE );
			serviceInitiators.add( DialectResolverInitiator.INSTANCE );
			serviceInitiators.add( DialectFactoryInitiator.INSTANCE ); // DialectFactory.class
			serviceInitiators.add( BatchBuilderInitiator.INSTANCE );
			serviceInitiators.add( JdbcServicesInitiator.INSTANCE );  // JdbcServices.class
			serviceInitiators.add( RefCursorSupportInitiator.INSTANCE );
	
			serviceInitiators.add( MutableIdentifierGeneratorFactoryInitiator.INSTANCE);
	
			serviceInitiators.add( JtaPlatformResolverInitiator.INSTANCE );
			serviceInitiators.add( JtaPlatformInitiator.INSTANCE );
			serviceInitiators.add( TransactionFactoryInitiator.INSTANCE );
	
			serviceInitiators.add( SessionFactoryServiceRegistryFactoryInitiator.INSTANCE );
	
			serviceInitiators.add( RegionFactoryInitiator.INSTANCE );
	 	}
 */
}
