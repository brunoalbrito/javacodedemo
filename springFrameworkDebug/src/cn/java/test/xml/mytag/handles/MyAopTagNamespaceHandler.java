package cn.java.test.xml.mytag.handles;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class MyAopTagNamespaceHandler extends NamespaceHandlerSupport{

	@Override
	public void init() {
		/*
		 	registerBeanDefinitionParser("tag1", new ConfigBeanDefinitionParser()); // <myaoptag:tag1>
			registerBeanDefinitionParser("tag2", new AspectJAutoProxyBeanDefinitionParser());  // <myaoptag:tag2>
			registerBeanDefinitionDecorator("tag3", new ScopedProxyBeanDefinitionDecorator());  // <myaoptag:tag3>
		 */
		
	}

}
