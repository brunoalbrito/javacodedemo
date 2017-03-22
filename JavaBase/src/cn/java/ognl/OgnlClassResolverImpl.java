package cn.java.ognl;

import java.util.HashMap;
import java.util.Map;

import ognl.ClassResolver;

public class OgnlClassResolverImpl implements ClassResolver {

	private Map<String, Class<?>> classes = new HashMap<String, Class<?>>(101);

	@Override
	public Class classForName(String className, Map context) throws ClassNotFoundException {
		Class<?> result = null;
		if ((result = classes.get(className)) == null) {
			try {
				result = Class.forName(className);
			} catch (ClassNotFoundException e1) {
				if (className.indexOf('.') == -1) {
					result = Class.forName("java.lang." + className);
					classes.put("java.lang." + className, result);
				}
			}
			classes.put(className, result);
		}
		return result;
	}

}