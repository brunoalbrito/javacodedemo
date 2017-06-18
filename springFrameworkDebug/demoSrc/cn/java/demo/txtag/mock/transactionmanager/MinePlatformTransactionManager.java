package cn.java.demo.txtag.mock.transactionmanager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import javax.sql.DataSource;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.DelegatingTransactionAttribute;
import org.springframework.transaction.interceptor.NoRollbackRuleAttribute;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.support.DefaultTransactionStatus;

/**
 * 事务管理器
 * 
 * @author zhouzhian
 */
public class MinePlatformTransactionManager implements PlatformTransactionManager {
	
	// 数据源，使用数据源的好处是：有数据库连接池的功能支持，减少了数据库连接的建立、销毁的损耗
	private DataSource dataSource; 
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	private Object synchronizedCreateConnection = new Object();
	private String connectionUrl;
	private String username;
	private String password;
	private ThreadLocal<ArrayBlockingQueue<TransactionInfoHolder>> transactionInfoHolderLocal = new ThreadLocal<ArrayBlockingQueue<TransactionInfoHolder>>();

	public PreparedStatement prepareStatement(String sql) throws SQLException {
		if (transactionInfoHolderLocal.get() == null) {
			throw new RuntimeException("not instance TransactionInfoHolder Queue");
		}
		ArrayBlockingQueue<TransactionInfoHolder> transactionInfoHolderQueue = null;
		transactionInfoHolderQueue = transactionInfoHolderLocal.get();
		if (transactionInfoHolderQueue.size() > 0) {
			TransactionInfoHolder transactionInfoHolder = transactionInfoHolderQueue.peek(); // 获取最近一个事务
			PreparedStatement pstmt = transactionInfoHolder.connection.prepareStatement(sql);
			pstmt.setQueryTimeout(transactionInfoHolder.transactionDefinition.getTimeout()); // 超时
			return pstmt;
		} else {
			throw new RuntimeException("not transaction in Queue");
		}
	}

	/**
	 * 获取事务
	 */
	@Override
	public TransactionStatus getTransaction(TransactionDefinition definition) throws TransactionException {
		
		ArrayBlockingQueue<TransactionInfoHolder> transactionInfoHolderQueue = null;
		if (transactionInfoHolderLocal.get() == null) {
			transactionInfoHolderLocal.set(new ArrayBlockingQueue<TransactionInfoHolder>(10));
		}
		transactionInfoHolderQueue = transactionInfoHolderLocal.get();

		TransactionStatus transactionStatus = null;
		if (definition instanceof DelegatingTransactionAttribute) { // 配置值
			DelegatingTransactionAttribute transactionAttribute = (DelegatingTransactionAttribute) definition;
			int isolationLevel = -1;
			// 处理事务隔离级别
			isolationLevel = transactionAttribute.getIsolationLevel();
			{
				switch (isolationLevel) {
				case TransactionDefinition.ISOLATION_DEFAULT:
					isolationLevel = Connection.TRANSACTION_NONE;
					break;
				case TransactionDefinition.ISOLATION_READ_COMMITTED:
					isolationLevel = Connection.TRANSACTION_READ_COMMITTED;
					break;
				case TransactionDefinition.ISOLATION_READ_UNCOMMITTED:
					isolationLevel = Connection.TRANSACTION_READ_UNCOMMITTED;
					break;
				case TransactionDefinition.ISOLATION_REPEATABLE_READ:
					isolationLevel = Connection.TRANSACTION_REPEATABLE_READ;
					break;
				case TransactionDefinition.ISOLATION_SERIALIZABLE:
					isolationLevel = Connection.TRANSACTION_SERIALIZABLE;
					break;
				default:
					break;
				}
			}

			System.out.println(
					"transactionAttribute.getTimeout() = " + transactionAttribute.getTimeout());
			
			// 调试异常机制
			if(definition instanceof RuleBasedTransactionAttribute)
			{
				RuleBasedTransactionAttribute ruleBasedTransactionAttribute = (RuleBasedTransactionAttribute)definition;
				List<RollbackRuleAttribute> rollbackRules = ruleBasedTransactionAttribute.getRollbackRules();
				for (RollbackRuleAttribute rollbackRuleAttribute : rollbackRules) {
					Exception exceptionTemp = new RuntimeException();
					if (rollbackRuleAttribute instanceof NoRollbackRuleAttribute) {
						System.out.println(rollbackRuleAttribute.getDepth(exceptionTemp)); // 匹配深度
					} else if (rollbackRuleAttribute instanceof RollbackRuleAttribute) {
						System.out.println(rollbackRuleAttribute.getDepth(exceptionTemp)); // 匹配深度
					}
					rollbackRuleAttribute.getExceptionName();
				}
			}

			// 处理事务的获取机制
			{
				Connection connection = null;
				boolean isUseTransaction;
				boolean readOnly = transactionAttribute.isReadOnly();
				switch (transactionAttribute.getPropagationBehavior()) {
				case TransactionDefinition.PROPAGATION_REQUIRED: // 如果存在事务就使用，如果不存在就创建
				{
					if (transactionInfoHolderQueue.size() > 0) {
						TransactionInfoHolder transactionInfoHolder = transactionInfoHolderQueue.peek();
						connection = transactionInfoHolder.connection;
						transactionStatus = transactionInfoHolder.transactionStatus;
					} else {
						synchronized (synchronizedCreateConnection) {
							connection = getConnection(readOnly, isolationLevel);
							try {
								connection.setAutoCommit(false); // 禁止自动提交
								connection.setReadOnly(readOnly); // 只读模式
								connection.setTransactionIsolation(isolationLevel); // 隔离级别
								isUseTransaction = true;
							} catch (SQLException e) {
								isUseTransaction = false;
								throw new RuntimeException(e);
							}
							transactionStatus = newTransactionStatus(connection, readOnly);
							TransactionInfoHolder transactionInfoHolder = new TransactionInfoHolder(connection,
									definition, transactionStatus, isUseTransaction);
							try {
								transactionInfoHolderQueue.put(transactionInfoHolder);
							} catch (InterruptedException e) {
								throw new RuntimeException(e);
							}
						}
					}
				}
					break;
				case TransactionDefinition.PROPAGATION_SUPPORTS: // 如果支持事务就使用事务，如果不支持就不使用
				{
					if (transactionInfoHolderQueue.size() > 0) {
						TransactionInfoHolder transactionInfoHolder = transactionInfoHolderQueue.peek();
						connection = transactionInfoHolder.connection;
						transactionStatus = transactionInfoHolder.transactionStatus;
					} else {
						synchronized (synchronizedCreateConnection) {
							connection = getConnection(readOnly, isolationLevel);
							try {
								connection.setAutoCommit(false); // 禁止自动提交
								connection.setReadOnly(readOnly); // 只读模式
								connection.setTransactionIsolation(isolationLevel); // 隔离级别
								isUseTransaction = true;
							} catch (SQLException e) {
								isUseTransaction = false;
							}
							transactionStatus = newTransactionStatus(connection, readOnly);
							TransactionInfoHolder transactionInfoHolder = new TransactionInfoHolder(connection,
									definition, transactionStatus, isUseTransaction);
							try {
								transactionInfoHolderQueue.put(transactionInfoHolder);
							} catch (InterruptedException e) {
								throw new RuntimeException(e);
							}
						}
					}
				}
					break;
				case TransactionDefinition.PROPAGATION_MANDATORY: // 如果支持事务就使用事务，如果不支持就抛异常
				{
					if (transactionInfoHolderQueue.size() > 0) {
						TransactionInfoHolder transactionInfoHolder = transactionInfoHolderQueue.peek();
						connection = transactionInfoHolder.connection;
						transactionStatus = transactionInfoHolder.transactionStatus;
					} else {
						synchronized (synchronizedCreateConnection) {
							connection = getConnection(readOnly, isolationLevel);
							try {
								connection.setAutoCommit(false); // 禁止自动提交
								connection.setReadOnly(readOnly); // 只读模式
								connection.setTransactionIsolation(isolationLevel); // 隔离级别
								isUseTransaction = true;
							} catch (SQLException e) {
								isUseTransaction = false;
								throw new RuntimeException(e);
							}
							transactionStatus = newTransactionStatus(connection, readOnly);
							TransactionInfoHolder transactionInfoHolder = new TransactionInfoHolder(connection,
									definition, transactionStatus, isUseTransaction);
							try {
								transactionInfoHolderQueue.put(transactionInfoHolder);
							} catch (InterruptedException e) {
								throw new RuntimeException(e);
							}
						}

					}
				}
					break;
				case TransactionDefinition.PROPAGATION_REQUIRES_NEW: // 创建一个新的事务，如果已经存在一个事务那么“挂起”该事务
				{
					synchronized (synchronizedCreateConnection) {
						connection = getConnection(readOnly, isolationLevel);
						try {
							connection.setAutoCommit(false); // 禁止自动提交
							connection.setReadOnly(readOnly); // 只读模式
							connection.setTransactionIsolation(isolationLevel); // 隔离级别
							isUseTransaction = true;
						} catch (SQLException e) {
							isUseTransaction = false;
							throw new RuntimeException(e);
						}
						transactionStatus = newTransactionStatus(connection, readOnly);
						TransactionInfoHolder transactionInfoHolder = new TransactionInfoHolder(connection, definition,
								transactionStatus, isUseTransaction);
						try {
							transactionInfoHolderQueue.put(transactionInfoHolder);
						} catch (InterruptedException e) {
							throw new RuntimeException(e);
						}
					}

				}
					break;
				case TransactionDefinition.PROPAGATION_NOT_SUPPORTED: // 如果支持事务就使用事务执行，如果不支持事务就使用非事务执行
				{
					if (transactionInfoHolderQueue.size() > 0) {
						TransactionInfoHolder transactionInfoHolder = transactionInfoHolderQueue.peek();
						connection = transactionInfoHolder.connection;
						transactionStatus = transactionInfoHolder.transactionStatus;
					} else {
						synchronized (synchronizedCreateConnection) {
							connection = getConnection(readOnly, isolationLevel);
							try {
								connection.setAutoCommit(false); // 禁止自动提交
								connection.setReadOnly(readOnly); // 只读模式
								connection.setTransactionIsolation(isolationLevel); // 隔离级别
								isUseTransaction = true;
							} catch (SQLException e) {
								isUseTransaction = false;
							}
							transactionStatus = newTransactionStatus(connection, readOnly);
							TransactionInfoHolder transactionInfoHolder = new TransactionInfoHolder(connection,
									definition, transactionStatus, isUseTransaction);
							try {
								transactionInfoHolderQueue.put(transactionInfoHolder);
							} catch (InterruptedException e) {
								throw new RuntimeException(e);
							}
						}
					}
				}
					break;
				case TransactionDefinition.PROPAGATION_NEVER: // 不能使用事务执行，如果存在一个事务就抛异常
				{
					if (transactionInfoHolderQueue.size() > 0) {
						TransactionInfoHolder transactionInfoHolder = transactionInfoHolderQueue.peek();
						connection = transactionInfoHolder.connection;
						transactionStatus = transactionInfoHolder.transactionStatus;
						if (transactionInfoHolder.isUseTransaction) {
							throw new RuntimeException("the pre-transaction is use Transaction");
						}
					} else {
						synchronized (synchronizedCreateConnection) {
							connection = getConnection(readOnly, isolationLevel);
							try {
								connection.setAutoCommit(true); // 自动提交
								connection.setReadOnly(readOnly); // 只读模式
								connection.setTransactionIsolation(isolationLevel); // 隔离级别
								isUseTransaction = false;
							} catch (SQLException e) {
								isUseTransaction = true;
								throw new RuntimeException(e);
							}
							transactionStatus = newTransactionStatus(connection, readOnly);
							TransactionInfoHolder transactionInfoHolder = new TransactionInfoHolder(connection,
									definition, transactionStatus, isUseTransaction);
							try {
								transactionInfoHolderQueue.put(transactionInfoHolder);
							} catch (InterruptedException e) {
								throw new RuntimeException(e);
							}
						}
					}
				}
					break;
				case TransactionDefinition.PROPAGATION_NESTED: // 嵌套使用事务执行
				{
					if (transactionInfoHolderQueue.size() > 0) {
						TransactionInfoHolder transactionInfoHolder = transactionInfoHolderQueue.peek();
						connection = transactionInfoHolder.connection;
						transactionStatus = transactionInfoHolder.transactionStatus;
						if (!transactionInfoHolder.isUseTransaction) {
							throw new RuntimeException("the pre-transaction is not use Transaction");
						}
					} else {
						throw new RuntimeException("not start Transaction");
					}
				}
					break;
				default: {
					throw new RuntimeException("not supported propagation");
				}
				}
			}
		} else {
			throw new RuntimeException("definition is not instanceof RuleBasedTransactionAttribute");
		}
		return transactionStatus; // 返回null表示不使用事务，返回不为null表示有使用事务
	}

	private TransactionStatus newTransactionStatus(Object transaction, boolean readOnly) {
		if (transaction == null) {
			throw new RuntimeException("创建数据库连接失败");
		}
		boolean newTransaction = true;
		boolean newSynchronization = true;
		boolean debug = false;
		Object suspendedResources = null;
		return new DefaultTransactionStatus(transaction, newTransaction, newSynchronization, readOnly, debug,
				suspendedResources);
	}

	private Connection getConnection(boolean readOnly, int isolationLevel) {
		if(this.dataSource!=null){ // 使用数据源创建
			try {
				return dataSource.getConnection();
			} catch (SQLException se) {
				throw new RuntimeException(se);
			}
		}
		else{
			// 加载MySql的驱动类
			try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
			// 连接MySql数据库
			try {
				Connection connection = DriverManager.getConnection(this.connectionUrl, this.username, this.password);
				return connection;
			} catch (SQLException se) {
				throw new RuntimeException(se);
			}
		}
		
	}

	/**
	 * 提交
	 */
	@Override
	public void commit(TransactionStatus status) throws TransactionException {
		if (status instanceof DefaultTransactionStatus) {
			ArrayBlockingQueue<TransactionInfoHolder> transactionInfoHolderQueue = transactionInfoHolderLocal.get();
			if (transactionInfoHolderQueue.size() > 0) {
				synchronized (synchronizedCreateConnection) {
					TransactionInfoHolder transactionInfoHolder = null;
					if ((transactionInfoHolder = transactionInfoHolderQueue.poll()) != null) {
						if (transactionInfoHolder.connection
								.equals(((DefaultTransactionStatus) status).getTransaction())) {
							try {
								transactionInfoHolder.connection.commit();
							} catch (SQLException e) {
								throw new TransactionException("事务提交失败", e) {
								};
							}
						} else {
							throw new TransactionException("事务嵌套异常") {
							};
						}
					}
				}
			}
		} else {
			throw new RuntimeException("status is not instanceof DefaultTransactionStatus");
		}
	}

	/**
	 * 回滚
	 */
	@Override
	public void rollback(TransactionStatus status) throws TransactionException {
		if (status instanceof DefaultTransactionStatus) {
			ArrayBlockingQueue<TransactionInfoHolder> transactionInfoHolderQueue = transactionInfoHolderLocal.get();
			if (transactionInfoHolderQueue.size() > 0) {
				synchronized (synchronizedCreateConnection) {
					TransactionInfoHolder transactionInfoHolder = null;
					if ((transactionInfoHolder = transactionInfoHolderQueue.poll()) != null) {
						if (transactionInfoHolder.connection
								.equals(((DefaultTransactionStatus) status).getTransaction())) {
							try {
								transactionInfoHolder.connection.rollback();
							} catch (SQLException e) {
								throw new TransactionException("事务回滚失败", e) {
								};
							}

						} else {
							throw new TransactionException("事务嵌套异常") {
							};
						}
					}
				}
			}
		} else {
			throw new RuntimeException("status is not instanceof DefaultTransactionStatus");
		}
	}

	/**
	 * 获取最近一个连接
	 * 
	 * @return
	 */
	public Connection getConnection() {
		synchronized (transactionInfoHolderLocal) {
			if (transactionInfoHolderLocal.get() == null) {
				throw new RuntimeException("not instance TransactionInfoHolder Queue");
			}
			ArrayBlockingQueue<TransactionInfoHolder> transactionInfoHolderQueue = null;
			transactionInfoHolderQueue = transactionInfoHolderLocal.get();
			if (transactionInfoHolderQueue.size() > 0) {
				TransactionInfoHolder transactionInfoHolder = transactionInfoHolderQueue.peek();
				return transactionInfoHolder.connection;
			} else {
				throw new RuntimeException("not transaction in Queue");
			}
		}
	}

	/**
	 * 事务信息存储器
	 * 
	 * @author zhouzhian
	 *
	 */
	private static class TransactionInfoHolder {
		public Connection connection;
		public TransactionDefinition transactionDefinition;
		public TransactionStatus transactionStatus;
		public boolean isUseTransaction;

		public TransactionInfoHolder(Connection connection, TransactionDefinition transactionDefinition,
				TransactionStatus transactionStatus, boolean isUseTransaction) {
			super();
			this.connection = connection;
			this.transactionDefinition = transactionDefinition;
			this.transactionStatus = transactionStatus;
		}
	}

	public String getConnectionUrl() {
		return connectionUrl;
	}

	public void setConnectionUrl(String connectionUrl) {
		this.connectionUrl = connectionUrl;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
