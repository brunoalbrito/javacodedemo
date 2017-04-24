package cn.java.demo.beantag.bean;

import cn.java.demo.beantag.customscope.InstancePoolScope;
import cn.java.demo.beantag.customscope.InstancePoolScopeAware;
import cn.java.demo.beantag.customscope.MySingletonScope;
import cn.java.demo.beantag.customscope.MySingletonScopeAware;

public class UseInstancePoolScopeBean implements InstancePoolScopeAware {

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

	private InstancePoolScope scope;
	
	@Override
	public void setInstancePoolScope(InstancePoolScope scope) {
		this.scope = scope;
		System.out.println(scope.getScopeNameX());
	}
	
	public InstancePoolScope getScope() {
		return scope;
	}

	@Override
	public void setInstanceInfo(String instanceInfo) {
		System.out.println("instanceInfo : " + instanceInfo);
	}
	

}
