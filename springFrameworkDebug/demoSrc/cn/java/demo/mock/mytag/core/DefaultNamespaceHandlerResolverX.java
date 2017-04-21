package cn.java.demo.mock.mytag.core;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.xml.NamespaceHandler;

public class DefaultNamespaceHandlerResolverX {
	private final String handlerMappingsLocation = "cn/java/demo/mock/mytag/schemas/META-INF/mytag.handlers";
	public NamespaceHandler resolve(String namespaceUri) {
		Map<String, Object> handlerMappings = getHandlerMappings();
		Object handlerOrClassName = handlerMappings.get(namespaceUri); // !!! 获取指定命名空间的处理器
		if (handlerOrClassName == null) {
			return null;
		}
		else if (handlerOrClassName instanceof NamespaceHandler) { // 已经是对象了
			return (NamespaceHandler) handlerOrClassName;
		}
		else { // 实例化
			String className = (String) handlerOrClassName;
			try {
				Class<?> handlerClass = Class.forName(className);
				if (!NamespaceHandler.class.isAssignableFrom(handlerClass)) {
					throw new RuntimeException("Class [" + className + "] for namespace [" + namespaceUri +
							"] does not implement the [" + NamespaceHandler.class.getName() + "] interface");
				}
				Constructor ctor = handlerClass.getDeclaredConstructor();
				if ((!Modifier.isPublic(ctor.getModifiers()) ||
						!Modifier.isPublic(ctor.getDeclaringClass().getModifiers())) && !ctor.isAccessible()) {
					ctor.setAccessible(true);
				}
				NamespaceHandler namespaceHandler = (NamespaceHandler) ctor.newInstance();// 实例化
				namespaceHandler.init(); // 处理器进行初始化
				handlerMappings.put(namespaceUri, namespaceHandler);
				return namespaceHandler;
			}
			catch (ClassNotFoundException ex) {
				throw new RuntimeException("NamespaceHandler class [" + className + "] for namespace [" +
						namespaceUri + "] not found", ex);
			}
			catch (LinkageError err) {
				throw new RuntimeException("Invalid NamespaceHandler class [" + className + "] for namespace [" +
						namespaceUri + "]: problem with handler class file or dependent class", err);
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage(), e);
			} 
		}
	}
	private Map<String, Object> getHandlerMappings() {
		synchronized (this) {
			try {
				// http://www.springframework.org/schema/c=org.springframework.beans.factory.xml.SimpleConstructorNamespaceHandler
				// http://www.springframework.org/schema/p=org.springframework.beans.factory.xml.SimplePropertyNamespaceHandler
				// http://www.springframework.org/schema/util=org.springframework.beans.factory.xml.UtilNamespaceHandler
				Properties mappings = loadAllProperties(this.handlerMappingsLocation, null); // "META-INF/spring.handlers"
				Map<String, Object> handlerMappings = new ConcurrentHashMap<String, Object>(mappings.size());
				mergePropertiesIntoMap(mappings, handlerMappings);
				return handlerMappings;
			} catch (IOException ex) {
				throw new IllegalStateException(
						"Unable to load NamespaceHandler mappings from location [" + this.handlerMappingsLocation + "]",
						ex);
			}
		}

	}

	public static <K, V> void mergePropertiesIntoMap(Properties props, Map<K, V> map) {
		if (map == null) {
			throw new IllegalArgumentException("Map must not be null");
		}
		if (props != null) {
			for (Enumeration<?> en = props.propertyNames(); en.hasMoreElements();) {
				String key = (String) en.nextElement();
				Object value = props.get(key);
				if (value == null) {
					// Allow for defaults fallback or potentially overridden
					// accessor...
					value = props.getProperty(key);
				}
				map.put((K) key, (V) value);
			}
		}
	}

	public static Properties loadAllProperties(String resourceName, ClassLoader classLoader) throws IOException {
		ClassLoader classLoaderToUse = classLoader;
		if (classLoaderToUse == null) {
			classLoaderToUse = DefaultNamespaceHandlerResolverX.class.getClassLoader();
		}
		Enumeration<URL> urls = (classLoaderToUse != null ? classLoaderToUse.getResources(resourceName)
				: ClassLoader.getSystemResources(resourceName)); // "META-INF/spring.handlers"
		Properties props = new Properties();
		while (urls.hasMoreElements()) {
			URL url = urls.nextElement();
			URLConnection con = url.openConnection();
			con.setUseCaches(con.getClass().getSimpleName().startsWith("JNLP")); // 使用本地缓存，如果需要
			InputStream is = con.getInputStream();
			try {
				if (resourceName.endsWith(".xml")) {
					props.loadFromXML(is);
				} else {
					props.load(is);
				}
			} finally {
				is.close();
			}
		}
		return props;
	}

}
