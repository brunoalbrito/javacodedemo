package cn.java.demo.data_jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test {
	
	public static void main(String[] args) throws Exception {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"classpath:cn/java/demo/data_jdbc/applicationContext.xml");
		
		{
			String databaseName = "dbname0";
			DataSource dataSource = context.getBean("embeddedDataSource0",DataSource.class);//　消息模板
			Connection connection = dataSource.getConnection();
			DatabaseMetaData metaData = connection.getMetaData();
			
			{
				System.out.println("----Driver info------");
				System.out.println("getDriverName : " + metaData.getDriverName());
				System.out.println("getDriverVersion : " + metaData.getDriverVersion());
				System.out.println("getDriverMajorVersion : " + metaData.getDriverMajorVersion());
				System.out.println("getDriverMinorVersion : " + metaData.getDriverMinorVersion());
				System.out.println("connection.getSchema().toString() = " + connection.getSchema().toString());
				System.out.println("connection.getCatalog().toString() = " + connection.getCatalog().toString());
			}
			
			{
				Statement statement = connection.createStatement();
				
				//　添加一条数据
				{
					String sql = "insert into table_full values(1,'username1','email0@qq.com');";
					statement.executeUpdate(sql);  
					System.out.println("statement.getUpdateCount() = " + statement.getUpdateCount());
				}
				
				// 查询数据
				{
					String sql = "select * from table_full;";
					ResultSet resultSet = statement.executeQuery(sql);
					
					printTableHeader(resultSet);
					printTableDataRows(resultSet);
					
					resultSet.close();
				}
				{
					
					String sql = "SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME LIKE 'TBPREFIX_%';";
					ResultSet resultSet = statement.executeQuery(sql);
					printTableHeader(resultSet);
					printTableDataRows(resultSet);
					resultSet.close();
				}
				
//				{
//					ResultSet resultSet = statement.executeQuery("show tables;");
//					while(resultSet.next()) {
//			            //获取字段
//			            System.out.println(resultSet.getString("Tables_in_"+databaseName));
//			            System.out.println(resultSet.getString(0));
//			        }
//			        //关闭资源，最先打开的最后关
//					resultSet.close();
//				}
				
				statement.close();
			}
			connection.close();
		}
	}
	
	/**
	 * 打印表数据
	 * @param resultSet
	 * @throws SQLException
	 */
	private static void printTableDataRows(ResultSet resultSet) throws SQLException{
		int col = resultSet.getMetaData().getColumnCount();
		while (resultSet.next()) {
			StringBuilder stringBuilder = new StringBuilder();
			for (int i = 1; i <= col; i++) {
				if(i!=1){
					stringBuilder.append("\t");
				}
				stringBuilder.append(resultSet.getString(i));
			}
			System.out.println(stringBuilder);
		}
	}
	
	/**
	 * 打印表头
	 * @param resultSet
	 * @throws SQLException
	 */
	private static void printTableHeader(ResultSet resultSet) throws SQLException{
		ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
			if(i!=1){
				stringBuilder.append("\t");
			}
			stringBuilder.append(resultSetMetaData.getColumnName(i));
		}
		System.out.println(stringBuilder);
		System.out.println("---------------------------------------------");
	}
}
