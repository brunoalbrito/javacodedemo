package cn.java.demo.beantag.bean.autowirebytype;

import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;

public class FooBeanInjectSelectedByOrder implements PriorityOrdered{
	
	private String fooId;
	private String fooName;
	private int order = Ordered.LOWEST_PRECEDENCE;
	
	public FooBeanInjectSelectedByOrder(String fooId, String fooName) {
		super();
		this.fooId = fooId;
		this.fooName = fooName;
	}
	
	@Override
	public String toString() {
		return "FooBeanInjectSelectedByOrder [fooId=" + fooId + ", fooName=" + fooName + "]";
	}

	@Override
	public int getOrder() {
		return this.order;
	}
	
	public void setOrder(int order) {
		this.order = order;
	}
	
}
