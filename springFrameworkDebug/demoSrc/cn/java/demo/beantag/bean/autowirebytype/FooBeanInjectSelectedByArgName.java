package cn.java.demo.beantag.bean.autowirebytype;

public class FooBeanInjectSelectedByArgName {
	
	private String fooId;
	private String fooName;
	
	public FooBeanInjectSelectedByArgName(String fooId, String fooName) {
		super();
		this.fooId = fooId;
		this.fooName = fooName;
	}
	
	@Override
	public String toString() {
		return "FooBeanInjectSelectedByArgName [fooId=" + fooId + ", fooName=" + fooName + "]";
	}
	
	
}
