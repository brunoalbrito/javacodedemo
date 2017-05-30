package cn.java.curd.util;

import java.io.File;

import cn.java.common.util.SessionFactoryUtil0;

public class SessionFactoryUtil extends SessionFactoryUtil0 {

	protected static String getPackageDir() {
		String dirName = SessionFactoryUtil0.class.getName();
		dirName = dirName.substring(0, dirName.length() - (SessionFactoryUtil0.class.getSimpleName().length() + 1));
		dirName = dirName.replace(".", File.separator);
		return dirName;
	}
}
