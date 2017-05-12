package cn.java.note.jmx.dynamic_mbean;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.apache.tomcat.util.modeler.Registry;

public class Test {

	public static void main(String[] args) throws Exception {
		FooService fooService = new FooService(0,"FooService0");
		MBeanServer mserver = Registry.getRegistry(null, null).getMBeanServer();
		ObjectName objectName = new ObjectName("Catalina:type=FooService");
		Registry.getRegistry(null, null).registerComponent(fooService, objectName, null);
		
		Thread.currentThread().sleep(1000*3600);
	}

	public static class FooService{
		private int beanId;
		private String beanName;
		
		public FooService(int beanId, String beanName) {
			super();
			this.beanId = beanId;
			this.beanName = beanName;
		}
		public int getBeanId() {
			return beanId;
		}
		public void setBeanId(int beanId) {
			this.beanId = beanId;
		}
		public String getBeanName() {
			return beanName;
		}
		public void setBeanName(String beanName) {
			this.beanName = beanName;
		}
		
	}
}
