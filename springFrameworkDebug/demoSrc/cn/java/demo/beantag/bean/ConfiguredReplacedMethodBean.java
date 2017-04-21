package cn.java.demo.beantag.bean;

import cn.java.demo.beantag.bean.lookupmethod.Property1;
import cn.java.demo.beantag.bean.lookupmethod.Property1Impl;

public class ConfiguredReplacedMethodBean {
	private String myName;
	private Property1 property1;
	
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
	public boolean setProperty1(Integer id,String myName) {
		property1 = new Property1Impl(id,myName+"(created in ConfiguredReplacedMethodBean)");
		return true;
	}

	public Property1 getProperty1() {
		return property1;
	}

}
