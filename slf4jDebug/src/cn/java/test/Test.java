package cn.java.test;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;

public class Test {
	
	public static void main(String[] args) throws Exception {
		testSlf4j();
		debugSlf4jMessageFormatter();
	}

	public static void testSlf4j() throws Exception {
		// logger === org.slf4j.impl.Log4jLoggerAdapter（slf4j-log4j12-1.7.25.jar包实现）
		org.slf4j.Logger logger = LoggerFactory.getLogger(Test.class); 
		logger.info("Hello World");
	}
	
	/**
	 * 消息格式化工具
	 * @throws Exception
	 */
	public static void debugSlf4jMessageFormatter() throws Exception {
		// 消息格式化工具 ,   {} 是占位符
		String messagePattern = "{} 不能小于 {}";
		Object[] argArray= {"2" ,"1"};
		Throwable throwable = null;
		FormattingTuple ft =  org.slf4j.helpers.MessageFormatter.arrayFormat( messagePattern,  argArray,  throwable);
		String msg = ft.getMessage();
		ft.getThrowable();
		System.out.println(msg);
	}
	
	/**
	 * log4j-1.2.17.jar 实现包（由log4j官方提供）
	 * @throws Exception
	 */
	public static void testLog4j() throws Exception {
		 org.apache.log4j.Logger log4jLogger = LogManager.getLogger(Test.class.getName());
		 log4jLogger.isTraceEnabled();
		 log4jLogger.isDebugEnabled();
		 String msg = "";
		 String callerFQCN = Test.class.getName();
		 log4jLogger.log(callerFQCN, Level.DEBUG, msg, null);
		 log4jLogger.log(callerFQCN, Level.TRACE, msg, null);
			 
	}
	
	public static void debugLog4j() throws Exception {
		Class.forName("org.apache.log4j.Log4jLoggerFactory");
		// force log4j to initialize
        org.apache.log4j.LogManager.getRootLogger();
        
        org.apache.log4j.Logger log4jLogger = null;

        // 日志记录器 1（根）
        log4jLogger = LogManager.getRootLogger(); // logName = "ROOT";

        // 日志记录器 2
        log4jLogger =   LogManager.getLogger(Test.class.getName());
        
        // ---------------------------------------------------------
        /**
	   
	    一次日志的写操作
	    logger --n个-->  appender（内部日志级别、内部过滤器） ---> 落地
	    
	     源代码
	    ---log4j如何识别配置文件
	         	1、尝试：系统环境变量  log4j.configuration 指定哪个文件作为log4j的配置文件
	         	2、尝试：使用log4j.xml文件
	         	3、尝试：使用log4j.properties文件
	    ---使用什么类解析配置文件
	         	1、尝试：系统环境变量 log4j.configuratorClass使用什么类解析配置文件
	         	2、没有指定解析类是xml文件，尝试使用org.apache.log4j.xml.DOMConfigurator解析xml配置文件
	         	3、使用 org.apache.log4j.PropertyConfigurator 解析配置文件
	    
	    --- void doAppend(LoggingEvent event)的流程 
			// Reminder: the nesting of calls is:
		    //
		    //    doAppend()
		    //      - check threshold
		    //      - filter
		    //      - append();
		    //        - checkEntryConditions();
		    //        - subAppend();
      
	    ---log4j.properties文件模板 
	       # 自定义变量  
	       # 日志级别 ALL、DEBUG、INFO、WARN、ERROR、FATAL、OFF、TRACE，一条日志要能记录进去要求“提供日志级别级别”大于“全局的日志级别和局部的日志级别”
	       thresholdLevel=DEBUG
	       # 声明使用哪些appender
	       rootCategory=DEBUG,appenderName1,appenderName2,Console
	       
	       # --------------log4j Core----------------
	       # log4j内部调试
	       log4j.debug=true
	       # 是否重置配置内容
	       log4j.reset=false
	      
	       # （全局）日志级别，支持动态配置${}指令，会尝试从“系统环境变量获取” 或 “配置文件内查找”
	       log4j.threshold=${thresholdLevel}
	       
	       # --------------category（rootCategory）------------------------
	       # 定义 rootLogger的配置
	       log4j.rootCategory=${rootCategory}
	       
	       # --------------appender------------------------
	       # 定义 appender和配置 appender1的filter
	       log4j.appender.appenderName1=cn.java.log4j.appender.MyAppender
	       log4j.appender.appenderName1.filter=cn.java.log4j.appender.filter.MyFilter
	       log4j.appender.appenderName1.filter.property1=property1Value
	       log4j.appender.appenderName1.filter.property2=property2Value
	       
	       # 定义 appender和配置 appender2的filter
	       log4j.appender.appenderName2=org.apache.log4j.ConsoleAppender
	       log4j.appender.appenderName2.filter=cn.java.log4j.appender.filter.MyFilter
	       log4j.appender.appenderName2.filter.property1=property1Value
	       log4j.appender.appenderName2.filter.property2=property2Value
	       
	       # 定义 appender和配置 appender3的filter、并且需要布局类的
	       log4j.appender.appenderName3=cn.java.log4j.appender.MyAppender
	       log4j.appender.appenderName3.filter=cn.java.log4j.appender.filter.MyFilter
	       log4j.appender.appenderName3.filter.property1=property1Value
	       log4j.appender.appenderName3.filter.property2=property2Value
	       # --- 实现org.apache.log4j.spi.OptionHandler接口和requiresLayout()返回才会解析下面的配置
	       log4j.appender.appenderName3.layout=cn.java.log4j.appender.layout.layout1
	       
	       # 定义 appender和配置 appender4的filter、并且需要布局类的、异常类的
	       log4j.appender.appenderName4=cn.java.log4j.appender.MyAppender
	       log4j.appender.appenderName4.filter=cn.java.log4j.appender.filter.MyFilter
	       log4j.appender.appenderName4.filter.property1=property1Value
	       log4j.appender.appenderName4.filter.property2=property2Value
	       # --- 实现org.apache.log4j.spi.OptionHandler接口和requiresLayout()返回才会解析下面的配置
	       log4j.appender.appenderName4.layout=cn.java.log4j.appender.layout.MyLayout1
	       log4j.appender.appenderName4.errorhandler=cn.java.log4j.appender.errorhandler.MyErrorHandler1
	       log4j.appender.appenderName4.errorhandler.property1=property1Value
	       log4j.appender.appenderName4.errorhandler.property2=property2Value
	       
	       # 定义 appender和配置 appenderName5的filter、并且需要布局类的、异常类的
	       log4j.appender.appenderName5=cn.java.log4j.appender.MyAppender
	       log4j.appender.appenderName5.filter=cn.java.log4j.appender.filter.MyFilter
	       log4j.appender.appenderName5.filter.property1=property1Value
	       log4j.appender.appenderName5.filter.property2=property2Value
	       # --- 实现org.apache.log4j.spi.OptionHandler接口和requiresLayout()返回才会解析下面的配置
	       log4j.appender.appenderName5.layout=cn.java.log4j.appender.layout.MyLayout1
	       log4j.appender.appenderName5.errorhandler=cn.java.log4j.appender.errorhandler.MyErrorHandler1
	       log4j.appender.appenderName5.errorhandler.property1=property1Value
	       log4j.appender.appenderName5.errorhandler.property2=property2Value
	       log4j.appender.appenderName5.threshold=DEBUG # 日志级别
	       log4j.appender.appenderName5.property1=property2Value
	       log4j.appender.appenderName5.property2=property2Value
	       
	       # 例子
		   log4j.appender.Console=org.apache.log4j.ConsoleAppender
		   log4j.appender.Console.layout=org.apache.log4j.PatternLayout
		   log4j.appender.Console.layout.ConversionPattern=%d [%t] %-5p [%c] - %m%n	  
		        
	       # --------------loggerFactory------------------------
	       # 日志工厂
	       log4j.loggerFactory=org.apache.log4j.DefaultCategoryFactory
	       log4j.factory.property1=property1Value
	       log4j.factory.property2=property2Value
	       
	       # --------------category（logger1）------------------------
	       # 有指定日志级别+有指定日志写入器appenderName1
	       log4j.category.logger1=DEBUG,appenderName1,appenderName3
	       log4j.additivity.logger1=true
	       
	       # --------------category（logger2）配置某个类的日志记录情况------------------------
	       # 没有指定日志级别（以逗号开头）+有指定日志写入器appenderName1
	       log4j.category.cn.java.service.UserService=,appenderName1,appenderName3
	       log4j.additivity.logger1=true
	       
	       # --------------category（logger3）配置某个类的日志记录情况------------------------
	       # 有指定日志级别+没有指定日志写入器appenderName1，没有appenderName1，那么会使用父Logger的
	       log4j.category.cn.java.service.AdminService=DEBUG
	       log4j.additivity.logger1=true
	       
	       # --------------------------------------
	       # 被渲染的类和对应的渲染器
	       log4j.renderer.cn.java.renderedClass=cn.java.renderingClass
	       
	       # --------------------------------------
	       # 异常渲染器
	       log4j.throwableRenderer=org.apache.log4j.spi.ThrowableRenderer
	       log4j.throwableRenderer.property1=property1Value
	       log4j.throwableRenderer.property2=property2Value
         */
        	
        
	}
}
