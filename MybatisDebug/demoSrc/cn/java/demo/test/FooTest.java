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
			// AboutResult.test(fooMapper);
			AboutParam.test(fooMapper);
			// AboutResultBean.test(fooMapper);
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
			select(fooMapper);
			insert(fooMapper);
			update(fooMapper);
			delete(fooMapper);
		}
		
		public static void select(FooMapper fooMapper){
			System.out.println(fooMapper.select1(0,"0","0"));
			System.out.println(fooMapper.select2(0));
			System.out.println(fooMapper.select3(0));
			System.out.println(fooMapper.select4(0));
			System.out.println(fooMapper.select5(0));
			System.out.println(fooMapper.select6(0));
			System.out.println(fooMapper.select7(0));
			System.out.println(fooMapper.select8(0));
			System.out.println(fooMapper.select9(0));
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
			System.out.println(fooMapper.insert0(foo));
			
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
			System.out.println(fooMapper.insert1(list));
		}

	}
	
	private static class AboutResultBean {
		
		public static void test(FooMapper fooMapper){
			System.out.println("--------------------------------selectAboutResult-----------------------------------------");
			selectAboutResultBean0(fooMapper);
			selectAboutResultBean1(fooMapper);
			selectAboutResultBean2(fooMapper);
		}
		
		public static void selectAboutResultBean0(FooMapper fooMapper){
			System.out.println(fooMapper.selectAboutResultBean0(1));
		}
		
		public static void selectAboutResultBean1(FooMapper fooMapper){
			System.out.println(fooMapper.selectAboutResultBean1(1));
		}
		
		public static void selectAboutResultBean2(FooMapper fooMapper){
			System.out.println(fooMapper.selectAboutResultBean2(1));
		}
	}

	private static class AboutResult {

		public static void test(FooMapper fooMapper){
			System.out.println("--------------------------------selectAboutResult-----------------------------------------");
			System.out.println("-----------selectAboutResult 0 - getResultByParam------------");
			selectAboutResult0(fooMapper);

			System.out.println("-----------selectAboutResult 1 - return Array------------");
			selectAboutResult1(fooMapper);

			System.out.println("-----------selectAboutResult 2 - return List------------");
			selectAboutResult2(fooMapper);

			System.out.println("-----------selectAboutResult 3 - return Map------------");
			selectAboutResult3(fooMapper);

			System.out.println("-----------selectAboutResult 4 - return Cursor------------");
			selectAboutResult4(fooMapper);

			System.out.println("-----------selectAboutResult 5 - return Bean------------");
			selectAboutResult5(fooMapper);
		}

		/**
		 * 透过参数获取返回值
		 * @param fooMapper
		 */
		public static void selectAboutResult0(FooMapper fooMapper){
			FooResultHandler result = new FooResultHandler();
			fooMapper.selectAboutResult0(1,"1","0",result);
			for (Object obj : result.getResultList()) {
				System.out.println(obj);
			}
		}

		/**
		 * 返回数组
		 * @param fooMapper
		 */
		public static void selectAboutResult1(FooMapper fooMapper){
			Foo[] fooArray = fooMapper.selectAboutResult1(1);
			for (Foo foo : fooArray) {
				System.out.println(foo);
			}
		}

		/**
		 * 返回 List
		 * @param fooMapper
		 */
		public static void selectAboutResult2(FooMapper fooMapper){
			List<Foo> fooList = fooMapper.selectAboutResult2(1);
			for (Iterator iterator = fooList.iterator(); iterator.hasNext();) {
				Foo foo = (Foo) iterator.next();
				System.out.println(foo);
			}
		}

		/**
		 * 返回 Map
		 * @param fooMapper
		 */
		public static void selectAboutResult3(FooMapper fooMapper){
			Map map = fooMapper.selectAboutResult3(1);
			System.out.println(map);
		}

		/**
		 * 返回 Cursor，不支持
		 * @param fooMapper
		 */
		public static void selectAboutResult4(FooMapper fooMapper){
//			Cursor cursor = fooMapper.selectAboutResult4(1);
//			for (Iterator iterator = cursor.iterator(); iterator.hasNext();) {
//				Foo foo = (Foo)iterator.next();
//				System.out.println(foo);
//			}
		}

		/**
		 * 其他类型
		 * @param fooMapper
		 */
		public static void selectAboutResult5(FooMapper fooMapper){
			Foo foo = fooMapper.selectAboutResult5(1);
			System.out.println(foo);
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
			selectAboutParam0(fooMapper);

			System.out.println("-----------selectAboutParam 1  - param UserDefine ------------");
			selectAboutParam1(fooMapper);

			System.out.println("-----------selectAboutParam 2  - param Unsafe ------------");
			selectAboutParam2(fooMapper);

			System.out.println("-----------selectAboutParam 3  - param Bean ------------");
			selectAboutParam3(fooMapper);

			System.out.println("-----------selectAboutParam 4  - param Bean And other ------------");
			selectAboutParam4(fooMapper);
			
			System.out.println("-----------selectAboutParam 5  - generic param name ------------");
			selectAboutParam5(fooMapper);
		}

		/**
		 * 翻页结果集 - RowBounds 是对结果集的筛选，不是决定数据库的返回结果集
		 * @param fooMapper
		 */
		public static void selectAboutParam0(FooMapper fooMapper){
			List<Foo> fooList = fooMapper.selectAboutParam0(1,new RowBounds(2,10));
			for (Iterator iterator = fooList.iterator(); iterator.hasNext();) {
				Foo foo = (Foo) iterator.next();
				System.out.println(foo);
			}
		}

		/**
		 * @param fooMapper
		 */
		public static void selectAboutParam1(FooMapper fooMapper){
			List<Foo> fooList = fooMapper.selectAboutParam1(1,0,10);
			for (Iterator iterator = fooList.iterator(); iterator.hasNext();) {
				Foo foo = (Foo) iterator.next();
				System.out.println(foo);
			}
		}

		/**
		 * 使用  ${} 是不安全的
		 * @param fooMapper
		 */
		public static void selectAboutParam2(FooMapper fooMapper){
			List<Foo> fooList = fooMapper.selectAboutParam2(1," OR 1=1");
			for (Iterator iterator = fooList.iterator(); iterator.hasNext();) {
				Foo foo = (Foo) iterator.next();
				System.out.println(foo);
			}
		}

		/**
		 * 参数是自定义对象
		 * @param fooMapper
		 */
		public static void selectAboutParam3(FooMapper fooMapper){
			Foo foo = new Foo();
			foo.setId(0);
			List<Foo> fooList = fooMapper.selectAboutParam3(foo);
			for (Iterator iterator = fooList.iterator(); iterator.hasNext();) {
				Foo fooTemp = (Foo) iterator.next();
				System.out.println(fooTemp);
			}
		}

		/**
		 * 参数是自定义对象
		 * @param fooMapper
		 */
		public static void selectAboutParam4(FooMapper fooMapper){
			Foo foo = new Foo();
			foo.setId(1);
			List<Foo> fooList = fooMapper.selectAboutParam4(foo,0);
			for (Iterator iterator = fooList.iterator(); iterator.hasNext();) {
				Foo fooTemp = (Foo) iterator.next();
				System.out.println(fooTemp);
			}
		}
		
		/**
		 * 参数是自定义对象
		 * @param fooMapper
		 */
		public static void selectAboutParam5(FooMapper fooMapper){
			Foo foo = new Foo();
			foo.setId(1);
			List<Foo> fooList = fooMapper.selectAboutParam5(foo,0);
			for (Iterator iterator = fooList.iterator(); iterator.hasNext();) {
				Foo fooTemp = (Foo) iterator.next();
				System.out.println(fooTemp);
			}
		}
	}


}
