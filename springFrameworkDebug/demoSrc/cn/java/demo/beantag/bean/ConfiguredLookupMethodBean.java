package cn.java.demo.beantag.bean;

import cn.java.demo.beantag.bean.lookupmethod.Property1;
import cn.java.demo.beantag.bean.lookupmethod.Property1Impl;

public class ConfiguredLookupMethodBean {
	private String myName;

	public String getMyName() {
		return myName;
	}

	public void setMyName(String myName) {
		this.myName = myName;
	}
	
	/**
	 * 这个方法会被劫持
	 * @param id
	 * @param myName
	 */
	public Property1 getProperty1(Integer id,String myName) {
		return new Property1Impl(id, myName+"(created in ConfiguredLookupMethodBean)");
	}
	
}
