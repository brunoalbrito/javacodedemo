package cn.java.note.override.relpace;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.springframework.beans.factory.support.MethodReplacer;

public class MethodReplacerImpl implements MethodReplacer {

	@Override
	public Object reimplement(Object obj, Method method, Object[] args) throws Throwable {
		if (Object.class.equals(method.getDeclaringClass())) {
			try {
				return method.invoke(this, args);
			} catch (Throwable t) {
				throw t;
			}
		}
		if (!Modifier.isAbstract(method.getModifiers())) { 
			return method.invoke(obj, args);
		}
		throw new Exception("the method is abstract.");
	}

}
