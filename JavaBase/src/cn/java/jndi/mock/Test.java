package cn.java.jndi.mock;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.WeakHashMap;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.spi.NamingManager;

public class Test {

	private static final String APP_RESOURCE_FILE_NAME = "jndi.properties";
	private static final String JRELIB_PROPERTY_FILE_NAME = "jndi.properties";

	public static void main(String[] args) throws Exception {

		// 获取jndi资源  -- 代码未展开
		{
			Hashtable<?, ?> icEnv = new Hashtable<Object, Object>();
			Context ctx = new InitialContext(icEnv);
			Object jndiObject = ctx.lookup("java:comp/env/resource0");
		}
		
		// 获取jndi资源  -- 代码展开
		{
			Hashtable<Object,Object> myProps = (Hashtable<Object, Object>) ResourceManager.getInitialEnvironment(null);
			
			// 初始化上下文
			Context defaultInitCtx = null;
			{
				if (myProps.get(Context.INITIAL_CONTEXT_FACTORY) != null) {
					defaultInitCtx = NamingManager.getInitialContext(myProps);
				}
			}
			
			// 获取JNDI资源
			Context context = defaultInitCtx;
			{
				// ctx === com.sun.jndi.url.java.javaURLContextFactory.getObjectInstance(null, null, null, myProps);
				// ctx === org.apache.naming.java.javaURLContextFactory.getObjectInstance(null, null, null, myProps);
				Context ctx = NamingManager.getURLContext("java", myProps);  
	            if (ctx != null) {
	            	context = ctx;
	            }
			}
			Object jndiObject = context.lookup("java:comp/env/resource0"); 
		}
		
	}



	public static class ResourceManager {
		public static Hashtable<?, ?> getInitialEnvironment(
				Hashtable<?, ?> env)
						throws Exception
		{
			VersionHelper12  helper = VersionHelper12.getVersionHelper();

			if (env == null) {
	            env = new Hashtable<>(11);
	        }
			{
				String[] props = VersionHelper12.PROPS;
				String[] jndiSysProps =  helper.getJndiProperties();
				for (int i = 0; i < props.length; i++) {
					Object val = env.get(props[i]);
					if (val == null) {
						if (val == null) {
							// Read system property.
							val = jndiSysProps[i];
						}
						if (val != null) {
							((Hashtable<String, Object>)env).put(props[i], val);
						}
					}
				}
			}

			Hashtable<Object, Object> env1 = (Hashtable<Object, Object>) ResourceManager.getApplicationResources();
			ResourceManager.mergeTables((Hashtable<Object, Object>)env, env1);
			/**
			 	env = {
			 		java.naming.factory.initial : ""
				 	java.naming.factory.object : ""
				 	java.naming.factory.url.pkgs : ""  // tomcat 的jndi实现了这个
				 	java.naming.factory.state : ""
				 	java.naming.provider.url : ""
				 	java.naming.dns.url : ""
				 	java.naming.factory.control : ""
				 	....
			 	}
			 */
			return env;
		}
		
		private static Hashtable<? super String, Object> getApplicationResources()
				throws Exception {
			VersionHelper12  helper = VersionHelper12.getVersionHelper();

			{
				WeakHashMap<Object, Hashtable<? super String, Object>> propertiesCache = new WeakHashMap<>(11);

				ClassLoader cl = helper.getContextClassLoader(); // 上下文类加载器
				Hashtable<? super String, Object> result = propertiesCache.get(cl);

				{

					// <getSystemResources>/jndi.properties
					NamingEnumeration<InputStream> resources =
							helper.getResources(cl, APP_RESOURCE_FILE_NAME);
					while (resources.hasMore()) {
						Properties props = new Properties();
						InputStream istream = resources.next();
						try {
							props.load(istream);
						} finally {
							istream.close();
						}

						if (result == null) {
							result = props;
						} else {
							mergeTables(result, props);
						}
					}
				}

				{
					// Merge in properties from file in <java.home>/lib.
					// <java.home>/lib/jndi.properties
					InputStream istream =
							helper.getJavaHomeLibStream(JRELIB_PROPERTY_FILE_NAME);
					if (istream != null) {
						try {
							Properties props = new Properties();
							props.load(istream);

							if (result == null) {
								result = props;
							} else {
								mergeTables(result, props);
							}
						} finally {
							istream.close();
						}
					}
				}
				if (result == null) {
					result = new Hashtable<>(11);
				}
				propertiesCache.put(cl, result);
				return result;
			}
		}

		private static void mergeTables(Hashtable<? super String, Object> props1,
				Hashtable<? super String, Object> props2) {
			for (Object key : props2.keySet()) {
				String prop = (String)key;
				Object val1 = props1.get(prop);
				if (val1 == null) {
					props1.put(prop, props2.get(prop));
				} else if (isListProperty(prop)) {
					String val2 = (String)props2.get(prop);
					props1.put(prop, ((String)val1) + ":" + val2);
				}
			}
		}

		private static boolean isListProperty(String prop) {
			prop = prop.intern();
			for (int i = 0; i < listProperties.length; i++) {
				if (prop == listProperties[i]) {
					return true;
				}
			}
			return false;
		}

		private static final String[] listProperties = {
				Context.OBJECT_FACTORIES,
				Context.URL_PKG_PREFIXES,
				Context.STATE_FACTORIES,
				// The following shouldn't create a runtime dependence on ldap package.
				javax.naming.ldap.LdapContext.CONTROL_FACTORIES
		};
	}
	public static class VersionHelper12 {
		private static VersionHelper12 helper = null;
		static {
			helper = new VersionHelper12();
		}

		public static VersionHelper12 getVersionHelper() {
			return helper;
		}

		final static String[] PROPS = new String[] {
				javax.naming.Context.INITIAL_CONTEXT_FACTORY,
				javax.naming.Context.OBJECT_FACTORIES,
				javax.naming.Context.URL_PKG_PREFIXES,  // tomcat使用这个配置
				javax.naming.Context.STATE_FACTORIES,
				javax.naming.Context.PROVIDER_URL,
				javax.naming.Context.DNS_URL,
				// The following shouldn't create a runtime dependence on ldap package.
				javax.naming.ldap.LdapContext.CONTROL_FACTORIES
		};

		InputStream getJavaHomeLibStream(final String filename) {
			return AccessController.doPrivileged(
					new PrivilegedAction<InputStream>() {
						public InputStream run() {
							try {
								String javahome = System.getProperty("java.home");
								if (javahome == null) {
									return null;
								}
								String pathname = javahome + java.io.File.separator +
										"lib" + java.io.File.separator + filename;
								return new java.io.FileInputStream(pathname);
							} catch (Exception e) {
								return null;
							}
						}
					}
					);
		}

		String[] getJndiProperties() {
			Properties sysProps = AccessController.doPrivileged(
					new PrivilegedAction<Properties>() {
						public Properties run() {
							try {
								return System.getProperties();
							} catch (SecurityException e) {
								return null;
							}
						}
					}
					);
			if (sysProps == null) {
				return null;
			}
			String[] jProps = new String[PROPS.length];
			for (int i = 0; i < PROPS.length; i++) {
				jProps[i] = sysProps.getProperty(PROPS[i]);
			}
			return jProps;
		}

		ClassLoader getContextClassLoader() {
			return AccessController.doPrivileged(
					new PrivilegedAction<ClassLoader>() {
						public ClassLoader run() {
							ClassLoader loader =
									Thread.currentThread().getContextClassLoader();
							if (loader == null) {
								// Don't use bootstrap class loader directly!
								loader = ClassLoader.getSystemClassLoader();
							}

							return loader;
						}
					}
					);
		}

		NamingEnumeration<InputStream> getResources(final ClassLoader cl,
				final String name) throws IOException {
			Enumeration<URL> urls;
			try {
				urls = AccessController.doPrivileged(
						new PrivilegedExceptionAction<Enumeration<URL>>() {
							public Enumeration<URL> run() throws IOException {
								return (cl == null)
										? ClassLoader.getSystemResources(name)
												: cl.getResources(name);
							}
						}
						);
			} catch (PrivilegedActionException e) {
				throw (IOException)e.getException();
			}
			return new InputStreamEnumeration(urls);
		}

		class InputStreamEnumeration implements NamingEnumeration<InputStream> {

			private final Enumeration<URL> urls;

			private InputStream nextElement = null;

			InputStreamEnumeration(Enumeration<URL> urls) {
				this.urls = urls;
			}

			/*
			 * Returns the next InputStream, or null if there are no more.
			 * An InputStream that cannot be opened is skipped.
			 */
			private InputStream getNextElement() {
				return AccessController.doPrivileged(
						new PrivilegedAction<InputStream>() {
							public InputStream run() {
								while (urls.hasMoreElements()) {
									try {
										return urls.nextElement().openStream();
									} catch (IOException e) {
										// skip this URL
									}
								}
								return null;
							}
						}
						);
			}

			public boolean hasMore() {
				if (nextElement != null) {
					return true;
				}
				nextElement = getNextElement();
				return (nextElement != null);
			}

			public boolean hasMoreElements() {
				return hasMore();
			}

			public InputStream next() {
				if (hasMore()) {
					InputStream res = nextElement;
					nextElement = null;
					return res;
				} else {
					throw new NoSuchElementException();
				}
			}

			public InputStream nextElement() {
				return next();
			}

			public void close() {
			}
		}

	}

}
