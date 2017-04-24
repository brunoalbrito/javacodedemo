package cn.java.ns.myns1.handlers;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class MyNs1Tag2BeanDefinitionParser  implements BeanDefinitionParser{
	
	@Override
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		System.out.println("---- 使用标签处理器解析（"+this.getClass().getSimpleName()+"）");
		/*
		 	element.getTagName() ===  "myns1:tag2"
		 	element.getLocalName() === "tag2"
		 	
		 	-------------
		 	<myns1:tag2 attr1="helloMyNs1Tag2_attr1" attr2="helloMyNs1Tag2_attr2" />
			
		 */

		
		System.out.println("element.getNodeName() = " + element.getNodeName() + " , element.getTagName() = " + element.getLocalName());
		return null;
		
	}
	
}
