package cn.java.internal.connection;

import java.io.File;

import org.hibernate.cfg.Configuration;

public class Test {

	public static void main(String[] args) {
		Configuration configuration = new Configuration();
		configuration.configure("./" + getDir() + "/hibernate.cfg.xml");
	}
	
	private static String getDir() {
		Class clazz = Test.class;
		String dirName = clazz.getName();
		dirName = dirName.substring(0, dirName.length() - (clazz.getSimpleName().length() + 1));
		dirName = dirName.replace(".", File.separator);
		return dirName;
	}
}
