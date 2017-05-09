package cn.java.note;

class Catalina {
	public void setAwait(boolean b) {

	}

	public void load() {

	}
}

public class Bootstrap {

	Catalina catalina = new Catalina();
	
	public static void main(String[] args) {
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.init();
		bootstrap.setAwait(true);
		String[] arguments = { "1", "2" };
		bootstrap.load(arguments);
		bootstrap.start();

	}
	
	public void init() {
		// 设置环境变量 //catalina.base = catalina.home = catalinaHome
		// 设置环境变量 //catalina.base = catalina.home = catalinaHome
		// 初始化类加载器 catalinaLoader，sharedLoader
		// 设置线程加载器 Thread.currentThread().setContextClassLoader(catalinaLoader);
		// 使用自定义类加载器 catalinaLoader加载类org.apache.catalina.startup.Catalina
		// 反射创建
		// org.apache.catalina.startup.Catalina對象，并保存父類加載器java.lang.ClassLoader
		// Object startupInstance = startupClass.newInstance();
		// catalinaDaemon = startupInstance;

	}

	public void setAwait(boolean await) {
		catalina.setAwait(await);
	}

	private void load(String[] arguments) {
		/**
		 使用Digester解析conf/server.xml服务器配置文件，并构造对象树
		 
		 
		 */
		catalina.load();
	}

	public void start() {

	}

	

}
