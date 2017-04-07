package cn.java.test.xml.mytag;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class MytagEntityResolver implements EntityResolver {
	/** Suffix for DTD files */
	public static final String DTD_SUFFIX = ".dtd";
	/** Suffix for schema definition files */
	public static final String XSD_SUFFIX = ".xsd";

	@Override
	public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
		
		// "/lib/spring-beans-4.3.6.RELEASE.jar/META-INF/spring.schemas"
		String resourceName = "cn/java/test/xml/mytag/META-INF/mytag.schemas";
		InputSource source = null;
		if (systemId != null) {
			if (systemId.endsWith(DTD_SUFFIX)) {
				return null;
			} else if (systemId.endsWith(XSD_SUFFIX)) {
				// 读取属性文件
				Enumeration<URL> urls = MytagEntityResolver.class.getClassLoader().getResources(resourceName); // 
				Properties mappings = new Properties();
				while (urls.hasMoreElements()) {
					URL url = urls.nextElement();
					URLConnection con = url.openConnection();
					InputStream is = con.getInputStream();
					try {
						if (resourceName.endsWith(".xml")) {
							mappings.loadFromXML(is);
						} else {
							mappings.load(is);
						}
					} finally {
						is.close();
					}
				}
				// 把Properties对象中的数据放入ConcurrentHashMap
				Map<String, String> schemaMappings = new ConcurrentHashMap<String, String>(mappings.size());
				if (mappings != null) {
					for (Enumeration<?> en = mappings.propertyNames(); en.hasMoreElements();) {
						String key = (String) en.nextElement();
						Object value = mappings.get(key);
						if (value == null) {
							value = mappings.getProperty(key);
						}
						schemaMappings.put((String) key, (String) value);
					}
				}
				// 获取指定key的值
				// systemId == "http://www.janchou.org/schema/mytag/spring-mytag-3.0.xsd"
				// resourceLocation = "cn/java/test/xml/mytag/schemas/spring-mytag-3.0.xsd"
				String resourceLocation = schemaMappings.get(systemId); 
				// 读取xsd文件
				InputStream inputStream = MytagEntityResolver.class.getClassLoader().getResourceAsStream(resourceLocation);
				source = new InputSource(inputStream);
				source.setPublicId(publicId);
				source.setSystemId(systemId);
				if(inputStream!=null){
					System.out.println("publicId="+publicId + ",systemId="+ systemId+ ",resourceLocation="+ resourceLocation);
				}
			}
		}

		if (source == null && systemId != null) { // 没有识别到
			String resourcePath = null;
			try {
				String decodedSystemId = URLDecoder.decode(systemId, "UTF-8");
				String givenUrl = new URL(decodedSystemId).toString();
				String systemRootUrl = new File("").toURI().toURL().toString();
				// Try relative to resource base if currently in system root.
				if (givenUrl.startsWith(systemRootUrl)) {
					resourcePath = givenUrl.substring(systemRootUrl.length());
				}
			} catch (Exception ex) {
				// No URL (or no resolvable URL) -> try relative to resource
				// base.
				resourcePath = systemId;
			}

			if (resourcePath != null) {
				source = new InputSource(MytagEntityResolver.class.getResourceAsStream(resourcePath));
				source.setPublicId(publicId);
				source.setSystemId(systemId);
			}
		}
		return source;
	}

}
