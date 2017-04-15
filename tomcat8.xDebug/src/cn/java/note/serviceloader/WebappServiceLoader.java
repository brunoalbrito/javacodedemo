package cn.java.note.serviceloader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * 识别 jar包或者类路径中
 * @author Administrator
 *
 * @param <T>
 */
public class WebappServiceLoader<T>  {
	
	/**
	 * 加载类
	 */
	public List<T> load(Class<T> serviceType) throws IOException {
		LinkedHashSet<String> servicesFound = new LinkedHashSet<>();
		
		// 查找要初始化的类
		servicesFound = this.checkAndParseFile();

		// 加载类、创建对象、转成某种类型的引用
		ClassLoader loader = this.getClass().getClassLoader();
		List<T> services = new ArrayList<>(servicesFound.size());
		for (String serviceClass : servicesFound) {
			try {
				Class<?> clazz = Class.forName(serviceClass, true, loader);
				services.add(serviceType.cast(clazz.newInstance())); // 创建对象,并转成serviceType类型的引用
			} catch (ClassNotFoundException | InstantiationException |
					IllegalAccessException | ClassCastException e) {
				throw new IOException(e);
			}
		}
		return Collections.unmodifiableList(services);
	}

	
	/**
	 * 在类路径中查找 “META-INF/services/javax.servlet.ServletContainerInitializer” 文件
	 * 并解析其中的文件内容
	 */
	private LinkedHashSet<String> checkAndParseFile() throws IOException{
		LinkedHashSet<String> servicesFound = new LinkedHashSet<>();
		String configFile = "META-INF/services/javax.servlet.ServletContainerInitializer";
		Enumeration<URL> resources = ClassLoader.getSystemResources(configFile); 
		// 解析配置文件
		while (resources.hasMoreElements()) {
			URL url = resources.nextElement();
			try (InputStream is = url.openStream();
					InputStreamReader in = new InputStreamReader(is, StandardCharsets.UTF_8);
					BufferedReader reader = new BufferedReader(in);) {
				String line;
				while ((line = reader.readLine()) != null) {
					int i = line.indexOf('#');
					if (i >= 0) {
						line = line.substring(0, i);
					}
					line = line.trim();
					if (line.length() == 0) {
						continue;
					}
					servicesFound.add(line);
				}
			}
		}
//    	servicesFound = [
//	       org.apache.jasper.servlet.JasperInitializer
//	       org.apache.tomcat.websocket.server.WsSci             
//		]
		
		{ // mock
			servicesFound.add("cn.java.note.serviceloader.JasperInitializer");
			servicesFound.add("cn.java.note.serviceloader.WsSci");
		}
		return servicesFound;
	}

}
