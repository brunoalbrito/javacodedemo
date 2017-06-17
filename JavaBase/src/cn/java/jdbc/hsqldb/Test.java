package cn.java.jdbc.hsqldb;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.Statement;
import java.util.Properties;

import org.hsqldb.jdbcDriver;

public class Test {

	public static void main(String[] args) throws Exception {
		Class.forName("org.hsqldb.jdbcDriver");
		
		Properties props = new Properties();
		props.setProperty("user", "sa");
		props.setProperty("password", "");
		Driver driver = new jdbcDriver();
		Connection connection = driver.connect("jdbc:hsqldb:mem:databaseName0", props);
		if(false){
			connection.setCatalog("catalog");
			connection.setSchema("schema");
		}
		DatabaseMetaData metaData = connection.getMetaData();
		{
			System.out.println("----Driver info------");
			System.out.println("getDriverName : " + metaData.getDriverName());
			System.out.println("getDriverVersion : " + metaData.getDriverVersion());
			System.out.println("getDriverMajorVersion : " + metaData.getDriverMajorVersion());
			System.out.println("getDriverMinorVersion : " + metaData.getDriverMinorVersion());
		}
		if(false){
			Statement stmt = connection.createStatement();
			String statement = "select * from table;";
			stmt.execute(statement);
			int rowsAffected = stmt.getUpdateCount();
			stmt.close();
		}
	}

}
