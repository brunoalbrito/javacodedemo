package cn.java.demo.contexttag.internal;

import java.lang.reflect.Field;
import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.beans.TypeConverter;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.DependencyDescriptor;

public class DependencyDescriptorTest {

	public static void main(String[] args) throws Exception {
		String beanName = "fooService0";
		Field field = FooService.class.getField("beanId");
		DependencyDescriptor desc = new DependencyDescriptor(field,true);
		desc.setContainingClass(FooService.class);
		ConfigurableListableBeanFactory beanFactory = null; //org.springframework.beans.factory.support.DefaultListableBeanFactory
		Set<String> autowiredBeanNames = new LinkedHashSet<String>(1);
		TypeConverter typeConverter = beanFactory.getTypeConverter();
		Object value = beanFactory.resolveDependency(desc, beanName, autowiredBeanNames, typeConverter);
	}

	public static class FooService {
		public Integer beanId;
	}
}
