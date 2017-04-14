package cn.java.note.beans.reflection.spring_reflection_utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.springframework.util.ReflectionUtils;

public class ReflectionUtilsTest {

	public static void main(String[] args) {
		Field field = null;
		ReflectionUtils.makeAccessible(field);
		Method method = null;
		ReflectionUtils.makeAccessible(field);
	}

}
