package cn.java.ns.myns1.handlers;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.xml.BeanDefinitionDecorator;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class MyNs1Tag3BeanDefinitionDecorator implements BeanDefinitionDecorator {

	@Override
	public BeanDefinitionHolder decorate(Node node, BeanDefinitionHolder definition, ParserContext parserContext) {
		if (node instanceof Element) {
			Element ele = (Element) node;
			System.out.println("---- 使用BeanDefinition装饰器解析（" + this.getClass().getSimpleName() + "）, ele.getNodeName() = " + ele.getNodeName());
		}
		return definition;
	}

}
