package cn.java.debug.note;

import java.io.InputStream;

import javax.persistence.SequenceGenerator;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.hibernate.MappingException;
import org.hibernate.annotations.common.reflection.ClassLoaderDelegate;
import org.hibernate.annotations.common.reflection.MetadataProvider;
import org.hibernate.annotations.common.reflection.MetadataProviderInjector;
import org.hibernate.annotations.common.reflection.ReflectionManager;
import org.hibernate.annotations.common.reflection.XPackage;
import org.hibernate.annotations.common.reflection.java.JavaReflectionManager;
import org.hibernate.annotations.common.util.StandardClassLoaderDelegateImpl;
import org.hibernate.cfg.EJB3DTDEntityResolver;
import org.hibernate.cfg.annotations.reflection.JPAMetadataProvider;
import org.hibernate.internal.util.xml.ErrorLogger;
import org.hibernate.internal.util.xml.Origin;
import org.hibernate.internal.util.xml.OriginImpl;
import org.hibernate.internal.util.xml.XMLHelper;
import org.hibernate.internal.util.xml.XmlDocument;
import org.hibernate.internal.util.xml.XmlDocumentImpl;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

public class DomParseTest {

	public static void main(String[] args) throws Exception {

	}

	/**
	 * 解析文件hibernate.cfg.xml  -- 全局配置文件
	 * @throws Exception
	 */
	public static void testDom4j(String[] args) throws Exception {
		// 获取文件输入流
		String resourceName = "/hibernate.cfg.xml";
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream stream = classLoader.getResourceAsStream("hibernate.cfg.xml");

		// 配置解析器
		SAXReader saxReader = new SAXReader();
		saxReader.setMergeAdjacentText( true );
		saxReader.setValidation( true );
		ErrorLogger errorLogger = new ErrorLogger(resourceName); // 错误处理器
		EntityResolver entityResolver = new EJB3DTDEntityResolver(); // 解析器
		saxReader.setEntityResolver( entityResolver );
		saxReader.setErrorHandler( errorLogger );

		// 进行解析
		Document document = saxReader.read( new InputSource( stream ) );
		if (errorLogger.hasErrors()) {
			throw new MappingException("invalid configuration", errorLogger.getErrors().get(0));
		}
	}
	
	/**
	 * 解析文件hibernate.cfg.xml  -- 全局配置文件
	 * @throws Exception
	 */
	public static void parseHibernateCfgDotXml() throws Exception {
		// 获取文件输入流
		String resourceName = "/hibernate.cfg.xml";
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream stream = classLoader.getResourceAsStream("hibernate.cfg.xml");

		// 配置解析器、进行解析
		XMLHelper xmlHelper = new XMLHelper();
		ErrorLogger errorLogger = new ErrorLogger(resourceName); // 错误处理器
		EntityResolver entityResolver = new EJB3DTDEntityResolver(); // 解析器
		Document document = xmlHelper.createSAXReader(errorLogger, entityResolver).read(new InputSource(stream));
		if (errorLogger.hasErrors()) {
			throw new MappingException("invalid configuration", errorLogger.getErrors().get(0));
		}
	}


	/**
	 * 解析文件student.hbm.xml  --- 映射文件
	 * @throws Exception
	 */
	public static void parseHbmDotXml() throws Exception {
		String originType = "resource";
		String resourceName = "cn/java/hibernate/entity/student.hbm.xml";
		String originName = resourceName;

		// 获取文件输入流
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream inputStream = classLoader.getResourceAsStream( resourceName );
		Origin origin = new OriginImpl( originType, originName );
		InputSource inputSource = new InputSource( inputStream );

		// 定义xml文件读取器
		ErrorLogger errorHandler = new ErrorLogger(); // 错误处理器
		EntityResolver entityResolver = new EJB3DTDEntityResolver(); // 解析器
		SAXReader saxReader = new SAXReader();
		saxReader.setEntityResolver( entityResolver );
		saxReader.setErrorHandler( errorHandler );
		saxReader.setMergeAdjacentText( true );
		saxReader.setValidation( true );

		// 设置校验规则
		saxReader.setFeature( "http://apache.org/xml/features/validation/schema", true );
		saxReader.setProperty(
				"http://apache.org/xml/properties/schema/external-schemaLocation",
				"http://xmlns.jcp.org/xml/ns/persistence/ormorm_2_1.xsd"
				);

		// 解析
		Document document = saxReader.read( inputSource );
		XmlDocument metadataXml =  new XmlDocumentImpl( document, origin.getType(), origin.getName() );
	}


}
