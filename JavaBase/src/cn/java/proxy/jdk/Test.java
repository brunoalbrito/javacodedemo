package cn.java.proxy.jdk;

import java.lang.reflect.Proxy;

public class Test {

	public static void main(String[] args) {
		Class interfaceClass = Service.class;
		Service user = (Service) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[] { interfaceClass },
				new ServiceInvocationHandler(interfaceClass));
		user.method1();
		user.method2("param...");
		user.method3("param...", 100);
	}

}
