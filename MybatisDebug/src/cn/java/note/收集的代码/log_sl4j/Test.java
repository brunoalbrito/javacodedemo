package cn.java.note.收集的代码.log_sl4j;

import org.apache.ibatis.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.spi.LocationAwareLogger;

public class Test {
	
	public static void main(String[] args){
		Log log = LogFactory.getLog(Test.class.getName()); // org.apache.ibatis.logging.slf4j.Slf4jImpl
		log.debug("hello this is debug...");
	}

	/**
	 * 以下使用slf4j作为日志工具
	 */
	public static void debug(){
		Log log = null;
		String clazz = Test.class.getName();

		// 1、获取日志工具
		Logger logger = LoggerFactory.getLogger(clazz); // org.slf4j.Logger
		if (logger instanceof LocationAwareLogger) {
			try {
				// check for slf4j >= 1.6 method signature
				logger.getClass().getMethod("log", Marker.class, String.class, int.class, String.class, Object[].class, Throwable.class);
				log = new Slf4jLocationAwareLoggerImpl((LocationAwareLogger) logger);
				return;
			} catch (SecurityException e) {
				// fail-back to Slf4jLoggerImpl
			} catch (NoSuchMethodException e) {
				// fail-back to Slf4jLoggerImpl
			}
		}
		
		// Logger is not LocationAwareLogger or slf4j version < 1.6
		log = new Slf4jLoggerImpl(logger);
		
		// 2、记录日志
		log.debug("hello this is debug...");
		
	}
}
