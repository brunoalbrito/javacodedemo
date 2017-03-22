package cn.java.note.main;

import java.io.IOException;
import java.io.InputStream;
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
import org.apache.ibatis.type.JdbcType;

import cn.java.entity.User;
import cn.java.mapper.UserMapper;

public class Debug {

	public static void main(String[] args) throws IOException {
		JdbcType mJdbcType = JdbcType.valueOf("VARCHAR");
		System.out.println(mJdbcType.TYPE_CODE);
		//		test();
	}
	
	public static void test() throws IOException {
		/*
		 	Configuration.xml ---> Mapper.xml ---> “com.test.mapper.XxxMapper接口” 

		 	1.解析配置文件成对象 org.apache.ibatis.session.Configuration，Configuration内部有一些内容的类别名
		 	2.创建 org.apache.ibatis.session.defaults.DefaultSqlSession，内部依赖 org.apache.ibatis.session.Configuration 和 org.apache.ibatis.executor.SimpleExecutor

		 	Mapper的方案
		 		case 1.只有Mapper.xml文件 --->(configuration.getMapper()可以得到)
			 	case 2.只有“com.test.mapper.XxxMapper接口” --->(configuration.getMapper()可以得到)
			 	case 3.有Mapper.xml文件 + “com.test.mapper.XxxMapper接口” --->(configuration.getMapper()可以得到)
		 	Mapper的加载
			 	case 1.只配置Mapper.xml文件，先加载Mapper.xml文件，然后根据文件中声明的命名空间，尝试加载 “com.test.mapper.XxxMapper接口”，对接口进行反射获取相关信息
			 	case 2.只配置 “com.test.mapper.XxxMapper接口”，先尝试加载 "com/test/mapper/XxxMapper.xml 文件"，对dom进行解析获取相关信息，再尝试加载 “com.test.mapper.XxxMapper接口”，对接口进行反射获取相关信息
		 	org.apache.ibatis.builder.xml.XMLMapperEntityResolver
		 	
		 	
		 	MapperProxy.invoke(...) ---> MapperMethod.execute(sqlSession, args) --->
		 	
		 	DefaultResultSetHandler.handleResultSets(...) ---> DefaultResultSetHandler.getFirstResultSet() ---> mappedStatement.getResultMaps() 
		 		---> DefaultResultSetHandler.handleResultSet(... ,resultMap, ...) [ ---> mappedStatement.getResultSets() ---> DefaultResultSetHandler.handleResultSet(... ,resultMap, ...) ]
		 	
		 	DefaultResultSetHandler.handleCursorResultSets(...) --->  DefaultResultSetHandler.getFirstResultSet()
		 	
		 	
		 			
		 */
		InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream); // sqlSessionFactory == org.apache.ibatis.session.defaults.DefaultSqlSessionFactory
		SqlSession sqlSession = sqlSessionFactory.openSession(true); // sqlSession == org.apache.ibatis.session.defaults.DefaultSqlSession
		
		UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
		User curUser = userMapper.selectUserById2("10003");
		if (curUser != null) {
			System.out.println("HelloWorld:" + curUser.getId());
		}

	}

}
