package cn.java.demo.beantag.bean.beanfactorypostprocessor.autowirebytype;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.SimpleAutowireCandidateResolver;

public class SimpleAutowireCandidateResolverExtend extends SimpleAutowireCandidateResolver {
	
	@SuppressWarnings("unused")
	@Override
	public boolean isAutowireCandidate(BeanDefinitionHolder bdHolder, DependencyDescriptor descriptor) {
		if(true){
			return super.isAutowireCandidate(bdHolder, descriptor);
		}
		if(bdHolder.getBeanDefinition().isAutowireCandidate()){
			String paramName = descriptor.getDependencyName();
			if((paramName!=null) && (paramName.equals(descriptor.getDependencyName()))){
				return true;
			}
			String[] aliases = bdHolder.getAliases();
			if((aliases!=null)){
				List list = Arrays.asList(aliases);
				if(list.contains(paramName)){
					return true;
				}
			}
		}
		return false;
	}
}
