package cn.java.demo.beantag.bean.lookupmethod;

public class Property1Impl implements Property1 {
	
	private Integer id;
	private String myName;
	
	public Property1Impl(Integer id,String myName){
		this.id = id;
		this.myName = myName;
	}
	
	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public String getMyName() {
		return myName;
	}

	@Override
	public String toString() {
		return "Property1Impl [id=" + id + ", myName=" + myName + "]";
	}
	
	
}
