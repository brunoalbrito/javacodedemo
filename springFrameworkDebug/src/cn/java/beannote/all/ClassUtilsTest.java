package cn.java.beannote.all;

import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.util.ClassUtils;

public class ClassUtilsTest {
	public static void main(String[] args) throws Exception {
		Class<?> targetClass = ClassUtilsTest.class;
		Set<Class<?>> classes = new LinkedHashSet<Class<?>>(ClassUtils.getAllInterfacesForClassAsSet(targetClass));
		classes.add(targetClass);	
	}
	
}
