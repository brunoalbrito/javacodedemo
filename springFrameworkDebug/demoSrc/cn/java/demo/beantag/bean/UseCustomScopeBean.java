package cn.java.demo.beantag.bean;

import cn.java.demo.beantag.customscope.MySingletonScope;
import cn.java.demo.beantag.customscope.MySingletonScopeAware;

public class UseCustomScopeBean implements MySingletonScopeAware {


	@Override
	public void setMySingletonScope(MySingletonScope scope) {
		System.out.println(scope.getScopeNameX());
	}

	
	private Integer id;
	private String beanName;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

}
