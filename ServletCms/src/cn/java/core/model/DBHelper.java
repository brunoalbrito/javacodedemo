package cn.java.core.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 测试可行
 * 
 * 单利模式
 * 
 * 处理连接的打开和关闭
 * 
 * @author Administrator
 *
 */
public class DBHelper {
	protected static String tableNamePrefix = "bizcn_";
	protected static String dbConnectUrl = "jdbc:mysql://localhost:3306/cms_servlet?user=root&password=&useUnicode=true&characterEncoding=UTF8";
	protected static String driverName = "com.mysql.jdbc.Driver";

	public static void loadDriver() throws ClassNotFoundException {
		loadDriver(driverName);
	}

	public static void loadDriver(String driverName) throws ClassNotFoundException {
		String className = driverName;// "com.mysql.jdbc.Driver";
		try {
			Class.forName(className);
		} catch (ClassNotFoundException e) {
			System.out.println("驱动加载失败...");
			throw e;
		}
	}

	public static Connection getConnect(String url) throws Exception {
		return getConnect(url, null, null);
	}

	public static Connection getConnect() throws Exception {
		return getConnect(dbConnectUrl);
	}

	public static Connection getConnect(String url, String username, String password) throws Exception {
		Connection connection = null;
		try {
			if (username == null || username.equals("")) {
				//url === jdbc:mysql://localhost:3306/pinphpdemo?user=root&password=&useUnicode=true&characterEncoding=UTF8
				connection = DriverManager.getConnection(url);
			} else {
				connection = DriverManager.getConnection(url, username, password);
			}
			System.out.println("连接数据库成功...");
			return connection;
		} catch (Exception e) {
			System.out.println("连接数据库失败...");
			throw e;
		}
	}

	public static String normalTableName(String tableName) {
		return tableNamePrefix + tableName;
	}

	/**
	 * 关闭连接
	 * 
	 * @param connection
	 */
	public static void close(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				System.out.println("连接关闭失败...");
				e.printStackTrace();
			}
			connection = null;
		}
	}

	public static void close(Statement statement, Connection connection) {
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				System.out.println("statement关闭失败...");
				e.printStackTrace();
			}
			statement = null;
		}
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				System.out.println("连接关闭失败...");
				e.printStackTrace();
			}
			connection = null;
		}
	}

	public static void close(ResultSet resultSet, Statement statement) {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				System.out.println("resultSet关闭失败...");
				e.printStackTrace();
			}
			resultSet = null;
		}
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				System.out.println("statement关闭失败...");
				e.printStackTrace();
			}
			statement = null;
		}
	}

	/**
	 * 关闭资源
	 * 
	 * @param resultSet
	 * @param statement
	 * @param connection
	 */
	public static void close(ResultSet resultSet, Statement statement, Connection connection) {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				System.out.println("resultSet关闭失败...");
				e.printStackTrace();
			}
			resultSet = null;
		}
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				System.out.println("statement关闭失败...");
				e.printStackTrace();
			}
			statement = null;
		}
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				System.out.println("连接关闭失败...");
				e.printStackTrace();
			}
			connection = null;
		}
	}

}
