package cn.java.demo.mock.mytag.handles;

import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

public class Tag2BeanDefinitionParser  implements BeanDefinitionParser{

	@Override
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		/*
		 	element.getTagName() ===  "myaoptag:tag2"
		 	element.getLocalName() === "tag2"
		 */
		{
			boolean debug = true;
			if(debug == true){
				System.out.println("--bof->"+this.getClass().getName()+":parse(...)");
				System.out.println("element.getNodeName() = " + element.getNodeName() + " , element.getTagName() = " + element.getLocalName());
				System.out.println("--eof->"+this.getClass().getName()+":parse(...)");
				return null;
			}
		}
		
		System.out.println("element.getNodeName()" + element.getNodeName() + " , element.getTagName()" + element.getLocalName());
		
		parserContext.extractSource(element);
		
		List<Element> childElts = DomUtils.getChildElements(element);
		for (Element elt: childElts) {
			String localName = parserContext.getDelegate().getLocalName(elt);
		}
		return null;
	}

}
