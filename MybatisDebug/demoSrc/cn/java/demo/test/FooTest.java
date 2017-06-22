package cn.java.demo.test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.session.TransactionIsolationLevel;

import cn.java.demo.mybatis.mapper.FooMapper;
import cn.java.demo.mybatis.mapper.bean.Foo;
import cn.java.demo.mybatis.mapper.resulthandler.FooResultHandler;

public class FooTest {


	public static void main(String[] args) throws IOException, SQLException {
		
		InputStream inputStream = Resources.getResourceAsStream("foo-config.xml");
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		
		// 自动提交的方式打开 DefaultSqlSession，会创建一个Transaction对象跟随
		SqlSession sqlSession = sqlSessionFactory.openSession(true);  // 打开连接（会从“数据源”中获取一个，如果没有会“创建一个”）
		{
			FooMapper fooMapper = sqlSession.getMapper(FooMapper.class);
			AboutResult.test(fooMapper);
			AboutParam.test(fooMapper);
			AboutResultBean.test(fooMapper);
			AboutCurd.test(fooMapper);
			// testTrasaction(sqlSession);
		}
		sqlSession.close(); // 断开连接（会断开从“数据源”获取的连接，实际上是把连接“放回数据源连接池”中）
		
	}

	/**
	 * 事务
	 * @param sqlSession
	 * @throws SQLException
	 */
	private static void testTrasaction() throws Exception{
		InputStream inputStream = Resources.getResourceAsStream("foo-config.xml");
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		
//		SqlSession sqlSession = sqlSessionFactory.openSession(false); // 不自动提交
		SqlSession sqlSession = sqlSessionFactory.openSession(TransactionIsolationLevel.REPEATABLE_READ); // 不自动提交+声明事务隔离级别
		
		FooMapper fooMapper = sqlSession.getMapper(FooMapper.class);
		sqlSession.getConnection().setAutoCommit(false);
		sqlSession.getConnection().commit();
		sqlSession.getConnection().rollback();
		sqlSession.rollback();
		sqlSession.commit();
	}

	/**
	 * 关于添加、删除、修改、查询
	 * @author Administrator
	 *
	 */
	private static class AboutCurd {
		public static void test(FooMapper fooMapper){
			System.out.println("--------------------------------AboutCurd-----------------------------------------");
			select(fooMapper);
			insert(fooMapper);
			update(fooMapper);
			delete(fooMapper);
		}
		
		public static void select(FooMapper fooMapper){
			System.out.println(fooMapper.selectWithSomeFileds(0,"0","0"));
			System.out.println(fooMapper.selectWithAlias(0));
			System.out.println(fooMapper.selectWithGroupOrderPagination(0));
			System.out.println(fooMapper.selectWithLeftJoin(0));
			System.out.println(fooMapper.selectSubQueryAsParam(0));
			System.out.println(fooMapper.selectSubQueryAsTempTable(0));
			System.out.println(fooMapper.selectSubQueryAsField(0));
			System.out.println(fooMapper.selectUnion(0));
			System.out.println(fooMapper.selectCount(0));
		}
		
		public static void delete(FooMapper fooMapper){
			Foo foo = new Foo();
			foo.setId(100);
			System.out.println(fooMapper.delete0(foo));
			foo.setId(101);
			System.out.println(fooMapper.delete0(foo));
			foo.setId(102);
			System.out.println(fooMapper.delete0(foo));
		}
		
		public static void update(FooMapper fooMapper){
			Foo foo = new Foo();
			foo.setId(100);
			foo.setAccount("account_100_new");
			foo.setPassword("password_100_new");
			System.out.println(fooMapper.update0(foo));
		}
		
		public static void insert(FooMapper fooMapper){
			Foo foo = new Foo();
			foo.setId(100);
			foo.setAccount("account_100");
			foo.setPassword("password_100");
			System.out.println(fooMapper.insertOne(foo));
			
			List list = new ArrayList();
			
			foo = new Foo();
			foo.setId(101);
			foo.setAccount("account_101");
			foo.setPassword("password_101");
			list.add(foo);
			
			foo = new Foo();
			foo.setId(102);
			foo.setAccount("account_102");
			foo.setPassword("password_102");
			list.add(foo);
			System.out.println(fooMapper.insertBatch(list));
		}

	}
	
	private static class AboutResultBean {
		
		public static void test(FooMapper fooMapper){
			System.out.println("--------------------------------selectAboutResult-----------------------------------------");
			{
				System.out.println(fooMapper.selectOneFillBeanObjectBySetter(1));
			}
			
			{
				System.out.println(fooMapper.selectOneFillBeanObjectByConstructorAccordingAnnotation(1));
			}
			
			{
				System.out.println(fooMapper.selectOneFillBeanObjectByConstructorWithoutAccordingAnnotation(1));
			}
		}
		
	}

	private static class AboutResult {

		public static void test(FooMapper fooMapper){
			System.out.println("--------------------------------selectAboutResult-----------------------------------------");
			System.out.println("-----------selectAboutResult 0 - getResultByParam------------");
			{
				// 透过参数获取返回值
				FooResultHandler result = new FooResultHandler();
				fooMapper.selectListReturnVoidGetResultByParam(1,"1","0",result);
				for (Object obj : result.getResultList()) {
					System.out.println(obj);
				}
			}

			System.out.println("-----------selectAboutResult 1 - return Array------------");
			{
				// 返回数组
				Foo[] fooArray = fooMapper.selectListReturnArray(1);
				for (Foo foo : fooArray) {
					System.out.println(foo);
				}
			}

			System.out.println("-----------selectAboutResult 2 - return List------------");
			{
				// 返回 List
				List<Foo> fooList = fooMapper.selectListReturnList(1);
				for (Iterator iterator = fooList.iterator(); iterator.hasNext();) {
					Foo foo = (Foo) iterator.next();
					System.out.println(foo);
				}
			}

			System.out.println("-----------selectAboutResult 3 - return Map------------");
			{
				// 返回 Map
				Map map = fooMapper.selectListReturnMapWithMapKey(1);
				System.out.println(map);
			}
			
			System.out.println("-----------selectAboutResult 4 - return Cursor------------");
			{
				// 返回 Cursor，不支持
				
//				Cursor cursor = fooMapper.selectAboutResult4(1);
//				for (Iterator iterator = cursor.iterator(); iterator.hasNext();) {
//					Foo foo = (Foo)iterator.next();
//					System.out.println(foo);
//				}
			}

			System.out.println("-----------selectAboutResult 5 - return Bean------------");
			{
				// 其他类型
				Foo foo = fooMapper.selectOneReturnBeanObject(1);
				System.out.println(foo);
			}
			
			System.out.println("-----------selectAboutResult 5 - return Map Without Annotation------------");
			{
				// 返回 Map
				Map map = fooMapper.selectOneReturnMapWithoutMapKey(1);
				System.out.println(map);
			}
		}

	}

	/**
	 * 关于参数的传递
	 * @author Administrator
	 *
	 */
	private static class AboutParam {

		public static void test(FooMapper fooMapper){
			System.out.println("--------------------------------selectAboutParam-----------------------------------------");
			System.out.println("-----------selectAboutParam 0  - param RowBounds 在结果集中进行筛选------------");
			{
				// 翻页结果集 - RowBounds 是对结果集的筛选，不是决定数据库的返回结果集
				List<Foo> fooList = fooMapper.selectListAndPaginationOnLocalResultSet(1,new RowBounds(2,10));
				for (Iterator iterator = fooList.iterator(); iterator.hasNext();) {
					Foo foo = (Foo) iterator.next();
					System.out.println(foo);
				}
			}

			System.out.println("-----------selectAboutParam 1  - param UserDefine ------------");
			{
				List<Foo> fooList = fooMapper.selectListAndPaginationOnDbServer(1,0,10);
				for (Iterator iterator = fooList.iterator(); iterator.hasNext();) {
					Foo foo = (Foo) iterator.next();
					System.out.println(foo);
				}
			}

			System.out.println("-----------selectAboutParam 2  - param Unsafe ------------");
			{
				// 使用  ${} 是不安全的
				List<Foo> fooList = fooMapper.selectListPassParamUnsafe(1," OR 1=1");
				for (Iterator iterator = fooList.iterator(); iterator.hasNext();) {
					Foo foo = (Foo) iterator.next();
					System.out.println(foo);
				}
			}

			System.out.println("-----------selectAboutParam 3  - param Bean ------------");
			{
				// 参数是自定义对象
				Foo foo = new Foo();
				foo.setId(0);
				List<Foo> fooList = fooMapper.selectListPassBeanObject(foo);
				for (Iterator iterator = fooList.iterator(); iterator.hasNext();) {
					Foo fooTemp = (Foo) iterator.next();
					System.out.println(fooTemp);
				}
			}

			System.out.println("-----------selectAboutParam 4  - param Bean And other ------------");
			{
				// 参数是自定义对象
				Foo foo = new Foo();
				foo.setId(1);
				List<Foo> fooList = fooMapper.selectListPassParamNamed(foo,0);
				for (Iterator iterator = fooList.iterator(); iterator.hasNext();) {
					Foo fooTemp = (Foo) iterator.next();
					System.out.println(fooTemp);
				}
			}
			
			System.out.println("-----------selectAboutParam 5  - generic param name ------------");
			{
				// 参数是自定义对象
				Foo foo = new Foo();
				foo.setId(1);
				List<Foo> fooList = fooMapper.selectListPassParamWithGenericNamed(foo,0);
				for (Iterator iterator = fooList.iterator(); iterator.hasNext();) {
					Foo fooTemp = (Foo) iterator.next();
					System.out.println(fooTemp);
				}
			}
		}
	}


}
