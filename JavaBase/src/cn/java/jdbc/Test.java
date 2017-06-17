package cn.java.jdbc;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


public class Test {

	/*
		CREATE TABLE `table1` (
			`field1`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
			`field2`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
			`field3`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL 
		)
		ENGINE=MyISAM
		DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
		CHECKSUM=0
		ROW_FORMAT=DYNAMIC
		DELAY_KEY_WRITE=0
		;
	 */
	/*
		数据库 - mysql
		{
			mysql.user 存放数据的“用户信息”（用户名、允许连接的IP、数据库服务器的权限、用户密码、最大连接数、用户的最大连接数）
			
			mysql.slave_master_info 存放数据的“订阅的主服务器信息”（master主机名、master用户名、master密码、master端口、尝试次数、master日志名、master日志偏移位置、行号）
			mysql.slave_relay_log_info存放salve的“同步信息”（master日志名、master日志偏移位置、slave日志名、slave日志偏移位置、行号）
			mysql.slave_worker_info 存放工作者的“同步信息”（master日志名、master日志偏移位置、slave日志名、slave日志偏移位置、行号）
			
			mysql.slow_log 存放“慢查询日志”（连接用户IP信息、开始时间、查询时间、锁定锁定时间、数据库名、执行SQL、服务器socketId）
		}
		
		数据库 - INFORMATION_SCHEMA
		{
			
			关于-服务器的配置状态
			{
				INFORMATION_SCHEMA.ENGINES 存放“存储引擎的原信息”（是否支持、是否支持事务、是否支持分布式事务、是否支持保存点savepoint）
			
				INFORMATION_SCHEMA.GLOBAL_VARIABLES 存放“全局”数据库“服务器的配置状态”
				INFORMATION_SCHEMA.SESSION_VARIABLES 存放“某个连接”数据库“服务器的配置状态”
				
				INFORMATION_SCHEMA.PLUGINS 存放“插件信息”（插件名、插件版本、插件状态、插件类型STORAGE ENGINE、插件类型版本、插件作者）
			}
			
			关于-服务器的运行状态
			{
				INFORMATION_SCHEMA.GLOBAL_STATUS 存放“全局”数据库“服务器的运行状态”
				INFORMATION_SCHEMA.SESSION_STATUS 存放“某个连接”数据库“服务器的运行状态”
				
				INFORMATION_SCHEMA.PROCESSLIST 存放“进程信息”（连接用户名、连接用户IP信息、数据库名、当前执行的SQL）
			}
			
			关于-数据库信息
			{
				INFORMATION_SCHEMA.SCHEMATA 存放“数据库的原信息”（数据库名、字符集、排序字符集）
				INFORMATION_SCHEMA.SCHEMA_PRIVILEGES 存放数据库的授权信息（授权给用户、数据库名、权限类型、是否授权）
				
				INFORMATION_SCHEMA.USER_PRIVILEGES 存放数据库服务器的授权信息（授权给用户、权限类型、是否授权）
			}
			
			关于-表信息
			{
				INFORMATION_SCHEMA.TABLES 存放“表的原信息”（所属数据库名、表名、使用引擎、行格式、排序字符集、行的格式、是否自增、已经使用索引长度、已经使用数据长度、允许使用最大数据长度、创建时间、注释...）
				INFORMATION_SCHEMA.TABLE_PRIVILEGES 存放数据库的授权信息（授权给用户、数据库名、表名、权限类型、是否授权）
				
				INFORMATION_SCHEMA.PARTITIONS 存放“分区信息”（所属数据库、所属表、分区名、子分区名、分区方式、分区表达式）
			}
			
			关于-列信息
			{
				INFORMATION_SCHEMA.COLUMNS 存放表的“列的原信息”（所属数据库名、所属表名、列名、数据类型、字符集、排序字符集、列类型、权限、注释...）
				
				INFORMATION_SCHEMA.KEY_COLUMN_USAGE 存放“主键信息”（数据库名、表名、字段名）
				
				INFORMATION_SCHEMA.STATISTICS 存放“索引信息（主键也会添加一条）”（数据库名、表名、索引哪个数据库、索引哪个字段、字段索引名、索引类型）
			}
			
		}
	 */

	/**
	 * 
	 * @param args
	 */
	@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
	public static void main(String[] args) {
		
		/**
		 *  预置语句与statement类的主要区别在于，前者可以只编译和优化一次，然后通过设置不同的参数值多次使用。 
		 */
		try {
			Connection connection = ConnectionUtil.getConnection();
			ConnectionUtil.printConnectionMetaData(connection);
			ConnectionUtil.printConnectionInfo(connection);
			
			System.out.println("------------StatementTest--------------------");
			StatementTest.insert(connection, new HashMap() {
				{
					put("field1", "id1");
					put("field2", "field2Value Statement");
					put("field3", "field3Value Statement");
				}
			});
			
			StatementTest.update(connection, new HashMap() {
				{
					put("field1", "id1");
					put("field2", "field2ValueNew Statement");
				}
			});
			StatementTest.selectAll(connection);
			StatementTest.delete(connection, "id1");

			System.out.println("------------PreparedStatementTest--------------------");
			PreparedStatementTest.insert(connection, new HashMap() {
				{
					put("field1", "id1");
					put("field2", "field2Value PreparedStatement");
					put("field3", "field3Value PreparedStatement");
				}
			});
			PreparedStatementTest.update(connection, new HashMap() {
				{
					put("field1", "id1");
					put("field2", "field2ValueNew PreparedStatement");
				}
			});
			PreparedStatementTest.selectAll(connection);
			PreparedStatementTest.selectAll2Step(connection);
			PreparedStatementTest.delete(connection, "id1");

			connection.close();
		} catch (Exception e) {
		}

	}

	private static class ConnectionUtil {
		private static Connection connection;

		public static Connection getConnection() {
			if (connection == null) {
				// 加载MySql的驱动类
				try {
					Class.forName("com.mysql.jdbc.Driver");
				} catch (ClassNotFoundException e) {
					System.out.println("找不到驱动程序类 ，加载驱动失败！");
					e.printStackTrace();
				}
				// 连接MySql数据库
				String url = "jdbc:mysql://127.0.0.1:3306/rap_db?useSSL=true&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true";
				String username = "root";
				String password = "123456";
				try {
					connection = DriverManager.getConnection(url, username, password);
				} catch (SQLException se) {
					System.out.println("数据库连接失败！");
					se.printStackTrace();
				}
			}
			return connection;
		}
		
		public static void printConnectionInfo(Connection connection) throws SQLException{
			System.out.println("   <<<   printConnectionInfo bof   >>>   ");
			System.out.println("getTransactionIsolation : " + connection.getTransactionIsolation());
			System.out.println("   <<<   printConnectionInfo eof   >>>   ");
		}
		
		/**
		 * 打印连接的原信息
		 * @param connection
		 * @throws SQLException
		 */
		public static void printConnectionMetaData(Connection connection) throws SQLException{
			System.out.println("   <<<   printConnectionMetaData bof   >>>   ");
			DatabaseMetaData metaData = connection.getMetaData();
			
			{
				System.out.println("----Database info------");
				System.out.println("getDatabaseProductName : " + metaData.getDatabaseProductName());
				System.out.println("getDatabaseProductVersion : " + metaData.getDatabaseProductVersion());
				System.out.println("getDatabaseMajorVersion : " + metaData.getDatabaseMajorVersion());
				System.out.println("getDatabaseMinorVersion : " + metaData.getDatabaseMinorVersion());
				System.out.println("getMaxConnections : " + metaData.getMaxConnections());
			}
			
			{
				System.out.println("----Driver info------");
				System.out.println("getDriverName : " + metaData.getDriverName());
				System.out.println("getDriverVersion : " + metaData.getDriverVersion());
				System.out.println("getDriverMajorVersion : " + metaData.getDriverMajorVersion());
				System.out.println("getDriverMinorVersion : " + metaData.getDriverMinorVersion());
			}
			
			{
				System.out.println("----JDBC version  info------");
				System.out.println("getJDBCMajorVersion : " + metaData.getJDBCMajorVersion());
				System.out.println("getJDBCMinorVersion : " + metaData.getJDBCMinorVersion());
			}
			
			{
				boolean metaSupportsRefCursors = ConnectionMetaDataUtil.supportsRefCursors( metaData );
				boolean metaSupportsNamedParams = metaData.supportsNamedParameters();
				boolean metaSupportsScrollable = metaData.supportsResultSetType( ResultSet.TYPE_SCROLL_INSENSITIVE );
				boolean metaSupportsBatchUpdates = metaData.supportsBatchUpdates();
				boolean metaReportsDDLCausesTxnCommit = metaData.dataDefinitionCausesTransactionCommit();
				boolean metaReportsDDLInTxnSupported = !metaData.dataDefinitionIgnoredInTransactions();
				boolean metaSupportsGetGeneratedKeys = metaData.supportsGetGeneratedKeys();
				String extraKeywordsString = metaData.getSQLKeywords(); // 数据库关键词
				int sqlStateType = metaData.getSQLStateType();
				boolean lobLocatorUpdateCopy = metaData.locatorsUpdateCopy();
				
				// 数据库关键词
				{
					Set<String> keywordSet = new HashSet<String>();
					keywordSet.addAll( Arrays.asList( extraKeywordsString.split( "," ) ) );
				}
				
				{
					switch ( sqlStateType ) {
						case DatabaseMetaData.sqlStateSQL99 : {
							System.out.println("sqlStateSQL99");
							break;
						}
						case DatabaseMetaData.sqlStateXOpen : {
							System.out.println("sqlStateXOpen");
							break;
						}
						default : {
							System.out.println("default");
							break;
						}
					}
				}
			}
			
			{
				ResultSet resultSet = metaData.getTypeInfo();
				String strTemp = "";
				while ( resultSet.next() ) {
						strTemp = "";
						strTemp = strTemp + ", TYPE_NAME = " + resultSet.getString( "TYPE_NAME" );
						strTemp = strTemp + ", DATA_TYPE = " + resultSet.getInt( "DATA_TYPE" );
						strTemp = strTemp + ", CREATE_PARAMS = " + ConnectionMetaDataUtil.interpretCreateParams( resultSet.getString( "CREATE_PARAMS" ) );
						strTemp = strTemp + ", UNSIGNED_ATTRIBUTE = " + resultSet.getBoolean( "UNSIGNED_ATTRIBUTE" );
						strTemp = strTemp + ", PRECISION = " + resultSet.getInt( "PRECISION" );
						strTemp = strTemp + ", MINIMUM_SCALE = " + resultSet.getShort( "MINIMUM_SCALE" );
						strTemp = strTemp + ", MAXIMUM_SCALE = " + resultSet.getShort( "MAXIMUM_SCALE" );
						strTemp = strTemp + ", FIXED_PREC_SCALE = " + resultSet.getBoolean( "FIXED_PREC_SCALE" );
						strTemp = strTemp + ", LITERAL_PREFIX = " + resultSet.getString( "LITERAL_PREFIX" );
						strTemp = strTemp + ", LITERAL_SUFFIX = " + resultSet.getString( "LITERAL_SUFFIX" );
						strTemp = strTemp + ", CASE_SENSITIVE = " + resultSet.getBoolean( "CASE_SENSITIVE" );
						strTemp = strTemp + ", SEARCHABLE = " + ConnectionMetaDataUtil.typeSearchabilityInterpret( resultSet.getShort( "SEARCHABLE" ) );
						strTemp = strTemp + ", NULLABLE = " + ConnectionMetaDataUtil.typeNullabilityInterpret( resultSet.getShort( "NULLABLE" ) );
						System.out.println("getTypeInfo ---> " + strTemp);
				}
				
				{
					String catalogName = connection.getCatalog(); // 数据库名
					try {
						boolean useContextualLobCreation = ConnectionMetaDataUtil.useContextualLobCreation(connection);
					} catch (NoSuchMethodException | SecurityException e) {
						e.printStackTrace();
					}
					
				}
			}
			System.out.println("   <<<   printConnectionMetaData eof   >>>   ");
		}
		

		private static class ConnectionMetaDataUtil {
			private static boolean useContextualLobCreation(Connection connection) throws SQLException, NoSuchMethodException, SecurityException {
				final DatabaseMetaData metaData = connection.getMetaData();
				if ( metaData.getJDBCMajorVersion() < 4 ) {
					return false;
				}
				Class connectionClass = Connection.class;
				Method createClobMethod = connectionClass.getMethod( "createClob", new Class[0] );
				if ( createClobMethod.getDeclaringClass().equals( Connection.class ) ) {
					try {
						Object clob = createClobMethod.invoke( connection, new Object[0] );
						try {
							Method freeMethod = clob.getClass().getMethod( "free", new Class[0] );
							freeMethod.invoke( clob, new Object[0] );
						}
						catch ( Throwable ignore ) {
							
						}
						return true;
					}
					catch ( Throwable ignore ) {
						
					}
				}
				return false;
			}
			public static String typeNullabilityInterpret(short code) {
				switch ( code ) {
					case DatabaseMetaData.typeNullable: {
						return "NULLABLE";
					}
					case DatabaseMetaData.typeNoNulls: {
						return "NON_NULLABLE";
					}
					case DatabaseMetaData.typeNullableUnknown: {
						return "UNKNOWN";
					}
					default: {
						throw new IllegalArgumentException( "Unknown type nullability code [" + code + "] enountered" );
					}
				}
			}
			
			public static String typeSearchabilityInterpret(short code) {
				switch ( code ) {
					case DatabaseMetaData.typeSearchable: {
						return "FULL";
					}
					case DatabaseMetaData.typePredNone: {
						return "NONE";
					}
					case DatabaseMetaData.typePredBasic: {
						return "BASIC";
					}
					case DatabaseMetaData.typePredChar: {
						return "CHAR";
					}
					default: {
						throw new IllegalArgumentException( "Unknown type searchability code [" + code + "] enountered" );
					}
				}
			}
			
			public static final String[] EMPTY_STRING_ARRAY = {};
			private static String[] interpretCreateParams(String value) {
				if ( value == null || value.length() == 0 ) {
					return EMPTY_STRING_ARRAY;
				}
				return value.split( "," );
			}
			
			public static boolean supportsRefCursors(DatabaseMetaData meta) {
				// Standard JDBC REF_CURSOR support was not added until Java 8, so we need to use reflection to attempt to
				// access these fields/methods...
				try {
					return (Boolean) meta.getClass().getMethod( "supportsRefCursors" ).invoke( null );
				}
				catch (NoSuchMethodException e) {
				}
				catch (Exception e) {
				}
				return false;
			}
		}
		
		
		/**
		 * 打印结果集的原信息
		 * @param resultSet
		 * @throws SQLException
		 */
		public static void printResultSetMetaData(ResultSet resultSet) throws SQLException{
			final ResultSetMetaData metaData = resultSet.getMetaData();
			final int columnCount = metaData.getColumnCount();
			System.out.println("   <<<   printResultSetMetaData bof   >>>   ");
			for (int i = 1; i <= columnCount; i++) { // 结果集每一列的信息
				System.out.println("columnInfo : " 
						+ "getColumnLabel="+metaData.getColumnLabel(i) 
						+ ", getColumnName="+metaData.getColumnName(i)
						+ ", getColumnType="+metaData.getColumnType(i)
						+ ", getColumnClassName="+metaData.getColumnClassName(i)
						);
			}
			System.out.println("   <<<   printResultSetMetaData eof   >>>   ");
		}

		/**
		 * 事务
		 * @param connection
		 * @throws SQLException
		 */
		public static void testTransaction(Connection connection) throws SQLException {
			boolean autoCommitOld = connection.getAutoCommit(); // 保存原来的
			connection.setAutoCommit(false);
			try {
				if(PreparedStatementTest.delete(connection, "field1Value")<=0){
					connection.rollback();
				}
				if(PreparedStatementTest.delete(connection, "field1Value")<=0){
					connection.rollback();
				}
				connection.commit();
			} catch (Exception e) {
				connection.rollback();
			}
			finally {
				connection.setAutoCommit(autoCommitOld); // 还原原来的
			}
		}
		
	}
	
	

	/**
	 * statement
	 * @author zhouzhian
	 *
	 */
	private static class StatementTest {
		
		public static int delete(Connection connection, String field1) {
			int affectedRowCount = 0;
			String sql = "delete from table1 where field1='" + field1 + "'";
			Statement stmt;
			try {
				stmt = createStatement(connection);
				stmt.execute(sql);
				affectedRowCount = stmt.getUpdateCount();
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return affectedRowCount;
		}

		public static Integer selectAll(Connection connection) {
			String sql = "select * from table1";
			Statement stmt;
			try {
				stmt = createStatement(connection);
				ResultSet rs = stmt.executeQuery(sql);
				int col = rs.getMetaData().getColumnCount();
				while (rs.next()) {
					for (int i = 1; i <= col; i++) {
						System.out.print(rs.getString(i) + "\t");
						if ((i == 2) && (rs.getString(i).length() < 8)) {
							System.out.print("\t");
						}
					}
					System.out.println("");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return null;
		}

		public static int update(Connection connection, HashMap<String, String> map) {
			int affectedRowCount = 0;
			String sql = "update table1 set field2='" + map.get("field2") + "' where field1='" + map.get("field1")+ "'";
			Statement stmt;
			try {
				stmt = createStatement(connection);
				stmt.execute(sql);
				affectedRowCount = stmt.getUpdateCount();
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return affectedRowCount;
		}

		public static int insert(Connection connection, HashMap<String, String> map) {
			int affectedRowCount = 0;
			String sql = "insert into table1 (field1,field2,field3) values('"+ map.get("field1")+"','"+ map.get("field2")+"','"+ map.get("field3")+"')";
			Statement stmt;
			try {
				stmt = createStatement(connection);
				stmt.execute(sql);
				affectedRowCount = stmt.getUpdateCount();
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return affectedRowCount;
		}

		public static Statement createStatement(Connection connection) throws SQLException {
			Statement stmt = connection.createStatement();
			stmt.setQueryTimeout(20);
			stmt.setFetchSize(100);
			return stmt;
		}
	}

	/**
	 * 预置语句 PreparedStatement
	 * @author zhouzhian
	 *
	 */
	private static class PreparedStatementTest {
		public static int delete(Connection connection, String field1) {
			int affectedRowCount = 0;
			String sql = "delete from table1 where field1=?";
			PreparedStatement pstmt;
			try {
				pstmt = prepareStatement(connection,sql);
				pstmt.setString(1, field1);
				affectedRowCount = pstmt.executeUpdate();
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return affectedRowCount;
		}

		public static Integer selectAll(Connection connection) {
			String sql = "select * from table1";
			PreparedStatement pstmt;
			try {
				pstmt = prepareStatement(connection,sql);
				ResultSet rs = pstmt.executeQuery();
				
				ConnectionUtil.printResultSetMetaData(rs);
				
				int col = rs.getMetaData().getColumnCount();
				while (rs.next()) {
					for (int i = 1; i <= col; i++) {
						System.out.print(rs.getString(i) + "\t");
						if ((i == 2) && (rs.getString(i).length() < 8)) {
							System.out.print("\t");
						}
					}
					System.out.println("");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		/**
		 * 多结果集的支持
		 * @param connection
		 * @return
		 */
		public static Integer selectAllMultipleResultSets(Connection connection) {
			String sql = "select * from table1";
			PreparedStatement pstmt;
			try {
				pstmt = prepareStatement(connection,sql);
				if(!pstmt.execute()){
					return null;
				}
				ResultSet rs = pstmt.getResultSet();
				while(rs != null){
					int col = rs.getMetaData().getColumnCount();
					while (rs.next()) {
						for (int i = 1; i <= col; i++) {
							System.out.print(rs.getString(i) + "\t");
							if ((i == 2) && (rs.getString(i).length() < 8)) {
								System.out.print("\t");
							}
						}
						System.out.println("");
					}
					
					if (pstmt.getConnection().getMetaData().supportsMultipleResultSets()) { // 多结果集的支持
						if (pstmt.getMoreResults() || (pstmt.getUpdateCount() != -1)) {
							rs = pstmt.getResultSet();
						}
						else{
							rs = null;
						}
					}
					else{
						rs = null;
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return null;
		}

		public static Integer selectAll2Step(Connection connection) {
			String sql = "select * from table1";
			PreparedStatement pstmt;
			try {
				pstmt = prepareStatement(connection,sql);
				if(!pstmt.execute()){
					return null;
				}
				ResultSet rs = pstmt.getResultSet();
				int col = rs.getMetaData().getColumnCount();
				while (rs.next()) {
					for (int i = 1; i <= col; i++) {
						System.out.print(rs.getString(i) + "\t");
						if ((i == 2) && (rs.getString(i).length() < 8)) {
							System.out.print("\t");
						}
					}
					System.out.println("");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return null;
		}

		public static int update(Connection connection, HashMap<String, String> map) {
			int affectedRowCount = 0;
			String sql = "update table1 set field2='" + map.get("field2") + "' where field1=? ";
			PreparedStatement pstmt;
			try {
				pstmt = prepareStatement(connection,sql);
				pstmt.setString(1, map.get("field1"));
				affectedRowCount = pstmt.executeUpdate();
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return affectedRowCount;
		}

		public static int insert(Connection connection, HashMap<String, String> map) {
			int affectedRowCount = 0;
			String sql = "insert into table1 (field1,field2,field3) values(?,?,?)";
			PreparedStatement pstmt;
			try {
				pstmt = prepareStatement(connection,sql);
				pstmt.setString(1, map.get("field1"));
				pstmt.setString(2, map.get("field2"));
				pstmt.setString(3, map.get("field3"));
				affectedRowCount = pstmt.executeUpdate();
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return affectedRowCount;
		}

		/**
		 * 预置语句对象
		 */
		public static PreparedStatement prepareStatement(Connection connection,String sql) throws SQLException {
			PreparedStatement pstmt =  (PreparedStatement) connection.prepareStatement(sql);
			pstmt.setQueryTimeout(20);
			pstmt.setFetchSize(100);
			return pstmt;
		}
	}


}
