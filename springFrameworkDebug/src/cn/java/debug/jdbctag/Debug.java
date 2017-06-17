package cn.java.debug.jdbctag;

import org.springframework.jdbc.datasource.SimpleDriverDataSource;

public class Debug {

	/*
	 	org.springframework.jdbc.config.JdbcNamespaceHandler
	 	
	 	id0 = org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseFactoryBean
		{
			databaseName : "databaseName0"
		}

		id0 = org.springframework.jdbc.datasource.init.DataSourceInitializer
		{
			dataSource : "dataSource0"
			enabled : true
		}
		org.springframework.jdbc.datasource.init.CompositeDatabasePopulator
		{
			populators : [
				org.springframework.jdbc.datasource.init.ResourceDatabasePopulator
				{
					ignoreFailedDrops : false,
					continueOnError : true,
					scripts : new  org.springframework.jdbc.config.SortedResourcesFactoryBean
					{
						locations : ["location0","location1"]
					}
					sqlScriptEncoding : "UTF-8",
				},
				org.springframework.jdbc.datasource.init.ResourceDatabasePopulator
				{
					ignoreFailedDrops : false,
					continueOnError : true,
					scripts : new  org.springframework.jdbc.config.SortedResourcesFactoryBean
					{
						locations : ["location0","location1"]
					}
					sqlScriptEncoding : "UTF-8",
				},
			]
		}

 		


	 */
	public static void main(String[] args) throws Exception {
		Class.forName("org.hsqldb.jdbcDriver");
		SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
//		dataSource.setDriverClass(org.hsqldb.jdbcDriver.class);
		dataSource.setUrl("jdbc:hsqldb:mem:databaseName0");
		dataSource.setUsername("sa");
		dataSource.setPassword("");
	}

}
