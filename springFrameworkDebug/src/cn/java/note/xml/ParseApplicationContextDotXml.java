package cn.java.note.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.xml.BeansDtdResolver;
import org.springframework.beans.factory.xml.PluggableSchemaResolver;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.XmlValidationModeDetector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class ParseApplicationContextDotXml {
	
	public static void main(String[] args) throws Exception {
		test();
	}
	
	/**
	 * 测试
	 * 
	 * @throws Exception
	 */
	public static void test() throws Exception {
		DefaultDocumentLoader defaultDocumentLoader = new DefaultDocumentLoader();
		
		
		File file = new File("/WEB-INF/applicationContext.xml");
		InputStream inputStream = new FileInputStream(file);
		InputSource inputSource = new InputSource(inputStream);
		
		// ------1------ 创建文档构建器工厂
		DocumentBuilderFactory factory = defaultDocumentLoader.createDocumentBuilderFactory(XmlValidationModeDetector.VALIDATION_NONE,false);

		// ------2------创建文档构建器
//		EntityResolver entityResolver = new ResourceEntityResolver(XmlWebApplicationContext);
		EntityResolver entityResolver = new ResourceEntityResolver(null);
		ErrorHandler errorHandler = new SimpleSaxErrorHandler(LogFactory.getLog(ParseApplicationContextDotXml.class.getClass()));
		DocumentBuilder builder = defaultDocumentLoader.createDocumentBuilder(factory, entityResolver, errorHandler);

		// ------4------创建“解析bean定义的委托”
		Document doc = builder.parse(inputSource); // 解析
		Element root = doc.getDocumentElement();
		BeanDefinitionParserDelegate delegate = new BeanDefinitionParserDelegate();
		delegate.initDefaults(root, null);
		
		// ------4------使用“解析bean定义的委托”解析文档
		defaultDocumentLoader.parseBeanDefinitions(root, delegate);
	}


	private static class DefaultDocumentLoader {
		
		protected final Log logger = LogFactory.getLog(getClass());
		/**
		 * JAXP attribute used to configure the schema language for validation.
		 */
		private static final String SCHEMA_LANGUAGE_ATTRIBUTE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";

		/**
		 * JAXP attribute value indicating the XSD schema language.
		 */
		private static final String XSD_SCHEMA_LANGUAGE = "http://www.w3.org/2001/XMLSchema";
		
		/**
		 * 创建文档构建器
		 */
		protected DocumentBuilder createDocumentBuilder(DocumentBuilderFactory factory, EntityResolver entityResolver,
				ErrorHandler errorHandler) throws ParserConfigurationException {

			DocumentBuilder docBuilder = factory.newDocumentBuilder();
			if (entityResolver != null) {
				docBuilder.setEntityResolver(entityResolver);
			}
			if (errorHandler != null) {
				docBuilder.setErrorHandler(errorHandler);
			}
			return docBuilder;
		}

		/**
		 * 创建文档构建器工厂
		 * @param validationMode
		 * @param namespaceAware
		 * @return
		 * @throws ParserConfigurationException
		 */
		protected DocumentBuilderFactory createDocumentBuilderFactory(int validationMode, boolean namespaceAware) throws ParserConfigurationException{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(namespaceAware);
			if (validationMode != XmlValidationModeDetector.VALIDATION_NONE) {
				factory.setValidating(true);
				if (validationMode == XmlValidationModeDetector.VALIDATION_XSD) {
					// Enforce namespace aware for XSD...
					factory.setNamespaceAware(true);
					try {
//						factory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
						factory.setAttribute(SCHEMA_LANGUAGE_ATTRIBUTE, XSD_SCHEMA_LANGUAGE);
					}
					catch (IllegalArgumentException ex) {
						ParserConfigurationException pcex = new ParserConfigurationException(
								"Unable to validate using XSD: Your JAXP provider [" + factory +
								"] does not support XML Schema. Are you running on Java 1.4 with Apache Crimson? " +
								"Upgrade to Apache Xerces (or Java 1.5) for full XSD support.");
						pcex.initCause(ex);
						throw pcex;
					}
				}
			}

			return factory;
		}
		
		
		/**
		 * 解析dom元素
		 * 
		 * @param root
		 * @param delegate
		 */
		protected void parseBeanDefinitions(Element root, BeanDefinitionParserDelegate delegate) {
			if (delegate.isDefaultNamespace(root)) { // org.springframework.beans.factory.xml.BeanDefinitionParserDelegate
				NodeList nl = root.getChildNodes();
				for (int i = 0; i < nl.getLength(); i++) {
					Node node = nl.item(i);
					if (node instanceof Element) {
						Element ele = (Element) node;
						if (delegate.isDefaultNamespace(ele)) {
							// parseDefaultElement(ele, delegate); // !!! 默认元素
						} else { // 嵌套自定义标签
							// delegate.parseCustomElement(ele); // !!! 自定义元素
						}
					}
				}
			} else { // 自定义标签
				// delegate.parseCustomElement(root);
			}
		}
	}

	/**
	 * 实体解析器（委托对象）
	 * @author zhouzhian
	 *
	 */
	private static class DelegatingEntityResolver implements EntityResolver {
		/** Suffix for DTD files */
		public static final String DTD_SUFFIX = ".dtd";

		/** Suffix for schema definition files */
		public static final String XSD_SUFFIX = ".xsd";

		private final EntityResolver dtdResolver;
		private final EntityResolver schemaResolver;

		public DelegatingEntityResolver(ClassLoader classLoader) {
			this.dtdResolver = new BeansDtdResolver();
			this.schemaResolver = new PluggableSchemaResolver(classLoader);
		}

		@Override
		public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
			if (systemId != null) {
				if (systemId.endsWith(DTD_SUFFIX)) {
					return this.dtdResolver.resolveEntity(publicId, systemId);
				} else if (systemId.endsWith(XSD_SUFFIX)) {
					return this.schemaResolver.resolveEntity(publicId, systemId);
				}
			}
			return null;
		}
	}

	/**
	 * 实体解析器
	 * @author zhouzhian
	 *
	 */
	private static class ResourceEntityResolver extends DelegatingEntityResolver {
		private static final Log logger = LogFactory.getLog(ResourceEntityResolver.class);
		private final ResourceLoader resourceLoader;

		public ResourceEntityResolver(ResourceLoader resourceLoader) {
			super(resourceLoader.getClassLoader());
			this.resourceLoader = resourceLoader;
		}

		@Override
		public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
			InputSource source = super.resolveEntity(publicId, systemId); // 调用父类的
			if (source == null && systemId != null) {
				String resourcePath = null;
				try {
					String decodedSystemId = URLDecoder.decode(systemId, "UTF-8");
					String givenUrl = new URL(decodedSystemId).toString();
					String systemRootUrl = new File("").toURI().toURL().toString();
					// Try relative to resource base if currently in system
					// root.
					if (givenUrl.startsWith(systemRootUrl)) {
						resourcePath = givenUrl.substring(systemRootUrl.length());
					}
				} catch (Exception ex) {
					// Typically a MalformedURLException or
					// AccessControlException.
					if (logger.isDebugEnabled()) {
						logger.debug("Could not resolve XML entity [" + systemId + "] against system root URL", ex);
					}
					// No URL (or no resolvable URL) -> try relative to resource
					// base.
					resourcePath = systemId;
				}
				if (resourcePath != null) {
					if (logger.isTraceEnabled()) {
						logger.trace(
								"Trying to locate XML entity [" + systemId + "] as resource [" + resourcePath + "]");
					}
					Resource resource = this.resourceLoader.getResource(resourcePath);
					source = new InputSource(resource.getInputStream());
					source.setPublicId(publicId);
					source.setSystemId(systemId);
					if (logger.isDebugEnabled()) {
						logger.debug("Found XML entity [" + systemId + "]: " + resource);
					}
				}
			}
			return source;
		}
	}

	/**
	 * 错误处理器
	 * 
	 * @author zhouzhian
	 *
	 */
	private static class SimpleSaxErrorHandler implements ErrorHandler {

		private final Log logger;

		/**
		 * Create a new SimpleSaxErrorHandler for the given Commons Logging
		 * logger instance.
		 */
		public SimpleSaxErrorHandler(Log logger) {
			this.logger = logger;
		}

		@Override
		public void warning(SAXParseException ex) throws SAXException {
			logger.warn("Ignored XML validation warning", ex);
		}

		@Override
		public void error(SAXParseException ex) throws SAXException {
			throw ex;
		}

		@Override
		public void fatalError(SAXParseException ex) throws SAXException {
			throw ex;
		}

	}

	/**
	 * 解析器（委托对象），“解析bean定义的委托”
	 * 
	 * @author zhouzhian
	 */
	private static class BeanDefinitionParserDelegate {
		public static final String DEFAULT_VALUE = "default";
		public static final String DEFAULT_AUTOWIRE_ATTRIBUTE = "default-autowire";
		public static final String AUTOWIRE_NO_VALUE = "no";
		public static final String BEANS_NAMESPACE_URI = "http://www.springframework.org/schema/beans";
		private final DocumentDefaultsDefinition defaults = new DocumentDefaultsDefinition();

		public BeanDefinitionParserDelegate() {
		}

		public void initDefaults(Element root, BeanDefinitionParserDelegate parent) {
			populateDefaults(this.defaults, (parent != null ? parent.defaults : null), root);// !!!
		}

		protected void populateDefaults(DocumentDefaultsDefinition defaults, DocumentDefaultsDefinition parentDefaults,
				Element root) {
			String autowire = root.getAttribute(DEFAULT_AUTOWIRE_ATTRIBUTE);
			if (DEFAULT_VALUE.equals(autowire)) {
				// Potentially inherited from outer <beans> sections, otherwise
				// falling back to 'no'.
				autowire = (parentDefaults != null ? parentDefaults.getAutowire() : AUTOWIRE_NO_VALUE);
			}
			defaults.setAutowire(autowire);
		}

		public String getNamespaceURI(Node node) {
			return node.getNamespaceURI();
		}

		public boolean isDefaultNamespace(String namespaceUri) {
			return (!StringUtils.hasLength(namespaceUri) || BEANS_NAMESPACE_URI.equals(namespaceUri));
		}

		public boolean isDefaultNamespace(Node node) {
			return isDefaultNamespace(getNamespaceURI(node));
		}
	}

	/**
	 * 默认定义
	 * @author zhouzhian
	 */
	private static class DocumentDefaultsDefinition {
		private String autowire;

		/**
		 * Set the default autowire setting for the document that's currently
		 * parsed.
		 */
		public void setAutowire(String autowire) {
			this.autowire = autowire;
		}

		/**
		 * Return the default autowire setting for the document that's currently
		 * parsed.
		 */
		public String getAutowire() {
			return this.autowire;
		}
	}

}
