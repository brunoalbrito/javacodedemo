package cn.java.demo.mock.mytag;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.beans.factory.xml.NamespaceHandler;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.beans.factory.xml.XmlReaderContext;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import cn.java.demo.mock.mytag.core.BeanDefinitionParserDelegateX;
import cn.java.demo.mock.mytag.core.DefaultNamespaceEntityResolverX;
import cn.java.demo.mock.mytag.core.DefaultNamespaceHandlerResolverX;
import cn.java.demo.mock.mytag.schemas.SimpleSaxErrorHandler;

public class Test {

	/**
	 */
	public static void main(String[] args) throws Exception {
		/**
			xmlns:xxx="..." 用来什么一个前缀xxx的命名空间  （在Spring内部识别命名空间处理器会用到，"META-INF/spring.handlers"）
			xsi:schemaLocation="..." 用来声明命名空间到systemId的映射 （在Spring内部做校验会用到，"META-INF/spring.schemas"）
			
			http://www.janchou.org/schema/mock/mytag/spring-mybeanstag-3.0.xsd 这是dom里面的systemId（在Spring内部做校验会用到，"META-INF/spring.schemas"）
		 	http://www.janchou.org/schema/mock/mybeanstag 这是命名空间（在Spring内部识别命名空间处理器会用到，"META-INF/spring.handlers"）
			
			如何开发自己的标签库：
				1、定义文件 mytag.schemas，cn\java\demo\mock\mytag\schemas\META-INF\mytag.schemas
					http\://www.janchou.org/schema/mock/mytag/spring-mybeanstag-3.0.xsd=cn/java/demo/mock/mytag/schemas/spring-mybeanstag-3.0.xsd
					http\://www.janchou.org/schema/mock/mytag/spring-myaoptag-4.2.xsd=cn/java/demo/mock/mytag/schemas/spring-myaoptag-4.2.xsd
				2、编写标签用法的限制文件 cn\java\demo\mock\mytag\schemas\spring-myaoptag-4.2.xsd 
					....
				3、定义文件 mytag.handlers
					http\://www.janchou.org/schema/mock/myaoptag=cn.java.demo.mock.mytag.handles.MyAopNamespaceHandler
				4、标签指定命名空间的处理器
					class MyAopNamespaceHandler extends NamespaceHandlerSupport {
						....
					}
					
			org.springframework.beans.factory.xml.DefaultBeanDefinitionDocumentReader.parseBeanDefinitions(...)	
		 */
		
		
		new Test().test();
	}

	public  void test() throws Exception {
		String currDirStr = Test.class.getResource(".").getFile();
		File file = new File(currDirStr + "applicationContext.xml");
		InputStream inputStream = new FileInputStream(file);
		InputSource inputSource = new InputSource(inputStream);
//		inputSource.setEncoding("UTF-8");
		// -------------解析、校验-----------------
		// 创建文档构建器工厂
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true); // 识别命名空间
		factory.setValidating(true); // 校验标签
		factory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
		DocumentBuilder docBuilder = factory.newDocumentBuilder();
		docBuilder.setErrorHandler(new SimpleSaxErrorHandler());// 错误处理器
		docBuilder.setEntityResolver(new DefaultNamespaceEntityResolverX()); // 解析器（校验器）
		Document doc = docBuilder.parse(inputSource); // 解析
		Element root = doc.getDocumentElement();

		// -------------读取、调用命名空间处理器-----------------
		// 获取节点属性
		System.out.println("getNodeName : " + root.getNodeName() + " , root.getAttribute('default-autowire') : " + root.getAttribute("default-autowire"));
		// 获取节点的命名空间
		BeanDefinitionParserDelegate delegate = new BeanDefinitionParserDelegateX(new XmlReaderContext(null,null,null,null,null,null));
		if (isDefaultNamespace(root)) {
			NodeList nl = root.getChildNodes();
			for (int i = 0; i < nl.getLength(); i++) {
				Node node = nl.item(i);
//				System.out.println("getNamespaceURI : " + node.getNamespaceURI()+" , getLocalName : " + node.getLocalName()+" , getPrefix : "
//						+ node.getPrefix()+" , getNodeName : " + node.getNodeName());
				if (node instanceof Element) {
					Element ele = (Element) node;
					if (isDefaultNamespace(ele)) {
						parseDefaultElement(ele,delegate);
					}
					else {  // 嵌套自定义标签
						parseCustomElement(ele,delegate);
					}
				}
			}
		}
		else{
			parseCustomElement(root,delegate);
		}
		// ------------------------
	}
	
	/**
	 * 自定义元素
	 * @param element
	 */
	private BeanDefinition parseCustomElement(Element element, BeanDefinitionParserDelegate delegate) {
		String namespaceUri = getNamespaceURI(element);
		NamespaceHandler handler = new DefaultNamespaceHandlerResolverX().resolve(namespaceUri);
		if (handler == null) {
			System.out.println("Unable to locate Spring NamespaceHandler for XML schema namespace [" + namespaceUri + "]");
			return null;
		}
		return handler.parse(element, new ParserContext(null,delegate));
	}
	
	/**
	 * 默认原始
	 * @param element
	 */
	private BeanDefinition parseDefaultElement(Element element, BeanDefinitionParserDelegate delegate)  throws ClassNotFoundException {
		System.out.println("---默认标签--- getNamespaceURI : " + element.getNamespaceURI()+" , getLocalName : " + element.getLocalName()+" , getPrefix : "
				+ element.getPrefix()+" , getTagName : " + element.getTagName()+" , getNodeName : " + element.getNodeName());
		
		// 获取节点的子节点
		NodeList nl = element.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node nodeTemp = nl.item(i);
			if (nodeTemp instanceof Element) {
				Element ele = (Element) nodeTemp;
				System.out.println(" - getNamespaceURI : " + ele.getNamespaceURI()+" , getLocalName : " + ele.getLocalName()+" , getPrefix : "
							+ ele.getPrefix()+" , getTagName : " + ele.getTagName()+" , getNodeName : " + ele.getNodeName());
			}
		}
		
		if(true){
			return null;
		}
		
		// ---------------------------解析一个bean标签，装饰一次---------------------------------------
		if (delegate.nodeNameEquals(element, BeanDefinitionParserDelegate.BEAN_ELEMENT)) { // bean标签需要走
			
			BeanDefinitionHolder bdHolder = null;
			{
				String id = element.getAttribute(BeanDefinitionParserDelegate.ID_ATTRIBUTE);
				String nameAttr = element.getAttribute(BeanDefinitionParserDelegate.NAME_ATTRIBUTE);
				
				List<String> aliases = new ArrayList<String>();
				if (StringUtils.hasLength(nameAttr)) {
					String[] nameArr = StringUtils.tokenizeToStringArray(nameAttr, BeanDefinitionParserDelegate.MULTI_VALUE_ATTRIBUTE_DELIMITERS);
					aliases.addAll(Arrays.asList(nameArr));
				}
				
				String beanName = id;
				
				String className = null;
				if (element.hasAttribute(BeanDefinitionParserDelegate.CLASS_ATTRIBUTE)) {
					className = element.getAttribute(BeanDefinitionParserDelegate.CLASS_ATTRIBUTE).trim();
				}
				GenericBeanDefinition bd = new GenericBeanDefinition();
				bd.setBeanClass(ClassUtils.forName(className, this.getClass().getClassLoader()));
				bd.setDescription(DomUtils.getChildElementValueByTagName(element, BeanDefinitionParserDelegate.DESCRIPTION_ELEMENT));
				
				if (element.hasAttribute(BeanDefinitionParserDelegate.FACTORY_METHOD_ATTRIBUTE)) {
					bd.setFactoryMethodName(element.getAttribute(BeanDefinitionParserDelegate.FACTORY_METHOD_ATTRIBUTE));
				}
				if (element.hasAttribute(BeanDefinitionParserDelegate.FACTORY_BEAN_ATTRIBUTE)) {
					bd.setFactoryBeanName(element.getAttribute(BeanDefinitionParserDelegate.FACTORY_BEAN_ATTRIBUTE));
				}
				
				String[] aliasesArray = StringUtils.toStringArray(aliases);
				bdHolder = new BeanDefinitionHolder(bd, beanName, aliasesArray);
			}
			
			
			// ---- 装饰 -----
			{
				BeanDefinitionHolder finalDefinition = bdHolder;
				
				// Decorate based on custom attributes first.
				NamedNodeMap attributes = element.getAttributes();
				for (int i = 0; i < attributes.getLength(); i++) {
					Node node = attributes.item(i);
					finalDefinition = decorateIfRequired(node, finalDefinition, delegate);
				}

				// Decorate based on custom nested elements.
				NodeList children = element.getChildNodes();
				for (int i = 0; i < children.getLength(); i++) {
					Node node = children.item(i);
					if (node.getNodeType() == Node.ELEMENT_NODE) {
						finalDefinition = decorateIfRequired(node, finalDefinition, delegate);
					}
				}
			}
			
			// 装饰完成，把bean信息添加到注册表中
//			BeanDefinitionReaderUtils.registerBeanDefinition(bdHolder, getReaderContext().getRegistry());
		}
		
		return null;
	}
	
	/**
	 * 装饰
	 */
	public BeanDefinitionHolder decorateIfRequired(
			Node node, BeanDefinitionHolder originalDef, BeanDefinitionParserDelegate delegate) {
		String namespaceUri = getNamespaceURI(node);
		NamespaceHandler handler = new DefaultNamespaceHandlerResolverX().resolve(namespaceUri);
		if (handler == null) {
			System.out.println("Unable to locate Spring NamespaceHandler for XML schema namespace [" + namespaceUri + "]");
			return null;
		}
		return handler.decorate(node, originalDef, new ParserContext(null,delegate));
	}
	
	// --------------------------------
	public boolean isDefaultNamespace(Node node) {
		return isDefaultNamespace(getNamespaceURI(node));
	}
	public String getNamespaceURI(Node node) {
		return node.getNamespaceURI();
	}
	public static final String BEANS_NAMESPACE_URI = "http://www.janchou.org/schema/mock/mybeanstag";
	public boolean isDefaultNamespace(String namespaceUri) {
		return (!hasLength(namespaceUri) || BEANS_NAMESPACE_URI.equals(namespaceUri));
	}
	public static boolean hasLength(String str) {
		return hasLength((CharSequence) str);
	}
	public static boolean hasLength(CharSequence str) {
		return (str != null && str.length() > 0);
	}
}
