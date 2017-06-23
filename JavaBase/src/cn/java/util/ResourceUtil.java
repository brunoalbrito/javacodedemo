package cn.java.util;

public class ResourceUtil {
	
	public String getFilePath(Class clazz,String fileName){
		return clazz.getResource(fileName).getFile();
	}
	
	public String getClassLoaderRootFile(Class clazz){
		return clazz.getClassLoader().getResource("").getFile();
	}
	
}
