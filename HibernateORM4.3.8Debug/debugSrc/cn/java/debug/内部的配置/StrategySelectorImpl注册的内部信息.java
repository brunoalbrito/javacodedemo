package cn.java.debug.内部的配置;

import org.hibernate.dialect.Dialect;


public class StrategySelectorImpl注册的内部信息 {
	
		/*
				org.hibernate.boot.registry.selector.internal.StrategySelectorImpl
				
				
		 		org.hibernate.boot.registry.selector.internal.StrategySelectorBuilder.addDialect(...)
		 		

		 		// strategySelector.registerStrategyImplementor( Dialect.class, ... );
		 		addDialect( strategySelector, Cache71Dialect.class );
                addDialect( strategySelector, CUBRIDDialect.class );
                addDialect( strategySelector, DB2Dialect.class );
                addDialect( strategySelector, DB2390Dialect.class );
                addDialect( strategySelector, DB2400Dialect.class );
                addDialect( strategySelector, DerbyTenFiveDialect.class );
                addDialect( strategySelector, DerbyTenSixDialect.class );
                addDialect( strategySelector, DerbyTenSevenDialect.class );
                addDialect( strategySelector, FirebirdDialect.class );
                addDialect( strategySelector, FrontBaseDialect.class );
                addDialect( strategySelector, H2Dialect.class );
                addDialect( strategySelector, HSQLDialect.class );
                addDialect( strategySelector, InformixDialect.class );
                addDialect( strategySelector, IngresDialect.class );
                addDialect( strategySelector, Ingres9Dialect.class );
                addDialect( strategySelector, Ingres10Dialect.class );
                addDialect( strategySelector, InterbaseDialect.class );
                addDialect( strategySelector, JDataStoreDialect.class );
                addDialect( strategySelector, MckoiDialect.class );
                addDialect( strategySelector, MimerSQLDialect.class );
//                strategySelector.registerStrategyImplementor( Dialect.class, "MySQL5", MySQL5Dialect.class );
//                strategySelector.registerStrategyImplementor( Dialect.class, "MySQL5InnoDB", MySQL5InnoDBDialect.class );
                addDialect( strategySelector, MySQL5Dialect.class );
                addDialect( strategySelector, MySQL5InnoDBDialect.class );
                addDialect( strategySelector, MySQL5Dialect.class );
                addDialect( strategySelector, MySQL5InnoDBDialect.class );
                addDialect( strategySelector, Oracle8iDialect.class );
                addDialect( strategySelector, Oracle9iDialect.class );
                addDialect( strategySelector, Oracle10gDialect.class );
                addDialect( strategySelector, PointbaseDialect.class );
                addDialect( strategySelector, PostgresPlusDialect.class );
                addDialect( strategySelector, PostgreSQL81Dialect.class );
                addDialect( strategySelector, PostgreSQL82Dialect.class );
                addDialect( strategySelector, PostgreSQL9Dialect.class );
                addDialect( strategySelector, ProgressDialect.class );
                addDialect( strategySelector, SAPDBDialect.class );
                addDialect( strategySelector, SQLServerDialect.class );
                addDialect( strategySelector, SQLServer2005Dialect.class );
                addDialect( strategySelector, SQLServer2008Dialect.class );
                addDialect( strategySelector, Sybase11Dialect.class );
                addDialect( strategySelector, SybaseAnywhereDialect.class );
                addDialect( strategySelector, SybaseASE15Dialect.class );
                addDialect( strategySelector, SybaseASE157Dialect.class );
                addDialect( strategySelector, TeradataDialect.class );
                addDialect( strategySelector, TimesTenDialect.class );
				---------------------------------
				// strategySelector.registerStrategyImplementor( JtaPlatform.class, ... );
				
				addJtaPlatforms(
                                strategySelector,
                                BorlandEnterpriseServerJtaPlatform.class,
                                "Borland",
                                "org.hibernate.service.jta.platform.internal.BorlandEnterpriseServerJtaPlatform"
                );

                addJtaPlatforms(
                                strategySelector,
                                BitronixJtaPlatform.class,
                                "Bitronix",
                                "org.hibernate.service.jta.platform.internal.BitronixJtaPlatform"
                );

                addJtaPlatforms(
                                strategySelector,
                                JBossAppServerJtaPlatform.class,
                                "JBossAS",
                                "org.hibernate.service.jta.platform.internal.JBossAppServerJtaPlatform"
                );

                addJtaPlatforms(
                                strategySelector,
                                JBossStandAloneJtaPlatform.class,
                                "JBossTS",
                                "org.hibernate.service.jta.platform.internal.JBossStandAloneJtaPlatform"
                );

                addJtaPlatforms(
                                strategySelector,
                                JOnASJtaPlatform.class,
                                "JOnAS",
                                "org.hibernate.service.jta.platform.internal.JOnASJtaPlatform"
                );

                addJtaPlatforms(
                                strategySelector,
                                JOTMJtaPlatform.class,
                                "JOTM",
                                "org.hibernate.service.jta.platform.internal.JOTMJtaPlatform"
                );

                addJtaPlatforms(
                                strategySelector,
                                JRun4JtaPlatform.class,
                                "JRun4",
                                "org.hibernate.service.jta.platform.internal.JRun4JtaPlatform"
                );

                addJtaPlatforms(
                                strategySelector,
                                OC4JJtaPlatform.class,
                                "OC4J",
                                "org.hibernate.service.jta.platform.internal.OC4JJtaPlatform"
                );

                addJtaPlatforms(
                                strategySelector,
                                OrionJtaPlatform.class,
                                "Orion",
                                "org.hibernate.service.jta.platform.internal.OrionJtaPlatform"
                );

                addJtaPlatforms(
                                strategySelector,
                                ResinJtaPlatform.class,
                                "Resin",
                                "org.hibernate.service.jta.platform.internal.ResinJtaPlatform"
                );

                addJtaPlatforms(
                                strategySelector,
                                SunOneJtaPlatform.class,
                                "SunOne",
                                "org.hibernate.service.jta.platform.internal.SunOneJtaPlatform"
                );

                addJtaPlatforms(
                                strategySelector,
                                WeblogicJtaPlatform.class,
                                "Weblogic",
                                "org.hibernate.service.jta.platform.internal.WeblogicJtaPlatform"
                );

                addJtaPlatforms(
                                strategySelector,
                                WebSphereJtaPlatform.class,
                                "WebSphere",
                                "org.hibernate.service.jta.platform.internal.WebSphereJtaPlatform"
                );

                addJtaPlatforms(
                                strategySelector,
                                WebSphereExtendedJtaPlatform.class,
                                "WebSphereExtended",
                                "org.hibernate.service.jta.platform.internal.WebSphereExtendedJtaPlatform"
                );
				---------------------------
				strategySelector.registerStrategyImplementor( TransactionFactory.class, JdbcTransactionFactory.SHORT_NAME, JdbcTransactionFactory.class );
                strategySelector.registerStrategyImplementor( TransactionFactory.class, "org.hibernate.transaction.JDBCTransactionFactory", JdbcTransactionFactory.class );

                strategySelector.registerStrategyImplementor( TransactionFactory.class, JtaTransactionFactory.SHORT_NAME, JtaTransactionFactory.class );
                strategySelector.registerStrategyImplementor( TransactionFactory.class, "org.hibernate.transaction.JTATransactionFactory", JtaTransactionFactory.class );

                strategySelector.registerStrategyImplementor( TransactionFactory.class, CMTTransactionFactory.SHORT_NAME, CMTTransactionFactory.class );
                strategySelector.registerStrategyImplementor( TransactionFactory.class, "org.hibernate.transaction.CMTTransactionFactory", CMTTransactionFactory.class );
				-----------------------------
				strategySelector.registerStrategyImplementor(
                                MultiTableBulkIdStrategy.class,
                                PersistentTableBulkIdStrategy.SHORT_NAME,
                                PersistentTableBulkIdStrategy.class
                );
                strategySelector.registerStrategyImplementor(
                                MultiTableBulkIdStrategy.class,
                                TemporaryTableBulkIdStrategy.SHORT_NAME,
                                TemporaryTableBulkIdStrategy.class
                );
				--------------------------------
				strategySelector.registerStrategyImplementor(
                                EntityCopyObserver.class,
                                EntityCopyNotAllowedObserver.SHORT_NAME,
                                EntityCopyNotAllowedObserver.class
                );
                strategySelector.registerStrategyImplementor(
                                EntityCopyObserver.class,
                                EntityCopyAllowedObserver.SHORT_NAME,
                                EntityCopyAllowedObserver.class
                );
                strategySelector.registerStrategyImplementor(
                                EntityCopyObserver.class,
                                EntityCopyAllowedLoggedObserver.SHORT_NAME,
                                EntityCopyAllowedLoggedObserver.class
                );

		 */

}
