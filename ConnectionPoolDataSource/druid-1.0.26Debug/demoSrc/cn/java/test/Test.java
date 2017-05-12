package cn.java.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import com.alibaba.druid.pool.DruidDataSource;

public class Test {
	
	/*
	 * http://druid.io/
	 * https://github.com/alibaba/druid
	 * http://www.infoq.com/cn/news/2015/04/druid-data/
	 * http://www.iteye.com/magazines/90
	 * https://www.oschina.net/p/druid
	 */
	public static void main(String[] args) throws SQLException {
		DruidDataSource dataSource = new DruidDataSource();
		// 配置
		{
			dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/rap_db?useSSL=false&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true");
			dataSource.setUsername("root");
			dataSource.setPassword("123456");
			
//			dataSource.setFilters("stat,default,counter"); // 过滤器工具
			{
				dataSource.setFilters("stat"); // 过滤器工具
				// stat过滤器的配置方式 1
				{
					Properties properties = new Properties();
					properties.put("druid.stat.mergeSql", "false");
					properties.put("druid.stat.slowSqlMillis", "3000"); // 超过多少毫秒算慢查询
					properties.put("druid.stat.logSlowSql", "true"); // 记录慢查询SQL
					dataSource.setConnectProperties(properties);
				}
				
				// stat过滤器的配置方式 1
				{
					String connectionProperties = "druid.stat.mergeSql=false;druid.stat.slowSqlMillis=3000;druid.stat.logSlowSql=true";
					dataSource.setConnectionProperties(connectionProperties);
				}
			}
			
			dataSource.setMaxActive(20);
			dataSource.setInitialSize(1);
			dataSource.setMaxWait(60000);
			dataSource.setMinIdle(1);
			
			dataSource.setTimeBetweenEvictionRunsMillis(60000);
			dataSource.setMinEvictableIdleTimeMillis(300000);
			
			dataSource.setValidationQuery("SELECT 'x'");
			dataSource.setTestWhileIdle(true);
			dataSource.setTestOnBorrow(false);
			dataSource.setTestOnReturn(false);
			
			dataSource.setPoolPreparedStatements(true);
			dataSource.setMaxPoolPreparedStatementPerConnectionSize(50);
		}
		// 初始化
		{
			dataSource.init();
		}
		
		// 使用
		{
			Connection connection = dataSource.getConnection(); // 从连接池中获取一个对象， 如果配置了过滤器，此时是被包装后的com.alibaba.druid.proxy.jdbc.ConnectionProxyImpl
			connection.setAutoCommit(false);
			PreparedStatement preparedStatement =  connection.prepareStatement("select id AS userid,account from tb_user WHERE id > ?"); // com.alibaba.druid.proxy.jdbc.PreparedStatementProxyImpl
			preparedStatement.setInt(1, -1);
			if(preparedStatement.execute()){
				ResultSet resultSet = preparedStatement.getResultSet();
				while (resultSet.next()) {
					StringBuilder stringBuilder = new StringBuilder();
					stringBuilder.append("userid = ");
					stringBuilder.append(resultSet.getString(1));
					stringBuilder.append(" , account = ");
					stringBuilder.append(resultSet.getString(2));
					System.out.println(stringBuilder);
					
				}
			}
			connection.commit();
			connection.rollback();
		}
		
		try {
			Thread.currentThread().sleep(1000*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// 关闭
		{
			dataSource.close();
		}
	}

	/*
 	    <bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource" init-method="init" destroy-method="close">   
	        <property name="url" value="${jdbc_url}" />  
	        <property name="username" value="${jdbc_user}" />  
	        <property name="password" value="${jdbc_password}" />  
	           
	        <property name="filters" value="stat" />  
	       
	        <property name="maxActive" value="20" />  
	        <property name="initialSize" value="1" />  
	        <property name="maxWait" value="60000" />  
	        <property name="minIdle" value="1" />  
	       
	        <property name="timeBetweenEvictionRunsMillis" value="60000" />  
	        <property name="minEvictableIdleTimeMillis" value="300000" />  
	       
	        <property name="validationQuery" value="SELECT 'x'" />  
	        <property name="testWhileIdle" value="true" />  
	        <property name="testOnBorrow" value="false" />  
	        <property name="testOnReturn" value="false" />  
	           
	        <property name="poolPreparedStatements" value="true" />  
	        <property name="maxPoolPreparedStatementPerConnectionSize" value="50" />  
	    </bean>  
	 */

}
