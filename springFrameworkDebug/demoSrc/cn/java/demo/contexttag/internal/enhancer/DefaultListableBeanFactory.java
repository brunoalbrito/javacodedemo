package cn.java.demo.contexttag.internal.enhancer;

public class DefaultListableBeanFactory implements BeanFactory{

	private String beanFactoryName;
	public DefaultListableBeanFactory(String beanFactoryName) {
		this.beanFactoryName = beanFactoryName;
	}

	@Override
	public String getBeanFactoryName() {
		return this.beanFactoryName;
	}

}
