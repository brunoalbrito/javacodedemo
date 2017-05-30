package cn.java.demo.beantag.bean.applicationlistener;

import org.springframework.context.ApplicationEvent;

public class ApplicationEventImpl extends ApplicationEvent{

	public ApplicationEventImpl(Object source) {
		super(source);
	}

}
