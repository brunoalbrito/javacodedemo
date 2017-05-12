package cn.java.jmx;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;

public class Test {

	public static void main(String[] args) throws Exception {
		String beanName="id0";
		ObjectName objectName = new ObjectName("com.alibaba.druid:type=DruidDataSource,id=" + beanName);
		testObjectRegIntoMBeanServer(null,objectName);
		Thread.currentThread().sleep(3600*1000);
	}
	

	/**
	 * 注册到MBeanServer主机
	 * @param object
	 * @param objectName
	 */
	public static void testObjectRegIntoMBeanServer(Object object,ObjectName objectName) {
		MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();
		try {
			mbeanServer.registerMBean(object, objectName);
		} catch (Throwable ex) {
		}
	}
	
	
}
