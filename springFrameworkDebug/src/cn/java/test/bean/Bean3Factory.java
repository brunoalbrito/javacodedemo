package cn.java.test.bean;

public class Bean3Factory {
	public String factoryBeanName = Bean3Factory.class.getName();
	
	/**
	 * 工厂方法
	 * @param factoryBean
	 * @return
	 */
	public static Bean3 getBean3(Object factoryBean){
		Bean3Factory factory = (Bean3Factory) factoryBean;
		System.out.println(factory.factoryBeanName);
		return new Bean3();
	}
	
	/**
	 * 带bean参数的工厂方法
	 * @param factoryBean
	 * @param userid
	 * @param username
	 * @return
	 */
	public static Bean3 getBean3(Object factoryBean,Integer userid,String username){
		Bean3Factory factory = (Bean3Factory) factoryBean;
		System.out.println(factory.factoryBeanName);
		return new Bean3(userid,username);
	}
	
	/**
	 * 带bean参数的工厂方法
	 * @param factoryBean
	 * @param userid
	 * @param username
	 * @return
	 */
	public static Bean3 getBean3(Object factoryBean,Integer userid,String username,Bean2 bean2){
		Bean3Factory factory = (Bean3Factory) factoryBean;
		System.out.println(factory.factoryBeanName);
		return new Bean3(userid,username,bean2);
	}
}
