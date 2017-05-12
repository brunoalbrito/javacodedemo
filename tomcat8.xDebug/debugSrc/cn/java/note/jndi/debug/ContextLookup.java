package cn.java.note.jndi.debug;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Savepoint;
import java.util.Hashtable;

import javax.naming.CompositeName;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.Reference;
import javax.naming.spi.NamingManager;
import javax.naming.spi.ObjectFactory;

import org.apache.naming.NamingContext;
import org.apache.naming.ResourceRef;
import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

public class ContextLookup {

	public static void main(String[] args) throws Exception {
	}

	public static void lookup() throws Exception {
//    	<Resource  
//        name="jndi/mybatis"  
//        type="javax.sql.DataSource"  
//        driverClassName="com.mysql.jdbc.Driver"  
//        maxIdle="2"  
//        maxWait="5000"  
//        username="root"  
//        password="123456"  
//        url="jdbc:mysql://localhost:3306/appdb"  
//        maxActive="4"/>  
		
		Reference ref = new ResourceRef("javax.sql.DataSource", "description", "Scope", "Auth", false);
		Hashtable<String, Object> contextEnv = new Hashtable<>();
		Name name = new CompositeName("jndi/mybatis");
		Context namingContext = new NamingContext(contextEnv, "/Catalina/localhost/webapp1");
		Object obj = NamingManager.getObjectInstance(ref, name, namingContext, contextEnv); // 创建对象
		// -----------
		{
//			 ref.getFactoryClassName() === org.apache.naming.factory.ResourceFactory
//			 NamingManager.getObjectFactoryFromReference(ref, factoryClassName);
			String factoryClassName = "org.apache.naming.factory.ResourceFactory";
			Class factoryClass = Class.forName(factoryClassName);
			ObjectFactory factory = (ObjectFactory) factoryClass.newInstance();
			Object instance	= factory.getObjectInstance(ref, name, namingContext, contextEnv);
		
			String javaxSqlDataSourceFactoryClassName = "org.apache.tomcat.dbcp.dbcp2.BasicDataSourceFactory";
			factory = (ObjectFactory) Class.forName(javaxSqlDataSourceFactoryClassName).newInstance();
			factory.getObjectInstance(ref, name, namingContext, contextEnv);
		}
		
		{
			// org.apache.tomcat.dbcp.dbcp2.BasicDataSourceFactory.createDataSource()
			final BasicDataSource dataSource = new org.apache.tomcat.dbcp.dbcp2.BasicDataSource();
			dataSource.setDefaultAutoCommit(false);
			dataSource.setDefaultReadOnly(false);
			dataSource.setDefaultTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
			dataSource.setDriverClassName("com.mysql.jdbc.Driver");
			dataSource.setPassword("123456");
			dataSource.setUrl("jdbc:mysql://localhost:3306/appdb");
			dataSource.setUsername("username");
			
			// org.apache.tomcat.dbcp.dbcp2.BasicDataSource.getConnection()
			Connection connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			PreparedStatement preparedStatement = connection.prepareStatement("select id,username from table1 Where id = ?");
			preparedStatement.setInt(0, 1);
			preparedStatement.execute();
			ResultSet resultSet = preparedStatement.getResultSet();
			while(resultSet.next()){
				resultSet.getInt("id");
				resultSet.getString("username");
			}
			
			
			connection.setAutoCommit(false); // 开启事务
			Savepoint savepoint = connection.setSavepoint(); // 开启子事务
			connection.releaseSavepoint(savepoint); // 释放保存点
			connection.rollback(savepoint);// 回滚子事务
			connection.commit(); // 提交事务
			connection.rollback();// 回滚事务
		}
		
		
		

	}

}
