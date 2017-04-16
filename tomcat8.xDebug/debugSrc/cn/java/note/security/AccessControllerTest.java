package cn.java.note.security;

import java.io.IOException;
import java.io.InputStream;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.catalina.ContainerServlet;
import org.apache.catalina.core.DefaultInstanceManager;

public class AccessControllerTest {

	private static boolean packageDefinitionEnabled = (System.getProperty("package.definition") == null && System.getProperty("package.access") == null) ? false : true;
	protected static boolean privileged = false; // 是否有特权
	private static Set<String> restrictedClasses = new HashSet<String>(); // 受限制的类

	public static void main(String[] args) throws Exception {
		Set<String> classNames = new HashSet<>();
		/**
		 	org/apache/catalina/core/RestrictedServlets.properties 文件声明被限制的类：
		 	restricted=cn.java.test.ClassName1
		 	restricted=cn.java.test.ClassName2
		 	restricted=cn.java.test.ClassName3
		 */
		loadProperties(classNames, "org/apache/catalina/core/RestrictedServlets.properties", "defaultInstanceManager.restrictedServletsResource");
		loadProperties(classNames, "org/apache/catalina/core/RestrictedListeners.properties", "defaultInstanceManager.restrictedListenersResource");
		loadProperties(classNames, "org/apache/catalina/core/RestrictedFilters.properties", "defaultInstanceManager.restrictedFiltersResource");
		restrictedClasses = Collections.unmodifiableSet(classNames);
		
		// 由于在cn.java.test.ClassName1中声明限制，所以以下调用会出错
		loadClassMaybePrivileged("cn.java.test.ClassName1",AccessControllerTest.class.getClassLoader());
		// 由于在cn.java.test.ClassName1中没有声明限制，所以以下调用不会出错
		loadClassMaybePrivileged("java.lang.String",AccessControllerTest.class.getClassLoader());
	}

	protected static Class<?> loadClass(String className, ClassLoader classLoader) throws ClassNotFoundException {
		return classLoader.loadClass(className);
	}

	protected static Class<?> loadClassMaybePrivileged(final String className, final ClassLoader classLoader) throws ClassNotFoundException {
		Class<?> clazz;
		if (AccessControllerTest.packageDefinitionEnabled) {
			try {
				clazz = AccessController.doPrivileged(new PrivilegedExceptionAction<Class<?>>() {
					@Override
					public Class<?> run() throws Exception {
						return loadClass(className, classLoader);
					}
				});
			} catch (PrivilegedActionException e) {
				Throwable t = e.getCause();
				if (t instanceof ClassNotFoundException) {
					throw (ClassNotFoundException) t;
				}
				throw new RuntimeException(t);
			}
		} else {
			// 加载类到内存
			clazz = loadClass(className, classLoader);
		}
		// 检查访问权限
		checkAccess(clazz);
		return clazz;
	}

	private static void checkAccess(Class<?> clazz) {
		if (privileged) { // 特权化的（有特权）直接返回
			return;
		}
		if (ContainerServlet.class.isAssignableFrom(clazz)) {
			throw new SecurityException("defaultInstanceManager.restrictedContainerServlet" + clazz);
		}
		while (clazz != null) {
			if (restrictedClasses.contains(clazz.getName())) {
				throw new SecurityException("defaultInstanceManager.restrictedClass" + clazz);
			}
			clazz = clazz.getSuperclass();
		}
	}

	private static void loadProperties(Set<String> classNames, String resourceName, String messageKey) {
		Properties properties = new Properties();
		ClassLoader cl = DefaultInstanceManager.class.getClassLoader();
		try (InputStream is = cl.getResourceAsStream(resourceName)) {
			if (is == null) {
				System.out.println((messageKey + resourceName));
			} else {
				properties.load(is);
			}
		} catch (IOException ioe) {
			System.out.println((messageKey + resourceName) + ioe);
		}
		if (properties.isEmpty()) {
			return;
		}
		for (Map.Entry<Object, Object> e : properties.entrySet()) {
			if ("restricted".equals(e.getValue())) {
				classNames.add(e.getKey().toString());
			} else {
				System.out.println("defaultInstanceManager.restrictedWrongValue" + resourceName + e.getKey() + e.getValue());
			}
		}
	}

}
