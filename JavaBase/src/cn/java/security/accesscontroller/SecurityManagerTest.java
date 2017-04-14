package cn.java.security.accesscontroller;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

/**
 * http://www.blogjava.net/DLevin/archive/2012/11/02/390637.html
 */
public class SecurityManagerTest {

//	一个名为 SecurityManager 的类负责实施系统安全策略。
//	在默认情况下不安装安全管理器，必须通过一个在启动时传递给 JVM 的、名为 java.security.manager 的环境变量显式地指定。
	
//	有关被授予特权的一些重要事项：
//	首先，这个概念仅存在于一个单独线程内。一旦特权代码完成了任务，特权将被保证清除或作废。
//	第二，在这个例子中，在run方法中的代码体被授予了特权。然而，如果它调用无特权的不可信代码，则那个代码将不会获得任何特权；
//	只有在特权代码具有许可并且在直到checkPermission调用的调用链中的所有随后的调用者也具有许可时, 一个许可才能被准予。
	public static void main(String[] args) throws PrivilegedActionException {
		getBeanInstance1();
		getBeanInstance2();
	}

	
	
	public static Object getBeanInstance1() {
		Object beanInstance = null;
		if (System.getSecurityManager() != null) { // 如果有配置安全，那么使用特权模式创建对象
			/*
			 * 在做访问控制决策时，如果checkPermission方法遇到一个通过doPrivileged调用而被表示为 "特权"的调用者，并且没有上下文自变量，checkPermission方法则将终止检查。
			 */
			beanInstance = AccessController.doPrivileged(new PrivilegedAction<Object>() {
				@Override
				public Object run() {
					return new Object();
				}
			}, AccessController.getContext());
		}
		else{ // 不使用特权模式创建对象
			beanInstance =  new Object();
		}
		return beanInstance;
	}
	
	public static Object getBeanInstance2() throws PrivilegedActionException {
		
		Object beanInstance = null;
		if (System.getSecurityManager() != null) { // 如果有配置安全，那么使用特权模式创建对象
			beanInstance = AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
				@Override
				public Object run() throws Exception {
					return new Object();
				}
			}, AccessController.getContext());
		}
		else{ // 不使用特权模式创建对象
			beanInstance =  new Object();
		}
		return beanInstance;
	}
}
