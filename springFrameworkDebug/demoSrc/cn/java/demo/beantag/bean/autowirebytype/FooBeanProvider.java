package cn.java.demo.beantag.bean.autowirebytype;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectProvider;

import cn.java.demo.beantag.bean.FooBean;

public class FooBeanProvider implements ObjectProvider<FooBean> {

	@Override
	public FooBean getObject() throws BeansException {
		return null;
	}

	@Override
	public FooBean getObject(Object... args) throws BeansException {
		return null;
	}

	@Override
	public FooBean getIfAvailable() throws BeansException {
		return null;
	}

	@Override
	public FooBean getIfUnique() throws BeansException {
		return null;
	}

}
