package cn.java.demo.beantag.internal.beanwrapper;

import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.core.MethodParameter;

class AutowireByTypeDependencyDescriptor extends DependencyDescriptor {
	public AutowireByTypeDependencyDescriptor(MethodParameter methodParameter, boolean eager) {
		super(methodParameter, false, eager);
	}

	@Override
	public String getDependencyName() {
		return null;
	}
}