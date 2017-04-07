package cn.java.note.loadallproperties;

import java.util.Properties;

import org.springframework.core.io.support.PropertiesLoaderUtils;

public class PropertiesLoaderUtilsTest {

	public static void main(String[] args) {
		Properties mappings =
				PropertiesLoaderUtils.loadAllProperties("META-INF/spring.schemas", PropertiesLoaderUtilsTest.class.getClassLoader()); // spring-xxxx.jar 包中 "META-INF/spring.schemas"

	}

}
