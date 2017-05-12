package cn.java.demo.contexttag.internal.enhancer;

public class FooServiceWithImplBeanFactoryAware implements BeanFactoryAware{
	private BeanFactory beanFactory;
	
	@Override
	public void setBeanFactory(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}
	
	
	public String getBeanFactoryName() {
		return this.beanFactory.getBeanFactoryName();
	}
	
	public String getBean0() {
		return "getBean0 in "+this.getClass().getSimpleName();
	}
	
	public String getBean1() {
		return "getBean0 in "+this.getClass().getSimpleName();
	}
	
	
}
