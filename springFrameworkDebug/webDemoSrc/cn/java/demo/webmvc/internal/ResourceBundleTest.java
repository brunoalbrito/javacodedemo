package cn.java.demo.webmvc.internal;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.ResourceBundle.Control;

public class ResourceBundleTest {
	public static void main(String[] args) throws Exception {
		{
			ResourceBundle bundle = ResourceBundle.getBundle("classpath:themes/theme0", Locale.CHINESE,ResourceBundleTest.class.getClassLoader(), new MessageSourceControl());
			if (bundle != null) {
				if (bundle.containsKey("themes.message.message0")) {
					System.out.println(bundle.getString("themes.message.message0"));
				}
			}
		}
		
		{
			ResourceBundle bundle = ResourceBundle.getBundle("classpath:themes/theme0", Locale.CHINESE,ResourceBundleTest.class.getClassLoader(), Control.getControl(Control.FORMAT_DEFAULT));
			if (bundle != null) {
				if (bundle.containsKey("themes.message.message0")) {
					System.out.println(bundle.getString("themes.message.message0"));
				}
			}
		}
	}
	
	private static class MessageSourceControl extends ResourceBundle.Control {
		
	}
	
//	private class MessageSourceControl extends ResourceBundle.Control {
//
//		@Override
//		public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload)
//				throws IllegalAccessException, InstantiationException, IOException {
//
//			// Special handling of default encoding
//			if (format.equals("java.properties")) {
//				String bundleName = toBundleName(baseName, locale);
//				final String resourceName = toResourceName(bundleName, "properties");
//				final ClassLoader classLoader = loader;
//				final boolean reloadFlag = reload;
//				InputStream stream;
//				try {
//					stream = AccessController.doPrivileged(
//							new PrivilegedExceptionAction<InputStream>() {
//								@Override
//								public InputStream run() throws IOException {
//									InputStream is = null;
//									if (reloadFlag) {
//										URL url = classLoader.getResource(resourceName);
//										if (url != null) {
//											URLConnection connection = url.openConnection();
//											if (connection != null) {
//												connection.setUseCaches(false);
//												is = connection.getInputStream();
//											}
//										}
//									}
//									else {
//										is = classLoader.getResourceAsStream(resourceName);
//									}
//									return is;
//								}
//							});
//				}
//				catch (PrivilegedActionException ex) {
//					throw (IOException) ex.getException();
//				}
//				if (stream != null) {
//					String encoding = getDefaultEncoding();
//					if (encoding == null) {
//						encoding = "ISO-8859-1";
//					}
//					try {
//						return loadBundle(new InputStreamReader(stream, encoding));
//					}
//					finally {
//						stream.close();
//					}
//				}
//				else {
//					return null;
//				}
//			}
//			else {
//				// Delegate handling of "java.class" format to standard Control
//				return super.newBundle(baseName, locale, format, loader, reload);
//			}
//		}
//
//		@Override
//		public Locale getFallbackLocale(String baseName, Locale locale) {
//			return (isFallbackToSystemLocale() ? super.getFallbackLocale(baseName, locale) : null);
//		}
//
//		@Override
//		public long getTimeToLive(String baseName, Locale locale) {
//			long cacheMillis = getCacheMillis();
//			return (cacheMillis >= 0 ? cacheMillis : super.getTimeToLive(baseName, locale));
//		}
//
//		@Override
//		public boolean needsReload(String baseName, Locale locale, String format, ClassLoader loader, ResourceBundle bundle, long loadTime) {
//			if (super.needsReload(baseName, locale, format, loader, bundle, loadTime)) {
//				cachedBundleMessageFormats.remove(bundle);
//				return true;
//			}
//			else {
//				return false;
//			}
//		}
//	}
}
