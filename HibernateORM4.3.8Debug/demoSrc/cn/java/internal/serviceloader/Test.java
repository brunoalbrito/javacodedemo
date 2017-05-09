package cn.java.internal.serviceloader;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.ServiceLoader;

public class Test {

	public static void main(String[] args) {
		System.out.println(FooService.class.getName());
	}
	public static void test(String[] args) {
		// 类加载器
		LinkedHashSet<ClassLoader> orderedClassLoaderSet = new LinkedHashSet<ClassLoader>();
		orderedClassLoaderSet.add(Thread.currentThread().getContextClassLoader());
		orderedClassLoaderSet.add(ClassLoader.getSystemClassLoader());
		AggregatedClassLoader aggregatedClassLoader = new AggregatedClassLoader(orderedClassLoaderSet);
		
		// 查找实现FooService接口的服务，并调用空构造函数进行实例化
		Class serviceFoo = FooService.class; 
		ServiceLoader<FooService> serviceLoader = ServiceLoader.load(serviceFoo, aggregatedClassLoader); // 识别 "META-INF/services/cn.java.serviceloader.FooService" 文件
		
		// 服务列表
		LinkedHashSet<FooService> services = new LinkedHashSet<FooService>();
		for (FooService service : serviceLoader) {
			services.add(service);
		}
		
		// 调用服务列表的方法
		for (FooService service : services) {
			service.method1(1);
		}
	}

	

	/**
	 * 类加载器
	 * @author Administrator
	 *
	 */
	private static class AggregatedClassLoader extends ClassLoader {
		private ClassLoader[] individualClassLoaders;

		private AggregatedClassLoader(final LinkedHashSet<ClassLoader> orderedClassLoaderSet) {
			super(null);
			individualClassLoaders = orderedClassLoaderSet.toArray(new ClassLoader[orderedClassLoaderSet.size()]);
		}

		@Override
		public Enumeration<URL> getResources(String name) throws IOException {
			final LinkedHashSet<URL> resourceUrls = new LinkedHashSet<URL>();

			for (ClassLoader classLoader : individualClassLoaders) {
				final Enumeration<URL> urls = classLoader.getResources(name);
				while (urls.hasMoreElements()) {
					resourceUrls.add(urls.nextElement());
				}
			}

			return new Enumeration<URL>() {
				final Iterator<URL> resourceUrlIterator = resourceUrls.iterator();

				@Override
				public boolean hasMoreElements() {
					return resourceUrlIterator.hasNext();
				}

				@Override
				public URL nextElement() {
					return resourceUrlIterator.next();
				}
			};
		}

		@Override
		protected URL findResource(String name) {
			for (ClassLoader classLoader : individualClassLoaders) {
				final URL resource = classLoader.getResource(name);
				if (resource != null) {
					return resource;
				}
			}
			return super.findResource(name);
		}

		@Override
		protected Class<?> findClass(String name) throws ClassNotFoundException {
			for (ClassLoader classLoader : individualClassLoaders) {
				try {
					return classLoader.loadClass(name);
				} catch (Exception ignore) {
				}
			}

			throw new ClassNotFoundException("Could not load requested class : " + name);
		}

		public void destroy() {
			individualClassLoaders = null;
		}
	}
}
