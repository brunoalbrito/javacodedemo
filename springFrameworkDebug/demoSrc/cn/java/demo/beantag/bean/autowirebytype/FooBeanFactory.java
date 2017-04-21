package cn.java.demo.beantag.bean.autowirebytype;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;

import cn.java.demo.beantag.bean.FooBean;

public class FooBeanFactory implements ObjectFactory<FooBean> {

	@Override
	public FooBean getObject() throws BeansException {
		return null;
	}

}
