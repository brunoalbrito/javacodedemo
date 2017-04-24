package cn.java.demo.beantag.bean.lookupmethod;

public class Property1LookupMethod implements Property1 {
	
	private Property1Impl property1;
	
	/**
	 * “构造函数参数的写法”和“被Lookup的方法的参数”一一对应
	 * @param id
	 * @param myName
	 */
	public Property1LookupMethod(int id,String myName) {
		System.out.println(this.getClass().getName() + ":LookupMethod(int id,String myName)...");
		property1 = new Property1Impl(id,myName+"(created in Property1LookupMethod - 我是“劫持者”)");
	}

	public Property1Impl getProperty1() {
		return property1;
	}

	@Override
	public Integer getId() {
		return property1.getId();
	}

	@Override
	public String getMyName() {
		return property1.getMyName();
	}
	
}
