package cn.java.demo.contexttag.bean;

public class BeanToBeInject {
	private Integer beanId;
	private String beanName;
	public BeanToBeInject(Integer beanId, String beanName) {
		super();
		this.beanId = beanId;
		this.beanName = beanName;
	}
	@Override
	public String toString() {
		return "BeanToBeInject [beanId=" + beanId + ", beanName=" + beanName + "]";
	}
	
	
}
