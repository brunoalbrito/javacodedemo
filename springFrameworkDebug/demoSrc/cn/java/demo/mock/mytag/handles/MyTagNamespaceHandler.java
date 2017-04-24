package cn.java.demo.mock.mytag.handles;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class MyTagNamespaceHandler extends NamespaceHandlerSupport{

	@Override
	public BeanDefinition parse(Element element, ParserContext parserContext) {
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
		/*
		 	registerBeanDefinitionParser("tag1", new ConfigBeanDefinitionParser()); // <myaoptag:tag1>
			registerBeanDefinitionParser("tag2", new AspectJAutoProxyBeanDefinitionParser());  // <myaoptag:tag2>
			registerBeanDefinitionDecorator("tag3", new ScopedProxyBeanDefinitionDecorator());  // <myaoptag:tag3>
		 */
		registerBeanDefinitionParser("tag1", new Tag1BeanDefinitionParser());
		registerBeanDefinitionParser("tag2", new Tag2BeanDefinitionParser());
		
	}

}
