package cn.java.demo.beantag.message_source;

import org.springframework.context.support.DelegatingMessageSource;

public class DelegatingMessageSourceExtend extends DelegatingMessageSource {

	public DelegatingMessageSourceExtend() {
		super();
		System.out.println("code in : " + this.getClass().getName());
	}

}
