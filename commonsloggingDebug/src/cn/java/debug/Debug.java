package cn.java.debug;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.java.test.Test;

public class Debug {

	public static void main(String[] args) {
		/*
		 		
		 		commons.loggging(核心)--->适配器(org.apache.commons.logging.impl.Log4JLogger)--->org.apache.log4j.Logger
		 		commons.loggging(核心)--->适配器(org.apache.commons.logging.impl.Jdk14Logger)--->java.util.logging.Logger
		 		commons.loggging(核心)--->适配器(org.apache.commons.logging.impl.Jdk13LumberjackLogger)--->java.util.logging.Logger
		 		commons.loggging(核心)--->适配器(org.apache.commons.logging.impl.SimpleLog)--->org.apache.commons.logging.impl.SimpleLog
		 	------------
		 	Log log = LogFactory.getLog(Test.class) === org.apache.commons.logging.impl.Log4JLogger
		 	Log log = LogFactory.getLog(Test.class) === org.apache.commons.logging.impl.Jdk14Logger
		 	Log log = LogFactory.getLog(Test.class) === org.apache.commons.logging.impl.SimpleLog
		 	
		 	-------------如何搜索LogFactory-------------
		 		1. System.getProperty("org.apache.commons.logging.LogFactory", null);（可在启动参数中定义）
		 		2. ClassLoader.getSystemResourceAsStream("META-INF/services/org.apache.commons.logging.LogFactory")
		 		3. 类路径下自定义的配置文件"commons-logging.properties".getProperty("org.apache.commons.logging.LogFactory")
		 		4. 使用默认的org.apache.commons.logging.impl.LogFactoryImpl
		 	-----------	
		 	org.apache.commons.logging.impl.LogFactoryImpl.getInstance(clazz);	
		 	{
		 		return org.apache.commons.logging.impl.LogFactoryImpl.getInstance(String name) 
		 		{
			 		Log instance = (Log) instances.get(name);
			        if (instance == null) {
			            instance = newInstance(name);
			            {
			            	Log instance; 
			            	if (logConstructor == null) {
				                instance = discoverLogImplementation(name); // 发现有实现接口的类， org.apache.commons.logging.impl.Log4JLogger
				                {
				                	String specifiedLogClassName = findUserSpecifiedLogClassName();
				                	{
				                		1. 类路径下自定义的配置文件"commons-logging.properties"中"org.apache.commons.logging.log"指定日志适配器类（可自己定义）
									    2. System.getProperty("org.apache.commons.logging.Log",null)指定日志适配器类（可在启动参数中定义）
									    3. System.getProperty("org.apache.commons.logging.log",null)指定日志适配器类（可在启动参数中定义）
				                	}
				                	if (specifiedLogClassName != null) { // 找到了类
				                		....
				                		result = createLogFromClass(specifiedLogClassName,logCategory,true);
				                		{
				                			for(;;) {
					                			try {
					                				Class c;
									                try {
									                    c = Class.forName(logAdapterClassName, true, currentCL); // 尝试加载类
									                } catch (ClassNotFoundException originalClassNotFoundException) {
									                	c = Class.forName(logAdapterClassName);
									                }
									                constructor = c.getConstructor(logConstructorSignature); // 创建logger对象
									                Object o = constructor.newInstance(params);
									                if (o instanceof Log) {
									                    logAdapterClass = c;
									                    logAdapter = (Log) o;//!!!!
									                    break;
									                }
									                handleFlawedHierarchy(currentCL, c);
					                			}
							                	currentCL = getParentClassLoader(currentCL);
				                			}
				                			return logAdapter;
				                		}
	                                    return result;
				                	}
				            //    	4. 使用系统内置的
		                	//     	classesToDiscover = {
							//            "org.apache.commons.logging.impl.Log4JLogger",
							//            "org.apache.commons.logging.impl.Jdk14Logger",
							//            "org.apache.commons.logging.impl.Jdk13LumberjackLogger",
							//            "org.apache.commons.logging.impl.SimpleLog"
							//	    };
							        for(int i=0; i<classesToDiscover.length && result == null; ++i) { // 发现类
							            result = createLogFromClass(classesToDiscover[i], logCategory, true);
							        }
							        
							        if (result == null) {
							            throw new LogConfigurationException("No suitable Log implementation");
							        }
							
							        return result;
				                }
				            }
				            else {
				                Object params[] = { name };
				                instance = (Log) logConstructor.newInstance(params);
				            }
				
				            if (logMethod != null) {
				                Object params[] = { this };
				                logMethod.invoke(instance, params);
				            }
				
				            return instance;
			            }
			            instances.put(name, instance);
			        }
			        return instance;
		 		}
		 	}
		 	-------------如何搜索日志适配器-------------
		 	
		 		1. 类路径下自定义的配置文件"commons-logging.properties"中"org.apache.commons.logging.log"指定日志适配器类（可自己定义）
			    2. System.getProperty("org.apache.commons.logging.Log",null)指定日志适配器类（可在启动参数中定义）
			    3. System.getProperty("org.apache.commons.logging.log",null)指定日志适配器类（可在启动参数中定义）
		        
	        	4、使用系统内置的 logAdapterClassNames = { 
	    	    	"org.apache.commons.logging.impl.Log4JLogger",
	    	    	"org.apache.commons.logging.impl.Jdk14Logger",
	    	        "org.apache.commons.logging.impl.Jdk13LumberjackLogger",
	    	        "org.apache.commons.logging.impl.SimpleLog"
	    	    }
		 */
		
	}

}
