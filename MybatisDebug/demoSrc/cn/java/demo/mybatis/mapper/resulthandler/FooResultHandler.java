package cn.java.demo.mybatis.mapper.resulthandler;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;

public class FooResultHandler<T> implements ResultHandler<T> {

	private final List<Object> list;

	public FooResultHandler() {
		list = new ArrayList<Object>();
	}

	/**
	 * 解析一行结果集调用一次
	 * org.apache.ibatis.executor.resultset.DefaultResultSetHandler.callResultHandler(...) 调用本方法
	 */
	@Override
	public void handleResult(ResultContext<? extends T> resultContext) {
		list.add(resultContext.getResultObject());
	}

	public List<Object> getResultList() {
		return list;
	}

}
