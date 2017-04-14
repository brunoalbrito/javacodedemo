package cn.java.test.bean.beans;

public class Bean3Factory {
	public String factoryBeanName = Bean3Factory.class.getName();
	
	/**
	 * 工厂方法
	 * @param factoryBean
	 * @return
	 */
	public  Bean3 getBean3(){
		System.out.println("public  Bean3 getBean3()");
		return new Bean3();
	}
	
	/**
	 * 带bean参数的工厂方法
	 * @param factoryBean
	 * @param userid
	 * @param username
	 * @return
	 */
	public  Bean3 getBean3( Integer userid,String username){
		System.out.println("public  Bean3 getBean3( Integer userid,String username)");
		return new Bean3(userid,username);
	}
	
	/**
	 * 带bean参数的工厂方法
	 * @param factoryBean
	 * @param userid
	 * @param username
	 * @return
	 */
	public  Bean3 getBean3(Integer userid,String username,Bean2 bean2){
		System.out.println("public  Bean3 getBean3(Integer userid,String username,Bean2 bean2)");
		return new Bean3(userid,username,bean2);
	}
}
