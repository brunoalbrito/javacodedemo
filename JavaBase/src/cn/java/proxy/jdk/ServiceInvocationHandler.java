package cn.java.proxy.jdk;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ServiceInvocationHandler<T> implements InvocationHandler, Serializable {

	private final Class<T> targetInterface;

	public ServiceInvocationHandler(Class<T> targetInterface) {
		this.targetInterface = targetInterface;
	}

	public Class<T> getTargetInterface() {
		return targetInterface;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if (Object.class.equals(method.getDeclaringClass())) { // 如果调用方法在Object中声明，对于invoke的第一个参数就使用this
			try {
				return method.invoke(this, args);
			} catch (Throwable t) {
				throw t;
			}
		}
		System.out.println("------ methodName : " + targetInterface.getName() + ":" + method.getName() + " ------");
		if (args != null) {
			int i = 0;
			for (Object arg : args) {
				System.out.println("param" + i + " : " + arg);
				i++;
			}
		}
		return null;
	}

}
