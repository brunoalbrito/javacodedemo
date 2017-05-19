package cn.java.demo.beantag.bean.initmehtod;

import org.springframework.beans.factory.InitializingBean;

public class DemoInitializingBean implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("---------和init-method=\"xxx\"配置等效的功能--------------");
	}

}
