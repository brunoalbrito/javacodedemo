package cn.java.note.main;

import java.io.IOException;

public class Cache缓存 {
	public static void test() throws IOException {
		/**
		
		
		----------------如何支持缓存------------------
		 	一级缓存：如 Redis、Memcache
		 	二级缓存：对象缓存 org.apache.ibatis.cache.impl.PerpetualCache
		 	
		 	org.apache.ibatis.session.defaults.DefaultSqlSession.selectList(){
		 		org.apache.ibatis.executor.CachingExecutor.query(....){
		 			if(一级缓存Redis配置检查){
		 				org.apache.ibatis.executor.SimpleExecutor.query(....){
		 					list = queryFromDatabase(ms, parameter, rowBounds, resultHandler, key, boundSql); // 从数据库获取!!!
		 					设置二级缓存（本地缓存）
		 				}
		 				设置一级缓存
		 			}
		 			else{
		 				org.apache.ibatis.executor.SimpleExecutor.query(....){
		 					list = queryFromDatabase(ms, parameter, rowBounds, resultHandler, key, boundSql); // 从数据库获取!!!
		 					设置二级缓存（本地缓存）
		 				}
		 			}
		 		}
		 	}
		 	
		 ---------------- 一级缓存的种类 ----------------
			--- 类型一： PerpetualCache
			org.apache.ibatis.cache.decorators.BlockingCache { // 有配置 blocking=true
		   		org.apache.ibatis.cache.decorators.SynchronizedCache { // 必有的包装
		       		org.apache.ibatis.cache.decorators.LoggingCache { // 必有的包装
			       		org.apache.ibatis.cache.decorators.SerializedCache { // 有配置 readWrite=true
				       		org.apache.ibatis.cache.decorators.ScheduledCache { // 有配置 clearInterval
					       		org.apache.ibatis.cache.decorators.LruCache {
					       			org.apache.ibatis.cache.impl.PerpetualCache("com.test.bean.TestMapper")
					       		}
					       	}
					    }
					 }
				}
			}
			--- 类型二： 不是LoggingCache的子类，必然会让LoggingCache包装
			org.apache.ibatis.cache.decorators.LoggingCache {
				cn.java.cache.MyCache1("com.test.bean.TestMapper")
			}
			--- 类型三： 其他类型的实现方式
			cn.java.cache.MyCache2("com.test.bean.TestMapper")
		 		
		 */
	}
}
