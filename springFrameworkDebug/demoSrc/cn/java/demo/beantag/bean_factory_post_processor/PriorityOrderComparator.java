package cn.java.demo.beantag.bean_factory_post_processor;

import org.springframework.core.OrderComparator;
import org.springframework.core.PriorityOrdered;

public class PriorityOrderComparator extends OrderComparator {
	
	@Override
	public Integer getPriority(Object obj) {
		if(obj instanceof PriorityOrdered){
			PriorityOrdered priorityOrdered = (PriorityOrdered)obj;
			return priorityOrdered.getOrder();
		}
		return null;
	}
}
