package cn.java.interceptor;

import java.util.Properties;

import org.apache.ibatis.executor.SimpleExecutor;
import org.apache.ibatis.executor.resultset.DefaultResultSetHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;

/**
 * 插件
 * 		通过org.apache.ibatis.session.Configuration.newXxxx() 创建处理的对象，会被应用插件
 * @author zhouzhian
 *
 */
public class MyInterceptor implements Interceptor{

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		return null;
	}

	@Override
	public Object plugin(Object target) {
		// 1.target === Executor 在创建Executor的时候会走进来
		if(target instanceof SimpleExecutor){ // 创建SimpleExecutor对象后调用

		}
		else if(target instanceof DefaultParameterHandler){ // 创建DefaultParameterHandler对象后调用

		}
		else if(target instanceof DefaultResultSetHandler){ // 创建DefaultResultSetHandler对象后调用
			
		}
		else if(target instanceof RoutingStatementHandler){ // 创建RoutingStatementHandler对象后调用
			
		}
		return target;
	}

	@Override
	public void setProperties(Properties properties) {

	}

}
