package cn.java.ns.myns2.handlers;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class MyNs2NamespaceHandler extends NamespaceHandlerSupport{
	
	
	
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
		registerBeanDefinitionParser("tag1", new MyNs2Tag1BeanDefinitionParser()); // <myns1:tag1>
		registerBeanDefinitionParser("tag2", new MyNs2Tag2BeanDefinitionParser()); // <myns1:tag2>
	}

}
