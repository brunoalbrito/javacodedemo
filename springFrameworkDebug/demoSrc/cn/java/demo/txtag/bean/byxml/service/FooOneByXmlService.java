package cn.java.demo.txtag.bean.byxml.service;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import cn.java.demo.txtag.mock.transactionmanager.MinePlatformTransactionManager;

public class FooOneByXmlService implements ApplicationContextAware{
	
	private static final String TRANSACTION_MANAGER = "transactionManagerx"; // 使用的事务管理器
	
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
	}

	public Integer deleteFooInfo() {
		return 0;
	}

	public Integer updateFooInfo() {
		return 0;
	}

	public List findFooList() {
		return null;
	}
	
	public String findFooOne() {
		return null;
	}

	private MinePlatformTransactionManager transactionManager;
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.transactionManager = applicationContext.getBean(TRANSACTION_MANAGER,MinePlatformTransactionManager.class);
	}
}
