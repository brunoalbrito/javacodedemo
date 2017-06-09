package cn.java.demo.beantag.internal.beanwrapper;

import org.springframework.beans.factory.config.DependencyDescriptor;

class NestedDependencyDescriptor extends DependencyDescriptor {

	public NestedDependencyDescriptor(DependencyDescriptor original) {
		super(original);
		increaseNestingLevel();
	}
}