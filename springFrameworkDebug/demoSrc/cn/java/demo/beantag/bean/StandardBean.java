package cn.java.demo.beantag.bean;

public class StandardBean {

	private Integer beanId;
	private String beanName;
	
	public StandardBean(Integer beanId) {
		this.beanId = beanId;
	}
	public StandardBean() {
		super();
	}
	
	public int getBeanId() {
		return beanId;
	}
	public void setBeanId(int beanId) {
		this.beanId = beanId;
	}
	public String getBeanName() {
		return beanName;
	}
	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}
	
	@Override
	public String toString() {
		return "StandardBean [beanId=" + beanId + ", beanName=" + beanName + "]";
	}
	
}
