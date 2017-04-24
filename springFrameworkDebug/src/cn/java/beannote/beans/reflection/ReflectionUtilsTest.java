package cn.java.beannote.beans.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.springframework.util.ReflectionUtils;

public class ReflectionUtilsTest {

	public static void main(String[] args) {
		Class<?> beanClass = Service1.class;
		Constructor<?>[] candidates = beanClass.getConstructors();
		for (Constructor<?> candidate : candidates) { // 迭代所有构造函数
			Class<?>[] paramTypes = candidate.getParameterTypes();
		}
		
	}

	public static void testMakeAccessible(String[] args) {
		Field field = null;
		ReflectionUtils.makeAccessible(field);
		Method method = null;
		ReflectionUtils.makeAccessible(field);
	}

	private static class Service1 {
		private String field1;

		public Service1() {
		}

		public Service1(String field1) {
			this.field1 = field1;
		}
	}

}
