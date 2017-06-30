package cn.java.demo.data_common.entity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "FooOneEntity")
@XmlType(propOrder = { "fooId","fooName"}) // 声明XML元素的顺序
public class FooOneEntity {
	private int fooId;
	private String fooName;
	
	@XmlElement(name="FooId")
	public int getFooId() {
		return fooId;
	}
	public void setFooId(int fooId) {
		this.fooId = fooId;
	}
	
	@XmlElement(name="FooName") // 不加时默认为@XmlElement(name="fooName")
	public String getFooName() {
		return fooName;
	}
	public void setFooName(String fooName) {
		this.fooName = fooName;
	}
	
}
