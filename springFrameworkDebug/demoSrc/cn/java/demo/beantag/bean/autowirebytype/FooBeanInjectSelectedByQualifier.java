package cn.java.demo.beantag.bean.autowirebytype;

public class FooBeanInjectSelectedByQualifier {
	
	private String fooId;
	private String fooName;
	
	public FooBeanInjectSelectedByQualifier(String fooId, String fooName) {
		super();
		this.fooId = fooId;
		this.fooName = fooName;
	}
	
	@Override
	public String toString() {
		return "FooBeanInjectSelectedByQualifier [fooId=" + fooId + ", fooName=" + fooName + "]";
	}
	
	
}
