package cn.java.test.xml.mytag;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;

public class Test {

	/**
	 */
	public static void main(String[] args) throws Exception {
		test();
	}

	public static void test() throws Exception {
		String currDirStr = Test.class.getResource(".").getFile();
		File file = new File(currDirStr + "applicationContext.xml");
		InputStream inputStream = new FileInputStream(file);
		InputSource inputSource = new InputSource(inputStream);
//		inputSource.setEncoding("UTF-8");
		// 创建文档构建器工厂
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		
		factory.setNamespaceAware(true); // 识别命名空间
		factory.setValidating(true); // 校验标签
		factory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
		
		DocumentBuilder docBuilder = factory.newDocumentBuilder();
		docBuilder.setErrorHandler(new SimpleSaxErrorHandler());// 错误处理器
		docBuilder.setEntityResolver(new MytagEntityResolver()); // 解析器（校验器）
		Document doc = docBuilder.parse(inputSource); // 解析
		Element root = doc.getDocumentElement();
		
		// 获取节点属性
		System.out.println("getAttribute : " + root.getAttribute("default-autowire"));

		// 获取节点的命名空间
		Node node = root;
		System.out.println("getNamespaceURI : " + node.getNamespaceURI());

		// 获取节点的子节点
		NodeList nl = root.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node nodeTemp = nl.item(i);
			if (nodeTemp instanceof Element) {
				Element ele = (Element) nodeTemp;
				System.out.println("getNamespaceURI : " + ele.getNamespaceURI()+" , getLocalName : " + ele.getLocalName()+" , getPrefix : "
							+ ele.getPrefix()+" , getTagName : " + ele.getTagName()+" , getNodeName : " + ele.getNodeName());
			}
		}
	}
}