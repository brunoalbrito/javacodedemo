package cn.java.note.reflect;

import java.lang.reflect.Method;

import org.apache.ibatis.reflection.ReflectionException;

public class Test {

	private static Method GET_NAME = null;
	private static Method GET_PARAMS = null;

	public static void main(String[] args) throws Exception {
		Class<?> paramClass =  Class.forName("java.lang.reflect.Parameter");
		GET_NAME = paramClass.getMethod("getName");
		GET_PARAMS = Method.class.getMethod("getParameters");

		MyClazz myClazz = new MyClazz();
		Method[] methods = myClazz.getClass().getMethods();
		Method method = null;
		for (Method methodx : methods) {
			if(methodx.getName()=="method1"){
				method = methodx;
				break;
			}
		}
		int paramIndex = 0;
		Object[] params = (Object[]) GET_PARAMS.invoke(method);
		System.out.println((String) GET_NAME.invoke(params[paramIndex])); // 是 arg0，不是 userid
	}

	private static class MyClazz {
		public void method1(int userid,String username){

		}
	}

	/**
	 * 获取参数实际名
	 * @param method
	 * @param paramIndex
	 * @return
	 */
	private static String getActualParamName(Method method, int paramIndex) {
		if (GET_PARAMS == null) {
			return null;
		}
		try {
			Object[] params = (Object[]) GET_PARAMS.invoke(method);
			return (String) GET_NAME.invoke(params[paramIndex]);
		} catch (Exception e) {
			throw new ReflectionException("Error occurred when invoking Method#getParameters().", e);
		}
	}

	public static class ReflectionException extends RuntimeException {
		private static final long serialVersionUID = 7642570221267566591L;
		public ReflectionException() {
			super();
		}

		public ReflectionException(String message) {
			super(message);
		}

		public ReflectionException(String message, Throwable cause) {
			super(message, cause);
		}

		public ReflectionException(Throwable cause) {
			super(cause);
		}
	}
}
