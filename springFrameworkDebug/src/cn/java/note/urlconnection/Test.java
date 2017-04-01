package cn.java.note.urlconnection;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.util.ResourceUtils;

public class Test {
	private static final String XML_FILE_EXTENSION = ".xml";
	
	public static void main(String[] args) throws Exception {
		String resourceName = "test.properties";
		Enumeration<URL> urls = ClassLoader.getSystemResources(resourceName)		;
		Properties props = new Properties();
		while (urls.hasMoreElements()) {
			URL url = urls.nextElement();
			URLConnection con = url.openConnection();
			ResourceUtils.useCachesIfNecessary(con);
			InputStream is = con.getInputStream();
			try {
				if (resourceName.endsWith(XML_FILE_EXTENSION)) {
					props.loadFromXML(is);
				}
				else {
					props.load(is);
				}
			}
			finally {
				is.close();
			}
		}
		Map<String, Object> handlerMappings = new ConcurrentHashMap<String, Object>(props.size());
		
	}

}
