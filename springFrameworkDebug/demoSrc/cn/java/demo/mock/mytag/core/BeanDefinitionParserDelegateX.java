package cn.java.demo.mock.mytag.core;

import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.beans.factory.xml.XmlReaderContext;
import org.w3c.dom.Node;

public class BeanDefinitionParserDelegateX extends BeanDefinitionParserDelegate{
	public BeanDefinitionParserDelegateX(XmlReaderContext readerContext) {
		super(readerContext);
	}
	public String getNamespaceURI(Node node) {
		return node.getNamespaceURI();
	}
	public String getLocalName(Node node) {
		return node.getLocalName();
	}
	
	public boolean nodeNameEquals(Node node, String desiredName) {
		return desiredName.equals(node.getNodeName()) || desiredName.equals(getLocalName(node));
	}
}

