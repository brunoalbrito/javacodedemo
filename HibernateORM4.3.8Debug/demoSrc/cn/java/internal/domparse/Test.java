package cn.java.internal.domparse;

import java.io.File;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Iterator;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.hibernate.MappingException;
import org.hibernate.cfg.EJB3DTDEntityResolver;
import org.hibernate.internal.util.xml.ErrorLogger;
import org.hibernate.internal.util.xml.Origin;
import org.hibernate.internal.util.xml.OriginImpl;
import org.hibernate.internal.util.xml.XMLHelper;
import org.hibernate.internal.util.xml.XmlDocument;
import org.hibernate.internal.util.xml.XmlDocumentImpl;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

public class Test {

	public static void main(String[] args) throws Exception {
		
		System.out.println("-------解析文件hibernate.cfg.xml-------");
		{
			parseHibernateCfgDotXmlWithDom4j();
			parseHibernateCfgDotXmlWithDom4jWrap();
		}
		
		System.out.println("-------解析文件student.hbm.xml-------");
		{
			parseHbmDotXmlWithDom4j();
		}
	}

	/**
	 * 解析文件hibernate.cfg.xml -- 全局配置文件
	 * 
	 * @throws Exception
	 */
	public static void parseHibernateCfgDotXmlWithDom4j() throws Exception {
		// 获取文件输入流
		String resourceName = File.separator + getPackageDir() + File.separator + "hibernate.cfg.xml";
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream stream = classLoader.getResourceAsStream(resourceName);

		// 配置解析器
		SAXReader saxReader = new SAXReader();
		saxReader.setMergeAdjacentText(true);
		saxReader.setValidation(true);
		ErrorLogger errorLogger = new ErrorLogger(resourceName); // 错误处理器
		EntityResolver entityResolver = new EJB3DTDEntityResolver(); // 解析器
		saxReader.setEntityResolver(entityResolver);
		saxReader.setErrorHandler(errorLogger);

		// 进行解析
		Document document = saxReader.read(new InputSource(stream));
		if (errorLogger.hasErrors()) {
			throw new MappingException("invalid configuration", errorLogger.getErrors().get(0));
		}
		printDocumentTree(document);
	}

	/**
	 * 打印文档树
	 * 
	 * @param document
	 */
	private static void printDocumentTree(Document document) {

		//		  <session-factory> 
		//			  <property name="" value="" /> 
		//			  <property name="" value="" /> 
		//			  <property name="" value="" /> 
		//			  <mapping /> 
		//			  <class-cache />
		//			  <collection-cache /> 
		//		  </session-factory>
		Element sfNode = document.getRootElement().element("session-factory");
		Iterator itr = sfNode.elementIterator("property");
		while (itr.hasNext()) {
			Element node = (Element) itr.next();
			String name = node.attributeValue("name");
			String value = node.getText().trim();
			System.out.println("name = " + name + " , value = " + value);
		}

		Iterator elements = sfNode.elementIterator();
		while (elements.hasNext()) {
			Element subelement = (Element) elements.next();
			String subelementName = subelement.getName();
			if ("mapping".equals(subelementName)) {

			} else if ("class-cache".equals(subelementName)) {

			} else if ("collection-cache".equals(subelementName)) {

			}
		}
	}

	/**
	 * 解析文件hibernate.cfg.xml -- 全局配置文件
	 * 
	 * @throws Exception
	 */
	public static void parseHibernateCfgDotXmlWithDom4jWrap() throws Exception {
		// 获取文件输入流
		String resourceName = File.separator + getPackageDir() + File.separator + "hibernate.cfg.xml";
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream stream = classLoader.getResourceAsStream(resourceName);

		// 配置解析器、进行解析
		XMLHelper xmlHelper = new XMLHelper();
		ErrorLogger errorLogger = new ErrorLogger(resourceName); // 错误处理器
		EntityResolver entityResolver = new EJB3DTDEntityResolver(); // 解析器
		Document document = xmlHelper.createSAXReader(errorLogger, entityResolver).read(new InputSource(stream));
		if (errorLogger.hasErrors()) {
			throw new MappingException("invalid configuration", errorLogger.getErrors().get(0));
		}
		printDocumentTree(document);
	}

	/**
	 * 解析文件student.hbm.xml --- 映射文件
	 * 
	 * @throws Exception
	 */
	public static void parseHbmDotXmlWithDom4j() throws Exception {
		String originType = "resource";
		String resourceName = File.separator + getPackageDir() + File.separator + "student.hbm.xml";
		String originName = resourceName;

		// 获取文件输入流
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream inputStream = classLoader.getResourceAsStream(resourceName);
		InputSource inputSource = new InputSource(inputStream);
		Origin origin = new OriginImpl(originType, originName);

		// 定义xml文件读取器
		EntityResolver entityResolver = new EJB3DTDEntityResolver(); // 解析器
		ErrorLogger errorHandler = new ErrorLogger(); // 错误处理器
		SAXReader saxReader = new SAXReader();
		saxReader.setEntityResolver(entityResolver);
		saxReader.setErrorHandler(errorHandler);
		saxReader.setMergeAdjacentText(true);
		saxReader.setValidation(true);
		Document document = null;
		XmlDocument metadataXml = null;
		try { // 尝试第一次
			// 设置校验规则
			saxReader.setFeature("http://apache.org/xml/features/validation/schema", true);
			saxReader.setProperty("http://apache.org/xml/properties/schema/external-schemaLocation", "http://xmlns.jcp.org/xml/ns/persistence/orm orm_2_1.xsd");

			// 解析
			document = saxReader.read(inputSource);
			metadataXml = new XmlDocumentImpl(document, origin.getType(), origin.getName());
		} catch (Exception e) { 
			System.out.println("e : " + e);
			errorHandler.reset();
			if (document != null) {
				try { // 尝试第二次
					saxReader.setFeature("http://apache.org/xml/features/validation/schema", true);
					saxReader.setProperty("http://apache.org/xml/properties/schema/external-schemaLocation", "http://java.sun.com/xml/ns/persistence/orm orm_2_0.xsd");
					document = saxReader.read(new StringReader(document.asXML()));
					if (errorHandler.hasErrors()) {
						errorHandler.logErrors();
						throw errorHandler.getErrors().get(0);
					}
					metadataXml = new XmlDocumentImpl(document, origin.getType(), origin.getName());
				} catch (Exception e2) {
					System.out.println("e2 : " + e2);
					errorHandler.reset();
					if (document != null) {
						try { // 尝试第三次
							saxReader.setFeature("http://apache.org/xml/features/validation/schema", true);
							saxReader.setProperty("http://apache.org/xml/properties/schema/external-schemaLocation", "http://java.sun.com/xml/ns/persistence/orm orm_1_0.xsd");
							document = saxReader.read(new StringReader(document.asXML()));
							if (errorHandler.hasErrors()) {
								errorHandler.logErrors();
								throw errorHandler.getErrors().get(0);
							}
							metadataXml = new XmlDocumentImpl(document, origin.getType(), origin.getName());
						} catch (Exception e3) {
							System.out.println("e3 : " + e3);
						}
					}
				}

			}
		}
		printHbmDotXml(metadataXml);

	}

	private static void printHbmDotXml(XmlDocument metadataXml) {
		if (metadataXml == null) {
			return;
		}
		//			  <hibernate-mapping package="cn.java.bean"> 
		//				  <class name="cn.java.demo.entity.Student"></class> 
		//				  <subclass></subclass>
		//				  <joined-subclass></joined-subclass>
		//				  <union-subclass></union-subclass> 
		//			  </hibernate-mapping>
		Document document = metadataXml.getDocumentTree();
		Element hmNode = document.getRootElement();
		Attribute packNode = hmNode.attribute("package");
		String defaultPackage = packNode != null ? packNode.getValue() : "";
		System.out.println("defaultPackage : " + defaultPackage);
		{
			Iterator[] classes = new Iterator[4];
			classes[0] = hmNode.elementIterator("class");
			classes[1] = hmNode.elementIterator("subclass");
			classes[2] = hmNode.elementIterator("joined-subclass");
			classes[3] = hmNode.elementIterator("union-subclass");
		}
	}

	private static String getPackageDir() {
		Class clazz = Test.class;
		String dirName = clazz.getName();
		dirName = dirName.substring(0, dirName.length() - (clazz.getSimpleName().length() + 1));
		dirName = dirName.replace(".", File.separator);
		return dirName;
	}

}
