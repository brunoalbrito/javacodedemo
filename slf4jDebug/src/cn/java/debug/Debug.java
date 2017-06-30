package cn.java.debug;

public class Debug {

	public static void main(String[] args) {
		/*
		 	机制：
		 		在类路径下查找收集org/slf4j/impl/StaticLoggerBinder.class类
		 
		 		
		 		
		 	org.slf4j.Logger logger = LoggerFactory.getLogger(Test.class) = org.slf4j.impl.StaticLoggerBinder.getLoggerFactory() = org.slf4j.impl.Log4jLoggerFactory.getLogger(name);
		 	
		 		
		 	一般由三种包组成：
		 		1、slf4j核心包，如：slf4j-api-1.7.25.jar
		 		2、适配器包，如：slf4j-log4j12-1.7.25.jar
		 		3、工具包： log4j-1.2.17.jar
		 		
		 	--------怎么使用slf4j---------
		 		1、需要slf4j-api-1.7.25.jar的接口包，统一抽象接口（由slf4j官方提供）
		 		2、需要slf4j-log4j12-1.7.25.jar适配器包，实现了org/slf4j/impl/StaticLoggerBinder（由slf4j官方提供，用户也可自己实现）
		 		3、需要log4j-1.2.17.jar实现包（由log4j官方提供）
		 	
		 	--------slf4j扩展机制(奇葩的地方)---------
			 	虽然slf4j-api-1.7.25.jar包内定义了 org.slf4j.impl.StaticLoggerBinder类，但是该类是无效类。
			 	主要就是在类路径下搜寻org.slf4j.impl.StaticLoggerBinder类（有且只能有一个），如：slf4j-log4j12-1.7.25.jar中就实现了org.slf4j.impl.StaticLoggerBinder类
			 	然后调用StaticLoggerBinder.getSingleton()、StaticLoggerBinder.getLoggerFactory()获取实际日志的适配器（LoggerFactory对象）
		 	
		 	--------slf4j 和 log4j 的关系---------
			 	第三方扩展包如果要支持slf4j的调用，只需定义一个org.slf4j.impl.StaticLoggerBinder的类，slf4j会自动搜寻到。
			 	slf4j： slf4j-api-1.7.25.jar（不依赖log4j的代码） ---> slf4j-log4j12-1.7.25.jar（这个包开始依赖log4j的代码）
			 	log4j： log4j-1.2.17.jar
		 */
	
	}

}
