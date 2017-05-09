package cn.java.internal.typecontributor;

import java.io.File;

import org.hibernate.cfg.Configuration;
import org.hibernate.type.TypeResolver;

public class Test {

	public static void main(String[] args) {
		registerOne();
	}
	public static void registerOne() {
		Configuration configuration = new Configuration();
		configuration.configure("./" + getDir() + "/hibernate.cfg.xml");
		TypeResolver typeResolver = configuration.getTypeResolver();
		/*
			typeResolver.registerTypeOverride(BasicType type);
			typeResolver.registerTypeOverride(UserType type, String[] keys);
			typeResolver.registerTypeOverride(CompositeUserType type, String[] keys)
		 */
	}
	
	private static String getDir() {
		Class clazz = Test.class;
		String dirName = clazz.getName();
		dirName = dirName.substring(0, dirName.length() - (clazz.getSimpleName().length() + 1));
		dirName = dirName.replace(".", File.separator);
		return dirName;
	}
}
