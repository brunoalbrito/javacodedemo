package cn.java.demo.beantag.bean.applicationlistener;

import org.springframework.context.ApplicationEvent;

public class FooApplicationEvent extends ApplicationEvent{

	public FooApplicationEvent(Object source) {
		super(source);
	}

}
