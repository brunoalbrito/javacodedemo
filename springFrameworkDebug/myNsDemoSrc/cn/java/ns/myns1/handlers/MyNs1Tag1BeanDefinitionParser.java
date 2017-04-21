package cn.java.ns.myns1.handlers;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.core.Ordered;
import org.w3c.dom.Element;

import cn.java.ns.myns1.beanpostprocessor.MyNs1BeanPostProcessorInMyNs1Tag1Impl;

public class MyNs1Tag1BeanDefinitionParser  implements BeanDefinitionParser{
	
	private static final String MyNs1BeanPostProcessorInMyNs1Tag1Impl_CONST = "cn.java.ns.myns1.beanpostprocessor.MyNs1BeanPostProcessorInMyNs1Tag1Impl";
	
	@Override
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		System.out.println("---- 使用标签处理器解析（"+this.getClass().getSimpleName()+"）");
		/*
		 	element.getTagName() ===  "myns1:tag1"
		 	element.getLocalName() === "tag1"
		 	
		 	-------------
		 	<myns1:tag1 attr1="helloMyNs1Tag1_attr1" attr2="helloMyNs1Tag1_attr2" />
			
		 */
		System.out.println("element.getNodeName() = " + element.getNodeName() + " , element.getTagName() = " + element.getLocalName());
		
		// 注入一个实现BeanPostProcessor接口的bean定义（注入钩子）
		RootBeanDefinition beanDefinition0 = new RootBeanDefinition(MyNs1BeanPostProcessorInMyNs1Tag1Impl.class);
		beanDefinition0.setSource(null);
		beanDefinition0.setAutowireMode(RootBeanDefinition.AUTOWIRE_BY_NAME);
		beanDefinition0.getPropertyValues().add("property1", Ordered.HIGHEST_PRECEDENCE);
		beanDefinition0.getPropertyValues().add("property2", "MyNs1BeanPostProcessorInMyNs1Tag1Impl_field2Value");
		beanDefinition0.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
		BeanDefinitionRegistry registry = parserContext.getRegistry();
		registry.registerBeanDefinition(MyNs1BeanPostProcessorInMyNs1Tag1Impl_CONST, beanDefinition0);
		
		return null;
		
	}
	
}
