package cn.java.demo.txtag.bean.byannotation.service;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.java.demo.txtag.exception.MineRuntimeException;
import cn.java.demo.txtag.exception.MineSubOneRuntimeException;
import cn.java.demo.txtag.mock.transactionmanager.MinePlatformTransactionManager;

public class FooOneByAnnotaionService implements ApplicationContextAware{
	
	private static final String TRANSACTION_MANAGER = "transactionManagerx"; // 使用的事务管理器
	
	@Transactional(transactionManager = TRANSACTION_MANAGER, propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ,
			timeout = TransactionDefinition.TIMEOUT_DEFAULT, readOnly = false, 
			rollbackFor = {MineRuntimeException.class},rollbackForClassName={"cn.java.demo.txtag.exception.MineRuntimeException"},
			noRollbackFor = {MineSubOneRuntimeException.class},noRollbackForClassName={"cn.java.demo.txtag.exception.MineSubOneRuntimeException"})
	public Integer insertFooInfo() {
		System.out.println("code in : "+this.getClass().getName());
		try {
			Connection connection = this.transactionManager.getConnection();
			DatabaseMetaData metaData = connection.getMetaData();
			System.out.println("getDriverName : " + metaData.getDriverName());
			System.out.println("getDriverVersion : " + metaData.getDriverVersion());
			System.out.println("getDriverMajorVersion : " + metaData.getDriverMajorVersion());
			System.out.println("getDriverMinorVersion : " + metaData.getDriverMinorVersion());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
		
//		int affectedRowCount = 0;
//		String sql = "insert into table1 (field1,field2,field3) values(?,?,?)";
//		PreparedStatement pstmt = null;
//		try {
//			pstmt =  (PreparedStatement) this.transactionManager.getConnection().prepareStatement(sql);
//			pstmt.setQueryTimeout(20);
//			pstmt.setFetchSize(100);
//			pstmt.setString(1, "field1_value");
//			pstmt.setString(2, "field2_value");
//			pstmt.setString(3, "field3_value");
//			affectedRowCount = pstmt.executeUpdate();
//		} catch (SQLException e) {
//			throw new RuntimeException(e);
//		}
//		finally {
//			if(pstmt!=null){
//				try {
//					pstmt.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//		return affectedRowCount;
		
	}

	@Transactional()
	public Integer deleteFooInfo() {
		return 0;
	}

	@Transactional()
	public Integer updateFooInfo() {
		return 0;
	}

	@Transactional()
	public List findFooList() {
		return null;
	}
	
	@Transactional()
	public String findFooOne() {
		return null;
	}

	private MinePlatformTransactionManager transactionManager;
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.transactionManager = applicationContext.getBean(TRANSACTION_MANAGER,MinePlatformTransactionManager.class);
	}
}
