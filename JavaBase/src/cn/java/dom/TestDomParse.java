package cn.java.dom;

import java.io.InputStream;
import java.util.Properties;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class TestDomParse {
	private XPath xpath;
	public XPath getXPath(){
		if(xpath==null){
			XPathFactory xPathFactory = XPathFactory.newInstance();
			xpath =  xPathFactory.newXPath();
		}
		return xpath;
	}

	public EntityResolver getEntityResolver(){
		return new XMLMapperEntityResolver();
	}

	public static void main(String[] args) {
		
	}
	

	public void run() throws Exception{
		InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream("mybatis-config.xml");
		InputSource inputSource = new InputSource(inputStream);
		Document document = createDocument(inputSource,true);

		String expression = "/configuration";
		QName returnType = XPathConstants.NODE;
		XPath xpath = getXPath();
		Node node = (Node) xpath.evaluate(expression, document, returnType);

		// 节点名称
		String name = node.getNodeName();

		//　节点属性
		Properties attributes = new Properties();
		NamedNodeMap attributeNodes = node.getAttributes();
		if (attributeNodes != null) {
			for (int i = 0; i < attributeNodes.getLength(); i++) {
				Node attribute = attributeNodes.item(i);
				String value = attribute.getNodeValue();
				attributes.put(attribute.getNodeName(), value);
			}
		}

		// 获取子节点
		if (node.getNodeType() == Node.CDATA_SECTION_NODE
				|| node.getNodeType() == Node.TEXT_NODE) { // 如果是文本节点
			String data = ((CharacterData) node).getData();
		}
		else{ // 不是文本节点，有子节点
			NodeList children = node.getChildNodes();
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
			}
		}
	}

	private Document createDocument(InputSource inputSource,boolean validation) throws Exception {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(validation); // 是否校验

			factory.setNamespaceAware(false);
			factory.setIgnoringComments(true);
			factory.setIgnoringElementContentWhitespace(false);
			factory.setCoalescing(false);
			factory.setExpandEntityReferences(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			builder.setEntityResolver(getEntityResolver()); // 实体解析器
			builder.setErrorHandler(new ErrorHandler() {
				@Override
				public void error(SAXParseException exception) throws SAXException {
					throw exception;
				}

				@Override
				public void fatalError(SAXParseException exception) throws SAXException {
					throw exception;
				}

				@Override
				public void warning(SAXParseException exception) throws SAXException {
				}
			});
			return builder.parse(inputSource);
		} catch (Exception e) {
			throw new Exception("Error creating document instance.  Cause: " + e, e);
		}
	}


}
