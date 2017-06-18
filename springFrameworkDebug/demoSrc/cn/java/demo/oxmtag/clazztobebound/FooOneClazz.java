package cn.java.demo.oxmtag.clazztobebound;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "FooOneClazz")
@XmlType(propOrder = { "fooId","fooName"}) // 声明XML元素的顺序
public class FooOneClazz {
	
	private Integer fooId;
	
	
	private String fooName;

	@XmlElement(name="FooId")
	public Integer getFooId() {
		return fooId;
	}

	public void setFooId(Integer fooId) {
		this.fooId = fooId;
	}

	@XmlElement(name="FooName") // 不加时默认为@XmlElement(name="fooName")
	public String getFooName() {
		return fooName;
	}

	public void setFooName(String fooName) {
		this.fooName = fooName;
	}

	@Override
	public String toString() {
		return "FooOneClazz [fooId=" + fooId + ", fooName=" + fooName + "]";
	}

}
