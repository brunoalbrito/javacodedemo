package cn.java.ns.myns1.handlers;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class MyNs1NamespaceHandler extends NamespaceHandlerSupport{

	@Override
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		System.out.println("- 使用命名空间处理器解析（"+this.getClass().getSimpleName()+"）");
		BeanDefinitionParserDelegate delegate = parserContext.getDelegate();
		/*
		 	element.getTagName() ===  "myaoptag:tag1"
		 	element.getLocalName() === "tag1"
		 */
		String localName = delegate.getLocalName(element); 
		return super.parse(element, parserContext);
	}
	
	@Override
	public void init() {
		System.out.println("- 初始化命名空间处理器（"+this.getClass().getSimpleName()+"）");
		// 注册 BeanDefinition 解析器
		registerBeanDefinitionParser("tag1", new MyNs1Tag1BeanDefinitionParser()); // <myns1:tag1>
		registerBeanDefinitionParser("tag2", new MyNs1Tag2BeanDefinitionParser()); // <myns1:tag2>
		// 注册 BeanDefinition 装饰器
		registerBeanDefinitionDecorator("tag3", new MyNs1Tag3BeanDefinitionDecorator()); // <myns1:tag3>
		
	}
	
	@Override
	public BeanDefinitionHolder decorate(
			Node node, BeanDefinitionHolder definition, ParserContext parserContext) {
		return super.decorate(node, definition, parserContext);
	}

}
