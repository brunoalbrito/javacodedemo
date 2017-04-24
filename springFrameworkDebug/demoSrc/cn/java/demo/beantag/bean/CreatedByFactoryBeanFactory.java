package cn.java.demo.beantag.bean;

public class CreatedByFactoryBeanFactory {
	public String factoryBeanName = CreatedByFactoryBeanFactory.class.getName();

	/**
	 * 工厂方法
	 * 
	 * @param factoryBean
	 * @return
	 */
	public CreatedByFactoryBean getCreatedByFactoryBean() {
		System.out.println(this.getClass().getName() + ":getBean3()");
		return new CreatedByFactoryBean();
	}

	/**
	 * 带bean参数的工厂方法
	 * 
	 * @param factoryBean
	 * @param userid
	 * @param username
	 * @return
	 */
	public CreatedByFactoryBean getCreatedByFactoryBean(Integer userid, String username) {
		System.out.println(this.getClass().getName() + ":getBean3(Integer userid,String username)");
		return new CreatedByFactoryBean(userid, username);
	}

	/**
	 * 带bean参数的工厂方法
	 * 
	 * @param factoryBean
	 * @param userid
	 * @param username
	 * @return
	 */
	public CreatedByFactoryBean getCreatedByFactoryBean(Integer userid, String username, FooBean fooBean) {
		System.out.println(this.getClass().getName() + ":getBean3(Integer userid,String username,FooBean fooBean)");
		return new CreatedByFactoryBean(userid, username, fooBean);
	}
}
