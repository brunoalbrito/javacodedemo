package cn.java.note.debug;

public class 如何搜索扩展 {

	public static void main(String[] args) {
		/*
		 	-------------如何创建Logger对象---------------
		 		getFactory().getInstance(clazz);
		 		org.apache.commons.logging.impl.LogFactoryImpl.getInstance(clazz);
		 		org.apache.commons.logging.impl.LogFactoryImpl.getInstance(name);
		 		org.apache.commons.logging.impl.LogFactoryImpl.newInstance(name);
		 		
		 	-------------如何搜索日志适配器-------------
		 		0、在配置中"org.apache.commons.logging.log"指定日志适配器类（可自己定义）
		        1、在系统环境变量"org.apache.commons.logging.Log"指定日志适配器类（可自己定义）
		        2、在系统环境变量"org.apache.commons.logging.log"指定日志适配器类（可自己定义）
		        
	        	3、使用系统内置的 logAdapterClassNames = { 
	    	    	"org.apache.commons.logging.impl.Log4JLogger",
	    	    	"org.apache.commons.logging.impl.Jdk14Logger",
	    	        "org.apache.commons.logging.impl.Jdk13LumberjackLogger",
	    	        "org.apache.commons.logging.impl.SimpleLog"
	    	    }
		 */
		
	}

}
