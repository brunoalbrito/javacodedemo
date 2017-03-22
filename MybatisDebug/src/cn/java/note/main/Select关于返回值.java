package cn.java.note.main;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.binding.MapperMethod.ParamMap;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class Select关于返回值 {
	public static <E, K, V, T> void select() throws IOException {
		InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		SqlSession sqlSession = sqlSessionFactory.openSession();  // sqlSession == org.apache.ibatis.session.defaults.DefaultSqlSession
		
		String commandName = "cn.java.UserMapper.method1";
		Map<String, Object> param = new ParamMap<Object>(){
			{
				put("param0","param0Value");
				put("param1",new Object());
			}
		};
		RowBounds rowBounds = new RowBounds(0,10); // 限制分页（这个是对查询结果进行遍历的条件，所以要限制数据库的返回是没有效果的）
//		MapperMethod.execute(sqlSession, args) 
//		if (method.returnsVoid() && method.hasResultHandler()) // mapper方法的写法1：没有放回值，使用引用传递参数，如： public void select0(String param0,ResultHandler result)
		{
			ResultHandler resultHandler = new ResultHandler(){
				private List<Object> list;
				
				public List<Object> getResultList() {
					return list;
				}
				
				/**
				 * 解析一行结果集调用一次
				 * org.apache.ibatis.executor.resultset.DefaultResultSetHandler.callResultHandler(...) 调用本方法
				 */
				@Override
				public void handleResult(ResultContext resultContext) {
					if(list==null){
						list = new ArrayList<Object>();
					}
					list.add(resultContext.getResultObject());
				}
			};
			sqlSession.select(commandName, param, rowBounds,resultHandler);
			sqlSession.select(commandName, param, resultHandler);
		}
//		else if (method.returnsMany())   // mapper方法的写法2：返回值类型是Collection的子类， 如：public List select1();
		{ // 返回值类型是Collection的子类， 如：public List select1();
			List<E> result;
			result = sqlSession.<E>selectList(commandName, param, rowBounds);
	        result = sqlSession.<E>selectList(commandName, param);
//	        return convertToArray(result);
		}
//		else if (method.returnsMap()) // mapper方法的写法3：返回值类型是Map的子类，并且有@MapKey(value="key1")注解，如：@MapKey(value="key1") public Map select2();
		{ // 返回值类型是Map的子类，并且有@MapKey(value="key1")注解，如：@MapKey(value="key1") public Map select2();
			Map<K, V> result; 
			result = sqlSession.<K, V>selectMap(commandName, param,"key1", rowBounds);
	        result = sqlSession.<K, V>selectMap(commandName, param, "key1");
//	        return result;
		}
//		else if (method.returnsCursor()) // mapper方法的写法4：返回值类型是Cursor， 如： public Cursor select3();
		{ // 返回值类型是Cursor， 如： public Cursor select3();  org.apache.ibatis.cursor.defaults.DefaultCursor
			Cursor<T> result;
			result = sqlSession.<T>selectCursor(commandName, param, rowBounds);
			result = sqlSession.<T>selectCursor(commandName, param);
//			return result;
		}
//		else // mapper方法的写法5：其他
		{
			Object result;
		    result = sqlSession.selectOne(commandName, param);
		}
		
	}
}
