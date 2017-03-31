package cn.java.proxy.cglib;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class BeanMethodInterceptor implements MethodInterceptor {

	private PropertyChangeSupport propertySupport;

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		propertySupport.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertySupport.removePropertyChangeListener(listener);
	}
	
	
	public static Object newInstance(Class clazz) {
		try {
			BeanMethodInterceptor interceptor = new BeanMethodInterceptor();
			Enhancer enhancer = new Enhancer();
			enhancer.setSuperclass(clazz); // 父类
			enhancer.setCallback(interceptor);  // 方法拦截器
			Object bean = enhancer.create(); // 创建bean对象
//			Class beanClazz = enhancer.createClass();
//			bean = beanClazz.newInstance();
			interceptor.propertySupport = new PropertyChangeSupport(bean);
			return bean;
		} catch (Throwable e) {
			e.printStackTrace();
			throw new Error(e.getMessage());
		}

	}
	
	/**
	 * 拦截器
	 */
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		Object retValFromSuper = null;
		try {
			if (!Modifier.isAbstract(method.getModifiers())) { // 父类的方法不是抽象方法
				retValFromSuper = proxy.invokeSuper(obj, args); // 调用父类的方法
			}
		} finally {
			String name = method.getName();
			if (name.equals("addPropertyChangeListener")) { // addPropertyChangeListener在父类的方法是抽象方法
				addPropertyChangeListener((PropertyChangeListener) args[0]);
			} else if (name.equals("removePropertyChangeListener")) {
				removePropertyChangeListener((PropertyChangeListener) args[0]);
			}
			
			if (name.startsWith("set") && args.length == 1 && method.getReturnType() == Void.TYPE) {
				char propName[] = name.substring("set".length()).toCharArray();
				propName[0] = Character.toLowerCase(propName[0]);
				propertySupport.firePropertyChange(new String(propName), null, args[0]);
			}
		}
		return retValFromSuper;
	}

	

}