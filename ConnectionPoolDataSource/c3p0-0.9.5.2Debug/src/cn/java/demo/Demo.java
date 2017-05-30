package cn.java.demo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class Demo {

	public static void main(String[] args) throws Exception {
		ComboPooledDataSource dataSource = new ComboPooledDataSource();
		dataSource.setDriverClass("com.mysql.jdbc.Driver");
		dataSource.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/rap_db?useSSL=true&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true");
		dataSource.setUser("root");
		dataSource.setPassword("123456");
		dataSource.setMaxPoolSize(5);
		dataSource.setMinPoolSize(5);
		dataSource.setInitialPoolSize(2);
		dataSource.setMaxIdleTime(5);
		dataSource.setAcquireIncrement(3); // 尝试次数
		
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

}
