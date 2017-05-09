package cn.java.noteagain;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class Wrapper {

	public void service() {
		Class classTemp = null;
		Servlet servletTemp = null;

		Servlet servlet = new Servlet();
		servlet.init();
		servlet.service();

		try {
			classTemp = Class.forName("cn.java.noteagain.Servlet");
			servletTemp = (Servlet) classTemp.newInstance();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public static void main(String[] args) {
		testReflect();
	}

	public static void testReflect() {
		Class classTemp = null;
		Servlet servlet = new Servlet();
		classTemp = servlet.getClass();
		Method[] methods = classTemp.getMethods();

		for (int i = 0; i < methods.length; i++) {
			Method methodTemp = methods[i];

			//取得注解列表
			Annotation[] annotations = methodTemp.getAnnotations();
			for (int j = 0; j < annotations.length; j++) {
				Annotation annotationTemp = annotations[j];
				System.out.println(annotationTemp.annotationType().getName()
						.toString());
			}
			

			// 返回类型
			String returnTypeName = methodTemp.getReturnType().getName()
					.toString();
			// 方法名
			String methodName = methodTemp.getName();

			// 参数数量
			int parameterCount = methodTemp.getParameterCount();
			String methodInfoStr = returnTypeName + " " + methodName + "(";

			// 参数列表
			Parameter[] parameters = methodTemp.getParameters();
			for (int j = 0; j < parameters.length; j++) {
				Parameter parameterTemp = parameters[j];
				String parmeterName = parameterTemp.getName();
				String parameterTypeName = parameterTemp.getType().getName()
						.toString();
				methodInfoStr = methodInfoStr + parameterTypeName + " "
						+ parmeterName + ",";
			}
			if (methodInfoStr.indexOf(',') != -1) {
				methodInfoStr = methodInfoStr.substring(0,
						methodInfoStr.length() - 1);
			}
			methodInfoStr += ")";
			System.out.println(methodInfoStr);
		}
	}

}
