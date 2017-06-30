package cn.java.demo.oxmtag.clazztobebound;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "FooTwoClazz")
@XmlType(propOrder = { "fooId", "fooName" })
public class FooTwoClazz {
	private Integer fooId;
	private String fooName;

	public Integer getFooId() {
		return fooId;
	}

	public void setFooId(Integer fooId) {
		this.fooId = fooId;
	}

	public String getFooName() {
		return fooName;
	}

	public void setFooName(String fooName) {
		this.fooName = fooName;
	}

	@Override
	public String toString() {
		return "FooTwoClazz [fooId=" + fooId + ", fooName=" + fooName + "]";
	}

}
