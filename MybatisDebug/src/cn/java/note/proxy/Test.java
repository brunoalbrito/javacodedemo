package cn.java.note.proxy;

import java.lang.reflect.Proxy;

public class Test {
	
	public static void main(String[] args) {
		Class interfaceClass = User.class;
		User user = (User) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[] { interfaceClass }, new ServiceProxy(interfaceClass));
		user.method1();
		user.method2("param...");
		user.method3("param...",100);
	}
	
}

