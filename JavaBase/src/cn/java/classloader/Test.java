package cn.java.classloader;

public class Test {

	public static void main(String[] args) {
		testClassLoaderLink();
	}
	
	/**
	 * 类加载器的
	 * http://blog.csdn.net/gjanyanlig/article/details/6818655/
	 * http://www.cnblogs.com/fingerboy/p/5456371.html
	 */
	public static void testClassLoaderLink() {
		/*
		 	1）Bootstrap ClassLoader
			负责加载$JAVA_HOME中jre/lib/rt.jar里所有的class，由C++实现，不是ClassLoader子类

			2）Extension ClassLoader
			负责加载java平台中扩展功能的一些jar包，包括$JAVA_HOME中jre/lib/*.jar或-Djava.ext.dirs指定目录下的jar包

			3）App ClassLoader
			负责加载classpath中指定的jar包及目录中class

			4）Custom ClassLoader
			属于应用程序根据自身需要自定义的ClassLoader，如tomcat、jboss都会根据j2ee规范自行实现ClassLoader

			加载过程中会先检查类是否被已加载，检查顺序是自底向上，从Custom ClassLoader到BootStrap ClassLoader逐层检查，
			只要某个classloader已加载就视为已加载此类，保证此类只所有ClassLoader加载一次。
			而加载的顺序是自顶向下，也就是由上层来逐层尝试加载此类。
		 */
		ClassLoader loader = Test.class.getClassLoader();
		System.out.println(loader); // sun.misc.Launcher$AppClassLoader@73d16e93  --- AppClassLoader
		System.out.println(loader.getParent()); // sun.misc.Launcher$ExtClassLoader@15db9742  --- ExtClassLoader
		System.out.println(loader.getParent().getParent()); // null  --- 返回时null的话,就默认使用启动类加载器作为父加载器.
	}

	/**
	 * 设置类加载器
	 */
	public static void testSetContextClassLoader() {
		ClassLoader classLoader = Test.class.getClassLoader();
		classLoader = Thread.currentThread().getContextClassLoader();
		Thread.currentThread().setContextClassLoader(classLoader); // 要装载的类，通过当前类的Classloader链装载不了，那就得设ContextClassLoader 
	}
	
	/**
	 * 两种资源加载方式的区别
	 */
	public static void testResourceAsStream() {
		/*
			------------Class.getResourceAsStream()------------------
			String.getResourceAsStream("myfile.txt") will look for a file in your classpath at the following location: "java/lang/myfile.txt". 
			If your path starts with a /, then it will be considered an absolute path, and will start searching from the root of the classpath. 
			So calling String.getResourceAsStream("/myfile.txt") will look at the following location in your class path ./myfile.txt.

			------------ClassLoader.getResourceAsStream(path)----------------
			ClassLoader.getResourceAsStream(path) will consider all paths to be absolute paths. 
			So calling String.getClassLoader().getResourceAsStream("myfile.txt") and String.getClassLoader().getResourceAsStream("/myfile.txt") will 
			both look for a file in your classpath at the following location: ./myfile.txt.
		 */
	}



}
