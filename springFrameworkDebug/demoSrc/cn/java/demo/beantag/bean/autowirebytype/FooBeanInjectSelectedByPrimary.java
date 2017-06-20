package cn.java.demo.beantag.bean.autowirebytype;

public class FooBeanInjectSelectedByPrimary {
	
	private String fooId;
	private String fooName;
	
	public FooBeanInjectSelectedByPrimary(String fooId, String fooName) {
		super();
		this.fooId = fooId;
		this.fooName = fooName;
	}
	
	@Override
	public String toString() {
		return "FooBeanInjectSelectedByPrimary [fooId=" + fooId + ", fooName=" + fooName + "]";
	}
	
	
}
